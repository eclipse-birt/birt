/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.script.internal.element.ReportDesign;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

/**
 * Engine implementation of IReportRunnable interface
 */
public class ReportRunnable implements IReportRunnable
{
	/**
	 * the report
	 */
	protected ReportDesignHandle designHandle;
	
	/**
	 * the cached engine IR object
	 */
	protected Report reportIR;

	/**
	 * report file name
	 */
	protected String reportName;
	
	/**
	 * reference to report engine
	 */
	protected IReportEngine engine = null;

	/**
	 * constructor
	 * 
	 * @param report reference to report
	 */
	ReportRunnable(ReportDesignHandle designHandle)
	{
		this.designHandle = designHandle;
	}

	/**
	 * @param name report file name
	 */
	public void setReportName(String name)
	{
		this.reportName = name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getReportName()
	 */
	public String getReportName()
	{
		return this.reportName;
	}

	/**
	 * @return reference to the report object
	 */
	public ReportDesignHandle getReport()
	{
		return designHandle;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getImage(java.lang.String)
	 */
	public IImage getImage(String name)
	{
		EmbeddedImage embeddedImage = designHandle.findImage(name);

		if (embeddedImage != null)
		{
			Image image = new Image(embeddedImage.getData(designHandle.getModule()), name);
			image.setReportRunnable(this);
			
			return image;
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getProperty(java.lang.String)
	 */
	public Object getProperty(String propertyName)
	{
		FactoryPropertyHandle handle = getDesignHandle().getFactoryPropertyHandle(propertyName);
		if(handle!=null)
			return handle.getStringValue();
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getProperty(java.lang.String, java.lang.String)
	 */
	public Object getProperty(String path, String propertyName)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getDesignHandle()
	 */
	public DesignElementHandle getDesignHandle()
	{
		return designHandle;
	}
	
	/**
	 * return report parameter definitions defined in report design
	 * 
	 * @param includeParameterGroups
	 *                whether returns one level of parameters with parameter
	 *                groups or return a flatten collection of all
	 *                parameters
	 * @return if includeParameterGroups = true, an ordered collection of
	 *         report parameters. Each item in the colleciton is of type
	 *         IParameterDefnBase if includeParameterGroups = false, a
	 *         collection of parameters. Each item in the collection is of
	 *         type IParameterDefn.
	 */
	public Collection getParameterDefns(boolean includeParameterGroups)
	{
		ArrayList params = new ReportParser( engine ).getParameters( designHandle,
				includeParameterGroups );
		return params;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getTestConfig()
	 */
	public HashMap getTestConfig()
	{
		HashMap configs = new HashMap( );
		Iterator iter = designHandle.configVariablesIterator( );
		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				ConfigVariableHandle handle = (ConfigVariableHandle) iter
						.next( );
				String name = handle.getName( );
				String value = handle.getValue( );
				configs.put( name, value );
			}
		}
		return configs;
	}

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.api.IReportRunnable#getReportEngine()
     */
    public IReportEngine getReportEngine() {
        return engine;
    }

    /**
     * @param engine The engine to set.
     */
    public void setReportEngine(IReportEngine engine) {
        this.engine = engine;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#setDesignHandle(org.eclipse.birt.report.model.api.ReportDesignHandle)
	 */
	public void setDesignHandle(ReportDesignHandle handle) {
		this.designHandle = handle;
		this.reportIR = null;
	}
	
	/**
	 * Returns the report design
	 * 
	 * @return the report design
	 */
	
	public IReportDesign getDesignInstance()
	{
		ReportDesign design = new ReportDesign( designHandle );
		return design;
	}
	
	public Report getReportIR( )
	{
		if (reportIR != null)
		{
			return reportIR;
		}
		synchronized ( this )
		{
			if ( reportIR != null )
			{
				return reportIR;
			}
			reportIR = new ReportParser( ).parse( designHandle );
		}
		return reportIR;
	}
	
	public void setReportIR( Report reportIR )
	{
		this.reportIR = reportIR;
	}
}