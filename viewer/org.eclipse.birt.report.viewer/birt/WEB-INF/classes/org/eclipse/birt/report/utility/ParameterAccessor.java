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

package org.eclipse.birt.report.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.engine.api.DataExtractionFormatInfo;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.session.IViewingSession;
import org.eclipse.birt.report.session.ViewingCache;
import org.eclipse.birt.report.session.ViewingSessionConfig;
import org.eclipse.birt.report.session.ViewingSessionConfig.ViewingSessionPolicy;
import org.eclipse.birt.report.session.ViewingSessionUtil;
import org.eclipse.birt.report.utility.filename.DefaultFilenameGenerator;
import org.eclipse.birt.report.utility.filename.IFilenameGenerator;
import org.eclipse.birt.report.utility.filename.IFilenameGeneratorFactory;

import com.ibm.icu.util.ULocale;

/**
 * Utilities class for all types of URl related operations.
 * <p>
 */

public class ParameterAccessor {

	// URL parameter constants
	/**
	 * URL parameter name that specifies the viewer id.
	 */
	public static final String PARAM_ID = "__id"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies the report title.
	 */
	public static final String PARAM_TITLE = "__title"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies whether show the report title.
	 */
	public static final String PARAM_SHOW_TITLE = "__showtitle"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies whether show the toolbar.
	 */
	public static final String PARAM_TOOLBAR = "__toolbar"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies whether show the navigationbar.
	 */
	public static final String PARAM_NAVIGATIONBAR = "__navigationbar"; //$NON-NLS-1$

	/**
	 * URL parameter name that specifies whether force prompting the parameter
	 * dialog.
	 */
	public static final String PARAM_PARAMETER_PAGE = "__parameterpage"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the report design name.
	 */
	public static final String PARAM_REPORT = "__report"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the report document name.
	 */
	public static final String PARAM_REPORT_DOCUMENT = "__document"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the output report document file path.
	 */
	public static final String PARAM_OUTPUT_DOCUMENT_NAME = "__outputDocName"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the format to display the report, html or pdf.
	 */
	public static final String PARAM_FORMAT = "__format"; //$NON-NLS-1$
	public static final String PARAM_EMITTER_ID = "__emitterid"; //$NON-NLS-1$

	/**
	 * Format parameter constants to display the report in html.
	 */
	public static final String PARAM_FORMAT_HTM = "htm"; //$NON-NLS-1$
	public static final String PARAM_FORMAT_HTML = "html"; //$NON-NLS-1$

	/**
	 * Format parameter constants to display the report in pdf.
	 */
	public static final String PARAM_FORMAT_PDF = "pdf"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the locale.
	 */
	public static final String PARAM_LOCALE = "__locale"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the time zone.
	 */
	public static final String PARAM_TIMEZONE = "__timezone"; //$NON-NLS-1$

	/**
	 * URL parameter name that determins to support the SVG or not.
	 */
	public static final String PARAM_SVG = "__svg"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the page number to display the report.
	 */
	public static final String PARAM_PAGE = "__page"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the page range to display the report.
	 */
	public static final String PARAM_PAGE_RANGE = "__pagerange"; //$NON-NLS-1$

	/**
	 * URL parameter name that indicates the parameter value is null.
	 */
	public static final String PARAM_ISNULL = "__isnull"; //$NON-NLS-1$

	/**
	 * URL parameter name that indicates the parameter value list is null.
	 */
	public static final String PARAM_ISNULLLIST = "__isnulllist"; //$NON-NLS-1$

	/**
	 * URL parameter name that indicate the report parameter as a locale string.
	 */
	public static final String PARAM_ISLOCALE = "__islocale"; //$NON-NLS-1$

	/**
	 * URL parameter name that determines to support masterpage or not.
	 */
	public static final String PARAM_MASTERPAGE = "__masterpage"; //$NON-NLS-1$

	/**
	 * URL parameter name that determines whether the BIRT application is running in
	 * the designer or standalone.
	 */
	public static final String PARAM_DESIGNER = "__designer"; //$NON-NLS-1$

	/**
	 * URL parameter name that determines whether to overwrite the document or not.
	 */
	public static final String PARAM_OVERWRITE = "__overwrite"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the image ID to display.
	 */
	public static final String PARAM_IMAGEID = "__imageid"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the bookmark expression.
	 */
	public static final String PARAM_BOOKMARK = "__bookmark"; //$NON-NLS-1$

	/**
	 * URL parameter name that indicate the bookmark is TOC name.
	 */
	public static final String PARAM_ISTOC = "__istoc"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives that image rtl option.
	 */
	public static final String PARAM_RTL = "__rtl"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the preview max rows option.
	 */
	public static final String PARAM_MAXROWS = "__maxrows"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the preview max cube fetch levels option.
	 */
	// public static final String PARAM_MAXCUBE_ROWLEVELS = "__maxrowlevels";
	// //$NON-NLS-1$
	// public static final String PARAM_MAXCUBE_COLUMNLEVELS = "__maxcolumnlevels";
	// //$NON-NLS-1$

	/**
	 * URL parameter name that gives the cube memory size option.
	 */
	public static final String PARAM_CUBEMEMSIZE = "__cubememsize"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the reportlet id.
	 */
	public static final String PARAM_INSTANCEID = "__instanceid"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate whether need to run a reportlet.
	 */
	public static final String PARAM_ISREPORTLET = "__isreportlet";//$NON-NLS-1$

	/**
	 * URL parameter name to indicate the export encoding.
	 */
	public static final String PARAM_EXPORT_ENCODING = "__exportencoding";//$NON-NLS-1$

	/**
	 * URL parameter name to indicate the page overflow when render report as PDF.
	 */
	public static final String PARAM_PAGE_OVERFLOW = "__pageoverflow";//$NON-NLS-1$

	/**
	 * URL parameter name to indicate if pagebreak pagination only when render
	 * report as PDF.
	 */
	public static final String PARAM_PAGEBREAK_ONLY = "__pagebreakonly";//$NON-NLS-1$

	/**
	 * Indentify the display text of select parameter
	 */
	public static final String PREFIX_DISPLAY_TEXT = "__isdisplay__"; //$NON-NLS-1$

	/**
	 * Indentify the parameter value is a locale string
	 */
	public static final String PREFIX_ISLOCALE = "__islocale__"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate the resource folder of all the report
	 * resources.
	 */
	public static final String PARAM_RESOURCE_FOLDER = "__resourceFolder"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate the target servlet pattern.
	 */
	public static final String PARAM_SERVLET_PATTERN = "__pattern"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate the target window.
	 */
	public static final String PARAM_TARGET = "__target"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate whether cache the parameter.
	 */
	public static final String PARAM_NOCACHE_PARAMETER = "__nocache"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate if open document as attachment(default is
	 * inline ).
	 */
	public static final String PARAM_AS_ATTACHMENT = "__asattachment"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate the execution name.
	 */
	public static final String PARAM_ACTION = "__action"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate the client DPI setting.
	 */
	public static final String PARAM_DPI = "__dpi"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate if force optimized HTML output.
	 */
	public static final String PARAM_AGENTSTYLE_ENGINE = "__agentstyle"; //$NON-NLS-1$

	/**
	 * Custom request headers to identify the request is a normal HTTP request or a
	 * soap request by AJAX.
	 */
	public static final String HEADER_REQUEST_TYPE = "request-type"; //$NON-NLS-1$

	/**
	 * The request type of "soap".
	 */
	public static final String HEADER_REQUEST_TYPE_SOAP = "soap"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the IID of the export data form.
	 */
	public static final String PARAM_IID = "iid"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate if close window after complete
	 */
	public static final String PARAM_CLOSEWIN = "__closewin"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the result set names of the export data form.
	 */
	public static final String PARAM_RESULTSETNAME = "__resultsetname"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the selected column numbers of the export data
	 * form.
	 */
	public static final String PARAM_SELECTEDCOLUMNNUMBER = "__selectedcolumnnumber"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the selected column names of the export data form.
	 */
	public static final String PARAM_SELECTEDCOLUMN = "__selectedcolumn"; //$NON-NLS-1$

	/**
	 * Parameter that indicated which appcontext extension will be loaded
	 */
	public static final String PARAM_APPCONTEXTNAME = "__appcontextname"; //$NON-NLS-1$

	/**
	 * Parameter that indicated data extraction format
	 */
	public static final String PARAM_DATA_EXTRACT_FORMAT = "__extractformat"; //$NON-NLS-1$

	/**
	 * Parameter that indicated data extraction extension id
	 */
	public static final String PARAM_DATA_EXTRACT_EXTENSION = "__extractextension"; //$NON-NLS-1$

	/**
	 * Parameter that indicates that the paths used in common URL parameters are
	 * encoded.
	 */
	public static final String PARAM_ENCODED_PATHS = "__encodedPaths"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the default locale of the BIRT viewer.
	 */
	public static final String INIT_PARAM_LOCALE = "BIRT_VIEWER_LOCALE"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the default time zone of the BIRT viewer.
	 */
	public static final String INIT_PARAM_TIMEZONE = "BIRT_VIEWER_TIMEZONE"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the working folder of the local BIRT viewer
	 * user.
	 */
	public static final String INIT_PARAM_WORKING_DIR = "BIRT_VIEWER_WORKING_FOLDER"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the repository location of the image files.
	 */
	public static final String INIT_PARAM_IMAGE_DIR = "BIRT_VIEWER_IMAGE_DIR"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the repository location of the logging
	 * files.
	 */
	public static final String INIT_PARAM_LOG_DIR = "BIRT_VIEWER_LOG_DIR"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the logging level.
	 */
	public static final String INIT_PARAM_LOG_LEVEL = "BIRT_VIEWER_LOG_LEVEL"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the repository location of the script files
	 * to run reports.
	 */
	public static final String INIT_PARAM_SCRIPTLIB_DIR = "BIRT_VIEWER_SCRIPTLIB_DIR"; //$NON-NLS-1$

	/**
	 * Context parameter name that determines the search strategy of searching
	 * report resources. True if only search the working folder, otherwise false.
	 */
	public static final String INIT_PARAM_WORKING_FOLDER_ACCESS_ONLY = "WORKING_FOLDER_ACCESS_ONLY"; //$NON-NLS-1$

	/**
	 * Context parameter name that specifies the policy to handle url report paths.
	 */
	public static final String INIT_PARAM_URL_REPORT_PATH_POLICY = "URL_REPORT_PATH_POLICY"; //$NON-NLS-1$

