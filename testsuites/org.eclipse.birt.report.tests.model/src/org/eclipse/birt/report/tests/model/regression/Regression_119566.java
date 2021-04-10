/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Can't create template report item in master page header/footer
 * </p>
 * Test description:
 * <p>
 * Create template report item in master page header/footer
 * </p>
 */

public class Regression_119566 extends BaseTestCase {

	private String filename = "Regression_119566.xml"; //$NON-NLS-1$

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

	public void test_regression_119566() throws DesignFileException, SemanticException {
		openDesign(filename);

		// text and label are under master page header/footer.

		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		TextItemHandle text = (TextItemHandle) designHandle.findElement("text"); //$NON-NLS-1$

		SlotHandle templateParamsSlot = designHandle.getSlot(ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT);

		assertEquals(0, templateParamsSlot.getCount());
		assertTrue(label.canTransformToTemplate());
		assertTrue(text.canTransformToTemplate());

		label.createTemplateElement("tempLabel"); //$NON-NLS-1$
		text.createTemplateElement("tempText"); //$NON-NLS-1$
		assertEquals(2, templateParamsSlot.getCount());
	}
}
