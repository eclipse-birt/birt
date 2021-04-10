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

package org.eclipse.birt.report.model.adapter.oda.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * The utility class to create a unique name for result set column.
 * 
 */
public class IdentifierUtility {

	private static final char RENAME_SEPARATOR = '_';
	private static final String UNNAME_PREFIX = "UNNAMED"; //$NON-NLS-1$

	private static final String PARAM_PREFIX = "param" + RENAME_SEPARATOR; //$NON-NLS-1$

	/**
	 * Get a uniqueName for columnName
	 * 
	 * @param orgColumnNameSet the old column name set
	 * @param newColumnNameSet the column name set
	 * @param columnNativeName the column native name
	 * @param index            the index
	 * @return the unique column name
	 */

	public static String getUniqueColumnName(Set orgColumnNameSet, Set newColumnNameSet, String columnNativeName,
			int index) {
		String newColumnName = null;
		if (columnNativeName == null || columnNativeName.trim().length() == 0
				|| newColumnNameSet.contains(columnNativeName)) {
			// name conflict or no name,give this column a unique name
			StringBuffer columnName = new StringBuffer();

			if (columnNativeName == null || columnNativeName.trim().length() == 0)
				columnName.append(UNNAME_PREFIX);
			else
				columnName.append(columnNativeName);

			columnName.append(RENAME_SEPARATOR);
			columnName.append(index + 1);

			int i = 1;
			while (orgColumnNameSet.contains(newColumnName) || newColumnNameSet.contains(newColumnName)) {
				columnName.append(String.valueOf(RENAME_SEPARATOR) + i);
				i++;
			}
			newColumnName = columnName.toString();
		} else {
			newColumnName = columnNativeName;
		}
		return newColumnName;
	}

	/**
	 * Updates data set parameters with unique names. If the name is already good,
	 * uses it directly. If the name is empty or duplicates with others, creates a
	 * unique name.
	 * 
	 * @param parameters a list containing data set parameters
	 */

	public static final void updateParams2UniqueName(List parameters) {
		List existedNames = collectParameterNames(parameters);

		List newNames = new ArrayList();

		for (int i = 0; i < parameters.size(); i++) {
			OdaDataSetParameter param = (OdaDataSetParameter) parameters.get(i);
			String name = param.getName();
			if (StringUtil.isBlank(name) || newNames.contains(name)) {
				String prefix = StringUtil.isBlank(name) ? PARAM_PREFIX : name + RENAME_SEPARATOR;

				int n = 1;
				while (true) {
					name = prefix + n;

					if (!existedNames.contains(name) && !newNames.contains(name))
						break;
					n++;
				}

				param.setName(name);
			}

			newNames.add(name);
		}
	}

	/**
	 * Returns a listing contains unique names of data set parameters.
	 * 
	 * @param parameters a list containing data set parameters
	 * 
	 * @return a listing contains unique names. Not empty string or null.
	 */

	private static List collectParameterNames(List parameters) {
		List names = new ArrayList();
		for (int i = 0; i < parameters.size(); i++) {
			OdaDataSetParameter param = (OdaDataSetParameter) parameters.get(i);
			String name = param.getName();
			if (!StringUtil.isBlank(name) && !names.contains(name))
				names.add(name);
		}

		return names;
	}
}
