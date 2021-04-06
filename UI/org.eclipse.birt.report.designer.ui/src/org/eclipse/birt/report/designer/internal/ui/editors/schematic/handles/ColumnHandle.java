/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ColumnTracker;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.IContainer;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.swt.SWT;

/**
 * Column MoveHandle
 */
public class ColumnHandle extends AbstractHandle implements IContainer {

	private int columnNumber;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return new ColumnTracker((TableEditPart) getOwner(), columnNumber, this);
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using a
	 * default {@link Locator}.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 */
	public ColumnHandle(TableEditPart owner, int number) {
		this(owner, new NothingLocator(owner.getFigure()), number);
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using the
	 * given <code>Locator</code>.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 * @param loc   The Locator used to place the handle.
	 */
	public ColumnHandle(TableEditPart owner, Locator loc, int number) {
		super(owner, loc);
		this.columnNumber = number;

		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return getBounds().getCopy().shrink(2, 2).contains(x, y);
	}

	/**
	 * @return width value.
	 */
	public int getWidth() {
		TableEditPart part = (TableEditPart) getOwner();

		return HandleAdapterFactory.getInstance().getColumnHandleAdapter(part.getColumn(columnNumber)).getWidth();

	}

	/**
	 * Initializes the handle. Sets the {@link DragTracker}and DragCursor.
	 */
	protected void initialize() {
		setOpaque(true);
		LineBorder bd = new LineBorder(1);
		bd.setColor(ReportColorConstants.HandleBorderColor);
		setBorder(bd);
		setCursor(Cursors.ARROW);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		if (isSelect()) {
			graphics.setBackgroundColor(ReportColorConstants.SelctionFillColor);
		} else {
			graphics.setBackgroundColor(ReportColorConstants.TableGuideFillColor);
		}
		graphics.setLineStyle(SWT.LINE_SOLID);
		Rectangle bounds = getBounds().getCopy();
		graphics.fillRectangle(bounds.resize(-1, -1));
		graphics.setBackgroundColor(ColorConstants.black);

//		ReportFigureUtilities.paintBevel( graphics,
//				getBounds( ).getCopy( ),
//				true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * IContainer#contains(int, int)
	 */
	public boolean contains(Point pt) {

		return false;
	}

	/**
	 * Get the column number.
	 * 
	 * @return Column number.
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * @param columnNumber
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * Judges if the column is selected.
	 * 
	 * @return true if selected, else false.
	 */
	public boolean isSelect() {
		TableEditPart part = (TableEditPart) getOwner();
		List list = part.getViewer().getSelectedEditParts();
		Object obj = part.getColumn(getColumnNumber());
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (((EditPart) list.get(i)).getModel() == obj) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#getOwner()
	 */
	public GraphicalEditPart getOwner() {
		// TODO Auto-generated method stub
		return super.getOwner();
	}
}