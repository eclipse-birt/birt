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

import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
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
}
