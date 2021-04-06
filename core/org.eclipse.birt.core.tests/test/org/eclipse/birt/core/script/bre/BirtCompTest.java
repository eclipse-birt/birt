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
package org.eclipse.birt.core.script.bre;

import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import junit.framework.TestCase;

/**
 * 
 */
public class BirtCompTest extends TestCase {
	private Context cx;
	private Scriptable scope;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
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
		scope.put(IScriptFunctionContext.FUNCTION_BEAN_NAME, scope, new IScriptFunctionContext() {

			@Override
			public Object findProperty(String name) {
				return null;
			}
		});
		new CoreJavaScriptInitializer().initialize(cx, scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() {
		Context.exit();
	}

	/**
	 * 
	 *
	 */
	@Test
	public void testAnyOf() {
		/*
		 * String script1 =
		 * "var array = new Array(4);array[0] = 100; array[1] = \"ABC\"; array[2] = \"1999-11-10\"; array[3] = null;"
		 * ;
		 */
		String script2 = "BirtComp.anyOf(100,100,\"ABC\", \"1999-11-10\",null);";
		String script3 = "BirtComp.anyOf(null,100,\"ABC\", \"1999-11-10\",null)";
		String script4 = "BirtComp.anyOf(\"ABC\",100,\"ABC\", \"1999-11-10\",null)";
		String script5 = "BirtComp.anyOf(new Date(99,10,10),100,\"ABC\", \"1999-11-10\",null)";
		String script6 = "BirtComp.anyOf(\"1999-11-10\",100,\"ABC\", \"1999-11-10\",null)";
		String script7 = "BirtComp.anyOf(20,100,\"ABC\", \"1999-11-10\",null)";
		String script8 = "array = new Array(3);array[0]=0;array[1]=1;array[2]=2;BirtComp.anyOf(1,array);";
		String script9 = "array = new Array(3);array[0]=0;array[1]=1;array[2]=2;BirtComp.anyOf(4,array);";
		assertTrue(((Boolean) cx.evaluateString(scope, script2, "inline", 1, null)).booleanValue());

		assertTrue(((Boolean) cx.evaluateString(scope, script3, "inline", 1, null)).booleanValue());

		assertTrue(((Boolean) cx.evaluateString(scope, script4, "inline", 1, null)).booleanValue());

		assertTrue(((Boolean) cx.evaluateString(scope, script5, "inline", 1, null)).booleanValue());

		assertTrue(((Boolean) cx.evaluateString(scope, script6, "inline", 1, null)).booleanValue());

		assertFalse(((Boolean) cx.evaluateString(scope, script7, "inline", 1, null)).booleanValue());

		assertTrue(((Boolean) cx.evaluateString(scope, script8, "inline", 1, null)).booleanValue());

		assertFalse(((Boolean) cx.evaluateString(scope, script9, "inline", 1, null)).booleanValue());
	}

	@Test
	public void testBetween() {
		String script1 = "BirtComp.between(\"1923-10-11\",new Date(10,11,11),new Date(33,11,11))";

		assertTrue(((Boolean) cx.evaluateString(scope, script1, "inline", 1, null)).booleanValue());

		String script2 = "BirtComp.between(100,101,102)";
		assertFalse(((Boolean) cx.evaluateString(scope, script2, "inline", 1, null)).booleanValue());

	}

	/**
	 * 
	 *
	 */
	@Test
	public void testNotBetween() {
		String script1 = "BirtComp.notBetween(\"1923-10-11\",new Date(10,11,11),new Date(33,11,11))";

		assertFalse(((Boolean) cx.evaluateString(scope, script1, "inline", 1, null)).booleanValue());

		String script2 = "BirtComp.notBetween(100,101,102)";
		assertTrue(((Boolean) cx.evaluateString(scope, script2, "inline", 1, null)).booleanValue());

	}

	/**
	 * 
	 *
	 */
	@Test
	public void testCompare() {
		Object[][] tests = new Object[][] {
				// Equal to
				{ "BirtComp.equalTo(100,100);", true }, { "BirtComp.equalTo(null,null)", true },
				{ "BirtComp.equalTo(\"ABC\",\"ABC\")", true },
				{ "BirtComp.equalTo(new Date(99,10,10),\"1999-11-10\")", true },
				{ "BirtComp.equalTo(\"1999-11-10\",new Date(99,10,10))", true }, { "BirtComp.equalTo(20,100)", false },
				{ "BirtComp.equalTo( new java.sql.Time(10,10,10), \"10:10:10.000\")", true },
				{ "BirtComp.equalTo( new java.sql.Date(80,9,9), \"1980-10-9 12:14:25\")", true },

				// NotEqual to
				{ "BirtComp.notEqual(100,100);", false }, { "BirtComp.notEqual(null,null)", false },
				{ "BirtComp.notEqual(\"ABC\",\"ABC\")", false },
				{ "BirtComp.notEqual(new Date(99,10,10),\"1999-11-10\")", false },
				{ "BirtComp.notEqual(\"1999-11-10\",new Date(99,10,10))", false },
				{ "BirtComp.notEqual(20,100)", true },

				// greater than
				{ "BirtComp.greaterThan(100,10);", true }, { "BirtComp.greaterThan(null,null)", false },
				{ "BirtComp.greaterThan(\"aBC\",\"ABC\")", true },
				{ "BirtComp.greaterThan(new Date(99,9,10),\"1999-11-10\")", false },
				{ "BirtComp.greaterThan(\"1999-11-10\",new Date(99,9,10))", true },
				{ "BirtComp.greaterThan(20,100)", false },

				// greater than or equal to
				{ "BirtComp.greaterOrEqual(100,10);", true }, { "BirtComp.greaterOrEqual(null,null)", true },
				{ "BirtComp.greaterOrEqual(\"aBC\",\"ABC\")", true },
				{ "BirtComp.greaterOrEqual(new Date(99,9,10),\"1999-11-10\")", false },
				{ "BirtComp.greaterOrEqual(\"1999-11-10\",new Date(99,9,10))", true },
				{ "BirtComp.greaterOrEqual(20,100)", false },

				// Less than
				{ "BirtComp.lessThan(10,100);", true }, { "BirtComp.lessThan(null,null)", false },
				{ "BirtComp.lessThan(\"aBC\",\"ABC\")", false },
				{ "BirtComp.lessThan(new Date(99,9,10),\"1999-11-10\")", true },
				{ "BirtComp.lessThan(\"1999-11-10\",new Date(99,9,10))", false }, { "BirtComp.lessThan(20,100)", true },

				// Less than or equal to
				{ "BirtComp.lessOrEqual(100,10);", false }, { "BirtComp.lessOrEqual(null,null)", true },
				{ "BirtComp.lessOrEqual(\"aBC\",\"ABC\")", false },
				{ "BirtComp.lessOrEqual(new Date(99,9,10),\"1999-11-10\")", true },
				{ "BirtComp.lessOrEqual(\"1999-11-10\",new Date(99,9,10))", false },
				{ "BirtComp.lessOrEqual(100,100)", true } };

		for (int i = 0; i < tests.length; i++) {
			assertTrue(((Boolean) cx.evaluateString(scope, (String) tests[i][0], "inline", 1, null))
					.equals((Boolean) tests[i][1]));
		}
	}

	/**
	 * 
	 *
	 */
	@Test
	public void testMatch() {
		String[] script = new String[] {
				// Equal to
				"BirtComp.match(\"x 99:02:03\",\".*[0-9]*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99::03\",\".*[0-9]*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\"x [0-9]*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\".*99*:[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\".*[0-9]*.[0-9]*:[0-9]*\");",
				"BirtComp.match(\"x 99:02:03\",\".*[0-9]*:[0-9]*:[0-9]*3.\");",

		};

		boolean[] result = new boolean[] { true, true, true, true, true, false };

		for (int i = 0; i < script.length; i++) {
			assertTrue(((Boolean) cx.evaluateString(scope, script[i], "inline", 1, null)).booleanValue() == result[i]);
		}
	}

	/**
	 * 
	 *
	 */
	@Test
	public void testLike() {
		String[] script = new String[] {
				// Equal to
				"BirtComp.like(\"x 99:02:03\",\"%:0_:03\");", "BirtComp.like(\"x 99::003\",\"%9_::__3\");",
				"BirtComp.like(\"x 99:02:03\",\"%99:02_03\");", "BirtComp.like(\"x 99:02:03\",\"x 99%0_\");",
				"BirtComp.like(\"x 99:02:03\",\"_ 99%03\");", "BirtComp.like(\"x 99:02:03\",\"%:0_:__3\");",
				"BirtComp.like(\"x 99:02:03\",\"%:0\\\\_03\");", "BirtComp.like(\"x 99:02_03\",\"%:0\\\\_03\");",
				"BirtComp.like(\"x 99:02_03\",\"%:02\\\\_03\");", "BirtComp.like(\"x 99:02_03\",\"\\\\%:02\\\\_03\");",
				"BirtComp.like(\"x 99%:02_03\",\"%\\\\%:02\\\\_03\");",
				"BirtComp.like(\"x 99%:02_03\",\"\\\\\\\\%\\\\%:02\\\\_03\");",
				"BirtComp.like(\"x \\\\99%:02_03\",\"_ \\\\\\\\99\\\\%:02\\\\_03\");",
				"BirtComp.like(\"ABC\",\"%AB%\");", "BirtComp.like(\"ABC\",\"%Ab%\");",
				"BirtComp.like(\"ABC\",\"%Ab%\",true);", "BirtComp.like(\"AB\\r\\nC\",\"%AB%\");",

		};

		boolean[] result = new boolean[] { true, true, true, true, true, false, false, false, true, false, true, false,
				true, true, false, true, true };

		for (int i = 0; i < script.length; i++) {
			assertEquals(result[i], ((Boolean) cx.evaluateString(scope, script[i], "inline", 1, null)).booleanValue());
		}

	}

	/**
	 * 
	 *
	 */
	@Test
	public void testNotLike() {
		String[] script = new String[] {
				// Equal to
				"BirtComp.notLike(\"x 99:02:03\",\"%:0_:03\");", "BirtComp.notLike(\"x 99::003\",\"%9_::__3\");",
				"BirtComp.notLike(\"x 99:02:03\",\"%99:02_03\");", "BirtComp.notLike(\"x 99:02:03\",\"x 99%0_\");",
				"BirtComp.notLike(\"x 99:02:03\",\"_ 99%03\");", "BirtComp.notLike(\"x 99:02:03\",\"%:0_:__3\");",
				"BirtComp.notLike(\"x 99:02:03\",\"%:0\\\\_03\");", "BirtComp.notLike(\"x 99:02_03\",\"%:0\\\\_03\");",
				"BirtComp.notLike(\"x 99:02_03\",\"%:02\\\\_03\");",
				"BirtComp.notLike(\"x 99:02_03\",\"\\\\%:02\\\\_03\");",
				"BirtComp.notLike(\"x 99%:02_03\",\"%\\\\%:02\\\\_03\");",
				"BirtComp.notLike(\"x 99%:02_03\",\"\\\\\\\\%\\\\%:02\\\\_03\");",
				"BirtComp.notLike(\"x \\\\99%:02_03\",\"_ \\\\\\\\99\\\\%:02\\\\_03\");" };

		boolean[] result = new boolean[] { false, false, false, false, false, true, true, true, false, true, false,
				true, false };

		for (int i = 0; i < script.length; i++) {
			assertEquals(result[i], ((Boolean) cx.evaluateString(scope, script[i], "inline", 1, null)).booleanValue());
		}
	}

	/**
	 * Test BirtComp.compareString function
	 * 
	 */
	@Test
	public void testCompareString() {
		String[] script = new String[] { "BirtComp.compareString(null,null)", "BirtComp.compareString(null,\"abc\")",
				"BirtComp.compareString(\"abc\",null);", "BirtComp.compareString(\"ABC\",\"ABC\")",
				"BirtComp.compareString(\"abc\",\"ABC\")", "BirtComp.compareString(\"ABC\",\"DEF\")",
				"BirtComp.compareString(\"abc\",\"ABC\",true)", "BirtComp.compareString(\"abc \",\"ABC\",true)",
				"BirtComp.compareString(\"abc \",\"ABC\",true,true)",
				"BirtComp.compareString(\"abc \",\"ABC\",false,true)", };

		boolean[] result = new boolean[] { true, false, false, true, false, false, true, false, true, false };

		for (int i = 0; i < script.length; i++) {
			assertTrue((Boolean) cx.evaluateString(scope, script[i], "inline", 1, null) == result[i]);
			System.out.println(i);
		}
	}
}
