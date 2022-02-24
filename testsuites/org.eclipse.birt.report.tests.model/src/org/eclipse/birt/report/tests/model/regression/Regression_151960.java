/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ColumnBandData;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b> Library element popup menu display is wrong.
 * <p>
 * <ol>
 * <li>Creat a libray, add a table to this libray.
 * <li>public this library.
 * <li>Creat a report design.
 * <li>Open the library explore, drag the table to the layout editor.
 * <li>Select the table column, copy the selection column.
 * <li>Select other column, clik the right button of the mouse to popup the menu
 * .
 * </ol>
 * <p>
 * <b>The result:</b>
 * <p>
 * Paste action and insert copied column action is highlight. Insert Copied
 * Action: we call:
 * 
 * <pre>
 *        TableHandle.canInsertAndPasteColumn( ColumnBandData data, int
 *        columnIndex)
 * </pre>
 * 
 * to check whether the copied column can be inserted and pasted. Paste Action:
 * we call
 * 
 * <pre>
 *        TableHandle.canPasteColumn( ColumnBandData data, int
 *        columnIndex,boolean inForce )
 * </pre>
 * 
 * to check whether the copied column can be pasted. These two functions both
 * return true, so I think the two functions have errors,
 * <p>
 * <b>Test description:</b>
 * <p>
 * Make sure that we can not change the column structure of the table copied
 * from library. The two check method should return false.
 * <p>
 */
public class Regression_151960 extends BaseTestCase {

	private final static String REPORT = "regression_151960.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( REPORT , REPORT );
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_151960() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		TableHandle childTable = (TableHandle) designHandle.findElement("NewTable"); //$NON-NLS-1$

		ColumnBandData columnData = childTable.copyColumn(2);
		assertFalse(childTable.canPasteColumn(columnData, 3, true));
		assertFalse(childTable.canInsertAndPasteColumn(columnData, 3));

	}
}
