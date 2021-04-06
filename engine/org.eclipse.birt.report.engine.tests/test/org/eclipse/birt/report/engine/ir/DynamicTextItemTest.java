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

package org.eclipse.birt.report.engine.ir;

/**
 * 
 */
public class DynamicTextItemTest extends ReportItemTestCase {
	public DynamicTextItemTest() {
		super(new DynamicTextItemDesign());
	}

	/**
	 * Test get/setExpression methods
	 * 
	 * set an expression
	 * 
	 * then get the expression and check the text type to test if they work
	 * correctly
	 */
	public void testExpression() {
		DynamicTextItemDesign multi = new DynamicTextItemDesign();
		Expression exp = Expression.newScript("content");
		String type = "auto";

		// Set
		multi.setContent(exp);
		multi.setContentType(type);

		// Get
		assertEquals(multi.getContent(), exp);
		assertEquals(multi.getContentType(), type);
	}

}
