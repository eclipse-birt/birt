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
package org.eclipse.birt.report.designer.util;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class ParameterDataTypeConverter {
	public static final String convertToColumnDataType(String paramDataType) {
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN;
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL;
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT;
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER;
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATE;
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(paramDataType)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_TIME;
		}
		return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY;
	}

	public static final String convertToParameDataType(String columnDataType) {
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_BOOLEAN;
		}
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_DATETIME;
		}
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_DECIMAL;
		}
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_FLOAT;
		}
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_STRING;
		}
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_INTEGER;
		}
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_DATE.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_DATE;
		}
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_TIME.equals(columnDataType)) {
			return DesignChoiceConstants.PARAM_TYPE_TIME;
		}
		return DesignChoiceConstants.PARAM_TYPE_ANY;
	}
}
