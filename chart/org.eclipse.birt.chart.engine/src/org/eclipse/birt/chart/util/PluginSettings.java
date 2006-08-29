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

package org.eclipse.birt.chart.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
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
public final class PluginSettings
{

	private static final String PLUGIN = "org.eclipse.birt.chart.engine"; //$NON-NLS-1$

	/**
	 * All available series types for which extensions are defined. Note that
	 * this list is index sensitive.
	 */
	private static String[] saSeries = {
			"org.eclipse.birt.chart.model.component.impl.SeriesImpl", //$NON-NLS-1$
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
	 * defined. Note that this list is index sensitive and corresponds to the
	 * series type list.
	 */
	private static String[] saDataSetProcessors = {
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.StockDataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.BubbleDataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.GanttDataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DifferenceDataSetProcessorImpl", //$NON-NLS-1$
	};

	/**
	 * All series renderer implementing class names for which extensions are
	 * defined. Note that this list is index sensitive and corresponds to the
	 * series type list.
	 */
	private static String[] saRenderers = {
			null, "org.eclipse.birt.chart.render.Area", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.render.Bar", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.render.Dial", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Line", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Pie", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.render.Stock", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Scatter", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Bubble", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Gantt", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Difference", //$NON-NLS-1$
	};

	/**
	 * All available device renderers provided in the out-of-the-box
	 * distribution
	 */
	private static String[][] saDevices = {
			{
					"dv.SWING", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.swing.SwingRendererImpl", //$NON-NLS-1$
					null,
					null
			}, {
					"dv.SWT", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.swt.SwtRendererImpl", //$NON-NLS-1$
					null,
					null
			}, {
					"dv.PNG24", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.PngRendererImpl", //$NON-NLS-1$
					null,
					"Deprecated, use PNG instead" //$NON-NLS-1$
			}, {
					"dv.GIF8", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.GifRendererImpl", //$NON-NLS-1$
					null,
					"Deprecated, use PNG instead" //$NON-NLS-1$
			}, {
					"dv.PNG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.PngRendererImpl", //$NON-NLS-1$
					"PNG", //$NON-NLS-1$
					"Portable Network Graphics" //$NON-NLS-1$
			}, {
					"dv.GIF", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.GifRendererImpl", //$NON-NLS-1$
					null,
					"Deprecated, use PNG instead" //$NON-NLS-1$
			}, {
					"dv.JPEG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.JpegRendererImpl", //$NON-NLS-1$
					null,
					null
			}, {
					"dv.JPG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.JpegRendererImpl", //$NON-NLS-1$
					"JPG", //$NON-NLS-1$
					null
			}, {
					"dv.BMP", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.image.BmpRendererImpl", //$NON-NLS-1$
					"BMP", //$NON-NLS-1$
					null
			}, {
					"dv.SVG", //$NON-NLS-1$
					"org.eclipse.birt.chart.device.svg.SVGRendererImpl", //$NON-NLS-1$
					"SVG", //$NON-NLS-1$
					null
			}
	};

	/**
	 * All available display servers provided in the out-of-the-box distribution
	 */
	private static String[][] saDisplayServers = {
			{
					"ds.SWING", "org.eclipse.birt.chart.device.swing.SwingDisplayServer" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"ds.SWT", "org.eclipse.birt.chart.device.swt.SwtDisplayServer" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"ds.SVG", "org.eclipse.birt.chart.device.svg.SVGDisplayServer" //$NON-NLS-1$ //$NON-NLS-2$
			}
	};

	/**
	 * All available aggregate functions used in orthogonal value aggregation
	 */
	private static String[][] saAggregateFunctions = {
			{
					"Sum", "Sum", "org.eclipse.birt.chart.aggregate.Sum" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			},
			{
					"Average", "Average", "org.eclipse.birt.chart.aggregate.Average" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
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

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/util" ); //$NON-NLS-1$

	/**
	 * A non-instantiable constructor
	 */
	private PluginSettings( )
	{
	}

	/**
	 * Returns a singleton instance of the plugin settings framework which uses
	 * the default configuration.
	 * 
	 * @return A singleton instance of the plugin settings framework
	 */
	public static PluginSettings instance( )
	{
		return instance( null );
	}

	/**
	 * Returns a singleton instance of the plugin settings framework with
	 * specific configuration.
	 * 
	 * @return A singleton instance of the plugin settings framework
	 */
	public static synchronized PluginSettings instance( PlatformConfig config )
	{
		if ( ps == null )
		{
			ps = new PluginSettings( );
			ps.bStandalone = System.getProperty( "STANDALONE" ) != null; //$NON-NLS-1$

			if ( !ps.bStandalone )
			{
				try
				{
					Platform.startup( config );
				}
				catch ( BirtException e )
				{
					logger.log( e );
				}
			}
		}
		return ps;
	}

	/**
	 * Retrieves the first instance of a data set processor registered as an
	 * extension for a given series type.
	 * 
	 * @param cSeries
	 *            The Class instance associated with the given series type
	 * 
	 * @return A newly created instance of a registered data set processor
	 *         extension
	 * 
	 * @throws PluginException
	 */
	public final IDataSetProcessor getDataSetProcessor( Class cSeries )
			throws ChartException
	{
		final String sFQClassName = cSeries.getName( );
		if ( inEclipseEnv( ) )
		{
			final Object oDSP = getPluginXmlObject( "datasetprocessors", "datasetProcessor", "series", "processor", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sFQClassName );

			if ( oDSP != null )
			{
				logger.log( ILogger.INFORMATION,
						Messages.getString( "info.eclenv.creating.dsp", //$NON-NLS-1$
								new Object[]{
									oDSP.getClass( ).getName( )
								},
								ULocale.getDefault( ) // LOCALE?
						) );
				return (IDataSetProcessor) oDSP;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.dsp", //$NON-NLS-1$
							new Object[]{
								sFQClassName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		else
		{
			for ( int i = 0; i < saSeries.length; i++ )
			{
				if ( sFQClassName.equals( saSeries[i] ) )
				{
					logger.log( ILogger.INFORMATION,
							Messages.getString( "info.stdenv.creating.dsp", //$NON-NLS-1$
									new Object[]{
										saDataSetProcessors[i]
									},
									ULocale.getDefault( ) // LOCALE?
							) );
					return (IDataSetProcessor) newInstance( saDataSetProcessors[i] );
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.dsp", //$NON-NLS-1$ 
							new Object[]{
								sFQClassName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a series renderer registered as an
	 * extension for a given series type.
	 * 
	 * @param cSeries
	 *            The Class instance associated with the given series type
	 * 
	 * @return A newly created (and initialized) instance of a registered series
	 *         renderer
	 * 
	 * @throws PluginException
	 */
	public final BaseRenderer getRenderer( Class cSeries )
			throws ChartException
	{
		final String sFQClassName = cSeries.getName( );
		if ( inEclipseEnv( ) )
		{
			final Object oSeriesRenderer = getPluginXmlObject( "modelrenderers", "modelRenderer", "series", "renderer", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sFQClassName );
			if ( oSeriesRenderer != null )
			{
				logger.log( ILogger.INFORMATION,
						Messages.getString( "info.eclenv.creating.series.renderer", //$NON-NLS-1$
								new Object[]{
									oSeriesRenderer.getClass( ).getName( )
								},
								ULocale.getDefault( ) // LOCALE?
						) );
				return (BaseRenderer) oSeriesRenderer;
			}
			logger.log( ILogger.ERROR,
					Messages.getString( "error.eclenv.cannot.find.series.renderer", //$NON-NLS-1$
							new Object[]{
								sFQClassName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		else
		{
			for ( int i = 0; i < saSeries.length; i++ )
			{
				if ( sFQClassName.equals( saSeries[i] ) )
				{
					if ( saRenderers[i] == null )
					{
						break;
					}
					logger.log( ILogger.INFORMATION,
							Messages.getString( "info.stdenv.creating.series.renderer", //$NON-NLS-1$
									new Object[]{
										saRenderers[i]
									},
									ULocale.getDefault( ) // LOCALE?
							) );
					return (BaseRenderer) newInstance( saRenderers[i] );
				}
			}
			logger.log( ILogger.ERROR,
					Messages.getString( "error.stdenv.cannot.find.series.renderer", //$NON-NLS-1$
							new Object[]{
								sFQClassName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a device renderer registered as an
	 * extension for a given name
	 * 
	 * @param sName
	 *            The name of the device renderer. Values registered in the
	 *            default distribution are dv.SWT, dv.SWING, dv.PNG, dv.JPEG,
	 *            dv.BMP
	 * 
	 * @return An newly initialized instance of the requested device renderer
	 * 
	 * @throws PluginException
	 */
	public final IDeviceRenderer getDevice( String sName )
			throws ChartException
	{
		if ( inEclipseEnv( ) )
		{
			final Object oDeviceRenderer = getPluginXmlObject( "devicerenderers", "deviceRenderer", "name", "device", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sName );
			if ( oDeviceRenderer != null )
			{
				logger.log( ILogger.INFORMATION,
						Messages.getString( "info.eclenv.creating.device", //$NON-NLS-1$
								new Object[]{
										sName,
										oDeviceRenderer.getClass( ).getName( )
								},
								ULocale.getDefault( ) // LOCALE?
						) );

				final String sFormat = getPluginXmlAttribute( "devicerenderers", "deviceRenderer", "name", "format", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						sName );
				if ( sFormat != null && sFormat.length( ) > 0 )
				{
					( (IDeviceRenderer) oDeviceRenderer ).setProperty( IDeviceRenderer.FORMAT_IDENTIFIER,
							sFormat );
				}
				return (IDeviceRenderer) oDeviceRenderer;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.device", //$NON-NLS-1$
							new Object[]{
								sName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		else
		{
			for ( int i = 0; i < saDevices.length; i++ )
			{
				if ( saDevices[i][0].equalsIgnoreCase( sName ) )
				{
					logger.log( ILogger.INFORMATION,
							Messages.getString( "info.stdenv.creating.device", //$NON-NLS-1$
									new Object[]{
											sName, saDevices[i][1]
									},
									ULocale.getDefault( ) // LOCALE?
							) );
					IDeviceRenderer idr = (IDeviceRenderer) newInstance( saDevices[i][1] );
					if ( saDevices[i][2] != null
							&& saDevices[i][2].length( ) > 0 )
					{
						idr.setProperty( IDeviceRenderer.FORMAT_IDENTIFIER,
								saDevices[i][2] );
					}
					return idr;
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.device", //$NON-NLS-1$
							new Object[]{
								sName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a display server registered as an
	 * extension for a given name
	 * 
	 * @param sName
	 *            The name of the display server. Values registered in the
	 *            default distribution are ds.SWT, ds.SWING
	 * 
	 * @return An newly initialized instance of the requested display server
	 * 
	 * @throws PluginException
	 */
	public final IDisplayServer getDisplayServer( String sName )
			throws ChartException
	{
		if ( inEclipseEnv( ) )
		{
			final Object oDisplayServer = getPluginXmlObject( "displayservers", "displayserver", "name", "server", sName ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			if ( oDisplayServer != null )
			{
				logger.log( ILogger.INFORMATION,
						Messages.getString( "info.eclenv.creating.display", //$NON-NLS-1$
								new Object[]{
										sName,
										oDisplayServer.getClass( ).getName( )
								},
								ULocale.getDefault( ) // LOCALE?
						) );
				return (IDisplayServer) oDisplayServer;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.display", //$NON-NLS-1$
							new Object[]{
								sName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		else
		{
			for ( int i = 0; i < saDisplayServers.length; i++ )
			{
				if ( saDisplayServers[i][0].equalsIgnoreCase( sName ) )
				{
					logger.log( ILogger.INFORMATION,
							Messages.getString( "info.stdenv.creating.display", //$NON-NLS-1$
									new Object[]{
											sName, saDisplayServers[i][1]
									},
									ULocale.getDefault( ) // LOCALE?
							) );
					return (IDisplayServer) newInstance( saDisplayServers[i][1] );
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.display", //$NON-NLS-1$
							new Object[]{
								sName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		return null;
	}

	/**
	 * Retrieves the first instance of a device renderer registered as an
	 * extension for a given name
	 * 
	 * @param sName
	 *            The name of the aggregate function.
	 * 
	 * @return An newly initialized instance of the requested aggregate
	 *         function.
	 * 
	 * @throws PluginException
	 */
	public final IAggregateFunction getAggregateFunction( String sName )
			throws ChartException
	{
		if ( inEclipseEnv( ) )
		{
			final Object oAggregateFunction = getPluginXmlObject( "aggregatefunctions", "aggregateFunction", "name", "function", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					sName );
			if ( oAggregateFunction != null )
			{
				logger.log( ILogger.INFORMATION,
						Messages.getString( "info.eclenv.creating.function", //$NON-NLS-1$
								new Object[]{
										sName,
										oAggregateFunction.getClass( )
												.getName( )
								},
								ULocale.getDefault( ) // LOCALE?
						) );
				return (IAggregateFunction) oAggregateFunction;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.function", //$NON-NLS-1$
							new Object[]{
								sName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		else
		{
			for ( int i = 0; i < saAggregateFunctions.length; i++ )
			{
				if ( saAggregateFunctions[i][0].equalsIgnoreCase( sName ) )
				{
					logger.log( ILogger.INFORMATION,
							Messages.getString( "info.stdenv.creating.function", //$NON-NLS-1$
									new Object[]{
											sName, saAggregateFunctions[i][1]
									},
									ULocale.getDefault( ) // LOCALE?
							) );
					return (IAggregateFunction) newInstance( saAggregateFunctions[i][2] );
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.function", //$NON-NLS-1$
							new Object[]{
								sName
							},
							ULocale.getDefault( ) // LOCALE?
					) );
		}
		return null;
	}

	/**
	 * Returns the localized display name of given Series Class.
	 * 
	 * @param seriesClassName
	 * @return
	 */
	public final String getSeriesDisplayName( String seriesClassName )
	{
		String sDisplayName = seriesClassName;
		try
		{
			Class seriesClass = Class.forName( seriesClassName );
			Method createMethod = seriesClass.getDeclaredMethod( "create", new Class[]{} ); //$NON-NLS-1$
			Series newSeries = (Series) createMethod.invoke( seriesClass,
					new Object[]{} );
			Method mDisplayName = seriesClass.getDeclaredMethod( "getDisplayName", new Class[]{} ); //$NON-NLS-1$
			Object oName = mDisplayName.invoke( newSeries, new Object[]{} );
			sDisplayName = (String) oName;
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace( );
		}
		catch ( NoSuchMethodException e )
		{
			e.printStackTrace( );
		}
		catch ( IllegalAccessException e )
		{
			e.printStackTrace( );
		}
		catch ( IllegalArgumentException e )
		{
			e.printStackTrace( );
		}
		catch ( InvocationTargetException e )
		{
			e.printStackTrace( );
		}
		return sDisplayName;
	}

	/**
	 * Returns a list of all series registered via extension point
	 * implementations (or simulated)
	 * 
	 * @return A list of series registered via extension point implementations
	 *         (or simulated)
	 */
	public final String[] getRegisteredSeries( ) throws ChartException
	{
		if ( inEclipseEnv( ) )
		{
			String[][] sers = getPluginXmlStrings( "modelrenderers", //$NON-NLS-1$
					"modelRenderer", //$NON-NLS-1$
					"series", //$NON-NLS-1$
					"renderer" ); //$NON-NLS-1$

			final String[] saSeries = new String[sers.length];
			for ( int i = 0; i < saSeries.length; i++ )
			{
				saSeries[i] = sers[i][0];
			}
			return saSeries;
		}
		else
		{
			return saSeries;
		}
	}

	/**
	 * Returns a list of registered device renderer output formats and
	 * descriptions.
	 * 
	 * @return
	 * @throws ChartException
	 */
	public final String[][] getRegisteredOutputFormats( ) throws ChartException
	{
		if ( inEclipseEnv( ) )
		{
			String[][] formats = getPluginXmlStrings( "devicerenderers", //$NON-NLS-1$
					"deviceRenderer", //$NON-NLS-1$
					"format", //$NON-NLS-1$
					"description" ); //$NON-NLS-1$

			ArrayList al = new ArrayList( );
			for ( int i = 0; i < formats.length; i++ )
			{
				if ( formats[i][0] != null && formats[i][0].length( ) > 0 )
				{
					al.add( formats[i] );
				}
			}
			return (String[][]) al.toArray( new String[0][0] );
		}
		else
		{
			ArrayList al = new ArrayList( );
			for ( int i = 0; i < saDevices.length; i++ )
			{
				if ( saDevices[i][2] != null && saDevices[i][2].length( ) > 0 )
				{
					al.add( new String[]{
							saDevices[i][2], saDevices[i][3]
					} );
				}
			}
			return (String[][]) al.toArray( new String[0][0] );
		}
	}

	/**
	 * Returns a list of all aggregate function names registered via extension
	 * point implementations (or simulated)
	 * 
	 * @return A list of all aggregate function names registered via extension
	 *         point implementations (or simulated)
	 */
	public final String[] getRegisteredAggregateFunctions( )
			throws ChartException
	{
		if ( inEclipseEnv( ) )
		{
			String[][] aggs = getPluginXmlStrings( "aggregatefunctions", //$NON-NLS-1$
					"aggregateFunction", //$NON-NLS-1$
					"name", //$NON-NLS-1$
					"function" ); //$NON-NLS-1$

			final String[] saFunctions = new String[aggs.length];
			for ( int i = 0; i < saFunctions.length; i++ )
			{
				saFunctions[i] = aggs[i][0];
			}
			return saFunctions;
		}
		else
		{
			final String[] saFunctions = new String[saAggregateFunctions.length];
			for ( int i = 0; i < saFunctions.length; i++ )
			{
				saFunctions[i] = saAggregateFunctions[i][0];
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
	public final String[] getRegisteredAggregateFunctionDisplayNames( )
			throws ChartException
	{
		if ( inEclipseEnv( ) )
		{
			String[][] aggs = getPluginXmlStrings( "aggregatefunctions", //$NON-NLS-1$
					"aggregateFunction", //$NON-NLS-1$
					"name", //$NON-NLS-1$
					"displayName" ); //$NON-NLS-1$

			final String[] saFunctions = new String[aggs.length];
			for ( int i = 0; i < saFunctions.length; i++ )
			{
				saFunctions[i] = aggs[i][1];
			}
			return saFunctions;
		}
		else
		{
			final String[] saFunctions = new String[saAggregateFunctions.length];
			for ( int i = 0; i < saFunctions.length; i++ )
			{
				saFunctions[i] = saAggregateFunctions[i][1];
			}
			return saFunctions;
		}
	}

	/**
	 * Attempts to internally create an instance of a given class using
	 * reflection using the default constructor.
	 * 
	 * @param sFQClassName
	 *            The fully qualified class name for which a new instance is
	 *            being requested
	 * 
	 * @return A new instance of the requested class
	 * 
	 * @throws PluginException
	 */
	private static final Object newInstance( String sFQClassName )
			throws ChartException
	{
		try
		{
			final Class c = Class.forName( sFQClassName );
			return c.newInstance( );
		}
		catch ( Exception ex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.PLUGIN,
					ex );
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
	private static final Object getPluginXmlObject( String sXsdListName,
			String sXsdComplexName, String sXsdElementName,
			String sXsdElementValue, String sLookupName ) throws ChartException
	{
		final IExtensionRegistry ier = Platform.getExtensionRegistry( );
		final IExtensionPoint iep = ier.getExtensionPoint( PLUGIN, sXsdListName );
		if ( iep == null )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.PLUGIN,
					"exception.cannot.find.plugin.entry", //$NON-NLS-1$
					new Object[]{
							sLookupName, sXsdElementName, sXsdElementValue
					},
					Messages.getResourceBundle( ) );
		}
		final IExtension[] iea = iep.getExtensions( );
		IConfigurationElement[] icea;

		for ( int i = 0; i < iea.length; i++ )
		{
			icea = iea[i].getConfigurationElements( );
			for ( int j = 0; j < icea.length; j++ )
			{
				if ( icea[j].getName( ).equals( sXsdComplexName ) )
				{
					if ( icea[j].getAttribute( sXsdElementName )
							.equals( sLookupName ) )
					{
						try
						{
							return icea[j].createExecutableExtension( sXsdElementValue );
						}
						catch ( FrameworkException cex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.PLUGIN,
									cex );
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Attempts to walk through the schema tree as defined in an extension point
	 * schema and retrieve the attribute value associated with the value for a
	 * given element name.
	 * 
	 * @param sXsdListName
	 * @param sXsdComplexName
	 * @param sXsdElementName
	 * @param sXsdElementValue
	 * @param sLookupName
	 * @return
	 * @throws ChartException
	 */
	private static final String getPluginXmlAttribute( String sXsdListName,
			String sXsdComplexName, String sXsdElementName,
			String sXsdElementValue, String sLookupName ) throws ChartException
	{
		final IExtensionRegistry ier = Platform.getExtensionRegistry( );
		final IExtensionPoint iep = ier.getExtensionPoint( PLUGIN, sXsdListName );
		if ( iep == null )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.PLUGIN,
					"exception.cannot.find.plugin.entry", //$NON-NLS-1$
					new Object[]{
							sLookupName, sXsdElementName, sXsdElementValue
					},
					Messages.getResourceBundle( ) );
		}
		final IExtension[] iea = iep.getExtensions( );
		IConfigurationElement[] icea;

		for ( int i = 0; i < iea.length; i++ )
		{
			icea = iea[i].getConfigurationElements( );
			for ( int j = 0; j < icea.length; j++ )
			{
				if ( icea[j].getName( ).equals( sXsdComplexName ) )
				{
					if ( icea[j].getAttribute( sXsdElementName )
							.equals( sLookupName ) )
					{
						return icea[j].getAttribute( sXsdElementValue );
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
	private static final String[][] getPluginXmlStrings( String sXsdListName,
			String sXsdComplexName, String sXsdElementName,
			String sXsdElementValue ) throws ChartException
	{
		final IExtensionRegistry ier = Platform.getExtensionRegistry( );
		final IExtensionPoint iep = ier.getExtensionPoint( PLUGIN, sXsdListName );
		if ( iep == null )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.PLUGIN,
					"exception.cannot.find.plugin.entry", //$NON-NLS-1$
					new Object[]{
							"", sXsdElementName, sXsdElementValue //$NON-NLS-1$
					},
					Messages.getResourceBundle( ) );
		}
		final IExtension[] iea = iep.getExtensions( );
		IConfigurationElement[] icea;

		List lst = new ArrayList( );

		for ( int i = 0; i < iea.length; i++ )
		{
			icea = iea[i].getConfigurationElements( );
			for ( int j = 0; j < icea.length; j++ )
			{
				if ( icea[j].getName( ).equals( sXsdComplexName ) )
				{
					lst.add( new String[]{
							icea[j].getAttribute( sXsdElementName ),
							icea[j].getAttribute( sXsdElementValue )
					} );
				}
			}
		}

		return (String[][]) lst.toArray( new String[0][0] );
	}

	/**
	 * Performs an internal check to test if the environment uses the extension
	 * loading framework or hardcoded classes as defined
	 * 
	 * @return 'true' if using the extension loading framework
	 */
	private boolean inEclipseEnv( )
	{
		if ( bStandalone )
		{
			return false;
		}
		return ( Platform.getExtensionRegistry( ) != null );
	}

	/**
	 * Registers an aggregate function implementation, the class should
	 * implement the IAggregateFunction interface. The displayName will be the
	 * same as the name.
	 * 
	 * @param sName
	 * @param sAggregateFunctionClass
	 *            the full qualified class name of the implementor.
	 * 
	 * @see IAggregateFunction
	 */
	public final void registerAggregateFunction( String sName,
			String sAggregateFunctionClass )
	{
		registerAggregateFunction( sName, sName, sAggregateFunctionClass );
	}

	/**
	 * Registers an aggregate function implementation, the class should
	 * implement the IAggregateFunction interface.
	 * 
	 * @param sName
	 * @param sDisplayName
	 * @param sAggregateFunctionClass
	 *            the full qualified class name of the implementor.
	 * 
	 * @see IAggregateFunction
	 */
	synchronized public final void registerAggregateFunction( String sName,
			String sDisplayName, String sAggregateFunctionClass )
	{
		String[][] newAggFuncs = new String[saAggregateFunctions.length + 1][3];
		for ( int i = 0; i < saAggregateFunctions.length; i++ )
		{
			newAggFuncs[i][0] = saAggregateFunctions[i][0];
			newAggFuncs[i][1] = saAggregateFunctions[i][1];
			newAggFuncs[i][2] = saAggregateFunctions[i][2];
		}
		newAggFuncs[saAggregateFunctions.length][0] = sName;
		newAggFuncs[saAggregateFunctions.length][1] = sDisplayName;
		newAggFuncs[saAggregateFunctions.length][2] = sAggregateFunctionClass;

		saAggregateFunctions = newAggFuncs;
	}

	/**
	 * Registers a device renderer implementation, this class should implement
	 * the IDeviceRenderer interface.
	 * 
	 * @param sName
	 * @param sDeviceClass
	 *            the full qualified class name of the implementor.
	 * 
	 * @see IDeviceRenderer
	 */
	synchronized public final void registerDevice( String sName,
			String sDeviceClass )
	{
		String[][] newDevs = new String[saDevices.length + 1][4];
		for ( int i = 0; i < saDevices.length; i++ )
		{
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
	synchronized public final void registerOutputFormat( String sDeviceName,
			String sFormat, String sDescription )
	{
		for ( int i = 0; i < saDevices.length; i++ )
		{
			if ( saDevices[i][0].equals( sDeviceName ) )
			{
				if ( sFormat != null && sFormat.length( ) > 0 )
				{
					saDevices[i][2] = sFormat;
				}
				if ( sDescription != null && sDescription.length( ) > 0 )
				{
					saDevices[i][3] = sDescription;
				}
				break;
			}
		}
	}

	/**
	 * Registers a display server implementation, this class should implement
	 * the IDisplayServer interface.
	 * 
	 * @param sName
	 * @param sServerClass
	 *            the full qualified class name of the implementor.
	 * 
	 * @see IDisplayServer
	 */
	synchronized public final void registerDisplayServer( String sName,
			String sServerClass )
	{
		String[][] newSvrs = new String[saDisplayServers.length + 1][2];
		for ( int i = 0; i < saDisplayServers.length; i++ )
		{
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
	 * @param sSeriesClass
	 *            the full qualified class name of which implements the Series
	 *            interface.
	 * @param sDataSetProcessorClass
	 *            the full qualified class name of which implements the
	 *            IDataSetProcessor interface.
	 * @param sRendererClass
	 *            the full qualified class name of which implements the
	 *            ISeriesRenderer interface.
	 * 
	 * @see ISeriesRenderer
	 */
	synchronized public final void registerSeriesRenderer( String sSeriesClass,
			String sDataSetProcessorClass, String sRendererClass )
	{
		String[] newSeries = new String[saSeries.length + 1];
		String[] newDSPs = new String[saSeries.length + 1];
		String[] newRenderers = new String[saSeries.length + 1];
		for ( int i = 0; i < saSeries.length; i++ )
		{
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