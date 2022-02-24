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
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Provides the resource bundle with the given locale.
 */

public interface IResourceBundleProvider {

	/**
	 * Returns the resource bundle with the locale.
	 * 
	 * @param locale the given locale
	 * @return the resource bundle
	 * @deprecated to support ICU4J, replaced by : getResourceBundle(ULocale locale)
	 */

	public ResourceBundle getResourceBundle(Locale locale);

	/**
	 * Returns the resource bundle with the locale.
	 * 
	 * @param locale the given locale
	 * @return the resource bundle
	 */

	public UResourceBundle getResourceBundle(ULocale locale);

}
