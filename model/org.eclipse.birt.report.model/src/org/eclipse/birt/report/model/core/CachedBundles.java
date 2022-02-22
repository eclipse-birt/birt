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

package org.eclipse.birt.report.model.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;

import org.eclipse.birt.report.model.util.ModelUtil;

/**
 *
 */

public class CachedBundles {

	/**
	 * The key is the message file name: baseName + locale information. The value is
	 * the LocaleResourceBundle.
	 * <p>
	 * The key can be:
	 * <ul>
	 * <li>baseName + "_" + language1 + "_" + country1 + "_" + variant1
	 * <li>baseName + "_" + language1 + "_" + country1
	 * <li>baseName + "_" + language1
	 * <li>baseName
	 * </ul>
	 */

	private Map<String, LocaleResourceBundle> bundles;

	/**
	 * Default constructor.
	 */

	CachedBundles() {

	}

	/**
	 * Finds cached messages for the given fileName.
	 *
	 * @param fileName    the message file name
	 * @param resourceKey Resource key of the user defined message.
	 *
	 * @return the corresponding locale-dependent messages.
	 */

	String getMessage(String fileName, String resourceKey) {
		if (bundles == null) {
			return null;
		}

		LocaleResourceBundle tmpBundle = bundles.get(fileName);
		if (tmpBundle == null) {
			return null;
		}

		return tmpBundle.getMessage(resourceKey);

	}

	/**
	 * Return a list of cached message keys. The list returned contains no duplicate
	 * keys.
	 *
	 * @param fileName the given message file name
	 * @return a list of cached message keys.
	 *
	 */

	Set<String> getMessageKeys(String fileName) {
		if (bundles == null) {
			return null;
		}
		LocaleResourceBundle tmpBundle = bundles.get(fileName);
		if (tmpBundle == null) {
			return null;
		}

		return tmpBundle.getMessageKeys();

	}

	/**
	 * Checks whether the given message file has been cached.
	 *
	 * @param fileName the message file name
	 * @return <code>true</code> if the message file has been cached. Otherwise
	 *         <code>false</code>.
	 */

	boolean isCached(String fileName) {
		if (bundles == null) {
			return false;
		}

		return bundles.containsKey(fileName);
	}

	/**
	 * Saves the given message file URL in JDK resource bundle format.
	 *
	 * @param fileName  the message file name
	 * @param bundleURL the message file URL. It can be <code>null</code>. In such
	 *                  case, the file doesn't exist.
	 */

	void addCachedBundle(String fileName, URL bundleURL) {
		assert fileName != null;

		// the initial capacity is 8.

		if (bundles == null) {
			bundles = new HashMap<>(ModelUtil.MAP_CAPACITY_LOW);
		}
		if (bundleURL == null) {
			bundles.put(fileName, new LocaleResourceBundle(null));
		} else {
			bundles.put(fileName, new LocaleResourceBundle(populateBundle(bundleURL)));
		}
	}

	/**
	 * Populates a <code>ResourceBundle</code> for a input file.
	 *
	 * @param file a file binds to a message file.
	 * @return A <code>ResourceBundle</code> for a input file, return
	 *         <code>null</code> if the file doesn't exist or any exception occurred
	 *         during I/O reading.
	 */

	public static PropertyResourceBundle populateBundle(URL bundleURL) {
		InputStream is = null;
		try {
			if (bundleURL == null) {
				return null;
			}
			is = bundleURL.openStream();
			PropertyResourceBundle bundle = new PropertyResourceBundle(is);
			is.close();
			is = null;
			return bundle;
		} catch (IOException e) {
			// just ignore.
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e1) {
					is = null;
					// ignore.
				}
			}
		}

		return null;
	}

	/**
	 * The internal resource bundle. Each bundle maps to a special message file with
	 * the given locale.
	 *
	 */

	private static class LocaleResourceBundle {

		private PropertyResourceBundle bundle;

		LocaleResourceBundle(PropertyResourceBundle bundle) {
			this.bundle = bundle;
		}

		private String getMessage(String resourceKey) {
			if (bundle == null) {
				return null;
			}

			String translation = (String) bundle.handleGetObject(resourceKey);
			return translation;
		}

		private Set<String> getMessageKeys() {
			if (bundle == null) {
				return Collections.emptySet();
			}

			Set<String> keys = new LinkedHashSet<>();
			Enumeration<String> enumeration = bundle.getKeys();
			while (enumeration.hasMoreElements()) {
				keys.add(enumeration.nextElement());
			}

			return keys;
		}
	}
}
