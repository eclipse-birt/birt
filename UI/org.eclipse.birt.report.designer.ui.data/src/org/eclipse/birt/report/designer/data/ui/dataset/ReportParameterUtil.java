
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 *
 */

public class ReportParameterUtil {
	public static void completeParamDefalutValues(IEngineTask engineTask, ModuleHandle moduleHandle) {
		List paramsList = moduleHandle.getAllParameters();
		for (int i = 0; i < paramsList.size(); i++) {
			Object parameterObject = paramsList.get(i);
			if (parameterObject instanceof ScalarParameterHandle) {
				ScalarParameterHandle parameterHandle = (ScalarParameterHandle) parameterObject;

				if ((parameterHandle.getDefaultValueList() == null || parameterHandle.getDefaultValueList().size() == 0)
						&& (parameterHandle.getDefaultValueListMethod() == null
								|| parameterHandle.getDefaultValueListMethod().trim().length() == 0)) {
					String paramType = parameterHandle.getParamType();
					if (DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals(paramType)) {
						engineTask.setParameter(parameterHandle.getName(),
								new Object[] { getDummyDefaultValue(parameterHandle) },
								parameterHandle.getDisplayName());
					} else {
						// no default value in handle
						engineTask.setParameter(parameterHandle.getName(), getDummyDefaultValue(parameterHandle),
								parameterHandle.getDisplayName());
					}
				}
			} else if (parameterObject instanceof DynamicFilterParameterHandle) {
				List defaultValue = ((DynamicFilterParameterHandle) parameterObject).getDefaultValueList();
				if (defaultValue == null || defaultValue.size() == 0) {
					// no default value in handle
					engineTask.setParameter(((DynamicFilterParameterHandle) parameterObject).getName(), "true",
							((DynamicFilterParameterHandle) parameterObject).getDisplayName());
				}
			}
		}
	}

	public static Object getDummyDefaultValue(ScalarParameterHandle parameterHandle) {
		String type = parameterHandle.getDataType();

		// No default value; if param allows null value, null is used
		if (!parameterHandle.isRequired()) {
			return null;
		}

		// Return a fixed default value appropriate for the data type
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
			return "";
		}
		if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)) {
			return new Double(0);
		}
		if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)) {
			return new BigDecimal((double) 0);
		}
		if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			return new Date(0);
		}
		if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type)) {
			return new java.sql.Date(0);
		}
		if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type)) {
			return new java.sql.Time(0);
		}
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type)) {
			return Boolean.FALSE;
		}
		if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			return Integer.valueOf(0);
		}
		return null;
	}
}
