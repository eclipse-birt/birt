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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test ReportElement Handle for reading/writing property masks.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>testPropertyMask</td>
 * <td>Gets the valid property mask.</td>
 * <td>The value is gotten correctly.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Sets the valid property mask.</td>
 * <td>The output file matches the golden file.</td>
 * </tr>
 *
 * </table>
 *
 */

public class ReportElementParseTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests property masks of an element. Following methods have been tested:
	 *
	 * <ul>
	 * <li>{@link ReportElementHandle#propertyMaskIterator()}
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testPropertyMask() throws Exception {
		openDesign("ReportElementParseTest.xml"); //$NON-NLS-1$

		LabelHandle handle = (LabelHandle) designHandle.findElement("bodyLabel1"); //$NON-NLS-1$

		// get masks from the design file.

		int count = 0;
		for (Iterator iterator = handle.propertyMaskIterator(); iterator.hasNext(); iterator.next()) {
			count++;
		}

		assertEquals(5, count);

		PropertyHandle propHandle = handle.getPropertyHandle(DesignElement.PROPERTY_MASKS_PROP);
		StructureHandle structHandle = propHandle.getAt(0);
		MemberHandle memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(PropertyMask.NAME_MEMBER);
		assertEquals(Label.TEXT_PROP, memberHandle.getStringValue());

		structHandle = propHandle.getAt(2);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(PropertyMask.NAME_MEMBER);
		assertEquals(Label.TEXT_ID_PROP, memberHandle.getStringValue());

		structHandle = propHandle.getAt(3);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(PropertyMask.NAME_MEMBER);
		assertNull(memberHandle.getStringValue());

		structHandle = propHandle.getAt(4);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(PropertyMask.NAME_MEMBER);
		assertNull(memberHandle.getStringValue());

		structHandle = propHandle.getAt(0);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		memberHandle.setValue(DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE, memberHandle.getStringValue());

		structHandle = propHandle.getAt(2);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		memberHandle.setValue(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK, memberHandle.getStringValue());

		structHandle = propHandle.getAt(3);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		try {
			memberHandle.setValue("not mask"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}
		propHandle.removeItem(3);

		PropertyMask mask = new PropertyMask();
		mask.setProperty((PropertyDefn) structHandle.getDefn().getMember(PropertyMask.NAME_MEMBER),
				ReportItem.HEIGHT_PROP);
		mask.setProperty((PropertyDefn) structHandle.getDefn().getMember(PropertyMask.MASK_MEMBER),
				DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK);

		propHandle.addItem(mask);

		// tests a label in components.

		handle = (LabelHandle) designHandle.findElement("base"); //$NON-NLS-1$

		propHandle = handle.getPropertyHandle(DesignElement.PROPERTY_MASKS_PROP);

		mask = new PropertyMask();
		mask.setProperty((PropertyDefn) structHandle.getDefn().getMember(PropertyMask.NAME_MEMBER),
				ReportItem.HEIGHT_PROP);
		mask.setProperty((PropertyDefn) structHandle.getDefn().getMember(PropertyMask.MASK_MEMBER),
				DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK);
		propHandle.addItem(mask);

		save();
		assertTrue(compareFile("ReportElementParseTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test semantic check for proerpty mask.
	 *
	 * @throws DesignFileException if syntax error in design file.
	 */

	public void testSemanticCheck() throws DesignFileException {
		openDesign("ReportElementParseTest_1.xml"); //$NON-NLS-1$

		List list = design.getErrorList();

		assertEquals(2, list.size());

		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				((ErrorDetail) list.get(0)).getErrorCode());
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_PROPERTY_NAME, ((ErrorDetail) list.get(1)).getErrorCode());
	}
}
