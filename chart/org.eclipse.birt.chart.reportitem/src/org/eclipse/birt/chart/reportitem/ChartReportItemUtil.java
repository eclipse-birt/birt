/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
import java.util.regex.Pattern;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeElementFactory;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil
{

	private static ICubeElementFactory cubeFactory = null;

	/**
	 * Specified the query expression of min aggregation binding
	 */
	public static final String QUERY_MIN = "chart__min"; //$NON-NLS-1$

	/**
	 * Specified the query expression of max aggregation binding
	 */
	public static final String QUERY_MAX = "chart__max"; //$NON-NLS-1$

	/**
	 * Specified property names defined in ExtendedItemHandle or IReportItem
	 */
	public static final String PROPERTY_XMLPRESENTATION = "xmlRepresentation"; //$NON-NLS-1$
	public static final String PROPERTY_CHART = "chart.instance"; //$NON-NLS-1$
	public static final String PROPERTY_SCALE = "chart.scale"; //$NON-NLS-1$
	public static final String PROPERTY_SCRIPT = "script"; //$NON-NLS-1$
	public static final String PROPERTY_ONRENDER = "onRender"; //$NON-NLS-1$
	public static final String PROPERTY_OUTPUT = "outputFormat"; //$NON-NLS-1$

	public synchronized static ICubeElementFactory getCubeElementFactory( )
			throws BirtException
	{
		if ( cubeFactory != null )
		{
			return cubeFactory;
		}

		try
		{
			Class cls = Class.forName( ICubeElementFactory.CUBE_ELEMENT_FACTORY_CLASS_NAME );
			cubeFactory = (ICubeElementFactory) cls.newInstance( );
		}
		catch ( Exception e )
		{
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.ERROR,
					e );
		}
		return cubeFactory;
	}

	/**
	 * Checks current chart is within cross tab.
	 * 
	 * @param chartHandle
	 *            the handle holding chart
	 * @return true means within cross tab, false means not
	 * @since 2.3
	 */
	public static boolean isChartInXTab( DesignElementHandle chartHandle )
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
						|| ( (ReportItemHandle) handle ).getDataSet( ) != null
						|| ( (ReportItemHandle) handle ).columnBindingsIterator( )
								.hasNext( ) )
				{
					return (ReportItemHandle) handle;
				}
			}
			ReportItemHandle result = getBindingHolder( handle.getContainer( ) );
			if ( result == null
					&& handle instanceof ReportItemHandle
					&& !( handle instanceof GridHandle ) )
			{
				result = (ReportItemHandle) handle;
			}
			return result;
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
	public static boolean canScaleShared( ExtendedItemHandle eih, Chart cm )
	{
		return cm instanceof ChartWithAxes
				&& eih.getDataSet( ) == null && getBindingHolder( eih ) != null
				&& isChartInXTab( eih );
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
		if ( itemHandle.getDataSet( ) != null )
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
			i = itemHandle.columnBindingsIterator( );
			while ( i.hasNext( ) )
			{
				list.add( i.next( ) );
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
				case GroupingUnitType.STRING_PREFIX:
					return IGroupDefinition.STRING_PREFIX_INTERVAL;
			}
			
			return IGroupDefinition.NO_INTERVAL;
		}

		return IGroupDefinition.NO_INTERVAL;
	}

	/**
	 * Convert interval range from Chart's to DtE's.
	 * 
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static double convertToDtEIntervalRange( DataType dataType,
			double intervalRange )
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
			// Currently, the interval range logic in chart is not uniform with
			// DtE, the difference is that 1 interval range means separating 2
			// elements in chart, but it means separating 1 elements in DtE, and
			// so all that interval ranges which are greater than or equal 1
			// should be increased 1 for DtE, we should add followed code to
			// convert interval range.
			if ( range >= 1 )
			{
				return (long) ( range + 1 );
			}

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

		if ( "Sum".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_SUM;

		}
		else if ( "Average".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE;

		}
		else if ( "Count".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT;

		}
		else if ( "DistinctCount".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT;

		}
		else if ( "First".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST;

		}
		else if ( "Last".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_LAST;

		}
		else if ( "Min".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MIN;

		}
		else if ( "Max".equals( agg ) ) { //$NON-NLS-1$
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MAX;
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
	 * @return
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

		if ( isBaseGroupingDefined( baseSD ))
		{
			return true;
		}
		
		return false;
	}
}
