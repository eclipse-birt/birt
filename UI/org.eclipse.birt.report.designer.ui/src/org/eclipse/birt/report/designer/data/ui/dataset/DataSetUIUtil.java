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
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

import com.actuate.birt.report.model.api.DataMartHandle;

/**
 * The utility class.
 */
public final class DataSetUIUtil
{
	// logger instance
	private static Logger logger = Logger.getLogger( DataSetUIUtil.class.getName( ) );
	
	/**
	 * Update column cache without holding events
	 * 
	 * @param dataSetHandle
	 * @throws SemanticException
	 */
	public static void updateColumnCache( DataSetHandle dataSetHandle )
			throws SemanticException
	{
		updateColumnCache( dataSetHandle, false );
	}
	
	/**
	 * Update column cache with clean the resultset property
	 * 
	 * @param dataSetHandle
	 * @throws SemanticException
	 */
	public static void updateColumnCacheAfterCleanRs(
			DataSetHandle dataSetHandle ) throws SemanticException
	{
		if ( dataSetHandle.getCachedMetaDataHandle( ) != null
				&& dataSetHandle.getCachedMetaDataHandle( ).getResultSet( ) != null )
			dataSetHandle.getCachedMetaDataHandle( )
					.getResultSet( )
					.clearValue( );
		if ( dataSetHandle instanceof OdaDataSetHandle )
		{
			if ( dataSetHandle.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP )
					.isLocal( ) )
				dataSetHandle.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP )
						.setValue( new ArrayList( ) );
		}
		updateColumnCache( dataSetHandle );

	}
	
	/**
	 * Save the column meta data to data set handle.
	 * 
	 * @param dataSetHandle
	 * @param holdEvent
	 */
	public static void updateColumnCache( DataSetHandle dataSetHandle,
			boolean holdEvent )
	{
		try
		{
			if ( dataSetHandle.getModuleHandle( ) instanceof ReportDesignHandle )
			{
				EngineConfig ec = new EngineConfig( );
				ReportEngine engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( ec );

				ReportDesignHandle copy = (ReportDesignHandle) ( dataSetHandle.getModuleHandle( )
						.copy( ).getHandle( null ) );

				DummyEngineTask engineTask = new DummyEngineTask( engine,
						new ReportEngineHelper( engine ).openReportDesign( copy ),
						copy );

				DataRequestSession session = engineTask.getDataSession( );

				Map appContext = new HashMap( );
				appContext.put( DataEngine.MEMORY_DATA_SET_CACHE,
						Integer.valueOf( dataSetHandle.getRowFetchLimit( ) ) );

				appContext.put( ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS,
						createResourceIdentifiers( ) );

				engineTask.setAppContext( appContext );
				engineTask.run( );

				DataService.getInstance( ).registerSession( dataSetHandle,
						session );
				session.refreshMetaData( dataSetHandle, holdEvent );
				engineTask.close( );
				engine.destroy( );
			}
			else if ( dataSetHandle.getModuleHandle( ) instanceof DataMartHandle )
			{
				DataSessionContext context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
						dataSetHandle.getModuleHandle( ) );

				Map appContext = new HashMap( );
				
				appContext.put( DataEngine.MEMORY_DATA_SET_CACHE,
						Integer.valueOf( dataSetHandle.getRowFetchLimit( ) ) );
				appContext.put( ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS,
						createResourceIdentifiers( ) );	
				
				context.setAppContext( appContext );

				DataRequestSession session = DataRequestSession.newSession( context );
				session.refreshMetaData( dataSetHandle, holdEvent );
				session.shutdown( );
			}
			else
			{
				DataSessionContext context = new DataSessionContext( DataEngineContext.DIRECT_PRESENTATION,
						dataSetHandle.getRoot( ),
						null );
				DataRequestSession drSession = DataRequestSession.newSession( context );
				drSession.refreshMetaData( dataSetHandle, holdEvent );
				drSession.shutdown( );
			}
		}
		catch ( BirtException ex )
		{
			logger.entering( DataSetUIUtil.class.getName( ),
					"updateColumnCache", //$NON-NLS-1$
					new Object[]{
						ex
					} );
		}
	}

	public static ResourceIdentifiers createResourceIdentifiers( )
	{
		ResourceIdentifiers ri = new ResourceIdentifiers( );
		ri.setDesignResourceBaseURI( getReportDesignPath( ) );
		ri.setApplResourceBaseURI( getBIRTResourcePath( ) );
		return ri;
	}

	/**
	 * Gets the BIRT resource path
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI getReportDesignPath( )
	{
		if ( Utility.getReportModuleHandle( ) == null
				|| Utility.getReportModuleHandle( ).getSystemId( ) == null )
		{
			return null;
		}
		try
		{
			return new URI( Utility.getReportModuleHandle( )
					.getSystemId( )
					.getPath( ) );
		}
		catch ( URISyntaxException e )
		{
			return null;
		}
	}

	/**
	 * Gets the report design file path
	 * 
	 * @return
	 */
	public static URI getBIRTResourcePath( )
	{
		try
		{
			return new URI( encode( ReportPlugin.getDefault( )
					.getResourceFolder( ) ) );
		}
		catch ( URISyntaxException e )
		{
			return null;
		}
	}

	private static String encode( String location )
	{
		try
		{
			return new File( location ).toURI( )
					.toASCIIString( )
					.replace( new File( "" ).toURI( ).toASCIIString( ), "" ); //$NON-NLS-1$//$NON-NLS-2$
		}
		catch ( Exception e )
		{
			return location;
		}
	}
	
	/**
	 * Add this method according to GUI's requirement.This method is only for temporarily usage.
	 * @param dataSetHandle
	 * @return
	 * @throws SemanticException
	 * @deprecated
	 */
	public static CachedMetaDataHandle getCachedMetaDataHandle(
			DataSetHandle dataSetHandle ) throws SemanticException
	{
		if ( !hasMetaData( dataSetHandle ) )
		{
			updateColumnCache( dataSetHandle, true );
		}

		return dataSetHandle.getCachedMetaDataHandle( );
	}
	
	/**
	 * Whether there is cached metadata in datasetHandle. The current status of
	 * datasetHandle will be processed, we won's do the refresh to retrieve the
	 * metadata. If the cached metadata handle is null or metadata handle is
	 * empty, return false.
	 * 
	 * @param dataSetHandle
	 * @return
	 */
	public static boolean hasMetaData( DataSetHandle dataSetHandle )
	{
		CachedMetaDataHandle metaData = dataSetHandle.getCachedMetaDataHandle( );
		if ( metaData == null )
			return false;
		else
		{
			Iterator iter = metaData.getResultSet( ).iterator( );
			if ( iter.hasNext( ) )
				return true;
			else
				return false;
		}
	}
	
	/**
	 * Map oda data type to model data type.
	 * 
	 * @param modelDataType
	 * @return
	 */
	public static String toModelDataType( int modelDataType )
	{
		if ( modelDataType == DataType.INTEGER_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER ;
		else if ( modelDataType == DataType.STRING_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
		else if ( modelDataType == DataType.DATE_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
		else if ( modelDataType == DataType.DECIMAL_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL;
		else if ( modelDataType == DataType.DOUBLE_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT;
		else if ( modelDataType == DataType.SQL_DATE_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATE;
		else if ( modelDataType == DataType.SQL_TIME_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_TIME;
		else if( modelDataType == DataType.BOOLEAN_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN;
		else if( modelDataType == DataType.JAVA_OBJECT_TYPE )
			return DesignChoiceConstants.COLUMN_DATA_TYPE_JAVA_OBJECT;
		
		return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY;
	}

	/**
	 * clear the property binding in dataset to disable it when run the query
	 * 
	 * @param dsHandle
	 * @param dataSetMap
	 * @param dataSourceMap
	 * @throws SemanticException
	 */
	public static void clearPropertyBindingMap( DataSetHandle dsHandle,
			Map dataSetMap, Map dataSourceMap ) throws SemanticException
	{
		if( dsHandle.getExtends( ) != null )
		{
			return;
		}
		if ( dsHandle instanceof JointDataSetHandle )
		{
			Iterator iter = ( (JointDataSetHandle) dsHandle ).dataSetsIterator( );
			while ( iter.hasNext( ) )
			{
				DataSetHandle ds = (DataSetHandle) iter.next( );
				if ( dsHandle != null )
				{
					clearPropertyBindingMap( ds, dataSetMap, dataSourceMap );
				}
			}
		}
		else if ( dsHandle instanceof OdaDataSetHandle )
		{
			List dataSetBindingList = dsHandle.getPropertyBindings( );
			List dataSourceBindingList = dsHandle.getDataSource( )
					.getPropertyBindings( );

			if ( !dataSetBindingList.isEmpty( ) )
				dataSetMap.put( dsHandle.getName( ), dataSetBindingList );
			if ( !dataSourceBindingList.isEmpty( ) )
				dataSourceMap.put( dsHandle.getDataSource( ).getName( ),
						dataSourceBindingList );

			for ( int i = 0; i < dataSetBindingList.size( ); i++ )
			{
				PropertyBinding binding = (PropertyBinding) dataSetBindingList.get( i );
				dsHandle.setPropertyBinding( binding.getName( ),
						(Expression) null );
			}
			for ( int i = 0; i < dataSourceBindingList.size( ); i++ )
			{
				PropertyBinding binding = (PropertyBinding) dataSourceBindingList.get( i );
				dsHandle.getDataSource( )
						.setPropertyBinding( binding.getName( ),
								(Expression) null );
			}
		}
	}
	
	/**
	 * reset the property binding in dataset.
	 * @param dsHandle
	 * @param dataSetMap
	 * @param dataSourceMap
	 * @throws SemanticException
	 */
	public static void resetPropertyBinding( DataSetHandle dsHandle, Map dataSetMap,
			Map dataSourceMap ) throws SemanticException
	{
		if ( dsHandle.getExtends( ) != null )
		{
			return;
		}
		if ( dsHandle instanceof JointDataSetHandle )
		{
			Iterator iter = ( (JointDataSetHandle) dsHandle ).dataSetsIterator( );
			while ( iter.hasNext( ) )
			{
				DataSetHandle ds = (DataSetHandle) iter.next( );
				if ( dsHandle != null )
				{
					resetPropertyBinding( ds, dataSetMap, dataSourceMap );
				}
			}
		}
		else
		{
			if ( dsHandle instanceof OdaDataSetHandle )
			{
				if ( dataSetMap.get( dsHandle.getName( ) ) != null )
				{
					List pList = (List) dataSetMap.get( dsHandle.getName( ) );
					
					for ( int i = 0; i < pList.size( ); i++ )
					{
						PropertyBinding binding = (PropertyBinding) pList.get( i );
						dsHandle.setPropertyBinding( binding.getName( ),
								binding.getValue( ) );
					}
				}
				if ( dataSourceMap.get( dsHandle.getDataSource( ).getName( ) ) != null )
				{
					List pList = (List) dataSourceMap.get( dsHandle.getDataSource( )
							.getName( ) );
					for ( int i = 0; i < pList.size( ); i++ )
					{
						PropertyBinding binding = (PropertyBinding) pList.get( i );
						dsHandle.getDataSource( )
								.setPropertyBinding( binding.getName( ),
										binding.getValue( ) );
					}
				}
			}
		}
	}

}
