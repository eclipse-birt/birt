/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.util;

/**
 * Provides data convert and format services
 * 
 */
public class DataUtil {

	public final static String BLANK_STRING = ""; //$NON-NLS-1$

	/**
	 * Convert Object to String
	 * 
	 * @param object
	 * @return String
	 */
	public static String getString(Object object, boolean allowNull) {
		if (object == null) {
			if (allowNull)
				return null;
			else
				return ""; //$NON-NLS-1$
		}

		return object.toString();
	}

	/**
	 * Convert Object to boolean
	 * 
	 * @param object
	 * @return boolean
	 */
	public static boolean getBoolean(Object object) {
		if (object == null)
			return false;

		if (object instanceof Boolean)
			return ((Boolean) object).booleanValue();

		return Boolean.valueOf(object.toString()).booleanValue();
	}

	/**
	 * Convert Object to int
	 * 
	 * @param obj
	 * @return
	 */
	public static int getInt(Object obj) {
		int num = -1;

		try {
			if (obj != null)
				num = Integer.parseInt(obj.toString());
		} catch (Exception e) {
			num = -1;
		}

		return num;
	}

	/**
	 * Trim String
	 * 
	 * @param plain
	 * @return
	 */
	public static String trim(String plain) {
		if (plain == null)
			return null;

		return plain.trim();
	}

	/**
	 * Returns number Setting value
	 * 
	 * @param value
	 * @return
	 */
	public static String getNumberSetting(String value) {
		int num = DataUtil.getInt(value);
		if (num >= 0)
			return BLANK_STRING + num;
		else
			return BLANK_STRING;
	}
}
