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

package org.eclipse.birt.report.designer.util;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.Display;

import junit.framework.TestCase;

/**
 * Class of test for MetricUtility
 */
public class MetricUtilityTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testInchToPixel() {
		org.eclipse.swt.graphics.Point dpi = Display.getDefault().getDPI();
		Point p = MetricUtility.inchToPixel(10, 10);
		assertEquals((int) dpi.x * 10, p.x);
		assertEquals((int) dpi.y * 10, p.y);
	}

}
