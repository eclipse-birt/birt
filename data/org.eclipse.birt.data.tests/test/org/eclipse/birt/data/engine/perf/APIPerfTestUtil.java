/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.perf;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;

import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.perf.util.SizeOfUtil;
import org.eclipse.birt.data.engine.perf.util.TimeUtil;
import org.eclipse.birt.data.engine.perf.util.SizeOfUtil.SizePoint;
import org.eclipse.birt.data.engine.perf.util.TimeUtil.TimePoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * interface of Query info provider
 */
interface QueryInfo
{
	public IBaseDataSourceDesign getDataSource( ) throws Exception;
	public IBaseDataSetDesign getDataSet( ) throws Exception;
	public QueryDefinition getQueryDefn( ) throws Exception;
	public String[] getExprNames( ) throws Exception;
}

/**
 * A basic class used to test the performance of DtE API in aspects of time and
 * space bench mark.
 * 
 * This can also be used as an demonstration to design other performance test.
 */
public class APIPerfTestUtil
{
	/** query info provider */
	private QueryInfo queryInfo;
	
	/**
	 * @return an instance of PerfTestUtil
	 */
	public static APIPerfTestUtil newInstance( )
	{
		return new APIPerfTestUtil( );
	}
	
	/**
	 * Set queryInfo provider
	 * 
	 * @param queryInfo
	 */
	public void setQueryInfo( QueryInfo queryInfo )
	{
		assert queryInfo != null;		
		this.queryInfo = queryInfo;
	}
	
	/**
	 * Test feature of time benchmark between sequential operations
	 * 
	 * Basic monitered event for time bench mark test.
	 * 		1: start data engine
	 * 		2: do query execution
	 * 		3: do retrive data
	 * 		4: whole operation
	 * 
	 * Define new bench mark test, please follow below steps:
	 * 		1: define which event needs to be monitered
	 * 		2: define the function which do the real bench mark test
	 * 		3: output returned result
	 * 
	 * @param isAverageValue
	 * @throws Exception
	 */
	public void runTimeBenchMark( boolean isAverageValue ) throws Exception
	{
		assert queryInfo != null;
		
		// prepare monitored event
		final int len = 23;
		String[] eventStr = new String[]{
				formatStr1( "start data engine", len ),
				formatStr1( "do query execution", len ),
				formatStr1( "do retreive data", len ),
				formatStr1( "whole operation", len )
		};
		
		String prefix = "time consumed for event: ";
		String[] eventOutputStr = new String[eventStr.length];
		for ( int i = 0; i < eventOutputStr.length; i++ )
		{
			eventOutputStr[i] = prefix + eventStr[i];
		}
		
		// do bench mark test
		final int eventCount = 4;
		final int loopCount = isAverageValue ? 4 : 1;
		long[] timeSpan = doTimeBenchMark( eventCount, loopCount );
		
		// output returned result
		for ( int i = 0; i < eventStr.length; i++ )
		{
			System.out.println( eventOutputStr[i]
					+ ":"
					+ TimeUtil.instance.getTimePointSpanStr( timeSpan[i] ) );
		}
		
	}
	
	/**
	 * According to passed loopCount, the value is calculted by repeatedly
	 * calling the function of doing the real bench mark to compute the avergae
	 * value of operation.
	 * 
	 * @param eventCount
	 * @param loopCount
	 * @return bench mark value of time
	 * @throws Exception
	 */
	private long[] doTimeBenchMark( int eventCount, int loopCount )
			throws Exception
	{
		assert eventCount > 0;
		assert loopCount > 0;
		
		long[][] timeSpanArray = new long[loopCount][eventCount];

		// do bench mark 
		for ( int j = 0; j < loopCount; j++ )
		{
			TimePoint[] tpArray = new TimePoint[eventCount];
			doTimeBenchMarkOnce( tpArray );
			
			for ( int i = 0; i < eventCount; i++ )
			{
				long timeSpan;			
				if ( i < eventCount - 1 )
					timeSpan = TimeUtil.instance.getTimePointSpan( tpArray[i],
							tpArray[i + 1] );
				else
					timeSpan = TimeUtil.instance.getTimePointSpan( tpArray[0],
							tpArray[i] );
				
				timeSpanArray[j][i] = timeSpan;
			}
		}
		
		// compute average value
		long[] aveTimeSpan = new long[eventCount];
		for ( int i = 0; i < eventCount; i++ )
		{
			long totalTimeSpan = 0;
			if ( loopCount == 1 )
			{
				aveTimeSpan[i] = timeSpanArray[0][i];
			}
			else
			{
				for ( int j = 1; j < loopCount; j++ )
				{
					totalTimeSpan += timeSpanArray[j][i];
				}
				aveTimeSpan[i] = totalTimeSpan / ( loopCount - 1 );
			}
		}

		return aveTimeSpan;
	}
	
