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

package org.eclipse.birt.report.model.api.util;

import org.eclipse.birt.report.model.core.namespace.NameExecutor;

/**
 * Utility class to provide some methods about cube.
 */
public class CubeUtil {

	private final static int DIMENSION_INDEX = 0;
	private final static int LEVEL_INDEX = 1;

	/**
	 * Splits a full name of the level element to a <code>String</code> array of
	 * length 2. The first member of the array is the name of the dimension element
	 * and the second is the name of the level element.
	 * 
	 * @param levelName
	 * @return an string array.
	 */
	public static String[] splitLevelName(String levelName) {
		String[] results = new String[2];
		results[DIMENSION_INDEX] = null;
		results[LEVEL_INDEX] = null;

		// if level name is null, we should do nothing
		if (levelName != null) {
			int index = levelName.lastIndexOf(NameExecutor.NAME_SEPARATOR);
			if (index > -1) {
				String dimension = levelName.substring(0, index);
				String level = levelName.substring(index + 1);
				results[DIMENSION_INDEX] = dimension;
				results[LEVEL_INDEX] = level;
			} else {
				results[DIMENSION_INDEX] = null;
				results[LEVEL_INDEX] = levelName;
			}
		}

		return results;
	}

	/**
	 * Gets the full name of the level element.
	 * 
	 * @param dimensionName the dimension name
	 * @param levelName     the short level name
	 * @return the full level name
	 */
	public static String getFullLevelName(String dimensionName, String levelName) {
		dimensionName = StringUtil.trimString(dimensionName);
		levelName = StringUtil.trimString(levelName);

		if (StringUtil.isBlank(dimensionName))
			return levelName;
		if (StringUtil.isBlank(levelName))
			return null;
		return dimensionName + NameExecutor.NAME_SEPARATOR + levelName;
	}
}
