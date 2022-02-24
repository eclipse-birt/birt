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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Provide way to check if user opens old design file
 * </p>
 * Test description:
 * <p>
 * Need conversion for old design file, no conversion for new design file
 * </p>
 */

public class Regression_137174 extends BaseTestCase {

	private String filename = "Regression_137174.xml"; //$NON-NLS-1$
	private String filename_lib = "Regression_137174_lib.xml"; //$NON-NLS-1$

	/**
	 * 
	 */

	protected void setUp() throws Exception {
		super.setUp();
		// removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + filename);
		copyInputToFile(INPUT_FOLDER + "/" + filename_lib);
	}

	protected void tearDown() {
		removeResource();
	}

	// Need further investigate
	public void test_regression_137174() {
//		List infos = ModuleUtil.checkVersion( getTempFolder( ) + "/" + INPUT_FOLDER
//				+ "/" + filename );
//		assertEquals( 1, infos.size( ) );
//
//		IVersionInfo versionInfo = (IVersionInfo) infos.get( 0 );
//		assertEquals( "1", versionInfo.getDesignFileVersion( ) ); //$NON-NLS-1$
//		assertNotNull( versionInfo.getLocalizedMessage( ) );
//
//		infos = ModuleUtil.checkVersion( getClassFolder( ) + "/" + INPUT_FOLDER + "/"
//				+ filename_lib ); //$NON-NLS-1$
//		assertEquals( 1, infos.size( ) );
	}

}
