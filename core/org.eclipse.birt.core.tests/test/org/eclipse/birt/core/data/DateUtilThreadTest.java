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

package org.eclipse.birt.core.data;

import org.junit.Test;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 * 
 */

public class DateUtilThreadTest extends TestCase
{
	@Test
    public void test() throws InterruptedException
	{
		TestThread[] tt = new TestThread[20];

		Thread[] threadArray = new Thread[20];
		for ( int i = 0; i < threadArray.length; i++ )
		{
			tt[i] = new TestThread( );
			threadArray[i] = new Thread( tt[i] );
			threadArray[i].start( );
		}
		long startTime = System.currentTimeMillis( );
		System.out.println( "starting...   " + startTime );
		while ( true )
		{
			boolean allThreadFinished = true;
			for ( int i = 0; i < threadArray.length; i++ )
			{
				if ( !(tt[i].status != 0) )
				{
					allThreadFinished = false;
					if( tt[i].status == -1 )
						fail( "Should not arrive here");
				}
			}
			if ( allThreadFinished )
			{
				break;
			}
			Thread.sleep( 1000 );
		}
		
		long endTime = System.currentTimeMillis( );
		System.out.println( "finished   " + endTime );
		System.out.println( "Used:   " + (endTime - startTime) );
	}
}

class TestThread implements Runnable
{
	public int status = 0;

	static String[] dateStrings = {
			"Jan 11, 1952", "1981", "1981-02-20 11:12:55.123"
	};

	public void run( )
	{
		for ( int k = 0; k < 10000; k++ )
		{
			// System.out.println( Thread.currentThread( ).getName( )
			// + " is running" );
			for ( int i = 0; i < dateStrings.length; i++ )
			{
				try
				{
					DataTypeUtil.toDate( dateStrings[i],ULocale.US );
				}
				catch ( Exception e1 )
				{
					status = -1;
				}
			}
		}
		if( status == 0 )
			status = 1;
	}
}
