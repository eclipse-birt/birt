/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * ChartReportItemHelper
 */

public class ChartReportItemHelper
{
	private static ChartReportItemHelper instance = new ChartReportItemHelper( );

	protected ChartReportItemHelper( )
	{

	}

	public static void initInstance( ChartReportItemHelper newInstance )
	{
		instance = newInstance;
	}

	public static ChartReportItemHelper instance( )
	{
		return instance;
	}
	
	public CubeHandle getBindingCubeHandle( ReportItemHandle itemHandle )
	{
		return ChartCubeUtil.getBindingCube( itemHandle );
	}
	
	public DataSetHandle getBindingDataSetHandle(ReportItemHandle itemHandle )
	{
		return ChartCubeUtil.getBindingDataSet( itemHandle );
	}
	
	public boolean checkCubeBindings( ExtendedItemHandle handle, Iterator<ComputedColumnHandle> columnBindings )
	{
		return ChartCubeUtil.checkColumnbindingForCube( columnBindings );
	}
	
	public ChartExpressionUtil.ExpressionCodec createExpressionCodec(
			ExtendedItemHandle handle )
	{
		return ChartModelHelper.instance( ).createExpressionCodec( );
	}
	
	public boolean loadExpression( ExpressionCodec exprCodec,
			ComputedColumnHandle cch )
	{
		return ChartItemUtil.loadExpression( exprCodec, cch );
	}
	
	public ComputedColumnHandle findDimensionBinding(
			ExpressionCodec exprCodec, String dimName, String levelName,
			Collection<ComputedColumnHandle> bindings, ReportItemHandle itemHandle )
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
	
	public Iterator<?> getAllUsedBindings( Chart cm, ReportItemHandle handle )
	{
		return handle.columnBindingsIterator( );
	}

	public String getMeasureExprIndicator( CubeHandle cubeHandle )
	{
		return ExpressionUtil.MEASURE_INDICATOR;
	}
}
