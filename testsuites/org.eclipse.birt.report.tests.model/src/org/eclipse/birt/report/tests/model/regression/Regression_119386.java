/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Copy/paste a cascading parameter group will cause error
 * </p>
 * Test description:
 * <p>
 * No error when copy/paste a cascading parameter group
 * </p>
 */

public class Regression_119386 extends BaseTestCase {

	private String filename = "Regression_119386.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	/**
	 * @throws DesignFileException
	 * @throws ContentException
	 * @throws NameException
	 */

	public void test_regression_119386() throws DesignFileException, ContentException, NameException {
		openDesign(filename);

		CascadingParameterGroupHandle paramsgroup = designHandle.findCascadingParameterGroup("ParameterGroup"); //$NON-NLS-1$
		IDesignElement paramsgroup1 = paramsgroup.copy();

		// Can't add a duplicated name group
		try {
			designHandle.getParameters().paste(paramsgroup1);
			fail();
		} catch (NameException e) {
			assertNotNull(e);
		}

		designHandle.rename(paramsgroup1.getHandle(designHandle.getModule()));
		designHandle.getParameters().paste(paramsgroup1);
	}
}
