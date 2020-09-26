/**
 * Copyright (c) 2020 QingLang, Inc. <baisui@qlangtech.com>
 *
 * This program is free software: you can use, redistribute, and/or modify
 * it under the terms of the GNU Affero General Public License, version 3
 * or later ("AGPL"), as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.apache.solr.update;

import com.codahale.metrics.Meter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;
import org.apache.solr.cloud.ZkController;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.core.SolrConfig.UpdateHandlerInfo;
import org.apache.solr.core.SolrCore;
import org.apache.solr.metrics.SolrMetricProducer;
import org.apache.solr.metrics.SolrMetricsContext;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.*;
import org.apache.solr.search.function.ValueSourceRangeFilter;
import org.apache.solr.util.RefCounted;
import org.apache.solr.util.TestInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;

/**
 * <code>DirectUpdateHandler2</code> implements an UpdateHandler where documents are added
 * directly to the main Lucene index as opposed to adding to a separate smaller index.
 *
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/09/25
 */
public class TisDirectUpdateHandler2 extends UpdateHandler implements SolrCoreState.IndexWriterCloser, SolrMetricProducer {

    private static final int NO_FILE_SIZE_UPPER_BOUND_PLACEHOLDER = -1;

    protected final SolrCoreState solrCoreState;

    // stats
    LongAdder addCommands = new LongAdder();

    Meter addCommandsCumulative;

    LongAdder deleteByIdCommands = new LongAdder();

    Meter deleteByIdCommandsCumulative;

    LongAdder deleteByQueryCommands = new LongAdder();

    Meter deleteByQueryCommandsCumulative;

    Meter expungeDeleteCommands;

    Meter mergeIndexesCommands;

    Meter commitCommands;

    Meter splitCommands;

    Meter optimizeCommands;

    Meter rollbackCommands;

    LongAdder numDocsPending = new LongAdder();

    LongAdder numErrors = new LongAdder();

    Meter numErrorsCumulative;

    SolrMetricsContext solrMetricsContext;

    // tracks when auto-commit should occur
    protected final CommitTracker commitTracker;

    protected final CommitTracker softCommitTracker;

    protected boolean commitWithinSoftCommit;

    /**
     * package access for testing
     *
     * @lucene.internal
     */
    void setCommitWithinSoftCommit(boolean value) {
        this.commitWithinSoftCommit = value;
    }

    protected boolean indexWriterCloseWaitsForMerges;

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public TisDirectUpdateHandler2(SolrCore core) {
        super(core, new TisUpdateLog());
        // 百岁修改 20150929
        PluginInfo ulogPluginInfo = core.getSolrConfig().getPluginInfo(UpdateLog.class.getName());
        this.ulog.init(ulogPluginInfo);
        this.ulog.init(this, core);
        // 百岁修改 20150929 end
        solrCoreState = core.getSolrCoreState();
        UpdateHandlerInfo updateHandlerInfo = core.getSolrConfig().getUpdateHandlerInfo();
        int docsUpperBound = updateHandlerInfo.autoCommmitMaxDocs;
        int timeUpperBound = updateHandlerInfo.autoCommmitMaxTime;
        long fileSizeUpperBound = updateHandlerInfo.autoCommitMaxSizeBytes;
        commitTracker = new CommitTracker("Hard", core, docsUpperBound, timeUpperBound, fileSizeUpperBound, updateHandlerInfo.openSearcher, false);
        int softCommitDocsUpperBound = updateHandlerInfo.autoSoftCommmitMaxDocs;
        int softCommitTimeUpperBound = updateHandlerInfo.autoSoftCommmitMaxTime;
        softCommitTracker = new CommitTracker("Soft", core, softCommitDocsUpperBound, softCommitTimeUpperBound, NO_FILE_SIZE_UPPER_BOUND_PLACEHOLDER, true, true);
        commitWithinSoftCommit = updateHandlerInfo.commitWithinSoftCommit;
        indexWriterCloseWaitsForMerges = updateHandlerInfo.indexWriterCloseWaitsForMerges;
        ZkController zkController = core.getCoreContainer().getZkController();
        if (zkController != null && core.getCoreDescriptor().getCloudDescriptor().getReplicaType() == Replica.Type.TLOG) {
            commitWithinSoftCommit = false;
            commitTracker.setOpenSearcher(true);
        }
    }

