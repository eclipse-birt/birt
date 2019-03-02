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

package org.eclipse.birt.data.engine.executor.transform;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.Util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The test case for class
 * "org.eclipse.birt.data.engine.executor.CachedResultSet"
 */

public class CachedResultSetTest extends Util.CachedResultSetTestHelper
{
	private CachedResultSet rs;

	/*
	 * @see TestCase#setUp()
	 */
	@Before
    public void cachedResultSetSetUp() throws Exception
	{

		rs = this.getDefaultCachedResultSet( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
    public void cachedResultSetTearDown() throws Exception
	{
		rs.close( );
	}

	/**
	 * Test case for method "first()".
	 * 
	 * @throws DataException
	 */
	@Test
    public void testFirst( ) throws DataException
	{
		for ( int i = 0; i < 3; i++ )
			for ( int j = 0; j < 3; j++ )
				for ( int k = 0; k < 3; k++ )
					for ( int l = 0; l < 3; l++ )
					{
						int id = rs.getCurrentResultIndex( );

						//move cursor to the first row of level 3 group which
						// hosts
						//the current row.
						rs.first( 3 );
						assertEquals( id - l, rs.getCurrentResultIndex( ) );

						//restore the cursor to the position before rs.first(3)
						// is called
						for ( int m = 0; m < l; m++ )
							rs.next( );

						//move cursor to the first row of level 2 group which
						// hosts
						//the current row
						rs.first( 2 );
						assertEquals( id - 3 * k - l,
								rs.getCurrentResultIndex( ) );
						for ( int m = 0; m < 3 * k + l; m++ )
							rs.next( );

						//move cursor to the first row of level 1 group which
						// hosts
						//the current row
						rs.first( 1 );
						assertEquals( id - 3 * k - 9 * j - l,
								rs.getCurrentResultIndex( ) );
						for ( int m = 0; m < 9 * j + 3 * k + l; m++ )
							rs.next( );

						rs.first( 0 );
						assertEquals( 0, rs.getCurrentResultIndex( ) );
						for ( int m = 0; m < 27 * i + 9 * j + 3 * k + l; m++ )
							rs.next( );
						rs.next( );
					}
		try
		{
			rs.first( -1 );
		}
		catch ( DataException e )
		{
			assertEquals( e.getErrorCode(), ResourceConstants.INVALID_GROUP_LEVEL );
		}
		try
		{
			rs.first( 5 );
		}
		catch ( DataException e )
		{
			assertEquals( e.getErrorCode( ), ResourceConstants.INVALID_GROUP_LEVEL);
		}
	}

	/**
	 * Test case for method "last()"
	 * 
	 * @throws DataException
	 */
	@Test
    public void testLast( ) throws DataException
	{
		for ( int i = 0; i < 3; i++ )
			for ( int j = 0; j < 3; j++ )
				for ( int k = 0; k < 3; k++ )
					for ( int l = 0; l < 3; l++ )
					{
						//move cursor to the last row of level 3 group which
						// hosts
						//the current row.
						rs.last( 3 );
						assertEquals( 27 * i + 9 * j + 3 * k + 2,
								rs.getCurrentResultIndex( ) );
						//restore the cursor to the position before rs.last(3)
						// is called
						rs.first( 3 );
						for ( int m = 0; m < l; m++ )
							rs.next( );

						//move cursor to the last row of level 2 group which
						// hosts
						//the current row
						rs.last( 2 );
						assertEquals( 27 * i + 9 * j + 8,
								rs.getCurrentResultIndex( ) );
						rs.first( 2 );
						for ( int m = 0; m < 3 * k + l; m++ )
							rs.next( );

						//move cursor to the last row of level 1 group which
						// hosts
						//the current row
						rs.last( 1 );
						assertEquals( 27 * i + 26, rs.getCurrentResultIndex( ) );
						rs.first( 1 );
						for ( int m = 0; m < 9 * j + 3 * k + l; m++ )
							rs.next( );

						//move cursor to the begin of whole list
						rs.last( 0 );
						assertEquals( 80, rs.getCurrentResultIndex( ) );
						rs.first( 0 );
						for ( int m = 0; m < 27 * i + 9 * j + 3 * k + l; m++ )
							rs.next( );

						//move forward cursor
						rs.next( );
					}
		try
		{
			rs.last( -1 );
		}
		catch ( DataException e )
		{
			assertEquals( e.getErrorCode( ), ResourceConstants.INVALID_GROUP_LEVEL );
		}
		try
		{
			rs.last( 5 );
		}
		catch ( DataException e )
		{
			assertEquals( e.getErrorCode( ), ResourceConstants.INVALID_GROUP_LEVEL );
		}
	}

	/**
	 * Test case for method "getCurrentResult()".
	 * 
	 * @throws DataException
	 */
	@Test
    public void testGetCurrentResult( ) throws Exception
	{
		for ( int i = 0; i < 3; i++ )
			for ( int j = 0; j < 3; j++ )
				for ( int k = 0; k < 3; k++ )
					for ( int l = 0; l < 3; l++ )
					{
						assertEquals( DataTypeUtil.toInteger( rs.getCurrentResult( )
								.getFieldValue( "COL0" ) ) //$NON-NLS-1$
								.intValue( ),
								i );
						assertEquals( DataTypeUtil.toInteger( rs.getCurrentResult( )
								.getFieldValue( "COL1" ) ) //$NON-NLS-1$
								.intValue( ),
								j );
						assertEquals( DataTypeUtil.toInteger( rs.getCurrentResult( )
								.getFieldValue( "COL2" ) ) //$NON-NLS-1$
								.intValue( ),
								k );
						assertEquals( DataTypeUtil.toInteger( rs.getCurrentResult( )
								.getFieldValue( "COL3" ) ) //$NON-NLS-1$
								.intValue( ),
								l );
						rs.next( );
					}
	}

	/**
	 * Test case for method "getCurrentResultIndex()".
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGetCurrentResultIndex( ) throws Exception
	{
		int i = 0;
		while ( rs.next( ) )
		{
			i++;
			assertEquals( rs.getCurrentResultIndex( ), i );
		}
	}

	/**
	 * Test case for method "getStartingGroupLevel()".
	 * 
	 * @throws DataException
	 */
	@Test
    public void testGetStartingGroupLevel( ) throws DataException
	{
		int[] expectedResult = this.prepareTestDataForGetStartingGroupLevel( );

		//test rs.getStartingGroupLevel()
		int counter = 0;
		assertEquals( rs.getStartingGroupLevel( ), expectedResult[counter] );
		while ( rs.next( ) )
		{
			assertEquals( rs.getStartingGroupLevel( ),
					expectedResult[++counter] );
		}

		//test exception
		try
		{
			rs.getStartingGroupLevel( );
		}
		catch ( DataException e )
		{
			assertEquals( e.getMessage( ), DataResourceHandle.getInstance( )
					.getMessage( ResourceConstants.NO_CURRENT_ROW ) );
		}
	}

	/**
	 * Test case for method "getEndingGroupLevel()"
	 * 
	 * @throws DataException
	 */
	@Test
    public void testGetEndingGroupLevel( ) throws DataException
	{
		int[] expectedResult = this.prepareTestDataForGetEndingGroupLevel( );
		int counter = 0;
		assertEquals( rs.getEndingGroupLevel( ), expectedResult[counter] );
		while ( rs.next( ) )
		{
			assertEquals( rs.getEndingGroupLevel( ), expectedResult[++counter] );
		}

		//test exception
		try
		{
			rs.getEndingGroupLevel( );
		}
		catch ( DataException e )
		{
			assertEquals( e.getMessage( ), DataResourceHandle.getInstance( )
					.getMessage( ResourceConstants.NO_CURRENT_ROW ) );
		}
	}

	/**
	 * Test case for method "getGroupCount()";
	 * 
	 * @throws DataException
	 *//*
	@Test
    public void testGetGroupCount( ) throws DataException
	{
		for ( int i = 0; i < 3; i++ )
			for ( int j = 0; j < 3; j++ )
				for ( int k = 0; k < 3; k++ )
					for ( int t = 0; t < 3; t++ )
					{
						assertEquals( rs.getGroupCount( ), 3 );
						rs.next( );
					}
	}*/

	/**
	 * Test case for method "getCurrentGroupData()".
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGetGroupData( ) throws Exception
	{
		CachedResultSet rs2 = this.getDefaultSubQueryCachedResultSet( );
		for ( int i = 0; i < 3; i++ )
			for ( int j = 0; j < 3; j++ )
			{
				assertEquals( DataTypeUtil.toInteger( rs2.getCurrentResult( )
						.getFieldValue( "COL0" ) ).intValue( ), 0 ); //$NON-NLS-1$
				assertEquals( DataTypeUtil.toInteger( rs2.getCurrentResult( )
						.getFieldValue( "COL1" ) ).intValue( ), 0 ); //$NON-NLS-1$
				assertEquals( DataTypeUtil.toInteger( rs2.getCurrentResult( )
						.getFieldValue( "COL2" ) ).intValue( ), i ); //$NON-NLS-1$
				assertEquals( DataTypeUtil.toInteger( rs2.getCurrentResult( )
						.getFieldValue( "COL3" ) ).intValue( ), j ); //$NON-NLS-1$
				rs2.next( );
			}
	}

	/**
	 * Parpare the test data for test case "testGetEndingGroupLevel()".
	 * 
	 * @return int[] the array which contains the expected values that are
	 *         returned by method "getEndingGroupLevel()".
	 */
	private int[] prepareTestDataForGetEndingGroupLevel( )
	{
		int[] expectedResult = new int[81];
		for ( int i = 0; i < 81; i++ )
			expectedResult[i] = 4;
		int counter = -1;
		for ( int i = 0; i < 3; i++ )
		{
			for ( int j = 0; j < 3; j++ )
			{
				for ( int k = 0; k < 3; k++ )
				{
					for ( int l = 0; l < 3; l++ )
					{
						++counter;
					}
					expectedResult[counter] = 3;
				}
				expectedResult[counter] = 2;
			}
			expectedResult[counter] = 1;
		}
		expectedResult[expectedResult.length - 1] = 0;
		return expectedResult;
	}

	/**
	 * Parpare the test data for test case "testGetStartingGroupLevel()".
	 * 
	 * @return int[] the array which contains the expected values that are
	 *         returned by method "getStartingGroupLevel()".
	 */
	private int[] prepareTestDataForGetStartingGroupLevel( )
	{
		int[] expectedResult = new int[81];
		for ( int i = 0; i < 81; i++ )
			expectedResult[i] = 4;
		int counter = 0;
		for ( int i = 0; i < 3; i++ )
		{
			expectedResult[counter] = 1;
			for ( int j = 0; j < 3; j++ )
			{
				if ( expectedResult[counter] == 4 )
					expectedResult[counter] = 2;
				for ( int k = 0; k < 3; k++ )
				{
					if ( expectedResult[counter] == 4 )
						expectedResult[counter] = 3;
					for ( int l = 0; l < 3; l++ )
					{
						counter++;
					}
				}
			}
		}
		expectedResult[0] = 0;
		return expectedResult;
	}
	
}
