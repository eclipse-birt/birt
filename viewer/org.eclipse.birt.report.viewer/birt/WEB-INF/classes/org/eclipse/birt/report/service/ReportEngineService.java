/*************************************************************************************
 * Copyright (c) 2004, 2024, 2025 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation	- Initial implementation
 *     Thomas Gutmann		- Enhanced exchange of display text for multi selections
 ************************************************************************************/

package org.eclipse.birt.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IRequestInfo;
import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IParameterDefn;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.session.IViewingSession;
import org.eclipse.birt.report.session.ViewingSessionUtil;
import org.eclipse.birt.report.soapengine.api.Column;
import org.eclipse.birt.report.soapengine.api.ResultSet;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataExtractionParameterUtil;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.LoggingUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Provides all the services from Engine.
 */

public class ReportEngineService {

	/**
	 * Dummy remote exception, used to encapsulate real exception. This mechanism is
	 * temporary and is used to prevent changing the method's signature (throw
	 * part).
	 *
	 * @deprecated this is a workaround, to be removed in the future
	 */
	@Deprecated
	public static class DummyRemoteException extends RemoteException {

		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param cause
		 */
		public DummyRemoteException(Throwable cause) {
			super(null, cause);
		}

	}

	private static ReportEngineService instance;

	/**
	 * Report engine instance.
	 */
	private IReportEngine engine = null;

	/**
	 * Static engine config instance.
	 */
	private EngineConfig config = null;

	/**
	 * Image handler instance.
	 */
	private HTMLServerImageHandler imageHandler = null;

	/**
	 * Constructor.
	 *
	 * @param servletContext
	 * @param config
	 */
	private ReportEngineService(ServletContext servletContext) {
		System.setProperty("RUN_UNDER_ECLIPSE", "false"); //$NON-NLS-1$ //$NON-NLS-2$

		if (servletContext == null) {
			return;
		}

		// Init context parameters
		ParameterAccessor.initParameters(servletContext);

		config = new EngineConfig();

		// Register new image handler
		HTMLRenderOption emitterConfig = new HTMLRenderOption();
		emitterConfig.setActionHandler(new HTMLActionHandler());
		imageHandler = new HTMLServerImageHandler();
		emitterConfig.setImageHandler(imageHandler);
		config.getEmitterConfigs().put("html", emitterConfig); //$NON-NLS-1$

		// Prepare log level.
		String logLevel = ParameterAccessor.logLevel;
		Level level = logLevel != null && logLevel.length() > 0 ? Level.parse(logLevel) : Level.OFF;
		config.setLogConfig(ParameterAccessor.logFolder, level);

		// Prepare ScriptLib location
		String scriptLibDir = ParameterAccessor.scriptLibDir;

		ArrayList<Object> jarFileList = new ArrayList<>();
		if (scriptLibDir != null) {
			File dir = new File(scriptLibDir);
			getAllJarFiles(dir, jarFileList);
		}

		StringBuilder scriptlibClassPath = new StringBuilder();
		for (int i = 0; i < jarFileList.size(); i++) {
			String p = null;
			try {
				p = ((File) jarFileList.get(i)).getCanonicalPath();
			} catch (IOException e) {
				p = ((File) jarFileList.get(i)).getAbsolutePath();
			}

			if (p != null && p.length() > 0) {
				if (scriptlibClassPath.length() > 0) {
					scriptlibClassPath.append(EngineConstants.PROPERTYSEPARATOR);
				}

				scriptlibClassPath.append(p);
			}
		}

		Map<String, Object> appContext = new HashMap<>();

		appContext.put(EngineConstants.WEBAPP_CLASSPATH_KEY, scriptlibClassPath.toString());

		// Set appcontext classloader to Engine config
		ClassLoader appClassLoader = BirtUtility.getAppClassLoader();
		if (appClassLoader == null) {
			appClassLoader = ReportEngineService.class.getClassLoader();
		}
		appContext.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, appClassLoader);

		// merget with user app context if applicable
		appContext = BirtUtility.getAppContext(appContext);

		config.getAppContext().putAll(appContext);

		config.setEngineHome(""); //$NON-NLS-1$

		// set maxrows
		config.setMaxRowsPerQuery(ParameterAccessor.maxRows);

