/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.utilities;

import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.browsers.BrowserAccessor;
import org.eclipse.birt.report.viewer.browsers.BrowserManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;

/**
 * Static accessor to display an arbitary url. It serves as an entry point to
 * integrate viewer.
 * <p>
 */
public class WebViewer {

	private static final String UTF_8 = "utf-8"; //$NON-NLS-1$
	/**
	 * HTML format name
	 */
	public final static String HTML = "html"; //$NON-NLS-1$
	public final static String HTM = "htm"; //$NON-NLS-1$

	/**
	 * PDF format name
	 */
	public final static String PDF = "pdf"; //$NON-NLS-1$

	/**
	 * DOC/DOCX format names
	 */
	public final static String DOC = "doc"; //$NON-NLS-1$
	public final static String DOCX = "docx"; //$NON-NLS-1$

	/**
	 * PPT/PPTX format names
	 */
	public final static String PPT = "ppt"; //$NON-NLS-1$
	public final static String PPTX = "pptx"; //$NON-NLS-1$

	/**
	 * Open document format names
	 */
	public final static String ODP = "odp"; //$NON-NLS-1$
	public final static String ODS = "ods"; //$NON-NLS-1$
	public final static String ODT = "odt"; //$NON-NLS-1$

	/**
	 * POSTSCRIPT format name
	 */
	public final static String POSTSCRIPT = "postscript"; //$NON-NLS-1$

	/**
	 * Report extension
	 */
	public static final String REPORT_DOCUMENT_FILE = ".rptdocument"; //$NON-NLS-1$

	/**
	 * Birt web viewer plugin id
	 */
	public final static String WebAppPlugin = ViewerPlugin.PLUGIN_ID;

	/**
	 * Locale preference name
	 */
	public final static String USER_LOCALE = "user_locale"; //$NON-NLS-1$

	/**
	 * Time zone preference name
	 */
	public final static String USER_TIME_ZONE = "user_time_zone"; //$NON-NLS-1$

	/**
	 * Preference key for SVG chart flag.
	 */
	public final static String SVG_FLAG = "svg_flag"; //$NON-NLS-1$

	public final static String BIDI_ORIENTATION = "bidi_orientation"; //$NON-NLS-1$
	public final static String BIDI_ORIENTATION_AUTO = "auto"; //$NON-NLS-1$
	public final static String BIDI_ORIENTATION_LTR = "ltr"; //$NON-NLS-1$
	public final static String BIDI_ORIENTATION_RTL = "rtl"; //$NON-NLS-1$

	/**
	 * Preference key for master page content flag.
	 */
	public final static String MASTER_PAGE_CONTENT = "master_page_content"; //$NON-NLS-1$

	/** Preference key for max rows. */
	public final static String PREVIEW_MAXROW = "preview_maxrow"; //$NON-NLS-1$

	/** Preference key for max cube fetch levels. */
//	public final static String PREVIEW_MAXCUBEROWLEVEL = "preview_maxrowlevelmember"; //$NON-NLS-1$

//	public final static String PREVIEW_MAXCUBECOLUMNLEVEL = "preview_maxcolumnlevelmember"; //$NON-NLS-1$

	/** Preference key for max in-memory cube size. */
	public final static String PREVIEW_MAXINMEMORYCUBESIZE = "preview_maxinmemorycubesize"; //$NON-NLS-1$

	// preview model.
	public static final String VIEWER_PREVIEW = "preview"; //$NON-NLS-1$

	// frameset model.
	public static final String VIEWER_FRAMESET = "frameset"; //$NON-NLS-1$

	// running model.
	public static final String VIEWER_RUN = "run"; //$NON-NLS-1$

	// document model
	public static final String VIEWER_DOCUMENT = "document"; //$NON-NLS-1$

	// output model
	public static final String VIEWER_OUTPUT = "output"; //$NON-NLS-1$

	// parameter name constants for the URL

