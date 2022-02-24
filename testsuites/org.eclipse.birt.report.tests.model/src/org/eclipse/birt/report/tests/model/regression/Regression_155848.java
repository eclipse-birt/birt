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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Description:</b>
 * <p>
 * Can't preview joint dataset which extended from library in HTML/PDF
 * <p>
 * <b>Steps to reproduce:</b>
 * <p>
 * Use attached design file and library, preview the design file in Web Viewer,
 * it works well, but when preview in HTML and PDF, ScriptEvaluationError shows
 * up
 * <p>
 * <b>Test Description:</b>
 * <p>
 * The solution is do not save library namespace to the extended joint data
 * sets. Test result set name don't have library name prefix
 *
 *
 */
public class Regression_155848 extends BaseTestCase

{
	private String filename = "Regression_155848.xml"; //$NON-NLS-1$

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws ExtendsException
	 */
	public void test_regression_155848() throws DesignFileException, ExtendsException {
		openDesign(filename);
		libraryHandle = designHandle.getLibrary("lib"); //$NON-NLS-1$
		JointDataSetHandle jointds = libraryHandle.findJointDataSet("jointds"); //$NON-NLS-1$

		ElementFactory factory = designHandle.getElementFactory();
		JointDataSetHandle dset = (JointDataSetHandle) factory.newElementFrom(jointds, "dset"); //$NON-NLS-1$

	}

}
