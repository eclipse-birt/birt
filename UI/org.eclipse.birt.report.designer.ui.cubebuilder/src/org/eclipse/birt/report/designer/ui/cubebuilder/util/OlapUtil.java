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
import org.eclipse.birt.report.model.api.olap.LevelHandle;

public class OlapUtil
{

	public static final String Level_Year = "Year";
	public static final String Level_Qtr = "Quater";
	public static final String Level_Month = "Month";
	public static final String Level_Week = "Week";
	public static final String Level_Day = "Day";

	public static LevelHandle getDateLevel( String type )
	{
		LevelHandle level = null;
		try
		{
			if ( type.equals( Level_Year ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( "Year" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
			}
			else if ( type.equals( Level_Qtr ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( "Quater" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
			}
			else if ( type.equals( Level_Month ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( "Month" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
			}
			else if ( type.equals( Level_Week ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( "Week" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
			}
			else if ( type.equals( Level_Day ) )
			{
				level = DesignElementFactory.getInstance( )
						.newTabularLevel( "Day" );
				level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
			}
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
			DataSetHandle dataset = (DataSetHandle) slot.get( i );
			datasets[i] = dataset;
		}
		return datasets;
	}

	public static int getIndexOfPrimaryDataset( DataSetHandle dataset )
	{
		SlotHandle slot = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getDataSets( );
		if ( slot == null || slot.getCount( ) == 0 || dataset == null )
			return -1;
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			DataSetHandle temp = (DataSetHandle) slot.get( i );
			if ( dataset == temp )
				return i;
		}
		return -1;
	}

	public static DataSetHandle getDataset( String datasetName )
	{
		SlotHandle slot = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getDataSets( );
		if ( slot == null || slot.getCount( ) == 0 || datasetName == null )
			return null;
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			DataSetHandle temp = (DataSetHandle) slot.get( i );
			if ( datasetName.equals( temp.getName( ) ) )
				return temp;
		}
		return null;
	}

}