	/**
	 * Key to indicate the format of the preview.
	 */
	public final static String FORMAT_KEY = "FORMAT_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the emitter used for preview.
	 */
	public final static String EMITTER_ID_KEY = "EMITTER_ID_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the emitter specific options used for preview. The value must
	 * be an instance of Map<String, String>.
	 */
	public final static String EMITTER_OPTIONS_KEY = "EMITTER_OPTIONS_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'allowPage' control of the preview.
	 */
	public final static String ALLOW_PAGE_KEY = "ALLOW_PAGE_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the output document file path.
	 */
	public final static String OUTPUT_DOCUMENT_KEY = "OUTPUT_DOCUMENT_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'servletName' of the preview.
	 */
	public final static String SERVLET_NAME_KEY = "SERVLET_NAME_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'documentName' of the preview.
	 */
	public final static String DOCUMENT_NAME_KEY = "DOCUMENT_NAME_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'resourceFolder'.
	 */
	public final static String RESOURCE_FOLDER_KEY = "RESOURCE_FOLDER_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate whether close window after complete
	 */
	public final static String CLOSE_WINDOW_KEY = "CLOSE_WINDOW_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate whether show the parameter page
	 */
	public final static String SHOW_PARAMETER_PAGE_KEY = "SHOW_PARAMETER_PAGE"; //$NON-NLS-1$

	/**
	 * Key to indicate which appcontext extension is loaded
	 */
	public final static String APPCONTEXT_EXTENSION_KEY = "APPCONTEXT_EXTENSION_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'maxRows'
	 */
	public final static String MAX_ROWS_KEY = "MAX_ROWS_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'maxLevelMember'
	 */
//	public final static String MAX_CUBE_ROW_LEVELS_KEY = "MAX_CUBE_ROW_LEVELS_KEY"; //$NON-NLS-1$

//	public final static String MAX_CUBE_COLUMN_LEVELS_KEY = "MAX_CUBE_COLUMN_LEVELS_KEY"; //$NON-NLS-1$

	/**
	 * Property to indicate whether it is a report debug mode
	 */
	public final static String REPORT_DEBUT_MODE = "report_debug_mode"; //$NON-NLS-1$

	/**
	 * ClassLoader to reload workspace class
	 */
	private static ReloadableClassLoader reloadableClassLoader = null;

	/**
	 * Locale mapping. Key as Locale display name, value is in format like "en_US"
	 */
	public static TreeMap<String, String> LOCALE_TABLE = null;

	private static Random random = new Random();

	static {

		// Initialize the locale mapping table
		LOCALE_TABLE = new TreeMap<String, String>(Collator.getInstance());
		Locale[] locales = Locale.getAvailableLocales();
		if (locales != null) {
			for (int i = 0; i < locales.length; i++) {
				Locale locale = locales[i];
				if (locale != null && locale.getCountry().length() != 0) {
					if (LOCALE_TABLE.containsValue(locale.getLanguage() + "_" + locale.getCountry())) {
						/**
						 * Some locale has same country & language with others ,only different in
						 * Variant. BIRT do not support different behavior for different variant. So
						 * filter out duplicate locale from LOCALE_TABLE.
						 */
						String existKey = getKeyByValue(locale.getLanguage() + "_" + locale.getCountry());
						if (locale.getDisplayName().length() < existKey.length()) {
							LOCALE_TABLE.remove(existKey);
							LOCALE_TABLE.put(locale.getDisplayName(), locale.getLanguage() + "_" + locale.getCountry());
						}
					} else {
						LOCALE_TABLE.put(locale.getDisplayName(), locale.getLanguage() + "_" + locale.getCountry()); //$NON-NLS-1$
					}

				}
			}
		}
	}

	private static Map<String, IWebAppInfo> apps = new LinkedHashMap<String, IWebAppInfo>();

	static {
		apps.put(ViewerPlugin.WEBAPP_CONTEXT, new IWebAppInfo() {

			public String getID() {
				return ViewerPlugin.PLUGIN_ID;
			}

			public String getName() {
				return ViewerPlugin.WEBAPP_CONTEXT;
			}

			public String getWebAppContextPath() {
				return ViewerPlugin.WEBAPP_CONTEXT_PATH;
			}

			public String getWebAppPath() {
				return ViewerPlugin.WEBAPP_PATH;
			}

			public boolean useCustomParamHandling() {
				return false;
			}

			public String getURIEncoding() {
				/* default to utf-8 */
				return null;
			}

		});
	}

	private static IWebAppInfo DEFAULT_WEBAPP = apps.get(ViewerPlugin.WEBAPP_CONTEXT);

