/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * BIRT UI supports showing the available report output formats dynamically, and
 * if user choose the extended output formats on drill through options or
 * visibility page of properties editor, it causes an error:
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Model supported extended output format types now on drill-through and
 * visibility.
 * <p>
 * Test user-defined output format can be set on action and hiderule
 * 
 */
public class Regression_154804 extends BaseTestCase {
	private String filename = "Regression_154804.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_Regression_154804() throws DesignFileException, SemanticException {
		openDesign(filename);
		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		ActionHandle action = label.getActionHandle();
		PropertyHandle propHandle = label.getPropertyHandle(ReportItem.VISIBILITY_PROP);
		HideRuleHandle hiderule = (HideRuleHandle) propHandle.iterator().next();

		assertEquals(DesignChoiceConstants.ACTION_FORMAT_TYPE_HTML, action.getFormatType());
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_PDF, hiderule.getFormat());

		action.setFormatType("userdefined"); //$NON-NLS-1$
		hiderule.setFormat("userdefined"); //$NON-NLS-1$
		assertEquals("userdefined", action.getFormatType()); //$NON-NLS-1$
		assertEquals("userdefined", hiderule.getFormat()); //$NON-NLS-1$

	}
}
