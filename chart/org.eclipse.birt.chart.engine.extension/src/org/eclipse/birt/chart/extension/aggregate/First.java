/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.extension.aggregate;

import org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter;
import org.eclipse.birt.core.data.DataType;

/**
 * 
 */

public class First extends AggregateFunctionAdapter {
	private Object first;

	public void accumulate(Object oValue) throws IllegalArgumentException {
		if (first == null && oValue != null) {
			first = oValue;
		}

	}

	public Object getAggregatedValue() {
		return first;
	}

	public void initialize() {
		first = null;

	}

	@Override
	public int getBIRTDataType() {
		return DataType.ANY_TYPE;
	}

}
