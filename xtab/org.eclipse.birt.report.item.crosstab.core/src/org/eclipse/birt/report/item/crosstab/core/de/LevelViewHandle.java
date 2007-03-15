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

import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;

/**
 * LevelViewHandle.
 */
public class LevelViewHandle extends AbstractCrosstabItemHandle
		implements
			ILevelViewConstants,
			ICrosstabConstants
{

	/**
	 * 
	 * @param handle
	 */
	LevelViewHandle( DesignElementHandle handle )
	{
		super( handle );
	}

	/**
	 * Gets the referred OLAP level handle.
	 * 
	 * @return the referred OLAP level handle.
	 */
	public LevelHandle getCubeLevel( )
	{
		return (LevelHandle) handle.getElementProperty( LEVEL_PROP );
	}

	/**
	 * Gets name of the referred OLAP level handle.
	 * 
	 * @return qualified name of the referred OLAP level handle
	 */
	public String getCubeLevelName( )
	{
		return handle.getStringProperty( LEVEL_PROP );
	}

	/**
	 * Gets the interval base of this level view.
	 * 
	 * @return the interval base
	 */

	public String getIntervalBase( )
	{
		return handle.getStringProperty( ILevelModel.INTERVAL_BASE_PROP );
	}

	/**
	 * Gets the interval range of this level view.
	 * 
	 * @return the interval range
	 */
	public double getIntervalRange( )
	{
		return handle.getFloatProperty( ILevelModel.INTERVAL_RANGE_PROP );
	}

	/**
	 * Gets the interval of this level view. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 * 
	 * </ul>
	 * 
	 * @return the interval value as a string
	 */
	public String getInterval( )
	{
		return handle.getStringProperty( ILevelModel.INTERVAL_PROP );
	}

	/**
	 * Returns the iterator for filter list defined on this level view. The
	 * element in the iterator is the corresponding <code>StructureHandle</code>
	 * that deal with a <code>FilterCond</code> in the list.
	 * 
	 * @return the iterator for <code>FilterCond</code> structure list
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = handle.getPropertyHandle( FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Return the sort type.
	 * 
	 * @return the sort type.
	 */

	public String getSortType( )
	{
		return handle.getStringProperty( SORT_TYPE_PROP );
	}

	/**
	 * Returns the sort direction of this level view. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 * 
	 * </ul>
	 * 
	 * @return the sort direction of this level view
	 */

	public String getSortDirection( )
	{
		return handle.getStringProperty( SORT_DIRECTION_PROP );
	}

	/**
	 * Returns the iterator for Sort list defined on this level. The element in
	 * the iterator is the corresponding <code>StructureHandle</code>.
	 * 
	 * @return the iterator for <code>SortKey</code> structure list
	 */

	public Iterator sortsIterator( )
	{
		PropertyHandle propHandle = handle.getPropertyHandle( SORT_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the group type of this level view. The return value is defined in
	 * <code>ICrosstabConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>LEVEL_TYPE_DYNAMIC</code>
	 * <li><code>LEVEL_TYPE_MIRRORED</code>
	 * </ul>
	 * 
	 * @return the sort direction of this level view
	 */

	public String getLevelType( )
	{
		return handle.getStringProperty( ILevelModel.LEVEL_TYPE_PROP );
	}

	/**
	 * Gets page break before property value of this level view.
	 * 
	 * @return page break before property value of this level view
	 */

	public String getPageBreakBefore( )
	{
		return handle.getStringProperty( PAGE_BREAK_BEFORE_PROP );
	}

	/**
	 * Gets page break after property value of this level.
	 * 
	 * @return page break after property value of this level
	 */

	public String getPageBreakAfter( )
	{
		return handle.getStringProperty( PAGE_BREAK_AFTER_PROP );
	}

	/**
	 * Sets page break before property value of this level
	 * 
	 * @param value
	 *            the page break before option to set
	 * @throws SemanticException
	 */
	public void setPageBreakBefore( String value ) throws SemanticException
	{
		handle.setStringProperty( PAGE_BREAK_BEFORE_PROP, value );
	}

	/**
	 * Sets page break after property value of this level.
	 * 
	 * @param value
	 *            the page break after option to set
	 * @throws SemanticException
	 * 
	 */
	public void setPageBreakAfter( String value ) throws SemanticException
	{
		handle.setStringProperty( PAGE_BREAK_AFTER_PROP, value );
	}

	/**
	 * Returns the aggregation header location of this level view. The return
	 * value is defined in <code>ICrosstabConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>AGGREGATION_HEADER_LOCATION_BEFORE</code>
	 * <li><code>AGGREGATION_HEADER_LOCATION_AFTER</code>
	 * </ul>
	 * 
	 * @return the aggregation header location of this level view
	 */

	public String getAggregationHeaderLocation( )
	{
		return handle.getStringProperty( AGGREGATION_HEADER_LOCATION_PROP );
	}

	/**
	 * Sets the aggregation header location of this level view. The input value
	 * can be one of:
	 * <ul>
	 * <li><code>AGGREGATION_HEADER_LOCATION_BEFORE</code>
	 * <li><code>AGGREGATION_HEADER_LOCATION_AFTER</code>
	 * </ul>
	 * 
	 * @param value
	 *            the aggregation header location to set
	 * @throws SemanticException
	 */
	public void setAggregationHeaderLocation( String value )
			throws SemanticException
	{
		handle.setStringProperty( AGGREGATION_HEADER_LOCATION_PROP, value );
	}

	/**
	 * Gets the member property handle of this level view.
	 * 
	 * @return the member property handle
	 */
	public PropertyHandle getMemberProperty( )
	{
		return handle.getPropertyHandle( MEMBER_PROP );
	}

	/**
	 * Gets the aggregation header property handle of this level view.
	 * 
	 * @return the aggregation header property handle
	 */
	public PropertyHandle getAggregationHeaderProperty( )
	{
		return handle.getPropertyHandle( AGGREGATION_HEADER_PROP );
	}

	/**
	 * Gets the detail cell of this level view.
	 * 
	 * @return the detail cell of this level view if set, otherwise null
	 */
	public CrosstabCellHandle getCell( )
	{
		PropertyHandle propHandle = getMemberProperty( );
		return propHandle.getContentCount( ) == 0
				? null
				: (CrosstabCellHandle) CrosstabUtil.getReportItem( propHandle
						.getContent( 0 ), CROSSTAB_CELL_EXTENSION_NAME );
	}

	/**
	 * Gets the aggregation header cell of this level view.
	 * 
	 * @return aggregation header cell if set, otherwise null
	 */
	public CrosstabCellHandle getAggregationHeader( )
	{
		PropertyHandle propHandle = getAggregationHeaderProperty( );
		return propHandle.getContentCount( ) == 0
				? null
				: (CrosstabCellHandle) CrosstabUtil.getReportItem( propHandle
						.getContent( 0 ), CROSSTAB_CELL_EXTENSION_NAME );
	}

	/**
	 * Adds a aggregation header to the level if it is empty.
	 * 
	 */
	public void addAggregationHeader( )
	{
		if ( getAggregationHeaderProperty( ).getContentCount( ) != 0 )
		{
			logger.log( Level.INFO, "the aggregation header is set" ); //$NON-NLS-1$
			return;
		}

		// can not add aggregation if this level is innermost
		if ( isInnerMost( ) )
		{
			logger
					.log(
							Level.WARNING,
							"This level: [" + handle.getName( ) + "] can not add aggregation for it is innermost" ); //$NON-NLS-1$//$NON-NLS-2$
			return;
		}

		CommandStack stack = getCommandStack( );
		stack.startTrans( null );
		try
		{
			getAggregationHeaderProperty( ).add(
					CrosstabExtendedItemFactory
							.createCrosstabCell( moduleHandle ) );

			// adjust the measure aggregations
			CrosstabReportItemHandle crosstab = getCrosstab( );
			if ( crosstab != null )
			{
				CrosstabUtil.adjustMeasureAggregations( crosstab,
						getAxisType( ), this,
						( (DimensionViewHandle) getContainer( ) )
								.getCubeDimensionName( ), getCubeLevelName( ),
						false, true );
			}
		}
		catch ( SemanticException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			stack.rollback( );
		}

		stack.commit( );
	}

	/**
	 * Removes the aggregation header cell if it is not empty, otherwise do
	 * nothing.
	 */
	public void removeAggregationHeader( )
	{
		if ( getAggregationHeaderProperty( ).getContentCount( ) > 0 )
		{
			CommandStack stack = getCommandStack( );
			stack.startTrans( null );

			try
			{
				getAggregationHeaderProperty( ).drop( 0 );

				// adjust the aggregations in measure elements
				CrosstabReportItemHandle crosstab = getCrosstab( );
				if ( crosstab != null )
				{
					CrosstabUtil.adjustMeasureAggregations( crosstab,
							getAxisType( ), this,
							( (DimensionViewHandle) getContainer( ) )
									.getCubeDimensionName( ),
							getCubeLevelName( ), isInnerMost( ), false );
				}

			}
			catch ( SemanticException e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
				stack.rollback( );
			}

			stack.commit( );
		}
	}

	/**
	 * Gets the position index where this level lies in the dimension view
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
	 * Justifies whether this level view is inner most in the crosstab. True if
	 * and only if it is the last one in its container dimension view and its
	 * container dimension view is the last one in crosstab view.
	 * 
	 * @return true if
	 */
	public boolean isInnerMost( )
	{
		DimensionViewHandle dimensionView = (DimensionViewHandle) getContainer( );

		// a level view is 'innerMost' if and only if it is the last one in its
		// container dimension view and its container dimension view is the last
		// one in crosstab view
		if ( dimensionView != null )
		{
			CrosstabViewHandle container = (CrosstabViewHandle) dimensionView
					.getContainer( );
			if ( container != null
					&& dimensionView.getIndex( ) == container
							.getDimensionCount( ) - 1
					&& getIndex( ) == dimensionView.getLevelCount( ) - 1 )
				return true;
		}

		return false;
	}

	/**
	 * Gets the axis type of this level view in the crosstab. If this level lies
	 * in the design tree, the returned value is either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. Otherwise return
	 * <code>ICrosstabConstants.NO_AXIS_TYPE</code>.
	 * 
	 * @return the axis type if this level resides in design tree, otherwise -1;
	 */
	public int getAxisType( )
	{
		DimensionViewHandle dimensionView = (DimensionViewHandle) CrosstabUtil
				.getReportItem( handle.getContainer( ),
						DIMENSION_VIEW_EXTENSION_NAME );
		return dimensionView == null ? NO_AXIS_TYPE : dimensionView
				.getAxisType( );

	}
}
