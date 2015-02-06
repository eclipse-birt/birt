/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;

/**
 * Utility class for Cube and XTab integration
 */

public class ChartCubeUtil extends ChartItemUtil
{

	public final static DimensionValue DEFAULT_COLUMN_WIDTH = new DimensionValue( 80,
			DesignChoiceConstants.UNITS_PT );
	public final static DimensionValue DEFAULT_ROW_HEIGHT = new DimensionValue( 30,
			DesignChoiceConstants.UNITS_PT );

	/**
	 * Returns the binding cube if the element or its container has cube binding
	 * or the reference to the cube
	 * 
	 * @param element
	 *            element handle
	 * @return the binding cube or null
	 * @since 2.3
	 */
	public static CubeHandle getBindingCube( DesignElementHandle element )
	{
		if ( element == null )
		{
			return null;
		}
		if ( element instanceof ReportItemHandle )
		{
			CubeHandle cube = ( (ReportItemHandle) element ).getCube( );
			if ( ( (ReportItemHandle) element ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				return getBindingCube( ( (ReportItemHandle) element ).getDataBindingReference( ) );
			}
			else if ( cube != null )
			{
				return cube;
			}
			else if ( ( (ReportItemHandle) element ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_DATA )
			{
				return null;
			}
		}
		if ( element.getContainer( ) != null )
		{
			return getBindingCube( element.getContainer( ) );
		}
		return null;
	}

	/**
	 * Gets all measure handles in the cube.
	 * 
	 * @param cube
	 *            cube handle
	 * @return all measure handles or empty list if no measure. The element in
	 *         list is <code>MeasureHandle</code>
	 * @since 2.3
	 */
	@SuppressWarnings("unchecked")
	public static List<MeasureHandle> getAllMeasures( CubeHandle cube )
	{
		if ( cube.getContentCount( ICubeModel.MEASURE_GROUPS_PROP ) > 0 )
		{
			List<MeasureHandle> measures = new ArrayList<MeasureHandle>( );
			Iterator<?> measureGroups = cube.getContents( ICubeModel.MEASURE_GROUPS_PROP )
					.iterator( );
			while ( measureGroups.hasNext( ) )
			{
				MeasureGroupHandle measureGroup = (MeasureGroupHandle) measureGroups.next( );
				measures.addAll( measureGroup.getContents( MeasureGroupHandle.MEASURES_PROP ) );
			}
			return measures;
		}
		return Collections.emptyList( );
	}

	/**
	 * Gets all level handles in the cube.
	 * 
	 * @param cube
	 *            cube handle
	 * @return all level handles or empty list if no level. The element in list
	 *         is <code>LevelHandle</code>
	 * @since 2.3
	 */
	@SuppressWarnings("unchecked")
	public static List<LevelHandle> getAllLevels( CubeHandle cube )
	{
		if ( cube.getContentCount( ICubeModel.DIMENSIONS_PROP ) > 0 )
		{
			List<LevelHandle> levels = new ArrayList<LevelHandle>( );
			Iterator<DimensionHandle> dimensions = cube.getContents( ICubeModel.DIMENSIONS_PROP )
					.iterator( );
			while ( dimensions.hasNext( ) )
			{
				DimensionHandle dimensionHandle = dimensions.next( );
				HierarchyHandle hierarchy = (HierarchyHandle) ( dimensionHandle ).getContent( DimensionHandle.HIERARCHIES_PROP,
						0 );
				int count = hierarchy.getLevelCount( );
				for ( int i = 0; i < count; i++ )
				{
					levels.add( hierarchy.getLevel( i ) );
				}
			}
			return levels;
		}
		return Collections.emptyList( );
	}

	/**
	 * Returns dimension number of cube.
	 * 
	 * @param cube
	 * @return dimension count
	 * @since 2.3
	 */
	public static int getDimensionCount( CubeHandle cube )
	{
		if ( cube == null )
		{
			return 0;
		}

		return cube.getContentCount( ICubeModel.DIMENSIONS_PROP );
	}

	/**
	 * Gets all innermost level expressions from cross tab.
	 * 
	 * @param xtab
	 *            cross tab
	 * @return Levels list, each element is String
	 * @since 2.3
	 */
	public static List<String> getAllLevelsBindingName(
			CrosstabReportItemHandle xtab )
	{
		List<String> levels = new ArrayList<String>( );

		// Add column levels
		Object content = getFirstContent( getInnermostLevelCell( xtab,
				ICrosstabConstants.COLUMN_AXIS_TYPE ) );
		if ( content instanceof DataItemHandle )
		{
			DataItemHandle dataItemHandle = (DataItemHandle) content;
			levels.add( dataItemHandle.getResultSetColumn( ) );
		}

		// Add row levels
		content = getFirstContent( getInnermostLevelCell( xtab,
				ICrosstabConstants.ROW_AXIS_TYPE ) );
		if ( content instanceof DataItemHandle )
		{
			DataItemHandle dataItemHandle = (DataItemHandle) content;
			levels.add( dataItemHandle.getResultSetColumn( ) );
		}
		return levels;
	}

	/**
	 * Returns dimension binding name list
	 * 
	 * @param columnBindings
	 *            all bindings
	 * @return binding name list
	 */
	public static List<String> getAllLevelsBindingName(
			Iterator<ComputedColumnHandle> columnBindings )
	{
		ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );
		List<String> bindings = new ArrayList<String>( );
		while ( columnBindings.hasNext( ) )
		{
			ComputedColumnHandle cc = columnBindings.next( );
			ChartReportItemUtil.loadExpression( exprCodec, cc );
			if ( exprCodec.isDimensionExpresion( ) )
			{
				bindings.add( cc.getName( ) );
			}
		}
		return bindings;
	}

	/**
	 * Returns any binding names excluding dimension binding. That means the
	 * list includes measure bindings or computed columns
	 * 
	 * @param columnBindings
	 *            all bindings
	 * @return binding name list
	 */
	public static List<String> getAllMeasuresBindingName(
			Iterator<ComputedColumnHandle> columnBindings )
	{
		ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );
		List<String> bindings = new ArrayList<String>( );
		while ( columnBindings.hasNext( ) )
		{
			ComputedColumnHandle cc = columnBindings.next( );
			ChartReportItemUtil.loadExpression( exprCodec, cc );
			if ( !exprCodec.isDimensionExpresion( ) )
			{
				bindings.add( cc.getName( ) );
			}
		}
		return bindings;
	}