	private static boolean adapterChecked = false;

	private static String getKeyByValue(String value) {
		Set<Entry<String, String>> entrySet = LOCALE_TABLE.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Map.Entry mapentry = (Map.Entry) iterator.next();
			if (mapentry.getValue().toString().equals(value)) {
				return mapentry.getKey().toString();
			}
		}
		return "";
	}

	private static void checkAdapter() {
		if (adapterChecked) {
			return;
		}

		Object webInfo = Platform.getAdapterManager().loadAdapter(ViewerPlugin.getDefault(),
				IWebAppInfo.class.getName());

		if (webInfo instanceof IWebAppInfo) {
			apps.put(((IWebAppInfo) webInfo).getName(), (IWebAppInfo) webInfo);

			DEFAULT_WEBAPP = (IWebAppInfo) webInfo;
		}

		adapterChecked = true;
	}

	public static IWebAppInfo getCurrentWebApp() {
		checkAdapter();

		return DEFAULT_WEBAPP;
	}

	/**
	 * Get web viewer base url.
	 * 
	 * @return base web viewer application url
	 */
	private static String getBaseURL(String webappName) {
		checkAdapter();

		IWebAppInfo app = apps.get(webappName);

		if (app == null) {
			app = DEFAULT_WEBAPP;
		}

		return "http://" + WebappAccessor.getHost() + ":" //$NON-NLS-1$ //$NON-NLS-2$
				+ WebappAccessor.getPort(app.getName()) + "/" + app.getName() + "/"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Create web viewer url to run the report.
	 * 
	 * @param report report file name
	 * @param params report parameter map
	 * @return valid web viewer url
	 */

	private static String createURL(String webappName, String report, Map params) {
		if (params == null || params.isEmpty())
			return createURL(webappName, null, report, null, null, null, null, null);

		String servletName = (String) params.get(SERVLET_NAME_KEY);
		String format = (String) params.get(FORMAT_KEY);
		String emitterid = (String) params.get(EMITTER_ID_KEY);
		String resourceFolder = (String) params.get(RESOURCE_FOLDER_KEY);
		Boolean allowPage = (Boolean) params.get(ALLOW_PAGE_KEY);
		Map<String, String> emitterOptions = (Map<String, String>) params.get(EMITTER_OPTIONS_KEY);
		String outputDocName = (String) params.get(OUTPUT_DOCUMENT_KEY);
		String showParameter = (String) params.get(SHOW_PARAMETER_PAGE_KEY);

		// maintain legacy support
		if (HTM.equalsIgnoreCase(format)) {
			format = HTML;
		}

		if (StringUtil.isBlank(format)) {
			format = StringUtil.isBlank(emitterid) ? HTML : null;
		}

		if (StringUtil.isBlank(servletName)) {
			if (!HTML.equalsIgnoreCase(format)) {
				servletName = VIEWER_PREVIEW;
			} else {
				if (allowPage == null) {
					servletName = VIEWER_FRAMESET;
				} else {
					servletName = allowPage.booleanValue() ? VIEWER_FRAMESET : VIEWER_PREVIEW;
				}
			}
		}

		// max rows setting
		String maxrows = (String) params.get(MAX_ROWS_KEY);

		// max level member setting
//		String maxrowlevels = (String) params.get( MAX_CUBE_ROW_LEVELS_KEY );
//		String maxcolumnlevels = (String) params.get( MAX_CUBE_COLUMN_LEVELS_KEY );

		// process common parameters
		Map<String, String> urlParams = prepareCommonURLParams(format, resourceFolder, maxrows, null, null);

		// if document mode, append document parameter in URL
		String documentName = (String) params.get(DOCUMENT_NAME_KEY);
		if (documentName != null && VIEWER_DOCUMENT.equals(servletName)) {
			// current opened report isn't document
			if (!isReportDocument(report)) {
				try {
					String encodedDocumentName = URLEncoder.encode(documentName, UTF_8);
					urlParams.put(ParameterAccessor.PARAM_REPORT_DOCUMENT, encodedDocumentName);

					String isCloseWin = (String) params.get(CLOSE_WINDOW_KEY);
					if (isCloseWin != null) {
						urlParams.put(ParameterAccessor.PARAM_CLOSEWIN, isCloseWin);
					}
				} catch (UnsupportedEncodingException e) {
					LogUtil.logWarning(e.getLocalizedMessage(), e);
				}
			}
		}

		// append appcontext extension name
		String appContextName = ViewerPlugin.getDefault().getPluginPreferences().getString(APPCONTEXT_EXTENSION_KEY);

		if (!StringUtil.isBlank(appContextName)) {
			try {
				String encodedAppContextName = URLEncoder.encode(appContextName.trim(), UTF_8);
				urlParams.put(ParameterAccessor.PARAM_APPCONTEXTNAME, encodedAppContextName);
			} catch (UnsupportedEncodingException e) {
				LogUtil.logWarning(e.getLocalizedMessage(), e);
			}
		}

		if (!StringUtil.isBlank(emitterid)) {
			urlParams.put(ParameterAccessor.PARAM_EMITTER_ID, emitterid.trim());
		}

		if (!StringUtil.isBlank(outputDocName)) {
			try {
				String encodedOutputDocumentName = URLEncoder.encode(outputDocName, UTF_8);
				urlParams.put(ParameterAccessor.PARAM_OUTPUT_DOCUMENT_NAME, encodedOutputDocumentName);
			} catch (UnsupportedEncodingException e) {
				LogUtil.logWarning(e.getLocalizedMessage(), e);
			}
		}

		if (showParameter != null) {
			urlParams.put(ParameterAccessor.PARAM_PARAMETER_PAGE, showParameter);
		}

		if (emitterOptions != null) {
			urlParams.putAll(emitterOptions);
		}

		return createURL(webappName, servletName, report, urlParams);
	}

	/**
	 * Create web viewer url to run the report.
	 * 
	 * @param servletName    servlet name to viewer report
	 * @param report         report file name
	 * @param format         report format
	 * @param resourceFolder the resource folder
	 * @param maxrows        max rows limited
	 * @param maxlevels      max level member limited
	 * @return valid web viewer url
	 */
	private static String createURL(String webappName, String servletName, String report, String format,
			String resourceFolder, String maxrows, String maxrowlevels, String maxcolumnlevels) {
		return createURL(webappName, servletName, report,
				prepareCommonURLParams(format, resourceFolder, maxrows, maxrowlevels, maxcolumnlevels));
	}

	private static String createURL(String webappName, String servletName, String report,
			Map<String, String> urlParams) {
		String encodedReportName = null;

		try {
			encodedReportName = URLEncoder.encode(report, UTF_8);
		} catch (UnsupportedEncodingException e) {
			LogUtil.logWarning(e.getLocalizedMessage(), e);
		}

		String reportParam = ParameterAccessor.PARAM_REPORT;
		if (isReportDocument(encodedReportName)) {
			reportParam = ParameterAccessor.PARAM_REPORT_DOCUMENT;
		}
		reportParam += "=" + encodedReportName; //$NON-NLS-1$

		// So far, only report name is encoded as utf-8 format
		return getBaseURL(webappName) + servletName + "?" //$NON-NLS-1$
				+ reportParam + convertParams(urlParams);
	}

	private static String convertParams(Map<String, String> params) {
		if (params != null && !params.isEmpty()) {
			StringBuffer sb = new StringBuffer();

			for (Entry<String, String> entry : params.entrySet()) {
				sb.append("&").append(entry.getKey()); //$NON-NLS-1$

				if (entry.getValue() != null) {
					sb.append("=").append(entry.getValue()); //$NON-NLS-1$
				}
			}

			return sb.toString();
		}

		return ""; //$NON-NLS-1$
	}

	private static Map<String, String> prepareCommonURLParams(String format, String resourceFolder, String maxrows,
			String maxrowlevels, String maxcolumnlevels) {
		String timeZone = ViewerPlugin.getDefault().getPluginPreferences().getString(USER_TIME_ZONE);
		if ("".equals(timeZone)) //$NON-NLS-1$
		{
			timeZone = null;
		}

		String locale = ViewerPlugin.getDefault().getPluginPreferences().getString(USER_LOCALE);

		if (LOCALE_TABLE.containsKey(locale)) {
			locale = LOCALE_TABLE.get(locale);
		} else {
			if ("".equals(locale)) //$NON-NLS-1$
			{
				locale = null;
			} else {
				try {
					locale = URLEncoder.encode(locale, UTF_8);
				} catch (UnsupportedEncodingException e) {
					locale = null;
					LogUtil.logWarning(e.getLocalizedMessage(), e);
				}
			}
		}

		boolean bSVGFlag = Platform.getPreferencesService().getBoolean(ViewerPlugin.PLUGIN_ID, SVG_FLAG, true, null);

		// cube memory size
		String cubeMemorySize = ViewerPlugin.getDefault().getPluginPreferences().getString(PREVIEW_MAXINMEMORYCUBESIZE);

		// read rtl value from the preferences
		boolean rtl = false;
		String bidiOrientation = ViewerPlugin.getDefault().getPluginPreferences().getString(BIDI_ORIENTATION);
		if (bidiOrientation == null) {
			bidiOrientation = BIDI_ORIENTATION_AUTO;
		}
		if (BIDI_ORIENTATION_LTR.equals(bidiOrientation)) {
			rtl = false;
		} else if (BIDI_ORIENTATION_RTL.equals(bidiOrientation)) {
			rtl = true;
		} else {
			// detect rtl from eclipse
			rtl = (Window.getDefaultOrientation() == SWT.RIGHT_TO_LEFT);
		}

		String masterPageContent = ViewerPlugin.getDefault().getPluginPreferences().getString(MASTER_PAGE_CONTENT);
		boolean bMasterPageContent = true;
		if ("false".equalsIgnoreCase(masterPageContent)) //$NON-NLS-1$
		{
			bMasterPageContent = false;
		}

		// handle resource folder encoding
		String encodedResourceFolder = null;

		if (resourceFolder != null) {
			try {
				encodedResourceFolder = URLEncoder.encode(resourceFolder, UTF_8);
			} catch (UnsupportedEncodingException e) {
				LogUtil.logWarning(e.getLocalizedMessage(), e);
			}
		}

		if (encodedResourceFolder == null) {
			encodedResourceFolder = ""; //$NON-NLS-1$
		}

		// workaround for postscript/doc/docx/ppt/pptx/odp/ods/odt formats, force
		// "Content-Disposition" as
		// "attachment"
		String asattachment = null;
		if (POSTSCRIPT.equalsIgnoreCase(format) || DOC.equalsIgnoreCase(format) || DOCX.equalsIgnoreCase(format)
				|| PPT.equalsIgnoreCase(format) || PPTX.equalsIgnoreCase(format) || ODP.equalsIgnoreCase(format)
				|| ODS.equalsIgnoreCase(format) || ODT.equalsIgnoreCase(format)) {
			asattachment = "&__asattachment=true"; //$NON-NLS-1$
		}

		// get the local DPI setting
		int dpi = Toolkit.getDefaultToolkit().getScreenResolution();

		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		if (format != null) {
			params.put(ParameterAccessor.PARAM_FORMAT, format);
		}
		params.put(ParameterAccessor.PARAM_SVG, String.valueOf(bSVGFlag));
		if (locale != null) {
			params.put(ParameterAccessor.PARAM_LOCALE, locale);
		}
		if (timeZone != null) {
			try {
				params.put(ParameterAccessor.PARAM_TIMEZONE, URLEncoder.encode(timeZone, UTF_8));
			} catch (UnsupportedEncodingException e) {
				LogUtil.logWarning(e.getLocalizedMessage(), e);
			}
		}
		params.put(ParameterAccessor.PARAM_MASTERPAGE, String.valueOf(bMasterPageContent));
		params.put(ParameterAccessor.PARAM_RTL, String.valueOf(rtl));
		if (!StringUtil.isBlank(maxrows)) {
			params.put(ParameterAccessor.PARAM_MAXROWS, maxrows.trim());
		}
//		if ( !StringUtil.isBlank( maxrowlevels ) )
//		{
//			params.put( ParameterAccessor.PARAM_MAXCUBE_ROWLEVELS,
//					maxrowlevels.trim( ) );
//		}
//		if ( !StringUtil.isBlank( maxcolumnlevels ) )
//		{
//			params.put( ParameterAccessor.PARAM_MAXCUBE_COLUMNLEVELS,
//					maxcolumnlevels.trim( ) );
//		}
		if (!StringUtil.isBlank(cubeMemorySize)) {
			params.put(ParameterAccessor.PARAM_CUBEMEMSIZE, cubeMemorySize.trim());
		}
		params.put(ParameterAccessor.PARAM_RESOURCE_FOLDER, encodedResourceFolder);
		if (asattachment != null) {
			params.put(ParameterAccessor.PARAM_AS_ATTACHMENT, "true"); //$NON-NLS-1$
		}

		// Do not append dpi for HTML viewer since actual dpi will be set in
		// viewer side
		if (!(HTML.equalsIgnoreCase(format))) {
			params.put(ParameterAccessor.PARAM_DPI, String.valueOf(dpi));
		}

		return params;
	}

	/**
	 * Start web application.
	 */
	private synchronized static void startWebApp(String webappName, String reportFileName) {
		checkAdapter();

		try {
			// if don't load debug ui, viewer will handle to set workspace
			// classpath
			String debugMode = System.getProperty(REPORT_DEBUT_MODE);
			if (debugMode == null) {
				// get workspace classpath
				URL[] urls = ViewerClassPathHelper.getWorkspaceClassPath(reportFileName);

				if (reloadableClassLoader == null) {
					// create ReloadableClassLoader
					reloadableClassLoader = new ReloadableClassLoader(urls, WebViewer.class.getClassLoader());
				} else {
					// reload class
					reloadableClassLoader.setUrls(urls);
					reloadableClassLoader.reload();
				}
			}

			IWebAppInfo app = apps.get(webappName);

			if (app != null) {
				WebappAccessor.start(app.getName(), app.getID());
			}
		} catch (CoreException e) {
			LogUtil.logError(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Stop web application
	 */
	private static void stopWebApp(String webappName) {
		try {
			WebappAccessor.stop(webappName);
		} catch (CoreException e) {
			LogUtil.logError(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Initiate the tomcat.
	 * 
	 */
	public static void startup() {
		checkAdapter();

		startup(DEFAULT_WEBAPP.getName());
	}

	public static void startup(String webappName) {
		startWebApp(webappName, null);
	}

	/**
	 * Initiate the tomcat.
	 * 
	 * @param browser SWT browser
	 * 
	 * @deprecated use {@link #startup(String)}
	 */
	public static void startup(Browser browser) {
		checkAdapter();

		startup(DEFAULT_WEBAPP.getName());
	}

	/**
	 * Stop the web server
	 */
	public static void stop() {
		stop(DEFAULT_WEBAPP.getName());
	}

	public static void stopAll() {
		try {
			WebappAccessor.stopAll();
		} catch (CoreException e) {
			LogUtil.logError(e.getLocalizedMessage(), e);
		}
	}

	public static void stop(String webappName) {
		stopWebApp(webappName);
	}

	/**
	 * Displays the specified url.
	 * 
	 * @param report report report
	 * @param format report format
	 */
	public static void display(String report, String format) {
		checkAdapter();

		display(DEFAULT_WEBAPP.getName(), report, format);
	}

	public static void display(String webappName, String report, String format) {
		display(webappName, report, format, true);
	}

	/**
	 * Displays the specified url.
	 * 
	 * @param report
	 * @param format
	 * @param allowPage
	 */
	public static void display(String report, String format, boolean allowPage) {
		checkAdapter();

		display(DEFAULT_WEBAPP.getName(), report, format, allowPage);
	}

	public static void display(String webappName, String report, String format, boolean allowPage) {
		if (format == null || format.trim().length() <= 0 || HTM.equalsIgnoreCase(format))
			format = HTML;

		String root = null;
		if (!HTML.equalsIgnoreCase(format)) {
			root = createURL(webappName, VIEWER_PREVIEW, report, format, null, null, null, null);
		} else {
			root = createURL(webappName, allowPage ? VIEWER_FRAMESET : VIEWER_PREVIEW, report, format, null, null, null,
					null) + "&" + random.nextInt(); //$NON-NLS-1$
		}

		startWebApp(webappName, report);

		try {
			boolean useExternal = ViewerPlugin.getDefault().getPluginPreferences()
					.getBoolean(BrowserManager.ALWAYS_EXTERNAL_BROWSER_KEY);

			BrowserAccessor.getPreviewBrowser(useExternal).displayURL(root);
		} catch (Exception e) {
			LogUtil.logError(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Displays the specified url useing eclipse SWT browser.
	 * 
	 * @param report  report report
	 * @param format  report format
	 * @param browser SWT browser instance
	 * @deprecated
	 */
	public static void display(String report, String format, Browser browser) {
		checkAdapter();

		startWebApp(DEFAULT_WEBAPP.getName(), report);
		browser.setUrl(createURL(DEFAULT_WEBAPP.getName(), "run", report, format, null, null, null, null) + "&" //$NON-NLS-1$ //$NON-NLS-2$
				+ random.nextInt());

	}

	/**
	 * Displays the specified url using eclipse SWT browser.
	 * 
	 * @param report      report report
	 * @param format      report format
	 * @param browser     SWT browser instance
	 * @param servletName servlet name to viewer report
	 * @deprecated
	 */
	public static void display(String report, String format, Browser browser, String servletName) {
		checkAdapter();

		startWebApp(DEFAULT_WEBAPP.getName(), report);
		browser.setUrl(createURL(DEFAULT_WEBAPP.getName(), servletName, report, format, null, null, null, null) + "&" //$NON-NLS-1$
				+ random.nextInt());
	}

	/**
	 * Displays the specified url using eclipse SWT browser.
	 * 
	 * @param report  report report
	 * @param browser SWT browser instance
	 * @param params  the parameter map to set
	 */

	public static void display(String report, Browser browser, Map params) {
		checkAdapter();

		display(DEFAULT_WEBAPP.getName(), report, browser, params);
	}

	public static void display(String webappName, String report, Browser browser, Map params) {
		startWebApp(webappName, report);
		browser.setUrl(createURL(webappName, report, params) + "&" + random.nextInt()); //$NON-NLS-1$
	}

	/**
	 * Displays the specified url using eclipse SWT browser.
	 * 
	 * @param report report report
	 * @param params the parameter map to set
	 */

	public static void display(String report, Map params) {
		checkAdapter();

		display(DEFAULT_WEBAPP.getName(), report, params);
	}

	public static void display(String webappName, String report, Map params) {
		startWebApp(webappName, report);

		try {

			boolean useExternal = ViewerPlugin.getDefault().getPluginPreferences()
					.getBoolean(BrowserManager.ALWAYS_EXTERNAL_BROWSER_KEY);

			BrowserAccessor.getPreviewBrowser(useExternal)
					.displayURL(createURL(webappName, report, params) + "&" + random.nextInt()); //$NON-NLS-1$
		} catch (Exception e) {
			LogUtil.logError(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Check whether the report is a document file.
	 * 
	 * @param reportName
	 * @return true or false
	 */
	private static boolean isReportDocument(String reportName) {
		if (reportName == null)
			return false;

		// only need handle ".rptdocument" case
		return reportName.toLowerCase().endsWith(".rptdocument"); //$NON-NLS-1$
	}

	/**
	 * Cancel the process
	 * 
	 * @param browser
	 */
	public static void cancel(Browser browser) {
		if (browser == null || browser.isDisposed()) {
			return;
		}

		try {
			browser.execute("try { if( birtProgressBar ){ birtProgressBar.cancel(); } } catch(e){}"); //$NON-NLS-1$
		} catch (Exception e) {
			LogUtil.logError(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @return Returns user specified app context name, if it's null, a default one
	 *         will be used.
	 */
	public static String getAppContextName() {
		String appContextName = ViewerPlugin.getDefault().getPluginPreferences().getString(APPCONTEXT_EXTENSION_KEY);

		if (appContextName != null && appContextName.trim().length() > 0) {
			return appContextName.trim();
		}

		return null;
	}

	/**
	 * Returns the application classloader
	 * 
	 * @return
	 */
	public static ClassLoader getAppClassLoader() {
		return reloadableClassLoader;
	}

	/**
	 * Returns the application classloader reloaded with given parent
	 */
	public static ClassLoader getAppClassLoader(ClassLoader parent) {
		if (reloadableClassLoader != null) {
			reloadableClassLoader.setParent(parent);
			reloadableClassLoader.reload();
		}
		return reloadableClassLoader;
	}
}
