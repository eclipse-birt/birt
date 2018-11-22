
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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


import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.Row4Aggregation;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * 
 */

public class Row4AggregationTest {
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * @see TestCase#tearDown()
	 */
@Test
    public void testDiskIndexBytes( ) throws IOException, DataException
	{
		int keyNumber = 120;
		BufferedStructureArray keyList = new BufferedStructureArray( Row4Aggregation.getCreator( ),
				10 );
		Row4Aggregation key = null;
		for ( int i = 0; i < keyNumber; i++ )
		{
			key = new Row4Aggregation( );
			Member member = new Member( );
			member.setKeyValues( new Object[]{ "str" + i } );
			key.setLevelMembers( new Member[]{ member } );
			key.setMeasures( new Object[]{ new Integer( i ) } );
			key.setParameterValues( new Object[]{ new Double( i ) } );
			key.setDimPos( new int[]{ i, i+1, i+3 } );
			keyList.add( key );
		}
		for ( int i = 0; i < keyNumber; i++ )
		{
			 System.out.println( i );
			key = (Row4Aggregation) keyList.get( i );
			Member member = key.getLevelMembers( )[0];
			assertEquals( member.getKeyValues( )[0], "str" + i );
			assertEquals( key.getMeasures( )[0], new Integer( i ) );
			assertEquals( key.getParameterValues( )[0], new Double( i ) );
			int[] dimPos = key.getDimPos( );
			assertEquals( dimPos[0], i );
			assertEquals( dimPos[1], i + 1 );
			assertEquals( dimPos[2], i + 3 );
		}
		keyList.clear( );
		keyList.close( );
	}
}
