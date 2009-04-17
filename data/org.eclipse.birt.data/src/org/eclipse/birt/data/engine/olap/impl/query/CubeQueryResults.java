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

import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.script.JSLevelAccessor;
import org.eclipse.birt.data.engine.olap.script.JSMeasureAccessor;
import org.eclipse.birt.data.engine.script.ScriptConstants;
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
	private Map appContext;
	private StopSign stopSign;
	private IBaseQueryResults outResults;
	private ICubeCursor cubeCursor;
	private String name;
	
	/**
	 * 
	 * @param preparedQuery
	 * @param scope
	 */
	public CubeQueryResults( IBaseQueryResults outResults, PreparedCubeQuery preparedQuery, DataEngineSession session, Scriptable scope, DataEngineContext context, Map appContext )
	{
		this.preparedQuery = preparedQuery;
		this.scope = scope;
		this.context = context;
		this.session = session;
		this.appContext = appContext;
		this.queryResultsId = ((ICubeQueryDefinition)preparedQuery.getCubeQueryDefinition( )).getQueryResultsID( );
		this.outResults = outResults;
		this.stopSign = session.getStopSign( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.ICubeQueryResults#getCubeCursor()
	 */
	public ICubeCursor getCubeCursor( ) throws DataException
	{
		if ( this.cubeCursor != null )
			return this.cubeCursor;
		try
		{
			stopSign.start( );
			CubeQueryExecutor executor = new CubeQueryExecutor( this.outResults, (ICubeQueryDefinition)preparedQuery.getCubeQueryDefinition( ), this.session,
					this.scope,
					this.context );
			BirtCubeView bcv = new BirtCubeView( executor, appContext );
			CubeCursor cubeCursor = bcv.getCubeCursor( stopSign );
			String newResultSetId = executor.getQueryResultsId( );
			if ( newResultSetId != null )
			{
				this.queryResultsId = newResultSetId;
			}
			this.scope.put( ScriptConstants.MEASURE_SCRIPTABLE,
					this.scope,
					new JSMeasureAccessor( cubeCursor, bcv.getMeasureMapping( )) );
			this.scope.put( ScriptConstants.DIMENSION_SCRIPTABLE,
					this.scope,
					new JSLevelAccessor( (ICubeQueryDefinition) this.preparedQuery.getCubeQueryDefinition( ),
							bcv ) );

			this.cubeCursor = new CubeCursorImpl( outResults,
					cubeCursor,
					this.scope,
					session.getEngineContext( ).getScriptContext( ),
					(ICubeQueryDefinition) this.preparedQuery.getCubeQueryDefinition( ),
					bcv );
			return this.cubeCursor;

		}
		catch ( OLAPException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.ICubeQueryResults#cancel()
	 */
	public void cancel( )
	{
		stopSign.stop( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.INamedObject#setName(java.lang.String)
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.INamedObject#getName()
	 */
	public String getName( )
	{
		return name;
	}
	
	
}
