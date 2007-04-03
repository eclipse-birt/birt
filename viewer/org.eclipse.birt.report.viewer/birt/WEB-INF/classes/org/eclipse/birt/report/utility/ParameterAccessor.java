/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;

/**
 * Utilites class for all types of URl related operatnios.
 * <p>
 */

public class ParameterAccessor
{

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
	 * URL parameter name that gives the format to display the report, html or
	 * pdf.
	 */
	public static final String PARAM_FORMAT = "__format"; //$NON-NLS-1$

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
	 * URL parameter name that gives the report design name.
	 */
	public static final String PARAM_ISNULL = "__isnull"; //$NON-NLS-1$

	/**
	 * URL parameter name that indicate the report parameter as a locale string.
	 */
	public static final String PARAM_ISLOCALE = "__islocale"; //$NON-NLS-1$

	/**
	 * URL parameter name that determines to support masterpage or not.
	 */
	public static final String PARAM_MASTERPAGE = "__masterpage"; //$NON-NLS-1$

	/**
	 * URL parameter name that determines whether the BIRT application is
	 * running in the designer or standalone.
	 */
	public static final String PARAM_DESIGNER = "__designer"; //$NON-NLS-1$

	/**
	 * URL parameter name that determines whether to overwrite the document or
	 * not.
	 */
	public static final String PARAM_OVERWRITE = "__overwrite"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the image ID to display.
	 */
	public static final String PARAM_IMAGEID = "__imageID"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the bookmark expression.
	 */
	public static final String PARAM_BOOKMARK = "__bookmark"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives that image rtl option.
	 */
	public static final String PARAM_RTL = "__rtl"; //$NON-NLS-1$

	/**
	 * URL parameter name that gives the preview max rows option.
	 */
	public static final String PARAM_MAXROWS = "__maxrows"; //$NON-NLS-1$

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
	public static final String PARAM_EXPORT_ENCODING = "__exportEncoding";//$NON-NLS-1$

	/**
	 * URL parameter name to indicate if fit to page when render report as PDF.
	 */
	public static final String PARAM_FIT_TO_PAGE = "__fittopage";//$NON-NLS-1$

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
	 * Custom request headers to identify the request is a normal HTTP request
	 * or a soap request by AJAX.
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
	 * Parameter name that gives the result set names of the export data form.
	 */
	public static final String PARAM_RESULTSETNAME = "ResultSetName"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the selected column numbers of the export data
	 * form.
	 */
	public static final String PARAM_SELECTEDCOLUMNNUMBER = "SelectedColumnNumber"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the selected column names of the export data
	 * form.
	 */
	public static final String PARAM_SELECTEDCOLUMN = "SelectedColumn"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the default locale of the BIRT viewer.
	 */
	public static final String INIT_PARAM_LOCALE = "BIRT_VIEWER_LOCALE"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the working folder of the local BIRT
	 * viewer user.
	 */
	public static final String INIT_PARAM_WORKING_DIR = "BIRT_VIEWER_WORKING_FOLDER"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the repository location of the image
	 * files.
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
	 * Context parameter name that gives the repository location of the script
	 * files to run reports.
	 */
	public static final String INIT_PARAM_SCRIPTLIB_DIR = "BIRT_VIEWER_SCRIPTLIB_DIR"; //$NON-NLS-1$

	/**
	 * Context parameter name that determines the search strategy of searching
	 * report resources. True if only search the working folder, otherwise
	 * false.
	 */
	public static final String INIT_PARAM_WORKING_FOLDER_ACCESS_ONLY = "WORKING_FOLDER_ACCESS_ONLY"; //$NON-NLS-1$

	/**
	 * The parameter name that gives the repository lication to put the created
	 * documents and report design files.
	 */
	public static final String INIT_PARAM_DOCUMENT_FOLDER = "BIRT_VIEWER_DOCUMENT_FOLDER"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives the absolute resource location
	 * directory.
	 */
	public static final String INIT_PARAM_BIRT_RESOURCE_PATH = "BIRT_RESOURCE_PATH"; //$NON-NLS-1$

	/**
	 * Context parameter name that gives preview report max rows limited.
	 */
	public static final String INIT_PARAM_VIEWER_MAXROWS = "BIRT_VIEWER_MAX_ROWS"; //$NON-NLS-1$

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
	 * Suffix of report document.
	 */
	public static final String SUFFIX_REPORT_DOCUMENT = ".rptdocument"; //$NON-NLS-1$

	/**
	 * Report working folder.
	 */
	public static String workingFolder = null;

	/**
	 * Document folder to put the report files and created documents.
	 */
	public static String documentFolder = null;

	/**
	 * Image folder to put the image files
	 */
	public static String imageFolder = null;

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
	 * Current web application locale.
	 */
	public static Locale webAppLocale = null;

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
	 * Application Context Attribute Name
	 */
	public static final String ATTR_APPCONTEXT_KEY = "AppContextKey"; //$NON-NLS-1$

	/**
	 * Application Context Attribute value
	 */
	public static final String ATTR_APPCONTEXT_VALUE = "AppContextValue"; //$NON-NLS-1$

	/**
	 * The initialized properties map
	 */
	public static Map initProps;

	/**
	 * viewer properties
	 */
	public static final String PROP_BASE_URL = "base_url"; //$NON-NLS-1$

	/**
	 * Engine supported output formats
	 */
	public static String[] supportedFormats = {PARAM_FORMAT_HTML,
			PARAM_FORMAT_PDF};

	/**
	 * Flag that indicated if support print on the server side.
	 */
	public static boolean isSupportedPrintOnServer = true;

	/**
	 * Get bookmark. If page exists, ignore bookmark.
	 * 
	 * @param request
	 * @return the bookemark
	 */

	public static String getBookmark( HttpServletRequest request )
	{
		int page = getParameterAsInt( request, PARAM_PAGE );
		return page < 1
				? getReportParameter( request, PARAM_BOOKMARK, null )
				: null;
	}

	/**
	 * Get query string with new parameter value.
	 * 
	 * @param request
	 *            http request
	 * @param name
	 *            parameter name
	 * @param value
	 *            default parameter value
	 * @return new query string with new parameter value
	 */

