/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 *
 */
public interface IQueryExecutor {
	/**
	 * Execute query
	 *
	 * @param eventHandler
	 * @param stopSign
	 * @throws DataException
	 */
	void execute(IEventHandler eventHandler) throws DataException;

	/**
	 * @return shard scope
	 */
	Scriptable getQueryScope() throws DataException;

	/**
	 * @return shard scope
	 */
	Scriptable getSharedScope() throws DataException;

	/**
	 * @return scope of JS aggregation object
	 */
	Scriptable getJSAggrValueObject();

	/**
	 * @return the nested level of this executor
	 */
	int getNestedLevel();

	/**
	 * @return
	 */
	IDataSourceInstanceHandle getDataSourceInstanceHandle();

	/**
	 * @return the data set associated with this executor
	 */
	DataSetRuntime getDataSet();

	/**
	 * @param nestedCount
	 * @return nested data set
	 */
	DataSetRuntime[] getNestedDataSets(int nestedCount);

	/**
	 * @return result set of underlying ODI layer
	 */
	IResultIterator getOdiResultSet();

	/**
	 * @return meta data of column binding
	 * @throws DataException
	 */
	IResultMetaData getResultMetaData() throws DataException;

	/**
	 * @return meta data of data set
	 * @throws DataException
	 */
	IResultClass getOdiResultClass() throws DataException;

	IQueryContextVisitor getQueryContextVisitor();

	/**
	 *
	 * @return
	 */
	Map getAppContext();

	/**
	 * close
	 */
	void close();

	DataEngineSession getSession();
}
