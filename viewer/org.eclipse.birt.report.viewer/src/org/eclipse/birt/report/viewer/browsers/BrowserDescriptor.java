/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.viewer.browsers;

import org.eclipse.help.browser.IBrowserFactory;

/**
 * Browser descriptor class.
 * <p>
 */
public class BrowserDescriptor {
	private String browserID;

	private String browserLabel;

	private IBrowserFactory factory;

	/**
	 * Browser descriptor constructor.
	 *
	 * @param id      ID of a browser as specified in plugin.xml
	 * @param label   name of the browser
	 * @param factory factory that creates instances of this browser
	 */
	public BrowserDescriptor(String id, String label, IBrowserFactory factory) {
		this.browserID = id;

		this.browserLabel = label;

		this.factory = factory;
	}

	/**
	 * Get browser id
	 *
	 * @return browser id
	 */
	public String getID() {
		return browserID;
	}

	/**
	 * Get browser label
	 *
	 * @return browser label
	 */
	public String getLabel() {
		return browserLabel;
	}

	/**
	 * Get browser factory.
	 *
	 * @return browser factory instance
	 */
	public IBrowserFactory getFactory() {
		return factory;
	}

	/**
	 * Is browser external or not
	 *
	 * @return browser external or not
	 */
	public boolean isExternal() {
		return !BrowserManager.BROWSER_ID_EMBEDDED.equals(getID());
	}
}
