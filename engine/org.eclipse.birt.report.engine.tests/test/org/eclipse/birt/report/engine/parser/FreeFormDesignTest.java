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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;

/**
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FreeFormDesignTest extends AbstractDesignTestCase {
	protected FreeFormItemDesign freeItem;

	@Override
	public void setUp() throws Exception {
		loadDesign("FreeFormItem_test.xml");
		freeItem = (FreeFormItemDesign) report.getContent(0);
		assertTrue(freeItem != null);
	}

	public void testFreeForm() {
		assertEquals(3, freeItem.getHeight().getMeasure(), Double.MIN_VALUE);
		assertEquals(3, freeItem.getWidth().getMeasure(), Double.MIN_VALUE);
		assertEquals(2, freeItem.getX().getMeasure(), Double.MIN_VALUE);
		assertEquals(3, freeItem.getY().getMeasure(), Double.MIN_VALUE);
		assertEquals("myFreeForm", freeItem.getName());
		assertEquals(1, freeItem.getItemCount());
	}

}
