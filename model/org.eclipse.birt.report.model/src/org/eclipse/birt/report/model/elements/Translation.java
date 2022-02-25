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

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.TranslationHandle;
import org.eclipse.birt.report.model.core.Module;

/**
 * This inner class represents an entry for the user-defined message. One
 * <code>Translation</code> entry concerns about the message it is defined for,
 * which is specified by the <code>resourceKey</code>. And for that message,
 * user can specify several translations for the text, one translation per
 * locale. The <code>text</code> stores the translated text for that locale.
 */

public class Translation implements Cloneable {

	/**
	 * resource key for the Message which this translation stored in.
	 */

	private String resourceKey = null;

	/**
	 * Locale for the translation.
	 */

	private String locale = null;

	/**
	 * Translated text for the locale.
	 */

	private String text = null;

	/**
	 * Cached handle to deal with the translation.
	 */

	private TranslationHandle handle = null;

	/**
	 * Constructs the translation with a resource key, locale and the text.
	 * <p>
	 *
	 * @param resourceKey the resource key for the Message which this translation
	 *                    stored in
	 * @param locale      the locale for the translation. Locale should be in
	 *                    java-defined format( en, en-US, zh_CN, etc.)
	 * @param text        the translated text for the locale
	 */

	public Translation(String resourceKey, String locale, String text) {
		this.resourceKey = resourceKey;
		this.locale = locale;
		this.text = text;
	}

	/**
	 * Constructs the translation with the resource key and the locale.
	 * <p>
	 *
	 * @param resourceKey the resource key for the Message which this translation
	 *                    stored in
	 * @param locale      Locale for the translation. Locale should be in
	 *                    java-defined format( en, en-US, zh_CN, etc.)
	 *
	 */

	public Translation(String resourceKey, String locale) {
		this.resourceKey = resourceKey;
		this.locale = locale;

	}

	/**
	 * Returns the resourceKey of this translation.
	 * <p>
	 *
	 * @return resourceKey of this translation.
	 */

	public String getResourceKey() {
		return resourceKey;
	}

	/**
	 * Returns the locale of this translation.
	 * <p>
	 *
	 * @return locale of this translation.
	 */

	public String getLocale() {
		return locale;
	}

	/**
	 * Returns translated text for this entry.
	 * <p>
	 *
	 * @return translated text related to the locale.
	 */

	public String getText() {
		return text;
	}

	/**
	 * Sets resourceKey to the Translation.
	 * <p>
	 *
	 * @param resourceKey resourceKey for the translation.
	 */

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	/**
	 * Sets locale for the Translation. Locale should follow standard java locale
	 * format, e.g: en_US, zh_CN...
	 * <p>
	 *
	 * @param locale locale of the Translation.
	 */

	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Sets the translation text for the translation.
	 * <p>
	 *
	 * @param text translated text for the message.
	 */

	public void setText(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Return a handle to deal with the translation.
	 *
	 * @param design module
	 * @return handle to deal with the translation.
	 */

	public TranslationHandle handle(Module module) {
		if (handle == null) {
			handle = new TranslationHandle((ModuleHandle) module.getHandle(module), this);
		}

		return handle;
	}

}
