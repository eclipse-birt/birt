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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;

import com.ibm.icu.util.ULocale;

/**
 * Parameter format utility class.
 */
public class ParameterUtil {

	private static final Logger logger = Logger.getLogger(ParameterUtil.class.getName());

	public static final String LABEL_NULL = Messages.getString("ParameterDialog.Label.Null"); //$NON-NLS-1$
	public static final String STANDARD_DATE_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss a"; //$NON-NLS-1$

	private ParameterUtil() {
	}

	/**
	 * Checks if is custom format category.
	 *
	 * @param formatCategory
	 * @return <code>true</code> if is custom type. else return <code>false</code>.
	 */
	public static boolean isCustomCategory(String formatCategory) {
		if (DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM.equals(formatCategory)
				|| DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM.equals(formatCategory)
				|| DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM.equals(formatCategory)
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_CUSTOM.equals(formatCategory)
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_CUSTOM.equals(formatCategory)) {
			return true;
		}
		return false;
	}

	public static String format(String value, String dataType, String formatCategory, String formatPattern,
			boolean canbeNull) {
		if (canbeNull && value == null) {
			return LABEL_NULL;
		}

		if (StringUtil.isBlank(value) || formatCategory == null) {
			return value;
		}

		try {
			String pattern = formatPattern;
			if (formatPattern == null) {
				if (ParameterUtil.isCustomCategory(formatCategory)) {
					return value;
				}
				pattern = formatCategory;
			}

			if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(dataType)) {
				value = convertToStandardFormat(DataTypeUtil.toDate(value));
			} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(dataType)) {
				value = convertToStandardFormat(DataTypeUtil.toSqlDate(value));
			} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(dataType)) {
				value = convertToStandardFormat(DataTypeUtil.toSqlTime(value));
			} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(dataType)) {
				value = new NumberFormatter(pattern).format(DataTypeUtil.toDouble(value).doubleValue());
			} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(dataType)) {
				value = new NumberFormatter(pattern).format(DataTypeUtil.toBigDecimal(value));
			} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(dataType)) {
				value = new StringFormatter(pattern).format(value);
			}
		} catch (BirtException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return value;
	}

	public static String convertToStandardFormat(Date date) {
		if (date == null) {
			return null;
		}
		return new DateFormatter(STANDARD_DATE_TIME_PATTERN, ULocale.getDefault()).format(date);
	}

	/**
	 * Formats value with pattern or category.
	 *
	 * @param handle
	 * @param inputStr
	 * @return formatted value.
	 */

	public static String format(ParameterHandle handle, String value) throws BirtException {
		if (handle instanceof ScalarParameterHandle) {
			ScalarParameterHandle sphandle = (ScalarParameterHandle) handle;

			value = format(value, sphandle.getDataType(), sphandle.getCategory(), sphandle.getPattern(),
					!sphandle.isRequired());

		}

		return value;
	}

	public static Object convert(Object value, String dataType) throws BirtException {
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(dataType)) {
			return DataTypeUtil.toBoolean(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(dataType)) {
			return DataTypeUtil.toDate(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(dataType)) {
			return DataTypeUtil.toSqlDate(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(dataType)) {
			return DataTypeUtil.toSqlTime(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(dataType)) {
			return DataTypeUtil.toBigDecimal(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(dataType)) {
			return DataTypeUtil.toDouble(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(dataType)) {
			return DataTypeUtil.toString(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(dataType)) {
			return DataTypeUtil.toInteger(value);
		}
		return value;
	}

}
