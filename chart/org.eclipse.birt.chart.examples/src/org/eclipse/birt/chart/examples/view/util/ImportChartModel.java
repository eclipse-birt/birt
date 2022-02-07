/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.examples.view.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.birt.chart.model.Chart;

public class ImportChartModel {
	private static Chart cm;

	/**
	 * Use Java reflection to get the designated class and method.
	 * 
	 * @param className  String
	 * @param methodName String
	 * @return Chart
	 */
	public static Chart getChartModel(String className, String methodName) {
		className = "org.eclipse.birt.chart.examples.view.models." + className; //$NON-NLS-1$
		try {
			Class modelClass = Class.forName(className);
			try {
				Method method = modelClass.getMethod(methodName, null);

				try {
					cm = (Chart) method.invoke(modelClass, null);
				} catch (InvocationTargetException iex) {
					iex.printStackTrace();
				} catch (IllegalAccessException iex) {
					iex.printStackTrace();
				}
			} catch (NoSuchMethodException mex) {
				mex.printStackTrace();
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return cm;
	}
}
