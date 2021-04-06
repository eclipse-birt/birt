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
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Add two report item, revert the first report item to template report item,
 * then revert it to report item, revert it back to template report item. Revert
 * the second report item to template report item, revert it report item, then
 * can't revert it back to template report item
 * </p>
 * Test description:
 * <p>
 * check if the second report item can be reverted back to template report item
 * </p>
 */

public class Regression_128338 extends BaseTestCase {

	private final static String INPUT = "Reg_128338.xml"; //$NON-NLS-1$

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
	public void test_regression_128338() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		LabelHandle label = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$
		TextItemHandle text = (TextItemHandle) designHandle.findElement("text");//$NON-NLS-1$

		// convert label to template report item, revert it to report item, and
		// revert it back to template report item again

		TemplateReportItemHandle templabel = (TemplateReportItemHandle) label.createTemplateElement("templabel");//$NON-NLS-1$

		templabel.transformToReportItem(label);

		LabelHandle label1 = (LabelHandle) designHandle.getBody().getContents().get(0);
		TemplateParameterDefinitionHandle def = (TemplateParameterDefinitionHandle) designHandle
				.getSlot(ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT).get(0);
		assertEquals(def.getName(), label1.getProperty(Label.REF_TEMPLATE_PARAMETER_PROP).toString());

		designHandle.getCommandStack().undo();

		// convert text to template report item, revert it to report item, and
		// revert it back to template report item again

		TemplateReportItemHandle temptext = (TemplateReportItemHandle) text.createTemplateElement("temptext");//$NON-NLS-1$

		temptext.transformToReportItem(text);

		TextItemHandle text1 = (TextItemHandle) designHandle.getBody().getContents().get(1);
		assertTrue(text1.canTransformToTemplate());

	}
}
