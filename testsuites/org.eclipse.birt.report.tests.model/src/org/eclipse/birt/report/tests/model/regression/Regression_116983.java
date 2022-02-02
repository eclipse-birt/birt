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

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Exception is thrown out when deleting a template item then save it.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create a template file.
 * <li>Add a label in it and transfer it to a template item.
 * <li>Delete this template item and save the template file.
 * <li>Exception is thrown out.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Follow the steps and save the template.
 * </p>
 */
public class Regression_116983 extends BaseTestCase {

	private String outFileName = "regression_116983_template.out";

	protected void setUp() throws Exception {
		super.setUp();

	}

	protected void tearDown() throws Exception {
		super.tearDown();

	}

	/**
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void test_regression_116983() throws SemanticException, IOException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle template = sessionHandle.createDesign();

		ElementFactory factory = template.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$

		template.getBody().add(label);

		// transfer to template item.

		template.findElement("label").createTemplateElement("templateLabel"); //$NON-NLS-1$//$NON-NLS-2$

		// drop it

		template.findElement("templateLabel").drop(); //$NON-NLS-1$

		// save the template

		// cannot create BaseTestCases.makeOutputDir
		String TempFile = this.genOutputFile(outFileName);
		// designHandle.saveAs( TempFile );
		template.saveAs(TempFile); // $NON-NLS-1$
	}
}
