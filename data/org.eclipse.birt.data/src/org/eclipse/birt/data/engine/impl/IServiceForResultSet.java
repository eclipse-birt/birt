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

import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Wrap the service which is provided for IResultIterator to make
 * IResultIterator knows only these information it needes.
 */
interface IServiceForResultSet {
	/**
	 * @return
	 */
	public DataEngineSession getSession();

	/**
	 * @return queryResults
	 */
	public IQueryResults getQueryResults();

	/**
	 * @return base query definition
	 */
	public IBaseQueryDefinition getQueryDefn();

	/**
	 * @param exprName
	 * @return
	 * @throws DataException
	 */
	public IBaseExpression getBindingExpr(String exprName) throws DataException;

	/**
	 * @param exprName
	 * @return
	 */
	public IScriptExpression getAutoBindingExpr(String exprName);

	/**
	 * the element is GroupBindingColumn
	 * 
	 * @return
	 */
	public List getAllBindingExprs();

	/**
	 * map of bound column name with associated expression
	 * 
	 * @return
	 */
	public Map getAllAutoBindingExprs();

	/**
	 * @param iterator
	 * @param subQueryName
	 * @param subScope
	 * @return the query results of specified sub query
	 * @throws DataException
	 */
	public IQueryResults execSubquery(IResultIterator iterator, String subQueryName, Scriptable subScope)
			throws DataException;

}
