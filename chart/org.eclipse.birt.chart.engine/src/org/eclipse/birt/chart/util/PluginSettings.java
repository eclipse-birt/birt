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

package org.eclipse.birt.chart.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.datafeed.IDataPointDefinition;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.internal.log.JavaUtilLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.IExtChartModelLoader;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.render.ISeriesRenderer;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;

import com.ibm.icu.util.ULocale;

/**
 * Provides a framework for locating extension implementations either via the
 * BIRT plugin.xml paradigm or via extensions that are explicitly specified in
 * this singleton instance.
 *
 * If the BIRT extension paradigm is to be used, ensure that the BIRT_HOME 'JVM
 * system variable' points to a valid folder containing all chart extensions
 * within a plugins/ subfolder.
 *
 * If the BIRT extension paradigm is not to be used and the default classloader
 * is requested for loading extensions, ensure that the BIRT_HOME 'JVM system
 * variable' is undefined.
 */
public final class PluginSettings {
	private static final String PLUGIN = "org.eclipse.birt.chart.engine"; //$NON-NLS-1$

	public static final String PROP_STANDALONE = "STANDALONE"; //$NON-NLS-1$

	public static final String PROP_LOGGING_LEVEL = "LOGGING_LEVEL"; //$NON-NLS-1$

	public static final String PROP_LOGGING_DIR = "LOGGING_DIR"; //$NON-NLS-1$

	/**
	 * All available series types for which extensions are defined. Note that this
	 * list is index sensitive.
	 */
	private static String[] saSeries = { "org.eclipse.birt.chart.model.component.impl.SeriesImpl", //$NON-NLS-1$
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
	};

	/**
	 * All data set processor implementing class names for which extensions are
	 * defined. Note that this list is index sensitive and corresponds to the series
	 * type list.
	 */
	private static String[] saDataSetProcessors = { "org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
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
	};

	/**
	 * All series renderer implementing class names for which extensions are
	 * defined. Note that this list is index sensitive and corresponds to the series
	 * type list.
	 */
	private static String[] saRenderers = { null, "org.eclipse.birt.chart.extension.render.Area", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Bar", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Dial", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Line", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Pie", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Stock", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Scatter", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Bubble", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Gantt", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.render.Difference", //$NON-NLS-1$
	};

	/**
	 * All available device renderers provided in the out-of-the-box distribution
	 */
	private static String[][] saDevices = { { "dv.SWING", //$NON-NLS-1$
			"org.eclipse.birt.chart.device.swing.SwingRendererImpl", //$NON-NLS-1$
			null, null },
			{ "dv.SWT", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.swt.SwtRendererImpl", //$NON-NLS-1$
					null, null },
			{ "dv.PNG24", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.PngRendererImpl", //$NON-NLS-1$
					null, "Deprecated, use PNG instead" //$NON-NLS-1$
			}, { "dv.GIF8", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.GifRendererImpl", //$NON-NLS-1$
					null, "Deprecated, use PNG instead" //$NON-NLS-1$
			}, { "dv.PNG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.PngRendererImpl", //$NON-NLS-1$
					"PNG", //$NON-NLS-1$
					"Portable Network Graphics" //$NON-NLS-1$
			}, { "dv.GIF", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.GifRendererImpl", //$NON-NLS-1$
					null, "Deprecated, use PNG instead" //$NON-NLS-1$
			}, { "dv.JPEG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.JpegRendererImpl", //$NON-NLS-1$
					null, null },
			{ "dv.JPG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.JpegRendererImpl", //$NON-NLS-1$
					"JPG", //$NON-NLS-1$
					null },
			{ "dv.BMP", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.BmpRendererImpl", //$NON-NLS-1$
					"BMP", //$NON-NLS-1$
					null },
			{ "dv.SVG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.svg.SVGRendererImpl", //$NON-NLS-1$
					"SVG", //$NON-NLS-1$
					null },
			{ "dv.PDF", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.pdf.PDFRendererImpl", //$NON-NLS-1$
					"PDF", //$NON-NLS-1$
					null } };

	/**
	 * All available display servers provided in the out-of-the-box distribution
	 */
	private static String[][] saDisplayServers = {
			{ "ds.SWING", "org.eclipse.birt.chart.device.swing.SwingDisplayServer" //$NON-NLS-1$ //$NON-NLS-2$
			}, { "ds.SWT", "org.eclipse.birt.chart.device.swt.SwtDisplayServer" //$NON-NLS-1$ //$NON-NLS-2$
			}, { "ds.SVG", "org.eclipse.birt.chart.device.svg.SVGDisplayServer" //$NON-NLS-1$ //$NON-NLS-2$
			}, { "ds.PDF", "org.eclipse.birt.chart.device.pdf.PDFDisplayServer" //$NON-NLS-1$ //$NON-NLS-2$
			} };

