package org.eclipse.birt.core.script;

import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import junit.framework.TestCase;

public class NativeNamedListTest extends TestCase {
	/**
	 * Create a Context instance
	 */
	Context cx;
	/**
	 * Create a Scriptable instance
	 */
	Scriptable scope;

	/**
	 * Record whether there exists an error
	 */
	boolean hasException;

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

		// ScriptableObject.defineClass(scope, NativeNamedList.class);

		registerBeans();

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
	 * Evaluate a JavaScript source string.
	 * 
	 * @param script
	 * @return the result
	 */
	protected Object evaluate(String script) {
		try {
			hasException = false;
			return cx.evaluateString(scope, script, "inline", 1, null);
		} catch (Throwable ex) {
			hasException = true;
		}
		return null;
	}

	StringBuffer buffer = new StringBuffer();

	protected void registerBeans() {
		String[] names = new String[] { "a", "b", "c", "d" };
		HashMap values = new HashMap();
		values.put("a", new Integer(123));
		values.put("b", "STRING");
		values.put("c", new Date());
		values.put("d", null);

		NativeNamedList params = new NativeNamedList(scope, names, values);
		scope.put("params", scope, params);
		scope.put("buffer", scope, Context.javaToJS(buffer, scope));
	}

	@Test
	public void testIn() {
		String script = "for (var a in params) { buffer.append(a); }";
		buffer.setLength(0);
		evaluate(script);
		assertTrue(!hasException);
		assertEquals("abcd", buffer.toString());
	}

	@Test
	public void testLength() {
		String script = "params.length";
		Object value = evaluate(script);
		assertTrue(!hasException);
		assertEquals(4, ((Number) value).intValue());
	}

	@Test
	public void testNameAccess() {
		String script = "params['a'] + params.b";
		Object value = evaluate(script);
		assertTrue(!hasException);
		assertEquals("123STRING", value.toString());
	}

	@Test
	public void testIndexAccess() {
		String script = "params[0] + params[params.length-3]";
		Object value = evaluate(script);
		assertTrue(!hasException);
		assertEquals("123STRING", value.toString());
	}

	@Test
	public void testEntryName() {
		String script = "params[0].name";
		Object value = evaluate(script);
		assertTrue(!hasException);
		assertEquals("a", value.toString());
	}

	@Test
	public void testEntryValue() {
		String script = "params[0].value + params[0]";
		Object value = evaluate(script);
		assertTrue(!hasException);
		assertEquals(246, ((Number) value).intValue());
	}

}