	public static Object getFirstContent( CrosstabCellHandle cell )
	{
		if ( cell != null )
		{
			List<?> contents = cell.getContents( );
			if ( contents != null && contents.size( ) >= 1 )
			{
				return contents.get( 0 );
			}
		}
		return null;
	}
	
	public static ExtendedItemHandle findAxisChartInCell( CrosstabCellHandle cell )
	{
		if ( cell != null )
		{
			List<?> contents = cell.getContents( );
			if ( contents != null && contents.size( ) >= 1 )
			{
				for ( int i = 0; i < contents.size( ); i++ )
				{
					Object obj = contents.get( i );
					if ( obj instanceof ExtendedItemHandle
							&& isAxisChart( (ExtendedItemHandle) obj ) )
					{
						return (ExtendedItemHandle) obj;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the level by index of all levels
	 * 
	 * @param xtab
	 * @param axisType
	 * @param levelIndex
	 *            index of all levels in xtab
	 * @return level
	 */
	public static LevelViewHandle getLevel( CrosstabReportItemHandle xtab,
			int axisType, int levelIndex )
	{
		if ( xtab == null )
		{
			return null;
		}
		CrosstabViewHandle xtabView = xtab.getCrosstabView( axisType );
		if ( xtabView == null )
		{
			return null;
		}
		int countAll = 0;
		int dimensionCount = xtabView.getDimensionCount( );
		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dim = xtabView.getDimension( i );
			if ( dim != null )
			{
				if ( levelIndex >= countAll
						&& levelIndex < countAll + dim.getLevelCount( ) )
				{
					return dim.getLevel( levelIndex - countAll );
				}
				countAll += dim.getLevelCount( );
			}
		}
		return null;
	}

	public static CrosstabCellHandle getInnermostLevelCell(
			CrosstabReportItemHandle xtab, int axisType )
	{
		int levelCount = getLevelCount( xtab, axisType );
		LevelViewHandle levelView = getLevel( xtab, axisType, levelCount - 1 );
		if ( levelView != null )
		{
			return levelView.getCell( );
		}
		return null;
	}

	/**
	 * Gets the count of all levels in the xtab
	 * 
	 * @param xtab
	 * @param axisType
	 * @return level count of all levels of all dimensions in xtab
	 */
	public static int getLevelCount( CrosstabReportItemHandle xtab, int axisType )
	{
		if ( xtab == null )
		{
			return 0;
		}
		CrosstabViewHandle xtabView = xtab.getCrosstabView( axisType );
		if ( xtabView == null )
		{
			return 0;
		}
		int countAll = 0;
		int dimensionCount = xtabView.getDimensionCount( );
		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dim = xtabView.getDimension( i );
			if ( dim != null )
			{
				countAll += dim.getLevelCount( );
			}
		}
		return countAll;
	}

	/**
	 * Gets the cell in cross tab which contains the chart
	 * 
	 * @param chartHandle
	 *            the handle with chart
	 * @return the cell which contains the chart or null
	 * @throws BirtException
	 * @since 2.3
	 */
	public static AggregationCellHandle getXtabContainerCell(
			DesignElementHandle chartHandle ) throws BirtException
	{
		DesignElementHandle container = chartHandle.getContainer( );
		if ( container instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle xtabHandle = (ExtendedItemHandle) container;
			String exName = xtabHandle.getExtensionName( );
			if ( ICrosstabConstants.AGGREGATION_CELL_EXTENSION_NAME.equals( exName ) )
			{
				return (AggregationCellHandle) xtabHandle.getReportItem( );
			}
		}
		return null;
	}
	
	/**
	 * Gets the cell in cross tab which contains the chart
	 * 
	 * @param chartHandle
	 *            the handle with chart
	 * @return the cell which contains the chart or null
	 * @throws BirtException
	 * @since 2.3
	 */
	public static CrosstabCellHandle getXtabContainerCell(
			DesignElementHandle chartHandle, boolean bOnlyAggrCell ) throws BirtException
	{
		DesignElementHandle container = chartHandle.getContainer( );
		if ( container instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle xtabHandle = (ExtendedItemHandle) container;
			String exName = xtabHandle.getExtensionName( );
			if ( ICrosstabConstants.AGGREGATION_CELL_EXTENSION_NAME.equals( exName ) )
			{
				return (AggregationCellHandle) xtabHandle.getReportItem( );
			}
			if (!bOnlyAggrCell)
			{
				IReportItem cellHandle = xtabHandle.getReportItem( );
				
				if ( cellHandle instanceof CrosstabCellHandle )
				{
					return (CrosstabCellHandle) cellHandle;
				}
			}
		}
		return null;
	}

	/**
	 * Creates the dimension expression according to level
	 * 
	 * @param level
	 *            level handle
	 * @return the dimension expression or null
	 * @since 2.3
	 */
	public static String createDimensionExpression( LevelHandle level )
	{
		if ( level == null )
		{
			return null;
		}
		if ( level instanceof TabularLevelHandle
				&& ( (TabularLevelHandle) level ).getDisplayColumnName( ) != null
				&& ( (TabularLevelHandle) level ).getDisplayColumnName( )
						.trim( )
						.length( ) > 0 )
		{
			return ExpressionUtil.createJSDimensionExpression( level.getContainer( )
					.getContainer( )
					.getName( ),
					level.getName( ),
					ICubeQueryUtil.DISPLAY_NAME_ATTR );
		}
		return ExpressionUtil.createJSDimensionExpression( level.getContainer( )
				.getContainer( )
				.getName( ), level.getName( ) );
	}
	
	/**
	 * Creates the dimension expression according to level attribute
	 * 
	 * @param level
	 *            level handle
	 * @param attributeName
	 *            level attribute name
	 * @return the dimension expression or null
	 * @since 2.6
	 */
	public static String createDimensionExpression( LevelHandle level,
			String attributeName )
	{
		if ( level == null )
		{
			return null;
		}
		// If no attribute, do not return expression with ["null"] attribute
		if ( attributeName == null )
		{
			return createDimensionExpression( level );
		}
		return ExpressionUtil.createJSDimensionExpression( level.getContainer( )
				.getContainer( )
				.getName( ), level.getName( ), attributeName );
	}

	/**
	 * Creates the measure expression according to measure
	 * 
	 * @param measure
	 *            measure handle
	 * @return the measure expression or null
	 * @since 2.3
	 */
	public static String createMeasureExpression( MeasureHandle measure )
	{
		if ( measure == null )
		{
			return null;
		}
		return ExpressionUtil.createJSMeasureExpression( measure.getName( ) );
	}

	public static String createLevelBindingName( LevelHandle level )
	{
		if ( level == null )
		{
			return null;
		}
		return level.getContainer( ).getContainer( ).getName( )
				+ EXPRESSION_SPLITTOR
				+ level.getName( );
	}
	
	public static String createLevelAttrBindingName( LevelHandle level, LevelAttributeHandle levelAttr )
	{
		String levelName = createLevelBindingName( level );
		if ( levelName == null || levelAttr == null )
		{
			return null;
		}
		return levelName + EXPRESSION_SPLITTOR + levelAttr.getName( );
	}

	public static String createMeasureBindingName( MeasureHandle measure )
	{
		if ( measure == null )
		{
			return null;
		}
		return measure.getContainer( ).getName( )
				+ EXPRESSION_SPLITTOR
				+ measure.getName( );
	}

	public static ComputedColumnHandle findBinding( ReportItemHandle handle,
			String expression )
	{
		if ( expression != null )
		{
			for ( Iterator<ComputedColumnHandle> bindings = getAllColumnBindingsIterator( handle ); bindings.hasNext( ); )
			{
				ComputedColumnHandle cc = bindings.next( );
				if ( expression.equals( cc.getExpression( ) ) )
				{
					return cc;
				}
			}
		}
		return null;
	}

	public static ComputedColumnHandle findLevelBinding(
			ReportItemHandle handle, String dimensionName, String levelName )
	{
		if ( dimensionName != null && levelName != null )
		{
			ExpressionCodec exprCodec = ChartModelHelper.instance( )
					.createExpressionCodec( );
			for ( Iterator<ComputedColumnHandle> bindings = getAllColumnBindingsIterator( handle ); bindings.hasNext( ); )
			{
				ComputedColumnHandle cch = bindings.next( );
				ChartReportItemUtil.loadExpression( exprCodec, cch );
				String[] levelNames = exprCodec.getLevelNames( );
				if ( levelNames != null
						&& dimensionName.equals( levelNames[0] )
						&& levelName.equals( levelNames[1] ) )
				{
					return cch;
				}
			}
		}
		return null;
	}

	public static ComputedColumnHandle findLevelAttrBinding(
			ReportItemHandle handle, String dimensionName, String levelName, String laName )
	{
		if ( dimensionName != null && levelName != null && laName!=null )
		{
			ExpressionCodec exprCodec = ChartModelHelper.instance( )
					.createExpressionCodec( );
			for ( Iterator<ComputedColumnHandle> bindings = getAllColumnBindingsIterator( handle ); bindings.hasNext( ); )
			{
				ComputedColumnHandle cch = bindings.next( );
				ChartReportItemUtil.loadExpression( exprCodec, cch );
				String[] levelNames = exprCodec.getLevelNames( );
				if ( levelNames != null
						&& dimensionName.equals( levelNames[0] )
						&& levelName.equals( levelNames[1] ) )
				{
					if (levelNames.length>2 && laName.equals( levelNames[2] ))
					{
						return cch;
					}
				}
			}
		}
		return null;
	}

	public static ComputedColumnHandle findMeasureBinding(
			ReportItemHandle handle, String measureName )
	{
		if ( measureName != null )
		{
			ExpressionCodec exprCodec = ChartModelHelper.instance( )
					.createExpressionCodec( );
			for ( Iterator<ComputedColumnHandle> bindings = getAllColumnBindingsIterator( handle ); bindings.hasNext( ); )
			{
				ComputedColumnHandle cch = bindings.next( );
				ChartReportItemUtil.loadExpression( exprCodec, cch );
				String name = exprCodec.getMeasureName( );
				if ( name != null && measureName.equals( name ) )
				{
					return cch;
				}
			}
		}
		return null;

	}

	/**
	 * Checks current chart is in cross tab's measure cell.
	 * 
	 * @param chartHandle
	 *            the handle holding chart
	 * @return true means within cross tab, false means not
	 * @since 2.3
	 */
	public static boolean isInXTabMeasureCell( DesignElementHandle chartHandle )
	{
		DesignElementHandle container = chartHandle.getContainer( );
		if ( container instanceof ExtendedItemHandle )
		{
			String exName = ( (ExtendedItemHandle) container ).getExtensionName( );
			if ( ICrosstabConstants.AGGREGATION_CELL_EXTENSION_NAME.equals( exName ) )
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isPlotChart( DesignElementHandle chartHandle )
	{
		if ( isChartHandle( chartHandle ) )
		{
			return TYPE_PLOT_CHART.equals( chartHandle.getProperty( PROPERTY_CHART_TYPE ) );
		}
		return false;
	}

	public static boolean isAxisChart( DesignElementHandle chartHandle )
	{
		if ( isChartHandle( chartHandle ) )
		{
			return TYPE_AXIS_CHART.equals( chartHandle.getProperty( PROPERTY_CHART_TYPE ) );
		}
		return false;
	}

	/**
	 * Updates runtime model to render chart plot only.
	 * 
	 * @param cm
	 *            chart model
	 * @param bRtL
	 *            indicates if in right-to-left context
	 * @return the modified chart model
	 * @since 2.3
	 */
	public static Chart updateModelToRenderPlot( Chart cm, boolean bRtL )
	{
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes chart = (ChartWithAxes) cm;
			boolean bTransposed = chart.isTransposed( );

			chart.getLegend( ).setVisible( false );
			chart.getTitle( ).setVisible( false );
			chart.getBlock( ).getInsets( ).set( 0, 0, 0, 0 );
			chart.setReverseCategory( bTransposed || bRtL );
			// To set visible back in case of invisible in axis chart
			chart.getPlot( ).getOutline( ).setVisible( false );
			chart.getPlot( ).getClientArea( ).setVisible( true );
			chart.getPlot( ).getInsets( ).set( 0, 0, 0, 0 );
			chart.getPlot( ).getClientArea( ).getInsets( ).set( 0, 0, 0, 0 );
			chart.getPlot( ).setVerticalSpacing( 0 );
			chart.getPlot( ).setHorizontalSpacing( 0 );

			Axis xAxis = chart.getAxes( ).get( 0 );
			Axis yAxis = xAxis.getAssociatedAxes( ).get( 0 );

			xAxis.getTitle( ).setVisible( false );
			xAxis.getLabel( ).setVisible( false );
			xAxis.getLineAttributes( ).setVisible( false );
			xAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.setCategoryAxis( true );

			yAxis.getTitle( ).setVisible( false );
			yAxis.getLabel( ).setVisible( false );
			yAxis.getLineAttributes( ).setVisible( false );
			yAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			yAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );
			yAxis.setLabelWithinAxes( true );
		}
		return cm;
	}

	/**
	 * Updates runtime model to render chart axis only.
	 * 
	 * @param cm
	 *            chart model
	 * @param bRtL
	 *            indicates if in right-to-left context
	 * @return the modified chart model
	 * @since 2.3
	 */
	public static Chart updateModelToRenderAxis( Chart cm, boolean bRtL )
	{
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes chart = (ChartWithAxes) cm;
			chart.getLegend( ).setVisible( false );
			chart.getTitle( ).setVisible( false );
			chart.getPlot( ).getOutline( ).setVisible( false );
			chart.getPlot( ).getClientArea( ).setVisible( false );
			chart.getBlock( ).getInsets( ).set( 0, 0, 0, 0 );
			chart.getPlot( ).getInsets( ).set( 0, 0, 0, 0 );
			chart.getPlot( ).getClientArea( ).getInsets( ).set( 0, 0, 0, 0 );
			chart.getPlot( ).setVerticalSpacing( 0 );
			chart.getPlot( ).setHorizontalSpacing( 0 );

			boolean bTransposed = chart.isTransposed( );
			Axis xAxis = chart.getAxes( ).get( 0 );
			Axis yAxis = xAxis.getAssociatedAxes( ).get( 0 );

			xAxis.getTitle( ).setVisible( false );
			xAxis.getLabel( ).setVisible( false );
			xAxis.getLineAttributes( ).setVisible( false );
			xAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.getMajorGrid( ).getLineAttributes( ).setVisible( false );
			xAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.getMinorGrid( ).getLineAttributes( ).setVisible( false );

			yAxis.getLabel( ).setVisible( true );
			yAxis.getMajorGrid( ).getTickAttributes( ).setVisible( true );
			yAxis.getTitle( ).setVisible( false );
			yAxis.getLineAttributes( ).setVisible( false );
			yAxis.getMajorGrid( ).getLineAttributes( ).setVisible( false );
			yAxis.getMinorGrid( ).getLineAttributes( ).setVisible( false );
			yAxis.getMajorGrid( )
					.setTickStyle( bTransposed || bRtL ? TickStyle.LEFT_LITERAL
							: TickStyle.RIGHT_LITERAL );
			yAxis.setLabelPosition( bTransposed || bRtL ? Position.LEFT_LITERAL
					: Position.RIGHT_LITERAL );
			yAxis.setLabelWithinAxes( true );
			if ( bTransposed )
			{
				// Show axis in the top in vertical direction
				yAxis.getOrigin( ).setType( IntersectionType.MAX_LITERAL );
			}
		}
		else
		{
			cm = null;
		}
		return cm;
	}

	public static String generateComputedColumnName( AggregationCellHandle cell )
	{
		return generateComputedColumnName( cell, ExpressionUtil.MEASURE_INDICATOR );
	}
	
	/**
	 * Generates the name of binding which references to xtab's measure.
	 * 
	 * @param cell
	 *            measure cell or total cell
	 * @param expressionIndicator
	 *            measure expression indicator
	 */
	public static String generateComputedColumnName( AggregationCellHandle cell, String expressionIndicator )
	{
		// Re-use the binding in measure view
		if ( cell.getContents( ).size( ) > 0
				&& cell.getContents( ).get( 0 ) instanceof DataItemHandle )
		{
			DataItemHandle dataItem = (DataItemHandle) cell.getContents( )
					.get( 0 );
			String bindingName = dataItem.getResultSetColumn( );
			if ( bindingName != null )
			{
				return bindingName;
			}
		}
		MeasureViewHandle measureView = (MeasureViewHandle) cell.getContainer( );
		LevelHandle rowLevelHandle = cell.getAggregationOnRow( );
		LevelHandle colLevelHandle = cell.getAggregationOnColumn( );
		String aggregationOnRow = rowLevelHandle == null ? null
				: rowLevelHandle.getFullName( );
		String aggregationOnColumn = colLevelHandle == null ? null
				: colLevelHandle.getFullName( );

		String name = ""; //$NON-NLS-1$
		String temp = measureView.getCubeMeasureName( );
		if ( temp != null && temp.length( ) > 0 )
			name = name + temp;

		if ( aggregationOnRow != null && aggregationOnRow.length( ) > 0 )
		{
			if ( name.length( ) > 0 )
			{
				name = name + "_" + aggregationOnRow; //$NON-NLS-1$
			}
			else
			{
				name = name + aggregationOnRow;
			}
		}
		if ( aggregationOnColumn != null && aggregationOnColumn.length( ) > 0 )
		{
			if ( name.length( ) > 0 )
			{
				name = name + "_" + aggregationOnColumn; //$NON-NLS-1$
			}
			else
			{
				name = name + aggregationOnColumn;
			}
		}
		if ( name.length( ) <= 0 )
		{
			name = "measure"; //$NON-NLS-1$
		}

		ComputedColumn column = StructureFactory.newComputedColumn( cell.getCrosstabHandle( ),
				name );
		String dataType = measureView.getDataType( );
		column.setDataType( dataType );
		if ( ExpressionUtil.DATASET_ROW_INDICATOR.equals( expressionIndicator ) )
		{
			column.setExpression( ExpressionUtil.createDataSetRowExpression( measureView.getCubeMeasureName( ) ) );
		}
		else
		{
			column.setExpression( ExpressionUtil.createJSMeasureExpression( measureView.getCubeMeasureName( ) ) );
		}
		column.setAggregateFunction( getDefaultMeasureAggregationFunction( measureView ) );
		if ( aggregationOnRow != null )
		{
			column.addAggregateOn( aggregationOnRow );
		}
		if ( aggregationOnColumn != null )
		{
			column.addAggregateOn( aggregationOnColumn );
		}

		// add the computed column to crosstab
		try
		{
			ComputedColumnHandle columnHandle = ( (ReportItemHandle) cell.getCrosstabHandle( ) ).addColumnBinding( column,
					false );
			return columnHandle.getName( );
		}
		catch ( SemanticException e )
		{
			logger.log( e );
		}

		return name;
	}

	/**
	 * Returns the default aggregation function for specific measure view
	 */
	static String getDefaultMeasureAggregationFunction( MeasureViewHandle mv )
	{
		if ( mv != null && mv.getCubeMeasure( ) != null )
		{
			String func = mv.getCubeMeasure( ).getFunction( );

			if ( func != null )
			{
				return getRollUpAggregationFunction( func );
			}
		}
		// TODO default function should be not always SUM
		return DesignChoiceConstants.MEASURE_FUNCTION_SUM;
	}

	/**
	 * TODO this method should provide by DTE?
	 */
	static String getRollUpAggregationFunction( String functionName )
	{
		if ( DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE.equals( functionName )
				|| DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT.equals( functionName )
				|| DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT.equals( functionName ) )
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_SUM;
		}
		else
		{
			return functionName;
		}
	}

	/**
	 * Returns bindings names whose dimension expressions equal with specified
	 * expression.
	 * 
	 * @param dimExpr
	 * @param values
	 * @return binding name list
	 */
	public static List<String> getRelatedBindingNames( String dimExpr,
			Collection<ComputedColumnHandle> values )
	{
		List<String> bindingNames = new ArrayList<String>( 1 );
		for ( Iterator<ComputedColumnHandle> iter = values.iterator( ); iter.hasNext( ); )
		{
			ComputedColumnHandle cch = iter.next( );
			if ( dimExpr.equals( cch.getExpression( ) ) )
			{
				bindingNames.add( cch.getName( ) );
			}
		}
		return bindingNames;
	}

	/**
	 * Returns bindings names whose dimension expressions equal with specified
	 * expression.
	 * 
	 * @param dimExpr
	 * @param values
	 * @return binding name list
	 */
	public static List<String> findDimensionBindingNames( String dimName,
			String levelName, Collection<ComputedColumnHandle> bindings )
	{
		List<String> bindingNames = new ArrayList<String>( 1 );
		ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );
		for ( ComputedColumnHandle cch : bindings )
		{
			ChartReportItemUtil.loadExpression( exprCodec, cch );
			String[] levelNames = exprCodec.getLevelNames( );
			if ( levelNames != null
					&& levelNames[0].equals( dimName )
					&& levelNames[1].equals( levelName ) )
			{
				bindingNames.add( cch.getName( ) );
			}
		}
		return bindingNames;
	}
	
