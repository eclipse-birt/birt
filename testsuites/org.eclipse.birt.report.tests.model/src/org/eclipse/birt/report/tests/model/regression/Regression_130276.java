/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Add a table with group in report, delete the table and undo will cause
 * SWTException
 * </p>
 * Test description:
 * <p>
 * Delete the table with group and undo, no error
 * </p>
 */

public class Regression_130276 extends BaseTestCase {

	private ElementFactory factory = null;

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_130276() throws DesignFileException, SemanticException {
		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = sessionHandle.createDesign();
		factory = new ElementFactory(designHandle.getModule());

		TableHandle table = factory.newTableItem("table"); //$NON-NLS-1$
		TableGroupHandle group = factory.newTableGroup();
		table.getGroups().add(group);
		designHandle.getBody().add(table);

		designHandle.findElement("table").drop(); //$NON-NLS-1$
		designHandle.getCommandStack().undo();

		assertNotNull(designHandle.findElement("table")); //$NON-NLS-1$
	}
}
