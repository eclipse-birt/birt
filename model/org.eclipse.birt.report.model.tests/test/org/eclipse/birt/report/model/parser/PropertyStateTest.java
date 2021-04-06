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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the generic parser problems.
 */

public class PropertyStateTest extends BaseTestCase {

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests the element which has the property name "name" or "extends".
	 */

	public void testConflictPropertyName() {
		try {
			openDesign("PropertyStateTest.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			assertEquals(2, e.getErrorList().size());
			int i = 0;

			assertEquals(DesignParserException.DESIGN_EXCEPTION_INVALID_PROPERTY_SYNTAX,
					((ErrorDetail) e.getErrorList().get(i++)).getErrorCode());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_INVALID_PROPERTY_SYNTAX,
					((ErrorDetail) e.getErrorList().get(i++)).getErrorCode());
		}
	}

	/**
	 * Tests the empty string in element's property or structure's member.
	 * 
	 * @throws Exception
	 */

	public void testEmptyString() throws Exception {
		openDesign("PropertyStateTest_1.xml"); //$NON-NLS-1$

		// Tests the textual property value

		OdaDataSourceHandle dataSourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource"); //$NON-NLS-1$
		assertEquals(null, dataSourceHandle.getDriverName());

		dataSourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource1"); //$NON-NLS-1$
		assertEquals(null, dataSourceHandle.getDriverName());

		// Tests the member value

		// Iterator iter = dataSourceHandle.publicDriverPropertiesIterator( );
		// ExtendedPropertyHandle propertyHandle = (ExtendedPropertyHandle) iter
		// .next( );
		// assertNotNull( propertyHandle );
		// assertEquals( null, propertyHandle.getValue( ) );

		// Test the resource value

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		assertEquals(null, labelHandle.getHelpText());
		assertEquals(" ", labelHandle.getHelpTextKey()); //$NON-NLS-1$

		// Test the expression value

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("data1"); //$NON-NLS-1$
		assertNull(dataHandle.getResultSetExpression());
		dataHandle.setResultSetColumn("   a   "); //$NON-NLS-1$
		assertEquals("a", dataHandle.getResultSetColumn()); //$NON-NLS-1$

		// Test the image data value

		Iterator iter = designHandle.imagesIterator();
		EmbeddedImageHandle imageHandle = (EmbeddedImageHandle) iter.next();
		assertNotNull(imageHandle);
		assertEquals(null, imageHandle.getData());

		// Test the string value

		TextItemHandle textHandle = (TextItemHandle) designHandle.findElement("text1"); //$NON-NLS-1$
		assertEquals("   ", textHandle.getContent()); //$NON-NLS-1$
		textHandle.setContent("   a   "); //$NON-NLS-1$
		assertEquals("   a   ", textHandle.getContent()); //$NON-NLS-1$

		// Test the choice value

		assertEquals("html", textHandle.getContentType()); //$NON-NLS-1$
		textHandle.setContentType("   auto   "); //$NON-NLS-1$
		assertEquals("auto", textHandle.getContentType()); //$NON-NLS-1$

		// Test the HTML value

		assertEquals("html", designHandle.getStringProperty(ReportDesign.DESCRIPTION_PROP)); //$NON-NLS-1$
		designHandle.setStringProperty(ReportDesign.DESCRIPTION_PROP, "   <p> html </p>   "); //$NON-NLS-1$
		assertEquals("<p> html </p>", designHandle.getStringProperty(ReportDesign.DESCRIPTION_PROP)); //$NON-NLS-1$

		// Save it and compare with golden file

		save();
		assertTrue(compareFile("PropertyStateTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the structure has no name.
	 */

	public void testBlankStructureName() {
		try {
			openDesign("PropertyStateTest_2.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			List list = e.getErrorList();

			assertEquals(1, list.size());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED,
					((ErrorDetail) list.get(0)).getErrorCode());
		}
	}

	/**
	 * Tests the structure has wrong name.
	 */

	public void testWrongStructureName() {
		try {
			openDesign("PropertyStateTest_3.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			List list = e.getErrorList();

			assertEquals(1, list.size());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_INVALID_STRUCTURE_NAME,
					((ErrorDetail) list.get(0)).getErrorCode());
		}
	}

	/**
	 * Tests the structure has wrong name.
	 */

	public void testBlankMemberName() {
		try {
			openDesign("PropertyStateTest_4.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			List list = e.getErrorList();

			assertEquals(2, list.size());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED,
					((ErrorDetail) list.get(0)).getErrorCode());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED,
					((ErrorDetail) list.get(1)).getErrorCode());
		}
	}

	/**
	 * Tests the property list has no name.
	 */

	public void testBlankPropertyListName() {
		try {
			openDesign("PropertyStateTest_5.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			List list = e.getErrorList();

			assertEquals(1, list.size());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED,
					((ErrorDetail) list.get(0)).getErrorCode());

		}
	}

	/**
	 * Tests the wrong name defined in extended property tag.
	 * 
	 * @throws DesignFileException if any exception.
	 */

	public void testWrongNameInExtendedProperty() throws DesignFileException {
		openDesign("PropertyStateTest_6.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests the name of the property list is wrong.
	 * 
	 * @throws DesignFileException if the design file is invalid
	 */

	public void testWrongPropertyListName() throws DesignFileException {

		openDesign("PropertyStateTest_7.xml"); //$NON-NLS-1$

		List list = designHandle.getErrorList();

		assertEquals(2, list.size());
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE, ((ErrorDetail) list.get(0)).getErrorCode());

		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY,
				((ErrorDetail) list.get(1)).getErrorCode());
	}

	/**
	 * Tests the member name is wrong.
	 */

	public void testWrongMemberName() throws DesignFileException {
		openDesign("PropertyStateTest_8.xml"); //$NON-NLS-1$

		List list = designHandle.getErrorList();

		assertEquals(2, list.size());
		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY,
				((ErrorDetail) list.get(1)).getErrorCode());

	}

	/**
	 * Tests the property name in text property is wrong.
	 */

	public void testWrongPropertyNameInTextProperty() throws DesignFileException {
		openDesign("PropertyStateTest_9.xml"); //$NON-NLS-1$

		List list = designHandle.getErrorList();

		assertEquals(1, list.size());
		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY,
				((ErrorDetail) list.get(0)).getErrorCode());
	}

