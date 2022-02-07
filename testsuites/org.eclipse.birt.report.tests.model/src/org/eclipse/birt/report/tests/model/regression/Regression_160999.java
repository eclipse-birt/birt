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

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Regression description:</b>
 * <p>
 * Exception is thrown out when create a template report item for the second
 * time
 * <p>
 * Step: <br>
 * 1. Drag a label from Palette View to Lay out.<br>
 * 2. Right click the label, choose "Create Template Report Item" in Context
 * menu.<br>
 * 3. Preview, right click the label, choose "Revert to Report Item" in Context
 * menu.<br>
 * 4. Preview, right click the label, choose "Create Template Report Item" in
 * Context menu, click on OK<br>
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test as the description
 * <p>
 */
public class Regression_160999 extends BaseTestCase {

	public void test_regression_160999() throws Exception {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label");
		designHandle.getBody().add(label);
		assertNotNull(label);

		// create a template element
		TemplateElementHandle templateElement = null;
		templateElement = label.createTemplateElement("template"); //$NON-NLS-1$
		assertNotNull(templateElement);

		// transform to report element
		((TemplateReportItemHandle) templateElement).transformToReportItem(label);

		// transform to template element
		templateElement = label.createTemplateElement("template"); //$NON-NLS-1$
		assertNotNull(templateElement);
		assertTrue(templateElement.isValidLayoutForCompoundElement());

	}
}
