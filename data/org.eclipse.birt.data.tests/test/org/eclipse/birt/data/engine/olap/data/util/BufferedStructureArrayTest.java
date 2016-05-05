
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
package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;


/**
 * 
 */

public class BufferedStructureArrayTest {
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * @see TestCase#tearDown()
	 */
@Test
    public void testMemberForTest( ) throws IOException
	{
		int objectNumber = 1001;
		BufferedStructureArray list = new BufferedStructureArray( MemberForTest.getMemberCreator( ),200 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			list.add( createMember( i ) );
		}
		assertEquals( list.size( ), objectNumber );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( list.get( i ), createMember( i ) );
		}
		list.close( );
	}
	@Test
    public void testMemberForTest1( ) throws IOException
	{
		int objectNumber = 10001;
		BufferedStructureArray list = new BufferedStructureArray( MemberForTest.getMemberCreator( ),200 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			list.add( createMember( i ) );
			try
			{
				list.get( i+1 );
				fail();
			}
			catch(IndexOutOfBoundsException e)
			{
			}
		}
		assertEquals( list.size( ), objectNumber );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( list.get( i ), createMember( i ) );
		}
		list.close( );
	}
	@Test
    public void testMemberForTest2( ) throws IOException
	{
		int objectNumber1 = 5401;
		int objectNumber2 = 2000;
		BufferedStructureArray list = new BufferedStructureArray( MemberForTest.getMemberCreator( ),200 );
		for ( int i = 0; i < objectNumber1; i++ )
		{
			list.add( createMember( i ) );
		}
		assertEquals( list.size( ), objectNumber1 );
		for ( int i = 0; i < objectNumber1; i++ )
		{
			assertEquals( list.get( i ), createMember( i ) );
		}
		for ( int i = 0; i < objectNumber2; i++ )
		{
			list.add( createMember( i ) );
		}
		assertEquals( list.size( ), objectNumber1 + objectNumber2 );
		for ( int i = 0; i < objectNumber2; i++ )
		{
			assertEquals( list.get( objectNumber1 + i ), createMember( i ) );
		}
		list.close( );
	}
	@Test
    public void testMemberForTest3( ) throws IOException
	{
		int objectNumber = 10001;
		BufferedStructureArray list = new BufferedStructureArray( MemberForTest.getMemberCreator( ),200 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			list.add( createMember( i ) );
			assertEquals( list.size( ), i + 1 );
			assertEquals( list.get( i ), createMember( i ) );
		}
		list.close( );
	}
	@Test
    public void testStress( ) throws IOException
	{
		long startTime = System.currentTimeMillis( );
		int objectNumber = 100000;
		BufferedStructureArray list = new BufferedStructureArray( MemberForTest.getMemberCreator( ),200 );
		for ( int i = 0; i < objectNumber; i++ )
		{
			list.add( createMember( i ) );
		}
		System.out.println( "used add:" +(System.currentTimeMillis( )-startTime)/100);
		assertEquals( list.size( ), objectNumber );
		for ( int i = 0; i < objectNumber; i++ )
		{
			assertEquals( list.get( i ), createMember( i ) );
		}
		System.out.println( "used get:" +(System.currentTimeMillis( )-startTime)/100);
		list.close( );
	}
	
	static private MemberForTest createMember( int i )
	{
		int iField = i;
		Date dateField = new Date( 190001000 + i * 1000 );
		String stringField = "string" + i;
		double doubleField = i + 10.0;
		BigDecimal bigDecimalField = new BigDecimal( "1010101010100101010110"
				+ i );
		boolean booleanField = ( i % 2 == 0 ? true : false );
		return new MemberForTest( iField,
				dateField,
				stringField,
				doubleField,
				bigDecimalField,
				booleanField );
	}
}
