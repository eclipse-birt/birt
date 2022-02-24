/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.util;

import junit.framework.TestCase;

import org.eclipse.birt.chart.internal.model.ContinuedFraction;
import org.eclipse.birt.chart.internal.model.Fraction;
import org.eclipse.birt.chart.internal.model.FractionApproximator;

/**
 * 
 */

public class FractionTest extends TestCase {

	public void testConstructorPrecise() {
		Fraction f = FractionApproximator.getExactFraction(0.02);
		assertEquals(f.getNumerator(), 1);
		assertEquals(f.getDenominator(), 50);

		f = FractionApproximator.getExactFraction(0.333333333333);
		assertEquals(f.getNumerator(), 33333333);
		assertEquals(f.getDenominator(), 100000000);

		f = FractionApproximator.getExactFraction(-0.333333333333);
		assertEquals(f.getNumerator(), -33333333);
		assertEquals(f.getDenominator(), 100000000);

		f = FractionApproximator.getExactFraction(-0.33);
		assertEquals(f.getNumerator(), -33);
		assertEquals(f.getDenominator(), 100);
	}

	public void testConstructorFixedNumerator() {
		Fraction f = FractionApproximator.getFractionWithNumerator(0.02, 2);
		assertEquals(f.getNumerator(), 2);
		assertEquals(f.getDenominator(), 100);

		f = FractionApproximator.getFractionWithNumerator(2.0, 1);
		assertEquals(f.getNumerator(), 2);
		assertEquals(f.getDenominator(), 1);

		f = FractionApproximator.getFractionWithNumerator(3.5, 1);
		assertEquals(f.getNumerator(), 7);
		assertEquals(f.getDenominator(), 2);

		f = FractionApproximator.getFractionWithNumerator(3.5, 10);
		assertEquals(f.getNumerator(), 10);
		assertEquals(f.getDenominator(), 3);

		f = FractionApproximator.getFractionWithNumerator(0.064, 1);
		assertEquals(f.getNumerator(), 1);
		assertEquals(f.getDenominator(), 16);

		f = FractionApproximator.getFractionWithNumerator(-0.064, 1);
		assertEquals(f.getNumerator(), 1);
		assertEquals(f.getDenominator(), -16);

		f = FractionApproximator.getFractionWithNumerator(-0.064, -1);
		assertEquals(f.getNumerator(), -1);
		assertEquals(f.getDenominator(), 16);

		f = FractionApproximator.getFractionWithNumerator(0, 1);
		assertEquals(f.getNumerator(), 0);
		assertEquals(f.getDenominator(), 1);
	}

	public void testConstructorMaxDigits() {
		Fraction f = FractionApproximator.getFractionWithMaxDigits(0.17, 1);
		assertEquals(f.getNumerator(), 1);
		assertEquals(f.getDenominator(), 6);

		f = FractionApproximator.getFractionWithMaxDigits(0.17, 2);
		assertEquals(f.getNumerator(), 8);
		assertEquals(f.getDenominator(), 47);

		f = FractionApproximator.getFractionWithMaxDigits(0.17, 3);
		assertEquals(f.getNumerator(), 17);
		assertEquals(f.getDenominator(), 100);

		f = FractionApproximator.getFractionWithMaxDigits(0.33, 1);
		assertEquals(f.getNumerator(), 1);
		assertEquals(f.getDenominator(), 3);

		f = FractionApproximator.getFractionWithMaxDigits(-0.33, 1);
		assertEquals(f.getNumerator(), 1);
		assertEquals(f.getDenominator(), -3);

	}

