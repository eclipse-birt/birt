/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.RectangleHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.elements.Style;

/**
 * The test case of rectangle parser and writer.
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
 * <td>Test all properties after parsing design file</td>
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
 */

public class RectangleItemParseTest extends ParserTestCase {
	String fileName = "RectangleItemParseTest.xml"; //$NON-NLS-1$
	String outFileName = "RectangleItemParseTest_out.xml"; //$NON-NLS-1$
	String goldenFileName = "RectangleItemParseTest_golden.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of rectangle.
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(fileName);

		GraphicMasterPageHandle masterPage = (GraphicMasterPageHandle) designHandle.getMasterPages().get(0);
		RectangleHandle rectangle = (RectangleHandle) masterPage.getContent().get(0);

		SharedStyleHandle style = designHandle.findStyle("My-Style"); //$NON-NLS-1$

		assertNotNull(rectangle);
		assertEquals("12mm", rectangle.getX().getStringValue()); //$NON-NLS-1$
		assertEquals("39mm", rectangle.getY().getStringValue()); //$NON-NLS-1$
		assertEquals("100mm", rectangle.getWidth().getStringValue()); //$NON-NLS-1$
		assertEquals("20mm", rectangle.getHeight().getStringValue()); //$NON-NLS-1$

		assertEquals(style.getStringProperty(Style.BACKGROUND_COLOR_PROP),
				rectangle.getStringProperty(Style.BACKGROUND_COLOR_PROP));
		assertEquals("white", rectangle.getStringProperty(Style.BORDER_TOP_COLOR_PROP)); //$NON-NLS-1$
		assertEquals("white", rectangle.getStringProperty(Style.BORDER_LEFT_COLOR_PROP)); //$NON-NLS-1$
		assertEquals("white", rectangle.getStringProperty(Style.BORDER_RIGHT_COLOR_PROP)); //$NON-NLS-1$
		assertEquals("white", rectangle.getStringProperty(Style.BORDER_BOTTOM_COLOR_PROP)); //$NON-NLS-1$

	}

	/**
	 * This test writes the design file and compare it with golden file.
	 * 
	 * @throws Exception
	 */

	public void testWriter() throws Exception {
		assertTrue(openWriteAndCompare(fileName, outFileName, goldenFileName));
	}

}
