/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.ui.integrate.SimpleUIServiceProviderImpl;
import org.eclipse.birt.chart.ui.swt.ChartUIFactory;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierHandler;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ApplyButtonHandler;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.TaskFormatChart;
import org.eclipse.birt.chart.ui.swt.wizard.TaskSelectData;
import org.eclipse.birt.chart.ui.swt.wizard.TaskSelectType;
import org.eclipse.birt.chart.ui.swt.wizard.preview.ChartLivePreviewThread;
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
public class ChartWizardLauncher implements ChartUIConstants {

	public void launch(String filePath) {
		try {
			// add radar type
			Class<?> claexten = Class.forName("org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionsImpl"); //$NON-NLS-1$
			Field saTypes = claexten.getDeclaredField("saTypes"); //$NON-NLS-1$
			saTypes.setAccessible(true);
			saTypes.set(null, new String[] { "org.eclipse.birt.chart.ui.swt.type.BarChart", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.type.LineChart", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.type.AreaChart", "org.eclipse.birt.chart.ui.swt.type.PieChart", //$NON-NLS-1$ //$NON-NLS-2$
					"org.eclipse.birt.chart.ui.swt.type.MeterChart", "org.eclipse.birt.chart.ui.swt.type.ScatterChart", //$NON-NLS-1$ //$NON-NLS-2$
					"org.eclipse.birt.chart.ui.swt.type.StockChart", "org.eclipse.birt.chart.ui.swt.type.GanttChart", //$NON-NLS-1$ //$NON-NLS-2$
					"org.eclipse.birt.chart.ui.swt.type.BubbleChart", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.type.DifferenceChart", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.type.TubeChart", "org.eclipse.birt.chart.ui.swt.type.ConeChart", //$NON-NLS-1$ //$NON-NLS-2$
					"org.eclipse.birt.chart.ui.swt.type.PyramidChart", //$NON-NLS-1$
					"org.eclipse.birt.chart.examples.radar.ui.type.RadarChart"//$NON-NLS-1$
			});
			// add ui provider
			Field saSeriesUI = claexten.getDeclaredField("saSeriesUI");//$NON-NLS-1$
			saSeriesUI.setAccessible(true);
			saSeriesUI.set(null, new String[] { "org.eclipse.birt.chart.ui.swt.series.SeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.AreaSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.BarSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.LineSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.MeterSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.PieSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.ScatterSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.StockSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.GanttSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.BubbleSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.ui.swt.series.DifferenceSeriesUIProvider", //$NON-NLS-1$
					"org.eclipse.birt.chart.examples.radar.ui.series.RadarSeriesUIProvider"//$NON-NLS-1$
			});

			// plugin settings
			Class<?> claps = Class.forName("org.eclipse.birt.chart.util.PluginSettings");//$NON-NLS-1$
			Field saDataSetProcessors = claps.getDeclaredField("saDataSetProcessors");//$NON-NLS-1$
			saDataSetProcessors.setAccessible(true);
			saDataSetProcessors.set(null,
					new String[] { "org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.StockDataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.BubbleDataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.GanttDataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DifferenceDataSetProcessorImpl", //$NON-NLS-1$
							"org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
					});

			Field saRenderers = claps.getDeclaredField("saRenderers");//$NON-NLS-1$
			saRenderers.setAccessible(true);
			saRenderers.set(null, new String[] { null, "org.eclipse.birt.chart.extension.render.Area", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Bar", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Dial", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Line", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Pie", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Stock", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Scatter", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Bubble", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Gantt", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.render.Difference", //$NON-NLS-1$
					"org.eclipse.birt.chart.examples.radar.render.Radar"//$NON-NLS-1$
			});

			Field saSeries = claps.getDeclaredField("saSeries");//$NON-NLS-1$
			saSeries.setAccessible(true);
			saSeries.set(null, new String[] { "org.eclipse.birt.chart.model.component.impl.SeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.BarSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.DialSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.LineSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.PieSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.StockSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl", //$NON-NLS-1$
					"org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl" //$NON-NLS-1$
			});
		} catch (ClassNotFoundException | SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		// Create display
		Display.getDefault();

		// Set standalone mode rather than OSGI mode
		PlatformConfig config = new PlatformConfig();
		config.setProperty("STANDALONE", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		ChartEngine.instance(config);
		final SampleStandardDataSheet ssd = new SampleStandardDataSheet();

		if (!UIHelper.isEclipseMode()) {
			// Registers the wizard task and the chart wizard
			try {
				TasksManager.instance().registerTask(TaskSelectType.class.getName(), new TaskSelectType());
				TasksManager.instance().registerTask(TaskSelectData.class.getName(), new TaskSelectData() {
					@Override
					public void doPreview() {
						super.doPreview();
						ssd.refreshSampleDataPreiview();
					}
				});
				TasksManager.instance().registerTask(TaskFormatChart.class.getName(), new TaskFormatChart());
				String sChartTasks = TaskSelectType.class.getName() + "," + TaskSelectData.class.getName() + "," //$NON-NLS-1$ //$NON-NLS-2$
						+ TaskFormatChart.class.getName();
				TasksManager.instance().registerWizard(ChartWizard.class.getName(), sChartTasks, ""); //$NON-NLS-1$
			} catch (Exception e) {
				WizardBase.displayException(e);
			}
		}

		Chart chart = null;
		Serializer serializer = null;
		final File chartFile = new File(filePath);

		// Reads the chart model
		InputStream is = null;
		try {
			serializer = SerializerImpl.instance();
			if (chartFile.exists()) {
				is = new FileInputStream(chartFile);
				chart = serializer.read(is);
			}
		} catch (Exception e) {
			// WizardBase.displayException( e );
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}

		// Configures the chart wizard.
		final ChartWizard chartWizard = new ChartWizard();
		// Customized data provider and data sheet as below
		IDataServiceProvider dataProvider = new DefaultDataServiceProviderImpl();

		// Create context
		final ChartWizardContext context = new ChartWizardContext(chart, new SimpleUIServiceProviderImpl(), null,
				dataProvider, ssd, new ChartUIFactory());
		((SimpleUIServiceProviderImpl) context.getUIServiceProvider())
				.setFormatSpecifierHandler(new FormatSpecifierHandler());
		ssd.setContext(context);

		// Use these methods to disable the UI you want.
		context.setEnabled(SUBTASK_TITLE, false);
		context.setEnabled(SUBTASK_LEGEND + BUTTON_LAYOUT, false);
		context.setEnabled(SUBTASK_SERIES_Y + BUTTON_LABEL, false);
		context.setEnabled(SUBTASK_SERIES_Y + BUTTON_CURVE, false);

		// Add predefined queries to select in data sheet
		context.addPredefinedQuery(QUERY_CATEGORY, new String[] { "row[\"abc\"]", "abc" //$NON-NLS-1$ //$NON-NLS-2$
		});
		context.addPredefinedQuery(QUERY_VALUE, new String[] {});

		context.setRtL(ChartUtil.isRightToLeftLocale(ULocale.getDefault()));

		// This array is for storing the latest chart data before pressing
		// apply button
		final Object[] applyData = new Object[1];

		// Add Apply button
		chartWizard.addCustomButton(new ApplyButtonHandler(chartWizard) {

			@Override
			public void run() {
				super.run();
				// Save the data when applying
				applyData[0] = context.getModel().copyInstance();
			}

		});

		ChartLivePreviewThread livePreviewThread = new ChartLivePreviewThread(dataProvider);
		livePreviewThread.start();
		context.setLivePreviewThread(livePreviewThread);

		// Opens the wizard
		ChartWizardContext contextResult = (ChartWizardContext) chartWizard.open(context);

		OutputStream os = null;
		try {

			if (contextResult != null) {
				os = new FileOutputStream(chartFile);
				// Pressing Finish
				serializer.write(contextResult.getModel(), os);

			} else if (applyData[0] != null) {
				os = new FileOutputStream(chartFile);
				// Pressing Cancel but Apply was pressed before, so revert to
				// the point pressing Apply
				serializer.write((Chart) applyData[0], os);
			}
		} catch (Exception e) {
			WizardBase.displayException(e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {

				}
			}
		}

	}

	public static void main(String[] args) {
		String filePath = args != null && args.length > 0 ? args[0] : "testChart.chart"; //$NON-NLS-1$
		new ChartWizardLauncher().launch(filePath);
	}
}
