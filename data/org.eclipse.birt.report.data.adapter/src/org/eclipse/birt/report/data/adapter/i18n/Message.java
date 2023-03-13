/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.adapter.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;

public class Message {
	private static ResourceBundle rb = AdapterResourceHandle.getInstance().getUResourceBundle();

	public static String getMessage(String key) {
		try {
			if (rb != null) {
				return rb.getString(key);
				// Fall through to return key
			}
		} catch (MissingResourceException e) {
		}
		return " #" + key + "# ";
	}

	public static String getMessage(String key, ULocale local) {
		try {
			ResourceBundle rb = AdapterResourceHandle.getInstance(local).getUResourceBundle();
			if (rb != null) {
				return rb.getString(key);
				// Fall through to return key
			}
		} catch (MissingResourceException e) {
		}
		return " #" + key + "# ";
	}

	public static String formatMessage(String key, Object[] args) {
		return MessageFormat.format(getMessage(key), args);
	}
}
