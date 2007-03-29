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

package org.eclipse.birt.chart.tests.engine.computation;

import junit.framework.TestCase;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.withaxes.ScaleContext;

/**
 * 
 */

public class ScaleContextTest extends TestCase
{

	public void testLinearWithoutFixed( )
	{
		// Without fixed value
		ScaleContext scale = new ScaleContext( 0,
				IConstants.LINEAR,
				new Integer( 0 ),
				new Integer( 5 ),
				new Integer( 1 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 0 ), scale.getMin( ) );
		assertEquals( new Double( 6 ), scale.getMax( ) );
		assertEquals( null, scale.getRealMin( ) );
		assertEquals( null, scale.getRealMax( ) );

		scale = new ScaleContext( 0,
				IConstants.LINEAR,
				new Integer( 0 ),
				new Integer( 5 ),
				new Double( 1.2 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 0 ), scale.getMin( ) );
		assertEquals( new Double( 6 ), scale.getMax( ) );

		scale = new ScaleContext( 0,
				IConstants.LINEAR,
				new Integer( 0 ),
				new Integer( 6 ),
				new Double( 1.2 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 0 ), scale.getMin( ) );
		assertEquals( new Double( 7.2 ), scale.getMax( ) );

		scale = new ScaleContext( 0,
				IConstants.LINEAR,
				new Integer( 1 ),
				new Integer( 6 ),
				new Double( 1.5 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 0 ), scale.getMin( ) );
		assertEquals( new Double( 7.5 ), scale.getMax( ) );

		scale = new ScaleContext( 0,
				IConstants.LINEAR,
				new Integer( -1 ),
				new Integer( 6 ),
				new Double( 1.5 ) );
		scale.computeMinMax( );
		assertEquals( new Double( -1.5 ), scale.getMin( ) );
		assertEquals( new Double( 7.5 ), scale.getMax( ) );
	}

	public void testLinearWithFixed( )
	{
		// With fixed value
		ScaleContext scale = new ScaleContext( 0,
				IConstants.LINEAR,
				new Integer( 0 ),
				new Integer( 5 ),
				new Integer( 1 ) );
		scale.setFixedValue( true, false, new Double( 1 ), null );
		scale.computeMinMax( );
		assertEquals( new Double( 1 ), scale.getMin( ) );
		assertEquals( new Double( 6 ), scale.getMax( ) );
		assertEquals( null, scale.getRealMin( ) );
		assertEquals( null, scale.getRealMax( ) );

		scale = new ScaleContext( 0,
				IConstants.LINEAR,
				new Integer( 0 ),
				new Integer( 5 ),
				new Double( 1.2 ) );
		scale.setFixedValue( true, true, new Double( 1 ), new Double( 5 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 1 ), scale.getMin( ) );
		assertEquals( new Double( 5 ), scale.getMax( ) );
		assertEquals( null, scale.getRealMin( ) );
		assertEquals( null, scale.getRealMax( ) );
		
	}

	public void testLinearWithMargin( )
	{
		// With margin area
		ScaleContext scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Integer( 1 ),
				new Integer( 5 ),
				new Integer( 1 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 0 ), scale.getMin( ) );
		assertEquals( new Double( 6 ), scale.getMax( ) );

		scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Integer( 1 ),
				new Integer( 6 ),
				new Double( 1 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 0 ), scale.getMin( ) );
		assertEquals( new Double( 7 ), scale.getMax( ) );

		scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Double( 0 ),
				new Double( 6 ),
				new Double( 2 ) );
		scale.computeMinMax( );
		assertEquals( new Double( -2 ), scale.getMin( ) );
		assertEquals( new Double( 8 ), scale.getMax( ) );

		scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Double( -1 ),
				new Double( 7 ),
				new Double( 2 ) );
		scale.computeMinMax( );
		assertEquals( new Double( -4 ), scale.getMin( ) );
		assertEquals( new Double( 10 ), scale.getMax( ) );

		scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Double( 3 ),
				new Double( 4 ),
				new Double( 1 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 2 ), scale.getMin( ) );
		assertEquals( new Double( 5 ), scale.getMax( ) );

		scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Double( -4 ),
				new Double( -3 ),
				new Double( 1 ) );
		scale.computeMinMax( );
		assertEquals( new Double( -5 ), scale.getMin( ) );
		assertEquals( new Double( -2 ), scale.getMax( ) );
		
		scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Double( 25.21 ),
				new Double( 27.9 ),
				new Double( 1 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 24 ), scale.getMin( ) );
		assertEquals( new Double( 29 ), scale.getMax( ) );
	}

	public void testLinearWithMarginAndFixed( )
	{
		ScaleContext scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Double( 1 ),
				new Double( 5 ),
				new Double( 1 ) );
		scale.setFixedValue( true, true, new Double( 0 ), new Double( 7 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 0 ), scale.getMin( ) );
		assertEquals( new Double( 7 ), scale.getMax( ) );
		assertEquals( null, scale.getRealMin( ) );
		// Real value is 6, but less than 7
		assertEquals( null, scale.getRealMax( ) );

		scale = new ScaleContext( 20,
				IConstants.LINEAR,
				new Double( 1 ),
				new Double( 5 ),
				new Double( 1 ) );
		scale.setFixedValue( true, true, new Double( 1 ), new Double( 5 ) );
		scale.computeMinMax( );
		assertEquals( new Double( 1 ), scale.getMin( ) );
		assertEquals( new Double( 5 ), scale.getMax( ) );
		assertEquals( 0, Math.round( Methods.asDouble( scale.getRealMin( ) )
				.doubleValue( ) ) );
		assertEquals( 6, Math.round( Methods.asDouble( scale.getRealMax( ) )
				.doubleValue( ) ) );
	}
}
