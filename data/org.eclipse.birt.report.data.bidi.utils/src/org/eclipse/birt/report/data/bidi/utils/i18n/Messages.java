/***********************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.bidi.utils.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * This class deals with the translation with the given key
 *
 *
 */

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.birt.report.data.bidi.utils.i18n.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * constructor
	 */

	private Messages() {
	}

	/**
	 * Gets the report ResourceBundle
	 *
	 * @return
	 */
	public static ResourceBundle getReportResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	/**
	 * Gets common translation for current local
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

	/**
	 * Gets formatted translation for current local
	 *
	 * @param key the key
	 * @return translated value string
	 */
	public static String getFormattedString(String key, Object[] arguments) {
		return MessageFormat.format(getString(key), arguments);
	}

	/**
	 * In meta xml file we use %keyName% as externalized key instead of value We use
	 * this method to translate the %keyName% into value from resource bundle.
	 *
	 * @param key the externalized key like %keyName%
	 * @return value the %keyName% represent
	 */

	public static String getXMLKey(String key) {
		return key;
	}
}
