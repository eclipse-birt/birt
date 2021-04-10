/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * @author Actuate Corporation
 * 
 */
public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.birt.chart.ui.i18n.nls";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = UResourceBundle.getBundleInstance(BUNDLE_NAME,
			ULocale.getDefault(), Messages.class.getClassLoader());

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

	public static String getString(String key, Object[] arguments) {
		return MessageFormat.format(getString(key), arguments);
	}

	public static String getString(String key, String argument) {
		return MessageFormat.format(getString(key), new Object[] { argument });
	}
}