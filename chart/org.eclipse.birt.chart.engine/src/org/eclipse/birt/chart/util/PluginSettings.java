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
import java.util.Locale;
import java.util.ResourceBundle;

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
import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;

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

	/**
	 * 
	 */
	private static final String PLUGIN = "org.eclipse.birt.chart.engine"; //$NON-NLS-1$

	/**
	 * All available series types for which extensions are defined. Note that
	 * this list is index sensitive.
	 */
	private static String[] saSeries = {
			"org.eclipse.birt.chart.model.component.impl.SeriesImpl", //$NON-NLS-1$
			//"org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.model.type.impl.BarSeriesImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.model.type.impl.LineSeriesImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.model.type.impl.PieSeriesImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.model.type.impl.StockSeriesImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl" //$NON-NLS-1$
	};

	/**
	 * All data set processor implementing class names for which extensions are
	 * defined. Note that this list is index sensitive and corresponds to the
	 * series type list.
	 */
	private static String[] saDataSetProcessors = {
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			//"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.StockDataSetProcessorImpl", //$NON-NLS-1$
			"org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", //$NON-NLS-1$
	};

	/**
	 * All series renderer implementing class names for which extensions are
	 * defined. Note that this list is index sensitive and corresponds to the
	 * series type list.
	 */
	private static String[] saRenderers = {
			null,
			//"org.eclipse.birt.chart.render.Area", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.render.Bar", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.render.Line", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Pie", //$NON-NLS-1$ 
			"org.eclipse.birt.chart.render.Stock", //$NON-NLS-1$
			"org.eclipse.birt.chart.render.Scatter" //$NON-NLS-1$
	};

	/**
	 * All available device renderers provided in the out-of-the-box
	 * distribution
	 */
	private static String[][] saDevices = {
			{
					"dv.SWING", "org.eclipse.birt.chart.device.swing.SwingRendererImpl" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.SWT", "org.eclipse.birt.chart.device.swt.SwtRendererImpl" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.PNG24", "org.eclipse.birt.chart.device.image.PngRendererImplOld" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.GIF8", "org.eclipse.birt.chart.device.image.GifRendererImplOld" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.PNG", "org.eclipse.birt.chart.device.image.PngRendererImpl" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.GIF", "org.eclipse.birt.chart.device.image.GifRendererImpl" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.JPEG", "org.eclipse.birt.chart.device.image.JpegRendererImpl" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.JPG", "org.eclipse.birt.chart.device.image.JpegRendererImpl" //$NON-NLS-1$ //$NON-NLS-2$
			},
			{
					"dv.BMP", "org.eclipse.birt.chart.device.image.BmpRendererImpl" //$NON-NLS-1$ //$NON-NLS-2$
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
			}
	};

	/**
	 * All available aggregate functions used in orthogonal value aggregation
	 */
	private static final String[][] saAggregateFunctions = {
			{
					"Sum", "org.eclipse.birt.chart.aggregate.Sum" //$NON-NLS-1$ //$NON-NLS-2$
			}, {
					"Average", "org.eclipse.birt.chart.aggregate.Average" //$NON-NLS-1$ //$NON-NLS-2$
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
	 * Returns a singleton instance of the plugin settings framework
	 * 
	 * @return A singleton instance of the plugin settings framework
	 */
	public static synchronized PluginSettings instance( )
	{
		if ( ps == null )
		{
			ps = new PluginSettings( );
			ps.bStandalone = System.getProperty( "STANDALONE" ) != null; //$NON-NLS-1$
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
								}, Locale.getDefault( ) // LOCALE?
								) ); // i18n_CONCATENATIONS_REMOVED
				return (IDataSetProcessor) oDSP;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.dsp", //$NON-NLS-1$
							new Object[]{
								sFQClassName
							}, Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
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
									}, Locale.getDefault( ) // LOCALE?
									) ); // i18n_CONCATENATIONS_REMOVED
					return (IDataSetProcessor) newInstance( saDataSetProcessors[i] );
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.dsp", //$NON-NLS-1$ 
							new Object[]{
								sFQClassName
							}, Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
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
								Locale.getDefault( ) // LOCALE?
								) ); // i18n_CONCATENATIONS_REMOVED
				return (BaseRenderer) oSeriesRenderer;
			}
			logger.log( ILogger.ERROR,
					Messages.getString( "error.eclenv.cannot.find.series.renderer", //$NON-NLS-1$
							new Object[]{
								sFQClassName
							},
							Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
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
									Locale.getDefault( ) // LOCALE?
									) ); // i18n_CONCATENATIONS_REMOVED
					return (BaseRenderer) newInstance( saRenderers[i] );
				}
			}
			logger.log( ILogger.ERROR,
					Messages.getString( "error.stdenv.cannot.find.series.renderer", //$NON-NLS-1$
							new Object[]{
								sFQClassName
							},
							Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
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
	 *            dv.BMP and dv.GIF
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
								}, Locale.getDefault( ) // LOCALE?
								) ); // i18n_CONCATENATIONS_REMOVED
				return (IDeviceRenderer) oDeviceRenderer;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.device", //$NON-NLS-1$
							new Object[]{
								sName
							}, Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
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
									}, Locale.getDefault( ) // LOCALE?
									) ); // i18n_CONCATENATIONS_REMOVED
					return (IDeviceRenderer) newInstance( saDevices[i][1] );
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.device", //$NON-NLS-1$
							new Object[]{
								sName
							}, Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED //$NON-NLS-1$
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
								}, Locale.getDefault( ) // LOCALE?
								) ); // i18n_CONCATENATIONS_REMOVED
				return (IDisplayServer) oDisplayServer;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.display", //$NON-NLS-1$
							new Object[]{
								sName
							}, Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
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
									}, Locale.getDefault( ) // LOCALE?
									) ); // i18n_CONCATENATIONS_REMOVED
					return (IDisplayServer) newInstance( saDisplayServers[i][1] );
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.display", //$NON-NLS-1$
							new Object[]{
								sName
							}, Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
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
								}, Locale.getDefault( ) // LOCALE?
								) ); // i18n_CONCATENATIONS_REMOVED
				return (IAggregateFunction) oAggregateFunction;
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.eclenv.cannot.find.function", //$NON-NLS-1$
							new Object[]{
								sName
							}, Locale.getDefault( ) // LOCALE?
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
									Locale.getDefault( ) // LOCALE?
									) ); // i18n_CONCATENATIONS_REMOVED
					return (IAggregateFunction) newInstance( saAggregateFunctions[i][1] );
				}
			}
			logger.log( ILogger.FATAL,
					Messages.getString( "error.stdenv.cannot.find.function", //$NON-NLS-1$
							new Object[]{
								sName
							}, Locale.getDefault( ) // LOCALE?
							) ); // i18n_CONCATENATIONS_REMOVED
		}
		return null;
	}

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
	public final String[] getRegisteredSeries( )
	{
		return saSeries;
	}

	/**
	 * Returns a list of all aggregate functions registered via extension point
	 * implementations (or simulated)
	 * 
	 * @return A list of all aggregate functions registered via extension point
	 *         implementations (or simulated)
	 */
	public final String[] getRegisteredAggregateFunctions( )
	{
		final String[] saFunctions = new String[saAggregateFunctions.length];
		for ( int i = 0; i < saFunctions.length; i++ )
		{
			saFunctions[i] = saAggregateFunctions[i][0];
		}
		return saFunctions;
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

	// /**
	// * Attempts to walk through the schema tree as defined in an extension
	// point
	// * schema and retrieve the value for a given element name.
	// *
	// * @param sXsdListName
	// * @param sXsdComplexName
	// * @param sXsdElementName
	// * @param sXsdElementValue
	// * @param sLookupName
	// *
	// * @return The text value representation associated with the given element
	// * name
	// */
	// private static final String getPluginXmlValue( String sXsdListName,
	// String sXsdComplexName, String sXsdElementName,
	// String sXsdElementValue, String sLookupName ) throws ChartException
	// {
	// final IExtensionRegistry ier = Platform.getExtensionRegistry( );
	// final IExtensionPoint iep = ier.getExtensionPoint( PLUGIN, sXsdListName
	// );
	// if ( iep == null )
	// {
	// throw new ChartException( ChartException.PLUGIN,
	// "exception.cannot.find.plugin.entry", //$NON-NLS-1$
	// new Object[]{
	// sLookupName, sXsdElementName, sXsdElementValue
	// },
	// ResourceBundle.getBundle( Messages.ENGINE,
	// Locale.getDefault( ) ) ); // i18n_CONCATENATIONS_REMOVED
	// }
	// final IExtension[] iea = iep.getExtensions( );
	// IConfigurationElement[] icea;
	//
	// for ( int i = 0; i < iea.length; i++ )
	// {
	// icea = iea[i].getConfigurationElements( );
	// for ( int j = 0; j < icea.length; j++ )
	// {
	// if ( icea[j].getName( ).equals( sXsdComplexName ) )
	// {
	// if ( icea[j].getAttribute( sXsdElementName )
	// .equals( sLookupName ) )
	// {
	// return icea[j].getAttribute( sXsdElementValue );
	// }
	// }
	// }
	// }
	// return null;
	// }

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
					ResourceBundle.getBundle( Messages.ENGINE,
							Locale.getDefault( ) ) ); // i18n_CONCATENATIONS_REMOVED
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

}