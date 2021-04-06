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

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * No exception for two group element with the same names
 * </p>
 * Test description:
 * <p>
 * Add two groups with the same name to the table, NameException should be
 * thrown out
 * </p>
 */

public class Regression_73182 extends BaseTestCase {

	private final static String INPUT = "Regression_73182.xml"; //$NON-NLS-1$

	/**
	 * @throws Exception
	 */

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
		// copyResource_INPUT( INPUT2, INPUT2 );
	}

	public void test_regression_73182() throws Exception {
		openDesign(INPUT);

		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		ElementFactory factory = designHandle.getElementFactory();
		TableGroupHandle group1 = factory.newTableGroup();
		group1.setName("group1"); //$NON-NLS-1$
		TableGroupHandle group2 = factory.newTableGroup();
		table.getGroups().add(group1);
		table.getGroups().add(group2);
		try {
			group2.setName("group1"); //$NON-NLS-1$
			table.getGroups().add(group2);
			fail();
		} catch (NameException e) {
			assertNotNull(e);
		}
	}
}
