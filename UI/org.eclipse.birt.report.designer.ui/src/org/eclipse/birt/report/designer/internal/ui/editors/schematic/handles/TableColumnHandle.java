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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.handles.MoveHandleLocator;

/**
 * Shows the Table ColumnHeader
 */

public class TableColumnHandle extends TableHFHandle {

	/**
	 * constructor, owner must be TableEditPart
	 * 
	 * @param owner
	 */
	public TableColumnHandle(TableEditPart owner) {
		super(owner, new TableColumnHandleLocator(owner));
		// setCursor( Cursors.HAND );
	}

	/*
	 * initialize the children Handle, include SelectionHandle and ResizeHandle
	 * -------------------------------------------------------------- | | | | |
	 * -------------------------------------------------------------- (ColumnHandle)
	 * (ColumnDragHandle)
	 * 
	 * @see org.eclipse.gef.examples.logicdesigner.edit.handle.TableHFHandle#
	 * initChildrenHandle()
	 */
	protected void initChildrenHandle() {
		TableEditPart part = getTableEditPart();
		int count = part.getColumnCount();

		// sort the columnNumber, because the list ensure the column number is
		// sort
		int width = 0;// the handle X value

		for (int i = 1; i < count + 1; i++) {
			ColumnHandle handle = new ColumnHandle(part, i);

			handle.setPreferredSize(getColumnWidth(i), getBounds().height);
			handle.setBounds(new Rectangle(width, getBounds().y, getColumnWidth(i), getBounds().height));

			width = width + getColumnWidth(i);
			add(handle);

			// initialize the resizeHandle
			if (i == count) {
				width -= 2;
			}

			ColumnDragHandle dragHandle = new ColumnDragHandle(part, i, (i == count) ? i : (i + 1));
			dragHandle.setBounds(new Rectangle(width, getBounds().y, 2, getBounds().height));
			add(dragHandle);
		}

	}

	private static class TableColumnHandleLocator extends MoveHandleLocator {

		private TableEditPart owner;

		/**
		 * @param ref
		 */
		public TableColumnHandleLocator(TableEditPart part) {
			super(part.getFigure());
			setOwner(part);
		}

		/*
		 * Sets the handle the bounds
		 * 
		 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
		 */
		public void relocate(IFigure target) {
			Rectangle bounds;
			if (getReference() instanceof HandleBounds)
				bounds = ((HandleBounds) getReference()).getHandleBounds();
			else
				bounds = getReference().getBounds();

			// bounds = new PrecisionRectangle(bounds.getResized(-1, -1));
			Insets referenceInsets = getReference().getInsets();
			bounds = new PrecisionRectangle(
					new Rectangle(bounds.x + referenceInsets.left, bounds.y + referenceInsets.top - HEIGHT,
							bounds.width - 1 - (referenceInsets.left + referenceInsets.right), HEIGHT));

			getReference().translateToAbsolute(bounds);
			target.translateToRelative(bounds);

			target.setBounds(bounds);
			relocateChildren(target, getReference());
		}

		private void relocateChildren(IFigure parent, IFigure reference) {
			List children = parent.getChildren();

			int size = children.size();
			int width = 0;

			Dimension pDim = parent.getSize();

			int height = pDim.height;
			int y = parent.getBounds().y;
			for (int i = 0; i < size; i++) {
				IFigure f = (IFigure) children.get(i);
				Rectangle bounds = f.getBounds().getCopy();

				bounds = new PrecisionRectangle(bounds);
				Dimension dim = new PrecisionDimension(bounds.getSize());
				if (f instanceof ColumnDragHandle) {
					dim = new PrecisionDimension(2, bounds.height);
				} else if (f instanceof ColumnHandle) {
					Object column = getOwner().getColumn(((ColumnHandle) f).getColumnNumber());
					dim = new PrecisionDimension(TableUtil.caleVisualWidth(getOwner(), column), bounds.height);
				}
				reference.translateToAbsolute(dim);
				f.translateToRelative(dim);

				if (i == 0) {
					width = bounds.x;
				}
				// if (dim.width == width)
				// {
				bounds.width = dim.width;
				bounds.height = height;
				// }
				bounds.y = y;
				bounds.x = width;
				if (!(f instanceof ColumnDragHandle)) {
					width = width + dim.width;
					// height = height + bounds.height;
				}

				if (i == size - 1 && f instanceof ColumnDragHandle) {
					/**
					 * This is the last ColumnDragHandle, adjust the position or it can't be
					 * displayed.
					 */
					bounds.x -= dim.width;
				}

				f.setBounds(bounds);
			}
		}

		public TableEditPart getOwner() {
			return owner;
		}

		public void setOwner(TableEditPart owner) {
			this.owner = owner;
		}
	}

}
