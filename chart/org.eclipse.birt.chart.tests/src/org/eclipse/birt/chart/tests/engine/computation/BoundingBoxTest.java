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

package org.eclipse.birt.chart.tests.engine.computation;

import junit.framework.TestCase;
import org.eclipse.birt.chart.computation.BoundingBox;

public class BoundingBoxTest extends TestCase {
	BoundingBox box;

	protected void setUp() throws Exception {
		box = new BoundingBox(0, 0.0, 0.0, 3.0, 4.0, 0.0);
		box.setLeft(2.0);
		box.setTop(1.0);
	}

	protected void tearDown() throws Exception {
		box = null;
	}

	public void testGetHeight() {
		assertTrue(box.getHeight() == 4.0);
	}

	public void testGetWidth() {
		assertTrue(box.getWidth() == 3.0);
	}

	public void testGetLeft() {
		assertTrue(box.getLeft() == 2.0);
	}

	public void testGetTop() {
		assertTrue(box.getTop() == 1.0);
	}

	public void testScale() {
		box.scale(2.0);
		assertTrue(box.getHeight() == 8.0);
		assertTrue(box.getWidth() == 6.0);
		assertTrue(box.getLeft() == 4.0);
		assertTrue(box.getTop() == 2.0);
	}

}
