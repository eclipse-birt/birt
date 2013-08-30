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

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.ui.swt.ChartUIFactory;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IImageServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelper;
import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.core.resources.IProject;

/**
 * 
 */

public class ChartReportItemUIFactory extends ChartUIFactory
{
	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$
	
	private static ChartReportItemUIFactory instance = new ChartReportItemUIFactory( );

	protected ReportDataServiceProvider reportDataProvider = null;
	
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
			IUIServiceProvider uiProvider, IImageServiceProvider imageProvider,
			IDataServiceProvider dataProvider, IChartDataSheet dataSheet )
	{
		return new ChartWizardContext( cm,
				uiProvider,
				imageProvider,
				dataProvider,
				dataSheet,
				this );
	}

	public DteAdapter createDteAdapter( )
	{
		return new DteAdapter( );
	}

	/**
	 * Creates instance of <code>ChartReportItemBuilderImpl</code>.
	 * 
	 * @param taskId
	 * @return instance of <code>ChartReportItemBuilderImpl</code>.
	 */
	public ChartReportItemBuilderImpl createReportItemBuilder(  String taskId )
	{
		return new ChartReportItemBuilderImpl( taskId );
	}
	
	/**
	 * Creates instance of <code>IMenuBuilder</code>.
	 * 
	 * @return instance of <code>IMenuBuilder</code>.
	 */
	public IMenuBuilder createMenuBuilder( )
	{
		return new ChartMenuBuilder();
	}
	
	/**
	 * Updates chart page section helper according to context.
	 * 
	 * @param sectionHelper
	 * @return chart page section helper according to context.
	 */
	public ISectionHelper updateChartPageSectionHelper( ISectionHelper sectionHelper )
	{
		return sectionHelper;
	}

	public ReportDataServiceProvider createReportDataServiceProvider(
			ExtendedItemHandle extendedHandle )
	{
		return new ReportDataServiceProvider( extendedHandle );
	}
	/**
	 * Creates Figure used for edit part in layout editor.
	 * 
	 * @param crii
	 *            report item
	 * @return figure instance
	 * @since 3.7
	 */
	public DesignerRepresentation createFigure( ChartReportItemImpl crii )
	{
		return new DesignerRepresentation( crii );
	}	
	
	/**
	 * Create chart preference page
	 * @param context
	 * @param project
	 * @return ChartConfigurationBlock
	 */
	public ChartConfigurationBlock createChartConfigurationBlock(IStatusChangeListener context,
			IProject project){
		return new ChartConfigurationBlock( context,
				project );
	}

}
