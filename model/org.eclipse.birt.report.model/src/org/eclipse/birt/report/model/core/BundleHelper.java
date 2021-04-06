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

import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.IResourceLocator;

import com.ibm.icu.util.ULocale;

/**
 * Helper class to deal with user-defined message files. The message files is
 * stored out of the design.
 * 
 */

public class BundleHelper extends ResourceHelper {

	/**
	 * the module
	 */

	private Module module = null;

	/**
	 * Private constructor. Constructs a helper given the message folder and common
	 * base name of the message bundles.
	 * 
	 * @param module   the module
	 * @param baseName base name of the resource bundle. The name is a common base
	 *                 name
	 * 
	 */

	private BundleHelper(Module module, String baseName) {
		super(baseName);
		this.module = module;
	}

	/**
	 * Gets a helper to deal with a bundle of message files.
	 * 
	 * @param module   the module
	 * @param baseName base name of the resource bundle. The name is a common base
	 *                 name
	 * @return a correspondent helper instance. Return <code>null</code> if the
	 *         <code>msgFolder</code> is null or not a directory.
	 * 
	 */

	public static BundleHelper getHelper(Module module, String baseName) {
		assert module != null;
		return new BundleHelper(module, baseName);
	}

	/**
	 * Return a collection of user-defined message keys in the referenced message
	 * files for the given locale. If the input <code>locale</code> is
	 * <code>null</code>, the locale for the current thread will be used instead.
	 * 
	 * @param locale locale to use when finding the bundles.
	 * @return a list of user-defined message keys in the referenced message files.
	 */

	public Collection getMessageKeys(ULocale locale) {
		CachedBundles moduleBundle = module.getResourceBundle();

		// create a dummy resource bundle to keep codes same

		if (moduleBundle == null)
			moduleBundle = new CachedBundles();

		Set<String> keys = new LinkedHashSet<String>();
		List<String> bundleNames = getMessageFilenames(locale);
		for (int i = 0; i < bundleNames.size(); i++) {
			String tmpName = bundleNames.get(i);
			if (!moduleBundle.isCached(tmpName)) {
				// load and search again

				moduleBundle.addCachedBundle(tmpName, findBundle(tmpName));
			}

			keys.addAll(moduleBundle.getMessageKeys(tmpName));

		}
		return keys;
	}

	/**
	 * Look up a user-defined message for the given locale in the referenced message
	 * files, the search uses a reduced form of Java locale-driven search algorithm:
	 * Language&Country, language, default.
	 * 
	 * @param resourceKey Resource key of the user defined message.
	 * @param locale      locale of message, if the input <code>locale</code> is
	 *                    <code>null</code>, the locale for the current thread will
	 *                    be used instead.
	 * @return the corresponding locale-dependent messages. Return <code>""</code>
	 *         if resoueceKey is blank. Return <code>null</code> if the message is
	 *         not found.
	 * 
	 */

	public String getMessage(String resourceKey, ULocale locale) {
		CachedBundles moduleBundle = module.getResourceBundle();

		// create a dummy resource bundle to keep codes same

		if (moduleBundle == null)
			moduleBundle = new CachedBundles();

		List<String> bundleNames = getMessageFilenames(locale);
		for (int i = 0; i < bundleNames.size(); i++) {
			String tmpName = bundleNames.get(i);
			if (!moduleBundle.isCached(tmpName)) {
				// load and search again

				moduleBundle.addCachedBundle(tmpName, findBundle(tmpName));
			}

			String translation = moduleBundle.getMessage(tmpName, resourceKey);
			if (translation != null)
				return translation;
		}

		return null;
	}

	private URL findBundle(String fileName) {
		assert fileName != null;

		return module.findResource(fileName, IResourceLocator.OTHERS);
	}

}