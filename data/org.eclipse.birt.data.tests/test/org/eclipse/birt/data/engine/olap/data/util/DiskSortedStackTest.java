
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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

/**
 *
 */

public class DiskSortedStackTest {
	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testDistinctAsc() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(100, true, true, MemberForTest2.getMemberCreator());
			stack.push(createMember(200));
			stack.push(createMember(250));
			stack.push(createMember(208));
			stack.push(createMember(211));
			stack.push(createMember(211));
			stack.push(createMember(213));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), createMember(i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistinctAsc1() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(100, true, true, MemberForTest2.getMemberCreator());
			stack.push(createMember(200));
			stack.push(createMember(250));
			stack.push(createMember(208));
			stack.push(createMember(211));
			stack.push(createMember(211));
			stack.push(createMember(213));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(1));
			}
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(2));
			}
			// assertEquals( stack.size( ), objectNumber );

			assertEquals(stack.pop(), createMember(1));
			assertEquals(stack.pop(), createMember(2));

			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistinctAsc2() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(2000, true, true, MemberForTest2.getMemberCreator());
			stack.push(createMember(200));
			stack.push(createMember(250));
			stack.push(createMember(208));
			stack.push(createMember(211));
			stack.push(createMember(211));
			stack.push(createMember(213));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), createMember(i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistinctAsc3() throws IOException {
		try {
			int objectNumber = 10001;
			DiskSortedStack stack = new DiskSortedStack(10, true, true, MemberForTest2.getMemberCreator());
			stack.push(createMember(200));
			stack.push(createMember(250));
			stack.push(createMember(208));
			stack.push(createMember(211));
			stack.push(createMember(211));
			stack.push(createMember(213));
			for (int i = objectNumber - 1; i >= 0; i--) {
				stack.push(createMember(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), createMember(i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNotDistinctAsc3() throws IOException {
		try {
			int objectNumber = 10001;
			DiskSortedStack stack = new DiskSortedStack(10, true, false, MemberForTest2.getMemberCreator());
			stack.push(createMember(100200));
			stack.push(createMember(100250));
			stack.push(createMember(100208));
			stack.push(createMember(100211));
			stack.push(createMember(100211));
			stack.push(createMember(100213));
			for (int i = objectNumber - 1; i >= 0; i--) {
				stack.push(createMember(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), createMember(i));
			}

			assertEquals(stack.pop(), createMember(100200));
			assertEquals(stack.pop(), createMember(100208));
			assertEquals(stack.pop(), createMember(100211));
			assertEquals(stack.pop(), createMember(100211));
			assertEquals(stack.pop(), createMember(100213));
			assertEquals(stack.pop(), createMember(100250));

			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStess() throws IOException {
		try {
			long startTime = System.currentTimeMillis();
			int objectNumber = 10000;
			DiskSortedStack stack = new DiskSortedStack(4000, true, true, MemberForStressTest.getMemberCreator());
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMemberForStressTest(i));
			}
			// assertEquals( stack.size( ), objectNumber );
			System.out.println("used push:" + (System.currentTimeMillis() - startTime) / 100);
			for (int i = 0; i < objectNumber; i++) {
				stack.pop();
			}
			stack.close();
			System.out.println("used pop:" + (System.currentTimeMillis() - startTime) / 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistinctDesc() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(100, false, true, MemberForTest2.getMemberCreator());
			stack.push(createMember(200));
			stack.push(createMember(250));
			stack.push(createMember(208));
			stack.push(createMember(211));
			stack.push(createMember(211));
			stack.push(createMember(213));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), createMember(objectNumber - 1 - i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistinctDesc1() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(100, false, true, MemberForTest2.getMemberCreator());

			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(10));
			}
			// assertEquals( stack.size( ), objectNumber );

			assertEquals(stack.pop(), createMember(10));
			assertEquals(stack.pop(), null);
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNotDistinctAsc() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(100, true, false, MemberForTest2.getMemberCreator());
			stack.push(createMember(2000));
			stack.push(createMember(2050));
			stack.push(createMember(2008));
			stack.push(createMember(2011));
			stack.push(createMember(2011));
			stack.push(createMember(2013));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), createMember(i));
			}
			assertEquals(stack.pop(), createMember(2000));
			assertEquals(stack.pop(), createMember(2008));
			assertEquals(stack.pop(), createMember(2011));
			assertEquals(stack.pop(), createMember(2011));
			assertEquals(stack.pop(), createMember(2013));
			assertEquals(stack.pop(), createMember(2050));
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNotDistinctDesc() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(100, false, false, MemberForTest2.getMemberCreator());
			stack.push(createMember(2000));
			stack.push(createMember(2050));
			stack.push(createMember(2008));
			stack.push(createMember(2011));
			stack.push(createMember(2011));
			stack.push(createMember(2013));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(i));
			}
			// assertEquals( stack.size( ), objectNumber );
			assertEquals(stack.pop(), createMember(2050));
			assertEquals(stack.pop(), createMember(2013));
			assertEquals(stack.pop(), createMember(2011));
			assertEquals(stack.pop(), createMember(2011));
			assertEquals(stack.pop(), createMember(2008));
			assertEquals(stack.pop(), createMember(2000));
			for (int i = 0; i < objectNumber; i++) {
				MemberForTest2 memberForTest = (MemberForTest2) stack.pop();
				assertEquals(memberForTest, createMember(objectNumber - 1 - i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNotDistinctDesc1() throws IOException {
		try {
			int objectNumber = 1001;
			DiskSortedStack stack = new DiskSortedStack(100, false, false, MemberForTest2.getMemberCreator());

			for (int i = 0; i < objectNumber; i++) {
				stack.push(createMember(10));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), createMember(10));
			}
			assertEquals(stack.pop(), null);
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static private MemberForTest2 createMember(int i) {
		int iField = i;
		Date dateField = new Date(190001000 + i * 1000);
		String stringField = "string" + i;
		double doubleField = i + 10.0;
		BigDecimal bigDecimalField = new BigDecimal("1010101010100101010110" + i);
		boolean booleanField = (i % 2 == 0 ? true : false);
		return new MemberForTest2(iField, dateField, stringField, doubleField, bigDecimalField, booleanField);
	}

	static private MemberForStressTest createMemberForStressTest(int i) {
		int iField = i;
		Date dateField = new Date(190001000 + i * 1000);
		String stringField = "string" + i;
		return new MemberForStressTest(iField, dateField, stringField);
	}

}
