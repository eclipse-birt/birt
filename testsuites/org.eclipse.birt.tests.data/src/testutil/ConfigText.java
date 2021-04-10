/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package testutil;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This is a message class which read values of certain properties from a
 * properties file
 */

public class ConfigText {

	private static final String BUNDLE_NAME = "testutil.messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private static String tableNameString = ConfigText.createRandomString();

	private ConfigText() {
	}

	/**
	 * Get the value from Resource bundle
	 * 
	 * @param key
	 * @return kep mapped string
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key).replaceAll(RESOURCE_BUNDLE.getString("TableNameParameter"),
					tableNameString);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Create a dynamic random string for table name
	 * 
	 * @return randomString
	 */
	private static String createRandomString() {
		char[] temp = Long.toString(System.currentTimeMillis()).toCharArray();
		char[] array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
		for (int i = 0; i < temp.length; i++) {
			temp[i] = array[Integer.parseInt(String.valueOf(temp[i]))];
		}
		return String.copyValueOf(temp);
	}
}