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
 * Calculator primarily for type Double. Note that all operands are expected to be
 * converted to Double before invoking any operation. Use getTypedObject() method
 * to convert operands to the desired datatype. 
 * Nulls are ignored in calculations. NaN and Infinity are supported: if one of the
 * operands is NaN Then the result is NaN as well. If you need to override this
 * behavior do so in the respective aggregate function implementation.
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
		if( a == null && b == null )
			return null;
		if( a == null )
			return (Double) b;
		if( b == null )
			return (Double) a;
		if( isNaNorInfinity( a, b ) )
			return Double.NaN;
		return (Double) a + (Double) b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#subtract(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number subtract( Object a, Object b ) throws DataException
	{
		if( a == null && b == null )
			return null;
		if( a == null )
			return 0.0D - (Double) b;
		if( b == null )
			return (Double) a;
		if( isNaNorInfinity( a, b ) )
			return Double.NaN;
		return (Double) a - (Double) b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.script.math.ICalculator#multiply(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Number multiply( Object a, Object b ) throws DataException
	{
		if( a == null && b == null )
			return null;
		if( a == null )
			return (Double) b;
		if( b == null )
			return (Double) a;
		if( isNaNorInfinity( a, b ) )
			return Double.NaN;
		return (Double) a * (Double) b;
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
		if( dividend == null )
			return null;
		if( divisor == null )
			return (Double) dividend;
		if( isNaNorInfinity( dividend, divisor ) )
			return Double.NaN;
		return (Double) dividend / (Double) divisor;
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

	protected boolean isNaNorInfinity( Object a, Object b )
	{
		return isNaNorInfinity( a ) || isNaNorInfinity( b );
	}

	protected boolean isNaNorInfinity( Object a )
	{
		return ( a instanceof Double
					&& ( ( (Double) a ).isInfinite( ) || ( (Double) a ).isNaN( ) ) 
				|| a instanceof Float
					&& ( ( (Float) a ).isInfinite( ) || ( (Float) a ).isNaN( ) )
				);
	}
}
