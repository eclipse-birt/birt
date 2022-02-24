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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber6">
 * 
 * <tr>
 * <td width="33%"><b>Method </b></td>
 * <td width="33%"><b>Test Case </b></td>
 * <td width="34%"><b>Expected Result </b></td>
 * </tr>
 * 
 * <tr>
 * <td width="33%">{@link #testParser()}</td>
 * <td width="33%">Test all properties</td>
 * <td width="34%">the correct value returned.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%">{@link #testWriter()}</td>
 * <td width="33%">Set new value to properties and save it.</td>
 * <td width="34%">new value should be save into the output file, and output
 * file is same as golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticError()}</td>
 * <td>Test semantic errors with the design file input.</td>
 * <td>The errors are collected, that is the value expr can not be empty.</td>
 * </tr>
 * </table>
 * 
 */

public class TextDataItemParseTest extends BaseTestCase {

	String fileName = "TextDataItemParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "TextDataItemParseTest_golden.xml"; //$NON-NLS-1$
	String checkFileName = "TextDataItemParseTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test all properties.
	 * 
	 * @throws Exception if opening design file failed.
	 */
	public void testParser() throws Exception {
		openDesign(fileName);
		TextDataHandle dataHandle = (TextDataHandle) designHandle.findElement("Text Data"); //$NON-NLS-1$
		assertNotNull(dataHandle.getElement());
		assertEquals("value expr", dataHandle.getValueExpr()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_AUTO, dataHandle.getContentType());
		assertTrue(dataHandle.hasExpression());
		assertEquals("Div", dataHandle.getTagType()); //$NON-NLS-1$
		assertEquals("English", dataHandle.getLanguage()); //$NON-NLS-1$
		assertEquals("Alt Text", dataHandle.getAltTextExpression().getStringExpression()); //$NON-NLS-1$
		assertEquals(1, dataHandle.getOrder()); // $NON-NLS-1$

		// test default value of Role in Text
		TextDataHandle data2Handle = (TextDataHandle) designHandle.findElement("Text Data2"); //$NON-NLS-1$
		assertEquals("p", data2Handle.getTagType()); //$NON-NLS-1$
	}

	/**
	 * This test sets properties, writes the design file and compares it with golden
	 * file.
	 * 
	 * @throws Exception if opening or saving design file failed.
	 */
	public void testWriter() throws Exception {
		openDesign(fileName);

		TextDataHandle dataHandle = (TextDataHandle) designHandle.findElement("Text Data"); //$NON-NLS-1$
		assertNotNull(dataHandle.getElement());
		dataHandle.setValueExpr("new value expr"); //$NON-NLS-1$
		dataHandle.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF);
		dataHandle.setHasExpression(false);
		dataHandle.setTagType("Div"); //$NON-NLS-1$
		dataHandle.setLanguage("English"); //$NON-NLS-1$
		dataHandle.setAltTextExpression(new Expression("Alt Text", ExpressionType.CONSTANT)); //$NON-NLS-1$
		dataHandle.setOrder(1); // $NON-NLS-1$
		save();
		assertTrue(compareFile(goldenFileName));

	}

	/**
	 * Checks the semantic error of ReportDesign.
	 * 
	 * @throws Exception
	 */

	public void testSemanticError() throws Exception {
		openDesign(checkFileName);

		List<ErrorDetail> errors = design.getErrorList();

		assertEquals(1, errors.size());

		int i = 0;

		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, errors.get(i++).getErrorCode());
	}

}