	/**
	 * Returns the first binding whose dimension expressions equal with
	 * specified expression.
	 * 
	 * @param dimExpr
	 * @param values
	 * @return binding name list
	 */
	public static ComputedColumnHandle findDimensionBinding(
			ExpressionCodec exprCodec, String dimName, String levelName,
			Collection<ComputedColumnHandle> bindings )
	{
		for ( ComputedColumnHandle cch : bindings )
		{
			ChartReportItemHelper.instance( ).loadExpression( exprCodec, cch );
			String[] levelNames = exprCodec.getLevelNames( );
			if ( levelNames != null
					&& levelNames[0].equals( dimName )
					&& levelNames[1].equals( levelName ) )
			{
				return cch;
			}
		}
		return null;
	}

	/**
	 * Finds the reference chart from plot chart or axis chart.
	 * 
	 * @param chartHandle
	 * @return reference chart handle or null
	 */
	@SuppressWarnings("unchecked")
	public static ExtendedItemHandle findReferenceChart(
			ExtendedItemHandle chartHandle )
	{
		if ( isAxisChart( chartHandle ) )
		{
			return (ExtendedItemHandle) chartHandle.getElementProperty( PROPERTY_HOST_CHART );
		}
		else if ( isPlotChart( chartHandle ) )
		{
			for ( Iterator<DesignElementHandle> iterator = chartHandle.clientsIterator( ); iterator.hasNext( ); )
			{
				DesignElementHandle client = iterator.next( );
				if ( isAxisChart( client ) )
				{
					return (ExtendedItemHandle) client;
				}
			}
		}
		return null;
	}

