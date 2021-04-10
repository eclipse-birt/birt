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

import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * Test Parser.
 * 
 */
public class LabelItemDesignTest extends TestCase {

	protected Report report = null;
	protected FreeFormItemDesign freeItem;

	public void setUp() throws Exception {
		String SAMPLE_DESIGN = "labelItem_test.xml";
		InputStream in = this.getClass().getResourceAsStream(SAMPLE_DESIGN);
		assertTrue(in != null);
		ReportParser parser = new ReportParser();
		report = parser.parse("", in);
		assertTrue(report != null);
		assertTrue(report.getErrors().isEmpty());
		freeItem = (FreeFormItemDesign) report.getContent(0);
		assertTrue(freeItem != null);
	}

	public void tearDown() {
	}

	/**
	 * test case to test the parser,especially the capability to parse the Lable. To
	 * get the content about Label from an external file and then compare the
	 * expected result with the real result of each property of DataSet. If they are
	 * the same,that means the IR is correct, otherwise, there exists errors in the
	 * parser
	 */

	public void testLabelItem() {
		LabelItemDesign label = (LabelItemDesign) freeItem.getItem(0);
		assertTrue(label != null);
		assertEquals("0cm", label.getX().toString());
		assertEquals("10cm", label.getY().toString());
		assertEquals("1.2cm", label.getHeight().toString());
		assertEquals("10cm", label.getWidth().toString());
		assertEquals("myLabel", label.getName());
		assertEquals("PAGE HEADER", label.getText());
		// assertEquals( "test2", label.getDataSet( ).getName( ) );
	}

	// TODO: test failed because DE doesn't support Action.
	public void testLabelAction() {
		LabelItemDesign label = (LabelItemDesign) freeItem.getItem(0);
		assertTrue(label != null);
		assertEquals("http://www.2t.cn", label.getAction().getHyperlink().getScriptText());
	}
}
