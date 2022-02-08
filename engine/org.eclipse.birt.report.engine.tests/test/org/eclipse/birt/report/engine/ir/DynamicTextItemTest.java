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
