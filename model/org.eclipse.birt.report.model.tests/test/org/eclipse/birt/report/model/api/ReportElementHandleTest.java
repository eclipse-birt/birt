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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Test ReportItemHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testPropertyMaskFromPropertyHandle()}</td>
 * <td>Sets and gets the valid property mask by the property handle.</td>
 * <td>The value is set correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets and gets the invalid value of the property mask.</td>
 * <td>Throws <code>PropertyValueException</code> with the error code
 * CHOICE_NOT_FOUND.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets and gets the mask on the invalid property.</td>
 * <td>The property mask is null.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testPropertyMaskOnProperty()}</td>
 * <td>Reads a local mask on the label..</td>
 * <td>The mask value matches with the input file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Reads a mask from a invalid property.</td>
 * <td>Return <code>null</code></td>.
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Reads a mask inherited from the parent and grandparent.</td>
 * <td>Mask values follows the inheritance mechanism.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a property mask and checks masks on three labels.</td>
 * <td>Mask values follows the inheritance mechanism.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a property value with the "locked" mask.</td>
 * <td>Throws <code>PropertyValueException</code> with the error code:
 * VALUE_LOCKED</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a property value with the "change" and "hide" mask.</td>
 * <td>Value is set correctly.</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td></td>
 * <td>Gets a property value with the "lock", "hide" and "change" masks.</td>
 * <td>Value is returned correctly.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class ReportElementHandleTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ReportElementHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests property masks of an element. Following methods have been tested:
	 * 
	 * <ul>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.ReportElementHandle#propertyMaskIterator()}
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testPropertyMaskFromPropertyHandle() throws Exception {
		List list = designHandle.getErrorList();

		assertEquals(2, list.size());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				((ErrorDetail) list.get(0)).getErrorCode());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				((ErrorDetail) list.get(0)).getErrorCode());

		LabelHandle handle = (LabelHandle) designHandle.findElement("bodyLabel1"); //$NON-NLS-1$

		// get masks from the design file.

		int count = 0;
		for (Iterator iterator = handle.propertyMaskIterator(); iterator.hasNext(); iterator.next())
			count++;
		assertEquals(5, count);

		PropertyHandle propHandle = handle.getPropertyHandle(DesignElement.PROPERTY_MASKS_PROP);
		StructureHandle structHandle = propHandle.getAt(0);
		MemberHandle memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(PropertyMask.NAME_MEMBER);
		assertEquals(Label.TEXT_PROP, memberHandle.getStringValue());

		structHandle = propHandle.getAt(1);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(PropertyMask.NAME_MEMBER);
		assertEquals(DesignElement.EXTENDS_PROP, memberHandle.getStringValue());

		structHandle = propHandle.getAt(4);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(PropertyMask.NAME_MEMBER);
		assertNull(memberHandle.getStringValue());

		structHandle = propHandle.getAt(0);
		memberHandle = structHandle.getMember(PropertyMask.MASK_MEMBER);
		memberHandle.setValue(DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE);
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE, memberHandle.getStringValue());

		// test on child

		handle = (LabelHandle) designHandle.findElement("child1"); //$NON-NLS-1$

		// get masks from the design file.

		count = 0;
		Iterator iterator = handle.propertyMaskIterator();
		for (; iterator.hasNext(); iterator.next())
			count++;
		assertEquals(5, count);
	}

	/**
	 * Tests property masks of an element. Following methods have been tested:
	 * 
	 * <ul>
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.ReportElementHandle#getPropertyMask(String)}
	 * <li>
	 * {@link org.eclipse.birt.report.model.api.ReportElementHandle#setPropertyMask(String, String)}
	 * </ul>
	 * 
	 * <p>
	 * Test cases:
	 * <ul>
	 * <li>Reads a local mask on the label.
	 * <li>Reads a mask inherited from the grandparent.
	 * <li>Reads a mask from a invalid property, return null.
	 * <li>Sets a property mask and checks masks on three labels.
	 * <li>Sets a property value with the "locked" mask.
	 * <li>Sets a property value with the "change" mask.
	 * <li>Sets a property value with the "hide" mask.
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testPropertyMaskOnProperty() throws Exception {
		LabelHandle bodyLabelHandle = (LabelHandle) designHandle.findElement("bodyLabel1"); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK, bodyLabelHandle.getPropertyMask(Label.TEXT_PROP));

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK,
				bodyLabelHandle.getPropertyMask(DesignElement.EXTENDS_PROP));

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE,
				bodyLabelHandle.getPropertyMask(Label.TEXT_ID_PROP));

		// gets this from its grandparent.

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK,
				bodyLabelHandle.getPropertyMask(ReportItem.HEIGHT_PROP));

		// gets a mask from a null property, return null.

		assertNull(bodyLabelHandle.getPropertyMask(TextItem.CONTENT_PROP));

		LabelHandle childLabelHandle = (LabelHandle) designHandle.findElement("child1"); //$NON-NLS-1$

		// gets all masks from its parent.

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE, childLabelHandle.getPropertyMask(Label.TEXT_PROP));

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE,
				childLabelHandle.getPropertyMask(DesignElement.EXTENDS_PROP));

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK,
				childLabelHandle.getPropertyMask(ReportItem.HEIGHT_PROP));

		// sets a mask to a label

		childLabelHandle.setPropertyMask(ReportItem.HEIGHT_PROP, DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE);

		// gets this from its parent now .

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE,
				bodyLabelHandle.getPropertyMask(ReportItem.HEIGHT_PROP));

		childLabelHandle.setPropertyMask(ReportItem.HEIGHT_PROP, null);

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK,
				bodyLabelHandle.getPropertyMask(ReportItem.HEIGHT_PROP));

		// sets a value for a locked property.

		try {
			childLabelHandle.setProperty(ReportItem.HEIGHT_PROP, "12pc"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_LOCKED, e.getErrorCode());
		}

		// sets a value for a changed property.

		bodyLabelHandle.setPropertyMask(ReportItem.HEIGHT_PROP, DesignChoiceConstants.PROPERTY_MASK_TYPE_CHANGE);
		bodyLabelHandle.setProperty(ReportItem.HEIGHT_PROP, "12pc"); //$NON-NLS-1$
		assertEquals("12pc", bodyLabelHandle //$NON-NLS-1$
				.getStringProperty(ReportItem.HEIGHT_PROP));

		bodyLabelHandle.setPropertyMask(ReportItem.HEIGHT_PROP, DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE);
		bodyLabelHandle.setProperty(ReportItem.HEIGHT_PROP, "2pc"); //$NON-NLS-1$
		assertEquals("2pc", bodyLabelHandle //$NON-NLS-1$
				.getStringProperty(ReportItem.HEIGHT_PROP));

		bodyLabelHandle.setPropertyMask(ReportItem.HEIGHT_PROP, DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK);
		assertEquals("2pc", bodyLabelHandle //$NON-NLS-1$
				.getStringProperty(ReportItem.HEIGHT_PROP));

		bodyLabelHandle.setPropertyMask(ReportItem.HEIGHT_PROP, DesignChoiceConstants.PROPERTY_MASK_TYPE_HIDE);

		// its grand-child changes its property mask, not affect itself.

		LabelHandle baseHandle = (LabelHandle) designHandle.findElement("base"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK, baseHandle.getPropertyMask(ReportItem.HEIGHT_PROP));

		assertEquals(DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK,
				childLabelHandle.getPropertyMask(ReportItem.HEIGHT_PROP));
	}

	/**
	 * Tests the property mask on method.
	 * 
	 * @throws SemanticException if any exception
	 */

	public void testPropertyMaskOnMethod() throws SemanticException {
		LabelHandle bodyLabelHandle = (LabelHandle) designHandle.findElement("bodyLabel1"); //$NON-NLS-1$

		LabelHandle childLabelHandle = (LabelHandle) designHandle.findElement("child1"); //$NON-NLS-1$

		assertEquals("hello, show me on create.", bodyLabelHandle //$NON-NLS-1$
				.getOnCreate());
		assertEquals("hello, show me on render.", bodyLabelHandle //$NON-NLS-1$
				.getOnRender());

		childLabelHandle.setOnCreate("new create script"); //$NON-NLS-1$

		try {
			childLabelHandle.setOnRender("new render script"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_LOCKED, e.getErrorCode());
		}
	}

	/**
	 * Tests whether property mask can be set on an element that is not report
	 * element.
	 * 
	 */

	public void testPropertyMaskOnListGroup() {
		ListHandle listHandle = (ListHandle) designHandle.findElement("first list"); //$NON-NLS-1$

		SlotHandle groupSlot = listHandle.getSlot(ListItem.GROUP_SLOT);
		ListGroupHandle group = (ListGroupHandle) groupSlot.get(0);

		try {
			group.setPropertyMask(GroupElement.INTERVAL_PROP, DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyNameException.DESIGN_EXCEPTION_PROPERTY_NAME_INVALID, e.getErrorCode());
			assertEquals("group1", //$NON-NLS-1$
					e.getElement().getLocalProperty(design, GroupElement.GROUP_NAME_PROP));
		}
	}

	/**
	 * Test isValidLayout method.
	 * 
	 * @throws Exception
	 */

	public void testIsValidLayout() throws Exception {
		openDesign("ReportElementHandleTest_isValidLayout.xml"); //$NON-NLS-1$
		GridHandle gridHandle = (GridHandle) designHandle.getBody().get(0);
		assertFalse(ModelUtil.isValidLayout(designHandle.getModule(), gridHandle.getElement()));
		// remove column property in grid
		gridHandle.getSlot(GridHandle.COLUMN_SLOT).drop(0);
		assertTrue(ModelUtil.isValidLayout(designHandle.getModule(), gridHandle.getElement()));
	}
}