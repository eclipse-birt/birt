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

import org.eclipse.birt.chart.internal.model.Fraction;
import org.eclipse.birt.chart.internal.model.FractionApproximator;

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
		Fraction f = FractionApproximator.getExactFraction( 0.02 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 50 );

		f = FractionApproximator.getExactFraction( 0.333333333333 );
		assertEquals( f.getNumerator( ), 33333333 );
		assertEquals( f.getDenominator( ), 100000000 );
		
		f = FractionApproximator.getExactFraction( -0.333333333333 );
		assertEquals( f.getNumerator( ), -33333333 );
		assertEquals( f.getDenominator( ), 100000000 );
		
		f = FractionApproximator.getExactFraction( -0.33 );
		assertEquals( f.getNumerator( ), -33 );
		assertEquals( f.getDenominator( ), 100 );
	}

	public void testConstructorFixedNumerator( )
	{
		Fraction f = FractionApproximator.getFractionWithNumerator( 0.02, 2 );
		assertEquals( f.getNumerator( ), 2 );
		assertEquals( f.getDenominator( ), 100 );

		f = FractionApproximator.getFractionWithNumerator( 2.0, 1 );
		assertEquals( f.getNumerator( ), 2 );
		assertEquals( f.getDenominator( ), 1 );

		f = FractionApproximator.getFractionWithNumerator( 3.5, 1 );
		assertEquals( f.getNumerator( ), 7 );
		assertEquals( f.getDenominator( ), 2 );
		
		f = FractionApproximator.getFractionWithNumerator( 3.5, 10 );
		assertEquals( f.getNumerator( ), 10 );
		assertEquals( f.getDenominator( ), 3 );
		
		f = FractionApproximator.getFractionWithNumerator( 0.064, 1 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 16 );
		
		f = FractionApproximator.getFractionWithNumerator( -0.064, 1 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), -16 );
		
		f = FractionApproximator.getFractionWithNumerator( -0.064, -1 );
		assertEquals( f.getNumerator( ), -1 );
		assertEquals( f.getDenominator( ), 16 );
		
		f = FractionApproximator.getFractionWithNumerator( 0, 1 );
		assertEquals( f.getNumerator( ), 0 );
		assertEquals( f.getDenominator( ), 1 );
	}

	public void testConstructorMaxDigits( )
	{
		Fraction f = FractionApproximator.getFractionWithMaxDigits( 0.17, 1 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 6 );

		f = FractionApproximator.getFractionWithMaxDigits( 0.17, 2 );
		assertEquals( f.getNumerator( ), 8);
		assertEquals( f.getDenominator( ), 47 );

		f = FractionApproximator.getFractionWithMaxDigits( 0.17, 3 );
		assertEquals( f.getNumerator( ), 17 );
		assertEquals( f.getDenominator( ), 100 );
		
		f = FractionApproximator.getFractionWithMaxDigits( 0.33, 1 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), 3 );
		
		f = FractionApproximator.getFractionWithMaxDigits( -0.33, 1 );
		assertEquals( f.getNumerator( ), 1 );
		assertEquals( f.getDenominator( ), -3 );
	
	}

	public void testToString( )
	{
		Fraction f = FractionApproximator.getFractionWithNumerator( 0.02, 2 );
		assertEquals( f.toString( ":" ), "2:100" ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( f.toString( ), "2/100" ); //$NON-NLS-1$
		f = new Fraction( 5, 1 );
		assertEquals( f.toString(), "5" );
		
		f = new Fraction( -2, 3 );
		assertEquals( f.toString(), "-2/3" );
		f = new Fraction( 2, -3 );
		assertEquals( f.toString(), "-2/3" );
		f = new Fraction( -2, -3 );
		assertEquals( f.toString(), "2/3" );
	}

	/*public void testEvaluate( )
	{
		double decimal = 0.02;
		Fraction f = new Fraction( decimal, 1 );
		assertEquals( f.evaluate( ), decimal, Double.POSITIVE_INFINITY );
	}*/

	/*public void testPlus( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f1.plus( f2 );
		assertEquals( f1, f3 );
	}

	public void testSubtract( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f3.subtract( f2 );
		assertEquals( f3, f1 );
	}

	public void testMultiply( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f1.multiply( f3 );
		assertEquals( f1, f2 );
	}

	public void testDivide( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 1, 6 );
		Fraction f3 = new Fraction( 1, 2 );
		f2.divide( f1 );
		assertEquals( f2, f3 );
	}*/

	public void testInvert( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( 3, 1 );
		f1.invert( );
		assertEquals( f1.getDenominator(), f2.getDenominator() );
		assertEquals( f2.getNumerator(), f1.getNumerator() );
	}
/*
	public void testNegate( )
	{
		Fraction f1 = new Fraction( 1, 3 );
		Fraction f2 = new Fraction( -1, 3 );
		f1.negate( );
		assertEquals( f1, f2 );
	}*/
}
