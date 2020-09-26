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
package com.qlangtech.tis.indexbuilder.index;

import com.qlangtech.tis.build.metrics.Counters;
import com.qlangtech.tis.build.metrics.Messages;
import com.qlangtech.tis.indexbuilder.HdfsIndexBuilder;
import com.qlangtech.tis.indexbuilder.doc.SolrDocPack;
import com.qlangtech.tis.indexbuilder.map.IndexConf;
import com.qlangtech.tis.indexbuilder.map.SuccessFlag;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.NoMergePolicy;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.store.RAMDirectory;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.update.DocumentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年1月17日
 */
public class IndexMaker implements Runnable {

    public static final Logger logger = LoggerFactory.getLogger(IndexMaker.class);

    private static final AtomicInteger failureCount = new AtomicInteger();

    protected final IndexSchema indexSchema;

    private final AtomicInteger aliveDocMakerCount;

    private IndexConf indexConf;

    private final BlockingQueue<SolrDocPack> docPoolQueues;

    // 这个maker的产出物
    private BlockingQueue<RAMDirectory> ramDirQueue;

    private final SuccessFlag successFlag;

    private Counters counters;

    private Messages messages;

    public int docMakeCount;

    public int preDocMakeCount;

    // 所以提交时间总和
    public long allConsumeTimemillis;

    /**
     * 取得结果运行标记
     *
     * @return
     */
    public SuccessFlag getResultFlag() {
        return this.successFlag;
    }

    // 存活的文档生成器
    private final AtomicInteger aliveIndexMakerCount;

    public IndexMaker(// 这个是下游的产出结果
    String name, // 这个是下游的产出结果
    IndexConf indexConf, // 这个是下游的产出结果
    IndexSchema indexSchema, // 这个是下游的产出结果
    Messages messages, // 这个是下游的产出结果
    Counters counters, // 这个是上游管道
    BlockingQueue<RAMDirectory> ramDirQueue, // ,
    BlockingQueue<SolrDocPack> docPoolQueues, // ,
    AtomicInteger aliveDocMakerCount, AtomicInteger aliveIndexMakerCount) // makerAllocator
    {
        this.successFlag = new SuccessFlag(name);
        this.counters = counters;
        this.messages = messages;
        this.aliveDocMakerCount = aliveDocMakerCount;
        this.aliveIndexMakerCount = aliveIndexMakerCount;
        this.indexConf = indexConf;
        if (indexSchema == null) {
            throw new IllegalArgumentException("indexSchema can not be null");
        }
        this.indexSchema = indexSchema;
        this.docPoolQueues = docPoolQueues;
        this.ramDirQueue = ramDirQueue;
    }

    // private IndexWriter getRAMIndexWriter(IndexConf indexConf, IndexSchema
    // schema, IndexWriter oldWriter,
    // boolean create) throws Exception {
    // if (create) {
    // return createRAMIndexWriter(indexConf, schema, false/* mrege */);
    // } else {
    // if ((this.docMakeCount % 5000 == 0) && needFlush(oldWriter)) {
    // synchronized (oldWriter) {
    // if ((this.docMakeCount % 5000 == 0) && needFlush(oldWriter)) {
    // addRAMToMergeQueue(oldWriter);
    // return createRAMIndexWriter(this.indexConf, this.indexSchema, false/*
    // mrege */);
    // } else {
    // return oldWriter;
    // }
    // }
    // } else {
    // return oldWriter;
    // }
    // }
    // }
    public static IndexWriter createRAMIndexWriter(IndexConf indexConf, IndexSchema schema, boolean merge) throws IOException {
        RAMDirectory ramDirectory = new RAMDirectory();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(schema.getIndexAnalyzer());
        indexWriterConfig.setMaxBufferedDocs(Integer.MAX_VALUE);
        indexWriterConfig.setRAMBufferSizeMB(IndexWriterConfig.DISABLE_AUTO_FLUSH);
        // indexWriterConfig.setTermIndexInterval(Integer.MAX_VALUE);
        if (merge) {
            TieredMergePolicy mergePolicy = new TieredMergePolicy();
            mergePolicy.setNoCFSRatio(1.0);
            // mergePolicy.setUseCompoundFile(indexConf.getMakerUseCompoundFile());
            mergePolicy.setSegmentsPerTier(10);
            mergePolicy.setMaxMergeAtOnceExplicit(50);
            mergePolicy.setMaxMergeAtOnce(50);
            indexWriterConfig.setMergePolicy(mergePolicy);
        } else {
            indexWriterConfig.setUseCompoundFile(false);
            indexWriterConfig.setMergePolicy(NoMergePolicy.INSTANCE);
        }
        indexWriterConfig.setOpenMode(OpenMode.CREATE);
        IndexWriter addWriter = new IndexWriter(ramDirectory, indexWriterConfig);
        // 必须commit一下才会产生segment*文件，如果不commit，indexReader读会报错。
        addWriter.commit();
        return addWriter;
    }

    private boolean needFlush(IndexWriter writer) {
        int maxDoc = writer.getDocStats().numDocs;
        long ramSize = ((RAMDirectory) writer.getDirectory()).ramBytesUsed();
        boolean overNum = maxDoc >= this.indexConf.getFlushCountThreshold();
        boolean overSize = ramSize >= this.indexConf.getFlushSizeThreshold();
        if (overNum || overSize) {
            logger.warn("ram has reach the threshold ，while Flush to disk,has Doc count{" + maxDoc + "},ram consume: {" + ramSize / (1024 * 1024) + " M}");
            return true;
        }
        return false;
    }