	/**
	 * The parameter name that gives the repository location to put the created
	 * documents and report design files.
	 */
	public static final String INIT_PARAM_DOCUMENT_FOLDER = "BIRT_VIEWER_DOCUMENT_FOLDER"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the absolute resource location directory.
	 */
	public static final String INIT_PARAM_BIRT_RESOURCE_PATH = "BIRT_RESOURCE_PATH"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives preview report max rows limited.
	 */
	public static final String INIT_PARAM_VIEWER_MAXROWS = "BIRT_VIEWER_MAX_ROWS"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives preview report cube fetch levels limited.
	 */
	public static final String INIT_PARAM_VIEWER_MAXCUBE_ROWLEVELS = "BIRT_VIEWER_MAX_CUBE_ROWLEVELS"; //$NON-NLS-1$
	public static final String INIT_PARAM_VIEWER_MAXCUBE_COLUMNLEVELS = "BIRT_VIEWER_MAX_CUBE_COLUMNLEVELS"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives cube memory size.
	 */
	public static final String INIT_PARAM_VIEWER_CUBEMEMSIZE = "BIRT_VIEWER_CUBE_MEMORY_SIZE"; //$NON-NLS-1$

	/**
	 * Context parameter name that if always overwrite generated document file.
	 */
	public static final String INIT_PARAM_OVERWRITE_DOCUMENT = "BIRT_OVERWRITE_DOCUMENT"; //$NON-NLS-1$

	/**
	 * Context parameter name that defines BIRT viewer configuration file.
	 */
	public static final String INIT_PARAM_CONFIG_FILE = "BIRT_VIEWER_CONFIG_FILE"; //$NON-NLS-1$

	/**
	 * Context parameter name that if support print on the server
	 */
	public static final String INIT_PARAM_PRINT_SERVERSIDE = "BIRT_VIEWER_PRINT_SERVERSIDE"; //$NON-NLS-1$

	/**
	 * Context parameter name that if force optimized HTML output.
	 */
	public static final String INIT_PARAM_AGENTSTYLE_ENGINE = "HTML_ENABLE_AGENTSTYLE_ENGINE"; //$NON-NLS-1$

	/**
	 * Class name to use for the export filename generator.
	 */
	public static final String INIT_PARAM_FILENAME_GENERATOR_CLASS = "BIRT_FILENAME_GENERATOR_CLASS"; //$NON-NLS-1$

	/**
	 * UTF-8 encode constants.
	 */
	public static final String UTF_8_ENCODE = "UTF-8"; //$NON-NLS-1$

	/**
	 * ISO-8859-1 encode constants.
	 */
	public static final String ISO_8859_1_ENCODE = "ISO-8859-1"; //$NON-NLS-1$

	/**
	 * Separator that connects the query parameter values.
	 */
	public static final String PARAMETER_SEPARATOR = "&"; //$NON-NLS-1$

	/**
	 * The character to start the query string in the url.
	 */
	public static final String QUERY_CHAR = "?"; //$NON-NLS-1$

	/**
	 * Equals operator.
	 */
	public static final String EQUALS_OPERATOR = "="; //$NON-NLS-1$

	/**
	 * Report working folder.
	 */
	public static String workingFolder = null;

	/**
	 * Log folder to put the generated log files
	 */
	public static String logFolder = null;

	/**
	 * Log level
	 */
	public static String logLevel = null;

	/**
	 * Script lib folder
	 */
	public static String scriptLibDir = null;

	/**
	 * Preview report max rows
	 */
	public static int maxRows;

	/**
	 * Preview report max cube fetch levels
	 */
	public static int maxCubeRowLevels;
	public static int maxCubeColumnLevels;

	/**
	 * Cube memory size
	 */
	public static int cubeMemorySize;

	/**
	 * Current web application locale.
	 */
	public static Locale webAppLocale = null;

	/**
	 * Current web application time zone.
	 */
	public static TimeZone webAppTimeZone = null;

	/**
	 * Indicating that if user can only access the files in working folder.
	 */
	public static boolean isWorkingFolderAccessOnly = false;

	/**
	 * Resource path set in the web application.
	 */
	public static String birtResourceFolder = null;

	/**
	 * Flag indicating that if initialize the context.
	 */
	protected static boolean isInitContext = false;

	/**
	 * Overwrite flag
	 */
	public static boolean isOverWrite;

	/**
	 * URL report paths policy constants
	 */
	public static final String POLICY_ALL = "all"; //$NON-NLS-1$
	public static final String POLICY_DOMAIN = "domain"; //$NON-NLS-1$
	public static final String POLICY_NONE = "none"; //$NON-NLS-1$

	/**
	 * Indicats the url report paths accessing policy
	 */
	public static String urlReportPathPolicy = POLICY_DOMAIN;

	/**
	 * Application Context Attribute Name
	 */
	public static final String ATTR_APPCONTEXT_KEY = "AppContextKey"; //$NON-NLS-1$

	/**
	 * Application Context Attribute value
	 */
	public static final String ATTR_APPCONTEXT_VALUE = "AppContextValue"; //$NON-NLS-1$

	/**
	 * Attribute for the BIRT viewing session.
	 */
	public static final String ATTR_VIEWING_SESSION = "ViewingSession"; //$NON-NLS-1$

	/**
	 * The initialized properties map
	 */
	public static Map initProps;

	/**
	 * The logger names to register. The key part contains the name of the logger
	 * and the value contains the name of the level.
	 */
	public static Map loggers;

	/**
	 * viewer properties
	 */
	public static final String PROP_BASE_URL = "base_url"; //$NON-NLS-1$

	/**
	 * Session id, defines a session id based on the HTTP session id. It is used to
	 * split sub-sessions inside an HTTP session.
	 */
	public static final String PARAM_VIEWING_SESSION_ID = "__sessionId"; //$NON-NLS-1$

	/**
	 * Engine supported output formats
	 */
	public static String[] supportedFormats = { PARAM_FORMAT_HTML, PARAM_FORMAT_PDF };

	/**
	 * Engine supported data extraction formats
	 */
	public static DataExtractionFormatInfo[] supportedDataExtractions = {};

	/**
	 * Supported emitters.
	 */
	public static Map supportedEmitters = null;

	/**
	 * Flag that indicated if support print on the server side.
	 */
	public static boolean isSupportedPrintOnServer = true;

	/**
	 * Optimized HTML output flag
	 */
	public static boolean isAgentStyle = true;

	/**
	 * Run in designer or not
	 */
	public static boolean isDesigner = false;

	/**
	 * Export filename generator instance.
	 */
	public static IFilenameGenerator exportFilenameGenerator = null;

	/**
	 * Get bookmark. If page exists, ignore bookmark.
	 *
	 * @param request
	 * @return the bookemark
	 */

	public static String getBookmark(HttpServletRequest request) {
		int page = getParameterAsInt(request, PARAM_PAGE);
		return page < 1 ? getReportParameter(request, PARAM_BOOKMARK, null) : null;
	}

	/**
	 * Returns whether the bookmark is TOC
	 *
	 * @param request
	 * @return boolean
	 */

	public static boolean isToc(HttpServletRequest request) {
		boolean flag = false;

		String isToc = getParameter(request, PARAM_ISTOC);
		if ("true".equalsIgnoreCase(isToc)) //$NON-NLS-1$
		{
			flag = true;
		}

		return flag;
	}

	/**
	 * Gets the query parameter string with the give name and value.
	 *
	 * @param paramName
	 * @param value
	 * @return
	 */

	public static String getQueryParameterString(String paramName, String value) {
		StringBuilder b = new StringBuilder();
		b.append(PARAMETER_SEPARATOR);
		b.append(paramName);
		b.append(EQUALS_OPERATOR);
		b.append(value);
		return b.toString();
	}

	/**
	 * Get report title.
	 *
	 * @param request http request
	 * @return report title
	 */

	public static String getTitle(HttpServletRequest request) {
		String title = getParameter(request, PARAM_TITLE);
		if (title == null) {
			title = BirtResources.getMessage(ResourceConstants.BIRT_VIEWER_TITLE);
		}

		return title;
	}

	/**
	 * Get report format from the emitter defined by the emitter id attribute. If no
	 * emitter id has been specified in the request, then use the format attribute.
	 *
	 * @param request http request
	 * @return report format
	 */

	public static String getFormat(HttpServletRequest request) {
		// get format from the URL
		boolean formatSpecified = false;
		String format = getParameter(request, PARAM_FORMAT);
		if (format != null && format.length() > 0) {
			formatSpecified = true;
			if (PARAM_FORMAT_HTM.equalsIgnoreCase(format)) {
				format = PARAM_FORMAT_HTML;
			}
		} else {
			format = PARAM_FORMAT_HTML;
		}

		// if emitter id is specified
		String emitterId = getEmitterId(request);
		if (emitterId != null && emitterId.length() > 0) {
			// get the format from the emitter
			String emitterFormat = getEmitterFormat(emitterId);
			if (emitterFormat != null) {
				format = emitterFormat;
			} else if (!formatSpecified) {
				format = null;
			}
		}

		return format; // The default format is html.
	}

	/**
	 * Get emitter id.
	 *
	 * @param request http request
	 * @return emitter id
	 */

	public static String getEmitterId(HttpServletRequest request) {
		String emitterId = getParameter(request, PARAM_EMITTER_ID);
		if (emitterId != null && emitterId.length() > 0) {
			return emitterId;
		}

		return null;
	}

	/**
	 * Get preview max rows.
	 *
	 * @param request http request
	 * @return max rows
	 */

	public static int getMaxRows(HttpServletRequest request) {
		int maxRows = ParameterAccessor.getParameterAsInt(request, PARAM_MAXROWS);
		return maxRows == -1 ? ParameterAccessor.maxRows : maxRows;
	}

	/**
	 * Get preview max cube fetch row levels.
	 *
	 * @param request http request
	 * @return max levels
	 */

	// public static int getMaxCubeRowLevels( HttpServletRequest request )
	// {
	// int curMaxRowLevels = ParameterAccessor.getParameterAsInt( request,
	// PARAM_MAXCUBE_ROWLEVELS );
	// if ( curMaxRowLevels <= 0 )
	// curMaxRowLevels = maxCubeRowLevels;
	//
	// return curMaxRowLevels;
	// }

	/**
	 * Get preview max cube fetch column levels.
	 *
	 * @param request http request
	 * @return max levels
	 */

