/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.script.JSDataSources;
import org.eclipse.birt.data.oda.util.driverconfig.ConfigManager;
import org.eclipse.birt.data.oda.util.driverconfig.DriverSetup;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of DataEngine class
 */
public class DataEngineImpl extends DataEngine
{
	private Scriptable 				sharedScope;
	private ExpressionCompiler		compiler;
	
	// Map of data source name (string) to DataSourceRT, for defined data sources
	private HashMap					dataSources = new HashMap();
	
	// Map of data set name (string) to IBaseDataSetDesign, for defined data sets
	private HashMap					dataSetDesigns = new HashMap();
	
	static private boolean			odaInitialized = false;
	
	/** Scripable object implementing "report.dataSources" array */
	private Scriptable				dataSourcesJSObject;
	
	/**
	 * Constructor to specify the JavaScript Context and shared scope
	 * to use by the Data Engine for all related ReportQuery processing.
	 * @param	sharedScope	The global JavaScript scope shared by all
	 * 					runtime components within a report sesssion. If this
	 *          parameter is null, a new standard top level scope will be created
	 *          and used.
	 * @param   homeDir Home directory of data engine; if not null, there must be a
	 *      subdirectory named "drivers" which contains all available ODA drivers. If null,
	 *      the data engine must be 
	 */
	public DataEngineImpl( Scriptable sharedScope, File homeDir )
	{
		this.sharedScope = sharedScope;
		if ( this.sharedScope == null )
		{
		    // No scope provided by the caller; create our own
		    Context cx = Context.enter();
		    this.sharedScope = cx.initStandardObjects();
		    Context.exit();
		}
		compiler = new ExpressionCompiler( );
		
		// ODA ConfigManager is static and should only be initialized once
		if ( ! odaInitialized )
		{
			initOda( homeDir );
			odaInitialized = true;
		}
	}
	
	// Initialize the ODA configuration 
	private static void initOda( File homeDir )
	{
		if ( homeDir == null )
		{
			// We are running in an Eclipse environment; find ODA drivers as plugins
			DriverSetup.setUp();
		}
		else
		{
			// Running in a server deloyment; find drivers under "drivers" direction
			// in home direction
			// TODO : this init mechanism is deprecated
			File driversHome = new File( homeDir, "drivers"); 
			ConfigManager.getInstance().setDriversHomeDir( driversHome );
		}
	}
	
	/**
	 * Creates a new top-level scope using given prototype
	 */
	static Scriptable createSubscope( Scriptable prototype )
	{
		Context cx = Context.enter();
		try
		{
	        Scriptable scope = cx.newObject( prototype );
	        scope.setPrototype( prototype );
	        scope.setParentScope( null );
	        return scope;
		}
		catch ( JavaScriptException e )
		{
			// Not expected; use provided scrope instead
			e.printStackTrace();
			return prototype;
		}
		finally
		{
			Context.exit();
		}
		
	}
	
	/**
	 * Provides the definition of a data source to Data Engine. A data source must be
	 * defined using this method prior to preparing any report query that uses such data source.
	 * <br>
	 * Data sources are uniquely identified name. If specified data source has already
	 * been defined, its definition will be updated with the content of the provided DataSourceDesign
	 */
	public void defineDataSource( IBaseDataSourceDesign dataSource ) throws DataException
	{
		if ( dataSource == null )
			throw new NullPointerException("dataSource param cannot be null ");
		String name = dataSource.getName();
		if ( name == null || name.length() == 0 )
			throw new IllegalArgumentException("Data source has no name");
		
	    // See if this data source is already defined; if so update its design
	    Object existingDefn = dataSources.get( dataSource.getName() );
	    if ( existingDefn != null )
	    {
	        (( DataSourceRuntime ) existingDefn).setDesign( dataSource );
	    }
	    else
	    {
	        // Create a corresponding runtime for the data source and add it to the map
	        DataSourceRuntime newDefn = DataSourceRuntime.newInstance( dataSource, this );
	        dataSources.put( newDefn.getName(), newDefn );
	    }
	}

