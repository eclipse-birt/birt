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

//import com.ibm.icu.util.ULocale;

/**
 * Calculator used when an operand is a string. The assumption is that any decimal
 * string can be converted to a BigDecimal. Note that GetTypedObject() of this
 * calculator returns BigDecimal as well.
 */

public class StringCalculator extends BigDecimalCalculator
{
	//private static ULocale locale = ULocale.getDefault( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.BigDecimalCalculator#add(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number add( Object a, Object b ) throws DataException
	{
		return super.add( getTypedObject( a ), getTypedObject( b ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.BigDecimalCalculator#divide(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number divide( Object dividend, Object divisor )
			throws DataException
	{
		return super.divide( getTypedObject( dividend ), getTypedObject( divisor ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.BigDecimalCalculator#multiply(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number multiply( Object a, Object b ) throws DataException
	{
		return super.multiply( getTypedObject( a ), getTypedObject( b ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.BigDecimalCalculator#safeDivide(java.lang.Object,
	 *      java.lang.Object, java.lang.Number)
	 */
	@Override
	public Number safeDivide( Object dividend, Object divisor, Number ifZero )
			throws DataException
	{
		return super.safeDivide( getTypedObject( dividend ), getTypedObject( divisor ), ifZero );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.aggregation.impl.calculator.BigDecimalCalculator#subtract(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Number subtract( Object a, Object b ) throws DataException
	{
		return super.subtract( getTypedObject( a ), getTypedObject( b ) );
	}
}
