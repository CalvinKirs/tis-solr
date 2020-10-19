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
package com.qlangtech.tis.realtime.s4employee;

import com.google.common.collect.Sets;
import com.qlangtech.tis.realtime.BasicBeanGroup;
import com.qlangtech.tis.realtime.BasicIncrTestCase;
import com.qlangtech.tis.realtime.BasicTestCase;
import com.qlangtech.tis.realtime.PojoCUD;
import com.qlangtech.tis.realtime.test.employees.dao.IEmployeesDAOFacade;
import com.qlangtech.tis.realtime.test.employees.pojo.DeptEmp;
import com.qlangtech.tis.realtime.test.employees.pojo.DeptEmpCriteria;
import com.qlangtech.tis.realtime.transfer.BasicRMListener;
import com.qlangtech.tis.spring.LauncherResourceUtils;

import java.util.Set;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @create: 2020-08-26 14:19
 */
public class TestS4employee extends BasicIncrTestCase {

    static final String collectionName = "search4test2";
    static final long wfTimestamp = 20201016143313l;

    private static final Set<String> includeSpringContext;


    static {
        includeSpringContext = Sets.newHashSet();
        includeSpringContext.add("employees-dao-context.xml");
        LauncherResourceUtils.resourceFilter = (res) -> {
            return includeSpringContext.contains(res.getFilename());
        };
    }

    private IEmployeesDAOFacade employeesDAOFacade;

    public static final String TAB_DEPT_EMP = "dept_emp";

    public TestS4employee() {
        //boolean shallRegisterMQ, String collectionName, long wfTimestamp, String... configLocations
        super(false, collectionName, wfTimestamp, "/conf/employees-dao-context.xml");
        this.employeesDAOFacade = this.appContext.getBean("employeesDAOFacade", IEmployeesDAOFacade.class);
    }

  //  public void testGetBeanGroup() {
//        BeanGroup beanGroup = new BeanGroup().invoke();
//        assertNotNull(beanGroup);
//        beanGroup.totalpayinfo.startCollectUpdateProp();
//        assertTrue(beanGroup.totalpayinfo.vals.isUpdatePropsCollect());
   // }

    /**
     * 添加部门职工
     *
     * @throws Exception
     */
    public void testAddDepyEmployee() throws Exception {

        BeanGroup bgroup = new BeanGroup(this.listenerBean);
        bgroup.invoke();
        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_DEPT_EMP, bgroup.deptEmp1)));
        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_DEPT_EMP, bgroup.deptEmp2)));

        listenerBean.setPojoVisit((pojo, doc, shareid) -> {
            assertEquals(6, pojo.getRowsPack().size());

        });
    }

    protected class BeanGroup extends BasicBeanGroup {
        BasicTestCase.DTO<DeptEmp> deptEmp1;
        BasicTestCase.DTO<DeptEmp> deptEmp2;

        public BeanGroup(BasicRMListener listenerBean) {
            super(listenerBean);
        }

        @Override
        public BeanGroup invoke() {

            PojoCUD<DeptEmp> deptEmpCUD = new PojoCUD<DeptEmp>() {

                @Override
                public String getTableName() {
                    return "dept_emp";
                }

                @Override
                public void initSyncWithDB(DeptEmp pojo) {
//String deptNo, Integer empNo
                    employeesDAOFacade.getDeptEmpDAO().deleteByPrimaryKey(pojo.getDeptNo(), pojo.getEmpNo());
                    employeesDAOFacade.getDeptEmpDAO().insertSelective(pojo);
//                    orderDAOFacade.getPayinfoDAO().deleteByPrimaryKey(pojo.getPayId());
//                    orderDAOFacade.getPayinfoDAO().insertSelective(pojo);
                }

                @Override
                public void updateByExampleSelective(DeptEmp pojo, DeptEmp oldPojo) {
                    assertNotNull(pojo.getDeptNo());
                    assertNotNull(pojo.getEmpNo());
                    assertNotNull(pojo.getFromDate());
                    assertNotNull(pojo.getToDate());
                    DeptEmpCriteria c = new DeptEmpCriteria();
                    c.createCriteria().andDeptNoEqualTo(oldPojo.getDeptNo()).andEmpNoEqualTo(oldPojo.getEmpNo());
                    employeesDAOFacade.getDeptEmpDAO().updateByExampleSelective(pojo, c);
                }
            };

            deptEmp1 = getBean("dept_emp_1.txt", DeptEmp.class, deptEmpCUD);
            deptEmp2 = getBean("dept_emp_2.txt", DeptEmp.class, deptEmpCUD);
            return this;
        }
    }

