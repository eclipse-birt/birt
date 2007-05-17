/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.impl.query;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.script.JSLevelAccessor;
import org.eclipse.birt.data.engine.olap.script.JSMeasureAccessor;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class CubeQueryResults implements ICubeQueryResults
{

	private PreparedCubeQuery preparedQuery;
	private Scriptable scope;
	private DataEngineContext context;
	private DataEngineSession session;
	private String queryResultsId;
	
	/**
	 * 
	 * @param preparedQuery
	 * @param scope
	 */
	public CubeQueryResults( PreparedCubeQuery preparedQuery, DataEngineSession session, Scriptable scope, DataEngineContext context )
	{
		this.preparedQuery = preparedQuery;
		this.scope = scope;
		this.context = context;
		this.session = session;
		this.queryResultsId = preparedQuery.getCubeQueryDefinition( ).getQueryResultsID( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.ICubeQueryResults#getCubeCursor()
	 */
	public CubeCursor getCubeCursor( ) throws DataException
	{
		try
		{
			CubeQueryExecutor executor = new CubeQueryExecutor( preparedQuery.getCubeQueryDefinition( ), this.session,
					this.scope, this.context );
			BirtCubeView bcv = new BirtCubeView( executor );
			CubeCursor cubeCursor = bcv.getCubeCursor( );
			this.queryResultsId = executor.getQueryResultsId( );
			this.scope.put( "measure",
					this.scope,
					new JSMeasureAccessor( cubeCursor) );
			this.scope.put( "dimension",
					this.scope,
					new JSLevelAccessor( this.preparedQuery.getCubeQueryDefinition( ),
							bcv ) );

			return new CubeCursorImpl( cubeCursor,
					this.scope,
					this.preparedQuery.getCubeQueryDefinition( ) );

		}
		catch ( OLAPException e )
		{
			throw new DataException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryResults#getID()
	 */
	public String getID( )
	{
		return this.queryResultsId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryResults#close()
	 */
	public void close( ) throws BirtException
	{
		// TODO Auto-generated method stub

	}

}
