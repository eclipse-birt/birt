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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.TemplateParameterDefinitionHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * the element is transformed to a template element, all properties are
 * localized and the extended information is cleared
 * </p>
 * Test description:
 * <p>
 * Convert a extended item to a template item and revert it back to a report
 * item. Check template definition has extend property, and report item has
 * extend property
 * </p>
 */
public class Regression_142928 extends BaseTestCase {

	private String filename = "Regression_142928.xml"; //$NON-NLS-1$

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
	public void test_regression_142928() throws DesignFileException, SemanticException {
		openDesign(filename);
		LabelHandle label = (LabelHandle) designHandle.findElement("NewLabel"); //$NON-NLS-1$
		assertNotNull(label);
		TemplateReportItemHandle labelTemp = (TemplateReportItemHandle) label.createTemplateElement("labeltemp"); //$NON-NLS-1$
		labelTemp.transformToReportItem(label);

		LabelHandle label1 = (LabelHandle) designHandle.getBody().getContents().get(0);
		TemplateParameterDefinitionHandle def = (TemplateParameterDefinitionHandle) designHandle
				.getSlot(ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT).get(0);

		assertEquals(def.getName(), label1.getProperty(Label.REF_TEMPLATE_PARAMETER_PROP).toString());

		assertEquals("Lib.NewLabel", def.getDefaultElement().getProperty( //$NON-NLS-1$
				Label.EXTENDS_PROP));
		assertEquals("Lib.NewLabel", label1.getProperty(Label.EXTENDS_PROP)); //$NON-NLS-1$

	}
}
