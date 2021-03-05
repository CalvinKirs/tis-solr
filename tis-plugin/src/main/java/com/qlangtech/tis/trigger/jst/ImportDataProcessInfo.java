/**
 * Copyright (c) 2020 QingLang, Inc. <baisui@qlangtech.com>
 * <p>
 * This program is free software: you can use, redistribute, and/or modify
 * it under the terms of the GNU Affero General Public License, version 3
 * or later ("AGPL"), as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.qlangtech.tis.trigger.jst;

import com.qlangtech.tis.cloud.ITISCoordinator;
import com.qlangtech.tis.fs.ITISFileSystem;
import com.qlangtech.tis.fullbuild.indexbuild.IndexBuildSourcePathCreator;
import com.qlangtech.tis.fullbuild.indexbuild.IIndexBuildParam;
import com.qlangtech.tis.fullbuild.indexbuild.LuceneVersion;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 共享区处理完成信息之后需要向弹内发送直接结果，以执行后续流程
 *
 * @author 百岁（baisui@qlangtech.com）
 * @date 2014年9月11日下午5:52:50
 */
public class ImportDataProcessInfo implements Serializable, Cloneable, IIndexBuildParam {

    public static final String KEY_DELIMITER = "split_char";

    public static final String DELIMITER_001 = "char001";

    public static final String DELIMITER_TAB = "tab";

    private static final long serialVersionUID = 1L;

    private final Integer taskId;

    private String indexName;

    private String indexBuilder;

    // 编译索引使用的Lucene版本
    private LuceneVersion luceneVersion;

    private String timepoint;

    private String buildTableTitleItems;

    private String hdfsdelimiter;


    // 如果使用的是hive数据源地址直接是由hive 之后之后的结果路径指定的
    private IndexBuildSourcePathCreator indexBuildSourcePathCreator;

    /**
     * 导入数据条数
     */
    private Long dumpCount;

    @Override
    public Long getDumpCount() {
        return dumpCount;
    }

    public void setDumpCount(Long dumpCount) {
        this.dumpCount = dumpCount;
    }

//    /**
//     * 容器规格
//     */
//    private ContainerSpecification containerSpecification;

    // exec type dump,create ,update
    private String execType;

    private final ITISFileSystem fileSystem;
    private final ITISCoordinator coordinator;

//    public String getRootDir() {
//        return fileSystem.getRootDir();
//    }

    // private Long dumpCount;
    public ImportDataProcessInfo(Integer taskId, ITISFileSystem fsFactory, ITISCoordinator coordinator) {
        super();
        this.taskId = taskId;
        this.fileSystem = fsFactory;
        this.coordinator = coordinator;
    }

    @Override
    public ITISCoordinator getCoordinator() {
        return this.coordinator;
    }

    public static String createIndexDir(ITISFileSystem fsFactory, String timePoint, String groupNum, String serviceName, boolean isSourceDir) {
        return fsFactory.getRootDir() + "/" + serviceName + "/all/" + groupNum + (!isSourceDir ? "/output" : StringUtils.EMPTY) + "/" + timePoint;
    }

    @Override
    public String getIndexBuilder() {
        return indexBuilder;
    }

    public void setIndexBuilder(String indexBuilder) {
        this.indexBuilder = indexBuilder;
    }

    public LuceneVersion getLuceneVersion() {
        return luceneVersion;
    }

    public void setLuceneVersion(LuceneVersion luceneVersion) {
        this.luceneVersion = luceneVersion;
    }

    public String getIndexBuildOutputPath(int groupIndex) {
        return createIndexDir(fileSystem, this.timepoint, String.valueOf(groupIndex), this.getIndexName(), false);
    }

    @Override
    public String getHdfsdelimiter() {
        return hdfsdelimiter;
    }

    public void setHdfsdelimiter(String hdfsdelimiter) {
        this.hdfsdelimiter = hdfsdelimiter;
    }

    @Override
    public IndexBuildSourcePathCreator getHdfsSourcePath() {
        return indexBuildSourcePathCreator;
    }

    public void setIndexBuildSourcePathCreator(IndexBuildSourcePathCreator hdfsSourcePath) {
        this.indexBuildSourcePathCreator = hdfsSourcePath;
    }

    @Override
    public String getBuildTableTitleItems() {
        return buildTableTitleItems;
    }

    public void setBuildTableTitleItems(String buildTableTitleItems) {
        this.buildTableTitleItems = buildTableTitleItems;
    }

    @Override
    public String getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(String timepoint) {
        this.timepoint = timepoint;
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    public String getCoreName(int groupNum) {
        return this.getIndexName() + '-' + groupNum;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public Integer getTaskId() {
        return taskId;
    }

//    public ContainerSpecification getContainerSpecification() {
//        return containerSpecification;
//    }
//
//    public void setContainerSpecification(ContainerSpecification containerSpecification) {
//        this.containerSpecification = containerSpecification;
//    }
//
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        ImportDataProcessInfo result = (ImportDataProcessInfo) super.clone();
//        result.containerSpecification = (ContainerSpecification) this.containerSpecification.clone();
//        return result;
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImportDataProcessInfo)) {
            return false;
        }
        ImportDataProcessInfo other = (ImportDataProcessInfo) obj;
        return other.getTaskId() == (other.getTaskId() + 0);
        // return super.equals(obj);
    }

    public static void main(String[] args) throws Exception {
    }
}
