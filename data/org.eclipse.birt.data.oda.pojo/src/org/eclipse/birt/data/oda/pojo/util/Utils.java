
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.util;

import java.lang.reflect.Method;

import org.eclipse.birt.data.oda.pojo.api.Constants;

/**
 *
 */

public class Utils {
	/**
	 * Convert <code>filter</code> to a regular expression
	 *
	 * @param filter: ? for any char; * for any string
	 * @return
	 */
	public static String toRegexPattern(String filter) {
		StringBuilder pattern = new StringBuilder(".*"); //$NON-NLS-1$
		boolean isWaitingForEndQuote = false;
		for (int i = 0; i < filter.length(); i++) {
			char c = filter.charAt(i);
			if (c == '*' || c == '?') {
				if (isWaitingForEndQuote) {
					pattern.append("\\E"); //$NON-NLS-1$
					isWaitingForEndQuote = false;
				}
				String s = c == '*' ? ".*" : "."; //$NON-NLS-1$ //$NON-NLS-2$
				pattern.append(s);
			} else {
				if (!isWaitingForEndQuote) {
					pattern.append("\\Q"); //$NON-NLS-1$
					isWaitingForEndQuote = true;
				}
				pattern.append(c);
			}
		}
		if (isWaitingForEndQuote) {
			pattern.append("\\E"); //$NON-NLS-1$
		}
		return pattern.append(".*").toString(); //$NON-NLS-1$
	}

	public static boolean isPojoDataSetClass(Class c) {
		if (c == null) {
			return false;
		}
		try {
			Method nextMethod = c.getMethod(Constants.NEXT_METHOD_NAME, (Class[]) null);
			if (nextMethod.getReturnType().isPrimitive()) {
				return false;
			}
		} catch (SecurityException | NoSuchMethodException e) {
			return false;
		}
		return true;
	}
}
