/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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
	@Override
	public boolean isAvailable() {
		return Constants.WS_WIN32.equalsIgnoreCase(Platform.getOS());
	}

	/**
	 * Create system browser.
	 *
	 * @return system browser instance
	 */
	@Override
	public IBrowser createBrowser() {
		return new SystemBrowserAdapter();
	}
}
