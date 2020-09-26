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
package com.qlangtech.tis.indexbuilder.exception;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年1月17日
 */
public class FieldException extends RuntimeException {

    String msg = "";

    String field = "";

    String fieldValue = "";

    /**
     * @param msg
     * @param e
     */
    public FieldException(String msg, String field, String fieldValue) {
        // TODO Auto-generated constructor stub
        super(msg);
        this.msg = msg;
        this.field = field;
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString() {
        return msg + ", field:" + field + ",value:" + fieldValue;
    }
}
