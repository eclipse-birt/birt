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

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
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
 * </p>
 * Description: Value2 should be cleaned after convert operator from between to
 * equal in highlight rule.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Insert a data.
 * <li>Set highlight rule in Property Editor. 2 between 1 and 3, color blue.
 * <li>Save the report
 * <li>Modify highlight rule, change to 1 equal 1, color blue.
 * <li>Save the report, see generated xml source.
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * In xml source, value2 should be cleaned in highlight expression.
 * <p>
 * <b>Actual result:</b>
 * <p>
 * In xml source, value1 is 1 and value2 is 3.
 * </p>
 * Test description:
 * <p>
 * Follow the steps, ensure that values is not write to xml when the rule
 * operator is "eq"
 * </p>
 */
public class Regression_132786 extends BaseTestCase {

	private final static String OUTPUT = "regression_132786.xml"; //$NON-NLS-1$

	/**
	 * @throws SemanticException
	 * @throws IOException
	 * @throws DesignFileException
	 */

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

	}

	public void test_regression_132786() throws SemanticException, IOException, DesignFileException {
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

		// change the highlight

		HighlightRuleHandle handle = (HighlightRuleHandle) label.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP)
				.getAt(0);
		handle.setOperator(DesignChoiceConstants.MAP_OPERATOR_EQ);

		// save the report and read it back.

		designHandle.saveAs(OUTPUT);
		designHandle = session.openDesign(OUTPUT);
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$
		HighlightRuleHandle highlightRuleHandle = (HighlightRuleHandle) labelHandle
				.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).getAt(0);
		assertNull(highlightRuleHandle.getValue2());
		assertEquals("eq", highlightRuleHandle.getOperator()); //$NON-NLS-1$
		assertEquals("1", highlightRuleHandle.getValue1());//$NON-NLS-1$
	}
}
