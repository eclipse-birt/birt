/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.i18n;

import com.ibm.icu.text.MessageFormat;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.ibm.icu.util.UResourceBundle;

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

public class JdbcResourceHandle {

	/**
	 * The actual resource bundle. The implementation assumes that Java will use a
	 * PropertyResourceBundle to access our files.
	 */

	protected UResourceBundle resources;

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

	public JdbcResourceHandle(ULocale locale) {
		String className = this.getClass().getName();
		String bundleName = ""; //$NON-NLS-1$

		// Create the base message file name formatted like a Java class.
		// The Java class loader will search for the file using the same
		// algorithm used to find classes.

		int index = className.lastIndexOf('.');
		if (index > -1) {
			// e.g: "org.eclipse.birt.report.model.util.Test"

			bundleName = className.substring(0, index) + "."; //$NON-NLS-1$
		}

		bundleName = bundleName + BUNDLE_NAME;
		if (locale == null)
			locale = ULocale.getDefault();
		resources = UResourceBundle.getBundleInstance(bundleName, locale.getName(), this.getClass().getClassLoader());
		assert resources != null : "ResourceBundle : " + BUNDLE_NAME + " for " + locale + " not found"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public JdbcResourceHandle(Locale locale) {
		this(ULocale.forLocale(locale));
	}

	/**
	 * Get a message given the message key. An assertion will be raised if the
	 * message key does not exist in the resource bundle.
	 * 
	 * @param key the message key
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString( String )
	 */

	public String getMessage(String key) {
		if (key == null)
			return null;
		try {
			return resources.getString(key);
		} catch (MissingResourceException e) {
			// It is a programming error to refer to a missing
			// message.
			assert false : key + " not found in resource bundle"; //$NON-NLS-1$
			return key;
		}
	}

	/**
	 * Get a message that has placeholders. An assertion will be raised if the
	 * message key does not exist in the resource bundle.
	 * 
	 * @param key       the message key
	 * @param arguments the set of arguments to be plugged into the message
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString( String )
	 * @see MessageFormat#format( String, Object[] )
	 */

	public String getMessage(String key, Object[] arguments) {
		String message = getMessage(key);
		return MessageFormat.format(message, arguments);
	}

	/**
	 * Returns the resource bundle for the current locale.
	 * 
	 * @return the resource bundle
	 * @see ResourceBundle
	 */

	public UResourceBundle getUResourceBundle() {
		return resources;
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public ResourceBundle getResourceBundle() {
		return (UResourceBundle) getUResourceBundle();
	}

}