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
package com.qlangtech.tis.plugin.ds;

import com.google.common.base.Joiner;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public class ColumnMetaData {

    public static StringBuffer buildExtractSQL(String tableName, List<ColumnMetaData> cols) {
        return buildExtractSQL(tableName, false, cols);
    }

    public static StringBuffer buildExtractSQL(String tableName, boolean useAlias, List<ColumnMetaData> cols) {
        if (CollectionUtils.isEmpty(cols)) {
            throw new IllegalStateException("tableName:" + tableName + "");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(Joiner.on(",").join(cols.stream().map((r) -> {
            if (useAlias) {
                return "a." + r.getKey();
            } else {
                return r.getKey();
            }
        }).iterator())).append("\n");
        sql.append("FROM ").append(tableName);
        if (useAlias) {
            sql.append(" AS a");
        }
        return sql;
    }

    private final String key;
    //java.sql.Types
    private final int type;

    private final int index;
    private ReservedFieldType schemaFieldType;

    // private final String dbType;
    // private final String hiveType;
    // 是否是主键
    private final boolean pk;

    /**
     * @param key  column名字
     * @param type column类型 java.sql.Types
     */
    public ColumnMetaData(int index, String key, int type, boolean pk) {
        super();
        this.pk = pk;
        this.key = key;
        this.type = type;
        this.index = index;
    }

    public ReservedFieldType getSchemaFieldType() {
        return schemaFieldType;
    }

    public void setSchemaFieldType(ReservedFieldType schemaFieldType) {
        this.schemaFieldType = schemaFieldType;
    }

    public int getIndex() {
        return index;
    }

    public String getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public boolean isPk() {
        return this.pk;
    }

    public static class ReservedFieldType {
        public final ReflectSchemaFieldType type;
        public final boolean tokenizer;

        public ReservedFieldType(ReflectSchemaFieldType type) {
            this(type, false);
        }

        public ReservedFieldType(ReflectSchemaFieldType type, boolean tokenizer) {
            this.type = type;
            this.tokenizer = tokenizer;
        }
    }

    public enum ReflectSchemaFieldType {
        STRING("string"), INT("int"), FLOAT("float"), LONG("long") //
        , DOUBLE("double"), IK("ik"), TEXT_WS("text_ws"), LIKE("like"), PINYIN("pinyin");

        public final String literia;

        private ReflectSchemaFieldType(String literia) {
            this.literia = literia;
        }
    }
}
