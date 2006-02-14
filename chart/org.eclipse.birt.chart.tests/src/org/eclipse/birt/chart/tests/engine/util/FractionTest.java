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

package org.eclipse.birt.chart.tests.engine.util;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.eclipse.birt.chart.util.Fraction;

/**
 * 
 */

public class FractionTest extends TestCase
{

	/**
	 * Provides the ability to run the tests contained here in.
	 * 
	 * @param args
	 *            Command line arguements.
	 */
	public static void main( String args[] )
	{
		TestRunner.run( FractionTest.class );
	}

	public void testConstructorPrecise( )
	{
		Fraction f = new Fraction( 0.02 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 50 );

		f = new Fraction( 0.333333333333 );
		assertEquals( f.getNumerator( ), 33333333 );
		assertEquals( f.getDenominator( ), 100000000 );
	}

	public void testConstructorFixedNumerator( )
	{
		Fraction f = new Fraction( 0.02, 2 );
		assertEquals( f.getNumerator( ), 2 );
		assertEquals( f.getDenominator( ), 100 );

		f = new Fraction( 2.0, 1 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 0 );

		f = new Fraction( 3.5, 1 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 0 );
	}

	public void testConstructorMaxRecursionTime( )
	{
		Fraction f = new Fraction( 0.17, (short) 1 );
		assertEquals( f.getNumerator( ), 0 );
		assertEquals( f.getDenominator( ), 1 );

		f = new Fraction( 0.17, (short) 2 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 5 );

		f = new Fraction( 0.17, (short) 3 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 6 );
	}

	public void testToString( )
	{
		Fraction f = new Fraction( 0.02, 2 );
		assertEquals( f.toString( ":" ), "2:100" ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( f.toString( ), "2/100" ); //$NON-NLS-1$
	}

	public void testEvaluate( )
	{
		double decimal = 0.02;
		Fraction f = new Fraction( decimal, 1 );
		assertEquals( f.evaluate( ), decimal, Double.POSITIVE_INFINITY );
	}

	public void testPlus( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f1.plus( f2 );
		assertEquals( f1, f3 );
	}

	public void testMinus( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f3.minus( f2 );
		assertEquals( f3, f1 );
	}

	public void testTimes( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f1.times( f3 );
		assertEquals( f1, f2 );
	}

	public void testDivide( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f2.divide( f1 );
		assertEquals( f2, f3 );
	}

	public void testInvert( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 3, 1 );
		f1.invert( );
		assertEquals( f1, f2 );
	}

	public void testNegate( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( -1, 3 );
		f1.negate( );
		assertEquals( f1, f2 );
	}
}
