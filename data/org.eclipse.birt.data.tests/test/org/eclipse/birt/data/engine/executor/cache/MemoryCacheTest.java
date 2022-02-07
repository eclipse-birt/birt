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
package org.eclipse.birt.data.engine.executor.cache;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IResultObject;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test next and fetch function of MemoryCacheTest
 */
public class MemoryCacheTest {
	private IResultObject[] resultObjects;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void memoryCacheSetUp() throws DataException {
		List columnsList = new ArrayList();
		ResultFieldMetadata columnMetaData = new ResultFieldMetadata(1, "col1", "col1",
				DataType.getClass(DataType.STRING_TYPE), "String" /* nativeTypeName */, true);
		columnsList.add(columnMetaData);

		resultObjects = new ResultObject[] { new ResultObject(new ResultClass(columnsList), new Object[] { "abc" }),
				new ResultObject(new ResultClass(columnsList), new Object[] { "def" }) };
	}

	/**
	 * Test next function
	 */
	@Test
	public void testNext() throws DataException {
		MemoryCache memoryCache = new MemoryCache(resultObjects, resultObjects[0].getResultClass(), null);
		int i = 0;

		assertEquals(memoryCache.getCurrentIndex(), -1);
		while (memoryCache.next()) {
			if (i == 0) {
				assertEquals(memoryCache.getCurrentIndex(), 0);
				assertEquals(memoryCache.getCurrentResult().getFieldValue(1).toString(), "abc");
			} else if (i == 1) {
				assertEquals(memoryCache.getCurrentIndex(), 1);
				assertEquals(memoryCache.getCurrentResult().getFieldValue(1).toString(), "def");
			}
			i++;
		}

		for (i = 0; i < 5; i++) {
			assertEquals(memoryCache.next(), false);
			assertEquals(memoryCache.getCurrentIndex(), 2);
			assertEquals(memoryCache.getCurrentResult(), null);
		}
	}

	/**
	 * Test fetch function
	 */
	@Test
	public void testFetch() throws DataException {
		MemoryCache memoryCache = new MemoryCache(resultObjects, resultObjects[0].getResultClass(), null);
		int i = 0;
		while (memoryCache.fetch() != null) {
			if (i == 0)
				assertEquals(memoryCache.getCurrentResult().getFieldValue(1).toString(), "abc");
			else if (i == 1)
				assertEquals(memoryCache.getCurrentResult().getFieldValue(1).toString(), "def");
			i++;
		}
		assertEquals(i, 2);
	}

}
