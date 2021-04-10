/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * The class implements drag/drop functions for column names table in set data
 * page.
 * 
 * @since 2.5
 */

public class ColumnNamesTableDragListener extends DragSourceAdapter {

	private Table table;

	private TableItem item;

	private ExtendedItemHandle itemHandle;

	/**
	 * 
	 */
	public ColumnNamesTableDragListener(Table table, ExtendedItemHandle itemhandle) {
		super();
		this.table = table;
		this.itemHandle = itemhandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd
	 * .DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		if (item != null) {
			event.data = ((ColumnBindingInfo) item.getData()).getName();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.
	 * DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		if (ChartReportItemHelper.instance().getBindingCubeHandle(itemHandle) != null) {
			event.doit = false;
		} else {
			int index = table.getSelectionIndex();
			if (index < 0) {
				item = null;
				event.doit = false;
			} else {
				item = table.getItem(index);
				String strColumnName = ((ColumnBindingInfo) item.getData()).getName();
				event.doit = (strColumnName != null && strColumnName.length() > 0);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceAdapter#dragFinished(org.eclipse.swt.dnd
	 * .DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		super.dragFinished(event);

		if (event.detail == DND.DROP_COPY && item != null) {
			// Reset column color
			String strColumnName = ((ColumnBindingInfo) item.getData()).getName();
			item.setBackground(ColorPalette.getInstance().getColor(strColumnName));
		}
	}
}