	/**
	 * Tests the resource key in text property is blank.
	 * 
	 * @throws DesignFileException if any exception.
	 */

	public void testBlankResourceKeyValueInTextProperty() throws DesignFileException {
		openDesign("PropertyStateTest_10.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests the user property with blank choices.
	 */

	public void testUserPropertyWithBlankChoices() {
		try {
			openDesign("PropertyStateTest_11.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			List list = e.getErrorList();

			assertEquals(2, list.size());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED,
					((ErrorDetail) list.get(0)).getErrorCode());
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_MISSING_CHOICES,
					((ErrorDetail) list.get(1)).getErrorCode());
		}
	}

	/**
	 * Tests the user property with wrong choices.
	 */

	public void testUserPropertyWithWrongChoices() {
		try {
			openDesign("PropertyStateTest_12.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			List list = e.getErrorList();

			assertEquals(2, list.size());
			assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY,
					((ErrorDetail) list.get(1)).getErrorCode());
			assertEquals(UserPropertyException.DESIGN_EXCEPTION_MISSING_CHOICES,
					((ErrorDetail) list.get(0)).getErrorCode());
		}
	}

	/**
	 * Tests the user property with value, but the value appears before the user
	 * property definition.
	 */

	public void testUserPropertyWithValue() throws DesignFileException {
		openDesign("PropertyStateTest_13.xml"); //$NON-NLS-1$

		List list = designHandle.getErrorList();

		assertEquals(1, list.size());
		assertEquals(DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY,
				((ErrorDetail) list.get(0)).getErrorCode());
	}

	/**
	 * Tests the parser recovers the choice not allowed exception.
	 * 
	 * @throws DesignFileException
	 */

	public void testChoiceNotAllowed() throws DesignFileException {
		// there is no such case now
	}

	/**
	 * Tests the parser can recover <code>DESIGN_EXCEPTION_NON_POSITIVE_VALUE</code>
	 * and <code>DESIGN_EXCEPTION_NEGATIVE_VALUE</code>. This applies to both
	 * element property values and structure member values.
	 * 
	 * @throws DesignFileException
	 */

	public void testValidator() throws DesignFileException {

		openDesign("PropertyStateTest_15.xml"); //$NON-NLS-1$
		List errors = designHandle.getErrorList();
		assertEquals(4, errors.size());

		ErrorDetail error = (ErrorDetail) errors.get(0);
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE, error.getErrorCode());
		assertEquals("style1", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(10, error.getLineNo());

		error = (ErrorDetail) errors.get(1);
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_NON_POSITIVE_VALUE, error.getErrorCode());
		assertEquals("style1", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(12, error.getLineNo());

		error = (ErrorDetail) errors.get(2);
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_NON_POSITIVE_VALUE, error.getErrorCode());
		assertEquals("parent", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(19, error.getLineNo());

		error = (ErrorDetail) errors.get(3);
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE, error.getErrorCode());
		assertEquals("form1", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(24, error.getLineNo());

		StyleHandle styleHandle = designHandle.findStyle("style1"); //$NON-NLS-1$
		Iterator iter = styleHandle.highlightRulesIterator();
		HighlightRuleHandle ruleHandle = (HighlightRuleHandle) iter.next();
		assertEquals("-2cm", ruleHandle.getBorderBottomWidth() //$NON-NLS-1$
				.getStringValue());
		assertEquals("0pt", ruleHandle.getFontSize().getStringValue()); //$NON-NLS-1$

		GraphicMasterPageHandle pageHandle = (GraphicMasterPageHandle) designHandle.findMasterPage("parent"); //$NON-NLS-1$
		assertEquals(0, pageHandle.getColumnCount());

		FreeFormHandle handle = (FreeFormHandle) designHandle.findElement("form1"); //$NON-NLS-1$
		assertEquals("-1in", handle.getWidth().getStringValue()); //$NON-NLS-1$
		assertEquals("-1in", handle.getStringProperty(ReportItem.WIDTH_PROP)); //$NON-NLS-1$

	}
}