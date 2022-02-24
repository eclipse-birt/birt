/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.service;

import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

public class ParameterDataTypeConverter {

	/**
	 * Parameter data type convertion from string to int.
	 * 
	 * @param type String
	 * @return
	 */
	public static final int convertDataType(String type) {
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type)) {
			return IScalarParameterDefn.TYPE_BOOLEAN;
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			return IScalarParameterDefn.TYPE_DATE_TIME;
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)) {
			return IScalarParameterDefn.TYPE_DECIMAL;
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)) {
			return IScalarParameterDefn.TYPE_FLOAT;
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
			return IScalarParameterDefn.TYPE_STRING;
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			return IScalarParameterDefn.TYPE_INTEGER;
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type)) {
			return IScalarParameterDefn.TYPE_DATE;
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type)) {
			return IScalarParameterDefn.TYPE_TIME;
		}
		return IScalarParameterDefn.TYPE_ANY;
	}

	/**
	 * Parameter data type convertion from int to string.
	 * 
	 * @param type String
	 * @return
	 */
	public static final String convertDataType(int type) {
		String dataType = DesignChoiceConstants.PARAM_TYPE_ANY;

		switch (type) {
		case IScalarParameterDefn.TYPE_BOOLEAN:
			dataType = DesignChoiceConstants.PARAM_TYPE_BOOLEAN;
			break;
		case IScalarParameterDefn.TYPE_DATE_TIME:
			dataType = DesignChoiceConstants.PARAM_TYPE_DATETIME;
			break;
		case IScalarParameterDefn.TYPE_DECIMAL:
			dataType = DesignChoiceConstants.PARAM_TYPE_DECIMAL;
			break;
		case IScalarParameterDefn.TYPE_FLOAT:
			dataType = DesignChoiceConstants.PARAM_TYPE_FLOAT;
			break;
		case IScalarParameterDefn.TYPE_STRING:
			dataType = DesignChoiceConstants.PARAM_TYPE_STRING;
			break;
		case IScalarParameterDefn.TYPE_INTEGER:
			dataType = DesignChoiceConstants.PARAM_TYPE_INTEGER;
			break;
		case IScalarParameterDefn.TYPE_DATE:
			dataType = DesignChoiceConstants.PARAM_TYPE_DATE;
			break;
		case IScalarParameterDefn.TYPE_TIME:
			dataType = DesignChoiceConstants.PARAM_TYPE_TIME;
			break;
		}

		return dataType;
	}
}
