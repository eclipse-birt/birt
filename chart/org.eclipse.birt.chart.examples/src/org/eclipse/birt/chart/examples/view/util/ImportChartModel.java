/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
