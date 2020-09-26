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
package com.qlangtech.tis.manage.common;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import java.util.List;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public abstract class BasicDAO<T, C> extends SqlMapClientDaoSupport {

    protected int count(String sqlmap, C param) {
        return (Integer) this.getSqlMapClientTemplate().queryForObject(sqlmap, param);
    }

    protected int countFromWriterDB(String sqlmap, C param) {
        return this.count(sqlmap, param);
    }

    protected int deleteRecords(String sqlmap, Object criteria) {
        return this.getSqlMapClientTemplate().delete(sqlmap, criteria);
    }

    protected Object insert(String sqlmap, T record) {
        return this.getSqlMapClientTemplate().insert(sqlmap, record);
    }

    @SuppressWarnings("unchecked")
    protected List<T> list(String sqlmap, C criteria) {
        return (List<T>) this.getSqlMapClientTemplate().queryForList(sqlmap, criteria);
    }

    @SuppressWarnings("unchecked")
    protected <TT> List<TT> listAnonymity(String sqlmap, Object criteria) {
        return (List<TT>) this.getSqlMapClientTemplate().queryForList(sqlmap, criteria);
    }

    @SuppressWarnings("unchecked")
    protected T load(String sqlmap, T query) {
        return (T) this.getSqlMapClientTemplate().queryForObject(sqlmap, query);
    }

    @SuppressWarnings("unchecked")
    protected T loadPojo(String sqlmap, Object query) {
        return (T) this.getSqlMapClientTemplate().queryForObject(sqlmap, query);
    }

    protected T loadFromWriterDB(String sqlmap, T query) {
        return this.load(sqlmap, query);
    }

    protected int updateRecords(String sqlmap, C criteria) {
        return this.getSqlMapClientTemplate().update(sqlmap, criteria);
    }
}
