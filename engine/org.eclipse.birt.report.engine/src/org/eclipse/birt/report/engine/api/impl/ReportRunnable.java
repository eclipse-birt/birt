/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

/**
 * Engine implementation of IReportRunnable interface
 */
public class ReportRunnable implements IReportRunnable
{
	/**
	 * the report
	 */
	protected Report report;

	/**
	 * report file name
	 */
	protected String reportName;
	
	/**
	 * reference to report engine
	 */
	protected ReportEngine engine = null;

	/**
	 * constructor
	 * 
	 * @param report reference to report
	 */
	ReportRunnable(Report report)
	{
		this.report = report;
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
	public Report getReport()
	{
		return report;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getImage(java.lang.String)
	 */
	public IImage getImage(String name)
	{
		EmbeddedImage embeddedImage = report.getReportDesign()
				.findImage(name);

		if (embeddedImage != null)
		{
			Image image = new Image(embeddedImage.getData(), name);
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
		return report.getReportDesign().handle();
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
		ArrayList params = report.getParameters(includeParameterGroups);
		return params;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IReportRunnable#getTestConfig()
	 */
	public HashMap getTestConfig()
	{
		return report.getConfigs();
	}

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.engine.api.IReportRunnable#getReportEngine()
     */
    public ReportEngine getReportEngine() {
        return engine;
    }

    /**
     * @param engine The engine to set.
     */
    public void setReportEngine(ReportEngine engine) {
        this.engine = engine;
    }
}