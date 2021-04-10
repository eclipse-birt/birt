
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
package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.Bytes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class BufferedPrimitiveDiskArrayTest {
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testInteger() throws IOException {
		int objectNumber = 1001;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(1);
		try {
			list.get(0);
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		try {
			list.get(1);
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		for (int i = 0; i < objectNumber; i++) {
			list.add(new Integer(i));
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(list.get(i), new Integer(i));
		}
		try {
			list.get(objectNumber);
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		list.clear();
		list.close();
	}

	@Test
	public void testSpecial() throws IOException {
		int objectNumber = 801;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(800);
		for (int i = 0; i < objectNumber; i++) {
			list.add(new Integer(i));
			try {
				list.get(i + 1);
				fail();
			} catch (IndexOutOfBoundsException e) {
			}
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(list.get(i), new Integer(i));
		}
		try {
			list.get(objectNumber);
			fail();
		} catch (IndexOutOfBoundsException e) {
		}
		list.clear();
		list.close();
	}

	@Test
	public void testStress() throws IOException {
		int objectNumber = 10000;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(40000);
		for (int i = 0; i < objectNumber; i++) {
			list.add(new Integer(i));
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(list.get(i), new Integer(i));
		}
		list.clear();
		list.close();
	}

	@Test
	public void testDouble() throws IOException {
		int objectNumber = 1001;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(700);
		for (int i = 0; i < objectNumber; i++) {
			list.add(new Double(i));
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(list.get(i), new Double(i));
		}
		list.close();
	}

	@Test
	public void testBoolean() throws IOException {
		int objectNumber = 1001;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(500);
		for (int i = 0; i < objectNumber; i++) {
			if (i % 2 == 0) {
				list.add(new Boolean(false));
			} else {
				list.add(new Boolean(true));
			}
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			if (i % 2 == 0) {
				assertEquals(list.get(i), new Boolean(false));
			} else {
				assertEquals(list.get(i), new Boolean(true));
			}
		}
		list.close();
	}

	@Test
	public void testString() throws IOException {
		int objectNumber = 200;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(300);
		for (int i = 0; i < objectNumber; i++) {
			list.add("string" + i);
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(list.get(i), "string" + i);
		}
		list.close();
	}

	@Test
	public void testBytes() throws IOException {
		int objectNumber = 100;
		byte[] b = null;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(300);
		for (int i = 0; i < objectNumber; i++) {
			b = new byte[3];
			b[0] = (byte) i;
			b[1] = (byte) (i + 1);
			b[2] = (byte) (i + 2);
			list.add(new Bytes(b));
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			b = ((Bytes) list.get(i)).bytesValue();
			assertEquals(b[0], i);
			assertEquals(b[1], i + 1);
			assertEquals(b[2], i + 2);
		}
		list.close();
	}

	@Test
	public void testBigDecimal() throws IOException {
		int objectNumber = 3000;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(300);
		for (int i = 0; i < objectNumber; i++) {
			list.add(new BigDecimal("1010101010101010101010" + i));
		}
		assertEquals(list.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(new BigDecimal("1010101010101010101010" + i), list.get(i));
		}
		list.close();
	}

	@Test
	public void testDate() throws IOException {
		int objectNumber = 4101;
		BufferedPrimitiveDiskArray list = new BufferedPrimitiveDiskArray(500);
		for (int i = 0; i < objectNumber; i++) {
			list.add(new Date(1900100000 + i * 1000));
		}
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(list.get(i), new Date(1900100000 + i * 1000));
		}
		list.close();
	}
}
