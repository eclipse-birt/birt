/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor.cache;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.perf.util.SizeOfUtil;

import com.ibm.icu.util.Calendar;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Test the function of SizeOfUtil
 */
public class SizeOfUtilTest {
	private ResultClass resultClass;
	private org.eclipse.birt.data.engine.executor.cache.SizeOfUtil sizeOfUtil;
	
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void sizeOfUtilsSetUp() throws DataException
	{
		resultClass = getResultClass( );
		sizeOfUtil = new org.eclipse.birt.data.engine.executor.cache.SizeOfUtil( resultClass );
	}
	
	/**
	 * @throws DataException
	 */
	@Test
    public void testSizeOfUtil( ) throws DataException
	{

		String x = System.getProperty( "java.version" );
//		if ( System.getProperty( "java.version" ).startsWith( "1.8" ) )
//		{
			runWithJDK18( );
//		}
//		else
//		{
//			runWithJDK14( );
//		}
	}

	/**
	 * @throws DataException
	 */
	private void runWithJDK14( ) throws DataException
	{
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 1, 1 ) ), 304 );
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 3, 3 ) ), 312 );
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 3, 7 ) ), 320 );
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 5, 8 ) ), 320 );
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 12, 7 ) ), 336 );
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 7, 12 ) ), 328 );
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 18, 22 ) ), 360 );
		assertEquals( sizeOfUtil.sizeOf( getResultObject( 38, 42 ) ), 416 );

		assertEquals( sizeOfUtil.sizeOf( getResultObjectWithNull( 3, 3, new int[]{
				2, 4, 6, 7
		} ) ), 144 );
		assertEquals( sizeOfUtil.sizeOf( getResultObjectWithNull( 3, 3, new int[]{
				2, 4
		} ) ), 208 );
		assertEquals( sizeOfUtil.sizeOf( getResultObjectWithNull( 3, 7, new int[]{
				2, 7
		} ) ), 192 );
		assertEquals( sizeOfUtil.sizeOf( getResultObjectWithNull( 5, 8, new int[]{
				3, 6
		} ) ), 272 );
		assertEquals( sizeOfUtil.sizeOf( getResultObjectWithNull( 12, 7, new int[]{
				1, 4, 5
		} ) ), 272 );
	}

	/**
	 * @throws DataException
	 */
	private void runWithJDK18( ) throws DataException
	{
		assertEquals( 520, sizeOfUtil.sizeOf( getResultObject( 1, 1 ) ) );
		assertEquals( 528, sizeOfUtil.sizeOf( getResultObject( 3, 3 ) ) );
		assertEquals( 536, sizeOfUtil.sizeOf( getResultObject( 3, 7 ) ) );
		assertEquals( 536, sizeOfUtil.sizeOf( getResultObject( 5, 8 ) ) );
		assertEquals( 552, sizeOfUtil.sizeOf( getResultObject( 12, 7 ) ) );
		assertEquals( 544, sizeOfUtil.sizeOf( getResultObject( 7, 12 ) ) );
		assertEquals( 576, sizeOfUtil.sizeOf( getResultObject( 18, 22 ) ) );
		assertEquals( 632, sizeOfUtil.sizeOf( getResultObject( 38, 42 ) ) );

		assertEquals( 192, sizeOfUtil.sizeOf( getResultObjectWithNull( 3, 3, new int[]{
				2, 4, 6, 7
		} ) ) );
		assertEquals( 280, sizeOfUtil.sizeOf( getResultObjectWithNull( 3, 3, new int[]{
				2, 4
		} ) ) );
		assertEquals( 256, sizeOfUtil.sizeOf( getResultObjectWithNull( 3, 7, new int[]{
				2, 7
		} ) ) );
		assertEquals( 472, sizeOfUtil.sizeOf( getResultObjectWithNull( 5, 8, new int[]{
				3, 6
		} ) ) );
		assertEquals( 464, sizeOfUtil.sizeOf( getResultObjectWithNull( 12, 7, new int[]{
				1, 4, 5
		} ) ) );
	}

	/**
	 * @return
	 * @throws DataException 
	 */
	private ResultClass getResultClass( ) throws DataException
	{
		ArrayList columnList = new ArrayList( );
		
		ResultFieldMetadata metaData = null;
		metaData = new ResultFieldMetadata( 1,
				"1",
				"1",
				Integer.class,
				Integer.class.getName( ),
				false );
		columnList.add( metaData );
		metaData = new ResultFieldMetadata( 2,
				"2",
				"2",
				Double.class,
				Double.class.getName( ),
				false );
		columnList.add( metaData );
		metaData = new ResultFieldMetadata( 3,
				"3",
				"3",
				BigDecimal.class,
				BigDecimal.class.getName( ),
				false );
		columnList.add( metaData );
		metaData = new ResultFieldMetadata( 4,
				"4",
				"4",
				Date.class,
				Date.class.getName( ),
				false );
		columnList.add( metaData );
		metaData = new ResultFieldMetadata( 5,
				"5",
				"5",
				Time.class,
				Time.class.getName( ),
				false );
		columnList.add( metaData );
		metaData = new ResultFieldMetadata( 6,
				"6",
				"6",
				Timestamp.class,
				Timestamp.class.getName( ),
				false );
		columnList.add( metaData );
		metaData = new ResultFieldMetadata( 7,
				"7",
				"7",
				byte[].class,
				byte[].class.getName( ),
				false );
		columnList.add( metaData );
		metaData = new ResultFieldMetadata( 8,
				"8",
				"8",
				String.class,
				String.class.getName( ),
				false );
		columnList.add( metaData );

		ResultClass reClass = new ResultClass( columnList );

		return reClass;
	}

	/**
	 * @param strinLen
	 * @param byteLen
	 * @return
	 */
	private ResultObject getResultObject( int strLen, int byteLen )
	{
		return getResultObjectWithNull( strLen, byteLen, new int[]{} );
	}
	
	/**
	 * @param strLen
	 * @param byteLen
	 * @param nullPos
	 * @return
	 */
	private ResultObject getResultObjectWithNull( int strLen,
			int byteLen, int[] nullPos )
	{
		Object[] objectArray = new Object[8];
		Calendar calendar = Calendar.getInstance( );
		// constant size
		objectArray[0] = new Integer( 10 );
		objectArray[1] = new Double( 10 );
		objectArray[2] = new BigDecimal( "1111111111111111111111111111" );
		
		calendar.clear( );
		calendar.set( 1919, 2, 2 );
		objectArray[3] =calendar.getTime( );
		
		calendar.clear( );
		calendar.set( 1970, 0, 1, 19, 2, 2 );
		objectArray[4] = new Time( calendar.getTimeInMillis( ) );
		
		calendar.clear( );
		calendar.set( 1919, 2, 2, 2, 2, 2 );
		objectArray[5] = new Timestamp( calendar.getTimeInMillis( ) );
		((Timestamp)objectArray[5]).setNanos( 2 );
		
		// variable size
		objectArray[6] = new byte[byteLen];
		objectArray[7] = SizeOfUtil.newString( strLen );
		
		// set null for object element
		for ( int i = 0; i < nullPos.length; i++ )
			objectArray[nullPos[i]] = null;

		ResultObject object = new ResultObject( resultClass, objectArray );

		return object;
	}
	
}
