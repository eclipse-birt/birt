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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description: Error message pops up when I want to transfer a label in a table
 * cell to a template item which is defined in a library.
 * 
 * Steps to reproduce:
 * <ol>
 * <li>Drag a table defined in a library into a report design.
 * <li>Select a label in the table cell and transfer it to a template item.
 * <li>Error message pops up "The TemplateReportItem" is not allowed directly or
 * indirectly inside the cell's slot(content)."
 * <li>But if I transfer an element in a table cell which is created in a report
 * design to a template item, it is allowed.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Call canTransformToTemplate() on the label, if the table extends from a
 * library it should return false, otherwise if the table is defined in report
 * it should return true;
 * </p>
 */
public class Regression_117676 extends BaseTestCase {

	private final static String INPUT = "regression_117676.xml"; //$NON-NLS-1$
	private final static String LIBRARY = "regression_117676_lib.xml";//$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);
		copyResource_INPUT(LIBRARY, LIBRARY);

	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_117676() throws DesignFileException {
		openDesign(INPUT);

		// 1. label from report itself

		TableHandle localTable = (TableHandle) designHandle.findElement("localTable"); //$NON-NLS-1$
		LabelHandle label1 = (LabelHandle) ((CellHandle) ((RowHandle) localTable.getDetail().get(0)).getCells().get(0))
				.getContent().get(0);
		assertTrue(label1.canTransformToTemplate());

		// 2. label from library

		TableHandle extendsTable = (TableHandle) designHandle.findElement("NewTable"); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) ((CellHandle) ((RowHandle) extendsTable.getDetail().get(0)).getCells()
				.get(0)).getContent().get(0);
		assertFalse(label2.canTransformToTemplate());

		try {
			// throw exception if performing transform

			label2.createTemplateElement("templateLabel"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof ContentException);
		}
	}
}
