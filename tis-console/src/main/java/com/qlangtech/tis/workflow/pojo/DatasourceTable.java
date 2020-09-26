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
package com.qlangtech.tis.workflow.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public class DatasourceTable implements Serializable, Comparable<DatasourceTable> {

    private Integer id;

    private String name;

    private String tableLogicName;

    private Integer dbId;

    private Byte syncOnline;

    private String gitTag;

    private Date createTime;

    private Date opTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTableLogicName() {
        return tableLogicName;
    }

    public void setTableLogicName(String tableLogicName) {
        this.tableLogicName = tableLogicName == null ? null : tableLogicName.trim();
    }

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Byte getSyncOnline() {
        return syncOnline;
    }

    public void setSyncOnline(Byte syncOnline) {
        this.syncOnline = syncOnline;
    }

    public String getGitTag() {
        return gitTag;
    }

    public void setGitTag(String gitTag) {
        this.gitTag = gitTag == null ? null : gitTag.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    @Override
    public int compareTo(DatasourceTable o) {
        return getTableLogicName().compareTo(o.getTableLogicName());
    }
}
