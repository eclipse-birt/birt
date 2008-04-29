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
import java.util.Date;


/**
 * 
 */

public class CalculatorFactory
{

	private CalculatorFactory( )
	{
	}

	/**
	 * 
	 * @param dataType
	 * @return
	 */
	public static ICalculator getCalculator( Class<?> clz )
	{
		if ( clz.equals( Boolean.class ) )
		{
			return new BooleanCalculator( );
		}
		else if ( Date.class.isAssignableFrom( clz ) )
		{
			return new DateCalculator( );
		}
		else if ( clz.equals( String.class ) )
		{
			return new StringCalculator( );
		}
		else if ( clz.equals( BigDecimal.class ) )
		{
			return new BigDecimalCalculator( );
		}
		else
		{
			return new NumberCalculator( );
		}
	}

}
