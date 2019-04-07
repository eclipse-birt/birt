
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache;


/**
 * 
 */

public class ResultSetUtilTest {
/*	public void testCreateLeadingBytes( )
	{
		byte[] indicator1 = {0,0,0,0,0};
		assertTrue( twoByteArrayEqual( ResultSetUtil.createLeadingBytes( indicator1 ),
				new byte[]{
					0
				} ) );
		byte[] indicator2 = {0,1,0,0 };
		assertTrue( twoByteArrayEqual( ResultSetUtil.createLeadingBytes( indicator2 ),
				new byte[]{
					80
				} ) );
		byte[] indicator3 = {0,1,0,1,1,1,0,0,0,0,0,0,0,1 };
		assertTrue( twoByteArrayEqual( ResultSetUtil.createLeadingBytes( indicator3 ),
				new byte[]{
					87,1
				} ) );
		
		byte[] indicator4 = {0,1,0,1,1,1,0,0,0,0,0,0,0,1,1,0 };
		assertTrue( twoByteArrayEqual( ResultSetUtil.createLeadingBytes( indicator4 ),
				new byte[]{
					87,1,-128
				} ) );
		
		byte[] indicator5 = {0,1,0,1,1,2,0,0,0,0,0,0,0,1,2,0 };
		assertTrue( twoByteArrayEqual( ResultSetUtil.createLeadingBytes( indicator5 ),
				new byte[]{
					-60,88,0,6,0
				} ) );
		
		byte[] indicator6 = {0,2,0,2,2,2,0,0,0,0,0,0,0,2,2,0 };
		assertTrue( twoByteArrayEqual( ResultSetUtil.createLeadingBytes( indicator6 ),
				new byte[]{
					-105,1,-128
				} ) );
	}
	@Test
    public void testReadLeadingBytes( )
	{
		byte[] indicator1 = new byte[]{0};
		assertTrue( twoByteArrayEqual( ResultSetUtil.readLeadingBytes( indicator1, 5 ),
				new byte[]{0,0,0,0,0} ) );
		
		byte[] indicator2 = { 80 };
		assertTrue( twoByteArrayEqual( ResultSetUtil.readLeadingBytes( indicator2, 4 ),
				new byte[]{
					0,1,0,0
				} ) );
		
		byte[] indicator3 = { 87,1 };
		assertTrue( twoByteArrayEqual( ResultSetUtil.readLeadingBytes( indicator3, 14 ),
				new byte[]{0,1,0,1,1,1,0,0,0,0,0,0,0,1 } ) );
		
		byte[] indicator4 = {87,1,-128};
		assertTrue( twoByteArrayEqual( ResultSetUtil.readLeadingBytes( indicator4, 16 ),
				new byte[]{0,1,0,1,1,1,0,0,0,0,0,0,0,1,1,0 } ) );
		
		byte[] indicator5 = {-60,88,0,6,0};
		assertTrue( twoByteArrayEqual( ResultSetUtil.readLeadingBytes( indicator5, 16 ),
				new byte[]{0,1,0,1,1,2,0,0,0,0,0,0,0,1,2,0 } ) );
		
		byte[] indicator6 = { -105,1,-128	};
		assertTrue( twoByteArrayEqual( ResultSetUtil.readLeadingBytes( indicator6, 16 ),
				new byte[] {0,2,0,2,2,2,0,0,0,0,0,0,0,2,2,0 }) );
	}*/
	private boolean twoByteArrayEqual( byte[] array1, byte[] array2)
	{
		if( array1.length!= array2.length )
			return false;
		for( int i = 0; i < array1.length; i++ )
		{
			if( array1[i] != array2[i] )
				return false;
		}
		return true;
	}
}
