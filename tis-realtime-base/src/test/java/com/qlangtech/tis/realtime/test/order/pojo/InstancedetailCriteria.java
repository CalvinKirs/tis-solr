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
package com.qlangtech.tis.realtime.test.order.pojo;

import com.qlangtech.tis.manage.common.TISBaseCriteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public class InstancedetailCriteria extends TISBaseCriteria {

    protected String orderByClause;

    protected List<Criteria> oredCriteria;

    private final Set<InstancedetailColEnum> cols = Sets.newHashSet();

    public InstancedetailCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    protected InstancedetailCriteria(InstancedetailCriteria example) {
        this.orderByClause = example.orderByClause;
        this.oredCriteria = example.oredCriteria;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
    }

    public final boolean isTargetColsEmpty() {
        return this.cols.size() < 1;
    }

    public final List<InstancedetailColEnum> getCols() {
        return Lists.newArrayList(this.cols);
    }

    public final void addSelCol(InstancedetailColEnum... colName) {
        for (InstancedetailColEnum c : colName) {
            if (!c.isPK()) {
                this.cols.add(c);
            }
        }
    }

    public static class Criteria {

        protected List<String> criteriaWithoutValue;

        protected List<Map<String, Object>> criteriaWithSingleValue;

        protected List<Map<String, Object>> criteriaWithListValue;

        protected List<Map<String, Object>> criteriaWithBetweenValue;

        protected Criteria() {
            super();
            criteriaWithoutValue = new ArrayList<String>();
            criteriaWithSingleValue = new ArrayList<Map<String, Object>>();
            criteriaWithListValue = new ArrayList<Map<String, Object>>();
            criteriaWithBetweenValue = new ArrayList<Map<String, Object>>();
        }

        public boolean isValid() {
            return criteriaWithoutValue.size() > 0 || criteriaWithSingleValue.size() > 0 || criteriaWithListValue.size() > 0 || criteriaWithBetweenValue.size() > 0;
        }

        public List<String> getCriteriaWithoutValue() {
            return criteriaWithoutValue;
        }

        public List<Map<String, Object>> getCriteriaWithSingleValue() {
            return criteriaWithSingleValue;
        }

        public List<Map<String, Object>> getCriteriaWithListValue() {
            return criteriaWithListValue;
        }

        public List<Map<String, Object>> getCriteriaWithBetweenValue() {
            return criteriaWithBetweenValue;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteriaWithoutValue.add(condition);
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("condition", condition);
            map.put("value", value);
            criteriaWithSingleValue.add(map);
        }

        protected void addCriterion(String condition, List<? extends Object> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("condition", condition);
            map.put("values", values);
            criteriaWithListValue.add(map);
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            List<Object> list = new ArrayList<Object>();
            list.add(value1);
            list.add(value2);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("condition", condition);
            map.put("values", list);
            criteriaWithBetweenValue.add(map);
        }

        public Criteria andInstanceIdIsNull() {
            addCriterion("instance_id is null");
            return this;
        }

        public Criteria andInstanceIdIsNotNull() {
            addCriterion("instance_id is not null");
            return this;
        }

        public Criteria andInstanceIdEqualTo(String value) {
            addCriterion("instance_id =", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdNotEqualTo(String value) {
            addCriterion("instance_id <>", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdGreaterThan(String value) {
            addCriterion("instance_id >", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdGreaterThanOrEqualTo(String value) {
            addCriterion("instance_id >=", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdLessThan(String value) {
            addCriterion("instance_id <", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdLessThanOrEqualTo(String value) {
            addCriterion("instance_id <=", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdLike(String value) {
            addCriterion("instance_id like", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdNotLike(String value) {
            addCriterion("instance_id not like", value, "instanceId");
            return this;
        }

        public Criteria andInstanceIdIn(List<String> values) {
            addCriterion("instance_id in", values, "instanceId");
            return this;
        }

        public Criteria andInstanceIdNotIn(List<String> values) {
            addCriterion("instance_id not in", values, "instanceId");
            return this;
        }

        public Criteria andInstanceIdBetween(String value1, String value2) {
            addCriterion("instance_id between", value1, value2, "instanceId");
            return this;
        }

        public Criteria andInstanceIdNotBetween(String value1, String value2) {
            addCriterion("instance_id not between", value1, value2, "instanceId");
            return this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return this;
        }

        public Criteria andOrderIdEqualTo(String value) {
            addCriterion("order_id =", value, "orderId");
            return this;
        }

        public Criteria andOrderIdNotEqualTo(String value) {
            addCriterion("order_id <>", value, "orderId");
            return this;
        }

        public Criteria andOrderIdGreaterThan(String value) {
            addCriterion("order_id >", value, "orderId");
            return this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("order_id >=", value, "orderId");
            return this;
        }

        public Criteria andOrderIdLessThan(String value) {
            addCriterion("order_id <", value, "orderId");
            return this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(String value) {
            addCriterion("order_id <=", value, "orderId");
            return this;
        }

        public Criteria andOrderIdLike(String value) {
            addCriterion("order_id like", value, "orderId");
            return this;
        }

        public Criteria andOrderIdNotLike(String value) {
            addCriterion("order_id not like", value, "orderId");
            return this;
        }

        public Criteria andOrderIdIn(List<String> values) {
            addCriterion("order_id in", values, "orderId");
            return this;
        }

        public Criteria andOrderIdNotIn(List<String> values) {
            addCriterion("order_id not in", values, "orderId");
            return this;
        }

        public Criteria andOrderIdBetween(String value1, String value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return this;
        }

        public Criteria andOrderIdNotBetween(String value1, String value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
            return this;
        }

        public Criteria andBatchMsgIsNull() {
            addCriterion("batch_msg is null");
            return this;
        }

        public Criteria andBatchMsgIsNotNull() {
            addCriterion("batch_msg is not null");
            return this;
        }

        public Criteria andBatchMsgEqualTo(String value) {
            addCriterion("batch_msg =", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgNotEqualTo(String value) {
            addCriterion("batch_msg <>", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgGreaterThan(String value) {
            addCriterion("batch_msg >", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgGreaterThanOrEqualTo(String value) {
            addCriterion("batch_msg >=", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgLessThan(String value) {
            addCriterion("batch_msg <", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgLessThanOrEqualTo(String value) {
            addCriterion("batch_msg <=", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgLike(String value) {
            addCriterion("batch_msg like", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgNotLike(String value) {
            addCriterion("batch_msg not like", value, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgIn(List<String> values) {
            addCriterion("batch_msg in", values, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgNotIn(List<String> values) {
            addCriterion("batch_msg not in", values, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgBetween(String value1, String value2) {
            addCriterion("batch_msg between", value1, value2, "batchMsg");
            return this;
        }

        public Criteria andBatchMsgNotBetween(String value1, String value2) {
            addCriterion("batch_msg not between", value1, value2, "batchMsg");
            return this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return this;
        }

        public Criteria andTypeEqualTo(Short value) {
            addCriterion("type =", value, "type");
            return this;
        }

        public Criteria andTypeNotEqualTo(Short value) {
            addCriterion("type <>", value, "type");
            return this;
        }

        public Criteria andTypeGreaterThan(Short value) {
            addCriterion("type >", value, "type");
            return this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("type >=", value, "type");
            return this;
        }

        public Criteria andTypeLessThan(Short value) {
            addCriterion("type <", value, "type");
            return this;
        }

        public Criteria andTypeLessThanOrEqualTo(Short value) {
            addCriterion("type <=", value, "type");
            return this;
        }

        public Criteria andTypeIn(List<Short> values) {
            addCriterion("type in", values, "type");
            return this;
        }

        public Criteria andTypeNotIn(List<Short> values) {
            addCriterion("type not in", values, "type");
            return this;
        }

        public Criteria andTypeBetween(Short value1, Short value2) {
            addCriterion("type between", value1, value2, "type");
            return this;
        }

        public Criteria andTypeNotBetween(Short value1, Short value2) {
            addCriterion("type not between", value1, value2, "type");
            return this;
        }

        public Criteria andWaitinginstanceIdIsNull() {
            addCriterion("waitinginstance_id is null");
            return this;
        }

        public Criteria andWaitinginstanceIdIsNotNull() {
            addCriterion("waitinginstance_id is not null");
            return this;
        }

        public Criteria andWaitinginstanceIdEqualTo(String value) {
            addCriterion("waitinginstance_id =", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdNotEqualTo(String value) {
            addCriterion("waitinginstance_id <>", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdGreaterThan(String value) {
            addCriterion("waitinginstance_id >", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdGreaterThanOrEqualTo(String value) {
            addCriterion("waitinginstance_id >=", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdLessThan(String value) {
            addCriterion("waitinginstance_id <", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdLessThanOrEqualTo(String value) {
            addCriterion("waitinginstance_id <=", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdLike(String value) {
            addCriterion("waitinginstance_id like", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdNotLike(String value) {
            addCriterion("waitinginstance_id not like", value, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdIn(List<String> values) {
            addCriterion("waitinginstance_id in", values, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdNotIn(List<String> values) {
            addCriterion("waitinginstance_id not in", values, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdBetween(String value1, String value2) {
            addCriterion("waitinginstance_id between", value1, value2, "waitinginstanceId");
            return this;
        }

        public Criteria andWaitinginstanceIdNotBetween(String value1, String value2) {
            addCriterion("waitinginstance_id not between", value1, value2, "waitinginstanceId");
            return this;
        }

        public Criteria andKindIsNull() {
            addCriterion("kind is null");
            return this;
        }

        public Criteria andKindIsNotNull() {
            addCriterion("kind is not null");
            return this;
        }

        public Criteria andKindEqualTo(Short value) {
            addCriterion("kind =", value, "kind");
            return this;
        }

        public Criteria andKindNotEqualTo(Short value) {
            addCriterion("kind <>", value, "kind");
            return this;
        }

        public Criteria andKindGreaterThan(Short value) {
            addCriterion("kind >", value, "kind");
            return this;
        }

        public Criteria andKindGreaterThanOrEqualTo(Short value) {
            addCriterion("kind >=", value, "kind");
            return this;
        }

        public Criteria andKindLessThan(Short value) {
            addCriterion("kind <", value, "kind");
            return this;
        }

        public Criteria andKindLessThanOrEqualTo(Short value) {
            addCriterion("kind <=", value, "kind");
            return this;
        }

        public Criteria andKindIn(List<Short> values) {
            addCriterion("kind in", values, "kind");
            return this;
        }

        public Criteria andKindNotIn(List<Short> values) {
            addCriterion("kind not in", values, "kind");
            return this;
        }

        public Criteria andKindBetween(Short value1, Short value2) {
            addCriterion("kind between", value1, value2, "kind");
            return this;
        }

        public Criteria andKindNotBetween(Short value1, Short value2) {
            addCriterion("kind not between", value1, value2, "kind");
            return this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return this;
        }

        public Criteria andParentIdEqualTo(String value) {
            addCriterion("parent_id =", value, "parentId");
            return this;
        }

        public Criteria andParentIdNotEqualTo(String value) {
            addCriterion("parent_id <>", value, "parentId");
            return this;
        }

        public Criteria andParentIdGreaterThan(String value) {
            addCriterion("parent_id >", value, "parentId");
            return this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(String value) {
            addCriterion("parent_id >=", value, "parentId");
            return this;
        }

        public Criteria andParentIdLessThan(String value) {
            addCriterion("parent_id <", value, "parentId");
            return this;
        }

        public Criteria andParentIdLessThanOrEqualTo(String value) {
            addCriterion("parent_id <=", value, "parentId");
            return this;
        }

        public Criteria andParentIdLike(String value) {
            addCriterion("parent_id like", value, "parentId");
            return this;
        }

        public Criteria andParentIdNotLike(String value) {
            addCriterion("parent_id not like", value, "parentId");
            return this;
        }

        public Criteria andParentIdIn(List<String> values) {
            addCriterion("parent_id in", values, "parentId");
            return this;
        }

        public Criteria andParentIdNotIn(List<String> values) {
            addCriterion("parent_id not in", values, "parentId");
            return this;
        }

        public Criteria andParentIdBetween(String value1, String value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return this;
        }

        public Criteria andParentIdNotBetween(String value1, String value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
            return this;
        }

        public Criteria andPricemodeIsNull() {
            addCriterion("pricemode is null");
            return this;
        }

        public Criteria andPricemodeIsNotNull() {
            addCriterion("pricemode is not null");
            return this;
        }

        public Criteria andPricemodeEqualTo(Short value) {
            addCriterion("pricemode =", value, "pricemode");
            return this;
        }

        public Criteria andPricemodeNotEqualTo(Short value) {
            addCriterion("pricemode <>", value, "pricemode");
            return this;
        }

        public Criteria andPricemodeGreaterThan(Short value) {
            addCriterion("pricemode >", value, "pricemode");
            return this;
        }

        public Criteria andPricemodeGreaterThanOrEqualTo(Short value) {
            addCriterion("pricemode >=", value, "pricemode");
            return this;
        }

        public Criteria andPricemodeLessThan(Short value) {
            addCriterion("pricemode <", value, "pricemode");
            return this;
        }

        public Criteria andPricemodeLessThanOrEqualTo(Short value) {
            addCriterion("pricemode <=", value, "pricemode");
            return this;
        }

        public Criteria andPricemodeIn(List<Short> values) {
            addCriterion("pricemode in", values, "pricemode");
            return this;
        }

        public Criteria andPricemodeNotIn(List<Short> values) {
            addCriterion("pricemode not in", values, "pricemode");
            return this;
        }

        public Criteria andPricemodeBetween(Short value1, Short value2) {
            addCriterion("pricemode between", value1, value2, "pricemode");
            return this;
        }

        public Criteria andPricemodeNotBetween(Short value1, Short value2) {
            addCriterion("pricemode not between", value1, value2, "pricemode");
            return this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return this;
        }

        public Criteria andMakenameIsNull() {
            addCriterion("makename is null");
            return this;
        }

        public Criteria andMakenameIsNotNull() {
            addCriterion("makename is not null");
            return this;
        }

        public Criteria andMakenameEqualTo(String value) {
            addCriterion("makename =", value, "makename");
            return this;
        }

        public Criteria andMakenameNotEqualTo(String value) {
            addCriterion("makename <>", value, "makename");
            return this;
        }

        public Criteria andMakenameGreaterThan(String value) {
            addCriterion("makename >", value, "makename");
            return this;
        }

        public Criteria andMakenameGreaterThanOrEqualTo(String value) {
            addCriterion("makename >=", value, "makename");
            return this;
        }

        public Criteria andMakenameLessThan(String value) {
            addCriterion("makename <", value, "makename");
            return this;
        }

        public Criteria andMakenameLessThanOrEqualTo(String value) {
            addCriterion("makename <=", value, "makename");
            return this;
        }

        public Criteria andMakenameLike(String value) {
            addCriterion("makename like", value, "makename");
            return this;
        }

        public Criteria andMakenameNotLike(String value) {
            addCriterion("makename not like", value, "makename");
            return this;
        }

        public Criteria andMakenameIn(List<String> values) {
            addCriterion("makename in", values, "makename");
            return this;
        }

        public Criteria andMakenameNotIn(List<String> values) {
            addCriterion("makename not in", values, "makename");
            return this;
        }

        public Criteria andMakenameBetween(String value1, String value2) {
            addCriterion("makename between", value1, value2, "makename");
            return this;
        }

        public Criteria andMakenameNotBetween(String value1, String value2) {
            addCriterion("makename not between", value1, value2, "makename");
            return this;
        }

        public Criteria andTasteIsNull() {
            addCriterion("taste is null");
            return this;
        }

        public Criteria andTasteIsNotNull() {
            addCriterion("taste is not null");
            return this;
        }

        public Criteria andTasteEqualTo(String value) {
            addCriterion("taste =", value, "taste");
            return this;
        }

        public Criteria andTasteNotEqualTo(String value) {
            addCriterion("taste <>", value, "taste");
            return this;
        }

        public Criteria andTasteGreaterThan(String value) {
            addCriterion("taste >", value, "taste");
            return this;
        }

        public Criteria andTasteGreaterThanOrEqualTo(String value) {
            addCriterion("taste >=", value, "taste");
            return this;
        }

        public Criteria andTasteLessThan(String value) {
            addCriterion("taste <", value, "taste");
            return this;
        }

        public Criteria andTasteLessThanOrEqualTo(String value) {
            addCriterion("taste <=", value, "taste");
            return this;
        }

        public Criteria andTasteLike(String value) {
            addCriterion("taste like", value, "taste");
            return this;
        }

        public Criteria andTasteNotLike(String value) {
            addCriterion("taste not like", value, "taste");
            return this;
        }

        public Criteria andTasteIn(List<String> values) {
            addCriterion("taste in", values, "taste");
            return this;
        }

        public Criteria andTasteNotIn(List<String> values) {
            addCriterion("taste not in", values, "taste");
            return this;
        }

        public Criteria andTasteBetween(String value1, String value2) {
            addCriterion("taste between", value1, value2, "taste");
            return this;
        }

        public Criteria andTasteNotBetween(String value1, String value2) {
            addCriterion("taste not between", value1, value2, "taste");
            return this;
        }

        public Criteria andSpecDetailNameIsNull() {
            addCriterion("spec_detail_name is null");
            return this;
        }

        public Criteria andSpecDetailNameIsNotNull() {
            addCriterion("spec_detail_name is not null");
            return this;
        }

        public Criteria andSpecDetailNameEqualTo(String value) {
            addCriterion("spec_detail_name =", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameNotEqualTo(String value) {
            addCriterion("spec_detail_name <>", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameGreaterThan(String value) {
            addCriterion("spec_detail_name >", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameGreaterThanOrEqualTo(String value) {
            addCriterion("spec_detail_name >=", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameLessThan(String value) {
            addCriterion("spec_detail_name <", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameLessThanOrEqualTo(String value) {
            addCriterion("spec_detail_name <=", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameLike(String value) {
            addCriterion("spec_detail_name like", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameNotLike(String value) {
            addCriterion("spec_detail_name not like", value, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameIn(List<String> values) {
            addCriterion("spec_detail_name in", values, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameNotIn(List<String> values) {
            addCriterion("spec_detail_name not in", values, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameBetween(String value1, String value2) {
            addCriterion("spec_detail_name between", value1, value2, "specDetailName");
            return this;
        }

        public Criteria andSpecDetailNameNotBetween(String value1, String value2) {
            addCriterion("spec_detail_name not between", value1, value2, "specDetailName");
            return this;
        }

        public Criteria andNumIsNull() {
            addCriterion("num is null");
            return this;
        }

        public Criteria andNumIsNotNull() {
            addCriterion("num is not null");
            return this;
        }

        public Criteria andNumEqualTo(BigDecimal value) {
            addCriterion("num =", value, "num");
            return this;
        }

        public Criteria andNumNotEqualTo(BigDecimal value) {
            addCriterion("num <>", value, "num");
            return this;
        }

        public Criteria andNumGreaterThan(BigDecimal value) {
            addCriterion("num >", value, "num");
            return this;
        }

        public Criteria andNumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("num >=", value, "num");
            return this;
        }

        public Criteria andNumLessThan(BigDecimal value) {
            addCriterion("num <", value, "num");
            return this;
        }

        public Criteria andNumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("num <=", value, "num");
            return this;
        }

        public Criteria andNumIn(List<BigDecimal> values) {
            addCriterion("num in", values, "num");
            return this;
        }

        public Criteria andNumNotIn(List<BigDecimal> values) {
            addCriterion("num not in", values, "num");
            return this;
        }

        public Criteria andNumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("num between", value1, value2, "num");
            return this;
        }

        public Criteria andNumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("num not between", value1, value2, "num");
            return this;
        }

        public Criteria andAccountNumIsNull() {
            addCriterion("account_num is null");
            return this;
        }

        public Criteria andAccountNumIsNotNull() {
            addCriterion("account_num is not null");
            return this;
        }

        public Criteria andAccountNumEqualTo(BigDecimal value) {
            addCriterion("account_num =", value, "accountNum");
            return this;
        }

        public Criteria andAccountNumNotEqualTo(BigDecimal value) {
            addCriterion("account_num <>", value, "accountNum");
            return this;
        }

        public Criteria andAccountNumGreaterThan(BigDecimal value) {
            addCriterion("account_num >", value, "accountNum");
            return this;
        }

        public Criteria andAccountNumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("account_num >=", value, "accountNum");
            return this;
        }

        public Criteria andAccountNumLessThan(BigDecimal value) {
            addCriterion("account_num <", value, "accountNum");
            return this;
        }

        public Criteria andAccountNumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("account_num <=", value, "accountNum");
            return this;
        }

        public Criteria andAccountNumIn(List<BigDecimal> values) {
            addCriterion("account_num in", values, "accountNum");
            return this;
        }

        public Criteria andAccountNumNotIn(List<BigDecimal> values) {
            addCriterion("account_num not in", values, "accountNum");
            return this;
        }

        public Criteria andAccountNumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("account_num between", value1, value2, "accountNum");
            return this;
        }

        public Criteria andAccountNumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("account_num not between", value1, value2, "accountNum");
            return this;
        }

        public Criteria andUnitIsNull() {
            addCriterion("unit is null");
            return this;
        }

        public Criteria andUnitIsNotNull() {
            addCriterion("unit is not null");
            return this;
        }

        public Criteria andUnitEqualTo(String value) {
            addCriterion("unit =", value, "unit");
            return this;
        }

        public Criteria andUnitNotEqualTo(String value) {
            addCriterion("unit <>", value, "unit");
            return this;
        }

        public Criteria andUnitGreaterThan(String value) {
            addCriterion("unit >", value, "unit");
            return this;
        }

        public Criteria andUnitGreaterThanOrEqualTo(String value) {
            addCriterion("unit >=", value, "unit");
            return this;
        }

        public Criteria andUnitLessThan(String value) {
            addCriterion("unit <", value, "unit");
            return this;
        }

        public Criteria andUnitLessThanOrEqualTo(String value) {
            addCriterion("unit <=", value, "unit");
            return this;
        }

        public Criteria andUnitLike(String value) {
            addCriterion("unit like", value, "unit");
            return this;
        }

        public Criteria andUnitNotLike(String value) {
            addCriterion("unit not like", value, "unit");
            return this;
        }

        public Criteria andUnitIn(List<String> values) {
            addCriterion("unit in", values, "unit");
            return this;
        }

        public Criteria andUnitNotIn(List<String> values) {
            addCriterion("unit not in", values, "unit");
            return this;
        }

        public Criteria andUnitBetween(String value1, String value2) {
            addCriterion("unit between", value1, value2, "unit");
            return this;
        }

        public Criteria andUnitNotBetween(String value1, String value2) {
            addCriterion("unit not between", value1, value2, "unit");
            return this;
        }

        public Criteria andAccountUnitIsNull() {
            addCriterion("account_unit is null");
            return this;
        }

        public Criteria andAccountUnitIsNotNull() {
            addCriterion("account_unit is not null");
            return this;
        }

        public Criteria andAccountUnitEqualTo(String value) {
            addCriterion("account_unit =", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitNotEqualTo(String value) {
            addCriterion("account_unit <>", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitGreaterThan(String value) {
            addCriterion("account_unit >", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitGreaterThanOrEqualTo(String value) {
            addCriterion("account_unit >=", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitLessThan(String value) {
            addCriterion("account_unit <", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitLessThanOrEqualTo(String value) {
            addCriterion("account_unit <=", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitLike(String value) {
            addCriterion("account_unit like", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitNotLike(String value) {
            addCriterion("account_unit not like", value, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitIn(List<String> values) {
            addCriterion("account_unit in", values, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitNotIn(List<String> values) {
            addCriterion("account_unit not in", values, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitBetween(String value1, String value2) {
            addCriterion("account_unit between", value1, value2, "accountUnit");
            return this;
        }

        public Criteria andAccountUnitNotBetween(String value1, String value2) {
            addCriterion("account_unit not between", value1, value2, "accountUnit");
            return this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return this;
        }

        public Criteria andPriceEqualTo(BigDecimal value) {
            addCriterion("price =", value, "price");
            return this;
        }

        public Criteria andPriceNotEqualTo(BigDecimal value) {
            addCriterion("price <>", value, "price");
            return this;
        }

        public Criteria andPriceGreaterThan(BigDecimal value) {
            addCriterion("price >", value, "price");
            return this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price >=", value, "price");
            return this;
        }

        public Criteria andPriceLessThan(BigDecimal value) {
            addCriterion("price <", value, "price");
            return this;
        }

        public Criteria andPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price <=", value, "price");
            return this;
        }

        public Criteria andPriceIn(List<BigDecimal> values) {
            addCriterion("price in", values, "price");
            return this;
        }

        public Criteria andPriceNotIn(List<BigDecimal> values) {
            addCriterion("price not in", values, "price");
            return this;
        }

        public Criteria andPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price between", value1, value2, "price");
            return this;
        }

        public Criteria andPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price not between", value1, value2, "price");
            return this;
        }

        public Criteria andMemberPriceIsNull() {
            addCriterion("member_price is null");
            return this;
        }

        public Criteria andMemberPriceIsNotNull() {
            addCriterion("member_price is not null");
            return this;
        }

        public Criteria andMemberPriceEqualTo(BigDecimal value) {
            addCriterion("member_price =", value, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceNotEqualTo(BigDecimal value) {
            addCriterion("member_price <>", value, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceGreaterThan(BigDecimal value) {
            addCriterion("member_price >", value, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("member_price >=", value, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceLessThan(BigDecimal value) {
            addCriterion("member_price <", value, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("member_price <=", value, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceIn(List<BigDecimal> values) {
            addCriterion("member_price in", values, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceNotIn(List<BigDecimal> values) {
            addCriterion("member_price not in", values, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("member_price between", value1, value2, "memberPrice");
            return this;
        }

        public Criteria andMemberPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("member_price not between", value1, value2, "memberPrice");
            return this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("fee is null");
            return this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("fee is not null");
            return this;
        }

        public Criteria andFeeEqualTo(BigDecimal value) {
            addCriterion("fee =", value, "fee");
            return this;
        }

        public Criteria andFeeNotEqualTo(BigDecimal value) {
            addCriterion("fee <>", value, "fee");
            return this;
        }

        public Criteria andFeeGreaterThan(BigDecimal value) {
            addCriterion("fee >", value, "fee");
            return this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fee >=", value, "fee");
            return this;
        }

        public Criteria andFeeLessThan(BigDecimal value) {
            addCriterion("fee <", value, "fee");
            return this;
        }

        public Criteria andFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fee <=", value, "fee");
            return this;
        }

        public Criteria andFeeIn(List<BigDecimal> values) {
            addCriterion("fee in", values, "fee");
            return this;
        }

        public Criteria andFeeNotIn(List<BigDecimal> values) {
            addCriterion("fee not in", values, "fee");
            return this;
        }

        public Criteria andFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee between", value1, value2, "fee");
            return this;
        }

        public Criteria andFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee not between", value1, value2, "fee");
            return this;
        }

        public Criteria andRatioIsNull() {
            addCriterion("ratio is null");
            return this;
        }

        public Criteria andRatioIsNotNull() {
            addCriterion("ratio is not null");
            return this;
        }

        public Criteria andRatioEqualTo(BigDecimal value) {
            addCriterion("ratio =", value, "ratio");
            return this;
        }

        public Criteria andRatioNotEqualTo(BigDecimal value) {
            addCriterion("ratio <>", value, "ratio");
            return this;
        }

        public Criteria andRatioGreaterThan(BigDecimal value) {
            addCriterion("ratio >", value, "ratio");
            return this;
        }

        public Criteria andRatioGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ratio >=", value, "ratio");
            return this;
        }

        public Criteria andRatioLessThan(BigDecimal value) {
            addCriterion("ratio <", value, "ratio");
            return this;
        }

        public Criteria andRatioLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ratio <=", value, "ratio");
            return this;
        }

        public Criteria andRatioIn(List<BigDecimal> values) {
            addCriterion("ratio in", values, "ratio");
            return this;
        }

        public Criteria andRatioNotIn(List<BigDecimal> values) {
            addCriterion("ratio not in", values, "ratio");
            return this;
        }

        public Criteria andRatioBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ratio between", value1, value2, "ratio");
            return this;
        }

        public Criteria andRatioNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ratio not between", value1, value2, "ratio");
            return this;
        }

        public Criteria andRatioFeeIsNull() {
            addCriterion("ratio_fee is null");
            return this;
        }

        public Criteria andRatioFeeIsNotNull() {
            addCriterion("ratio_fee is not null");
            return this;
        }

        public Criteria andRatioFeeEqualTo(BigDecimal value) {
            addCriterion("ratio_fee =", value, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeNotEqualTo(BigDecimal value) {
            addCriterion("ratio_fee <>", value, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeGreaterThan(BigDecimal value) {
            addCriterion("ratio_fee >", value, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ratio_fee >=", value, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeLessThan(BigDecimal value) {
            addCriterion("ratio_fee <", value, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ratio_fee <=", value, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeIn(List<BigDecimal> values) {
            addCriterion("ratio_fee in", values, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeNotIn(List<BigDecimal> values) {
            addCriterion("ratio_fee not in", values, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ratio_fee between", value1, value2, "ratioFee");
            return this;
        }

        public Criteria andRatioFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ratio_fee not between", value1, value2, "ratioFee");
            return this;
        }

        public Criteria andRatioCauseIsNull() {
            addCriterion("ratio_cause is null");
            return this;
        }

        public Criteria andRatioCauseIsNotNull() {
            addCriterion("ratio_cause is not null");
            return this;
        }

        public Criteria andRatioCauseEqualTo(String value) {
            addCriterion("ratio_cause =", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseNotEqualTo(String value) {
            addCriterion("ratio_cause <>", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseGreaterThan(String value) {
            addCriterion("ratio_cause >", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseGreaterThanOrEqualTo(String value) {
            addCriterion("ratio_cause >=", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseLessThan(String value) {
            addCriterion("ratio_cause <", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseLessThanOrEqualTo(String value) {
            addCriterion("ratio_cause <=", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseLike(String value) {
            addCriterion("ratio_cause like", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseNotLike(String value) {
            addCriterion("ratio_cause not like", value, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseIn(List<String> values) {
            addCriterion("ratio_cause in", values, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseNotIn(List<String> values) {
            addCriterion("ratio_cause not in", values, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseBetween(String value1, String value2) {
            addCriterion("ratio_cause between", value1, value2, "ratioCause");
            return this;
        }

        public Criteria andRatioCauseNotBetween(String value1, String value2) {
            addCriterion("ratio_cause not between", value1, value2, "ratioCause");
            return this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return this;
        }

        public Criteria andStatusEqualTo(Short value) {
            addCriterion("status =", value, "status");
            return this;
        }

        public Criteria andStatusNotEqualTo(Short value) {
            addCriterion("status <>", value, "status");
            return this;
        }

        public Criteria andStatusGreaterThan(Short value) {
            addCriterion("status >", value, "status");
            return this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("status >=", value, "status");
            return this;
        }

        public Criteria andStatusLessThan(Short value) {
            addCriterion("status <", value, "status");
            return this;
        }

        public Criteria andStatusLessThanOrEqualTo(Short value) {
            addCriterion("status <=", value, "status");
            return this;
        }

        public Criteria andStatusIn(List<Short> values) {
            addCriterion("status in", values, "status");
            return this;
        }

        public Criteria andStatusNotIn(List<Short> values) {
            addCriterion("status not in", values, "status");
            return this;
        }

        public Criteria andStatusBetween(Short value1, Short value2) {
            addCriterion("status between", value1, value2, "status");
            return this;
        }

        public Criteria andStatusNotBetween(Short value1, Short value2) {
            addCriterion("status not between", value1, value2, "status");
            return this;
        }

        public Criteria andKindmenuIdIsNull() {
            addCriterion("kindmenu_id is null");
            return this;
        }

        public Criteria andKindmenuIdIsNotNull() {
            addCriterion("kindmenu_id is not null");
            return this;
        }

        public Criteria andKindmenuIdEqualTo(String value) {
            addCriterion("kindmenu_id =", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdNotEqualTo(String value) {
            addCriterion("kindmenu_id <>", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdGreaterThan(String value) {
            addCriterion("kindmenu_id >", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdGreaterThanOrEqualTo(String value) {
            addCriterion("kindmenu_id >=", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdLessThan(String value) {
            addCriterion("kindmenu_id <", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdLessThanOrEqualTo(String value) {
            addCriterion("kindmenu_id <=", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdLike(String value) {
            addCriterion("kindmenu_id like", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdNotLike(String value) {
            addCriterion("kindmenu_id not like", value, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdIn(List<String> values) {
            addCriterion("kindmenu_id in", values, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdNotIn(List<String> values) {
            addCriterion("kindmenu_id not in", values, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdBetween(String value1, String value2) {
            addCriterion("kindmenu_id between", value1, value2, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuIdNotBetween(String value1, String value2) {
            addCriterion("kindmenu_id not between", value1, value2, "kindmenuId");
            return this;
        }

        public Criteria andKindmenuNameIsNull() {
            addCriterion("kindmenu_name is null");
            return this;
        }

        public Criteria andKindmenuNameIsNotNull() {
            addCriterion("kindmenu_name is not null");
            return this;
        }

        public Criteria andKindmenuNameEqualTo(String value) {
            addCriterion("kindmenu_name =", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameNotEqualTo(String value) {
            addCriterion("kindmenu_name <>", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameGreaterThan(String value) {
            addCriterion("kindmenu_name >", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameGreaterThanOrEqualTo(String value) {
            addCriterion("kindmenu_name >=", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameLessThan(String value) {
            addCriterion("kindmenu_name <", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameLessThanOrEqualTo(String value) {
            addCriterion("kindmenu_name <=", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameLike(String value) {
            addCriterion("kindmenu_name like", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameNotLike(String value) {
            addCriterion("kindmenu_name not like", value, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameIn(List<String> values) {
            addCriterion("kindmenu_name in", values, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameNotIn(List<String> values) {
            addCriterion("kindmenu_name not in", values, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameBetween(String value1, String value2) {
            addCriterion("kindmenu_name between", value1, value2, "kindmenuName");
            return this;
        }

        public Criteria andKindmenuNameNotBetween(String value1, String value2) {
            addCriterion("kindmenu_name not between", value1, value2, "kindmenuName");
            return this;
        }

        public Criteria andMenuIdIsNull() {
            addCriterion("menu_id is null");
            return this;
        }

        public Criteria andMenuIdIsNotNull() {
            addCriterion("menu_id is not null");
            return this;
        }

        public Criteria andMenuIdEqualTo(String value) {
            addCriterion("menu_id =", value, "menuId");
            return this;
        }

        public Criteria andMenuIdNotEqualTo(String value) {
            addCriterion("menu_id <>", value, "menuId");
            return this;
        }

        public Criteria andMenuIdGreaterThan(String value) {
            addCriterion("menu_id >", value, "menuId");
            return this;
        }

        public Criteria andMenuIdGreaterThanOrEqualTo(String value) {
            addCriterion("menu_id >=", value, "menuId");
            return this;
        }

        public Criteria andMenuIdLessThan(String value) {
            addCriterion("menu_id <", value, "menuId");
            return this;
        }

        public Criteria andMenuIdLessThanOrEqualTo(String value) {
            addCriterion("menu_id <=", value, "menuId");
            return this;
        }

        public Criteria andMenuIdLike(String value) {
            addCriterion("menu_id like", value, "menuId");
            return this;
        }

        public Criteria andMenuIdNotLike(String value) {
            addCriterion("menu_id not like", value, "menuId");
            return this;
        }

        public Criteria andMenuIdIn(List<String> values) {
            addCriterion("menu_id in", values, "menuId");
            return this;
        }

        public Criteria andMenuIdNotIn(List<String> values) {
            addCriterion("menu_id not in", values, "menuId");
            return this;
        }

        public Criteria andMenuIdBetween(String value1, String value2) {
            addCriterion("menu_id between", value1, value2, "menuId");
            return this;
        }

        public Criteria andMenuIdNotBetween(String value1, String value2) {
            addCriterion("menu_id not between", value1, value2, "menuId");
            return this;
        }

        public Criteria andMemoIsNull() {
            addCriterion("memo is null");
            return this;
        }

        public Criteria andMemoIsNotNull() {
            addCriterion("memo is not null");
            return this;
        }

        public Criteria andMemoEqualTo(String value) {
            addCriterion("memo =", value, "memo");
            return this;
        }

        public Criteria andMemoNotEqualTo(String value) {
            addCriterion("memo <>", value, "memo");
            return this;
        }

        public Criteria andMemoGreaterThan(String value) {
            addCriterion("memo >", value, "memo");
            return this;
        }

        public Criteria andMemoGreaterThanOrEqualTo(String value) {
            addCriterion("memo >=", value, "memo");
            return this;
        }

        public Criteria andMemoLessThan(String value) {
            addCriterion("memo <", value, "memo");
            return this;
        }

        public Criteria andMemoLessThanOrEqualTo(String value) {
            addCriterion("memo <=", value, "memo");
            return this;
        }

        public Criteria andMemoLike(String value) {
            addCriterion("memo like", value, "memo");
            return this;
        }

        public Criteria andMemoNotLike(String value) {
            addCriterion("memo not like", value, "memo");
            return this;
        }

        public Criteria andMemoIn(List<String> values) {
            addCriterion("memo in", values, "memo");
            return this;
        }

        public Criteria andMemoNotIn(List<String> values) {
            addCriterion("memo not in", values, "memo");
            return this;
        }

        public Criteria andMemoBetween(String value1, String value2) {
            addCriterion("memo between", value1, value2, "memo");
            return this;
        }

        public Criteria andMemoNotBetween(String value1, String value2) {
            addCriterion("memo not between", value1, value2, "memo");
            return this;
        }

        public Criteria andIsRatioIsNull() {
            addCriterion("is_ratio is null");
            return this;
        }

        public Criteria andIsRatioIsNotNull() {
            addCriterion("is_ratio is not null");
            return this;
        }

        public Criteria andIsRatioEqualTo(Short value) {
            addCriterion("is_ratio =", value, "isRatio");
            return this;
        }

        public Criteria andIsRatioNotEqualTo(Short value) {
            addCriterion("is_ratio <>", value, "isRatio");
            return this;
        }

        public Criteria andIsRatioGreaterThan(Short value) {
            addCriterion("is_ratio >", value, "isRatio");
            return this;
        }

        public Criteria andIsRatioGreaterThanOrEqualTo(Short value) {
            addCriterion("is_ratio >=", value, "isRatio");
            return this;
        }

        public Criteria andIsRatioLessThan(Short value) {
            addCriterion("is_ratio <", value, "isRatio");
            return this;
        }

        public Criteria andIsRatioLessThanOrEqualTo(Short value) {
            addCriterion("is_ratio <=", value, "isRatio");
            return this;
        }

        public Criteria andIsRatioIn(List<Short> values) {
            addCriterion("is_ratio in", values, "isRatio");
            return this;
        }

        public Criteria andIsRatioNotIn(List<Short> values) {
            addCriterion("is_ratio not in", values, "isRatio");
            return this;
        }

        public Criteria andIsRatioBetween(Short value1, Short value2) {
            addCriterion("is_ratio between", value1, value2, "isRatio");
            return this;
        }

        public Criteria andIsRatioNotBetween(Short value1, Short value2) {
            addCriterion("is_ratio not between", value1, value2, "isRatio");
            return this;
        }

        public Criteria andEntityIdIsNull() {
            addCriterion("entity_id is null");
            return this;
        }

        public Criteria andEntityIdIsNotNull() {
            addCriterion("entity_id is not null");
            return this;
        }

        public Criteria andEntityIdEqualTo(String value) {
            addCriterion("entity_id =", value, "entityId");
            return this;
        }

        public Criteria andEntityIdNotEqualTo(String value) {
            addCriterion("entity_id <>", value, "entityId");
            return this;
        }

        public Criteria andEntityIdGreaterThan(String value) {
            addCriterion("entity_id >", value, "entityId");
            return this;
        }

        public Criteria andEntityIdGreaterThanOrEqualTo(String value) {
            addCriterion("entity_id >=", value, "entityId");
            return this;
        }

        public Criteria andEntityIdLessThan(String value) {
            addCriterion("entity_id <", value, "entityId");
            return this;
        }

        public Criteria andEntityIdLessThanOrEqualTo(String value) {
            addCriterion("entity_id <=", value, "entityId");
            return this;
        }

        public Criteria andEntityIdLike(String value) {
            addCriterion("entity_id like", value, "entityId");
            return this;
        }

        public Criteria andEntityIdNotLike(String value) {
            addCriterion("entity_id not like", value, "entityId");
            return this;
        }

        public Criteria andEntityIdIn(List<String> values) {
            addCriterion("entity_id in", values, "entityId");
            return this;
        }

        public Criteria andEntityIdNotIn(List<String> values) {
            addCriterion("entity_id not in", values, "entityId");
            return this;
        }

        public Criteria andEntityIdBetween(String value1, String value2) {
            addCriterion("entity_id between", value1, value2, "entityId");
            return this;
        }

        public Criteria andEntityIdNotBetween(String value1, String value2) {
            addCriterion("entity_id not between", value1, value2, "entityId");
            return this;
        }

        public Criteria andIsValidIsNull() {
            addCriterion("is_valid is null");
            return this;
        }

        public Criteria andIsValidIsNotNull() {
            addCriterion("is_valid is not null");
            return this;
        }

        public Criteria andIsValidEqualTo(Short value) {
            addCriterion("is_valid =", value, "isValid");
            return this;
        }

        public Criteria andIsValidNotEqualTo(Short value) {
            addCriterion("is_valid <>", value, "isValid");
            return this;
        }

        public Criteria andIsValidGreaterThan(Short value) {
            addCriterion("is_valid >", value, "isValid");
            return this;
        }

        public Criteria andIsValidGreaterThanOrEqualTo(Short value) {
            addCriterion("is_valid >=", value, "isValid");
            return this;
        }

        public Criteria andIsValidLessThan(Short value) {
            addCriterion("is_valid <", value, "isValid");
            return this;
        }

        public Criteria andIsValidLessThanOrEqualTo(Short value) {
            addCriterion("is_valid <=", value, "isValid");
            return this;
        }

        public Criteria andIsValidIn(List<Short> values) {
            addCriterion("is_valid in", values, "isValid");
            return this;
        }

        public Criteria andIsValidNotIn(List<Short> values) {
            addCriterion("is_valid not in", values, "isValid");
            return this;
        }

        public Criteria andIsValidBetween(Short value1, Short value2) {
            addCriterion("is_valid between", value1, value2, "isValid");
            return this;
        }

        public Criteria andIsValidNotBetween(Short value1, Short value2) {
            addCriterion("is_valid not between", value1, value2, "isValid");
            return this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return this;
        }

        public Criteria andCreateTimeEqualTo(Long value) {
            addCriterion("create_time =", value, "createTime");
            return this;
        }

        public Criteria andCreateTimeNotEqualTo(Long value) {
            addCriterion("create_time <>", value, "createTime");
            return this;
        }

        public Criteria andCreateTimeGreaterThan(Long value) {
            addCriterion("create_time >", value, "createTime");
            return this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("create_time >=", value, "createTime");
            return this;
        }

        public Criteria andCreateTimeLessThan(Long value) {
            addCriterion("create_time <", value, "createTime");
            return this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Long value) {
            addCriterion("create_time <=", value, "createTime");
            return this;
        }

        public Criteria andCreateTimeIn(List<Long> values) {
            addCriterion("create_time in", values, "createTime");
            return this;
        }

        public Criteria andCreateTimeNotIn(List<Long> values) {
            addCriterion("create_time not in", values, "createTime");
            return this;
        }

        public Criteria andCreateTimeBetween(Long value1, Long value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return this;
        }

        public Criteria andCreateTimeNotBetween(Long value1, Long value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return this;
        }

        public Criteria andOpTimeIsNull() {
            addCriterion("op_time is null");
            return this;
        }

        public Criteria andOpTimeIsNotNull() {
            addCriterion("op_time is not null");
            return this;
        }

        public Criteria andOpTimeEqualTo(Long value) {
            addCriterion("op_time =", value, "opTime");
            return this;
        }

        public Criteria andOpTimeNotEqualTo(Long value) {
            addCriterion("op_time <>", value, "opTime");
            return this;
        }

        public Criteria andOpTimeGreaterThan(Long value) {
            addCriterion("op_time >", value, "opTime");
            return this;
        }

        public Criteria andOpTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("op_time >=", value, "opTime");
            return this;
        }

        public Criteria andOpTimeLessThan(Long value) {
            addCriterion("op_time <", value, "opTime");
            return this;
        }

        public Criteria andOpTimeLessThanOrEqualTo(Long value) {
            addCriterion("op_time <=", value, "opTime");
            return this;
        }

        public Criteria andOpTimeIn(List<Long> values) {
            addCriterion("op_time in", values, "opTime");
            return this;
        }

        public Criteria andOpTimeNotIn(List<Long> values) {
            addCriterion("op_time not in", values, "opTime");
            return this;
        }

        public Criteria andOpTimeBetween(Long value1, Long value2) {
            addCriterion("op_time between", value1, value2, "opTime");
            return this;
        }

        public Criteria andOpTimeNotBetween(Long value1, Long value2) {
            addCriterion("op_time not between", value1, value2, "opTime");
            return this;
        }

        public Criteria andLastVerIsNull() {
            addCriterion("last_ver is null");
            return this;
        }

        public Criteria andLastVerIsNotNull() {
            addCriterion("last_ver is not null");
            return this;
        }

        public Criteria andLastVerEqualTo(Long value) {
            addCriterion("last_ver =", value, "lastVer");
            return this;
        }

        public Criteria andLastVerNotEqualTo(Long value) {
            addCriterion("last_ver <>", value, "lastVer");
            return this;
        }

        public Criteria andLastVerGreaterThan(Long value) {
            addCriterion("last_ver >", value, "lastVer");
            return this;
        }

        public Criteria andLastVerGreaterThanOrEqualTo(Long value) {
            addCriterion("last_ver >=", value, "lastVer");
            return this;
        }

        public Criteria andLastVerLessThan(Long value) {
            addCriterion("last_ver <", value, "lastVer");
            return this;
        }

        public Criteria andLastVerLessThanOrEqualTo(Long value) {
            addCriterion("last_ver <=", value, "lastVer");
            return this;
        }

        public Criteria andLastVerIn(List<Long> values) {
            addCriterion("last_ver in", values, "lastVer");
            return this;
        }

        public Criteria andLastVerNotIn(List<Long> values) {
            addCriterion("last_ver not in", values, "lastVer");
            return this;
        }

        public Criteria andLastVerBetween(Long value1, Long value2) {
            addCriterion("last_ver between", value1, value2, "lastVer");
            return this;
        }

        public Criteria andLastVerNotBetween(Long value1, Long value2) {
            addCriterion("last_ver not between", value1, value2, "lastVer");
            return this;
        }

        public Criteria andLoadTimeIsNull() {
            addCriterion("load_time is null");
            return this;
        }

        public Criteria andLoadTimeIsNotNull() {
            addCriterion("load_time is not null");
            return this;
        }

        public Criteria andLoadTimeEqualTo(Integer value) {
            addCriterion("load_time =", value, "loadTime");
            return this;
        }

        public Criteria andLoadTimeNotEqualTo(Integer value) {
            addCriterion("load_time <>", value, "loadTime");
            return this;
        }

        public Criteria andLoadTimeGreaterThan(Integer value) {
            addCriterion("load_time >", value, "loadTime");
            return this;
        }

        public Criteria andLoadTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("load_time >=", value, "loadTime");
            return this;
        }

        public Criteria andLoadTimeLessThan(Integer value) {
            addCriterion("load_time <", value, "loadTime");
            return this;
        }

        public Criteria andLoadTimeLessThanOrEqualTo(Integer value) {
            addCriterion("load_time <=", value, "loadTime");
            return this;
        }

        public Criteria andLoadTimeIn(List<Integer> values) {
            addCriterion("load_time in", values, "loadTime");
            return this;
        }

        public Criteria andLoadTimeNotIn(List<Integer> values) {
            addCriterion("load_time not in", values, "loadTime");
            return this;
        }

        public Criteria andLoadTimeBetween(Integer value1, Integer value2) {
            addCriterion("load_time between", value1, value2, "loadTime");
            return this;
        }

        public Criteria andLoadTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("load_time not between", value1, value2, "loadTime");
            return this;
        }

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return this;
        }

        public Criteria andModifyTimeEqualTo(Integer value) {
            addCriterion("modify_time =", value, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeNotEqualTo(Integer value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeGreaterThan(Integer value) {
            addCriterion("modify_time >", value, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeLessThan(Integer value) {
            addCriterion("modify_time <", value, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Integer value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeIn(List<Integer> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeNotIn(List<Integer> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeBetween(Integer value1, Integer value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return this;
        }

        public Criteria andModifyTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
            return this;
        }

        public Criteria andDrawStatusIsNull() {
            addCriterion("draw_status is null");
            return this;
        }

        public Criteria andDrawStatusIsNotNull() {
            addCriterion("draw_status is not null");
            return this;
        }

        public Criteria andDrawStatusEqualTo(Byte value) {
            addCriterion("draw_status =", value, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusNotEqualTo(Byte value) {
            addCriterion("draw_status <>", value, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusGreaterThan(Byte value) {
            addCriterion("draw_status >", value, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("draw_status >=", value, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusLessThan(Byte value) {
            addCriterion("draw_status <", value, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusLessThanOrEqualTo(Byte value) {
            addCriterion("draw_status <=", value, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusIn(List<Byte> values) {
            addCriterion("draw_status in", values, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusNotIn(List<Byte> values) {
            addCriterion("draw_status not in", values, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusBetween(Byte value1, Byte value2) {
            addCriterion("draw_status between", value1, value2, "drawStatus");
            return this;
        }

        public Criteria andDrawStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("draw_status not between", value1, value2, "drawStatus");
            return this;
        }

        public Criteria andBookmenuIdIsNull() {
            addCriterion("bookmenu_id is null");
            return this;
        }

        public Criteria andBookmenuIdIsNotNull() {
            addCriterion("bookmenu_id is not null");
            return this;
        }

        public Criteria andBookmenuIdEqualTo(String value) {
            addCriterion("bookmenu_id =", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdNotEqualTo(String value) {
            addCriterion("bookmenu_id <>", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdGreaterThan(String value) {
            addCriterion("bookmenu_id >", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdGreaterThanOrEqualTo(String value) {
            addCriterion("bookmenu_id >=", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdLessThan(String value) {
            addCriterion("bookmenu_id <", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdLessThanOrEqualTo(String value) {
            addCriterion("bookmenu_id <=", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdLike(String value) {
            addCriterion("bookmenu_id like", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdNotLike(String value) {
            addCriterion("bookmenu_id not like", value, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdIn(List<String> values) {
            addCriterion("bookmenu_id in", values, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdNotIn(List<String> values) {
            addCriterion("bookmenu_id not in", values, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdBetween(String value1, String value2) {
            addCriterion("bookmenu_id between", value1, value2, "bookmenuId");
            return this;
        }

        public Criteria andBookmenuIdNotBetween(String value1, String value2) {
            addCriterion("bookmenu_id not between", value1, value2, "bookmenuId");
            return this;
        }

        public Criteria andMakeIdIsNull() {
            addCriterion("make_id is null");
            return this;
        }

        public Criteria andMakeIdIsNotNull() {
            addCriterion("make_id is not null");
            return this;
        }

        public Criteria andMakeIdEqualTo(String value) {
            addCriterion("make_id =", value, "makeId");
            return this;
        }

        public Criteria andMakeIdNotEqualTo(String value) {
            addCriterion("make_id <>", value, "makeId");
            return this;
        }

        public Criteria andMakeIdGreaterThan(String value) {
            addCriterion("make_id >", value, "makeId");
            return this;
        }

        public Criteria andMakeIdGreaterThanOrEqualTo(String value) {
            addCriterion("make_id >=", value, "makeId");
            return this;
        }

        public Criteria andMakeIdLessThan(String value) {
            addCriterion("make_id <", value, "makeId");
            return this;
        }

        public Criteria andMakeIdLessThanOrEqualTo(String value) {
            addCriterion("make_id <=", value, "makeId");
            return this;
        }

        public Criteria andMakeIdLike(String value) {
            addCriterion("make_id like", value, "makeId");
            return this;
        }

        public Criteria andMakeIdNotLike(String value) {
            addCriterion("make_id not like", value, "makeId");
            return this;
        }

        public Criteria andMakeIdIn(List<String> values) {
            addCriterion("make_id in", values, "makeId");
            return this;
        }

        public Criteria andMakeIdNotIn(List<String> values) {
            addCriterion("make_id not in", values, "makeId");
            return this;
        }

        public Criteria andMakeIdBetween(String value1, String value2) {
            addCriterion("make_id between", value1, value2, "makeId");
            return this;
        }

        public Criteria andMakeIdNotBetween(String value1, String value2) {
            addCriterion("make_id not between", value1, value2, "makeId");
            return this;
        }

        public Criteria andMakePriceIsNull() {
            addCriterion("make_price is null");
            return this;
        }

        public Criteria andMakePriceIsNotNull() {
            addCriterion("make_price is not null");
            return this;
        }

        public Criteria andMakePriceEqualTo(BigDecimal value) {
            addCriterion("make_price =", value, "makePrice");
            return this;
        }

        public Criteria andMakePriceNotEqualTo(BigDecimal value) {
            addCriterion("make_price <>", value, "makePrice");
            return this;
        }

        public Criteria andMakePriceGreaterThan(BigDecimal value) {
            addCriterion("make_price >", value, "makePrice");
            return this;
        }

        public Criteria andMakePriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("make_price >=", value, "makePrice");
            return this;
        }

        public Criteria andMakePriceLessThan(BigDecimal value) {
            addCriterion("make_price <", value, "makePrice");
            return this;
        }

        public Criteria andMakePriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("make_price <=", value, "makePrice");
            return this;
        }

        public Criteria andMakePriceIn(List<BigDecimal> values) {
            addCriterion("make_price in", values, "makePrice");
            return this;
        }

        public Criteria andMakePriceNotIn(List<BigDecimal> values) {
            addCriterion("make_price not in", values, "makePrice");
            return this;
        }

        public Criteria andMakePriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("make_price between", value1, value2, "makePrice");
            return this;
        }

        public Criteria andMakePriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("make_price not between", value1, value2, "makePrice");
            return this;
        }

        public Criteria andProdplanIdIsNull() {
            addCriterion("prodplan_id is null");
            return this;
        }

        public Criteria andProdplanIdIsNotNull() {
            addCriterion("prodplan_id is not null");
            return this;
        }

        public Criteria andProdplanIdEqualTo(String value) {
            addCriterion("prodplan_id =", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdNotEqualTo(String value) {
            addCriterion("prodplan_id <>", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdGreaterThan(String value) {
            addCriterion("prodplan_id >", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdGreaterThanOrEqualTo(String value) {
            addCriterion("prodplan_id >=", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdLessThan(String value) {
            addCriterion("prodplan_id <", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdLessThanOrEqualTo(String value) {
            addCriterion("prodplan_id <=", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdLike(String value) {
            addCriterion("prodplan_id like", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdNotLike(String value) {
            addCriterion("prodplan_id not like", value, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdIn(List<String> values) {
            addCriterion("prodplan_id in", values, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdNotIn(List<String> values) {
            addCriterion("prodplan_id not in", values, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdBetween(String value1, String value2) {
            addCriterion("prodplan_id between", value1, value2, "prodplanId");
            return this;
        }

        public Criteria andProdplanIdNotBetween(String value1, String value2) {
            addCriterion("prodplan_id not between", value1, value2, "prodplanId");
            return this;
        }

        public Criteria andIsWaitIsNull() {
            addCriterion("is_wait is null");
            return this;
        }

        public Criteria andIsWaitIsNotNull() {
            addCriterion("is_wait is not null");
            return this;
        }

        public Criteria andIsWaitEqualTo(Byte value) {
            addCriterion("is_wait =", value, "isWait");
            return this;
        }

        public Criteria andIsWaitNotEqualTo(Byte value) {
            addCriterion("is_wait <>", value, "isWait");
            return this;
        }

        public Criteria andIsWaitGreaterThan(Byte value) {
            addCriterion("is_wait >", value, "isWait");
            return this;
        }

        public Criteria andIsWaitGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_wait >=", value, "isWait");
            return this;
        }

        public Criteria andIsWaitLessThan(Byte value) {
            addCriterion("is_wait <", value, "isWait");
            return this;
        }

        public Criteria andIsWaitLessThanOrEqualTo(Byte value) {
            addCriterion("is_wait <=", value, "isWait");
            return this;
        }

        public Criteria andIsWaitIn(List<Byte> values) {
            addCriterion("is_wait in", values, "isWait");
            return this;
        }

        public Criteria andIsWaitNotIn(List<Byte> values) {
            addCriterion("is_wait not in", values, "isWait");
            return this;
        }

        public Criteria andIsWaitBetween(Byte value1, Byte value2) {
            addCriterion("is_wait between", value1, value2, "isWait");
            return this;
        }

        public Criteria andIsWaitNotBetween(Byte value1, Byte value2) {
            addCriterion("is_wait not between", value1, value2, "isWait");
            return this;
        }

        public Criteria andSpecdetailIdIsNull() {
            addCriterion("specdetail_id is null");
            return this;
        }

        public Criteria andSpecdetailIdIsNotNull() {
            addCriterion("specdetail_id is not null");
            return this;
        }

        public Criteria andSpecdetailIdEqualTo(String value) {
            addCriterion("specdetail_id =", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdNotEqualTo(String value) {
            addCriterion("specdetail_id <>", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdGreaterThan(String value) {
            addCriterion("specdetail_id >", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdGreaterThanOrEqualTo(String value) {
            addCriterion("specdetail_id >=", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdLessThan(String value) {
            addCriterion("specdetail_id <", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdLessThanOrEqualTo(String value) {
            addCriterion("specdetail_id <=", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdLike(String value) {
            addCriterion("specdetail_id like", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdNotLike(String value) {
            addCriterion("specdetail_id not like", value, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdIn(List<String> values) {
            addCriterion("specdetail_id in", values, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdNotIn(List<String> values) {
            addCriterion("specdetail_id not in", values, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdBetween(String value1, String value2) {
            addCriterion("specdetail_id between", value1, value2, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailIdNotBetween(String value1, String value2) {
            addCriterion("specdetail_id not between", value1, value2, "specdetailId");
            return this;
        }

        public Criteria andSpecdetailPriceIsNull() {
            addCriterion("specdetail_price is null");
            return this;
        }

        public Criteria andSpecdetailPriceIsNotNull() {
            addCriterion("specdetail_price is not null");
            return this;
        }

        public Criteria andSpecdetailPriceEqualTo(BigDecimal value) {
            addCriterion("specdetail_price =", value, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceNotEqualTo(BigDecimal value) {
            addCriterion("specdetail_price <>", value, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceGreaterThan(BigDecimal value) {
            addCriterion("specdetail_price >", value, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("specdetail_price >=", value, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceLessThan(BigDecimal value) {
            addCriterion("specdetail_price <", value, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("specdetail_price <=", value, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceIn(List<BigDecimal> values) {
            addCriterion("specdetail_price in", values, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceNotIn(List<BigDecimal> values) {
            addCriterion("specdetail_price not in", values, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("specdetail_price between", value1, value2, "specdetailPrice");
            return this;
        }

        public Criteria andSpecdetailPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("specdetail_price not between", value1, value2, "specdetailPrice");
            return this;
        }

        public Criteria andMakepriceModeIsNull() {
            addCriterion("makeprice_mode is null");
            return this;
        }

        public Criteria andMakepriceModeIsNotNull() {
            addCriterion("makeprice_mode is not null");
            return this;
        }

        public Criteria andMakepriceModeEqualTo(Byte value) {
            addCriterion("makeprice_mode =", value, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeNotEqualTo(Byte value) {
            addCriterion("makeprice_mode <>", value, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeGreaterThan(Byte value) {
            addCriterion("makeprice_mode >", value, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("makeprice_mode >=", value, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeLessThan(Byte value) {
            addCriterion("makeprice_mode <", value, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeLessThanOrEqualTo(Byte value) {
            addCriterion("makeprice_mode <=", value, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeIn(List<Byte> values) {
            addCriterion("makeprice_mode in", values, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeNotIn(List<Byte> values) {
            addCriterion("makeprice_mode not in", values, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeBetween(Byte value1, Byte value2) {
            addCriterion("makeprice_mode between", value1, value2, "makepriceMode");
            return this;
        }

        public Criteria andMakepriceModeNotBetween(Byte value1, Byte value2) {
            addCriterion("makeprice_mode not between", value1, value2, "makepriceMode");
            return this;
        }

        public Criteria andOriginalPriceIsNull() {
            addCriterion("original_price is null");
            return this;
        }

        public Criteria andOriginalPriceIsNotNull() {
            addCriterion("original_price is not null");
            return this;
        }

        public Criteria andOriginalPriceEqualTo(String value) {
            addCriterion("original_price =", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceNotEqualTo(String value) {
            addCriterion("original_price <>", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceGreaterThan(String value) {
            addCriterion("original_price >", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceGreaterThanOrEqualTo(String value) {
            addCriterion("original_price >=", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceLessThan(String value) {
            addCriterion("original_price <", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceLessThanOrEqualTo(String value) {
            addCriterion("original_price <=", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceLike(String value) {
            addCriterion("original_price like", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceNotLike(String value) {
            addCriterion("original_price not like", value, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceIn(List<String> values) {
            addCriterion("original_price in", values, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceNotIn(List<String> values) {
            addCriterion("original_price not in", values, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceBetween(String value1, String value2) {
            addCriterion("original_price between", value1, value2, "originalPrice");
            return this;
        }

        public Criteria andOriginalPriceNotBetween(String value1, String value2) {
            addCriterion("original_price not between", value1, value2, "originalPrice");
            return this;
        }

        public Criteria andIsBuynumberChangedIsNull() {
            addCriterion("is_buynumber_changed is null");
            return this;
        }

        public Criteria andIsBuynumberChangedIsNotNull() {
            addCriterion("is_buynumber_changed is not null");
            return this;
        }

        public Criteria andIsBuynumberChangedEqualTo(Byte value) {
            addCriterion("is_buynumber_changed =", value, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedNotEqualTo(Byte value) {
            addCriterion("is_buynumber_changed <>", value, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedGreaterThan(Byte value) {
            addCriterion("is_buynumber_changed >", value, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_buynumber_changed >=", value, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedLessThan(Byte value) {
            addCriterion("is_buynumber_changed <", value, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedLessThanOrEqualTo(Byte value) {
            addCriterion("is_buynumber_changed <=", value, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedIn(List<Byte> values) {
            addCriterion("is_buynumber_changed in", values, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedNotIn(List<Byte> values) {
            addCriterion("is_buynumber_changed not in", values, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedBetween(Byte value1, Byte value2) {
            addCriterion("is_buynumber_changed between", value1, value2, "isBuynumberChanged");
            return this;
        }

        public Criteria andIsBuynumberChangedNotBetween(Byte value1, Byte value2) {
            addCriterion("is_buynumber_changed not between", value1, value2, "isBuynumberChanged");
            return this;
        }

        public Criteria andRatioOperatorIdIsNull() {
            addCriterion("ratio_operator_id is null");
            return this;
        }

        public Criteria andRatioOperatorIdIsNotNull() {
            addCriterion("ratio_operator_id is not null");
            return this;
        }

        public Criteria andRatioOperatorIdEqualTo(String value) {
            addCriterion("ratio_operator_id =", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdNotEqualTo(String value) {
            addCriterion("ratio_operator_id <>", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdGreaterThan(String value) {
            addCriterion("ratio_operator_id >", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdGreaterThanOrEqualTo(String value) {
            addCriterion("ratio_operator_id >=", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdLessThan(String value) {
            addCriterion("ratio_operator_id <", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdLessThanOrEqualTo(String value) {
            addCriterion("ratio_operator_id <=", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdLike(String value) {
            addCriterion("ratio_operator_id like", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdNotLike(String value) {
            addCriterion("ratio_operator_id not like", value, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdIn(List<String> values) {
            addCriterion("ratio_operator_id in", values, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdNotIn(List<String> values) {
            addCriterion("ratio_operator_id not in", values, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdBetween(String value1, String value2) {
            addCriterion("ratio_operator_id between", value1, value2, "ratioOperatorId");
            return this;
        }

        public Criteria andRatioOperatorIdNotBetween(String value1, String value2) {
            addCriterion("ratio_operator_id not between", value1, value2, "ratioOperatorId");
            return this;
        }

        public Criteria andChildIdIsNull() {
            addCriterion("child_id is null");
            return this;
        }

        public Criteria andChildIdIsNotNull() {
            addCriterion("child_id is not null");
            return this;
        }

        public Criteria andChildIdEqualTo(String value) {
            addCriterion("child_id =", value, "childId");
            return this;
        }

        public Criteria andChildIdNotEqualTo(String value) {
            addCriterion("child_id <>", value, "childId");
            return this;
        }

        public Criteria andChildIdGreaterThan(String value) {
            addCriterion("child_id >", value, "childId");
            return this;
        }

        public Criteria andChildIdGreaterThanOrEqualTo(String value) {
            addCriterion("child_id >=", value, "childId");
            return this;
        }

        public Criteria andChildIdLessThan(String value) {
            addCriterion("child_id <", value, "childId");
            return this;
        }

        public Criteria andChildIdLessThanOrEqualTo(String value) {
            addCriterion("child_id <=", value, "childId");
            return this;
        }

        public Criteria andChildIdLike(String value) {
            addCriterion("child_id like", value, "childId");
            return this;
        }

        public Criteria andChildIdNotLike(String value) {
            addCriterion("child_id not like", value, "childId");
            return this;
        }

        public Criteria andChildIdIn(List<String> values) {
            addCriterion("child_id in", values, "childId");
            return this;
        }

        public Criteria andChildIdNotIn(List<String> values) {
            addCriterion("child_id not in", values, "childId");
            return this;
        }

        public Criteria andChildIdBetween(String value1, String value2) {
            addCriterion("child_id between", value1, value2, "childId");
            return this;
        }

        public Criteria andChildIdNotBetween(String value1, String value2) {
            addCriterion("child_id not between", value1, value2, "childId");
            return this;
        }

        public Criteria andKindBookmenuIdIsNull() {
            addCriterion("kind_bookmenu_id is null");
            return this;
        }

        public Criteria andKindBookmenuIdIsNotNull() {
            addCriterion("kind_bookmenu_id is not null");
            return this;
        }

        public Criteria andKindBookmenuIdEqualTo(String value) {
            addCriterion("kind_bookmenu_id =", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdNotEqualTo(String value) {
            addCriterion("kind_bookmenu_id <>", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdGreaterThan(String value) {
            addCriterion("kind_bookmenu_id >", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdGreaterThanOrEqualTo(String value) {
            addCriterion("kind_bookmenu_id >=", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdLessThan(String value) {
            addCriterion("kind_bookmenu_id <", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdLessThanOrEqualTo(String value) {
            addCriterion("kind_bookmenu_id <=", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdLike(String value) {
            addCriterion("kind_bookmenu_id like", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdNotLike(String value) {
            addCriterion("kind_bookmenu_id not like", value, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdIn(List<String> values) {
            addCriterion("kind_bookmenu_id in", values, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdNotIn(List<String> values) {
            addCriterion("kind_bookmenu_id not in", values, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdBetween(String value1, String value2) {
            addCriterion("kind_bookmenu_id between", value1, value2, "kindBookmenuId");
            return this;
        }

        public Criteria andKindBookmenuIdNotBetween(String value1, String value2) {
            addCriterion("kind_bookmenu_id not between", value1, value2, "kindBookmenuId");
            return this;
        }

        public Criteria andSpecpriceModeIsNull() {
            addCriterion("specprice_mode is null");
            return this;
        }

        public Criteria andSpecpriceModeIsNotNull() {
            addCriterion("specprice_mode is not null");
            return this;
        }

        public Criteria andSpecpriceModeEqualTo(Byte value) {
            addCriterion("specprice_mode =", value, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeNotEqualTo(Byte value) {
            addCriterion("specprice_mode <>", value, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeGreaterThan(Byte value) {
            addCriterion("specprice_mode >", value, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("specprice_mode >=", value, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeLessThan(Byte value) {
            addCriterion("specprice_mode <", value, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeLessThanOrEqualTo(Byte value) {
            addCriterion("specprice_mode <=", value, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeIn(List<Byte> values) {
            addCriterion("specprice_mode in", values, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeNotIn(List<Byte> values) {
            addCriterion("specprice_mode not in", values, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeBetween(Byte value1, Byte value2) {
            addCriterion("specprice_mode between", value1, value2, "specpriceMode");
            return this;
        }

        public Criteria andSpecpriceModeNotBetween(Byte value1, Byte value2) {
            addCriterion("specprice_mode not between", value1, value2, "specpriceMode");
            return this;
        }

        public Criteria andWorkerIdIsNull() {
            addCriterion("worker_id is null");
            return this;
        }

        public Criteria andWorkerIdIsNotNull() {
            addCriterion("worker_id is not null");
            return this;
        }

        public Criteria andWorkerIdEqualTo(String value) {
            addCriterion("worker_id =", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdNotEqualTo(String value) {
            addCriterion("worker_id <>", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdGreaterThan(String value) {
            addCriterion("worker_id >", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdGreaterThanOrEqualTo(String value) {
            addCriterion("worker_id >=", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdLessThan(String value) {
            addCriterion("worker_id <", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdLessThanOrEqualTo(String value) {
            addCriterion("worker_id <=", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdLike(String value) {
            addCriterion("worker_id like", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdNotLike(String value) {
            addCriterion("worker_id not like", value, "workerId");
            return this;
        }

        public Criteria andWorkerIdIn(List<String> values) {
            addCriterion("worker_id in", values, "workerId");
            return this;
        }

        public Criteria andWorkerIdNotIn(List<String> values) {
            addCriterion("worker_id not in", values, "workerId");
            return this;
        }

        public Criteria andWorkerIdBetween(String value1, String value2) {
            addCriterion("worker_id between", value1, value2, "workerId");
            return this;
        }

        public Criteria andWorkerIdNotBetween(String value1, String value2) {
            addCriterion("worker_id not between", value1, value2, "workerId");
            return this;
        }

        public Criteria andIsBackauthIsNull() {
            addCriterion("is_backauth is null");
            return this;
        }

        public Criteria andIsBackauthIsNotNull() {
            addCriterion("is_backauth is not null");
            return this;
        }

        public Criteria andIsBackauthEqualTo(Byte value) {
            addCriterion("is_backauth =", value, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthNotEqualTo(Byte value) {
            addCriterion("is_backauth <>", value, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthGreaterThan(Byte value) {
            addCriterion("is_backauth >", value, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_backauth >=", value, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthLessThan(Byte value) {
            addCriterion("is_backauth <", value, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthLessThanOrEqualTo(Byte value) {
            addCriterion("is_backauth <=", value, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthIn(List<Byte> values) {
            addCriterion("is_backauth in", values, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthNotIn(List<Byte> values) {
            addCriterion("is_backauth not in", values, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthBetween(Byte value1, Byte value2) {
            addCriterion("is_backauth between", value1, value2, "isBackauth");
            return this;
        }

        public Criteria andIsBackauthNotBetween(Byte value1, Byte value2) {
            addCriterion("is_backauth not between", value1, value2, "isBackauth");
            return this;
        }

        public Criteria andServiceFeeModeIsNull() {
            addCriterion("service_fee_mode is null");
            return this;
        }

        public Criteria andServiceFeeModeIsNotNull() {
            addCriterion("service_fee_mode is not null");
            return this;
        }

        public Criteria andServiceFeeModeEqualTo(Byte value) {
            addCriterion("service_fee_mode =", value, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeNotEqualTo(Byte value) {
            addCriterion("service_fee_mode <>", value, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeGreaterThan(Byte value) {
            addCriterion("service_fee_mode >", value, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("service_fee_mode >=", value, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeLessThan(Byte value) {
            addCriterion("service_fee_mode <", value, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeLessThanOrEqualTo(Byte value) {
            addCriterion("service_fee_mode <=", value, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeIn(List<Byte> values) {
            addCriterion("service_fee_mode in", values, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeNotIn(List<Byte> values) {
            addCriterion("service_fee_mode not in", values, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeBetween(Byte value1, Byte value2) {
            addCriterion("service_fee_mode between", value1, value2, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeModeNotBetween(Byte value1, Byte value2) {
            addCriterion("service_fee_mode not between", value1, value2, "serviceFeeMode");
            return this;
        }

        public Criteria andServiceFeeIsNull() {
            addCriterion("service_fee is null");
            return this;
        }

        public Criteria andServiceFeeIsNotNull() {
            addCriterion("service_fee is not null");
            return this;
        }

        public Criteria andServiceFeeEqualTo(String value) {
            addCriterion("service_fee =", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeNotEqualTo(String value) {
            addCriterion("service_fee <>", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeGreaterThan(String value) {
            addCriterion("service_fee >", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeGreaterThanOrEqualTo(String value) {
            addCriterion("service_fee >=", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeLessThan(String value) {
            addCriterion("service_fee <", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeLessThanOrEqualTo(String value) {
            addCriterion("service_fee <=", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeLike(String value) {
            addCriterion("service_fee like", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeNotLike(String value) {
            addCriterion("service_fee not like", value, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeIn(List<String> values) {
            addCriterion("service_fee in", values, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeNotIn(List<String> values) {
            addCriterion("service_fee not in", values, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeBetween(String value1, String value2) {
            addCriterion("service_fee between", value1, value2, "serviceFee");
            return this;
        }

        public Criteria andServiceFeeNotBetween(String value1, String value2) {
            addCriterion("service_fee not between", value1, value2, "serviceFee");
            return this;
        }

        public Criteria andOrignIdIsNull() {
            addCriterion("orign_id is null");
            return this;
        }

        public Criteria andOrignIdIsNotNull() {
            addCriterion("orign_id is not null");
            return this;
        }

        public Criteria andOrignIdEqualTo(String value) {
            addCriterion("orign_id =", value, "orignId");
            return this;
        }

        public Criteria andOrignIdNotEqualTo(String value) {
            addCriterion("orign_id <>", value, "orignId");
            return this;
        }

        public Criteria andOrignIdGreaterThan(String value) {
            addCriterion("orign_id >", value, "orignId");
            return this;
        }

        public Criteria andOrignIdGreaterThanOrEqualTo(String value) {
            addCriterion("orign_id >=", value, "orignId");
            return this;
        }

        public Criteria andOrignIdLessThan(String value) {
            addCriterion("orign_id <", value, "orignId");
            return this;
        }

        public Criteria andOrignIdLessThanOrEqualTo(String value) {
            addCriterion("orign_id <=", value, "orignId");
            return this;
        }

        public Criteria andOrignIdLike(String value) {
            addCriterion("orign_id like", value, "orignId");
            return this;
        }

        public Criteria andOrignIdNotLike(String value) {
            addCriterion("orign_id not like", value, "orignId");
            return this;
        }

        public Criteria andOrignIdIn(List<String> values) {
            addCriterion("orign_id in", values, "orignId");
            return this;
        }

        public Criteria andOrignIdNotIn(List<String> values) {
            addCriterion("orign_id not in", values, "orignId");
            return this;
        }

        public Criteria andOrignIdBetween(String value1, String value2) {
            addCriterion("orign_id between", value1, value2, "orignId");
            return this;
        }

        public Criteria andOrignIdNotBetween(String value1, String value2) {
            addCriterion("orign_id not between", value1, value2, "orignId");
            return this;
        }

        public Criteria andAdditionPriceIsNull() {
            addCriterion("addition_price is null");
            return this;
        }

        public Criteria andAdditionPriceIsNotNull() {
            addCriterion("addition_price is not null");
            return this;
        }

        public Criteria andAdditionPriceEqualTo(BigDecimal value) {
            addCriterion("addition_price =", value, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceNotEqualTo(BigDecimal value) {
            addCriterion("addition_price <>", value, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceGreaterThan(BigDecimal value) {
            addCriterion("addition_price >", value, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("addition_price >=", value, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceLessThan(BigDecimal value) {
            addCriterion("addition_price <", value, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("addition_price <=", value, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceIn(List<BigDecimal> values) {
            addCriterion("addition_price in", values, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceNotIn(List<BigDecimal> values) {
            addCriterion("addition_price not in", values, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("addition_price between", value1, value2, "additionPrice");
            return this;
        }

        public Criteria andAdditionPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("addition_price not between", value1, value2, "additionPrice");
            return this;
        }

        public Criteria andHasAdditionIsNull() {
            addCriterion("has_addition is null");
            return this;
        }

        public Criteria andHasAdditionIsNotNull() {
            addCriterion("has_addition is not null");
            return this;
        }

        public Criteria andHasAdditionEqualTo(Byte value) {
            addCriterion("has_addition =", value, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionNotEqualTo(Byte value) {
            addCriterion("has_addition <>", value, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionGreaterThan(Byte value) {
            addCriterion("has_addition >", value, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionGreaterThanOrEqualTo(Byte value) {
            addCriterion("has_addition >=", value, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionLessThan(Byte value) {
            addCriterion("has_addition <", value, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionLessThanOrEqualTo(Byte value) {
            addCriterion("has_addition <=", value, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionIn(List<Byte> values) {
            addCriterion("has_addition in", values, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionNotIn(List<Byte> values) {
            addCriterion("has_addition not in", values, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionBetween(Byte value1, Byte value2) {
            addCriterion("has_addition between", value1, value2, "hasAddition");
            return this;
        }

        public Criteria andHasAdditionNotBetween(Byte value1, Byte value2) {
            addCriterion("has_addition not between", value1, value2, "hasAddition");
            return this;
        }

        public Criteria andSeatIdIsNull() {
            addCriterion("seat_id is null");
            return this;
        }

        public Criteria andSeatIdIsNotNull() {
            addCriterion("seat_id is not null");
            return this;
        }

        public Criteria andSeatIdEqualTo(String value) {
            addCriterion("seat_id =", value, "seatId");
            return this;
        }

        public Criteria andSeatIdNotEqualTo(String value) {
            addCriterion("seat_id <>", value, "seatId");
            return this;
        }

        public Criteria andSeatIdGreaterThan(String value) {
            addCriterion("seat_id >", value, "seatId");
            return this;
        }

        public Criteria andSeatIdGreaterThanOrEqualTo(String value) {
            addCriterion("seat_id >=", value, "seatId");
            return this;
        }

        public Criteria andSeatIdLessThan(String value) {
            addCriterion("seat_id <", value, "seatId");
            return this;
        }

        public Criteria andSeatIdLessThanOrEqualTo(String value) {
            addCriterion("seat_id <=", value, "seatId");
            return this;
        }

        public Criteria andSeatIdLike(String value) {
            addCriterion("seat_id like", value, "seatId");
            return this;
        }

        public Criteria andSeatIdNotLike(String value) {
            addCriterion("seat_id not like", value, "seatId");
            return this;
        }

        public Criteria andSeatIdIn(List<String> values) {
            addCriterion("seat_id in", values, "seatId");
            return this;
        }

        public Criteria andSeatIdNotIn(List<String> values) {
            addCriterion("seat_id not in", values, "seatId");
            return this;
        }

        public Criteria andSeatIdBetween(String value1, String value2) {
            addCriterion("seat_id between", value1, value2, "seatId");
            return this;
        }

        public Criteria andSeatIdNotBetween(String value1, String value2) {
            addCriterion("seat_id not between", value1, value2, "seatId");
            return this;
        }
    }
}
