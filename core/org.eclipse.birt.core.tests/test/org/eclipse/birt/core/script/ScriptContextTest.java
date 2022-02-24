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

package org.eclipse.birt.core.script;

import java.text.DateFormat;
import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * 
 */
public class ScriptContextTest extends TestCase {

	ScriptContext context;

	@Before
	public void setUp() {
		context = new ScriptContext();
	}

	@After
	public void tearDown() {
		context.close();
	}

	/**
	 * test if the enterScope & exitScope is correct.
	 */
	@Test
	public void testScope() throws BirtException {
		// register A in root
		context.setAttribute("A", new Integer(10));
		// register B in root
		ScriptContext context1 = context.newContext(null);
		context.setAttribute("B", new Integer(20));
		Object result = eval(context1, "A + B");
		assertEquals(((Number) result).doubleValue(), 30.0, Double.MIN_VALUE);
		// B is valid now
		boolean hasException = false;
		try {
			result = eval(context, "A + B");
		} catch (Exception ex) {
			hasException = true;
		}
		assertTrue(!hasException);
		// A is still valid
		result = eval(context, "A");
		assertEquals(((Number) result).doubleValue(), 10.0, Double.MIN_VALUE);
	}

	private Object eval(ScriptContext scriptContext, String script) throws BirtException {
		Object result = scriptContext.evaluate(context.compile("javascript", "<inline>", 1, script));
		return result;
	}

	/**
	 * Test if we can use NativeJavaObject as scope.
	 */
	@Test
	public void testJavaScope() throws BirtException {
		StringBuffer buffer = new StringBuffer();
		// define a function in the root
		eval(context, "function getText() { return 'TEXT'};");

		ScriptContext context1 = context.newContext(buffer);
		// enter java-based scope
		eval(context1, "append(getText());");
		eval(context1, "append('TEXT2');");

		assertEquals("TEXTTEXT2", buffer.toString());
		Object result = eval(context, "getText()");
		assertEquals("TEXT", result);
	}

	/**
	 * compile a script and running it in different scope to see if it returns
	 * differnt values.
	 * 
	 * Expected:
	 * 
	 * the same code running in different scope reutrns different values.
	 */
	@Test
	public void testCompiledScript() throws BirtException {
		ScriptContext context1 = context.newContext(null);
		eval(context1, "function getText() { return 'A'}");
		assertEquals("A", eval(context1, "getText()"));
		ScriptContext context2 = context.newContext(null);
		eval(context2, "function getText() { return 'B'}");
		assertEquals("B", eval(context2, "getText()"));
		boolean hasException = false;
		try {
			eval(context, "getText()");
		} catch (Exception ex) {
			hasException = true;
		}
		assertTrue(hasException);

	}

	/**
	 * Test if the defineClass/definePackage is supported by script.
	 */
	@Test
	public void testGlobal() throws BirtException {
		eval(context, "importPackage(java.util)");
		eval(context, "importClass(java.text.DateFormat)");
		Object list = eval(context, "new ArrayList()");
		Object fmt = eval(context, "DateFormat.getInstance()");
		assertTrue(list instanceof ArrayList);
		assertTrue(fmt instanceof DateFormat);
	}

	/**
	 * context shares the object in the root scope
	 */
	@Test
	public void testRootScope() throws BirtException {
		context.setAttribute("share", "ABCDEFG");
		Object result = eval(context, "share + 'c'");
		assertEquals("ABCDEFGc", result.toString());
		context.close();
	}

	/**
	 * In javascript, the "this" always point to the current scope.
	 */
	@Test
	public void testThisObject() throws BirtException {
		context.setAttribute("A", "ABCDE");

		ScriptContext context1 = context.newContext(null);
		context1.setAttribute("a", "VALUE");
		Object result = eval(context1, "a");
		assertEquals("VALUE", result);

		// it can use this to access the member of scope
		result = eval(context1, "this.a");
		assertEquals("VALUE", result);

		// it can access the member of parent
		result = eval(context1, "A");
		assertEquals("ABCDE", result);

		// it can not use this to access the member of parent.
		result = eval(context1, "this.A");
		assertEquals(null, result);

		context.close();
	}
}
