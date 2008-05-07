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

import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.browsers.BrowserAccessor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.browser.Browser;

/**
 * Static accessor to display an arbitary url. It serves as an entry point to
 * integrate viewer.
 * <p>
 */
public class WebViewer
{

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
	 * DOC format name
	 */
	public final static String DOC = "doc"; //$NON-NLS-1$

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
	 * locale preference name
	 */
	public final static String USER_LOCALE = "user_locale"; //$NON-NLS-1$

	/**
	 * Preference key for SVG chart flag.
	 */
	public final static String SVG_FLAG = "svg_flag"; //$NON-NLS-1$

	/**
	 * Preference key for master page content flag.
	 */
	public final static String MASTER_PAGE_CONTENT = "master_page_content"; //$NON-NLS-1$

	/** Preference key for max rows. */
	public final static String PREVIEW_MAXROW = "preview_maxrow"; //$NON-NLS-1$

	/** Preference key for max cube fetch levels. */
	public final static String PREVIEW_MAXCUBEROWLEVEL = "preview_maxrowlevelmember"; //$NON-NLS-1$

	public final static String PREVIEW_MAXCUBECOLUMNLEVEL = "preview_maxcolumnlevelmember"; //$NON-NLS-1$

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
	public final static String MAX_CUBE_ROW_LEVELS_KEY = "MAX_CUBE_ROW_LEVELS_KEY"; //$NON-NLS-1$

	public final static String MAX_CUBE_COLUMN_LEVELS_KEY = "MAX_CUBE_COLUMN_LEVELS_KEY"; //$NON-NLS-1$

	/**
	 * Property to indicate whether it is a report debug mode
	 */
	public final static String REPORT_DEBUT_MODE = "report_debug_mode"; //$NON-NLS-1$

	/**
	 * ClassLoader to reload workspace class
	 */
	private static ReloadableClassLoader reloadableClassLoader = null;

	/**
	 * locale mapping. Save some time.
	 */
	public static TreeMap LocaleTable = null;

	static
	{
		// Initialize the locale mapping table
		LocaleTable = new TreeMap( Collator.getInstance( ) );
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
			return createURL( null, report, null, null, null, null, null );
		String servletName = (String) params.get( SERVLET_NAME_KEY );
		String format = (String) params.get( FORMAT_KEY );
		String resourceFolder = (String) params.get( RESOURCE_FOLDER_KEY );
		Boolean allowPage = (Boolean) params.get( ALLOW_PAGE_KEY );

		if ( format == null || format.trim( ).length( ) <= 0
				|| HTM.equalsIgnoreCase( format ) )
			format = HTML;

		if ( !HTML.equalsIgnoreCase( format ) )
		{
			servletName = VIEWER_PREVIEW;
		}
		else
		{
			if ( servletName == null || servletName.trim( ).length( ) <= 0 )
			{
				if ( allowPage == null )
					servletName = VIEWER_FRAMESET;
				else
				{
					servletName = allowPage.booleanValue( )
							? VIEWER_FRAMESET
							: VIEWER_PREVIEW;
				}
			}
		}

		// max rows setting
		String maxrows = (String) params.get( MAX_ROWS_KEY );

		// max level member setting
		String maxrowlevels = (String) params.get( MAX_CUBE_ROW_LEVELS_KEY );
		String maxcolumnlevels = (String) params
				.get( MAX_CUBE_COLUMN_LEVELS_KEY );

		String url = createURL( servletName, report, format, resourceFolder,
				maxrows, maxrowlevels, maxcolumnlevels );

		// if document mode, append document parameter in URL
		String documentName = (String) params.get( DOCUMENT_NAME_KEY );
		if ( documentName != null && VIEWER_DOCUMENT.equals( servletName ) )
		{
			// current opened report isn't document
			if ( !isReportDocument( report ) )
			{
				try
				{
					String encodedDocumentName = URLEncoder.encode(
							documentName, UTF_8 );
					url += "&__document=" + encodedDocumentName; //$NON-NLS-1$

					String isCloseWin = (String) params.get( CLOSE_WINDOW_KEY );
					if ( isCloseWin != null )
						url += "&__closewin=" + isCloseWin; //$NON-NLS-1$
				}
				catch ( UnsupportedEncodingException e )
				{
					LogUtil.logWarning( e.getLocalizedMessage( ), e );
				}
			}
		}

		// append appcontext extension name
		String appContextName = ViewerPlugin.getDefault( )
				.getPluginPreferences( ).getString( APPCONTEXT_EXTENSION_KEY );
		if ( appContextName != null && appContextName.trim( ).length( ) > 0 )
		{
			try
			{
				String encodedAppContextName = URLEncoder.encode(
						appContextName.trim( ), UTF_8 );
				url += "&__appcontextname=" + encodedAppContextName; //$NON-NLS-1$
			}
			catch ( UnsupportedEncodingException e )
			{
				LogUtil.logWarning( e.getLocalizedMessage( ), e );
			}
		}

		return url;
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
	 * @param maxlevels
	 *            max level member limited
	 * @return valid web viewer url
	 */
	private static String createURL( String servletName, String report,
			String format, String resourceFolder, String maxrows,
			String maxrowlevels, String maxcolumnlevels )
	{
		String encodedReportName = null;

		try
		{
			encodedReportName = URLEncoder.encode( report, UTF_8 );
		}
		catch ( UnsupportedEncodingException e )
		{
			LogUtil.logWarning( e.getLocalizedMessage( ), e );
		}

		String locale = ViewerPlugin.getDefault( ).getPluginPreferences( )
				.getString( USER_LOCALE );

		if ( LocaleTable.containsKey( locale ) )
		{
			locale = (String) LocaleTable.get( locale );
		}
		else
		{
			if ( "".equals( locale ) ) //$NON-NLS-1$
			{
				locale = null;
			}
			else
			{
				try
				{
					locale = URLEncoder.encode( locale, UTF_8 );
				}
				catch ( UnsupportedEncodingException e )
				{
					locale = null;
					LogUtil.logWarning( e.getLocalizedMessage( ), e );
				}
			}
		}
		
		String svgFlag = ViewerPlugin.getDefault( ).getPluginPreferences( )
				.getString( SVG_FLAG );
		boolean bSVGFlag = false;

		// cube memory size
		String cubeMemorySize = ViewerPlugin.getDefault( )
				.getPluginPreferences( )
				.getString( PREVIEW_MAXINMEMORYCUBESIZE );

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
						UTF_8 );
		}
		catch ( UnsupportedEncodingException e )
		{
			LogUtil.logWarning( e.getLocalizedMessage( ), e );
		}
		if ( encodedResourceFolder == null )
			encodedResourceFolder = ""; //$NON-NLS-1$