	/**
	 * Do time space bench mark
	 * 
	 * @param tpArray
	 * @throws Exception
	 */
	private void doTimeBenchMarkOnce( TimePoint[] tpArray ) throws Exception
	{
		// 0: time point of start data engine
		tpArray[0] = TimeUtil.instance.getTimePoint( );
		
		DataEngine dataEngine1 = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		dataEngine1.defineDataSource( queryInfo.getDataSource() );
		dataEngine1.defineDataSet( queryInfo.getDataSet() );

		// 1: time point of start do execution
		tpArray[1] = TimeUtil.instance.getTimePoint( );
		
		IPreparedQuery preparedQuery = dataEngine1.prepare( queryInfo.getQueryDefn( ) );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator ri = queryResults.getResultIterator( );
		
		// 2: time point of start retrive data
		tpArray[2] = TimeUtil.instance.getTimePoint( );
		
		String[] exprs = queryInfo.getExprNames( );
		while ( ri.next( ) )
		{
			if ( exprs == null )
				continue;
			
			for ( int j = 0; j < exprs.length; j++ )
			{
				ri.getValue( exprs[j] );
			}
		}

		// 3: time point of everything is done
		tpArray[3] = TimeUtil.instance.getTimePoint( );
		
		ri.close( );
		queryResults.close( );
		dataEngine1.shutdown( );
	}
	
	/**
	 * Test feature of space benchmark between sequential operations
	 * 
	 * Basic monitered event for space bench mark test.
	 * 		1: do query execution
	 * 		2: do retrive data
	 * 		3: close result iterator
	 * 		4: close query result
	 * 		5: close data engine
	 * 		6: whole operation
	 * 
	 * @param isAverageValue
	 * @throws Exception
	 */
	public void runSpaceBenchMark( boolean isAverageValue ) throws Exception
	{
		assert queryInfo != null;
		
		// prepare monitered event
		final int len = 23;
		String[] eventStr = new String[]{
				formatStr1( "do query execution", len ),
				formatStr1( "do retreive data", len ),
				formatStr1( "close result iterator", len ),
				formatStr1( "close query results", len ),
				formatStr1( "close data engine", len ),
				formatStr1( "whole operation", len )
		};
		
		String prefix = "memory consumed for event: ";
		String[] eventOutputStr = new String[eventStr.length];
		for ( int i = 0; i < eventOutputStr.length; i++ )
		{
			eventOutputStr[i] = prefix + eventStr[i];
		}

		// do bench mark test
		final int eventCount = 6;
		final int loopCount = isAverageValue ? 4 : 1;
		long[] sizeSpan = doSpaceBenchMark( eventCount, loopCount );
		
		// output returned result
		for ( int i = 0; i < eventStr.length; i++ )
		{
			System.out.println( eventOutputStr[i]
					+ ":" + formatLong( sizeSpan[i], 10 ) + " bytes" );
		}
	}
	