	/**
	 * All available default aggregation names.
	 *
	 * @since 2.3
	 *
	 */
	public static final class DefaultAggregations {
		public static final String SUM = "Sum"; //$NON-NLS-1$
		public static final String AVERAGE = "Average"; //$NON-NLS-1$
		public static final String COUNT = "Count"; //$NON-NLS-1$
		public static final String DISTINCT_COUNT = "DistinctCount"; //$NON-NLS-1$
		public static final String FIRST = "First"; //$NON-NLS-1$
		public static final String LAST = "Last"; //$NON-NLS-1$
		public static final String MIN = "Min"; //$NON-NLS-1$
		public static final String MAX = "Max"; //$NON-NLS-1$
		public static final String WEIGHTED_AVERAGE = "WeightedAverage"; //$NON-NLS-1$
		public static final String MEDIAN = "Median"; //$NON-NLS-1$
		public static final String MODE = "Mode"; //$NON-NLS-1$
		public static final String STDDEV = "STDDEV"; //$NON-NLS-1$
		public static final String VARIANCE = "Variance"; //$NON-NLS-1$
		public static final String IRR = "Irr"; //$NON-NLS-1$
		public static final String MIRR = "Mirr"; //$NON-NLS-1$
		public static final String NPV = "NPV"; //$NON-NLS-1$
		public static final String PERCENTILE = "Percentile"; //$NON-NLS-1$
		public static final String QUARTILE = "Quartile"; //$NON-NLS-1$
		public static final String MOVING_AVERAGE = "MovingAverage"; //$NON-NLS-1$
		public static final String RUNNING_SUM = "RunningSum"; //$NON-NLS-1$
		public static final String RUNNING_NPV = "RunningNPV"; //$NON-NLS-1$
		public static final String RANK = "Rank"; //$NON-NLS-1$
		public static final String TOP = "Top"; //$NON-NLS-1$
		public static final String TOP_PERCENT = "TopPercent"; //$NON-NLS-1$
		public static final String BOTTOM = "Bottom"; //$NON-NLS-1$
		public static final String BOTTOM_PERCENT = "BottomPercent"; //$NON-NLS-1$
		public static final String PERCENT_RANK = "PercentRank"; //$NON-NLS-1$
		public static final String PERCENT_SUM = "PercentSum"; //$NON-NLS-1$
		public static final String RUNNING_COUNT = "RunningCount"; //$NON-NLS-1$
		public static final String RANGE = "Range"; //$NON-NLS-1$
	}

	/**
	 * All available base aggregate functions used in orthogonal value aggregation.
	 */
	private static String[][] saBaseAggregateFunctions = {
			{ DefaultAggregations.SUM, "Sum", "org.eclipse.birt.chart.extension.aggregate.Sum" //$NON-NLS-1$ //$NON-NLS-2$
			}, { DefaultAggregations.AVERAGE, "Average", "org.eclipse.birt.chart.extension.aggregate.Average" //$NON-NLS-1$ //$NON-NLS-2$
			}, { DefaultAggregations.COUNT, "Count", "org.eclipse.birt.chart.extension.aggregate.Count" //$NON-NLS-1$ //$NON-NLS-2$
			}, { DefaultAggregations.DISTINCT_COUNT, "DistinctCount", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.aggregate.DistinctCount" //$NON-NLS-1$
			}, { DefaultAggregations.FIRST, "First", "org.eclipse.birt.chart.extension.aggregate.First" //$NON-NLS-1$ //$NON-NLS-2$
			}, { DefaultAggregations.LAST, "Last", "org.eclipse.birt.chart.extension.aggregate.Last" //$NON-NLS-1$ //$NON-NLS-2$
			}, { DefaultAggregations.MIN, "Min", "org.eclipse.birt.chart.extension.aggregate.Min" //$NON-NLS-1$ //$NON-NLS-2$
			}, { DefaultAggregations.MAX, "Max", "org.eclipse.birt.chart.extension.aggregate.Max" //$NON-NLS-1$ //$NON-NLS-2$
			}, { DefaultAggregations.RANGE, "Range", //$NON-NLS-1$
					"org.eclipse.birt.chart.extension.aggregate.Range" //$NON-NLS-1$
			} };

	/**
	 * All series datapoint definitions implementing class names for which
	 * extensions are defined. Note that this list is index sensitive and
	 * corresponds to the series type list.
	 */
	private static String[] saDataPointDefinitions = { null, null, null, null, null, null,
			"org.eclipse.birt.chart.extension.datafeed.StockDataPointDefinition", //$NON-NLS-1$
			null, "org.eclipse.birt.chart.extension.datafeed.BubbleDataPointDefinition", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.datafeed.GanttDataPointDefinition", //$NON-NLS-1$
			"org.eclipse.birt.chart.extension.datafeed.DifferenceDataPointDefinition", //$NON-NLS-1$
	};

	/**
	 * A singleton instance created lazily when requested for
	 */
	private static PluginSettings ps = null;

	/**
	 * A boolean indicating if extensions are to be retrieved directly and by
	 * bypassing the extension loading framework.
	 */
	private boolean bStandalone = false;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/util"); //$NON-NLS-1$

	/**
	 * A non-instantiable constructor
	 */
	private PluginSettings() {
	}

	/**
	 * Returns a singleton instance of the plugin settings framework which uses the
	 * default configuration.
	 *
	 * @return A singleton instance of the plugin settings framework
	 */
	public static PluginSettings instance() {
		return instance(null);
	}

