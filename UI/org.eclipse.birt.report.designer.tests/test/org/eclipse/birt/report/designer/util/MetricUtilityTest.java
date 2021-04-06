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

package org.eclipse.birt.report.designer.util;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.Display;

/**
 * Class of test for MetricUtility
 */
public class MetricUtilityTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
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