	public static String getEncodedQueryString( HttpServletRequest request,
			String name, String value )
	{
		String queryString = ""; //$NON-NLS-1$
		Enumeration e = request.getParameterNames( );
		Set nullParams = getParameterValues( request, PARAM_ISNULL );
		boolean isFirst = true;

		while ( e.hasMoreElements( ) )
		{
			String paramName = (String) e.nextElement( );

			if ( paramName != null
					&& !paramName.equalsIgnoreCase( PARAM_ISNULL ) )
			{
				String paramValue = getParameter( request, paramName, false );

				if ( nullParams != null
						&& nullParams.remove( toUTFString( paramName ) )
						&& !paramName.equalsIgnoreCase( name ) ) // Parameter
				// value is
				// null.
				{
					paramName = urlEncode( paramName, ISO_8859_1_ENCODE );
					queryString += ( isFirst ? "" : PARAMETER_SEPARATOR ) + PARAM_ISNULL + EQUALS_OPERATOR + paramName; //$NON-NLS-1$
					isFirst = false;
					continue;
				}

				if ( paramName.equalsIgnoreCase( name ) )
				{
					paramValue = value;
				}

				paramName = urlEncode( paramName, ISO_8859_1_ENCODE );
				paramValue = urlEncode( paramValue, UTF_8_ENCODE );
				queryString += ( isFirst ? "" : PARAMETER_SEPARATOR ) + paramName + EQUALS_OPERATOR + paramValue; //$NON-NLS-1$
				isFirst = false;
			}
		}

		if ( nullParams != null && nullParams.size( ) > 0 )
		{
			Iterator i = nullParams.iterator( );

			while ( i.hasNext( ) )
			{
				String paramName = (String) i.next( );

				if ( paramName != null && !paramName.equalsIgnoreCase( name ) )
				{
					paramName = urlEncode( paramName, UTF_8_ENCODE );
					queryString += ( isFirst ? "" : PARAMETER_SEPARATOR ) + PARAM_ISNULL + EQUALS_OPERATOR + paramName; //$NON-NLS-1$ 
					isFirst = false;
				}
			}
		}

		if ( name != null && name.length( ) > 0 ) // Only handle valid name.
		{
			if ( getParameter( request, name ) == null )
			{
				String paramValue = value;
				paramValue = urlEncode( paramValue, UTF_8_ENCODE );
				queryString += ( isFirst ? "" : PARAMETER_SEPARATOR ) + name + EQUALS_OPERATOR + paramValue; //$NON-NLS-1$
				isFirst = false;
			}
		}
		return queryString;
	}

	/**
	 * Gets the query parameter string with the give name and value.
	 * 
	 * @param paramName
	 * @param value
	 * @return
	 */

	public static String getQueryParameterString( String paramName, String value )
	{
		StringBuffer b = new StringBuffer( );
		b.append( PARAMETER_SEPARATOR );
		b.append( paramName );
		b.append( EQUALS_OPERATOR );
		b.append( value );
		return b.toString( );
	}

	/**
	 * Get report title.
	 * 
	 * @param request
	 *            http request
	 * @return report title
	 */

	public static String getTitle( HttpServletRequest request )
	{
		String title = getParameter( request, PARAM_TITLE );
		if ( title == null )
		{
			title = BirtResources
					.getMessage( ResourceConstants.BIRT_VIEWER_TITLE );
		}

		return htmlEncode( title );
	}

	/**
	 * Get report format.
	 * 
	 * @param request
	 *            http request
	 * @return report format
	 */

	public static String getFormat( HttpServletRequest request )
	{
		String format = getParameter( request, PARAM_FORMAT );
		if ( format != null && format.length( ) > 0 )
		{
			if ( PARAM_FORMAT_HTM.equalsIgnoreCase( format ) )
				return PARAM_FORMAT_HTML;

			return format;
		}

		return PARAM_FORMAT_HTML; // The default format is html.
	}

	/**
	 * Get preview max rows.
	 * 
	 * @param request
	 *            http request
	 * @return max rows
	 */

	public static int getMaxRows( HttpServletRequest request )
	{
		int curMaxRows = ParameterAccessor.getParameterAsInt( request,
				PARAM_MAXROWS );
		if ( curMaxRows <= 0 )
			curMaxRows = maxRows;

		return curMaxRows;
	}

	/**
	 * Get report element's iid.
	 * 
	 * @param request
	 * @return report element's iid
	 */

	public static String getIId( HttpServletRequest request )
	{
		return getReportParameter( request, PARAM_IID, null );
	}

	/**
	 * Get report locale from Http request.
	 * 
	 * @param request
	 *            http request
	 * @return report locale
	 */

	public static Locale getLocale( HttpServletRequest request )
	{
		Locale locale = null;

		// Get Locale from URL parameter
		locale = getLocaleFromString( getParameter( request, PARAM_LOCALE ) );

		// Get Locale from client browser
		if ( locale == null )
			locale = request.getLocale( );

		// Get Locale from Web Context
		if ( locale == null )
			locale = webAppLocale;

		return locale;
	}

	/**
	 * Check whether the viewer is set rtl option.
	 * 
	 * @param request
	 * @return
	 */

	public static boolean isRtl( HttpServletRequest request )
	{
		boolean isRtl = false;

		if ( "true".equalsIgnoreCase( getParameter( request, PARAM_RTL ) ) ) //$NON-NLS-1$
		{
			isRtl = true;
		}

		return isRtl;
	}

	/**
	 * Get report locale from a given string.
	 * 
	 * @param locale
	 *            locale string
	 * @return report locale
	 */

	public static Locale getLocaleFromString( String locale )
	{
		if ( locale == null || locale.length( ) <= 0 )
		{
			return null;
		}

		int index = locale.indexOf( '_' );

		if ( index != -1 )
		{
			String language = locale.substring( 0, index );
			String country = locale.substring( index + 1 );
			return new Locale( language, country );
		}

		return new Locale( locale );
	}

	/**
	 * Get report locale in string.
	 * 
	 * @param request
	 *            http request
	 * @return report String
	 */

	public static String getLocaleString( HttpServletRequest request )
	{
		return getParameter( request, PARAM_LOCALE );
	}

	/**
	 * Get report page from Http request. If frameset pattern, default page is
	 * 1.
	 * 
	 * @param request
	 *            http request
	 * @return report page number
	 */

	public static int getPage( HttpServletRequest request )
	{
		int page = getParameterAsInt( request, PARAM_PAGE );
		if ( page > 0 )
			return page;

		String servletPath = request.getServletPath( );
		if ( IBirtConstants.SERVLET_PATH_FRAMESET
				.equalsIgnoreCase( servletPath )
				&& PARAM_FORMAT_HTML.equalsIgnoreCase( getFormat( request ) ) )
		{
			page = 1;
		}
		else
		{
			page = 0;
		}

		return page;
	}

