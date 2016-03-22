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

import org.eclipse.birt.core.data.DataType;


/**
 * 
 */

public class CalculatorFactory
{

	private CalculatorFactory( )
	{
	}
	
	public static ICalculator getCalculator( int dataType )
	{
		if ( dataType == DataType.BOOLEAN_TYPE )
		{
			return new BooleanCalculator( );
		}
		else if ( dataType == DataType.DATE_TYPE )
		{
			return new DateCalculator( );
		}
		else if ( dataType == DataType.STRING_TYPE )
		{
			return new StringCalculator( );
		}
		else if ( dataType == DataType.DECIMAL_TYPE )
		{
			return new BigDecimalCalculator( );
		}
		else
		{
			return new NumberCalculator( );
		}
	}
}
