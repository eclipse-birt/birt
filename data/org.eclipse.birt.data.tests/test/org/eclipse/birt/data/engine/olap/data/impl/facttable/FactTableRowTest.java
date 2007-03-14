
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;

import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionKey;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRow;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;

import junit.framework.TestCase;

/**
 * 
 */

public class FactTableRowTest extends TestCase
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}
	
	public void testSaveAndLoad() throws IOException
	{
		final int rowLen = 100000;
		DiskSortedStack result = new DiskSortedStack( Constants.FACT_TABLE_BUFFER_SIZE,
				true,
				false,
				FactTableRow.getCreator( ) );
		for(int i=rowLen-1;i>=0;i--)
		{
			result.push( createRow(i) );
		}
		for(int i=0;i<rowLen;i++)
		{
			checkEquals( (FactTableRow) result.pop( ), createRow( i ) );
		}
	}
	
	public void testSaveAndLoad2() throws IOException
	{
		final int rowLen = 100000;
		DiskSortedStack result = new DiskSortedStack( Constants.FACT_TABLE_BUFFER_SIZE,
				true,
				false,
				FactTableRow.getCreator( ) );
		for(int i=rowLen-1;i>=0;i--)
		{
			result.push( createRow2(i) );
		}
		for(int i=0;i<rowLen;i++)
		{
			checkEquals( (FactTableRow) result.pop( ), createRow2( i ) );
		}
	}
	
	private void checkEquals( FactTableRow factTableRow1,
			FactTableRow factTableRow2 )
	{
		assertEquals( factTableRow1, factTableRow2 );
		assertEquals( factTableRow1.measures.length,
				factTableRow2.measures.length );
		for ( int i = 0; i < factTableRow2.measures.length; i++ )
		{
			assertEquals( factTableRow1.measures[i], factTableRow2.measures[i] );
		}
	}
	
	private FactTableRow createRow( int iValue )
	{
		final int dimensionLen = 3;
		final int measureLen = 3;
		FactTableRow factTableRow = new FactTableRow( );
		factTableRow.dimensionKeys = new DimensionKey[dimensionLen];
		for ( int i = 0; i < dimensionLen; i++ )
		{
			factTableRow.dimensionKeys[i] = 
				new DimensionKey( i );
			for( int j=0;j<i;j++)
			{
				factTableRow.dimensionKeys[i].keyValues[j] = new Integer(iValue+j);
			}
		}
		factTableRow.measures = new Object[measureLen];
		for ( int i = 0; i < measureLen; i++ )
		{
			factTableRow.measures[i] = new Integer(iValue+i);
		}
		return factTableRow;
	}
	
	private FactTableRow createRow2( int iValue )
	{
		final int dimensionLen = 3;
		final int measureLen = 3;
		FactTableRow factTableRow = new FactTableRow( );
		factTableRow.dimensionKeys = new DimensionKey[dimensionLen];
		for ( int i = 0; i < dimensionLen; i++ )
		{
			factTableRow.dimensionKeys[i] = 
				new DimensionKey( i );
			for ( int j = 0; j < i; j++ )
			{
				if ( j != 1 )
					factTableRow.dimensionKeys[i].keyValues[j] =
							new Integer( iValue + j );
			}
		}
		factTableRow.measures = new Object[measureLen];
		for ( int i = 0; i < measureLen; i++ )
		{
			factTableRow.measures[i] = new Integer( iValue + i );
		}
		return factTableRow;
	}
}