		// configure the loggers
		LoggingUtil.configureLoggers(ParameterAccessor.loggers, level, ParameterAccessor.logFolder);
	}

	/**
	 * Get engine instance.
	 *
	 * @return the single report engine service
	 */
	public static ReportEngineService getInstance() {
		return instance;
	}

	/**
	 * Get engine instance.
	 *
	 * @param servletConfig
	 * @throws BirtException
	 *
	 */
	public synchronized static void initEngineInstance(ServletConfig servletConfig) throws BirtException {
		initEngineInstance(servletConfig.getServletContext());
	}

	/**
	 * Get engine instance.
	 *
	 * @param servletContext
	 * @throws BirtException
	 *
	 */
	public synchronized static void initEngineInstance(ServletContext servletContext) throws BirtException {
		if (ReportEngineService.instance != null) {
			return;
		}
		ReportEngineService.instance = new ReportEngineService(servletContext);
	}

	/**
	 * Get all the files under the specified folder (including all the files under
	 * sub-folders)
	 *
	 * @param dir      - the folder to look into
	 * @param fileList - the fileList to be returned
	 */
	private void getAllJarFiles(File dir, ArrayList<Object> fileList) {
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files == null) {
				return;
			}

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					if (file.getName().endsWith(".jar")) { //$NON-NLS-1$
						fileList.add(file);
					}
				} else if (file.isDirectory()) {
					getAllJarFiles(file, fileList);
				}
			}
		}
	}

	/**
	 * Set Engine context.
	 *
	 * @param servletContext
	 * @param request
	 * @deprecated
	 * @throws BirtException
	 */
	@Deprecated
	public synchronized void setEngineContext(ServletContext servletContext, HttpServletRequest request)
			throws BirtException {
		setEngineContext(servletContext);
	}

	/**
	 * Set Engine context.
	 *
	 * @param servletContext
	 * @throws BirtException
	 */
	public synchronized void setEngineContext(ServletContext servletContext) throws BirtException {
		if (engine == null) {
			IPlatformContext platformContext = new PlatformServletContext(servletContext);
			config.setPlatformContext(platformContext);

			// Startup OSGI Platform
			Platform.startup(config);

			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			if (factory == null) {
				// if null, throw exception
				throw new ViewerException(ResourceConstants.REPORT_SERVICE_EXCEPTION_STARTUP_REPORTENGINE_ERROR);
			}
			engine = factory.createReportEngine(config);

			// Get supported output formats
			// exclude formats which are comment out at viewer.properties
			ArrayList<String> tmpSupportedFormats = new ArrayList<String>();
			for (String format : engine.getSupportedFormats()) {
				if (ParameterAccessor.initProps.get("viewer.extension." + format) != null) {
					tmpSupportedFormats.add(format);
				}
			}
			ParameterAccessor.supportedFormats = new String[tmpSupportedFormats.size()];
			ParameterAccessor.supportedFormats = tmpSupportedFormats.toArray(ParameterAccessor.supportedFormats);

			// Get supported data extraction extensions
			ParameterAccessor.supportedDataExtractions = engine.getDataExtractionFormatInfo();

			// Get the supported emitters
			Map<Object, Object> supportedEmitters = new HashMap<>();
			EmitterInfo[] emitterInfos = engine.getEmitterInfo();
			for (int i = 0; i < emitterInfos.length; i++) {
				EmitterInfo emitterInfo = emitterInfos[i];
				supportedEmitters.put(emitterInfo.getID(), emitterInfo);
			}
			ParameterAccessor.supportedEmitters = supportedEmitters;
		}
	}

	/**
	 * Open report design.
	 *
	 * @param report
	 * @param options the config options in the report design
	 * @return the report runnable
	 * @throws EngineException
	 */
	public IReportRunnable openReportDesign(String report, Map options) throws EngineException {
		File file = new File(report);
		if (!file.exists()) {
			throw new EngineException(MessageConstants.DESIGN_FILE_NOT_FOUND_EXCEPTION, report);
		}

		try {
			InputStream in = new FileInputStream(file);
			String systemId = report;
			try {
				systemId = file.toURI().toURL().toString();
			} catch (MalformedURLException ue) {
				systemId = report;
			}
			return engine.openReportDesign(systemId, in, options);
		} catch (FileNotFoundException ioe) {
			throw new EngineException(MessageConstants.DESIGN_FILE_NOT_FOUND_EXCEPTION, report);
		}
	}

	/**
	 * Open report design by using the input stream
	 *
	 * @param systemId     the system Id of the report design
	 * @param reportStream - the input stream
	 * @param options      the config options in the report design
	 * @return IReportRunnable
	 * @throws EngineException
	 */
	public IReportRunnable openReportDesign(String systemId, InputStream reportStream, Map options)
			throws EngineException {
		return engine.openReportDesign(systemId, reportStream, options);
	}

	/**
	 * createGetParameterDefinitionTask.
	 *
	 * @param runnable
	 * @deprecated
	 * @return the get parameter definition task
	 */
	@Deprecated
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(IReportRunnable runnable) {
		IGetParameterDefinitionTask task = null;

		try {
			task = engine.createGetParameterDefinitionTask(runnable);
		} catch (Exception e) {
		}

		return task;
	}

	/**
	 * createGetParameterDefinitionTask.
	 *
	 * @param runnable
	 * @param options
	 * @return the get parameter definition task
	 */
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(IReportRunnable runnable,
			InputOptions options) {
		IGetParameterDefinitionTask task = null;

		try {
			HttpServletRequest request = (HttpServletRequest) options.getOption(InputOptions.OPT_REQUEST);
			Locale locale = (Locale) options.getOption(InputOptions.OPT_LOCALE);
			TimeZone timeZone = (TimeZone) options.getOption(InputOptions.OPT_TIMEZONE);

			task = engine.createGetParameterDefinitionTask(runnable);
			task.setLocale(locale);

			com.ibm.icu.util.TimeZone tz = BirtUtility.toICUTimeZone(timeZone);
			if (tz != null) {
				task.setTimeZone(tz);
			}

			// set app context
			Map<String, Object> context = BirtUtility.getAppContext(request);
			task.setAppContext(context);
		} catch (Exception e) {
		}

		return task;
	}

	/**
	 * Open report document from archive,
	 *
	 * @param docName  the name of the report document
	 * @param systemId the system ID to search the resource in the document,
	 *                 generally it is the file name of the report design
	 * @param options  the config options used in document
	 * @return the report docuement
	 */

	public IReportDocument openReportDocument(String systemId, String docName, Map options) throws RemoteException {
		if (docName == null) {
			return null;
		}

		IReportDocument document = null;

		try {
			document = engine.openReportDocument(systemId, docName, options);
		} catch (EngineException e) {
			// TODO: remove RemoteException in the method signature and
			// throw ReportServiceException directly
			throwDummyException(e);
		}

		return document;
	}

	/**
	 * Render image.
	 *
	 * @param imageId
	 * @param request
	 * @param outputStream
	 * @throws RemoteException
	 */
	public void renderImage(String imageId, HttpServletRequest request, OutputStream outputStream)
			throws RemoteException {
		assert (this.imageHandler != null);

		try {
			IViewingSession session = ViewingSessionUtil.getSession(request);
			if (session != null) {
				this.imageHandler.getImage(outputStream, session.getImageTempFolder(), imageId);
			} else {
				throw new ReportServiceException(
						BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_NO_VIEWING_SESSION));
			}
		} catch (BirtException | ReportServiceException e) {
			throwDummyException(e);
		}
	}

	/**
	 * Create HTML render option.
	 *
	 * @param svgFlag
	 * @param servletPath
	 * @param request
	 * @return HTML render option from the given arguments
	 * @throws ReportServiceException
	 */
	private HTMLRenderOption createHTMLRenderOption(boolean svgFlag, String servletPath, HttpServletRequest request,
			IViewingSession session) {
		String baseURL = null;

		// try to get base url from config file
		if (!ParameterAccessor.isDesigner()) {
			baseURL = ParameterAccessor.getBaseURL();
		}

		if (baseURL == null) {
			// if not HTML format, use full URL.
			if (ParameterAccessor.isOpenAsAttachment(request)
					|| !ParameterAccessor.PARAM_FORMAT_HTML.equalsIgnoreCase(ParameterAccessor.getFormat(request))) {
				baseURL = request.getScheme() + "://" //$NON-NLS-1$
						+ request.getServerName() + ":" //$NON-NLS-1$
						+ request.getServerPort();
			} else {
				baseURL = ""; //$NON-NLS-1$
			}
		}

		// append application context path
		baseURL += request.getContextPath();

		HTMLRenderOption renderOption = new HTMLRenderOption();
		renderOption.setImageDirectory(session.getImageTempFolder());
		renderOption.setBaseImageURL(createBaseImageUrl(session, baseURL));
		renderOption.setBaseURL(baseURL);
		if (servletPath == null || servletPath.length() == 0) {
			servletPath = IBirtConstants.SERVLET_PATH_RUN;
		}
		renderOption.setOption(IBirtConstants.SERVLET_PATH, servletPath);
		renderOption.setEnableAgentStyleEngine(ParameterAccessor.isAgentStyle(request));
		renderOption.setSupportedImageFormats(svgFlag ? "PNG;GIF;JPG;BMP;SWF;SVG" : "PNG;GIF;JPG;BMP;SWF"); //$NON-NLS-1$ //$NON-NLS-2$
		return renderOption;
	}

	/**
	 * Creates a base image URL based on the current BIRT viewing session.
	 *
	 * @param session BIRT viewing session
	 * @param baseURL base URL
	 */
	private String createBaseImageUrl(IViewingSession session, String baseURL) {
		String sessionIdPart = ""; //$NON-NLS-1$
		// Prepare image base url.
		if (session != null) {
			sessionIdPart = ParameterAccessor.PARAM_VIEWING_SESSION_ID + "=" + session.getId() + "&"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return baseURL + IBirtConstants.SERVLET_PATH_PREVIEW + "?" + sessionIdPart + ParameterAccessor.PARAM_IMAGEID //$NON-NLS-1$
				+ "="; //$NON-NLS-1$
	}

	/**
	 * Create PDF render option.
	 *
	 * @param servletPath
	 * @param request
	 * @param pageOverflow
	 * @param isDesigner
	 * @return the PDF render option
	 */
	private PDFRenderOption createPDFRenderOption(String servletPath, HttpServletRequest request, int pageOverflow,
			boolean isDesigner, boolean isPDF) {
		String baseURL = null;
		// try to get base url from config file
		if (!isDesigner) {
			baseURL = ParameterAccessor.getBaseURL();
		}

		if (baseURL == null) {
			if (ParameterAccessor.isOpenAsAttachment(request)) {
				baseURL = request.getScheme() + "://" //$NON-NLS-1$
						+ request.getServerName() + ":" //$NON-NLS-1$
						+ request.getServerPort();
			} else {
				baseURL = ""; //$NON-NLS-1$
			}
		}

		// append application context path
		baseURL += request.getContextPath();

		PDFRenderOption renderOption = new PDFRenderOption();
		renderOption.setBaseURL(baseURL);
		if (servletPath == null || servletPath.length() == 0) {
			servletPath = IBirtConstants.SERVLET_PATH_RUN;
		}
		renderOption.setOption(IBirtConstants.SERVLET_PATH, servletPath);
		renderOption.setSupportedImageFormats(isPDF ? "PNG;GIF;JPG;BMP;SVG" : "PNG;GIF;JPG;BMP"); //$NON-NLS-1$ //$NON-NLS-2$

		// page overflow setting
		switch (pageOverflow) {
		case IBirtConstants.PAGE_OVERFLOW_AUTO:
			renderOption.setOption(IPDFRenderOption.PAGE_OVERFLOW,
					Integer.valueOf(IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES));
			break;
		case IBirtConstants.PAGE_OVERFLOW_ACTUAL:
			renderOption.setOption(IPDFRenderOption.PAGE_OVERFLOW, Integer.valueOf(IPDFRenderOption.ENLARGE_PAGE_SIZE));
			break;
		case IBirtConstants.PAGE_OVERFLOW_FITTOPAGE:
			renderOption.setOption(IPDFRenderOption.FIT_TO_PAGE, Boolean.TRUE);
			break;
		default:
			renderOption.setOption(IPDFRenderOption.PAGE_OVERFLOW,
					Integer.valueOf(IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES));
		}

		// pagebreak pagination only setting
		// Bug 238716
		renderOption.setOption(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY, Boolean.FALSE);

		return renderOption;
	}

	/**
	 * Run and render a report,
	 *
	 * @param request
	 *
	 * @param runnable
	 * @param outputStream
	 * @param format
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @deprecated
	 * @throws RemoteException
	 */
	@Deprecated
	public void runAndRenderReport(HttpServletRequest request, IReportRunnable runnable, OutputStream outputStream,
			String format, Locale locale, boolean rtl, Map parameters, boolean masterPage, boolean svgFlag)
			throws RemoteException {
		runAndRenderReport(request, runnable, outputStream, format, locale, rtl, parameters, masterPage, svgFlag, null,
				null, null, null, null, null, null);
	}

	/**
	 * Run and render a report with certain servlet path
	 *
	 * @param request
	 *
	 * @param runnable
	 * @param outputStream
	 * @param format
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @param displayTexts
	 * @param servletPath
	 * @param reportTitle
	 * @deprecated
	 * @throws RemoteException
	 */
	@Deprecated
	public void runAndRenderReport(HttpServletRequest request, IReportRunnable runnable, OutputStream outputStream,
			String format, Locale locale, boolean rtl, Map parameters, boolean masterPage, boolean svgFlag,
			Map displayTexts, String servletPath, String reportTitle) throws RemoteException {
		runAndRenderReport(request, runnable, outputStream, format, locale, rtl, parameters, masterPage, svgFlag, null,
				null, null, displayTexts, servletPath, reportTitle, null);
	}

	/**
	 * Run and render a report with certain servlet path
	 *
	 * @param request
	 *
	 * @param runnable
	 * @param outputStream
	 * @param format
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @param displayTexts
	 * @param servletPath
	 * @param reportTitle
	 * @param maxRows
	 * @throws RemoteException
	 * @deprecated
	 */
	@Deprecated
	public void runAndRenderReport(HttpServletRequest request, IReportRunnable runnable, OutputStream outputStream,
			String format, Locale locale, boolean rtl, Map parameters, boolean masterPage, boolean svgFlag,
			Map displayTexts, String servletPath, String reportTitle, Integer maxRows) throws RemoteException {
		runAndRenderReport(request, runnable, outputStream, format, locale, rtl, parameters, masterPage, svgFlag, null,
				null, null, displayTexts, servletPath, reportTitle, maxRows);
	}

	/**
	 * Run and render a report,
	 *
	 * @param request
	 *
	 * @param runnable
	 * @param outputStream
	 * @param format
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @param embeddable
	 * @param activeIds
	 * @param renderOption
	 * @param displayTexts
	 * @param iServletPath
	 * @param reportTitle
	 * @param maxRows
	 * @throws RemoteException
	 * @deprecated
	 */
	@Deprecated
	public void runAndRenderReport(HttpServletRequest request, IReportRunnable runnable, OutputStream outputStream,
			String format, Locale locale, boolean rtl, Map parameters, boolean masterPage, boolean svgFlag,
			Boolean embeddable, List activeIds, RenderOption renderOption, Map displayTexts, String iServletPath,
			String reportTitle, Integer maxRows) throws RemoteException {
		InputOptions inputOptions = new InputOptions();

		inputOptions.setOption(InputOptions.OPT_REQUEST, request);
		inputOptions.setOption(InputOptions.OPT_LOCALE, locale);
		inputOptions.setOption(InputOptions.OPT_IS_MASTER_PAGE_CONTENT, Boolean.valueOf(masterPage));
		inputOptions.setOption(InputOptions.OPT_SVG_FLAG, Boolean.valueOf(svgFlag));
		inputOptions.setOption(InputOptions.OPT_RTL, Boolean.valueOf(rtl));
		inputOptions.setOption(InputOptions.OPT_FORMAT, format);
		inputOptions.setOption(InputOptions.OPT_SERVLET_PATH, iServletPath);

		runAndRenderReport(runnable, outputStream, inputOptions, parameters, embeddable, activeIds, renderOption,
				displayTexts, reportTitle, maxRows);
	}

	/**
	 *
	 * @param runnable
	 * @param outputStream
	 * @param inputOptions
	 * @param parameters
	 * @param embeddable
	 * @param activeIds
	 * @param displayTexts
	 * @param reportTitle
	 * @param maxRows
	 * @throws RemoteException
	 */
	public void runAndRenderReport(IReportRunnable runnable, OutputStream outputStream, InputOptions inputOptions,
			Map parameters, Boolean embeddable, List activeIds, RenderOption aRenderOption, Map displayTexts,
			String reportTitle, Integer maxRows) throws RemoteException {
		assert runnable != null;

		IRunAndRenderTask runAndRenderTask = null;
		try {
			runAndRenderTask = createRunAndRenderTask(runnable, outputStream, inputOptions, parameters, embeddable,
					activeIds, aRenderOption, displayTexts, reportTitle, maxRows);
		} catch (ReportServiceException e) {
			// TODO: remove RemoteException in the method signature and throw
			// ReportServiceException directly
			throwDummyException(e);
		}

		boolean isDesigner = isDesigner(inputOptions);
		HttpServletRequest request = (HttpServletRequest) inputOptions.getOption(InputOptions.OPT_REQUEST);

		// add task into session
		BirtUtility.addTask(request, runAndRenderTask);

		try {
			runAndRenderTask.run();
		} catch (BirtException e) {
			// TODO: remove RemoteException in the method signature and
			// throw ReportServiceException directly
			throwDummyException(e);
		} finally {
			// Remove task from http session
			BirtUtility.removeTask(request);

			// Append errors
			if (isDesigner) {
				BirtUtility.error(request, runAndRenderTask.getErrors());
			}

			runAndRenderTask.close();
		}
	}

	/**
	 * @throws ReportServiceException
	 */
	private IRunAndRenderTask createRunAndRenderTask(IReportRunnable runnable, OutputStream outputStream,
			InputOptions inputOptions, Map<?, ?> parameters, Boolean embeddable, List<?> activeIds,
			RenderOption aRenderOption,
			Map<?, ?> displayTexts, String reportTitle, Integer maxRows) throws ReportServiceException {
		RenderOption renderOption = aRenderOption;

		HttpServletRequest request = (HttpServletRequest) inputOptions.getOption(InputOptions.OPT_REQUEST);
		Locale locale = (Locale) inputOptions.getOption(InputOptions.OPT_LOCALE);
		TimeZone timeZone = (TimeZone) inputOptions.getOption(InputOptions.OPT_TIMEZONE);
		Boolean isMasterPageContent = (Boolean) inputOptions.getOption(InputOptions.OPT_IS_MASTER_PAGE_CONTENT);
		boolean masterPage = isMasterPageContent == null ? true : isMasterPageContent.booleanValue();
		Boolean svgFlag = (Boolean) inputOptions.getOption(InputOptions.OPT_SVG_FLAG);
		String format = (String) inputOptions.getOption(InputOptions.OPT_FORMAT);
		String emitterId = (String) inputOptions.getOption(InputOptions.OPT_EMITTER_ID);
		boolean rtl = isRtl(inputOptions);
		boolean isDesigner = isDesigner(inputOptions);
		int pageOverflow = getPageOverflow(inputOptions);

		String iServletPath = (String) inputOptions.getOption(InputOptions.OPT_SERVLET_PATH);

		String servletPath = iServletPath;
		if (servletPath == null) {
			servletPath = request.getServletPath();
		}

		IRunAndRenderTask runAndRenderTask = engine.createRunAndRenderTask(runnable);
		runAndRenderTask.setLocale(locale);

		com.ibm.icu.util.TimeZone tz = BirtUtility.toICUTimeZone(timeZone);
		if (tz != null) {
			runAndRenderTask.setTimeZone(tz);
		}

		if (parameters != null) {
			runAndRenderTask.setParameterValues(parameters);
		}

		// Set display text for selected parameters
		if (displayTexts != null) {
			Iterator<?> keys = displayTexts.keySet().iterator();
			while (keys.hasNext()) {
				String paramName = DataUtil.getString(keys.next());
				if (displayTexts.get(paramName) instanceof ArrayList) {
					ArrayList<String> displayTextList = (ArrayList<String>) displayTexts.get(paramName);
					String[] displayText = displayTextList.toArray(new String[displayTextList.size()]);
					runAndRenderTask.setParameterDisplayText(paramName, displayText);
				} else {
					String displayText = DataUtil.getString(displayTexts.get(paramName));
					runAndRenderTask.setParameterDisplayText(paramName, displayText);
				}
			}
		}

		// set MaxRows settings
		if (maxRows != null) {
			runAndRenderTask.setMaxRowsPerQuery(maxRows.intValue());
		}

		// set app context
		Map<String, Object> context = BirtUtility.getAppContext(request);
		runAndRenderTask.setAppContext(context);

		ViewerHTMLActionHandler handler = new ViewerHTMLActionHandler(locale, timeZone, rtl, masterPage, format,
				svgFlag, Boolean.toString(isDesigner));
		handler.setPageOverflow(pageOverflow);

		String resourceFolder = ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_RESOURCE_FOLDER);
		handler.setResourceFolder(resourceFolder);

		IViewingSession session = ViewingSessionUtil.getSession(request);
		if (!ParameterAccessor.isPDFLayout(format)) {
			if (session != null) {
				handler.setViewingSessionId(session.getId());
			} else {
				throw new IllegalStateException(
						BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_NO_VIEWING_SESSION));
			}
		}

		// Render options
		if (renderOption == null) {
			if (ParameterAccessor.isPDFLayout(format)) {
				renderOption = createPDFRenderOption(servletPath, request, pageOverflow, isDesigner,
						IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase(format));
			} else {
				// If format isn't HTML, force SVG to false
				if (!IBirtConstants.HTML_RENDER_FORMAT.equalsIgnoreCase(format)) {
					svgFlag = false;
				}

				renderOption = createHTMLRenderOption(svgFlag, servletPath, request, session);
			}
		}

		renderOption.setOutputStream(outputStream);
		renderOption.setOutputFormat(format);
		renderOption.setEmitterID(emitterId);
		renderOption.setOption(IHTMLRenderOption.MASTER_PAGE_CONTENT, Boolean.valueOf(masterPage));
		renderOption.setActionHandler(handler);

		if (reportTitle != null) {
			renderOption.setOption(IHTMLRenderOption.HTML_TITLE, reportTitle);
		}

		if (renderOption instanceof IHTMLRenderOption) {
			boolean isEmbeddable = false;
			if (embeddable != null) {
				isEmbeddable = embeddable.booleanValue();
			}

			if (IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(servletPath)) {
				isEmbeddable = true;
			}

			((IHTMLRenderOption) renderOption).setEmbeddable(isEmbeddable);
		}

		renderOption.setOption(IHTMLRenderOption.INSTANCE_ID_LIST, activeIds);

		// initialize emitter configs
		initializeEmitterConfigs(request, renderOption.getOptions());

		runAndRenderTask.setRenderOption(renderOption);

		return runAndRenderTask;
	}

	/**
	 * @param e
	 * @throws DummyRemoteException
	 */
	private void throwDummyException(Exception e) throws DummyRemoteException {
		if (e instanceof ReportServiceException) {
			throw new DummyRemoteException(e);
		}
		throw new DummyRemoteException(new ReportServiceException(e.getLocalizedMessage(), e));
	}

	/**
	 * @param inputOptions
	 * @return
	 */
	private boolean isRtl(InputOptions inputOptions) {
		Boolean isRtl = (Boolean) inputOptions.getOption(InputOptions.OPT_RTL);
		boolean rtl = isRtl == null ? false : isRtl.booleanValue();
		return rtl;
	}

	/**
	 * @param inputOptions
	 * @return
	 */
	private int getPageOverflow(InputOptions inputOptions) {
		Integer pageOverflowInt = (Integer) inputOptions.getOption(InputOptions.OPT_PAGE_OVERFLOW);
		int pageOverflow = (pageOverflowInt != null) ? pageOverflowInt.intValue() : 0;
		return pageOverflow;
	}

	/**
	 * @param inputOptions
	 * @return
	 */
	private boolean isDesigner(InputOptions inputOptions) {
		Boolean isDesignerBool = (Boolean) inputOptions.getOption(InputOptions.OPT_IS_DESIGNER);
		boolean isDesigner = false;
		if (isDesignerBool != null) {
			isDesigner = isDesignerBool.booleanValue();
		}
		return isDesigner;
	}

	/**
	 * Fills dynamic options with parameters from request.
	 */
	private void initializeEmitterConfigs(HttpServletRequest request, Map config) {
		if (config == null) {
			return;
		}

		for (Iterator itr = request.getParameterMap().entrySet().iterator(); itr.hasNext();) {
			Entry entry = (Entry) itr.next();

			String name = String.valueOf(entry.getKey());

			// only process parameters start with "__"
			if (name.startsWith("__")) //$NON-NLS-1$
			{
				// TODO: don't use ParameterAccessor directly (fails in taglib
				// mode)
				config.put(name.substring(2), ParameterAccessor.getParameter(request, name));
			}
		}
	}

	/**
	 * Run report.
	 *
	 * @param request
	 *
	 * @param runnable
	 * @param documentName
	 * @param locale
	 * @param parameters
	 * @deprecated
	 * @throws RemoteException
	 */
	@Deprecated
	public void runReport(HttpServletRequest request, IReportRunnable runnable, String documentName, Locale locale,
			Map parameters) throws RemoteException {
		runReport(request, runnable, documentName, locale, parameters, null, null);
	}

	/**
	 * Run report.
	 *
	 * @param request
	 *
	 * @param runnable
	 * @param documentName
	 * @param locale
	 * @param parameters
	 * @param displayTexts
	 * @deprecated
	 * @throws RemoteException
	 */
	@Deprecated
	public void runReport(HttpServletRequest request, IReportRunnable runnable, String documentName, Locale locale,
			Map parameters, Map displayTexts) throws RemoteException {
		runReport(request, runnable, documentName, locale, parameters, displayTexts, null);
	}

	/**
	 *
	 * @param request
	 * @param runnable
	 * @param documentName
	 * @param locale
	 * @param parameters
	 * @param displayTexts
	 * @param object
	 * @deprecated
	 */
	@Deprecated
	private void runReport(HttpServletRequest request, IReportRunnable runnable, String documentName, Locale locale,
			Map parameters, Map displayTexts, Object object) throws RemoteException {
		runReport(request, runnable, documentName, locale, null, parameters, displayTexts, null);
	}

	/**
	 * Run report.
	 *
	 * @param request
	 *
	 * @param runnable
	 * @param documentName
	 * @param locale
	 * @param timeZone
	 * @param parameters
	 * @param displayTexts
	 * @param maxRows
	 * @return list of exceptions which occurred during the run or null
	 * @throws RemoteException
	 */
	public List<Exception> runReport(HttpServletRequest request, IReportRunnable runnable, String documentName,
			Locale locale, TimeZone timeZone, Map<?, ?> parameters, Map<?, ?> displayTexts, Integer maxRows)
			throws RemoteException {
		assert runnable != null;

		// Prepare the run report task.
		IRunTask runTask;
		runTask = engine.createRunTask(runnable);
		runTask.setLocale(locale);

		com.ibm.icu.util.TimeZone tz = BirtUtility.toICUTimeZone(timeZone);
		if (tz != null) {
			runTask.setTimeZone(tz);
		}

		runTask.setParameterValues(parameters);

		// set MaxRows settings
		if (maxRows != null) {
			runTask.setMaxRowsPerQuery(maxRows.intValue());
		}

		// add task into session
		BirtUtility.addTask(request, runTask);

		// Set display text for selected parameters
		if (displayTexts != null) {
			Iterator<?> keys = displayTexts.keySet().iterator();
			while (keys.hasNext()) {
				String paramName = DataUtil.getString(keys.next());
				if (displayTexts.get(paramName) instanceof ArrayList) {
					ArrayList<String> displayTextList = (ArrayList<String>) displayTexts.get(paramName);
					String[] displayText = displayTextList.toArray(new String[displayTextList.size()]);
					runTask.setParameterDisplayText(paramName, displayText);
				} else {
					String displayText = DataUtil.getString(displayTexts.get(paramName));
					runTask.setParameterDisplayText(paramName, displayText);
				}
			}
		}

		// set app context
		Map<String, Object> context = BirtUtility.getAppContext(request);
		runTask.setAppContext(context);

		// Run report.
		try {
			runTask.run(documentName);
		} catch (BirtException e) {
			// clear document file
			File doc = new File(documentName);
			if (doc != null) {
				doc.delete();
			}

			throwDummyException(e);
		} finally {
			// Remove task from http session
			BirtUtility.removeTask(request);

			// Append errors
			List<Exception> errors = runTask.getErrors();
			if (ParameterAccessor.isDesigner()) {
				BirtUtility.error(request, runTask.getErrors());
			}

			runTask.close();

			// check for non-fatal errors
			if (!errors.isEmpty()) {
				return errors;
			}
		}
		return null;
	}

	/**
	 * Render report page.
	 *
	 * @param request
	 * @param reportDocument
	 * @param pageNumber
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @deprecated
	 * @return report page content
	 * @throws RemoteException
	 */
	@Deprecated
	public ByteArrayOutputStream renderReport(HttpServletRequest request, IReportDocument reportDocument,
			long pageNumber, boolean masterPage, boolean svgFlag, List activeIds, Locale locale, boolean rtl)
			throws RemoteException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		renderReport(out, request, reportDocument, null, pageNumber, null, masterPage, svgFlag, activeIds, locale, rtl,
				null);
		return out;
	}

	/**
	 * Render report page.
	 *
	 * @param request
	 * @param reportDocument
	 * @param pageNumber
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @return report page content
	 * @throws RemoteException
	 */
	public ByteArrayOutputStream renderReport(HttpServletRequest request, IReportDocument reportDocument, String format,
			long pageNumber, boolean masterPage, boolean svgFlag, List activeIds, Locale locale, boolean rtl)
			throws RemoteException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		renderReport(out, request, reportDocument, format, pageNumber, null, masterPage, svgFlag, activeIds, locale,
				rtl, null);
		return out;
	}

	/**
	 * Render report page.
	 *
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param pageNumber
	 * @param pageRange
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @deprecated
	 * @throws RemoteException
	 */
	@Deprecated
	public void renderReport(OutputStream os, HttpServletRequest request, IReportDocument reportDocument,
			long pageNumber, String pageRange, boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl, String iServletPath) throws RemoteException {
		renderReport(os, request, reportDocument, null, pageNumber, pageRange, masterPage, svgFlag, activeIds, locale,
				rtl, iServletPath);
	}

	/**
	 * Render report page.
	 *
	 * @param out
	 * @param request
	 * @param reportDocument
	 * @param format
	 * @param pageNumber
	 * @param pageRange
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @throws RemoteException
	 * @deprecated use renderReport with InputOptions instead
	 */
	@Deprecated
	public void renderReport(OutputStream out, HttpServletRequest request, IReportDocument reportDocument,
			String format, long pageNumber, String pageRange, boolean masterPage, boolean svgFlag, List activeIds,
			Locale locale, boolean rtl, String iServletPath) throws RemoteException {
		InputOptions inputOptions = new InputOptions();

		inputOptions.setOption(InputOptions.OPT_REQUEST, request);
		inputOptions.setOption(InputOptions.OPT_LOCALE, locale);
		inputOptions.setOption(InputOptions.OPT_IS_MASTER_PAGE_CONTENT, Boolean.valueOf(masterPage));
		inputOptions.setOption(InputOptions.OPT_SVG_FLAG, Boolean.valueOf(svgFlag));
		inputOptions.setOption(InputOptions.OPT_RTL, Boolean.valueOf(rtl));
		inputOptions.setOption(InputOptions.OPT_FORMAT, format);
		inputOptions.setOption(InputOptions.OPT_SERVLET_PATH, iServletPath);
		renderReport(out, reportDocument, pageNumber, pageRange, inputOptions, activeIds);
	}

	/**
	 * Render report page.
	 *
	 * @param out
	 * @param reportDocument
	 * @param pageNumber
	 * @param pageRange
	 * @param inputOptions
	 * @param activeIds
	 * @throws RemoteException
	 */
	public void renderReport(OutputStream out, IReportDocument reportDocument, long pageNumber, String pageRange,
			InputOptions inputOptions, List activeIds) throws RemoteException {
		if (out == null) {
			return;
		}

		HttpServletRequest request = (HttpServletRequest) inputOptions.getOption(InputOptions.OPT_REQUEST);
		String format = (String) inputOptions.getOption(InputOptions.OPT_FORMAT);
		String iServletPath = (String) inputOptions.getOption(InputOptions.OPT_SERVLET_PATH);

		IRenderTask renderTask = null;
		try {
			renderTask = createRenderTask(out, reportDocument, inputOptions, pageNumber, activeIds);
		} catch (ReportServiceException e) {
			// TODO: remove RemoteException in the method signature and throw
			// ReportServiceException directly
			throwDummyException(e);
		}

		// get servlet path
		String servletPath = iServletPath;
		if (servletPath == null) {
			servletPath = request.getServletPath();
		}

		// Render designated page.
		try {
			if (pageNumber > 0) {
				renderTask.setPageNumber(pageNumber);
			}

			if (pageRange != null) {
				if (!IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(servletPath)
						|| !ParameterAccessor.PARAM_FORMAT_HTML.equalsIgnoreCase(format)) {
					renderTask.setPageRange(pageRange);
				}
			}

			renderTask.render();
		} catch (EngineException e) {
			// TODO: remove RemoteException in the method signature and
			// throw ReportServiceException directly
			throwDummyException(e);
		} finally {
			// Remove task from http session
			BirtUtility.removeTask(request);

			// Append errors
			if (ParameterAccessor.isDesigner()) {
				BirtUtility.error(request, renderTask.getErrors());
			}

			renderTask.close();
		}
	}

	/**
	 * Creates a new render task and configure it.
	 *
	 * @param out            output stream
	 * @param reportDocument report document
	 * @param inputOptions   input options
	 * @param pageNumber     page number
	 * @param activeIds      active IDs
	 * @return configured render task
	 * @throws ViewingSessionExpiredException
	 */
	private IRenderTask createRenderTask(OutputStream out, IReportDocument reportDocument, InputOptions inputOptions,
			long pageNumber, List activeIds) throws ReportServiceException {
		HttpServletRequest request = (HttpServletRequest) inputOptions.getOption(InputOptions.OPT_REQUEST);
		Locale locale = (Locale) inputOptions.getOption(InputOptions.OPT_LOCALE);
		TimeZone timeZone = (TimeZone) inputOptions.getOption(InputOptions.OPT_TIMEZONE);
		Boolean isMasterPageContent = (Boolean) inputOptions.getOption(InputOptions.OPT_IS_MASTER_PAGE_CONTENT);
		boolean masterPage = isMasterPageContent == null ? true : isMasterPageContent.booleanValue();
		Boolean svgFlag = (Boolean) inputOptions.getOption(InputOptions.OPT_SVG_FLAG);
		String format = (String) inputOptions.getOption(InputOptions.OPT_FORMAT);
		String emitterId = (String) inputOptions.getOption(InputOptions.OPT_EMITTER_ID);

		String iServletPath = (String) inputOptions.getOption(InputOptions.OPT_SERVLET_PATH);
		boolean rtl = isRtl(inputOptions);
		boolean isDesigner = isDesigner(inputOptions);
		int pageOverflow = getPageOverflow(inputOptions);

		if (reportDocument == null) {
			throw new ReportServiceException(
					BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_NO_REPORT_DOCUMENT));
		}

		// get servlet path
		String servletPath = iServletPath;
		if (servletPath == null) {
			servletPath = request.getServletPath();
		}

		// Create render task.
		IRenderTask renderTask = engine.createRenderTask(reportDocument);

		// add task into session
		BirtUtility.addTask(request, renderTask);

		// set app context
		Map context = BirtUtility.getAppContext(request);
		renderTask.setAppContext(context);

		RenderOption renderOption = null;

		if (format == null) {
			format = ParameterAccessor.getFormat(request);
		}

		if (ParameterAccessor.isPDFLayout(format)) {
			renderOption = createPDFRenderOption(servletPath, request, pageOverflow, ParameterAccessor.isDesigner(),
					IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase(format));
		} else {
			// If format isn't HTML, force SVG to false
			if (!IBirtConstants.HTML_RENDER_FORMAT.equalsIgnoreCase(format)) {
				svgFlag = false;
			}

			IViewingSession session = ViewingSessionUtil.getSession(request);
			if (session == null) {
				throw new IllegalStateException(
						BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_NO_VIEWING_SESSION));
			}
			renderOption = createHTMLRenderOption(svgFlag, servletPath, request, session);
		}

		// If not excel format, set HTMLPagination to true.
		if (!IBirtConstants.EXCEL_RENDER_FORMAT.equalsIgnoreCase(format)) {
			((IRenderOption) renderOption).setOption(IRenderOption.HTML_PAGINATION, Boolean.TRUE);
		}

		renderOption.setOutputStream(out);
		renderOption.setOutputFormat(format);
		renderOption.setEmitterID(emitterId);

		ViewerHTMLActionHandler handler = null;
		if (ParameterAccessor.isPDFLayout(format)) {
			handler = new ViewerHTMLActionHandler(reportDocument, pageNumber, locale, timeZone, false, rtl, masterPage,
					format, svgFlag, Boolean.toString(isDesigner));
		} else {
			boolean isEmbeddable = false;
			if (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(servletPath)
					|| IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase(servletPath)) {
				isEmbeddable = true;
			}
			if (renderOption instanceof IHTMLRenderOption) {
				((IHTMLRenderOption) renderOption).setEmbeddable(isEmbeddable);
			}

			renderOption.setOption(IHTMLRenderOption.INSTANCE_ID_LIST, activeIds);
			renderOption.setOption(IHTMLRenderOption.MASTER_PAGE_CONTENT, Boolean.valueOf(masterPage));
			handler = new ViewerHTMLActionHandler(reportDocument, pageNumber, locale, timeZone, isEmbeddable, rtl,
					masterPage, format, svgFlag, Boolean.toString(isDesigner));

			IViewingSession session = ViewingSessionUtil.getSession(request);
			if (session == null) {
				throw new ReportServiceException(
						BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_NO_VIEWING_SESSION));
			}
			handler.setViewingSessionId(session.getId());
		}
		handler.setPageOverflow(pageOverflow);

		String resourceFolder = ParameterAccessor.getParameter(request, ParameterAccessor.PARAM_RESOURCE_FOLDER);
		handler.setResourceFolder(resourceFolder);
		renderOption.setActionHandler(handler);

		// initialize emitter configs
		// (only non-reportlet mode, reportlet mode uses pageNumber == -1)
		if (pageNumber >= 0) {
			initializeEmitterConfigs(request, renderOption.getOptions());
		}

		// String reportTitle = ParameterAccessor.htmlDecode(
		// ParameterAccessor.getTitle( request ) );
		// if ( reportTitle != null )
		// renderOption.setOption( IHTMLRenderOption.HTML_TITLE, reportTitle );

		renderTask.setRenderOption(renderOption);
		renderTask.setLocale(locale);

		com.ibm.icu.util.TimeZone tz = BirtUtility.toICUTimeZone(timeZone);
		if (tz != null) {
			renderTask.setTimeZone(tz);
		}

		return renderTask;
	}

	/**
	 * Render reportlet page with certain servlet path
	 *
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param reportletId
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @deprecated
	 * @throws RemoteException
	 */
	@Deprecated
	public void renderReportlet(OutputStream os, HttpServletRequest request, IReportDocument reportDocument,
			String reportletId, boolean masterPage, boolean svgFlag, List activeIds, Locale locale, boolean rtl)
			throws RemoteException {
		renderReportlet(os, request, reportDocument, reportletId, null, masterPage, svgFlag, activeIds, locale, rtl,
				null);
	}

	/**
	 * Render reportlet page.
	 *
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param reportletId
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @deprecated
	 * @throws RemoteException
	 */
	@Deprecated
	public void renderReportlet(OutputStream os, HttpServletRequest request, IReportDocument reportDocument,
			String reportletId, boolean masterPage, boolean svgFlag, List activeIds, Locale locale, boolean rtl,
			String iServletPath) throws RemoteException {
		renderReportlet(os, request, reportDocument, reportletId, null, masterPage, svgFlag, activeIds, locale, rtl,
				iServletPath);
	}

	/**
	 * Render reportlet.
	 *
	 * @param request
	 * @param reportDocument
	 * @param reportletId
	 * @param format
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @return outputstream
	 * @throws RemoteException
	 * @deprecated use
	 *             {@link #renderReportlet(OutputStream, IReportDocument, InputOptions, String, List)}
	 */
	@Deprecated
	public OutputStream renderReportlet(HttpServletRequest request, IReportDocument reportDocument, String reportletId,
			String format, boolean masterPage, boolean svgFlag, List activeIds, Locale locale, boolean rtl,
			String iServletPath) throws RemoteException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		renderReportlet(out, request, reportDocument, reportletId, format, masterPage, svgFlag, activeIds, locale, rtl,
				iServletPath);
		return out;
	}

	/**
	 * Render reportlet page.
	 *
	 * @param out
	 * @param request
	 * @param reportDocument
	 * @param reportletId
	 * @param format
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @throws RemoteException
	 * @deprecated use
	 *             {@link #renderReportlet(OutputStream, IReportDocument, InputOptions, String, List)}
	 */
	@Deprecated
	public void renderReportlet(OutputStream out, HttpServletRequest request, IReportDocument reportDocument,
			String reportletId, String format, boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl, String iServletPath) throws RemoteException {
		InputOptions inputOptions = new InputOptions();

		inputOptions.setOption(InputOptions.OPT_REQUEST, request);
		inputOptions.setOption(InputOptions.OPT_LOCALE, locale);
		inputOptions.setOption(InputOptions.OPT_IS_MASTER_PAGE_CONTENT, Boolean.valueOf(masterPage));
		inputOptions.setOption(InputOptions.OPT_SVG_FLAG, Boolean.valueOf(svgFlag));
		inputOptions.setOption(InputOptions.OPT_RTL, Boolean.valueOf(rtl));
		inputOptions.setOption(InputOptions.OPT_FORMAT, format);
		inputOptions.setOption(InputOptions.OPT_SERVLET_PATH, iServletPath);
		renderReportlet(out, reportDocument, inputOptions, reportletId, activeIds);
	}

	/**
	 * Render reportlet page.
	 *
	 * @param out
	 * @param reportDocument
	 * @param inputOptions
	 * @param reportletId
	 * @param activeIds
	 * @throws RemoteException
	 */
	public void renderReportlet(OutputStream out, IReportDocument reportDocument, InputOptions inputOptions,
			String reportletId, List activeIds) throws RemoteException {
		if (out == null) {
			return;
		}

		HttpServletRequest request = (HttpServletRequest) inputOptions.getOption(InputOptions.OPT_REQUEST);

		IRenderTask renderTask = null;
		try {
			renderTask = createRenderTask(out, reportDocument, inputOptions, -1, activeIds);
		} catch (ReportServiceException e) {
			// TODO: remove RemoteException in the method signature and
			// throw ReportServiceException directly
			throwDummyException(e);
		}

		// Render designated page.
		try {
			if (ParameterAccessor.isIidReportlet(request)) {
				InstanceID instanceId = InstanceID.parse(reportletId);
				renderTask.setInstanceID(instanceId);
			} else {
				renderTask.setReportlet(reportletId);
			}

			renderTask.render();
		} catch (EngineException e) {
			// TODO: remove RemoteException in the method signature and
			// throw ReportServiceException directly
			throwDummyException(e);
		} finally {
			// Remove task from http session
			BirtUtility.removeTask(request);

			// Append errors
			if (ParameterAccessor.isDesigner()) {
				BirtUtility.error(request, renderTask.getErrors());
			}

			renderTask.close();
		}
	}

	/**
	 * Get query result sets.
	 *
	 * @param document
	 * @return the result sets from the document
	 * @throws RemoteException
	 */
	public ResultSet[] getResultSets(IReportDocument document) throws RemoteException {
		assert document != null;

		ResultSet[] resultSetArray = null;

		IDataExtractionTask dataTask = engine.createDataExtractionTask(document);

		try {
			List resultSets = dataTask.getResultSetList();
			resultSetArray = new ResultSet[resultSets.size()];

			if (resultSets.size() > 0) {
				for (int k = 0; k < resultSets.size(); k++) {
					resultSetArray[k] = new ResultSet();
					IResultSetItem resultSetItem = (IResultSetItem) resultSets.get(k);
					assert resultSetItem != null;

					resultSetArray[k].setQueryName(resultSetItem.getResultSetName());

					IResultMetaData metaData = resultSetItem.getResultMetaData();
					assert metaData != null;

					List<Column> columnArray = new ArrayList<>();
					for (int i = 0; i < metaData.getColumnCount(); i++) {
						if (!metaData.getAllowExport(i)) {
							continue;
						}
						Column column = new Column();

						String name = metaData.getColumnName(i);
						column.setName(name);

						String label = metaData.getColumnLabel(i);
						if (label == null || label.length() <= 0) {
							label = name;
						}
						column.setLabel(label);

						column.setVisibility(true);

						columnArray.add(column);
					}
					resultSetArray[k].setColumn(columnArray.toArray(new Column[0]));
				}
			}
		} catch (Exception e) {
			// TODO: remove RemoteException in the method signature and
			// throw ReportServiceException directly
			throwDummyException(e);
		} finally {
			dataTask.close();
		}

		return resultSetArray;
	}

	/**
	 * Extract data that call user extended extension
	 *
	 * @param document
	 * @param aExtractFormat
	 * @param extractExtension
	 * @param resultSetName
	 * @param instanceId
	 * @param columns
	 * @param locale
	 * @param timeZone
	 * @param options
	 * @param out
	 * @throws RemoteException
	 */
	public void extractDataEx(IReportDocument document, String aExtractFormat, String extractExtension,
			String resultSetName, String instanceId, Collection<String> columns, Locale locale, TimeZone timeZone,
			Map options,
			OutputStream out) throws RemoteException {
		assert document != null;
		IDataExtractionTask dataTask = null;
		String extractFormat = aExtractFormat;
		try {
			if (extractFormat == null || "".equals(extractFormat)) {
				extractFormat = ParameterAccessor.getExtractFormat(extractExtension);
			}

			String[] columnNames = DataExtractionParameterUtil.getColumnNames(columns);

			// create DataExtractionTask
			dataTask = engine.createDataExtractionTask(document);

			// set resultSetName
			if (resultSetName != null) {
				dataTask.selectResultSet(resultSetName);
			}

			// set instanceId
			if (instanceId != null) {
				dataTask.setInstanceID(InstanceID.parse(instanceId));
			}

			// set locale information
			dataTask.setLocale(locale);

			com.ibm.icu.util.TimeZone tz = BirtUtility.toICUTimeZone(timeZone);
			if (tz != null) {
				dataTask.setTimeZone(tz);
			}

			DataExtractionOption extractOption = null;

			// create DataExtractionOption object
			if (DataExtractionParameterUtil.EXTRACTION_FORMAT_CSV.equals(extractFormat)) {
				// CSV data extraction option
				extractOption = DataExtractionParameterUtil.createCSVOptions(columnNames, locale, timeZone, options);

			} else {
				// default to common data extraction option
				extractOption = DataExtractionParameterUtil.createOptions(null, columnNames, locale, timeZone, options);
			}

			extractOption.setOutputFormat(extractFormat);
			extractOption.setExtension(extractExtension);
			extractOption.setOutputStream(out);

			// set selected columns
			if (columnNames != null && columnNames.length > 0) {
				dataTask.selectColumns(columnNames);
			}

			// do extract
			dataTask.extract(extractOption);
		} catch (BirtException e) {
			throwDummyException(e);
		} finally {
			if (dataTask != null) {
				dataTask.close();
			}
		}

	}

	/**
	 * Extract data.
	 *
	 * @param document
	 * @param resultSetName
	 * @param columns
	 * @param locale
	 * @param outputStream
	 * @param encoding
	 * @throws RemoteException
	 * @deprecated use
	 *             {@link #extractDataEx(IReportDocument, String, String, String, String, Collection, Locale, Map, OutputStream)}
	 */
	@Deprecated
	public void extractData(IReportDocument document, String resultSetName, Collection<String> columns, Locale locale,
			OutputStream outputStream, String encoding) throws RemoteException {
		extractData(document, resultSetName, columns, locale, outputStream, encoding,
				DataExtractionParameterUtil.DEFAULT_SEP.charAt(0), false);
	}

	/**
	 * Extract data.
	 *
	 * @param document
	 * @param resultSetName
	 * @param columns
	 * @param locale
	 * @param outputStream
	 * @param encoding
	 * @param sep
	 * @param isExportDataType
	 * @throws RemoteException
	 * @deprecated use
	 *             {@link #extractDataEx(IReportDocument, String, String, String, String, Collection, Locale, Map, OutputStream)}
	 */
	@Deprecated
	public void extractData(IReportDocument document, String resultSetName, Collection columns, Locale locale,
			OutputStream outputStream, String encoding, char sep, boolean isExportDataType) throws RemoteException {
		assert document != null;
		assert resultSetName != null && resultSetName.length() > 0;
		assert columns != null && !columns.isEmpty();

		Map<String, Object> options = new HashMap<>();
		options.put(DataExtractionParameterUtil.PARAM_SEP, Character.toString(sep));
		options.put(DataExtractionParameterUtil.PARAM_EXPORT_DATATYPE, Boolean.valueOf(isExportDataType));
		options.put(DataExtractionParameterUtil.PARAM_EXPORT_ENCODING, encoding);
		extractDataEx(document, DataExtractionParameterUtil.EXTRACTION_FORMAT_CSV,
				DataExtractionParameterUtil.EXTRACTION_EXTENSION_CSV, resultSetName, null, columns, locale, null,
				options, outputStream);
	}

	/**
	 * Prepare the report parameters.
	 *
	 * @param request
	 * @param task
	 * @param configVars
	 * @param locale
	 * @deprecated
	 * @return map of the request parameters
	 */
	@Deprecated
	public HashMap parseParameters(HttpServletRequest request, IGetParameterDefinitionTask task, Map configVars,
			Locale locale) {
		assert task != null;
		HashMap<String, Object> params = new HashMap<>();

		Collection<IScalarParameterDefn> parameterList = task.getParameterDefns(false);
		for (Iterator<IScalarParameterDefn> iter = parameterList.iterator(); iter.hasNext();) {
			IScalarParameterDefn parameterObj = iter.next();

			String paramValue = null;
			Object paramValueObj = null;

			// ScalarParameterHandle paramHandle = ( ScalarParameterHandle )
			// parameterObj
			// .getHandle( );
			String paramName = parameterObj.getName();
			String format = parameterObj.getDisplayFormat();

			// Get default value from task
			ReportParameterConverter converter = new ReportParameterConverter(format, locale);

			if (ParameterAccessor.isReportParameterExist(request, paramName)) {
				// Get value from http request
				paramValue = ParameterAccessor.getReportParameter(request, paramName, paramValue);
				paramValueObj = converter.parse(paramValue, parameterObj.getDataType());
			} else if (ParameterAccessor.isDesigner() && configVars.containsKey(paramName)) {
				// Get value from test config
				String configValue = (String) configVars.get(paramName);
				ReportParameterConverter cfgConverter = new ReportParameterConverter(format, Locale.US);
				paramValueObj = cfgConverter.parse(configValue, parameterObj.getDataType());
			} else {
				paramValueObj = task.getDefaultValue(parameterObj.getName());
			}

			params.put(paramName, paramValueObj);
		}

		return params;
	}

	/**
	 * Check whether missing parameter or not.
	 *
	 * @param task
	 * @param parameters
	 * @deprecated
	 * @return true if all the parameter values are valid, otherwise false
	 */
	@Deprecated
	public boolean validateParameters(IGetParameterDefinitionTask task, Map parameters) {
		assert task != null;
		assert parameters != null;

		boolean missingParameter = false;

		Collection<IScalarParameterDefn> parameterList = task.getParameterDefns(false);
		for (Iterator<IScalarParameterDefn> iter = parameterList.iterator(); iter.hasNext();) {
			IScalarParameterDefn parameterObj = iter.next();

			String parameterName = parameterObj.getName();
			Object parameterValue = parameters.get(parameterName);

			if (parameterObj.isHidden()) {
				continue;
			}

			if (parameterValue == null && !parameterObj.allowNull()) {
				missingParameter = true;
				break;
			}

			if (IParameterDefn.TYPE_STRING == parameterObj.getDataType()) {
				String parameterStringValue = (String) parameterValue;
				if (parameterStringValue != null && parameterStringValue.length() <= 0 && !parameterObj.allowBlank()) {
					missingParameter = true;
					break;
				}
			}
		}

		return missingParameter;
	}

	/**
	 * uses to clear the data cach.
	 *
	 * @param dataSet the dataset handle
	 * @throws BirtException
	 */
	public void clearCache(DataSetHandle dataSet) throws BirtException {
		DataSessionContext context;
		DataRequestSession requestSession = null;
		try {
			context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION, dataSet.getModuleHandle(),
					null);
			requestSession = DataRequestSession.newSession(context);

			IModelAdapter modelAdaptor = requestSession.getModelAdaptor();
			DataSourceHandle dataSource = dataSet.getDataSource();

			IBaseDataSourceDesign sourceDesign = modelAdaptor.adaptDataSource(dataSource);
			IBaseDataSetDesign dataSetDesign = modelAdaptor.adaptDataSet(dataSet);

			requestSession.clearCache(sourceDesign, dataSetDesign);
		} finally {
			if (requestSession != null) {
				requestSession.shutdown();
			}
		}
	}

	/**
	 * Collects all the distinct values for the given element and bindColumnName.
	 * This method will traverse the design tree for the given element and get the
	 * nearest binding column holder of it. The nearest binding column holder must
	 * be a list or table item, and it defines a distinct data set and bingding
	 * columns in it. If the element is null, binding name is empty or the binding
	 * column holder is not found, then return <code>Collections.EMPTY_LIST</code>.
	 * Caller can specify the max row number and start row number by implement the
	 * interface IRequestInfo.
	 *
	 * @param bindingName
	 * @param elementHandle
	 * @param requestInfo
	 * @return list of available column value
	 * @throws BirtException
	 */

	public List getColumnValueSet(String bindingName, DesignElementHandle elementHandle, IRequestInfo requestInfo)
			throws BirtException {
		if (bindingName == null || elementHandle == null || !(elementHandle instanceof ReportItemHandle)) {
			return Collections.EMPTY_LIST;
		}

		// if there is no effective holder of bindings, return empty
		ReportItemHandle reportItem = getBindingHolder(elementHandle);
		if (reportItem == null) {
			return Collections.EMPTY_LIST;
		}

		List selectValueList = new ArrayList();
		DataRequestSession session = DataRequestSession.newSession(
				new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION, reportItem.getModuleHandle()));
		selectValueList.addAll(session.getColumnValueSet(reportItem.getDataSet(), reportItem.paramBindingsIterator(),
				reportItem.columnBindingsIterator(), bindingName, requestInfo));
		session.shutdown();

		return selectValueList;
	}

	/**
	 * Collects all the distinct values for the given element and bindColumnName.
	 * This method will traverse the design tree for the given element and get the
	 * nearest binding column holder of it. The nearest binding column holder must
	 * be a list or table item, and it defines a distinct data set and bingding
	 * columns in it. If the element is null, binding name is empty or the binding
	 * column holder is not found, then return <code>Collections.EMPTY_LIST</code>.
	 *
	 * @param bindingName
	 * @param elementHandle
	 * @return list of the avaliable column value
	 * @throws BirtException
	 */

	public List getColumnValueSet(String bindingName, DesignElementHandle elementHandle) throws BirtException {
		if (bindingName == null || elementHandle == null || !(elementHandle instanceof ReportItemHandle)) {
			return Collections.EMPTY_LIST;
		}

		// if there is no effective holder of bindings, return empty
		ReportItemHandle reportItem = getBindingHolder(elementHandle);
		if (reportItem == null) {
			return Collections.EMPTY_LIST;
		}

		List selectValueList = new ArrayList();
		DataRequestSession session = DataRequestSession.newSession(
				new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION, reportItem.getModuleHandle()));
		selectValueList.addAll(session.getColumnValueSet(reportItem.getDataSet(), reportItem.paramBindingsIterator(),
				reportItem.columnBindingsIterator(), bindingName));
		session.shutdown();

		return selectValueList;
	}

	/**
	 * Returns the element handle which can save binding columns the given element
	 *
	 * @param handle the handle of the element which needs binding columns
	 * @return the holder for the element,or itself if no holder available
	 */

	private ReportItemHandle getBindingHolder(DesignElementHandle handle) {
		if (handle instanceof ReportElementHandle) {
			if (handle instanceof ListingHandle) {
				return (ReportItemHandle) handle;
			}
			if (handle instanceof ReportItemHandle) {
				if (((ReportItemHandle) handle).getDataSet() != null
						|| ((ReportItemHandle) handle).columnBindingsIterator().hasNext()) {
					return (ReportItemHandle) handle;
				}
			}
			ReportItemHandle result = getBindingHolder(handle.getContainer());
			if (result == null && handle instanceof ReportItemHandle) {
				result = (ReportItemHandle) handle;
			}
			return result;
		}
		return null;
	}

	/**
	 * Gets the mime-type of the given format.
	 *
	 * @param format
	 * @return the mime-type of the given format.
	 *
	 * @deprecated use ParameterAccessor#getEmitterMimeType(String,String)
	 */
	@Deprecated
	public String getMIMEType(String format) {
		return engine.getMIMEType(format);
	}

	/**
	 * Get the engine configuration
	 *
	 * @return the engine config
	 */
	public EngineConfig getEngineConfig() {
		return config;
	}

	/**
	 * Shutdown ReportEngineService, set instance as null
	 */
	public static void shutdown() {
		instance = null;
	}
}