	public static boolean isDetailCell( AggregationCellHandle cell )
	{
		return IMeasureViewConstants.DETAIL_PROP.equals( cell.getModelHandle( )
				.getContainerPropertyHandle( )
				.getPropertyDefn( )
				.getName( ) );
	}

	public static boolean isAggregationCell( AggregationCellHandle cell )
	{
		return IMeasureViewConstants.AGGREGATIONS_PROP.equals( cell.getModelHandle( )
				.getContainerPropertyHandle( )
				.getPropertyDefn( )
				.getName( ) );
	}

	public static int getXTabAxisType( boolean bTransposed )
	{
		return bTransposed ? ICrosstabConstants.ROW_AXIS_TYPE
				: ICrosstabConstants.COLUMN_AXIS_TYPE;
	}

	/**
	 * Updates XTab for Axis chart sync
	 * 
	 * @param cell
	 * @param hostChartHandle
	 * @param bTransOld
	 * @param cmNew
	 * @throws BirtException
	 */
	public static void updateXTabForAxis( AggregationCellHandle cell,
			ExtendedItemHandle hostChartHandle, boolean bTransOld,
			ChartWithAxes cmNew ) throws BirtException
	{
		if ( cell == null )
		{
			return;
		}
		boolean bTransNew = cmNew.isTransposed( );
		if ( bTransOld != bTransNew )
		{
			// Update xtab direction for multiple measure case
			updateXTabDirection( cell.getCrosstab( ), bTransNew );

			// Update the chart's direction in other measures
			boolean bNewTotalJustAdded = false;
			List<ExtendedItemHandle> otherPlotCharts = findChartInOtherMeasures( cell,
					true );
			for ( int i = 0; i < otherPlotCharts.size( ); i++ )
			{
				ExtendedItemHandle chartInOtherMeasure = otherPlotCharts.get( i );
				if ( chartInOtherMeasure != null )
				{
					// Update some properties when transposing
					ChartWithAxes cwa = updateChartModelWhenTransposing( chartInOtherMeasure,
							cmNew );
					AggregationCellHandle cellAgg = getXtabContainerCell( chartInOtherMeasure );
					if ( removeAxisChartInXTab( cellAgg, bTransOld, true ) )
					{
						bNewTotalJustAdded = addAxisChartInXTab( cellAgg,
								cwa,
								chartInOtherMeasure,
								bNewTotalJustAdded );
					}
					else
					{
						// If axis chart is not deleted, just update cells
						updateAxisChartCells( cell, cmNew );
					}
				}
			}

			// Delete grand total only once, since assume that multiple
			// measures will have the same grand total
			if ( removeAxisChartInXTab( cell, bTransOld, true ) )
			{
				addAxisChartInXTab( cell,
						cmNew,
						hostChartHandle,
						bNewTotalJustAdded );
			}
			else
			{
				// If axis chart is not deleted, just update cells
				updateAxisChartCells( cell, cmNew );
			}
		}
	}