	/**
	 * Provides the definition of a data set to Data Engine. A data set must be
	 * defined using this method prior to preparing any report query that uses such data set.
	 * <br>
	 * Data sets are uniquely identified name. If specified data set has already
	 * been defined, its definition will be updated with the content of the provided DataSetDesign
	 */
	public void defineDataSet( IBaseDataSetDesign dataSet ) throws DataException
	{
		if ( dataSet == null )
			throw new NullPointerException("dataSet param cannot be null ");
		String name = dataSet.getName();
		if ( name == null || name.length() == 0 )
			throw new IllegalArgumentException("Data Set has no name");
		
		// Sanity check: a data set must have a data source with the proper type, and
		// the data source must have be defined
		String dataSourceName = dataSet.getDataSourceName();
		DataSourceRuntime dsource = this.getDataSourceRuntime( dataSourceName );
		if ( dsource == null )
			throw new DataException( ResourceConstants.UNDEFINED_DATA_SOURCE, dataSourceName );
		
		Class dSourceClass;
		if ( dataSet instanceof IOdaDataSetDesign )
			dSourceClass = IOdaDataSourceDesign.class;
		else if ( dataSet instanceof IScriptDataSetDesign )
			dSourceClass = IScriptDataSourceDesign.class;
		else
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE );
		
		if ( ! dSourceClass.isInstance(dsource.getDesign()) )
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASOURCE_TYPE );
			
		dataSetDesigns.put( name, dataSet );
	}
	
	/**
	 * Returns the runtime defn of a data source. If data source is not found, returns null.
	 */
	DataSourceRuntime getDataSourceRuntime( String name )
	{
	    return ( DataSourceRuntime) dataSources.get( name );
	}

	/**
	 * Returns the design of a data set. If data set is not found, returns null.
	 */
	IBaseDataSetDesign getDataSetDesign( String name )
	{
	    return ( IBaseDataSetDesign) dataSetDesigns.get( name );
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
		PreparedDataSourceQuery result = PreparedDataSourceQuery.newInstance( this,
				querySpec );
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
	public void closeDataSource( String dataSourceName )
			throws DataException
	{
		DataSourceRuntime  ds = getDataSourceRuntime( dataSourceName );
		if ( ds != null )
		{
			closeDataSource( ds );
		}
	}

	/** Close the specified DataSourceDefn, if it is open */
	private static void closeDataSource( DataSourceRuntime ds ) throws DataException
	{
		assert ds != null;
		// Data source is open if it has an associated odi Data Source
		IDataSource odiDS = ds.getOdiDataSource();
		if ( odiDS != null )
		{
			ds.beforeClose();
			odiDS.close();
			ds.setOdiDataSource( null );
			ds.afterClose();
		}
	}
	
	/**
	 * Gets the shared Rhino scope used by this data engine
	 */
	public Scriptable getSharedScope()
	{
	    return sharedScope; 
	}
	
	/**
	 * Gets the expression compiler used by this data engine
	 */
	ExpressionCompiler getExpressionCompiler()
	{
		return compiler;
	}
	
	public void shutdown()
	{
		// Close all open data sources
		Collection col = dataSources.values();
		Iterator it = col.iterator();
		while (it.hasNext())
		{
			DataSourceRuntime ds = (DataSourceRuntime)it.next();
			try
			{
				closeDataSource( ds );
			}
			catch ( DataException e )
			{
				// TODO: log exception here
				e.printStackTrace();
			}
		}
		
		sharedScope = null;
		dataSetDesigns = null;
		dataSources = null;
		compiler = null;
	}
	
	/**
	 * Gets the Scriptable object that implements the "report.dataSources" array
	 */
	// TODO: Add this method to DataEngine api 
	public Scriptable getDataSourcesScriptObject()
	{
		if ( dataSourcesJSObject == null )
		{
			dataSourcesJSObject = new JSDataSources( this.dataSources );
		}
		return dataSourcesJSObject;
	}
}
