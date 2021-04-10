/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public static final int SUMMARY_AGGR = 0;
	public static final int RUNNING_AGGR = 1;

	static final int UNKNOWN = 0;

	static final int NULL = 1;

	static final int DATE = 2;

	static final int CALENDAR = 3;

	static final int NUMBER = 4;

	static final int BIGDECIMAL = 5;

	static final int TEXT = 6;

	static final int CUSTOM = 7;

	/**
	 * An internally generated notification indicating that a function implementer
	 * should accumulate another value (to be subsequently aggregated)
	 * 
	 * @param oValue The numeric value to be accumulated
	 */
	public void accumulate(Object oValue) throws IllegalArgumentException;

	/**
	 * Returns the aggregated value as determined by the function implementation.
	 * 
	 * @return The aggregated value as determined by the function implementation.
	 */
	public Object getAggregatedValue();

	/**
	 * Sends out a notification to a function implementation subclass to initialize
	 * local member variables.
	 */
	public void initialize();

	/**
	 * Returns the count of aggregate parameter.
	 * 
	 * @since BIRT 2.3
	 */
	public int getParametersCount();

	/**
	 * Returns display text of aggregate parameters.
	 * 
	 * @since BIRT 2.3
	 */
	public String[] getDisplayParameters();

	/**
	 * Returns aggregate type.
	 * 
	 * @see #SUMMARY_AGGR
	 * @see #RUNNING_AGGR
	 * @since BIRT 2.3
	 */
	public int getType();

	/**
	 * Returns the aggregation data type defined by BIRT.
	 * 
	 * @since BIRT 2.5.2
	 * @return The aggregation data type defined by BIRT.
	 */
	public int getBIRTDataType();
}