/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.aggregation.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Represents the built-in summary accumulator
 */
public abstract class SummaryAccumulator extends Accumulator
{

	protected int dataType = DataType.UNKNOWN_TYPE;

	protected boolean isFinished = false;
	
	protected ICalculator calculator;

	public void start( )
	{
		isFinished = false;
		dataType = DataType.UNKNOWN_TYPE;
		calculator = null;
	}

	public void finish( ) throws DataException
	{
		isFinished = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.aggregation.Accumulator#getValue()
	 */
	public Object getValue( ) throws DataException
	{
		if ( !isFinished )
		{
			throw new RuntimeException( "Error! Call summary total function before finished the dataset" ); //$NON-NLS-1$
		}
		return getSummaryValue( );
	}

	/**
	 * convert <code>obj</code> to Date or Double object.
	 */
	protected Object getTypedData( Object obj ) throws DataException
	{
		Object value = obj;
		switch ( dataType )
		{
			case DataType.UNKNOWN_TYPE :
				if ( obj instanceof Date )
				{
					dataType = DataType.DATE_TYPE;
				}
				else if ( obj instanceof BigDecimal )
				{
					dataType = DataType.DECIMAL_TYPE;
				}
				else
				{
					value = toDouble( obj );
					dataType = DataType.DOUBLE_TYPE;
				}
				break;
			case DataType.DOUBLE_TYPE :
				value = toDouble( obj );
				break;
		}
		return value;
	}

	/**
	 * try to convert <code>obj</code> to Double object.
	 * if it fails, a DataException will be thrown.
	 * @param obj
	 * @return
	 * @throws DataException
	 */
	protected Object toDouble( Object obj ) throws DataException
	{
		Object value = null;
		try
		{
			value = DataTypeUtil.toDouble( obj );
		}
		catch ( BirtException e )
		{
			throw new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
		}
		return value;
	}

	abstract public Object getSummaryValue( ) throws DataException;

}
