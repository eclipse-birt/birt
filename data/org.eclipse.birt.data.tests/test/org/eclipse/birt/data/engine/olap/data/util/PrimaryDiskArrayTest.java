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

package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.util.PrimitiveDiskArray;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class PrimaryDiskArrayTest {

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
		PrimitiveDiskArray array = new PrimitiveDiskArray();
		for (int i = 0; i < objectNumber; i++) {
			array.add(new Integer(i));
		}
		assertEquals(array.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(array.get(i), new Integer(i));
		}
		array.close();
	}

	@Test
	public void testDouble() throws IOException {
		int objectNumber = 1001;
		PrimitiveDiskArray array = new PrimitiveDiskArray();
		for (int i = 0; i < objectNumber; i++) {
			array.add(new Double(i));
		}
		assertEquals(array.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(array.get(i), new Double(i));
		}
		array.close();
	}

	@Test
	public void testBoolean() throws IOException {
		int objectNumber = 1001;
		PrimitiveDiskArray array = new PrimitiveDiskArray();
		for (int i = 0; i < objectNumber; i++) {
			if (i % 2 == 0) {
				array.add(new Boolean(false));
			} else {
				array.add(new Boolean(true));
			}
		}
		assertEquals(array.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			if (i % 2 == 0) {
				assertEquals(array.get(i), new Boolean(false));
			} else {
				assertEquals(array.get(i), new Boolean(true));
			}
		}
		array.close();
	}

	@Test
	public void testString() throws IOException {
		int objectNumber = 200;
		PrimitiveDiskArray array = new PrimitiveDiskArray();
		for (int i = 0; i < objectNumber; i++) {
			array.add("string" + i);
		}
		assertEquals(array.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(array.get(i), "string" + i);
		}
		array.close();
	}

	@Test
	public void testBigDecimal() throws IOException {
		int objectNumber = 3000;
		PrimitiveDiskArray array = new PrimitiveDiskArray();
		for (int i = 0; i < objectNumber; i++) {
			array.add(new BigDecimal("1010101010101010101010" + i));
		}
		assertEquals(array.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(array.get(i), new BigDecimal("1010101010101010101010" + i));
		}
		array.close();
	}

	@Test
	public void testDate() throws IOException {
		int objectNumber = 4101;
		PrimitiveDiskArray array = new PrimitiveDiskArray();
		for (int i = 0; i < objectNumber; i++) {
			array.add(new Date(1900100000 + i * 1000));
		}
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(array.get(i), new Date(1900100000 + i * 1000));
		}
		array.close();
	}
}
