/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Class ErrorDetail can be enhanced
 * <p>
 * GUI will add a new feature: clicking on the items in proplem view, it will
 * jump to the error line in XML source. The error items are from
 * ModuleHandle.getErrorList() and ModuleHandle.getWarningList(), and each item
 * is an instance of ErrorDetail. But it seems that ErrorDetail.LineNo is always
 * 0, and if user clicks the error item in problem view, it always jump to line
 * 1 in XML source, I think model should enhance it. In addtion, can model
 * support another two attributes: CHAR_START and CHAR_END ? the two attributes
 * tell us where the error starts and end, just like Java editor does. I think
 * it will be more accurate to jump to the error position.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Make sure the ErrorDetail.getLineNo( ) works.
 * <p>
 */
public class Regression_158113 extends BaseTestCase {

	private final static String REPORT = "regression_158113.xml"; //$NON-NLS-1$

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_158113() throws DesignFileException {
		openDesign(REPORT);
		List errors = designHandle.getErrorList();
		assertEquals(1, errors.size());

		ErrorDetail error = (ErrorDetail) errors.get(0);
		assertEquals(25, error.getLineNo());
		assertEquals("label1", error.getElement().getName()); //$NON-NLS-1$
	}
}
