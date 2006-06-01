/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.ui.swt.DefaultUIServiceProviderImpl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.TaskFormatChart;
import org.eclipse.birt.chart.ui.swt.wizard.TaskSelectData;
import org.eclipse.birt.chart.ui.swt.wizard.TaskSelectType;
import org.eclipse.birt.core.ui.frameworks.taskwizard.TasksManager;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.utils.UIHelper;
import org.eclipse.swt.widgets.Display;

/**
 * A wizard launcher for Chart builder.
 * <p>
 * All used icons should be moved into current root folder from
 * <b>org.eclipse.birt.chart.ui/icons</b>.
 * <p>
 * If the eclipse extension is expected to use, append the argument in VM,
 * <b>-DBIRT_HOME=birt_home_directory</b>; or append <b>-DSTANDALONE</b> to
 * use hard-coded manifest, in
 * <code>org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionImpl</code> and
 * <code>org.eclipse.birt.chart.util.PluginSettings</code>.
 * 
 */
public class ChartWizardLauncher
{

	public static void main( String[] args )
	{
		init( );

		Chart chart = null;
		Serializer serializer = null;
		File chartFile = new File( "testChart.chart" ); //$NON-NLS-1$

		// Reads the chart model
		try
		{
			serializer = SerializerImpl.instance( );
			if ( chartFile.exists( ) )
			{

				chart = serializer.read( new FileInputStream( chartFile ) );
			}
		}
		catch ( Exception e )
		{
			WizardBase.displayException( e );
		}

		// Configures the chart wizard.
		ChartWizard chartWizard = new ChartWizard( );
		ChartWizardContext context = new ChartWizardContext( chart );

		/*
		 * Used to fetch data. Default implementation of <code>IDataServiceProvider</code>.
		 * 
		 * @see the implementation for BIRT, <code>org.eclipse.birt.chart.reportitem.ReportDataServiceProvider</code>
		 * 
		 */
		context.setDataServiceProvider( new DefaultDataServiceProviderImpl( ) );
		/*
		 * Used to invoke some builders outside. Default implementation of
		 * <code>IUIServiceProvider</code>.
		 * 
		 * @see the implementation for BIRT, <code>org.eclipse.birt.chart.reportitem.ChartReportItemBuilderImpl</code>
		 * 
		 */
		context.setUIServiceProvider( new DefaultUIServiceProviderImpl( ) );

		// Opens the wizard
		context = (ChartWizardContext) chartWizard.open( context );

		// Writes the chart model
		if ( context != null )
		{
			chart = context.getModel( );
			try
			{
				serializer.write( chart, new FileOutputStream( chartFile ) );
			}
			catch ( Exception e )
			{
				WizardBase.displayException( e );
			}
		}
		else
		{
			System.out.println( "Wizard was cancelled!" ); //$NON-NLS-1$
		}
	}

	static void init( )
	{
		// Create display
		Display.getDefault( );

		if ( !UIHelper.isEclipseMode( ) )
		{
			// Registers the wizard task and the chart wizard
			try
			{
				TasksManager.instance( )
						.registerTask( "org.eclipse.birt.chart.ui.swt.wizard.TaskSelectType", //$NON-NLS-1$
								new TaskSelectType( ) );
				TasksManager.instance( )
						.registerTask( "org.eclipse.birt.chart.ui.swt.wizard.TaskSelectData", //$NON-NLS-1$
								new TaskSelectData( ) );
				TasksManager.instance( )
						.registerTask( "org.eclipse.birt.chart.ui.swt.wizard.TaskFormatChart", //$NON-NLS-1$
								new TaskFormatChart( ) );
				String sChartTasks = "org.eclipse.birt.chart.ui.swt.wizard.TaskSelectType,org.eclipse.birt.chart.ui.swt.wizard.TaskSelectData,org.eclipse.birt.chart.ui.swt.wizard.TaskFormatChart"; //$NON-NLS-1$
				TasksManager.instance( )
						.registerWizard( "org.eclipse.birt.chart.ui.ChartWizard", //$NON-NLS-1$
								sChartTasks,
								"" ); //$NON-NLS-1$
			}
			catch ( Exception e )
			{
				WizardBase.displayException( e );
			}
		}
	}
}
