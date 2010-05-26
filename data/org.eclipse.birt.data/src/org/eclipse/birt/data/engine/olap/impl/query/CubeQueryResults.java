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

import java.io.IOException;
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
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.document.CubeRADocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
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

	protected ICubeQueryDefinition cubeQueryDefinition;
	private Scriptable scope;
	protected DataEngineContext context;
	protected DataEngineSession session;
	private String queryResultsId;
	protected Map appContext;
	private StopSign stopSign;
	private IBaseQueryResults outResults;
	protected ICubeCursor cubeCursor;
	private String name;
	private PreparedCubeQuery preparedQuery;
	
	/**
	 * 
	 * @param preparedQuery
	 * @param scope
	 */
	public CubeQueryResults( IBaseQueryResults outResults, PreparedCubeQuery preparedQuery, DataEngineSession session, Scriptable scope, DataEngineContext context, Map appContext )
	{
		this.cubeQueryDefinition = (ICubeQueryDefinition)preparedQuery.getCubeQueryDefinition( );
		this.preparedQuery = preparedQuery;
		this.scope = scope;
		this.context = context;
		this.session = session;
		this.appContext = appContext;
		this.queryResultsId = cubeQueryDefinition.getQueryResultsID( );
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
			CubeQueryExecutor executor = new CubeQueryExecutor( this.outResults, cubeQueryDefinition, this.session,
					this.scope,
					this.context );
			executor.getFacttableBasedFilterHelpers( ).addAll( this.preparedQuery.getInternalFilters( ) );
			
			IDocumentManager documentManager = getDocumentManager( executor );
			ICube cube = loadCube( documentManager, executor );
			
			BirtCubeView bcv = new BirtCubeView( executor, cube, appContext );		
			CubeCursor cubeCursor = bcv.getCubeCursor( stopSign, cube );		
			cube.close( );
			
			String newResultSetId = executor.getQueryResultsId( );
			if ( newResultSetId != null )
			{
				this.queryResultsId = newResultSetId;
			}
			this.scope.put( ScriptConstants.MEASURE_SCRIPTABLE,
					this.scope,
					new JSMeasureAccessor( cubeCursor, bcv.getMeasureMapping( ) ) );
			this.scope.put( ScriptConstants.DIMENSION_SCRIPTABLE,
					this.scope,
					new JSLevelAccessor( this.cubeQueryDefinition, bcv ) );

			this.cubeCursor = new CubeCursorImpl( outResults,
					cubeCursor,
					this.scope,
					session.getEngineContext( ).getScriptContext( ),
					cubeQueryDefinition,
					bcv );
			return this.cubeCursor;

		}
		catch ( OLAPException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
	
	/**
	 * Get the document manager.
	 * 
	 * @param executor
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDocumentManager getDocumentManager( CubeQueryExecutor executor )
			throws DataException, IOException
	{
		IDocumentManager manager = null;
		if ( preparedQuery.isFromDataMart( ) )
		{
			//cube should be always loaded from data mart, nothing to do with MODE
			return CubeRADocumentManagerFactory.createRADocumentManager( executor.getCubeQueryDefinition( ).getName( ), 
					executor.getContext( ).getDocReader( ) );
		}
		if ( executor.getContext( ).getMode( ) == DataEngineContext.DIRECT_PRESENTATION
				|| executor.getContext( ).getMode( ) == DataEngineContext.MODE_GENERATION )
		{
			manager = DocManagerMap.getDocManagerMap( )
					.get( String.valueOf( executor.getSession( )
							.getEngine( )
							.hashCode( ) ),
							executor.getSession( ).getTempDir( )
									+ executor.getSession( ).getEngine( ).hashCode( ) );
		}
		if ( manager != null )
		{
			if( manager.exist( NamingUtil.getCubeDocName( executor.getCubeQueryDefinition( ).getName( ) ) ))
				return manager;
		}
		return CubeRADocumentManagerFactory.createRADocumentManager( executor.getCubeQueryDefinition( ).getName( ), 
				executor.getContext( ).getDocReader( ) );
	}

	/**
	 * 
	 * @param cubeName
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private ICube loadCube( IDocumentManager documentManager, CubeQueryExecutor executor ) throws DataException,
			IOException
	{
		ICube cube = null;
		
		cube = CubeQueryExecutorHelper.loadCube( executor.getCubeQueryDefinition( )
				.getName( ),
				documentManager,
				executor.getSession( ).getStopSign( ) );

		return cube;
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