		String reportParam = "__report"; //$NON-NLS-1$
		if ( isReportDocument( encodedReportName ) )
			reportParam = "__document"; //$NON-NLS-1$
		reportParam += "=" + encodedReportName; //$NON-NLS-1$

		// workaround for postscript format, force "Content-Disposition" as
		// "attachment"
		String asattachment = null;
		if ( POSTSCRIPT.equalsIgnoreCase( format ) )
			asattachment = "&__asattachment=true"; //$NON-NLS-1$	

		// get the local DPI setting
		int dpi = Toolkit.getDefaultToolkit( ).getScreenResolution( );

		// So far, only report name is encoded as utf-8 format
		return getBaseURL( )
				+ servletName
				+ "?" //$NON-NLS-1$
				+ reportParam
				+ "&__format=" + format //$NON-NLS-1$
				+ "&__svg=" + String.valueOf( bSVGFlag ) //$NON-NLS-1$
				+ ( locale != null ? "&__locale=" + locale : "" ) //$NON-NLS-1$ //$NON-NLS-2$
				+ "&__masterpage=" + String.valueOf( bMasterPageContent ) //$NON-NLS-1$
				+ "&__rtl=" + String.valueOf( rtl ) //$NON-NLS-1$
				+ ( maxrows != null && maxrows.trim( ).length( ) > 0
						? "&__maxrows=" + maxrows : "" ) //$NON-NLS-1$ //$NON-NLS-2$
				+ ( maxrowlevels != null && maxrowlevels.trim( ).length( ) > 0
						? "&__maxrowlevels=" + maxrowlevels : "" ) //$NON-NLS-1$ //$NON-NLS-2$
				+ ( maxcolumnlevels != null
						&& maxcolumnlevels.trim( ).length( ) > 0
						? "&__maxcolumnlevels=" + maxcolumnlevels : "" ) //$NON-NLS-1$ //$NON-NLS-2$
				+ ( cubeMemorySize != null
						&& cubeMemorySize.trim( ).length( ) > 0
						? "&__cubememsize=" + cubeMemorySize : "" ) //$NON-NLS-1$ //$NON-NLS-2$
				+ "&__resourceFolder=" + encodedResourceFolder //$NON-NLS-1$
				+ ( asattachment != null ? asattachment : "" ) //$NON-NLS-1$
				+ "&__dpi=" + dpi; //$NON-NLS-1$
	}

	/**
	 * Start web application.
	 */
	private synchronized static void startWebApp( )
	{
		try
		{
			// if don't load debug ui, viewer will handle to set workspace
			// classpath
			String debugMode = System.getProperty( REPORT_DEBUT_MODE );
			if ( debugMode == null )
			{
				// get workspace classpath
				String classpaths = ViewerClassPathHelper
						.getWorkspaceClassPath( );

				URL[] urls = ViewerClassPathHelper.parseURLs( classpaths );
				if ( reloadableClassLoader == null )
				{
					// create ReloadableClassLoader
					reloadableClassLoader = new ReloadableClassLoader( urls,
							WebViewer.class.getClassLoader( ) );
				}
				else
				{
					// reload class
					reloadableClassLoader.setUrls( urls );
					reloadableClassLoader.reload( );
				}
			}

			WebappAccessor.start( ViewerPlugin.WEBAPP_CONTEXT );
		}
		catch ( CoreException e )
		{
			LogUtil.logError( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 * Stop web application
	 */
	private static void stopWebApp( )
	{
		try
		{
			WebappAccessor.stop( ViewerPlugin.WEBAPP_CONTEXT );
		}
		catch ( CoreException e )
		{
			LogUtil.logError( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 * Initiate the tomcat.
	 * 
	 */
	public static void startup( )
	{
		startWebApp( );
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
	 * Stop the web server
	 */
	public static void stop( )
	{
		stopWebApp( );
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
		if ( format == null || format.trim( ).length( ) <= 0
				|| HTM.equalsIgnoreCase( format ) )
			format = HTML;

		String root = null;
		if ( !HTML.equalsIgnoreCase( format ) )
		{
			root = createURL( VIEWER_PREVIEW, report, format, null, null, null,
					null );
		}
		else
		{
			root = createURL( allowPage ? VIEWER_FRAMESET : VIEWER_PREVIEW,
					report, format, null, null, null, null )
					+ "&" + new Random( ).nextInt( ); //$NON-NLS-1$
		}

		startWebApp( );

		try
		{
			BrowserAccessor.getPreviewBrowser( false ).displayURL( root );
		}
		catch ( Exception e )
		{
			LogUtil.logError( e.getLocalizedMessage( ), e );
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
		startWebApp( );
		browser
				.setUrl( createURL(
						"run", report, format, null, null, null, null ) + "&" + new Random( ).nextInt( ) ); //$NON-NLS-1$ //$NON-NLS-2$

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
	 * @deprecated
	 */
	public static void display( String report, String format, Browser browser,
			String servletName )
	{
		startWebApp( );
		browser.setUrl( createURL( servletName, report, format, null, null,
				null, null )
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
		startWebApp( );
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
		startWebApp( );

		try
		{
			BrowserAccessor.getPreviewBrowser( false )
					.displayURL(
							createURL( report, params )
									+ "&" + new Random( ).nextInt( ) ); //$NON-NLS-1$
		}
		catch ( Exception e )
		{
			LogUtil.logError( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 * Check whether the report is a document file.
	 * 
	 * @param reportName
	 * @return true or false
	 */
	private static boolean isReportDocument( String reportName )
	{
		if ( reportName == null )
			return false;

		Pattern p = Pattern.compile( ".[a-z]{3}document$" ); //$NON-NLS-1$
		Matcher m = p.matcher( reportName );
		if ( m.find( ) )
			return true;

		return false;
	}

	/**
	 * Cancel the process
	 * 
	 * @param browser
	 */
	public static void cancel( Browser browser )
	{
		if ( browser == null || browser.isDisposed( ) )
		{
			return;
		}

		try
		{
			browser
					.execute( "try { if( birtProgressBar ){ birtProgressBar.cancel(); } } catch(e){}" ); //$NON-NLS-1$
		}
		catch ( Exception e )
		{
			LogUtil.logError( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 * Returns the application classloader
	 * 
	 * @return
	 */
	public static ClassLoader getAppClassLoader( )
	{
		return reloadableClassLoader;
	}
}