	private static ChartWithAxes updateChartModelWhenTransposing(
			ExtendedItemHandle eih, ChartWithAxes cmFrom )
			throws ExtendedElementException
	{
		IChartReportItem reportItem = (IChartReportItem) eih.getReportItem( );
		ChartWithAxes cmOld = (ChartWithAxes) reportItem.getProperty( ChartReportItemConstants.PROPERTY_CHART );
		ChartWithAxes cmNew = cmOld.copyInstance( );

		cmNew.setTransposed( cmFrom.isTransposed( ) );
		// To resolve potential wrong axis type issue when flipping axes
		cmNew.getBaseAxes( )[0].setType( AxisType.TEXT_LITERAL );
		Query queryFrom = cmFrom.getAxes( )
				.get( 0 )
				.getSeriesDefinitions( )
				.get( 0 )
				.getDesignTimeSeries( )
				.getDataDefinition( )
				.get( 0 );
		Query queryTo = cmNew.getAxes( )
				.get( 0 )
				.getSeriesDefinitions( )
				.get( 0 )
				.getDesignTimeSeries( )
				.getDataDefinition( )
				.get( 0 );
		queryTo.setDefinition( queryFrom.getDefinition( ) );
		reportItem.executeSetModelCommand( eih, cmOld, cmNew );
		return cmNew;
	}

