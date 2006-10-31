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

package org.eclipse.birt.report.viewer.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.browsers.BrowserAccessor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.browser.Browser;

/**
 * Static accessor to display an arbitary url. It serves as an entry point to
 * integrate viewer.
 * <p>
 */
public class WebViewer
{

	/**
	 * HTML format name
	 */
	final public static String HTML = "html"; //$NON-NLS-1$

	/**
	 * PDF format name
	 */
	final public static String PDF = "pdf"; //$NON-NLS-1$

	/**
	 * DOC format name
	 */
	final public static String DOC = "doc";

	/**
	 * Birt web viewer plugin id
	 */
	final public static String WebAppPlugin = ViewerPlugin.PLUGIN_ID;

	/**
	 * locale preference name
	 */
	final public static String USER_LOCALE = "user_locale"; //$NON-NLS-1$

	/**
	 * Preference key for SVG chart flag.
	 */
	final public static String SVG_FLAG = "svg_flag"; //$NON-NLS-1$

	/**
	 * Preference key for master page content flag.
	 */
	final public static String MASTER_PAGE_CONTENT = "master_page_content"; //$NON-NLS-1$

	/** Preference key for max rows. */
	final public static String PREVIEW_MAXROW = "preview_maxrow"; //$NON-NLS-1$

	// parameter name constants for the URL

	/**
	 * Key to indicate the format of the preview.
	 */

	public final static String FORMAT_KEY = "FORMAT_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'allowPage' control of the preview.
	 */

	public final static String ALLOW_PAGE_KEY = "ALLOW_PAGE_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'servletName' of the preview.
	 */

	public final static String SERVLET_NAME_KEY = "SERVLET_NAME_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'resourceFolder'.
	 */

	public final static String RESOURCE_FOLDER_KEY = "RESOURCE_FOLDER_KEY"; //$NON-NLS-1$

	/**
	 * Key to indicate the 'maxRows'
	 */

	public final static String MAX_ROWS_KEY = "MAX_ROWS_KEY"; //$NON-NLS-1$

	/**
	 * locale mapping. Save some time.
	 */
	public static TreeMap LocaleTable = null;

	static
	{
		// Initialize the locale mapping table
		LocaleTable = new TreeMap( );
		Locale[] locales = Locale.getAvailableLocales( );
		if ( locales != null )
		{
			for ( int i = 0; i < locales.length; i++ )
			{
				Locale locale = locales[i];
				if ( locale != null )
				{
					LocaleTable.put( locale.getDisplayName( ), locale
							.getLanguage( )
							+ "_" + locale.getCountry( ) ); //$NON-NLS-1$
				}
			}
		}
	}

	/**
	 * Get web viewer base url.
	 * 
	 * @return base web viewer application url
	 */
	private static String getBaseURL( )
	{
		return "http://" + WebappAccessor.getHost( ) + ":" //$NON-NLS-1$ //$NON-NLS-2$
				+ WebappAccessor.getPort( ) + "/viewer/"; //$NON-NLS-1$
	}

	/**
	 * Create web viewer url to run the report.
	 * 
	 * @param report
	 *            report file name
	 * @param params
	 *            report parameter map
	 * @return valid web viewer url
	 */