	/**
	 * Get report page range from Http request.
	 * 
	 * @param request
	 *            http request
	 * @return report page range
	 */

	public static String getPageRange( HttpServletRequest request )
	{
		return getParameter( request, PARAM_PAGE_RANGE );
	}

	/**
	 * Get reportlet id from Http request.
	 * 
	 * @param request
	 *            http request
	 * @return reportlet id
	 */

	public static String getReportletId( HttpServletRequest request )
	{

		if ( isIidReportlet( request ) )
			return getParameter( request, PARAM_INSTANCEID );

		if ( isBookmarkReportlet( request ) )
			return getParameter( request, PARAM_BOOKMARK );

		return null;

	}

	/**
	 * Get named parameters from http request. parameter names and values are
	 * all in iso-8859-1 format in request.
	 * 
	 * @param request
	 *            incoming http request
	 * @param parameterName
	 *            parameter name in UTF-8 format
	 * @return named parameters from http request
	 */

	public static Set getParameterValues( HttpServletRequest request,
			String parameterName )
	{
		return getParameterValues( request, parameterName, true );
	}

	/**
	 * Get report file name. If passed file path is null, get report file from
	 * request.
	 * 
	 * @param request
	 * @param filePath
	 * @return report file
	 */
	public static String getReport( HttpServletRequest request, String filePath )
	{
		if ( filePath == null )
			filePath = DataUtil
					.trimString( getParameter( request, PARAM_REPORT ) );

		// if file path is an absolute file, return it directly
		if ( !isRelativePath( filePath ) )
			return filePath;

		// relative to working folder
		if ( isRelativePath( workingFolder ) )
		{
			filePath = getRealPath( workingFolder + "/" + filePath, request //$NON-NLS-1$
					.getSession( ).getServletContext( ) );
		}
		else
		{
			filePath = workingFolder + File.separator + filePath;
		}

		return filePath;
	}

	/**
	 * Get report document name. If passed file path is null, get document file
	 * from request. If isCreated is true, try to create the document file when
	 * file path is null.
	 * 
	 * @param request
	 * @param filePath
	 * @param isCreated
	 * @return
	 */
	public static String getReportDocument( HttpServletRequest request,
			String filePath, boolean isCreated )
	{
		if ( filePath == null )
			filePath = DataUtil.trimString( getParameter( request,
					PARAM_REPORT_DOCUMENT ) );

		// don't need create the document file from report
		if ( filePath.length( ) <= 0 && !isCreated )
			return null;

		if ( filePath.length( ) <= 0 )
		{
			filePath = generateDocumentFromReport( request );
			filePath = createDocumentPath( filePath, request );
		}
		else
		{
			// if file path is an absolute file, return it directly
			if ( !isRelativePath( filePath ) )
				return filePath;

			// relative to working folder
			if ( isRelativePath( workingFolder ) )
			{
				filePath = getRealPath( workingFolder + "/" + filePath, request //$NON-NLS-1$
						.getSession( ).getServletContext( ) );
			}
			else
			{
				filePath = workingFolder + File.separator + filePath;
			}
		}

		return filePath;

	}

	/**
	 * Create the file path of the the document. The document will be put under
	 * the document folder based on different session id.
	 * 
	 * @param filePath
	 *            the document path cretaed from the report design file.
	 * @param request
	 *            Http request, used to get the session Id.
	 * @return
	 * @throws AxisFault
	 */
	protected static String createDocumentPath( String filePath,
			HttpServletRequest request )
	{

		String documentName = null;

		if ( ( filePath == null ) || ( filePath.length( ) == 0 ) )
			return ""; //$NON-NLS-1$

		String sessionId = request.getSession( ).getId( );
		String fileSeparator = "\\"; //$NON-NLS-1$

		if ( filePath.lastIndexOf( fileSeparator ) == -1 )
			fileSeparator = "/"; //$NON-NLS-1$

		// parse document file name
		if ( filePath.lastIndexOf( fileSeparator ) != -1 )
		{

			documentName = filePath.substring( filePath
					.lastIndexOf( fileSeparator ) + 1 );
		}
		else
		{
			documentName = filePath;
		}

		String hashCode = Integer.toHexString( filePath.hashCode( ) );
		return documentFolder + File.separator + sessionId + File.separator
				+ hashCode + File.separator + documentName;
	}

