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

import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Template definition should be removed after the report item is deleted
 * <p>
 * Steps to reproduce:
 * <p>
 * <ol>
 * <li>New a report, extends lib.label
 * <li>Convert the label to template report item, and revert it back to the
 * report item
 * <li>Delete the label
 * <li>Remove the included library
 * </ol>
 * <p>
 * Expected result:
 * <p>
 * Library can be removed
 * <p>
 * Actual result:
 * <p>
 * A BIRT exception occurred. Plug-in Provider:Eclipse.org Plug-in Name:BIRT
 * Model Plug-in ID:org.eclipse.birt.report.model Version:2.1.0.v20060808-0630
 * Error Code:Error.LibraryException.LIBRARY_HAS_DESCENDENTS Error Message:The
 * library "Lib" can no be dropped because it has descendents "NewLabel" in the
 * current module.
 * <p>
 * Test description:
 * <p>
 * Follow the steps, make sure that no exception will thrown when dropping a
 * label that is reverted from a template label.
 * </p>
 */
public class Regression_153040 extends BaseTestCase {

	private final static String INPUT = "regression_153040.xml"; //$NON-NLS-1$

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
		copyResource_INPUT("regression_153040_lib.xml", "regression_153040_lib.xml");
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * Tests when the label is deleted, the template definition will be cleared too.
	 *
	 * @throws Exception
	 */

	public void test_regression_153040() throws Exception {
		this.openDesign(INPUT);

		// originally template definition slot is empty

		SlotHandle templateDefinitions = designHandle.getSlot(ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT);
		assertEquals(0, templateDefinitions.getCount());

		LibraryHandle lib = designHandle.getLibrary("regression_153040_lib"); //$NON-NLS-1$
		LabelHandle baseLabel = (LabelHandle) lib.findElement("baseLabel"); //$NON-NLS-1$

		LabelHandle label = (LabelHandle) designHandle.getElementFactory().newElementFrom(baseLabel, "newLabel"); //$NON-NLS-1$

		designHandle.getBody().add(label);

		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) label
				.createTemplateElement("templateLabel"); //$NON-NLS-1$
		assertEquals(1, templateDefinitions.getCount());

		// revert to report item.

		LabelHandle label1 = designHandle.getElementFactory().newLabel("label1"); //$NON-NLS-1$
		templateLabel.transformToReportItem(label1);

		// drop the label, which refers a template definition, make sure the
		// unused template definition is removed.

		label1.drop();
		assertEquals(0, templateDefinitions.getCount());
	}
}
