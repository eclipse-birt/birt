
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionKey;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.junit.Test;

/**
 *
 */

public class FactTableRowTest {
	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testSaveAndLoad() throws IOException {
		final int rowLen = 100000;
		final int FACT_TABLE_BUFFER_SIZE = 40000;
		DiskSortedStack result = new DiskSortedStack(FACT_TABLE_BUFFER_SIZE, true, false, FactTableRow.getCreator());
		for (int i = rowLen - 1; i >= 0; i--) {
			result.push(createRow(i));
		}
		for (int i = 0; i < rowLen; i++) {
			checkEquals((FactTableRow) result.pop(), createRow(i));
		}
		result.close();
	}

	@Test
	public void testSaveAndLoad2() throws IOException {
		final int rowLen = 100000;
		final int FACT_TABLE_BUFFER_SIZE = 40000;
		DiskSortedStack result = new DiskSortedStack(FACT_TABLE_BUFFER_SIZE, true, false, FactTableRow.getCreator());
		for (int i = rowLen - 1; i >= 0; i--) {
			result.push(createRow2(i));
		}
		for (int i = 0; i < rowLen; i++) {
			checkEquals((FactTableRow) result.pop(), createRow2(i));
		}
		result.close();
	}

	private void checkEquals(FactTableRow factTableRow1, FactTableRow factTableRow2) {
		assertEquals(factTableRow1, factTableRow2);
		assertEquals(factTableRow1.getMeasures().length, factTableRow2.getMeasures().length);
		for (int i = 0; i < factTableRow2.getMeasures().length; i++) {
			assertEquals(factTableRow1.getMeasures()[i], factTableRow2.getMeasures()[i]);
		}
	}

	private FactTableRow createRow(int iValue) {
		final int dimensionLen = 3;
		final int measureLen = 3;
		FactTableRow factTableRow = new FactTableRow();
		factTableRow.setDimensionKeys(new DimensionKey[dimensionLen]);
		for (int i = 0; i < dimensionLen; i++) {
			factTableRow.getDimensionKeys()[i] = new DimensionKey(i);
			for (int j = 0; j < i; j++) {
				factTableRow.getDimensionKeys()[i].getKeyValues()[j] = new Integer(iValue + j);
			}
		}
		factTableRow.setMeasures(new Object[measureLen]);
		for (int i = 0; i < measureLen; i++) {
			factTableRow.getMeasures()[i] = new Integer(iValue + i);
		}
		return factTableRow;
	}

	private FactTableRow createRow2(int iValue) {
		final int dimensionLen = 3;
		final int measureLen = 3;
		FactTableRow factTableRow = new FactTableRow();
		factTableRow.setDimensionKeys(new DimensionKey[dimensionLen]);
		for (int i = 0; i < dimensionLen; i++) {
			factTableRow.getDimensionKeys()[i] = new DimensionKey(i);
			for (int j = 0; j < i; j++) {
				if (j != 1) {
					factTableRow.getDimensionKeys()[i].getKeyValues()[j] = new Integer(iValue + j);
				}
			}
		}
		factTableRow.setMeasures(new Object[measureLen]);
		for (int i = 0; i < measureLen; i++) {
			factTableRow.getMeasures()[i] = new Integer(iValue + i);
		}
		return factTableRow;
	}
}
