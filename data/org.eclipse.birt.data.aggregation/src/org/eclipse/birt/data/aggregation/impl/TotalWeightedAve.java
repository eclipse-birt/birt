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
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.calculator.CalculatorFactory;
import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.aggregation.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 * Implements the built-in Total.weightedAva aggregation
 */
public class TotalWeightedAve extends AggrFunction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName( )
	{
		return IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC;
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
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
	 */
	public IParameterDefn[] getParameterDefn( )
	{
		return new IParameterDefn[]{
				new ParameterDefn( Constants.EXPRESSION_NAME,
						Constants.EXPRESSION_DISPLAY_NAME,
						false,
						true,
						SupportedDataTypes.CALCULATABLE,
						"" ), //$NON-NLS-1$
				new ParameterDefn( "weight", Messages.getString( "TotalWeightedAve.param.weight" ), false, true, SupportedDataTypes.CALCULATABLE, "" ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		};
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

		private Number wsum = null;

		private Number weightsum = null;

		MyAccumulator( ICalculator calc )
		{
			super( calc );
		}

		public void start( )
		{
			super.start( );
			wsum = null;
			weightsum = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[])
		 */
		public void onRow( Object[] args ) throws DataException
		{
			assert ( args.length > 1 );

			// Skip rows with either NULL value or weight
			if ( args[0] != null && args[1] != null )
			{
				wsum = calculator.add( wsum, calculator.multiply( calculator.getTypedObject( args[0] ),
						calculator.getTypedObject( args[1] ) ) );
				weightsum = calculator.add( weightsum, calculator.getTypedObject( args[1] ) );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
		public Object getSummaryValue( ) throws DataException
		{
			if( weightsum ==  null )
				return null;
			try
			{
				return calculator.divide( wsum, weightsum );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( new AggrException( ResourceConstants.DATATYPEUTIL_ERROR,
						e ) );
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
		return Messages.getString( "TotalWeightedAve.description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "TotalWeightedAve.displayName" ); //$NON-NLS-1$
	}
}