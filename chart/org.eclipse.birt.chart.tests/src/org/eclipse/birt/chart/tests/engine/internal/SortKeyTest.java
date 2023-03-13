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

package org.eclipse.birt.chart.tests.engine.internal;

import org.eclipse.birt.chart.internal.datafeed.GroupKey;
import org.eclipse.birt.chart.model.attribute.SortOption;

import junit.framework.TestCase;

public class SortKeyTest extends TestCase {
	GroupKey key;

	@Override
	protected void setUp() {
		key = new GroupKey("key", SortOption.ASCENDING_LITERAL); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() {
		key = null;
	}

	public void testGetKey() {
		assertEquals("key", key.getKey()); //$NON-NLS-1$
	}

	public void testGetDirection() {
		assertEquals(SortOption.ASCENDING_LITERAL, key.getDirection());
	}

	public void testGetKeyIndex() {
		key.setKeyIndex(2);
		assertEquals(2, key.getKeyIndex());
	}
}
