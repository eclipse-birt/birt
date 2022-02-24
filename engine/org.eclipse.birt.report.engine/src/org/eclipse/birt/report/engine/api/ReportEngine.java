/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * This is a wrapper class for the IReportEngine. The new user should use the
 * IReportEngineFactory to create the IReportEngine instead of use this class
 * directly.
 * 
 * @see org.eclipes.birt.report.engine.api.ReportRunner
 */

public class ReportEngine implements IReportEngine {
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(IReportEngine.class.getName());

	/**
	 * the report engine.
	 */
	protected IReportEngine engine;

	/**
	 * Constructor. If config is null, engine derives BIRT_HOME from the location of
	 * the engine jar file, and derives data driver directory as $BIRT_HOME/drivers.
	 * For a simple report with no images and links, engine will run without
	 * complaining. If the report has image/chart defined, the engine has to be
	 * configured with relevant image and chart handlers.
	 * 
	 * @param config an engine configuration object used to configure the engine
	 */
	public ReportEngine(EngineConfig config) {
		try {
			Platform.startup(config);
		} catch (BirtException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}

		Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		if (factory instanceof IReportEngineFactory) {
			engine = ((IReportEngineFactory) factory).createReportEngine(config);
		}
		if (engine == null) {
			System.out.println("Can not load the report engine");
		}
	}

	/**
	 * get the root scope used by the engine
	 * 
	 * @return
	 */
	public Object getRootScope() {
		return engine.getRootScope();
	}

	/**
	 * Change the log level to newLevel
	 * 
	 * @param newLevel - new log level
	 */
	public void changeLogLevel(Level newLevel) {
		engine.changeLogLevel(newLevel);
	}

	/**
	 * returns the engine configuration object
	 * 
	 * @return the engine configuration object
	 */
	public EngineConfig getConfig() {
		return engine.getConfig();
	}

	/**
	 * opens a report design file and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param designName the full path of the report design file
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input file does not exist, or the
	 *                         file is invalid
	 */
	public IReportRunnable openReportDesign(String designName) throws EngineException {
		return engine.openReportDesign(designName);
	}

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
	public IReportRunnable openReportDesign(ReportDesignHandle designHandle) throws EngineException {
		return engine.openReportDesign(designHandle);
	}

	/**
	 * opens a report design stream and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design
	 * runnableobject.
	 * 
	 * @param designStream the report design input stream
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the stream
	 *                         does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(InputStream designStream) throws EngineException {
		return engine.openReportDesign(designStream);
	}

	/**
	 * opens a report design stream and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param name         system id of the report design
	 * @param designStream input stream of the report design
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the stream
	 *                         does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(String name, InputStream designStream) throws EngineException {
		return engine.openReportDesign(name, designStream);
	}

	/**
	 * creates an engine task for running and rendering report directly to output
	 * format
	 * 
	 * @param reportRunnable the runnable report design object
	 * @return a run and render report task
	 */
	public IRunAndRenderTask createRunAndRenderTask(IReportRunnable reportRunnable) {
		return engine.createRunAndRenderTask(reportRunnable);
	}

	/**
	 * creates an engine task for obtaining report parameter definitions
	 * 
	 * @param reportRunnable the runnable report design object
	 * @return a run and render report task
	 */
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(IRunnable reportRunnable) {
		return engine.createGetParameterDefinitionTask(reportRunnable);
	}

	/**
	 * creates an engine task for obtaining report parameter definitions
	 * 
	 * @param reportRunnable the runnable report design object
	 * @return a GetParameterDefinitionTask
	 */
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(IReportRunnable reportRunnable) {
		return engine.createGetParameterDefinitionTask(reportRunnable);
	}

	/**
	 * returns all supported output formats through BIRT engine emitter extensions
	 * 
	 * @return all supported output formats through BIRT engine emitter extensions
	 */
	public String[] getSupportedFormats() {
		return engine.getSupportedFormats();
	}

	/**
	 * Return all the emitter information which BIRT Engine can load.
	 * 
	 * @return the emitter information
	 */
	public EmitterInfo[] getEmitterInfo() {
		return engine.getEmitterInfo();
	}

	/**
	 * the MIME type for the specific formatted supported by the extension.
	 * 
	 * @param format      the output format
	 * @param extensionID the extension ID, which could be null if only one plugin
	 *                    supports the output format
	 * @return the MIME type for the specific formatted supported by the extension.
	 */
	public String getMIMEType(String format) {
		return engine.getMIMEType(format);
	}

	/**
	 * shuts down the report engine
	 */
	public void destroy() {
		if (engine != null) {
			engine.destroy();
			engine = null;
		}
	}

	/**
	 * creates a task to run a report to generate a report document
	 * 
	 * @param reportRunnable the runnable report design object
	 * @return a task that runs the report
	 */
	public IRunTask createRunTask(IReportRunnable reportRunnable) {
		return engine.createRunTask(reportRunnable);
	}

	/**
	 * creates a task that renders the report to a specific output format.
	 * 
	 * @param reportDocument a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	public IRenderTask createRenderTask(IReportDocument reportDocument) {
		return engine.createRenderTask(reportDocument);
	}

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param fileName the report document name. report document is an archive in
	 *                 BIRT.
	 * @return A handle to the report document
	 * @throws EngineException throwed when the report document archive does not
	 *                         exist, or the file is not a valud report document
	 */
	public IReportDocument openReportDocument(String fileName) throws EngineException {
		return engine.openReportDocument(fileName);
	}

