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
package com.qlangtech.tis.manage.servlet;

import com.google.common.collect.Lists;
import com.qlangtech.tis.async.message.client.consumer.IMQConsumerStatusFactory;
import com.qlangtech.tis.manage.common.ConfigFileContext;
import com.qlangtech.tis.manage.common.HttpUtils;
import com.qlangtech.tis.manage.common.MockHttpURLConnection;
import com.qlangtech.tis.realtime.transfer.IIncreaseCounter;
import com.qlangtech.tis.trigger.jst.ILogListener;
import com.qlangtech.tis.trigger.socket.ExecuteState;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @create: 2020-08-31 10:28
 */
public class TestIncrTagHeatBeatMonitor extends TestCase {

    private static final String collectionName = "search4totalpay";

    private static final String tag_talpayinfo = "totalpayinfo";

    private static final String tag_servicebillinfo = "servicebillinfo";

    // public void test(){
    // System.out.println(  String.format("hell%d",1) );
    // }
    public void testBuild() {
        String resName = "collection_TopicTags_status_%d.json";
        AtomicInteger resFetchCount = new AtomicInteger();
        HttpUtils.mockConnMaker = new HttpUtils.DefaultMockConnectionMaker() {

            @Override
            protected MockHttpURLConnection createConnection(int appendOrder, List<ConfigFileContext.Header> heads, ConfigFileContext.HTTPMethod method, byte[] content, HttpUtils.ClasspathRes cpRes) {
                // return super.createConnection(appendOrder, heads, method, content, cpRes);
                String res = String.format(resName, resFetchCount.incrementAndGet());
                try {
                    return new MockHttpURLConnection(cpRes.getResourceAsStream(res));
                } catch (Throwable e) {
                    throw new RuntimeException(res, e);
                }
            }
        };
        HttpUtils.addMockApply(0, "incr-control", StringUtils.EMPTY, TestIncrTagHeatBeatMonitor.class);
        // 增量节点处理
        final Map<String, TopicTagStatus> /* tag */
        transferTagStatus = new HashMap<>();
        final Map<String, TopicTagStatus> /* tag */
        binlogTopicTagStatus = new HashMap<>();
        Collection<TopicTagIncrStatus.FocusTags> focusTags = Lists.newArrayList();
        // String topic, Collection<String> tags
        ArrayList<String> tags = Lists.newArrayList(tag_servicebillinfo, tag_talpayinfo, "orderdetail", "specialfee", "ent_expense", "payinfo", "order_bill", "instancedetail", "ent_expense_order");
        focusTags.add(new TopicTagIncrStatus.FocusTags("test-topic", tags));
        TopicTagIncrStatus topicTagIncrStatus = new TopicTagIncrStatus(focusTags);
        MockWebSocketMessagePush wsMessagePush = new MockWebSocketMessagePush();
        MockMQConsumerStatus mqConsumerStatus = new MockMQConsumerStatus();
        IncrTagHeatBeatMonitor incrTagHeatBeatMonitor = new IncrTagHeatBeatMonitor(this.collectionName, wsMessagePush, transferTagStatus, binlogTopicTagStatus, topicTagIncrStatus, mqConsumerStatus);
        incrTagHeatBeatMonitor.build();
        assertEquals(6, wsMessagePush.count);
    }

    private static final class MockWebSocketMessagePush implements ILogListener {

        private int count;

        @Override
        public void read(Object event) {
        }

        @Override
        public boolean isClosed() {
            return this.count++ >= 5;
        }

        @Override
        public void sendMsg2Client(Object biz) throws IOException {
            ExecuteState<TopicTagIncrStatus.TisIncrStatus> event = (ExecuteState<TopicTagIncrStatus.TisIncrStatus>) biz;
            TopicTagIncrStatus.TisIncrStatus msg = event.getMsg();
            // System.out.println("SOLR_CONSUME_COUNT:" + msg.getSummary().get(IIncreaseCounter.SOLR_CONSUME_COUNT));
            if (count > 1) {
                assertEquals(100, (int) msg.getSummary().get(IIncreaseCounter.SOLR_CONSUME_COUNT));
                assertEquals(100, (int) msg.getSummary().get(IIncreaseCounter.TABLE_CONSUME_COUNT));
                Optional<TopicTagIncrStatus.TopicTagIncr> talpayinfo = msg.getTags().stream().filter((t) -> tag_talpayinfo.equals(t.getTag())).findFirst();
                assertTrue(talpayinfo.isPresent());
                assertEquals(10, talpayinfo.get().getTrantransferIncr());
                Optional<TopicTagIncrStatus.TopicTagIncr> servicebillinfo = msg.getTags().stream().filter((t) -> tag_servicebillinfo.equals(t.getTag())).findFirst();
                assertTrue(servicebillinfo.isPresent());
                assertEquals(20, servicebillinfo.get().getTrantransferIncr());
            }
        // System.out.println(JSON.toJSONString(biz, true));
        }
    }

    private static final class MockMQConsumerStatus implements IMQConsumerStatusFactory.IMQConsumerStatus {

        private long totalDiff;
        // @Override
        // public long getTotalDiff() {
        // return this.totalDiff += 1000;
        // }
        // 
        // @Override
        // public void close() {
        // 
        // }
    }
}
