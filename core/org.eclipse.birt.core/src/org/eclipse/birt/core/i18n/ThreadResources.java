/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.core.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;

/**
 * Provides access to a resource bundle associated with this thread. The
 * application calls <code>setThreadLocale</code> to set the locale for the
 * thread, then calls the <code>getMessage</code> methods.
 *
 * @see ResourceHandle
 */

public class ThreadResources {

	/**
	 * List of resource bundles keyed by the locale.
	 */

	private static Map resourceMap = new HashMap();

	/**
	 * List of resource bundles keyed by the current user-thread.
	 */

	private static ThreadLocal resources = new ThreadLocal();

	/**
	 * Set the locale of current user-thread. This method should be called before
	 * access to any localized message. Call with null to clear the thread locale.
	 *
	 * @param locale Locale of the current thread.
	 */

	public static void setLocale(ULocale locale) {
		ResourceHandle resourceHandle = null;
		if (locale != null) {
			synchronized (resourceMap) {
				resourceHandle = (ResourceHandle) resourceMap.get(locale);

				if (resourceHandle == null) {
					resourceHandle = new ResourceHandle(locale);
					resourceMap.put(locale, resourceHandle);
				}
			}
		}

		resources.set(resourceHandle);
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	@Deprecated
	public static void setLocale(Locale locale) {
		setLocale(ULocale.forLocale(locale));
	}

	/**
	 * Get the locale of current user-thread.
	 *
	 * @return Locale of the current thread.
	 */
	public static ULocale getULocale() {
		ResourceHandle handle = (ResourceHandle) resources.get();
		assert handle != null;
		return handle.getUResourceBundle().getULocale();
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	@Deprecated
	public static Locale getLocale() {
		return getULocale().toLocale();
	}

	/**
	 * Get a message given the message key. An assertion will be raised if the
	 * message key does not exist in the resource bundle. The locale must have
	 * previously been set for this thread.
	 *
	 * @param key the message key
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString( String )
	 * @see ResourceHandle#getMessage( String )
	 */

	public static String getMessage(String key) {
		ResourceHandle handle = (ResourceHandle) resources.get();
		assert handle != null;
		return handle.getMessage(key);
	}

	/**
	 * Get a message that has placeholders. An assertion will be raised if the
	 * message key does not exist in the resource bundle. The locale must have
	 * previously been set for this thread.
	 *
	 * @param key       the message key
	 * @param arguments the set of arguments to be plugged into the message
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString( String )
	 * @see MessageFormat#format( String, Object[] )
	 * @see ResourceHandle#getMessage( String, Object[] )
	 */

	public static String getMessage(String key, Object[] arguments) {
		ResourceHandle handle = (ResourceHandle) resources.get();
		assert handle != null;
		return handle.getMessage(key, arguments);
	}

}
