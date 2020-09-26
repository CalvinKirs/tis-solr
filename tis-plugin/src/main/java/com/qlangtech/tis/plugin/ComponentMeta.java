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

import com.qlangtech.tis.TIS;
import com.qlangtech.tis.extension.impl.XmlFile;
import com.qlangtech.tis.util.RobustReflectionConverter;
import com.qlangtech.tis.util.XStream2;
import com.qlangtech.tis.util.XStream2PluginInfoReader;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 组件元数据信息
 *
 * @author 百岁（baisui@qlangtech.com）
 * @create: 2020-04-24 16:24
 */
public class ComponentMeta {

    public final List<IRepositoryResource> resources;

    public ComponentMeta(List<IRepositoryResource> resources) {
        this.resources = resources;
    }

    public ComponentMeta(IRepositoryResource resource) {
        this(Collections.singletonList(resource));
    }

    /**
     * 下载配置文件
     */
    public void downloaConfig() {
        resources.forEach((r) -> r.copyConfigFromRemote());
    }

    /**
     * 取得元数据信息
     *
     * @return
     */
    public Set<XStream2.PluginMeta> loadPluginMeta() {
        synchronized (RobustReflectionConverter.usedPluginInfo) {
            RobustReflectionConverter.usedPluginInfo.remove();
            XStream2PluginInfoReader reader = new XStream2PluginInfoReader(XmlFile.DEFAULT_DRIVER);
            for (IRepositoryResource res : this.resources) {
                File targetFile = res.getTargetFile();
                if (!targetFile.exists()) {
                    throw new IllegalStateException("file:" + targetFile.getAbsolutePath() + " is not exist");
                }
                try {
                    XmlFile xmlFile = new XmlFile(reader, targetFile);
                    xmlFile.read();
                } catch (IOException e) {
                    throw new RuntimeException(targetFile.getAbsolutePath(), e);
                }
            }
            return RobustReflectionConverter.usedPluginInfo.get();
        }
    }

    /**
     * 同步插件
     */
    public void synchronizePluginsFromRemoteRepository() {
        try {
            this.downloaConfig();
            for (XStream2.PluginMeta m : loadPluginMeta()) {
                m.copyFromRemote();
            }
        } finally {
            TIS.permitInitialize = true;
        }
        if (TIS.initialized) {
            throw new IllegalStateException("make sure TIS plugin have not be initialized");
        }
    }
}
