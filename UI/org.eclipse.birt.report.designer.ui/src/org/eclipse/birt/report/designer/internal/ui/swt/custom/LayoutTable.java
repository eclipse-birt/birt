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

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The table
 */

public class LayoutTable extends Composite {

	/**
	 * The number of extra pixels taken as horizontal trim by the table column. To
	 * ensure there are N pixels available for the content of the column, assign
	 * N+COLUMN_TRIM for the column width.
	 *
	 * @since 3.1
	 */

	private static int COLUMN_TRIM = "carbon".equals(SWT.getPlatform()) ? 24 : 3; //$NON-NLS-1$

	public static class ColumnsDescription {

		private ColumnLayoutData[] columns;
		private String[] headers;
		private boolean drawLines;

		public ColumnsDescription(ColumnLayoutData[] columns, String[] headers, boolean drawLines) {
			Assert.isNotNull(columns);
			this.columns = columns;
			this.headers = headers;
			this.drawLines = drawLines;
		}

		public ColumnsDescription(String[] headers, boolean drawLines) {
			this(createColumnWeightData(headers.length), headers, drawLines);
		}

		public ColumnsDescription(ColumnLayoutData[] columns, boolean drawLines) {
			this(columns, null, drawLines);
		}

		public ColumnsDescription(int nColumns, boolean drawLines) {
			this(createColumnWeightData(nColumns), null, drawLines);
		}

		private static ColumnLayoutData[] createColumnWeightData(int nColumns) {
			ColumnLayoutData[] data = new ColumnLayoutData[nColumns];
			for (int i = 0; i < nColumns; i++) {
				data[i] = new ColumnWeightData(1);
			}
			return data;
		}

		private boolean isHeaderVisible() {
			return headers != null && headers.length > 0;
		}
	}

	private ColumnsDescription columnDescription;
	private Table table;

	private boolean formStyle = false;

	public LayoutTable(Composite parent, ColumnsDescription columnsDescription, int style, boolean isFormStyle) {
		super(parent, SWT.NONE);
		formStyle = isFormStyle;
		Assert.isNotNull(columnsDescription);
		this.columnDescription = columnsDescription;
		if (formStyle) {
			setLayout(UIUtil.createGridLayoutWithMargin(1));
		} else {
			setLayout(UIUtil.createGridLayoutWithoutMargin());
		}
		if (!formStyle) {
			table = new Table(this, style);
		} else {
			table = FormWidgetFactory.getInstance().createTable(this, style);
		}
		table.setHeaderVisible(columnsDescription.headers != null);
		table.setLinesVisible(columnsDescription.drawLines);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		int columnCount = columnDescription.isHeaderVisible() ? columnDescription.headers.length
				: columnDescription.columns.length;
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			if (columnDescription.isHeaderVisible()) {
				column.setText(columnDescription.headers[i]);
			}
			column.setMoveable(false);
			if (i < columnsDescription.columns.length) {
				column.setResizable(columnsDescription.columns[i].resizable);
			}
		}

		addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				if (table != null && !table.isDisposed()) {
					Rectangle area = getClientArea();
					Point preferredSize = computeTableSize(table);
					int width = area.width - 2 * table.getBorderWidth();
					if (preferredSize.y > area.height) {
						// Subtract the scrollbar width from the total column
						// width
						// if a vertical scrollbar will be required
						Point vBarSize = table.getVerticalBar().getSize();
						width -= vBarSize.x;
					}
					if (formStyle) {
						width -= 2;
					}
					layoutTable(width, area);
				}
			}
		});
	}

	private Point computeTableSize(Table table) {
		Point result = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		int width = 0;
		int size = columnDescription.columns.length;
		for (int i = 0; i < size; ++i) {
			ColumnLayoutData layoutData = columnDescription.columns[i];
			if (layoutData instanceof ColumnPixelData) {
				ColumnPixelData col = (ColumnPixelData) layoutData;
				width += col.width;
				if (col.addTrim) {
					width += COLUMN_TRIM;
				}
			} else if (layoutData instanceof ColumnWeightData) {
				ColumnWeightData col = (ColumnWeightData) layoutData;
				width += col.minimumWidth;
			} else {
				Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
			}
		}
		if (width > result.x) {
			result.x = width;
		}
		return result;
	}

	private void layoutTable(int width, Rectangle area) {
		boolean increase = (table.getSize().x < area.width);
		// XXX: Layout is being called with an invalid value the first time
		// it is being called on Linux. This method resets the
		// Layout to null so we make sure we run it only when
		// the value is OK.
		if (width <= 1) {
			return;
		}

		TableColumn[] tableColumns = table.getColumns();
		int size = Math.min(columnDescription.columns.length, tableColumns.length);
		int[] widths = new int[size];
		int fixedWidth = 0;
		int numberOfWeightColumns = 0;
		int totalWeight = 0;

		// First calculate space occupied by fixed columns
		for (int i = 0; i < size; i++) {
			ColumnLayoutData col = columnDescription.columns[i];
			if (col instanceof ColumnPixelData) {
				ColumnPixelData cpd = (ColumnPixelData) col;
				int pixels = cpd.width;
				if (cpd.addTrim) {
					pixels += COLUMN_TRIM;
				}
				widths[i] = pixels;
				fixedWidth += pixels;
			} else if (col instanceof ColumnWeightData) {
				ColumnWeightData cw = (ColumnWeightData) col;
				numberOfWeightColumns++;
				// first time, use the weight specified by the column data,
				// otherwise use the actual width as the weight
				// int weight = firstTime ? cw.weight :
				// tableColumns[i].getWidth();
				int weight = cw.weight;
				totalWeight += weight;
			} else {
				Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
			}
		}

		// Do we have columns that have a weight
		if (numberOfWeightColumns > 0) {
			// Now distribute the rest to the columns with weight.
			int rest = width - fixedWidth;
			int totalDistributed = 0;
			for (int i = 0; i < size; ++i) {
				ColumnLayoutData col = columnDescription.columns[i];
				if (col instanceof ColumnWeightData) {
					ColumnWeightData cw = (ColumnWeightData) col;
					// calculate weight as above
					// int weight = firstTime ? cw.weight :
					// tableColumns[i].getWidth();
					int weight = cw.weight;
					int pixels = totalWeight == 0 ? 0 : weight * rest / totalWeight;
					if (pixels < cw.minimumWidth) {
						pixels = cw.minimumWidth;
					}
					totalDistributed += pixels;
					widths[i] = pixels;
				}
			}

			// Distribute any remaining pixels to columns with weight.
			int diff = rest - totalDistributed;
			for (int i = 0; diff > 0; ++i) {
				if (i == size) {
					i = 0;
				}
				ColumnLayoutData col = columnDescription.columns[i];
				if (col instanceof ColumnWeightData) {
					++widths[i];
					--diff;
				}
			}
		}

		if (increase) {
			table.setSize(area.width, area.height);
		}
		for (int i = 0; i < size; i++) {
			tableColumns[i].setWidth(widths[i]);
		}
		if (!increase) {
			table.setSize(area.width, area.height);
		}
	}

	public Table getTable() {
		return table;
	}
}
