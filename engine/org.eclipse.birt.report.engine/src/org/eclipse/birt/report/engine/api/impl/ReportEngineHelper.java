/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * a helper class that does most of the dirty work for report engine
 */
public class ReportEngineHelper 
{
	
	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger( ReportEngineHelper.class.getName() );
	
	/**
	 * reference the the public report engine object
	 */
	private ReportEngine engine;
	
	/**
	 * extension manager
	 */
	private ExtensionManager extensionMgr;
	
	/**
	 * constructor
	 * 
	 * @param engine the report engine
	 */
	public ReportEngineHelper(ReportEngine engine)
	{
		this.engine = engine;
		
		extensionMgr = ExtensionManager.getInstance();
		
		
	}
	
	/**
	 * opens a report design file and creates a report design runnable. From the ReportRunnable
	 * object, embedded images and parameter definitions can be retrieved. Constructing
	 * an engine task requires a report design runnable object.  
	 * 
	 * @param designName the full path of the report design file
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input file does not exist, or the 
	 * file is invalid
	 */
	public IReportRunnable openReportDesign(String designName) throws EngineException
	{
		Report report;
		File file = new File(designName);
		if(!file.exists())
		{
			logger.log(Level.SEVERE, "{0} not found!", designName); //$NON-NLS-1$
			throw new EngineException(MessageConstants.FILE_NOT_FOUND_EXCEPTION, designName);
		}
		
		try 
		{
			report = new ReportParser( ).parse(designName);
		} 
		catch (DesignFileException e) 
		{
			logger.log(Level.SEVERE, "invalid design file {0}", designName); //$NON-NLS-1$
			throw new EngineException(MessageConstants.INVALID_DESIGNFILE_EXCEPTION, designName, e);
		}
		assert(report != null);
		ReportRunnable runnable = new ReportRunnable(report);
		runnable.setReportName(designName);
		return runnable;
	}
	
	/**
	 * opens a report design stream and creates a report design runnable. From the ReportRunnable
	 * object, embedded images and parameter definitions can be retrieved. Constructing
	 * an engine task requires a report design runnableobject. 
	 * 
	 * @param designStream the report design input stream  
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the 
	 * stream does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(InputStream designStream) throws EngineException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * creates a report design runnable based on a report design handle. From the 
	 * ReportRunnable object, embedded images and parameter definitions can be retrieved. 
	 * Constructing an engine task requires a report design runnable object. 
	 * 
	 * @param designStream the report design input stream  
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the 
	 * stream does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(DesignElementHandle designHandle) throws EngineException
	{
		assert (designHandle instanceof ReportDesignHandle);
		Report report = new ReportParser( ).parse((ReportDesignHandle)designHandle);
				
		assert(report != null);
		ReportRunnable ret = new ReportRunnable(report);
		ret.setReportName(((ReportDesignHandle)designHandle).getFileName());
		return ret;
	}
	
	/**
	 * creates an engine task for running and rendering report directly to
	 * output format 
	 * 
	 * @param reportRunnable the runnable report design object
	 * @return a run and render report task 
	 */
	public IRunAndRenderTask createRunAndRenderTask(IReportRunnable reportRunnable)
	{
		return new RunAndRenderTask(engine, reportRunnable);
	}

	
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(IReportRunnable reportRunnable)
	{
		return new GetParameterDefinitionTask(engine, reportRunnable);
	}
	
	/**
	 * returns all supported output formats through BIRT engine emitter extensions
	 * 
	 * @return all supported output formats through BIRT engine emitter extensions  
	 */
	public String[] getSupportedFormats()
	{
		HashMap emitterMap = extensionMgr.getEmitterExtensions();
		return (String[])emitterMap.keySet().toArray();
	}

	/**
	 * returns a list of strings that describes the supported option names
	 * 
	 * @param format the output format
	 * @param extensionID the extension ID, which could be null if only one plugin supports
	 * the output format
	 * @return a list of strings that describes the supported option names
	 */
	public String[] getSupportedOptions(String format) {
		return extensionMgr.getOptions(format);
	}

	/**
	 * the MIME type for the specific formatted supported by the extension. 
	 * 
	 * @param format the output format
	 * @param extensionID the extension ID, which could be null if only one plugin supports
	 * the output format
	 * @return the MIME type for the specific formatted supported by the extension. 
	 */
	public String getMIMEType(String format) {
		return extensionMgr.getMIMEType(format);
	}
}
