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
 * This class provides default plugin values for testing the 'engine plugin' in a non-plugin environment
 * 
 * @author Actuate Corporation
 */
public final class PluginSettings
{
    private static final String PLUGIN = "org.eclipse.birt.chart.engine";

    private static String[] saSeries =
    {
        "org.eclipse.birt.chart.model.component.impl.SeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.BarSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.LineSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.PieSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.StockSeriesImpl",
        "org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl"
    };

    private static String[] saDataSetProcessors =
    {
        "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl",
        "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl", "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl",
        "org.eclipse.birt.chart.datafeed.StockDataSetProcessorImpl",
        "org.eclipse.birt.chart.datafeed.DataSetProcessorImpl",
    };

    private static String[] saRenderers =
    {
        null, "org.eclipse.birt.chart.render.Bar", "org.eclipse.birt.chart.render.Line",
        "org.eclipse.birt.chart.render.Pie", "org.eclipse.birt.chart.render.Stock",
        "org.eclipse.birt.chart.render.Scatter"
    };

    private static String[][] saDevices =
    {
        {
            "dv.SWING", "org.eclipse.birt.chart.device.swing.SwingRendererImpl"
        },
        {
            "dv.SWT", "org.eclipse.birt.chart.device.swt.SwtRendererImpl"
        },
        {
            "dv.PNG24", "org.eclipse.birt.chart.device.image.PngRendererImpl"
        },
        {
            "dv.GIF8", "org.eclipse.birt.chart.device.image.GifRendererImpl"
        }
    };

    private static String[][] saXServers =
    {
        {
            "ds.SWING", "org.eclipse.birt.chart.device.swing.SwingDisplayServer"
        },
        {
            "ds.SWT", "org.eclipse.birt.chart.device.swt.SwtDisplayServer"
        }
    };

    /**
     *  
     */
    private static PluginSettings ps = null;

    /**
     *  
     */
    private PluginSettings()
    {
    }

    /**
     * Returns a singleton instance
     * 
     * @return
     */
    public static synchronized PluginSettings instance()
    {
        if (ps == null)
        {
            ps = new PluginSettings();
        }
        return ps;
    }

    /**
     * 
     * @param cSeries
     * 
     * @return
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
                    "(ECLIPSE-ENV) Creating dsp " + oDSP.getClass().getName());
                return (IDataSetProcessor) oDSP;
            }
            DefaultLoggerImpl.instance()
                .log(ILogger.FATAL, "(ECLIPSE-ENV) Could not find dsp impl for " + sFQClassName);
        }
        else
        {
            for (int i = 0; i < saSeries.length; i++)
            {
                if (sFQClassName.equals(saSeries[i]))
                {
                    DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                        "(STANDALONE-ENV) Creating dsp " + saDataSetProcessors[i]);
                    return (IDataSetProcessor) newInstance(saDataSetProcessors[i]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(STANDALONE-ENV) Could not find dsp impl for " + sFQClassName);
        }
        return null;
    }

    /**
     * @param cSeries
     * 
     * @return
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
                    "(ECLIPSE-ENV) Creating series renderer " + oSeriesRenderer.getClass().getName());
                return (BaseRenderer) oSeriesRenderer;
            }
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "(ECLIPSE-ENV) Could not find series renderer impl for " + sFQClassName);
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
                        "(STANDALONE-ENV) Creating series renderer " + saRenderers[i]);
                    return (BaseRenderer) newInstance(saRenderers[i]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.ERROR,
                "(STANDALONE-ENV) Could not find series renderer impl for " + sFQClassName);
        }
        return null;
    }

    /**
     * 
     * @param sName
     * @return
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
                    "(ECLIPSE-ENV) Creating device " + sName + " as " + oDeviceRenderer.getClass().getName());
                return (IDeviceRenderer) oDeviceRenderer;
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(ECLIPSE-ENV) Could not find device renderer impl for " + sName);
        }
        else
        {
            for (int i = 0; i < saDevices.length; i++)
            {
                if (saDevices[i][0].equalsIgnoreCase(sName))
                {
                    DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                        "(STANDALONE-ENV) Creating device " + sName + " as " + saDevices[i][1]);
                    return (IDeviceRenderer) newInstance(saDevices[i][1]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(STANDALONE-ENV) Could not find device renderer impl for " + sName);
        }
        return null;
    }

    /**
     * 
     * @param sName
     * @return
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
                    "(ECLIPSE-ENV) Creating display server " + sName + " as " + oDisplayServer.getClass().getName());
                return (IDisplayServer) oDisplayServer;
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(ECLIPSE-ENV) Could not find display server impl for " + sName);
        }
        else
        {
            for (int i = 0; i < saXServers.length; i++)
            {
                if (saXServers[i][0].equalsIgnoreCase(sName))
                {
                    DefaultLoggerImpl.instance().log(ILogger.INFORMATION,
                        "(STANDALONE-ENV) Creating display server " + sName + " as " + saXServers[i][1]);
                    return (IDisplayServer) newInstance(saXServers[i][1]);
                }
            }
            DefaultLoggerImpl.instance().log(ILogger.FATAL,
                "(STANDALONE-ENV) Could not find display server impl for " + sName);
        }
        return null;
    }

    /**
     * 
     * @return A list of series registered via extension points (or simulated)
     */
    public final String[] getRegisteredSeries()
    {
        return saSeries;
    }

    /**
     * 
     * @param sFQClassName
     * 
     * @return
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
     * 
     * @param sXsdListName
     * @param sXsdComplexName
     * @param sXsdElementName
     * @param sXsdElementValue
     * @param sLookupName
     * 
     * @return
     */
    private static final String getPluginXmlValue(String sXsdListName, String sXsdComplexName, String sXsdElementName,
        String sXsdElementValue, String sLookupName) throws PluginException
    {
        final IExtensionRegistry ier = Platform.getExtensionRegistry();
        final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
        if (iep == null)
        {
            throw new PluginException("Unable to locate any entries for lookup=" + sLookupName + "; element=("
                + sXsdElementName + ":" + sXsdElementValue + ") in any plugin.xml file in all of the available plugins");
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
     * 
     * @param sXsdListName
     * @param sXsdComplexName
     * @param sXsdElementName
     * @param sXsdElementValue
     * @param sLookupName
     * 
     * @return
     */
    private static final Object getPluginXmlObject(String sXsdListName, String sXsdComplexName, String sXsdElementName,
        String sXsdElementValue, String sLookupName) throws PluginException
    {
        final IExtensionRegistry ier = Platform.getExtensionRegistry();
        final IExtensionPoint iep = ier.getExtensionPoint(PLUGIN, sXsdListName);
        if (iep == null)
        {
            throw new PluginException("Unable to locate any entries for lookup=" + sLookupName + "; element=("
                + sXsdElementName + ":" + sXsdElementValue + ") in any plugin.xml file in all of the available plugins");
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
     * 
     * @return
     */
    private boolean inEclipseEnv()
    {
        return (Platform.getExtensionRegistry() != null);
    }

}