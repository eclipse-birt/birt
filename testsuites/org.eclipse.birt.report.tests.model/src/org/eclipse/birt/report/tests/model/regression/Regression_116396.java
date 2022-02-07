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

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Copy/Paste report items in library is not supported
 * </p>
 * Test description:
 * <p>
 * Copy and paste a label, table in library.
 * </p>
 */
public class Regression_116396 extends BaseTestCase {

	/**
	 * @throws NameException
	 * @throws ContentException
	 * 
	 */
	public void test_regression_116396() throws ContentException, NameException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		LibraryHandle libHandle = sessionHandle.createLibrary();
		ElementFactory factory = libHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table1", 1); //$NON-NLS-1$
		LabelHandle label = factory.newLabel("label1"); //$NON-NLS-1$

		libHandle.getComponents().add(table);
		libHandle.getComponents().add(label);

		TableHandle copiedTable = (TableHandle) table.copy().getHandle(libHandle.getModule()); // $NON-NLS-1$
		LabelHandle copiedLabel = (LabelHandle) label.copy().getHandle(libHandle.getModule()); // $NON-NLS-1$

		// paste the copied one.

		DesignElementHandle copiedTableHandle = copiedTable;
		DesignElementHandle copiedLabelHandle = copiedLabel;

		copiedTableHandle.setName("copiedTable"); //$NON-NLS-1$
		copiedLabelHandle.setName("copiedLabel"); //$NON-NLS-1$

		libHandle.getComponents().add(copiedTableHandle);
		libHandle.getComponents().add(copiedLabelHandle);

		// make sure the copied ones exist.

		assertNotNull(libHandle.findElement("copiedTable")); //$NON-NLS-1$
		assertNotNull(libHandle.findElement("copiedLabel")); //$NON-NLS-1$

	}
}
