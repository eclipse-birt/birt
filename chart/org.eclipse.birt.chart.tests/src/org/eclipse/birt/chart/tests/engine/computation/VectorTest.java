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

import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;

public class VectorTest extends TestCase {

	Vector v1, v2, v3;

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 */
	protected void setUp() throws Exception {
		v1 = new Vector();
		v2 = new Vector(1.0, 2.0, 3.0, false);
		v3 = new Vector(0.0, 3.0, 4.0);
	}

	/**
	 * Collect and empty any objects that are defined in the setUp() method.
	 */
	protected void tearDown() throws Exception {
		v1 = null;
		v2 = null;
	}

	/**
	 * Test setter and getter methods.
	 * 
	 */
	public void testSetAndGet() {
		v1.set(5.0, 5.0, 5.0);
		for (int i = 0; i < 3; i++) {
			assertTrue(v1.get(i) == 5.0);
		}
	}

	/**
	 * Test Add operation of Vector.
	 * 
	 */
	public void testAdd() {
		v1.add(v2);
		for (int i = 0; i < 3; i++) {
			assertTrue(v1.get(i) == (i + 1));
		}
	}

	/**
	 * Test Sub operation of Vector.
	 * 
	 */
	public void testSub() {
		v1.sub(v2);
		for (int i = 0; i < 3; i++) {
			assertTrue(v1.get(i) == -(i + 1));
		}
	}

	/**
	 * Test Scale the Vector.
	 * 
	 */
	public void testScale() {
		v2.scale(2.0);
		for (int i = 0; i < 3; i++) {
			assertTrue(v2.get(i) == 2 * (i + 1));
		}
	}

	/**
	 * Test the perspective of the Vector.
	 * 
	 */
	public void testPerspective() {
		v2.perspective(3.0);
		assertTrue(v2.get(0) == 1.0);
		assertTrue(v2.get(1) == 2.0);
		assertTrue(v2.get(2) == -1 / 3.0);
	}

	/**
	 * Test whether the Vector is a point.
	 * 
	 */
	public void testIsPoint() {
		assertTrue(v1.isPoint());
		assertFalse(v2.isPoint());
	}

	/**
	 * Test cross Product.
	 * 
	 */
	public void testCrossProduct() {
		v1 = v2.crossProduct(v3);
		assertTrue(v1.get(0) == -1.0);
		assertTrue(v1.get(1) == -4.0);
		assertTrue(v1.get(2) == 3.0);
	}

	/**
	 * Test scalar product.
	 * 
	 */
	public void testScaleProduct() {
		assertTrue(v2.scalarProduct(v2) == 14);
	}

	/**
	 * Test cosine value.
	 * 
	 */
	public void testCosineValue() {
		assertTrue(v3.cosineValue(v3) == 1.0);
	}

	/**
	 * Test the rotation.
	 * 
	 */
	public void testRotate() {
		v2.rotate(Angle3DImpl.create(-20, 45, 0));
		assertTrue(v2.get(0) == 2.3020938426523667);
		assertTrue(v2.get(1) == 2.2214053848974857);
		assertTrue(v2.get(2) == 1.9405468444669185);
	}

	/**
	 * Test the project of the Vector.
	 * 
	 */
	public void testProject() {
		v3.project(4);
		assertTrue(v3.get(0) == 0.0);
		assertTrue(v3.get(1) == 0.0);
		assertTrue(v3.get(2) == -0.25);
	}

	/**
	 * Test the inverse direction of the Vector.
	 * 
	 */
	public void testInverse() {
		v2.inverse();
		for (int i = 0; i < 3; i++) {
			assertTrue(v2.get(i) == -(i + 1));
		}
	}

	/**
	 * Test the string parse.
	 * 
	 */
	public void testToString() {
		assertEquals("X:0.0,Y:3.0,Z:4.0,PV:1.0", v3.toString()); //$NON-NLS-1$
	}
}
