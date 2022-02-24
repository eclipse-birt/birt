/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.device.svg.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.util.SecurityUtil;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * @author Actuate Corporation
 * 
 */
public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.birt.chart.device.svg.i18n.nls";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = UResourceBundle.getBundleInstance(BUNDLE_NAME,
			ULocale.getDefault(), SecurityUtil.getClassLoader(Messages.class));

	private Messages() {
	}

	public static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key key
	 * @param oas arguments
	 */
	public static String getString(String key, Object[] oas) {
		try {
			return SecurityUtil.formatMessage(RESOURCE_BUNDLE.getString(key), oas);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key key
	 * @param oa  single argument
	 */
	public static String getString(String key, Object oa) {
		try {
			return SecurityUtil.formatMessage(RESOURCE_BUNDLE.getString(key), new Object[] { oa });
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
