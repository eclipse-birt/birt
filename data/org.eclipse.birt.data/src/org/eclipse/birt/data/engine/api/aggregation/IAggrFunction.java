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

package org.eclipse.birt.data.engine.api.aggregation;

/**
 * Each instance of IAggrFunction defines an aggregation function which can be
 * used in BIRT. All user defined aggregations should implement this interface.
 */

public interface IAggrFunction {

	int SUMMARY_AGGR = 0;
	int RUNNING_AGGR = 1;

	/**
	 * Gets the name that identifies the aggregate function represented by this
	 * class. For BIRT built-in aggregations, this is the name of the JavaScript
	 * function (e.g., "SUM", "AVG", etc).
	 */
	String getName();

	/**
	 * Gets the display name that identifies the aggregation function in the report
	 * designer user interface.
	 *
	 * @return
	 */
	String getDisplayName();

	/**
	 * Gets the functional description of this aggregation function.
	 *
	 * @return
	 */
	String getDescription();

	/**
	 * Gets the type of the Aggregation. The aggregation can be SUMMARY_AGGR,
	 * meaning that only one value is calculated for the whole series of data rows
	 * (e.g., the Total.SUM function). Or it can be RUNNING_AGGR, meaning that a
	 * value is calculated for each row in the series (e.g., the movingAve
	 * function).
	 *
	 * @return Type of the aggregation. Value can be SUMMARY_AGGR or RUNNING_AGGR
	 */
	int getType();

	/**
	 * get the aggregation data type.
	 *
	 * @return
	 */
	int getDataType();

	/**
	 * Gets information about the parameters that this aggregate function takes as
	 * an array of IParameterDefn values.
	 * <p>
	 * The length of the returned array is the number of runtime parameters that
	 * this aggregate function takes. Note that this number excludes the optional
	 * filter and group parameters common to all aggregate functions. Those two
	 * parameters are handled by the DtE.
	 * <p>
	 * If the n'th element in the array is true, it means that the n'th parameter is
	 * a dynamic parameter which needs to be evaluated at each row. Otherwise the
	 * parameter is static, and it only needs to be evaluated once at the start of
	 * the accumulation
	 * <p>
	 * For example, the Total.movingAve function is defined as <br>
	 * movingAve( expr, window [, filter [, group ]] )<br>
	 * The expr parameter is the data being aggregated over and should be calculated
	 * at every row. The window parameter on the other hand must be a fixed number
	 * for each series of data. Therefore the class implementing the movingAve
	 * function should return boolean array [true, false].
	 */
	IParameterDefn[] getParameterDefn();

	/**
	 * Returns the number of passes over the data series that the accumulator of
	 * this aggregate requires. For SUMMARY aggregates, the accumulator returns a
	 * value after all passes are complete. For RUNNING aggregates, the accumulator
	 * returns data in the last pass.
	 *
	 * @return
	 */
	int getNumberOfPasses();

	/**
	 * Returns the default aggregation result if the data series that the
	 * accumulator of this aggregation function requires is empty.
	 *
	 * @return
	 */
	Object getDefaultValue();

	/**
	 * To indicate whether the data series is order sensitive for the aggregation
	 * value.
	 *
	 * @return
	 */
	boolean isDataOrderSensitive();

	/**
	 * Creates a new instance of the accumulator for this aggregation.
	 *
	 * @return A new instance of the accumulator
	 */
	Accumulator newAccumulator();

}
