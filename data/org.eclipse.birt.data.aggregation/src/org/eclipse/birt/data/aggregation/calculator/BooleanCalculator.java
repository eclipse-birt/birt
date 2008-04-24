/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.aggregation.calculator;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class BooleanCalculator extends NumberCalculator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#add(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number add( Object a, Object b ) throws DataException
	{
		Number[] args = convert( a, b );
		return super.add( args[0], args[1] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#divide(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number divide( Object dividend, Object divisor )
			throws DataException
	{
		Number[] args = convert( dividend, divisor );
		return super.divide( args[0], args[1] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#multiply(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number multiply( Object a, Object b ) throws DataException
	{
		Number[] args = convert( a, b );
		return super.multiply( args[0], args[1] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#safeDivide(java.lang.Object,
	 *      java.lang.Object, java.lang.Number)
	 */
	@Override
	public Number safeDivide( Object dividend, Object divisor, Number ifZero )
			throws DataException
	{
		Number[] args = convert( dividend, divisor );
		return super.safeDivide( args[0], args[1], ifZero );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.NumberCalculator#subtract(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number subtract( Object a, Object b ) throws DataException
	{
		Number[] args = convert( a, b );
		return super.subtract( args[0], args[1] );
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	private Number[] convert( Object a, Object b )
	{
		Number[] args = new Number[2];

		args[0] = ( a instanceof Boolean ) ? ( ( (Boolean) a ).booleanValue( )
				? 1D : 0D ) : (Number) a;
		args[1] = ( b instanceof Boolean ) ? ( ( (Boolean) b ).booleanValue( )
				? 1D : 0D ) : (Number) b;
		return args;
	}
}
