/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Change predefine style "table" to "list", no effect
 * </p>
 * Test description:
 * <p>
 * check if the style "list" will apply to the list automatically
 * </p>
 */

public class Regression_128356 extends BaseTestCase {

	private final static String INPUT = "Reg_128356.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 * @throws NameException
	 */

	public void test_regression_128356() throws DesignFileException, NameException {
		openDesign(INPUT);
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		ListHandle list = (ListHandle) designHandle.findElement("list"); //$NON-NLS-1$
		SharedStyleHandle style = designHandle.findStyle("table"); //$NON-NLS-1$

		assertEquals("#0000FF", table //$NON-NLS-1$
				.getStringProperty(Style.BACKGROUND_COLOR_PROP));

		style.setName("list"); //$NON-NLS-1$
		assertNull(table.getStringProperty(Style.BACKGROUND_COLOR_PROP));
		assertEquals("#0000FF", list //$NON-NLS-1$
				.getStringProperty(Style.BACKGROUND_COLOR_PROP));
	}

}
