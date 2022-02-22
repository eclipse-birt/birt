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

package org.eclipse.birt.core.ui.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 *
 */

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.birt.core.ui.i18n.nls"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = UResourceBundle.getBundleInstance(BUNDLE_NAME,
			ULocale.getDefault(), Messages.class.getClassLoader());

	private Messages() {
	}

	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getFormattedString(String key, Object[] arguments) {
		return MessageFormat.format(getString(key), arguments);
	}

	public static String getFormattedString(String key, String argument) {
		return MessageFormat.format(getString(key), new Object[] { argument });
	}
}
