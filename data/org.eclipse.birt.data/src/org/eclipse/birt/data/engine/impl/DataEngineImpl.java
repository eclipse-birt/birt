/**************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 **************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.QueryResults;
import org.eclipse.birt.data.engine.script.JSDataSources;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of DataEngine class
 */
public class DataEngineImpl extends DataEngine
{
	private Scriptable 				sharedScope;

	// Map of data source name (string) to DataSourceRT, for defined data sources
	private HashMap					dataSources = new HashMap();
	
	// Map of data set name (string) to IBaseDataSetDesign, for defined data sets
	private HashMap					dataSetDesigns = new HashMap();
	
	/** Scripable object implementing "report.dataSources" array */
	private Scriptable				dataSourcesJSObject;

	// data engine context
	private DataEngineContext context;
	private DataSourceManager dataSourceManager;
	
	protected static Logger logger = Logger.getLogger( DataEngineImpl.class.getName( ) );

	/**
	 * Constructor to specify the DataEngine Context to use by the Data Engine
	 * for all related ReportQuery processing.
	 * 
	 * @param context
	 *            scope of Context: The global JavaScript scope shared by all
	 *            runtime components within a report sesssion. If this parameter
	 *            is null, a new standard top level scope will be created and
	 *            used.
	 */
	public DataEngineImpl( DataEngineContext context )
	{
		assert context != null;
		
		logger.entering( DataEngineImpl.class.getName( ),
				"DataEngineImpl",
				context );
		
		this.context = context;
		this.sharedScope = context.getJavaScriptScope( );
		
		if(context.getTmpdir( )!=null)
			DataEngineContextExt.getInstance( ).setTmpdir( context.getTmpdir( ) );
		
		Context cx = Context.enter( );
		if ( this.sharedScope == null )
		{
			this.sharedScope = new ImporterTopLevel( cx );
		}
		new CoreJavaScriptInitializer( ).initialize( cx, sharedScope );
		Context.exit( );
				
		dataSourceManager = new DataSourceManager( logger );
		
		logger.exiting( DataEngineImpl.class.getName( ), "DataEngineImpl" );
		logger.log( Level.INFO, "Data Engine starts up" );
	}

