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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.AbstractHandle;

/**
 * Table guide cell handle
 */
public abstract class TableHFHandle extends AbstractHandle {

	public static final int WIDTH = 40;

	public static final int HEIGHT = 19;

	/**
	 * Creates a Column and Row Handle for the given <code>TableEditPart</code>
	 * using a default {@link Locator}.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 * @param loc   The Locator used to place the handle.
	 */
	public TableHFHandle(TableEditPart owner, Locator loc) {
		super(owner, loc);
		initialize();
	}

	/*
	 * returns null, because replace by the ColumnHadle and ColumnDragHandle tracker
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return null;
	}

	/*
	 * Judge if the Point is in this handle.
	 * 
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return super.containsPoint(x, y);
	}

	/**
	 * Initializes the handle. Sets the {@link DragTracker}and DragCursor.
	 */
	protected void initialize() {
		// should draw the fill rectangle
		setOpaque(true);
		// draw the border line width is 1
		LineBorder bd = new LineBorder(1);
		bd.setColor(ReportColorConstants.HandleBorderColor);
		setBorder(bd);

		// set the default cursor, may not be a SIZEALL cursor()
		setCursor(Cursors.ARROW);

		initChildrenHandle();
	}

	/*
	 * Call the super paintFigure, paint the fill Rectangle
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
	}

	protected abstract void initChildrenHandle();

	protected int getRowHeight(Object row) {
		return TableUtil.caleVisualHeight(getTableEditPart(), row);
	}

	protected int getColumnWidth(Object column) {
		return TableUtil.caleVisualWidth(getTableEditPart(), column);
	}

	protected int getRowNumber(Object row) {
		RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(row);
		return adapt.getRowNumber();
	}

	protected int getColumnNumber(Object column) {
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(column);
		return adapt.getColumnNumber();
	}

	protected int getRowHeight(int rowNumber) {
		Object row = getTableEditPart().getRow(rowNumber);
		return getRowHeight(row);
	}

	protected int getColumnWidth(int columnNumber) {
		Object column = getTableEditPart().getColumn(columnNumber);
		if (column == null) {
			return HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableEditPart().getModel())
					.getDefaultWidth(columnNumber);
		}

		return getColumnWidth(column);
	}

	/**
	 * Gets the TableEditPart
	 * 
	 * @return the table edit part.
	 */
	protected TableEditPart getTableEditPart() {
		return (TableEditPart) getOwner();
	}

}
