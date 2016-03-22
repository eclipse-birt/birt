/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.calculator.CalculatorFactory;
import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 * Implements the built-in Total.AVE aggregation
 */
public class TotalAve extends AggrFunction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName( )
	{
		return IBuildInAggregation.TOTAL_AVE_FUNC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
	 */
	public int getType( )
	{
		return SUMMARY_AGGR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getDateType()
	 */
	public int getDataType( )
	{
		return DataType.DOUBLE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
	public Accumulator newAccumulator( )
	{
		return new MyAccumulator( CalculatorFactory.getCalculator( getDataType( ) ) );
	}

	private static class MyAccumulator extends SummaryAccumulator
	{

		private Number sum = null;

		private int count = 0;
		
		MyAccumulator( ICalculator calc )
        {
        	super( calc );
        }
		
		public void start( )
		{
			super.start( );
			sum = null;
			count = 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[])
		 */
		public void onRow( Object[] args ) throws DataException
		{
			assert ( args.length > 0 );
			if ( args[0] != null )
			{
				sum = calculator.add( sum, calculator.getTypedObject( args[0] ) );
				count++;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
		public Object getSummaryValue( ) throws DataException
		{
			if ( count > 0 )
			{
				return calculator.divide( sum, calculator.getTypedObject( count ) );
			}
			else
			{
				return null;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "TotalAve.description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "TotalAve.displayName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getParameterDefn()
	 */
	public IParameterDefn[] getParameterDefn( )
	{
		return new IParameterDefn[]{
			new ParameterDefn( Constants.EXPRESSION_NAME,
					Constants.EXPRESSION_DISPLAY_NAME,
					false,
					true,
					SupportedDataTypes.CALCULATABLE,
					"" ) //$NON-NLS-1$
		};
	}
}