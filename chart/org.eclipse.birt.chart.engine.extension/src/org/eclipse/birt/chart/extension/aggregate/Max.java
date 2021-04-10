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

public class Max extends AggregateFunctionAdapter {

	private Object max;

	public void accumulate(Object oValue) throws IllegalArgumentException {
		if (max == null) {
			max = oValue;
		} else if (oValue instanceof Comparable) {
			max = ((Comparable) oValue).compareTo(max) >= 0 ? oValue : max;
		}

	}

	public Object getAggregatedValue() {
		return max;
	}

	public void initialize() {
		max = null;
	}

	@Override
	public int getBIRTDataType() {
		return DataType.ANY_TYPE;
	}
}
