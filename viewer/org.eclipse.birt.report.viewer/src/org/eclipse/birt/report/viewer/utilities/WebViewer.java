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

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.browsers.BrowserAccessor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.browser.Browser;

/**
 * Static accessor to display an arbitary url.
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
	 * Get web viewer base url.
	 * 
	 * @return base web viewer application url
	 */
	private static String getBaseURL( )
	{
		return "http://" + WebappAccessor.getHost( ) + ":" + WebappAccessor.getPort( )	+ "/viewer/"; //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
	}
	
	/**
	 * Create web viewer url to run the report.
	 * 
	 * @param servletName servlet name to viewer report
	 * @param uri report uri 
	 * @param format report format
	 * @return valid web viewer url
	 */
	private static String createURL( String servletName, String uri, String format )
	{
		String encodedUri = null;
		try
		{
			encodedUri = URLEncoder.encode( uri, "utf-8" ); //$NON-NLS-1$
		}
		catch ( UnsupportedEncodingException e )
		{
			;
		}
		
		/**
		 * So far, only uri is encoded as utf-8 format 
		 */
		return getBaseURL( ) + servletName + "?" + "__uri=" + encodedUri + "&__format=" + format; //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
	}

	/**
	 * Displays the specified url.
	 * 
	 * @param uri report uri
	 * @param format report format
	 */
	private static void startWebApp( )
	{
		try
		{
			WebappAccessor.start( "viewer", WebAppPlugin, Path.EMPTY ); //$NON-NLS-1$
		}
		catch ( CoreException e )
		{
			;
		}
	}
	
	/**
	 * Initiate the tomcat.
	 * 
	 * @param browser SWT browser
	 */
	public static void startup( Browser browser )
	{
		startWebApp( );

		browser.setUrl( getBaseURL( ) + "initservlet" ); //$NON-NLS-1$
	}

	/**
	 * Displays the specified url.
	 * 
	 * @param uri report uri
	 * @param format report format
	 */
	public static void display( String uri, String format )
	{
		startWebApp( );

		String root = null;

		if ( WebViewer.PDF.equalsIgnoreCase( format ) )
		{
			root = createURL( "engineservlet", uri, format ); //$NON-NLS-1$
		}
		else
		{
			root = createURL( "viewerservlet", uri, format ); //$NON-NLS-1$
		}

		try
		{
			BrowserAccessor.getPreviewBrowser( false ).displayURL( root );
		}
		catch ( Exception e )
		{
			;
		}
	}

	/**
	 * Displays the specified url useing eclipse SWT browser.
	 * 
	 * @param uri report uri
	 * @param format report format
	 * @param browser SWT browser instance
	 */
	public static void display( String uri, String format, Browser browser )
	{
		startWebApp( );

		browser.setUrl( createURL( "engineservlet", uri, format ) ); //$NON-NLS-1$
	}

}