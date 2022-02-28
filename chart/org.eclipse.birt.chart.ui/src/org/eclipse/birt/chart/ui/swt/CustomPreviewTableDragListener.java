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

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 *
 */

public class CustomPreviewTableDragListener extends DragSourceAdapter {

	private String strHeader;
	private CustomPreviewTable customTable;

	/**
	 *
	 */
	public CustomPreviewTableDragListener(CustomPreviewTable customTable, String strHeader) {
		super();
		this.strHeader = strHeader;
		this.customTable = customTable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.
	 * DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = strHeader;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.
	 * DragSourceEvent)
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		event.doit = (strHeader != null && strHeader.length() > 0);
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		super.dragFinished(event);

		if (event.detail == DND.DROP_COPY) {
			// Reset column color
			for (int i = 0; i < customTable.getColumnNumber(); i++) {
				customTable.setColumnColor(i, ColorPalette.getInstance().getColor(customTable.getColumnHeading(i)));
			}
		}
	}

}
