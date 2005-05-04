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

import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IReportRunnable;
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

	protected String reportName;
	
	protected HashMap configs;

	ReportRunnable(Report report)
	{
		this.report = report;
	}

	public void setReportName(String name)
	{
		this.reportName = name;
	}

	public String getReportName()
	{
		return this.reportName;
	}
	/**
	 * @return
	 */
	public Report getReport()
	{
		return report;
	}


	/**
	 * returns an image stored in a report design file. null if the image
	 * name does not exist
	 * 
	 * @param name
	 *                the image name for the embedded image
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

	/**
	 * returns the property value for things like report description, title,
	 * etc.
	 * 
	 * @param propertyName
	 *                the name of the property
	 * @return the property value for things like report description, title,
	 *         etc.
	 */
	public Object getProperty(String propertyName)
	{
		FactoryPropertyHandle handle = getDesignHandle().getFactoryPropertyHandle(propertyName);
		if(handle!=null)
		{
			return handle.getStringValue();
		}
		return null;
		
		
	}

	/**
	 * returns the property value defined on a components in a report
	 * design. For example, getProperty("/dataSets/dsName", "url") wil
	 * return the url value for a data set with name dsName.
	 * 
	 * @param propertyName
	 *                the name of the property
	 * @param path
	 *                a simplified XPath that allows access to properties
	 *                for components in a report design. Only downward path
	 *                is allowed, i.e., no .. in the path.
	 * @return the property value for things like report description, title,
	 *         etc.
	 */
	public Object getProperty(String path, String propertyName)
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IReportRunnable#getDesignHandle()
	 */
	public DesignElementHandle getDesignHandle()
	{
		return report.getReportDesign().handle();
	}
	
	/**
	 * return report parameter definitions
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

	/**
	 * @return Returns the configs.
	 */
	public HashMap getTestConfig()
	{
		return report.getConfigs();
	}

}