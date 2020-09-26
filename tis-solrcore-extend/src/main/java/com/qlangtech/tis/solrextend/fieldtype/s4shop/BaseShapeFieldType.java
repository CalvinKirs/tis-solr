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
package com.qlangtech.tis.solrextend.fieldtype.s4shop;

import java.util.Collections;
import java.util.List;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.SpatialRecursivePrefixTreeFieldType;
import org.locationtech.spatial4j.shape.Shape;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年1月17日
 */
public abstract class BaseShapeFieldType extends SpatialRecursivePrefixTreeFieldType {

    public BaseShapeFieldType() {
        super();
    }

    private static final ThreadLocal<ShapeStrStore> shapeStrTL = new ThreadLocal<ShapeStrStore>() {

        @Override
        protected ShapeStrStore initialValue() {
            return new ShapeStrStore();
        }
    };

    private static class ShapeStrStore {

        private String shapeStr;
    }

    protected final String getStoredValue(Shape shape, String shapeStr) {
        return shapeStrTL.get().shapeStr;
    }

    @Override
    public List<IndexableField> createFields(SchemaField field, Object val) {
        // 客户端 传入的参数是这样的
        if (isShapeLiteria(val)) {
            shapeStrTL.get().shapeStr = String.valueOf(val);
            return super.createFields(field, val);
        }
        StringBuffer buffer = buildShapLiteria(val);
        if (buffer == null) {
            // logger.warn("field:"+ field.getName() +",val:" + val + " is illegal");
            return Collections.emptyList();
        }
        shapeStrTL.get().shapeStr = buffer.toString();
        return super.createFields(field, parseShape(buffer.toString()));
    }

    protected abstract StringBuffer buildShapLiteria(Object val);

    protected abstract boolean isShapeLiteria(Object val);
}
