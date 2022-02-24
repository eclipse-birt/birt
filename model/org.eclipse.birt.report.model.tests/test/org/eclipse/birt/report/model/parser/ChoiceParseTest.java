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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for Choice and extended choice parse.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testChoiceWithProperty()}</td>
 * <td width="33%" height="16">Sets/gets normal predefined choice property.</td>
 * <td>Sets/gets values correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Sets/gets baseType value for an extended choice
 * property.</td>
 * <td>Sets/gets values correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Sets an undefined choice for a choice set.</td>
 * <td>Throws a <code>PropertyValueException</code> with the code
 * <code>CHOICE_NOT_FOUND</code></td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Sets a invalid baseType choice for an extended
 * choice set.</td>
 * <td>Throws a <code>PropertyValueException</code> with the code
 * <code>INVALID_VALUE</code></td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16">{@link #testChoiceWithMember()}</td>
 * <td width="33%" height="16">Sets/gets normal and default predefined choice
 * property.</td>
 * <td>Sets/gets values correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Sets an undefined choice for a choice set.</td>
 * <td>Throws a <code>PropertyValueException</code> with the code
 * <code>CHOICE_NOT_FOUND</code></td>
 * </tr>
 * 
 * </table>
 * 
 */

public class ChoiceParseTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ChoiceParseTest.xml"); //$NON-NLS-1$
		assertNotNull(design);
	}

	/**
	 * Tests the choice and extended choice property.
	 * 
	 * <p>
	 * 
	 * Test cases are:
	 * <ul>
	 * <li>Sets/gets normal predefined choice property.
	 * <li>Sets/gets baseType value for an extended choice property.
	 * <li>Sets an undefined choice for a choice set.
	 * <li>sets a invalid baseType choice for an extended choice set.
	 * </ul>
	 * 
	 * @throws Exception
	 */
	public void testChoiceWithProperty() throws Exception {

		StyleElement myStyle = design.findStyle("MyStyle"); //$NON-NLS-1$
		assertNotNull(myStyle);

		StyleHandle styleHandle = (StyleHandle) myStyle.getHandle(design);

		// if the format of font.size is invalid, then do not use this style.

		StyleElement style1 = design.findStyle("style1"); //$NON-NLS-1$
		assertNotNull(style1);

		assertEquals(DesignChoiceConstants.FONT_SIZE_X_SMALL, styleHandle.getFontSize().getStringValue());

		styleHandle.getFontSize().setStringValue(DesignChoiceConstants.FONT_SIZE_XX_SMALL);

		// Gets the choice value out of values of ChoiceSet.

		assertEquals(DesignChoiceConstants.FONT_WEIGHT_NORMAL, styleHandle.getFontWeight());

		// please know that setProperty/getProperty must use name value instead
		// of interval values.

		styleHandle.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_400);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_400, styleHandle.getFontWeight());

		styleHandle.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_LIGHTER);

		// sets an undefined choice for a choice set.

		try {
			styleHandle.setFontWeight(new String("450")); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND);
		}

		// we sets this to 12pt.
		styleHandle = (StyleHandle) style1.getHandle(design);

		style1.getHandle(design).setProperty(Style.FONT_SIZE_PROP,
				new DimensionValue(12.0, DesignChoiceConstants.UNITS_MM));
		assertEquals("12mm", //$NON-NLS-1$
				style1.getHandle(design).getStringProperty(Style.FONT_SIZE_PROP).toString());

		// sets a invalid choice for a choice set.

		try {
			style1.getHandle(design).setProperty(Style.FONT_SIZE_PROP, new String("asdf")); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE);
		}

		style1.getHandle(design).setProperty(Style.FONT_SIZE_PROP,
				new DimensionValue(12.0, DesignChoiceConstants.UNITS_CM));
		assertEquals(style1.getProperty(design, Style.FONT_SIZE_PROP).toString(), "12cm"); //$NON-NLS-1$

		// tests a style in an element.

		Label label2 = (Label) design.findElement("label2"); //$NON-NLS-1$
		assertNotNull(label2);

		label2 = (Label) design.findElement("label2"); //$NON-NLS-1$
		assertNotNull(label2);
		assertEquals("12mm", //$NON-NLS-1$
				label2.getHandle(design).getStringProperty(Style.FONT_SIZE_PROP).toString());

		label2.getHandle(design).setProperty(Style.FONT_SIZE_PROP,
				new DimensionValue(12.0, DesignChoiceConstants.UNITS_IN));

		save();
		assertTrue(compareFile("ChoiceParseTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the choice and extended choice with the member.
	 * 
	 * <p>
	 * 
	 * Test cases are:
	 * <ul>
	 * <li>Sets/gets normal and default predefined choice property.
	 * <li>Sets an undefined choice for a choice set.
	 * </ul>
	 * 
	 * @throws Exception
	 */
	public void testChoiceWithMember() throws Exception {
		StyleElement myStyle = design.findStyle("MyStyle"); //$NON-NLS-1$
		assertNotNull(myStyle);

		PropertyHandle propHandle = myStyle.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);

		// get first maprule structure

		StructureHandle strHandle = propHandle.getAt(0);
		assertNotNull(strHandle);

		// get display member

		MemberHandle memberHandle = strHandle.getMember(StyleRule.OPERATOR_MEMBER);
		assertEquals(memberHandle.getStringValue(), DesignChoiceConstants.MAP_OPERATOR_EQ);

		// sets a valid operator

		memberHandle.setValue(DesignChoiceConstants.MAP_OPERATOR_TRUE);
		assertEquals(memberHandle.getStringValue(), DesignChoiceConstants.MAP_OPERATOR_TRUE);

		// invalid value in design file. Should be default value.

		strHandle = propHandle.getAt(1);
		assertNotNull(strHandle);

		memberHandle = strHandle.getMember(StyleRule.OPERATOR_MEMBER);
		assertEquals(memberHandle.getStringValue(), DesignChoiceConstants.MAP_OPERATOR_LIKE);

		// sets an invalid string operator for the maprule.

		try {
			memberHandle.setValue("nono"); //$NON-NLS-1$
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND);
		}

		// sets an invalid int operator for the maprule.

		try {
			memberHandle.setIntValue(12);
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE);
		}

		// if it is invalid, then it should be default value.

		assertEquals(memberHandle.getStringValue(), DesignChoiceConstants.MAP_OPERATOR_LIKE);
	}

	/**
	 * Test value of 'verticalAlign' property is valid or not. If value of
	 * 'verticalAlign' property is not one of 'top','bottom' or 'middle',
	 * 
	 * 
	 * @throws Exception
	 */

	public void testVerticalAlign() throws Exception {
		StyleElement myStyle = design.findStyle("MyStyle"); //$NON-NLS-1$
		assertNotNull(myStyle);

		String verticalAlign = (String) myStyle.getLocalProperty(design, IStyleModel.VERTICAL_ALIGN_PROP);
		assertEquals(DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE, verticalAlign);

		// not valid choice
		myStyle = design.findStyle("style1");//$NON-NLS-1$
		verticalAlign = (String) myStyle.getLocalProperty(design, IStyleModel.VERTICAL_ALIGN_PROP);
		assertEquals("baseline", verticalAlign); //$NON-NLS-1$

		TableHandle tableHandle = (TableHandle) designHandle.getBody().get(0);
		GroupHandle groupHandle = (GroupHandle) tableHandle.getGroups().get(0);
		RowHandle rowHandle = (RowHandle) groupHandle.getHeader().get(0);
		CellHandle cellHandle = (CellHandle) rowHandle.getCells().get(0);
		verticalAlign = (String) cellHandle.getElement().getLocalProperty(design, IStyleModel.VERTICAL_ALIGN_PROP);
		assertEquals("baseline", verticalAlign); //$NON-NLS-1$

	}
}
