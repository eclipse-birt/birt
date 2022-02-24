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

package org.eclipse.birt.data.aggregation.calculator;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public interface ICalculator {

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public Object getTypedObject(Object obj) throws DataException;

	/**
	 * add operand <code>a</code> and operand <code>b</code> and return the result.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Number add(Object a, Object b) throws DataException;

	/**
	 * subtract operand <code>a</code> and operand <code>b</code> and return the
	 * result.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Number subtract(Object a, Object b) throws DataException;

	/**
	 * multiply operand <code>a</code> and operand <code>b</code> and return the
	 * result.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Number multiply(Object a, Object b) throws DataException;

	/**
	 * divide operand <code>a</code> and operand <code>b</code> and return the
	 * result. This operation will cause an {@link ArithmeticException} if the
	 * divisor is zero.
	 * 
	 * @param dividend
	 * @param divisor
	 * @return
	 */
	public Number divide(Object dividend, Object divisor) throws DataException;

	/**
	 * safeDivide operand <code>a</code> and operand <code>b</code> and return the
	 * result. This operation will return <code>ifZero</code> rather than cause an
	 * {@link ArithmeticException} if the divisor is zero.
	 * 
	 * @param dividend
	 * @param divisor
	 * @Param ifZero
	 * @return
	 */
	public Number safeDivide(Object dividend, Object divisor, Number ifZero) throws DataException;

}
