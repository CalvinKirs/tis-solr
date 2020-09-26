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
package com.qlangtech.tis.plugin;

import java.io.File;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @create: 2020-04-24 16:14
 */
public interface IRepositoryResource {

    /**
     * 拷贝配置文件到本地
     */
    public void copyConfigFromRemote();

    /**
     * 目标文件
     *
     * @return
     */
    public File getTargetFile();
}
