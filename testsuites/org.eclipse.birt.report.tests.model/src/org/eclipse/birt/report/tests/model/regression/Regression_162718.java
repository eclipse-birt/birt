/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Note: Comment this test case temporarily since BIRT doesn't support it in
 * R230&231</b><br>
 * Regression description:
 * <p>
 * GroupHandle should support getPageBreakInside method,thanks.<br>
 * <ol>
 * Support the new "Page Break Inside" property for report item, row, group
 * dialog.
 * </ol>
 * <p>
 */
public class Regression_162718 extends BaseTestCase {

	public void test_regression_162718() throws Exception {
		/*
		 * createDesign( );
		 * 
		 * // Create a GroupHandle ElementFactory factory =
		 * designHandle.getElementFactory( ); TableGroupHandle group =
		 * factory.newTableGroup( );
		 * 
		 * // Get and set pageBreakAfter group.setPageBreakAfter(
		 * DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID ); assertEquals(
		 * DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID, group .getPageBreakAfter( ) );
		 * 
		 * // Get and set pageBreakBefore group .setPageBreakBefore(
		 * DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS ); assertEquals(
		 * DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS, group .getPageBreakBefore( )
		 * );
		 * 
		 * // Get and set pageBreakIn group .setPageBreakInside(
		 * DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID ); assertEquals(
		 * DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID, group .getPageBreakInside( )
		 * );
		 */
	}
}
