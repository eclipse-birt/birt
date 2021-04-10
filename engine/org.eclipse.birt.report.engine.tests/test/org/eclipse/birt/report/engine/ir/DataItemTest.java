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
 * DataItem test
 * 
 */
public class DataItemTest extends ReportItemTestCase {

	public DataItemTest() {
		super(new DataItemDesign());
	}

	/**
	 * Test all get/set accessorss
	 * 
	 * set values of the data item
	 * 
	 * then get the values one by one to test if they work correctly
	 */

	public void testAccessor() {
		DataItemDesign dataItem = (DataItemDesign) element;
		ActionDesign action = new ActionDesign();
		String exp = "field";

		// Set
		dataItem.setAction(action);
		dataItem.setBindingColumn(exp);

		// Get
		assertEquals(dataItem.getAction(), action);
		assertEquals(exp, dataItem.getBindingColumn());
	}
}
