
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

/**
 *
 */

public class JointDataSetParameterUtil {
	private final static String seperator = "::";
	private final static String PREFIX = "outputParams";

	/**
	 *
	 * @param dataSetName
	 * @param sourceParameterName
	 * @return
	 */
	public static String getParameterName(String dataSetName, String sourceParameterName) {
		return dataSetName + seperator + sourceParameterName;
	}

	/**
	 *
	 * @param parameterName
	 * @return
	 */
	private static String extractDataSetName(String parameterName) {
		String[] s = parameterName.split("\\Q::\\E");
		assert s.length >= 2;

		return s[0];
	}

	/**
	 *
	 * @param datasetName
	 * @return
	 */
	public static boolean isDatasetParameter(String datasetName, boolean isLeftDataSet, String parameterName) {
		return datasetName.equals(extractDataSetName(parameterName))
				|| (isLeftDataSet && (datasetName + "1").equals(extractDataSetName(parameterName)))
				|| (!isLeftDataSet && (datasetName + "2").equals(extractDataSetName(parameterName)));
	}

	/**
	 *
	 * @param parameterName
	 * @return
	 */
	public static String extractParameterName(String parameterName) {
		return parameterName.replaceFirst("\\Q" + extractDataSetName(parameterName) + "::\\E", "");
	}

	/**
	 *
	 * @param parameterName
	 * @return
	 */
	public static String buildOutputParamsExpr(String parameterName) {
		return PREFIX + "[\"" + parameterName + "\"]";
	}
}
