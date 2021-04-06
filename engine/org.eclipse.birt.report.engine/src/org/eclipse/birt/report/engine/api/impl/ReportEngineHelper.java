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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.report.engine.api.DataExtractionFormatInfo;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDatasetPreviewTask;
import org.eclipse.birt.report.engine.api.IDocumentWriter;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * a helper class that does most of the dirty work for report engine
 */
public class ReportEngineHelper {
	/**
	 * reference the the public report engine object
	 */
	private ReportEngine engine;

	/**
	 * logger used to log syntax errors.
	 */
	protected Logger logger;

	/**
	 * extension manager
	 */
	private ExtensionManager extensionMgr;

	private EmitterInfo[] emitterInfos;

	/**
	 * constructor
	 * 
	 * @param engine the report engine
	 */
	public ReportEngineHelper(ReportEngine engine) {
		this.engine = engine;
		this.logger = engine.getLogger();
		this.extensionMgr = ExtensionManager.getInstance();
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
		return openReportDesign(designName, (IResourceLocator) null);
	}

	public IReportRunnable openReportDesign(String designName, IResourceLocator locator) throws EngineException {
		File file = new File(designName);
		if (!file.exists()) {
			logger.log(Level.SEVERE, "{0} not found!", file.getAbsolutePath()); //$NON-NLS-1$
			throw new EngineException(MessageConstants.DESIGN_FILE_NOT_FOUND_EXCEPTION, designName);
		}

		try {
			InputStream in = new FileInputStream(file);
			String systemId = designName;
			try {
				systemId = file.toURI().toURL().toString();
			} catch (MalformedURLException ue) {
				systemId = designName;
			}
			return openReportDesign(systemId, in, locator);
		} catch (FileNotFoundException ioe) {
			logger.log(Level.SEVERE, "{0} not found!", file.getAbsolutePath()); //$NON-NLS-1$
			throw new EngineException(MessageConstants.DESIGN_FILE_NOT_FOUND_EXCEPTION, designName);
		}
	}

	/**
	 * opens a report design stream and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design
	 * runnableobject.
	 * 
	 * And the user must close the report design stream after get the
	 * IReportRunnable.
	 * 
	 * @param designStream the report design input stream
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the stream
	 *                         does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(InputStream designStream) throws EngineException {
		return openReportDesign("<stream>", designStream);
	}

	/**
	 * opens a report design stream and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design
	 * runnableobject.
	 * 
	 * And the user must close the report design stream after get the
	 * IReportRunnable.
	 * 
	 * @param designName   the stream's name
	 * @param designStream the report design input stream
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the stream
	 *                         does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(String designName, InputStream designStream) throws EngineException {
		return openReportDesign(designName, designStream, new HashMap());
	}

	public IReportRunnable openReportDesign(String designName, InputStream designStream, IResourceLocator locator)
			throws EngineException {
		HashMap options = new HashMap();
		if (locator != null) {
			options.put(ModuleOption.RESOURCE_LOCATOR_KEY, locator);
		}
		return openReportDesign(designName, designStream, options);
	}

	/**
	 * use the engine config to setup the module options. Engine config contains two
	 * properties for the module options:
	 * <li>resourceLocator
	 * <li>resourceFolder If the options contains no property, copy the property
	 * from the engine config.
	 * 
	 * Disable Semantic Check as default unless PARSER_SEMANTIC_CHECK_KEY is
	 * specified
	 * 
	 * @param options
	 */
	protected void intializeModuleOptions(Map options) {
		EngineConfig config = engine.getConfig();
		if (config != null) {
			if (options.get(ModuleOption.RESOURCE_LOCATOR_KEY) == null) {
				IResourceLocator locator = config.getResourceLocator();
				if (locator != null) {
					options.put(ModuleOption.RESOURCE_LOCATOR_KEY, locator);
				}
			}
			if (options.get(ModuleOption.RESOURCE_FOLDER_KEY) == null) {
				String resourcePath = config.getResourcePath();
				if (resourcePath != null) {
					options.put(ModuleOption.RESOURCE_FOLDER_KEY, resourcePath);
				}
			}
		}
		Object semanticCheck = options.get(ModuleOption.PARSER_SEMANTIC_CHECK_KEY);
		if (semanticCheck == null) {
			options.put(ModuleOption.PARSER_SEMANTIC_CHECK_KEY, Boolean.FALSE);
		}
	}

	public ReportDesignHandle getReportDesignHandle(String designName, InputStream designStream, Map options)
			throws EngineException {
		ReportDesignHandle designHandle;
		try {
			if (options == null) {
				options = new HashMap();
			}
			intializeModuleOptions(options);
			ReportParser parser = new ReportParser(options);
			designHandle = parser.getDesignHandle(designName, designStream);
		} catch (DesignFileException e) {
			logger.log(Level.SEVERE, "invalid design file {0}", designName); //$NON-NLS-1$
			throw new EngineException(MessageConstants.INVALID_DESIGN_FILE_EXCEPTION, designName, e);
		}
		assert (designHandle != null);
		return designHandle;
	}

	public IReportRunnable openReportDesign(String designName, InputStream designStream, Map options)
			throws EngineException {

		ReportRunnable runnable = new ReportRunnable(engine, getReportDesignHandle(designName, designStream, options));
		runnable.setReportName(designName);
		return runnable;
	}

	/**
	 * creates a report design runnable based on a report design handle. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param designStream the report design input stream
	 * @return a report design runnable object
	 * @throws EngineException throwed when the input stream is null, or the stream
	 *                         does not yield a valid report design
	 */
	public IReportRunnable openReportDesign(ReportDesignHandle designHandle) throws EngineException {
		ReportRunnable ret = new ReportRunnable(engine, designHandle);
		ret.setReportName((designHandle).getFileName());
		return ret;
	}