	/**
	 * Clears the report document/image files which had been created last time
	 * the server starts up.
	 */
	protected static void clearTempFiles( )
	{
		// clear the document files
		File file = new File( documentFolder );
		deleteDir( file );
		makeDir( documentFolder );

		// clear image files
		file = new File( imageFolder );
		deleteDir( file );
		makeDir( imageFolder );
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 */

	protected static boolean deleteDir( File dir )
	{
		if ( dir.isDirectory( ) )
		{
			String[] children = dir.list( );
			for ( int i = 0; i < children.length; i++ )
			{
				boolean success = deleteDir( new File( dir, children[i] ) );
				if ( !success )
				{
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete( );
	}

	/**
	 * Get report parameter by given name.
	 * 
	 * @param request
	 *            http request
	 * @param name
	 *            parameter name
	 * @param defaultValue
	 *            default parameter value
	 * @return parameter value
	 */

	public static String getReportParameter( HttpServletRequest request,
			String name, String defaultValue )
	{
		assert request != null && name != null;

		String value = getParameter( request, name );

		if ( value == null || value.length( ) <= 0 ) // Treat
		// it as blank value.
		{
			value = ""; //$NON-NLS-1$
		}

		Map paramMap = request.getParameterMap( );
		String ISOName = toISOString( name );

		if ( paramMap == null || !paramMap.containsKey( ISOName ) )
		{
			value = defaultValue;
		}

		Set nullParams = getParameterValues( request, PARAM_ISNULL );

		if ( nullParams != null && nullParams.contains( name ) )
		{
			value = null;
		}

		return value;
	}

	/**
	 * Get result set name.
	 * 
	 * @param request
	 * @return
	 */

	public static String getResultSetName( HttpServletRequest request )
	{
		return getReportParameter( request, PARAM_RESULTSETNAME, null );
	}

	/**
	 * Get solected column name list.
	 * 
	 * @param request
	 * @return
	 */

	public static Collection getSelectedColumns( HttpServletRequest request )
	{
		ArrayList columns = new ArrayList( );

		int columnCount = getParameterAsInt( request,
				PARAM_SELECTEDCOLUMNNUMBER );
		for ( int i = 0; i < columnCount; i++ )
		{
			String paramName = PARAM_SELECTEDCOLUMN + String.valueOf( i );
			String columnName = getParameter( request, paramName );
			columns.add( columnName );
		}

		return columns;
	}

	/**
	 * Check whether enable svg support or not.
	 * 
	 * @param request
	 *            http request
	 * @return whether or not render content toolbar
	 */

	public static boolean getSVGFlag( HttpServletRequest request )
	{
		boolean svg = false;

		if ( "true".equalsIgnoreCase( getParameter( request, PARAM_SVG ) ) ) //$NON-NLS-1$
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

	public static Locale getWebAppLocale( )
	{
		return webAppLocale;
	}

	/**
	 * This function is used to encode an ordinary string that may contain
	 * characters or more than one consecutive spaces for appropriate HTML
	 * display.
	 * 
	 * @param s
	 * @return String
	 */
	public static final String htmlEncode( String s )
	{
		String sHtmlEncoded = ""; //$NON-NLS-1$

		if ( s == null )
		{
			return null;
		}

		StringBuffer sbHtmlEncoded = new StringBuffer( );
		final char chrarry[] = s.toCharArray( );

		for ( int i = 0; i < chrarry.length; i++ )
		{
			char c = chrarry[i];

			switch ( c )
			{
				case '\t' :
					sbHtmlEncoded.append( "&#09;" ); //$NON-NLS-1$
					break;
				case '\n' :
					sbHtmlEncoded.append( "<br>" ); //$NON-NLS-1$
					break;
				case '\r' :
					sbHtmlEncoded.append( "&#13;" ); //$NON-NLS-1$
					break;
				case ' ' :
					sbHtmlEncoded.append( "&#32;" ); //$NON-NLS-1$
					break;
				case '"' :
					sbHtmlEncoded.append( "&#34;" ); //$NON-NLS-1$
					break;
				case '\'' :
					sbHtmlEncoded.append( "&#39;" ); //$NON-NLS-1$
					break;
				case '<' :
					sbHtmlEncoded.append( "&#60;" ); //$NON-NLS-1$
					break;
				case '>' :
					sbHtmlEncoded.append( "&#62;" ); //$NON-NLS-1$
					break;
				case '`' :
					sbHtmlEncoded.append( "&#96;" ); //$NON-NLS-1$
					break;
				case '&' :
					sbHtmlEncoded.append( "&#38;" ); //$NON-NLS-1$
					break;
				default :
					sbHtmlEncoded.append( c );
			}
		}

		sHtmlEncoded = sbHtmlEncoded.toString( );
		return sHtmlEncoded;
	}

	/**
	 * This function is used to decode a htmlEncoded string and convert to the
	 * orginial string
	 * 
	 * @param s
	 * @return String
	 */
	public static final String htmlDecode( String s )
	{
		if ( s == null )
			return null;

		String sHtmlDecoded = s.replaceAll( "&#09;", "\t" ); //$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "<br>", "\n" ); //$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#13;", "\r" ); //$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#32;", " " );//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#34;", "\"" );//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#39;", "'" );//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#60;", "<" );//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#62;", ">" );//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#96;", "`" );//$NON-NLS-1$ //$NON-NLS-2$
		sHtmlDecoded = sHtmlDecoded.replaceAll( "&#38;", "&" );//$NON-NLS-1$ //$NON-NLS-2$

		return sHtmlDecoded;
	}

	/**
	 * Initial the parameters class. Web.xml is in UTF-8 format. No need to do
	 * encoding convertion.
	 * 
	 * @param config
	 *            Servlet configuration
	 */

	public synchronized static void initParameters( ServletConfig config )
	{
		if ( !isInitContext )
		{
			ParameterAccessor.initParameters( config.getServletContext( ) );
		}
	}

	/**
	 * Initial the parameters class. Web.xml is in UTF-8 format. No need to do
	 * encoding convertion.
	 * 
	 * @param context
	 *            Servlet Context
	 */

	public synchronized static void initParameters( ServletContext context )
	{
		if ( isInitContext )
			return;

		// Working folder setting
		workingFolder = processWorkingFolder( context, context
				.getInitParameter( INIT_PARAM_WORKING_DIR ) );

		// Document folder setting
		documentFolder = processRealPath( context, context
				.getInitParameter( INIT_PARAM_DOCUMENT_FOLDER ),
				IBirtConstants.DEFAULT_DOCUMENT_FOLDER, true );

		// Image folder setting
		imageFolder = processRealPath( context, context
				.getInitParameter( ParameterAccessor.INIT_PARAM_IMAGE_DIR ),
				IBirtConstants.DEFAULT_IMAGE_FOLDER, true );

		// Log folder setting
		logFolder = processRealPath( context, context
				.getInitParameter( ParameterAccessor.INIT_PARAM_LOG_DIR ),
				IBirtConstants.DEFAULT_LOGS_FOLDER, true );

		// Log level setting
		logLevel = context
				.getInitParameter( ParameterAccessor.INIT_PARAM_LOG_LEVEL );

		// Script lib folder setting
		scriptLibDir = processRealPath(
				context,
				context
						.getInitParameter( ParameterAccessor.INIT_PARAM_SCRIPTLIB_DIR ),
				IBirtConstants.DEFAULT_SCRIPTLIB_FOLDER, false );

		// WebApp Locale setting
		webAppLocale = getLocaleFromString( context
				.getInitParameter( INIT_PARAM_LOCALE ) );
		if ( webAppLocale == null )
			webAppLocale = Locale.getDefault( );

		isWorkingFolderAccessOnly = Boolean
				.valueOf(
						context
								.getInitParameter( INIT_PARAM_WORKING_FOLDER_ACCESS_ONLY ) )
				.booleanValue( );

		// Get preview report max rows parameter from Servlet Context
		String s_maxRows = context.getInitParameter( INIT_PARAM_VIEWER_MAXROWS );
		try
		{
			maxRows = Integer.valueOf( s_maxRows ).intValue( );
		}
		catch ( NumberFormatException e )
		{
			maxRows = -1;
		}

		// default resource path
		birtResourceFolder = processRealPath( context, context
				.getInitParameter( INIT_PARAM_BIRT_RESOURCE_PATH ), null, false );

		// get the overwrite flag
		String s_overwrite = DataUtil.trimString( context
				.getInitParameter( INIT_PARAM_OVERWRITE_DOCUMENT ) );
		if ( "true".equalsIgnoreCase( s_overwrite ) ) //$NON-NLS-1$
		{
			isOverWrite = true;
		}
		else
		{
			isOverWrite = false;
		}

		// initialize the application properties
		initProps = initViewerProps( context, initProps );

		// print on the server side
		String flag = DataUtil.trimString( context
				.getInitParameter( INIT_PARAM_PRINT_SERVERSIDE ) );
		if ( IBirtConstants.VAR_ON.equalsIgnoreCase( flag ) )
		{
			isSupportedPrintOnServer = true;
		}
		else if ( IBirtConstants.VAR_OFF.equalsIgnoreCase( flag ) )
		{
			isSupportedPrintOnServer = false;
		}

		// clear temp files
		clearTempFiles( );

		// Finish init context
		isInitContext = true;
	}

	/**
	 * Check whether the viewer is used in designer or not.
	 * 
	 * @param request
	 * @return
	 */

	public static boolean isDesigner( HttpServletRequest request )
	{
		boolean isDesigner = false;

		if ( "true".equalsIgnoreCase( getParameter( request, PARAM_DESIGNER ) ) ) //$NON-NLS-1$
		{
			isDesigner = true;
		}

		return isDesigner;
	}

	/***************************************************************************
	 * For export data
	 **************************************************************************/

	/**
	 * Check whether the request is to get image.
	 * 
	 * @param request
	 *            http request
	 * @return is get image or not
	 */

	public static boolean isGetImageOperator( HttpServletRequest request )
	{
		String imageName = getParameter( request, PARAM_IMAGEID );
		return imageName != null && imageName.length( ) > 0;
	}

	/**
	 * Check whether the request is to get reportlet.
	 * 
	 * @param request
	 *            http request
	 * @return is get reportlet or not
	 */

	public static boolean isGetReportlet( HttpServletRequest request )
	{

		return isBookmarkReportlet( request ) || isIidReportlet( request );
	}

	/**
	 * if the PARAM_ISREPORTLET is trure and the PARAM_BOOKMARK is not null,
	 * this method will return true. Otherwise, return false.
	 * 
	 * @param request
	 * @return true for render the reportlet based on bookmark, else, false.
	 */
	public static boolean isBookmarkReportlet( HttpServletRequest request )
	{
		if ( "true" //$NON-NLS-1$
		.equalsIgnoreCase( getParameter( request, PARAM_ISREPORTLET ) ) )
		{
			String bookmark = getParameter( request, PARAM_BOOKMARK );
			return bookmark != null && bookmark.length( ) > 0;
		}
		return false;
	}

	/**
	 * if the PARAM_INSTANCEID parameter in the url is not null, then return
	 * true to render the reportlet.
	 * 
	 * @param request
	 * @return true for render the reprtlet based on the instance id.
	 */
	public static boolean isIidReportlet( HttpServletRequest request )
	{
		String instanceId = getParameter( request, PARAM_INSTANCEID );
		return instanceId != null && instanceId.length( ) > 0;
	}

	/**
	 * Check whether the viewer allows master page content or not.
	 * 
	 * @param request
	 * @return
	 */

	public static boolean isMasterPageContent( HttpServletRequest request )
	{
		boolean isMasterPageContent = true;

		if ( "false".equalsIgnoreCase( getParameter( request, PARAM_MASTERPAGE ) ) ) //$NON-NLS-1$
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

	public static boolean isOverwrite( HttpServletRequest request )
	{
		boolean overwrite = isOverWrite;

		String urlParam = getParameter( request, PARAM_OVERWRITE );
		if ( "true".equalsIgnoreCase( urlParam ) ) //$NON-NLS-1$
		{
			overwrite = true;
		}
		else if ( "false".equalsIgnoreCase( urlParam ) ) //$NON-NLS-1$
		{
			overwrite = false;
		}

		return overwrite;
	}

	/**
	 * Checks if a given file name is a relative path.
	 * 
	 * @param fileName
	 *            The file name.
	 * @return A <code>boolean</code> value indicating if the file name is a
	 *         relative path or not.
	 */

	public static boolean isRelativePath( String fileName )
	{
		if ( fileName == null )
		{
			return false;
		}

		return !new File( fileName ).isAbsolute( );
	}

	/**
	 * Check whether report parameter exists in the url.
	 * 
	 * @param request
	 *            http request
	 * @param name
	 *            parameter name
	 * @return whether report parameter exists in the url
	 */

	public static boolean isReportParameterExist( HttpServletRequest request,
			String name )
	{
		assert request != null && name != null;

		boolean isExist = false;

		Map paramMap = request.getParameterMap( );
		String ISOName = toISOString( name );

		if ( paramMap != null && paramMap.containsKey( ISOName ) )
		{
			isExist = true;
		}

		Set nullParams = getParameterValues( request, PARAM_ISNULL );

		if ( nullParams != null && nullParams.contains( name ) )
		{
			isExist = true;
		}

		return isExist;
	}

	/**
	 * If set isWorkingFolderAccessOnly as true, check the file if exist in
	 * working folder.
	 * 
	 * @param filePath
	 * @return boolean
	 */

	public static boolean isValidFilePath( String filePath )
	{
		if ( filePath == null )
			return false;

		if ( isWorkingFolderAccessOnly )
		{
			File docFile = new File( filePath );
			if ( !docFile.isAbsolute( ) )
			{
				if ( filePath.indexOf( ".." ) != -1 ) //$NON-NLS-1$
					return false;

				return true;
			}

			File docFolder = new File( workingFolder );
			if ( docFolder.isAbsolute( ) )
			{
				String absolutePath = docFile.getAbsolutePath( );
				String docFolderPath = docFolder.getAbsolutePath( );

				// if OS is windows, ignore the case sensitive.
				if ( isWindowsPlatform( ) )
				{
					absolutePath = absolutePath.toLowerCase( );
					docFolderPath = docFolderPath.toLowerCase( );
				}

				return absolutePath.startsWith( docFolderPath );
			}
			else
			{
				// if workingFolder is relative path, return false.
				return false;
			}
		}

		return true;
	}

	/**
	 * Generate document name according to report name.
	 * 
	 * @param request
	 * @return document name.
	 */

	protected static String generateDocumentFromReport(
			HttpServletRequest request )
	{
		String fileName = getReport( request, null );
		if ( fileName.indexOf( '.' ) >= 0 )
		{
			fileName = fileName.substring( 0, fileName.lastIndexOf( '.' ) );
		}

		// Get viewer id
		String id = getParameter( request, PARAM_ID );
		if ( id != null && id.length( ) > 0 )
		{
			fileName = fileName + id + SUFFIX_REPORT_DOCUMENT;
		}
		else
		{
			fileName = fileName + SUFFIX_REPORT_DOCUMENT;
		}

		return fileName;
	}

	/**
	 * Get named parameter from http request. parameter names and values are all
	 * in iso-8859-1 format in request.
	 * 
	 * @param request
	 *            incoming http request
	 * @param parameterName
	 *            parameter name in UTF-8 format
	 * @return
	 */

	public static String getParameter( HttpServletRequest request,
			String parameterName )
	{
		return getParameter( request, parameterName, true );
	}

	/**
	 * Get named parameter from http request. parameter names and values are all
	 * in iso-8859-1 format in request.
	 * 
	 * @param request
	 *            incoming http request
	 * @param parameterName
	 *            parameter name
	 * @param isUTF
	 *            is parameter in UTF-8 formator not
	 * @return
	 */

	protected static String getParameter( HttpServletRequest request,
			String parameterName, boolean isUTF )
	{
		String ISOParameterName = ( isUTF )
				? toISOString( parameterName )
				: parameterName;
		String value = request.getParameter( ISOParameterName );
		String encoding = request.getCharacterEncoding( );
		return toUTFString( value, encoding );
	}

	/**
	 * Get named parameter as integer from http request. parameter names and
	 * values are all in iso-8859-1 format in request.
	 * 
	 * @param request
	 * @param parameterName
	 * @return
	 */

	protected static int getParameterAsInt( HttpServletRequest request,
			String parameterName )
	{
		int iValue = -1;
		String value = getParameter( request, parameterName );

		if ( value != null && value.length( ) > 0 )
		{
			try
			{
				iValue = Integer.parseInt( value );
			}
			catch ( NumberFormatException ex )
			{
				iValue = -1;
			}
		}
		return iValue;
	}

	/**
	 * Get named parameters from http request. parameter names and values are
	 * all in iso-8859-1 format in request.
	 * 
	 * @param request
	 *            incoming http request
	 * @param parameterName
	 *            parameter name
	 * @param isUTF
	 *            is parameter in UTF-8 formator not
	 * @return
	 */

	protected static Set getParameterValues( HttpServletRequest request,
			String parameterName, boolean isUTF )
	{
		HashSet parameterValues = null;
		String ISOParameterName = ( isUTF )
				? toISOString( parameterName )
				: parameterName;
		String[] ISOParameterValues = request
				.getParameterValues( ISOParameterName );

		if ( ISOParameterValues != null )
		{
			parameterValues = new HashSet( );

			for ( int i = 0; i < ISOParameterValues.length; i++ )
			{
				parameterValues.add( toUTFString( ISOParameterValues[i] ) );
			}
		}

		return parameterValues;
	}

	/**
	 * Convert UTF-8 string into ISO-8895-1
	 * 
	 * @param s
	 *            UTF-8 string
	 * @return
	 */

	protected static String toISOString( String s )
	{
		String ISOString = s;

		if ( s != null )
		{
			try
			{
				ISOString = new String( s.getBytes( UTF_8_ENCODE ),
						ISO_8859_1_ENCODE );
			}
			catch ( UnsupportedEncodingException e )
			{
				ISOString = s;
			}
		}

		return ISOString;
	}

	/**
	 * Convert ISO-8895-1 string into UTF-8.
	 * 
	 * @param s
	 *            ISO-8895-1 string
	 * @return
	 */

	protected static String toUTFString( String s )
	{
		String UTFString = s;

		if ( s != null )
		{
			try
			{
				UTFString = new String( s.getBytes( ISO_8859_1_ENCODE ),
						UTF_8_ENCODE );
			}
			catch ( UnsupportedEncodingException e )
			{
				UTFString = s;
			}
		}

		return UTFString;
	}

	/**
	 * Convert the string with the given encoding into UTF-8.
	 * 
	 * @param s
	 *            ISO-8895-1 string
	 * @param encoding
	 *            the current encoding of the string
	 * @return the converted UTF-8 string
	 */

	protected static String toUTFString( String s, String encoding )
	{
		String UTFString = s;
		String sourceEncoding = encoding;

		if ( s != null )
		{
			if ( sourceEncoding == null )
			{
				sourceEncoding = ISO_8859_1_ENCODE;
			}
			try
			{
				UTFString = new String( s.getBytes( sourceEncoding ),
						UTF_8_ENCODE );
			}
			catch ( UnsupportedEncodingException e )
			{
				UTFString = s;
			}
		}

		return UTFString;
	}

	/**
	 * URL encoding based on incoming encoding format.
	 * 
	 * @param s
	 *            string to be encoded.
	 * @param format
	 *            encoding format.
	 * @return
	 */

	public static String urlEncode( String s, String format )
	{
		String encodedString = s;

		if ( s != null )
		{
			try
			{
				encodedString = URLEncoder.encode( s, format );
			}
			catch ( UnsupportedEncodingException e )
			{
				encodedString = s;
			}
		}

		return encodedString;
	}

	/**
	 * Parse config file name from report design filename.
	 * 
	 * @param reportDesignName
	 *            String
	 * @return String
	 */

	public static String getConfigFileName( String reportDesignName )
	{
		if ( reportDesignName == null )
			return null;

		String[] result = reportDesignName.split( "\\." ); //$NON-NLS-1$
		String extensionName = result[result.length - 1];
		String configFileName = reportDesignName.substring( 0, reportDesignName
				.length( )
				- extensionName.length( ) )
				+ IBirtConstants.SUFFIX_DESIGN_CONFIG;

		return configFileName;
	}

	/**
	 * Get current format of parameter.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param paramName
	 *            String
	 * 
	 * @return String
	 */

	public static String getFormat( HttpServletRequest request, String paramName )
	{
		if ( request == null || paramName == null )
			return null;

		return getParameter( request, paramName + "_format" ); //$NON-NLS-1$
	}

	/**
	 * @return the isWorkingFolderAccessOnly
	 */
	public static boolean isWorkingFolderAccessOnly( )
	{
		return isWorkingFolderAccessOnly;
	}

	/**
	 * if display text of select parameter
	 * 
	 * @param paramName
	 * @return
	 */
	public static String isDisplayText( String paramName )
	{
		if ( paramName == null )
			return null;

		if ( paramName.startsWith( PREFIX_DISPLAY_TEXT ) )
		{
			return paramName.replaceFirst( PREFIX_DISPLAY_TEXT, "" ); //$NON-NLS-1$
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

	public static String generateFileName( HttpServletRequest request,
			String format )
	{
		String defaultName = "BIRTReport"; //$NON-NLS-1$
		String fileName = defaultName;
		BaseAttributeBean attrBean = (BaseAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		if ( attrBean == null )
			return fileName;

		String baseName = attrBean.getReportDesignName( );
		if ( baseName == null || baseName.length( ) == 0 )
			baseName = attrBean.getReportDocumentName( );

		if ( baseName == null || baseName.trim( ).length( ) <= 0 )
			return fileName;

		int index = baseName.lastIndexOf( '/' );
		if ( index == -1 )
			index = baseName.lastIndexOf( '\\' );

		// if base name contains parent package name, substring the
		// design file name; otherwise let it be
		if ( index != -1 )
		{
			baseName = baseName.substring( index + 1 );
		}

		// get the report design name, then extract the name without
		// file extension and set it to fileName; otherwise do noting and
		// let fileName with the default name
		int dotIndex = baseName.lastIndexOf( '.' );
		if ( dotIndex > 0 )
		{
			fileName = baseName.substring( 0, dotIndex );
		}

		// check whether the file name contains non US-ASCII characters

		for ( int i = 0; i < fileName.length( ); i++ )
		{
			char c = fileName.charAt( i );

			// char is from 0-127

			if ( c < 0x00 || c >= 0x80 )
			{
				fileName = defaultName;
				break;
			}
		}

		// append extension name
		String extensionName = getExtensionName( format );
		if ( extensionName != null && extensionName.length( ) > 0 )
		{
			fileName += "." + extensionName; //$NON-NLS-1$
		}

		return fileName;
	}

	/**
	 * Reset isInitContext flag
	 */
	public static void reset( )
	{
		isInitContext = false;
	}

	/**
	 * 
	 * Check if OS system is windows
	 * 
	 * @return boolean
	 */
	protected static boolean isWindowsPlatform( )
	{
		return System.getProperty( "os.name" ).toLowerCase( ).indexOf( //$NON-NLS-1$
				"windows" ) >= 0; //$NON-NLS-1$
	}

	/**
	 * Get the resource folder.
	 * 
	 * @param request
	 *            the request to retrieve
	 * @return the resource folder of the request
	 */

	public static String getResourceFolder( HttpServletRequest request )
	{
		String resourceFolder = null;

		// get resource folder from request first
		resourceFolder = getParameter( request, PARAM_RESOURCE_FOLDER );

		// if the resource folder in the request is null or empty, read it from
		// web init params
		if ( resourceFolder == null || resourceFolder.trim( ).length( ) <= 0 )
			resourceFolder = birtResourceFolder;

		return resourceFolder;
	}

	/**
	 * Push User-defined application context object into engine context map.
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	public static Map pushAppContext( Map map, HttpServletRequest request )
	{
		if ( map == null )
			map = new HashMap( );

		// Get application context key from request
		String appContextKey = (String) request
				.getAttribute( ATTR_APPCONTEXT_KEY );

		// Put application context object
		if ( appContextKey != null )
			map.put( appContextKey, request
					.getAttribute( ATTR_APPCONTEXT_VALUE ) );

		return map;
	}

	/**
	 * Returns the encoding for export data.
	 * 
	 * @param request
	 * @return
	 */

	public static String getExportEncoding( HttpServletRequest request )
	{
		String encoding = getParameter( request, PARAM_EXPORT_ENCODING );

		// use UTF-8 as the default encoding
		if ( encoding == null )
			encoding = UTF_8_ENCODE;

		return encoding;
	}

	/**
	 * Check whether show the report title.
	 * 
	 * @param request
	 * @return
	 */

	public static boolean isShowTitle( HttpServletRequest request )
	{
		boolean isTitle = true;

		if ( "false".equalsIgnoreCase( getParameter( request, PARAM_SHOW_TITLE ) ) ) //$NON-NLS-1$
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

	public static boolean isShowToolbar( HttpServletRequest request )
	{
		boolean isToolbar = true;

		if ( "false".equalsIgnoreCase( getParameter( request, PARAM_TOOLBAR ) ) ) //$NON-NLS-1$
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

	public static boolean isShowNavigationbar( HttpServletRequest request )
	{
		boolean isNavigationbar = true;

		if ( "false".equalsIgnoreCase( getParameter( request, PARAM_NAVIGATIONBAR ) ) ) //$NON-NLS-1$
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

	public static String getShowParameterPage( HttpServletRequest request )
	{
		return getParameter( request, PARAM_PARAMETER_PAGE );
	}

	/**
	 * Returns the application properties
	 * 
	 * @param context
	 * @param props
	 * @return
	 */
	public synchronized static Map initViewerProps( ServletContext context,
			Map props )
	{
		// initialize map
		if ( props == null )
			props = new HashMap( );

		// get config file
		String file = context.getInitParameter( INIT_PARAM_CONFIG_FILE );
		if ( file == null || file.trim( ).length( ) <= 0 )
			file = IBirtConstants.DEFAULT_VIEWER_CONFIG_FILE;

		try
		{

			InputStream is = null;
			if ( isRelativePath( file ) )
			{
				// realtive path
				if ( !file.startsWith( "/" ) ) //$NON-NLS-1$
					file = "/" + file; //$NON-NLS-1$

				is = context.getResourceAsStream( file );
			}
			else
			{
				// absolute path
				is = new FileInputStream( file );
			}

			// parse the properties file
			PropertyResourceBundle bundle = new PropertyResourceBundle( is );
			if ( bundle != null )
			{
				Enumeration keys = bundle.getKeys( );
				while ( keys != null && keys.hasMoreElements( ) )
				{
					String key = (String) keys.nextElement( );
					String value = (String) bundle.getObject( key );
					if ( key != null && value != null )
						props.put( key, value );
				}
			}
		}
		catch ( Exception e )
		{
		}

		return props;
	}

	/**
	 * Returns the property by name from initialized properties map
	 * 
	 * @param key
	 * @return
	 */
	public static String getInitProp( String key )
	{
		if ( initProps == null || key == null )
			return null;

		return (String) initProps.get( key );
	}

	/**
	 * Returns the extension name according to format
	 * 
	 * @param format
	 * @return
	 */
	public static String getExtensionName( String format )
	{
		if ( format == null )
			return null;

		String key = "viewer.extension." + format; //$NON-NLS-1$
		return DataUtil.trimString( getInitProp( key ) );
	}

	/**
	 * Returns the base url defined in config file
	 * 
	 * @return
	 */
	public static String getBaseURL( )
	{
		String baseURL = getInitProp( PROP_BASE_URL );
		if ( baseURL != null && baseURL.length( ) > 0 )
		{
			if ( baseURL.endsWith( "/" ) ) //$NON-NLS-1$
				baseURL = baseURL.substring( 0, baseURL.length( ) - 1 );
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
	protected static String convertSystemPath( String path )
	{
		if ( path == null )
			return path;

		// parse System Properties
		Pattern p = Pattern.compile( "\\$\\s*\\{([^\\}]*)\\}\\s*(.*)", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE );
		Matcher m = p.matcher( path );
		if ( m.find( ) )
		{
			String sysPath = DataUtil.trimSepEnd( System.getProperty( m.group(
					1 ).trim( ) ) );
			return DataUtil.trimString( sysPath ) + m.group( 2 ).trim( );
		}

		return path;
	}

	/**
	 * Process working folder setting. If path is a relative path, first
	 * relative to context.
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static String processWorkingFolder( ServletContext context,
			String path )
	{
		path = convertSystemPath( DataUtil.trimString( path ) );
		String realPath = null;

		// If path is a relative path
		if ( isRelativePath( path ) )
		{
			realPath = getRealPath( path, context );
			makeDir( realPath );
			return DataUtil.trimSepEnd( path );
		}
		else
		{
			// Path is an absolute path
			realPath = path;
		}

		// try to create folder
		makeDir( realPath );

		return DataUtil.trimSepEnd( realPath );
	}

	/**
	 * Process folder settings with absolute path. If path is a relative path,
	 * first relative to context. If set canWrite to true, then check the folder
	 * if writable.If not, relative to ${java.io.tmpdir} folder.
	 * 
	 * @param context
	 * @param path
	 * @param defaultPath
	 * @param canWrite
	 * @return
	 */
	public static String processRealPath( ServletContext context, String path,
			String defaultPath, boolean canWrite )
	{
		String realPath = null;
		boolean isRelative = false;

		path = convertSystemPath( path );

		// Using default path
		if ( path == null || path.trim( ).length( ) <= 0 )
		{
			path = DataUtil.trimString( defaultPath );
		}

		// If path is a relative path
		if ( isRelativePath( path ) )
		{
			isRelative = true;
			if ( !path.startsWith( "/" ) ) //$NON-NLS-1$
				path = "/" + path; //$NON-NLS-1$

			realPath = DataUtil.trimSepEnd( getRealPath( path, context ) );
		}
		else
		{
			// Path is an absolute path
			realPath = DataUtil.trimSepEnd( path );
		}

		boolean flag = makeDir( realPath );

		// don't need writable
		if ( !canWrite )
			return realPath;

		// check if the folder is writable
		if ( flag )
		{
			try
			{
				if ( canWrite && new File( realPath ).canWrite( ) )
					return realPath;
			}
			catch ( Exception e )
			{
			}
		}

		// try to create folder in ${java.io.tmpdir}
		if ( isRelative )
		{
			realPath = DataUtil.trimSepEnd( System
					.getProperty( "java.io.tmpdir" ) ) + path; //$NON-NLS-1$
		}
		else
		{
			// if absolute path, create default path in temp folder
			if ( defaultPath != null )
				realPath = DataUtil.trimSepEnd( System
						.getProperty( "java.io.tmpdir" ) ) + File.separator + defaultPath; //$NON-NLS-1$
		}

		// try to create folder
		makeDir( realPath );

		return realPath;
	}

	/**
	 * Returns real path relative to context
	 * 
	 * @param path
	 * @param context
	 * @return
	 */
	private static String getRealPath( String path, ServletContext context )
	{
		assert path != null;
		String realPath = null;
		try
		{
			if ( !path.startsWith( "/" ) ) //$NON-NLS-1$
				path = "/" + path; //$NON-NLS-1$

			realPath = context.getRealPath( path );
			if ( realPath == null )
			{
				URL url = context.getResource( "/" ); //$NON-NLS-1$
				if ( url != null )
					realPath = DataUtil.trimString( url.getFile( ) ) + path;
			}
		}
		catch ( Exception e )
		{
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
	private static boolean makeDir( String path )
	{
		assert path != null;
		File file = new File( path );
		if ( !file.exists( ) )
			return file.mkdirs( );

		return true;
	}

	/**
	 * Returns the temp image folder with session id
	 * 
	 * @param request
	 * @return
	 */
	public static String getImageTempFolder( HttpServletRequest request )
	{
		String tempFolder = imageFolder;

		// get session id
		String sessionId = request.getSession( ).getId( );
		if ( sessionId != null )
			tempFolder = tempFolder + File.separator + sessionId;

		return tempFolder;
	}

	/**
	 * Clear the temp files when session is expired
	 * 
	 * @param sessionId
	 */
	public static void clearSessionFiles( String sessionId )
	{
		if ( sessionId == null )
			return;

		// clear document folder
		String tempFolder = documentFolder + File.separator + sessionId;
		File file = new File( tempFolder );
		deleteDir( file );

		// clear image folder
		tempFolder = imageFolder + File.separator + sessionId;
		file = new File( tempFolder );
		deleteDir( file );
	}

	/**
	 * Returns if fit to page
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isFitToPage( HttpServletRequest request )
	{
		String fitToPage = getParameter( request, PARAM_FIT_TO_PAGE );
		if ( "true".equalsIgnoreCase( fitToPage ) ) //$NON-NLS-1$
			return true;

		return false;
	}

	/**
	 * Returns if pagebreak pagination only
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isPagebreakOnly( HttpServletRequest request )
	{
		String pagebreakOnly = getParameter( request, PARAM_PAGEBREAK_ONLY );
		if ( "true".equalsIgnoreCase( pagebreakOnly ) ) //$NON-NLS-1$
			return true;

		return false;
	}

	/**
	 * Returns how to open attachment( inline or attachment )
	 * 
	 * @param request
	 * @return
	 */
	public static String getOpenType( HttpServletRequest request )
	{
		if ( "true".equalsIgnoreCase( getParameter( request, //$NON-NLS-1$
				PARAM_AS_ATTACHMENT ) ) )
			return IBirtConstants.OPEN_TYPE_ATTACHMENT;

		return IBirtConstants.OPEN_TYPE_INLINE;
	}

	/**
	 * Returns action name
	 * 
	 * @param request
	 * @return
	 */
	public static String getAction( HttpServletRequest request )
	{
		return getParameter( request, PARAM_ACTION );
	}

	/**
	 * Returns the dpi setting from http request
	 * 
	 * @param request
	 * @return
	 */
	public static Number getDpi( HttpServletRequest request )
	{
		String dpi = getParameter( request, PARAM_DPI );
		if ( dpi == null || dpi.trim( ).length( ) <= 0 )
			return null;

		return Integer.valueOf( dpi );
	}
}