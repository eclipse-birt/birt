
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
package org.eclipse.birt.data.engine.olap.data.impl;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

/**
 * Defines a cube aggregation.
 */

public class AggregationDefinition {
	private DimLevel[] levels;
	private int[] sortTypes;
	private AggregationFunctionDefinition[] aggregationFunctions;
	private AggregationFunctionDefinition[] aggregationTimeFunctions;
	private DrilledInfo drilledAggregation;

	/**
	 * 
	 * @param levelNames
	 * @param sortTypes
	 * @param aggregationFunctions
	 */
	public AggregationDefinition(DimLevel[] levels, int[] sortTypes,
			AggregationFunctionDefinition[] aggregationFunctions) {
		this.levels = levels;

		int timeFunctionCount = 0;
		if (aggregationFunctions != null) {
			for (int i = 0; i < aggregationFunctions.length; i++) {
//				aggregationFunctions[i].setTimeFunction( new TestTimeFunction( ) );
				if (aggregationFunctions[i].getTimeFunction() != null) {
					timeFunctionCount++;
				}
			}
			this.aggregationFunctions = new AggregationFunctionDefinition[aggregationFunctions.length];
			if (timeFunctionCount > 0)
				this.aggregationTimeFunctions = new AggregationFunctionDefinition[timeFunctionCount];
			int ptr = 0;
			int tPtr = 0;
			for (int i = 0; i < aggregationFunctions.length; i++) {
				if (aggregationFunctions[i].getTimeFunction() == null) {
					this.aggregationFunctions[ptr++] = aggregationFunctions[i];
				} else {
					this.aggregationTimeFunctions[tPtr++] = aggregationFunctions[i];
				}
			}
			if (timeFunctionCount > 0) {
				for (int i = 0; i < aggregationTimeFunctions.length; i++) {
					this.aggregationFunctions[ptr++] = aggregationTimeFunctions[i];
				}
			}
		}
		this.sortTypes = sortTypes;

		if (this.levels != null && this.levels.length == 0) {
			// always use null to represent no aggregate on
			this.levels = null;
			this.sortTypes = null;
		}
	}

	public AggregationFunctionDefinition[] getAggregationTimeFunctions() {
		return aggregationTimeFunctions;
	}

	/**
	 * 
	 * @return
	 */
	public AggregationFunctionDefinition[] getAggregationFunctions() {
		return aggregationFunctions;
	}

	public void setAggregationFunctions(AggregationFunctionDefinition[] aggregationFunctions) {
		int timeFunctionCount = 0;
		for (int i = 0; i < aggregationFunctions.length; i++) {
			if (aggregationFunctions[i].getTimeFunction() != null) {
				timeFunctionCount++;
			}
		}
		this.aggregationFunctions = new AggregationFunctionDefinition[aggregationFunctions.length - timeFunctionCount];
		this.aggregationTimeFunctions = new AggregationFunctionDefinition[aggregationFunctions.length
				- timeFunctionCount];
		int ptr = 0;
		int tPtr = 0;
		for (int i = 0; i < aggregationFunctions.length; i++) {
			if (aggregationFunctions[i].getTimeFunction() == null) {
				this.aggregationFunctions[ptr++] = aggregationFunctions[i];
			} else {
				this.aggregationTimeFunctions[tPtr++] = aggregationFunctions[i];
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public DimLevel[] getLevels() {
		return levels;
	}

	/**
	 * 
	 * @return
	 */
	public int[] getSortTypes() {
		return sortTypes;
	}

	/**
	 * set its related drilled info
	 * 
	 * @param aggregation
	 */
	public void setDrilledInfo(DrilledInfo aggregation) {
		drilledAggregation = aggregation;
	}

	/**
	 * get its related drilled info
	 * 
	 * @return
	 */
	public DrilledInfo getDrilledInfo() {
		return this.drilledAggregation;
	}

}
