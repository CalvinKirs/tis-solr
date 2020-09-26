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
package com.qlangtech.tis.fullbuild.indexbuild;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2015年11月3日 下午1:56:53
 */
public interface IRemoteJobTriggerFactory extends IIndexBuildJobFactory, ITableDumpJobFactory {
    // 创建dump数据任务
    // @Deprecated
    // public IRemoteJobTrigger createDumpJob(String indexName, String startTime, TaskContext context) throws Exception;
}
