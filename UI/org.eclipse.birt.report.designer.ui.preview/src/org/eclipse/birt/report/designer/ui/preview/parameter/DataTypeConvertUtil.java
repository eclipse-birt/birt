/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Converts the input text on paramter dialog to the value of chosen data type
 */
public class DataTypeConvertUtil {

	public static Object convert(Object value, String type) throws BirtException {
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type)) {
			return DataTypeUtil.toBoolean(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			return DataTypeUtil.toDate(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)) {
			return DataTypeUtil.toBigDecimal(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)) {
			return DataTypeUtil.toDouble(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
			return DataTypeUtil.toString(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			return DataTypeUtil.toInteger(value);
		}
		return value;
	}
}
