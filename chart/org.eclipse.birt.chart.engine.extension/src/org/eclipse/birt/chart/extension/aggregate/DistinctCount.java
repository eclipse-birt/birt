/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

	@Override
	public void accumulate(Object oValue) throws IllegalArgumentException {
		uniqueValues.add(oValue);
	}

	@Override
	public Object getAggregatedValue() {
		return Integer.valueOf(uniqueValues.size());
	}

	@Override
	public void initialize() {
		uniqueValues = new HashSet<>();
	}

	@Override
	public int getBIRTDataType() {
		return DataType.INTEGER_TYPE;
	}

}
