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

import java.text.ParseException;

import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.core.DataException;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class StringCalculator extends NumberCalculator
{
	private static ULocale locale = ULocale.getDefault( );
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
	private Number[] convert( Object a, Object b ) throws DataException
	{
		Number[] arguments = new Number[2];

		arguments[0] = ( a instanceof String ) ? toDouble( (String) a )
				: (Number) a;
		arguments[1] = ( b instanceof String ) ? toDouble( (String) b )
				: (Number) b;
		return arguments;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	private Double toDouble( String source ) throws DataException
	{
		try
		{
			return Double.valueOf( (String) source );
		}
		catch ( NumberFormatException e )
		{
			try
			{
				Number number = NumberFormat.getInstance( locale )
						.parse( (String) source );
				if ( number != null )
					return new Double( number.doubleValue( ) );

				throw DataException.wrap( new CoreException( ResourceConstants.CONVERT_FAILS,
						new Object[]{
								source.toString( ), "Double" //$NON-NLS-1$
						} ) );
			}
			catch ( ParseException e1 )
			{
				throw DataException.wrap( new CoreException( ResourceConstants.CONVERT_FAILS,
						new Object[]{
								source.toString( ), "Double" //$NON-NLS-1$
						} ) );
			}
		}
	}

}
