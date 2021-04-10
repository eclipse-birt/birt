/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * NPE is thrown out when clearing content in a template item nested in a table.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create a template file.
 * <li>Create a data source and related data set.
 * <li>Drag data set from data explorer view into layout view.
 * <li>Select a label and create a template item.
 * <li>Double click this label template and clear its content.
 * <li>Press "Enter".
 * <li>NullPointException is thrown out.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Following the steps, ensure no exception throws when editing the template
 * content.
 * </p>
 */
public class Regression_116782 extends BaseTestCase {

	private final static String INPUT = "regression_116782.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * 
	 */
	public void test_regression_116782() throws DesignFileException, SemanticException {
		openDesign(INPUT);

		// get the template label inside table detail.

		TableHandle table = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		CellHandle cell = (CellHandle) ((RowHandle) table.getDetail().get(0)).getCells().get(0);
		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) cell.getContent().get(0);

		assertEquals("templateLabel", templateLabel.getName()); //$NON-NLS-1$

		// edit the default label

		DesignElementHandle defaultElement = templateLabel.getDefaultElement();
		assertTrue(defaultElement instanceof LabelHandle);

		LabelHandle defaultLabel = (LabelHandle) defaultElement;
		assertEquals("Sample Label", defaultLabel.getText()); //$NON-NLS-1$

		defaultLabel.setStringProperty(StyleHandle.COLOR_PROP, "green"); //$NON-NLS-1$

		// transfer the template item to label.

		templateLabel.transformToReportItem(defaultLabel);

	}
}
