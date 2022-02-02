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
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Description:
 * <p>
 * Template report item in master page can't be reverted to report item
 * <p>
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a report and switch to Master Page
 * <li>Choose the data in master page footer and change it to template report
 * item
 * <li>Double click the template report item or right click it and choose
 * "revert to Report Item "
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * The template report item is reverted to the data
 * <p>
 * <b>Actual result:</b>
 * <p>
 * The template report item can't be reverted to the data Test description:
 * <p>
 * Test description:
 * <p>
 * Do as the bug described,retrieve the template item and transform it back to a
 * report item and retrieve the text item back.
 * </p>
 */
public class Regression_122302 extends BaseTestCase {

	private final static String INPUT = "regression_122302.xml"; //$NON-NLS-1$

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
	public void test_regression_122302() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		SimpleMasterPageHandle pageHandle = (SimpleMasterPageHandle) designHandle.getMasterPages().get(0);
		TextItemHandle textHandle = (TextItemHandle) pageHandle.getSlot(SimpleMasterPageHandle.PAGE_FOOTER_SLOT).get(0);
		textHandle.createTemplateElement("t1"); //$NON-NLS-1$

		// retrieve the template item and transform it back to a report item.

		TemplateReportItemHandle templateItem = (TemplateReportItemHandle) pageHandle
				.getSlot(SimpleMasterPageHandle.PAGE_FOOTER_SLOT).get(0);
		assertEquals("t1", templateItem.getName()); //$NON-NLS-1$
		templateItem.transformToReportItem((ReportItemHandle) templateItem.getDefaultElement());

		// retrieve the text item back
		textHandle = (TextItemHandle) pageHandle.getSlot(SimpleMasterPageHandle.PAGE_FOOTER_SLOT).get(0);
		assertEquals("text1", textHandle.getName()); //$NON-NLS-1$
	}
}
