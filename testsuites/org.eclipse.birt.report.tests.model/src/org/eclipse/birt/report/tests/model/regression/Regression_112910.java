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
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Elements in the library is used to extend, they must have a unqiue name. Now
 * an element without a name can be added into a library without any error.
 * Expected that an exception wlll be thrown.
 * </p>
 * Test description:
 * <p>
 * Add a table without name into a library, ensure that Model will throw an
 * exception.
 * </p>
 */
public class Regression_112910 extends BaseTestCase {

	/**
	 * @throws NameException
	 */
	public void test_regression_112910() throws NameException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		LibraryHandle libHandle = sessionHandle.createLibrary();

		ElementFactory factory = libHandle.getElementFactory();
		TableHandle table = factory.newTableItem(null);

		// clear the name.

		table.setName(null);

		try {
			libHandle.getComponents().add(table);
			fail();
		} catch (Exception e) {
			// success
		}

	}
}
