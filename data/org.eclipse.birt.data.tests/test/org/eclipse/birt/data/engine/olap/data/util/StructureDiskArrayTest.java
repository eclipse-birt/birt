
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

import org.eclipse.birt.data.engine.olap.data.util.StructureDiskArray;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class StructureDiskArrayTest {
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testMemberForTest() throws IOException {
		int objectNumber = 1001;
		StructureDiskArray array = new StructureDiskArray(MemberForTest.getMemberCreator());
		for (int i = 0; i < objectNumber; i++) {
			array.add(createMember(i));
		}
		assertEquals(array.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(array.get(i), createMember(i));
		}
		array.close();
	}

	@Test
	public void testMemberForTest1() throws IOException {
		int objectNumber = 1001;
		StructureDiskArray array = new StructureDiskArray(MemberForTest.getMemberCreator());
		for (int i = 0; i < objectNumber; i++) {
			if (i == 100) {
				i = i + 1 - 1;
			}
			array.add(createMember1(i));
		}
		assertEquals(array.size(), objectNumber);
		for (int i = 0; i < objectNumber; i++) {
			System.out.println(i);
			assertEquals(array.get(i), createMember1(i));
		}
		array.close();
	}

	@Test
	public void testMemberForTest2() throws IOException {
		int objectNumber = 1001;
		StructureDiskArray array = new StructureDiskArray(MemberForTest.getMemberCreator());
		for (int i = 0; i < objectNumber; i++) {
			array.add(createMember(i));
		}
		assertEquals(array.size(), objectNumber);
		array.add(createMember(200));
		array.add(createMember(200));
		array.add(createMember(205));
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(array.get(i), createMember(i));
		}
		assertEquals(array.get(objectNumber), createMember(200));
		assertEquals(array.get(objectNumber + 1), createMember(200));
		assertEquals(array.get(objectNumber + 2), createMember(205));
		array.close();
	}

	static private MemberForTest createMember(int i) {
		int iField = i;
		Date dateField = new Date(190001000 + i * 1000);
		String stringField = "string" + i;
		double doubleField = i + 10.0;
		BigDecimal bigDecimalField = new BigDecimal("1010101010100101010110" + i);
		boolean booleanField = (i % 2 == 0 ? true : false);
		return new MemberForTest(iField, dateField, stringField, doubleField, bigDecimalField, booleanField);
	}

	static private MemberForTest createMember1(int i) {
		int iField = i;
		Date dateField = new Date(190001000 + i * 1000);
		String stringField = "string" + i;
		double doubleField = i + 10.0;
		BigDecimal bigDecimalField = new BigDecimal("1010101010100101010110" + i);
		boolean booleanField = (i % 2 == 0 ? true : false);
		if (i < 200) {
			dateField = null;
			stringField = null;
			bigDecimalField = null;
		}
		return new MemberForTest(iField, dateField, stringField, doubleField, bigDecimalField, booleanField);
	}
}
