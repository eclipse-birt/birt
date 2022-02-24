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

package org.eclipse.birt.report.model.api.extension;

import java.util.Locale;

import com.ibm.icu.util.ULocale;

/**
 * The interface for getting localized messages from extension element. The
 * extension element can has its own message file to take the translation from
 * resource key to localized message according to the given locale.
 */

public interface IMessages {

	/**
	 * Returns the localized message given the resource key and given locale.
	 * 
	 * @param key    the resource key
	 * @param locale the locale
	 * @return the localized message
	 * @deprecated to support ICU4J, replaced by : getMessage(String key, ULocale
	 *             lcoale)
	 */

	public String getMessage(String key, Locale locale);

	/**
	 * Returns the localized message given the resource key and given locale.
	 * 
	 * @param key    the resource key
	 * @param locale the locale of type <code>ULocale</code>
	 * @return the localized message
	 */

	public String getMessage(String key, ULocale locale);

}
