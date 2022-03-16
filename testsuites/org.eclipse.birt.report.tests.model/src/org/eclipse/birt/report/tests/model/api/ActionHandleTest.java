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

package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestCases for ActionHandle class. ActionHandle should be got from the
 * specific ElementHandle that contains an Action.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 *
 * <tr>
 * <td>{@link #testTargetFileType()}</td>
 * <td>Test targetbookmarktype and targetfiletype for Action.</td>
 * <td>Get specified target type.</td>
 * </tr>
 *
 * </table>
 *
 */
public class ActionHandleTest extends BaseTestCase {

	ActionHandle actionHandle = null;

	public ActionHandleTest(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(ActionHandleTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * Test targetbookmarktype and targetfiletype for Action.
	 *
	 * @throws Exception
	 */
	public void testTargetFileType() throws Exception {
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		LibraryHandle library = sessionHandle.createLibrary();
		ElementFactory factory = new ElementFactory(library.getModule());
		ImageHandle image1 = factory.newImage("image1");
		ImageHandle image2 = factory.newImage("image2");
		LabelHandle label1 = factory.newLabel("label1");
		LabelHandle label2 = factory.newLabel("label2");
		library.getComponents().add(image1);
		library.getComponents().add(image2);
		library.getComponents().add(label1);
		library.getComponents().add(label2);

		// TargetBookMark only support for bookmark link and drill-through link
		Action action = StructureFactory.createAction();
		image1.setAction(action);
		image1.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
		image1.getActionHandle().setTargetBookmarkType(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK);
		assertNull(image1.getActionHandle().getTargetBookmarkType());

		action = StructureFactory.createAction();
		image2.setAction(action);
		image2.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK);
		image2.getActionHandle().setTargetBookmarkType(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC);
		assertEquals(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC, image2.getActionHandle().getTargetBookmarkType());

		// TargetFileType only support for URI link and drill-through link
		action = StructureFactory.createAction();
		label1.setAction(action);
		label1.getActionHandle().setTargetFileType(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN);
		assertNotNull(label1.getActionHandle().getTargetFileType());
		image1.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
		assertEquals(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN,
				label1.getActionHandle().getTargetFileType());
		library.getCommandStack().undo();
		assertNull(label1.getActionHandle().getTargetFileType());
		library.getCommandStack().redo();
		assertEquals(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN,
				label1.getActionHandle().getTargetFileType());

		action = StructureFactory.createAction();
		label2.setAction(action);
		label2.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH);
		label2.getActionHandle().setTargetFileType(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT);
		assertEquals(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT,
				label2.getActionHandle().getTargetFileType());
		label2.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK);
		assertNull(label2.getActionHandle().getTargetFileType());

	}

	public void test_HandleStructure() throws Exception {

		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		LibraryHandle library = sessionHandle.createLibrary();
		ElementFactory factory = new ElementFactory(library.getModule());
		ImageHandle image1 = factory.newImage("image1");
		LabelHandle label1 = factory.newLabel("label1");
		library.getComponents().add(image1);
		library.getComponents().add(label1);
		Action action = StructureFactory.createAction();
		image1.setAction(action);
		image1.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
		image1.getActionHandle().setTargetBookmarkType(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK);
		label1.setAction(action);
		label1.getActionHandle().setTargetFileType(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN);
		image1.getActionHandle().setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);

	}

}
