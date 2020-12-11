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

import com.google.common.collect.Maps;
import com.qlangtech.tis.TIS;
import com.qlangtech.tis.TisZkClient;
import com.qlangtech.tis.fs.ITaskContext;
import com.qlangtech.tis.fullbuild.indexbuild.TaskContext;
import com.qlangtech.tis.git.GitUtils;
import com.qlangtech.tis.hdfs.client.context.TSearcherDumpContext;
import com.qlangtech.tis.hdfs.client.data.SourceDataProvider;
import com.qlangtech.tis.manage.common.CenterResource;
import com.qlangtech.tis.manage.common.HttpUtils;
import com.qlangtech.tis.offline.TableDumpFactory;
import com.qlangtech.tis.order.center.IParamContext;
import com.qlangtech.tis.plugin.PluginStore;
import com.qlangtech.tis.plugin.ds.DataSourceFactoryPluginStore;
import com.qlangtech.tis.plugin.ds.PostedDSProp;
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

    private static final String DB_ORDER = "order1";

    private static final String TABLE_TOTALPAYINFO = "totalpayinfo";

    public void testDumpTotalpayinfo() throws Exception {
        GitUtils.ExecuteGetTableConfigCount = 0;
        PluginStore<TableDumpFactory> pluginStore = TIS.getPluginStore(TableDumpFactory.class);
        DataSourceFactoryPluginStore dataBasePluginStore = TIS.getDataBasePluginStore(null, new PostedDSProp(DB_ORDER));
        assertNotNull(pluginStore);
        TableDumpFactory tableDumpPlugin = pluginStore.getPlugin();
        assertNotNull(tableDumpPlugin);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String startTimeStamp = format.format(new Date());
        TisZkClient zkClient = EasyMock.createMock("tisZkClient", TisZkClient.class);

        AtomicReference<StatusRpcClient.AssembleSvcCompsite> statusRpc = new AtomicReference<>();
        statusRpc.set(StatusRpcClient.AssembleSvcCompsite.MOCK_PRC);
        SingleTableDumpTask tableDumpTask = new SingleTableDumpTask(tableDumpPlugin, dataBasePluginStore.getPlugin(), zkClient, statusRpc) {

            protected void registerZKDumpNodeIn(TaskContext context) {
            }
        };
        tableDumpTask.setSourceDataProviderFactoryInspect((dbmeta, datasourceFactory) -> {
            assertEquals("dbmeta dbEnum size", 1, dbmeta.getDbEnum().size());
            List<String> dbs = dbmeta.getDbEnum().get("192.168.28.200");
            assertNotNull(dbs);
            assertEquals(4, dbs.size());
//            List<SourceDataProvider<String, String>> result = datasourceFactory.result;
//            assertEquals("parse sub db size", 4, result.size());
        });
        Map<String, String> params = Maps.newHashMap();
        TaskContext taskContext = TaskContext.create(params);
        params.put(ITableDumpConstant.DUMP_START_TIME, startTimeStamp);
        params.put(ITableDumpConstant.JOB_NAME, DB_ORDER + "." + TABLE_TOTALPAYINFO);
        params.put(ITableDumpConstant.DUMP_TABLE_NAME, TABLE_TOTALPAYINFO);
        params.put(ITableDumpConstant.DUMP_DBNAME, DB_ORDER);
        params.put(IParamContext.KEY_TASK_ID, "1234567");
        // 有已经导入的数据存在是否有必要重新导入
        params.put(ITableDumpConstant.DUMP_FORCE, "true");
        EasyMock.replay(zkClient);
        // TaskReturn result =
        tableDumpTask.map(taskContext);
        assertEquals(1, GitUtils.ExecuteGetTableConfigCount);
        int allTableDumpRows = tableDumpTask.getAllTableDumpRows();
        assertTrue(allTableDumpRows > 0);
        tableDumpPlugin.startTask((r) -> {
            testConnectionWorkRegular(r, tableDumpTask.getDumpContext(), startTimeStamp);
        });

    }

    private void testConnectionWorkRegular(ITaskContext r, TSearcherDumpContext dumpContext, String startTimeStamp) {
        Connection con = r.getObj();
        assertNotNull(con);
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.createStatement();
            // result = stmt.executeQuery("desc " + DB_ORDER + "." + TABLE_TOTALPAYINFO);
            final String tableName = DB_ORDER + "." + TABLE_TOTALPAYINFO;
            result = stmt.executeQuery("select count(1) FROM " + tableName + " WHERE pt='" + startTimeStamp + "'");
            if (result.next()) {
                int rows = result.getInt(1);
                assertEquals(dumpContext.getAllTableDumpRows().get(), rows);
            } else {
                fail("can not get new dump table rows:" + TABLE_TOTALPAYINFO);
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
