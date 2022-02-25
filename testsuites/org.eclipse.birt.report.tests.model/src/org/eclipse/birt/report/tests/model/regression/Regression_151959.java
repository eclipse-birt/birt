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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * inappropriate screen operation
 * <p>
 * <b>steps to reproduce:</b>
 * <ol>
 * <li>create a report
 * <li>add a grid item (3x3)
 * <li>switch to Outline view
 * <li>expand the grid item to see its rows and cells
 * <li>select the grid item, and drag it into any of its own cell
 * </ol>
 * <b>expected behavior:</b>
 * <p>
 * is it an invalid screen operation? if it is, it should be banned
 * <p>
 * <b>actual behavior:</b>
 * <p>
 * the grid item was disappeared surprisingly
 * <p>
 * <b>Test Description:</b>
 * <p>
 * CellHandle.canContain( int slotId, TableHandle content ), if the Cell is one
 * of the Table's, return false.
 */
public class Regression_151959 extends BaseTestCase {

	/**
	 * @throws Exception
	 */
	public void test_regression_151959() {
		createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		GridHandle grid = factory.newGridItem("grid", 3, 3); //$NON-NLS-1$
		CellHandle cell = grid.getCell(0, 0);

		assertFalse(cell.canContain(0, grid));
	}

}
