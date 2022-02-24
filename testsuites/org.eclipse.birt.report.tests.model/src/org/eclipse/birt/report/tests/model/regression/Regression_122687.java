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
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * If I create a table and set the background color to blue in my library and
 * then drag the table from the library to the report the background color does
 * not go with it.
 * </p>
 * Test description:
 * <p>
 * parent table background color is "aqua", extends from parent table and ensure
 * that child table has the color.
 * </p>
 */
public class Regression_122687 extends BaseTestCase {

	private final static String REPORT = "regression_122687.xml"; //$NON-NLS-1$
	private final static String LibraryName = "regression_122687_lib.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(REPORT, REPORT);
		copyResource_INPUT(LibraryName, LibraryName);
	}

	/**
	 * @throws SemanticException
	 * @throws DesignFileException
	 */
	public void test_regression_122687() throws SemanticException, DesignFileException {
		openDesign(REPORT);

		LibraryHandle lib = designHandle.getLibrary("regression_122687_lib"); //$NON-NLS-1$
		TableHandle parent = (TableHandle) lib.findElement("table1"); //$NON-NLS-1$
		assertNotNull(parent);
		// parent table background color is "aqua"
		System.out.println(StyleHandle.BACKGROUND_COLOR_PROP);
		// above printout is "backgroundColor"

		System.out.println(parent.getStringProperty(StyleHandle.BACKGROUND_COLOR_PROP));
		// nothing returned from parent.getStringProperty(...)

		assertEquals("aqua", parent //$NON-NLS-1$
				.getStringProperty(StyleHandle.BACKGROUND_COLOR_PROP));
		// assertFalse("aqua".equals(parent.getStringProperty(
		// StyleHandle.BACKGROUND_COLOR_PROP )));

		// extends from parent table.

		ElementFactory factory = designHandle.getElementFactory();
		TableHandle child = (TableHandle) factory.newElementFrom(parent, "childTable"); //$NON-NLS-1$

		designHandle.getBody().add(child);

		// ensure that child table has the color.

		TableHandle childTable = (TableHandle) designHandle.findElement("childTable"); //$NON-NLS-1$

		assertEquals("aqua", childTable //$NON-NLS-1$
				.getStringProperty(StyleHandle.BACKGROUND_COLOR_PROP));

	}
}
