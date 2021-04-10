/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * This class deals with the translation with the given key.
 */
public class Messages {

	/** The resource bundle name. */
	private static final String BUNDLE_NAME = "org.eclipse.birt.report.engine.emitter.config.i18n.messages"; //$NON-NLS-1$

	/** The resource bundle. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Constructor of this class.
	 */
	private Messages() {
		// This is private constructor.
	}

	/**
	 * Returns the resource bundle.
	 * 
	 * @return the resource bundle.
	 */
	public static ResourceBundle getReportResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	/**
	 * Returns common translation for current local.
	 * 
	 * @param key the key to translate.
	 * @return translated value string.
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
	 * Returns formatted translation for current local.
	 * 
	 * @param key the key to translate.
	 * @return translated value string.
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
