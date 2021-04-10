/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.api.aggregation;

import org.eclipse.birt.core.data.DataType;

/**
 * All multipass aggregations must implement this interface.
 * 
 * @deprecated use AggrFunction instead
 */
public abstract class Aggregation implements IAggregation {
	/**
	 * Returns the number of passes over the data series that the accumulator of
	 * this aggregate requires. For SUMMARY aggregates, the accumulator returns a
	 * value after all passes are complete. For RUNNING aggregates, the accumulator
	 * returns data in the last pass.
	 * 
	 * @return
	 */
	public int getNumberOfPasses() {
		return 1;
	}

	/**
	 * get aggregation data type
	 */
	public int getDataType() {
		return DataType.ANY_TYPE;
	}

}
