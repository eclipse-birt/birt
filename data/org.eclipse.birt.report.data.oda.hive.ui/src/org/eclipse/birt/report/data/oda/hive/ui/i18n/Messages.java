/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

/**
 * Class to load/format i18n messages
 */
package org.eclipse.birt.report.data.oda.hive.ui.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static ResourceBundle rb = ResourceBundle
			.getBundle("org.eclipse.birt.report.data.oda.hive.ui.i18n.Messages");

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

	public static String formatMessage(String key, Object[] args) {
		return MessageFormat.format(getMessage(key), args);
	}
}
