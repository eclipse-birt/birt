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

package org.eclipse.birt.report.viewer.browsers.system;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.help.browser.IBrowser;
import org.eclipse.swt.program.Program;

/**
 * Derived from HELP's SystemBrowserAdapter. System browser only works in win32
 * system.
 * 
 * @version $
 */
public class SystemBrowserAdapter implements IBrowser
{
	String[] cmdarray;

	/**
	 * Adapter constructor.
	 */
	public SystemBrowserAdapter( )
	{
		;
	}

	/**
	 * Close browser
	 */
	public void close( )
	{
		;
	}

	/**
	 * Display arbitary url.
	 * 
	 * @param url
	 */
	public void displayURL( String url )
	{
		if ( !Program.launch( url ) )
		{
			ViewerPlugin.logError( 
					ViewerPlugin.getFormattedResourceString( "viewer.browser.systemBrowser.noprogramforurl", //$NON-NLS-1$
							new Object[] { url } ), null );
		}
	}

	/**
	 * Is browser supports close operation.
	 * 
	 * @return browser supports close operation
	 */
	public boolean isCloseSupported( )
	{
		return false;
	}

	/**
	 * Is setting browser window location supported.
	 * 
	 * @return setting browser window location or not
	 */
	public boolean isSetLocationSupported( )
	{
		return false;
	}

	/**
	 * Is setting browser window size supported.
	 * 
	 * @return setting browser window size or not
	 */
	public boolean isSetSizeSupported( )
	{
		return false;
	}

	/**
	 * Set browser window location.
	 * 
	 * @param x X coordinate of browser window's top-left corner
	 * @param y Y coordinate of browser window's top-left corner
	 */
	public void setLocation( int x, int y )
	{
		;
	}

	/**
	 * Set browser window size.
	 * 
	 * @param width browser window width
	 * @param height browser window height
	 */
	public void setSize( int width, int height )
	{
		;
	}
}