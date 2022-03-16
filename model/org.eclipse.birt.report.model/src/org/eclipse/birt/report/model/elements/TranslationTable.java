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

package org.eclipse.birt.report.model.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Internal data structure to store a bunch of user-defined messages. The
 * translation table stores locale/message pairs. One message can be defined in
 * several translations, one translation per locale. When we need to check some
 * user-defined messages in a report design document, then we use the
 * TranslationTable.
 * <p>
 * Translations have the following rules:
 * <ul>
 * <li>Each translation has a resource key.</li>
 * <li>The resource-key names are unique within the design.</li>
 * <li>The translation locale names are unique within a resource.</li>
 * </ul>
 *
 * @see ReportDesign
 */

public final class TranslationTable implements Cloneable {

	/**
	 * Bunch of user Messages keyed by resourceKey. The structure of this map is
	 * like this:
	 *
	 * <pre>
	 *         resourceMap --
	 *                      |--[resourceKey:ArrayList&lt;Translation&gt;]
	 *                      |
	 *                      |
	 *                      |--[resourceKey:ArrayList&lt;Translation&gt;]
	 *                      |
	 *                      |
	 *                      |--[resourceKey:ArrayList&lt;Translation&gt;]
	 * </pre>
	 */

	Map<String, List<Translation>> resourceMap = null;

	/**
	 * Default constructor.
	 */

	public TranslationTable() {
		// stored in input order.

		resourceMap = new LinkedHashMap<>();
	}

	/**
	 * Adds a new translation entry. A report file can reference message IDs that
	 * are defined by the customers. One entry of <code>Translation</code>
	 * represents a translated message for a specific locale.
	 * <p>
	 *
	 * @param newTranslation new entry of <code>Translation</code> that are to be
	 *                       added to the table.
	 */

	public void add(Translation newTranslation) {
		assert newTranslation != null;

		String resourceKey = newTranslation.getResourceKey();

		List<Translation> translationList = resourceMap.get(resourceKey);

		if (translationList == null) {
			translationList = new ArrayList<>();
			resourceMap.put(resourceKey, translationList);
		}

		translationList.add(newTranslation);
	}

	/**
	 * Removes a Translation from the table.
	 * <p>
	 *
	 * @param trans new entry of <code>Translation</code> that are to be removed
	 *
	 * @return <code>true</code> if the translation table contains the given
	 *         translation.
	 *
	 */

	public boolean remove(Translation trans) {
		if (trans == null) {
			return false;
		}

		List<Translation> translationList = resourceMap.get(trans.getResourceKey());
		if (translationList == null) {
			return false;
		}

		return translationList.remove(trans);
	}

	/**
	 * Finds user defined messages for the current thread's locale.
	 *
	 * @param resourceKey Resource key of the user defined message.
	 * @return the corresponding locale-dependent messages. Return <code>null</code>
	 *         if resoueceKey is blank.
	 */

	public String getMessage(String resourceKey) {
		return getMessage(resourceKey, ThreadResources.getLocale());
	}

	/**
	 * Finds user defined messages for the given locale.
	 * <p>
	 * If matching message for the given locale(LANGUAGE_COUNTRY) is not found. It
	 * will try to match using only the language. If still not found, and there is a
	 * translation that was defined without specifying the locale, then the text of
	 * this translation will be returned. Or else, return <code>null</code>.
	 *
	 * @param resourceKey Resource key of the user defined message.
	 * @param theLocale   locale of a message.
	 * @return the corresponding locale-dependent messages. Return <code>null</code>
	 *         if resoueceKey is blank.
	 */

