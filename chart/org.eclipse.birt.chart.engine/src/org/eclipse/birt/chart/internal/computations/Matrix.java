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

package org.eclipse.birt.chart.internal.computations;

/**
 * Matrix
 */
public class Matrix {

	private double[][] data;

	private int m, n;

	/**
	 * @param m number of rows.
	 * @param n number of colums.
	 */
	public Matrix(int m, int n) {
		this.m = m;
		this.n = n;
		data = new double[m][n];
	}

	/**
	 * @param data an array of doubles.
	 * @param m    number of rows.
	 * @param n    number of colums.
	 */
	public Matrix(double[][] data, int m, int n) {
		this.data = data;
		this.m = m;
		this.n = n;
	}

	/**
	 * @param A an array of doubles packed by columns.
	 * @param m number of rows.
	 * @exception IllegalArgumentException Array length must be a multiple of m.
	 */
	public Matrix(double A[], int m) {
		this.m = m;
		n = (m != 0 ? A.length / m : 0);
		if (m * n != A.length) {
			throw new IllegalArgumentException("Array length must be a multiple of m."); //$NON-NLS-1$
		}
		data = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				data[i][j] = A[i + j * m];
			}
		}
	}

	/**
	 * @return matrix a deep copy of matrix.
	 */
	public Matrix copy() {
		Matrix matrix = new Matrix(m, n);
		double[][] A = matrix.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = data[i][j];
			}
		}
		return matrix;
	}

	/**
	 * @return data a pointer to the matrix data array.
	 */
	public double[][] getArray() {
		return data;
	}

	/**
	 * @return A a copy of the matrix data array.
	 */
	public double[][] getArrayCopy() {
		double[][] A = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = data[i][j];
			}
		}
		return A;
	}

	/**
	 * @return m the number of rows.
	 */
	public int getRowDimension() {
		return m;
	}

	/**
	 * @return n the number of columns.
	 */
	public int getColumnDimension() {
		return n;
	}

	/**
	 * @param i row index.
	 * @param j column index.
	 * @return data(i,j) a single element within Matrix.
	 */
	public double get(int i, int j) {
		return data[i][j];
	}

	/**
	 * @param i row index.
	 * @param j column index.
	 * @param s a single element set to the Matrix.
	 * @exception ArrayIndexOutOfBoundsException
	 */
	public void set(int i, int j, double s) {
		data[i][j] = s;
	}

	/**
	 * @return matrix transposed matrix (A')
	 */
	public Matrix transpose() {
		Matrix matrix = new Matrix(n, m);
		double[][] C = matrix.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				C[j][i] = data[i][j];
			}
		}
		return matrix;
	}

	/**
	 * @param matrix another matrix
	 * @return mResult matrix1 * matrix2
	 * @exception IllegalArgumentException Matrix inner dimensions must agree.
	 */
	public Matrix times(Matrix matrix) {
		if (matrix.m != n) {
			throw new IllegalArgumentException("Matrix inner dimensions must agree."); //$NON-NLS-1$
		}
		Matrix mResult = new Matrix(m, matrix.n);
		double[][] A = mResult.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < matrix.n; j++) {
				double s = 0;
				for (int k = 0; k < n; k++) {
					s += data[i][k] * matrix.data[k][j];
				}
				A[i][j] = s;
			}
		}
		return mResult;
	}

	/**
	 * @param m number of rows.
	 * @param n number of colums.
	 * @return matrix an m*n matrix with "1" on the diagonal and "0" elsewhere.
	 */
	public static Matrix identity(int m, int n) {
		Matrix matrix = new Matrix(m, n);
		double[][] A = matrix.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = (i == j ? 1.0 : 0.0);
			}
		}
		return matrix;
	}

	/**
	 * @return inversed matrix.
	 */
	public Matrix inverse() {
		return (new MatrixDecomposition(this).decomposition());
	}
}
