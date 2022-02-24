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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.LayerConstants;

/**
 * Util class
 */

public class CrosstabTableUtil {

	/**
	 * Calculates height of row
	 * 
	 * @param part
	 * @param row
	 * @return
	 */
	public static int caleVisualHeight(AbstractTableEditPart part, int rowNumber) {

		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return 0;
		}

		if (rowNumber <= data.rowHeights.length) {
			return data.findRowData(rowNumber).height;
		}
		return 0;
	}

	/**
	 * Calculates the width of column
	 * 
	 * @param part
	 * @param Column
	 * @return
	 */
	public static int caleVisualWidth(AbstractTableEditPart part, int columnNumber) {
		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return 0;
		}

		if (columnNumber <= data.columnWidths.length) {
			return data.findColumnData(columnNumber).width;
		}
		return 0;
	}
}
