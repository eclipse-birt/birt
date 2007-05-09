/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.util;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;

public class OlapUtil
{

	public static final String Level_Year = "Year";
	public static final String Level_Qtr = "Quarter";
	public static final String Level_Month = "Month";
	public static final String Level_Week = "Week";
	public static final String Level_Day = "Day";

	public static LevelHandle getDateLevel( DimensionHandle dimension,ResultSetColumnHandle column,
			String type )
	{
		TabularLevelHandle level = null;
		try
		{
			if ( type.equals( Level_Year ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( dimension,"Year" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
				level.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR );
			}
			else if ( type.equals( Level_Qtr ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( dimension,"Quarter" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
				level.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER );
			}
			else if ( type.equals( Level_Month ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( dimension,"Month" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
				level.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH );
			}
			else if ( type.equals( Level_Week ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( dimension,"Week" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
				level.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK );
			}
			else if ( type.equals( Level_Day ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( dimension,"Day" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
				level.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY );
			}
			level.setColumnName( column.getColumnName( ) );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}

		return level;
	}

	public static String[] getDataFieldNames( DataSetHandle dataset )
	{
		String[] columns = new String[0];
		try
		{
			List columnList = DataUtil.getColumnList( dataset );
			columns = new String[columnList.size( )];
			for ( int i = 0; i < columnList.size( ); i++ )
			{
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get( i );
				columns[i] = resultSetColumn.getColumnName( );
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		return columns;
	}

	public static ResultSetColumnHandle[] getDataFields( DataSetHandle dataset )
	{
		ResultSetColumnHandle[] columns = new ResultSetColumnHandle[0];
		try
		{
			List columnList = DataUtil.getColumnList( dataset );
			columns = new ResultSetColumnHandle[columnList.size( )];
			for ( int i = 0; i < columnList.size( ); i++ )
			{
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get( i );
				columns[i] = resultSetColumn;
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		return columns;
	}

	public static ResultSetColumnHandle getDataField( DataSetHandle dataset,
			String fieldName )
	{
		try
		{
			List columnList = DataUtil.getColumnList( dataset );
			for ( int i = 0; i < columnList.size( ); i++ )
			{
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get( i );
				if ( fieldName.equals( resultSetColumn.getColumnName( ) ) )
					return resultSetColumn;
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		return null;
	}

	public static String[] getAvailableDatasetNames( )
	{
		SlotHandle slot = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getDataSets( );
		if ( slot == null || slot.getCount( ) == 0 )
			return new String[0];
		String[] datasets = new String[slot.getCount( )];
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			DataSetHandle dataset = (DataSetHandle) slot.get( i );
			datasets[i] = dataset.getName( );
		}
		return datasets;
	}

	public static DataSetHandle[] getAvailableDatasets( )
	{
		SlotHandle slot = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getDataSets( );
		if ( slot == null || slot.getCount( ) == 0 )
			return new DataSetHandle[0];
		DataSetHandle[] datasets = new DataSetHandle[slot.getCount( )];
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			datasets[i] = (DataSetHandle) slot.get( i );
		}
		return datasets;
	}

	public static int getIndexOfPrimaryDataset( DataSetHandle dataset )
	{
		SlotHandle slot = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getDataSets( );
		if ( slot == null || slot.getCount( ) == 0 )
			return -1;
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			if ( slot.get( i ) == dataset )
				return i;
		}
		return -1;
	}

	public static DataSetHandle getDataset( String datasetName )
	{
		SlotHandle slot = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getDataSets( );
		if ( slot == null || slot.getCount( ) == 0 )
			return null;
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			if ( ((DataSetHandle)slot.get( i )).getName( ).equals( datasetName ) )
				return (DataSetHandle)slot.get( i );
		}
		return null;
	}

}
