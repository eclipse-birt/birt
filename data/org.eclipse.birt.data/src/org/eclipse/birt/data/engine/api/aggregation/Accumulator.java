/*
 *************************************************************************
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api.aggregation;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * The Accumulator class calculates values for its associated Aggregation.
 * Calculation of aggregate values in BIRT is iteration-driven. The data engine
 * make a pass over the range of data rows on which an aggregation is defined,
 * and passes the values calculated on each row to the Accumulator to process.
 * There are 3 steps involved in calculating an aggregate value:
 * <p>
 * (1) Start: the Data Engine calls the start() method of the Accumulator upon
 * the start of a new range of data rows on which an aggregation is
 * requested<br>
 * (2) OnRow: for each data row in range (after applying the optional filtering
 * condition), the onRow() method is called with values calculated based on the
 * data row. <br>
 * (3) Finish: After the onRow call for the last data row in range, the Data
 * Engine calls the finish() method. <br>
 * <p>
 * For SUMMARY_AGGR type aggregation, the Data Engine calls getValue() after the
 * finish() call to obtain the value of the aggregation. For RUNNING_AGGR type
 * aggregation, it calls getValue() after each onRow() call.
 * <p>
 * Each instance of the accumulator can be used to calculate aggregations over
 * multiple groups of data. The implementation should re-initialize the class
 * for a new group in the start() method.
 */
abstract public class Accumulator {

	/**
	 * This method is called before the the first onRow() call. This is an
	 * opportunity to initialize any internal data structure. If the accumulator is
	 * multi-pass, then this method will be called at the begin of each iteration.
	 *
	 * @throws DataException
	 */
	public void start() throws DataException {
	}

	/**
	 * This method is called after the last onRow() call. This is an opportunity for
	 * the Accumulator to finalize its calculation, and to release any resource
	 * allocated to facilitate the calcuation. If the accumulator is multi-pass,
	 * then this method will be called by the end of each iteration. getValue() may
	 * be called after finish() of lass pass for SUMMARY_AGGR type of aggregation.
	 *
	 * @throws DataException
	 */
	public void finish() throws DataException {
	}

	/**
	 * This method is called on every data row to supply arguments to the
	 * aggregation. The args[] argument provides an array of argument values that
	 * this aggregation expected. The number of arguments and their types are
	 * defined by the Aggregation.getParameterDefn() method.
	 * <p>
	 *
	 * Each argument can have type java.lang.Integer, java.lang.Double,
	 * java.lang.Boolean, java.lang.String, java.util.Date, java.math.BigDecimal or
	 * java.sql.Blob. It can also be null.
	 * <p>
	 * If an argument is not of an expected type (e.g., a String is passed in to a
	 * SUM accumulator), the method should make reasonable efforts to convert the
	 * argument to its expected type. If no reasonable conversion can be made, this
	 * method may throw an RuntimeException or its subclass to indicate such an
	 * error.
	 * <p>
	 *
	 * @param rowValue Argument to the aggregate function calculated based on
	 *                 current data row.
	 * @throws DataException data type conversion will throw
	 *                       <code>DteException</code>
	 */
	abstract public void onRow(Object[] args) throws DataException;

	/**
	 * This method is called after the finish() method to obtain the aggregate value
	 * in last pass of this aggregation. The return value can be of class
	 * java.lang.Integer, java.lang.Double, java.lang.Boolean, java.lang.String,
	 * java.util.Date, java.math.BigDecimal or java.sql.Blob. Or it can be null to
	 * indicate a null aggregate value.
	 *
	 * @return Aggregate value calculated over the processed rows
	 */
	abstract public Object getValue() throws DataException;

}
