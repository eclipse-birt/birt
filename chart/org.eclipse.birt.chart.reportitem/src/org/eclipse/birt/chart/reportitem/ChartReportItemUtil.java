/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil implements ChartReportItemConstants
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * Returns the element handle which can save binding columns the given
	 * element
	 * 
	 * @param handle
	 *            the handle of the element which needs binding columns
	 * @return the holder for the element,or itself if no holder available
	 */
	public static ReportItemHandle getBindingHolder( DesignElementHandle handle )
	{
		if ( handle instanceof ReportElementHandle )
		{
			if ( handle instanceof ListingHandle )
			{
				return (ReportItemHandle) handle;
			}
			if ( handle instanceof ReportItemHandle )
			{
				if ( ( (ReportItemHandle) handle ).getDataBindingReference( ) != null
						|| ( (ReportItemHandle) handle ).getCube( ) != null
						|| ( (ReportItemHandle) handle ).getDataSet( ) != null )
				{
					return (ReportItemHandle) handle;
				}
			}

			return getBindingHolder( handle.getContainer( ) );
		}
		return null;
	}

	/**
	 * Checks if shared scale is needed when computation
	 * 
	 * @param eih
	 *            handle
	 * @param cm
	 *            chart model
	 * @return shared binding needed or not
	 * @since 2.3
	 */
	public static boolean canScaleShared( ReportItemHandle eih, Chart cm )
	{
		return cm instanceof ChartWithAxes
				&& eih.getDataSet( ) == null && getBindingHolder( eih ) != null
				&& ChartXTabUtil.isInXTabMeasureCell( eih );
	}

	/**
	 * @return Returns if current eclipse environment is RtL.
	 */
	public static boolean isRtl( )
	{
		// get -dir rtl option
		boolean rtl = false;
		String eclipseCommands = System.getProperty( "eclipse.commands" ); //$NON-NLS-1$
		if ( eclipseCommands != null )
		{
			String[] options = eclipseCommands.split( "-" ); //$NON-NLS-1$
			String regex = "[\\s]*[dD][iI][rR][\\s]*[rR][tT][lL][\\s]*"; //$NON-NLS-1$
			Pattern pattern = Pattern.compile( regex );
			for ( int i = 0; i < options.length; i++ )
			{
				String option = options[i];
				if ( pattern.matcher( option ).matches( ) )
				{
					rtl = true;
					break;
				}
			}
		}
		return rtl;
	}

	/**
	 * Gets all column bindings from handle and its container
	 * 
	 * @param itemHandle
	 *            handle
	 * @return Iterator of all bindings
	 */
	public static Iterator getColumnDataBindings( ReportItemHandle itemHandle )
	{
		if ( itemHandle.getDataSet( ) != null || itemHandle.getCube( ) != null )
		{
			return itemHandle.columnBindingsIterator( );
		}
		DesignElementHandle handle = getBindingHolder( itemHandle );
		if ( handle instanceof ReportItemHandle )
		{
			ArrayList list = new ArrayList( );
			Iterator i = ( (ReportItemHandle) handle ).columnBindingsIterator( );
			while ( i.hasNext( ) )
			{
				list.add( i.next( ) );
			}
			if ( handle != itemHandle )
			{
				// Do not add same handle twice
				i = itemHandle.columnBindingsIterator( );
				while ( i.hasNext( ) )
				{
					list.add( i.next( ) );
				}
			}
			return list.iterator( );
		}
		return null;
	}

	/**
	 * Convert group unit type from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static int convertToDtEGroupUnit( DataType dataType,
			GroupingUnitType groupUnitType, double intervalRange )
	{
		if ( dataType == DataType.NUMERIC_LITERAL )
		{
			if ( intervalRange == 0 )
			{
				return IGroupDefinition.NO_INTERVAL;
			}

			return IGroupDefinition.NUMERIC_INTERVAL;
		}
		else if ( dataType == DataType.DATE_TIME_LITERAL )
		{
			switch ( groupUnitType.getValue( ) )
			{
				case GroupingUnitType.SECONDS :
					return IGroupDefinition.SECOND_INTERVAL;

				case GroupingUnitType.MINUTES :
					return IGroupDefinition.MINUTE_INTERVAL;

				case GroupingUnitType.HOURS :
					return IGroupDefinition.HOUR_INTERVAL;

				case GroupingUnitType.DAYS :
					return IGroupDefinition.DAY_INTERVAL;

				case GroupingUnitType.WEEKS :
					return IGroupDefinition.WEEK_INTERVAL;

				case GroupingUnitType.MONTHS :
					return IGroupDefinition.MONTH_INTERVAL;

				case GroupingUnitType.QUARTERS :
					return IGroupDefinition.QUARTER_INTERVAL;

				case GroupingUnitType.YEARS :
					return IGroupDefinition.YEAR_INTERVAL;
			}
		}
		else if ( dataType == DataType.TEXT_LITERAL )
		{
			switch ( groupUnitType.getValue( ) )
			{
				case GroupingUnitType.STRING_PREFIX :
					return IGroupDefinition.STRING_PREFIX_INTERVAL;
			}

			return IGroupDefinition.NO_INTERVAL;
		}

		return IGroupDefinition.NO_INTERVAL;
	}

	/**
	 * Convert interval range from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static double convertToDtEIntervalRange( DataType dataType,
			GroupingUnitType groupUnitType, double intervalRange )
	{
		double range = intervalRange;
		if ( Double.isNaN( intervalRange ) )
		{
			range = 0;
		}

		if ( dataType == DataType.DATE_TIME_LITERAL && range <= 0 )
		{
			range = 1;
		}
		else if ( dataType == DataType.TEXT_LITERAL )
		{
			return (long) range;
		}

		return range;
	}

	/**
	 * Convert sort direction from Chart's to DtE's.
	 * 
	 * @param sortOption
	 * @since BIRT 2.3
	 */
	public static int convertToDtESortDirection( SortOption sortOption )
	{
		if ( sortOption == SortOption.ASCENDING_LITERAL )
		{
			return IGroupDefinition.SORT_ASC;
		}
		else if ( sortOption == SortOption.DESCENDING_LITERAL )
		{
			return IGroupDefinition.SORT_DESC;
		}
		return IGroupDefinition.NO_SORT;
	}

	/**
	 * Convert aggregation name from Chart's to DtE's.
	 * 
	 * @param agg
	 * @since BIRT 2.3
	 */
	public static String convertToDtEAggFunction( String agg )
	{

		if ( "Sum".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_SUM;

		}
		else if ( "Average".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE;

		}
		else if ( "Count".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT;

		}
		else if ( "DistinctCount".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT;

		}
		else if ( "First".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST;

		}
		else if ( "Last".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_LAST;

		}
		else if ( "Min".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MIN;

		}
		else if ( "Max".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MAX;
		}
		else if ( "WeightedAverage".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG;
		}
		else if ( "Median".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN;
		}
		else if ( "Mode".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MODE;
		}
		else if ( "STDDEV".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV;
		}
		else if ( "Variance".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE;
		}
		else if ( "Irr".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IRR;
		}
		else if ( "Mirr".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR;
		}
		else if ( "NPV".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_NPV;
		}
		else if ( "Percentile".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE;
		}
		else if ( "Quartile".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE;
		}
		else if ( "MovingAverage".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE;
		}
		else if ( "RunningSum".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM;
		}
		else if ( "RunningNPV".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV;
		}
		else if ( "Rank".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RANK;
		}
		else if ( "Top".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N;
		}
		else if ( "TopPercent".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT;
		}
		else if ( "Bottom".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N;
		}
		else if ( "BottomPercent".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT;
		}
		else if ( "PercentRank".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK;
		}
		else if ( "PercentSum".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM;
		}
		else if ( "RunningCount".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT;
		}

		return null;
	}

	/**
	 * Checks if result set is empty
	 * 
	 * @param set
	 *            result set
	 * @throws BirtException
	 * @since 2.3
	 */
	public static boolean isEmpty( IBaseResultSet set ) throws BirtException
	{
		if ( set instanceof IQueryResultSet )
		{
			return ( (IQueryResultSet) set ).isEmpty( );
		}
		// TODO add code to check empty for ICubeResultSet
		return false;
	}

	/**
	 * Check if Y grouping is defined.
	 * 
	 * @param orthSeriesDefinition
	 * @since BIRT 2.3
	 */
	public static boolean isYGroupingDefined(
			SeriesDefinition orthSeriesDefinition )
	{
		if ( orthSeriesDefinition == null )
		{
			return false;
		}
		String yGroupExpr = null;
		if ( orthSeriesDefinition.getQuery( ) != null )
		{
			yGroupExpr = orthSeriesDefinition.getQuery( ).getDefinition( );
		}

		return yGroupExpr != null && !"".equals( yGroupExpr ); //$NON-NLS-1$
	}

	/**
	 * Check if base series grouping is defined.
	 * 
	 * @param baseSD
	 * @since BIRT 2.3
	 */
	public static boolean isBaseGroupingDefined( SeriesDefinition baseSD )
	{
		if ( baseSD.getGrouping( ) != null && baseSD.getGrouping( ).isEnabled( ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * Check if current chart has defined grouping.
	 * 
	 * @param cm
	 * @since BIRT 2.3
	 */
	public static boolean canContainGrouping( Chart cm )
	{
		SeriesDefinition baseSD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );

			orthAxisArray = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0], true );
			orthSD = (SeriesDefinition) ( (Axis) orthAxisArray[0] ).getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( ).get( 0 );
			orthSD = (SeriesDefinition) baseSD.getSeriesDefinitions( ).get( 0 );
		}

		if ( isBaseGroupingDefined( baseSD ) || isYGroupingDefined( orthSD ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * Check if chart has aggregation.
	 * 
	 * @param cm
	 */
	public static boolean hasAggregation( Chart cm )
	{
		SeriesDefinition baseSD = null;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( ).get( 0 );
		}

		if ( isBaseGroupingDefined( baseSD ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * Finds chart report item from handle
	 * 
	 * @param eih
	 *            extended item handle with chart
	 * @since 2.3
	 */
	public static ChartReportItemImpl getChartReportItemFromHandle(
			ExtendedItemHandle eih )
	{
		ChartReportItemImpl item = null;
		if ( !isChartHandle( eih ) )
		{
			return null;
		}
		try
		{
			item = (ChartReportItemImpl) eih.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( e );
		}
		if ( item == null )
		{
			try
			{
				eih.loadExtendedElement( );
				item = (ChartReportItemImpl) eih.getReportItem( );
			}
			catch ( ExtendedElementException eeex )
			{
				logger.log( eeex );
			}
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemPresentationImpl.log.UnableToLocateWrapper" ) ); //$NON-NLS-1$
			}
		}
		return item;
	}

	/**
	 * Checks if the object is handle with Chart model
	 * 
	 * @param content
	 *            the object to check
	 * @since 2.3
	 */
	public static boolean isChartHandle( Object content )
	{
		return content instanceof ExtendedItemHandle
				&& CHART_EXTENSION_NAME.equals( ( (ExtendedItemHandle) content ).getExtensionName( ) );
	}

	/**
	 * Finds Chart model from handle
	 * 
	 * @param handle
	 *            the handle with chart
	 * @since 2.3
	 */
	public static Chart getChartFromHandle( ExtendedItemHandle handle )
	{
		ChartReportItemImpl item = getChartReportItemFromHandle( handle );
		if ( item == null )
		{
			return null;
		}
		return (Chart) ( item ).getProperty( PROPERTY_CHART );
	}

	/**
	 * Gets all column bindings. If the handle's contain has column bindings,
	 * will combine the bindings with the handle's.
	 * 
	 * @param itemHandle
	 *            handle
	 * @return the iterator of all column bindings
	 * @since 2.3
	 */
	public static Iterator getAllColumnBindingsIterator(
			ReportItemHandle itemHandle )
	{
		ReportItemHandle container = ChartReportItemUtil.getBindingHolder( itemHandle );
		if ( container != null && container != itemHandle )
		{
			// Add all bindings to an iterator
			List allBindings = new ArrayList( );
			for ( Iterator ownBindings = itemHandle.columnBindingsIterator( ); ownBindings.hasNext( ); )
			{
				allBindings.add( ownBindings.next( ) );
			}
			for ( Iterator containerBindings = container.columnBindingsIterator( ); containerBindings.hasNext( ); )
			{
				allBindings.add( containerBindings.next( ) );
			}
			return allBindings.iterator( );
		}
		return itemHandle.columnBindingsIterator( );
	}

	/**
	 * Transforms dimension value to points.
	 * 
	 * @param handle
	 * @param dpi
	 *            to convert px unit
	 * 
	 * @return the dimension value with measure of points
	 * @since 2.3
	 */
	public static double convertToPoints(
			org.eclipse.birt.report.model.api.DimensionHandle handle, int dpi )
	{
		double retValue = 0.0;

		if ( handle.isSet( )
				&& handle.getMeasure( ) > 0
				&& handle.getUnits( ).trim( ).length( ) > 0 )
		{
			if ( handle.getUnits( ) == DesignChoiceConstants.UNITS_PT )
			{
				retValue = handle.getMeasure( );
			}
			else if ( handle.getUnits( ) == DesignChoiceConstants.UNITS_PX )
			{
				retValue = ( handle.getMeasure( ) * 72d ) / dpi;
			}
			else
			{
				retValue = DimensionUtil.convertTo( handle.getMeasure( ),
						handle.getUnits( ),
						DesignChoiceConstants.UNITS_PT ).getMeasure( );
			}
		}
		return retValue;
	}

	/**
	 * Creates the default bounds for chart model.
	 * 
	 * @param eih
	 *            chart handle
	 * @param cm
	 *            chart model
	 * @return default bounds
	 * @since 2.3
	 */
	public static Bounds createDefaultChartBounds( ExtendedItemHandle eih,
			Chart cm )
	{
		// Axis chart case
		if ( ChartXTabUtil.isAxisChart( eih ) )
		{
			// Axis chart must be ChartWithAxes
			ChartWithAxes cmWA = (ChartWithAxes) cm;
			if ( cmWA.isTransposed( ) )
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_BLOCK_WIDTH,
						DEFAULT_AXIS_CHART_BLOCK_SIZE );
			}
			else
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_AXIS_CHART_BLOCK_SIZE,
						DEFAULT_CHART_BLOCK_HEIGHT );
			}
		}
		// Plot or ordinary chart case
		else
		{
			return BoundsImpl.create( 0,
					0,
					DEFAULT_CHART_BLOCK_WIDTH,
					DEFAULT_CHART_BLOCK_HEIGHT );
		}
	}

	/**
	 * Convert model/engine aggregate expression to chart.
	 * 
	 * @param agg
	 * @since 2.3
	 */
	public static String convertToChartAggExpression( String agg )
	{

		if ( DesignChoiceConstants.AGGREGATION_FUNCTION_SUM.equalsIgnoreCase( agg ) )
		{
			return "Sum"; //$NON-NLS-1$

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE.equalsIgnoreCase( agg ) )
		{
			return "Average"; //$NON-NLS-1$

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT.equalsIgnoreCase( agg ) )
		{
			return "Count"; //$NON-NLS-1$

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT.equalsIgnoreCase( agg ) )
		{
			return "DistinctCount"; //$NON-NLS-1$

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST.equalsIgnoreCase( agg ) )
		{
			return "First"; //$NON-NLS-1$

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_LAST.equalsIgnoreCase( agg ) )
		{
			return "Last"; //$NON-NLS-1$

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MIN.equalsIgnoreCase( agg ) )
		{
			return "Min"; //$NON-NLS-1$

		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MAX.equalsIgnoreCase( agg ) )
		{
			return "Max"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG.equalsIgnoreCase( agg ) )
		{
			return "WeightedAverage"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN.equalsIgnoreCase( agg ) )
		{
			return "Median"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MODE.equalsIgnoreCase( agg ) )
		{
			return "Mode"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV.equalsIgnoreCase( agg ) )
		{
			return "STDDEV"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE.equalsIgnoreCase( agg ) )
		{
			return "Variance"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IRR.equalsIgnoreCase( agg ) )
		{
			return "Irr"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR.equalsIgnoreCase( agg ) )
		{
			return "Mirr"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_NPV.equalsIgnoreCase( agg ) )
		{
			return "NPV"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE.equalsIgnoreCase( agg ) )
		{
			return "Percentile"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE.equalsIgnoreCase( agg ) )
		{
			return "Quartile"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE.equalsIgnoreCase( agg ) )
		{
			return "MovingAverage"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM.equalsIgnoreCase( agg ) )
		{
			return "RunningSum"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV.equalsIgnoreCase( agg ) )
		{
			return "RunningNPV"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RANK.equalsIgnoreCase( agg ) )
		{
			return "Rank"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N.equalsIgnoreCase( agg ) )
		{
			return "Top"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT.equalsIgnoreCase( agg ) )
		{
			return "TopPercent"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N.equalsIgnoreCase( agg ) )
		{
			return "Bottom"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT.equalsIgnoreCase( agg ) )
		{
			return "BottomPercent"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK.equalsIgnoreCase( agg ) )
		{
			return "PercentRank"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM.equalsIgnoreCase( agg ) )
		{
			return "PercentSum"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT.equalsIgnoreCase( agg ) )
		{
			return "RunningCount"; //$NON-NLS-1$
		}

		return null;
	}

	/**
	 * Check if chart is child of multi-views handle.
	 * 
	 * @param handle
	 * @return
	 * @since 2.3
	 */
	public static boolean isChildOfMultiViewsHandle( DesignElementHandle handle )
	{
		if ( handle != null
				&& handle.getContainer( ) instanceof MultiViewsHandle )
		{
			return true;
		}
		return false;
	}
}
