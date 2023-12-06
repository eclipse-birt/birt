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

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.Date;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.ULocale;

/**
 * Format utility.
 *
 */
public class FormatUtil {

	/**
	 * Checks is custom.
	 *
	 * @param formatCategory
	 * @return <code>true</code> if is custom type. else return <code>false</code>.
	 */
	private static boolean isCustom(String formatCategory) {
		if (DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM.equals(formatCategory)
				|| DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM.equals(formatCategory)
				|| DesignChoiceConstants.DATETIME_FORMAT_TYPE_CUSTOM.equals(formatCategory)) {
			return true;
		}
		return false;
	}

	/**
	 * Formats value with pattern or category.
	 *
	 * @param handle
	 * @param inputStr
	 * @return formatted value.
	 */

	public static String format(ParameterHandle handle, String inputStr) throws BirtException {

		if (inputStr == null || inputStr.trim().length() == 0) {
			return null;
		}

		if (handle instanceof ScalarParameterHandle) {
			inputStr = formatScalarParameter((ScalarParameterHandle) handle, inputStr);
		}

		return inputStr;
	}

	/**
	 * Formats scalar parameter value.
	 *
	 * @param handle
	 * @param inputStr
	 * @return formatted value.
	 */

	private static String formatScalarParameter(ScalarParameterHandle handle, String inputStr) throws BirtException {
		String pattern = handle.getPattern();
		String category = handle.getCategory();

		if (pattern == null) {
			if (isCustom(category)) {
				return inputStr;
			}
			pattern = category;
		}

		String dataType = handle.getDataType();
		if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(dataType)) {
			Date date = DataTypeUtil.toDate(inputStr, ULocale.US);
			DateFormatter formatter = new DateFormatter(pattern);
			inputStr = formatter.format(date);
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(dataType)) {
			inputStr = new NumberFormatter(pattern).format(DataTypeUtil.toDouble(inputStr).doubleValue());
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(dataType)) {
			inputStr = new NumberFormatter(pattern).format(DataTypeUtil.toBigDecimal(inputStr));
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(dataType)) {
			inputStr = new StringFormatter(pattern).format(inputStr);
		}
		return inputStr;
	}

}
