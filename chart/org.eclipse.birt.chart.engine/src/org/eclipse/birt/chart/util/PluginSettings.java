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

import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.PluginException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;

/**
 * Provides a framework for locating extension implementations either
 * via the BIRT plugin.xml paradigm or via extensions that are explicitly
 * specified in this singleton instance.
 * 
 * If the BIRT extension paradigm is to be used, ensure that the BIRT_HOME
 * 'JVM system variable' points to a valid folder containing all chart
 * extensions within a plugins/ subfolder.
 * 
 * If the BIRT extension paradigm is not to be used and the default
 * classloader is requested for loading extensions, ensure that the
 * BIRT_HOME 'JVM system variable' is undefined.
 */
public final class PluginSettings
{
    /**
     * 
     */
    private static final String PLUGIN = "org.eclipse.birt.chart.engine";

    /**
     * All available series types for which extensions are defined.
     * Note that this list is index sensitive.
     */
    private static String[] saSeries =
    {
        "org.eclipse.birt.chart.model.component.impl.SeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.BarSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.LineSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.PieSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.StockSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl"
    };

    /**
     * All data set processor implementing class names for which
     * extensions are defined. Note that this list is index sensitive
     * and corresponds to the series type list.
     */
    private static String[] saDataSetProcessors =
    {
        "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl",
        "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl",
        "org.eclipse.birt.chart.datafeed.StockDataSetProcessorImpl",
        "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl",
    };

    /**
     * All series renderer implementing class names for which extensions
     * are defined. Note that this list is index sensitive and
     * corresponds to the series type list.
     */
    private static String[] saRenderers =
    {
        null, "org.eclipse.birt.chart.render.Bar", "org.eclipse.birt.chart.render.Line",
        "org.eclipse.birt.chart.render.Pie", "org.eclipse.birt.chart.render.Stock",
        "org.eclipse.birt.chart.render.Scatter"
    };

    /**
     * All available device renderers provided in the out-of-the-box distribution
     */
    private static String[][] saDevices =
    {
        {
            "dv.SWING", "org.eclipse.birt.chart.device.swing.SwingRendererImpl"
        },
        {
            "dv.SWT", "org.eclipse.birt.chart.device.swt.SwtRendererImpl"
        },
        {
            "dv.PNG24", "org.eclipse.birt.chart.device.image.PngRendererImplOld"
        },
        {
            "dv.GIF8", "org.eclipse.birt.chart.device.image.GifRendererImplOld"
        },
        {
            "dv.PNG", "org.eclipse.birt.chart.device.image.PngRendererImpl"
        },
        {
            "dv.GIF", "org.eclipse.birt.chart.device.image.GifRendererImpl"
        },
        {
            "dv.JPEG", "org.eclipse.birt.chart.device.image.JpegRendererImpl"
        },
        {
            "dv.JPG", "org.eclipse.birt.chart.device.image.JpegRendererImpl"
        },
        {
            "dv.BMP", "org.eclipse.birt.chart.device.image.BmpRendererImpl"
        }
    };

    /**
     * All available display servers provided in the out-of-the-box distribution
     */
    private static String[][] saDisplayServers =
    {
        {
            "ds.SWING", "org.eclipse.birt.chart.device.swing.SwingDisplayServer"
        },
        {
            "ds.SWT", "org.eclipse.birt.chart.device.swt.SwtDisplayServer"
        }
    };

    /**
     * A singleton instance created lazily when requested for
     */
    private static PluginSettings ps = null;
    
    /**
     * A boolean indicating if extensions are to be retrieved directly
     * and by bypassing the extension loading framework. 
     */
    private boolean bStandalone = false;

    /**
     * A non-instantiable constructor
     */
    private PluginSettings()
    {
    }

    /**
     * Returns a singleton instance of the plugin settings framework 
     * 
     * @return  A singleton instance of the plugin settings framework
     */
    public static synchronized PluginSettings instance()
    {
        if (ps == null)
        {
            ps = new PluginSettings();
            //ps.bStandalone = (System.getProperty("BIRT_HOME") == null);
        }
        return ps;
    }

