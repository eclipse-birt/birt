/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartExpressionProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * ChartReportItemUIUtil
 * 
 * @since 2.5.3
 */

public class ChartReportItemUIUtil
{

	/**
	 * Creates chart filter factory instance according to specified item handle.
	 * 
	 * @param item
	 * @return
	 * @throws ExtendedElementException
	 */
	public static ChartFilterFactory createChartFilterFactory( Object item )
			throws ExtendedElementException
	{
		if ( item instanceof ExtendedItemHandle )
		{
			return getChartFilterFactory( ( (ExtendedItemHandle) item ).getReportItem( ) );
		}
		else if ( item instanceof IReportItem )
		{
			return createChartFilterFactory( (IReportItem) item);
		}
		return new ChartFilterFactory( );
	}
	
	private static ChartFilterFactory getChartFilterFactory( IReportItem adaptableObj )
	{
		ChartFilterFactory factory = ChartUtil.getAdapter(  adaptableObj, ChartFilterFactory.class );
		if ( factory != null )
		{
			return factory;
		}
		
		return new ChartFilterFactory( );
	}
	
	/**
	 * Returns the categories list in BIRT chart expression builder
	 * 
	 * @param builderCommand
	 * @return category style
	 */
	public static int getExpressionBuilderStyle( int builderCommand )
	{
		if ( builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS )
		{
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_CHART_DATAPOINTS )
		{
			return ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_SCRIPT_DATAPOINTS )
		{
			// Script doesn't support column binding expression.
			return ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_TRIGGERS_SIMPLE )
		{
			// Bugzilla#202386: Tooltips never support chart
			// variables. Use COMMAND_EXPRESSION_TRIGGERS_SIMPLE for un-dp
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS )
		{
			// Bugzilla#202386: Tooltips never support chart
			// variables. Use COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS for dp
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_CUBE_EXPRESSION_TOOLTIPS_DATAPOINTS )
		{
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_HYPERLINK )
		{
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_HYPERLINK_DATAPOINTS )
		{
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		}
		else if ( builderCommand == IUIServiceProvider.COMMAND_HYPERLINK_LEGEND )
		{
			// Add Legend item variables and remove column bindings
			return ChartExpressionProvider.CATEGORY_WITH_LEGEND_ITEMS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT
					| ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES;
		}
		return ChartExpressionProvider.CATEGORY_BASE;
	}}