	public static void updateXTabDirection( CrosstabReportItemHandle xtab,
			boolean bTransposed ) throws SemanticException
	{
		if ( bTransposed )
		{
			if ( !ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL.equals( xtab.getMeasureDirection( ) ) )
			{
				xtab.setMeasureDirection( ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL );
			}
		}
		else
		{
			if ( !ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( xtab.getMeasureDirection( ) ) )
			{
				xtab.setMeasureDirection( ICrosstabConstants.MEASURE_DIRECTION_VERTICAL );
			}
		}
	}

	/**
	 * Finds the plot/axis chart handle in other measures. If only one measure,
	 * return empty list.
	 * 
	 * @param cell
	 * @param bPlotChart
	 *            true returns plot chart, false returns axis true
	 * @return list contains plot chart handle
	 */
	public static List<ExtendedItemHandle> findChartInOtherMeasures(
			AggregationCellHandle cell, boolean bPlotChart )
	{
		if ( cell.getCrosstab( ).getMeasureCount( ) > 1 )
		{
			List<ExtendedItemHandle> list = new ArrayList<ExtendedItemHandle>( );
			for ( int i = 0; i < cell.getCrosstab( ).getMeasureCount( ); i++ )
			{
				MeasureViewHandle mv = cell.getCrosstab( ).getMeasure( i );
				if ( mv == cell.getContainer( ) )
				{
					// Do not check current measure
					continue;
				}
				// Check detail cell
				AggregationCellHandle aggCell = mv.getCell( );
				Object content = ChartCubeUtil.getFirstContent( aggCell );
				if ( bPlotChart
						&& ChartCubeUtil.isPlotChart( (DesignElementHandle) content )
						|| !bPlotChart
						&& ChartCubeUtil.isAxisChart( (DesignElementHandle) content ) )
				{
					list.add( (ExtendedItemHandle) content );
				}
				// Check total cells
				for ( int j = 0; j < mv.getAggregationCount( ); j++ )
				{
					aggCell = mv.getAggregationCell( j );
					content = ChartCubeUtil.getFirstContent( aggCell );
					if ( bPlotChart
							&& ChartCubeUtil.isPlotChart( (DesignElementHandle) content )
							|| !bPlotChart
							&& ChartCubeUtil.isAxisChart( (DesignElementHandle) content ) )
					{
						list.add( (ExtendedItemHandle) content );
					}
				}
			}
			return list;
		}
		return Collections.emptyList( );
	}

	/**
	 * Removes Axis chart in Xtab.
	 * 
	 * @param cell
	 * @param bTransposed
	 * @param bCleanSpan
	 *            indicates if column and row span need to clean
	 * @return if axis chart is removed
	 * @throws BirtException
	 */
	public static boolean removeAxisChartInXTab( AggregationCellHandle cell,
			boolean bTransposed, boolean bCleanSpan ) throws BirtException
	{
		if ( bCleanSpan )
		{
			cell.setSpanOverOnRow( null );
			cell.setSpanOverOnColumn( null );
		}
		AggregationCellHandle grandTotalAggCell = getGrandTotalAggregationCell( cell,
				bTransposed );
		if ( grandTotalAggCell != null
				&& grandTotalAggCell.getContents( ).size( ) > 0 )
		{
			ExtendedItemHandle axisChart = findAxisChartInCell( grandTotalAggCell );
			if ( axisChart != null )
			{
				axisChart.dropAndClear( );
			}

			if ( isEmptyInAllGrandTotalCells( cell.getCrosstab( ), bTransposed ) )
			{
				// Delete blank grand total cell
				cell.getCrosstab( )
						.removeGrandTotal( getXTabAxisType( bTransposed ) );
			}
			return true;
		}
		return false;
	}

	public static AggregationCellHandle getGrandTotalAggregationCell(
			AggregationCellHandle cell, boolean bTransposed )
	{
		if ( cell == null )
		{
			return null;
		}
		if ( bTransposed )
		{
			return ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( null,
					null,
					cell.getDimensionName( ICrosstabConstants.COLUMN_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.COLUMN_AXIS_TYPE ) );
		}
		else
		{
			return ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( cell.getDimensionName( ICrosstabConstants.ROW_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.ROW_AXIS_TYPE ),
					null,
					null );
		}
	}

