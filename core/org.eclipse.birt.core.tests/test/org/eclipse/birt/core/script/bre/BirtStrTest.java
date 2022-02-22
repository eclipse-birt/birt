
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

public class BirtStrTest extends TestCase {
	String str = " I am a test    string";

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

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_left(String,
	 * int)'
	 */
	@Test
	public void testLeftStringInt() {
		String script1 = "BirtStr.left(\"" + str + "\",5)";

		assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), " I am");
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_right(
	 * String, int)'
	 */
	@Test
	public void testRightStringInt() {
		try {
			String script1 = "BirtStr.right(\"" + str + "\",0)";
			String script2 = "BirtStr.right(\"" + str + "\",5)";
			String script3 = "BirtStr.right(\"" + str + "\",50)";
			String script4 = "BirtStr.right(" + null + ",1)";
			String script5 = "BirtStr.right(\"" + str + "\",-2)";
			assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), "");
			assertEquals(((String) cx.evaluateString(scope, script2, "inline", 1, null)), "tring");
			assertEquals(((String) cx.evaluateString(scope, script3, "inline", 1, null)), str);
			assertEquals(((String) cx.evaluateString(scope, script4, "inline", 1, null)), null);
			cx.evaluateString(scope, script5, "inline", 1, null);

			fail("it should be invalid");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_toUpper(
	 * String)'
	 */
	@Test
	public void testToUpper() {
		String script1 = "BirtStr.toUpper(\"" + str + "\")";
		String script2 = "BirtStr.toUpper(" + null + ")";

		String script3 = "a = new Array(\"aaa\",\"bb\",\"23\");BirtStr.toUpper(a);";
		String script4 = "a = new Array(\"aaa\",\"bb\",23);BirtStr.toUpper(a);";

		String script5 = "BirtStr.toLower(123)";

		assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), " I AM A TEST    STRING");

		assertEquals(((String) cx.evaluateString(scope, script2, "inline", 1, null)), null);

		Object result1 = cx.evaluateString(scope, script3, "inline", 1, null);
		if (!(result1 instanceof Object[])) {
			fail("The evaluated result should be an array!");
		}

		assertTrue(eqaulArray((Object[]) result1, new String[] { "AAA", "BB", "23" }));

		result1 = cx.evaluateString(scope, script4, "inline", 1, null);
		assertTrue(eqaulArray((Object[]) result1, new String[] { "AAA", "BB", "23" }));

		result1 = cx.evaluateString(scope, script5, "inline", 1, null);
		assertEquals((String) result1, "123");
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_toLower(
	 * String)'
	 */
	@Test
	public void testToLower() {
		String script1 = "BirtStr.toLower(\"" + str + "\")";
		String script2 = "BirtStr.toLower(" + null + ")";

		String script3 = "a = new Array(\"AAA\",\"BB\",\"23\");BirtStr.toLower(a);";
		String script4 = "a = new Array(\"AAA\",\"BB\",23);BirtStr.toLower(a);";

		String script5 = "BirtStr.toLower(123)";

		assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), " i am a test    string");

		assertEquals(((String) cx.evaluateString(scope, script2, "inline", 1, null)), null);

		Object result1 = cx.evaluateString(scope, script3, "inline", 1, null);
		if (!(result1 instanceof Object[])) {
			fail("The evaluated result should be an array!");
		}

		assertTrue(eqaulArray((Object[]) result1, new String[] { "aaa", "bb", "23" }));

		result1 = cx.evaluateString(scope, script4, "inline", 1, null);
		assertTrue(eqaulArray((Object[]) result1, new String[] { "aaa", "bb", "23" }));

		result1 = cx.evaluateString(scope, script5, "inline", 1, null);
		assertEquals((String) result1, "123");
	}

	private boolean eqaulArray(Object[] arr1, Object[] arr2) {
		if (arr1 == null && arr2 == null) {
			return true;
		}

		if (arr1 == null || arr2 == null || (arr1.length != arr2.length)) {
			return false;
		}

		for (int i = 0; i < arr1.length; i++) {
			if ((arr1[i] != null && !arr1[i].equals(arr2[i])) || (arr2[i] != null && !arr2[i].equals(arr1[i]))) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_trim(String)
	 * '
	 */
	@Test
	public void testTrim() {
		String script1 = "BirtStr.trim(\"" + str + "\")";
		String script2 = "BirtStr.trim(" + null + ")";
		assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), "I am a test string");

		assertEquals(((String) cx.evaluateString(scope, script2, "inline", 1, null)), null);
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_trimLeft(
	 * String)'
	 */
	@Test
	public void testTrimLeft() {
		String script1 = "BirtStr.trimLeft(\"" + str + "\")";
		String script2 = "BirtStr.trimLeft(" + null + ")";
		assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), "I am a test    string");

		assertEquals(((String) cx.evaluateString(scope, script2, "inline", 1, null)), null);
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_trimRight(
	 * String)'
	 */
	@Test
	public void testTrimRight() {
		String script1 = "BirtStr.trimRight(\"" + str + "      \")";
		String script2 = "BirtStr.trimRight(" + null + ")";
		String script3 = "BirtStr.trimRight(\"" + " " + "\")";
		assertEquals(((String) cx.evaluateString(scope, script1, "inline", 1, null)), " I am a test    string");

		assertEquals(((String) cx.evaluateString(scope, script2, "inline", 1, null)), null);

		assertEquals(((String) cx.evaluateString(scope, script3, "inline", 1, null)), "");
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_indexOf(
	 * String, String, int)'
	 */
	@Test
	public void testIndexOfStringStringInt() {
		String script1 = "BirtStr.indexOf(\"a\",\"" + str + "\",1)";
		String script2 = "BirtStr.indexOf(\"a\",\"" + str + "\",4)";
		assertEquals((cx.evaluateString(scope, script1, "inline", 1, null)), new Integer(3));

		assertEquals((cx.evaluateString(scope, script2, "inline", 1, null)), new Integer(6));
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_search(
	 * String, String, int)'
	 */
	@Test
	public void testSearchStringStringInt() {
		assertEquals(((Integer) cx.evaluateString(scope, "BirtStr.search(\"a test\",\" I am a test    string\",0)",
				"inline", 1, null)), new Integer(6));

		assertEquals(((Integer) cx.evaluateString(scope, "BirtStr.search(\"a*t\",\" I am a test    string\",4)",
				"inline", 1, null)), new Integer(6));
		assertEquals(((Integer) cx.evaluateString(scope, "BirtStr.search(\"a?t\",\" I am a test    string\",0)",
				"inline", 1, null)), new Integer(6));
		assertEquals(((Integer) cx.evaluateString(scope, "BirtStr.search(\"a*t\",\" I am a test    string\",9)",
				"inline", 1, null)), new Integer(-1));
		assertEquals(((Integer) cx.evaluateString(scope, "BirtStr.search(\" I*a*t\",\" I am a test    string\",0)",
				"inline", 1, null)), new Integer(0));

		assertEquals(((Integer) cx.evaluateString(scope, "BirtStr.search(\"g\",\" I am a test    string\",0)", "inline",
				1, null)), new Integer(21));
		assertEquals(((Integer) cx.evaluateString(scope, "BirtStr.search(\"am\",\"I am a test    string\",2)", "inline",
				1, null)), new Integer(2));

		assertEquals(
				((Integer) cx.evaluateString(scope, "BirtStr.search(\"A\",\"Actuate Shanghai\",0)", "inline", 1, null)),
				new Integer(0));
	}

	@Test
	public void testSearchStringString() {
		assertEquals(
				(cx.evaluateString(scope, "BirtStr.search(\"a?t\",\" I am a test    string\")", "inline", 1, null)),
				new Integer(6));

		assertEquals(
				(cx.evaluateString(scope, "BirtStr.search(\"a*t\",\" I am a test    string\")", "inline", 1, null)),
				new Integer(3));

		assertEquals(
				(cx.evaluateString(scope, "BirtStr.search(\"a\\\\*t\",\" I am * test    string\")", "inline", 1, null)),
				new Integer(-1));

		assertEquals(
				(cx.evaluateString(scope, "BirtStr.search(\"\\\\? *\",\" I am * test    string\")", "inline", 1, null)),
				new Integer(-1));

		assertEquals(
				(cx.evaluateString(scope, "BirtStr.search(\"\\\\?\",\" I? am * test    string\")", "inline", 1, null)),
				new Integer(2));

		assertEquals((cx.evaluateString(scope, "BirtStr.search(\"\\\\? *\",\" I? am * test    string\")", "inline", 1,
				null)), new Integer(2));

		assertEquals((cx.evaluateString(scope, "BirtStr.search(\"? \\\\*\",\"abc *abc *abc\")", "inline", 1, null)),
				new Integer(2));

		assertEquals((cx.evaluateString(scope, "BirtStr.search(\"? \\\\*\",\"abc *abc *abc\", 6)", "inline", 1, null)),
				new Integer(7));

	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.core.script.bre.NativeBirtStr.jsStaticFunction_charLength(
	 * String)'
	 */
	@Test
	public void testCharLength() {

	}

	/**
	 * Test if ConString can be convert to String.
	 *
	 */
	@Test
	public void testConString() {
		assertEquals((cx.evaluateString(scope, "BirtStr.toUpper('a' + new java.lang.String('b'))", "inline", 1, null)),
				"AB");
	}

}
