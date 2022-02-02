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
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Change the highlight rule of element in table will affect the hight rule of
 * the table.
 * </p>
 * Test description:
 * <p>
 * Get the label inside a table, add a highlight rule, make sure the table
 * container's highlight rule is not affected.
 * </p>
 */
public class Regression_102808 extends BaseTestCase {

	private final static String INPUT = "regression_102808.xml"; //$NON-NLS-1$

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

	public void test_regression_102808() throws DesignFileException, SemanticException {
		openDesign(INPUT);

		// get the label inside a table, add a highlight rule.

		LabelHandle label = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		assertNotNull(label);
		assertNull(label.getProperty(StyleHandle.HIGHLIGHT_RULES_PROP));

		HighlightRule rule = StructureFactory.createHighlightRule();
		rule.setValue1("2 > 1"); //$NON-NLS-1$
		rule.setProperty(HighlightRule.BACKGROUND_COLOR_MEMBER, "red"); //$NON-NLS-1$

		PropertyHandle highlightHandle = label.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);
		highlightHandle.addItem(rule);
		assertNotNull(label.getProperty(StyleHandle.HIGHLIGHT_RULES_PROP));

		// make sure the table container's highlight rule is not affected.

		TableHandle table = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		PropertyHandle tableHighlight = table.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);
		assertNull(tableHighlight.getListValue());
	}
}
