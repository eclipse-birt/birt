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

package org.eclipse.birt.report.model.extension;

import java.util.Locale;
import java.util.MissingResourceException;

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IResourceBundleProvider;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Represents the default implementation for <code>IMessages</code>. This
 * implementation takes the instance of <code>ThreadResources</code> or
 * <code>IResourceBundleProvider</code>.
 */

public class DefaultMessages implements IMessages {

	private ThreadResources resources;

	private IResourceBundleProvider provider;

	/**
	 * Constructor with thread resources, which specified by the class loader and
	 * base name of resource bundle.
	 *
	 * @param resources thread resources instance
	 */

	public DefaultMessages(ThreadResources resources) {
		this.resources = resources;
	}

	/**
	 * Constructor with the resource bundle provider, which provides the resource
	 * bundle with the given locale.
	 *
	 * @param provider the resource bundle provider
	 */

	public DefaultMessages(IResourceBundleProvider provider) {
		this.provider = provider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IMessages#getMessage(java.lang.
	 * String, java.util.Locale)
	 */

	@Override
	public String getMessage(String key, ULocale locale) {
		if (provider != null) {
			UResourceBundle resourceBundle = provider.getResourceBundle(locale);
			if (resourceBundle != null) {
				try {
					String message = resourceBundle.getString(key);
					if (message != null) {
						return message;
					}
				} catch (MissingResourceException e) {
					// Do nothing.
				}
			}
		}

		if (resources != null) {
			String message = resources.getMessage(key);
			if (message != null) {
				return message;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IMessages#getMessage(java.lang.
	 * String, java.util.Locale)
	 */

	@Override
	public String getMessage(String key, Locale locale) {
		return getMessage(key, ULocale.forLocale(locale));
	}
}