	public void testExactFration() {
		// Integer
		helpTestExactFraction(0, "0", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(1, "1", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(2, "2", "2"); //$NON-NLS-1$ //$NON-NLS-2$

		// 0.1 ~ 0.9
		helpTestExactFraction(0.1, "0,10", "1/10"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.2, "0,5", "1/5"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.3, "0,3,3", "3/10"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.4, "0,2,2", "2/5"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.5, "0,2", "1/2"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.6, "0,1,1,2", "3/5"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.7, "0,1,2,3", "7/10"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.8, "0,1,4", "4/5"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.9, "0,1,9", "9/10"); //$NON-NLS-1$ //$NON-NLS-2$

		// 0.25, 0.75
		helpTestExactFraction(0.25, "0,4", "1/4"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.75, "0,1,3", "3/4"); //$NON-NLS-1$ //$NON-NLS-2$

		// Double precision
		helpTestExactFraction(0.30000000000000004, "0,3,3", "3/10"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(0.7999999999999999, "0,1,4", "4/5"); //$NON-NLS-1$ //$NON-NLS-2$

		// Above 1
		helpTestExactFraction(1.1, "1,10", "11/10"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(1.21, "1,4,1,3,5", "121/100"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(5.81, "5,1,4,3,1,4", "581/100"); //$NON-NLS-1$ //$NON-NLS-2$

		// Negative value
		helpTestExactFraction(-1.1, "-1,-10", "-11/10"); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestExactFraction(-121, "-121", "-121"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void helpTestExactFraction(double decimal, String fractionListStr, String fractionStr) {
		ContinuedFraction cf = new ContinuedFraction(decimal);
		assertEquals(fractionListStr, cf.toString());
		assertEquals(fractionStr, cf.getExactFraction().toString());
	}

	public void testMaxDigitsFraction() {
		// Integer
		helpTestMaxDigitsFraction(0, "0", "0", 3); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestMaxDigitsFraction(1, "1", "1", 3); //$NON-NLS-1$ //$NON-NLS-2$
		helpTestMaxDigitsFraction(2, "2", "2", 3); //$NON-NLS-1$ //$NON-NLS-2$

		helpTestMaxDigitsFraction(0.333, "0,3,333", "1/3", 3);//$NON-NLS-1$ //$NON-NLS-2$

		helpTestMaxDigitsFraction(0.353, "0,2,1,4,1,58", "6/17", 2);//$NON-NLS-1$ //$NON-NLS-2$
		helpTestMaxDigitsFraction(0.353, "0,2,1,4,1,58", "6/17", 3);//$NON-NLS-1$ //$NON-NLS-2$
		helpTestMaxDigitsFraction(0.353, "0,2,1,4,1,58", "353/1000", 4);//$NON-NLS-1$ //$NON-NLS-2$
	}

	private void helpTestMaxDigitsFraction(double decimal, String fractionListStr, String fractionStr,
			int maxDigitsForDenominator) {
		ContinuedFraction cf = new ContinuedFraction(decimal);
		assertEquals(fractionListStr, cf.toString());
		assertEquals(fractionStr, cf.getFractionWithMaxDigits(maxDigitsForDenominator).toString());
	}

	public void testFixedNumberatorFraction() {
		// Integer
		helpTestFixedNumeratorFraction(0, "0", 1); //$NON-NLS-1$
		helpTestFixedNumeratorFraction(1, "1", 1); //$NON-NLS-1$
		helpTestFixedNumeratorFraction(2, "2", 1); //$NON-NLS-1$ /

		helpTestFixedNumeratorFraction(0.333, "1/3", 1);//$NON-NLS-1$
		helpTestFixedNumeratorFraction(0.333, "3/9", 3);//$NON-NLS-1$

		helpTestFixedNumeratorFraction(0.353, "1/3", 1);//$NON-NLS-1$
		helpTestFixedNumeratorFraction(0.353, "3/8", 3);//$NON-NLS-1$
	}

	private void helpTestFixedNumeratorFraction(double decimal, String fractionStr, long numerator) {
		assertEquals(fractionStr, FractionApproximator.getFractionWithNumerator(decimal, numerator).toString());
	}

	public void testToString() {
		Fraction f = FractionApproximator.getFractionWithNumerator(0.02, 2);
		assertEquals(f.toString(":"), "2:100"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(f.toString(), "2/100"); //$NON-NLS-1$
		f = new Fraction(5, 1);
		assertEquals(f.toString(), "5");//$NON-NLS-1$

		f = new Fraction(-2, 3);
		assertEquals(f.toString(), "-2/3");//$NON-NLS-1$
		f = new Fraction(2, -3);
		assertEquals(f.toString(), "-2/3");//$NON-NLS-1$
		f = new Fraction(-2, -3);
		assertEquals(f.toString(), "2/3");//$NON-NLS-1$
	}

	/*
	 * public void testEvaluate( ) { double decimal = 0.02; Fraction f = new
	 * Fraction( decimal, 1 ); assertEquals( f.evaluate( ), decimal,
	 * Double.POSITIVE_INFINITY ); }
	 */

	/*
	 * public void testPlus( ) { Fraction f1 = new Fraction( 1, 3 ); Fraction f2 =
	 * new Fraction( 1, 6 ); Fraction f3 = new Fraction( 1, 2 ); f1.plus( f2 );
	 * assertEquals( f1, f3 ); }
	 * 
	 * public void testSubtract( ) { Fraction f1 = new Fraction( 1, 3 ); Fraction f2
	 * = new Fraction( 1, 6 ); Fraction f3 = new Fraction( 1, 2 ); f3.subtract( f2
	 * ); assertEquals( f3, f1 ); }
	 * 
	 * public void testMultiply( ) { Fraction f1 = new Fraction( 1, 3 ); Fraction f2
	 * = new Fraction( 1, 6 ); Fraction f3 = new Fraction( 1, 2 ); f1.multiply( f3
	 * ); assertEquals( f1, f2 ); }
	 * 
	 * public void testDivide( ) { Fraction f1 = new Fraction( 1, 3 ); Fraction f2 =
	 * new Fraction( 1, 6 ); Fraction f3 = new Fraction( 1, 2 ); f2.divide( f1 );
	 * assertEquals( f2, f3 ); }
	 */

	public void testInvert() {
		Fraction f1 = new Fraction(1, 3);
		Fraction f2 = new Fraction(3, 1);
		f1.invert();
		assertEquals(f1.getDenominator(), f2.getDenominator());
		assertEquals(f2.getNumerator(), f1.getNumerator());
	}
	/*
	 * public void testNegate( ) { Fraction f1 = new Fraction( 1, 3 ); Fraction f2 =
	 * new Fraction( -1, 3 ); f1.negate( ); assertEquals( f1, f2 ); }
	 */
}
