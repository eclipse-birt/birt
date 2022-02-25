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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.CellDragTracker;
import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Table Utility class
 */
public class TableUtil {

	/**
	 * Calculate the real selected objects in the selected bounds, complement the
	 * list if not in it.
	 *
	 * @param bounds
	 * @param selection
	 * @param children
	 */
	public static void calculateNewSelection(Rectangle bounds, List selection, List children) {

		for (int i = 0; i < children.size(); i++) {
			EditPart child = (EditPart) children.get(i);
			if (!child.isSelectable() || isInTable(child)) {
				continue;
			}
			IFigure figure = ((GraphicalEditPart) child).getFigure();
			Rectangle r = figure.getBounds().getCopy();
			figure.translateToAbsolute(r);

			Rectangle rect = bounds.getCopy().intersect(r);

			if (rect.width > 0 && rect.height > 0 && figure.isShowing()
					&& child.getTargetEditPart(CellDragTracker.MARQUEE_REQUEST) == child && isFigureVisible(figure)) {
				if (!selection.contains(child)) {
					selection.add(child);
				}

			}
		}
	}

	/**
	 * Returns the union bounds for all the tabelCell in the given List.
	 *
	 * @param list
	 * @return
	 */
	public static Rectangle getUnionBounds(List list) {

		int size = list.size();
		if (size == 0) {
			return new Rectangle();
		}
		IFigure figure = ((GraphicalEditPart) list.get(0)).getFigure();
		Rectangle retValue = figure.getBounds().getCopy();

		for (int i = 1; i < size; i++) {
			GraphicalEditPart cellPart = (GraphicalEditPart) list.get(i);
			retValue.union(cellPart.getFigure().getBounds());
		}
		retValue.shrink(2, 2);
		figure.translateToAbsolute(retValue);

		return retValue;
	}

	/**
	 * Checks if the given editPart child is in a Table.
	 *
	 * @param child
	 * @return
	 */
	private static boolean isInTable(EditPart child) {
		if (child instanceof AbstractCellEditPart) {
			return false;
		}
		EditPart part = child.getParent();
		while (part != null) {
			if (part instanceof AbstractCellEditPart) {
				return true;
			}
			part = part.getParent();
		}
		return false;
	}

	/**
	 * Checks if the given figure is visible.
	 *
	 * @param fig
	 * @return
	 */
	private static boolean isFigureVisible(IFigure fig) {
		Rectangle figBounds = fig.getBounds().getCopy();
		IFigure walker = fig.getParent();
		while (!figBounds.isEmpty() && walker != null) {
			walker.translateToParent(figBounds);
			figBounds.intersect(walker.getBounds());
			walker = walker.getParent();
		}
		return !figBounds.isEmpty();
	}

	/**
	 * Calculates the X value of row
	 *
	 * @param part
	 * @param i
	 * @return
	 */
	public static int caleY(AbstractTableEditPart part, int row) {
		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		// if the Layout data is not existed, use the model data instead
		if (data == null) {
			return 0;
		}
		int height = 0;
		for (int i = 1; i < row; i++) {
			height = height + data.findRowData(i).height;
		}
		return height;
	}

	/**
	 * Calculates height of row
	 *
	 * @param part
	 * @param row
	 * @return
	 */
	public static int caleVisualHeight(TableEditPart part, Object row) {

		RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(row);

		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return adapt.getHeight();
		}
		int rowNumber = adapt.getRowNumber();
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
	public static int caleVisualWidth(TableEditPart part, Object column) {
		return caleVisualWidth(part, -1, column);
	}

	public static int caleVisualWidth(TableEditPart part, int columnIndex, Object column) {
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(column);

		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return adapt.getWidth();
		}

		int columnNumber = (columnIndex <= 0) ? adapt.getColumnNumber() : columnIndex;

