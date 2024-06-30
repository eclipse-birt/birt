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

package org.eclipse.birt.report.model.tests.table;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.report.model.api.extension.IMessages;

import com.ibm.icu.util.ULocale;

/**
 * Implements <code>IMessages</code> for testing
 */

public class MessagesImpl implements IMessages {

	/**
	 * The resource bundle for English.
	 */

	private ResourceBundle englishResourceBundle = ResourceBundle
			.getBundle("org.eclipse.birt.report.model.tests.table.message", Locale.ENGLISH); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IMessages#getMessage(java.lang.
	 * String)
	 */

	@Override
	public String getMessage(String key, Locale locale) {
		if (Locale.ENGLISH.equals(locale)) {
			return englishResourceBundle.getString(key);
		}

		return "Not supported locale"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IMessages#getMessage(java.lang.
	 * String)
	 */

	@Override
	public String getMessage(String key, ULocale locale) {
		if (ULocale.ENGLISH.equals(locale)) {
			return englishResourceBundle.getString(key);
		}

		return "Not supported locale"; //$NON-NLS-1$
	}
}
