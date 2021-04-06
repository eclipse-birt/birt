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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.datatools.connectivity.oda.design.CustomData;

/**
 * The internal utility to compare two values.
 * 
 */

class CompareUtil {

	static boolean isEquals(CustomData value1, CustomData value2) {
		if (value1 == value2)
			return true;

		if (value1 == null || value2 == null)
			return false;

		if (equals(value1.getProviderId(), value2.getProviderId()))
			return equals(value1.getValue(), value2.getValue());
		return false;
	}

	/**
	 * Determines two given values are equal or not.
	 * 
	 * @param value1 value1
	 * @param value2 value2
	 * @return <code>true</code> if two values are equal. Otherwise
	 *         <code>false</code>.
	 */

	static boolean isEquals(Object value1, Object value2) {
		if (value1 instanceof CustomData) {
			if (value2 instanceof CustomData)
				return isEquals((CustomData) value1, (CustomData) value2);
			return false;
		}

		if (AdapterUtil.isNullExpression(value1))
			value1 = null;
		if (AdapterUtil.isNullExpression(value2))
			value2 = null;

		return equals(value1, value2);
	}

	/**
	 * Determines two given values are equal or not.
	 * 
	 * @param value1 value1
	 * @param value2 value2
	 * @return <code>true</code> if two values are equal. Otherwise
	 *         <code>false</code>.
	 */

	private static boolean equals(Object value1, Object value2) {
		// may be same string or both null.

		if (value1 == value2)
			return true;

		if (value1 == null || value2 == null)
			return false;

		assert value1 != null && value2 != null;

		if (value1.getClass() != value2.getClass())
			return false;

		if (Expression.class != value1.getClass()
				&& (!(value1 instanceof Comparable) || !(value2 instanceof Comparable)))
			return false;

		if (!value1.equals(value2))
			return false;

		return true;
	}

}