	/**
	 * creates an engine task for running and rendering report directly to output
	 * format
	 * 
	 * @param reportRunnable the runnable report design object
	 * @return a run and render report task
	 */
	public IRunAndRenderTask createRunAndRenderTask(IReportRunnable reportRunnable) {
		return new RunAndRenderTask(engine, reportRunnable);
	}

	public IGetParameterDefinitionTask createGetParameterDefinitionTask(ReportRunnable reportRunnable) {
		return new GetParameterDefinitionTask(engine, reportRunnable);
	}

	/**
	 * returns all supported output formats through BIRT engine emitter extensions
	 * 
	 * @return all supported output formats through BIRT engine emitter extensions
	 */
	public String[] getSupportedFormats() {
		return (String[]) extensionMgr.getSupportedFormat().toArray(new String[0]);
	}

	/**
	 * return all emitter info through BIRT engine emitter extension. If there are
	 * several emitters for a same format, then the default emitter specified by
	 * EngineConfig is used, if no default emitter is specified in EngineConfig,
	 * then the first emitter is used.
	 * 
	 * @return all emitter info through BIRT engine emitter extension
	 */
	public synchronized EmitterInfo[] getEmitterInfo() {
		if (emitterInfos == null) {
			EngineConfig config = engine.getConfig();
			Map<String, EmitterInfo> emitters = new HashMap<String, EmitterInfo>();
			EmitterInfo[] tempEmitterInfo = extensionMgr.getEmitterInfo();
			for (EmitterInfo emitterInfo : tempEmitterInfo) {
				String format = emitterInfo.getFormat();
				String id = emitterInfo.getID();
				if (!emitters.containsKey(format) || id.equals(config.getDefaultEmitter(format))) {
					emitters.put(format, emitterInfo);
				}
			}
			emitterInfos = new EmitterInfo[emitters.size()];
			emitters.values().toArray(emitterInfos);
			for (EmitterInfo emitterInfo : emitterInfos) {
				String format = emitterInfo.getFormat();
				String id = emitterInfo.getID();
				String defaultEmitter = config.getDefaultEmitter(format);
				if (defaultEmitter != null && !defaultEmitter.equals(id)) {
					logger.log(Level.WARNING, "Emitter " + defaultEmitter + " doens't exist! Emitter " + id
							+ " is used for " + format + ".");
				}
			}
		}
		return emitterInfos;
	}

	/**
	 * Returns all info of data extraction extension.
	 */
	public DataExtractionFormatInfo[] getDataExtractionFormatInfo() {
		return extensionMgr.getDataExtractionExtensionInfo();
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
		return extensionMgr.getMIMEType(format);
	}

	public IReportDocument openReportDocument(String docArchiveName) throws EngineException {
		return openReportDocument(null, docArchiveName, new HashMap());
	}

	public IReportDocument openReportDocument(String systemId, String docArchiveName) throws EngineException {
		return openReportDocument(systemId, docArchiveName, new HashMap());
	}

	public IReportDocument openReportDocument(String systemId, String docArchiveName, IResourceLocator locator)
			throws EngineException {
		HashMap options = new HashMap();
		if (locator != null) {
			options.put(ModuleOption.RESOURCE_LOCATOR_KEY, locator);
		}
		return openReportDocument(systemId, docArchiveName, options);
	}

	public IReportDocument openReportDocument(String systemId, String docArchiveName, Map options)
			throws EngineException {
		IDocArchiveReader reader = null;
		try {
			File file = new File(docArchiveName);
			if (file.exists()) {
				if (file.isDirectory()) {
					reader = new FolderArchiveReader(docArchiveName);
				} else {
					reader = new FileArchiveReader(docArchiveName);
				}
			} else {
				if (docArchiveName.endsWith("\\") || docArchiveName.endsWith("/")) {
					reader = new FolderArchiveReader(docArchiveName);
				} else {
					reader = new FileArchiveReader(docArchiveName);
				}
			}
		} catch (IOException e) {
			throw new EngineException(e.getLocalizedMessage());
		}

		return openReportDocument(systemId, reader, options);
	}

	public IReportDocument openReportDocument(String systemId, IDocArchiveReader archive, IResourceLocator locator)
			throws EngineException {
		HashMap options = new HashMap();
		if (locator != null) {
			options.put(ModuleOption.RESOURCE_LOCATOR_KEY, locator);
		}
		return openReportDocument(systemId, archive, options);
	}

	public IReportDocument openReportDocument(String systemId, IDocArchiveReader archive, Map options)
			throws EngineException {
		if (options == null) {
			options = new HashMap();
		}
		intializeModuleOptions(options);

		ReportDocumentReader reader = new ReportDocumentReader(systemId, engine, archive, options);
		engine.cacheOpenedDocument(reader);

		return reader;
	}

	public IDocumentWriter openDocumentWriter(IArchiveFile file) {
		return new DocumentWriter(file);
	}

	public IRunTask createRunTask(IReportRunnable runnable) {
		return new RunTask(engine, runnable);
	}

	public IRenderTask createRenderTask(IReportDocument reportDoc) {
		return new RenderTask(engine, reportDoc);
	}

	public IRenderTask createRenderTask(IReportDocument reportDocument, IReportRunnable reportRunnable) {
		return new RenderTask(engine, reportRunnable, reportDocument);
	}

	public IDataExtractionTask createDataExtractionTask(IReportDocument reportDoc) {
		try {
			return new DataExtractionTask(engine, reportDoc);
		} catch (EngineException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex); // $NON-NLS-1$
		}
		return null;
	}

	public IDatasetPreviewTask createDatasetPreviewTask() throws EngineException {
		return new DatasetPreviewTask(engine);
	}
}
