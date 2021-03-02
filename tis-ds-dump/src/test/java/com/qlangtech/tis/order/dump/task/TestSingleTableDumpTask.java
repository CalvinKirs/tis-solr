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
package com.qlangtech.tis.order.dump.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qlangtech.tis.TisZkClient;
import com.qlangtech.tis.fs.ITaskContext;
import com.qlangtech.tis.fullbuild.indexbuild.TaskContext;
import com.qlangtech.tis.git.GitUtils;
import com.qlangtech.tis.hdfs.client.bean.CommonTerminatorBeanFactory;
import com.qlangtech.tis.hdfs.client.context.TSearcherDumpContext;
import com.qlangtech.tis.manage.common.CenterResource;
import com.qlangtech.tis.manage.common.HttpUtils;
import com.qlangtech.tis.offline.FileSystemFactory;
import com.qlangtech.tis.offline.TableDumpFactory;
import com.qlangtech.tis.order.center.IParamContext;
import com.qlangtech.tis.plugin.ds.DataDumpers;
import com.qlangtech.tis.plugin.ds.DataSourceFactory;
import com.qlangtech.tis.plugin.ds.IDataSourceDumper;
import com.qlangtech.tis.plugin.ds.TISTable;
import com.qlangtech.tis.sql.parser.tuple.creator.EntityName;
import com.tis.hadoop.rpc.StatusRpcClient;
import junit.framework.TestCase;
import org.easymock.EasyMock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 测试单表导入
 *
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public class TestSingleTableDumpTask extends TestCase implements ITableDumpConstant {

    static {
        HttpUtils.addMockGlobalParametersConfig();
        CenterResource.setNotFetchFromCenterRepository();
    }

    private static final String DB_EMPLOYEES = "employees";

    private static final String TABLE_EMPLOYEES = "employees";

    public void testDumpEmployees() throws Exception {
        GitUtils.ExecuteGetTableConfigCount = 0;

        EntityName entityName = EntityName.parse(DB_EMPLOYEES + "." + TABLE_EMPLOYEES);
        TISTable tisTable = CommonTerminatorBeanFactory.getTisTable(entityName);

        FileSystemFactory fsFactory = FileSystemFactory.getFsFactory("local_fs");
        assertNotNull(fsFactory);

        DataSourceFactory dataBasePluginStore = EasyMock.createMock("dataSourceFactory", DataSourceFactory.class);

        int splitCount = 1;
        List<IDataSourceDumper> dumpers = Lists.newArrayList();
        dumpers.add(new TestEmployeeDataSourceDumper());

        DataDumpers dataDumpers = new DataDumpers(splitCount, dumpers.iterator());

        EasyMock.expect(dataBasePluginStore.getDataDumpers(tisTable)).andReturn(dataDumpers).anyTimes();

        TableDumpFactory tableDumpPlugin = new MockTableDumpFactory(fsFactory); //EasyMock.createMock("tableDumpFactory", TableDumpFactory.class);// pluginStore.getPlugin();
        // assertNotNull(tableDumpPlugin);

        //EasyMock.expect(tableDumpPlugin.getFileSystem()).andReturn(fsFactory);
        // tableDumpPlugin.getFileSystem();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String startTimeStamp = format.format(new Date());
        TisZkClient zkClient = EasyMock.createMock("tisZkClient", TisZkClient.class);

        AtomicReference<StatusRpcClient.AssembleSvcCompsite> statusRpc = new AtomicReference<>();
        statusRpc.set(StatusRpcClient.AssembleSvcCompsite.MOCK_PRC);
        SingleTableDumpTask tableDumpTask = new SingleTableDumpTask(tableDumpPlugin, dataBasePluginStore, zkClient, statusRpc) {

            protected void registerZKDumpNodeIn(TaskContext context) {
            }
        };
        tableDumpTask.setSourceDataProviderFactoryInspect((datasourceFactory) -> {
//            Objects.requireNonNull(dbmeta, "param dbmeta can not be null");
//            assertEquals("dbmeta dbEnum size", 1, dbmeta.getDbEnum().size());
//            List<String> dbs = dbmeta.getDbEnum().get("192.168.28.200");
//            assertNotNull(dbs);
//            assertEquals(4, dbs.size());
//            List<SourceDataProvider<String, String>> result = datasourceFactory.result;
//            assertEquals("parse sub db size", 4, result.size());
        });
        Map<String, String> params = Maps.newHashMap();
        TaskContext taskContext = TaskContext.create(params);
        params.put(ITableDumpConstant.DUMP_START_TIME, startTimeStamp);
        params.put(ITableDumpConstant.JOB_NAME, DB_EMPLOYEES + "." + TABLE_EMPLOYEES);
        params.put(ITableDumpConstant.DUMP_TABLE_NAME, TABLE_EMPLOYEES);
        params.put(ITableDumpConstant.DUMP_DBNAME, DB_EMPLOYEES);
        params.put(IParamContext.KEY_TASK_ID, "1234567");
        // 有已经导入的数据存在是否有必要重新导入
        params.put(ITableDumpConstant.DUMP_FORCE, "true");
        EasyMock.replay(zkClient, dataBasePluginStore);
        // TaskReturn result =
        tableDumpTask.map(taskContext);
        assertEquals(1, GitUtils.ExecuteGetTableConfigCount);
        int allTableDumpRows = tableDumpTask.getAllTableDumpRows();
        assertTrue(allTableDumpRows > 0);
        tableDumpPlugin.startTask((r) -> {
            testConnectionWorkRegular(r, tableDumpTask.getDumpContext(), startTimeStamp);
        });

        EasyMock.verify(zkClient, dataBasePluginStore);

    }

    private void testConnectionWorkRegular(ITaskContext r, TSearcherDumpContext dumpContext, String startTimeStamp) {
        Connection con = r.getObj();
        assertNotNull(con);
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.createStatement();
            // result = stmt.executeQuery("desc " + DB_ORDER + "." + TABLE_TOTALPAYINFO);
            final String tableName = DB_EMPLOYEES + "." + TABLE_EMPLOYEES;
            result = stmt.executeQuery("select count(1) FROM " + tableName + " WHERE pt='" + startTimeStamp + "'");
            if (result.next()) {
                int rows = result.getInt(1);
                assertEquals(dumpContext.getAllTableDumpRows().get(), rows);
            } else {
                fail("can not get new dump table rows:" + TABLE_EMPLOYEES);
            }
            result = stmt.executeQuery("show partitions " + tableName);
            int ptCount = 0;
            while (result.next()) {
                System.out.println(result.getString(1));
                ptCount++;
            }
            int maxPtCount = ((ITableDumpConstant.MAX_PARTITION_SAVE + 1) * ITableDumpConstant.RAND_GROUP_NUMBER);
            assertTrue("ptCount shall big than 0", ptCount > 0);
            assertTrue("ptCount:" + ptCount + " <= ITableDumpConstant.MAX_PARTITION_SAVE:" + maxPtCount, ptCount <= maxPtCount);
            // success.set(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                result.close();
            } catch (Throwable e) {
            }
            try {
                stmt.close();
            } catch (Throwable e) {
            }
            try {
                con.close();
            } catch (Throwable e) {
            }
        }
    }
}
