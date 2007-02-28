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

package org.eclipse.birt.report.engine.api;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;


/**
 * A report engine provides an entry point for reporting functionalities. It is
 * where the report generation and rendering process are globally customized. It
 * is also the place where engine statistics are collected. Through report
 * engine, reports can be generated and rendered to different output formats.
 * Queries can also be executed for preview purpose without involving a full
 * report generation.
 * <p>
 * Engine supports running different types of tasks. Example tasks include
 * running a report design to generate a report instance file, rendering a
 * report instance to output format, running a report directly to output,
 * running a dataset for preview, seaching a report, etc.
 */

public interface IReportEngine
{

	/**
	 * get the root scope used by the engine
	 * 
	 * @return
	 */
	public Object getRootScope( );

	/**
	 * Change the log level to newLevel
	 * 
	 * @param newLevel -
	 *            new log level
	 */
	public void changeLogLevel( Level newLevel );

	/**
	 * returns the engine configuration object
	 * 
	 * @return the engine configuration object
	 */
	public EngineConfig getConfig( );

	/**
	 * opens a report design file and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param designName
	 *            the full path of the report design file
	 * @return a report design runnable object
	 * @throws EngineException
	 *             throwed when the input file does not exist, or the file is
	 *             invalid
	 */
	public IReportRunnable openReportDesign( String designName )
			throws EngineException;
	
	public IReportRunnable openReportDesign( String designName,
			IResourceLocator locator ) throws EngineException;

	/**
	 * opens a report designHandle and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param designHandle
	 * @return a report design runnable object
	 * @throws EngineException
	 */
	public IReportRunnable openReportDesign( ReportDesignHandle designHandle )
			throws EngineException;

	/**
	 * opens a report design stream and creates a report design runnable. From
	 * the ReportRunnable object, embedded images and parameter definitions can
	 * be retrieved. Constructing an engine task requires a report design
	 * runnableobject.
	 * 
	 * @param designStream
	 *            the report design input stream
	 * @return a report design runnable object
	 * @throws EngineException
	 *             throwed when the input stream is null, or the stream does not
	 *             yield a valid report design
	 */
	public IReportRunnable openReportDesign( InputStream designStream )
			throws EngineException;

	public IReportRunnable openReportDesign( String name,
			InputStream designStream ) throws EngineException;

	public IReportRunnable openReportDesign( String name,
			InputStream designStream, IResourceLocator locator )
			throws EngineException;
	/**
	 * open the report design and return the runnable
	 * @param name system id of the report design.
	 * @param designStream stream of the report desgin.
	 * @param options options used to parse the design.
	 * @return
	 * @throws EngineException
	 * @see ModelOptions
	 */
	public IReportRunnable openReportDesign( String name,
			InputStream designStream, Map options )
			throws EngineException;
	
	/**
	 * creates an engine task for running and rendering report directly to
	 * output format
	 * 
	 * @param reportRunnable
	 *            the runnable report design object
	 * @return a run and render report task
	 */
	public IRunAndRenderTask createRunAndRenderTask(
			IReportRunnable reportRunnable );

	/**
	 * creates an engine task for obtaining report parameter definitions
	 * 
	 * @param reportRunnable
	 *            the runnable report design object
	 * @return a run and render report task
	 */
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(
			IReportRunnable reportRunnable );

	/**
	 * returns all supported output formats through BIRT engine emitter
	 * extensions
	 * 
	 * @return all supported output formats through BIRT engine emitter
	 *         extensions
	 */
	public String[] getSupportedFormats( );

	/**
	 * Return all the emitter information which BIRT Engine can load.
	 * 
	 * @return the emitter information
	 */
	public EmitterInfo[] getEmitterInfo( );

	/**
	 * the MIME type for the specific formatted supported by the extension.
	 * 
	 * @param format
	 *            the output format
	 * @param extensionID
	 *            the extension ID, which could be null if only one plugin
	 *            supports the output format
	 * @return the MIME type for the specific formatted supported by the
	 *         extension.
	 */
	public String getMIMEType( String format );

	/**
	 * shut down the engine, release all the resources.
	 */
	public void destroy( );

	/**
	 * creates a task to run a report to generate a report document
	 * 
	 * @param reportRunnable
	 *            the runnable report design object
	 * @return a task that runs the report
	 */
	public IRunTask createRunTask( IReportRunnable reportRunnable );

	/**
	 * creates a task that renders the report to a specific output format.
	 * 
	 * @param reportDocument
	 *            a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	public IRenderTask createRenderTask( IReportDocument reportDocument );

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param fileName
	 *            the report document name. report document is an archive in
	 *            BIRT.
	 * @return A handle to the report document
	 * @throws EngineException
	 *             throwed when the report document archive does not exist, or
	 *             the file is not a valud report document
	 */
	public IReportDocument openReportDocument( String fileName )
			throws EngineException;

	public IReportDocument openReportDocument( String fileName,
			IResourceLocator locator ) throws EngineException;
	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param systemId
	 *            the system id the opend document. It is used to access the resources with
	 *            relative path in the report document. If it is NULL, a saved one is used.
	 * @param fileName
	 *            the report document name. report document is an archive in
	 *            BIRT.
	 * @return A handle to the report document
	 * @throws EngineException
	 *             throwed when the report document archive does not exist, or
	 *             the file is not a valud report document
	 */
	public IReportDocument openReportDocument( String systemId,
			String fileName ) throws EngineException;

	public IReportDocument openReportDocument( String systemId,
			String fileName, IResourceLocator locator ) throws EngineException;

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param systemId
	 *            the system id the opend document. It is used to access the resources with
	 *            relative path in the report document. If it is NULL, a saved one is used.
	 * @param fileName
	 *            the report document name. report document is an archive in
	 *            BIRT.
	 * @param options
	 * 				Map defins the options used to parse the design file.
	 * @return A handle to the report document
	 * @throws EngineException
	 *             throwed when the report document archive does not exist, or
	 *             the file is not a valud report document
	 */
	public IReportDocument openReportDocument( String systemId,
			String fileName, Map options ) throws EngineException;
	
	public IReportDocument openReportDocument( String systemId,
			IDocArchiveReader archiveReader, Map options )
			throws EngineException;
	/**
	 * creates a task that allows data extraction from a report document
	 * 
	 * @param reportDocument
	 *            a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	public IDataExtractionTask createDataExtractionTask(
			IReportDocument reportDocument );

	/**
	 * shut down the engine, release all the resources.
	 * 
	 * @deprecated
	 */
	public void shutdown( );
}
