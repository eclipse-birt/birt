/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *******************************************************************************/

package org.eclipse.birt.core.script;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import junit.framework.TestCase;

/**
 * Unit test for CoreJavaScriptWrapper class.
 */

public class CoreJavaScriptWrapperTest extends TestCase {

	/**
	 * Create a Context instance
	 */
	Context cx;
	/**
	 * Create a Scriptable instance
	 */
	Scriptable scope;

	IJavascriptWrapper coreWrapper;

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

		coreWrapper = new CoreJavaScriptWrapper();
	}

	@Override
	@After
	public void tearDown() {
		Context.exit();
	}

	@Test
	public void testWrap() {
		// test nativeArray
		NativeArray nativeArray = new NativeArray(new Object[] { "one", "two" });
		Object object = coreWrapper.wrap(cx, scope, nativeArray, null);
		assertTrue(object instanceof org.mozilla.javascript.NativeArray);

		// test list
		List<Integer> list = new ArrayList<>();
		object = coreWrapper.wrap(cx, scope, list, null);
		assertTrue(object instanceof NativeJavaList);

		// test map
		Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
		object = coreWrapper.wrap(cx, scope, linkedHashMap, null);
		assertTrue(object instanceof NativeJavaLinkedHashMap);

		// test BirtHashMap
		BirtHashMap birtHashMap = new BirtHashMap();
		object = coreWrapper.wrap(cx, scope, birtHashMap, null);
		assertTrue(object instanceof NativeJavaMap);
	}
}
