/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
