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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Add a group on a table, Set group header's background color, undo twice will
 * cause NPE
 * </p>
 * Test description:
 * <p>
 * undo twice, no error
 * </p>
 */

public class Regression_130278 extends BaseTestCase {

	private final static String INPUT = "Reg_130278.xml"; //$NON-NLS-1$
	private ElementFactory factory = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	public void test_regression_130278() throws SemanticException, DesignFileException {
		openDesign(INPUT);
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$

		factory = new ElementFactory(designHandle.getModule());
		TableGroupHandle group = factory.newTableGroup();
		RowHandle header = factory.newTableRow();
		group.getHeader().add(header);

		table.getGroups().add(group);
		header.setProperty(Style.BACKGROUND_COLOR_PROP, "red"); //$NON-NLS-1$

		designHandle.getCommandStack().undo();
		assertEquals(null, header.getStringProperty(Style.BACKGROUND_COLOR_PROP));

		designHandle.getCommandStack().undo();

		assertEquals(0, table.getGroups().getCount());
	}

}
