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
package com.qlangtech.tis.build.metrics;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/09/25
 */
public class Messages {

    public enum Message {

        BEGIN_OPTIMIZE,
        INDEX_TIME,
        OPTIMIZE_TIME,
        ERROR_MSG,
        ALL_TIME,
        WAIT_OPTIMIZE
    }

    public void addMessage(Message msg, String msgContent) {
    }
}
