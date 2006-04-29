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
import java.util.Random;
import java.util.TreeMap;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.utility.ParameterAccessor;
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
	 * @param servletName
	 *            servlet name to viewer report
	 * @param report
	 *            report file name
	 * @param format
	 *            report format
	 * @return valid web viewer url
	 */
	private static String createURL( String servletName, String report,
			String format )
	{
		String encodedReportName = null;

		try
		{
			encodedReportName = URLEncoder.encode( report,
					ParameterAccessor.UTF_8_ENCODE );
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

		// So far, only report name is encoded as utf-8 format
		return getBaseURL( )
				+ servletName
				+ ParameterAccessor.QUERY_CHAR
				+ ParameterAccessor.PARAM_REPORT
				+ ParameterAccessor.EQUALS_OPERATOR
				+ encodedReportName + ParameterAccessor.PARAMETER_SEPARATOR
				+ ParameterAccessor.PARAM_FORMAT
				+ ParameterAccessor.EQUALS_OPERATOR
				+ format + ParameterAccessor.PARAMETER_SEPARATOR
				+ ParameterAccessor.PARAM_SVG
				+ ParameterAccessor.EQUALS_OPERATOR
				+ String.valueOf( bSVGFlag ) + ParameterAccessor.PARAMETER_SEPARATOR
				+ ( LocaleTable.containsKey( locale )
						? ParameterAccessor.PARAM_LOCALE
								+ ParameterAccessor.EQUALS_OPERATOR
								+ LocaleTable.get( locale )
						: "" ) //$NON-NLS-1$
				+ "&__designer=true" + ParameterAccessor.PARAMETER_SEPARATOR//$NON-NLS-1$
				+ ParameterAccessor.PARAM_MASTERPAGE
				+ ParameterAccessor.EQUALS_OPERATOR
				+ String.valueOf( bMasterPageContent );
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
		startWebApp( );
		String root = null;

		if ( WebViewer.PDF.equalsIgnoreCase( format ) )
		{
			root = createURL( IBirtConstants.VIEWER_RUN, report, format );
		}
		else
		{
			root = createURL( allowPage
					? IBirtConstants.VIEWER_FRAMESET
					: IBirtConstants.VIEWER_RUN, report, format )
					+ ParameterAccessor.PARAMETER_SEPARATOR
					+ new Random( ).nextInt( );
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
	 */
	public static void display( String report, String format, Browser browser )
	{
		startWebApp( );
		browser.setUrl( createURL( IBirtConstants.VIEWER_RUN, report, format )
				+ ParameterAccessor.PARAMETER_SEPARATOR
				+ new Random( ).nextInt( ) );

	}

}