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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.LineHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.LineItem;

/**
 * The test case of LineItem parser and writer.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test properties of LineItem after parsing design file</td>
 * <td>All properties are right</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Compare the written file with the golden file</td>
 * <td>Two files are same</td>
 * </tr>
 * </table>
 * 
 * @see LineItem
 */

public class LineItemParseTest extends ParserTestCase {

	String fileName = "LineItemParseTest.xml"; //$NON-NLS-1$
	String outFileName = "LineItemParseTest_out.xml"; //$NON-NLS-1$
	String goldenFileName = "LineItemParseTest_golden.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		LineHandle lineHandle = getLineHandle();

		assertEquals(DesignChoiceConstants.LINE_ORIENTATION_VERTICAL, lineHandle.getOrientation());
	}

	/**
	 * This test writes the design file and compare it with golden file.
	 * 
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		LineHandle lineHandle = getLineHandle();
		lineHandle.setOrientation(DesignChoiceConstants.LINE_ORIENTATION_HORIZONTAL);

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Gets the line handle.
	 * 
	 * @return the handle of the line
	 * 
	 * @throws Exception
	 */

	private LineHandle getLineHandle() throws Exception {
		openDesign(fileName);
		MasterPageHandle page = designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		assertNotNull(page);
		SlotHandle contents = page.getSlot(0);
		assertEquals(1, contents.getCount());

		LineHandle lineHandle = (LineHandle) contents.get(0);
		return lineHandle;
	}
}