	// public static int getMaxCubeColumnLevels( HttpServletRequest request )
	// {
	// int curMaxColumnLevels = ParameterAccessor.getParameterAsInt( request,
	// PARAM_MAXCUBE_COLUMNLEVELS );
	// if ( curMaxColumnLevels <= 0 )
	// curMaxColumnLevels = maxCubeColumnLevels;
	//
	// return curMaxColumnLevels;
	// }

	/**
	 * Get cube memory size.
	 *
	 * @param request http request
	 * @return memory size
	 */

	public static int getCubeMemorySize(HttpServletRequest request) {
		int curMaxMemSize = ParameterAccessor.getParameterAsInt(request, PARAM_CUBEMEMSIZE);
		if (curMaxMemSize <= 0) {
			curMaxMemSize = cubeMemorySize;
		}

		return curMaxMemSize;
	}

	/**
	 * Get report element's iid.
	 *
	 * @param request
	 * @return report element's iid
	 */

	public static String getInstanceId(HttpServletRequest request) {
		return getParameter(request, PARAM_INSTANCEID);
	}

	/**
	 * Returns the timezone from the http request.
	 *
	 * @param request http request
	 * @return TimeZone instance. If the timezone ID from the request is unknown,
	 *         returns the GMT timezone by default. If no timezone ID was given in
	 *         the request, return null.
	 */
	public static TimeZone getTimeZone(HttpServletRequest request) {
		TimeZone timeZone = getTimeZoneFromString(getParameter(request, PARAM_TIMEZONE));

		// Get Locale from Web Context
		if (timeZone == null) {
			timeZone = webAppTimeZone;
		}
		return timeZone;
	}

	/**
	 * Returns a time zone from the given string.
	 *
	 * @param timeZoneString time zone string
	 * @return TimeZone instance. If the timezone ID from the string is unknown,
	 *         returns the GMT timezone by default. If the string is null, returns
	 *         null.
	 */
	public static TimeZone getTimeZoneFromString(String timeZoneString) {
		if (timeZoneString != null) {
			timeZoneString = timeZoneString.trim();
			if (!"".equals(timeZoneString)) { //$NON-NLS-1$
				return TimeZone.getTimeZone(timeZoneString);
			}
		}
		return null;
	}

	/**
	 * Get report locale from Http request.
	 *
	 * @param request http request
	 * @return report locale
	 */
	public static Locale getLocale(HttpServletRequest request) {
		Locale locale;

		// Get Locale from URL parameter
		locale = getLocaleFromString(getParameter(request, PARAM_LOCALE));

		// Get Locale from client browser
		if (locale == null) {
			locale = request.getLocale();
		}

		// Get Locale from Web Context
		if (locale == null) {
			locale = webAppLocale;
		}

		return locale;
	}

	/**
	 * Check whether the viewer is set rtl option.
	 *
	 * @param request
	 * @return
	 */

	public static boolean isRtl(HttpServletRequest request) {
		boolean isRtl = false;

		if ("true".equalsIgnoreCase(getParameter(request, PARAM_RTL))) //$NON-NLS-1$
		{
			isRtl = true;
		}

		return isRtl;
	}

	/**
	 * Get report locale from a given string.
	 *
	 * @param locale locale string
	 * @return report locale
	 */
	public static Locale getLocaleFromString(String locale) {
		if (locale == null || locale.length() <= 0) {
			return null;
		}
		// remove all '<' character to avoid xss attack
		locale = locale.replace('<', ' ');
		return new ULocale(locale).toLocale();
	}

	/**
	 * Get report locale in string.
	 *
	 * @param request http request
	 * @return report String
	 */

	public static String getLocaleString(HttpServletRequest request) {
		return getParameter(request, PARAM_LOCALE);
	}

	/**
	 * Get report page from Http request. If frameset pattern, default page is 1.
	 *
	 * @param request http request
	 * @return report page number
	 */

	public static int getPage(HttpServletRequest request) {
		int page = getParameterAsInt(request, PARAM_PAGE);
		if (page > 0) {
			return page;
		}

		String servletPath = request.getServletPath();
		if (IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase(servletPath)
				&& PARAM_FORMAT_HTML.equalsIgnoreCase(getFormat(request))) {
			page = 1;
		} else {
			page = 0;
		}

		return page;
	}

	/**
	 * Get report page range from Http request.
	 *
	 * @param request http request
	 * @return report page range
	 */

	public static String getPageRange(HttpServletRequest request) {
		return getParameter(request, PARAM_PAGE_RANGE);
	}

	/**
	 * Get reportlet id from Http request.
	 *
	 * @param request http request
	 * @return reportlet id
	 */

	public static String getReportletId(HttpServletRequest request) {

		if (isIidReportlet(request)) {
			return getParameter(request, PARAM_INSTANCEID);
		}

		if (isBookmarkReportlet(request)) {
			return getParameter(request, PARAM_BOOKMARK);
		}

		return null;

	}

	/**
	 * Get report file name. If passed file path is null, get report file from
	 * request.
	 *
	 * @param request
	 * @param filePath
	 * @return report file
	 */
	public static String getReport(HttpServletRequest request, String filePath) {
		if (filePath == null) {
			filePath = DataUtil.trimString(getParameter(request, PARAM_REPORT));
		}
		filePath = decodeFilePath(request, filePath);

		return getRealPathOnWorkingFolder(filePath, request);
	}

	/**
	 * Get report document name. If passed file path is null, get document file from
	 * request. If isCreated is true, try to create the document file when file path
	 * is null.
	 *
	 * @param request
	 * @param filePath
	 * @param isCreate
	 * @return
	 * @throws ViewerException
	 */
	public static String getReportDocument(HttpServletRequest request, String filePath, boolean isCreate)
			throws ViewerException {
		if (filePath == null) {
			filePath = DataUtil.trimString(getParameter(request, PARAM_REPORT_DOCUMENT));
		}
		filePath = decodeFilePath(request, filePath);

		// don't need create the document file from report
		if (filePath.length() <= 0 && !isCreate) {
			return null;
		}

		if (filePath.length() <= 0) {
			// use an existing BIRT viewing session, if available, else create
			// one
			IViewingSession session = ViewingSessionUtil.getSession(request);
			if (session == null) {
				throw new ViewerException(BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_NO_VIEWING_SESSION));
			}
			// return the cached document file path
			return session.getCachedReportDocument(getReport(request, null), null);
		} else {
			filePath = getRealPathOnWorkingFolder(filePath, request);
		}

