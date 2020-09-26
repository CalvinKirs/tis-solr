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
package com.qlangtech.tis.solrextend.queryparse.s4menuNameSuggestion;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年1月17日
 */
public class Order2Doc {

    private int orderId;

    private int docId;

    public Order2Doc(int orderId, int docId) {
        this.orderId = orderId;
        this.docId = docId;
    }

    public int getDocId() {
        return docId;
    }

    @Override
    public int hashCode() {
        return this.orderId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return this.hashCode() == obj.hashCode();
    }
}
