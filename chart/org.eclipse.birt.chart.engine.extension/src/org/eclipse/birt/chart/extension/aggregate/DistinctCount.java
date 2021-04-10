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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter;
import org.eclipse.birt.core.data.DataType;

/**
 * 
 */

public class DistinctCount extends AggregateFunctionAdapter {
	private Set<Object> uniqueValues;

	public void accumulate(Object oValue) throws IllegalArgumentException {
		uniqueValues.add(oValue);
	}

	public Object getAggregatedValue() {
		return Integer.valueOf(uniqueValues.size());
	}

	public void initialize() {
		uniqueValues = new HashSet<Object>();
	}

	@Override
	public int getBIRTDataType() {
		return DataType.INTEGER_TYPE;
	}

}
