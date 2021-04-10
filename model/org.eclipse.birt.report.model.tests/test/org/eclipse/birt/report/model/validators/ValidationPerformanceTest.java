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

package org.eclipse.birt.report.model.validators;

import org.eclipse.birt.report.model.api.DesignFileException;

/**
 * Helps tuning validation performance.
 */

public class ValidationPerformanceTest extends ValidatorTestCase {

	static final String DESIGN_FILE_WITH_LONG_TABLE = "ValidationPerformanceTest_1.xml"; //$NON-NLS-1$

	MyListener listener = new MyListener();

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests the performance of checking report.
	 * 
	 * @throws DesignFileException if any exception
	 */

	public void testCheckReport() throws DesignFileException {
		openDesign(DESIGN_FILE_WITH_LONG_TABLE);

		designHandle.addValidationListener(listener);

		long before = System.currentTimeMillis();
		designHandle.checkReport();
		long after = System.currentTimeMillis();

		System.out.println(after - before);
	}
}