		return filePath;

	}

	/**
	 * Returns the real path based on working folder. If file path is an absolute
	 * path, return it directly. Else, return the absolute path based on working
	 * folder.
	 *
	 * @param filePath
	 * @param request
	 * @return
	 */
	public static String getRealPathOnWorkingFolder(String filePath, HttpServletRequest request) {

		// if file path is a non-relative path, return it directly
		if (filePath == null || filePath.length() == 0 || isUniversalPath(filePath)) {
			return filePath;
		}

		// relative to working folder
		if (!isUniversalPath(workingFolder)) {
			filePath = getRealPath(workingFolder + "/" + filePath, request //$NON-NLS-1$
					.getSession().getServletContext());
		} else {
			filePath = workingFolder + "/" + filePath; //$NON-NLS-1$
		}

		return filePath;
	}

	/**
	 * Get report parameter by given name.
	 *
	 * @param request      http request
	 * @param name         parameter name
	 * @param defaultValue default parameter value
	 * @return parameter value
	 */

	public static String getReportParameter(HttpServletRequest request, String name, String defaultValue) {
		assert request != null && name != null;

		String value = getParameter(request, name);
		if (value == null || value.length() <= 0) // Treat
		// it as blank value.
		{
			value = ""; //$NON-NLS-1$
		}

		Map paramMap = request.getParameterMap();
		if (paramMap == null || !paramMap.containsKey(name)) {
			value = defaultValue;
		}

		Set nullParams = getParameterValues(request, PARAM_ISNULL);

		if (nullParams != null && nullParams.contains(name)) {
			value = null;
		}

		return value;
	}

	/**
	 * Get report parameters by given name, support multi-value parameter.
	 *
	 * @param request   http request
	 * @param paramName parameter name
	 * @return parameter value
	 */

	public static List getReportParameters(HttpServletRequest request, String paramName) {
		assert request != null && paramName != null;

		List<String> paramList = new ArrayList<>();

		Set params = getParameterValues(request, paramName);
		if (params != null) {
			Iterator it = params.iterator();
			while (it.hasNext()) {
				String value = (String) it.next();
				if (value != null) {
					paramList.add(value);
				}
			}
		}

		Set nullParams = getParameterValues(request, PARAM_ISNULL);
		if (nullParams != null && nullParams.contains(paramName)) {
			paramList.add(null);
		}

		return paramList;
	}

	/**
	 * Get result set name.
	 *
	 * @param request
	 * @return
	 */

	public static String getResultSetName(HttpServletRequest request) {
		return getReportParameter(request, PARAM_RESULTSETNAME, null);
	}

	/**
	 * Get selected column name list.
	 *
	 * @param request
	 * @return
	 */

	public static Collection getSelectedColumns(HttpServletRequest request) {
		ArrayList<String> columns = new ArrayList<>();

		int columnCount = getParameterAsInt(request, PARAM_SELECTEDCOLUMNNUMBER);
		for (int i = 0; i < columnCount; i++) {
			String paramName = PARAM_SELECTEDCOLUMN + String.valueOf(i);
			String columnName = getParameter(request, paramName);
			if (columnName != null && !"".equals(columnName)) {
				columns.add(columnName);
			}
		}

		return columns;
	}

	/**
	 * Check whether enable svg support or not.
	 *
	 * @param request http request
	 * @return whether or not render content toolbar
	 */

	public static boolean getSVGFlag(HttpServletRequest request) {
		boolean svg = false;

		if ("true".equalsIgnoreCase(getParameter(request, PARAM_SVG))) //$NON-NLS-1$
		{
			svg = true;
		}

		return svg;
	}

	/**
	 * Get web application locale.
	 *
	 * @return report locale
	 */

	public static Locale getWebAppLocale() {
		return webAppLocale;
	}

	/**
	 * Returns the time zone configured in the web context.
	 *
	 * @return time zone object
	 */
	public static TimeZone getWebAppTimeZone() {
		return webAppTimeZone;
	}

	public static final String htmlHeaderValueEncode(String s) {
		if (s == null) {
			return s;
		}

		s = s.replaceAll("\\s", " "); //$NON-NLS-1$ //$NON-NLS-2$

		return s;

		// TODO filter other unxpected characters.
		// // these are only allowed characters in html header value.
		// // ^[a-zA-Z0-9()\-=\*\.\?;,+\/:&_ ]*$
		// StringBuilder sb = new StringBuilder( );
		//
		// for ( int i = 0; i < s.length( ); i++ )
		// {
		// char c = s.charAt( i );
		//
		// if ( ( c >= 'a' && c <= 'z' )
		// || ( c >= 'A' && c <= 'Z' )
		// || ( c >= '0' && c <= '9' )
		// || c == '-'
		// || c == '='
		// || c == '('
		// || c == ')'
		// || c == '*'
		// || c == '.'
		// || c == '?'
		// || c == ';'
		// || c == ','
		// || c == '+'
		// || c == '/'
		// || c == ':'
		// || c == '&'
		// || c == '_'
		// || c == ' ' )
		// {
		// sb.append( c );
		// }
		// }
		//
		// return sb.toString( );
	}

	/**
	 * This function is used to encode an ordinary string that may contain
	 * characters or more than one consecutive spaces for appropriate HTML display.
	 *
	 * @param s
	 * @return String
	 */
	public static final String htmlEncode(String s) {
		String sHtmlEncoded; //$NON-NLS-1$

		if (s == null) {
			return null;
		}

		StringBuilder sbHtmlEncoded = new StringBuilder();
		final char chrarry[] = s.toCharArray();
		final int length = chrarry.length;

		for (int i = 0; i < length; i++) {
			char c = chrarry[i];

			switch (c) {
			case '\t':
				sbHtmlEncoded.append("&#09;"); //$NON-NLS-1$
				break;
			case '\n':
				sbHtmlEncoded.append("<br>"); //$NON-NLS-1$
				break;
			case '\r':
				sbHtmlEncoded.append("&#13;"); //$NON-NLS-1$
				break;
			case ' ':
				sbHtmlEncoded.append("&#32;"); //$NON-NLS-1$
				break;
			case '"':
				sbHtmlEncoded.append("&#34;"); //$NON-NLS-1$
				break;
			case '\'':
				sbHtmlEncoded.append("&#39;"); //$NON-NLS-1$
				break;
			case '<':
				sbHtmlEncoded.append("&#60;"); //$NON-NLS-1$
				break;
			case '>':
				sbHtmlEncoded.append("&#62;"); //$NON-NLS-1$
				break;
			case '`':
				sbHtmlEncoded.append("&#96;"); //$NON-NLS-1$
				break;
			case '&':
				sbHtmlEncoded.append("&#38;"); //$NON-NLS-1$
				break;
			case '\\':
				sbHtmlEncoded.append("&#92;"); //$NON-NLS-1$
				break;
			case '/':
				sbHtmlEncoded.append("&#47;"); //$NON-NLS-1$
				break;
			default:
				if ((c > 0xd7ff && c < 0xdc00) && (i + 1) < length) {
					i++;

					char nc = chrarry[i];

					if (nc > 0xdbff && nc < 0xe000) {
						// surrogates matched, must be character >= 0x10000

						int rc = ((c - 0xd7c0) << 10) | (nc & 0x3ff);

						sbHtmlEncoded.append("&#") //$NON-NLS-1$
								.append(rc).append(';');
					} else {
						sbHtmlEncoded.append(c);
						sbHtmlEncoded.append(nc);
					}
				} else {
					sbHtmlEncoded.append(c);
				}
			}
		}

		sHtmlEncoded = sbHtmlEncoded.toString();
		return sHtmlEncoded;
	}

	/**
	 * This function is used to decode a htmlEncoded string and convert to the
	 * orginial string
	 *
	 * @param s
	 * @return String
	 */
	public static final String htmlDecode(String s) {
		if (s == null) {
			return null;
		}

		String sHtmlDecoded = s.replace("&#09;", "\t"); //$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("<br>", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#13;", "\r"); //$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#32;", " ");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#34;", "\"");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#39;", "'");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#60;", "<");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#62;", ">");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#96;", "`");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#38;", "&");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#92;", "\\");//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replace("&#47;", "/");//$NON-NLS-1$ //$NON-NLS-2$

		return sHtmlDecoded;
	}

	/**
	 * Initial the parameters class. Web.xml is in UTF-8 format. No need to do
	 * encoding convertion.
	 *
	 * @param config Servlet configuration
	 */

	public synchronized static void initParameters(ServletConfig config) {
		if (!isInitContext) {
			ParameterAccessor.initParameters(config.getServletContext());
		}
	}

	/**
	 * Initial the parameters class. Web.xml is in UTF-8 format. No need to do
	 * encoding convertion.
	 *
	 * @param context Servlet Context
	 */

	public synchronized static void initParameters(ServletContext context) {
		if (isInitContext) {
			return;
		}

		if ("true".equalsIgnoreCase(System.getProperty(IBirtConstants.SYS_PROP_BIRT_ISDESIGNER))) { //$NON-NLS-1$
			isDesigner = true;
		}

		String workingPath = "${" + IBirtConstants.SYS_PROP_WORKING_PATH + "}/"; //$NON-NLS-1$//$NON-NLS-2$

		// Working folder setting
		workingFolder = processWorkingFolder(context, context.getInitParameter(INIT_PARAM_WORKING_DIR));

		// Document folder setting
		String initDocumentFolder = context.getInitParameter(INIT_PARAM_DOCUMENT_FOLDER);
		if (isDesigner && initDocumentFolder == null) {
			initDocumentFolder = workingPath + IBirtConstants.DEFAULT_DOCUMENT_FOLDER;
		}
		String documentFolder = processRealPath(context, initDocumentFolder, IBirtConstants.DEFAULT_DOCUMENT_FOLDER,
				true);

		// Image folder setting
		String initImageFolder = context.getInitParameter(ParameterAccessor.INIT_PARAM_IMAGE_DIR);
		if (isDesigner && initImageFolder == null) {
			initImageFolder = workingPath + IBirtConstants.DEFAULT_IMAGE_FOLDER;
		}
		String imageFolder = processRealPath(context, initImageFolder, IBirtConstants.DEFAULT_IMAGE_FOLDER, true);

		// Log folder setting
		String initLogFolder = context.getInitParameter(ParameterAccessor.INIT_PARAM_LOG_DIR);
		if (isDesigner && initLogFolder == null) {
			initLogFolder = workingPath + IBirtConstants.DEFAULT_LOGS_FOLDER;
		}
		logFolder = processRealPath(context, initLogFolder, IBirtConstants.DEFAULT_LOGS_FOLDER, true);

		// Log level setting
		logLevel = context.getInitParameter(ParameterAccessor.INIT_PARAM_LOG_LEVEL);
		if (logLevel == null) {
			logLevel = IBirtConstants.DEFAULT_LOGS_LEVEL;
		}

		String rootPath = "${" + IBirtConstants.SYS_PROP_ROOT_PATH + "}/"; //$NON-NLS-1$//$NON-NLS-2$
		// Script lib folder setting
		String initScriptlibFolder = context.getInitParameter(ParameterAccessor.INIT_PARAM_SCRIPTLIB_DIR);
		if (isDesigner && initScriptlibFolder == null) {
			initScriptlibFolder = rootPath + IBirtConstants.DEFAULT_SCRIPTLIB_FOLDER;
		}
		scriptLibDir = processRealPath(context, initScriptlibFolder, IBirtConstants.DEFAULT_SCRIPTLIB_FOLDER, false);

		// WebApp Locale setting
		webAppLocale = getLocaleFromString(context.getInitParameter(INIT_PARAM_LOCALE));
		if (webAppLocale == null) {
			webAppLocale = Locale.getDefault();
		}

		webAppTimeZone = getTimeZoneFromString(context.getInitParameter(INIT_PARAM_TIMEZONE));

		isWorkingFolderAccessOnly = Boolean
				.parseBoolean(context.getInitParameter(INIT_PARAM_WORKING_FOLDER_ACCESS_ONLY));

		urlReportPathPolicy = context.getInitParameter(INIT_PARAM_URL_REPORT_PATH_POLICY);

		// Get preview report max rows parameter from ServletContext
		String s_maxRows = context.getInitParameter(INIT_PARAM_VIEWER_MAXROWS);
		try {
			maxRows = Integer.parseInt(s_maxRows);
		} catch (NumberFormatException e) {
			maxRows = -1;
		}

		// Get preview report max cube fetch levels parameter from
		// ServletContext
		String s_maxRowLevels = context.getInitParameter(INIT_PARAM_VIEWER_MAXCUBE_ROWLEVELS);
		try {
			maxCubeRowLevels = Integer.parseInt(s_maxRowLevels);
		} catch (NumberFormatException e) {
			maxCubeRowLevels = -1;
		}

		String s_maxColumnLevels = context.getInitParameter(INIT_PARAM_VIEWER_MAXCUBE_COLUMNLEVELS);
		try {
			maxCubeColumnLevels = Integer.parseInt(s_maxColumnLevels);
		} catch (NumberFormatException e) {
			maxCubeColumnLevels = -1;
		}

		// Get cube memory size parameter from ServletContext
		String s_cubeMemSize = context.getInitParameter(INIT_PARAM_VIEWER_CUBEMEMSIZE);
		try {
			cubeMemorySize = Integer.parseInt(s_cubeMemSize);
		} catch (NumberFormatException e) {
			cubeMemorySize = 0;
		}

		// default resource path
		String initResourceFolder = context.getInitParameter(INIT_PARAM_BIRT_RESOURCE_PATH);
		if (isDesigner && initResourceFolder == null) {
			initResourceFolder = "${" + IBirtConstants.SYS_PROP_RESOURCE_PATH + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		birtResourceFolder = processRealPath(context, initResourceFolder, null, false);

		if (isDesigner) {
			// workaround for Bugzilla bug 231715
			// (must be removed once the web.xml is used for the designer)
			isOverWrite = true;
		} else {
			// get the overwrite flag
			String s_overwrite = DataUtil.trimString(context.getInitParameter(INIT_PARAM_OVERWRITE_DOCUMENT));
			if ("true".equalsIgnoreCase(s_overwrite)) //$NON-NLS-1$
			{
				isOverWrite = true;
			} else {
				isOverWrite = false;
			}
		}

		// initialize the application properties
		initProps = initViewerProps(context, initProps);

		if (loggers == null) {
			loggers = new HashMap();
		}

		// retrieve the logger names from the application properties
		for (Iterator i = initProps.keySet().iterator(); i.hasNext();) {
			String name = (String) i.next();
			if (name.startsWith("logger.")) //$NON-NLS-1$
			{
				String loggerName = name.replaceFirst("logger.", //$NON-NLS-1$
						"" //$NON-NLS-1$
				);
				String levelName = (String) initProps.get(name);

				loggers.put(loggerName, levelName);

				i.remove();
			}
		}

		// print on the server side
		String flag = DataUtil.trimString(context.getInitParameter(INIT_PARAM_PRINT_SERVERSIDE));
		if (IBirtConstants.VAR_ON.equalsIgnoreCase(flag)) {
			isSupportedPrintOnServer = true;
		} else if (IBirtConstants.VAR_OFF.equalsIgnoreCase(flag)) {
			isSupportedPrintOnServer = false;
		}

		// get agent style flag
		String s_agentstyle = context.getInitParameter(INIT_PARAM_AGENTSTYLE_ENGINE);
		if ("false".equalsIgnoreCase(s_agentstyle)) { //$NON-NLS-1$
			isAgentStyle = false;
		}

		// try from servlet context
		String exportFilenameGeneratorClassName = context.getInitParameter(INIT_PARAM_FILENAME_GENERATOR_CLASS);
		if (exportFilenameGeneratorClassName != null) {
			Object generatorInstance = null;
			try {
				Class generatorClass = Class.forName(exportFilenameGeneratorClassName);
				generatorInstance = generatorClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (generatorInstance != null) {
				if (generatorInstance instanceof IFilenameGeneratorFactory) {
					exportFilenameGenerator = ((IFilenameGeneratorFactory) generatorInstance)
							.createFilenameGenerator(context);
				} else if (generatorInstance instanceof IFilenameGenerator) {
					exportFilenameGenerator = (IFilenameGenerator) generatorInstance;
				}
			}
		}

		if (exportFilenameGenerator == null) {
			exportFilenameGenerator = new DefaultFilenameGenerator();
		}

		initViewingSessionConfig(documentFolder, imageFolder);

		// Finish init context
		isInitContext = true;
	}

	/**
	 * Initializes the viewing session configuration.
	 *
	 * @param documentFolder
	 * @param imageFolder
	 */
	private static void initViewingSessionConfig(String documentFolder, String imageFolder) {
		// instantiate viewing cache and configure the viewing session utility
		// class
		ViewingSessionUtil.viewingCache = new ViewingCache(documentFolder, imageFolder);
		ViewingSessionUtil.defaultConfig = new ViewingSessionConfig();
		long sessionTimeout = getLongInitProp("viewer.session.timeout"); //$NON-NLS-1$
		if (sessionTimeout <= 0l) {
			sessionTimeout = 0l;
		}
		ViewingSessionUtil.defaultConfig.setSessionTimeout(sessionTimeout);

		float sessionCountThresholdFactor = getFloatInitProp("viewer.session.loadFactor"); //$NON-NLS-1$
		if (sessionCountThresholdFactor >= 0.1f) {
			ViewingSessionUtil.defaultConfig.setSessionCountThresholdFactor(sessionCountThresholdFactor);
		}

		int minimumSessionCountThreshold = getIntegerInitProp("viewer.session.minimumThreshold"); //$NON-NLS-1$
		if (minimumSessionCountThreshold > 0) {
			ViewingSessionUtil.defaultConfig.setMinimumSessionCountThreshold(minimumSessionCountThreshold);
		}

		int maximumSessionCount = getIntegerInitProp("viewer.session.maximumSessionCount"); //$NON-NLS-1$
		if (maximumSessionCount >= 0) {
			ViewingSessionUtil.defaultConfig.setMaximumSessionCount(maximumSessionCount);
		}

		int maximumSessionCountPolicy = getIntegerInitProp("viewer.session.maximumSessionCountPolicy"); //$NON-NLS-1$
		switch (maximumSessionCountPolicy) {
		case 0:
			ViewingSessionUtil.defaultConfig.setMaxSessionCountPolicy(ViewingSessionPolicy.SESSION_POLICY_DISCARD_NEW);
			break;
		case 1:
			ViewingSessionUtil.defaultConfig
					.setMaxSessionCountPolicy(ViewingSessionPolicy.SESSION_POLICY_DISCARD_OLDEST);
			break;
		}
	}

	/**
	 * Check whether the viewer is used in designer or not.
	 *
	 * @return
	 */
	public static boolean isDesigner() {
		return isDesigner;
	}

	/***************************************************************************
	 * For export data
	 **************************************************************************/

	/**
	 * Check whether the request is to get image.
	 *
	 * @param request http request
	 * @return is get image or not
	 */

	public static boolean isGetImageOperator(HttpServletRequest request) {
		String imageName = getParameter(request, PARAM_IMAGEID);
		return imageName != null && imageName.length() > 0;
	}

	/**
	 * Returns whether the current servlet is the given servlet.
	 *
	 * @param request request
	 * @param servlet servlet to check
	 * @return true if the servlet path matches
	 */
	public static boolean isServlet(HttpServletRequest request, String servlet) {
		return servlet.equalsIgnoreCase(request.getServletPath());
	}

	/**
	 * Check whether the request is to get reportlet.
	 *
	 * @param request http request
	 * @return is get reportlet or not
	 */

	public static boolean isGetReportlet(HttpServletRequest request) {

		return isBookmarkReportlet(request) || isIidReportlet(request);
	}

	/**
	 * if the PARAM_ISREPORTLET is trure and the PARAM_BOOKMARK is not null, this
	 * method will return true. Otherwise, return false.
	 *
	 * @param request
	 * @return true for render the reportlet based on bookmark, else, false.
	 */
	public static boolean isBookmarkReportlet(HttpServletRequest request) {
		if ("true" //$NON-NLS-1$
				.equalsIgnoreCase(getParameter(request, PARAM_ISREPORTLET))) {
			String bookmark = getParameter(request, PARAM_BOOKMARK);
			return bookmark != null && bookmark.length() > 0;
		}
		return false;
	}

	/**
	 * if the PARAM_INSTANCEID parameter in the url is not null, then return true to
	 * render the reportlet.
	 *
	 * @param request
	 * @return true for render the reprtlet based on the instance id.
	 */
	public static boolean isIidReportlet(HttpServletRequest request) {
		String instanceId = getParameter(request, PARAM_INSTANCEID);
		return instanceId != null && instanceId.length() > 0;
	}

	/**
	 * Check whether the viewer allows master page content or not.
	 *
	 * @param request
	 * @return
	 */

	public static boolean isMasterPageContent(HttpServletRequest request) {
		boolean isMasterPageContent = true;

		if ("false".equalsIgnoreCase(getParameter(request, PARAM_MASTERPAGE))) //$NON-NLS-1$
		{
			isMasterPageContent = false;
		}

		return isMasterPageContent;
	}

	/**
	 * Check whether report design will overwrite report doc or not.
	 *
	 * @param request
	 * @return
	 */

	public static boolean isOverwrite(HttpServletRequest request) {
		boolean overwrite = isOverWrite;

		String urlParam = getParameter(request, PARAM_OVERWRITE);
		if ("true".equalsIgnoreCase(urlParam)) //$NON-NLS-1$
		{
			overwrite = true;
		} else if ("false".equalsIgnoreCase(urlParam)) //$NON-NLS-1$
		{
			overwrite = false;
		}

		return overwrite;
	}

	/**
	 * Checks if a given file name is a relative path. This will only check for
	 * local file path.
	 *
	 * @param fileName The file name.
	 * @return A <code>boolean</code> value indicating if the file name is a
	 *         relative path or not.
	 */

	public static boolean isRelativePath(String fileName) {
		if (fileName == null) {
			return false;
		}

		return !new File(fileName).isAbsolute();
	}

	/**
	 * Check if the given file path is a universal path, it could be either an
	 * absolute file path or a valid url path. This will check for both local file
	 * path and global url path like "http://", "jndi://", etc.
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isUniversalPath(String fileName) {
		if (fileName == null) {
			return false;
		}

		File f = new File(fileName);

		if (f.isAbsolute()) {
			return true;
		}

		try {
			new URL(fileName);
			return true;
		} catch (MalformedURLException e) {
		}

		return false;
	}

	/**
	 * Check whether report parameter exists in the url.
	 *
	 * @param request http request
	 * @param name    parameter name
	 * @return whether report parameter exists in the url
	 */

	public static boolean isReportParameterExist(HttpServletRequest request, String name) {
		assert request != null && name != null;

		boolean isExist = false;

		Map paramMap = request.getParameterMap();
		if (paramMap != null) {
			isExist = (paramMap.containsKey(name));
		}
		Set nullParams = getParameterValues(request, PARAM_ISNULL);
		if (nullParams != null && nullParams.contains(name)) {
			isExist = true;
		}

		return isExist;
	}

	/**
	 * If set isWorkingFolderAccessOnly as true, check the file if exist in working
	 * folder.
	 *
	 * @param filePath
	 * @return boolean
	 */

	public static boolean isValidFilePath(HttpServletRequest request, String filePath) {
		if (filePath == null) {
			return false;
		}

		// check and aply url report path policy
		if (!POLICY_ALL.equalsIgnoreCase(urlReportPathPolicy)) {
			File f = new File(filePath);

			if (!f.isAbsolute()) {
				try {
					URL url = new URL(filePath);

					if (POLICY_DOMAIN.equalsIgnoreCase(urlReportPathPolicy)) {
						String dm = request.getServerName();

						if (!dm.equals(url.getHost())) {
							return false;
						}
					} else {
						return false;
					}
				} catch (MalformedURLException e) {
					// ignore
				}
			}
		}

		if (isWorkingFolderAccessOnly) {
			// TODO check non-file path case

			File docFile = new File(filePath);
			if (!docFile.isAbsolute()) {
				if (filePath.indexOf("..") != -1) { //$NON-NLS-1$
					return false;
				}

				return true;
			}

			File docFolder = new File(workingFolder);
			if (docFolder.isAbsolute()) {
				String absolutePath = docFile.getAbsolutePath();
				String docFolderPath = docFolder.getAbsolutePath();

				// if OS is windows, ignore the case sensitive.
				if (isWindowsPlatform()) {
					absolutePath = absolutePath.toLowerCase();
					docFolderPath = docFolderPath.toLowerCase();
				}

				return absolutePath.startsWith(docFolderPath);
			} else {
				// if workingFolder is relative path, return false.
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets a named parameter from the http request. The given parameter name must
	 * be in UTF-8.
	 *
	 * @param request       incoming http request
	 * @param parameterName parameter name
	 * @return
	 */

	public static String getParameter(HttpServletRequest request, String parameterName) {

		if (request.getCharacterEncoding() == null) {
			try {
				request.setCharacterEncoding(UTF_8_ENCODE);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return request.getParameter(parameterName);
	}

	/**
	 * Get named parameter as integer from http request. parameter names and values
	 * are all in iso-8859-1 format in request.
	 *
	 * @param request
	 * @param parameterName
	 * @return
	 */

	protected static int getParameterAsInt(HttpServletRequest request, String parameterName) {
		int iValue = -1;
		String value = getParameter(request, parameterName);

		if (value != null && value.length() > 0) {
			try {
				iValue = Integer.parseInt(value);
			} catch (NumberFormatException ex) {
				iValue = -1;
			}
		}
		return iValue;
	}

	/**
	 * Get named parameters from http request. parameter names and values are all in
	 * iso-8859-1 format in request.
	 *
	 * @param request       incoming http request
	 * @param parameterName parameter name
	 * @return
	 */

	public static Set getParameterValues(HttpServletRequest request, String parameterName) {
		Set<String> parameterValues = null;
		String[] parameterValuesArray = request.getParameterValues(parameterName);

		if (parameterValuesArray != null) {
			parameterValues = new LinkedHashSet<>();

			for (int i = 0; i < parameterValuesArray.length; i++) {
				parameterValues.add(parameterValuesArray[i]);
			}
		}

		return parameterValues;
	}

	/**
	 * URL encoding based on incoming encoding format.
	 *
	 * @param s      string to be encoded.
	 * @param format encoding format.
	 * @return
	 */

	public static String urlEncode(String s, String format) {
		String encodedString = s;

		if (s != null) {
			try {
				encodedString = URLEncoder.encode(s, format);
			} catch (UnsupportedEncodingException e) {
				encodedString = s;
			}
		}

		return encodedString;
	}

	/**
	 * Encode a file name in base 64.
	 *
	 * @param fileName
	 * @return file name encoded in base 64
	 */
	public static String encodeBase64(String fileName) {
		if (fileName != null) {
			try {
				byte[] decodedBytes = fileName.getBytes(ParameterAccessor.UTF_8_ENCODE);
				byte[] encodedBytes = Base64.encodeBase64(decodedBytes);
				return new String(encodedBytes, ParameterAccessor.UTF_8_ENCODE);
			} catch (UnsupportedEncodingException e) {
				return fileName;
			}
		} else {
			return null;
		}
	}

	/**
	 * Decodes a base64 string.
	 *
	 * @param string
	 * @return
	 */
	public static String decodeBase64(String string) {
		try {
			byte[] encodedBytes = string.getBytes(ParameterAccessor.UTF_8_ENCODE);
			byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
			return new String(decodedBytes, ParameterAccessor.UTF_8_ENCODE);
		} catch (UnsupportedEncodingException e) {
			return string;
		}

	}

	/**
	 * Decodes a file name according to the value of the "encoded paths" flag.
	 *
	 * @param request
	 * @param filePath file path to decode
	 * @return
	 */
	public static String decodeFilePath(HttpServletRequest request, String filePath) {
		if (filePath == null) {
			return null;
		}
		filePath = htmlDecode(filePath);
		if (isEncodedPaths(request)) {
			return decodeBase64(filePath);
		} else {
			return filePath;
		}
	}

	/**
	 * Parse config file name from report design filename.
	 *
	 * @param reportDesignName String
	 * @return String
	 */

	public static String getConfigFileName(String reportDesignName) {
		if (reportDesignName == null) {
			return null;
		}

		String[] result = reportDesignName.split("\\."); //$NON-NLS-1$
		String extensionName = result[result.length - 1];
		String configFileName = reportDesignName.substring(0, reportDesignName.length() - extensionName.length())
				+ IBirtConstants.SUFFIX_DESIGN_CONFIG;

		return configFileName;
	}

	/**
	 * Get current format of parameter.
	 *
	 * @param request   HttpServletRequest
	 * @param paramName String
	 *
	 * @return String
	 */

	public static String getFormat(HttpServletRequest request, String paramName) {
		if (request == null || paramName == null) {
			return null;
		}

		return getParameter(request, paramName + "_format"); //$NON-NLS-1$
	}

	/**
	 * @return the isWorkingFolderAccessOnly
	 */
	public static boolean isWorkingFolderAccessOnly() {
		return isWorkingFolderAccessOnly;
	}

	/**
	 * @return the url report path accessing policy
	 */
	public static String getUrlReportPathPolicy() {
		return urlReportPathPolicy;
	}

	/**
	 * if display text of select parameter
	 *
	 * @param paramName
	 * @return
	 */
	public static String isDisplayText(String paramName) {
		if (paramName == null) {
			return null;
		}

		if (paramName.startsWith(PREFIX_DISPLAY_TEXT)) {
			return paramName.replaceFirst(PREFIX_DISPLAY_TEXT, ""); //$NON-NLS-1$
		}

		return null;
	}

	/**
	 * Generates a file name for output attachment.
	 *
	 * @param request
	 * @param format
	 * @return the file name
	 */
	public static IFilenameGenerator getFilenameGenerator() {
		return exportFilenameGenerator;
	}

	/**
	 * Reset isInitContext flag
	 */
	public static void reset() {
		isInitContext = false;
	}

	/**
	 *
	 * Check if OS system is windows
	 *
	 * @return boolean
	 */
	public static boolean isWindowsPlatform() {
		return System.getProperty("os.name").toLowerCase().indexOf( //$NON-NLS-1$
				"windows") >= 0; //$NON-NLS-1$
	}

	/**
	 * Get the resource folder.
	 *
	 * @param request the request to retrieve
	 * @return the resource folder of the request
	 */

	public static String getResourceFolder(HttpServletRequest request) {
		// get resource folder from request first
		String resourceFolder = getParameter(request, PARAM_RESOURCE_FOLDER);

		resourceFolder = decodeFilePath(request, resourceFolder);

		// set it as init params from web.xml
		if (resourceFolder == null || resourceFolder.trim().length() <= 0) {
			resourceFolder = birtResourceFolder;
		}

		return resourceFolder;
	}

	/**
	 * Push user-defined application context object into engine context map. The
	 * user-defined application context is retrieved from the http request, if
	 * available, else from the session. If nothing is found, nothing is added and
	 * the map is returned as is.
	 *
	 * @param map     application context map
	 * @param request http request object containing appContext key to push
	 * @return map containing the appContext key
	 */
	public static Map pushAppContext(Map map, HttpServletRequest request) {
		if (map == null) {
			map = new HashMap();
		}

		// Get application context key from request
		String appContextKey = (String) request.getAttribute(ATTR_APPCONTEXT_KEY);

		if (appContextKey != null) {
			map.put(appContextKey, request.getAttribute(ATTR_APPCONTEXT_VALUE));
		} else {
			// check if the session contains it
			HttpSession session = request.getSession(false);
			if (session != null) {
				// Get application context key from the session
				appContextKey = (String) session.getAttribute(ATTR_APPCONTEXT_KEY);
				if (appContextKey != null) {
					map.put(appContextKey, session.getAttribute(ATTR_APPCONTEXT_VALUE));

				}
			}
		}

		return map;
	}

	/**
	 * Returns the encoding for export data.
	 *
	 * @param request
	 * @return
	 */

	public static String getExportEncoding(HttpServletRequest request) {
		String encoding = getParameter(request, PARAM_EXPORT_ENCODING);

		// use UTF-8 as the default encoding
		if (encoding == null) {
			encoding = UTF_8_ENCODE;
		}

		return encoding;
	}

	/**
	 * Check whether show the report title.
	 *
	 * @param request
	 * @return
	 */

	public static boolean isShowTitle(HttpServletRequest request) {
		boolean isTitle = true;

		if ("false".equalsIgnoreCase(getParameter(request, PARAM_SHOW_TITLE))) //$NON-NLS-1$
		{
			isTitle = false;
		}

		return isTitle;
	}

	/**
	 * Check whether show the toolbar.
	 *
	 * @param request
	 * @return
	 */

	public static boolean isShowToolbar(HttpServletRequest request) {
		boolean isToolbar = true;

		if ("false".equalsIgnoreCase(getParameter(request, PARAM_TOOLBAR))) //$NON-NLS-1$
		{
			isToolbar = false;
		}

		return isToolbar;
	}

	/**
	 * Check whether show the navigationbar.
	 *
	 * @param request
	 * @return
	 */

	public static boolean isShowNavigationbar(HttpServletRequest request) {
		boolean isNavigationbar = true;

		if ("false".equalsIgnoreCase(getParameter(request, PARAM_NAVIGATIONBAR))) //$NON-NLS-1$
		{
			isNavigationbar = false;
		}

		return isNavigationbar;
	}

	/**
	 * Check whether show parameter dialog or not. Default to false.
	 *
	 * @param request
	 * @return
	 */

	public static String getShowParameterPage(HttpServletRequest request) {
		return getParameter(request, PARAM_PARAMETER_PAGE);
	}

	/**
	 * Returns the application properties
	 *
	 * @param context
	 * @param props
	 * @return
	 */
	public synchronized static Map initViewerProps(ServletContext context, Map props) {
		// initialize map
		if (props == null) {
			props = new HashMap();
		}

		// get config file
		String file = context.getInitParameter(INIT_PARAM_CONFIG_FILE);
		if (file == null || file.trim().length() <= 0) {
			file = IBirtConstants.DEFAULT_VIEWER_CONFIG_FILE;
		}

		try {

			InputStream is = null;
			if (isRelativePath(file)) {
				// realtive path
				if (!file.startsWith("/")) { // $NON-NLS-1$
					file = "/" + file; //$NON-NLS-1$
				}

				is = context.getResourceAsStream(file);
			} else {
				// absolute path
				is = new FileInputStream(file);
			}

			// parse the properties file
			PropertyResourceBundle bundle = new PropertyResourceBundle(is);
			if (bundle != null) {
				Enumeration<String> keys = bundle.getKeys();
				while (keys != null && keys.hasMoreElements()) {
					String key = keys.nextElement();
					String value = (String) bundle.getObject(key);
					if (key != null && value != null) {
						props.put(key, value);
					}
				}
			}
		} catch (Exception e) {
		}

		return props;
	}

	/**
	 * Returns the property by name from initialized properties map
	 *
	 * @param key
	 * @return
	 */
	public static String getInitProp(String key) {
		if (initProps == null || key == null) {
			return null;
		}

		return (String) initProps.get(key);
	}

	/**
	 * Returns the property by name from initialized properties map
	 *
	 * @param key
	 * @return
	 */
	public static int getIntegerInitProp(String key) {
		String value = getInitProp(key);
		if (value == null) {
			return 0;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Returns the property by name from initialized properties map
	 *
	 * @param key
	 * @return
	 */
	public static long getLongInitProp(String key) {
		String value = getInitProp(key);
		if (value == null) {
			return 0l;
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return 0l;
		}
	}

	/**
	 * Returns the property by name from initialized properties map
	 *
	 * @param key
	 * @return
	 */
	public static float getFloatInitProp(String key) {
		String value = getInitProp(key);
		if (value == null) {
			return 0.0f;
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return 0.0f;
		}
	}

	/**
	 * Returns the extension name according to format
	 *
	 * @param format
	 * @return
	 */
	public static String getExtensionName(String format) {
		if (format == null) {
			return null;
		}

		String key = "viewer.extension." + format.replace(' ', '_'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return DataUtil.trimString(getInitProp(key));
	}

	/**
	 * Returns the output format label name
	 *
	 * @param format
	 * @return
	 */
	public static String getOutputFormatLabel(String format) {
		if (format == null) {
			return null;
		}

		String key = "viewer.label." + format.replace(' ', '_'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String label = DataUtil.trimString(getInitProp(key));
		if (label.length() <= 0) {
			label = format;
		}

		return label;
	}

	/**
	 * Returns the base url defined in config file
	 *
	 * @return
	 */
	public static String getBaseURL() {
		String baseURL = getInitProp(PROP_BASE_URL);
		if (baseURL != null && baseURL.length() > 0) {
			if (baseURL.endsWith("/")) { //$NON-NLS-1$
				baseURL = baseURL.substring(0, baseURL.length() - 1);
			}
		}

		return baseURL;
	}

	/**
	 * convert path from System Properties Definition. For example:
	 * ${java.io.tmpdir}
	 *
	 * @param path
	 * @return
	 */
	protected static String convertSystemPath(String path) {
		if (path == null) {
			return path;
		}

		// parse System Properties
		Pattern p = Pattern.compile("\\$\\s*\\{([^\\}]*)\\}\\s*(.*)", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(path);
		if (m.find()) {
			String sysPath = DataUtil.trimSepEnd(System.getProperty(m.group(1).trim()));
			if (sysPath.length() <= 0) {
				return DataUtil.trimSepFirst(m.group(2).trim());
			} else {
				return sysPath + m.group(2).trim();
			}
		}

		return path;
	}

	/**
	 * Process working folder setting. If path is a relative path, first relative to
	 * context.
	 *
	 * @param context
	 * @param path
	 * @return
	 */
	public static String processWorkingFolder(ServletContext context, String path) {
		path = convertSystemPath(DataUtil.trimString(path));
		String realPath = null;

		if (isRelativePath(path)) {
			// If path is a relative path
			realPath = getRealPath(path, context);
		} else {
			// Path is an absolute path
			realPath = path;
		}

		// try to create folder
		makeDir(realPath);
		return DataUtil.trimSepEnd(realPath);
	}

	/**
	 * Process folder settings with absolute path. If path is a relative path, first
	 * relative to context. If set canWrite to true, then check the folder if
	 * writable.If not, relative to ${java.io.tmpdir} folder.
	 *
	 * @param context
	 * @param path
	 * @param defaultPath
	 * @param canWrite
	 * @return
	 */
	private static String processRealPath(ServletContext context, String path, String defaultPath, boolean canWrite) {
		String realPath = null;
		boolean isRelative = false;

		path = convertSystemPath(path);

		// Using default path
		if (path == null || path.trim().length() <= 0) {
			path = DataUtil.trimString(defaultPath);
		}

		// If path is a relative path
		if (isRelativePath(path)) {
			isRelative = true;
			if (!path.startsWith("/")) { // $NON-NLS-1$
				path = "/" + path; //$NON-NLS-1$
			}

			realPath = DataUtil.trimSepEnd(getRealPath(path, context));
		} else {
			// Path is an absolute path
			realPath = DataUtil.trimSepEnd(path);
		}

		boolean flag = makeDir(realPath);

		// don't need writable
		if (!canWrite) {
			return realPath;
		}

		// check if the folder is writable
		if (flag) {
			try {
				if (canWrite && new File(realPath).canWrite()) {
					return realPath;
				}
			} catch (Exception e) {
			}
		}

		// try to create folder in ${java.io.tmpdir}
		if (isRelative) {
			realPath = DataUtil.trimSepEnd(System.getProperty("java.io.tmpdir")) + path; //$NON-NLS-1$
		} else // if absolute path, create default path in temp folder
		if (defaultPath != null) {
			realPath = DataUtil.trimSepEnd(System.getProperty("java.io.tmpdir")) + File.separator + defaultPath; //$NON-NLS-1$
		}

		// try to create folder
		makeDir(realPath);

		return realPath;
	}

	/**
	 * Returns real path relative to context
	 *
	 * @param path
	 * @param context
	 * @return
	 */
	private static String getRealPath(String path, ServletContext context) {
		assert path != null;
		String realPath = null;
		try {
			String orginalPath = path;

			if (!path.startsWith("/")) //$NON-NLS-1$
			{
				path = "/" + path; //$NON-NLS-1$
			}

			realPath = context.getRealPath(path);
			if (realPath == null) {
				// try to get root path from system properties
				String rootPath = System.getProperty(IBirtConstants.SYS_PROP_ROOT_PATH);
				if (rootPath != null && isUniversalPath(rootPath)) {
					path = path.substring(1);
					realPath = DataUtil.trimSepEnd(rootPath) + "/" + path; //$NON-NLS-1$
				} else {
					URL url = context.getResource("/"); //$NON-NLS-1$
					if (url != null) {
						String urlRoot = null;
						// for file urls
						if ("file".equalsIgnoreCase(url.getProtocol())) //$NON-NLS-1$
						{
							urlRoot = DataUtil.trimString(url.getPath());
						}
						// for other url protocals, e.g. path in an unpacked
						// war, or other global urls
						else {
							urlRoot = DataUtil.trimString(url.toExternalForm());
						}
						if (orginalPath.startsWith(urlRoot)) {
							realPath = orginalPath;
						} else if (urlRoot.endsWith("/") //$NON-NLS-1$
								|| orginalPath.startsWith("/")) //$NON-NLS-1$
						{
							realPath = urlRoot + orginalPath;
						} else {
							realPath = urlRoot + "/" + orginalPath; //$NON-NLS-1$
						}
					}
				}
			}
		} catch (Exception e) {
			realPath = path;
		}

		return realPath;
	}

	/**
	 * Make directory
	 *
	 * @param path
	 * @return
	 */
	private static boolean makeDir(String path) {
		assert path != null;
		File file = new File(path);
		if (!file.exists()) {
			return file.mkdirs();
		}

		return true;
	}

	/**
	 * Returns the overflow mode
	 *
	 * @param request
	 * @return
	 */
	public static int getPageOverflow(HttpServletRequest request) {
		int pageOverflow = getParameterAsInt(request, PARAM_PAGE_OVERFLOW);
		if (pageOverflow < 0) {
			pageOverflow = IBirtConstants.PAGE_OVERFLOW_AUTO;
		}

		return pageOverflow;
	}

	/**
	 * Returns if pagebreak pagination only
	 *
	 * @param request
	 * @return
	 */
	public static boolean isPagebreakOnly(HttpServletRequest request) {
		String pagebreakOnly = getParameter(request, PARAM_PAGEBREAK_ONLY);
		if ("false".equalsIgnoreCase(pagebreakOnly)) { //$NON-NLS-1$
			return false;
		}

		return true;
	}

	/**
	 * Returns how to open attachment( inline or attachment )
	 *
	 * @param request
	 * @return
	 */
	public static String getOpenType(HttpServletRequest request) {
		if ("true".equalsIgnoreCase(getParameter(request, //$NON-NLS-1$
				PARAM_AS_ATTACHMENT))) {
			return IBirtConstants.OPEN_TYPE_ATTACHMENT;
		}

		return IBirtConstants.OPEN_TYPE_INLINE;
	}

	/**
	 * Returns whether open report as attachment
	 *
	 * @param request
	 * @return
	 */
	public static boolean isOpenAsAttachment(HttpServletRequest request) {
		if ("true".equalsIgnoreCase(getParameter(request, //$NON-NLS-1$
				PARAM_AS_ATTACHMENT))) {
			return true;
		}

		return false;
	}

	/**
	 * Returns action name
	 *
	 * @param request
	 * @return
	 */
	public static String getAction(HttpServletRequest request) {
		return getParameter(request, PARAM_ACTION);
	}

	/**
	 * Returns the dpi setting from http request
	 *
	 * @param request
	 * @return
	 */
	public static Number getDpi(HttpServletRequest request) {
		String dpi = getParameter(request, PARAM_DPI);
		if (dpi == null || dpi.trim().length() <= 0) {
			return null;
		}

		return Integer.valueOf(dpi);
	}

	/**
	 * Check If force optimized HTML output.
	 *
	 * @param request
	 * @return
	 */
	public static boolean isAgentStyle(HttpServletRequest request) {
		boolean flag = isAgentStyle;

		String urlParam = getParameter(request, PARAM_AGENTSTYLE_ENGINE);
		if ("true".equalsIgnoreCase(urlParam)) //$NON-NLS-1$
		{
			flag = true;
		} else if ("false".equalsIgnoreCase(urlParam)) //$NON-NLS-1$
		{
			flag = false;
		}

		return flag;
	}

	/**
	 * Check whether the output format uses PDF Layout
	 *
	 * @param format
	 * @return
	 */
	public static boolean isPDFLayout(String format) {
		if (format == null) {
			return false;
		}

		if (IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase(format)
				|| IBirtConstants.POSTSCRIPT_RENDER_FORMAT.equalsIgnoreCase(format)
				|| IBirtConstants.PPT_RENDER_FORMAT.equalsIgnoreCase(format)) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the flag to indicate whether close current window. Currently, it is
	 * only used when output confirm information.
	 *
	 * @param request
	 * @return
	 */
	public static boolean isCloseWindow(HttpServletRequest request) {
		String isCloseWin = getParameter(request, PARAM_CLOSEWIN);
		if ("true".equalsIgnoreCase(isCloseWin)) { //$NON-NLS-1$
			return true;
		}

		return false;
	}

	/**
	 * Returns whether the "encoded paths" flag is set.
	 *
	 * @param request
	 * @return true if the flag is set
	 */
	public static boolean isEncodedPaths(HttpServletRequest request) {
		String encodedPaths = getParameter(request, PARAM_ENCODED_PATHS);
		return ("true".equalsIgnoreCase(encodedPaths)); //$NON-NLS-1$
	}

	/**
	 * Returns the appcontext extension name
	 *
	 * @param request
	 * @return
	 */
	public static String getAppContextName(HttpServletRequest request) {
		return getParameter(request, PARAM_APPCONTEXTNAME);
	}

	/**
	 * Returns the data extraction format
	 *
	 * @param request
	 * @return
	 */
	public static String getExtractFormat(HttpServletRequest request) {
		return getParameter(request, PARAM_DATA_EXTRACT_FORMAT);
	}

	/**
	 * Returns the data extraction extension
	 *
	 * @param request
	 * @return
	 */
	public static String getExtractExtension(HttpServletRequest request) {
		return getParameter(request, PARAM_DATA_EXTRACT_EXTENSION);
	}

	/**
	 * Returns all URL parameters as map
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParameterAsMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();

		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = getParameter(request, name);
			map.put(name, value);
		}

		// decode paths if necessary
		if (isEncodedPaths(request)) {
			if (map.containsKey(PARAM_RESOURCE_FOLDER)) {
				map.put(PARAM_RESOURCE_FOLDER, decodeBase64(map.get(PARAM_RESOURCE_FOLDER)));
			}
			if (map.containsKey(PARAM_REPORT)) {
				map.put(PARAM_REPORT, decodeBase64(map.get(PARAM_REPORT)));
			}
			if (map.containsKey(PARAM_REPORT_DOCUMENT)) {
				map.put(PARAM_REPORT_DOCUMENT, decodeBase64(map.get(PARAM_REPORT_DOCUMENT)));
			}
		}

		return map;
	}

	/**
	 * Gets the mime-type of the given emitter id or format. If the emitter id is
	 * defined, use it, else use the format.
	 *
	 * @param emitterId emitter id
	 * @param format    format
	 * @return mime-type of the extended emitter format
	 */
	public static String getEmitterMimeType(String emitterId) {
		if (emitterId != null) {
			EmitterInfo emitterInfo = getEmitterInfo(emitterId);
			if (emitterInfo != null) {
				return emitterInfo.getMimeType();
			}
		}
		return null;
	}

	/**
	 * Returns the emitter info for a given emitter id.
	 *
	 * @param emitterId emitter ID
	 * @return EmitterInfo instance or null if the emitterId is invalid
	 */
	public static EmitterInfo getEmitterInfo(String emitterId) {
		return (EmitterInfo) supportedEmitters.get(emitterId);
	}

	/**
	 * Returns the format returned by the emitter designed by the given emitter id.
	 *
	 * @param emitterId emitter id
	 * @return format string or null if no emitter exists
	 */
	public static String getEmitterFormat(String emitterId) {
		EmitterInfo emitterInfo = getEmitterInfo(emitterId);
		if (emitterInfo != null) {
			return emitterInfo.getFormat();
		} else {
			return null;
		}
	}

	/**
	 * Gets the mime-type of the given data extraction format.
	 *
	 * @param format
	 * @return mime-type of the extended data extraction format
	 */
	public static String getExtractionMIMEType(String extractFormat, String extractExtension) {
		if (supportedDataExtractions.length <= 0) {
			return null;
		}

		String mimeType = null;
		if (extractExtension != null) {
			// get MIME type by extension id
			for (int i = 0; i < supportedDataExtractions.length; i++) {
				DataExtractionFormatInfo info = supportedDataExtractions[i];
				if (info != null && extractExtension.equals(info.getId())) {
					mimeType = info.getMimeType();
					break;
				}
			}
		} else if (extractFormat != null) {
			// get MIME type by extraction format
			for (int i = 0; i < supportedDataExtractions.length; i++) {
				DataExtractionFormatInfo info = supportedDataExtractions[i];
				if (info != null && extractFormat.equals(info.getFormat())) {
					mimeType = info.getMimeType();
					break;
				}
			}
		}

		return mimeType;
	}

	/**
	 * Returns the extract format by extract extension id.
	 *
	 * @param extractExtension
	 * @return
	 */
	public static String getExtractFormat(String extractExtension) {
		if (supportedDataExtractions.length <= 0) {
			return null;
		}

		String extractFormat = null;

		// get extraction format by extension id
		for (int i = 0; i < supportedDataExtractions.length; i++) {
			DataExtractionFormatInfo info = supportedDataExtractions[i];
			if (info != null && extractExtension.equals(info.getId())) {
				extractFormat = info.getFormat();
				break;
			}
		}

		return extractFormat;
	}

	/**
	 * Validate extract format
	 *
	 * @param extractFormat
	 * @return
	 */
	public static boolean validateExtractFormat(String extractFormat) {
		if (supportedDataExtractions.length <= 0 || extractFormat == null) {
			return false;
		}

		// validate extraction format
		for (int i = 0; i < supportedDataExtractions.length; i++) {
			DataExtractionFormatInfo info = supportedDataExtractions[i];
			if (info != null && extractFormat.equals(info.getFormat())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Validate extract extension
	 *
	 * @param extractExtension
	 * @return
	 */
	public static boolean validateExtractExtension(String extractExtension) {
		if (supportedDataExtractions.length <= 0 || extractExtension == null) {
			return false;
		}

		// validate extraction extension id
		for (int i = 0; i < supportedDataExtractions.length; i++) {
			DataExtractionFormatInfo info = supportedDataExtractions[i];
			if (info != null && extractExtension.equals(info.getId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Creates an options map for the filename generator.
	 *
	 * @param context context
	 * @return options map
	 * @see IFilenameGenerator
	 */
	public static Map makeFilenameGeneratorOptions(IContext context) {
		HttpServletRequest request = context.getRequest();
		Map options = new HashMap();
		options.put(IFilenameGenerator.OPTIONS_SERVLET_CONTEXT, request.getSession().getServletContext());
		options.put(IFilenameGenerator.OPTIONS_HTTP_REQUEST, request);
		BaseAttributeBean attrBean = (BaseAttributeBean) context.getBean();
		options.put(IFilenameGenerator.OPTIONS_VIEWER_ATTRIBUTES_BEAN, attrBean);
		if (attrBean != null) {
			String reportDesignName = attrBean.getReportDesignName();
			String reportDocumentName = attrBean.getReportDocumentName();
			if (reportDesignName != null) {
				File reportDesign = new File(reportDesignName);
				options.put(IFilenameGenerator.OPTIONS_REPORT_DESIGN, reportDesign.getName());
			}
			if (reportDocumentName != null) {
				File reportDocument = new File(reportDocumentName);
				options.put(IFilenameGenerator.OPTIONS_REPORT_DOCUMENT, reportDocument.getName());
			}
		}
		return options;
	}

	/**
	 * @param context
	 * @param format
	 * @param emitterId
	 * @return
	 */
	public static String getExportFilename(IContext context, String format, String emitterId) {
		IFilenameGenerator gen = ParameterAccessor.getFilenameGenerator();
		Map options = ParameterAccessor.makeFilenameGeneratorOptions(context);

		if (emitterId != null) {
			EmitterInfo emitterInfo = ParameterAccessor.getEmitterInfo(emitterId);
			if (emitterInfo != null) {
				options.put(IFilenameGenerator.OPTIONS_EMITTER_INFO, emitterInfo);
			}
		}
		String extensionName = ParameterAccessor.getExtensionName(format);
		if (extensionName != null) {
			options.put(IFilenameGenerator.OPTIONS_TARGET_FILE_EXTENSION, extensionName);
		}

		String baseName = (String) options.get(IFilenameGenerator.OPTIONS_REPORT_DESIGN);
		if (baseName == null || baseName.length() == 0) {
			baseName = (String) options.get(IFilenameGenerator.OPTIONS_REPORT_DOCUMENT);
		}

		baseName = stripFileExtension(baseName);
		return gen.getFilename(baseName, extensionName, IFilenameGenerator.OUTPUT_TYPE_EXPORT, options);
	}

	/**
	 * Returns the extraction file name.
	 *
	 * @param context       birt context
	 * @param extractFormat extraction extension
	 * @return extraction file name
	 */
	public static String getExtractionFilename(IContext context, String extractExtension, String extractFormat) {
		IFilenameGenerator gen = ParameterAccessor.getFilenameGenerator();
		Map options = ParameterAccessor.makeFilenameGeneratorOptions(context);
		if (extractFormat != null) {
			options.put(IFilenameGenerator.OPTIONS_TARGET_FILE_EXTENSION, extractFormat);
		}
		if (extractExtension != null) {
			options.put(IFilenameGenerator.OPTIONS_EXTRACTION_EXTENSION, extractExtension);
		}

		String baseName = stripFileExtension((String) options.get(IFilenameGenerator.OPTIONS_REPORT_DOCUMENT));
		return gen.getFilename(baseName, extractFormat, IFilenameGenerator.OUTPUT_TYPE_DATA_EXTRACTION, options);
	}

	/**
	 * Returns the report document name based on the report design name.
	 *
	 * @param context birt context
	 * @return report document name
	 */
	public static String getGeneratedReportDocumentName(IContext context) {
		IFilenameGenerator gen = ParameterAccessor.getFilenameGenerator();
		Map options = ParameterAccessor.makeFilenameGeneratorOptions(context);
		String baseName = stripFileExtension((String) options.get(IFilenameGenerator.OPTIONS_REPORT_DESIGN));
		return gen.getFilename(baseName, IBirtConstants.SUFFIX_DESIGN_DOCUMENT,
				IFilenameGenerator.OUTPUT_TYPE_REPORT_DOCUMENT, options);
	}

	/**
	 * Returns the file name without extension from a base file name.
	 *
	 * @param baseName file name to strip
	 * @return file name without extension
	 */
	public static String stripFileExtension(String baseName) {
		String fileName = baseName;

		if (baseName == null || baseName.trim().length() <= 0) {
			return fileName;
		}

		// get the report design name, then extract the name without
		// file extension and set it to fileName; otherwise do noting and
		// let fileName with the default name
		int dotIndex = baseName.lastIndexOf('.');
		if (dotIndex > 0) {
			fileName = baseName.substring(0, dotIndex);
		}

		return fileName;
	}

	public static String[] sortSupportedFormatsByDisplayName(String[] values) {
		Arrays.sort(values, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if (getOutputFormatLabel(o1) != null) {
					return getOutputFormatLabel(o1).compareToIgnoreCase(getOutputFormatLabel(o2));
				}
				return o1.compareToIgnoreCase(o2);
			}
		});
		return values;
	}
}
