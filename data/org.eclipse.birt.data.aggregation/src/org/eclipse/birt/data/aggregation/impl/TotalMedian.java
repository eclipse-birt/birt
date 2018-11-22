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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Implements the built-in Total.median aggregation
 */
public class TotalMedian extends AggrFunction
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
     */
    public String getName()
    {
        return IBuildInAggregation.TOTAL_MEDIAN_FUNC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
     */
    public int getType()
    {
        return SUMMARY_AGGR;
    }
    
    /*
     * (non-Javadoc)
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
					"" )//$NON-NLS-1$
		};
	}

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
    public Accumulator newAccumulator()
    {
    	return new MyAccumulator( CalculatorFactory.getCalculator( getDataType( ) ) );
    }

    private static class MyAccumulator extends SummaryAccumulator
    {
        private List list;

        private Object ret = null;

        MyAccumulator( ICalculator calc )
        {
        	super( calc );
        }

        public void start()
        {
            super.start();
            list = new ArrayList();
            ret = null;
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
				list.add( calculator.getTypedObject( args[0] ) );
			}
		}

        public void finish( ) throws DataException
		{
			int size = list.size( );
			if ( size > 0 )
			{
				Object[] values = list.toArray( );
				Arrays.sort( values );

				if ( size % 2 == 0 )
				{
					Object d1 = values[size / 2 - 1];
					Object d2 = values[size / 2];
					ret = calculator.divide(
							calculator.add( calculator.getTypedObject( d1 ), calculator.getTypedObject( d2 ) ),
							calculator.getTypedObject( 2 ) );
				}
				else
				{
					ret = calculator.getTypedObject( values[size / 2] );
				}
			}
			super.finish( );
		}

        /*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
		 */
        public Object getSummaryValue()
        {
            return ret;
        }

    }
    /*
     * (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
     */
	public String getDescription( )
	{
		return Messages.getString("TotalMedian.description"); //$NON-NLS-1$
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString("TotalMedian.displayName"); //$NON-NLS-1$
	}
}