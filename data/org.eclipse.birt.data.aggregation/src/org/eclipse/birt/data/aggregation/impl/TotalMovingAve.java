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

import java.util.LinkedList;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
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
 * Implements the built-in Total.movingAva aggregation
 */
public class TotalMovingAve extends AggrFunction
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName( )
	{
		return IBuildInAggregation.TOTAL_MOVINGAVE_FUNC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
	 */
	public int getType( )
	{
		return RUNNING_AGGR;
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
						"" ),//$NON-NLS-1$
				new ParameterDefn( "window", Messages.getString( "TotalMovingAve.param.window" ), false, false, SupportedDataTypes.CALCULATABLE, "" ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

	private static class MyAccumulator extends RunningAccumulator
	{

		private LinkedList list;

		private int window = 1;

		private Number sum = null;
		
		MyAccumulator( ICalculator calc )
		{
			super( calc );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.RunningAccumulator#start()
		 */
		public void start( ) throws DataException
		{
			super.start( );
			sum = null;
			list = new LinkedList( );
			window = 1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[])
		 */
		public void onRow( Object[] args ) throws DataException
		{
			assert ( args.length > 1 );
			if ( args[0] != null && args[1] != null )
			{
				try
				{
					if ( list.size( ) == 0 )
					{
						window = DataTypeUtil.toInteger( args[1] ).intValue( );
						assert ( window > 0 );
					}
					list.addLast( args[0] );
					sum = calculator.add( sum, calculator.getTypedObject( args[0] ) );

					if ( list.size( ) > window )
					{
						sum = calculator.subtract( sum, calculator.getTypedObject( list.get( 0 ) ) );
						list.remove( 0 );
					}
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
		 * @see org.eclipse.birt.data.engine.aggregation.Accumulator#getValue()
		 */
		public Object getValue( ) throws DataException
		{
			if ( list.size( ) == 0 )
			{
				return null;
			}

			return calculator.divide( sum, calculator.getTypedObject( list.size( ) ) );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "TotalMovingAve.description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "TotalMovingAve.displayName" ); //$NON-NLS-1$
	}
}