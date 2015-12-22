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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.IComputedMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabReportItemTask;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabEventHandler;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabClassInfo;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabImpl;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.CompatibilityStatus;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.api.util.CubeUtil;

/**
 * CrosstabReportItemHandle.
 */
public class CrosstabReportItemHandle extends AbstractCrosstabItemHandle implements
		ICrosstabReportItemConstants,
		ICrosstabConstants
{

	int compStatus = 0;

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
	 * @return Returns the summary text of this crosstab
	 */
	public String getSummary( )
	{
		return handle.getStringProperty( SUMMARY_PROP );
	}

	/**
	 * Sets the caption of this crosstab
	 * 
	 * @param value
	 * @throws SemanticException
	 */
	public void setCaption( String value ) throws SemanticException
	{
		handle.setProperty( CAPTION_PROP, value );
	}

	/**
	 * Sets the caption key of this crosstab
	 * 
	 * @param value
	 * @throws SemanticException
	 */
	public void setCaptionKey( String value ) throws SemanticException
	{
		handle.setProperty( CAPTION_ID_PROP, value );
	}

	/**
	 * Sets the summary of this crosstab
	 * 
	 * @param value
	 * @throws SemanticException
	 */
	public void setSummary( String value ) throws SemanticException
	{
		handle.setProperty( SUMMARY_PROP, value );
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
		if ( direction != null && direction.equals( getMeasureDirection( ) ) )
		{
			return;
		}

		CommandStack stack = getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemHandle.msg.change.measure.direction" ) ); //$NON-NLS-1$

		try
		{
			handle.setStringProperty( MEASURE_DIRECTION_PROP, direction );

			new CrosstabReportItemTask( this ).validateCrosstab( );
			CrosstabModelUtil.updateHeaderCell( this, -2, -1 );
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
		if ( value == isHideMeasureHeader( ) )
		{
			return;
		}

		handle.setProperty( HIDE_MEASURE_HEADER_PROP, Boolean.valueOf( value ) );
		CrosstabModelUtil.updateHeaderCell( this, -1, -1 );
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
	 * Sets the empty cell value of this crosstab.
	 */
	public void setEmptyCellValue( String value ) throws SemanticException
	{
		handle.setStringProperty( EMPTY_CELL_VALUE_PROP, value );
	}

	/**
	 * @return Returns the page break internval for row area.
	 * 
	 * @since 2.6.1
	 */
	public int getRowPageBreakInterval( )
	{
		return handle.getIntProperty( ROW_PAGE_BREAK_INTERVAL_PROP );
	}

	/**
	 * Sets the page break interval for row area.
	 * 
	 * @param value
	 * @throws SemanticException
	 * 
	 * @since 2.6.1
	 */
	public void setRowPageBreakInterval( int value ) throws SemanticException
	{
		handle.setIntProperty( ROW_PAGE_BREAK_INTERVAL_PROP, value );
	}

	/**
	 * @return Returns the page break internval for column area.
	 * 
	 * @since 2.6.1
	 */
	public int getColumnPageBreakInterval( )
	{
		return handle.getIntProperty( COLUMN_PAGE_BREAK_INTERVAL_PROP );
	}

	/**
	 * Sets the page break interval for column area.
	 * 
	 * @param value
	 * @throws SemanticException
	 * 
	 * @since 2.6.1
	 */
	public void setColumnPageBreakInterval( int value )
			throws SemanticException
	{
		handle.setIntProperty( COLUMN_PAGE_BREAK_INTERVAL_PROP, value );
	}

	/**
	 * Returns the iterator for filter list defined on Crosstab. The element in
	 * the iterator is the corresponding <code>DesignElementHandle</code> that
	 * deal with a <code>FilterConditionElementHandle</code> in the list.
	 * 
	 * @return the iterator for <code>FilterConditionElementHandle</code>
	 *         element list
	 */
	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = handle.getPropertyHandle( FILTER_PROP );
		if ( propHandle == null )
		{
			return Collections.EMPTY_LIST.iterator( );
		}
		return propHandle.getListValue( ).iterator( );
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
		handle.setProperty( ReportItemHandle.CUBE_PROP, cube );
	}

	/**
	 * Gets the name of referred OLAP cube element.
	 * 
	 * @return name of the referred OLAP cube element.
	 */
	public String getCubeName( )
	{
		String cubeName = handle.getStringProperty( ReportItemHandle.CUBE_PROP );
		if( cubeName == null && this.getCube( ) != null )
		{
			cubeName = this.getCube( ).getName( );
		}
		
		return cubeName;
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
	 * Gets the level by full level name.
	 * 
	 * @param fullLevelName
	 * @return
	 */
	public LevelViewHandle getLevel( String fullLevelName )
	{
		String[] slices = CubeUtil.splitLevelName( fullLevelName );
		DimensionViewHandle dv = getDimension( slices[0] );
		if( dv != null )
		{
			return dv.getLevel( fullLevelName );
		}
		return null;
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
		return (MeasureViewHandle) CrosstabUtil.getReportItem( element );
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

	public List<MeasureViewHandle> getAllMeasures( )
	{
		List<MeasureViewHandle> measureList = new ArrayList<MeasureViewHandle>( );
		for ( int i = 0; i < getMeasureCount( ); i++ )
		{
			measureList.add( getMeasure( i ) );
		}
		return measureList;
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
			throw new CrosstabException( handle.getElement( ),
					Messages.getString( MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_MEASURE,
							measureHandle.getQualifiedName( ) ) );
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

				CrosstabModelUtil.updateHeaderCell( this, -1, -1 );

				if ( !( measureHandle.getContainer( ).getContainer( ) instanceof CubeHandle ) )
				{
					ComputedColumnHandle cc = CrosstabUtil.getMeasureBindingColumnHandle( mv );
					cc.setDataType( measureHandle.getDataType( ) );
					cc.setExpression( ExpressionUtil.createJSMeasureExpression( measureHandle.getName( ) ) );
				}
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

	public ComputedMeasureViewHandle insertComputedMeasure( String name,
			int index ) throws SemanticException
	{
		return this.insertComputedMeasure( name, index, false );
	}
	
	public ComputedMeasureViewHandle insertComputedMeasure( String name,
			int index, boolean isMeasureView ) throws SemanticException
	{
		if ( name == null )
		{
			throw new CrosstabException( Messages.getString( "CrosstabReportItemHandle.exception.name.blank" ) ); //$NON-NLS-1$
		}

		// check duplicate name
		if ( getMeasure( name ) != null )
		{
			throw new CrosstabException( Messages.getString( "CrosstabReportItemHandle.exception.name.duplicate" ) ); //$NON-NLS-1$
		}

		CommandStack stack = getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemHandle.msg.insert.measure" ) ); //$NON-NLS-1$

		ComputedMeasureViewHandle mv = null;

		try
		{
			ExtendedItemHandle extendedItemHandle = CrosstabExtendedItemFactory.createComputedMeasureView( moduleHandle,
					name );

			if ( extendedItemHandle != null )
			{
				if( isMeasureView )
				{
					// Set measure name property
					extendedItemHandle.setProperty( IComputedMeasureViewConstants.MEASURE_NAME_PROP, name );
				}
				
				getMeasuresProperty( ).add( extendedItemHandle, index );

				// validate possible aggregation cells
				new CrosstabReportItemTask( this ).validateCrosstab( );

				mv = (ComputedMeasureViewHandle) CrosstabUtil.getReportItem( extendedItemHandle );
				CrosstabModelUtil.updateHeaderCell( this, -1, -1 );
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

	public List<ComputedMeasureViewHandle> getComputedMeasures( )
	{
		List<ComputedMeasureViewHandle> ms = new ArrayList<ComputedMeasureViewHandle>( );

		for ( int i = 0; i < getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = getMeasure( i );

			if ( measureView instanceof ComputedMeasureViewHandle )
			{
				ms.add( (ComputedMeasureViewHandle) measureView );
			}
		}

		if ( ms.size( ) == 0 )
		{
			return Collections.emptyList( );
		}

		return ms;
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
					MessageConstants.CROSSTAB_EXCEPTION_MEASURE_NOT_FOUND,
					name );
			throw new CrosstabException( handle.getElement( ),
					Messages.getString( MessageConstants.CROSSTAB_EXCEPTION_MEASURE_NOT_FOUND,
							name ) );
		}

		removeMeasure( measureView.getIndex( ) );
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

		int targetAxis = MEASURE_DIRECTION_VERTICAL.equals( getMeasureDirection( ) ) ? ROW_AXIS_TYPE
				: COLUMN_AXIS_TYPE;

		// check redundant subtotals
		List<LevelViewHandle> levels = CrosstabModelUtil.getAllAggregationLevels( this,
				targetAxis );

		for ( LevelViewHandle lv : levels )
		{
			if ( lv.isInnerMost( ) || lv.getAggregationHeader( ) == null )
			{
				continue;
			}

			// if no aggregation measure after removal, we should remove the
			// subtotal header
			if ( lv.getAggregationMeasures( ).size( ) == 0 )
			{
				lv.getAggregationHeaderProperty( ).drop( 0 );
			}
		}

		// check redundant grandtotal
		CrosstabCellHandle grandtotalCell = getGrandTotal( targetAxis );
		if ( grandtotalCell != null
				&& getAggregationMeasures( targetAxis ).size( ) == 0 )
		{
			// if no aggregation measure after removal, we should remove the
			// grandtotal header
			grandtotalCell.getModelHandle( ).drop( );
		}

		CrosstabModelUtil.updateHeaderCell( this, -1, -1 );
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
	 * Swaps the crosstab row area and column area. Note this call is not
	 * equivalent to calling <code>pivotDimension</code> to interchange
	 * dimensions from row area to column area. Specifically this call will
	 * retain all the original subtotal and grandtotal info in both area after
	 * the swapping, while <code>pivotDimension</code> may remove the grandtotal
	 * or recreate some cells during the processing.
	 * 
	 * @throws SemanticException
	 * 
	 * @since 2.5.1
	 */
	public void pivotCrosstab( ) throws SemanticException
	{
		new CrosstabReportItemTask( this ).pivotCrosstab( );
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
		List<MeasureViewHandle> measures = new ArrayList<MeasureViewHandle>( );
		List<String> functions = new ArrayList<String>( );

		for ( int i = 0; i < getMeasureCount( ); i++ )
		{
			MeasureViewHandle mv = getMeasure( i );
			measures.add( mv );
			functions.add( CrosstabModelUtil.getDefaultMeasureAggregationFunction( mv ) );
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
		return handle.getDimensionProperty( ReportItemHandle.WIDTH_PROP );
	}

	/**
	 * Gets the dimension value handle for the crosstab height.
	 * 
	 * @return crosstab height dimension value handle
	 */
	public org.eclipse.birt.report.model.api.DimensionHandle getHeight( )
	{
		return handle.getDimensionProperty( ReportItemHandle.HEIGHT_PROP );
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
					MessageConstants.CROSSTAB_EXCEPTION_MEASURE_NOT_FOUND,
					name );
			throw new CrosstabException( handle.getElement( ),
					Messages.getString( MessageConstants.CROSSTAB_EXCEPTION_MEASURE_NOT_FOUND,
							name ) );
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
					String.valueOf( fromIndex ) );
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
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#getSimpleElement()
	 */
	public IReportItem getSimpleElement( )
	{
		return new CrosstabImpl( this );
	}

	private IMethodInfo[] getFilteredMethods( String contextName )
	{
		CrosstabClassInfo info = new CrosstabClassInfo( ICrosstabEventHandler.class );
		List list = info.getMethods( );

		List filtered = new ArrayList( );

		for ( Iterator itr = list.iterator( ); itr.hasNext( ); )
		{
			IMethodInfo md = (IMethodInfo) itr.next( );

			String name = md.getName( );

			if ( name != null && name.startsWith( contextName ) )
			{
				filtered.add( md );
			}
		}

		return (IMethodInfo[]) filtered.toArray( new IMethodInfo[filtered.size( )] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#getMethods(java
	 * .lang.String)
	 */
	public IMethodInfo[] getMethods( String methodName )
	{
		if ( ON_PREPARE_METHOD.equals( methodName )
				|| ON_CREATE_METHOD.equals( methodName )
				|| ON_RENDER_METHOD.equals( methodName ) )
		{
			return getFilteredMethods( methodName );
		}
		// else if ( ON_PAGEBREAK_METHOD.equals( methodName ) )
		// {
		// CrosstabClassInfo info = new CrosstabClassInfo(
		// ICrosstabEventHandler.class );
		// List list = info.getMethods( );
		//
		// List filtered = new ArrayList( );
		//
		// for ( Iterator itr = list.iterator( ); itr.hasNext( ); )
		// {
		// IMethodInfo md = (IMethodInfo) itr.next( );
		//
		// String name = md.getName( );
		//
		// if ( name != null && name.startsWith( "on" ) //$NON-NLS-1$
		// && name.endsWith( "PageBreak" ) ) //$NON-NLS-1$
		// {
		// filtered.add( md );
		// }
		// }
		//
		// return (IMethodInfo[]) filtered.toArray( new
		// IMethodInfo[filtered.size( )] );
		// }

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#checkCompatibility
	 * ()
	 */
	public CompatibilityStatus checkCompatibility( )
	{
		compStatus = checkVersion( handle.getExtensionVersion( ) );

		// update old version
		if ( compStatus < 0 )
		{
			CompatibilityStatus status = new CompatibilityStatus( );

			List errorList = new ArrayList( 2 );

			status.setStatusType( CompatibilityStatus.CONVERT_COMPATIBILITY_TYPE );

			try
			{
				handle.setExtensionVersion( CROSSTAB_CURRENT_VERSION );
			}
			catch ( SemanticException e )
			{
				errorList.add( e );
			}

			// adding crosstab header support (? -> 2.3.0)
			if ( getHeader( ) == null )
			{
				PropertyHandle headerHandle = handle.getPropertyHandle( HEADER_PROP );

				if ( headerHandle != null )
				{
					try
					{
						headerHandle.setValue( CrosstabExtendedItemFactory.createCrosstabCell( getModuleHandle( ) ) );
						CrosstabModelUtil.validateCrosstabHeader( this );
					}
					catch ( SemanticException e )
					{
						errorList.add( e );
					}
				}
			}

			if ( errorList.size( ) > 0 )
			{
				status.setErrors( errorList );
			}

			return status;
		}
		else if ( compStatus > 0 )
		{
			return new CompatibilityStatus( Collections.EMPTY_LIST,
					CompatibilityStatus.NOT_SUPPORTED_TYPE );
		}

		return COMP_OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElement#validate()
	 */
	public List validate( )
	{
		List list = new ArrayList( );

		if ( !( this.getModelHandle( ).isInTemplateParameter( ) )
				&& this.getCube( ) == null
				&& !( this.getModelHandle( ).getRoot( ) instanceof LibraryHandle ) )
		{
			ExtendedElementException extendedException = new ExtendedElementException( this.getModelHandle( )
					.getElement( ),
					"org.eclipse.birt.report.item.crosstab.core",//$NON-NLS-1$
					"CrosstabReportItemHandle.Error.HasNoCube",//$NON-NLS-1$
					new Object[]{
						"Erorr" //$NON-NLS-1$
					},
					Messages.getResourceBundle( ) );
			list.add( extendedException );
		}

		return list;
	}

	// Support the multiple left coner head cells

	public CrosstabCellHandle getHeader( int index )
	{
		PropertyHandle headerHandle = handle.getPropertyHandle( HEADER_PROP );
		if ( headerHandle == null )
		{
			return null;
		}
		List list = headerHandle.getContents( );
		if ( list == null || index < 0 || index > list.size( ) - 1 )
		{
			return null;
		}
		return (CrosstabCellHandle) CrosstabUtil.getReportItem( headerHandle.getContent( index ) );

	}

	public int getHeaderCount( )
	{
		PropertyHandle headerHandle = handle.getPropertyHandle( HEADER_PROP );
		if ( headerHandle == null )
		{
			return 0;
		}
		return headerHandle.getContentCount( );
	}

}