	/**
	 * @return context, the context used by this data engine instance
	 */
	public DataEngineContext getContext( )
	{
		return context;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.DataEngine#getQueryResults(int)
	 */
	public IQueryResults getQueryResults( String queryResultID ) throws DataException
	{
		if ( context.getMode( ) != DataEngineContext.MODE_PRESENTATION )
			throw new DataException( ResourceConstants.WRONG_STATUS );

		return new QueryResults( this.context, queryResultID );
	}
	
	/**
	 * Provides the definition of a data source to Data Engine. A data source
	 * must be defined using this method prior to preparing any report query
	 * that uses such data source. <br>
	 * Data sources are uniquely identified name. If specified data source has
	 * already been defined, its definition will be updated with the content of
	 * the provided DataSourceDesign
	 */
	public void defineDataSource( IBaseDataSourceDesign dataSource )
			throws DataException
	{
		logger.entering( DataEngineImpl.class.getName( ),
				"defineDataSource",
				dataSource == null ? null : dataSource.getName( ) );
		if ( dataSource == null )
		{
			NullPointerException e = new NullPointerException( "dataSource param cannot be null" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"defineDataSource",
					"dataSource param cannot be null",
					e );
			throw e;
		}
		if ( dataSources == null )
		{
			IllegalStateException e = new IllegalStateException( "DataEngine has been shutdown" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"defineDataSource",
					"DataEngine has been shutdown",
					e );
			throw e;
		}

		String name = dataSource.getName( );
		if ( name == null || name.length( ) == 0 )
		{
			IllegalArgumentException e=new IllegalArgumentException( "Data source has no name" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"defineDataSource",
					"Data source has no name",
					e );
			throw e; 
		}

		if ( logger.isLoggable( Level.FINE ) )
			logger.logp( Level.FINE,
					DataEngineImpl.class.getName( ),
					"defineDataSource",
					"DataEngine.defineDataSource: "
							+ LogUtil.toString( dataSource ) );
		
		// See if this data source is already defined; if so update its design
		Object existingDefn = dataSources.get( dataSource.getName( ) );
		if ( existingDefn != null )
			this.dataSourceManager.addDataSource( (DataSourceRuntime) existingDefn );
		
		// Create a corresponding runtime for the data source and add it to
		// the map
		DataSourceRuntime newDefn = DataSourceRuntime.newInstance( dataSource,
				this );
		dataSources.put( newDefn.getName( ), newDefn );
		
		logger.exiting( DataEngineImpl.class.getName( ), "defineDataSource" );
	}

	/**
	 * Provides the definition of a data set to Data Engine. A data set must be
	 * defined using this method prior to preparing any report query that uses such data set.
	 * <br>
	 * Data sets are uniquely identified name. If specified data set has already
	 * been defined, its definition will be updated with the content of the provided DataSetDesign
	 */
	public void defineDataSet( IBaseDataSetDesign dataSet )
			throws DataException
	{
		logger.entering( DataEngineImpl.class.getName( ),
				"defineDataSet",
				dataSet == null ? null : dataSet.getName( ) );
		if ( dataSet == null )
		{
			NullPointerException e = new NullPointerException( "dataSource param cannot be null" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"defineDataSet",
					"dataSource param cannot be null",
					e );
			throw e;
		}
		if ( dataSources == null )
		{
			IllegalStateException e = new IllegalStateException( "DataEngine has been shutdown" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"defineDataSet",
					"DataEngine has been shutdown",
					e );
			throw e;
		}
		String name = dataSet.getName( );
		if ( name == null || name.length( ) == 0 )
		{
			IllegalArgumentException e=new IllegalArgumentException( "Data source has no name" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"defineDataSet",
					"Data source has no name",
					e );
			throw e; 
		}

		if ( logger.isLoggable( Level.FINE ) )
			logger.logp( Level.FINE,
					DataEngineImpl.class.getName( ),
					"defineDataSet",
					"DataEngine.defineDataSet: " + LogUtil.toString( dataSet ) );
					
		if ( !(dataSet instanceof IJointDataSetDesign) )
		{
			// Sanity check: a data set must have a data source with the proper
			// type, and the data source must have be defined
			String dataSourceName = dataSet.getDataSourceName( );
			DataSourceRuntime dsource = this.getDataSourceRuntime( dataSourceName );
			if ( dsource == null )
			{
				DataException e = new DataException( ResourceConstants.UNDEFINED_DATA_SOURCE,
						dataSourceName );
				logger.logp( Level.WARNING,
						DataEngineImpl.class.getName( ),
						"defineDataSet",
						"Data source {" + dataSourceName + "} is not defined",
						e );
				throw e;
			}

			Class dSourceClass;
			if ( dataSet instanceof IOdaDataSetDesign )
				dSourceClass = IOdaDataSourceDesign.class;
			else if ( dataSet instanceof IScriptDataSetDesign )
				dSourceClass = IScriptDataSourceDesign.class;
			else
			{
				DataException e = new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE );
				logger.logp( Level.WARNING,
						DataEngineImpl.class.getName( ),
						"defineDataSet",
						"Unsupported data set type: " + dataSet.getName( ),
						e );
				throw e;
			}

			if ( !dSourceClass.isInstance( dsource.getDesign( ) ) )
			{
				DataException e = new DataException( ResourceConstants.UNSUPPORTED_DATASOURCE_TYPE,
						dsource.getName( ) );
				logger.logp( Level.WARNING,
						DataEngineImpl.class.getName( ),
						"defineDataSet",
						"Unsupported data source type: " + dsource.getName( ),
						e );
				throw e;
			}
		}
		dataSetDesigns.put( name, dataSet );
		logger.exiting( DataEngineImpl.class.getName( ), "defineDataSet" );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.DataEngine#clearCache(org.eclipse.birt.data.engine.api.IBaseDataSourceDesign,
	 *      org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	public void clearCache( IBaseDataSourceDesign dataSource,
			IBaseDataSetDesign dataSet ) throws BirtException
	{
		if ( dataSource == null || dataSet == null )
			return;

		DataSetCacheManager.getInstance( ).clearCache( dataSource, dataSet );
	}
	
	/**
	 * Returns the runtime defn of a data source. If data source is not found,
	 * returns null.
	 */
	DataSourceRuntime getDataSourceRuntime( String name )
	{
		return (DataSourceRuntime) dataSources.get( name );
	}

	/**
	 * Returns the design of a data set. If data set is not found, returns null.
	 */
	IBaseDataSetDesign getDataSetDesign( String name )
	{
		return (IBaseDataSetDesign) dataSetDesigns.get( name );
	}

	/**
	 * Verifies the elements of a report query spec
	 * and provides a hint to the query to prepare and optimize 
	 * an execution plan.
	 * The given querySpec could be a ReportQueryDefn 
	 * (raw data transform) spec generated by the factory 
	 * based on static definition found in a report design.
	 * <p> 
	 * This report query spec could be further refined by FPE 
	 * during engine execution after having resolved any related
	 * runtime condition.  This is probably not in BIRT Release 1.
	 * For example, a nested report item might not be rendered based
	 * on a runtime condition.  Thus its associated data expression
	 * could be removed from the report query defn given to 
	 * DtE to prepare.
	 * <p>
	 * During prepare, the DTE does not open a data set. 
	 * In other words, any before-open script on a data set will not be
	 * evaluated at this stage.  That could mean that certain query 
	 * plan generation must be deferred 
	 * to execution time since necessary result set metadata 
	 * might not be available at Prepare time.
	 * @param	querySpec	An IReportQueryDefn object that specifies
	 * 				the data access and data transforms services
	 * 				needed from DtE to produce a set of query results.
	 * @return		The PreparedQuery object that contains a prepared 
	 * 				ReportQuery ready for execution.
	 * @throws 		DataException if error occurs in Data Engine
	 */
	public IPreparedQuery prepare( IQueryDefinition querySpec )
		throws DataException
	{
	    return prepare( querySpec, null );
	}

	/*
	 * If user wants to use data set cache option, this method should be called
	 * to pass cache option information from the upper layer.
	 * 
	 * @see org.eclipse.birt.data.engine.api.DataEngine#prepare(org.eclipse.birt.data.engine.api.IQueryDefinition,
	 *      java.util.Map)
	 */
	public IPreparedQuery prepare( IQueryDefinition querySpec,
	        						Map appContext )
		throws DataException
	{
		if ( logger.isLoggable( Level.FINER ) )
			logger.entering( DataEngineImpl.class.getName( ),
					"prepare",
					LogUtil.toString( querySpec ) );
		if ( dataSources == null )
		{
			IllegalStateException e = new IllegalStateException( "DataEngine has been shutdown" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"prepare",
					"DataEngine has been shutdown",
					e );
			throw e;
		}

		if ( logger.isLoggable( Level.FINE ) )
			logger.fine( "Start to prepare query: "
					+ LogUtil.toString( querySpec ) );

		IPreparedQuery result = PreparedQueryUtil.newInstance( this,
				querySpec,
				appContext );
		
		logger.fine( "Finished preparing query." );
		logger.exiting( DataEngineImpl.class.getName( ), "prepare" );
		return result;
	}
	
	/**
	 * Provides a hint to DtE that the consumer is done with the given 
	 * data source connection, and 
	 * that its resources can be safely released as appropriate.
	 * This tells DtE that there is no more ReportQuery
	 * on a data set that uses such data source connection.
	 * The data source identified by name, should be one referenced 
	 * in one or more of the previously prepared ReportQuery.  
	 * Otherwise, it would simply return with no-op.
	 * <br>
	 * In BIRT Release 1, this method will likely be called by FPE 
	 * at the end of a report generation.
	 * @param	dataSourceName	The name of a data source connection.
	 */
	public void closeDataSource( String dataSourceName ) throws DataException
	{
		logger.entering( "DataEngineImpl",
				"closeDataSource",
				dataSourceName );
		if ( dataSources == null )
		{
			IllegalStateException e = new IllegalStateException( "DataEngine has been shutdown" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"closeDataSource",
					"DataEngine has been shutdown",
					e );
			throw e;
		}

		logger.logp( Level.FINE,
				DataEngineImpl.class.getName( ),
				"closeDataSource",
				"Close DataSource :" + dataSourceName );

		DataSourceRuntime ds = getDataSourceRuntime( dataSourceName );
		if ( ds != null )
		{
			closeDataSource( ds );
		}
		logger.exiting( DataEngineImpl.class.getName( ), "closeDataSource" );
	}

	/** Close the specified DataSourceDefn, if it is open */
	private static void closeDataSource( DataSourceRuntime ds )
			throws DataException
	{
		assert ds != null;
		if ( ds.isOpen( ) )
		{
			ds.beforeClose( );
			ds.closeOdiDataSource( );
			ds.afterClose( );
		}
	}

	/**
	 * Gets the shared Rhino scope used by this data engine
	 */
	public Scriptable getSharedScope( )
	{
		return sharedScope;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.DataEngine#shutdown()
	 */
	public void shutdown( )
	{
		logger.entering( "DataEngineImpl", "shutdown" );
		if ( dataSources == null )
		{
			// Already shutdown
			logger.fine( "The data engine has already been shutdown" );
			return;
		}
		// Close all open data sources
		Collection col = dataSources.values( );
		Iterator it = col.iterator( );
		while ( it.hasNext( ) )
		{
			DataSourceRuntime ds = (DataSourceRuntime) it.next( );
			try
			{
				closeDataSource( ds );
			}
			catch ( DataException e )
			{
				if ( logger.isLoggable( Level.FINE ) )
					logger.log( Level.FINE, "The data source ("
							+ ds + ") fails to shut down", e );
			}
		}
		
		this.dataSourceManager.close( );
		
		logger.logp( Level.INFO,
				DataEngineImpl.class.getName( ),
				"shutdown",
				"Data engine shuts down" );

		sharedScope = null;
		dataSetDesigns = null;
		dataSources = null;
		logger.exiting( DataEngineImpl.class.getName( ), "shutdown" );
	}

	/**
	 * Gets the Scriptable object that implements the "report.dataSources" array
	 */
	// TODO: Add this method to DataEngine api
	public Scriptable getDataSourcesScriptObject( )
	{
		if ( dataSources == null )
		{
			IllegalStateException e = new IllegalStateException( "DataEngine has been shutdown" );
			logger.logp( Level.WARNING,
					DataEngineImpl.class.getName( ),
					"closeDataSource",
					"DataEngine has been shutdown",
					e );
			throw e;
		}

		if ( dataSourcesJSObject == null )
		{
			dataSourcesJSObject = new JSDataSources( this.dataSources );
		}
		return dataSourcesJSObject;
	}
}