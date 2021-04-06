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

package org.eclipse.birt.report.model.api.util;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.util.DataTypeConversionUtil;

/**
 * The utility class to convert values between parameter type choices and column
 * data type choices.
 */
public class DataTypeConvertUtil {

	/**
	 * Returns the corresponding parameter type choice with the given column data
	 * type. The column data type values are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>COLUMN_DATA_TYPE_BOOLEAN</code>
	 * <li><code>COLUMN_DATA_TYPE_DATETIME</code>
	 * <li><code>COLUMN_DATA_TYPE_DATE</code>
	 * <li><code>COLUMN_DATA_TYPE_TIME</code>
	 * <li><code>COLUMN_DATA_TYPE_DECIMAL</code>
	 * <li><code>COLUMN_DATA_TYPE_FLOAT</code>
	 * <li><code>COLUMN_DATA_TYPE_INTEGER</code>
	 * <li><code>COLUMN_DATA_TYPE_STRING</code>
	 * </ul>
	 * 
	 * 
	 * @param columnType the column data type
	 * @return the parameter type
	 */

	public static String converToParamType(String columnType) {
		if (StringUtil.isBlank(columnType))
			return null;

		if (DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals(columnType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals(columnType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_DATE.equals(columnType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_TIME.equals(columnType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals(columnType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals(columnType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals(columnType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals(columnType))
			return DataTypeConversionUtil.converToParamType(columnType);

		return columnType;
	}

	/**
	 * Returns the corresponding column data type choice with the given parameter
	 * type. The column data type values are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_BOOLEAN</code>
	 * <li><code>PARAM_TYPE_DATETIME</code>
	 * <li><code>PARAM_TYPE_DATE</code>
	 * <li><code>PARAM_TYPE_TIME</code>
	 * <li><code>PARAM_TYPE_DECIMAL</code>
	 * <li><code>PARAM_TYPE_FLOAT</code>
	 * <li><code>PARAM_TYPE_INTEGER</code>
	 * <li><code>PARAM_TYPE_STRING</code>
	 * </ul>
	 * 
	 * @param paramType the parameter type
	 * @return the column data type
	 */

	public static String converToColumnDataType(String paramType) {
		if (StringUtil.isBlank(paramType))
			return null;

		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(paramType)
				|| DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(paramType)
				|| DesignChoiceConstants.PARAM_TYPE_DATE.equals(paramType)
				|| DesignChoiceConstants.PARAM_TYPE_TIME.equals(paramType)
				|| DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(paramType)
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(paramType)
				|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(paramType)
				|| DesignChoiceConstants.PARAM_TYPE_STRING.equals(paramType))
			return DataTypeConversionUtil.converToColumnDataType(paramType);

		return paramType;
	}

}
