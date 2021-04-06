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

package org.eclipse.birt.chart.internal.model;

import org.eclipse.birt.chart.internal.computations.Matrix;

/**
 * A utility class to calculate curve fitting points using the LOWESS algorithm.
 */
public class FittingCalculator {

	private double[] fittedValue;

	/**
	 * The constructor.
	 */
	public FittingCalculator() {
	}

	/**
	 * The constructor.
	 * 
	 * @param xa               x values.
	 * @param ya               y valus.
	 * @param windowProportion window proportion for LOWESS algorithm.
	 */
	public FittingCalculator(double[] xa, double[] ya, double windowProportion) {
		calculate(xa, ya, windowProportion);
	}

	/**
	 * Returns the calculated fitted value.
	 * 
	 * @return
	 */
	public double[] getFittedValue() {
		return fittedValue;
	}

	/**
	 * Calculate the fitting points by given parameters. This uses a LOWESS
	 * algorithm.
	 * 
	 * @param xa               x values.
	 * @param ya               y valus.
	 * @param windowProportion window proportion for LOWESS algorithm.
	 */
	public void calculate(double[] xa, double[] ya, double windowProportion) {
		fittedValue = new double[xa.length];

		int window = (int) Math.round(xa.length * windowProportion);

		if (window > 1) {
			double[] windowXa = new double[window];
			double[] windowYa = new double[window];
			double[] weights = new double[window];

			Matrix X = new Matrix(window, 2);
			Matrix Y = new Matrix(window, 1);
			Matrix W = new Matrix(window, window);

			// iterate each given point.
			for (int index = 0; index < xa.length; index++) {
				// Calculates window value and weights using LOWESS algorithm.
				int total = xa.length;
				int windowStart = 0;
				double maxDistance = 0;

				// detect a proper window range first.
				if (index < window) {
					for (int i = 0; i < window; i++) {
						if ((xa[index] - xa[i]) <= (xa[window + i] - xa[index])) {
							windowStart = i;
							maxDistance = Math.max(xa[index] - xa[windowStart],
									xa[windowStart + window - 1] - xa[index]);
							break;
						}
					}
				} else if (index >= window && index < total - window) {
					for (int i = 0; i < window; i++) {
						if (xa[index] - xa[index - window + 1 + i] <= xa[index + 1 + i] - xa[index]) {
							windowStart = index - window + 1 + i;
							maxDistance = Math.max(xa[index] - xa[windowStart],
									xa[windowStart + window - 1] - xa[index]);
							break;
						}
					}
				} else {
					for (int i = 0; i < window; i++) {
						if ((xa[total - 1 - i] - xa[index]) <= (xa[index] - xa[total - window - 1 - i])) {
							windowStart = total - window - i;
							maxDistance = Math.max(xa[index] - xa[windowStart],
									xa[windowStart + window - 1] - xa[index]);
							break;
						}
					}
				}

				// construct window data
				System.arraycopy(xa, windowStart, windowXa, 0, window);
				System.arraycopy(ya, windowStart, windowYa, 0, window);

				// calculate weights using tricube function.
				int windowIndex = index - windowStart;

				for (int i = 0; i < window; i++) {
					double distance = Math.abs(windowXa[windowIndex] - windowXa[i]);

					weights[i] = Math.pow(1.0 - Math.pow((distance / maxDistance), 3.0), 3.0);
				}

				// Apply WLS(Weighted Least Square) regression method
				// ===================================================
				// XL = Y
				// WXL = WY
				// XtWXL = XtWY
				// (XtWX)i(XtWX)L = (XtWX)i(XtWY)
				// L = (XtWX)i(XtWY)
				//
				// ** (M)i is the inverse of M.

				for (int i = 0; i < window; i++) {
					X.set(i, 0, 1);
					X.set(i, 1, windowXa[i]);
					Y.set(i, 0, windowYa[i]);
					W.set(i, i, weights[i]);
				}

				Matrix XTW = X.transpose().times(W);

				Matrix L;
				try {
					L = XTW.times(X).inverse().times(XTW.times(Y));
				} catch (Exception ex) {
					// in some cases, the matrix may be singular due to too many
					// null weights, just use original value as the estimation.
					L = new Matrix(2, 1);
					L.set(0, 0, ya[index]);
					L.set(1, 0, 0);
				}

				fittedValue[index] = L.get(0, 0) + L.get(1, 0) * xa[index];
			}
		} else {
			// use original y value as estimation for trivial cases.
			for (int i = 0; i < xa.length; i++) {
				fittedValue[i] = ya[i];
			}
		}
	}

}
