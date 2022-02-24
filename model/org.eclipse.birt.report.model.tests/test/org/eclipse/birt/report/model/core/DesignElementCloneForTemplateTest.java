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

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the cloneForTemplate feature of
 * <code>{@link org.eclipse.birt.report.model.core.DesignElement}</code>.
 * Cloning the original design element, references to the parent in library is
 * kept.
 */

public class DesignElementCloneForTemplateTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * Tests the extend relationship in a simple element.
	 * <ul>
	 * <li>Extends relationship is kept.
	 * </ul>
	 * 
	 * @throws Exception if any exception.
	 */

	public void testCloneForTemplateLabel() throws Exception {
		openDesign("DesignElementCloneForTemplate.xml"); //$NON-NLS-1$

		// The derived list is not cloned

		TemplateElementHandle templateLabelHandle = (TemplateElementHandle) designHandle.findElement("template label"); //$NON-NLS-1$
		assertNotNull(templateLabelHandle);

		String copiedExtendsName = ((DesignElement) templateLabelHandle.copyDefaultElement()).getExtendsName();

		assertEquals("Library_CloneForTemplate.NewLabel", copiedExtendsName); //$NON-NLS-1$

	}

	/**
	 * Tests the extend relationship in a complex element.
	 * <ul>
	 * <li>Extends relationship is kept for table.
	 * <li>Rows and cells inside keep their baseID.
	 * </ul>
	 * 
	 * @throws Exception if any exception.
	 */

	public void testCloneForTemplateTable() throws Exception {
		openDesign("DesignElementCloneForTemplate.xml"); //$NON-NLS-1$

		// The derived list is not cloned

		TemplateElementHandle templateLabelHandle = (TemplateElementHandle) designHandle.findElement("template table"); //$NON-NLS-1$
		assertNotNull(templateLabelHandle);

		TableItem copiedTable = (TableItem) templateLabelHandle.copyDefaultElement();
		String copiedExtendsName = copiedTable.getExtendsName();
		assertEquals("Library_CloneForTemplate.NewTable", copiedExtendsName); //$NON-NLS-1$

		// verify contents in the detail slot.

		ContainerSlot slot = copiedTable.getSlot(TableItem.DETAIL_SLOT);
		assertEquals(1, slot.getCount());

		TableRow row = (TableRow) slot.getContent(0);
		slot = row.getSlot(TableRow.CONTENT_SLOT);

		assertTrue(DesignElement.NO_BASE_ID != row.getBaseId());
		assertEquals(3, slot.getCount());

		// verify contents in the group header slot.

		TableGroup group = (TableGroup) copiedTable.getSlot(TableItem.GROUP_SLOT).getContent(0);
		slot = group.getSlot(TableGroup.HEADER_SLOT);
		assertEquals(1, slot.getCount());

		row = (TableRow) slot.getContent(0);
		slot = row.getSlot(TableRow.CONTENT_SLOT);

		assertTrue(DesignElement.NO_BASE_ID != row.getBaseId());
		assertEquals(3, slot.getCount());
	}
}
