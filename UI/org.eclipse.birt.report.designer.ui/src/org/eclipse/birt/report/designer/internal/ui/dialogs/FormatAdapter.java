/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.text.Collator;
import java.util.TreeMap;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

import com.ibm.icu.util.ULocale;

/**
 * FormatAdapter
 */
public abstract class FormatAdapter {

	/** property: label of NONE */
	public static final String NONE = Messages.getString("FormatAdapter.DisplayName.None"); //$NON-NLS-1$

	/** property: tree map of local table */
	public final static TreeMap<String, ULocale> LOCALE_TABLE = new TreeMap<>(Collator.getInstance());

	static {
		// Initialize the locale mapping table
		ULocale[] locales = ULocale.getAvailableLocales();
		if (locales != null) {
			for (int i = 0; i < locales.length; i++) {
				ULocale locale = locales[i];
				if (locale != null) {
					LOCALE_TABLE.put(locale.getDisplayName(), locale);
				}
			}
		}
	}

	/**
	 * Get locale display name
	 *
	 * @param locale locale for the display name
	 * @return Return the locale display name
	 */
	public static String getLocaleDisplayName(ULocale locale) {
		if (locale == null) {
			return NONE;
		}
		return locale.getDisplayName();
	}

	/**
	 * Get locale display name array
	 *
	 * @return Return the locale display name array
	 */
	public static String[] getLocaleDisplayNames() {
		String[] oldNames = LOCALE_TABLE.keySet().toArray(new String[0]);
		String[] newNames = new String[oldNames.length + 1];
		newNames[0] = NONE;
		System.arraycopy(oldNames, 0, newNames, 1, oldNames.length);
		return newNames;
	}

	/**
	 * Get the locale of display name
	 *
	 * @param localeDisplayName the display name to get the locale
	 * @return Return the locale of the display name
	 */
	public static ULocale getLocaleByDisplayName(String localeDisplayName) {
		if (NONE.equals(localeDisplayName) || localeDisplayName == null) {
			return null;
		}
		return LOCALE_TABLE.get(localeDisplayName);
	}

	/**
	 * Get the matrix of choice like array
	 *
	 * @param structName  structure name
	 * @param popertyName property name
	 * @return Return the matrix of choice like array
	 */
	public static String[][] getChoiceArray(String structName, String popertyName) {
		IChoiceSet set = ChoiceSetFactory.getStructChoiceSet(structName, popertyName);
		IChoice[] choices = set.getChoices();

		String[][] ca;

		if (choices.length > 0) {
			ca = new String[choices.length][2];
			for (int i = 0, j = 0; i < choices.length; i++) {
				{
					ca[j][0] = choices[i].getDisplayName();
					ca[j][1] = choices[i].getName();
					j++;
				}
			}
		} else {
			ca = new String[0][0];
		}

		return ca;
	}

	/**
	 * Get the init choice array
	 *
	 * @return Return the init choice array
	 */
	public abstract String[][] initChoiceArray();

	/**
	 * Gets the format types for display names.
	 *
	 * @param locale locale of the format types
	 *
	 * @return Return the format types for display names.
	 */
	public abstract String[] getFormatTypes(ULocale locale);

	/**
	 * Get the index of category
	 *
	 * @param name name to get the category index
	 * @return Return the index of category
	 */
	public abstract int getIndexOfCategory(String name);

	/**
	 * Get the display name based on category
	 *
	 * @param category
	 * @return Return the display name based on category
	 */
	public abstract String getDisplayName4Category(String category);

	/**
	 * Gets the corresponding category for given display name.
	 *
	 * @param displayName display name
	 *
	 * @return Return the corresponding category for given display name
	 */
	public abstract String getCategory4DisplayName(String displayName);

	/**
	 * Gets the corresponding category for given display name locale based
	 *
	 * @param displayName display name
	 * @param locale      locale of display name
	 *
	 * @return Return the corresponding category for given display name locale based
	 */
	public abstract String getPattern4DisplayName(String displayName, ULocale locale);
}
