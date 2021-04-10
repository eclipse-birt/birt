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

import org.eclipse.birt.chart.internal.computations.Matrix;
import junit.framework.TestCase;

public class MatrixTest extends TestCase {

	Matrix matrix;

	protected void setUp() throws Exception {
		super.setUp();
		double[][] d = { { 1.0, 2.0 }, { 3.0, 4.0 } };
		matrix = new Matrix(d, 2, 2);
	}

	protected void tearDown() throws Exception {
		matrix = null;
		super.tearDown();
	}

	public void testCopy() {
		Matrix matrixCopy = matrix.copy();
		for (int i = 0; i < 2; i++) {
			assertEquals(matrixCopy.get(i, 0), (double) (i * 2 + 1), 0);
			assertEquals(matrixCopy.get(i, 1), (double) (i * 2 + 2), 0);
		}
	}

	public void testGetDimension() {
		assertEquals(matrix.getRowDimension(), 2);
		assertEquals(matrix.getColumnDimension(), 2);
	}

	public void testTranspose() {
		Matrix matrixTrans = matrix.transpose();
		assertEquals(matrixTrans.get(0, 0), 1, 0);
		assertEquals(matrixTrans.get(0, 1), 3, 0);
		assertEquals(matrixTrans.get(1, 0), 2, 0);
		assertEquals(matrixTrans.get(1, 1), 4, 0);
	}

	public void testTimes() {
		Matrix matrixTimes = matrix.times(matrix);
		assertEquals(matrixTimes.get(0, 0), 7, 0);
		assertEquals(matrixTimes.get(0, 1), 10, 0);
		assertEquals(matrixTimes.get(1, 0), 15, 0);
		assertEquals(matrixTimes.get(1, 1), 22, 0);
	}

	public void testIdentity() {
		Matrix matrixId = Matrix.identity(3, 3);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == j) {
					assertEquals(matrixId.get(i, j), 1, 0);
				} else {
					assertEquals(matrixId.get(i, j), 0, 0);
				}
			}
		}
	}

	public void testInverse() {
		Matrix matrixInv = matrix.inverse();
		assertEquals(matrixInv.get(0, 0), -2.0, 0);
		assertEquals(matrixInv.get(0, 1), 1, 0);
		assertEquals(matrixInv.get(1, 0), 1.5, 0);
		assertEquals(matrixInv.get(1, 1), -0.5, 0);
	}

}
