/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.ui.integrate.SimpleUIServiceProviderImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ApplyButtonHandler;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.TaskFormatChart;
import org.eclipse.birt.chart.ui.swt.wizard.TaskSelectData;
import org.eclipse.birt.chart.ui.swt.wizard.TaskSelectType;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.core.ui.frameworks.taskwizard.TasksManager;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.utils.UIHelper;
import org.eclipse.swt.widgets.Display;

import com.ibm.icu.util.ULocale;

/**
 * A wizard launcher for Chart builder.
 * <p>
 * Set special locale to enable BiDi support, for example, append VM arguments
 * <b>-Duser.language=ar_AB</b>.
 * <p>
 * Also could specify file name in program arguments to open the expected chart.
 */
public class ChartWizardLauncher implements ChartUIConstants
{

	public void launch( String filePath )
	{
		init( );

		Chart chart = null;
		Serializer serializer = null;
		final File chartFile = new File( filePath );

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
		final ChartWizard chartWizard = new ChartWizard( );
		// Customized data provider and data sheet as below
		IDataServiceProvider dataProvider = new DefaultDataServiceProviderImpl( );
		// Create context
		final ChartWizardContext context = new ChartWizardContext( chart,
				new SimpleUIServiceProviderImpl( ),
				dataProvider,
				new SampleStandardDataSheet( dataProvider ) );

		// Use these methods to disable the UI you want.
		context.setEnabled( SUBTASK_TITLE, false );
		context.setEnabled( SUBTASK_LEGEND + BUTTON_LAYOUT, false );
		context.setEnabled( SUBTASK_SERIES_Y + BUTTON_LABEL, false );
		context.setEnabled( SUBTASK_SERIES_Y + BUTTON_CURVE, false );

		// Add predefined queries to select in data sheet
		context.addPredefinedQuery( QUERY_CATEGORY, new String[]{
				"row[\"abc\"]", "abc" //$NON-NLS-1$ //$NON-NLS-2$
		} );

		context.setRtL( ChartUtil.isRightToLeftLocale( ULocale.getDefault( ) ) );

		// This array is for storing the latest chart data before pressing
		// apply button
		final Object[] applyData = new Object[1];

		// Add Apply button
		chartWizard.addCustomButton( new ApplyButtonHandler( chartWizard ) {

			public void run( )
			{
				super.run( );
				// Save the data when applying
				applyData[0] = context.getModel( ).copyInstance( );
			}

		} );

		// Opens the wizard
		ChartWizardContext contextResult = (ChartWizardContext) chartWizard.open( context );

		if ( contextResult != null )
		{
			// Pressing Finish
			try
			{
				serializer.write( contextResult.getModel( ),
						new FileOutputStream( chartFile ) );
			}
			catch ( Exception e )
			{
				WizardBase.displayException( e );
			}
		}
		else if ( applyData[0] != null )
		{
			// Pressing Cancel but Apply was pressed before, so revert to
			// the point pressing Apply
			try
			{
				serializer.write( (Chart) applyData[0],
						new FileOutputStream( chartFile ) );
			}
			catch ( Exception e )
			{
				WizardBase.displayException( e );
			}
		}

	}

	void init( )
	{
		// Create display
		Display.getDefault( );

		// Set standalone mode rather than OSGI mode
		PlatformConfig config = new PlatformConfig( );
		config.setProperty( "STANDALONE", "true" ); //$NON-NLS-1$ //$NON-NLS-2$
		ChartEngine.instance( config );

		if ( !UIHelper.isEclipseMode( ) )
		{
			// Registers the wizard task and the chart wizard
			try
			{
				TasksManager.instance( )
						.registerTask( TaskSelectType.class.getName( ),
								new TaskSelectType( ) );
				TasksManager.instance( )
						.registerTask( TaskSelectData.class.getName( ),
								new TaskSelectData( ) );
				TasksManager.instance( )
						.registerTask( TaskFormatChart.class.getName( ),
								new TaskFormatChart( ) );
				String sChartTasks = TaskSelectType.class.getName( )
						+ "," + TaskSelectData.class.getName( ) + "," + TaskFormatChart.class.getName( ); //$NON-NLS-1$ //$NON-NLS-2$
				TasksManager.instance( )
						.registerWizard( ChartWizard.class.getName( ),
								sChartTasks,
								"" ); //$NON-NLS-1$
			}
			catch ( Exception e )
			{
				WizardBase.displayException( e );
			}
		}
	}

	public static void main( String[] args )
	{
		String filePath = args != null && args.length > 0 ? args[0]
				: "testChart.chart"; //$NON-NLS-1$
		new ChartWizardLauncher( ).launch( filePath );
	}
}
