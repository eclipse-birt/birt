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

package org.eclipse.birt.report.viewer.browsers;

import org.eclipse.help.browser.IBrowser;

/**
 * Static accessor class for available borwsers.
 * <p>
 */
public class BrowserAccessor
{
	private static IBrowser browser;

	private static IBrowser internalBrowser;

	/**
	 * Get current preview browser.
	 * 
	 * @param forceExternal forece using external browser or not
	 * @return browser instance
	 */
	public static synchronized IBrowser getPreviewBrowser( boolean forceExternal )
	{
		if ( !forceExternal )
		{
			if ( internalBrowser == null )
			{
				internalBrowser = BrowserManager.getInstance( )
						.createBrowser( false );
			}

			return internalBrowser;
		}
		if ( browser == null )
		{
			browser = BrowserManager.getInstance( ).createBrowser( true );
		}

		return browser;
	}
}