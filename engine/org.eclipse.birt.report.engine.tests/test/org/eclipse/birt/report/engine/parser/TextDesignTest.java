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

import org.eclipse.birt.report.engine.ir.TextItemDesign;

/**
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TextDesignTest extends AbstractDesignTestCase {
	protected TextItemDesign text;

	public void setUp() throws Exception {
		loadDesign("text-item-test.xml");
		text = (TextItemDesign) report.getContent(0);
		assertTrue(text != null);
	}

	public void testTextBasic() {
		// assertEquals( "dset", text.getDataSet( ).getName( ) );
		assertEquals("text", text.getName());
		assertEquals(1, text.getX().getMeasure(), Double.MIN_VALUE);
		assertEquals(2, text.getY().getMeasure(), Double.MIN_VALUE);
		assertEquals(3, text.getWidth().getMeasure(), Double.MIN_VALUE);
		assertEquals(4, text.getHeight().getMeasure(), Double.MIN_VALUE);
		assertEquals("auto", text.getTextType());

		assertEquals("text content", text.getText());
		assertEquals("content-key", text.getTextKey());
	}
}
