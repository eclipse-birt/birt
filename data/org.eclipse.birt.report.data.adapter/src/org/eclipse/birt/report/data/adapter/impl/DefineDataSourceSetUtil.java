/*
 *************************************************************************
 * Copyright (c) 2006, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.data.adapter.impl;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IShutdownListener;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IDataSetInterceptor;
import org.eclipse.birt.report.data.adapter.api.IDataSetInterceptorContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;

public class DefineDataSourceSetUtil
{

	public static void defineDataSourceAndDataSet( DataSetHandle dataSet,
			DataRequestSession sessionImpl ) throws BirtException
	{
		if ( dataSet == null )
			return;

		IModelAdapter modelAdaptor = sessionImpl.getModelAdaptor( );
		DataSourceHandle dataSource = dataSet.getDataSource( );
		if ( dataSource != null )
		{
			sessionImpl.defineDataSource( modelAdaptor.adaptDataSource( dataSource ) );
		}
		
		if ( dataSet instanceof JointDataSetHandle )
		{
			JointDataSetHandle jointDataSet = (JointDataSetHandle) dataSet;
			Iterator iter = jointDataSet.dataSetsIterator( );
			while ( iter.hasNext( ) )
			{
				DataSetHandle childDataSet = (DataSetHandle) iter.next( );
				if ( childDataSet != null )
				{
					DataSourceHandle childDataSource = childDataSet.getDataSource( );
					if ( childDataSource != null )
					{
						sessionImpl.defineDataSource( modelAdaptor.adaptDataSource( childDataSource ) );
					}
					defineDataSourceAndDataSet( childDataSet, sessionImpl );
				}
			}

		}
		else if ( dataSet instanceof DerivedDataSetHandle )
		{
			List inputDataSet = ( (DerivedDataSetHandle) dataSet ).getInputDataSets( );
			for ( int i = 0; i < inputDataSet.size( ); i++ )
			{
				defineDataSourceAndDataSet( (DataSetHandle) inputDataSet.get( i ),
						sessionImpl );
			}
		}
		sessionImpl.defineDataSet( modelAdaptor.adaptDataSet( dataSet ) );
	}

	public static void defineDataSourceAndDataSet( DataSetHandle dataSet,
			DataEngine dataEngine, IModelAdapter modelAdaptor,
			QueryExecutionHelper.DataSetHandleProcessContext context )
			throws BirtException
	{

		if ( dataSet == null )
			return;

		DataSourceHandle dataSource = dataSet.getDataSource( );
		if ( dataSource != null
				&& ( (DataEngineImpl) dataEngine ).getDataSourceRuntime( dataSource.getQualifiedName( ) ) == null )
		{
			dataEngine.defineDataSource( modelAdaptor.adaptDataSource( dataSource ) );
		}
		if ( dataSet instanceof JointDataSetHandle )
		{
			JointDataSetHandle jointDataSet = (JointDataSetHandle) dataSet;
			Iterator iter = jointDataSet.dataSetsIterator( );
			while ( iter.hasNext( ) )
			{
				DataSetHandle childDataSet = (DataSetHandle) iter.next( );
				if ( childDataSet != null )
				{
					DataSourceHandle childDataSource = childDataSet.getDataSource( );
					if ( childDataSource != null )
					{
						dataEngine.defineDataSource( modelAdaptor.adaptDataSource( childDataSource ) );
					}
					defineDataSourceAndDataSet( childDataSet, dataEngine, modelAdaptor, context );
				}
			}

		}
		else if ( dataSet instanceof DerivedDataSetHandle )
		{
			List inputDataSet = ( (DerivedDataSetHandle) dataSet ).getInputDataSets( );
			for ( int i = 0; i < inputDataSet.size( ); i++ )
			{
				defineDataSourceAndDataSet( (DataSetHandle) inputDataSet.get( i ),
						dataEngine,
						modelAdaptor, context );
			}
		}
		
		IBaseDataSetDesign design = ( (DataEngineImpl) dataEngine ).getDataSetDesign( dataSet.getQualifiedName( ) );
		if ( design == null )
		{
			design = modelAdaptor.adaptDataSet( dataSet );
			dataEngine.defineDataSet( design );
		}

		if ( context != null )
			context.process( design, dataSet );
	}
	
	/**
	 * prepare for transient query
	 * @param sessionContext
	 * @param dataEngine
	 * @param handle
	 * @param queryDefn
	 * @throws BirtException
	 */
	public static void prepareForTransientQuery( DataSessionContext dContext, DataEngineImpl dataEngine, DataSetHandle handle,
			IQueryDefinition queryDefn, IDataQueryDefinition[] registedQueries, IDataSetInterceptorContext interceptorContext ) throws BirtException 
	{
		if (interceptorContext == null)
			return;
		
		IBaseDataSetDesign design = null;
		if( handle == null )
		{
			if( queryDefn.getDataSetName( ) == null )
			{
				if( queryDefn.getSourceQuery( )!= null&& queryDefn.getSourceQuery( ) instanceof IQueryDefinition )
					design = dataEngine.getDataSetDesign( ((IQueryDefinition)queryDefn.getSourceQuery( )).getDataSetName( ) );
			}
			else
				design = dataEngine.getDataSetDesign( queryDefn.getDataSetName( ) );
		}
		else
		{
			design = dataEngine.getDataSetDesign( handle.getQualifiedName( ) );			
		}
		final IDataSetInterceptor dataSetInterceptor = DataSetInterceptorFinder.find( design );
		if ( dataSetInterceptor != null )
		{
			dataSetInterceptor.preDefineDataSet( dataEngine.getDataSourceDesign( design.getDataSourceName( ) ),
					design,
					queryDefn,
					registedQueries,
					dContext,
					dataEngine.getSession( ).getTempDir( ),
					interceptorContext );
			dataEngine.addShutdownListener( new IShutdownListener( ) {

				public void dataEngineShutdown( )
				{
					try
					{
						dataSetInterceptor.close( );
					}
					catch ( BirtException e )
					{
					}
				}
			} );
			return;
		}
		
		if ( handle instanceof JointDataSetHandle )
		{
			JointDataSetHandle jointDataSet = (JointDataSetHandle) handle;
			Iterator iter = jointDataSet.dataSetsIterator( );
			while ( iter.hasNext( ) )
			{
				DataSetHandle childDataSet = (DataSetHandle) iter.next( );
				if ( childDataSet != null )
				{
					prepareForTransientQuery( dContext, dataEngine, childDataSet, queryDefn, registedQueries, interceptorContext );
				}
			}

		}
		if ( handle instanceof DerivedDataSetHandle )
		{
			List<DataSetHandle>  inputDataSet = ( (DerivedDataSetHandle) handle ).getInputDataSets( );
			for ( int i = 0; i < inputDataSet.size( ); i++ )
			{
				prepareForTransientQuery( dContext, dataEngine, inputDataSet.get(i), queryDefn, registedQueries, interceptorContext );
			}
		}
	}

}
