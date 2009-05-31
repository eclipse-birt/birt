/**************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;


public class DataSetDesignHelper
{

	protected static Logger logger = Logger.getLogger( DataSetDesignHelper.class.getName( ) );
	private static Object delegateObject = null;
	
	static
	{
		try
		{
			delegateObject = Thread.currentThread( )
					.getContextClassLoader( )
					.loadClass( "org.eclipse.birt.data.engine.impl.DataSetDesignHelperDelegate" )
					.newInstance( );
		}
		catch ( ClassNotFoundException e )
		{
		}
		catch ( InstantiationException e )
		{
		}
		catch ( IllegalAccessException e )
		{
		}
		catch ( SecurityException e )
		{
		}
	}

	public static void vailidateDataSetDesign( IBaseDataSetDesign design,
			Map dataSoureRuntime ) throws DataException
	{
		if ( delegateObject != null )
		{
			Method method = getMethod( "vailidateDataSetDesign",
					delegateObject.getClass( ),
					new Class[]{
							IBaseDataSetDesign.class, Map.class
					} );
			invokeMethod( method, delegateObject, new Object[]{
					design, dataSoureRuntime
			} );
			return;
		}
		if ( !( design instanceof IJointDataSetDesign ) )
		{
			// Sanity check: a data set must have a data source with the proper
			// type, and the data source must have be defined
			String dataSourceName = design.getDataSourceName( );
			DataSourceRuntime dsource = (DataSourceRuntime) dataSoureRuntime.get( dataSourceName );
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
			if ( design instanceof IOdaDataSetDesign )
				dSourceClass = IOdaDataSourceDesign.class;
			else if ( design instanceof IScriptDataSetDesign )
				dSourceClass = IScriptDataSourceDesign.class;
			else
			{
				DataException e = new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE );
				logger.logp( Level.WARNING,
						DataEngineImpl.class.getName( ),
						"defineDataSet",
						"Unsupported data set type: " + design.getName( ),
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
	}
	
	public static  DataSetRuntime createExtenalInstance( IBaseDataSetDesign dataSetDefn,
			IQueryExecutor queryExecutor, DataEngineSession session )
	{
		if ( delegateObject != null )
		{
			Method method = getMethod( "createExtenalInstance",
					delegateObject.getClass( ),
					new Class[]{
							IBaseDataSetDesign.class,
							IQueryExecutor.class,
							DataEngineSession.class
					} );
			return (DataSetRuntime) invokeMethod( method,
					delegateObject,
					new Object[]{
							dataSetDefn, queryExecutor, session
					} );
		}
		return null;
	}
	
	public static IPreparedQuery createPreparedQueryInstance( IBaseDataSetDesign des, DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, Map appContext ) throws DataException
	{
		if ( delegateObject != null )
		{
			Method method = getMethod( "createPreparedQueryInstance",
					delegateObject.getClass( ),
					new Class[]{
							IBaseDataSetDesign.class,
							DataEngineImpl.class,
							IQueryDefinition.class,
							Map.class
					} );
			return (IPreparedQuery) invokeMethod( method,
					delegateObject,
					new Object[]{
							des, dataEngine, queryDefn, appContext
					} );
		}
		return null;
	}
	
	public static IBaseDataSetDesign createAdapter( IBaseDataSetDesign dataSetDesign )
	{
		if ( delegateObject != null )
		{
			Method method = getMethod( "createAdapter",
					delegateObject.getClass( ),
					new Class[]{
						IBaseDataSetDesign.class
					} );
			return (IBaseDataSetDesign) invokeMethod( method,
					delegateObject,
					new Object[]{
						dataSetDesign
					} );
		}
		return null;
	}
	
	public static IResultMetaData getResultMetaData( IBaseQueryDefinition baseQueryDefn, IQuery odiQuery ) throws DataException
	{
		if ( delegateObject != null )
		{
			Method method = getMethod( "getResultMetaData",
					delegateObject.getClass( ),
					new Class[]{
							IBaseQueryDefinition.class, IQuery.class
					} );
			return (IResultMetaData) invokeMethod( method,
					delegateObject,
					new Object[]{
							baseQueryDefn, odiQuery
					} );
		}
		return null;
	}
	
	public static IResultClass getResultClass( IQuery odiQuery )
	{
		if ( delegateObject != null )
		{
			Method method = getMethod( "getResultClass",
					delegateObject.getClass( ),
					new Class[]{
						IQuery.class
					} );
			return (IResultClass) invokeMethod( method,
					delegateObject,
					new Object[]{
						odiQuery
					} );
		}
		return null;
	}
	
	/**
	 * 
	 * @param methodName
	 * @param targetClass
	 * @param argument
	 * @return
	 */
	private static Method getMethod(String methodName, Class targetClass, Class[] argument) 
	{
		assert methodName != null;
		assert targetClass != null;
		assert argument != null;

		try
		{
			return targetClass.getMethod( methodName, argument );
		}
		catch ( SecurityException e )
		{
		}
		catch ( NoSuchMethodException e )
		{
		}
		return null;
	}
	
	/**
	 * Invoke a method.
	 * 
	 * @param method
	 * @param targetObject
	 * @param argument
	 */
	private static Object invokeMethod( Method method, Object targetObject, Object[] argument )
	{
		try
		{
			return method.invoke( targetObject, argument );
		}
		catch ( IllegalArgumentException e )
		{
		}
		catch ( IllegalAccessException e )
		{
		}
		catch ( InvocationTargetException e )
		{
		}
		return null;
	}
}