//
//    public void testAddTotalpayInfo() throws Exception {
//        assertNotNull(orderDAOFacade);
//        // TisIncrLauncher.main(new String[]{"search4totalpay", "20190820171040"});
//        CountDownLatch countd = new CountDownLatch(1);
//        System.out.println("test over");
//        BeanGroup beanGroup = new BeanGroup().invoke();
//        DTO<Specialfee> specialfee = beanGroup.getSpecialfee();
//        DTO<MallShop> shopPojo = beanGroup.getShopPojo();
//        DTO<Card> card = beanGroup.getCard();
//        DTO<Customer> custm = beanGroup.getCustm();
//        DTO<Payinfo> payinfo1 = beanGroup.getPayinfo1();
//        DTO<Payinfo> payinfo2 = beanGroup.getPayinfo2();
//        DTO<TakeoutOrderExtra> takeoutOrderExtra = beanGroup.getTakeoutOrderExtra();
//        DTO<OrderBill> orderBill1 = beanGroup.getOrderBill1();
//        DTO<Instancedetail> instance1 = beanGroup.getInstance1();
//        DTO<Instancedetail> instance2 = beanGroup.getInstance2();
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_PAYINFO, payinfo1)));
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_PAYINFO, payinfo2)));
//        // assertTrue(listenerBean.consume(createInsertMQMessage(TAB_TAKEOUT_ORDER_EXTRA, takeoutOrderExtra)));
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_ORDER_BILL, orderBill1)));
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_INSTANCE, instance1)));
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_INSTANCE, instance2)));
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_ORDERINFO, beanGroup.orderdetail)));
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_TOTALPAY, beanGroup.totalpayinfo)));
//        assertTrue(listenerBean.consume(createInsertMQMessage(TAB_SPECIALFEE, specialfee)));
//        listenerBean.setPojoVisit((pojo, doc, shareid) -> {
//            try {
//                assertEquals(6, pojo.getRowsPack().size());
//                assertTrue("isAdd shall be true", pojo.isAdd());
//                // assertEquals(TAB_TOTALPAY, pojo.getPrimaryTableName());
//                // 内部会执行field transfer逻辑
//                // DefaultTableWrapper totalpayinfoWrapper = new DefaultTableWrapper(TAB_TOTALPAY, listenerBean, totalpayinfo);
//                listenerBean.getTabColumnMeta(TAB_TOTALPAY).assertEqual(beanGroup.totalpayinfo.junitValsExample, doc.getInputDoc());
//                // DefaultTableWrapper orderdetailWrapper = new DefaultTableWrapper(TAB_ORDERINFO, listenerBean, beanGroup);
//                listenerBean.getTabColumnMeta(TAB_ORDERINFO).assertEqual(beanGroup.orderdetail.junitValsExample, doc.getInputDoc());
//                DefaultTableWrapper instanceWrapper = new DefaultTableWrapper(TAB_INSTANCE, listenerBean, instance1.junitValsExample);
//                listenerBean.getTabColumnMeta(TAB_INSTANCE).assertEqual(instanceWrapper, doc.getInputDoc());
//                // 由于card和customer不会被监听所以，在totalpay发起添加事件之后card和customer需要被从数据库中reload回来
//                listenerBean.getTabColumnMeta(TAB_CARD).assertEqual(card.junitValsExample, doc.getInputDoc());
//                listenerBean.getTabColumnMeta(TAB_CUSTOMER).assertEqual(custm.junitValsExample, doc.getInputDoc());
//                listenerBean.getTabColumnMeta(TAB_MALL_SHOP).assertEqual(shopPojo.junitValsExample, doc.getInputDoc());
//                listenerBean.getTabColumnMeta(TAB_SPECIALFEE).assertEqual(specialfee.junitValsExample, doc.getInputDoc());
//                listenerBean.getTabColumnMeta(TAB_ORDER_BILL).assertEqual(orderBill1.junitValsExample, doc.getInputDoc());
//                // FIXME 由于监听先忽略，先暂时停止断言
//                // listenerBean.getTabColumnMeta(TAB_TAKEOUT_ORDER_EXTRA).assertEqual(takeoutOrderExtra.junitValsExample, doc.getInputDoc());
//                listenerBean.getTabColumnMeta(TAB_PAYINFO).assertEqual(payinfo1.junitValsExample, doc.getInputDoc());
//                // payinfo
//                assertEquals(1, doc.getFieldValue("is_enterprise_card"));
//                assertEquals("009_1_5.01_15.0_00340013elev1WSJknd89T3LY5Mpmz;008_1_6.18_23.98_003400136cd36c1d016cd373758508ab", doc.getFieldValue("kindpay"));
//                // from instancedetail
//                assertEquals(1, doc.getFieldValue("has_fetch"));
//                assertEquals("8f931168c2b44f7bb05e77e27286ce0c,8f931168c2b44f7bb05e77e27286cccc", doc.getFieldValue("customer_ids"));
//            // 8f931168c2b44f7bb05e77e27286ce0c
//            // for (SolrInputField f : doc.getInputDoc()) {
//            // System.out.println(f.getName() + ":" + f.getValue());
//            // }
//            } finally {
//            }
//            countd.countDown();
//        });
//        assertTrue("wait timeout", countd.await(Time_Window_15000 + 50000, TimeUnit.MILLISECONDS));
//    }
//
//    /**
//     * 测试更新流程
//     */
//    public void testUpdateTotalpayInfo() throws Exception {
//        BeanGroup beanGroup = new BeanGroup().invoke();
//        SolrDocument solrDocument = deseriablizeDoc("doc_1.txt");
//        MockTisCloudClient.expectGetDocById(String.valueOf(solrDocument.getFieldValue("totalpay_id")), solrDocument);
//        final long timestamp = System.currentTimeMillis() / 1000;
//        CountDownLatch countd = new CountDownLatch(1);
//        assertTrue(listenerBean.consume(createUpdateMQMessage(TAB_PAYINFO, beanGroup.payinfo1, (pay) -> {
//            pay.put("coupon_fee", "9.00");
//            pay.put("coupon_cost", "2.7");
//            pay.put("coupon_num", "3");
//            pay.put("coupon_num", "3");
//            pay.put(KEY_MODIFY_TIME, String.valueOf(timestamp));
//            return pay;
//        })));
//        listenerBean.setPojoVisit((pojo, doc, shareid) -> {
//            assertEquals(1, pojo.getRowsPack().size());
//            assertFalse("update shall be true", pojo.isAdd());
//            // payinfo
//            assertEquals(1, doc.getFieldValue("is_enterprise_card"));
//            assertEquals("009_1_5.01_18.9_00340013elev1WSJknd89T3LY5Mpmz;008_1_6.18_23.98_003400136cd36c1d016cd373758508ab", doc.getFieldValue("kindpay"));
//            assertEquals(beanGroup.payinfo1.junitValsExample.getColumn(KEY_MODIFY_TIME), String.valueOf(doc.getFieldValue(BasicPojoConsumer.VERSION_FIELD_NAME)));
//            countd.countDown();
//        });
//        assertTrue("wait timeout", countd.await(Time_Window_15000 + 50000, TimeUnit.MILLISECONDS));
//        MockTisCloudClient.validateExpect();
//    }
}
