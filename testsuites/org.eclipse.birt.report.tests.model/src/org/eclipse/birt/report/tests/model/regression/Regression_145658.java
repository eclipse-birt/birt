/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b> Back compatibility problem.
 * <p>
 * If report table extends a library table, the table group will be renamed to
 * "NewTableGroup1". While, actually group element should get its name from
 * virtual parent first.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Make sure that extended table group will get its name from its virtual parent
 * first.
 * <p>
 */
public class Regression_145658 extends BaseTestCase {

	private final static String REPORT = "regression_145658.xml"; //$NON-NLS-1$
	private final static String LIBRARY = "regression_145658_lib.xml";

	/**
	 * @throws DesignFileException
	 * @throws ExtendsException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_145658() throws DesignFileException, ExtendsException {
		openDesign(REPORT);
		LibraryHandle lib = designHandle.getLibrary("regression_145658_lib"); //$NON-NLS-1$
		TableHandle table = (TableHandle) lib.findElement("NewTable"); //$NON-NLS-1$

		ElementFactory factory = designHandle.getElementFactory();
		TableHandle newTable = (TableHandle) factory.newElementFrom(table, null);

		TableGroupHandle group = (TableGroupHandle) newTable.getGroups().get(0);
		assertEquals("NewTableGroup1", group.getName()); //$NON-NLS-1$

	}
}
