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

package org.eclipse.birt.report.model.i18n;

import com.ibm.icu.util.ULocale;

/**
 * Returns the localized messages from I18N message file. This class can not be
 * instantiated.
 */

public class ModelMessages {

	/**
	 * This class can not be instantiated.
	 */

	private ModelMessages() {
	}

	/**
	 * The I18N resource handler.
	 */

	private static ThreadResources threadResources = null;

	static {
		threadResources = new ThreadResources();
	}

	/**
	 * Returns the localized message with the given resource key.
	 * 
	 * @param key the resource key
	 * @return the localized message
	 */

	public static String getMessage(String key) {
		return threadResources.getMessage(key);
	}

	/**
	 * Returns the localized message with the given resource key.
	 * 
	 * @param key    the resource key
	 * @param locale the locale
	 * @return the localized message
	 */

	public static String getMessage(String key, ULocale locale) {
		return threadResources.getMessage(key, locale);
	}

	/**
	 * Returns the localized message with the given resource key and arguments.
	 * 
	 * @param key       the resource key
	 * @param arguments the arguments for localized message
	 * @return the localized message
	 */

	public static String getMessage(String key, Object[] arguments) {
		return threadResources.getMessage(key, arguments);
	}

}
