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
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
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
		if ( dataSet instanceof DerivedDataSetHandle )
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
			DataEngine dataEngine, IModelAdapter modelAdaptor ) throws BirtException
	{

		if ( dataSet == null )
			return;

		DataSourceHandle dataSource = dataSet.getDataSource( );
		if ( dataSource != null
				&& ( (DataEngineImpl) dataEngine ).getDataSourceRuntime( dataSource.getName( ) ) == null )
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
					defineDataSourceAndDataSet( childDataSet, dataEngine, modelAdaptor );
				}
			}

		}
		if ( dataSet instanceof DerivedDataSetHandle )
		{
			List inputDataSet = ( (DerivedDataSetHandle) dataSet ).getInputDataSets( );
			for ( int i = 0; i < inputDataSet.size( ); i++ )
			{
				defineDataSourceAndDataSet( (DataSetHandle) inputDataSet.get( i ),
						dataEngine,
						modelAdaptor );
			}
		}
		if(  ( (DataEngineImpl) dataEngine ).getDataSetDesign( dataSet.getQualifiedName( ) ) == null )
			dataEngine.defineDataSet( modelAdaptor.adaptDataSet( dataSet ) );
	}

}
