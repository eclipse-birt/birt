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

package org.eclipse.birt.report.model.i18n;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import org.eclipse.birt.report.model.util.ModelUtil;

import com.ibm.icu.util.ULocale;

/**
 * Represents the set of resource bundle with the specific base name of resource
 * bundle. This set of resource bundle is associated with the current thread.
 * The locale is kept in one static thread-local variable,
 * {@link java.lang.ThreadLocal}. When getting message, the actual localized
 * message is from the resource bundle for the kept locale. The application
 * calls <code>setLocale(Locale)</code> to set the locale for the current
 * thread, before calling the <code>getMessage()</code> methods.
 * 
 * @see ModelResourceHandle
 */

public class ThreadResources {

	/**
	 * List of resource bundles keyed by the locale, which is cached for the whole
	 * application.
	 */

	private Map<ULocale, ModelResourceHandle> resourceMap = new HashMap<ULocale, ModelResourceHandle>(
			ModelUtil.MAP_CAPACITY_LOW);

	/**
	 * Thread-local variable for the current thread.
	 */

	private static ThreadLocal threadLocal = new ThreadLocal();

	/**
	 * Constructs the thread resources with the given class loader and base name.
	 * The <code>theClass</code> provides the class loader for loading resource
	 * bundle, and the <code>baseName</code> is the full qualified class name for
	 * resource bundle. If the message file is located in
	 * "org.eclipse.birt.report.model.message.properties", the base name is
	 * "org.eclipse.birt.report.model.message".
	 * 
	 * @param classLoader the class loader for loading the given resource bundle
	 * @param baseName    the base name of resource bundle, the full qualified class
	 *                    name
	 * @throws MissingResourceException if no resource bundle for the specified base
	 *                                  name can be found.
	 */

	public ThreadResources() {
	}

	/**
	 * Sets the locale of current user-thread. This method should be called before
	 * access to any localized message. If the locale is <code>null</code>, the
	 * default locale will be set.
	 * 
	 * @param locale locale of the current thread.
	 */

	public static void setLocale(ULocale locale) {
		if (locale == null)
			threadLocal.set(ULocale.getDefault());
		else
			threadLocal.set(locale);
	}

	/**
	 * Gets the locale of current user-thread.
	 * 
	 * @return the locale of the current thread.
	 */

	public static ULocale getLocale() {
		ULocale locale = (ULocale) threadLocal.get();
		if (locale == null)
			locale = ULocale.getDefault();
		return locale;
	}

	/**
	 * Gets the localized message with the resource key.
	 * 
	 * @param key the resource key
	 * @return the localized message for that key. Returns the key itself if the
	 *         message was not found.
	 */

	public String getMessage(String key) {
		return getMessage(key, getLocale());
	}

	/**
	 * Gets the localized message with the resource key.
	 * 
	 * @param key    the resource key
	 * @param locale the locale
	 * @return the localized message for that key. Returns the key itself if the
	 *         message was not found.
	 */

	public String getMessage(String key, ULocale locale) {
		ModelResourceHandle resourceHandle = getResourceHandle(locale);
		if (resourceHandle != null)
			return resourceHandle.getMessage(key);

		return key;
	}

	/**
	 * Gets the localized message with the resource key and arguments.
	 * 
	 * @param key       the resource key
	 * @param arguments the set of arguments to place the place-holder of message
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 */

	public String getMessage(String key, Object[] arguments) {
		ModelResourceHandle resourceHandle = getResourceHandle(getLocale());
		if (resourceHandle != null)
			return resourceHandle.getMessage(key, arguments);

		return key;
	}

	/**
	 * Returns the resource handle with the locale of this thread. The resource
	 * handle will be cached.
	 * 
	 * @return the resource handle with the locale of this thread
	 */

	private ModelResourceHandle getResourceHandle(ULocale locale) {
		ModelResourceHandle resourceHandle = (ModelResourceHandle) resourceMap.get(locale);
		if (resourceHandle != null)
			return resourceHandle;

		synchronized (resourceMap) {
			resourceHandle = (ModelResourceHandle) resourceMap.get(locale);
			if (resourceHandle != null)
				return resourceHandle;

			resourceHandle = new ModelResourceHandle(locale);
			resourceMap.put(locale, resourceHandle);
		}

		return resourceHandle;
	}

}