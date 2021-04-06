/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Copy a label with style in grid, the pasted label won't have style properties
 * </p>
 * Test description:
 * <p>
 * Copy/Paste a styled label in grid
 * </p>
 */

public class Regression_74253 extends BaseTestCase {

	private String filename = "Regression_74253.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws ContentException
	 * @throws NameException
	 */

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	public void test_regression_74253() throws DesignFileException, ContentException, NameException {
		openDesign(filename);
		GridHandle grid = (GridHandle) designHandle.findElement("Grid");//$NON-NLS-1$
		LabelHandle label = (LabelHandle) designHandle.findElement("Label");//$NON-NLS-1$

		LabelHandle label2 = (LabelHandle) label.copy().getHandle(design);
		label2.setName("label2");//$NON-NLS-1$

		CellHandle cell = grid.getCell(0, 1);
		cell.getContent().paste(label2, 0);

		assertEquals("MyStyle", label2.getProperty(Label.STYLE_PROP));//$NON-NLS-1$
		assertEquals("blue", label2.getProperty(Style.COLOR_PROP));//$NON-NLS-1$
		assertEquals("italic", label2.getProperty(Style.FONT_STYLE_PROP));//$NON-NLS-1$

	}
}