	private static String createURL( String report, Map params )
	{
		if ( params == null || params.isEmpty( ) )
			return createURL( null, report, null, true, null, null );
		String servletName = (String) params.get( SERVLET_NAME_KEY );
		String format = (String) params.get( FORMAT_KEY );
		String resourceFolder = (String) params.get( RESOURCE_FOLDER_KEY );
		Boolean allowPage = (Boolean) params.get( ALLOW_PAGE_KEY );
		if ( PDF.equalsIgnoreCase( format ) )
		{
			servletName = "run"; //$NON-NLS-1$
		}
		else
		{
			if ( servletName == null || servletName.trim( ).length( ) <= 0 )
			{
				if ( allowPage == null )
					servletName = "frameset"; //$NON-NLS-1$
				else
				{
					servletName = allowPage.booleanValue( )
							? "frameset" : "run"; //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		String maxrows = null;
		if ( params.get( MAX_ROWS_KEY ) != null )
			maxrows = (String) params.get( MAX_ROWS_KEY );
		else
			maxrows = ViewerPlugin.getDefault( ).getPluginPreferences( )
					.getString( WebViewer.PREVIEW_MAXROW );

		return createURL( servletName, report, format, true, resourceFolder,
				maxrows );
	}

	/**
	 * Create web viewer url to run the report.
	 * 
	 * @param servletName
	 *            servlet name to viewer report
	 * @param report
	 *            report file name
	 * @param format
	 *            report format
	 * @param resourceFolder
	 *            the resource folder
	 * @param maxrows
	 *            max rows limited
	 * @return valid web viewer url
	 */
	private static String createURL( String servletName, String report,
			String format, boolean inDesigner, String resourceFolder,
			String maxrows )
	{
		String encodedReportName = null;

		try
		{
			encodedReportName = URLEncoder.encode( report, "utf-8" ); //$NON-NLS-1$
		}
		catch ( UnsupportedEncodingException e )
		{
			// Do nothing
		}

		String locale = ViewerPlugin.getDefault( ).getPluginPreferences( )
				.getString( USER_LOCALE );

		String svgFlag = ViewerPlugin.getDefault( ).getPluginPreferences( )
				.getString( SVG_FLAG );
		boolean bSVGFlag = false;

		// get -dir rtl option
		boolean rtl = false;
		String eclipseCommands = System.getProperty( "eclipse.commands" ); //$NON-NLS-1$
		if ( eclipseCommands != null )
		{
			String[] options = eclipseCommands.split( "-" ); //$NON-NLS-1$
			String regex = "[\\s]*[dD][iI][rR][\\s]*[rR][tT][lL][\\s]*"; //$NON-NLS-1$
			Pattern pattern = Pattern.compile( regex );
			for ( int i = 0; i < options.length; i++ )
			{
				String option = options[i];
				if ( pattern.matcher( option ).matches( ) )
				{
					rtl = true;
					break;
				}
			}
		}

		if ( "true".equalsIgnoreCase( svgFlag ) ) //$NON-NLS-1$
		{
			bSVGFlag = true;
		}

		String masterPageContent = ViewerPlugin.getDefault( )
				.getPluginPreferences( ).getString( MASTER_PAGE_CONTENT );
		boolean bMasterPageContent = true;
		if ( "false".equalsIgnoreCase( masterPageContent ) ) //$NON-NLS-1$
		{
			bMasterPageContent = false;
		}

		// handle resource folder encoding

		String encodedResourceFolder = null;

		try
		{
			if ( resourceFolder != null )
				encodedResourceFolder = URLEncoder.encode( resourceFolder,
						"utf-8" ); //$NON-NLS-1$
		}
		catch ( UnsupportedEncodingException e )
		{
			// Do nothing
		}
		if ( encodedResourceFolder == null )
			encodedResourceFolder = ""; //$NON-NLS-1$

		// So far, only report name is encoded as utf-8 format
		return getBaseURL( )
				+ servletName
				+ "?" //$NON-NLS-1$
				+ "__report=" + encodedReportName //$NON-NLS-1$
				+ "&__format=" + format //$NON-NLS-1$
				+ "&__svg=" + String.valueOf( bSVGFlag ) //$NON-NLS-1$
				+ ( LocaleTable.containsKey( locale )
						? "&__locale=" + LocaleTable.get( locale ) : "" ) //$NON-NLS-1$ //$NON-NLS-2$
				+ "&__designer=" //$NON-NLS-1$
				+ String.valueOf( inDesigner )
				+ "&__masterpage=" + String.valueOf( bMasterPageContent ) //$NON-NLS-1$
				+ "&__rtl=" + String.valueOf( rtl ) //$NON-NLS-1$
				+ ( maxrows != null && maxrows.trim( ).length( ) > 0
						? "&__maxrows=" + maxrows : "" ) //$NON-NLS-1$ //$NON-NLS-2$
				+ "&__resourceFolder=" + encodedResourceFolder; //$NON-NLS-1$
	}

	/**
	 * Start web application.
	 */
	private static void startWebApp( )
	{
		try
		{
			WebappAccessor.start( "viewer", WebAppPlugin, Path.EMPTY ); //$NON-NLS-1$
		}
		catch ( CoreException e )
		{
			// Do nothing
		}
	}

	/**
	 * Initiate the tomcat.
	 * 
	 * @param browser
	 *            SWT browser
	 */
	public static void startup( Browser browser )
	{
		startWebApp( );
	}

	/**
	 * Displays the specified url.
	 * 
	 * @param report
	 *            report report
	 * @param format
	 *            report format
	 */
	public static void display( String report, String format )
	{
		display( report, format, true );
	}

	/**
	 * Displays the specified url.
	 * 
	 * @param report
	 * @param format
	 * @param allowPage
	 */
	public static void display( String report, String format, boolean allowPage )
	{
		String root = null;

		if ( WebViewer.PDF.equalsIgnoreCase( format ) )
		{
			root = createURL( "run", report, format, true, null, null ); //$NON-NLS-1$
		}
		else
		{
			root = createURL(
					allowPage ? "frameset" : "run", report, format, true, null, null ) + "&" + new Random( ).nextInt( ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		try
		{
			BrowserAccessor.getPreviewBrowser( false ).displayURL( root );
		}
		catch ( Exception e )
		{
			// Do nothing
		}
	}

	/**
	 * Displays the specified url useing eclipse SWT browser.
	 * 
	 * @param report
	 *            report report
	 * @param format
	 *            report format
	 * @param browser
	 *            SWT browser instance
	 * @deprecated
	 */
	public static void display( String report, String format, Browser browser )
	{
		browser
				.setUrl( createURL( "run", report, format, true, null, null ) + "&" + new Random( ).nextInt( ) ); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/**
	 * Displays the specified url using eclipse SWT browser.
	 * 
	 * @param report
	 *            report report
	 * @param format
	 *            report format
	 * @param browser
	 *            SWT browser instance
	 * @param servletName
	 *            servlet name to viewer report
	 */
	public static void display( String report, String format, Browser browser,
			String servletName )
	{
		browser.setUrl( createURL( servletName, report, format, true, null,
				null )
				+ "&" + new Random( ).nextInt( ) ); //$NON-NLS-1$
	}

	/**
	 * Displays the specified url using eclipse SWT browser.
	 * 
	 * @param report
	 *            report report
	 * @param browser
	 *            SWT browser instance
	 * @param params
	 *            the parameter map to set
	 */

	public static void display( String report, Browser browser, Map params )
	{
		browser.setUrl( createURL( report, params )
				+ "&" + new Random( ).nextInt( ) ); //$NON-NLS-1$
	}

	/**
	 * Displays the specified url using eclipse SWT browser.
	 * 
	 * @param report
	 *            report report
	 * @param params
	 *            the parameter map to set
	 */

	public static void display( String report, Map params )
	{
		try
		{
			BrowserAccessor.getPreviewBrowser( false )
					.displayURL(
							createURL( report, params )
									+ "&" + new Random( ).nextInt( ) ); //$NON-NLS-1$
		}
		catch ( Exception e )
		{
			// Do nothing
		}
	}

}