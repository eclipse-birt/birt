/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Auto text cannot be inserted in library master page
 * </p>
 * Test description:
 * <p>
 * Add autotext to library master page header/footer
 * </p>
 */

public class Regression_141218 extends BaseTestCase {

	private String filename = "Regression_141218.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file

		copyInputToFile(INPUT_FOLDER + "/" + filename);
	}

	/**
	 * @throws DesignFileException
	 * @throws ContentException
	 * @throws NameException
	 */
	public void test_regression_141218() throws DesignFileException, ContentException, NameException {
		openLibrary(filename, true);
		SimpleMasterPageHandle masterpage = (SimpleMasterPageHandle) libraryHandle.findMasterPage("masterpage"); //$NON-NLS-1$
		AutoTextHandle autotext = libraryHandle.getElementFactory().newAutoText("text1"); //$NON-NLS-1$
		AutoTextHandle autotext2 = libraryHandle.getElementFactory().newAutoText("text2"); //$NON-NLS-1$

		masterpage.getPageHeader().add(autotext);
		masterpage.getPageFooter().add(autotext2);

		DesignElementHandle text1 = masterpage.getPageHeader().get(0);
		DesignElementHandle text2 = masterpage.getPageFooter().get(0);
		assertTrue(text1 instanceof AutoTextHandle);
		assertTrue(text2 instanceof AutoTextHandle);

		assertEquals("text1", text1.getName()); //$NON-NLS-1$
		assertEquals("text2", text2.getName()); //$NON-NLS-1$
	}
}
