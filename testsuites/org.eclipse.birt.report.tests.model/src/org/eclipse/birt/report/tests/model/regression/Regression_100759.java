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
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * steps to reproduce:
 * <ol>
 * <li>new a style and apply it to a gird
 * <li>save and delete style
 * <li>check Grid style. Its property editor's style still original style.
 * <li>try to change grid style None.
 * </ol>
 * <p>
 * <b>actual result:</b>
 * <p>
 * Grid style can't be edited after delete style in outline view. Now go to
 * problem view, "The style 'NewStyle'used by Grid 'null' is not found" is added
 * to problem view. And Grid element outline icon display error.
 * <p>
 * <b>expected result:</b>
 * <p>
 * Grid style can be set to None after delete it in outline view
 * </p>
 * Test description:
 * <p>
 * Follow the steps, ensure that the element style can be set to None if style
 * value is un-resolved.
 * </p>
 */
public class Regression_100759 extends BaseTestCase {

	private final static String INPUT = "regression_100759.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyResource_INPUT(INPUT, INPUT);

	}

	public void tearDown() {

	}

	public void test_regression_100759() throws DesignFileException, SemanticException {
		openDesign(INPUT);

		GridHandle grid = (GridHandle) designHandle.findElement("grid1"); //$NON-NLS-1$
		assertEquals("s1", grid.getStringProperty(GridHandle.STYLE_PROP)); //$NON-NLS-1$
		StyleHandle style = designHandle.findStyle("s1"); //$NON-NLS-1$

		// delete the style
		style.drop();

		// now the style is unresolved.

		assertEquals("s1", grid.getStringProperty(GridHandle.STYLE_PROP)); //$NON-NLS-1$
		assertNull(grid.getStyle());

		// make sure that we can clear the unresolved style property.

		grid.setStringProperty(GridHandle.STYLE_PROP, null);
		assertEquals(null, grid.getProperty(GridHandle.STYLE_PROP));
	}
}
