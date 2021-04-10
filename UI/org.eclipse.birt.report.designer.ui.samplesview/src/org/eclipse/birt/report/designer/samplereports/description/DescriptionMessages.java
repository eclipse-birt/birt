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

package org.eclipse.birt.report.designer.samplereports.description;

import java.util.ResourceBundle;

public class DescriptionMessages {

	private static final String BUNDLE_NAME = "org.eclipse.birt.report.designer.samplereports.description.descriptions"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * constructor
	 */
	private DescriptionMessages() {
	}

	public static ResourceBundle getReportResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	/**
	 * Gets common translation for current local
	 * 
	 * @param key the key
	 * @return translated value string
	 */

	public static String getDescription(String key) {

		try {
			String result = RESOURCE_BUNDLE.getString(key);
			return result;
		} catch (Exception e) {
			assert false;
			return key;
		}
	}
}
