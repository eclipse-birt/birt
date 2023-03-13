/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * COMMENT - Add description of this class or interface here. Description should
 * go beyond the class/interface name. Use the following template:
 *
 * <Short description of class (noun phrase) followed by a dot> <More elaborate
 * description of what kind of object this class or interface represents.> <Give
 * information on (special) characteristics if possible.>
 */
public class AutoResizeTableLayout extends TableLayout implements ControlListener {

	private final Table table;
	private List<ColumnLayoutData> columns = new ArrayList<>();
	private boolean autosizing = false;
	private int oldWidth;

	/**
	 * COMMENT - Add concise description of this constructor. Description should go
	 * beyond the constructor's name.
	 *
	 *
	 */
	public AutoResizeTableLayout(final Table table) {
		this.table = table;
		table.addControlListener(this);
	}

	@Override
	public void addColumnData(ColumnLayoutData data) {
		columns.add(data);
		super.addColumnData(data);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	@Override
	public void controlMoved(ControlEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	@Override
	public void controlResized(ControlEvent e) {
		// only react on changing width min. few pixels
		// (see workaround for SWT bug getting unnecessary scroll bar)
		if (Math.abs(oldWidth - table.getClientArea().width) > 5) {
			if (autosizing) {
				return;
			}
			autosizing = true;
			try {
				autoSizeColumns();
			} finally {
				autosizing = false;
				oldWidth = table.getClientArea().width;
			}
		}
	}

	/**
	 * <Short description (short verb phrase possible) followed by a dot> <More
	 * elaborate description of "what" this method does. Omit the "how" unless
	 * necessary.>
	 *
	 *
	 */
	private void autoSizeColumns() {
		int width = table.getClientArea().width;
		width -= 10; // workaround for SWT bug getting unnecessary scroll bar

		if (width <= 1) {
			return;
		}

		TableColumn[] tableColumns = table.getColumns();
		int size = Math.min(columns.size(), tableColumns.length);
		int[] widths = new int[size];
		int fixedWidth = 0;
		int numberOfWeightColumns = 0;
		int totalWeight = 0;

		// First calc space occupied by fixed columns
		for (int i = 0; i < size; ++i) {
			ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
			if (col instanceof ColumnPixelData) {
				int pixels = ((ColumnPixelData) col).width;
				widths[i] = pixels;
				fixedWidth += pixels;
			} else if (col instanceof ColumnWeightData) {
				ColumnWeightData cw = (ColumnWeightData) col;
				++numberOfWeightColumns;
				int weight = cw.weight;
				totalWeight += weight;
			} else {
				throw new IllegalStateException("Unknown column layout data"); //$NON-NLS-1$
			}
		}

		// Do we have columns that have weight?
		if (numberOfWeightColumns > 0) {
			// Now, distribute the rest to the columns with weight
			int rest = width - fixedWidth;
			int totalDistributed = 0;
			for (int i = 0; i < size; ++i) {
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					ColumnWeightData cw = (ColumnWeightData) col;
					int weight = cw.weight;
					int pixels = totalWeight == 0 ? 0 : weight * rest / totalWeight;
					if (pixels < cw.minimumWidth) {
						pixels = cw.minimumWidth;
					}
					totalDistributed += pixels;
					widths[i] = pixels;
				}
			}
			// Distribute any remaining pixels to columns with weight
			int diff = rest - totalDistributed;
			for (int i = 0; diff > 0; ++i) {
				if (i == size) {
					i = 0;
				}
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					++widths[i];
					--diff;
				}
			}
		}
		for (int i = 0; i < size; i++) {
			if (tableColumns[i].getWidth() != widths[i]) {
				tableColumns[i].setWidth(widths[i]);
			}
		}
	}

	/**
	 * remove this from the tables listeners.
	 */
	public void dispose() {
		table.removeControlListener(this);
	}
}
