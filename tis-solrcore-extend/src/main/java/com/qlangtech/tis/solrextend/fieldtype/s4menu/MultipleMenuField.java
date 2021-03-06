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
package com.qlangtech.tis.solrextend.fieldtype.s4menu;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.schema.StrField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * multiple_menu_info字段的结构变成:菜单id_价格(规格id_规格价格)(规格id_规格价格);菜单id_价格(规格id_规格价格)(规格id_规格价格)
 * 所有的menuId可以当做索引来搜索
 *
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年1月17日
 */
public class MultipleMenuField extends StrField {

    public static final Pattern MENU_INFO_PATTERN = Pattern.compile("(\\w+)_(\\d{1})_([\\d.]+)([\\-()\\w_.]*)");

    // static final Pattern MENU_SPEC_PATTERN = Pattern.compile("");
    public static final String SEPARATOR = ";";

    @Override
    protected IndexableField createField(String name, String val, org.apache.lucene.index.IndexableFieldType type) {
        if (StringUtils.isBlank(val)) {
            return null;
        }
        org.apache.lucene.document.Field f = new Field(name, val, type);
        String[] menuInfos = val.split(SEPARATOR);
        final List<String> menuIds = new ArrayList<>();
        for (String menuInfo : menuInfos) {
            Matcher matcher = MENU_INFO_PATTERN.matcher(menuInfo);
            if (matcher.matches()) {
                menuIds.add(matcher.group(1));
            }
        }
        final int length = menuIds.size();
        f.setTokenStream(new TokenStream() {

            private final CharTermAttribute termAtt = (CharTermAttribute) addAttribute(CharTermAttribute.class);

            int index = 0;

            @Override
            public boolean incrementToken() throws IOException {
                clearAttributes();
                if (index >= length) {
                    return false;
                } else {
                    termAtt.setEmpty().append(menuIds.get(index++));
                    return true;
                }
            }
        });
        return f;
    }

    @Override
    public boolean isPolyField() {
        return false;
    }
}
