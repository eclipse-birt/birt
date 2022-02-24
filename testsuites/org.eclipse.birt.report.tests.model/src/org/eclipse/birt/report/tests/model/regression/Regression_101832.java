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

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Description: NullPointerException throws out when editing the highlight Steps
 * to reproduce:
 * <ol>
 * <li>Add a table and bind with data set
 * <li>Add a highlight to the table
 * <li>Select a table row, switch to Property Editor->Highlight
 * <li>Edit the highlight rule
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * <ol>
 * <li>Don't apply the table highlight rule to the table row and table cell
 * <li>or could edit the highlight rule
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * NullPointerException throws out when editing the highlight
 * <p>
 * Test description:
 * <p>
 * Add a highlight rule on label and edit it, ensure there won't be any
 * exception
 * <p>
 */
public class Regression_101832 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */
	public void test_regression_101832() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$
		designHandle.getBody().add(label);

		// add highlight

		HighlightRule highlight = StructureFactory.createHighlightRule();
		highlight.setOperator("between"); //$NON-NLS-1$
		highlight.setValue1("1");//$NON-NLS-1$
		highlight.setValue2("3");//$NON-NLS-1$

		label.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).addItem(highlight);

		// edit the highlight

		HighlightRuleHandle handle = (HighlightRuleHandle) label.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP)
				.getAt(0);
		handle.setOperator(DesignChoiceConstants.MAP_OPERATOR_EQ);
		assertEquals("eq", handle.getOperator()); //$NON-NLS-1$
	}
}
