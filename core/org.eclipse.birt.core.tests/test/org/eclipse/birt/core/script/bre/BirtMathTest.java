
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.core.script.bre;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import junit.framework.TestCase;

/**
 *
 */

public class BirtMathTest extends TestCase {
	private Context cx;
	private Scriptable scope;

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		/*
		 * Creates and enters a Context. The Context stores information about the
		 * execution environment of a script.
		 */

		cx = Context.enter();
		/*
		 * Initialize the standard objects (Object, Function, etc.) This must be done
		 * before scripts can be executed. Returns a scope object that we use in later
		 * calls.
		 */
		scope = cx.initStandardObjects();

		new CoreJavaScriptInitializer().initialize(cx, scope);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	@After
	public void tearDown() {
		Context.exit();
	}

	@Test
	public void testAdd() {
		String script1 = "BirtMath.add( null, 1 )";
		String script2 = "BirtMath.add( 1,  null)";
		String script3 = "BirtMath.add( 1,  1   )";

		assertEquals(((Number) cx.evaluateString(scope, script1, "inline", 1, null)).doubleValue(), 1, 0);

		assertEquals(((Number) cx.evaluateString(scope, script2, "inline", 1, null)).doubleValue(), 1, 0);

		assertEquals(((Number) cx.evaluateString(scope, script3, "inline", 1, null)).doubleValue(), 2, 0);
	}

