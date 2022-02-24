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

import java.net.URL;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.birt.report.model.core.CachedBundles;
import org.eclipse.birt.report.model.core.ResourceHelper;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;

/**
 * Represents a set of resources for a given package and locale. This class will
 * associate with a user session. Each user session has a single locale. This
 * class assumes that the resources are in the same location as the class
 * itself, and are named "Messages.properties", "Messages_xx.properties", etc.
 * <p>
 * Once stable, the application will not access a message that does not exist.
 * To help get the system stable, this class raises an assertion if the message
 * key refers to a missing exception. The class then returns the message key
 * itself as the message.
 * <p>
 * This class primarily works with messages. It can be extended to work with
 * other resources as the need arises.
 * 
 * @see ThreadResources
 */

class ResourceHandle {

	protected final static ResourceHandle defaultBundle;

	static {
		defaultBundle = new ResourceHandle();
	}

	/**
	 * The actual resource bundle. The implementation assumes that Java will use a
	 * PropertyResourceBundle to access our files.
	 */

	protected PropertyResourceBundle resources;

	/**
	 * Name of the resource bundle.
	 */

	private final static String BUNDLE_NAME = "Messages"; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param locale the user's locale. If null, the default locale for the JVM will
	 *               be used.
	 */

	ResourceHandle() {
		ULocale emptyLocale = new ULocale("", "");
		String bundleName = BUNDLE_NAME;

		List<String> resourceFiles = ResourceHelper.getHelper(bundleName).getMessageFilenames(emptyLocale);

		URL fileURL = ResourceHandle.class.getResource(resourceFiles.get(0));
		assert fileURL != null;

		resources = CachedBundles.populateBundle(fileURL);
		assert resources != null;
	}

	/**
	 * Constructor.
	 * 
	 * @param locale the user's locale. If null, the default locale for the JVM will
	 *               be used.
	 */

	ResourceHandle(ULocale locale) {
		String bundleName = BUNDLE_NAME;
		if (locale == null)
			locale = ULocale.getDefault();

		List<String> resourceFiles = ResourceHelper.getHelper(bundleName).getMessageFilenames(locale, false);

		for (int i = 0; i < resourceFiles.size(); i++) {
			String tmpFileName = resourceFiles.get(i);
			URL fileURL = ResourceHandle.class.getResource(tmpFileName);
			if (fileURL == null)
				continue;

			PropertyResourceBundle tmpBundle = CachedBundles.populateBundle(fileURL);
			if (tmpBundle != null) {
				resources = tmpBundle;
				break;
			}
		}

		if (resources == null)
			resources = defaultBundle.resources;
	}

	/**
	 * Get a message given the message key. An assertion will be raised if the
	 * message key does not exist in the resource bundle.
	 * 
	 * @param key the message key
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString(String )
	 */

	public String getMessage(String key) {
		if (key == null)
			return null;

		String retMsg = null;
		try {
			retMsg = resources.getString(key);
		} catch (MissingResourceException e) {
			retMsg = null;
		}

		if (retMsg != null)
			return retMsg;

		try {
			retMsg = defaultBundle.resources.getString(key);
		} catch (MissingResourceException e) {

			// It is a programming error to refer to a missing
			// message.
			assert false : key + " not found in resource bundle"; //$NON-NLS-1$
			return key;
		}

		return retMsg;
	}

	/**
	 * Get a message that has placeholders. An assertion will be raised if the
	 * message key does not exist in the resource bundle.
	 * 
	 * @param key       the message key
	 * @param arguments the set of arguments to be plugged into the message
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString(String )
	 * @see MessageFormat#format(String, Object[] )
	 */

	public String getMessage(String key, Object[] arguments) {
		String message = getMessage(key);
		return MessageFormat.format(message, arguments);
	}
}
