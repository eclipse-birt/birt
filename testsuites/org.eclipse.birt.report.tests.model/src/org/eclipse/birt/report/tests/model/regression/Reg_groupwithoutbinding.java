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
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The design file is a published sample report. There is a goup without data
 * binding in design.xml, fail to delete the element in group header/footer
 * </p>
 * Test description:
 * <p>
 * Can drop the elements in the empty group.
 * </p>
 */

public class Reg_groupwithoutbinding extends BaseTestCase {

	private final static String INPUT = "Reg_groupwithoutbinding.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_groupwithoutbinding() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		TextItemHandle text1 = (TextItemHandle) designHandle.findElement("text1"); //$NON-NLS-1$
		TextItemHandle text4 = (TextItemHandle) designHandle.findElement("text4"); //$NON-NLS-1$
		assertTrue(text1.canDrop());
		assertTrue(text4.canDrop());
		text1.drop();
		text4.drop();

		ListHandle list = (ListHandle) designHandle.findElement("list"); //$NON-NLS-1$
		GroupHandle group = (GroupHandle) list.getGroups().get(0);
		assertEquals(2, group.getHeader().getContents().size());
		assertEquals(0, group.getFooter().getContents().size());
	}
}
