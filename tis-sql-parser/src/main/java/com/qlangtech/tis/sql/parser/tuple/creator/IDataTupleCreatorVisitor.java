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
package com.qlangtech.tis.sql.parser.tuple.creator;

import com.qlangtech.tis.sql.parser.tuple.creator.impl.FunctionDataTupleCreator;
import com.qlangtech.tis.sql.parser.tuple.creator.impl.TableTupleCreator;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年5月13日
 */
public interface IDataTupleCreatorVisitor {

    public void visit(FunctionDataTupleCreator function);

    public void visit(TableTupleCreator tableTuple);
}
