/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * TestCases for Table of Content.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * <tr>
 * <td>{@link #testTocProperty()}</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetAllToc()}</td>
 * </tr>
 * </table>
 * 
 */
public class TocSupportTest extends BaseTestCase {
	String fileName = "TocSupportTest.xml";
	String fileName1 = "TocSupportTest_1.xml";

	public TocSupportTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {

		return new TestSuite(TocSupportTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + fileName);
		copyInputToFile(INPUT_FOLDER + "/" + fileName1);

	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Test set/getTocExpression
	 * 
	 * @throws Exception @SuppressWarnings("deprecation")
	 */

	public void testTocProperty() throws Exception {
		openDesign(fileName);

		TableHandle table = (TableHandle) designHandle.findElement("MyTable");
		assertNotNull("should not be null", table);
		assertNull(table.getTocExpression());
		table.setTocExpression("This Section");
		assertEquals("This Section", table.getTocExpression());
	}

	/**
	 * Test getAllTocs
	 * 
	 * @throws Exception
	 */
	public void testGetAllToc() throws Exception {
		openDesign(fileName1);
		assertEquals(2, designHandle.getAllTocs().size());

		// add toc
		TextItemHandle text = (TextItemHandle) designHandle.findElement("mytext");
		text.setTocExpression("Mytext");
		assertEquals(3, designHandle.getAllTocs().size());
		designHandle.getCommandStack().undo();
		assertEquals(2, designHandle.getAllTocs().size());
		designHandle.getCommandStack().redo();

		// remove toc
		text.clearProperty(IReportItemModel.TOC_PROP);
		assertEquals(2, designHandle.getAllTocs().size());
	}
}
