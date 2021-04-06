/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupElementFactory;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * In-line item from library can't be restore
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library named L1.
 * <li>New a grid and add a lable in grid.
 * <li>New a report design and use the library.
 * <li>Drop the grid from library explorer into layout.
 * <li>Modify the properties on label
 * </ol>
 * <p>
 * <b>Actual result:</b>
 * <p>
 * The restore properties buttom of label is gray always.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test the logic on GroupElmentHandle::hasLocalPropertiesForExtendedElements( )
 * <p>
 */
public class Regression_146185 extends BaseTestCase {

	private final static String REPORT = "regression_146185.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_145698() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		GridHandle grid = (GridHandle) designHandle.findElement("grid1"); //$NON-NLS-1$
		assertNotNull(grid);
		LabelHandle label = (LabelHandle) grid.getCellContent(1, 1).get(0);

		List elements = new ArrayList();
		elements.add(label);

		GroupElementHandle groupElementHandle = GroupElementFactory.newGroupElement(designHandle, elements);

		assertFalse(groupElementHandle.hasLocalPropertiesForExtendedElements());

		// change the text, make sure that the local properties can be restored.

		label.setText("www"); //$NON-NLS-1$

		assertTrue(groupElementHandle.hasLocalPropertiesForExtendedElements());
	}
}
