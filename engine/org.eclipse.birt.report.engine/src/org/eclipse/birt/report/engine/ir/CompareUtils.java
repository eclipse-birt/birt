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

package org.eclipse.birt.report.engine.ir;

/**
 * Compare utility used to compare two object.
 * 
 * This class is used by Style to compare two style properties.
 * 
 */
class CompareUtils {

	/**
	 * compare two object. Note: if both object is null, the method will return true
	 * 
	 * @param obj1 object 1
	 * @param obj2 object 2
	 * @return return true same (or both null), false otherwise.
	 */
	static boolean isEquals(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 != null) {
			return obj1.equals(obj2);
		}
		return false;
	}
}
