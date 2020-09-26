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
package com.koubei.web.tag.pager.k2;

import com.koubei.web.tag.pager.AbstractPageNumShowRule;
import com.koubei.web.tag.pager.AroundTag;
import com.koubei.web.tag.pager.FormtDirectJump;
import com.koubei.web.tag.pager.GlobalStyleFactory;
import com.koubei.web.tag.pager.Pager;
import com.koubei.web.tag.pager.Pager.DirectJump;
import com.koubei.web.tag.pager.Pager.NavigationStyle;
import com.koubei.web.tag.pager.Pager.PageNumShowRule;
import com.koubei.web.tag.pager.Pager.PageStatistics;

/**
 *         2010-12-3
 *
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public class K2StyleFactory extends GlobalStyleFactory {

    private final K2AroundTag aroundTag;

    private boolean havePageStatistics;

    public K2StyleFactory(Pager pager, K2AroundTag aroundTag) {
        this(pager, aroundTag, false);
    }

    public K2StyleFactory(Pager pager, K2AroundTag aroundTag, boolean havePageStatistics) {
        super(pager);
        this.aroundTag = aroundTag;
        this.havePageStatistics = havePageStatistics;
    }

    @Override
    public DirectJump getDirectJump() {
        if (aroundTag.isContainForm()) {
            return new FormtDirectJump(this.getPager(), new FormtDirectJump.DirectJumpCss("jump", "jump-inp", "submit", "option"));
        }
        return NULL_DIRECT_JUMP;
    }

    @Override
    public PageStatistics getPageStatistics() {
        if (!havePageStatistics) {
            return NULL_PAGE_STSTISTICS;
        }
        return new PageStatistics() {

            @Override
            public void build(StringBuffer builder, int page, int pageCount) {
                // <span class="sum">第<b>1</b>/335页</span>
                builder.append("<span class=\"sum\">第<b>").append(page).append("</b>").append("/").append(pageCount).append("页</span>");
            }
        };
    }

    @Override
    public PageNumShowRule getPageNumShowRule() {
        return new AbstractPageNumShowRule.PageNumShowRule20101203(AbstractPageNumShowRule.K2_IGNORE_TAG);
    }

    @Override
    public NavigationStyle getNaviagationStyle() {
        return new NavigationStyle20101203() {

            @Override
            public AroundTag getAroundStyle() {
                return aroundTag;
            }

            @Override
            public String getCurrentPageTag(int page) {
                return "<strong class=\"current\">" + page + "</strong>";
            }

            @Override
            public void popNextPageLink(StringBuffer pageHtml, int page) {
                popDivHreLink(pageHtml, page, "下一页", "next");
            }

            @Override
            public void popPerPageLink(StringBuffer pageHtml, int page) {
                popDivHreLink(pageHtml, page, "上一页", "pre");
            }
        };
    }
}
