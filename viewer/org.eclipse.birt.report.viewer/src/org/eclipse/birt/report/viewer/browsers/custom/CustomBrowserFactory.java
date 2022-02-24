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

package org.eclipse.birt.report.viewer.browsers.custom;

import org.eclipse.help.browser.IBrowser;
import org.eclipse.help.browser.IBrowserFactory;

/**
 * Produces Custom Browser. The original implementation is from HELP.
 * <p>
 */
public class CustomBrowserFactory implements IBrowserFactory {
	/**
	 * Is custom browser factory available.
	 * 
	 * @return custom browser available or not
	 */
	public boolean isAvailable() {
		return true;
	}

	/**
	 * Create cutom browser instance.
	 * 
	 * @return custom browser instance
	 */
	public IBrowser createBrowser() {
		return new CustomBrowserAdapter();
	}
}
