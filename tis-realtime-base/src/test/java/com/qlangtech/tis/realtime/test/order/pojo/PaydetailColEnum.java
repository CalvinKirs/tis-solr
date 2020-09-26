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

import com.google.common.collect.ImmutableList;
import java.util.List;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public enum PaydetailColEnum {

    PAYDETAIL_ID("paydetail_id", 12, true),
    PAY_ID("pay_id", 12, false),
    KINDPAYDETAIL_ID("kindpaydetail_id", 12, false),
    KINDPAYDETAIL_OPTION_ID("kindpaydetail_option_id", 12, false),
    MEMO("memo", 12, false),
    ENTITY_ID("entity_id", 12, false),
    IS_VALID("is_valid", -6, false),
    CREATE_TIME("create_time", -5, false),
    OP_TIME("op_time", -5, false),
    LAST_VER("last_ver", -5, false),
    LOAD_TIME("load_time", 4, false),
    MODIFY_TIME("modify_time", 4, false);

    private final String name;

    private final int jdbcType;

    private final boolean pk;

    private static final List<PaydetailColEnum> pks = (new ImmutableList.Builder<PaydetailColEnum>()).add(PAYDETAIL_ID).build();

    private PaydetailColEnum(String name, int jdbcType, boolean pk) {
        this.jdbcType = jdbcType;
        this.name = name;
        this.pk = pk;
    }

    public String getName() {
        return this.name;
    }

    public int getJdbcType() {
        return this.jdbcType;
    }

    public boolean isPK() {
        return this.pk;
    }

    public static List<PaydetailColEnum> getPKs() {
        return pks;
    }
}
