/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * The helper to get the locale related message file names.
 *
 */

public class ResourceHelper {

	/**
	 * base name of the resource bundle. The name is a common base name
	 */

	protected String baseName = null;

	ResourceHelper(String baseName) {
		this.baseName = baseName;
	}

	/**
	 * Gets a helper to deal with a bundle of message files.
	 * 
	 * @param baseName base name of the resource bundle. The name is a common base
	 *                 name
	 * @return a correspondent helper instance. Return <code>null</code> if the
	 *         <code>msgFolder</code> is null or not a directory.
	 * 
	 */

	public static ResourceHelper getHelper(String baseName) {
		return new ResourceHelper(baseName);
	}

	/**
	 * Return a message resource name list for the given locale. A message key
	 * should be look into the files in the sequence order from the first to the
	 * last. Content of the list is <code>String</code>.
	 * <p>
	 * If the given locale is <code>null</code>, locale of the current thread will
	 * be used.
	 * 
	 * @param locale locale to use when locating the bundles.
	 * 
	 * @return a message file list for the given locale.
	 */

	public List<String> getMessageFilenames(ULocale locale) {
		return getMessageFilenames(locale, true);
	}

	/**
	 * Return a message resource name list for the given locale. A message key
	 * should be look into the files in the sequence order from the first to the
	 * last. Content of the list is <code>String</code>.
	 * <p>
	 * If the given locale is <code>null</code>, locale of the current thread will
	 * be used.
	 * 
	 * @param locale        locale to use when locating the bundles.
	 * @param appendDefault if <code>true</code>, add the message file with empty
	 *                      locale into the return list.
	 * 
	 * @return a message file list for the given locale.
	 */

	public List<String> getMessageFilenames(ULocale locale, boolean appendDefault) {
		if (locale == null)
			locale = ThreadResources.getLocale();

		List<String> bundleNames = new ArrayList<String>();

		if (this.baseName == null)
			return bundleNames;

		// find the correspondent message files.
		// e.g: message

		final String language = locale.getLanguage();
		final int languageLength = language.length();

		final String country = locale.getCountry();
		final int countryLength = country.length();

		final String variant = locale.getVariant();
		final int variantLength = variant.length();

		if (languageLength > 0 && countryLength > 0) {
			// LANGUAGE_COUNTRY

			StringBuffer temp = new StringBuffer(baseName);
			temp.append("_"); //$NON-NLS-1$
			temp.append(language);
			temp.append("_"); //$NON-NLS-1$
			temp.append(country);

			// LANGUAGE_COUNTRY_VARIANT

			StringBuffer variantTmp = new StringBuffer(temp.toString());
			if (variantLength > 0) {
				variantTmp.append("_"); //$NON-NLS-1$
				variantTmp.append(variant);

				variantTmp.append(".properties"); //$NON-NLS-1$
				bundleNames.add(variantTmp.toString());
			}

			temp.append(".properties"); //$NON-NLS-1$

			bundleNames.add(temp.toString());

		}

		if (languageLength > 0) {
			// LANGUAGE

			StringBuffer temp = new StringBuffer(baseName);
			temp.append("_"); //$NON-NLS-1$
			temp.append(language);
			temp.append(".properties"); //$NON-NLS-1$

			bundleNames.add(temp.toString());
		}

		// default.

		if (appendDefault)
			bundleNames.add(baseName + ".properties"); //$NON-NLS-1$

		return bundleNames;
	}

}
