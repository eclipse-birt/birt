/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.view.description;

import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.birt.chart.examples.view.description.nl"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * constructor
	 */
	private Messages() {
	}

	public static ResourceBundle getReportResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	/**
	 * Gets common translation for current locale
	 * 
	 * @param key the key
	 * @return translated value string
	 * @deprecated
	 */

	public static String getDescription(String key) {
		return getString(key);
	}

	/**
	 * Gets common translation for current locale
	 * 
	 * @param key the key
	 * @return translated value string
	 */

	public static String getString(String key) {
		try {
			String result = RESOURCE_BUNDLE.getString(key);
			return result;
		} catch (Exception e) {
			assert false;
			return key;
		}
	}
}