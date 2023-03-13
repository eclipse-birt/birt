/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.aggregate;

/**
 * This interface defines the extension interface for all chart aggregate
 * functions.
 */
public interface IAggregateFunction {
	int SUMMARY_AGGR = 0;
	int RUNNING_AGGR = 1;

	int UNKNOWN = 0;

	int NULL = 1;

	int DATE = 2;

	int CALENDAR = 3;

	int NUMBER = 4;

	int BIGDECIMAL = 5;

	int TEXT = 6;

	int CUSTOM = 7;

	/**
	 * An internally generated notification indicating that a function implementer
	 * should accumulate another value (to be subsequently aggregated)
	 *
	 * @param oValue The numeric value to be accumulated
	 */
	void accumulate(Object oValue) throws IllegalArgumentException;

	/**
	 * Returns the aggregated value as determined by the function implementation.
	 *
	 * @return The aggregated value as determined by the function implementation.
	 */
	Object getAggregatedValue();

	/**
	 * Sends out a notification to a function implementation subclass to initialize
	 * local member variables.
	 */
	void initialize();

	/**
	 * Returns the count of aggregate parameter.
	 *
	 * @since BIRT 2.3
	 */
	int getParametersCount();

	/**
	 * Returns display text of aggregate parameters.
	 *
	 * @since BIRT 2.3
	 */
	String[] getDisplayParameters();

	/**
	 * Returns aggregate type.
	 *
	 * @see #SUMMARY_AGGR
	 * @see #RUNNING_AGGR
	 * @since BIRT 2.3
	 */
	int getType();

	/**
	 * Returns the aggregation data type defined by BIRT.
	 *
	 * @since BIRT 2.5.2
	 * @return The aggregation data type defined by BIRT.
	 */
	int getBIRTDataType();
}
