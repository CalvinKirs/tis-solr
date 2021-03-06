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
package com.qlangtech.tis.solrextend.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import com.google.common.collect.Lists;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/09/25
 */
public class IKTokenFilter extends TokenFilter {

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

    private static final TisIKConfiguration tisConfig;

    static {
        tisConfig = new TisIKConfiguration();
        tisConfig.setUseSmart(false);
    }

    static final Comparator<Lexeme> compar = new Comparator<Lexeme>() {

        public int compare(Lexeme o1, Lexeme o2) {
            int r = o1.getBegin() - o2.getBegin();
            return r == 0 ? o1.getEndPosition() - o2.getEndPosition() : r;
        }
    };

    // private IKSegmenter ikSeg;
    private Iterator<Lexeme> lsIterator;

    private int preBegin = -1;

    public IKTokenFilter(TokenStream input) {
        super(input);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        this.clearAttributes();
        String content = null;
        while (true) {
            if (this.lsIterator == null) {
                if (!this.input.incrementToken()) {
                    return false;
                }
                IKSegmenter ikSeg = new IKSegmenter(new StringReader(this.termAtt.toString()), tisConfig);
                List<Lexeme> ls = Lists.newArrayList();
                Lexeme l = null;
                while ((l = ikSeg.next()) != null) {
                    if (l.getOffset() > 0) {
                        continue;
                    }
                    // if (StringUtils.length(l.getLexemeText()) > 1) {
                    ls.add(l);
                // }
                }
                ls.sort(compar);
                this.lsIterator = ls.iterator();
            }
            Lexeme l = null;
            if (this.lsIterator.hasNext()) {
                l = lsIterator.next();
                content = l.getLexemeText();
                // 必须是普通word
                if ((l.getBeginPosition() > preBegin)) {
                    // System.out.println(content);
                    // if (StringUtils.length(content) > 1) {
                    this.posIncrAtt.setPositionIncrement(1);
                    this.preBegin = l.getBeginPosition();
                // }
                } else {
                    this.posIncrAtt.setPositionIncrement(0);
                }
                offsetAtt.setOffset(l.getBegin(), l.getEndPosition());
                this.termAtt.copyBuffer(content.toCharArray(), 0, content.length());
                return true;
            } else {
                this.lsIterator = null;
                this.preBegin = -1;
            }
        }
    }

    @Override
    public void end() throws IOException {
        super.end();
    }

    public static void main(String[] args) throws Exception {
        IKSegmenter ikSeg = new IKSegmenter(new StringReader("我爱北京天安门"), false);
        Lexeme l = null;
        // while ((l = ikSeg.next()) != null) {
        // System.out.println(
        // l + ",begin:" + l.getBeginPosition() + ",end:" + l.getEndPosition() +
        // ",offset:" + l.getOffset());
        // }
        System.out.println("=========================================");
        ikSeg = new IKSegmenter(new StringReader("马尼拉二手交易市场"), false);
        List<Lexeme> ls = Lists.newArrayList();
        while ((l = ikSeg.next()) != null) {
            ls.add(l);
        // System.out.println(l);
        }
        ls.sort(compar);
        int preBegin = -1;
        int preEnd = -1;
        for (Lexeme le : ls) {
            // if (le.getLexemeType() == 4 && (le.getBeginPosition() >
            // preBegin)) {
            // System.out.println(le + "," + le.getLexemeType());
            // preBegin = le.getBeginPosition();
            // }
            System.out.println(le + "," + le.getLexemeType());
        // System.out.println(le + "," + le.getLexemeType());
        }
    }
}
