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

import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Drag a cascading parameter from library explorer into outline view, error
 * message pops up.
 * </p>
 * Test description:
 * <p>
 * Extend a cascading parameter group, no error
 * </p>
 */
public class Regression_120292 extends BaseTestCase {

	private String filename = "Regression_120292.xml"; //$NON-NLS-1$
	private String libraryname = "Regression_120292_Lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);
		copyResource_INPUT(libraryname, libraryname);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_120292() throws DesignFileException, SemanticException {
		openDesign(filename);
		designHandle.includeLibrary(libraryname, "Lib"); //$NON-NLS-1$

		libraryHandle = designHandle.getLibrary("Lib"); //$NON-NLS-1$
		CascadingParameterGroupHandle paramGroup = libraryHandle.findCascadingParameterGroup("ParamGroup"); //$NON-NLS-1$
		CascadingParameterGroupHandle extendGroup = (CascadingParameterGroupHandle) designHandle.getElementFactory()
				.newElementFrom(paramGroup, paramGroup.getName());
		designHandle.getParameters().add(extendGroup);
		assertEquals("ParamGroup", extendGroup.getName()); //$NON-NLS-1$
		assertEquals("p1", extendGroup.getParameters().get(0).getName()); //$NON-NLS-1$
		assertEquals("p2", extendGroup.getParameters().get(1).getName()); //$NON-NLS-1$
	}
}