	/**
	 * Returns a singleton instance of the plugin settings framework with specific
	 * configuration.
	 *
	 * Initializes the OSGi Platform framework to load chart extension bundles
	 * unless the STANDALONE flag was set in PlatformConfig property.
	 *
	 * @return A singleton instance of the plugin settings framework
	 */
	public static synchronized PluginSettings instance(PlatformConfig config) {
		if (ps == null) {
			ps = new PluginSettings();

			String loggingDir = null;
			Level loggingLevel = null;

			if (config != null) {
				ps.bStandalone = config.getProperty(PROP_STANDALONE) != null;
				loggingDir = (String) config.getProperty(PROP_LOGGING_DIR);
				loggingLevel = (Level) config.getProperty(PROP_LOGGING_LEVEL);
			}

			if (!ps.bStandalone) {
				try {
					Platform.startup(config);
				} catch (BirtException e) {
					logger.log(e);
				}
			}

			ps.initFileLogger(loggingDir, loggingLevel);
		}
		return ps;
	}

	private void initFileLogger(String loggingDir, Level loggingLevel) {
		if (loggingLevel != null && loggingLevel == Level.OFF) {
			return;
		}

		// initialize the file logger
		try {
			String dir = null;
			if (loggingDir != null) {
				dir = loggingDir;
			}

			JavaUtilLoggerImpl.initFileHandler(dir, loggingLevel);
		} catch (SecurityException | IOException e) {
			logger.log(e);
		}

	}

	/**
	 * Retrieves the first instance of a data set processor registered as an
	 * extension for a given series type.
	 *
	 * @param cSeries The Class instance associated with the given series type
	 *
	 * @return A newly created instance of a registered data set processor extension
	 *
	 * @throws ChartException
	 */
	public IDataSetProcessor getDataSetProcessor(Class<?> cSeries) throws ChartException {
		final String sFQClassName = cSeries.getName();
		if (inEclipseEnv()) {
			final Object oDSP = getPluginXmlObject("datasetprocessors", "datasetProcessor", "series", "processor", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sFQClassName);

			if (oDSP != null) {
				logger.log(ILogger.INFORMATION, Messages.getString("info.eclenv.creating.dsp", //$NON-NLS-1$
						new Object[] { oDSP.getClass().getName() }, ULocale.getDefault() // LOCALE?
				));
				return (IDataSetProcessor) oDSP;
			}
			logger.log(ILogger.FATAL, Messages.getString("error.eclenv.cannot.find.dsp", //$NON-NLS-1$
					new Object[] { sFQClassName }, ULocale.getDefault() // LOCALE?
			));
		} else {
			for (int i = 0; i < saSeries.length; i++) {
				if (sFQClassName.equals(saSeries[i])) {
					logger.log(ILogger.INFORMATION, Messages.getString("info.stdenv.creating.dsp", //$NON-NLS-1$
							new Object[] { saDataSetProcessors[i] }, ULocale.getDefault() // LOCALE?
					));
					return (IDataSetProcessor) newInstance(saDataSetProcessors[i]);
				}
			}
			logger.log(ILogger.FATAL, Messages.getString("error.stdenv.cannot.find.dsp", //$NON-NLS-1$
					new Object[] { sFQClassName }, ULocale.getDefault() // LOCALE?
			));
		}
		return null;
	}

	private Map<String, Object> chartModelPackagesMap = null;

	/**
	 * Returns extra chart model packages.
	 *
	 * @return map of chart extension package definition.
	 * @throws ChartException
	 * @since 2.6
	 */
	public Map<String, Object> getExtChartModelPackages() throws ChartException {
		if (chartModelPackagesMap != null) {
			return chartModelPackagesMap;
		}

		chartModelPackagesMap = new LinkedHashMap<>();

		if (!inEclipseEnv()) {
			return chartModelPackagesMap;
		}

		String sXsdListName = "charttypes"; //$NON-NLS-1$
		String sXsdComplexName = "chartType"; //$NON-NLS-1$
		String sXsdElementName = "namespaceURI"; //$NON-NLS-1$
		String sXsdElementValue = "modelLoader"; //$NON-NLS-1$
		final IExtensionRegistry ier = Platform.getExtensionRegistry();
		final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
		if (iep == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN, "exception.cannot.find.plugin.entry", //$NON-NLS-1$
					new Object[] { "", sXsdElementName, sXsdElementValue //$NON-NLS-1$
					}, Messages.getResourceBundle());
		}
		final IExtension[] iea = iep.getExtensions();
		IConfigurationElement[] icea;

