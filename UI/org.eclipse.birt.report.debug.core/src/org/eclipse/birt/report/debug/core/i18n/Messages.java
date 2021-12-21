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

package org.eclipse.birt.report.debug.core.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Messages
 */
public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.birt.report.debug.core.i18n.nls";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = UResourceBundle.getBundleInstance(BUNDLE_NAME,
			ULocale.getDefault().getName(), Messages.class.getClassLoader());

	private Messages() {
	}

	/**
	 * 
	 * @return the default resource bundle of this plugin
	 */
	public static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	/**
	 * 
	 * @param locale
	 * @return resource bundle with the given locale
	 */
	public static ResourceBundle getResourceBundle(ULocale locale) {
		if (ULocale.getDefault().equals(locale) || locale == null) {
			return RESOURCE_BUNDLE;
		}
		return UResourceBundle.getBundleInstance(BUNDLE_NAME, locale.getName(), Messages.class.getClassLoader());
	}

	/**
	 * 
	 * @param key
	 * @return the externalized message if found, otherwise null
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key
	 * @param locale
	 * @return the externalized message if found, otherwise null
	 */
	public static String getString(String key, ULocale locale) {
		try {
			return getResourceBundle(locale).getString(key);
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key
	 * @param oas
	 * @return the externalized message if found, otherwise null
	 */
	public static String getString(String key, Object[] oas) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), oas);
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key
	 * @param oa
	 * @return the externalized message if found, otherwise null
	 */
	public static String getString(String key, Object oa) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), new Object[] { oa });
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}
}
