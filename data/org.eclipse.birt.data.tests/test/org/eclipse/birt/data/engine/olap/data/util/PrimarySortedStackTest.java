
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

import org.eclipse.birt.data.engine.olap.data.util.PrimitiveDiskSortedStack;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class PrimarySortedStackTest {
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
			PrimitiveDiskSortedStack stack = new PrimitiveDiskSortedStack(100, true, true);
			stack.push(new Integer(200));
			stack.push(new Integer(250));
			stack.push(new Integer(208));
			stack.push(new Integer(211));
			stack.push(new Integer(211));
			stack.push(new Integer(213));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(new Integer(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), new Integer(i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistinctAsc1() throws IOException {
		try {
			int objectNumber = 10001;
			PrimitiveDiskSortedStack stack = new PrimitiveDiskSortedStack(10, true, true);
			stack.push(new Integer(200));
			stack.push(new Integer(250));
			stack.push(new Integer(208));
			stack.push(new Integer(211));
			stack.push(new Integer(211));
			stack.push(new Integer(213));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(new Integer(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), new Integer(i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDistinctDesc() throws IOException {
		try {
			int objectNumber = 1001;
			PrimitiveDiskSortedStack stack = new PrimitiveDiskSortedStack(100, false, true);
			stack.push(new Integer(200));
			stack.push(new Integer(250));
			stack.push(new Integer(208));
			stack.push(new Integer(211));
			stack.push(new Integer(211));
			stack.push(new Integer(213));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(new Integer(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				assertEquals(stack.pop(), new Integer(objectNumber - 1 - i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNotDistinctAsc() throws IOException {
		try {
			int objectNumber = 1001;
			PrimitiveDiskSortedStack stack = new PrimitiveDiskSortedStack(100, true, false);
			stack.push(new Integer(2000));
			stack.push(new Integer(2050));
			stack.push(new Integer(2008));
			stack.push(new Integer(2011));
			stack.push(new Integer(2011));
			stack.push(new Integer(2013));
			for (int i = 0; i < objectNumber; i++) {
				if (i == 901) {
					i = 901;
				}
				stack.push(new Integer(i));
			}
			// assertEquals( stack.size( ), objectNumber );

			for (int i = 0; i < objectNumber; i++) {
				if (i == 901) {
					i = 901;
				}
				assertEquals(stack.pop(), new Integer(i));
			}
			assertEquals(stack.pop(), new Integer(2000));
			assertEquals(stack.pop(), new Integer(2008));
			assertEquals(stack.pop(), new Integer(2011));
			assertEquals(stack.pop(), new Integer(2011));
			assertEquals(stack.pop(), new Integer(2013));
			assertEquals(stack.pop(), new Integer(2050));
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNotDistinctDesc() throws IOException {
		try {
			int objectNumber = 1001;
			PrimitiveDiskSortedStack stack = new PrimitiveDiskSortedStack(100, false, false);
			stack.push(new Integer(2000));
			stack.push(new Integer(2050));
			stack.push(new Integer(2008));
			stack.push(new Integer(2011));
			stack.push(new Integer(2011));
			stack.push(new Integer(2013));
			for (int i = 0; i < objectNumber; i++) {
				stack.push(new Integer(i));
			}
			// assertEquals( stack.size( ), objectNumber );
			assertEquals(stack.pop(), new Integer(2050));
			assertEquals(stack.pop(), new Integer(2013));
			assertEquals(stack.pop(), new Integer(2011));
			assertEquals(stack.pop(), new Integer(2011));
			assertEquals(stack.pop(), new Integer(2008));
			assertEquals(stack.pop(), new Integer(2000));
			for (int i = 0; i < objectNumber; i++) {
				Object memberForTest = (Object) stack.pop();
				assertEquals(memberForTest, new Integer(objectNumber - 1 - i));
			}
			stack.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
