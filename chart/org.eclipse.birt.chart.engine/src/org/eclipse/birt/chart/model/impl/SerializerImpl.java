/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.model.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.component.ChartPreferences;
import org.eclipse.birt.chart.model.util.ModelResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * @author Actuate Corporation
 */
public class SerializerImpl implements Serializer
{

    public static final String CHART_START_MARKER = "<!-- Chart Starts Here. -->";

    public static final String CHART_END_MARKER = "<!-- Chart Ends Here. -->";

    private static Serializer sz = null;

    static
    {
        EPackage.Registry.INSTANCE.put("http://birt.eclipse.org/ChartModel", ModelFactory.eINSTANCE);
    }

    /**
     * Cannot invoke constructor; use instance() instead
     */
    private SerializerImpl()
    {

    }

    /**
     * 
     * @return A singleton instance of the chart serializer
     */
    public static synchronized final Serializer instance()
    {
        if (sz == null)
        {
            sz = new SerializerImpl();
        }
        return sz;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.ISerialization#write(org.eclipse.birt.chart.model.Chart, java.io.OutputStream)
     */
    public void write(Chart cModel, OutputStream os) throws IOException
    {
        // REMOVE ANY TRANSIENT RUNTIME SERIES
        cModel.clearSections(IConstants.RUN_TIME);
        
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = rsChart.createResource(URI.createFileURI("test.chart"));

        // Add the chart to the resource
        rChart.getContents().add(cModel);

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");

        // Save the resource to disk
        rChart.save(os, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.ISerialization#write(org.eclipse.birt.chart.model.Chart,
     *      org.eclipse.emf.common.util.URI)
     */
    public void write(Chart cModel, URI uri) throws IOException
    {
        // REMOVE ANY TRANSIENT RUNTIME SERIES
        cModel.clearSections(IConstants.RUN_TIME);
        
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = null;

        // Create resources to represent the disk files to be used to store the
        // models
        rChart = rsChart.createResource(uri);

        // Add the chart to the resource
        rChart.getContents().add(cModel);

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");

        // Save the resource to disk
        rChart.save(options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.ISerialization#asXml(org.eclipse.birt.chart.model.Chart, boolean)
     */
    public ByteArrayOutputStream asXml(Chart cModel, boolean bStripHeaders) throws IOException
    {
        // REMOVE ANY TRANSIENT RUNTIME SERIES
        cModel.clearSections(IConstants.RUN_TIME);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = rsChart.createResource(URI.createFileURI("test.chart"));

        // Add the chart to the resource
        rChart.getContents().add(cModel);

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");
        if (bStripHeaders)
        {
            options.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
        }

        // Save the resource to disk
        rChart.save(baos, options);
        return baos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.Serializer#savePreferences(org.eclipse.birt.chart.model.component.ChartPreferences,
     *      java.io.OutputStream)
     */
    public void savePreferences(ChartPreferences preferences, OutputStream os) throws IOException
    {
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = rsChart.createResource(URI.createFileURI("test.chart"));

        // Add the chart to the resource
        rChart.getContents().add(preferences);

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");

        // Save the resource to disk
        rChart.save(os, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.ISerialization#read(java.io.InputStream)
     */
    public Chart read(InputStream is) throws IOException
    {
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = rsChart.createResource(URI.createFileURI("test.chart"));

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");

        rChart.load(is, options);
        return (Chart) rChart.getContents().get(0);
    }

    private StringBuffer getChartStringFromStream(InputStream is)
    {
        StringBuffer sbChart = new StringBuffer("");
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            boolean bChartStarted = false;
            while (true)
            {
                String sTmp = reader.readLine();
                if (sTmp == null)
                {
                    break;
                }
                if (sTmp.startsWith("<?")) // For encoding info.
                {
                    sbChart.append(sTmp);
                    sbChart.append("\n");
                }
                if (sTmp.equals(CHART_START_MARKER))
                {
                    bChartStarted = true;
                    continue;
                }
                if (bChartStarted)
                {
                    if (!sTmp.equals(CHART_END_MARKER))
                    {
                        sbChart.append(sTmp);
                        sbChart.append("\n");
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        catch (UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        catch (IOException e )
        {
            e.printStackTrace();
        }
        if (sbChart.length() > 0)
        {
            // Remove the last newline added in loop above.
            sbChart.deleteCharAt(sbChart.length() - 1);
        }
        return sbChart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.ISerialization#read(org.eclipse.emf.common.util.URI)
     */
    public Chart read(URI uri) throws IOException
    {
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = null;

        rChart = rsChart.createResource(uri);

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");

        rChart.load(options);
        return (Chart) rChart.getContents().get(0);
    }

    public Chart readEmbedded(URI uri) throws IOException
    {
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = null;

        StringBuffer sb = null;
        sb = getChartStringFromStream(new FileInputStream(uri.toFileString()));

        File fTmp = File.createTempFile("_ChartResource", ".chart");
        FileWriter writer = new FileWriter(fTmp);
        System.out.println(fTmp.getAbsolutePath());
        writer.write(sb.toString());
        writer.flush();
        writer.close();
        URI uriEmbeddedModel = URI.createFileURI(fTmp.getAbsolutePath());
        rChart = rsChart.getResource(uriEmbeddedModel, true);

        rChart.load(Collections.EMPTY_MAP);

        // Delete the temporary file once the model is loaded.
        if (fTmp.exists())
        {
            fTmp.delete();
        }
        return (Chart) rChart.getContents().get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.ISerialization#fromXml(byte[], boolean)
     */
    public Chart fromXml(ByteArrayInputStream byais, boolean bStripHeaders) throws IOException
    {
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = rsChart.createResource(URI.createFileURI("test.chart"));

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");
        if (bStripHeaders)
        {
            options.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
        }
        rChart.load(byais, options);
        return (Chart) rChart.getContents().get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.Serializer#loadPreferences(java.io.InputStream)
     */
    public ChartPreferences loadPreferences(InputStream is) throws IOException
    {
        // Create and setup local ResourceSet
        ResourceSet rsChart = new ResourceSetImpl();
        rsChart.getResourceFactoryRegistry().getExtensionToFactoryMap().put("chart", new ModelResourceFactoryImpl());

        // Create resources to represent the disk files to be used to store the
        // models
        Resource rChart = rsChart.createResource(URI.createFileURI("test.chart"));

        Map options = new HashMap();
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");

        rChart.load(is, options);
        return (ChartPreferences) rChart.getContents().get(0);
    }
}