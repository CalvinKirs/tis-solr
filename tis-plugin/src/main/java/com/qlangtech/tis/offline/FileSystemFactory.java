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
package com.qlangtech.tis.offline;

import com.qlangtech.tis.TIS;
import com.qlangtech.tis.extension.Describable;
import com.qlangtech.tis.extension.Descriptor;
import com.qlangtech.tis.extension.DescriptorExtensionList;
import com.qlangtech.tis.fs.ITISFileSystemFactory;
import com.qlangtech.tis.plugin.IdentityName;
import com.qlangtech.tis.plugin.PluginStore;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public abstract class FileSystemFactory implements Describable<FileSystemFactory>, ITISFileSystemFactory, IdentityName {

    public static FileSystemFactory getFsFactory(String fsName) {
        PluginStore<FileSystemFactory> pluginStore = TIS.getPluginStore(FileSystemFactory.class);
        return pluginStore.find(fsName);
    // List<FileSystemFactory> fsFactories = TIS.get().loadGlobalComponent().getFsFactories();
    // for (FileSystemFactory fsFactory : fsFactories) {
    // if (StringUtils.equals(fsName, fsFactory.getName())) {
    // return fsFactory;
    // }
    // }
    // throw new IllegalStateException("fileSystem has not be initialized,fsName:" + fsName
    // + " can not find relevant 'ITISFileSystem' in [" + fsFactories.stream().map((r) -> r.getName()).collect(Collectors.joining(",")) + "]");
    }

    @Override
    public Descriptor<FileSystemFactory> getDescriptor() {
        return TIS.get().getDescriptor(this.getClass());
    }
    // public static DescriptorExtensionList<FileSystemFactory, Descriptor<FileSystemFactory>> all() {
    // return TIS.get().getDescriptorList(FileSystemFactory.class);
    // }
}
