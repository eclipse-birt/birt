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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Add toc property to group automatically
 * </p>
 * Test description:
 * <p>
 * Use group key as the automated generated toc
 * </p>
 */

public class Regression_119320 extends BaseTestCase {

	private String filename = "Regression_119320.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyInputToFile(INPUT_FOLDER + "/" + filename);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_119320() throws DesignFileException, SemanticException {
		openDesign(filename);

		// Use group key as the default toc expression

		TableGroupHandle group = designHandle.getElementFactory().newTableGroup();
		group.setKeyExpr("row['a']"); //$NON-NLS-1$
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		table.getGroups().add(group);

		assertEquals("row['a']", group.getTocExpression()); //$NON-NLS-1$

		// Change toc expression and group key

		group.setTocExpression("row['c']"); //$NON-NLS-1$
		group.setKeyExpr("row['b']"); //$NON-NLS-1$
		assertEquals("row['c']", group.getTocExpression()); //$NON-NLS-1$

	}
}