    public TisDirectUpdateHandler2(SolrCore core, UpdateHandler updateHandler) {
        super(core, updateHandler.getUpdateLog());
        solrCoreState = core.getSolrCoreState();
        UpdateHandlerInfo updateHandlerInfo = core.getSolrConfig().getUpdateHandlerInfo();
        int docsUpperBound = updateHandlerInfo.autoCommmitMaxDocs;
        int timeUpperBound = updateHandlerInfo.autoCommmitMaxTime;
        long fileSizeUpperBound = updateHandlerInfo.autoCommitMaxSizeBytes;
        commitTracker = new CommitTracker("Hard", core, docsUpperBound, timeUpperBound, fileSizeUpperBound, updateHandlerInfo.openSearcher, false);
        int softCommitDocsUpperBound = updateHandlerInfo.autoSoftCommmitMaxDocs;
        int softCommitTimeUpperBound = updateHandlerInfo.autoSoftCommmitMaxTime;
        softCommitTracker = new CommitTracker("Soft", core, softCommitDocsUpperBound, softCommitTimeUpperBound, NO_FILE_SIZE_UPPER_BOUND_PLACEHOLDER, updateHandlerInfo.openSearcher, true);
        commitWithinSoftCommit = updateHandlerInfo.commitWithinSoftCommit;
        indexWriterCloseWaitsForMerges = updateHandlerInfo.indexWriterCloseWaitsForMerges;
        UpdateLog existingLog = updateHandler.getUpdateLog();
        if (this.ulog != null && this.ulog == existingLog) {
            // If we are reusing the existing update log, inform the log that its update handler has changed.
            // We do this as late as possible.
            this.ulog.init(this, core);
        }
    }

    @Override
    public SolrMetricsContext getSolrMetricsContext() {
        return solrMetricsContext;
    }

    @Override
    public void initializeMetrics(SolrMetricsContext parentContext, String scope) {
        solrMetricsContext = parentContext.getChildContext(this);
        commitCommands = solrMetricsContext.meter(this, "commits", getCategory().toString(), scope);
        solrMetricsContext.gauge(this, () -> commitTracker.getCommitCount(), true, "autoCommits", getCategory().toString(), scope);
        solrMetricsContext.gauge(this, () -> softCommitTracker.getCommitCount(), true, "softAutoCommits", getCategory().toString(), scope);
        if (commitTracker.getDocsUpperBound() > 0) {
            solrMetricsContext.gauge(this, () -> commitTracker.getDocsUpperBound(), true, "autoCommitMaxDocs", getCategory().toString(), scope);
        }
        if (commitTracker.getTimeUpperBound() > 0) {
            solrMetricsContext.gauge(this, () -> "" + commitTracker.getTimeUpperBound() + "ms", true, "autoCommitMaxTime", getCategory().toString(), scope);
        }
        if (commitTracker.getTLogFileSizeUpperBound() > 0) {
            solrMetricsContext.gauge(this, () -> commitTracker.getTLogFileSizeUpperBound(), true, "autoCommitMaxSize", getCategory().toString(), scope);
        }
        if (softCommitTracker.getDocsUpperBound() > 0) {
            solrMetricsContext.gauge(this, () -> softCommitTracker.getDocsUpperBound(), true, "softAutoCommitMaxDocs", getCategory().toString(), scope);
        }
        if (softCommitTracker.getTimeUpperBound() > 0) {
            solrMetricsContext.gauge(this, () -> "" + softCommitTracker.getTimeUpperBound() + "ms", true, "softAutoCommitMaxTime", getCategory().toString(), scope);
        }
        optimizeCommands = solrMetricsContext.meter(this, "optimizes", getCategory().toString(), scope);
        rollbackCommands = solrMetricsContext.meter(this, "rollbacks", getCategory().toString(), scope);
        splitCommands = solrMetricsContext.meter(this, "splits", getCategory().toString(), scope);
        mergeIndexesCommands = solrMetricsContext.meter(this, "merges", getCategory().toString(), scope);
        expungeDeleteCommands = solrMetricsContext.meter(this, "expungeDeletes", getCategory().toString(), scope);
        solrMetricsContext.gauge(this, () -> numDocsPending.longValue(), true, "docsPending", getCategory().toString(), scope);
        solrMetricsContext.gauge(this, () -> addCommands.longValue(), true, "adds", getCategory().toString(), scope);
        solrMetricsContext.gauge(this, () -> deleteByIdCommands.longValue(), true, "deletesById", getCategory().toString(), scope);
        solrMetricsContext.gauge(this, () -> deleteByQueryCommands.longValue(), true, "deletesByQuery", getCategory().toString(), scope);
        solrMetricsContext.gauge(this, () -> numErrors.longValue(), true, "errors", getCategory().toString(), scope);
        addCommandsCumulative = solrMetricsContext.meter(this, "cumulativeAdds", getCategory().toString(), scope);
        deleteByIdCommandsCumulative = solrMetricsContext.meter(this, "cumulativeDeletesById", getCategory().toString(), scope);
        deleteByQueryCommandsCumulative = solrMetricsContext.meter(this, "cumulativeDeletesByQuery", getCategory().toString(), scope);
        numErrorsCumulative = solrMetricsContext.meter(this, "cumulativeErrors", getCategory().toString(), scope);
    }

