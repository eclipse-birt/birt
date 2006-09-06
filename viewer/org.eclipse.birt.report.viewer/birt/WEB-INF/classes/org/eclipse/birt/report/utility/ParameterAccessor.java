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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;

/**
 * Utilites class for all types of URl related operatnios.
 * <p>
 */

public class ParameterAccessor
{

	// servlet path constants
	/**
	 * Servlet path for parameter model.
	 */
	public static final String SERVLET_PATH_PARAMETER = "/parameter"; //$NON-NLS-1$

	/**
	 * Servlet path for preview model.
	 */
	public static final String SERVLET_PATH_PREVIEW = "/preview"; //$NON-NLS-1$

	/**
	 * Servlet path for frameset model.
	 */
	public static final String SERVLET_PATH_FRAMESET = "/frameset"; //$NON-NLS-1$

	/**
	 * Servlet path for running model.
	 */
	public static final String SERVLET_PATH_RUN = "/run"; //$NON-NLS-1$

	/**
	 * Servlet path for running model.
	 */
	public static final String SERVLET_PATH_DOWNLOAD = "/download"; //$NON-NLS-1$

	// URL parameter constants
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
	 * URL parameter name that gives the report design name.
	 */

	public static final String PARAM_ISNULL = "__isnull"; //$NON-NLS-1$

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
	 * Indentify the display text of select parameter
	 */

	public static final String PREFIX_DISPLAY_TEXT = "__isdisplay__"; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate the resource folder of all the report
	 * resources.
	 */

	public static final String PARAM_RESOURCE_FOLDER = "__resourceFolder"; //$NON-NLS-1$

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
	 * Servlet parameter name that gives the default locale of the BIRT viewer.
	 */

	public static final String INIT_PARAM_LOCALE = "BIRT_VIEWER_LOCALE"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that gives the working folder of the local BIRT
	 * viewer user.
	 */

	public static final String INIT_PARAM_REPORT_DIR = "BIRT_VIEWER_WORKING_FOLDER"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that gives the repository location of the image
	 * files.
	 */

	public static final String INIT_PARAM_IMAGE_DIR = "BIRT_VIEWER_IMAGE_DIR"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that gives the repository location of the logging
	 * files.
	 */

	public static final String INIT_PARAM_LOG_DIR = "BIRT_VIEWER_LOG_DIR"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that gives the logging level.
	 */

	public static final String INIT_PARAM_LOG_LEVEL = "BIRT_VIEWER_LOG_LEVEL"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that gives the repository location of the script
	 * files to run reports.
	 */

	public static final String INIT_PARAM_SCRIPTLIB_DIR = "BIRT_VIEWER_SCRIPTLIB_DIR"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that determines the search strategy of searching
	 * report resources. True if only search the document folder, otherwise
	 * false.
	 */

	public static final String INIT_PARAM_DOCUMENT_FOLDER_ACCESS_ONLY = "DOCUMENT_FOLDER_ACCESS_ONLY"; //$NON-NLS-1$

	/**
	 * The parameter name that gives the repository lication to put the created
	 * documents and report design files.
	 */
	public static final String INIT_PARAM_DOCUMENT_FOLDER = "BIRT_VIEWER_DOCUMENT_FOLDER"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that gives the absolute resource location
	 * directory.
	 */

	public static final String INIT_PARAM_BIRT_RESOURCE_PATH = "BIRT_RESOURCE_PATH"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that gives preview report max rows limited.
	 */

	public static final String INIT_PARAM_VIEWER_MAXROWS = "BIRT_VIEWER_MAX_ROWS"; //$NON-NLS-1$

	/**
	 * Servlet parameter name that if always overwrite generated document file.
	 */

	public static final String INIT_PARAM_OVERWRITE_DOCUMENT = "BIRT_OVERWRITE_DOCUMENT"; //$NON-NLS-1$

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
	 * File package name to store the documents.
	 */

	public static final String DOCUMENTS_DIR = "documents";//$NON-NLS-1$

	/**
	 * Report working folder.
	 */

	public static String workingFolder = null;

	/**
	 * Document folder to put the report files and created documents.
	 */

	public static String documentFolder = null;

	/**
	 * Preview report max rows
	 */

	public static int maxRows;

	/**
	 * Current web application locale.
	 */

	protected static Locale webAppLocale = null;

	/**
	 * Flag indicating that if user can only access the file in working folder.
	 */

	protected static boolean isDocumentFolderAccessOnly = false;

	/**
	 * Resource path set in the web application.
	 */

	protected static String birtResourceFolder = null;

	/**
	 * Flag indicating that if initialize the context.
	 */

	protected static boolean isInitContext = false;

	/**
	 * Overwrite flag
	 */
	public static boolean isOverWrite;

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
	 * Get report page from Http request.
	 * 
	 * @param request
	 *            http request
	 * @return report locale
	 */