	private static boolean isEmptyInAllGrandTotalCells(
			CrosstabReportItemHandle xtab, boolean bTransposed )
	{
		for ( int i = 0; i < xtab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle mv = xtab.getMeasure( i );
			AggregationCellHandle grandTotalCell = getGrandTotalAggregationCell( mv.getCell( ),
					bTransposed );
			if ( grandTotalCell != null
					&& grandTotalCell.getContents( ).size( ) > 0 )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds Axis chart in XTab
	 * 
	 * @param cell
	 * @param cwa
	 * @param chartHandle
	 * @param bNewTotalJustAdded
	 *            the flag indicates if grand total is just added
	 * @throws BirtException
	 * 
	 */
	public static boolean addAxisChartInXTab( AggregationCellHandle cell,
			ChartWithAxes cwa, ExtendedItemHandle hostChartHandle,
			boolean bNewTotalJustAdded ) throws BirtException
	{
		// Update cells that contain axis charts
		if ( !updateAxisChartCells( cell, cwa ) )
		{
			return false;
		}

		if ( !isYAxisVisible( cwa ) )
		{
			// Do not add axis chart if Y axis is invisible
			return bNewTotalJustAdded;
		}

		boolean bTransposed = cwa.isTransposed( );
		int axisType = getXTabAxisType( bTransposed );

		// Create grand total cell on demand
		// If just added, even if grand total is not added this time, delete
		// data item
		boolean bNewGrandTotol = bNewTotalJustAdded;
		if ( cell.getCrosstab( ).getGrandTotal( axisType ) == null )
		{
			bNewGrandTotol = true;
			// Add grand total and remove inner items
			cell.getCrosstab( ).addGrandTotal( axisType );
			deleteGrandTotalItems( cell.getCrosstab( ), bTransposed );
		}

		AggregationCellHandle grandTotalAggCell;
		if ( bTransposed )
		{
			grandTotalAggCell = ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( null,
					null,
					cell.getDimensionName( ICrosstabConstants.COLUMN_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.COLUMN_AXIS_TYPE ) );
		}
		else
		{
			grandTotalAggCell = ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( cell.getDimensionName( ICrosstabConstants.ROW_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.ROW_AXIS_TYPE ),
					null,
					null );
		}
		initCellSize( grandTotalAggCell );
		if ( bNewGrandTotol )
		{
			// Only delete data item in grand total when it's created by chart
			Object content = ChartCubeUtil.getFirstContent( grandTotalAggCell );
			if ( content instanceof DesignElementHandle )
			{
				( (DesignElementHandle) content ).dropAndClear( );
			}
		}
		
		// If axis chart exists in grand total cell, should not add another axis
		// chart again
		if ( findAxisChartInCell( grandTotalAggCell ) != null )
		{
			return bNewTotalJustAdded;
		}
		
		if ( grandTotalAggCell != null )
		{
			// Create axis chart handle which references to host chart
			ExtendedItemHandle axisChartHandle = createChartHandle( cell.getModelHandle( ),
					ChartReportItemConstants.TYPE_AXIS_CHART,
					hostChartHandle );
			grandTotalAggCell.addContent( axisChartHandle, 0 );
		}
		return bNewGrandTotol;
	}

	public static boolean addAxisChartInXTab( AggregationCellHandle cell,
			ChartWithAxes cwa, ExtendedItemHandle hostChartHandle )
			throws BirtException
	{
		return addAxisChartInXTab( cell,
				cwa,
				hostChartHandle,
				!ChartInXTabStatusManager.hasGrandItem( hostChartHandle ) );
	}
	
	private static void initCellSize( AggregationCellHandle cell )
			throws BirtException
	{
		if ( cell.getWidth( ) == null || cell.getWidth( ).getMeasure( ) == 0 )
		{
			// Set a default width to avoid null size issue in fixed layout
			cell.getCrosstab( ).setColumnWidth( cell, DEFAULT_COLUMN_WIDTH );
		}
		if ( cell.getHeight( ) == null || cell.getHeight( ).getMeasure( ) == 0 )
		{
			// Set a default height to avoid clipping issue in fixed layout
			cell.getCrosstab( ).setRowHeight( cell, DEFAULT_ROW_HEIGHT );
		}
	}

	/**
	 * Updates cells that contains axis chart
	 * 
	 * @param cell
	 *            cell that contains axis chart
	 * @param cwa
	 *            chart model
	 * @return if cell is updated completely
	 * @throws BirtException
	 */
	public static boolean updateAxisChartCells( AggregationCellHandle cell,
			ChartWithAxes cwa ) throws BirtException
	{
		initCellSize( cell );
		
		boolean bTransposed = cwa.isTransposed( );
		if ( bTransposed )
		{
			// Set cell span
			cell.setSpanOverOnRow( cell.getAggregationOnRow( ) );
			cell.setSpanOverOnColumn( null );
			CrosstabCellHandle rowCell = ChartCubeUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.ROW_AXIS_TYPE );
			if ( rowCell == null )
			{
				return false;
			}
			if ( rowCell.getHeight( ) == null
					|| rowCell.getHeight( ).getMeasure( ) == 0 )
			{
				// Set a default height for cell to fit with chart
				cell.getCrosstab( ).setRowHeight( rowCell, DEFAULT_ROW_HEIGHT );
			}
			// Set 0 padding to level cell to avoid size difference between
			// browsers
			removeCellPadding( rowCell, bTransposed );
		}
		else
		{
			// Set cell span
			cell.setSpanOverOnColumn( cell.getAggregationOnColumn( ) );
			cell.setSpanOverOnRow( null );
			CrosstabCellHandle columnCell = ChartCubeUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.COLUMN_AXIS_TYPE );
			if ( columnCell == null )
			{
				return false;
			}
			if ( columnCell.getWidth( ) != null
					|| columnCell.getWidth( ).getMeasure( ) == 0 )
			{
				// Set a default width for cell to fit with chart
				cell.getCrosstab( ).setColumnWidth( columnCell,
						DEFAULT_COLUMN_WIDTH );
			}
			// Set 0 padding to level cell to avoid size difference between
			// browsers
			removeCellPadding( columnCell, bTransposed );
		}
		// Set 0 padding to all related aggregation cells to avoid size
		// difference between browsers
		removeCellPadding( cell, bTransposed );
		removeCellPadding( getGrandTotalAggregationCell( cell, !bTransposed ),
				bTransposed );
		removeCellPadding( getMeasureAggregationCell( cell ), bTransposed );

		return true;
	}

