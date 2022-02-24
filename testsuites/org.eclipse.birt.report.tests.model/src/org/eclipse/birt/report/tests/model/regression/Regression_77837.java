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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Highlight expression property is trimmed in handle but isn't trimmed in
 * structure
 * </p>
 * Test description:
 * <p>
 * Highlight expression property shouldn't be trimmed in both handle and
 * structure
 * </p>
 */
public class Regression_77837 extends BaseTestCase {

	private String filename = "Regression_77837.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
		// copyResource_INPUT( INPUT2, INPUT2 );
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_77837() throws DesignFileException, SemanticException {
		openDesign(filename);

		HighlightRule highlightrule = StructureFactory.createHighlightRule();
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$

		PropertyHandle propHandle = table.getPropertyHandle(Style.HIGHLIGHT_RULES_PROP);
		propHandle.addItem(highlightrule);

		// No trim for highlight expression in handle

		Iterator iter = propHandle.iterator();
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) iter.next();
		highlightHandle.setValue1("  a  "); //$NON-NLS-1$
		assertEquals("  a  ", highlightHandle.getValue1()); //$NON-NLS-1$

		// No trim for highlight expression in structure

		highlightrule.setValue1(" a b "); //$NON-NLS-1$
		assertEquals(" a b ", highlightrule.getValue1()); //$NON-NLS-1$

	}
}
