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

	public static final String NONE = Messages.getString("FormatAdapter.DisplayName.None"); //$NON-NLS-1$

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

	public static String getLocaleDisplayName(ULocale locale) {
		if (locale == null) {
			return NONE;
		}
		return locale.getDisplayName();
	}

	public static String[] getLocaleDisplayNames() {
		String[] oldNames = LOCALE_TABLE.keySet().toArray(new String[0]);
		String[] newNames = new String[oldNames.length + 1];
		newNames[0] = NONE;
		System.arraycopy(oldNames, 0, newNames, 1, oldNames.length);
		return newNames;
	}

	public static ULocale getLocaleByDisplayName(String localeDisplayName) {
		if (NONE.equals(localeDisplayName) || localeDisplayName == null) {
			return null;
		}
		return LOCALE_TABLE.get(localeDisplayName);
	}

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

	public abstract String[][] initChoiceArray();

	/**
	 * Gets the format types for display names.
	 */
	public abstract String[] getFormatTypes(ULocale locale);

	public abstract int getIndexOfCategory(String name);

	public abstract String getDisplayName4Category(String category);

	/**
	 * Gets the corresponding category for given display name.
	 */
	public abstract String getCategory4DisplayName(String displayName);

	public abstract String getPattern4DisplayName(String displayName, ULocale locale);
}
