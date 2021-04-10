/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * This report design was done with BIRT report designer ver 1.0.1. But when I
 * open the same report in BIRT 2.1 and switch to preview it returns the
 * following error.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Bug solution is to trime quotes for old version design file.
 * <p>
 * Open old design file, check the quote in parameter value is trimed
 */
public class Regression_150222 extends BaseTestCase {

	private String filename = "Regression_150222.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws Exception
	 */
	public void test_regression_150222() throws DesignFileException {
		openDesign(filename);
		ScalarParameterHandle p1 = (ScalarParameterHandle) designHandle.findParameter("routename"); //$NON-NLS-1$
		ScalarParameterHandle p2 = (ScalarParameterHandle) designHandle.findParameter("DateUnit"); //$NON-NLS-1$
		ScalarParameterHandle p3 = (ScalarParameterHandle) designHandle.findParameter("BlockUnit"); //$NON-NLS-1$
		ScalarParameterHandle p4 = (ScalarParameterHandle) designHandle.findParameter("blocksize"); //$NON-NLS-1$
		ScalarParameterHandle p5 = (ScalarParameterHandle) designHandle.findParameter("partnum"); //$NON-NLS-1$
		ScalarParameterHandle p6 = (ScalarParameterHandle) designHandle.findParameter("location"); //$NON-NLS-1$
		ScalarParameterHandle p7 = (ScalarParameterHandle) designHandle.findParameter("DB"); //$NON-NLS-1$
		ScalarParameterHandle p8 = (ScalarParameterHandle) designHandle.findParameter("schema"); //$NON-NLS-1$

		assertEquals("CTD Sherlock Packs Rev 1", p1.getDefaultValue()); //$NON-NLS-1$
		assertEquals("D", p2.getDefaultValue()); //$NON-NLS-1$
		assertEquals("D", p3.getDefaultValue()); //$NON-NLS-1$
		assertEquals("7|D|1|D", p4.getDefaultValue()); //$NON-NLS-1$
		assertEquals("%", p5.getDefaultValue()); //$NON-NLS-1$
		assertEquals("%", p6.getDefaultValue()); //$NON-NLS-1$
		assertEquals("devspq", p7.getDefaultValue()); //$NON-NLS-1$
		assertEquals("active", p8.getDefaultValue()); //$NON-NLS-1$

	}
}
