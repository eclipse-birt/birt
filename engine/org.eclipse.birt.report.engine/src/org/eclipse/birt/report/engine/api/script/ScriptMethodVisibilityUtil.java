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

package org.eclipse.birt.report.engine.api.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Util class that check methods in script interface are visible or not.
 * 
 */

public class ScriptMethodVisibilityUtil {
	/**
	 * map with non visible methods. key is class name. value is list each item is
	 * method name.
	 */

	private static Map nonVisibleMethodsMap = new HashMap();

	static {
		// TODO set un-visible method and class name.

	}

	/**
	 * Check script method is hide or not. if exist in map, that means need to be
	 * hidden, return true. else return false.
	 * 
	 * @param className  class name.for example :org.eclipse.birt.report.engine.api.
	 * @param methodName method name.for example: isHide.
	 * @return <code>true</code> if need to be hidden.else return
	 *         <code>false</code>.
	 */

	public static boolean isHide(String className, String methodName) {
		List methods = (List) nonVisibleMethodsMap.get(className);
		if (methods == null)
			return false;

		if (methods.contains(methodName))
			return true;

		return false;
	}
}
