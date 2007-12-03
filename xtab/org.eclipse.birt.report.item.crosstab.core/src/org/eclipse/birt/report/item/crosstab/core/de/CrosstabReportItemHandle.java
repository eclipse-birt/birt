/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabReportItemTask;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * CrosstabReportItemHandle.
 */
public class CrosstabReportItemHandle extends AbstractCrosstabItemHandle implements
		ICrosstabReportItemConstants,
		ICrosstabConstants
{

	/**
	 * 
	 * @param handle
	 */
	CrosstabReportItemHandle( DesignElementHandle handle )
	{
		super( handle );
	}

	/**
	 * Gets the dimension value handle for the row where the given cell resides.
	 * 
	 * @param cell
	 *            the cell which the row contains
	 * @return the dimension value handle for the row where the cell resides
	 * @throws CrosstabException
	 */
	public org.eclipse.birt.report.model.api.DimensionHandle getRowHeight(
			CrosstabCellHandle cell ) throws CrosstabException
	{
		cell = CrosstabModelUtil.locateRowHeightCell( this, cell );

		if ( cell != null )
		{
			return cell.getHeight( );
		}

		throw new CrosstabException( this.getModelHandle( ).getElement( ),
				Messages.getString( "CrosstabReportItemHandle.error.locate.row.cell" ) ); //$NON-NLS-1$
	}

	/**
	 * Gets the dimension value handle for the column width where the cell
	 * resides.
	 * 
	 * @param cell
	 *            the cell which the column contains
	 * @return the dimension value handle for the column width where the cell
	 *         resides
	 * @throws CrosstabException
	 */
	public org.eclipse.birt.report.model.api.DimensionHandle getColumnWidth(
			CrosstabCellHandle cell ) throws CrosstabException
	{
		cell = CrosstabModelUtil.locateColumnWidthCell( this, cell );

		if ( cell != null )
		{
			return cell.getWidth( );
		}

		throw new CrosstabException( this.getModelHandle( ).getElement( ),
				Messages.getString( "CrosstabReportItemHandle.error.locate.column.cell" ) ); //$NON-NLS-1$
	}

	/**
	 * Sets the row height where the cell resides.
	 * 
	 * @param cell
	 * @param value
	 * @throws CrosstabException
	 */
	public void setRowHeight( CrosstabCellHandle cell, DimensionValue value )
			throws CrosstabException
	{
		cell = CrosstabModelUtil.locateRowHeightCell( this, cell );

		if ( cell != null )
		{
			try
			{
				cell.getHeight( ).setValue( value );
			}
			catch ( SemanticException e )
			{
				throw new CrosstabException( this.getModelHandle( )
						.getElement( ),
						Messages.getString( "CrosstabReportItemHandle.error.set.row.height" ), //$NON-NLS-1$
						e );
			}
		}
		else
		{
			throw new CrosstabException( this.getModelHandle( ).getElement( ),
					Messages.getString( "CrosstabReportItemHandle.error.locate.row.cell" ) ); //$NON-NLS-1$
		}
	}

	/**
	 * Sets the column width where the cell resides.
	 * 
	 * @param cell
	 * @param value
	 * @throws CrosstabException
	 */
	public void setColumnWidth( CrosstabCellHandle cell, DimensionValue value )
			throws CrosstabException
	{
		cell = CrosstabModelUtil.locateColumnWidthCell( this, cell );

		if ( cell != null )
		{
			try
			{
				cell.getWidth( ).setValue( value );
			}
			catch ( SemanticException e )
			{
				throw new CrosstabException( this.getModelHandle( )
						.getElement( ),
						Messages.getString( "CrosstabReportItemHandle.error.set.column.width" ), //$NON-NLS-1$
						e );
			}
		}
		else
		{
			throw new CrosstabException( this.getModelHandle( ).getElement( ),
					Messages.getString( "CrosstabReportItemHandle.error.locate.column.cell" ) ); //$NON-NLS-1$
		}
	}

	/**
	 * Gets the measures property handle.
	 * 
	 * @return measures property handle
	 */

	PropertyHandle getMeasuresProperty( )
	{
		return handle.getPropertyHandle( MEASURES_PROP );
	}

	/**
	 * Gets the rows property handle.
	 * 
	 * @return rows property handle
	 */

	PropertyHandle getRowsProperty( )
	{
		return handle.getPropertyHandle( ROWS_PROP );
	}

	/**
	 * Gets the columns property handle.
	 * 
	 * @return columns property handle
	 */

	PropertyHandle getColumnsProperty( )
	{
		return handle.getPropertyHandle( COLUMNS_PROP );
	}

	/**
	 * Gets the property handle for row/column crosstab views.The axis type can
	 * be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @return property handle for row/column crosstab view
	 */
	protected PropertyHandle getCrosstabViewProperty( int axisType )
	{
		switch ( axisType )
		{
			case ROW_AXIS_TYPE :
				return getRowsProperty( );
			case COLUMN_AXIS_TYPE :
				return getColumnsProperty( );
		}
		return null;
	}

	/**
	 * Gets the row/column crosstab view for this crosstab. The axis type can be
	 * either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 * @return
	 */
	public CrosstabViewHandle getCrosstabView( int axisType )
	{
		PropertyHandle propHandle = getCrosstabViewProperty( axisType );
		if ( propHandle == null || propHandle.getContentCount( ) <= 0 )
			return null;
		return (CrosstabViewHandle) CrosstabUtil.getReportItem( propHandle.getContent( 0 ),
				CROSSTAB_VIEW_EXTENSION_NAME );
	}

	/**
	 * Adds a row/column crosstab view into this crosstab.The axis type can be
	 * either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 * @return
	 * 
	 */
	public CrosstabViewHandle addCrosstabView( int axisType )
			throws SemanticException
	{
		PropertyHandle propHandle = getCrosstabViewProperty( axisType );
		if ( propHandle == null || propHandle.getContentCount( ) > 0 )
			return null;

		// create a crosstab view and add it
		ExtendedItemHandle extendedItem = CrosstabExtendedItemFactory.createCrosstabView( moduleHandle );
		propHandle.add( extendedItem );

		return (CrosstabViewHandle) CrosstabUtil.getReportItem( extendedItem );
	}

	/**
	 * Returns the caption text of this crosstab.
	 * 
	 * @return the caption text
	 */

	public String getCaption( )
	{
		return handle.getStringProperty( CAPTION_PROP );
	}

	/**
	 * Returns the resource key of the caption.
	 * 
	 * @return the resource key of the caption
	 */

	public String getCaptionKey( )
	{
		return handle.getStringProperty( CAPTION_ID_PROP );
	}

	/**
	 * Returns the measure direction of this crosstab. The return value is
	 * defined in <code>ICrosstabConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>MEASURE_DIRECTION_HORIZONTAL</code>
	 * <li><code>MEASURE_DIRECTION_VERTICAL</code>
	 * 
	 * </ul>
	 * 
	 * @return the measure direction of this crosstab
	 */

	public String getMeasureDirection( )
	{
		return handle.getStringProperty( MEASURE_DIRECTION_PROP );
	}

	/**
	 * Sets the measure direction of this crosstab. The input direction must be
	 * one of:
	 * 
	 * <ul>
	 * <li><code>MEASURE_DIRECTION_HORIZONTAL</code>
	 * <li><code>MEASURE_DIRECTION_VERTICAL</code>
	 * 
	 * @param direction
	 *            measure direction to set, must be one of above choices
	 * @throws SemanticException
	 */
	public void setMeasureDirection( String direction )
			throws SemanticException
	{
		CommandStack stack = getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemHandle.msg.change.measure.direction" ) ); //$NON-NLS-1$

		try
		{
			handle.setStringProperty( MEASURE_DIRECTION_PROP, direction );

			new CrosstabReportItemTask( this ).validateCrosstab( );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );
	}

	/**
	 * Sets the page layout of this crosstab. The given value is defined in
	 * <code>ICrosstabConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_LAYOUT_DOWN_THEN_OVER</code>
	 * <li><code>PAGE_LAYOUT_OVER_THEN_DOWN</code>
	 * 
	 * </ul>
	 */
	public void setPageLayout( String value ) throws SemanticException
	{
		handle.setProperty( PAGE_LAYOUT_PROP, value );
	}

	/**
	 * Sets if repeat crosstab row header for each page. The row header is
	 * normally on left of the detail area.
	 */
	public void setRepeatRowHeader( boolean value ) throws SemanticException
	{
		handle.setProperty( REPEAT_ROW_HEADER_PROP, Boolean.valueOf( value ) );
	}

	/**
	 * Sets if repeat crosstab column header for each page. The column header is
	 * normally on top of the detail area.
	 */
	public void setRepeatColumnHeader( boolean value ) throws SemanticException
	{
		handle.setProperty( REPEAT_COLUMN_HEADER_PROP, Boolean.valueOf( value ) );
	}

	/**
	 * Sets if hide measure header for this crosstab.
	 */
	public void setHideMeasureHeader( boolean value ) throws SemanticException
	{
		handle.setProperty( HIDE_MEASURE_HEADER_PROP, Boolean.valueOf( value ) );
	}

	/**
	 * Returns the page layout of this crosstab. The return value is defined in
	 * <code>ICrosstabConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_LAYOUT_DOWN_THEN_OVER</code>
	 * <li><code>PAGE_LAYOUT_OVER_THEN_DOWN</code>
	 * 
	 * </ul>
	 * 
	 * @return the page layout of this crosstab
	 */

	public String getPageLayout( )
	{
		return handle.getStringProperty( PAGE_LAYOUT_PROP );
	}

	/**
	 * Returns if repeat crosstab row header for each page. The row header is
	 * normally on left of the detail area.
	 * 
	 * @return
	 */
	public boolean isRepeatRowHeader( )
	{
		return handle.getBooleanProperty( REPEAT_ROW_HEADER_PROP );
	}

	/**
	 * Returns if repeat crosstab column header for each page. The column header
	 * is normally on top of the detail area.
	 * 
	 * @return
	 */
	public boolean isRepeatColumnHeader( )
	{
		return handle.getBooleanProperty( REPEAT_COLUMN_HEADER_PROP );
	}

	/**
	 * Returns if hide measure header for this crosstab.
	 * 
	 * @return
	 */
	public boolean isHideMeasureHeader( )
	{
		return handle.getBooleanProperty( HIDE_MEASURE_HEADER_PROP );
	}

	/**
	 * Gets the empty cell value of this crosstab.
	 * 
	 * @return the empty cell value
	 */

	public String getEmptyCellValue( )
	{
		return handle.getStringProperty( EMPTY_CELL_VALUE_PROP );
	}

	/**
	 * @since 2.3
	 */
	public CrosstabCellHandle getHeader( )
	{
		PropertyHandle headerHandle = handle.getPropertyHandle( HEADER_PROP );

		if ( headerHandle != null && headerHandle.getContentCount( ) > 0 )
		{
			return (CrosstabCellHandle) CrosstabUtil.getReportItem( headerHandle.getContent( 0 ) );
		}

		return null;
	}

	/**
	 * Gets the referred OLAP cube element.
	 * 
	 * @return the referred OLAP cube element
	 */

	public CubeHandle getCube( )
	{
		return ( (ReportItemHandle) handle ).getCube( );
	}

	/**
	 * 
	 * @param cube
	 * @throws SemanticException
	 */
	public void setCube( CubeHandle cube ) throws SemanticException
	{
		handle.setProperty( IReportItemModel.CUBE_PROP, cube );
	}

	/**
	 * Gets the name of referred OLAP cube element.
	 * 
	 * @return name of the referred OLAP cube element.
	 */
	public String getCubeName( )
	{
		return handle.getStringProperty( IReportItemModel.CUBE_PROP );
	}

	/**
	 * Finds a dimension view that refers a cube dimension element with the
	 * given name.
	 * 
	 * @param name
	 *            name of the cube dimension element to find
	 * @return dimension view if found, otherwise null
	 */
	public DimensionViewHandle getDimension( String name )
	{
		DimensionViewHandle dimensionView = findDimension( ROW_AXIS_TYPE, name );;
		if ( dimensionView != null )
			return dimensionView;
		dimensionView = findDimension( COLUMN_AXIS_TYPE, name );
		if ( dimensionView != null )
			return dimensionView;
		return null;
	}

	/**
	 * Gets the row/column dimension view that refers a cube dimension element
	 * with the given name. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @param name
	 *            name of the cube dimension element
	 * @return
	 */
	private DimensionViewHandle findDimension( int axisType, String name )
	{
		CrosstabViewHandle crosstabView = getCrosstabView( axisType );
		return crosstabView == null ? null : crosstabView.getDimension( name );
	}

	/**
	 * Gets the row/column dimension with the given index. The axis type can be
	 * either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @param index
	 *            a 0-based integer of the dimension position
	 * @return the dimension view handle if found, otherwise null
	 */
	public DimensionViewHandle getDimension( int axisType, int index )
	{
		CrosstabViewHandle crosstabView = getCrosstabView( axisType );
		return crosstabView == null ? null : crosstabView.getDimension( index );
	}

	/**
	 * Gets the row/column dimension count. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @return count of row/column dimension
	 */
	public int getDimensionCount( int axisType )
	{
		CrosstabViewHandle crosstabView = getCrosstabView( axisType );
		return crosstabView == null ? 0 : crosstabView.getDimensionCount( );
	}

	/**
	 * Finds a measure view that refers a cube measure element with the given
	 * name.
	 * 
	 * @param name
	 *            name of the cube measure element to find
	 * @return measure view if found, otherwise null
	 */
	public MeasureViewHandle getMeasure( String name )
	{
		for ( int i = 0; i < getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = getMeasure( i );
			if ( measureView != null )
			{
				String cubeMeasureName = measureView.getCubeMeasureName( );
				if ( ( cubeMeasureName != null && cubeMeasureName.equals( name ) )
						|| ( cubeMeasureName == null && name == null ) )
					return measureView;
			}
		}
		return null;
	}

	/**
	 * Gets the measure view with the given index. Position index is 0-based
	 * integer.
	 * 
	 * @param index
	 *            a 0-based integer of the measure position
	 * @return the measure view handle if found, otherwise null
	 */
	public MeasureViewHandle getMeasure( int index )
	{
		DesignElementHandle element = getMeasuresProperty( ).getContent( index );
		return (MeasureViewHandle) CrosstabUtil.getReportItem( element,
				MEASURE_VIEW_EXTENSION_NAME );
	}

	/**
	 * Gets the measure view count.
	 * 
	 * @return count of measure view
	 */
	public int getMeasureCount( )
	{
		return getMeasuresProperty( ).getContentCount( );
	}

	/**
	 * Inserts a row/column dimension into the given position. The axis type can
	 * be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param dimensionHandle
	 *            the OLAP dimension handle to use
	 * @param axisType
	 *            row/column axis type
	 * @param index
	 *            insert position, a 0-based integer
	 * @return
	 * @throws SemanticException
	 */
	public DimensionViewHandle insertDimension(
			DimensionHandle dimensionHandle, int axisType, int index )
			throws SemanticException
	{
		return new CrosstabReportItemTask( this ).insertDimension( dimensionHandle,
				axisType,
				index );
	}

	/**
	 * Inserts a measure into the given position. Position index is 0-based
	 * integer.
	 * 
	 * @param measureHandle
	 *            the OLAP measure handle to use
	 * @param index
	 *            insert position, a 0-based integer
	 * @return
	 * @throws SemanticException
	 */
	public MeasureViewHandle insertMeasure( MeasureHandle measureHandle,
			int index ) throws SemanticException
	{
		// if this measure handle has referred by an existing measure view,
		// then log error and do nothing
		if ( measureHandle != null
				&& getMeasure( measureHandle.getQualifiedName( ) ) != null )
		{
			logger.log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_MEASURE,
					measureHandle.getQualifiedName( ) );
			throw new CrosstabException( handle.getElement( ), new String[]{
					measureHandle.getQualifiedName( ),
					handle.getElement( ).getIdentifier( )
			}, MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_MEASURE );
		}

		CommandStack stack = getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemHandle.msg.insert.measure" ) ); //$NON-NLS-1$

		MeasureViewHandle mv = null;

		try
		{
			ExtendedItemHandle extendedItemHandle = CrosstabExtendedItemFactory.createMeasureView( moduleHandle,
					measureHandle );

			if ( extendedItemHandle != null )
			{
				getMeasuresProperty( ).add( extendedItemHandle, index );

				// validate possible aggregation cells
				new CrosstabReportItemTask( this ).validateCrosstab( );

				mv = (MeasureViewHandle) CrosstabUtil.getReportItem( extendedItemHandle );
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );

		return mv;
	}

	/**
	 * Removes a dimension view that refers a cube dimension name with the given
	 * name from the design tree.
	 * 
	 * @param name
	 *            name of the dimension view to remove
	 * @throws SemanticException
	 */
	public void removeDimension( String name ) throws SemanticException
	{
		new CrosstabReportItemTask( this ).removeDimension( name );
	}

	/**
	 * Removes a row/column dimension view in the given position. The axis type
	 * can be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @param index
	 *            the position index of the dimension to remove, 0-based integer
	 * @throws SemanticException
	 */
	public void removeDimension( int axisType, int index )
			throws SemanticException
	{
		new CrosstabReportItemTask( this ).removeDimension( axisType, index );
	}

	/**
	 * Removes a measure view with the given name from the design tree.
	 * 
	 * @param name
	 *            name of the measure view to remove
	 * @throws SemanticException
	 */
	public void removeMeasure( String name ) throws SemanticException
	{
		MeasureViewHandle measureView = getMeasure( name );
		if ( measureView == null )
		{
			logger.log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					name );
			throw new CrosstabException( handle.getElement( ), new String[]{
					name, handle.getElement( ).getIdentifier( )
			}, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND );
		}

		measureView.handle.drop( );
	}

	/**
	 * Removes a measure view in the given position.
	 * 
	 * @param index
	 *            the position index of the measure view to remove, 0-based
	 *            integer
	 * @throws SemanticException
	 */
	public void removeMeasure( int index ) throws SemanticException
	{
		getMeasuresProperty( ).drop( index );
	}

	/**
	 * Moves the dimension view with the given name to the target index in the
	 * target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param name
	 *            name of the dimension view to move
	 * @param targetAxisType
	 *            row/column axis type of the move target
	 * @param targetIndex
	 *            the position index of the move target
	 * @throws SemanticException
	 */
	public void pivotDimension( String name, int targetAxisType, int targetIndex )
			throws SemanticException
	{
		new CrosstabReportItemTask( this ).pivotDimension( name,
				targetAxisType,
				targetIndex );
	}

	/**
	 * Moves the dimension view in the source position of source row/column to
	 * the target index in the target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param srcAxisType
	 *            the source row/column axis type
	 * @param srcIndex
	 *            the source position index
	 * @param targetAxisType
	 *            row/column axis type of the move target
	 * @param targetIndex
	 *            the position index of the move target
	 * @throws SemanticException
	 */
	public void pivotDimension( int srcAxisType, int srcIndex,
			int targetAxisType, int targetIndex ) throws SemanticException
	{
		new CrosstabReportItemTask( this ).pivotDimension( srcAxisType,
				srcIndex,
				targetAxisType,
				targetIndex );
	}

	/**
	 * Gets the row/column grand total cell of this crosstab. The axis type can
	 * be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @return row/column grand total cell if set, otherwise null
	 */
	public CrosstabCellHandle getGrandTotal( int axisType )
	{
		CrosstabViewHandle crosstabView = getCrosstabView( axisType );
		return crosstabView == null ? null : crosstabView.getGrandTotal( );
	}

	/**
	 * Adds a row/column grand total to the crosstab if it is empty. The axis
	 * type can be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @return the row/column grand total of this crosstab
	 * 
	 */
	public CrosstabCellHandle addGrandTotal( int axisType )
			throws SemanticException
	{
		// this add grand total for all measures
		List measures = new ArrayList( );
		List functions = new ArrayList( );

		for ( int i = 0; i < getMeasureCount( ); i++ )
		{
			measures.add( getMeasure( i ) );
			functions.add( DEFAULT_MEASURE_FUNCTION );
		}

		return addGrandTotal( axisType, measures, functions );
	}

	/**
	 * Removes row/column grand total from crosstab if it is not empty,
	 * otherwise do nothing. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 */
	public void removeGrandTotal( int axisType ) throws SemanticException
	{
		new CrosstabReportItemTask( this ).removeGrandTotal( axisType );
	}

	/**
	 * Removes row/column grand total from crosstab on particular measure,
	 * otherwise do nothing. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 */
	public void removeGrandTotal( int axisType, int measureIndex )
			throws SemanticException
	{
		new CrosstabReportItemTask( this ).removeGrandTotal( axisType,
				measureIndex );
	}

	/**
	 * Gets the dimension value handle for the crosstab width.
	 * 
	 * @return crosstab width dimension value handle
	 */
	public org.eclipse.birt.report.model.api.DimensionHandle getWidth( )
	{
		return handle.getDimensionProperty( IReportItemModel.WIDTH_PROP );
	}

	/**
	 * Gets the dimension value handle for the crosstab height.
	 * 
	 * @return crosstab height dimension value handle
	 */
	public org.eclipse.birt.report.model.api.DimensionHandle getHeight( )
	{
		return handle.getDimensionProperty( IReportItemModel.HEIGHT_PROP );
	}

	/**
	 * Pivots a measure with the given name to the specified position.
	 * 
	 * @param name
	 * @param toIndex
	 * @throws SemanticException
	 */
	public void pivotMeasure( String name, int toIndex )
			throws SemanticException
	{
		MeasureViewHandle measureView = getMeasure( name );
		if ( measureView == null )
		{
			logger.log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					name );
			throw new CrosstabException( handle.getElement( ), new String[]{
					name, handle.getElement( ).getIdentifier( )
			}, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND );
		}

		measureView.handle.moveTo( toIndex );
	}

	/**
	 * Pivots a measure in the given position to the specified position.
	 * 
	 * @param fromIndex
	 * @param toIndex
	 * @throws SemanticException
	 */
	public void pivotMeasure( int fromIndex, int toIndex )
			throws SemanticException
	{
		MeasureViewHandle measureView = getMeasure( fromIndex );
		if ( measureView == null )
		{
			logger.log( Level.INFO,
					MessageConstants.CROSSTAB_EXCEPTION_MEASURE_NOT_FOUND,
					new Object[]{
							String.valueOf( fromIndex ),
							handle.getElement( ).getIdentifier( )
					} );
			return;
		}
		measureView.handle.moveTo( toIndex );
	}

	// /**
	// * Returns the mirrored starting level in specific axis.
	// *
	// * @return
	// */
	// public LevelHandle getMirroredStartingLevel( int axisType )
	// {
	// return new CrosstabReportItemTask( this )
	// .getMirroredStartingLevel( axisType );
	// }
	//
	// /**
	// * Sets mirrrored starting level property for specific axis.
	// */
	// public void setMirroredStartingLevel( int axisType, LevelHandle value )
	// throws SemanticException
	// {
	// new CrosstabReportItemTask( this ).setMirroredStartingLevel( axisType,
	// value );
	// }

	/**
	 * Adds the grand-total in the specified axis type. The selected measure
	 * list and function list must match.
	 * 
	 * @param axisType
	 *            the axis type to add the grand-total
	 * @param measureList
	 *            the list of measure views that will be applied the
	 *            aggregations by the grand-total
	 * @param functionList
	 *            the list of the aggregation that the measure will be applied
	 *            by the grand-total
	 * @return
	 * @throws SemanticException
	 */
	public CrosstabCellHandle addGrandTotal( int axisType, List measureList,
			List functionList ) throws SemanticException
	{
		return new CrosstabReportItemTask( this ).addGrandTotal( axisType,
				measureList,
				functionList );
	}

	/**
	 * Gets the measure view list that define aggregations for the row/column
	 * grand total in the crosstab. Each item in the list is instance of
	 * <code>MeasureViewHandle</code>.
	 * 
	 * @param axisType
	 * @return
	 */
	public List getAggregationMeasures( int axisType )
	{
		return new CrosstabReportItemTask( this ).getAggregationMeasures( axisType );
	}

	/**
	 * Gets the aggregation function for the row/column grand total in the
	 * crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param measureView
	 * @return
	 */
	public String getAggregationFunction( int axisType,
			MeasureViewHandle measureView )
	{
		return new CrosstabReportItemTask( this ).getAggregationFunction( axisType,
				measureView );
	}

	/**
	 * Gets the aggregation function for the row/column grand total in the
	 * crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param measureView
	 * @param function
	 * @return
	 * @throws SemanticException
	 */
	public void setAggregationFunction( int axisType,
			MeasureViewHandle measureView, String function )
			throws SemanticException
	{
		new CrosstabReportItemTask( this ).setAggregationFunction( axisType,
				measureView,
				function );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.ReportItem#checkCompatibility()
	 */
	public List checkCompatibility( )
	{
		// update header
		if ( getHeader( ) == null )
		{
			PropertyHandle headerHandle = handle.getPropertyHandle( HEADER_PROP );

			if ( headerHandle != null )
			{
				try
				{
					headerHandle.setValue( CrosstabExtendedItemFactory.createCrosstabCell( getModuleHandle( ) ) );
				}
				catch ( SemanticException e )
				{
					List errorList = new ArrayList( 1 );
					errorList.add( e );
					return errorList;
				}
			}
		}

		return Collections.EMPTY_LIST;
	}
}