    /**
     * Retrieves the first instance of a data set processor registered as
     * an extension for a given series type. 
     * 
     * @param   cSeries The Class instance associated with the given series type
     * 
     * @return  A newly created instance of a registered data set processor extension  
     * 
     * @throws PluginException
     */
    public final IDataSetProcessor getDataSetProcessor(Class cSeries) throws PluginException
    {
        final String sFQClassName = cSeries.getName();
        if (inEclipseEnv())
        {
            final Object oDSP = getPluginXmlObject("datasetprocessors", "datasetProcessor", "series", "processor",
                sFQClassName);
            if (oDSP != null)
            {
                DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                    "(ECLIPSE-ENV) Creating dsp {0}" + oDSP.getClass().getName()); // i18n_CONCATENATIONS_REMOVED
                return (IDataSetProcessor) oDSP;
            }
            DefaultLoggerImpl.instance()
                .log(ILogger.FATAL, "(ECLIPSE-ENV) Could not find dsp impl for {0}" + sFQClassName); // i18n_CONCATENATIONS_REMOVED
        }
        else
        {
            for (int i = 0; i < saSeries.length; i++)
            {
                if (sFQClassName.equals(saSeries[i]))
                {
                    DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                        "(STANDALONE-ENV) Creating dsp {0}" + saDataSetProcessors[i]); // i18n_CONCATENATIONS_REMOVED
                    return (IDataSetProcessor) newInstance(saDataSetProcessors[i]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(STANDALONE-ENV) Could not find dsp impl for {0}" + sFQClassName); // i18n_CONCATENATIONS_REMOVED
        }
        return null;
    }

    /**
     * Retrieves the first instance of a series renderer registered as
     * an extension for a given series type. 
     * 
     * @param   cSeries The Class instance associated with the given series type
     * 
     * @return  A newly created (and initialized) instance of a registered series renderer  
     * 
     * @throws PluginException
     */
    public final BaseRenderer getRenderer(Class cSeries) throws PluginException
    {
        final String sFQClassName = cSeries.getName();
        if (inEclipseEnv())
        {
            final Object oSeriesRenderer = getPluginXmlObject("modelrenderers", "modelRenderer", "series", "renderer",
                sFQClassName);
            if (oSeriesRenderer != null)
            {
                DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                    "(ECLIPSE-ENV) Creating series renderer {0}" + oSeriesRenderer.getClass().getName()); // i18n_CONCATENATIONS_REMOVED
                return (BaseRenderer) oSeriesRenderer;
            }
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "(ECLIPSE-ENV) Could not find series renderer impl for {0}" + sFQClassName); // i18n_CONCATENATIONS_REMOVED
        }
        else
        {
            for (int i = 0; i < saSeries.length; i++)
            {
                if (sFQClassName.equals(saSeries[i]))
                {
                    if (saRenderers[i] == null)
                    {
                        break;
                    }
                    DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                        "(STANDALONE-ENV) Creating series renderer {0}" + saRenderers[i]); // i18n_CONCATENATIONS_REMOVED
                    return (BaseRenderer) newInstance(saRenderers[i]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "(STANDALONE-ENV) Could not find series renderer impl for {0}" + sFQClassName); // i18n_CONCATENATIONS_REMOVED
        }
        return null;
    }

