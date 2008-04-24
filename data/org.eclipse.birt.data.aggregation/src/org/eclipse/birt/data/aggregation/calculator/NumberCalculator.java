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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Calculator for Double objects. Note we assume both of the operands are
 * instances of Number( Byte, Short, Integer, Long, Float, Double, except
 * BigDecimal ) and not be null.
 */

public class NumberCalculator implements ICalculator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#add(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number add( Object a, Object b ) throws DataException
	{
		return ( (Number) a ).doubleValue( ) + ( (Number) b ).doubleValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#subtract(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number subtract( Object a, Object b ) throws DataException
	{
		return ( (Number) a ).doubleValue( ) - ( (Number) b ).doubleValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#multiply(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number multiply( Object a, Object b ) throws DataException
	{
		return ( (Number) a ).doubleValue( ) * ( (Number) b ).doubleValue( );
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
		return ( (Number) dividend ).doubleValue( )
				/ ( (Number) divisor ).doubleValue( );
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
	 * @see org.eclipse.birt.data.engine.api.aggregation.ICalculator#getTypedObject(java.lang.Object)
	 */
	public Object getTypedObject( Object obj ) throws DataException
	{
		try
		{
			return DataTypeUtil.toDouble( obj );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}
}
