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

import java.math.BigDecimal;
import java.math.MathContext;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class BigDecimalCalculator implements ICalculator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#add(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number add( Object a, Object b ) throws DataException
	{
		BigDecimal[] args = convert( a, b );
		return args[0].add( args[1] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#divide(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number divide( Object dividend, Object divisor )
			throws DataException
	{
		BigDecimal[] args = convert( dividend, divisor );
		return args[0].divide( args[1], MathContext.DECIMAL128 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#multiply(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number multiply( Object a, Object b ) throws DataException
	{
		BigDecimal[] args = convert( a, b );
		return args[0].multiply( args[1] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#safeDivide(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
	public Number safeDivide( Object dividend, Object divisor, Number ifZero )
			throws DataException
	{
		try
		{
			return divide( dividend, divisor );
		}
		catch ( ArithmeticException e )
		{
			return ifZero;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#subtract(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number subtract( Object a, Object b ) throws DataException
	{
		BigDecimal[] args = convert( a, b );
		return args[0].subtract( args[1] );
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	private BigDecimal[] convert( Object a, Object b ) throws DataException
	{
		BigDecimal[] args = new BigDecimal[2];
		args[0] = ( !( a instanceof BigDecimal ) )
				? BigDecimal.valueOf( ( (Number) a ).doubleValue( ) )
				: (BigDecimal) a;
		args[1] = ( !( b instanceof BigDecimal ) )
				? BigDecimal.valueOf( ( (Number) b ).doubleValue( ) )
				: (BigDecimal) b;
		return args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.ICalculator#getTypedObject(java.lang.Object)
	 */
	public Object getTypedObject( Object obj ) throws DataException
	{
		try
		{
			return DataTypeUtil.toBigDecimal( obj );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}
}
