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
