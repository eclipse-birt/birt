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

import org.eclipse.core.runtime.Platform;
import org.eclipse.help.browser.IBrowser;
import org.eclipse.help.browser.IBrowserFactory;
import org.eclipse.osgi.service.environment.Constants;

/**
 * The System browser factory is derived from Help's SystemBrowserFactory.
 * <p>
 */
public class SystemBrowserFactory implements IBrowserFactory {
	/**
	 * Constructor.
	 */
	public SystemBrowserFactory() {
		super();
	}

	/**
	 * Is system browser factory available.
	 * 
	 * @return browser factory available or not
	 */
	public boolean isAvailable() {
		return Constants.WS_WIN32.equalsIgnoreCase(Platform.getOS());
	}

	/**
	 * Create system browser.
	 * 
	 * @return system browser instance
	 */
	public IBrowser createBrowser() {
		return new SystemBrowserAdapter();
	}
}