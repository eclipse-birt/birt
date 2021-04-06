/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug description: </b>
 * <p>
 * Exception was thrown out when move library to sub-folder.
 * </p>
 * <b> Test description:</b>
 * <p>
 * Make sure correct exception is thrown out instead of design syntax error
 * </p>
 */
public class Regression_245341 extends BaseTestCase {

	private final static String INPUT = "regression_245341.xml";
	private final static String LIBRARY = "regression_245341_lib.xml";

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * 
	 */
	public void test_regression_117978() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		ModuleHandle moduleHandle = designHandle.getModuleHandle();
		try {
			moduleHandle.includeLibrary(LIBRARY, "regression_245341_lib");
			fail();
		} catch (Exception e) {
			String error = "The library namespace \"regression_245341_lib\" exists already.";
			assertEquals(error, e.getMessage());
		}
	}
}