    private // Directory ramdir
    void addRAMToMergeQueue(IndexWriter indexWriter) throws Exception {
        RAMDirectory ramDirectory = (RAMDirectory) indexWriter.getDirectory();
        try {
            indexWriter.commit();
            indexWriter.close();
            logger.warn("ramIndexQueueSize=" + ramDirQueue.size());
            // logger.warn("ramIndexQueueSize="+ramIndexQueue.size());
            ramDirQueue.put(ramDirectory);
        } catch (OutOfMemoryError e) {
            throw new RuntimeException("RAM has overhead:" + (ramDirectory.ramBytesUsed() / (1024 * 1024)) + "M,FlushCountThreshold:" + this.indexConf.getFlushCountThreshold() + ",FlushSizeThreshold:" + this.indexConf.getFlushSizeThreshold(), e);
        }
    }

    public void run() {
        try {
            HdfsIndexBuilder.setMdcAppName(this.indexConf.getCollectionName());
            doRun();
        } catch (Throwable e) {
            logger.error("maker error" + e.toString(), e);
            messages.addMessage(Messages.Message.ERROR_MSG, "index maker fatal error:" + e.toString());
            successFlag.setMsg(SuccessFlag.Flag.FAILURE, "index maker fatal error:" + e.toString());
        } finally {
            aliveIndexMakerCount.decrementAndGet();
        }
    }

    public void doRun() throws IOException, InterruptedException {
        int[] failureCount = new int[1];
        IndexWriter indexWriter = createRAMIndexWriter(this.indexConf, this.indexSchema, false);
        SolrDocPack docPack = null;
        while (true) {
            /*
             * if (interruptFlag.flag == InterruptFlag.Flag.PAUSE) {
             * synchronized (interruptFlag) { interruptFlag.wait(); } }
             */
            docPack = docPoolQueues.poll(2, TimeUnit.SECONDS);
            try {
                if (docPack == null) {
                    if (this.aliveDocMakerCount.get() > 0) {
                        Thread.sleep(1000);
                        continue;
                    }
                    addRAMToMergeQueue(indexWriter);
                    // successFlag.setFlag(SuccessFlag.Flag.SUCCESS);
                    successFlag.setMsg(SuccessFlag.Flag.SUCCESS, "LuceneDocMaker success");
                    return;
                }
            } catch (Exception e1) {
                logger.error("IndexMaker+" + this.successFlag.getName(), e1);
                counters.incrCounter(Counters.Counter.INDEXMAKE_FAIL, 1);
                messages.addMessage(Messages.Message.ERROR_MSG, "index maker index error:" + e1.toString());
            }
            try {
                if (!appendDocument(indexWriter, docPack, failureCount)) {
                    // 有错误中断了
                    return;
                }
                if ((this.docMakeCount > (this.preDocMakeCount + 5000)) && needFlush(indexWriter)) {
                    addRAMToMergeQueue(indexWriter);
                    indexWriter = createRAMIndexWriter(this.indexConf, this.indexSchema, false);
                    preDocMakeCount = this.docMakeCount;
                }
            } catch (Exception e) {
                logger.error("IndexMaker+" + successFlag.getName(), e);
                counters.incrCounter(Counters.Counter.INDEXMAKE_FAIL, 1);
                messages.addMessage(Messages.Message.ERROR_MSG, "index maker index error:" + e.toString());
                // index error, solrDoc:" + docPack);
                if (failureCount[0]++ > indexConf.getMaxFailCount()) {
                    // successFlag.setFlag(SuccessFlag.Flag.FAILURE);
                    successFlag.setMsg(SuccessFlag.Flag.FAILURE, "LuceneDocMaker error:failureCount>" + indexConf.getMaxFailCount());
                    return;
                }
            }
        }
    }

    private final boolean appendDocument(final IndexWriter indexWriter, SolrDocPack docPack, int[] failureCount) throws Exception {
        long current = System.currentTimeMillis();
        try {
            SolrInputDocument inputDoc = null;
            for (int i = 0; i <= docPack.getCurrentIndex(); i++) {
                inputDoc = docPack.getDoc(i);
                try {
                    writeSolrInputDocument(indexWriter, inputDoc);
                    this.docMakeCount++;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    messages.addMessage(Messages.Message.ERROR_MSG, "index maker index error, solrDoc:" + inputDoc);
                    if (++failureCount[0] > indexConf.getMaxFailCount()) {
                        // successFlag.setFlag(SuccessFlag.Flag.FAILURE);
                        successFlag.setMsg(SuccessFlag.Flag.FAILURE, "LuceneDocMaker error:failureCount>" + indexConf.getMaxFailCount());
                        return false;
                    }
                }
            }
        } finally {
            this.allConsumeTimemillis += (System.currentTimeMillis() - current);
        }
        return true;
    }

    protected void writeSolrInputDocument(IndexWriter indexWriter, SolrInputDocument inputDoc) throws IOException {
        indexWriter.addDocument(DocumentBuilder.toDocument(inputDoc, this.indexSchema));
    }
}
