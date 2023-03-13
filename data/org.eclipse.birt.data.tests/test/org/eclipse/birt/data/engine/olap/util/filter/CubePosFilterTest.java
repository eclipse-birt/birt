
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.util.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.junit.Test;

/**
 *
 */

public class CubePosFilterTest {
	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Test
	public void testValidFilter1() throws DataException, IOException {
		ValidCubePosFilter validCubePosFilter = new ValidCubePosFilter(new String[] { "country", "year", "product" });
		SimpleDiskList[] simpleDiskLists = new SimpleDiskList[3];
		simpleDiskLists[0] = new SimpleDiskList(new int[] { 1, 2, 3, 4, 6, 8, 9, 10, 11, 18 });
		simpleDiskLists[1] = new SimpleDiskList(new int[] { 11, 12, 13, 14, 6, 8, 9, 10, 11, 18, 19 });
		simpleDiskLists[2] = new SimpleDiskList(new int[] { 1, 3, 5, 7, 9, 18, 29, 40, 51, 58, 69 });
		validCubePosFilter.addDimPositions(simpleDiskLists);
		simpleDiskLists = new SimpleDiskList[3];
		simpleDiskLists[0] = new SimpleDiskList(new int[] { 101, 102, 103 });
		simpleDiskLists[1] = new SimpleDiskList(new int[] { 119 });
		simpleDiskLists[2] = new SimpleDiskList(new int[] { 12, 69 });
		validCubePosFilter.addDimPositions(simpleDiskLists);
		assertTrue(validCubePosFilter.getFilterResult(new int[] { 101, 119, 12 }));
		assertTrue(validCubePosFilter.getFilterResult(new int[] { 1, 11, 1 }));
		assertTrue(validCubePosFilter.getFilterResult(new int[] { 1, 19, 69 }));
		assertTrue(validCubePosFilter.getFilterResult(new int[] { 18, 19, 69 }));
		assertTrue(validCubePosFilter.getFilterResult(new int[] { 11, 18, 69 }));
		assertTrue(validCubePosFilter.getFilterResult(new int[] { 8, 8, 51 }));
		assertTrue(validCubePosFilter.getFilterResult(new int[] { 1, 11, 69 }));
		assertFalse(validCubePosFilter.getFilterResult(new int[] { 1, 10, 11 }));
		assertFalse(validCubePosFilter.getFilterResult(new int[] { 0, 0, 0 }));
		assertFalse(validCubePosFilter.getFilterResult(new int[] { 1, 0, 69 }));
		assertFalse(validCubePosFilter.getFilterResult(new int[] { 1, 119, 69 }));
		assertFalse(validCubePosFilter.getFilterResult(new int[] { 1, 11, 12 }));
	}

	@Test
	public void testInvalidFilter1() throws DataException, IOException {
		InvalidCubePosFilter invalidCubePosFilter = new InvalidCubePosFilter(
				new String[] { "country", "year", "product" });
		SimpleDiskList[] simpleDiskLists = new SimpleDiskList[3];
		simpleDiskLists[0] = new SimpleDiskList(new int[] { 1, 2, 3, 4, 6, 8, 9, 10, 11, 18 });
		simpleDiskLists[1] = new SimpleDiskList(new int[] { 11, 12, 13, 14, 6, 8, 9, 10, 11, 18, 19 });
		simpleDiskLists[2] = new SimpleDiskList(new int[] { 1, 3, 5, 7, 9, 18, 29, 40, 51, 58, 69 });
		invalidCubePosFilter.addDimPositions(simpleDiskLists);
		simpleDiskLists = new SimpleDiskList[3];
		simpleDiskLists[0] = new SimpleDiskList(new int[] { 101, 102, 103 });
		simpleDiskLists[1] = new SimpleDiskList(new int[] { 119 });
		simpleDiskLists[2] = new SimpleDiskList(new int[] { 12, 69 });
		invalidCubePosFilter.addDimPositions(simpleDiskLists);
		assertFalse(invalidCubePosFilter.getFilterResult(new int[] { 101, 119, 12 }));
		assertFalse(invalidCubePosFilter.getFilterResult(new int[] { 1, 11, 1 }));
		assertFalse(invalidCubePosFilter.getFilterResult(new int[] { 1, 19, 69 }));
		assertFalse(invalidCubePosFilter.getFilterResult(new int[] { 18, 19, 69 }));
		assertFalse(invalidCubePosFilter.getFilterResult(new int[] { 11, 18, 69 }));
		assertFalse(invalidCubePosFilter.getFilterResult(new int[] { 8, 8, 51 }));
		assertFalse(invalidCubePosFilter.getFilterResult(new int[] { 1, 11, 69 }));
		assertTrue(invalidCubePosFilter.getFilterResult(new int[] { 1, 10, 11 }));
		assertTrue(invalidCubePosFilter.getFilterResult(new int[] { 0, 0, 0 }));
		assertTrue(invalidCubePosFilter.getFilterResult(new int[] { 1, 0, 69 }));
		assertTrue(invalidCubePosFilter.getFilterResult(new int[] { 1, 119, 69 }));
		assertTrue(invalidCubePosFilter.getFilterResult(new int[] { 1, 11, 12 }));
	}
}

class SimpleDiskList implements IDiskArray {
	private Integer[] values = null;

	SimpleDiskList(int[] iValues) {
		values = new Integer[iValues.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = new Integer(iValues[i]);
		}
	}

	@Override
	public boolean add(Object o) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object get(int index) throws IOException {
		return values[index];
	}

	@Override
	public int size() {
		return values.length;
	}
}
