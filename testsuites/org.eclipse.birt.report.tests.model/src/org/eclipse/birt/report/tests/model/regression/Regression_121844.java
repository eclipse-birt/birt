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

import java.io.IOException;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description: Change data set which is binded to a table extended from a
 * library, label and image in the table cell are not displayed.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Use a library in a report.
 * <li>Extend a table from a library.
 * <li>Edit the table data binding. Change its data set.
 * <li>Preview.
 * <li>The label and image in table cell are not displayed.
 * </ol>
 * <p>
 * Test description:
 * <p>
 * Ensure that model support changing data set of extended table, and model
 * support changing of binding columns.
 * </p>
 */
public class Regression_121844 extends BaseTestCase {

	private final static String INPUT = "regression_121844.xml"; //$NON-NLS-1$
	private final static String LIB = "regression_121844_lib.xml"; //$NON-NLS-1$
	private final static String OUTPUT = "regression_121844.out"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyResource_INPUT(INPUT, INPUT);

		copyResource_INPUT(LIB, LIB);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */

	public void test_regression_121844() throws DesignFileException, SemanticException, IOException {
		openDesign(INPUT);

		TableHandle table = (TableHandle) designHandle.findElement("NewTable"); //$NON-NLS-1$
		assertEquals("regression_121844_lib.Data Set", table.getDataSet().getQualifiedName()); //$NON-NLS-1$

		DataSetHandle localDSet = designHandle.findDataSet("Data Set1"); //$NON-NLS-1$

		// bind table to a local data set.

		table.setDataSet(localDSet);

		ComputedColumn col1 = StructureFactory.createComputedColumn();
		col1.setName("EMPLOYEENUMBER"); //$NON-NLS-1$
		col1.setExpression("dataSetRow[\"EMPLOYEENUMBER\"]"); //$NON-NLS-1$

		ComputedColumn col2 = StructureFactory.createComputedColumn();
		col2.setName("LASTNAME");//$NON-NLS-1$
		col2.setExpression("dataSetRow[\"LASTNAME\"]");//$NON-NLS-1$

		table.addColumnBinding(col1, true);
		table.addColumnBinding(col2, true);

		saveAs(OUTPUT);
	}
}