		if (columnNumber <= data.columnWidths.length) {
			return data.findColumnData(columnNumber).width;
		}
		return 0;
	}

	/**
	 * Calculates the Y value of column
	 *
	 * @param part
	 * @param i
	 * @return
	 */
	public static int caleX(AbstractTableEditPart part, int column) {

		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return 0;
		}
		int height = 0;
		for (int i = 1; i < column; i++) {
			height = height + data.findColumnData(i).width;
		}
		return height;
	}

	/**
	 * Get selected cells
	 *
	 * @param part
	 * @return
	 */
	public static List getSelectionCells(ITableLayoutOwner part) {
		List list = part.getViewer().getSelectedEditParts();
		List temp = new ArrayList();

		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (list.get(i) instanceof AbstractCellEditPart) {
				temp.add(list.get(i));
			}
		}
		return temp;
	}

	/**
	 * @param list
	 * @return
	 */
	public static ISelection filletCellInSelectionEditorpart(ISelection selection) {
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return new StructuredSelection(Collections.EMPTY_LIST);
		}
		List list = ((IStructuredSelection) selection).toList();
		list = filterRemoveEditpart(list);
		List retValue = filletCellModel(list);

		return new StructuredSelection(retValue);
	}

	private static List filterRemoveEditpart(List list) {
		List retValue = new ArrayList(list);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (obj instanceof ReportElementEditPart && ((ReportElementEditPart) obj).isDelete()) {
				retValue.remove(obj);
			}
		}
		return retValue;
	}

	public static List filletCellModel(List list) {
		List retValue = new ArrayList(list);
		boolean hasRowOrColumn = false;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (obj instanceof TableEditPart.DummyColumnEditPart || obj instanceof TableEditPart.DummyRowEditPart) {
				hasRowOrColumn = true;
				break;
			}
		}
		if (hasRowOrColumn) {
			for (int i = 0; i < size; i++) {
				Object obj = list.get(i);
				if (obj instanceof TableCellEditPart) {
					retValue.remove(obj);
				}
			}
		}
		return retValue;
	}

	/**
	 * Get minimum height of row.
	 *
	 * @param part
	 * @param rowNumber
	 * @return
	 */
	public static int getMinHeight(AbstractTableEditPart part, int rowNumber) {

		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return 0;
		}
		if (rowNumber <= data.rowHeights.length) {
			return data.findRowData(rowNumber).minRowHeight;
		}
		return 0;
	}

	/**
	 * Get minimum width of column.
	 *
	 * @param part
	 * @param columnNumber
	 * @return
	 */
	public static int getMinWidth(AbstractTableEditPart part, int columnNumber) {

		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return 0;
		}
		if (columnNumber <= data.columnWidths.length) {
			return data.findColumnData(columnNumber).minColumnWidth;
		}
		return 0;
	}

	/**
	 * Gets the table contents height
	 *
	 * @param part
	 * @return
	 */
	public static int getTableContentsHeight(TableEditPart part) {
		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return 0;
		}
		int height = 0;
		if (data.rowHeights == null) {
			return height;
		}
		int len = data.rowHeights.length;
		for (int i = 0; i < len; i++) {
			height = height + data.rowHeights[i].height;
		}

		return height;
	}

	/**
	 * Gets the table contents width
	 *
	 * @param part
	 * @return
	 */
	public static int getTableContentsWidth(TableEditPart part) {
		IFigure figure = part.getLayer(LayerConstants.PRIMARY_LAYER);
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager().getConstraint(figure);
		if (data == null) {
			return 0;
		}
		int width = 0;
		if (data.columnWidths == null) {
			return width;
		}
		int len = data.columnWidths.length;
		for (int i = 0; i < len; i++) {
			width = width + data.columnWidths[i].width;
		}

		return width;
	}

	/**
	 * @param list
	 * @return true if the all objectt int the list is CellHandle.
	 */
	public static boolean isAllCell(List list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (!(obj instanceof CellHandle)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param intValue
	 * @return true if the int array is continuous.
	 */
	public static boolean isContinue(int[] intValue) {
		if (intValue == null || intValue.length < 2) {
			return true;
		}
		Arrays.sort(intValue);
		int len = intValue.length;
		return len - 1 == intValue[len - 1] - intValue[0];
	}
}
