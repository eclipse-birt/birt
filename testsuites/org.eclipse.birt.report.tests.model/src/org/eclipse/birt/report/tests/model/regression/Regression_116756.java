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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * When creating table template in layout editor, exception is thrown out.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create a template file.
 * <li>Add a table.
 * <li>Right click on the table and create a template item.
 * <li>Exception is thrown out.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Follow the steps, ensure that no exception will be thrown out.
 * </p>
 */
public class Regression_116756 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 * 
	 */
	public void test_regression_116756() throws SemanticException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle template = sessionHandle.createDesign();

		ElementFactory factory = template.getElementFactory();
		TableHandle table = factory.newTableItem("table1", 1); //$NON-NLS-1$

		template.getBody().add(table);

		// create template table.

		TableHandle tableHandle = (TableHandle) template.findElement("table1"); //$NON-NLS-1$
		tableHandle.createTemplateElement("templateTable"); //$NON-NLS-1$

		TemplateReportItemHandle templateTable = (TemplateReportItemHandle) template.findElement("templateTable"); //$NON-NLS-1$
		assertNotNull(templateTable);
	}
}
