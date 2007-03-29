/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.script.data;

import org.eclipse.birt.chart.script.api.data.ISeriesGrouping;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class SeriesGroupingTest extends BaseChartTestCase
{

	public void testGroupInterval( )
	{
		ISeriesGrouping grouping = getChartWithoutAxes( ).getCategory( )
				.getGrouping( );

		assertEquals( grouping.getGroupInterval( ), 0 );

		grouping.setGroupInterval( 1 );
		assertEquals( grouping.getGroupInterval( ), 1 );
	}

	public void testGroupType( )
	{
		ISeriesGrouping grouping = getChartWithoutAxes( ).getCategory( )
				.getGrouping( );

		assertEquals( grouping.getGroupType( ), "Text" );

		grouping.setGroupType( "Numeric" );
		assertEquals( "Test setting group type",
				grouping.getGroupType( ),
				"Numeric" );

		grouping.setGroupType( "Num" );
		assertEquals( "Test invalid group type",
				grouping.getGroupType( ),
				"Numeric" );
	}

	public void testGroupUnit( )
	{
		ISeriesGrouping grouping = getChartWithoutAxes( ).getCategory( )
				.getGrouping( );

		assertEquals( grouping.getGroupUnit( ), "Seconds" );

		grouping.setGroupUnit( "Days" );
		assertEquals( "Test setting group unit",
				grouping.getGroupUnit( ),
				"Days" );

		grouping.setGroupUnit( "dd" );
		assertEquals( "Test invalid group unit",
				grouping.getGroupUnit( ),
				"Seconds" );
	}

	public void testEnabled( )
	{
		ISeriesGrouping grouping = getChartWithoutAxes( ).getCategory( )
				.getGrouping( );

		assertEquals( grouping.isEnabled( ), true );

		grouping.setEnabled( false );
		assertEquals( grouping.isEnabled( ), false );
	}
}
