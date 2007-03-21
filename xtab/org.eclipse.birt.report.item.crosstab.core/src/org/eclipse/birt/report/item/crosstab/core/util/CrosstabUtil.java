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

package org.eclipse.birt.report.item.crosstab.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.IAggregationCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.util.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Utility clas for crosstab.
 */

public class CrosstabUtil implements ICrosstabConstants
{

	/**
	 * 
	 * @param element
	 * @return report item if found, otherwise null
	 */
	public static IReportItem getReportItem( DesignElementHandle element )
	{
		if ( !( element instanceof ExtendedItemHandle ) )
			return null;
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) element;
		try
		{
			return extendedItem.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			return null;
		}
	}

	/**
	 * 
	 * @param element
	 * @param extensionName
	 * @return report item if found, otherwise null
	 */
	public static IReportItem getReportItem( DesignElementHandle element,
			String extensionName )
	{
		if ( !( element instanceof ExtendedItemHandle ) )
			return null;
		if ( extensionName == null )
			throw new IllegalArgumentException(
					"extension name can not be null" ); //$NON-NLS-1$
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) element;
		if ( extensionName.equals( extendedItem.getExtensionName( ) ) )
		{
			try
			{
				return extendedItem.getReportItem( );
			}
			catch ( ExtendedElementException e )
			{
				return null;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param levelView
	 * @param functions
	 * @param measures
	 * @return
	 * @throws SemanticException
	 */
	public static CrosstabCellHandle addAggregationHeader(
			LevelViewHandle levelView, List functions, List measures )
			throws SemanticException
	{
		if ( levelView == null || !isValidParameters( functions, measures ) )
			return null;

		// can not add aggregation if this level is innermost
		if ( levelView.isInnerMost( ) )
		{
			levelView
					.getLogger( )
					.log(
							Level.WARNING,
							"This level: [" + levelView.getModelHandle( ).getName( ) + "] can not add aggregation for it is innermost" ); //$NON-NLS-1$//$NON-NLS-2$
			return null;
		}
		if ( levelView.getAggregationHeader( ) != null )
		{
			levelView.getLogger( ).log( Level.INFO,
					"the aggregation header is set" ); //$NON-NLS-1$
			return levelView.getAggregationHeader( );
		}

		CommandStack stack = levelView.getCommandStack( );
		stack.startTrans( null );
		try
		{
			levelView.getAggregationHeaderProperty( ).add(
					CrosstabExtendedItemFactory.createCrosstabCell( levelView
							.getModuleHandle( ) ) );

			// adjust the measure aggregations
			CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
			if ( crosstab != null && measures != null )
			{
				CrosstabModelUtil.addMeasureAggregations( crosstab, levelView,
						functions, measures, false );
			}
		}
		catch ( SemanticException e )
		{
			levelView.getLogger( ).log( Level.WARNING, e.getMessage( ), e );
			stack.rollback( );
			throw e;
		}

		stack.commit( );
		return levelView.getAggregationHeader( );
	}

	/**
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param functions
	 * @param measures
	 * @return
	 * @throws SemanticException
	 */
	public static CrosstabCellHandle addGrandTotal(
			CrosstabReportItemHandle crosstab, int axisType, List functions,
			List measures ) throws SemanticException
	{
		if ( crosstab == null || !CrosstabModelUtil.isValidAxisType( axisType ) )
			return null;
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );
		if ( crosstabView == null )
		{
			CommandStack stack = crosstab.getCommandStack( );
			stack.startTrans( null );

			CrosstabCellHandle grandTotal;
			try
			{
				crosstabView = crosstab.addCrosstabView( axisType );
				grandTotal = addGrandTotal( crosstabView, functions, measures );
			}
			catch ( SemanticException e )
			{
				stack.rollback( );
				throw e;
			}

			stack.commit( );

			return grandTotal;
		}
		return addGrandTotal( crosstabView, functions, measures );
	}

	/**
	 * 
	 * @param crosstabView
	 * @param function
	 * @param measures
	 * @return
	 */
	private static CrosstabCellHandle addGrandTotal(
			CrosstabViewHandle crosstabView, List functions, List measures )
			throws SemanticException
	{
		if ( crosstabView == null || !isValidParameters( functions, measures ) )
			return null;
		PropertyHandle propHandle = crosstabView.getGrandTotalProperty( );

		if ( propHandle.getContentCount( ) > 0 )
			return crosstabView.getGrandTotal( );

		CommandStack stack = crosstabView.getCommandStack( );
		try
		{
			stack.startTrans( null );

			ExtendedItemHandle grandTotal = CrosstabExtendedItemFactory
					.createCrosstabCell( crosstabView.getModuleHandle( ) );
			propHandle.add( grandTotal );

			// adjust the measure aggregations
			CrosstabReportItemHandle crosstab = crosstabView.getCrosstab( );
			if ( crosstab != null && measures != null )
			{
				CrosstabModelUtil
						.addMeasureAggregations( crosstab, crosstabView
								.getAxisType( ), functions, measures, false );
			}

			stack.commit( );
			return (CrosstabCellHandle) getReportItem( grandTotal );
		}
		catch ( SemanticException e )
		{
			crosstabView.getLogger( ).log( Level.INFO, e.getMessage( ), e );
			stack.rollback( );
			throw e;
		}
	}

	private static boolean isValidParameters( List functions, List measures )
	{
		if ( functions != null && measures != null
				&& measures.size( ) != functions.size( ) )
			return false;
		return true;
	}

	/**
	 * Gets the measure view list that define aggregations for the given level
	 * view. Each item in the list is instance of <code>MeasureViewHandle</code>.
	 * 
	 * @param levelView
	 * @return
	 */
	public static List getAggregationMeasures( LevelViewHandle levelView )
	{
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if ( levelView == null || levelView.getAggregationHeader( ) == null
				|| levelView.getCubeLevelName( ) == null
				|| levelView.getCubeLevelName( ).length( ) <= 0 )
			return Collections.EMPTY_LIST;
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		if ( crosstab == null )
			return Collections.EMPTY_LIST;

		int axisType = levelView.getAxisType( );
		String levelName = levelView.getCubeLevelName( );
		List measures = new ArrayList( );
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			if ( measures.contains( measureView ) )
				continue;
			if ( isAggregationOn( measureView, levelName, axisType ) )
				measures.add( measureView );
		}
		return measures;
	}

	/**
	 * Justifies whether the given measure is aggregated on the level view.
	 * 
	 * @param measureView
	 * @param levelName
	 * @param axisType
	 * @return
	 */
	public static boolean isAggregationOn( MeasureViewHandle measureView,
			String levelName, int axisType )
	{
		assert measureView != null;
		assert CrosstabModelUtil.isValidAxisType( axisType );

		String propName = null;
		if ( axisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP;
		else if ( axisType == ICrosstabConstants.ROW_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_ROW_PROP;
		for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
		{
			AggregationCellHandle cell = measureView.getAggregationCell( j );
			String aggregationOn = cell.getModelHandle( ).getStringProperty(
					propName );
			if ( ( levelName == null && aggregationOn == null )
					|| ( levelName != null && levelName.equals( aggregationOn ) ) )
				return true;
		}
		return false;
	}

	/**
	 * Gets the measure view list that define aggregations for the row/column
	 * grand total in the crosstab. Each item in the list is instance of
	 * <code>MeasureViewHandle</code>.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static List getAggregationMeasures(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		// if crosstab is null or has no grand total, then return empty
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null )
			return Collections.EMPTY_LIST;

		List measures = new ArrayList( );
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			if ( measures.contains( measureView ) )
				continue;
			if ( isAggregationOn( measureView, null, axisType ) )
				measures.add( measureView );
		}
		return measures;
	}

	/**
	 * Gets the aggregation function for the level view sub-total. If the level
	 * view is null or not define any sub-total, return null.
	 * 
	 * @param levelView
	 * @param measureView
	 * @return
	 */
	public static String getAggregationFunction( LevelViewHandle levelView,
			MeasureViewHandle measureView )
	{
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if ( levelView == null || levelView.getAggregationHeader( ) == null
				|| levelView.getCubeLevelName( ) == null
				|| levelView.getCubeLevelName( ).length( ) <= 0
				|| measureView == null )
			return null;

		// if crosstab is not found, or level and measure not reside in the same
		// one then return null
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		if ( crosstab == null || crosstab != measureView.getCrosstab( ) )
			return null;

		String levelName = levelView.getCubeLevelName( );
		int axisType = levelView.getAxisType( );
		String propName = null;
		if ( axisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP;
		else if ( axisType == ICrosstabConstants.ROW_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_ROW_PROP;

		// retrieve all aggregations for the measure a
		for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
		{
			AggregationCellHandle cell = measureView.getAggregationCell( j );
			if ( levelName.equals( cell.getModelHandle( ).getStringProperty(
					propName ) ) )
			{
				String function = CrosstabModelUtil.getAggregationFunction(
						crosstab, cell );
				if ( function != null )
					return function;
			}
		}

		return null;
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
	public static String getAggregationFunction(
			CrosstabReportItemHandle crosstab, int axisType,
			MeasureViewHandle measureView )
	{
		// if crosstab is null or not define any grand total, then return null
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null
				|| measureView == null || crosstab != measureView.getCrosstab( ) )
			return null;

		for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
		{
			AggregationCellHandle cell = measureView.getAggregationCell( j );
			if ( ( axisType == COLUMN_AXIS_TYPE && cell
					.getAggregationOnColumn( ) == null )
					|| ( axisType == ROW_AXIS_TYPE && cell
							.getAggregationOnRow( ) == null ) )
			{
				String function = CrosstabModelUtil.getAggregationFunction(
						crosstab, cell );
				if ( function != null )
					return function;
			}
		}
		return null;
	}

	/**
	 * Inserts a dimension view to given row/column axis in the specified
	 * position.
	 * 
	 * @param crosstab
	 * @param dimensionView
	 * @param axisType
	 * @param index
	 * @param measureListMap
	 * @param functionMap
	 * @throws SemanticException
	 */
	public static void insertDimension( CrosstabReportItemHandle crosstab,
			DimensionViewHandle dimensionView, int axisType, int index,
			Map measureListMap, Map functionMap ) throws SemanticException
	{
		if ( crosstab == null || dimensionView == null
				|| !CrosstabModelUtil.isValidAxisType( axisType ) )
			return;

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( null );

		try
		{
			CrosstabViewHandle crosstabView = crosstab
					.getCrosstabView( axisType );

			if ( crosstabView == null )
			{
				// if the crosstab view is null, then create and add a crosstab
				// view first, and then add the dimension to it second;
				crosstabView = crosstab.addCrosstabView( axisType );
			}
			crosstabView.getViewsProperty( ).add(
					dimensionView.getModelHandle( ), index );

			String dimensionName = dimensionView.getCubeDimensionName( );
			// adjust measure aggregations
			for ( int i = 0; i < dimensionView.getLevelCount( ); i++ )
			{
				LevelViewHandle levelView = dimensionView.getLevel( i );
				String levelName = levelView.getCubeLevelName( );
				List measures = (List) ( measureListMap == null
						? null
						: measureListMap.get( levelName ) );
				List functions = (List) ( functionMap == null
						? null
						: functionMap.get( levelName ) );
				CrosstabModelUtil.insertLevel( crosstab, levelView,
						dimensionName, levelView.getCubeLevelName( ), axisType,
						measures, functions );
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Inserts a level handle into a dimension view. This method will add the
	 * aggregations and data-item automatically.
	 * 
	 * @param dimensionView
	 * @param levelHandle
	 * @param index
	 * @return
	 * @throws SemanticException
	 */
	public static LevelViewHandle insertLevel(
			DimensionViewHandle dimensionView, LevelHandle levelHandle,
			int index ) throws SemanticException
	{
		ExtendedItemHandle extendedItemHandle = CrosstabExtendedItemFactory
				.createLevelView( dimensionView.getModuleHandle( ), levelHandle );
		if ( extendedItemHandle == null )
			return null;

		if ( levelHandle != null )
		{
			// if cube dimension container of this cube level element is not
			// what is referred by this dimension view, then the insertion is
			// forbidden
			if ( !levelHandle.getContainer( ).getContainer( )
					.getQualifiedName( ).equals(
							dimensionView.getCubeDimensionName( ) ) )
			{
				// TODO: throw exception
				dimensionView.getLogger( ).log( Level.WARNING, "" ); //$NON-NLS-1$
				return null;
			}

			// if this level handle has referred by an existing level view,
			// then log error and do nothing
			if ( dimensionView.getLevel( levelHandle.getQualifiedName( ) ) != null )
			{
				dimensionView.getLogger( ).log( Level.SEVERE,
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL,
						levelHandle.getQualifiedName( ) );
				throw new CrosstabException( dimensionView.getModelHandle( )
						.getElement( ), new String[]{
						levelHandle.getQualifiedName( ),
						dimensionView.getModelHandle( ).getElement( )
								.getIdentifier( )},
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL );
			}
		}

		CommandStack stack = dimensionView.getCommandStack( );
		stack.startTrans( null );

		LevelViewHandle levelView = null;
		try
		{
			dimensionView.getLevelsProperty( ).add( extendedItemHandle, index );

			// if level handle is specified, then adjust aggregations
			if ( levelHandle != null )
			{
				levelView = (LevelViewHandle) getReportItem(
						extendedItemHandle, LEVEL_VIEW_EXTENSION_NAME );

				CrosstabReportItemHandle crosstab = dimensionView.getCrosstab( );
				if ( levelView != null && crosstab != null )
				{
					List measures = CrosstabModelUtil
							.getReportItems( crosstab
									.getModelHandle( )
									.getContents(
											ICrosstabReportItemConstants.MEASURES_PROP ) );
					CrosstabModelUtil.insertLevel( crosstab, levelView,
							dimensionView.getCubeDimensionName( ), levelHandle
									.getQualifiedName( ), dimensionView
									.getAxisType( ), measures, null );
				}
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );

		return levelView;
	}

	/**
	 * Gets the aggregation function for the level view sub-total. If the level
	 * view is null or not define any sub-total, return null.
	 * 
	 * @param levelView
	 * @param measureView
	 * @param function
	 * @return
	 * @throws SemanticException
	 */
	public static void setAggregationFunction( LevelViewHandle levelView,
			MeasureViewHandle measureView, String function )
			throws SemanticException
	{
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if ( levelView == null || levelView.getAggregationHeader( ) == null
				|| levelView.getCubeLevelName( ) == null
				|| levelView.getCubeLevelName( ).length( ) <= 0
				|| measureView == null )
			return;

		// if crosstab is not found, or level and measure not reside in the same
		// one then return null
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		if ( crosstab == null || crosstab != measureView.getCrosstab( ) )
			return;

		String levelName = levelView.getCubeLevelName( );
		int axisType = levelView.getAxisType( );
		String propName = null;
		if ( axisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP;
		else if ( axisType == ICrosstabConstants.ROW_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_ROW_PROP;

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( null );

		try
		{
			// retrieve all aggregations for the measure a
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( levelName.equals( cell.getModelHandle( )
						.getStringProperty( propName ) ) )
				{
					CrosstabModelUtil.setAggregationFunction( crosstab, cell,
							function );
				}
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
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
	public static void setAggregationFunction(
			CrosstabReportItemHandle crosstab, int axisType,
			MeasureViewHandle measureView, String function )
			throws SemanticException
	{
		// if crosstab is null or not define any grand total, then return null
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null
				|| measureView == null || crosstab != measureView.getCrosstab( ) )
			return;

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( null );

		try
		{
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( ( axisType == COLUMN_AXIS_TYPE && cell
						.getAggregationOnColumn( ) == null )
						|| ( axisType == ROW_AXIS_TYPE && cell
								.getAggregationOnRow( ) == null ) )
				{
					CrosstabModelUtil.setAggregationFunction( crosstab, cell,
							function );
				}
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}
}
