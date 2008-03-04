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

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.aggregation.SummaryAccumulator;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 * Implements the built-in Total.mode aggregation
 */
public class TotalMode extends AggrFunction
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
     */
    public String getName()
    {
        return IBuildInAggregation.TOTAL_MODE_FUNC;
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
		return DataType.ANY_TYPE;
	}

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
     */
    public IParameterDefn[] getParameterDefn( )
	{
		return new IParameterDefn[]{
			new ParameterDefn( Constants.DATA_FIELD_NAME,
					Constants.DATA_FIELD_DISPLAY_NAME,
					false,
					true,
					SupportedDataTypes.ANY,
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
        return new MyAccumulator();
    }

    private class MyAccumulator extends SummaryAccumulator
    {
    	// Maps a value (Double) to its count (Integer)
        private HashMap cacheMap;
        private HashMap modeMap;//used by for muti-mode storage, return the first appeared mode
        private Object mode;
		private int maxCount;
		private int rowIndex;


        public void start()
        {
            super.start();
            rowIndex = 0;
            cacheMap = new HashMap();
            maxCount = 0;
            mode = null;
            modeMap = new HashMap();
        }

        class Counter{
        	int count;  //the frequence 
        	int firstIndex;//row index for the first appearance
        	
        	public Counter( int count, int firstIndex )
        	{
        		this.count = count;
        		this.firstIndex = firstIndex;
        	}
        }
        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[])
         */
        public void onRow(Object[] args) throws DataException
        {
            assert (args.length > 0);
            if (args[0] != null)
            {
            	Object value = getTypedData( args[0] );
				Counter counter = (Counter) cacheMap.get( value );
				if ( counter == null )
				{
					// first occurrence
					counter = new Counter( 1, rowIndex );
					cacheMap.put( value, counter );
				}
				else
				{
					counter.count++;
				}

				if ( counter.count > maxCount )
				{
					mode = value;
					maxCount = counter.count;
					modeMap.clear( );
					modeMap.put( value, counter );
				}
				else if ( counter.count == maxCount )
				{// Keep track of all modes with the maximum count
					modeMap.put( value, counter );
				}
				rowIndex++;
            }
        }

        public void finish() throws DataException
        {
            super.finish();
            cacheMap = null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
         */
        public Object getSummaryValue()
        {
        	if ( maxCount == 1 )// all of the objects are unique values
			{
				// no modes; ROM scripting spec says we should return null
				return null;
			}
			else if ( modeMap.isEmpty( ) == false )
			{// find the mode with the minimum index in all searched modes
				int minIndex = Integer.MAX_VALUE;
				for ( Iterator i = modeMap.keySet( ).iterator( ); i.hasNext( ); )
				{
					Object key = (Object) i.next( );
					Counter info = (Counter) modeMap.get( key );
					if ( info.firstIndex < minIndex )
					{
						minIndex = info.firstIndex;
						mode = key;
					}
				}
				modeMap = null;
			}
        	return mode;
        }

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
     */
	public String getDescription( )
	{
		return Messages.getString("TotalMode.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString("TotalMode.displayName"); //$NON-NLS-1$
	}
}