		for (int i = 0; i < iea.length; i++) {
			icea = iea[i].getConfigurationElements();
			for (int j = 0; j < icea.length; j++) {
				if (icea[j].getName().equals(sXsdComplexName)) {
					try {
						chartModelPackagesMap.put(icea[j].getAttribute(sXsdElementName),
								((IExtChartModelLoader) icea[j].createExecutableExtension(sXsdElementValue))
										.getChartTypePackage());
					} catch (FrameworkException cex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN, cex);
					}
				}
			}
		}

		return chartModelPackagesMap;
	}

	/**
	 * Retrieves the first instance of a series renderer registered as an extension
	 * for a given series type.
	 *
	 * @param cSeries The Class instance associated with the given series type
	 *
	 * @return A newly created (and initialized) instance of a registered series
	 *         renderer
	 *
	 * @throws ChartException
	 */
	public BaseRenderer getRenderer(Class<?> cSeries) throws ChartException {
		final String sFQClassName = cSeries.getName();
		if (inEclipseEnv()) {
			final Object oSeriesRenderer = getPluginXmlObject("modelrenderers", "modelRenderer", "series", "renderer", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sFQClassName);
			if (oSeriesRenderer != null) {
				logger.log(ILogger.INFORMATION, Messages.getString("info.eclenv.creating.series.renderer", //$NON-NLS-1$
						new Object[] { oSeriesRenderer.getClass().getName() }, ULocale.getDefault() // LOCALE?
				));
				return (BaseRenderer) oSeriesRenderer;
			}
			logger.log(ILogger.ERROR, Messages.getString("error.eclenv.cannot.find.series.renderer", //$NON-NLS-1$
					new Object[] { sFQClassName }, ULocale.getDefault() // LOCALE?
			));
		} else {
			for (int i = 0; i < saSeries.length; i++) {
				if (sFQClassName.equals(saSeries[i])) {
					if (saRenderers[i] == null) {
						break;
					}
					logger.log(ILogger.INFORMATION, Messages.getString("info.stdenv.creating.series.renderer", //$NON-NLS-1$
							new Object[] { saRenderers[i] }, ULocale.getDefault() // LOCALE?
					));
					return (BaseRenderer) newInstance(saRenderers[i]);
				}
			}
			logger.log(ILogger.ERROR, Messages.getString("error.stdenv.cannot.find.series.renderer", //$NON-NLS-1$
					new Object[] { sFQClassName }, ULocale.getDefault() // LOCALE?
			));
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a device renderer registered as an extension
	 * for a given name
	 *
	 * @param sName The name of the device renderer. Values registered in the
	 *              default distribution are dv.SWT, dv.SWING, dv.PNG, dv.JPEG,
	 *              dv.BMP
	 *
	 * @return An newly initialized instance of the requested device renderer
	 *
	 * @throws ChartException
	 */
	public IDeviceRenderer getDevice(String sName) throws ChartException {
		if (inEclipseEnv()) {
			final Object oDeviceRenderer = getPluginXmlObject("devicerenderers", "deviceRenderer", "name", "device", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sName);
			if (oDeviceRenderer != null) {
				logger.log(ILogger.INFORMATION, Messages.getString("info.eclenv.creating.device", //$NON-NLS-1$
						new Object[] { sName, oDeviceRenderer.getClass().getName() }, ULocale.getDefault() // LOCALE?
				));

				final String sFormat = getPluginXmlAttribute("devicerenderers", "deviceRenderer", "name", "format", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						sName);
				if (sFormat != null && sFormat.length() > 0) {
					((IDeviceRenderer) oDeviceRenderer).setProperty(IDeviceRenderer.FORMAT_IDENTIFIER, sFormat);
				}
				return (IDeviceRenderer) oDeviceRenderer;
			}
			logger.log(ILogger.FATAL, Messages.getString("error.eclenv.cannot.find.device", //$NON-NLS-1$
					new Object[] { sName }, ULocale.getDefault() // LOCALE?
			));
		} else {
			for (int i = 0; i < saDevices.length; i++) {
				if (saDevices[i][0].equalsIgnoreCase(sName)) {
					logger.log(ILogger.INFORMATION, Messages.getString("info.stdenv.creating.device", //$NON-NLS-1$
							new Object[] { sName, saDevices[i][1] }, ULocale.getDefault() // LOCALE?
					));
					IDeviceRenderer idr = (IDeviceRenderer) newInstance(saDevices[i][1]);
					if (saDevices[i][2] != null && saDevices[i][2].length() > 0) {
						idr.setProperty(IDeviceRenderer.FORMAT_IDENTIFIER, saDevices[i][2]);
					}
					return idr;
				}
			}
			logger.log(ILogger.FATAL, Messages.getString("error.stdenv.cannot.find.device", //$NON-NLS-1$
					new Object[] { sName }, ULocale.getDefault() // LOCALE?
			));
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a display server registered as an extension
	 * for a given name
	 *
	 * @param sName The name of the display server. Values registered in the default
	 *              distribution are ds.SWT, ds.SWING
	 *
	 * @return An newly initialized instance of the requested display server
	 *
	 * @throws ChartException
	 */
	public IDisplayServer getDisplayServer(String sName) throws ChartException {
		if (inEclipseEnv()) {
			final Object oDisplayServer = getPluginXmlObject("displayservers", "displayserver", "name", "server", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sName);
			if (oDisplayServer != null) {
				logger.log(ILogger.INFORMATION, Messages.getString("info.eclenv.creating.display", //$NON-NLS-1$
						new Object[] { sName, oDisplayServer.getClass().getName() }, ULocale.getDefault() // LOCALE?
				));
				return (IDisplayServer) oDisplayServer;
			}
			logger.log(ILogger.FATAL, Messages.getString("error.eclenv.cannot.find.display", //$NON-NLS-1$
					new Object[] { sName }, ULocale.getDefault() // LOCALE?
			));
		} else {
			for (int i = 0; i < saDisplayServers.length; i++) {
				if (saDisplayServers[i][0].equalsIgnoreCase(sName)) {
					logger.log(ILogger.INFORMATION, Messages.getString("info.stdenv.creating.display", //$NON-NLS-1$
							new Object[] { sName, saDisplayServers[i][1] }, ULocale.getDefault() // LOCALE?
					));
					return (IDisplayServer) newInstance(saDisplayServers[i][1]);
				}
			}
			logger.log(ILogger.FATAL, Messages.getString("error.stdenv.cannot.find.display", //$NON-NLS-1$
					new Object[] { sName }, ULocale.getDefault() // LOCALE?
			));
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a device renderer registered as an extension
	 * for a given name
	 *
	 * @param sName The name of the aggregate function.
	 *
	 * @return An newly initialized instance of the requested aggregate function.
	 *
	 * @throws ChartException
	 */
	public IAggregateFunction getAggregateFunction(String sName) throws ChartException {
		if (inEclipseEnv()) {
			final Object oAggregateFunction = getPluginXmlObject("aggregatefunctions", "aggregateFunction", "name", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"function", //$NON-NLS-1$
					sName);
			if (oAggregateFunction != null) {
				logger.log(ILogger.INFORMATION, Messages.getString("info.eclenv.creating.function", //$NON-NLS-1$
						new Object[] { sName, oAggregateFunction.getClass().getName() }, ULocale.getDefault() // LOCALE?
				));
				return (IAggregateFunction) oAggregateFunction;
			}
			logger.log(ILogger.FATAL, Messages.getString("error.eclenv.cannot.find.function", //$NON-NLS-1$
					new Object[] { sName }, ULocale.getDefault() // LOCALE?
			));
		} else {
			for (int i = 0; i < saBaseAggregateFunctions.length; i++) {
				if (saBaseAggregateFunctions[i][0].equalsIgnoreCase(sName)) {
					logger.log(ILogger.INFORMATION, Messages.getString("info.stdenv.creating.function", //$NON-NLS-1$
							new Object[] { sName, saBaseAggregateFunctions[i][1] }, ULocale.getDefault() // LOCALE?
					));
					return (IAggregateFunction) newInstance(saBaseAggregateFunctions[i][2]);
				}
			}
			logger.log(ILogger.FATAL, Messages.getString("error.stdenv.cannot.find.function", //$NON-NLS-1$
					new Object[] { sName }, ULocale.getDefault() // LOCALE?
			));
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a series renderer registered as an extension
	 * for a given series type.
	 *
	 * @param cSeries The Class instance associated with the given series type
	 *
	 * @return A newly created (and initialized) instance of a registered series
	 *         renderer
	 *
	 * @throws ChartException
	 */
	public IDataPointDefinition getDataPointDefinition(Class<?> cSeries) throws ChartException {
		final String sFQClassName = cSeries.getName();
		if (inEclipseEnv()) {
			final Object oDefinition = getPluginXmlObject("datapointdefinitions", "datapointDefinition", "series", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"definition", //$NON-NLS-1$
					sFQClassName);
			if (oDefinition != null) {
				logger.log(ILogger.INFORMATION, Messages.getString("info.eclenv.creating.datapoint.definition", //$NON-NLS-1$
						new Object[] { oDefinition.getClass().getName() }, ULocale.getDefault() // LOCALE?
				));
				return (IDataPointDefinition) oDefinition;
			}
			logger.log(ILogger.INFORMATION, Messages.getString("error.stdenv.cannot.find.datapoint.definition", //$NON-NLS-1$
					new Object[] { sFQClassName }, ULocale.getDefault() // LOCALE?
			));
		} else {
			for (int i = 0; i < saDataPointDefinitions.length; i++) {
				if (sFQClassName.equals(saSeries[i])) {
					if (saDataPointDefinitions[i] == null) {
						break;
					}
					logger.log(ILogger.INFORMATION, Messages.getString("info.eclenv.creating.datapoint.definition", //$NON-NLS-1$
							new Object[] { saRenderers[i] }, ULocale.getDefault() // LOCALE?
					));
					return (IDataPointDefinition) newInstance(saDataPointDefinitions[i]);
				}
			}
			logger.log(ILogger.INFORMATION, Messages.getString("error.stdenv.cannot.find.datapoint.definition", //$NON-NLS-1$
					new Object[] { sFQClassName }, ULocale.getDefault() // LOCALE?
			));
		}
		return null;
	}

	/**
	 * Returns the localized display name of given Series Class.
	 *
	 * @param seriesClassName
	 * @return display name of series.
	 */
	public String getSeriesDisplayName(final String seriesClassName) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			@Override
			public String run() {
				String sDisplayName = seriesClassName;
				try {
					Class<?> seriesClass = Class.forName(seriesClassName);
					Method createMethod = seriesClass.getDeclaredMethod("create", new Class[] {}); //$NON-NLS-1$
					Series newSeries = (Series) createMethod.invoke(seriesClass, new Object[] {});
					Method mDisplayName = seriesClass.getDeclaredMethod("getDisplayName", new Class[] {}); //$NON-NLS-1$
					Object oName = mDisplayName.invoke(newSeries, new Object[] {});
					sDisplayName = (String) oName;
				} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				return sDisplayName;
			}
		});
	}

	/**
	 * Returns a list of all series registered via extension point implementations
	 * (or simulated)
	 *
	 * @return A list of series registered via extension point implementations (or
	 *         simulated)
	 */
	public String[] getRegisteredSeries() throws ChartException {
		if (inEclipseEnv()) {
			String[][] sers = getPluginXmlStrings("modelrenderers", //$NON-NLS-1$
					"modelRenderer", //$NON-NLS-1$
					"series", //$NON-NLS-1$
					"renderer"); //$NON-NLS-1$

			final String[] saSeries = new String[sers.length];
			for (int i = 0; i < saSeries.length; i++) {
				saSeries[i] = sers[i][0];
			}
			return saSeries;
		}
		return saSeries;
	}

	/**
	 * Returns a list of registered device renderer output formats and display
	 * names.
	 *
	 * @return all registered output formats and display names
	 * @throws ChartException
	 */
	public String[][] getRegisteredOutputFormats() throws ChartException {
		if (inEclipseEnv()) {
			String[][] formats = getPluginXmlStrings("devicerenderers", //$NON-NLS-1$
					"deviceRenderer", //$NON-NLS-1$
					"format", //$NON-NLS-1$
					"displayName"); //$NON-NLS-1$

			ArrayList<String[]> al = new ArrayList<>();
			for (int i = 0; i < formats.length; i++) {
				if (formats[i][0] != null && formats[i][0].length() > 0) {
					al.add(formats[i]);
				}
			}
			return al.toArray(new String[0][0]);
		} else {
			ArrayList<String[]> al = new ArrayList<>();
			for (int i = 0; i < saDevices.length; i++) {
				if (saDevices[i][2] != null && saDevices[i][2].length() > 0) {
					al.add(new String[] { saDevices[i][2], saDevices[i][3] });
				}
			}
			return al.toArray(new String[0][0]);
		}
	}

	/**
	 * Returns a list of all aggregate function names registered via extension point
	 * implementations (or simulated)
	 *
	 * @return A list of all aggregate function names registered via extension point
	 *         implementations (or simulated)
	 */
	public String[] getRegisteredAggregateFunctions() throws ChartException {
		if (inEclipseEnv()) {
			String[][] aggs = getPluginXmlStrings("aggregatefunctions", //$NON-NLS-1$
					"aggregateFunction", //$NON-NLS-1$
					"name", //$NON-NLS-1$
					"function"); //$NON-NLS-1$

			final String[] saFunctions = new String[aggs.length];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = aggs[i][0];
			}
			return saFunctions;
		} else {
			final String[] saFunctions = new String[saBaseAggregateFunctions.length];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = saBaseAggregateFunctions[i][0];
			}
			return saFunctions;
		}
	}

	/**
	 * Returns Summary or Running aggregates name.
	 *
	 * @param aggregateType
	 * @return supported function names of specified aggregate type.
	 * @throws ChartException
	 * @since BIRT 2.3
	 */
	public String[] getRegisteredAggregateFunctions(int aggregateType) throws ChartException {
		if (inEclipseEnv()) {
			String[][] aggs = getPluginXmlStrings("aggregatefunctions", //$NON-NLS-1$
					"aggregateFunction", //$NON-NLS-1$
					"name", //$NON-NLS-1$
					"function"); //$NON-NLS-1$

			List<String> funcList = new ArrayList<>();
			for (int i = 0; i < aggs.length; i++) {
				IAggregateFunction aFunc = getAggregateFunction(aggs[i][0]);
				if (aFunc.getType() == aggregateType) {
					funcList.add(aggs[i][0]);
				}
			}

			final String[] saFunctions = new String[funcList.size()];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = funcList.get(i);
			}

			return saFunctions;
		} else {
			final String[] saFunctions = new String[saBaseAggregateFunctions.length];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = saBaseAggregateFunctions[i][0];
			}
			return saFunctions;
		}
	}

	/**
	 * Returns a list of all aggregate function display names registered via
	 * extension point implementations (or simulated)
	 *
	 * @return A list of all aggregate function display names registered via
	 *         extension point implementations (or simulated)
	 */
	public String[] getRegisteredAggregateFunctionDisplayNames() throws ChartException {
		if (inEclipseEnv()) {
			String[][] aggs = getPluginXmlStrings("aggregatefunctions", //$NON-NLS-1$
					"aggregateFunction", //$NON-NLS-1$
					"name", //$NON-NLS-1$
					"displayName"); //$NON-NLS-1$

			final String[] saFunctions = new String[aggs.length];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = aggs[i][1];
			}
			return saFunctions;
		} else {
			final String[] saFunctions = new String[saBaseAggregateFunctions.length];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = saBaseAggregateFunctions[i][1];
			}
			return saFunctions;
		}
	}

	/**
	 * Returns Summary or Running aggregates display name.
	 *
	 * @param aggregateType
	 * @return display names of aggregate function of specified aggregate type.
	 * @throws ChartException
	 * @since BIRT 2.3
	 */
	public String[] getRegisteredAggregateFunctionDisplayNames(int aggregateType) throws ChartException {
		if (inEclipseEnv()) {
			String[][] aggs = getPluginXmlStrings("aggregatefunctions", //$NON-NLS-1$
					"aggregateFunction", //$NON-NLS-1$
					"name", //$NON-NLS-1$
					"displayName"); //$NON-NLS-1$

			List<String> funcList = new ArrayList<>();
			for (int i = 0; i < aggs.length; i++) {
				IAggregateFunction aFunc = getAggregateFunction(aggs[i][0]);
				if (aFunc.getType() == aggregateType) {
					funcList.add(aggs[i][1]);
				}
			}

			final String[] saFunctions = new String[funcList.size()];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = funcList.get(i);
			}

			return saFunctions;
		} else {
			final String[] saFunctions = new String[saBaseAggregateFunctions.length];
			for (int i = 0; i < saFunctions.length; i++) {
				saFunctions[i] = saBaseAggregateFunctions[i][1];
			}
			return saFunctions;
		}
	}

	/**
	 * Attempts to internally create an instance of a given class using reflection
	 * using the default constructor.
	 *
	 * @param sFQClassName The fully qualified class name for which a new instance
	 *                     is being requested
	 *
	 * @return A new instance of the requested class
	 *
	 * @throws ChartException
	 */
	private static Object newInstance(String sFQClassName) throws ChartException {
		try {
			final Class<?> c = Class.forName(sFQClassName);
			return SecurityUtil.newClassInstance(c);
		} catch (Exception ex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN, ex);
		}
	}

	/**
	 * Attempts to walk through the schema tree as defined in an extension point
	 * schema and instantiate the class associated with the value for a given
	 * element name.
	 *
	 * @param sXsdListName
	 * @param sXsdComplexName
	 * @param sXsdElementName
	 * @param sXsdElementValue
	 * @param sLookupName
	 *
	 * @return An instance of the value class instantiated via the extension
	 *         framework
	 */
	private static Object getPluginXmlObject(String sXsdListName, String sXsdComplexName, String sXsdElementName,
			String sXsdElementValue, String sLookupName) throws ChartException {
		final IExtensionRegistry ier = Platform.getExtensionRegistry();
		final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
		if (iep == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN, "exception.cannot.find.plugin.entry", //$NON-NLS-1$
					new Object[] { sLookupName, sXsdElementName, sXsdElementValue }, Messages.getResourceBundle());
		}
		final IExtension[] iea = iep.getExtensions();
		IConfigurationElement[] icea;

		for (int i = 0; i < iea.length; i++) {
			icea = iea[i].getConfigurationElements();
			for (int j = 0; j < icea.length; j++) {
				if (icea[j].getName().equals(sXsdComplexName)) {
					if (icea[j].getAttribute(sXsdElementName).equals(sLookupName)) {
						try {
							return icea[j].createExecutableExtension(sXsdElementValue);
						} catch (FrameworkException cex) {
							throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN, cex);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Attempts to walk through the schema tree as defined in an extension point
	 * schema and retrieve the attribute value associated with the value for a given
	 * element name.
	 *
	 * @param sXsdListName
	 * @param sXsdComplexName
	 * @param sXsdElementName
	 * @param sXsdElementValue
	 * @param sLookupName
	 * @return XML attribute
	 * @throws ChartException
	 */
	private static String getPluginXmlAttribute(String sXsdListName, String sXsdComplexName, String sXsdElementName,
			String sXsdElementValue, String sLookupName) throws ChartException {
		final IExtensionRegistry ier = Platform.getExtensionRegistry();
		final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
		if (iep == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN, "exception.cannot.find.plugin.entry", //$NON-NLS-1$
					new Object[] { sLookupName, sXsdElementName, sXsdElementValue }, Messages.getResourceBundle());
		}
		final IExtension[] iea = iep.getExtensions();
		IConfigurationElement[] icea;

		for (int i = 0; i < iea.length; i++) {
			icea = iea[i].getConfigurationElements();
			for (int j = 0; j < icea.length; j++) {
				if (icea[j].getName().equals(sXsdComplexName)) {
					if (icea[j].getAttribute(sXsdElementName).equals(sLookupName)) {
						return icea[j].getAttribute(sXsdElementValue);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Attempts to walk through the schema tree as defined in an extension point
	 * schema and instantiate the class associated with the value for a given
	 * element name.
	 *
	 * @param sXsdListName
	 * @param sXsdComplexName
	 * @param sXsdElementName
	 * @param sXsdElementValue
	 * @param sLookupName
	 *
	 * @return An array of the text value via the extension framework
	 */
	private static String[][] getPluginXmlStrings(String sXsdListName, String sXsdComplexName, String sXsdElementName,
			String sXsdElementValue) throws ChartException {
		final IExtensionRegistry ier = Platform.getExtensionRegistry();
		final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
		if (iep == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN, "exception.cannot.find.plugin.entry", //$NON-NLS-1$
					new Object[] { "", sXsdElementName, sXsdElementValue //$NON-NLS-1$
					}, Messages.getResourceBundle());
		}
		final IExtension[] iea = iep.getExtensions();
		IConfigurationElement[] icea;

		List<String[]> lst = new ArrayList<>();

		for (int i = 0; i < iea.length; i++) {
			icea = iea[i].getConfigurationElements();
			for (int j = 0; j < icea.length; j++) {
				if (icea[j].getName().equals(sXsdComplexName)) {
					lst.add(new String[] { icea[j].getAttribute(sXsdElementName),
							icea[j].getAttribute(sXsdElementValue) });
				}
			}
		}

		return lst.toArray(new String[0][0]);
	}

	/**
	 * Performs an internal check to test if the environment uses the extension
	 * loading framework or hardcoded classes as defined
	 *
	 * @return 'true' if using the extension loading framework
	 */
	public boolean inEclipseEnv() {
		if (bStandalone) {
			return false;
		}
		return (Platform.getExtensionRegistry() != null);
	}

	/**
	 * Registers an aggregate function implementation, the class should implement
	 * the IAggregateFunction interface. The displayName will be the same as the
	 * name.
	 *
	 * @param sName
	 * @param sAggregateFunctionClass the full qualified class name of the
	 *                                implementor.
	 *
	 * @see IAggregateFunction
	 */
	public void registerAggregateFunction(String sName, String sAggregateFunctionClass) {
		registerAggregateFunction(sName, sName, sAggregateFunctionClass);
	}

	/**
	 * Registers an aggregate function implementation, the class should implement
	 * the IAggregateFunction interface.
	 *
	 * @param sName
	 * @param sDisplayName
	 * @param sAggregateFunctionClass the full qualified class name of the
	 *                                implementor.
	 *
	 * @see IAggregateFunction
	 */
	synchronized public void registerAggregateFunction(String sName, String sDisplayName,
			String sAggregateFunctionClass) {
		String[][] newAggFuncs = new String[saBaseAggregateFunctions.length + 1][3];
		for (int i = 0; i < saBaseAggregateFunctions.length; i++) {
			newAggFuncs[i][0] = saBaseAggregateFunctions[i][0];
			newAggFuncs[i][1] = saBaseAggregateFunctions[i][1];
			newAggFuncs[i][2] = saBaseAggregateFunctions[i][2];
		}
		newAggFuncs[saBaseAggregateFunctions.length][0] = sName;
		newAggFuncs[saBaseAggregateFunctions.length][1] = sDisplayName;
		newAggFuncs[saBaseAggregateFunctions.length][2] = sAggregateFunctionClass;

		saBaseAggregateFunctions = newAggFuncs;
	}

	/**
	 * Registers a device renderer implementation, this class should implement the
	 * IDeviceRenderer interface.
	 *
	 * @param sName
	 * @param sDeviceClass the full qualified class name of the implementor.
	 *
	 * @see IDeviceRenderer
	 */
	synchronized public void registerDevice(String sName, String sDeviceClass) {
		String[][] newDevs = new String[saDevices.length + 1][4];
		for (int i = 0; i < saDevices.length; i++) {
			newDevs[i][0] = saDevices[i][0];
			newDevs[i][1] = saDevices[i][1];
			newDevs[i][2] = saDevices[i][2];
			newDevs[i][3] = saDevices[i][3];
		}
		newDevs[saDevices.length][0] = sName;
		newDevs[saDevices.length][1] = sDeviceClass;

		saDevices = newDevs;
	}

	/**
	 * Registers an output format for given device renderer name.
	 *
	 * @param sDeviceName
	 * @param sFormat
	 * @param sDescription
	 */
	synchronized public void registerOutputFormat(String sDeviceName, String sFormat, String sDescription) {
		for (int i = 0; i < saDevices.length; i++) {
			if (saDevices[i][0].equals(sDeviceName)) {
				if (sFormat != null && sFormat.length() > 0) {
					saDevices[i][2] = sFormat;
				}
				if (sDescription != null && sDescription.length() > 0) {
					saDevices[i][3] = sDescription;
				}
				break;
			}
		}
	}

	/**
	 * Registers a display server implementation, this class should implement the
	 * IDisplayServer interface.
	 *
	 * @param sName
	 * @param sServerClass the full qualified class name of the implementor.
	 *
	 * @see IDisplayServer
	 */
	synchronized public void registerDisplayServer(String sName, String sServerClass) {
		String[][] newSvrs = new String[saDisplayServers.length + 1][2];
		for (int i = 0; i < saDisplayServers.length; i++) {
			newSvrs[i][0] = saDisplayServers[i][0];
			newSvrs[i][1] = saDisplayServers[i][1];
		}
		newSvrs[saDisplayServers.length][0] = sName;
		newSvrs[saDisplayServers.length][1] = sServerClass;

		saDisplayServers = newSvrs;
	}

	/**
	 * Registers a new Series renderer implementation.
	 *
	 * @param sSeriesClass           the full qualified class name of which
	 *                               implements the Series interface.
	 * @param sDataSetProcessorClass the full qualified class name of which
	 *                               implements the IDataSetProcessor interface.
	 * @param sRendererClass         the full qualified class name of which
	 *                               implements the ISeriesRenderer interface.
	 *
	 * @see ISeriesRenderer
	 */
	synchronized public void registerSeriesRenderer(String sSeriesClass, String sDataSetProcessorClass,
			String sRendererClass) {
		String[] newSeries = new String[saSeries.length + 1];
		String[] newDSPs = new String[saSeries.length + 1];
		String[] newRenderers = new String[saSeries.length + 1];
		for (int i = 0; i < saSeries.length; i++) {
			newSeries[i] = saSeries[i];
			newDSPs[i] = saDataSetProcessors[i];
			newRenderers[i] = saRenderers[i];
		}
		newSeries[saSeries.length] = sSeriesClass;
		newDSPs[saSeries.length] = sDataSetProcessorClass;
		newRenderers[saSeries.length] = sRendererClass;

		saSeries = newSeries;
		saDataSetProcessors = newDSPs;
		saRenderers = newRenderers;
	}

}
