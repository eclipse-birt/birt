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

package org.eclipse.birt.data.aggregation.impl.rank;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.calculator.CalculatorFactory;
import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.aggregation.i18n.ResourceConstants;
import org.eclipse.birt.data.aggregation.impl.AggrException;
import org.eclipse.birt.data.aggregation.impl.AggrFunction;
import org.eclipse.birt.data.aggregation.impl.Constants;
import org.eclipse.birt.data.aggregation.impl.ParameterDefn;
import org.eclipse.birt.data.aggregation.impl.SupportedDataTypes;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Implements the built-in Total.Quartile aggregation.
 */
public class TotalQuartile extends AggrFunction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName( )
	{
		return IBuildInAggregation.TOTAL_QUARTILE_FUNC;
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
				new ParameterDefn( "quart", Messages.getString( "TotalQuartile.param.quart" ), false, false, SupportedDataTypes.CALCULATABLE, "" ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

	/**
	 * 
	 * 
	 */
	private static class MyAccumulator extends PercentileAccumulator
	{
		MyAccumulator( ICalculator calc )
		{
			super( calc );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.rank.PercentileAccumulator#getPctValue(java.lang.Double)
		 */
		protected double getPctValue( Double d ) throws DataException
		{
			validatePctValue( d );
			int quar = d.intValue( );
			double result = 0;
			if ( quar == 0 )
				result = 0;
			else if ( quar == 1 )
				result = 0.25;
			else if ( quar == 2 )
				result = 0.5;
			else if ( quar == 3 )
				result = 0.75;
			else if ( quar == 4 )
				result = 1;

			return result;
		}

		/**
		 * 
		 * @param d
		 * @throws DataException
		 */
		private void validatePctValue( Double d ) throws DataException
		{
			if ( d == null
					|| d.isNaN( ) || d.doubleValue( ) < 0
					|| d.doubleValue( ) > 4 )
				throw DataException.wrap( new AggrException( ResourceConstants.INVALID_QUARTILE_ARGUMENT ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "TotalQuartile.description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "TotalQuartile.displayName" ); //$NON-NLS-1$
	}
}