	@Test
	public void testRound() {
		String[] scripts = { "BirtMath.round( 0 )", "BirtMath.round( 100.5999 )", "BirtMath.round( 100.5999,1 )",
				"BirtMath.round( 100.5999,2 )", "BirtMath.round( 100.5999,-1 )", "BirtMath.round( 100.5999,-2 )",
				"BirtMath.round( 100.5999,-3 )", "BirtMath.round( 999.5999,-1 )", "BirtMath.round( 999.5999,-2 )",
				"BirtMath.round( 999.5999,-3 )", "BirtMath.round( 999.5999,-4 )" };

		double values[] = { 0, 101, 100.6, 100.60, 100, 100, 0, 1000, 1000, 1000, 0 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).doubleValue(), values[i],
					0);
		}

	}

	@Test
	public void testRoundUp() throws IllegalAccessException, InstantiationException, InvocationTargetException {
		String[] scripts = { "BirtMath.roundUp( 0 )", "BirtMath.roundUp( 100.5999 )", "BirtMath.roundUp( 100.5999,1 )",
				"BirtMath.roundUp( 100.5999,2 )", "BirtMath.roundUp( 100.5999,-1 )", "BirtMath.roundUp( 100.5999,-2 )",
				"BirtMath.roundUp( 100.5999,-3 )", "BirtMath.roundUp( 100.5999,-4 )", "BirtMath.roundUp( 100.5999,-6 )",
				"BirtMath.roundUp( 999.5999,-1 )", "BirtMath.roundUp( 999.5999,-2 )", "BirtMath.roundUp( 999.5999,-3 )",
				"BirtMath.roundUp( 999.5999,-4 )", "BirtMath.roundUp( 100.213,0 )", "BirtMath.roundUp( 100.213,1 )",
				"BirtMath.roundUp( 100.213,2 )", "BirtMath.roundUp( 100.213,-1 )", "BirtMath.roundUp( 100.213,-2 )",
				"BirtMath.roundUp( 100.213,-4 )" };

		double values[] = { 0, 101, 100.6, 100.60, 110, 200, 1000, 10000, 1000000, 1000, 1000, 1000, 10000, 101, 100.3,
				100.22, 110, 200, 10000 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).doubleValue(), values[i],
					0);
		}

	}

	@Test
	public void testRoundDown() {
		String[] scripts = { "BirtMath.roundDown( 0 )", "BirtMath.roundDown( 100.5999 )",
				"BirtMath.roundDown( 100.5999,1 )", "BirtMath.roundDown( 100.5999,2 )",
				"BirtMath.roundDown( 100.5999,-1 )", "BirtMath.roundDown( 100.5999,-2 )",
				"BirtMath.roundDown( 100.5999,-3 )", "BirtMath.roundDown( 100.5999,-4 )",
				"BirtMath.roundDown( 100.5999,-6 )", "BirtMath.roundDown( 999.5999,-1 )",
				"BirtMath.roundDown( 999.5999,-2 )", "BirtMath.roundDown( 999.5999,-3 )",
				"BirtMath.roundDown( 999.5999,-4 )", "BirtMath.roundDown( 100.213,0 )",
				"BirtMath.roundDown( 100.213,1 )", "BirtMath.roundDown( 100.213,2 )",
				"BirtMath.roundDown( 100.213,-1 )", "BirtMath.roundDown( 100.213,-2 )",
				"BirtMath.roundDown( 100.213,-4 )" };

		double values[] = { 0, 100, 100.5, 100.59, 100, 100, 0, 0, 0, 990, 900, 0, 0, 100, 100.2, 100.21, 100, 100, 0 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).doubleValue(), values[i],
					0);
		}
	}

	@Test
	public void testCeiling() {
		String[] scripts = { "BirtMath.ceiling( 0, 0 )", "BirtMath.ceiling( 100.5999,10 )",
				"BirtMath.ceiling( 100.5999,20 )", "BirtMath.ceiling( 100.5999,30 )", "BirtMath.ceiling( 100.5999,5 )",
				"BirtMath.ceiling( -100.5999,-10 )", "BirtMath.ceiling( -100.5999,-3 )",
				"BirtMath.ceiling( -100.5999,-4 )", "BirtMath.ceiling( 100.5999,1000 )",
				"BirtMath.ceiling( 999.5999,10000 )", "BirtMath.ceiling( 999.5999,0 )", "BirtMath.ceiling( 0,100 )",
				"BirtMath.ceiling( 0,-100 )", "BirtMath.ceiling( 25.34, 0.1 )" };

		double values[] = { 0, 110, 120, 120, 105, -110, -102, -104, 1000, 10000, 0, 0, 0, 25.4 };

		for (int i = 0; i < scripts.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).doubleValue(), values[i],
					0);
		}

		try {
			cx.evaluateString(scope, "BirtMath.ceiling(-10,1)", "inline", 1, null);
			fail("Should not arrive here");
		} catch (Exception e) {

		}

		try {
			cx.evaluateString(scope, "BirtMath.ceiling(10,-1)", "inline", 1, null);
			fail("Should not arrive here");
		} catch (Exception e) {

		}

	}

	@Test
	public void testMod() {
		String[] scripts = { "BirtMath.mod( 0, 10 )", "BirtMath.mod( 0, -10 )", "BirtMath.mod( 100.5,10 )",
				"BirtMath.mod( 100.5,100 )", "BirtMath.mod( 100.5,1000 )", "BirtMath.mod( 100.5,15 )",
				"BirtMath.mod( 100.5,-10 )", "BirtMath.mod( 100.5,-100 )", "BirtMath.mod( 100.5,-1000 )",
				"BirtMath.mod( 100.5,-15 )", "BirtMath.mod( -100.5,10 )", "BirtMath.mod( -100.5,100 )",
				"BirtMath.mod( -100.5,1000 )", "BirtMath.mod( -100.5,15 )", "BirtMath.mod( -100.5,-10 )",
				"BirtMath.mod( -100.5,-100 )", "BirtMath.mod( -100.5,-1000 )", "BirtMath.mod( -100.5,-15 )" };

		double values[] = { 0, 0, 0.5, 0.5, 100.5, 10.5, -9.5, -99.5, -899.5, -4.5, 9.5, 99.5, 899.5, 4.5, -0.5, -0.5,
				-100.5, -10.5 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).doubleValue(), values[i],
					0);
		}

		try {
			cx.evaluateString(scope, "BirtMath.mod(0,0)", "inline", 1, null);

			fail("Should not arrive here");
		} catch (Exception e) {

		}
	}

	@Test
	public void testSafeDivide() {
		String[] scripts = { "BirtMath.safeDivide( 0, 10,-1 )", "BirtMath.safeDivide( 10.5, -10,-1 )",
				"BirtMath.safeDivide( 100.5,10,-1 )", "BirtMath.safeDivide( 100.5,0,-1 )",
				"BirtMath.safeDivide( 0, 0, -2 )", };

		double values[] = { 0, -1.05, 10.05, -1, -2 };

		for (int i = 0; i < values.length; i++) {
			assertEquals(((Number) cx.evaluateString(scope, scripts[i], "inline", 1, null)).doubleValue(), values[i],
					0);
		}
	}
}
