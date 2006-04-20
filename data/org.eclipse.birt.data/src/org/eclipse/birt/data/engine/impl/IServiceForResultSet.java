/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.DataEngineContext;
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
public interface IServiceForResultSet
{
	/**
	 * @return
	 */
	public DataEngineContext getContext( );
	
	/**
	 * @return queryResults
	 */
	public IQueryResults getQueryResults( );

	/**
	 * @return base query definition
	 */
	public IBaseQueryDefinition getQueryDefn( );
	
	/**
	 * @param exprName
	 * @return
	 */
	public IBaseExpression getBaseExpression( String exprName );
	
	/**
	 * @param exprName
	 * @return
	 */
	public IScriptExpression getAutoBindingExpr( String exprName );
	
	/**
	 * @param iterator
	 * @param subQueryName
	 * @param subScope
	 * @return the query results of specified sub query
	 * @throws DataException
	 */
	public QueryResults execSubquery( IResultIterator iterator,
			String subQueryName, Scriptable subScope ) throws DataException;
	
}