	/**
	 * creates a task that allows data extraction from a report document
	 * 
	 * @param reportDocument a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	public IDataExtractionTask createDataExtractionTask(IReportDocument reportDocument) {
		return engine.createDataExtractionTask(reportDocument);
	}

	/**
	 * shut down the engine, release all the resources.
	 * 
	 * @deprecated
	 */
	public void shutdown() {
		engine.shutdown();
	}

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param systemId the system id the opend document. It is used to access the
	 *                 resources with relative path in the report document. If it is
	 *                 NULL, a saved one is used.
	 * @param fileName the report document name. report document is an archive in
	 *                 BIRT.
	 * @return A handle to the report document
	 * @throws EngineException throwed when the report document archive does not
	 *                         exist, or the file is not a valid report document
	 */
	public IReportDocument openReportDocument(String systemId, String fileName) throws EngineException {
		return engine.openReportDocument(systemId, fileName);
	}

	/**
	 * opens a report design file and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param designName the full path of the report design file
	 * @param locator    the resource locator used to locate files referenced in the
	 *                   design
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input file does not exist, or the
	 *                         file is invalid
	 */
	public IReportRunnable openReportDesign(String designName, IResourceLocator locator) throws EngineException {
		return engine.openReportDesign(designName, locator);
	}

	/**
	 * opens a report design stream and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param name         system id of the report design
	 * @param designStream input stream of the report design
	 * @param locator      the resource locator used to locate files referenced in
	 *                     the design
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the stream
	 *                         does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(String name, InputStream designStream, IResourceLocator locator)
			throws EngineException {
		return engine.openReportDesign(name, designStream, locator);
	}

	/**
	 * open the report design and return the runnable
	 * 
	 * @param name         system id of the report design.
	 * @param designStream input stream of the report desgin.
	 * @param options      options used to parse the design.
	 * @return a report design runnable object
	 * @throws EngineException
	 */
	public IReportRunnable openReportDesign(String name, InputStream designStream, Map options) throws EngineException {
		return engine.openReportDesign(name, designStream, options);
	}

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param fileName the report document name. report document is an archive in
	 *                 BIRT.
	 * @param locator  the resource locator used to locate files referenced in the
	 *                 design
	 * @return A handle to the report document
	 * @throws EngineException throwed when the report document archive does not
	 *                         exist, or the file is not a valud report document
	 */
	public IReportDocument openReportDocument(String fileName, IResourceLocator locator) throws EngineException {
		return engine.openReportDocument(fileName, locator);
	}

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param systemId the system id the opend document. It is used to access the
	 *                 resources with relative path in the report document. If it is
	 *                 NULL, a saved one is used.
	 * @param fileName the report document name. report document is an archive in
	 *                 BIRT.
	 * @param locator  the resource locator used to locate files referenced in the
	 *                 design
	 * @return A handle to the report document
	 * @throws EngineException throwed when the report document archive does not
	 *                         exist, or the file is not a valud report document
	 */
	public IReportDocument openReportDocument(String systemId, String fileName, IResourceLocator locator)
			throws EngineException {
		return engine.openReportDocument(systemId, fileName, locator);
	}

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param systemId the system id the opend document. It is used to access the
	 *                 resources with relative path in the report document. If it is
	 *                 NULL, a saved one is used.
	 * @param fileName the report document name. report document is an archive in
	 *                 BIRT.
	 * @param options  Map defines the options used to parse the design file.
	 * @return A handle to the report document
	 * @throws EngineException throwed when the report document archive does not
	 *                         exist, or the file is not a valid report document
	 */
	public IReportDocument openReportDocument(String systemId, String fileName, Map options) throws EngineException {
		return engine.openReportDocument(systemId, fileName, options);
	}

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param systemId the system id the opend document. It is used to access the
	 *                 resources with relative path in the report document. If it is
	 *                 NULL, a saved one is used.
	 * @param reader   a report archive for reading
	 * @param options  Map defines the options used to parse the design file.
	 * @return A handle to the report document
	 * @throws EngineException throwed when the report document archive does not
	 *                         exist, or the file is not a valid report document
	 */
	public IReportDocument openReportDocument(String systemId, IDocArchiveReader reader, Map options)
			throws EngineException {
		return engine.openReportDocument(systemId, reader, options);
	}

	/**
	 * get the logger used by report engine
	 * 
	 * @return the logger used by the report engine
	 */
	public Logger getLogger() {
		return engine.getLogger();
	}

	/**
	 * set the logger used by report engine.
	 * 
	 * @param logger
	 */
	public void setLogger(Logger logger) {
		engine.setLogger(logger);
	}

	/**
	 * create a task that renders the report to a specific output format.
	 * 
	 * @param reportDocument a handle to an IReportDocument object
	 * @param reportRunnable the runnable report design object
	 * @return a task that renders a report to an output format
	 */
	public IRenderTask createRenderTask(IReportDocument reportDocument, IReportRunnable reportRunnable) {
		return engine.createRenderTask(reportDocument, reportRunnable);
	}

	/**
	 * Returns data extraction extension information.
	 * 
	 * @return the data extraction extension information
	 */
	public DataExtractionFormatInfo[] getDataExtractionFormatInfo() {
		return engine.getDataExtractionFormatInfo();
	}

	/**
	 * creates a document writer that can write this archive file
	 * 
	 * @param file the archive file
	 * @return a document writer of this archive file
	 * @throws EngineException
	 */
	public IDocumentWriter openDocumentWriter(IArchiveFile file) throws EngineException {
		return engine.openDocumentWriter(file);
	}

	/**
	 * get the BIRT version
	 */
	public String getVersion() {
		return engine.getVersion();
	}

	public IEngineTask createEngineTask(String taskName) throws EngineException {
		return engine.createEngineTask(taskName);
	}

	public IDatasetPreviewTask createDatasetPreviewTask() throws EngineException {
		return engine.createDatasetPreviewTask();
	}

}
