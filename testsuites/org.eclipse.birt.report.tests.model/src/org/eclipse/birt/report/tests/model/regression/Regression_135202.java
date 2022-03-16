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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description: When delete a dataset which is the resource of a joint dataset,
 * an warning message should be offered. GUI handle.clientsIterator() to
 * determine if the reference existing. Model don't provide the list in this
 * case.
 * </p>
 * Test description:
 * <p>
 * Joint DataSet "jointDS" use "ds1" and "ds2", make sure that clientsIterator()
 * should be provided for Joint-dataset to datasets references.
 * </p>
 */
public class Regression_135202 extends BaseTestCase {

	private final static String INPUT = "regression_135202.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 *
	 */

	public void test_regression_135202() throws DesignFileException, SemanticException {
		openDesign(INPUT);

		DataSetHandle jointDS = designHandle.findDataSet("jointDS"); //$NON-NLS-1$
		DataSetHandle ds1 = designHandle.findDataSet("ds1"); //$NON-NLS-1$
		DataSetHandle ds2 = designHandle.findDataSet("ds1"); //$NON-NLS-1$

		Iterator client1 = ds1.clientsIterator();
		Iterator client2 = ds2.clientsIterator();

		// ensure they have references

		assertTrue(client1.hasNext());
		assertTrue(client2.hasNext());

		// ensure client refers to joint ds.

		assertEquals(jointDS, client1.next());
		assertEquals(jointDS, client2.next());
	}
}
