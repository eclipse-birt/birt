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

import java.util.List;

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for master page parsing, writing and referring.
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
 * <td>Test whether all master page properties can be read.</td>
 * <td>All master page property values should be right.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Test writer.</td>
 * <td>The output file should be same as golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticError()}</td>
 * <td>Test semantic errors with the design file input.</td>
 * <td>The errors are collected, such as the height or width is negative, the
 * height or width is unset when the page type is customized, the height or
 * width is set when the page type is not customized, the value of margin is
 * larger than the height or width.</td>
 * </tr>
 * </table>
 * 
 */

public class MasterPageParseTest extends BaseTestCase {

	private String fileName = "MasterPageParseTest.xml"; //$NON-NLS-1$
	private String simpleMasterPageFile = "SimpleMasterPageTest.xml"; //$NON-NLS-1$
	private String goldenFileName = "MasterPageParseTest_golden.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test parser and getting property.
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(fileName);
		List<ErrorDetail> errors = design.getErrorList();
		assertTrue(errors.isEmpty());

		GraphicMasterPageHandle page = (GraphicMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		assertNotNull(page);

		assertEquals(DesignChoiceConstants.PAGE_SIZE_US_LETTER, page.getPageType());
		assertEquals(DesignChoiceConstants.PAGE_ORIENTATION_PORTRAIT, page.getOrientation());

		assertEquals(2, page.getColumnCount());
		assertEquals("2mm", page.getColumnSpacing().getStringValue()); //$NON-NLS-1$
		assertEquals("yellow", page.getPrivateStyle().getBackgroundColor().getStringValue()); //$NON-NLS-1$
		assertEquals("red", page.getPrivateStyle().getColor().getStringValue()); //$NON-NLS-1$
		assertEquals("12mm", page.getPrivateStyle().getFontSize().getStringValue()); //$NON-NLS-1$
		assertEquals("1in", page.getTopMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("2in", page.getBottomMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("1in", page.getLeftMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("2in", page.getRightMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("script of onPageStart", page.getOnPageStart()); //$NON-NLS-1$
		assertEquals("script of onPageEnd", page.getOnPageEnd()); //$NON-NLS-1$

		SimpleMasterPageHandle simplePage = (SimpleMasterPageHandle) designHandle.findMasterPage("Simple MasterPage"); //$NON-NLS-1$
		assertEquals("0.25in", simplePage.getTopMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.25in", simplePage.getBottomMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.25in", simplePage.getLeftMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.25in", simplePage.getRightMargin().getStringValue()); //$NON-NLS-1$

	}

	/**
	 * Test writer.
	 * 
	 * @throws Exception any exception caught
	 */
	public void testWriter() throws Exception {
		openDesign(fileName);

		GraphicMasterPageHandle page = (GraphicMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		page.setColumnCount(5);

		assertEquals(5, page.getColumnCount());
		assertEquals("2mm", page.getColumnSpacing().getStringValue()); //$NON-NLS-1$

		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE);
		page.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LEGAL);

		page.setOnPageStart("new script of onPageStart"); //$NON-NLS-1$
		page.setOnPageEnd("new script of onPageEnd"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * @throws Exception
	 * 
	 */

	public void testSimpleMasterPage() throws Exception {

		openDesign(simpleMasterPageFile);

		SimpleMasterPageHandle simplePage = (SimpleMasterPageHandle) designHandle.findMasterPage("Simple Page"); //$NON-NLS-1$

		simplePage.setOrientation("landscape"); //$NON-NLS-1$
		assertEquals("landscape", simplePage.getOrientation()); //$NON-NLS-1$

		simplePage.setName("New Simple Page"); //$NON-NLS-1$
		assertEquals("New Simple Page", simplePage.getName()); //$NON-NLS-1$

		assertEquals("2.4cm", simplePage.getHeaderHeight().getValue().toString()); //$NON-NLS-1$
		assertEquals("1.2cm", simplePage.getFooterHeight().getValue().toString()); //$NON-NLS-1$

		save();
		assertTrue(compareFile("TestSimpleMasterPage_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testMasterPageContainment() throws Exception {
		openDesign(simpleMasterPageFile);
		SimpleMasterPageHandle simplePage = (SimpleMasterPageHandle) designHandle.findMasterPage("Simple Page"); //$NON-NLS-1$
		simplePage.getPageHeader().drop(0);

		// add a grid in which contains a table into the simple page

		try {
			GridHandle grid = designHandle.getElementFactory().newGridItem("grid", 1, 1); //$NON-NLS-1$
			TableHandle table = designHandle.getElementFactory().newTableItem("table"); //$NON-NLS-1$
			grid.getCell(1, 1).getContent().add(table);
			simplePage.getPageHeader().add(grid);
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT, e.getErrorCode());
		} catch (NameException e) {
			assert false;
		}

		// add a table into a grid which lies in header of page

		try {
			GridHandle grid = designHandle.getElementFactory().newGridItem("grid", 1, 1); //$NON-NLS-1$
			TableHandle table = designHandle.getElementFactory().newTableItem("table"); //$NON-NLS-1$
			simplePage.getPageHeader().add(grid);
			grid.getCell(1, 1).getContent().add(table);
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT, e.getErrorCode());
		} catch (NameException e) {
			assert false;
		}

	}

	/**
	 * Tests the semantic errors of the master page.
	 * 
	 * @throws Exception
	 */

	public void testSemanticError() throws Exception {
		String checkFileName1 = "MasterPageParseTest_1.xml"; //$NON-NLS-1$
		String checkFileName2 = "MasterPageParseTest_2.xml"; //$NON-NLS-1$
		String checkFileName3 = "MasterPageParseTest_3.xml"; //$NON-NLS-1$
		String checkFileName4 = "MasterPageParseTest_4.xml"; //$NON-NLS-1$
		String checkFileName5 = "MasterPageParseTest_5.xml"; //$NON-NLS-1$
		String checkFileName6 = "MasterPageParseTest_6.xml"; //$NON-NLS-1$

		openDesign(checkFileName1);
		List<ErrorDetail> errors = design.getErrorList();

		int i = 0;

		assertEquals(1, errors.size());

		ErrorDetail error = errors.get(i++);
		assertEquals("Second Page", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_SIZE, error.getErrorCode());

		openDesign(checkFileName2);
		errors = design.getErrorList();
		assertEquals(1, errors.size());
		error = errors.get(0);
		assertEquals("Third Page", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_MARGINS, error.getErrorCode());

		openDesign(checkFileName3);
		errors = design.getErrorList();
		assertEquals(1, errors.size());
		error = errors.get(0);
		assertEquals("Forth Page", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_MARGINS, error.getErrorCode());

		openDesign(checkFileName4);
		errors = design.getErrorList();
		assertEquals(1, errors.size());
		error = errors.get(0);
		assertEquals("Fifth Page", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_MULTI_COLUMN, error.getErrorCode());

		openDesign(checkFileName5);
		errors = design.getErrorList();
		assertEquals(1, errors.size());
		error = errors.get(0);
		assertEquals("Sixth Page", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_PAGE_SIZE, error.getErrorCode());

		openDesign(checkFileName6);

		errors = design.getErrorList();
		assertEquals(1, errors.size());
		error = errors.get(0);
		assertEquals("My Page", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT, error.getErrorCode());
	}

}