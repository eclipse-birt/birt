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
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Left margin can accept "1,2in" but get exception from DimensionHandle when
 * edit it.
 * <p>
 * steps to reproduce:
 * <ol>
 * <li>Create new design file.
 * <li>Open property sheet view.
 * <li>Swith to master page view.
 * <li>Select margin node, input 1,2in for left margin
 * <li>Click on left margin again, try to edit the value.
 * <li>Get exception as follwing: ava.lang.NumberFormatException: For input
 * string: "1,2"
 * </ol>
 * 
 * <p>
 * Test description:
 * <p>
 * Set margin property as "1,2in" in en_US locale, and get it back as string
 * value, ensure that the locale-dependent input value is correctly parsed.
 * </p>
 */
public class Regression_102725 extends BaseTestCase {

	private final static String INPUT = "regression_102725.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_102725() throws DesignFileException, SemanticException {
		openDesign(INPUT, ULocale.ENGLISH);

		MasterPageHandle pageHandle = designHandle.findMasterPage("Simple MasterPage"); //$NON-NLS-1$
		DimensionHandle leftMarginHandle = pageHandle.getLeftMargin();

		// "1,2" is parsed as 12 in English locale.

		leftMarginHandle.setStringValue("1,2in"); //$NON-NLS-1$
		assertEquals("12in", pageHandle.getStringProperty(MasterPageHandle.LEFT_MARGIN_PROP)); //$NON-NLS-1$

	}
}
