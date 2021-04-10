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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;

/**
 * 
 */
public class DynamicTextItemDesignTest extends AbstractDesignTestCase {

	protected DynamicTextItemDesign dynamicText;

	public void setUp() throws Exception {
		loadDesign("dynamic-text-test.xml");
		dynamicText = (DynamicTextItemDesign) report.getContent(0);
		assertTrue(dynamicText != null);
	}

	public void testMultiBasic() {
		assertEquals(1, dynamicText.getX().getMeasure(), Double.MIN_VALUE);
		assertEquals(2, dynamicText.getY().getMeasure(), Double.MIN_VALUE);
		assertEquals(3, dynamicText.getWidth().getMeasure(), Double.MIN_VALUE);
		assertEquals(4, dynamicText.getHeight().getMeasure(), Double.MIN_VALUE);
		assertEquals("dynamic_text", dynamicText.getName());
		assertEquals("row[\"COLUMN_4\"]",

				dynamicText.getContent().getScriptText());
		assertEquals("dset.contentType", dynamicText.getContentType());
	}
}
