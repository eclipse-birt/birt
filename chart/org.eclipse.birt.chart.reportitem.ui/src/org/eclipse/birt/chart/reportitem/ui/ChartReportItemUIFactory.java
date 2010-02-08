/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * 
 */

public class ChartReportItemUIFactory
{

	private static ChartReportItemUIFactory instance = new ChartReportItemUIFactory( );

	protected ChartReportItemUIFactory( )
	{

	}

	public static ChartReportItemUIFactory instance( )
	{
		return instance;
	}

	public static void initInstance( ChartReportItemUIFactory newInstance )
	{
		instance = newInstance;
	}

	public IChartDataSheet createDataSheet( ExtendedItemHandle handle,
			ReportDataServiceProvider dataProvider )
	{
		return new StandardChartDataSheet( handle, dataProvider );
	}

	public ChartWizardContext createWizardContext( Chart cm,
			IUIServiceProvider uiProvider, IDataServiceProvider dataProvider,
			IChartDataSheet dataSheet )
	{
		return new ChartWizardContext( cm, uiProvider, dataProvider, dataSheet );
	}
	
	public DteAdapter createDteAdapter( )  {
		return new DteAdapter();
	}
}