    private void deleteAll() throws IOException {
        if (log.isInfoEnabled()) {
            log.info("{} REMOVING ALL DOCUMENTS FROM INDEX", core.getLogId());
        }
        RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
        try {
            iw.get().deleteAll();
        } finally {
            iw.decref();
        }
    }

    protected void rollbackWriter() throws IOException {
        numDocsPending.reset();
        solrCoreState.rollbackIndexWriter(core);
    }

    @Override
    public int addDoc(AddUpdateCommand cmd) throws IOException {
        TestInjection.injectDirectUpdateLatch();
        try {
            return addDoc0(cmd);
        } catch (SolrException e) {
            throw e;
        } catch (AlreadyClosedException e) {
            throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, String.format(Locale.ROOT, "Server error writing document id %s to the index", cmd.getPrintableId()), e);
        } catch (IllegalArgumentException iae) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, String.format(Locale.ROOT, "Exception writing document id %s to the index; possible analysis error: " + iae.getMessage() + (iae.getCause() instanceof BytesRefHash.MaxBytesLengthExceededException ? ". Perhaps the document has an indexed string field (solr.StrField) which is too large" : ""), cmd.getPrintableId()), iae);
        } catch (RuntimeException t) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, String.format(Locale.ROOT, "Exception writing document id %s to the index; possible analysis error.", cmd.getPrintableId()), t);
        }
    }

    /**
     * This is the implementation of {@link #addDoc(AddUpdateCommand)}. It is factored out to allow an exception
     * handler to decorate RuntimeExceptions with information about the document being handled.
     *
     * @param cmd the command.
     * @return the count.
     */
    private int addDoc0(AddUpdateCommand cmd) throws IOException {
        int rc = -1;
        addCommands.increment();
        addCommandsCumulative.mark();
        // if there is no ID field, don't overwrite
        if (idField == null) {
            cmd.overwrite = false;
        }
        try {
            if ((cmd.getFlags() & UpdateCommand.IGNORE_INDEXWRITER) != 0) {
                if (ulog != null)
                    ulog.add(cmd);
                return 1;
            }
            if (cmd.overwrite) {
                // Check for delete by query commands newer (i.e. reordered). This
                // should always be null on a leader
                List<UpdateLog.DBQ> deletesAfter = null;
                if (ulog != null && cmd.version > 0) {
                    deletesAfter = ulog.getDBQNewer(cmd.version);
                }
                if (deletesAfter != null) {
                    addAndDelete(cmd, deletesAfter);
                } else {
                    doNormalUpdate(cmd);
                }
            } else {
                allowDuplicateUpdate(cmd);
            }
            if ((cmd.getFlags() & UpdateCommand.IGNORE_AUTOCOMMIT) == 0) {
                long currentTlogSize = getCurrentTLogSize();
                if (commitWithinSoftCommit) {
                    commitTracker.addedDocument(-1, currentTlogSize);
                    softCommitTracker.addedDocument(cmd.commitWithin);
                } else {
                    softCommitTracker.addedDocument(-1);
                    commitTracker.addedDocument(cmd.commitWithin, currentTlogSize);
                }
            }
            rc = 1;
        } finally {
            if (rc != 1) {
                numErrors.increment();
                numErrorsCumulative.mark();
            } else {
                numDocsPending.increment();
            }
        }
        return rc;
    }

    private void allowDuplicateUpdate(AddUpdateCommand cmd) throws IOException {
        RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
        try {
            IndexWriter writer = iw.get();
            Iterable<Document> nestedDocs = cmd.getLuceneDocsIfNested();
            if (nestedDocs != null) {
                writer.addDocuments(nestedDocs);
            } else {
                writer.addDocument(cmd.getLuceneDocument());
            }
            if (ulog != null)
                ulog.add(cmd);
        } finally {
            iw.decref();
        }
    }

    private void doNormalUpdate(AddUpdateCommand cmd) throws IOException {
        RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
        try {
            IndexWriter writer = iw.get();
            updateDocOrDocValues(cmd, writer);
            // log version was definitely committed.
            if (ulog != null)
                ulog.add(cmd);
        } finally {
            iw.decref();
        }
    }

    private void addAndDelete(AddUpdateCommand cmd, List<UpdateLog.DBQ> deletesAfter) throws IOException {
        // this logic is different enough from doNormalUpdate that it's separate
        log.info("Reordered DBQs detected.  Update={} DBQs={}", cmd, deletesAfter);
        List<Query> dbqList = new ArrayList<>(deletesAfter.size());
        for (UpdateLog.DBQ dbq : deletesAfter) {
            try {
                DeleteUpdateCommand tmpDel = new DeleteUpdateCommand(cmd.req);
                tmpDel.query = dbq.q;
                tmpDel.version = -dbq.version;
                dbqList.add(getQuery(tmpDel));
            } catch (Exception e) {
                log.error("Exception parsing reordered query : {}", dbq, e);
            }
        }
        RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
        try {
            IndexWriter writer = iw.get();
            // see comment in deleteByQuery
            synchronized (solrCoreState.getUpdateLock()) {
                updateDocOrDocValues(cmd, writer);
                if (cmd.isInPlaceUpdate() && ulog != null) {
                    // This is needed due to LUCENE-7344.
                    ulog.openRealtimeSearcher();
                }
                for (Query q : dbqList) {
                    writer.deleteDocuments(new DeleteByQueryWrapper(q, core.getLatestSchema()));
                }
                // this needs to be protected by update lock
                if (ulog != null)
                    ulog.add(cmd, true);
            }
        } finally {
            iw.decref();
        }
    }

    private void updateDeleteTrackers(DeleteUpdateCommand cmd) {
        if ((cmd.getFlags() & UpdateCommand.IGNORE_AUTOCOMMIT) == 0) {
            if (commitWithinSoftCommit) {
                softCommitTracker.deletedDocument(cmd.commitWithin);
            } else {
                commitTracker.deletedDocument(cmd.commitWithin);
            }
            if (commitTracker.getTimeUpperBound() > 0) {
                commitTracker.scheduleCommitWithin(commitTracker.getTimeUpperBound());
            }
            long currentTlogSize = getCurrentTLogSize();
            commitTracker.scheduleMaxSizeTriggeredCommitIfNeeded(currentTlogSize);
            if (softCommitTracker.getTimeUpperBound() > 0) {
                softCommitTracker.scheduleCommitWithin(softCommitTracker.getTimeUpperBound());
            }
        }
    }

    // we don't return the number of docs deleted because it's not always possible to quickly know that info.
    @Override
    public void delete(DeleteUpdateCommand cmd) throws IOException {
        TestInjection.injectDirectUpdateLatch();
        deleteByIdCommands.increment();
        deleteByIdCommandsCumulative.mark();
        if ((cmd.getFlags() & UpdateCommand.IGNORE_INDEXWRITER) != 0) {
            if (ulog != null)
                ulog.delete(cmd);
            return;
        }
        Term deleteTerm = getIdTerm(cmd.getIndexedId(), false);
        // SolrCore.verbose("deleteDocuments",deleteTerm,writer);
        RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
        try {
            iw.get().deleteDocuments(deleteTerm);
        } finally {
            iw.decref();
        }
        if (ulog != null)
            ulog.delete(cmd);
        updateDeleteTrackers(cmd);
    }

    public void clearIndex() throws IOException {
        deleteAll();
        if (ulog != null) {
            ulog.deleteAll();
        }
    }

    private Query getQuery(DeleteUpdateCommand cmd) {
        Query q;
        try {
            // move this higher in the stack?
            QParser parser = QParser.getParser(cmd.getQuery(), cmd.req);
            q = parser.getQuery();
            q = QueryUtils.makeQueryable(q);
            // Make sure not to delete newer versions
            if (ulog != null && cmd.getVersion() != 0 && cmd.getVersion() != -Long.MAX_VALUE) {
                BooleanQuery.Builder bq = new BooleanQuery.Builder();
                bq.add(q, Occur.MUST);
                SchemaField sf = ulog.getVersionInfo().getVersionField();
                ValueSource vs = sf.getType().getValueSource(sf, null);
                ValueSourceRangeFilter filt = new ValueSourceRangeFilter(vs, Long.toString(Math.abs(cmd.getVersion())), null, true, true);
                FunctionRangeQuery range = new FunctionRangeQuery(filt);
                // formulated in the "MUST_NOT" sense so we can delete docs w/o a version (some tests depend on this...)
                bq.add(range, Occur.MUST_NOT);
                q = bq.build();
            }
            return q;
        } catch (SyntaxError e) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
        }
    }

    // we don't return the number of docs deleted because it's not always possible to quickly know that info.
    @Override
    public void deleteByQuery(DeleteUpdateCommand cmd) throws IOException {
        TestInjection.injectDirectUpdateLatch();
        deleteByQueryCommands.increment();
        deleteByQueryCommandsCumulative.mark();
        boolean madeIt = false;
        try {
            if ((cmd.getFlags() & UpdateCommand.IGNORE_INDEXWRITER) != 0) {
                if (ulog != null)
                    ulog.deleteByQuery(cmd);
                madeIt = true;
                return;
            }
            Query q = getQuery(cmd);
            boolean delAll = MatchAllDocsQuery.class == q.getClass();
            // currently for testing purposes.  Do a delete of complete index w/o worrying about versions, don't log, clean up most state in update log, etc
            if (delAll && cmd.getVersion() == -Long.MAX_VALUE) {
                synchronized (solrCoreState.getUpdateLock()) {
                    deleteAll();
                    ulog.deleteAll();
                    return;
                }
            }
            // 
            synchronized (solrCoreState.getUpdateLock()) {
                // Once LUCENE-7344 is resolved, we can consider removing this.
                if (ulog != null)
                    ulog.openRealtimeSearcher();
                if (delAll) {
                    deleteAll();
                } else {
                    RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
                    try {
                        iw.get().deleteDocuments(new DeleteByQueryWrapper(q, core.getLatestSchema()));
                    } finally {
                        iw.decref();
                    }
                }
                // this needs to be protected by the update lock
                if (ulog != null)
                    ulog.deleteByQuery(cmd);
            }
            madeIt = true;
            updateDeleteTrackers(cmd);
        } finally {
            if (!madeIt) {
                numErrors.increment();
                numErrorsCumulative.mark();
            }
        }
    }

    @Override
    public int mergeIndexes(MergeIndexesCommand cmd) throws IOException {
        TestInjection.injectDirectUpdateLatch();
        mergeIndexesCommands.mark();
        int rc;
        log.info("start {}", cmd);
        List<DirectoryReader> readers = cmd.readers;
        if (readers != null && readers.size() > 0) {
            List<CodecReader> mergeReaders = new ArrayList<>();
            for (DirectoryReader reader : readers) {
                for (LeafReaderContext leaf : reader.leaves()) {
                    mergeReaders.add(SlowCodecReaderWrapper.wrap(leaf.reader()));
                }
            }
            RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
            try {
                iw.get().addIndexes(mergeReaders.toArray(new CodecReader[mergeReaders.size()]));
            } finally {
                iw.decref();
            }
            rc = 1;
        } else {
            rc = 0;
        }
        log.info("end_mergeIndexes");
        // TODO: consider soft commit issues
        if (rc == 1 && commitTracker.getTimeUpperBound() > 0) {
            commitTracker.scheduleCommitWithin(commitTracker.getTimeUpperBound());
        } else if (rc == 1 && softCommitTracker.getTimeUpperBound() > 0) {
            softCommitTracker.scheduleCommitWithin(softCommitTracker.getTimeUpperBound());
        }
        return rc;
    }

    public void prepareCommit(CommitUpdateCommand cmd) throws IOException {
        boolean error = true;
        try {
            log.debug("start {}", cmd);
            RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
            try {
                SolrIndexWriter.setCommitData(iw.get(), cmd.getVersion());
                iw.get().prepareCommit();
            } finally {
                iw.decref();
            }
            log.debug("end_prepareCommit");
            error = false;
        } finally {
            if (error) {
                numErrors.increment();
                numErrorsCumulative.mark();
            }
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    public void commit(CommitUpdateCommand cmd) throws IOException {
        TestInjection.injectDirectUpdateLatch();
        if (cmd.prepareCommit) {
            prepareCommit(cmd);
            return;
        }
        if (cmd.optimize) {
            optimizeCommands.mark();
        } else {
            commitCommands.mark();
            if (cmd.expungeDeletes)
                expungeDeleteCommands.mark();
        }
        Future[] waitSearcher = null;
        if (cmd.waitSearcher) {
            waitSearcher = new Future[1];
        }
        boolean error = true;
        try {
            // only allow one hard commit to proceed at once
            if (!cmd.softCommit) {
                solrCoreState.getCommitLock().lock();
            }
            log.debug("start {}", cmd);
            if (cmd.openSearcher) {
                // we can cancel any pending soft commits if this commit will open a new searcher
                softCommitTracker.cancelPendingCommit();
            }
            if (!cmd.softCommit && (cmd.openSearcher || !commitTracker.getOpenSearcher())) {
                // cancel a pending hard commit if this commit is of equal or greater "strength"...
                // If the autoCommit has openSearcher=true, then this commit must have openSearcher=true
                // to cancel.
                commitTracker.cancelPendingCommit();
            }
            RefCounted<IndexWriter> iw = solrCoreState.getIndexWriter(core);
            try {
                IndexWriter writer = iw.get();
                if (cmd.optimize) {
                    writer.forceMerge(cmd.maxOptimizeSegments);
                } else if (cmd.expungeDeletes) {
                    writer.forceMergeDeletes();
                }
                if (!cmd.softCommit) {
                    synchronized (solrCoreState.getUpdateLock()) {
                        // postSoft... see postSoft comments.
                        if (ulog != null)
                            ulog.preCommit(cmd);
                    }
                    if (writer.hasUncommittedChanges()) {
                        SolrIndexWriter.setCommitData(writer, cmd.getVersion());
                        writer.commit();
                    } else {
                        log.debug("No uncommitted changes. Skipping IW.commit.");
                    }
                    // SolrCore.verbose("writer.commit() end");
                    numDocsPending.reset();
                    callPostCommitCallbacks();
                }
            } finally {
                iw.decref();
            }
            if (cmd.optimize) {
                callPostOptimizeCallbacks();
            }
            if (cmd.softCommit) {
                // ulog.preSoftCommit();
                synchronized (solrCoreState.getUpdateLock()) {
                    if (ulog != null)
                        ulog.preSoftCommit(cmd);
                    core.getSearcher(true, false, waitSearcher, true);
                    if (ulog != null)
                        ulog.postSoftCommit(cmd);
                }
                callPostSoftCommitCallbacks();
            } else {
                synchronized (solrCoreState.getUpdateLock()) {
                    if (ulog != null)
                        ulog.preSoftCommit(cmd);
                    if (cmd.openSearcher) {
                        core.getSearcher(true, false, waitSearcher);
                    } else {
                        // force open a new realtime searcher so realtime-get and versioning code can see the latest
                        RefCounted<SolrIndexSearcher> searchHolder = core.openNewSearcher(true, true);
                        searchHolder.decref();
                    }
                    if (ulog != null)
                        ulog.postSoftCommit(cmd);
                }
                // postCommit currently means new searcher has
                if (ulog != null)
                    ulog.postCommit(cmd);
            // also been opened
            }
            if (cmd.softCommit) {
                softCommitTracker.didCommit();
            } else {
                commitTracker.didCommit();
            }
            log.debug("end_commit_flush");
            error = false;
        } finally {
            if (!cmd.softCommit) {
                solrCoreState.getCommitLock().unlock();
            }
            addCommands.reset();
            deleteByIdCommands.reset();
            deleteByQueryCommands.reset();
            if (error) {
                numErrors.increment();
                numErrorsCumulative.mark();
            }
        }
        // outside any synchronized block so that other update operations can proceed.
        if (waitSearcher != null && waitSearcher[0] != null) {
            try {
                waitSearcher[0].get();
            } catch (InterruptedException | ExecutionException e) {
                SolrException.log(log, e);
            }
        }
    }

    @Override
    public void newIndexWriter(boolean rollback) throws IOException {
        solrCoreState.newIndexWriter(core, rollback);
    }

    /**
     * @since Solr 1.4
     */
    @Override
    public void rollback(RollbackUpdateCommand cmd) throws IOException {
        TestInjection.injectDirectUpdateLatch();
        if (core.getCoreContainer().isZooKeeperAware()) {
            throw new UnsupportedOperationException("Rollback is currently not supported in SolrCloud mode. (SOLR-4895)");
        }
        rollbackCommands.mark();
        boolean error = true;
        try {
            log.info("start {}", cmd);
            rollbackWriter();
            // callPostRollbackCallbacks();
            // reset commit tracking
            commitTracker.didRollback();
            softCommitTracker.didRollback();
            log.info("end_rollback");
            error = false;
        } finally {
            addCommandsCumulative.mark(-addCommands.sumThenReset());
            deleteByIdCommandsCumulative.mark(-deleteByIdCommands.sumThenReset());
            deleteByQueryCommandsCumulative.mark(-deleteByQueryCommands.sumThenReset());
            if (error) {
                numErrors.increment();
                numErrorsCumulative.mark();
            }
        }
    }

    @Override
    public UpdateLog getUpdateLog() {
        return ulog;
    }

    @Override
    public void close() throws IOException {
        log.debug("closing {}", this);
        commitTracker.close();
        softCommitTracker.close();
        numDocsPending.reset();
        try {
            SolrMetricProducer.super.close();
        } catch (Exception e) {
            throw new IOException("Error closing", e);
        }
    }

    // IndexWriterCloser interface method - called from solrCoreState.decref(this)
    @Override
    public void closeWriter(IndexWriter writer) throws IOException {
        log.trace("closeWriter({}): ulog={}", writer, ulog);
        assert TestInjection.injectNonGracefullClose(core.getCoreContainer());
        boolean clearRequestInfo = false;
        SolrQueryRequest req = new LocalSolrQueryRequest(core, new ModifiableSolrParams());
        SolrQueryResponse rsp = new SolrQueryResponse();
        if (SolrRequestInfo.getRequestInfo() == null) {
            clearRequestInfo = true;
            // important for debugging
            SolrRequestInfo.setRequestInfo(new SolrRequestInfo(req, rsp));
        }
        try {
            if (TestInjection.injectSkipIndexWriterCommitOnClose(writer)) {
                // if this TestInjection triggers, we do some simple rollback()
                // (which closes the underlying IndexWriter) and then return immediately
                log.warn("Skipping commit for IndexWriter.close() due to TestInjection");
                if (writer != null) {
                    writer.rollback();
                }
                // means we can't delete them on windows (needed for tests)
                if (ulog != null)
                    ulog.close(false);
                return;
            }
            // do a commit before we quit?
            boolean tryToCommit = writer != null && ulog != null && ulog.hasUncommittedChanges() && ulog.getState() == UpdateLog.State.ACTIVE;
            // be tactical with this lock! closing the updatelog can deadlock when it tries to commit
            solrCoreState.getCommitLock().lock();
            try {
                try {
                    if (log.isInfoEnabled()) {
                        log.info("Committing on IndexWriter.close() {}.", (tryToCommit ? "" : " ... SKIPPED (unnecessary)"));
                    }
                    if (tryToCommit) {
                        CommitUpdateCommand cmd = new CommitUpdateCommand(req, false);
                        cmd.openSearcher = false;
                        cmd.waitSearcher = false;
                        cmd.softCommit = false;
                        synchronized (solrCoreState.getUpdateLock()) {
                            ulog.preCommit(cmd);
                        }
                        // todo: refactor this shared code (or figure out why a real CommitUpdateCommand can't be used)
                        SolrIndexWriter.setCommitData(writer, cmd.getVersion());
                        writer.commit();
                        synchronized (solrCoreState.getUpdateLock()) {
                            ulog.postCommit(cmd);
                        }
                    }
                } catch (Throwable th) {
                    log.error("Error in final commit", th);
                    if (th instanceof OutOfMemoryError) {
                        throw (OutOfMemoryError) th;
                    }
                }
            } finally {
                solrCoreState.getCommitLock().unlock();
            }
        } finally {
            if (clearRequestInfo)
                SolrRequestInfo.clearRequestInfo();
        }
        // cap any ulog files.
        try {
            if (ulog != null)
                ulog.close(false);
        } catch (Throwable th) {
            log.error("Error closing log files", th);
            if (th instanceof OutOfMemoryError) {
                throw (OutOfMemoryError) th;
            }
        }
        if (writer != null) {
            writer.close();
        }
    }

    @Override
    public void split(SplitIndexCommand cmd) throws IOException {
        commit(new CommitUpdateCommand(cmd.req, false));
        SolrIndexSplitter splitter = new SolrIndexSplitter(cmd);
        splitCommands.mark();
        NamedList<Object> results = new NamedList<>();
        try {
            splitter.split(results);
            cmd.rsp.addResponse(results);
        } catch (IOException e) {
            numErrors.increment();
            numErrorsCumulative.mark();
            throw e;
        }
    }

    /**
     * Calls either {@link IndexWriter#updateDocValues} or <code>IndexWriter#updateDocument</code>(s) as
     * needed based on {@link AddUpdateCommand#isInPlaceUpdate}.
     * <p>
     * If the this is an UPDATE_INPLACE cmd, then all fields included in
     * {@link AddUpdateCommand#getLuceneDocument} must either be the uniqueKey field, or be DocValue
     * only fields.
     * </p>
     *
     * @param cmd    - cmd apply to IndexWriter
     * @param writer - IndexWriter to use
     */
    private void updateDocOrDocValues(AddUpdateCommand cmd, IndexWriter writer) throws IOException {
        // this code path requires an idField in order to potentially replace a doc
        assert idField != null;
        // AKA dedupe
        boolean hasUpdateTerm = cmd.updateTerm != null;
        if (cmd.isInPlaceUpdate()) {
            if (hasUpdateTerm) {
                throw new IllegalStateException("cmd.updateTerm/dedupe is not compatible with in-place updates");
            }
            // we don't support the solrInputDoc with nested child docs either but we'll throw an exception if attempted
            Term updateTerm = new Term(idField.getName(), cmd.getIndexedId());
            Document luceneDocument = cmd.getLuceneDocument();
            final List<IndexableField> origDocFields = luceneDocument.getFields();
            final List<Field> fieldsToUpdate = new ArrayList<>(origDocFields.size());
            for (IndexableField field : origDocFields) {
                if (!field.name().equals(updateTerm.field())) {
                    fieldsToUpdate.add((Field) field);
                }
            }
            log.debug("updateDocValues({})", cmd);
            writer.updateDocValues(updateTerm, fieldsToUpdate.toArray(new Field[fieldsToUpdate.size()]));
        } else {
            // more normal path
            Iterable<Document> nestedDocs = cmd.getLuceneDocsIfNested();
            // AKA nested child docs
            boolean isNested = nestedDocs != null;
            Term idTerm = getIdTerm(isNested ? new BytesRef(cmd.getRootIdUsingRouteParam()) : cmd.getIndexedId(), isNested);
            Term updateTerm = hasUpdateTerm ? cmd.updateTerm : idTerm;
            if (isNested) {
                log.debug("updateDocuments({})", cmd);
                writer.updateDocuments(updateTerm, nestedDocs);
            } else {
                Document luceneDocument = cmd.getLuceneDocument();
                log.debug("updateDocument({})", cmd);
                writer.updateDocument(updateTerm, luceneDocument);
            }
            // (used in near-duplicate replacement)
            if (hasUpdateTerm) {
                // rare
                BooleanQuery.Builder bq = new BooleanQuery.Builder();
                // don't want the one we added above (will be unique)
                bq.add(new TermQuery(updateTerm), Occur.MUST_NOT);
                // same ID
                bq.add(new TermQuery(idTerm), Occur.MUST);
                writer.deleteDocuments(new DeleteByQueryWrapper(bq.build(), core.getLatestSchema()));
            }
        }
    }

    private Term getIdTerm(BytesRef termVal, boolean isNested) {
        boolean useRootId = isNested || core.getLatestSchema().isUsableForChildDocs();
        return new Term(useRootId ? IndexSchema.ROOT_FIELD_NAME : idField.getName(), termVal);
    }

    // ///////////////////////////////////////////////////////////////////
    // SolrInfoBean stuff: Statistics and Module Info
    // ///////////////////////////////////////////////////////////////////
    @Override
    public String getName() {
        return DirectUpdateHandler2.class.getName();
    }

    @Override
    public String getDescription() {
        return "Update handler that efficiently directly updates the on-disk main lucene index";
    }

    @Override
    public SolrCoreState getSolrCoreState() {
        return solrCoreState;
    }

    private long getCurrentTLogSize() {
        return ulog != null && ulog.hasUncommittedChanges() ? ulog.getCurrentLogSizeFromStream() : -1;
    }

    // allow access for tests
    public CommitTracker getCommitTracker() {
        return commitTracker;
    }

    // allow access for tests
    public CommitTracker getSoftCommitTracker() {
        return softCommitTracker;
    }
}
