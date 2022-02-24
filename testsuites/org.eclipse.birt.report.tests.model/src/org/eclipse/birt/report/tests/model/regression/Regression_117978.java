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
import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExpressionPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: User properties and named expression can't be saved.
 * 
 * Steps to reproduce:
 * <ol>
 * <li>Set user properties and named expression in a report design.
 * <li>Save the report file.
 * <li>Reopen it and find they are not saved.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Add a user property to a label, save the design, reopen it and make sure the
 * user property is properly stored
 * </p>
 */
public class Regression_117978 extends BaseTestCase {

	private final static String OUTPUT = "regression_117978.out"; //$NON-NLS-1$

	/**
	 * @throws IOException
	 * @throws DesignFileException
	 * @throws SemanticException
	 * 
	 */
	public void test_regression_117978() throws IOException, DesignFileException, SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$
		designHandle.getBody().add(label);

		UserPropertyDefn expr2 = new UserPropertyDefn();
		expr2.setName("Assignee"); //$NON-NLS-1$
		expr2.setType(new ExpressionPropertyType());
		expr2.setDefault("Anonymous"); //$NON-NLS-1$
		label.addUserPropertyDefn(expr2);
		label.setStringProperty("Assignee", "testValue"); //$NON-NLS-1$//$NON-NLS-2$

		// save the report

		// designHandle.saveAs( this.getClassFolder( ) + "/" + OUTPUT_FOLDER + "/" +
		// OUTPUT );
		String TempFile = this.genOutputFile(OUTPUT);
		designHandle.saveAs(TempFile);

		// reopen it.

		designHandle = session.openDesign(TempFile);
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$

		assertNotNull(labelHandle);
		List userProperties = labelHandle.getUserProperties();
		assertEquals(1, userProperties.size());

		UserPropertyDefn prop = (UserPropertyDefn) userProperties.get(0);
		assertEquals("Assignee", prop.getName()); //$NON-NLS-1$
		assertEquals("Anonymous", prop.getDefault().toString()); //$NON-NLS-1$

		assertEquals("testValue", labelHandle.getStringProperty("Assignee")); //$NON-NLS-1$//$NON-NLS-2$
	}
}