	/**
	 * Deletes all data item in specified grand total.
	 * 
	 * @param xtab
	 * @param bTransposed
	 * @throws SemanticException
	 */
	private static void deleteGrandTotalItems( CrosstabReportItemHandle xtab,
			boolean bTransposed ) throws SemanticException
	{
		for ( int i = 0; i < xtab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle mv = xtab.getMeasure( i );
			AggregationCellHandle aggCell = getGrandTotalAggregationCell( mv.getCell( ),
					bTransposed );
			Object content = getFirstContent( aggCell );
			if ( content instanceof DataItemHandle )
			{
				( (DataItemHandle) content ).dropAndClear( );
			}
		}
	}

	/**
	 * Updates Axis chart in Xtab by replacing date item with axis chart.
	 * 
	 * @param cell
	 * @param cwa
	 * @param hostChartHandle
	 * @throws BirtException
	 */
	public static void updateAxisChart( AggregationCellHandle cell,
			ChartWithAxes cwa, ExtendedItemHandle hostChartHandle )
			throws BirtException
	{
		boolean bTransposed = cwa.isTransposed( );
		if ( getGrandTotalCell( cell, bTransposed ) != null )
		{
			AggregationCellHandle grandTotalAggCell = getGrandTotalAggregationCell( cell,
					bTransposed );

			Object content = ChartCubeUtil.getFirstContent( grandTotalAggCell );
			if ( content instanceof DataItemHandle )
			{
				// Do not delete the data item
				// Create axis chart handle, and insert it before data item
				if ( isYAxisVisible( cwa ) )
				{
					ExtendedItemHandle axisChartHandle = createChartHandle( cell.getModelHandle( ),
							ChartReportItemConstants.TYPE_AXIS_CHART,
							hostChartHandle );
					grandTotalAggCell.addContent( axisChartHandle, 0 );
				}
				if ( !ChartInXTabStatusManager.hasGrandItem( cell ) )
				{
					// Delete it since it doesn't exist before update
					( (DataItemHandle) content ).dropAndClear( );
				}
			}
			else if ( ChartCubeUtil.isAxisChart( (DesignElementHandle) content ) )
			{
				// If axis chart doesn't host this plot chart, update the
				// hostChart reference
				if ( ( (ExtendedItemHandle) content ).getElementProperty( PROPERTY_HOST_CHART ) != hostChartHandle )
				{
					( (ExtendedItemHandle) content ).setProperty( PROPERTY_HOST_CHART,
							hostChartHandle );
				}
			}
			else if ( content == null )
			{
				// Add axis chart if no content in grand total
				addAxisChartInXTab( cell, cwa, hostChartHandle );
			}
		}
		else
		{
			// Add axis chart if no grand total
			addAxisChartInXTab( cell, cwa, hostChartHandle );
		}
	}

	public static AggregationCellHandle getMeasureAggregationCell(
			AggregationCellHandle cell )
	{
		if ( cell == null )
		{
			return null;
		}
		return ( (MeasureViewHandle) cell.getContainer( ) ).getCell( );
	}

	private static CrosstabCellHandle getGrandTotalCell(
			CrosstabCellHandle cell, boolean bTransposed )
	{
		if ( cell == null )
		{
			return null;
		}
		return cell.getCrosstab( )
				.getGrandTotal( bTransposed ? ICrosstabConstants.ROW_AXIS_TYPE
						: ICrosstabConstants.COLUMN_AXIS_TYPE );
	}

	public static ExtendedItemHandle createChartHandle(
			DesignElementHandle anyHandle, String chartType,
			ExtendedItemHandle hostChartHandle ) throws SemanticException
	{
		ExtendedItemHandle chartHandle = anyHandle.getElementFactory( )
				.newExtendedItem( null,
						ChartReportItemConstants.CHART_EXTENSION_NAME );
		if ( chartType != null )
		{
			chartHandle.setProperty( ChartReportItemConstants.PROPERTY_CHART_TYPE,
					chartType );
		}
		if ( hostChartHandle != null )
		{
			chartHandle.setProperty( ChartReportItemConstants.PROPERTY_HOST_CHART,
					hostChartHandle );
		}
		else
		{
			String namePrexif = Messages.getString( (String) chartHandle.getDefn( )
					.getDisplayNameKey( ) );
			chartHandle.makeUniqueName( namePrexif );
		}
		return chartHandle;
	}

	private static boolean isYAxisVisible( ChartWithAxes cwa )
	{
		Axis yAxis = cwa.getAxes( ).get( 0 ).getAssociatedAxes( ).get( 0 );
		return yAxis.getLineAttributes( ).isVisible( );
	}

	private static void removeCellPadding( CrosstabCellHandle cell,
			boolean bTransposed ) throws SemanticException
	{
		if ( cell != null )
		{
			if ( bTransposed )
			{
				cell.getModelHandle( )
						.setProperty( StyleHandle.PADDING_TOP_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
				cell.getModelHandle( )
						.setProperty( StyleHandle.PADDING_BOTTOM_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
			}
			else
			{
				cell.getModelHandle( )
						.setProperty( StyleHandle.PADDING_LEFT_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
				cell.getModelHandle( )
						.setProperty( StyleHandle.PADDING_RIGHT_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
			}
		}
	}

	/**
	 * Check if current column bindings contain one dimension and one measure at
	 * least.
	 * 
	 * @param columnBindings
	 * @return check result
	 */
	public static boolean checkColumnbindingForCube(
			Iterator<ComputedColumnHandle> columnBindings )
	{
		boolean containDimension = false;
		boolean containMeasure = false;
		ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );
		while ( columnBindings.hasNext( ) )
		{
			ComputedColumnHandle cc = columnBindings.next( );
			ChartReportItemUtil.loadExpression( exprCodec, cc );

			if ( !containDimension && exprCodec.isDimensionExpresion( ) )
			{
				containDimension = true;
			}
			if ( !containMeasure && exprCodec.isMeasureExpresion( ) )
			{
				containMeasure = true;
			}
		}
		return containDimension && containMeasure;
	}

}
