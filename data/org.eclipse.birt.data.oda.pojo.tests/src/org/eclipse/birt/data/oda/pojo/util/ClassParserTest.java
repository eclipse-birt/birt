/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;

/**
 *
 */

public class ClassParserTest {
	@SuppressWarnings({ "unchecked", "nls" })
	@Test
	public void testGetEssentialClass() throws SecurityException, NoSuchMethodException {
		Class c = TestClass.class;
		ClassParser cp = new ClassParser(null);
		assertEquals(String.class, cp.getEssentialClass(getGenericReturnType(c, "getNestGenericList")));
		assertEquals(Object.class, cp.getEssentialClass(getGenericReturnType(c, "getNestGenericAndCommonList")));
		assertEquals(String.class, cp.getEssentialClass(getGenericReturnType(c, "getCommonObject")));
		assertEquals(Integer.TYPE, cp.getEssentialClass(getGenericReturnType(c, "getPrimitive")));
		assertEquals(ClassParser.class, cp.getEssentialClass(getGenericReturnType(c, "getObjects")));
		assertEquals(Integer.TYPE, cp.getEssentialClass(getGenericReturnType(c, "getPrimitives")));
		assertEquals(String.class, cp.getEssentialClass(getGenericReturnType(c, "getArrayListNest")));
		assertEquals(String.class, cp.getEssentialClass(getGenericReturnType(c, "getListArrayNest")));
	}

	@SuppressWarnings("unchecked")
	private static Type getGenericReturnType(Class c, String methodName)
			throws SecurityException, NoSuchMethodException {
		Method m = c.getMethod(methodName, (Class[]) null);
		System.out.println(m.getGenericReturnType());
		return m.getGenericReturnType();
	}

	private interface TestClass {
		List<List<String>> getNestGenericList();

		@SuppressWarnings("unchecked")
		List<List<List>> getNestGenericAndCommonList();

		String getCommonObject();

		int getPrimitive();

		ClassParser[] getObjects();

		int[] getPrimitives();

		List<String>[] getArrayListNest();

		List<String[]> getListArrayNest();

	}

	@SuppressWarnings({ "unchecked", "nls" })
	@Test
	public void testGetTypeLabel() throws SecurityException, NoSuchMethodException {
		Class c = TestClass.class;
		assertEquals("java.util.List<java.util.List<java.lang.String>>",
				ClassParser.getTypeLabel(getGenericReturnType(c, "getNestGenericList")));
		assertEquals("java.util.List<java.util.List<java.util.List>>",
				ClassParser.getTypeLabel(getGenericReturnType(c, "getNestGenericAndCommonList")));
		assertEquals(String.class.getName(), ClassParser.getTypeLabel(getGenericReturnType(c, "getCommonObject")));
		assertEquals(Integer.TYPE.getName(), ClassParser.getTypeLabel(getGenericReturnType(c, "getPrimitive")));
		assertEquals(ClassParser.class.getName() + "[]",
				ClassParser.getTypeLabel(getGenericReturnType(c, "getObjects")));
		assertEquals("int[]", ClassParser.getTypeLabel(getGenericReturnType(c, "getPrimitives")));
		assertEquals("java.util.List<java.lang.String>[]",
				ClassParser.getTypeLabel(getGenericReturnType(c, "getArrayListNest")));
		assertEquals("java.util.List<java.lang.String[]>",
				ClassParser.getTypeLabel(getGenericReturnType(c, "getListArrayNest")));
	}

	@Test
	public void testGetEssentialClassFromArray() {
		ClassParser cp = new ClassParser(null);
		assertEquals(Boolean.TYPE, cp.getEssentialClassFromArray(boolean[][].class));
		assertEquals(Byte.TYPE, cp.getEssentialClassFromArray(byte[].class));
		assertEquals(Character.TYPE, cp.getEssentialClassFromArray(char[][][][].class));
		assertEquals(ClassParser.class, cp.getEssentialClassFromArray(ClassParser[].class));
		assertEquals(Integer.class, cp.getEssentialClassFromArray(Integer[].class));
		assertEquals(Double.TYPE, cp.getEssentialClassFromArray(double[][][][].class));
		assertEquals(Float.TYPE, cp.getEssentialClassFromArray(float[][][][].class));
		assertEquals(Integer.TYPE, cp.getEssentialClassFromArray(int[][][][].class));
		assertEquals(Long.TYPE, cp.getEssentialClassFromArray(long[][][][].class));
		assertEquals(Short.TYPE, cp.getEssentialClassFromArray(short[][][][].class));
	}

	@SuppressWarnings("nls")
	@Test
	public void testTypeLabelFromArray() {
		assertEquals("boolean[][]", ClassParser.getTypeLabelFromArray(boolean[][].class));
		assertEquals("byte[]", ClassParser.getTypeLabelFromArray(byte[].class));
		assertEquals("char[][][][]", ClassParser.getTypeLabelFromArray(char[][][][].class));
		assertEquals(ClassParser.class.getName() + "[]", ClassParser.getTypeLabelFromArray(ClassParser[].class));
		assertEquals(Integer.class.getName() + "[]", ClassParser.getTypeLabelFromArray(Integer[].class));
		assertEquals("double[][][][]", ClassParser.getTypeLabelFromArray(double[][][][].class));
		assertEquals("float[][][][]", ClassParser.getTypeLabelFromArray(float[][][][].class));
		assertEquals("int[][][][]", ClassParser.getTypeLabelFromArray(int[][][][].class));
		assertEquals("long[][][][]", ClassParser.getTypeLabelFromArray(long[][][][].class));
		assertEquals("short[][][][]", ClassParser.getTypeLabelFromArray(short[][][][].class));
	}
}
