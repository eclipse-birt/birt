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

package org.eclipse.birt.report.designer.internal.ui.layout;

import org.eclipse.birt.report.designer.util.ITableLayoutCalculator;

/**
 * Helper for table layout.
 */

public class TableLayoutHelper {

	/**
	 * Calculate the table column width with given defined column widths and
	 * calculator.
	 * 
	 * @param columnWidths
	 * @param containerWidth
	 * @param calculator
	 */
	public static void calculateColumnWidth(TableLayoutData.ColumnData[] columnWidths, final int containerWidth,
			ITableLayoutCalculator calculator) {
		int size = columnWidths.length;

		final int[] hintWidth = calculator.getIntColWidth();

		final int[] minWidth = new int[size];
		final int[] maxWidth = new int[size];

		for (int i = 0; i < size; i++) {
			TableLayoutData.ColumnData colData = columnWidths[i];

			minWidth[i] = Math.max(colData.minColumnWidth, colData.trueMinColumnWidth);

			maxWidth[i] = Math.max(containerWidth, minWidth[i]);
		}

		int[] width = refineColumnWidth(new TableLayoutDataProvider() {

			public int getAvailableWidth() {
				return containerWidth;
			}

			public int[] getHintColumnWidth() {
				return hintWidth;
			}

			public int[] getMaxColumnWidth() {
				return maxWidth;
			}

			public int[] getMinColumnWidth() {
				return minWidth;
			}
		});

		for (int i = 0; i < size; i++) {
			TableLayoutData.ColumnData colData = columnWidths[i];

			colData.width = width[i];
		}
	}

	/**
	 * Refines the calculated column width by TableLayoutCalculator according to
	 * given column data.
	 * 
	 * @see org.eclipse.birt.report.designer.util.ITableLayoutCalculator
	 * 
	 * @param provider
	 * @return
	 */
	public static int[] refineColumnWidth(TableLayoutDataProvider provider) {
		int[] hintWidth = provider.getHintColumnWidth();
		int[] minWidth = provider.getMinColumnWidth();
		int[] maxWidth = provider.getMaxColumnWidth();

		int availableWidth = provider.getAvailableWidth();

		assert hintWidth.length == minWidth.length;
		assert minWidth.length == maxWidth.length;

		checkValid(hintWidth, minWidth);
		checkValid(maxWidth, minWidth);

		int size = hintWidth.length;

		int totalHintWidth = getSum(hintWidth);
		int totalMinWidth = getSum(minWidth);
		int totalMaxWidth = getSum(maxWidth);

		if (totalMinWidth >= availableWidth) {
			for (int i = 0; i < size; i++) {
				// sets the width with min value.
				hintWidth[i] = minWidth[i];
			}

			return hintWidth;
		}

		if (totalMaxWidth <= availableWidth) {
			for (int i = 0; i < size; i++) {
				hintWidth[i] = maxWidth[i];
			}

			return hintWidth;
		}

		if (totalMaxWidth > availableWidth && totalMinWidth < availableWidth) {
			availableWidth = Math.max(totalMinWidth, availableWidth);

			if (totalHintWidth < availableWidth) {
				int T = availableWidth - totalHintWidth;

				int delta = 0;

				for (int i = 0; i < size; i++) {
					int xdelta = T * hintWidth[i] / totalHintWidth;

					int n = hintWidth[i] + xdelta;

					hintWidth[i] = n;

					delta += xdelta;
				}

				if (delta < T) {
					int xx = T - delta;

					while (xx > 0) {
						for (int i = 0; i < size; i++) {
							if (xx <= 0) {
								break;
							}

							hintWidth[i]++;
							xx--;
						}
					}
				}
			} else if (totalHintWidth > availableWidth) {
				int T = totalHintWidth - availableWidth;

				int delta = 0;

				for (int i = 0; i < size; i++) {
					int xdelta = T * hintWidth[i] / totalHintWidth;

					int n = Math.max(hintWidth[i] - xdelta, minWidth[i]);

					delta += hintWidth[i] - n;

					hintWidth[i] = n;
				}

				if (delta < T) {
					int xx = T - delta;

					while (xx > 0) {
						for (int i = 0; i < size; i++) {
							if (xx <= 0) {
								break;
							}

							if (hintWidth[i] > minWidth[i]) {
								hintWidth[i]--;
								xx--;
							}
						}
					}
				}
			}
		}

		return hintWidth;
	}

	private static void checkValid(int[] width, int[] minWidth) {
		for (int i = 0; i < width.length; i++) {
			if (width[i] < minWidth[i]) {
				width[i] = minWidth[i];
			}
		}
	}

	private static int getSum(int[] width) {
		int rt = 0;

		for (int i = 0; i < width.length; i++) {
			rt += width[i];
		}

		return rt;
	}
}