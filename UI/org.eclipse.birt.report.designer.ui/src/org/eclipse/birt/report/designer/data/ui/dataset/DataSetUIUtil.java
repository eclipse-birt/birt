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

import java.util.Iterator;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

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
		if ( dataSetHandle instanceof OdaDataSetHandle )
			dataSetHandle.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP )
					.clearValue( );
		updateColumnCache( dataSetHandle );

	}
	
	/**
	 * Save the column meta data to data set handle.
	 * 
	 * @param dataSetHandle
	 * @param items
	 * @throws SemanticException
	 */
	public static void updateColumnCache( DataSetHandle dataSetHandle,
			boolean holdEvent )
			throws SemanticException
	{
		DataSessionContext context = null;
		try
		{
			context = new DataSessionContext( DataEngineContext.DIRECT_PRESENTATION,
					dataSetHandle.getRoot( ),
					null );
			DataRequestSession drSession = DataRequestSession.newSession( context );
			drSession.refreshMetaData( dataSetHandle, holdEvent );
			drSession.shutdown( );
		}
		catch ( BirtException e )
		{
			logger.entering( DataSetUIUtil.class.getName( ),
					"updateColumnCache",
					new Object[]{
						e
					} );
		}
	}
	
	/**
	 * Add this method according to GUI's requirement.This method is only for temporarily usage.
	 * @param dataSetHandle
	 * @return
	 * @throws SemanticException
	 * @deprecated
	 */
	public static CachedMetaDataHandle getCachedMetaDataHandle( DataSetHandle dataSetHandle ) throws SemanticException
	{
		if( dataSetHandle.getCachedMetaDataHandle( ) == null )
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
		
		return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY;
	}
	
	public static void setExpressionButtonImage( Button button )
	{
		GridData gd = new GridData( );
		gd.widthHint = 20;
		gd.heightHint = 20;
		button.setLayoutData( gd );

		String symbolicName;
		if ( button.isEnabled( ) )
			symbolicName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_BUILDERS;
		else
			symbolicName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_BUILDERS;

		Image image = ReportPlatformUIImages.getImage( symbolicName );
		if ( image != null )
		{
			image.setBackground( button.getBackground( ) );
			button.setImage( image );
		}
	}
}
