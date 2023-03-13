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

import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * In report preview page, the datasource/dataset property binding lists cannot
 * be retrieved. The dataset binding can not work correctly in layout preview
 * because it's value cannot get from the binding list. But in the data set edit
 * page, the binding value can be got.
 * </p>
 * Test description:
 * <p>
 * The bug is caused by Clone logic. In the clone logic, all elements ID will be
 * cleared and reassigne to a new number. But for the property binding case,
 * that will not work after the clone operation. So, when user copy the report
 * design tree, just keep the ID with the ID map so that the proeprty binding
 * can work properly. Check element id with copy method.
 * </p>
 */

public class Regression_121352 extends BaseTestCase {

	private String filename = "Regression_121352.xml"; //$NON-NLS-1$
	private String outfile = "Regression_121352_out.xml"; //$NON-NLS-1$
	private String goldenfile = "Regression_121352_golden.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
//		removeResource( );

		// retrieve two input files from tests-model.jar file
		copyInputToFile(INPUT_FOLDER + "/" + filename);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + goldenfile);
	}

	/**
	 * @throws Exception
	 */
	public void test_regression_121352() throws Exception {
		openDesign(filename);
		List bindingList = designHandle.getListProperty(Module.PROPERTY_BINDINGS_PROP);
		assertEquals(2, bindingList.size());

		// test list and member values

		PropertyBinding binding = (PropertyBinding) bindingList.get(0);
		assertEquals("text", binding.getName()); //$NON-NLS-1$
		assertEquals(23, binding.getID().longValue());
		assertEquals("params[p1]", binding.getValue()); //$NON-NLS-1$

		binding = (PropertyBinding) bindingList.get(1);
		assertEquals("column", binding.getName()); //$NON-NLS-1$
		assertEquals(22, binding.getID().longValue());
		assertEquals("params[p2]", binding.getValue()); //$NON-NLS-1$

		// get the element based on the id and test getPropertyBinding method

		DesignElementHandle tempHandle = designHandle.getElementByID(23);
		assertNotNull(tempHandle);
		assertTrue(tempHandle instanceof LabelHandle);
		assertNotNull(tempHandle.getPropertyDefn("text")); //$NON-NLS-1$
		assertEquals("params[p1]", tempHandle.getPropertyBinding("text")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(bindingList.get(0), designHandle.getModule().findPropertyBinding(tempHandle.getElement(), "text")); //$NON-NLS-1$

		tempHandle = designHandle.getElementByID(22);
		assertNotNull(tempHandle);
		assertTrue(tempHandle instanceof CellHandle);
		assertNotNull(tempHandle.getPropertyDefn("column")); //$NON-NLS-1$
		assertEquals("params[p2]", tempHandle.getPropertyBinding("column")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(bindingList.get(1),
				designHandle.getModule().findPropertyBinding(tempHandle.getElement(), "column")); //$NON-NLS-1$

		ReportDesignHandle copyHandle;
		ReportDesign copy = (ReportDesign) designHandle.copy();
		assertNotNull(copy);
		copyHandle = copy.handle();
		assertNotNull(copyHandle);

		// makeOutputDir();
		// saveAs( copyHandle, outfile );
		// assertTrue( compareTextFile( goldenfile, outfile) );

		String TempFile = this.genOutputFile(outfile);
		System.out.println(TempFile);
		designHandle.saveAs(TempFile);
		assertTrue(compareTextFile(goldenfile, outfile));
	}
}
