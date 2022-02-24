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
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Add a parameter group, error "the element ParameterGroup is not supported
 * yet" design
 * </p>
 * Test description:
 * <p>
 * Support paramter group
 * </p>
 */

public class Regression_78837 extends BaseTestCase {

	private String INPUT = "Regression_78837.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws ContentException
	 * @throws NameException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
		// copyResource_INPUT( INPUT2, INPUT2 );
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_78837() throws DesignFileException, ContentException, NameException {
		openDesign(INPUT);
		ElementFactory factory = designHandle.getElementFactory();
		ParameterGroupHandle group = factory.newParameterGroup("group"); //$NON-NLS-1$
		ScalarParameterHandle param = factory.newScalarParameter("p1"); //$NON-NLS-1$

		designHandle.getParameters().add(group);
		group.getParameters().add(param);

	}
}
