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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnBandData;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>Open the attached file
 * <li>Choose the column[2] which contains a label , copy and paste to column[3]
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * It will be pasted to column[3]
 * <p>
 * <b>Actual result:</b>
 * <p>
 * It is pasted to column[4]
 * </p>
 * Test description:
 * <p>
 * Open the attached design, copy column from 3 to 4, make sure that column 4 is
 * correct and column 5 is not affected.
 * </p>
 */
public class Regression_135265 extends BaseTestCase {

	private final static String INPUT = "regression_135265.xml"; //$NON-NLS-1$

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
	public void test_regression_135265() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		TableHandle table = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$

		ColumnBandData columnBand = table.copyColumn(3);
		table.pasteColumn(columnBand, 4, true);

		// make sure the column copied correctly from column 3 to column 4

		// get the corresponding row.

		RowHandle row = (RowHandle) ((TableGroupHandle) table.getGroups().get(0)).getHeader().get(1);

		DataItemHandle data = (DataItemHandle) ((CellHandle) row.getCells().get(1)).getContent().get(0);
		DataItemHandle copiedData = (DataItemHandle) ((CellHandle) row.getCells().get(2)).getContent().get(0);

		assertNotNull(data);
		assertNotNull(copiedData);

		// check that the cell in column 5 is not affected.

		assertEquals(0, ((CellHandle) row.getCells().get(3)).getContent().getCount());
	}
}
