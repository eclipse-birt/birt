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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The getColor Method in HighlightRule return String but not Integer type
 * <p>
 * Test description: Get and set the color on Highlight and return the color
 * using the getColor
 * <p>
 * </p>
 */
public class Regression_167527 extends BaseTestCase {

	private final static String REPORT = "regression_167527.xml";

	public void test_regression_167527() throws Exception {
		// open the report design
		openDesign(REPORT);

		// find the styles
		StyleHandle style2 = designHandle.findStyle("My-Style2"); //$NON-NLS-1$
		StyleHandle style3 = designHandle.findStyle("My-Style3"); //$NON-NLS-1$

		assertNotNull(style2);
		assertNotNull(style3);

		// get the highlight rules
		Iterator highlightRules = style2.highlightRulesIterator();
		assert (highlightRules.hasNext());

		HighlightRuleHandle style2Highlight = (HighlightRuleHandle) highlightRules.next();

		// get the color of highlight rule
		assertEquals(ColorPropertyType.RED, style2Highlight.getColor().getStringValue());
	}
}