    /**
     * Retrieves the first instance of a device renderer registered as
     * an extension for a given name 
     * 
     * @param   sName   The name of the device renderer.
     *                  Values registered in the default distribution are
     *                  dv.SWT, dv.SWING, dv.PNG, dv.JPEG, dv.BMP and dv.GIF 
     * 
     * @return  An newly initialized instance of the requested device renderer
     * 
     * @throws PluginException
     */
    public final IDeviceRenderer getDevice(String sName) throws PluginException
    {
        if (inEclipseEnv())
        {
            final Object oDeviceRenderer = getPluginXmlObject("devicerenderers", "deviceRenderer", "name", "device",
                sName);
            if (oDeviceRenderer != null)
            {
                DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                    "(ECLIPSE-ENV) Creating device {0} as {1}" + sName + oDeviceRenderer.getClass().getName()); // i18n_CONCATENATIONS_REMOVED
                return (IDeviceRenderer) oDeviceRenderer;
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(ECLIPSE-ENV) Could not find device renderer impl for {0}" + sName); // i18n_CONCATENATIONS_REMOVED
        }
        else
        {
            for (int i = 0; i < saDevices.length; i++)
            {
                if (saDevices[i][0].equalsIgnoreCase(sName))
                {
                    DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                        "(STANDALONE-ENV) Creating device {0} as {1}" + sName + saDevices[i][1]); // i18n_CONCATENATIONS_REMOVED
                    return (IDeviceRenderer) newInstance(saDevices[i][1]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(STANDALONE-ENV) Could not find device renderer impl for {0}" + sName); // i18n_CONCATENATIONS_REMOVED
        }
        return null;
    }

    /**
     * Retrieves the first instance of a display server registered as
     * an extension for a given name 
     * 
     * @param   sName   The name of the display server.
     *                  Values registered in the default distribution are
     *                  ds.SWT, ds.SWING
     *                   
     * @return  An newly initialized instance of the requested display server
     * 
     * @throws PluginException
     */
    public final IDisplayServer getDisplayServer(String sName) throws PluginException
    {
        if (inEclipseEnv())
        {
            final Object oDisplayServer = getPluginXmlObject("displayservers", "displayserver", "name", "server", sName);
            if (oDisplayServer != null)
            {
                DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                    "(ECLIPSE-ENV) Creating display server {0} as {1}" + sName + oDisplayServer.getClass().getName()); // i18n_CONCATENATIONS_REMOVED
                return (IDisplayServer) oDisplayServer;
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(ECLIPSE-ENV) Could not find display server impl for {0}" + sName); // i18n_CONCATENATIONS_REMOVED
        }
        else
        {
            for (int i = 0; i < saDisplayServers.length; i++)
            {
                if (saDisplayServers[i][0].equalsIgnoreCase(sName))
                {
                    DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                        "(STANDALONE-ENV) Creating display server {0} as {1}" + sName + saDisplayServers[i][1]); // i18n_CONCATENATIONS_REMOVED
                    return (IDisplayServer) newInstance(saDisplayServers[i][1]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(STANDALONE-ENV) Could not find display server impl for {0}" + sName); // i18n_CONCATENATIONS_REMOVED
        }
        return null;
    }

    /**
     * Returns a list of all series registered via extension point implementations (or simulated)
     *  
     * @return A list of series registered via extension point implementations (or simulated)
     */
    public final String[] getRegisteredSeries()
    {
        return saSeries;
    }

    /**
     * Attempts to internally create an instance of a given class using reflection
     * using the default constructor.
     * 
     * @param   sFQClassName    The fully qualified class name for which a new instance is being requested
     * 
     * @return  A new instance of the requested class
     * 
     * @throws PluginException
     */
    private static final Object newInstance(String sFQClassName) throws PluginException
    {
        try
        {
            final Class c = Class.forName(sFQClassName);
            return c.newInstance();
        }
        catch (Exception ex )
        {
            throw new PluginException(ex);
        }
    }

    /**
     * Attempts to walk through the schema tree as defined in an extension
     * point schema and retrieve the value for a given element name.
     * 
     * @param sXsdListName
     * @param sXsdComplexName
     * @param sXsdElementName
     * @param sXsdElementValue
     * @param sLookupName
     * 
     * @return  The text value representation associated with the given element name
     */
    private static final String getPluginXmlValue(String sXsdListName, String sXsdComplexName, String sXsdElementName,
        String sXsdElementValue, String sLookupName) throws PluginException
    {
        final IExtensionRegistry ier = Platform.getExtensionRegistry();
        final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
        if (iep == null)
        {
            throw new PluginException("Unable to locate any entries for lookup={0}; element=({1}:{2}) in any plugin.xml file in all of the available plugins"  + sLookupName +  sXsdElementName + sXsdElementValue, null ); // i18n_CONCATENATIONS_REMOVED
        }
        final IExtension[] iea = iep.getExtensions();
        IConfigurationElement[] icea;

        for (int i = 0; i < iea.length; i++)
        {
            icea = iea[i].getConfigurationElements();
            for (int j = 0; j < icea.length; j++)
            {
                if (icea[j].getName().equals(sXsdComplexName))
                {
                    if (icea[j].getAttribute(sXsdElementName).equals(sLookupName))
                    {
                        return icea[j].getAttribute(sXsdElementValue);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Attempts to walk through the schema tree as defined in an extension
     * point schema and instantiate the class associated with the value for
     * a given element name.
     * 
     * @param sXsdListName
     * @param sXsdComplexName
     * @param sXsdElementName
     * @param sXsdElementValue
     * @param sLookupName
     * 
     * @return  An instance of the value class instantiated via the extension framework
     */
    private static final Object getPluginXmlObject(String sXsdListName, String sXsdComplexName, String sXsdElementName,
        String sXsdElementValue, String sLookupName) throws PluginException
    {
        final IExtensionRegistry ier = Platform.getExtensionRegistry();
        final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
        if (iep == null)
        {
            throw new PluginException("Unable to locate any entries for lookup={0}; element=({0}:{1}) in any plugin.xml file in all of the available plugins" + sLookupName + sXsdElementName + sXsdElementValue, null); // i18n_CONCATENATIONS_REMOVED
        }
        final IExtension[] iea = iep.getExtensions();
        IConfigurationElement[] icea;

        for (int i = 0; i < iea.length; i++)
        {
            icea = iea[i].getConfigurationElements();
            for (int j = 0; j < icea.length; j++)
            {
                if (icea[j].getName().equals(sXsdComplexName))
                {
                    if (icea[j].getAttribute(sXsdElementName).equals(sLookupName))
                    {
                        try
                        {
                            return icea[j].createExecutableExtension(sXsdElementValue);
                        }
                        catch (FrameworkException cex )
                        {
                            throw new PluginException(cex);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Performs an internal check to test if the environment uses the
     * extension loading framework or hardcoded classes as defined
     *  
     * @return  'true' if using the extension loading framework
     */
    private boolean inEclipseEnv()
    {
        if (bStandalone)
        {
            return false;
        }
        return (Platform.getExtensionRegistry() != null);
    }

}
