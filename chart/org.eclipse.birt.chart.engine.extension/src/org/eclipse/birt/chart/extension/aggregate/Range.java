/*******************************************************************************
 * Copyright (c) 2004, 2016 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.extension.aggregate;

import java.math.BigDecimal;

import org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter;
import org.eclipse.birt.core.data.DataType;

/**
 * 
 */

public class Range extends AggregateFunctionAdapter
{

	private Object max;
	private Object min;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public void accumulate( Object oValue ) throws IllegalArgumentException
	{
		if ( max == null )
		{
			max = oValue;
			min = oValue;
		}
		else if ( oValue instanceof Comparable )
		{
			max = ( (Comparable) oValue ).compareTo( max ) >= 0 ? oValue : max;
			min = ( (Comparable) oValue ).compareTo( min ) <= 0 ? oValue : min;
		}

	}

	public Object getAggregatedValue( )
	{
		switch ( getDataType( ) )
		{
			case NUMBER :
				return new Double( ( (double) max ) - (double) min );

			case BIGDECIMAL :
				return ( (BigDecimal) max ).subtract( (BigDecimal) min );

			default :
				return null; // THIS CONDITION SHOULD NEVER ARISE
		}
	}

	public void initialize( )
	{
		max = null;
		min = null;
	}

	@Override
	public int getBIRTDataType( )
	{
		return DataType.DOUBLE_TYPE;
	}
}
