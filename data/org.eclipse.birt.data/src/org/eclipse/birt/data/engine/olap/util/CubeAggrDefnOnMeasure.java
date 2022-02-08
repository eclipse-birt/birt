
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.util;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;

/**
 * 
 */

public class CubeAggrDefnOnMeasure extends CubeAggrDefn {
	private String measure;

	/**
	 * For time function
	 * 
	 * @param name
	 * @param measure
	 * @param aggrLevels
	 * @param aggrName
	 * @param timeFunctionName
	 * @param arguments
	 * @param filterExpression
	 */
	public CubeAggrDefnOnMeasure(String name, String measure, List aggrLevels, String aggrName,
			ITimeFunction timeFunciton, List arguments, IBaseExpression filterExpression) {
		super(name, aggrLevels, aggrName, timeFunciton, arguments, filterExpression);
		this.measure = measure;
	}

	/**
	 * Return the measure that featured this aggregation.
	 * 
	 * @return
	 */
	public String getMeasure() {
		return measure;
	}
}
