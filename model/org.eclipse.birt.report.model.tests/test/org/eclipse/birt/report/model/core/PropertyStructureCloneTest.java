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

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test for PropertyStructure clone facility.
 * 
 */

public class PropertyStructureCloneTest extends BaseTestCase {

	private final static String INPUT_FILE = "PropertyStructureCloneTest.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE = "PropertyStructureCloneTest_golden.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE1 = "PropertyStructureCloneTest1_golden.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(INPUT_FILE);
	}

	/**
	 * Copy action from Image1 to Image2, then change the type of "action1", write
	 * them back, ensure that change to image1 won't affect image2.
	 * 
	 * @throws Exception
	 */

	public void testCopy() throws Exception {
		ImageHandle image1 = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$

		ActionHandle actionHandle = image1.getActionHandle();
		Action action = (Action) actionHandle.getStructure();
		assertNotNull(action);

		ImageHandle image2 = (ImageHandle) designHandle.findElement("Image2"); //$NON-NLS-1$

		// copy the action and set it on image2.

		image2.setAction((Action) action.copy());

		// change the original action.

		actionHandle.setURI("www.sina.com.cn\\abc.jsp"); //$NON-NLS-1$
		actionHandle.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);

		save();
		// compare with golden file to ensure that change to the
		// original action won't affect the copy.
		assertTrue(compareFile(GOLDEN_FILE));
	}

	/**
	 * Copy action from image1 to image2. Change a list value of action in image1,
	 * ensure that it won't affect action on image2.
	 * 
	 * @throws Exception
	 */

	public void testCopy2() throws Exception {
		ImageHandle image1 = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$

		ActionHandle actionHandle = image1.getActionHandle();
		Action action = (Action) actionHandle.getStructure();
		assertNotNull(action);

		ImageHandle image2 = (ImageHandle) designHandle.findElement("Image2"); //$NON-NLS-1$
		image2.setAction((Action) action.copy());

		MemberHandle paramsHandle = actionHandle.getParamBindings();
		ParamBindingHandle param1 = (ParamBindingHandle) paramsHandle.getAt(0);

		param1.setExpression("new Expression"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(GOLDEN_FILE1));
	}
}