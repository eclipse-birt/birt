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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * Parameters in cascading parameter group will be renamed when extending the
 * cascading parameter group
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library, add a cascading parameter group with two parameters
 * "Country" and "State"
 * <li>New a report, extends lib.cascadingparametergroup
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * The parameters in report are named "Country", "State"
 * <p>
 * <b>Actual result:</b>
 * <p>
 * The names of parameters in report are "NewParameter","NewParameter1"
 * </p>
 * Test description:
 * <p>
 * Follow the steps, extends a parameter group in the library, check the
 * parameter name inside the child parameter group
 * </p>
 */
public class Regression_122606 extends BaseTestCase {

	private final static String INPUT = "regression_122606.xml"; //$NON-NLS-1$

	private final static String LibraryName = "regression_122606_lib.xml";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);
		copyResource_INPUT(LibraryName, LibraryName);
	}

	/**
	 * @throws DesignFileException
	 * @throws ExtendsException
	 * @throws NameException
	 * @throws ContentException
	 */
	public void test_regression_122606() throws DesignFileException, ExtendsException, ContentException, NameException {
		openDesign(INPUT);

		LibraryHandle includeLib = designHandle.findLibrary(LibraryName); // $NON-NLS-1$
		ParameterGroupHandle parent = (ParameterGroupHandle) includeLib.getParameters().get(0);

		assertEquals("p1", parent.getParameters().get(0).getName()); //$NON-NLS-1$
		assertEquals("p2", parent.getParameters().get(1).getName()); //$NON-NLS-1$

		// extends from the parameter group in the library.

		ElementFactory factory = designHandle.getElementFactory();
		ParameterGroupHandle child = (ParameterGroupHandle) factory.newElementFrom(parent, parent.getName());
		designHandle.getParameters().add(child);

		// Check the parameter name inside the child parameter group

		ParameterGroupHandle childHandle = (ParameterGroupHandle) designHandle.getParameters().get(0);
		assertEquals(parent.getName(), childHandle.getName());

		ParameterHandle p1 = (ParameterHandle) childHandle.getParameters().get(0);
		ParameterHandle p2 = (ParameterHandle) childHandle.getParameters().get(1);

		assertEquals("p1", p1.getName()); //$NON-NLS-1$
		assertEquals("p2", p2.getName()); //$NON-NLS-1$

	}
}
