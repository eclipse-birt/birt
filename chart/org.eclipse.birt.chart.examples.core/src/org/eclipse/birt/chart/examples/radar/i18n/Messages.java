/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.radar.i18n;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.util.SecurityUtil;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Provides useful methods to retrieve localized text for the
 * org.eclipse.birt.chart.device.extension plug-in classes
 */
public final class Messages {

	/**
	 * Bundle name
	 */
	private static final String RADAR_EXAMPLES = "org.eclipse.birt.chart.examples.radar.i18n.nls"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = UResourceBundle.getBundleInstance(RADAR_EXAMPLES,
			ULocale.getDefault(), SecurityUtil.getClassLoader(Messages.class));

	private static Map<ULocale, ResourceBundle> hmLocalToBundle = new HashMap<ULocale, ResourceBundle>(2);

	private Messages() {
	}

	public static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	public static ResourceBundle getResourceBundle(ULocale locale) {
		ResourceBundle bundle = hmLocalToBundle.get(locale);

		if (bundle == null) {
			bundle = UResourceBundle.getBundleInstance(RADAR_EXAMPLES, locale,
					SecurityUtil.getClassLoader(Messages.class));
			hmLocalToBundle.put(locale, bundle);
		}

		return bundle;
	}

	/**
	 * @param key
	 */
	public static String getString(String key) {
		return getString(key, ULocale.getDefault());
	}

	/**
	 * @param key
	 * @param lcl
	 */
	public static String getString(String key, ULocale lcl) {
		try {
			return getResourceBundle(lcl).getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key key
	 * @param oa  single argument
	 */
	public static String getString(String key, Object oa, ULocale lcl) {
		return getString(key, new Object[] { oa }, lcl);
	}

	/**
	 * @param key
	 * @param oa
	 * @param lcl
	 */
	public static String getString(String key, Object[] oa, ULocale lcl) {
		try {
			return SecurityUtil.formatMessage(getResourceBundle(lcl).getString(key), oa);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return '!' + key + '!';
		}
	}
}
