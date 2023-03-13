
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
package org.eclipse.birt.data.engine.olap.data.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionKey;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.junit.Test;

/**
 *
 */

public class DimensionKeyTest {
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
		int keyCount = 10000;
		BufferedStructureArray bufferedStructureArray = new BufferedStructureArray(DimensionKey.getCreator(), 2000);
		for (int i = 0; i < keyCount; i++) {
			bufferedStructureArray.add(create(i));
		}
		for (int i = 0; i < keyCount; i++) {
			assertEquals(bufferedStructureArray.get(i), create(i));
		}
		bufferedStructureArray.clear();
		bufferedStructureArray.close();
	}

	private DimensionKey create(int i) {
		DimensionKey key = new DimensionKey(3);
		key.getKeyValues()[0] = new Integer(i);
		key.getKeyValues()[1] = String.valueOf(i + 1);
		key.getKeyValues()[2] = new Date(i + 2);
		return key;
	}
}
