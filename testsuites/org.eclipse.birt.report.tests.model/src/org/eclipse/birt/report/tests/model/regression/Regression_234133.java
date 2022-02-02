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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * NPE when include two libraries in report
 */
public class Regression_234133 extends BaseTestCase {

	private final static String INPUT = "regression_234133.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_234133() throws DesignFileException, SemanticException {
		openDesign(INPUT, ULocale.ENGLISH);
		ReportDesignHandle designHandle_output = (ReportDesignHandle) designHandle.copy().getHandle(null);
		List libs = designHandle_output.getModuleHandle().getLibraries();
		assertEquals(2, libs.size());
		assertEquals("regression_234133_1", ((LibraryHandle) libs.get(0)).getNamespace());
		assertEquals("regression_234133_2", ((LibraryHandle) libs.get(1)).getNamespace());

	}
}
