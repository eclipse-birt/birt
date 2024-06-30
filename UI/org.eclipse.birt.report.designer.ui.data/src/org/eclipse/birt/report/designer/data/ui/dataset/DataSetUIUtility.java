/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * This is a general utility file serving for the data set pages in design time.
 *
 * @author xwu
 *
 */
public class DataSetUIUtility {

	/**
	 * Get the default analysis type according to the data type.
	 *
	 * @param dataType
	 * @return
	 */
	public static String getDefaultAnalysisType(String dataType) {
		String defaultAnalysisType = null;

		dataType = dataType.toLowerCase();

		if (dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_JAVA_OBJECT.equals(dataType)) {
			defaultAnalysisType = DesignChoiceConstants.ANALYSIS_TYPE_MEASURE;
		} else if (DesignChoiceConstants.COLUMN_DATA_TYPE_TIME.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_DATE.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals(dataType)) {
			defaultAnalysisType = DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION;
		} else if (DesignChoiceConstants.COLUMN_DATA_TYPE_BLOB.equals(dataType)) {
			defaultAnalysisType = DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE;
		}

		return defaultAnalysisType;
	}

}