	public static int getPage( HttpServletRequest request )
	{
		int page = getParameterAsInt( request, PARAM_PAGE );
		return page <= 0 ? 1 : page; // The default page value is 1.
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
	 * Get report file name.
	 * 
	 * @param request
	 *            http request
	 * @return report file name
	 */

	public static String getReport( HttpServletRequest request )
	{
		String filePath = getParameter( request, PARAM_REPORT );
		filePath = preProcess( filePath );
		filePath = createAbsolutePath( filePath );
		return filePath;
	}

	/**
	 * Get report document name.
	 * 
	 * @param request
	 *            http request
	 * @return report file name
	 * @throws AxisFault
	 */

	public static String getReportDocument( HttpServletRequest request )
	{
		String filePath = getParameter( request, PARAM_REPORT_DOCUMENT );

		filePath = preProcess( filePath );

		if ( "".equals( filePath ) ) //$NON-NLS-1$
		{
			filePath = generateDocumentFromReport( request );
			filePath = createDocumentPath( filePath, request );
		}
		else
		{
			filePath = createAbsolutePath( filePath );
		}

		return filePath;

	}

	/**
	 * Create the file path of the the document. The document will be put under
	 * the working folder birt document directory based on different session id.
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

		String temp = null;
		String projectName = null;
		String documentName = null;

		if ( ( filePath == null ) || ( filePath.length( ) == 0 ) )
			return ""; //$NON-NLS-1$

		String sessionId = request.getSession( ).getId( );
		String fileSeparator = "\\"; //$NON-NLS-1$

		if ( filePath.lastIndexOf( fileSeparator ) == -1 )
			fileSeparator = "/"; //$NON-NLS-1$

		if ( filePath.lastIndexOf( fileSeparator ) != -1 )
		{

			documentName = filePath.substring( filePath
					.lastIndexOf( fileSeparator ) );
			temp = filePath
					.substring( 0, filePath.lastIndexOf( fileSeparator ) );

			if ( temp.lastIndexOf( fileSeparator ) != -1 )
				projectName = temp
						.substring( temp.lastIndexOf( fileSeparator ) );
			else
				projectName = temp;

			projectName = validateProjectName( projectName );
		}
		else
			documentName = filePath;

		String targetFolder = documentFolder + fileSeparator + DOCUMENTS_DIR
				+ fileSeparator + sessionId + projectName;

		String documentPath = targetFolder + documentName;

		return documentPath;

	}

	/**
	 * make sure the project name is a valid folder name. If the name contains
	 * ":", remove it. If the project name does not start with "\" or "/", add
	 * the file separator at the beginning of the name. If the project name is
	 * null, just return it.
	 * 
	 * @param projectName
	 * @return
	 */
	private static String validateProjectName( String projectName )
	{
		if ( projectName == null )
			return projectName;

		if ( projectName.indexOf( ":" ) != -1 )
			projectName = projectName.substring( 0, projectName.indexOf( ":" ) );

		if ( !"\\".equals( projectName.substring( 0, 1 ) )
				&& !"/".equals( projectName.substring( 0, 1 ) ) )
		{
			projectName = File.separator + projectName;
		}
		return projectName;

	}

	/**
	 * Clears the report document files which had been created last time the
	 * server starts up.
	 */
	protected static void clearDocuments( )
	{
		String targetFolder = documentFolder + File.separator + DOCUMENTS_DIR;
		File file = new File( targetFolder );

		boolean success = file.delete( );
		if ( !success )
			deleteDir( file );

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

		if ( value == null || ( (String) value ).trim( ).length( ) <= 0 ) // Treat
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
	 * Get current working folder.
	 * 
	 * @return Returns the workingFolder.
	 */

	public static String getWorkingFolder( )
	{
		return workingFolder;
	}

	/**
	 * Gets the current document folder.
	 * 
	 * @return returns the documentFolder.
	 */
	public static String getDocumentFolder( )
	{
		return documentFolder;
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

		// Report root.in the web.xml has higher priority.
		workingFolder = context.getInitParameter( INIT_PARAM_REPORT_DIR );
		documentFolder = context.getInitParameter( INIT_PARAM_DOCUMENT_FOLDER );

		if ( workingFolder == null || workingFolder.trim( ).length( ) <= 0 )
		{
			// Use birt dir as default report root.
			workingFolder = getRealPath( context, "/" ); //$NON-NLS-1$
		}

		// Report root could be empty. .WAR
		// Clear out report location.
		if ( workingFolder != null
				&& workingFolder.trim( ).endsWith( File.separator ) )
		{
			workingFolder = workingFolder.trim( ).substring( 0,
					workingFolder.trim( ).length( ) - 1 );
		}

		if ( documentFolder == null || documentFolder.trim( ).length( ) <= 0 )
			documentFolder = workingFolder;

		// Get Web App Default Locale
		webAppLocale = getLocaleFromString( context
				.getInitParameter( INIT_PARAM_LOCALE ) );
		if ( webAppLocale == null )
			webAppLocale = Locale.getDefault( );

		isDocumentFolderAccessOnly = Boolean
				.valueOf(
						context
								.getInitParameter( INIT_PARAM_DOCUMENT_FOLDER_ACCESS_ONLY ) )
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

		// get the default resource path
		birtResourceFolder = context
				.getInitParameter( INIT_PARAM_BIRT_RESOURCE_PATH );

		// get the overwrite flag
		String s_overwrite = context
				.getInitParameter( INIT_PARAM_OVERWRITE_DOCUMENT );
		if ( "true".equalsIgnoreCase( s_overwrite ) ) //$NON-NLS-1$
		{
			isOverWrite = true;
		}
		else
		{
			isOverWrite = false;
		}

		clearDocuments( );

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
		boolean inDEsigner = false;

		if ( "true".equalsIgnoreCase( getParameter( request, PARAM_DESIGNER ) ) ) //$NON-NLS-1$
		{
			inDEsigner = true;
		}

		return inDEsigner;
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
		if ( "true"
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

	/***************************************************************************
	 * protected routines
	 **************************************************************************/

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
	 * If a report file name is a relative path, it is relative to document
	 * folder. So if a report file path is relative path, it's absolute path is
	 * synthesized by appending file path to the document folder path.
	 * 
	 * @param file
	 * @return
	 */

	protected static String createAbsolutePath( String filePath )
	{
		if ( isRelativePath( filePath ) )
		{
			return documentFolder + File.separator + filePath;
		}
		return filePath;
	}

	/**
	 * If set isDocumentFolderAccessOnly as true, check the file if exist in
	 * document folder.
	 * 
	 * @param filePath
	 * @return boolean
	 */

	public static boolean isValidFilePath( String filePath )
	{
		assert filePath != null;
		if ( isDocumentFolderAccessOnly )
		{
			String absolutePath = new File( filePath ).getAbsolutePath( );
			String docFolderPath = new File( documentFolder ).getAbsolutePath( );
			// if OS is windows, ignore the case sensitive.
			if ( isWindowsPlatform( ) )
			{
				absolutePath = absolutePath.toLowerCase( );
				docFolderPath = docFolderPath.toLowerCase( );
			}

			return absolutePath.startsWith( docFolderPath );
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
		String fileName = getReport( request );
		if ( fileName.indexOf( '.' ) >= 0 )
		{
			fileName = fileName.substring( 0, fileName.lastIndexOf( '.' ) )
					+ SUFFIX_REPORT_DOCUMENT;
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
	 * Preprocess a string.
	 * 
	 * @param string
	 *            the string to process.
	 * @return a string which is not null and without excessive spaces.
	 */

	protected static String preProcess( String string )
	{
		return string == null ? "" : string.trim( ); //$NON-NLS-1$
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

	protected static String urlEncode( String s, String format )
	{
		assert ISO_8859_1_ENCODE.equalsIgnoreCase( format )
				|| UTF_8_ENCODE.equalsIgnoreCase( format );
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

		String configFileName = null;

		if ( reportDesignName.endsWith( IBirtConstants.SUFFIX_DESIGN_FILE ) )
		{
			configFileName = reportDesignName.replaceFirst(
					IBirtConstants.SUFFIX_DESIGN_FILE,
					IBirtConstants.SUFFIX_DESIGN_CONFIG );
		}
		else if ( reportDesignName
				.endsWith( IBirtConstants.SUFFIX_TEMPLATE_FILE ) )
		{
			configFileName = reportDesignName.replaceFirst(
					IBirtConstants.SUFFIX_TEMPLATE_FILE,
					IBirtConstants.SUFFIX_DESIGN_CONFIG );
		}

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
		return isDocumentFolderAccessOnly;
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
	 * Generates a file name for the pdf output.
	 * 
	 * @param request
	 * @return the file name
	 */

	public static String generateFileName( HttpServletRequest request )
	{
		String defaultName = "BIRTReport"; //$NON-NLS-1$
		String fileName = defaultName;
		BaseAttributeBean attrBean = (BaseAttributeBean) request
				.getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
		if ( attrBean == null )
			return fileName + ".pdf"; //$NON-NLS-1$
		String baseName = attrBean.getReportDesignName( );
		if ( baseName == null || baseName.length( ) == 0 )
			baseName = attrBean.getReportDocumentName( );
		assert baseName != null && baseName.length( ) > 0;

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

		fileName = fileName + ".pdf"; //$NON-NLS-1$ 
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
	 * Get the resource real path
	 * 
	 * @param context
	 *            ServletContext
	 * @param path
	 *            String
	 * 
	 * @return String
	 */
	public static String getRealPath( ServletContext context, String path )
	{
		// Get real path
		String resourcePath = context.getRealPath( path );

		// If null, use getResource method to get path
		if ( resourcePath == null )
		{
			try
			{
				URL url = context.getResource( path );
				if ( url != null )
					resourcePath = url.getFile( );
			}
			catch ( Exception e )
			{
			}
		}

		return resourcePath;
	}
}