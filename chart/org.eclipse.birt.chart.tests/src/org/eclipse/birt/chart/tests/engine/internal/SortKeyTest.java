/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.internal;

import junit.framework.TestCase;

import org.eclipse.birt.chart.internal.datafeed.GroupKey;
import org.eclipse.birt.chart.model.attribute.SortOption;

public class SortKeyTest extends TestCase {
	GroupKey key;

	protected void setUp() {
		key = new GroupKey("key", SortOption.ASCENDING_LITERAL); //$NON-NLS-1$
	}

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