	/**
	 * @param eventCount
	 * @param loopCount
	 * @return bench mark value of space
	 * @throws Exception
	 */
	private long[] doSpaceBenchMark( int eventCount, int loopCount )
			throws Exception
	{
		assert eventCount > 0;
		assert loopCount > 0;
		
		long[][] spaceSpan = new long[loopCount][eventCount];

		// do bench mark 
		for ( int j = 0; j < loopCount; j++ )
		{
			SizePoint[] spArray = new SizePoint[eventCount];
			doSpaceBenchMarkOnce( spArray );
			
			for ( int i = 0; i < eventCount; i++ )
			{
				long sizeSpan;
				if ( i < eventCount - 1 )
					sizeSpan = SizeOfUtil.instance.getSizePointSpan( spArray[i],
							spArray[i + 1] );
				else
					sizeSpan = SizeOfUtil.instance.getSizePointSpan( spArray[0],
							spArray[i] );

				spaceSpan[j][i] = sizeSpan;
			}
		}
		
		// compute average value
		long[] aveSpaceSpan = new long[eventCount];
		for ( int i = 0; i < eventCount; i++ )
		{
			long totalSpaceSpan = 0;
			if ( loopCount == 1 )
			{
				aveSpaceSpan[i] = spaceSpan[0][i];
			}
			else
			{
				for ( int j = 1; j < loopCount; j++ )
				{
					totalSpaceSpan += spaceSpan[j][i];
				}
				aveSpaceSpan[i] = totalSpaceSpan / ( loopCount - 1 );
			}
		}

		return aveSpaceSpan;
	}
	
	/**
	 * Run test of memory consumed change
	 * 
	 * @param spArray
	 *            output sizePointArray
	 */
	private void doSpaceBenchMarkOnce( SizePoint[] spArray ) throws Exception
	{
		DataEngine dataEngine1 = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		dataEngine1.defineDataSource( queryInfo.getDataSource( ));
		dataEngine1.defineDataSet( queryInfo.getDataSet( ) );
		
		// 0: space point of start do execution
		spArray[0] = SizeOfUtil.instance.getUsedMemorySizePoint( );
		
		IPreparedQuery preparedQuery = dataEngine1.prepare( queryInfo.getQueryDefn( ) );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator ri = queryResults.getResultIterator( );
		
		// 1: space point of start retrive data
		spArray[1] = SizeOfUtil.instance.getUsedMemorySizePoint( );
		
		String[] exprs = queryInfo.getExprNames( );
		while ( ri.next( ) )
		{
			for ( int j = 0; j < exprs.length; j++ )
			{
				ri.getValue( exprs[j] );
			}
		}
		
		// 2: space point of start close result iterator
		spArray[2] = SizeOfUtil.instance.getUsedMemorySizePoint( );
		
		ri.close( );
		
		// 3: space point of start close query results
		spArray[3] = SizeOfUtil.instance.getUsedMemorySizePoint( );
		
		queryResults.close( );
		
		// 4: space point of start shut down data engine		
		spArray[4] = SizeOfUtil.instance.getUsedMemorySizePoint( );
		
		dataEngine1.shutdown( );
		
		// 5: space point of everything is done		
		spArray[5] = SizeOfUtil.instance.getUsedMemorySizePoint( );
	}
	
	/**
	 * Format long value
	 * 
	 * @param value
	 * @param length
	 * @return string
	 */
	private static String formatLong( long value, int length )
	{
		boolean isPostive = value >= 0;
		value = isPostive ? value : value * -1;
		
		String result = formatStr2( "" + value, length - 1 );
		if ( isPostive )
			result = " " + result;
		else
			result = "-" + result;
		
		return result;
	}

	/**
	 * Add space char to the end of string
	 * 
	 * @param inputStr
	 * @param length
	 * @return string
	 */
	private static String formatStr1( String inputStr, int length )
	{
		return formatStr(inputStr, length, true);
	}
	
	/**
	 * Add space char to the beginning of string
	 * 
	 * @param inputStr
	 * @param length
	 * @return string
	 */
	private static String formatStr2( String inputStr, int length )
	{
		return formatStr( inputStr, length, false );
	}

	/**
	 * Format string, add space char to the string
	 * @param inputStr
	 * @param length
	 * @param appendToTail
	 * @return string
	 */
	private static String formatStr( String inputStr, int length,
			boolean appendToTail )
	{
		if ( inputStr == null )
			return null;

		int inputLen = inputStr.length( );
		if ( inputLen >= length )
			return inputStr;

		int appendLen = length - inputLen;
		char[] appendChar = new char[appendLen];
		for ( int i = 0; i < appendLen; i++ )
		{
			appendChar[i] = ' ';
		}

		String result;
		if ( appendToTail == true )
			result = inputStr + new String( appendChar );
		else
			result = new String( appendChar ) + inputStr;

		return result;
	}

}
