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

package org.eclipse.birt.report.resource;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.ibm.icu.text.MessageFormat;

/**
 * Class that handle externalized string used by the Birt viewer.
 * <p>
 */
public class BirtResources {
	private static String BACKSLASH = "\\";

	/**
	 * List of resource bundles keyed by the locale, which is cached for the whole
	 * application.
	 */

	private static Map resourceMap = new HashMap();

	/**
	 * Thread-local variable for the current thread.
	 */

	private static ThreadLocal threadLocal = new ThreadLocal();

	/**
	 * Sets the locale of current user-thread. This method should be called before
	 * access to any localized message. If the locale is <code>null</code>, the
	 * default locale will be set.
	 *
	 * @param locale locale of the current thread.
	 */

	public static void setLocale(Locale locale) {
		if (locale == null) {
			threadLocal.set(Locale.getDefault());
		} else {
			threadLocal.set(locale);
		}
	}

	/**
	 * Gets the locale of current user-thread.
	 *
	 * @return the locale of the current thread.
	 */

	public static Locale getLocale() {
		Locale locale = (Locale) threadLocal.get();
		if (locale == null) {
			locale = Locale.getDefault();
		}
		return locale;
	}

	/**
	 * Gets the localized message with the resource key.
	 *
	 * @param key the resource key
	 * @return the localized message for that key. Returns the key itself if the
	 *         message was not found.
	 */

	public static String getMessage(String key) {
		ViewerResourceHandle resourceHandle = getResourceHandle();
		if (resourceHandle != null) {
			return resourceHandle.getMessage(key);
		}

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

	public static String getMessage(String key, Object[] arguments) {
		ViewerResourceHandle resourceHandle = getResourceHandle();
		if (resourceHandle != null) {
			return resourceHandle.getMessage(key, arguments);
		}

		return key;
	}

	/**
	 * Returns an escaped version of getMessage.
	 *
	 * @see #getMessage(String)
	 * @see #makeJavaScriptString(String)
	 */
	public static String getJavaScriptMessage(String key) {
		return makeJavaScriptString(getMessage(key));
	}

	/**
	 * Returns an escaped version of getMessage.
	 *
	 * @see #getMessage(String, Object[])
	 * @see #makeJavaScriptString(String)
	 */
	public static String getJavaScriptMessage(String key, Object[] arguments) {
		return makeJavaScriptString(getMessage(key, arguments));
	}

	/**
	 * Returns the text from getMessage(), where all double-quotes are replaced by
	 * entities.
	 *
	 * @see #getMessage(String)
	 * @see #makeJavaScriptString(String)
	 */
	public static String getHtmlMessage(String key) {
		return makeHtmlString(getMessage(key));
	}

	/**
	 * Returns the text from getMessage(), where all double-quotes are replaced by
	 * entities.
	 *
	 * @see #getMessage(String, Object[])
	 * @see #makeJavaScriptString(String)
	 */
	public static String getHtmlMessage(String key, Object[] arguments) {
		return makeHtmlString(getMessage(key, arguments));
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 *
	 * @param key resource key
	 * @return resource string
	 * @deprecated see getMessage( String key )
	 */
	@Deprecated
	public static String getString(String key) {
		return getMessage(key);
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 *
	 * @param key       resource key
	 * @param arguments list of arguments
	 * @return locale string
	 * @deprecated the getMessage( String key ) will returnt he formated message.
	 */
	@Deprecated
	public static String getFormattedString(String key, Object[] arguments) {
		return MessageFormat.format(getString(key), arguments);
	}

	/**
	 * Returns the resource handle with the locale of this thread. The resource
	 * handle will be cached.
	 *
	 * @return the resource handle with the locale of this thread
	 */

	public static ViewerResourceHandle getResourceHandle() {
		Locale locale = getLocale();

		ViewerResourceHandle resourceHandle = (ViewerResourceHandle) resourceMap.get(locale);
		if (resourceHandle != null) {
			return resourceHandle;
		}

		synchronized (resourceMap) {
			if (resourceMap.get(locale) != null) {
				return (ViewerResourceHandle) resourceMap.get(locale);
			}

			resourceHandle = new ViewerResourceHandle(locale);
			resourceMap.put(locale, resourceHandle);
		}

		return resourceHandle;
	}

	/**
	 * Escapes a string to make it usable in JavaScript.
	 *
	 * @param s input string
	 * @return escaped string, without quotes
	 */
	public static String makeJavaScriptString(String s) {
		StringBuilder output = new StringBuilder(s.length());
		CharacterIterator it = new StringCharacterIterator(s);
		for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
			switch (c) {
			// backspace
			case 0x08:
				output.append(BACKSLASH + "b");
				break;
			// tab
			case 0x09:
				output.append(BACKSLASH + "t");
				break;
			// newline
			case 0x0A:
				output.append(BACKSLASH + "n");
				break;
			// form feed
			case 0x0C:
				output.append(BACKSLASH + "f");
				break;
			// carriage return
			case 0x0D:
				output.append(BACKSLASH + "r");
				break;
			// single quote
			case 0x27:
				// double quote
			case 0x22:
				// slash
			case 0x2F:
				// backslash
			case 0x5C:
				output.append(BACKSLASH + c);
				break;
			// string ranges
			default:
				output.append(c);
			}
		}
		return output.toString();
	}

	/**
	 * Converts the double-quotes in the given string in HTML entities.
	 *
	 * @param s input string
	 * @return converted string
	 */
	public static String makeHtmlString(String s) {
		return s.replace("\"", "&quot;");
	}

}
