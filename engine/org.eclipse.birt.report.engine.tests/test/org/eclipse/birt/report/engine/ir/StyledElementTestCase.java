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

package org.eclipse.birt.report.engine.ir;

/**
 * Base class of styled element test case.
 *
 */
abstract public class StyledElementTestCase extends ReportElementTestCase {

	public StyledElementTestCase(ReportElementDesign e) {
		super(e);
	}

	/**
	 * Test get/setStyle methods
	 *
	 * set the style
	 *
	 * then get it to test if they work correctly
	 */

	public void testBaseStyle() {
		((StyledElementDesign) element).setStyleName("style");
		assertEquals("style", ((StyledElementDesign) element).getStyleName());
	}

}