	public String getMessage(String resourceKey, ULocale theLocale) {
		if (StringUtil.isBlank(resourceKey)) {
			return null;
		}

		String locale = theLocale == null ? null : theLocale.toString();

		if (locale == null) {
			Translation trans = findTranslation(resourceKey, null);
			return trans != null ? trans.getText() : null;
		}

		List<Translation> translationList = resourceMap.get(resourceKey);
		if (translationList == null) {
			return null;
		}

		// en_US_VARIANT will be cut to en_US.

		if (locale.length() > 5) {
			locale = locale.substring(0, 5);
		}

		// First, match the whole word( LANGUAGE_COUNTRY or LANGUAGE ) or a null

		// If locale for "en" is defined ( <Translation/
		// locale="en"></Translation> )
		// return the text even if it is blank. This allows someone to create
		// entries for each language so that the translation person can just
		// "fill
		// in the blanks."

		Translation trans = findTranslation(resourceKey, locale);

		if (trans != null) {
			return trans.getText();
		}

		// Secondly, match the Language only: en, zh

		if (locale != null && locale.length() >= 2) {
			String language = locale.substring(0, 2);

			trans = findTranslation(resourceKey, language);

			if (trans != null) {
				return trans.getText();
			}

		}

		// Translation for the locale is not defined.
		// Return the text that is keyed by null(
		// <Translation>Foo</Translation>).
		// Translation without a locale or the locale is just a blank string is
		// keyed by null.

		trans = findTranslation(resourceKey, null);
		if (trans != null) {
			return trans.getText();
		}

		return null;
	}

	/**
	 * Finds a <code>Translation</code> by the message resource key and the locale.
	 * <p>
	 *
	 * @param resourceKey resourceKey of the user-defined message where the
	 *                    translation is defined in.
	 * @param locale      locale for the translation. Locale is in java-defined
	 *                    format( en, en-US, zh_CN, etc.)
	 * @return the <code>Translation</code> that matches.
	 */

	public Translation findTranslation(String resourceKey, String locale) {
		List<Translation> translationList = resourceMap.get(resourceKey);
		if (translationList == null) {
			return null;
		}

		for (Iterator<Translation> transIterator = translationList.iterator(); transIterator.hasNext();) {

			Translation trans = transIterator.next();

			if ((locale == null && trans.getLocale() == null) || (locale != null && locale.equalsIgnoreCase(trans.getLocale()))) {
				return trans;
			}
		}

		return null;

	}

	/**
	 * Returns if the specified translation is contained in the translation table.
	 *
	 * @param trans a given <code>Translation</code>
	 * @return <code>true</code> if the <code>Translation</code> is contained in the
	 *         translation table, return <code>false</code> otherwise.
	 */

	public boolean contains(Translation trans) {
		if (trans == null) {
			return false;
		}

		return findTranslation(trans.getResourceKey(), trans.getLocale()) != null;
	}

	/**
	 * Gets the whole collection of the translations defined for the report design.
	 * <p>
	 * Return null if there is no translation stored.
	 * <p>
	 *
	 * @return a list containing all the Translations. Return null if there is no
	 *         translation stored.
	 */

	public List<Translation> getTranslations() {
		ArrayList<Translation> translations = new ArrayList<>();

		Iterator<List<Translation>> iterator = resourceMap.values().iterator();

		while (iterator.hasNext()) {
			translations.addAll(iterator.next());
		}

		if (translations.isEmpty()) {
			return null;
		}

		return translations;
	}

	/**
	 * Returns the collection of translations defined for a specific message. The
	 * message is identified by its resourceKey.
	 * <p>
	 *
	 * @param resourceKey resource key for the message.
	 * @return a list containing all the Translations defined for the message.
	 */

	public List<Translation> getTranslations(String resourceKey) {
		return resourceMap.get(resourceKey);
	}

	/**
	 * Returns a string array containing all the resourceKeys defined for messages.
	 * <p>
	 *
	 * @return a string array containing all the resourcekeys defined for messages
	 *         return <code>null</code> if there is no messages stored.
	 */

	public String[] getResourceKeys() {
		Set<String> keySet = resourceMap.keySet();

		int size = keySet.size();
		if (size == 0) {
			return null;
		}

		String[] keys = new String[size];
		keySet.toArray(keys);

		return keys;
	}

	/**
	 * Makes a clone of this translation. The cloned translation contains a copy of
	 * the translation message map which in the original one.
	 *
	 * @return the cloned translation.
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		TranslationTable table = (TranslationTable) super.clone();
		table.resourceMap = new LinkedHashMap<>();

		for (Iterator<List<Translation>> it = resourceMap.values().iterator(); it.hasNext();) {
			List<Translation> transList = it.next();
			for (int i = 0; i < transList.size(); i++) {
				Translation trans = transList.get(i);
				table.add((Translation) trans.clone());
			}

		}
		return table;
	}
}
