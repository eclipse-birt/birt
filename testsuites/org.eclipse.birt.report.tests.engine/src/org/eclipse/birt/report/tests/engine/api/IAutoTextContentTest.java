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

package org.eclipse.birt.report.tests.engine.api;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * Test IAutoTextContent API methods
 */
public class IAutoTextContentTest extends BaseEmitter {

	private String reportName = "IAutoTextContentTest";
	private static int count = 0;

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(reportName + ".rptdesign", reportName + ".rptdesign");
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	protected String getReportName() {
		return reportName;
	}

	/**
	 * Test set/getType()
	 */
	public void testGetType() {
		IAutoTextContent autoTextContent = new ReportContent().createAutoTextContent();
		autoTextContent.setType(1);
		assertEquals(1, autoTextContent.getType());
	}

	public void testAutoTextContentFromReport() throws Exception {
		this.runandthenrender_emitter(EMITTER_HTML);
	}

	public void startPage(IPageContent page) {
		IAutoTextContent autoTextContent = null;
		switch (count) {
		case 0:
			assertTrue(((ArrayList) page.getPageHeader().getChildren()).get(0) instanceof IAutoTextContent);
			autoTextContent = (IAutoTextContent) ((ArrayList) page.getPageHeader().getChildren()).get(0);
			assertEquals("1", autoTextContent.getText());
			assertEquals(IAutoTextContent.PAGE_NUMBER, autoTextContent.getType());
			assertTrue(((ArrayList) page.getPageFooter().getChildren()).get(0) instanceof IAutoTextContent);
			autoTextContent = (IAutoTextContent) ((ArrayList) page.getPageFooter().getChildren()).get(0);
			assertEquals("2", autoTextContent.getText());
			assertEquals(IAutoTextContent.TOTAL_PAGE, autoTextContent.getType());
			break;
		case 1:
			assertTrue(((ArrayList) page.getPageHeader().getChildren()).get(0) instanceof ITableContent);
			IContent tableContent = (ITableContent) ((ArrayList) page.getPageHeader().getChildren()).get(0);
			IContent rowContent = (IContent) ((ArrayList) tableContent.getChildren()).get(0);
			IContent cellContent = (IContent) ((ArrayList) rowContent.getChildren()).get(0);
			assertTrue(((ArrayList) cellContent.getChildren()).get(0) instanceof IAutoTextContent);
			autoTextContent = (IAutoTextContent) ((ArrayList) cellContent.getChildren()).get(0);
			assertEquals("2", autoTextContent.getText());
			assertEquals(IAutoTextContent.PAGE_NUMBER, autoTextContent.getType());

			cellContent = (IContent) ((ArrayList) rowContent.getChildren()).get(2);
			assertTrue(((ArrayList) cellContent.getChildren()).get(0) instanceof IAutoTextContent);
			autoTextContent = (IAutoTextContent) ((ArrayList) cellContent.getChildren()).get(0);
			assertEquals("2", autoTextContent.getText());
			assertEquals(IAutoTextContent.TOTAL_PAGE, autoTextContent.getType());

			break;
		}
		count++;
	}

}
