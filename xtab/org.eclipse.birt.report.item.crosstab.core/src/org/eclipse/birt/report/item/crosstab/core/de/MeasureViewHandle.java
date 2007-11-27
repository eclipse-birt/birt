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

import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.IAggregationCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * MeasureViewHandle.
 */
public class MeasureViewHandle extends AbstractCrosstabItemHandle
		implements
			IMeasureViewConstants,
			ICrosstabConstants
{

	/**
	 * 
	 * @param handle
	 */
	MeasureViewHandle( DesignElementHandle handle )
	{
		super( handle );
	}

	/**
	 * Gets the referred OLAP measure handle of this measure view.
	 * 
	 * @return the referred OLAP measure handle
	 */
	public MeasureHandle getCubeMeasure( )
	{
		return (MeasureHandle) handle.getElementProperty( MEASURE_PROP );
	}

	/**
	 * Gets name of the referred OLAP measure handle in this measure view.
	 * 
	 * @return name of the referred OLAP measure handle
	 */
	public String getCubeMeasureName( )
	{
		return handle.getStringProperty( MEASURE_PROP );
	}

	/**
	 * Gets the data type of this measure view. It is identical with the data
	 * type set in the referred cube measure element.
	 * 
	 * @return
	 */
	public String getDataType( )
	{
		MeasureHandle cubeMeasure = getCubeMeasure( );
		return cubeMeasure == null ? null : cubeMeasure.getDataType( );
	}

	/**
	 * Gets the aggregations property handle of this measure view.
	 * 
	 * @return the aggregations property handle
	 */
	PropertyHandle getAggregationsProperty( )
	{
		return handle.getPropertyHandle( AGGREGATIONS_PROP );
	}

	/**
	 * Gets the detail slot handle of this measure view.
	 * 
	 * @return the detail slot handle
	 */
	PropertyHandle getDetailProperty( )
	{
		return handle.getPropertyHandle( DETAIL_PROP );
	}

	/**
	 * Gets the header slot handle of this measure view.
	 * 
	 * @return the header slot handle
	 */
	PropertyHandle getHeaderProperty( )
	{
		return handle.getPropertyHandle( HEADER_PROP );
	}

	/**
	 * Gets the detail cell of this measure view.
	 * 
	 * @return the detail cell of this measure view if set, otherwise null
	 */
	public CrosstabCellHandle getCell( )
	{
		PropertyHandle propHandle = getDetailProperty( );
		return propHandle.getContentCount( ) == 0
				? null
				: (CrosstabCellHandle) CrosstabUtil.getReportItem( propHandle
						.getContent( 0 ), CROSSTAB_CELL_EXTENSION_NAME );
	}

	/**
	 * Adds an aggregation cell with the specific row/column dimension and
	 * level.
	 * 
	 * @param rowDimension
	 *            qualified name of the row dimension
	 * @param rowLevel
	 *            qualified name of the row level
	 * @param colDimension
	 *            qualified name of the column dimension
	 * @param colLevel
	 *            qualifed name of the column level
	 * @return the added aggregation cell if succeed, otherwise null
	 * @throws SemanticException
	 */
	public AggregationCellHandle addAggregation( String rowDimension,
			String rowLevel, String colDimension, String colLevel )
			throws SemanticException
	{
		AggregationCellHandle aggregation = getAggregationCell( rowDimension,
				rowLevel, colDimension, colLevel );
		if ( aggregation != null )
		{
			logger
					.log(
							Level.INFO,
							Messages
									.getString( "MeasureViewHandle.info.aggregation.already.exist" ) ); //$NON-NLS-1$
			return aggregation;
		}
		ExtendedItemHandle aggregationCell = CrosstabExtendedItemFactory
				.createAggregationCell( moduleHandle );
		if ( aggregationCell != null )
		{
			CommandStack stack = getCommandStack( );
			stack.startTrans( Messages
					.getString( "MeasureViewHandle.msg.add.aggregation" ) ); //$NON-NLS-1$

			try
			{
				aggregationCell.setProperty(
						IAggregationCellConstants.AGGREGATION_ON_ROW_PROP,
						rowLevel );
				aggregationCell.setProperty(
						IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP,
						colLevel );
				getAggregationsProperty( ).add( aggregationCell );
			}
			catch ( SemanticException e )
			{
				stack.rollback( );
				throw e;
			}
			stack.commit( );
		}
		return (AggregationCellHandle) CrosstabUtil
				.getReportItem( aggregationCell );
	}

	/**
	 * 
	 * @param rowDimension
	 *            qualified name of the row dimension
	 * @param rowLevel
	 *            qualified name of the row level
	 * @param colDimension
	 *            qualified name of the column dimension
	 * @param colLevel
	 *            qualifed name of the column level
	 * @throws SemanticException
	 */
	public void removeAggregation( String rowDimension, String rowLevel,
			String colDimension, String colLevel ) throws SemanticException
	{
		AggregationCellHandle cell = getAggregationCell( rowDimension,
				rowLevel, colDimension, colLevel );
		if ( cell != null )
		{
			cell.handle.drop( );
		}
	}

	/**
	 * Finds an aggregation cell which uses the given row/column dimension and
	 * level.
	 * 
	 * @param rowDimension
	 *            qualified name of the row dimension
	 * @param rowLevel
	 *            qualified name of the row level
	 * @param colDimension
	 *            qualified name of the column dimension
	 * @param colLevel
	 *            qualifed name of the column level
	 * @return the aggregation cell handle if found, otherwise null
	 */
	public AggregationCellHandle getAggregationCell( String rowDimension,
			String rowLevel, String colDimension, String colLevel )
	{
		int count = getAggregationCount( );
		if ( count == 0 )
			return null;
		DesignElementHandle found = null;
		for ( int i = 0; i < count; i++ )
		{
			DesignElementHandle element = getAggregationsProperty( )
					.getContent( i );
			String row = element
					.getStringProperty( IAggregationCellConstants.AGGREGATION_ON_ROW_PROP );
			String column = element
					.getStringProperty( IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP );
			if ( ( rowLevel != null && rowLevel.equals( row ) )
					|| ( rowLevel == null && row == null ) )
			{
				if ( ( colLevel != null && colLevel.equals( column ) )
						|| ( colLevel == null && column == null ) )
				{
					found = element;
					break;
				}
			}
		}
		return (AggregationCellHandle) CrosstabUtil.getReportItem( found,
				AGGREGATION_CELL_EXTENSION_NAME );
	}

	/**
	 * Gets the aggregation cell count for this measure.
	 * 
	 * @return count of aggregation cell for this measure
	 */
	public int getAggregationCount( )
	{
		return getAggregationsProperty( ).getContentCount( );
	}

	/**
	 * Gets the aggregation cell with the given index. Position index is 0-based
	 * integer.
	 * 
	 * @param index
	 *            a 0-based integer of the aggregation cell position
	 * @return the aggregation cell handle if found, otherwise null
	 */
	public AggregationCellHandle getAggregationCell( int index )
	{
		DesignElementHandle element = getAggregationsProperty( ).getContent(
				index );
		return (AggregationCellHandle) CrosstabUtil.getReportItem( element,
				AGGREGATION_CELL_EXTENSION_NAME );
	}

	/**
	 * Removes aggregation cell at the given position. The position index is
	 * 0-based integer.
	 * 
	 * @param index
	 *            the position index of the aggregation cell to remove
	 * @throws SemanticException
	 */
	public void removeAggregation( int index ) throws SemanticException
	{
		getAggregationsProperty( ).drop( index );
	}

	/**
	 * Gets the position index where this measure lies in the crosstab
	 * container. The returned value is a 0-based integer if this level is in
	 * the design tree. Otherwise return -1.
	 * 
	 * @return position index if found, otherwise -1
	 */
	public int getIndex( )
	{
		return handle.getIndex( );
	}

	/**
	 * Gets measure header cell for specific dimension and level.
	 * 
	 * @param dimensionName
	 *            name of the dimension to find
	 * @param levelName
	 *            name of the level to find
	 * @return the header cell which refers the given dimension and level
	 */
	public CrosstabCellHandle getHeader( )
	{
		DesignElementHandle headerCell = getHeaderCell( );
		return (CrosstabCellHandle) ( headerCell == null ? null : CrosstabUtil
				.getReportItem( headerCell, CROSSTAB_CELL_EXTENSION_NAME ) );
	}

	/**
	 * Gets measure header cell.
	 * 
	 * @return the design element handle for the header cell if found, otherwise
	 *         null
	 */
	private DesignElementHandle getHeaderCell( )
	{
		PropertyHandle propHandle = getHeaderProperty( );
		if ( propHandle.getContentCount( ) <= 0 )
			return null;
		return propHandle.getContent( 0 );
	}

	/**
	 * Removes header cell for current measure.
	 * 
	 * @throws SemanticException
	 */
	public void removeHeader( ) throws SemanticException
	{
		DesignElementHandle headerCell = getHeaderCell( );
		if ( headerCell != null )
		{
			headerCell.drop( );
		}
	}

	/**
	 * Adds header cell for current measure. If header cell already exists, this
	 * method just does nothing.
	 * 
	 * @throws SemanticException
	 */
	public void addHeader( ) throws SemanticException
	{
		PropertyHandle propHandle = getHeaderProperty( );

		if ( propHandle.getContentCount( ) > 0 )
		{
			logger.log( Level.INFO,
					"Measure header is set, need not add another" ); //$NON-NLS-1$
			return;
		}

		ExtendedItemHandle headerCell = CrosstabExtendedItemFactory
				.createCrosstabCell( moduleHandle );
		propHandle.add( headerCell );
	}
	
	/**
	 * Returns the iterator for filter list defined on this measure view. The
	 * element in the iterator is the corresponding
	 * <code>DesignElementHandle</code> that deal with a
	 * <code>FilterConditionElementHandle</code> in the list.
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
}
