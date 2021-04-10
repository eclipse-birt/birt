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

import org.eclipse.birt.report.model.api.ColumnBandData;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * NPE when copy and paste table column
 * </p>
 * Test description:
 * <p>
 * No error when copy and paste table column
 * </p>
 */

public class Regression_139415 extends BaseTestCase {

	private String filename = "Regression_139415.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_139415() throws DesignFileException, SemanticException {
		openDesign(filename);
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$

		ColumnHandle column = (ColumnHandle) table.getColumns().get(0);
		IDesignElement tocopy = column.copy();
		table.getColumns().paste(tocopy, 1);

		ColumnBandData colData = table.copyColumn(1);
		table.pasteColumn(colData, 2, true);
	}
}
