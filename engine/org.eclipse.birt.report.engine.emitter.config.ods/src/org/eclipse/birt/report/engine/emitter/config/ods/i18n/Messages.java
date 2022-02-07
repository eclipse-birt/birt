/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.config.ods.i18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class deals with the translation with the given key.
 */
public class Messages {

	/** The resource bundle name. */
	private static final String BUNDLE_NAME = "org.eclipse.birt.report.engine.emitter.config.ods.i18n.messages"; //$NON-NLS-1$

	/** The resource bundle. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	private static Map<Locale, ResourceBundle> localeToBundle = new HashMap<Locale, ResourceBundle>();

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
	 * Returns the resource bundle.
	 * 
	 * @return the resource bundle.
	 */
	public static ResourceBundle getReportResourceBundle(Locale locale) {
		ResourceBundle bundle = getReportResourceBundle();
		if (locale != null) {
			bundle = localeToBundle.get(locale);
			if (bundle == null) {
				bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
				localeToBundle.put(locale, bundle);
			}
		}
		return bundle == null ? RESOURCE_BUNDLE : bundle;
	}

	/**
	 * Returns common translation for current local.
	 * 
	 * @param key the key to translate.
	 * @return translated value string.
	 */

	public static String getString(String key) {
		return getString(key, Locale.getDefault());
	}

	/**
	 * Returns common translation for current local.
	 * 
	 * @param key the key to translate.
	 * @return translated value string.
	 */
	public static String getString(String key, Locale locale) {
		try {
			String result = getReportResourceBundle(locale).getString(key);
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
		return getFormattedString(key, arguments, Locale.getDefault());
	}

	/**
	 * Returns formatted translation for current local.
	 * 
	 * @param key the key to translate.
	 * @return translated value string.
	 */
	public static String getFormattedString(String key, Object[] arguments, Locale locale) {
		return MessageFormat.format(getString(key, locale), arguments);
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
