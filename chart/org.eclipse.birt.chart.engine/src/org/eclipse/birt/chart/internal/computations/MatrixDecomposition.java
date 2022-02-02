/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.computations;

/**
 * MatrixDecomposition
 */
public class MatrixDecomposition {

	private double[][] data;
	private int m;

	public MatrixDecomposition(Matrix A) {
		data = A.getArrayCopy();
		m = A.getRowDimension();
	}

	protected Matrix decomposition() {
		return ludecomposition();
	}

	private Matrix ludecomposition() {
		double[][] temp = new double[m][2 * m];
		for (int i = 0; i < m; i++) {
			temp[i][m + i] = 1.0;
			for (int j = 0; j < m; j++) {
				temp[i][j] = data[i][j];
			}
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < i; j++) {
				if (temp[i][j] != 0) {
					double multiple = temp[i][j] / temp[j][j];
					for (int k = 0; k < 2 * m; k++) {

						temp[i][k] -= temp[j][k] * multiple;

					}
				}
			}
		}

		for (int i = m - 1; i >= 0; i--) {
			for (int j = m - 1; j > i; j--) {
				if (temp[i][j] != 0) {
					double multiple = temp[i][j] / temp[j][j];
					for (int k = 0; k < 2 * m; k++) {
						temp[i][k] -= temp[j][k] * multiple;
					}
				}
			}
		}

		for (int i = 0; i < m; i++) {
			if (temp[i][i] == 0) {
				throw new IllegalArgumentException("Matrix is singular."); //$NON-NLS-1$
			} else if (temp[i][i] != 1) {
				double multiple = temp[i][i];
				for (int j = 0; j < 2 * m; j++) {
					temp[i][j] /= multiple;
				}
			}
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				data[i][j] = temp[i][j + m];
			}
		}

		return new Matrix(data, m, m);
	}
}
