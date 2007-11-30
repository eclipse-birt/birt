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

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil
{

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

	/**
	 * Checks current chart is within cross tab.
	 * 
	 * @param chartHandle
	 *            the handle holding chart
	 * @return true means within cross tab, false means not
	 */
	public static boolean isChartInXTab( DesignElementHandle chartHandle )
	{
		DesignElementHandle container = chartHandle.getContainer( );
		if ( container instanceof ExtendedItemHandle )
		{
			String exName = ( (ExtendedItemHandle) container ).getExtensionName( );
			if ( ICrosstabConstants.CROSSTAB_CELL_EXTENSION_NAME.equals( exName ) )
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
	 * Checks if shared binding is needed when computation
	 * 
	 * @param eih
	 *            handle
	 * @param cm
	 *            chart model
	 * @return shared binding needed or not
	 */
	public static boolean canBindingShared( ExtendedItemHandle eih, Chart cm )
	{
		// TODO enable shared binding later
		return false;
		// return cm instanceof ChartWithAxes
		// && eih.getDataSet( ) == null && getBindingHolder( eih ) != null;
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
}
