/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.engine.internal;

import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.Polygon;

import junit.framework.TestCase;

public class PolygonTest extends TestCase {

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Collect and empty any objects that are used in multiple tests.
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testContain() {
		Polygon p1 = new Polygon();

		p1.add(0, 0);
		p1.add(100, 0);
		p1.add(50, 50);

		assertTrue(p1.contains(new Point(50, 10)));
		assertFalse(p1.contains(new Point(10, 20)));
	}

}
