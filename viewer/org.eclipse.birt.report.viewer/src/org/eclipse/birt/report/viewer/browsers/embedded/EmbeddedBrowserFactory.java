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

package org.eclipse.birt.report.viewer.browsers.embedded;

import org.eclipse.help.browser.IBrowser;
import org.eclipse.help.browser.IBrowserFactory;

/**
 * Implementation of Embedded browser factory.
 * Original implementation is from HELP.
 * <p>
 */
public class EmbeddedBrowserFactory implements IBrowserFactory
{
	/**
	 * Factory Constructor.
	 */
	public EmbeddedBrowserFactory( )
	{
		super( );
	}

	/**
	 * Is embedded browser factory available.
	 * 
	 * @return browser factory available or not
	 */
	public boolean isAvailable( )
	{
		return true;
	}

	/**
	 * Create embedded browser.
	 * 
	 * @return embedded browser instance
	 */
	public IBrowser createBrowser( )
	{
		return new EmbeddedBrowserAdapter( );
	}
}