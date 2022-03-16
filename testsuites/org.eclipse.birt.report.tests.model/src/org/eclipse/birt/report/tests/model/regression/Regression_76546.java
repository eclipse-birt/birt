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
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Error message for the duplicated group name is bad worded
 * </p>
 * Test description:
 * <p>
 * Check error message for the duplicated group name
 * </p>
 */

public class Regression_76546 extends BaseTestCase {

	private String filename = "Regression_76546.xml"; //$NON-NLS-1$
	private String error = "The name \"group\" duplicates an existing name. Please choose a different name."; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	/**
	 * @throws DesignFileException
	 * @throws NameException
	 * @throws ContentException
	 */
	public void test_regression_76546() throws DesignFileException, NameException, ContentException {
		openDesign(filename);
		TableGroupHandle group = designHandle.getElementFactory().newTableGroup();
		group.setName("group"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$

		try {
			table.getGroups().add(group);
			fail();
		} catch (NameException e) {
			assertNotNull(e);
			assertEquals(error, e.getMessage());
		}

	}
}
