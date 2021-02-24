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
package com.qlangtech.tis.sql.parser.er;

import org.apache.commons.lang.StringUtils;

import java.util.Optional;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @create: 2020-06-03 18:09
 */
public class TableMeta {

    public static boolean hasValidPrimayTableSharedKey(Optional<TableMeta> ptab) {
        return ptab.isPresent() && StringUtils.isNotEmpty(ptab.get().getSharedKey());
    }

    // 主索引表名称
    private final String tabName;

    private final String sharedKey;

    public TableMeta(String tabName, String sharedKey) {
        this.tabName = tabName;
        this.sharedKey = sharedKey;
    }

    public String getTabName() {
        return tabName;
    }

    public String getSharedKey() {
        return sharedKey;
    }
}
