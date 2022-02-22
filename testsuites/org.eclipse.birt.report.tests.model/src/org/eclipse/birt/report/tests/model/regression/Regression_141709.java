/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.interfaces.IParameterModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The Interface "IScalarParameterModel" should provide a "TEXT_ID_PROP" member
 * </p>
 * Test description:
 * <p>
 * "promptTextID" is added. And add setPromptTextID and getPromptTextID methods
 * </p>
 */

public class Regression_141709 extends BaseTestCase {

	private String filename = "Regression_141709.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_141709() throws DesignFileException, SemanticException {
		openDesign(filename);
		ScalarParameterHandle param = (ScalarParameterHandle) designHandle.findParameter("p1"); //$NON-NLS-1$
		assertEquals("k1", param //$NON-NLS-1$
				.getProperty(IParameterModel.PROMPT_TEXT_ID_PROP));
		param.setPromptTextID("k2"); //$NON-NLS-1$
		assertEquals("k2", param.getPromptTextID()); //$NON-NLS-1$

	}
}
