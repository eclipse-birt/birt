/*******************************************************************************
 * Copyright (c) 2004,2010 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.framework.URLClassLoader;
import org.eclipse.birt.report.engine.api.DataExtractionFormatInfo;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDatasetPreviewTask;
import org.eclipse.birt.report.engine.api.IDocumentWriter;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.IRunnable;
import org.eclipse.birt.report.engine.api.IStatusHandler;
import org.eclipse.birt.report.engine.api.impl.LinkedObjectManager.LinkedEntry;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.extension.engine.IReportEngineExtension;
import org.eclipse.birt.report.engine.extension.engine.IReportEngineExtensionFactory;
import org.eclipse.birt.report.engine.internal.util.BundleVersionUtil;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManagerFactory;
import org.eclipse.birt.report.engine.util.SecurityUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.mozilla.javascript.ScriptableObject;

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

public class ReportEngine implements IReportEngine {

	public static final String PROPERTYSEPARATOR = File.pathSeparator;

	static protected Logger logger = Logger.getLogger(ReportEngine.class.getName());

	/**
	 * engine configuration object
	 */
	protected EngineConfig config;

	/**
	 * A helper object to carry out most ReportEngine jobs
	 */
	protected ReportEngineHelper helper;

	/**
	 * root script scope. contains objects shared by the whole engine.
	 */
	protected ScriptableObject rootScope;

	/**
	 * the class loader used the report engine
	 */
	protected URLClassLoader engineClassLoader;

	private LinkedObjectManager<ReportDocumentReader> openedDocuments;

	private EngineExtensionManager extensionManager = new EngineExtensionManager();

	private Map<String, Object> beans;

	private LoggerSetting loggerSetting;

	/**
	 * Create a Report Engine using a configuration.
	 *
	 * The user must set the BIRT_HOME in the EngineConfig.
	 *
	 * @param config an engine configuration object used to configure the engine
	 */
	public ReportEngine(EngineConfig config) {
		if (config == null) {
			throw new NullPointerException("config is null");
		}
		this.config = config;
		beans = new HashMap<>();
		mergeConfigToAppContext();

		intializeLogger();

		logger.log(Level.FINE, "ReportEngine created. EngineConfig: {0} ", config);
		this.helper = new ReportEngineHelper(this);
		openedDocuments = new LinkedObjectManager<>();
		IStatusHandler handler = config.getStatusHandler();
		if (handler != null) {
			handler.initialize();
		}

		registerCustomFontConfig();
	}

	private void mergeConfigToAppContext() {
		mergeConfigProperty(EngineConstants.APPCONTEXT_CLASSLOADER_KEY);
		mergeSystemProperty(EngineConstants.WEBAPP_CLASSPATH_KEY);
		mergeSystemProperty(EngineConstants.PROJECT_CLASSPATH_KEY);
		mergeSystemProperty(EngineConstants.WORKSPACE_CLASSPATH_KEY);
	}

	private void mergeConfigProperty(String property) {
		Map appContext = config.getAppContext();
		// The configuration in appContext has higher priority.
		if (!appContext.containsKey(property)) {
			Object value = config.getProperty(property);
			if (value != null) {
				appContext.put(property, value);
			}
		}
	}

	/**
	 * setup class path properties.
	 *
	 * The class path is defined in following sequence:
	 *
	 * <li>a. defined in the appContext.</li>
	 *
	 * <li>b. defined in the engine configuration.</li>
	 *
	 * <li>c. defined in the system configuration.</li>
	 *
	 * After this method, the class path are set into the appContext, so we can
	 * safely get the class path from the appContext only.
	 */
	private void mergeSystemProperty(String property) {
		Map appContext = config.getAppContext();
		// The configuration in appContext has higher priority.
		if (!appContext.containsKey(property)) {
			Object value = config.getProperty(property);
			if (value == null) {
				value = SecurityUtil.getSystemProperty(property);
			}
			if (value != null) {
				appContext.put(property, value);
			}
		}
	}

	/**
	 * set up engine logging
	 */
	private void intializeLogger() {
		Logger logger = null;
		String dest = null;
		String file = null;
		Level level = null;
		int rollingSize = 0;
		int maxBackupIndex = 1;
		if (config != null) {
			logger = config.getLogger();
			dest = config.getLogDirectory();
			file = config.getLogFile();
			level = config.getLogLevel();
			rollingSize = config.getLogRollingSize();
			maxBackupIndex = config.getLogMaxBackupIndex();
		}

		loggerSetting = EngineLogger.createSetting(logger, dest, file, level, rollingSize, maxBackupIndex);
	}

	/**
	 * set custom font configuration file.
	 */
	private void registerCustomFontConfig() {
		if (config != null) {
			URL customFontConfig = config.getFontConfig();
			FontMappingManagerFactory.setCustomFontConfig(customFontConfig);
		}
	}

	/**
	 * get the root scope used by the engine
	 *
	 * @return
	 */
	@Override
	public Object getRootScope() {
		return rootScope;
	}

	/**
	 * Change the log level to newLevel
	 *
	 * @param newLevel - new log level
	 */
	@Override
	public void changeLogLevel(Level newLevel) {
		EngineLogger.changeLogLevel(loggerSetting, newLevel);
	}

	/**
	 * returns the engine configuration object
	 *
	 * @return the engine configuration object
	 */
	@Override
	public EngineConfig getConfig() {
		return config;
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
	@Override
	public IReportRunnable openReportDesign(String designName) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDesign: designName={0} ", designName);
		IResourceLocator locator = config.getResourceLocator();
		return helper.openReportDesign(designName, locator);
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
	@Override
	public IReportRunnable openReportDesign(ReportDesignHandle designHandle) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDesign: designHandle={0} ", designHandle);
		return helper.openReportDesign(designHandle);
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
	@Override
	public IReportRunnable openReportDesign(InputStream designStream) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDesign: designStream={0} ", designStream);
		return helper.openReportDesign(designStream);
	}

	@Override
	public IReportRunnable openReportDesign(String name, InputStream designStream) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDesign: name={0}, designStream={1} ",
				new Object[] { name, designStream });
		return helper.openReportDesign(name, designStream);
	}

	/**
	 * creates an engine task for running and rendering report directly to output
	 * format
	 *
	 * @param reportRunnable the runnable report design object
	 * @return a run and render report task
	 */
	@Override
	public IRunAndRenderTask createRunAndRenderTask(IReportRunnable reportRunnable) {
		logger.log(Level.FINE, "ReportEngine.createRunAndRenderTask: reportRunnable={0} ", reportRunnable);
		return helper.createRunAndRenderTask(reportRunnable);
	}

	/**
	 * creates an engine task for obtaining report parameter definitions
	 *
	 * @param reportRunnable the runnable report design object
	 * @return a run and render report task
	 */
	@Override
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(IRunnable reportRunnable) {
		logger.log(Level.FINE, "ReportEngine.createGetParameterDefinitionTask: reportRunnable={0} ", reportRunnable);
		return helper.createGetParameterDefinitionTask((ReportRunnable) reportRunnable);
	}

	/**
	 * creates an engine task for obtaining report parameter definitions
	 *
	 * @param reportRunnable the runnable report design object
	 * @return a GetParameterDefinitionTask
	 */
	@Override
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(IReportRunnable reportRunnable) {
		logger.log(Level.FINE, "ReportEngine.createGetParameterDefinitionTask: reportRunnable={0} ", reportRunnable);
		return helper.createGetParameterDefinitionTask((ReportRunnable) reportRunnable);
	}

	/**
	 * returns all supported output formats through BIRT engine emitter extensions
	 *
	 * @return all supported output formats through BIRT engine emitter extensions
	 */
	@Override
	public String[] getSupportedFormats() {
		return helper.getSupportedFormats();
	}

	/**
	 * Return all the emitter information which BIRT Engine can load.
	 *
	 * @return the emitter information
	 */
	@Override
	public EmitterInfo[] getEmitterInfo() {
		return helper.getEmitterInfo();
	}

	/**
	 * the MIME type for the specific formatted supported by the extension.
	 *
	 * @param format      the output format
	 * @param extensionID the extension ID, which could be null if only one plugin
	 *                    supports the output format
	 * @return the MIME type for the specific formatted supported by the extension.
	 */
	@Override
	public String getMIMEType(String format) {
		return helper.getMIMEType(format);
	}

	/**
	 * shuts down the report engine
	 */
	@Override
	public void destroy() {
		logger.fine("ReportEngine.destroy");
		rootScope = null;
		helper = null;
		synchronized (openedDocuments) {
			for (ReportDocumentReader document : openedDocuments) {
				logger.log(Level.WARNING, "{0} is not closed.", document.getName());
				document.setEngineCacheEntry(null);
				document.close();
			}
			openedDocuments.clear();
		}
		IStatusHandler handler = config.getStatusHandler();
		if (handler != null) {
			handler.finish();
		}
		if (extensionManager != null) {
			extensionManager.close();
			extensionManager = null;
		}
		EngineLogger.removeSetting(loggerSetting);

		if (engineClassLoader != null) {
			engineClassLoader.close();
		}
	}

	/**
	 * creates a task to run a report to generate a report document
	 *
	 * @param reportRunnable the runnable report design object
	 * @return a task that runs the report
	 */
	@Override
	public IRunTask createRunTask(IReportRunnable reportRunnable) {
		logger.log(Level.FINE, "ReportEngine.createRunTask: reportRunnable={0} ", reportRunnable);
		return helper.createRunTask(reportRunnable);
	}

	/**
	 * creates a task that renders the report to a specific output format.
	 *
	 * @param reportDocument a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	@Override
	public IRenderTask createRenderTask(IReportDocument reportDocument) {
		logger.log(Level.FINE, "ReportEngine.createRenderTask: reportDocument={0} ", reportDocument);
		return helper.createRenderTask(reportDocument);
	}

	/**
	 * creates a task that renders the report to a specific output format.
	 *
	 * @param reportDocument a handle to an IReportDocument object
	 * @param reportRunnable the runnable report design object
	 * @return a task that renders a report to an output format
	 */
	@Override
	public IRenderTask createRenderTask(IReportDocument reportDocument, IReportRunnable reportRunnable) {
		logger.log(Level.FINE, "ReportEngine.createRenderTask: reportDocument={0}, runnable={1}",
				new Object[] { reportDocument, reportRunnable });
		return helper.createRenderTask(reportDocument, reportRunnable);
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
	@Override
	public IReportDocument openReportDocument(String fileName) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDocument: fileName={0} ", fileName);
		return helper.openReportDocument(fileName);
	}

	/**
	 * creates a task that allows data extraction from a report document
	 *
	 * @param reportDocument a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	@Override
	public IDataExtractionTask createDataExtractionTask(IReportDocument reportDocument) {
		logger.log(Level.FINE, "ReportEngine.createDataExtractionTask: reportDocument={0} ", reportDocument);
		return helper.createDataExtractionTask(reportDocument);
	}

	/**
	 * shut down the engine, release all the resources.
	 *
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void shutdown() {
		destroy();
	}

	@Override
	public IReportDocument openReportDocument(String systemId, String fileName) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDocument: systemID={0}, file={1} ",
				new Object[] { systemId, fileName });
		return openReportDocument(systemId, fileName, (IResourceLocator) null);
	}

	@Override
	public IReportRunnable openReportDesign(String designName, IResourceLocator locator) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDesign: design={0}, locator={1} ",
				new Object[] { designName, locator });
		return helper.openReportDesign(designName, locator);
	}

	@Override
	public IReportRunnable openReportDesign(String name, InputStream designStream, IResourceLocator locator)
			throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDesign: name={0}, designStream={1}, locator={2} ",
				new Object[] { name, designStream, locator });
		return helper.openReportDesign(name, designStream, locator);
	}

	@Override
	public IReportRunnable openReportDesign(String name, InputStream designStream, Map options) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDesign: name={0}, designStream={1}, options={3} ",
				new Object[] { name, designStream, options });
		return helper.openReportDesign(name, designStream, options);
	}

	@Override
	public IReportDocument openReportDocument(String fileName, IResourceLocator locator) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDocument: file={0}, locator={1} ",
				new Object[] { fileName, locator });
		return openReportDocument(fileName, fileName, locator);
	}

	@Override
	public IReportDocument openReportDocument(String systemId, String fileName, IResourceLocator locator)
			throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDocument: systemId={0}, file={1}, locator={2} ",
				new Object[] { systemId, fileName, locator });
		return helper.openReportDocument(systemId, fileName, locator);
	}

	@Override
	public IReportDocument openReportDocument(String systemId, String fileName, Map options) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDocument: systemId={0}, file={1}, options={2} ",
				new Object[] { systemId, fileName, options });
		return helper.openReportDocument(systemId, fileName, options);
	}

	@Override
	public IReportDocument openReportDocument(String systemId, IDocArchiveReader reader, Map options)
			throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openReportDocument: systemId={0}, reader={1}, options={2} ",
				new Object[] { systemId, reader, options });
		return helper.openReportDocument(systemId, reader, options);
	}

	@Override
	public IDocumentWriter openDocumentWriter(IArchiveFile file) throws EngineException {
		logger.log(Level.FINE, "ReportEngine.openDocumentWriter: archive={0} ", new Object[] { file });
		return helper.openDocumentWriter(file);
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public void setLogger(Logger logger) {
		if (logger != null) {
			EngineLogger.setLogger(loggerSetting, logger);
		}
	}

	public ClassLoader getEngineClassLoader() {
		if (engineClassLoader != null) {
			return engineClassLoader;
		}
		synchronized (this) {
			if (engineClassLoader == null) {
				engineClassLoader = createEngineClassLoader();
			}
		}
		return engineClassLoader;
	}

	private URLClassLoader createEngineClassLoader() {
		ArrayList<URL> urls = new ArrayList<>();

		String[] CLASS_PATHES = { EngineConstants.WEBAPP_CLASSPATH_KEY, EngineConstants.PROJECT_CLASSPATH_KEY,
				EngineConstants.WORKSPACE_CLASSPATH_KEY };

		HashMap appContext = getAppContext();
		for (int i = 0; i < CLASS_PATHES.length; i++) {
			final String classPathName = CLASS_PATHES[i];
			Object propValue = appContext.get(classPathName);
			if (propValue instanceof String) {
				String classPath = (String) propValue;
				if (classPath.length() != 0) {
					String[] jars = classPath.split(PROPERTYSEPARATOR, -1);
					if (jars != null && jars.length != 0) {
						for (int j = 0; j < jars.length; j++) {
							File file = new File(jars[j]);
							try {
								urls.add(file.toURI().toURL());
							} catch (MalformedURLException e) {
								logger.log(Level.WARNING, e.getMessage(), e);
							}
						}
					}
				}
			}
		}
		ClassLoader appContextClassLoader = getAppContextClassLoader();

		return new URLClassLoader(urls.toArray(new URL[urls.size()]), appContextClassLoader);
	}

	private HashMap getAppContext() {
		return config.getAppContext();
	}

	private ClassLoader getAppContextClassLoader() {
		Map appContext = getAppContext();
		Object appLoader = appContext.get(EngineConstants.APPCONTEXT_CLASSLOADER_KEY);
		if (appLoader instanceof ClassLoader) {
			return (ClassLoader) appLoader;
		}

		return IReportEngine.class.getClassLoader();
	}

	@Override
	public DataExtractionFormatInfo[] getDataExtractionFormatInfo() {
		return helper.getDataExtractionFormatInfo();
	}

	public IReportEngineExtension getEngineExtension(String name) {
		if (extensionManager != null) {
			return extensionManager.getExtension(name);
		}
		return null;
	}

	public String[] getEngineExtensions(ReportRunnable runnable) {
		if (extensionManager != null) {
			return extensionManager.getExtensions(runnable);
		}
		return null;
	}

	public Iterator<ReportDocumentReader> getOpenedDocuments() {
		return openedDocuments.iterator();
	}

	void cacheOpenedDocument(ReportDocumentReader document) {
		synchronized (openedDocuments) {
			LinkedEntry<ReportDocumentReader> entry = openedDocuments.add(document);
			document.setEngineCacheEntry(entry);
		}
	}

	@Override
	public String getVersion() {
		return BundleVersionUtil.getBundleVersion("org.eclipse.birt.report.engine");
	}

	private class EngineExtensionManager {

		HashMap<String, IReportEngineExtension> exts = new HashMap<>();

		EngineExtensionManager() {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint("org.eclipse.birt.core.FactoryService");
			IExtension[] extensions = extensionPoint.getExtensions();
			for (IExtension extension : extensions) {
				IConfigurationElement[] elements = extension.getConfigurationElements();
				for (IConfigurationElement element : elements) {
					String type = element.getAttribute("type");
					if ("org.eclipse.birt.report.engine.extension".equals(type)) {
						try {
							Object factoryObject = element.createExecutableExtension("class");
							if (factoryObject instanceof IReportEngineExtensionFactory) {
								IReportEngineExtensionFactory factory = (IReportEngineExtensionFactory) factoryObject;
								IReportEngineExtension engineExtension = factory.createExtension(ReportEngine.this);
								exts.put(engineExtension.getExtensionName(), engineExtension);
							}
						} catch (CoreException ex) {
							logger.log(Level.WARNING, "can not load the engine extension factory", ex);
						}
					}
				}
			}
		}

		IReportEngineExtension getExtension(String name) {
			if (exts.containsKey(name)) {
				return exts.get(name);
			}
			return null;
		}

		String[] getExtensions(IReportRunnable runnable) {
			ArrayList<String> extensions = new ArrayList<>();
			for (Map.Entry<String, IReportEngineExtension> entry : exts.entrySet()) {
				String extName = entry.getKey();
				IReportEngineExtension ext = entry.getValue();
				if (ext.needExtension(runnable)) {
					extensions.add(extName);
				}
			}
			return extensions.toArray(new String[extensions.size()]);
		}

		void close() {
			for (IReportEngineExtension ext : exts.values()) {
				if (ext != null) {
					ext.close();
				}
			}
			exts.clear();
		}
	}

	@Override
	public IEngineTask createEngineTask(String taskName) throws EngineException {
		String extName = taskName;
		int index = taskName.lastIndexOf('.');
		if (index != -1) {
			extName = taskName.substring(0, index);
			taskName = taskName.substring(index + 1);
		}
		IReportEngineExtension extension = extensionManager.getExtension(extName);
		if (extension != null) {
			return extension.createEngineTask(taskName);
		}
		return null;
	}

	public DataEngineFactory getDataEngineFactory() {
		return DataEngineFactory.getInstance();
	}

	@Override
	public IDatasetPreviewTask createDatasetPreviewTask() throws EngineException {
		logger.log(Level.FINE, "createDatasetPreviewTask");
		return helper.createDatasetPreviewTask();
	}
}
