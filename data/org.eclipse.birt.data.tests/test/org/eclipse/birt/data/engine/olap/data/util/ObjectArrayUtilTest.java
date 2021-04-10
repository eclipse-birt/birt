
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

import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class ObjectArrayUtilTest {
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testObjectArrayUtil() {
		Object[][] objectArrays = new Object[3][];
		objectArrays[0] = new Object[2];
		objectArrays[1] = new Object[3];
		objectArrays[2] = new Object[4];
		objectArrays[0][0] = new Integer(0);
		objectArrays[0][1] = new Integer(1);
		objectArrays[1][0] = new Integer(2);
		objectArrays[1][1] = new Integer(3);
		objectArrays[1][2] = new Integer(4);
		objectArrays[2][0] = new Integer(5);
		objectArrays[2][1] = new Integer(6);
		objectArrays[2][2] = new Integer(7);
		objectArrays[2][3] = new Integer(8);
		Object[] objectArray = ObjectArrayUtil.convert(objectArrays);
		assertEquals(objectArray.length, 13);
		assertEquals(objectArray[0], new Integer(3));
		assertEquals(objectArray[1], new Integer(2));
		assertEquals(objectArray[2], new Integer(0));
		assertEquals(objectArray[3], new Integer(1));
		assertEquals(objectArray[4], new Integer(3));
		assertEquals(objectArray[5], new Integer(2));
		assertEquals(objectArray[6], new Integer(3));
		assertEquals(objectArray[7], new Integer(4));
		assertEquals(objectArray[8], new Integer(4));
		assertEquals(objectArray[9], new Integer(5));
		assertEquals(objectArray[10], new Integer(6));
		assertEquals(objectArray[11], new Integer(7));
		assertEquals(objectArray[12], new Integer(8));
		objectArrays = ObjectArrayUtil.convert(objectArray);

		assertEquals(objectArrays[0].length, 2);
		assertEquals(objectArrays[1].length, 3);
		assertEquals(objectArrays[2].length, 4);
		assertEquals(objectArrays[0][0], new Integer(0));
		assertEquals(objectArrays[0][1], new Integer(1));
		assertEquals(objectArrays[1][0], new Integer(2));
		assertEquals(objectArrays[1][1], new Integer(3));
		assertEquals(objectArrays[1][2], new Integer(4));
		assertEquals(objectArrays[2][0], new Integer(5));
		assertEquals(objectArrays[2][1], new Integer(6));
		assertEquals(objectArrays[2][2], new Integer(7));
		assertEquals(objectArrays[2][3], new Integer(8));
	}
}
