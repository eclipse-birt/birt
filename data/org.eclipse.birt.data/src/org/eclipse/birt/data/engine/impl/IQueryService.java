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
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Wrap the service which is provided for IQueryResults to make IQueryResults
 * knows only these information it needes.
 */
public interface IQueryService
{
	/**
	 * @return
	 */
	public DataEngineContext getContext( );

	/**
	 * @return base query definition
	 */
	public IBaseQueryDefinition getQueryDefn( );

	/**
	 * @return
	 */
	public IPreparedQuery getPreparedQuery( );
	
	/**
	 * @return
	 */
	public int getGroupLevel( );

	/**
	 * @param count
	 * @return
	 */
	public DataSetRuntime[] getDataSetRuntimes( int count );
	
	public DataSetRuntime getDataSetRuntime( );

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public IResultMetaData getResultMetaData( ) throws DataException;
	
	/**
	 * @return
	 */
	public IResultIterator executeQuery( ) throws DataException;

	
	/**
	 * @param iterator
	 * @param subQueryName
	 * @param subScope
	 * @return
	 * @throws DataException
	 */
	public QueryResults execSubquery( IResultIterator iterator,
			String subQueryName, Scriptable subScope ) throws DataException;
	
	/**
	 * 
	 */
	public void close( );

	/**
	 * @param exprName
	 * @return
	 */
	public IBaseExpression getBaseExpression( String exprName );

	/**
	 * @return
	 */
	public ExprManager getExprManager( );
	
}
