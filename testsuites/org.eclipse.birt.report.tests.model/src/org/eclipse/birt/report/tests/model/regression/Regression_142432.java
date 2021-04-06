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

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * MasterPage from library can't be restored to modify
 * </p>
 * Test description:
 * <p>
 * Extends a lib.masterpage without modification, can't restore. If any property
 * change, restore is enabled
 * </p>
 */

public class Regression_142432 extends BaseTestCase {

	private String filename = "Regression_142432.xml"; //$NON-NLS-1$
	private String libraryname = "Regression_142432_lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file

		copyInputToFile(INPUT_FOLDER + "/" + filename);
		copyInputToFile(INPUT_FOLDER + "/" + libraryname);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_142432() throws DesignFileException, SemanticException {
		openLibrary(libraryname, true);
		MasterPageHandle masterpage = libraryHandle.findMasterPage("NewSimpleMasterPage"); //$NON-NLS-1$

		openDesign(filename);
		designHandle.includeLibrary(libraryname, "Lib"); //$NON-NLS-1$
		MasterPageHandle mp = (MasterPageHandle) designHandle.getElementFactory().newElementFrom(masterpage, "mp"); //$NON-NLS-1$
		designHandle.getMasterPages().add(mp);

		assertFalse(mp.hasLocalProperties());
		mp.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE);
		assertTrue(mp.hasLocalProperties());

		// check group element handle.

		List pages = new ArrayList();
		pages.add(mp);

		GroupElementHandle group = new SimpleGroupElementHandle(designHandle, pages);
		assertTrue(group.hasLocalPropertiesForExtendedElements());
	}

}
