/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompiler;
import org.eclipse.birt.data.engine.expression.InvalidExpression;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.GregorianCalendar;

/**
 * Tests script evaluation
 */
public class ScriptEvalTest {
	private Scriptable scope;
	private ScriptContext scontext;

	@Before
	public void scriptEvalSetUp() throws Exception {
		scontext = new ScriptContext();
		scope = Context.enter().initStandardObjects();
	}

	@After
	public void scriptEvalTearDown() throws Exception {
		Context.exit();
		scontext.close();
	}

	// Test javascript regular expression
	@Test
	public void testJSRE() throws Exception {
		String test = "/^Mini/.test(\"Minia\")";
		Object result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, test, "", 0);
		assertResult(result, true);
	}

	// Makes sure JS evaluation returns the correct data type
	@Test
	public void testDataType() throws Exception {
		String script = "1 + 2 ";
		Object result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof Integer);
		assertEquals(result, new Integer(3));

		// Repeat same script to exercise cache code path
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertEquals(result, new Integer(3));
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertEquals(result, new Integer(3));

		script = "new Number(123)";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof Double);
		assertEquals(result, new Double(123));

		script = "new Date(2005, 1, 1)";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof Date);

		script = "true";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof Boolean);
		assertEquals(result, new Boolean(true));

		script = "new Boolean(true)";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof Boolean);
		assertEquals(result, new Boolean(true));

		script = "\"abc\"";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof String);
		assertEquals(result, "abc");

		script = "new String(\"abc\")";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof String);
		assertEquals(result, "abc");

		script = "java.lang.Long.decode(\"24\")";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertTrue(result instanceof Long);
		assertEquals(result, new Long("24"));

		// Test eval with null context; should succeed; test is repeated
		// several times to make sure that the eval function does not
		// exit more scope than it enters
		script = "1";
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		result = ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, script, "", 0);
		assertEquals(result, new Double(1));
	}

	// Tests error handling
	@Test
	public void testErrHandling() throws Exception {
		// Syntax error; repeat the same expression multiple times to make sure that
		// script caching does not affect accuracy of error reporting
		handleErrorTest("var x=", true);
		handleErrorTest("var x=", true);
		handleErrorTest("var x=", true);

		handleErrorTest("var x=\"missing end quote", true);

		// Invalid reference
		handleErrorTest("x.y", false);
		handleErrorTest("x.y", false);
		handleErrorTest("x.y", false);
		handleErrorTest("x.y", false);
		handleErrorTest("x.y", false);
		handleErrorTest("x.y", false);
		handleErrorTest("x.y", false);
	}

	private void handleErrorTest(String errScript, boolean compileError) throws Exception {
		// Script runtime error should always be converted to DataException
		final String source = "TestSource" + new Random().nextInt(1000);
		final int lineNo = 365;
		try {
			ScriptEvalUtil.evaluateJSAsExpr(scontext, scope, errScript, source, lineNo);
			fail("Exception expected");
		} catch (Exception e) {
			// Make sure error message contains our source string and lineNo
			String errMsg = e.getLocalizedMessage();
		}

		// Also make sure that compiler does not throw runtime error
		ExpressionCompiler c = new ExpressionCompiler();
		try {
			CompiledExpression exp = c.compile(errScript, null, scontext);
			if (compileError) {
				assertTrue(exp instanceof InvalidExpression);
			} else {
				exp.evaluate(scontext, scope);
				fail("Exception expected in evaluate");
			}
		} catch (DataException e) {
			// we are ok
		}

	}

	/*
	 * @Test public void test_ANY( ) { try { ScriptEvalUtil.evalConditionalExpr(
	 * "aaaaab", IConditionalExpression.OP_ANY, "a*b", null ); fail(
	 * "Exception expected in compare" ); } catch ( DataException e ) { // we are ok
	 * } }
	 */

	/**
	 *
	 */
	@Test
	public void test_BOTTOM_PERCENT() {
		try {
			ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_BOTTOM_PERCENT, "a*b", null);
			fail("Exception expected in compare");
		} catch (DataException e) {
			// we are ok
		}
	}

	/**
	 *
	 */
	@Test
	public void test_TOP_PERCENT() {
		try {
			ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_TOP_PERCENT, "a*b", null);
			fail("Exception expected in compare");
		} catch (DataException e) {
			// we are ok
		}
	}

	/**
	 *
	 */
	@Test
	public void test_BOTTOM_N() {
		try {
			ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_BOTTOM_N, "a*b", null);
			fail("Exception expected in compare");
		} catch (DataException e) {
			// we are ok
		}
	}

	/**
	 *
	 */
	@Test
	public void test_TOP_N() {
		// OP_TOP_N
		try {
			ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_TOP_N, "a*b", null);
			fail("Exception expected in compare");
		} catch (DataException e) {
			// we are ok
		}
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_LIKE() throws DataException {
		final Object[] cases = {
				// String, Pattern, TRUE/FALSE
				new Object[] { "anything", "", Boolean.FALSE }, new Object[] { "anything", null, Boolean.FALSE },
				new Object[] { null, "something", Boolean.FALSE }, new Object[] { null, "", Boolean.TRUE },
				new Object[] { "", null, Boolean.TRUE }, new Object[] { "", "", Boolean.TRUE },

				new Object[] { "aaab", "a%b", Boolean.TRUE }, new Object[] { "aaab", "a*b", Boolean.FALSE },
				new Object[] { "aaab", "a.*b", Boolean.FALSE }, new Object[] { "aaab", "a\\%b", Boolean.FALSE },
				new Object[] { "aaab", "aa_b", Boolean.TRUE }, new Object[] { "aaab", "aa\\_b", Boolean.FALSE },
				new Object[] { "aaab", "a_b", Boolean.FALSE }, new Object[] { "aaab", "aa.b", Boolean.FALSE },
				new Object[] { "a%b", "a\\%b", Boolean.TRUE }, new Object[] { "a%b", "a\\\\%b", Boolean.FALSE },
				new Object[] { "aaab", "aaab\\", Boolean.FALSE }, new Object[] { "aaab", "aa[a]b", Boolean.FALSE },
				new Object[] { "aaab", "aa[[a]b", Boolean.FALSE }, };

		for (int i = 0; i < cases.length; i++) {
			Object[] c = (Object[]) cases[i];
			String str = (String) c[0];
			String pattern = (String) c[1];
			Boolean expected = (Boolean) c[2];
			Object result = ScriptEvalUtil.evalConditionalExpr(str, IConditionalExpression.OP_LIKE, pattern, null);
			assertEquals(result, expected);
		}
	}

	@Test
	public void test_MATCH() throws DataException {
		final Object[] cases = {
				// String, Pattern, TRUE/FALSE/ null for exception
				new Object[] { "anything", "", Boolean.TRUE }, new Object[] { "anything", null, Boolean.TRUE },
				new Object[] { null, "something", Boolean.FALSE }, new Object[] { null, "", Boolean.TRUE },
				new Object[] { "", null, Boolean.TRUE }, new Object[] { "", "", Boolean.TRUE },

				new Object[] { "aaab", "a", Boolean.TRUE }, new Object[] { "bbbb", "a", Boolean.FALSE },
				new Object[] { "bbbb", "ab", Boolean.FALSE }, new Object[] { "aaab", "^a.*b$", Boolean.TRUE },
				new Object[] { "aaab", "a.*b", Boolean.TRUE }, new Object[] { "aaab", "^ab", Boolean.FALSE },
				new Object[] { "aaab", "aa_b", Boolean.FALSE }, new Object[] { "aaab", "A", Boolean.FALSE },

				new Object[] { "aaab", "/a.*b/", Boolean.TRUE }, new Object[] { "aaab", "/a.*b/mig", Boolean.TRUE },
				new Object[] { "aaab", "/a.*/b/i", Boolean.FALSE }, new Object[] { "aaab", "/A/i", Boolean.TRUE },
				new Object[] { "aaab", "//m", Boolean.TRUE }, new Object[] { "aaab", "/\\w*B/i", Boolean.TRUE },

				// Invalid pattern
				new Object[] { "aaab", "[a", null }, new Object[] { "aaab", "/a/p", null }, };

		for (int i = 0; i < cases.length; i++) {
			Object[] c = (Object[]) cases[i];
			String str = (String) c[0];
			String pattern = (String) c[1];
			Boolean expected = (Boolean) c[2];
			try {
				Object result = ScriptEvalUtil.evalConditionalExpr(str, IConditionalExpression.OP_MATCH, pattern, null);
				assertEquals(result, expected);
			} catch (DataException e) {
				assertNull(expected);
			}
		}
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_NOT_LIKE() throws DataException {
		final Object[] cases = {
				// String, Pattern, TRUE/FALSE
				new Object[] { "anything", "", Boolean.FALSE }, new Object[] { "anything", null, Boolean.FALSE },
				new Object[] { null, "something", Boolean.FALSE }, new Object[] { null, "", Boolean.TRUE },
				new Object[] { "", null, Boolean.TRUE }, new Object[] { "", "", Boolean.TRUE },

				new Object[] { "aaab", "a%b", Boolean.TRUE }, new Object[] { "aaab", "a*b", Boolean.FALSE },
				new Object[] { "aaab", "a.*b", Boolean.FALSE }, new Object[] { "aaab", "a\\%b", Boolean.FALSE },
				new Object[] { "aaab", "aa_b", Boolean.TRUE }, new Object[] { "aaab", "aa\\_b", Boolean.FALSE },
				new Object[] { "aaab", "a_b", Boolean.FALSE }, new Object[] { "aaab", "aa.b", Boolean.FALSE },
				new Object[] { "a%b", "a\\%b", Boolean.TRUE }, new Object[] { "a%b", "a\\\\%b", Boolean.FALSE },
				new Object[] { "aaab", "aaab\\", Boolean.FALSE }, new Object[] { "aaab", "aa[a]b", Boolean.FALSE },
				new Object[] { "aaab", "aa[[a]b", Boolean.FALSE }, };

		for (int i = 0; i < cases.length; i++) {
			Object[] c = (Object[]) cases[i];
			String str = (String) c[0];
			String pattern = (String) c[1];
			Boolean expected = (Boolean) c[2];
			Object result = ScriptEvalUtil.evalConditionalExpr(str, IConditionalExpression.OP_NOT_LIKE, pattern, null);
			assertEquals(result, new Boolean(!expected.booleanValue()));
		}
	}

	@Test
	public void test_NOT_MATCH() throws DataException {
		final Object[] cases = {
				// String, Pattern, TRUE/FALSE/ null for exception
				new Object[] { "anything", "", Boolean.TRUE }, new Object[] { "anything", null, Boolean.TRUE },
				new Object[] { null, "something", Boolean.FALSE }, new Object[] { null, "", Boolean.TRUE },
				new Object[] { "", null, Boolean.TRUE }, new Object[] { "", "", Boolean.TRUE },

				new Object[] { "aaab", "a", Boolean.TRUE }, new Object[] { "bbbb", "a", Boolean.FALSE },
				new Object[] { "bbbb", "ab", Boolean.FALSE }, new Object[] { "aaab", "^a.*b$", Boolean.TRUE },
				new Object[] { "aaab", "a.*b", Boolean.TRUE }, new Object[] { "aaab", "^ab", Boolean.FALSE },
				new Object[] { "aaab", "aa_b", Boolean.FALSE }, new Object[] { "aaab", "A", Boolean.FALSE },

				new Object[] { "aaab", "/a.*b/", Boolean.TRUE }, new Object[] { "aaab", "/a.*b/mig", Boolean.TRUE },
				new Object[] { "aaab", "/a.*/b/i", Boolean.FALSE }, new Object[] { "aaab", "/A/i", Boolean.TRUE },
				new Object[] { "aaab", "//m", Boolean.TRUE }, new Object[] { "aaab", "/\\w*B/i", Boolean.TRUE },

				// Invalid pattern
				new Object[] { "aaab", "[a", null }, new Object[] { "aaab", "/a/p", null }, };

		for (int i = 0; i < cases.length; i++) {
			Object[] c = (Object[]) cases[i];
			String str = (String) c[0];
			String pattern = (String) c[1];
			Boolean expected = (Boolean) c[2];
			try {
				Object result = ScriptEvalUtil.evalConditionalExpr(str, IConditionalExpression.OP_NOT_MATCH, pattern,
						null);
				assertEquals(result, new Boolean(!expected.booleanValue()));
			} catch (DataException e) {
				assertNull(expected);
			}
		}
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_FALSE() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("true", IConditionalExpression.OP_FALSE, null, null, null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(new Boolean(false), IConditionalExpression.OP_FALSE, null, null,
				null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr("Boolean(false)", IConditionalExpression.OP_FALSE, null, null,
				null);
		assertResult(result, false);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_TRUE() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("true", IConditionalExpression.OP_TRUE, null, null, null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Boolean(false), IConditionalExpression.OP_TRUE, null, null,
				null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr("asdasd", IConditionalExpression.OP_TRUE, null, null, null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(new Boolean(true), IConditionalExpression.OP_TRUE,
				"Mar 18, 2003 12:00:00 AM", null);
		assertResult(result, true);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_NOT_NULL() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("true", IConditionalExpression.OP_NOT_NULL, null, null, null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(null, IConditionalExpression.OP_NOT_NULL, null, null, null);
		assertResult(result, false);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_NULL() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("true", IConditionalExpression.OP_NULL, null, null, null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(null, IConditionalExpression.OP_NULL, null, null, null);
		assertResult(result, true);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_NOT_BETWEEN() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_NOT_BETWEEN,
				new Timestamp((new GregorianCalendar(2004, 1, 1)).getTimeInMillis()),
				new Timestamp((new GregorianCalendar(2004, 1, 3)).getTimeInMillis()));
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_NOT_BETWEEN,
				new Timestamp((new GregorianCalendar(2004, 1, 3)).getTimeInMillis()),
				new Timestamp((new GregorianCalendar(2004, 1, 1)).getTimeInMillis()));
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_NOT_BETWEEN, "01/03/2004", "01/06/2004");
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_NOT_BETWEEN, "01/06/2004", "01/03/2004");
		assertResult(result, true);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_BETWEEN() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr(new Integer(10), IConditionalExpression.OP_BETWEEN, new Double(9.9),
				new BigDecimal(19.9));
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Integer(10), IConditionalExpression.OP_BETWEEN,
				new Double(10.9), new BigDecimal(19.9));
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(new Integer(10), IConditionalExpression.OP_BETWEEN, new Double(20),
				new BigDecimal(9.9));
		assertResult(result, false);

		// Nov 1, 2001 between "Oct 20, 2001 12:00:00 AM"
		// and "Nov 20, 2001 12:00:00 AM"
		GregorianCalendar calendar = new GregorianCalendar(2001, 11 - 1, 1);
		result = ScriptEvalUtil.evalConditionalExpr(calendar.getTime(), IConditionalExpression.OP_BETWEEN,
				"Oct 20, 2001 12:00:00 AM", "Nov 20, 2001 12:00:00 AM");
		assertResult(result, true);

		// the case when obj shares the same type as op1.value but not op2.value
		result = ScriptEvalUtil.evalConditionalExpr(new Double(10), IConditionalExpression.OP_BETWEEN, new Double(9),
				new Integer(20));
		assertResult(result, true);

		// the case when obj is neither date nor number
		result = ScriptEvalUtil.evalConditionalExpr("10", IConditionalExpression.OP_BETWEEN, new Integer(9),
				new Double(20.0));
		assertResult(result, true);

//		result = ScriptEvalUtil.evalConditionalExpr( calendar.getTime( ),
//				IConditionalExpression.OP_BETWEEN,
//				ExprTextAndValue.newInstance( "\"01/01/03\"", "01/01/03" ),
//				ExprTextAndValue.newInstance( "01/01/04", new Double( 0.25 ) ) );
//		assertResult( result, false );

		result = ScriptEvalUtil.evalConditionalExpr(new Boolean(true), IConditionalExpression.OP_BETWEEN,
				new Double(0.5), new Double(2));
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Boolean(true), IConditionalExpression.OP_BETWEEN,
				new Double(-1), new Double(0.5));
		assertResult(result, false);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_GT() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr(new Integer(10), IConditionalExpression.OP_GT, new Double(9.9),
				null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Integer(10), IConditionalExpression.OP_GT, new BigDecimal(19.9),
				null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(new BigDecimal(10), IConditionalExpression.OP_GT, new Double(10.0),
				null);
		assertResult(result, false);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_GE() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr(new BigDecimal(10), IConditionalExpression.OP_GE, new Double(10.0),
				null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_GE, new Timestamp((new GregorianCalendar(2004, 1, 3)).getTimeInMillis()),
				null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_GE, "01/01/2004", null);
		assertResult(result, true);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_LE() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr(new BigDecimal(10), IConditionalExpression.OP_LE, new Double(10.0),
				null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_LE, new Timestamp((new GregorianCalendar(2004, 1, 3)).getTimeInMillis()),
				null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new Date((new GregorianCalendar(2004, 1, 2)).getTimeInMillis()),
				IConditionalExpression.OP_LE, "01/01/2004", null);
		assertResult(result, false);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_LTtest() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr(new Integer(10), IConditionalExpression.OP_LT, new Double(9.9),
				null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr(new Integer(10), IConditionalExpression.OP_LT, new BigDecimal(19.9),
				null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr(new BigDecimal(10), IConditionalExpression.OP_LT, new Double(10.0),
				null);
		assertResult(result, false);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_NE() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_NE, "aaaaab", null);
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_NE, "s", null);
		assertResult(result, true);
	}

	/**
	 * @throws DataException
	 */
	@Test
	public void test_EQ() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_EQ, "aaaaab", null);
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr("aaaaab", IConditionalExpression.OP_EQ, "s", null);
		assertResult(result, false);
	}

	/**
	 *
	 * @throws DataException
	 */
	@Test
	public void test_IN() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("100", IConditionalExpression.OP_IN, new String[] { "200", "100" });
		assertResult(result, true);

		result = ScriptEvalUtil.evalConditionalExpr("100", IConditionalExpression.OP_IN, new String[] { "200", "400" });
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr("100", IConditionalExpression.OP_IN, new String[] { null });
		assertResult(result, false);
	}

	/**
	 *
	 * @throws DataException
	 */
	@Test
	public void test_NOT_IN() throws DataException {
		Object result;
		result = ScriptEvalUtil.evalConditionalExpr("100", IConditionalExpression.OP_NOT_IN,
				new String[] { "200", "100" });
		assertResult(result, false);

		result = ScriptEvalUtil.evalConditionalExpr("100", IConditionalExpression.OP_NOT_IN,
				new String[] { "200", "400" });
		assertResult(result, true);
	}

	private void assertResult(Object result, boolean expectedResult) {
		assertTrue(result instanceof Boolean);
		assertEquals(result, new Boolean(expectedResult));
	}
}
