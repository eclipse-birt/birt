/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.MoveHandleLocator;

/**
 * Table row guide cell handle
 */
public class TableRowHandle extends TableHFHandle {

	/**
	 * constructor, owner must be TableEditPart
	 * 
	 * @param owner
	 */
	public TableRowHandle(TableEditPart owner) {
		super(owner, new TableRowHandleLocator(owner));
	}

	protected void initChildrenHandle() {
		// TODO this method don't call if the column is hide
		TableEditPart part = getTableEditPart();
		List list = part.getRows();

		// add corner handle
		CornerHandle conner = new CornerHandle(part);
		conner.setLocation(new Point(0, 0));
		conner.setSize(WIDTH, HEIGHT);
		add(conner);

		// sort the columnNumber, because the list ensure the column number is
		// sort
		// Collections.sort( list, new TableGridLayer.NumberComparator( ) );
		int size = list.size();
		int height = HEIGHT;// the handle X value
		for (int i = 0; i < size; i++) {
			Object row = list.get(i);
			RowHandle handle = new RowHandle(part, getRowNumber(row));

			handle.setPreferredSize(getBounds().width, getRowHeight(row));

			handle.setBounds(new Rectangle(getBounds().x, height, getBounds().width, getRowHeight(row)));

			height = height + getRowHeight(row);
			add(handle);

			// initiate the resizeHandle
			if (i == size - 1) {
				height -= 2;
			}

			RowDragHandle dragHandle = new RowDragHandle(part, getRowNumber(row),
					(i == size - 1) ? getRowNumber(row) : (getRowNumber(row) + 1));
			dragHandle.setBounds(new Rectangle(getBounds().x, height, getBounds().width, 2));
			add(dragHandle);
		}

	}

	private static class TableRowHandleLocator extends MoveHandleLocator {

		private TableEditPart owner;

		/**
		 * @param ref
		 */
		public TableRowHandleLocator(TableEditPart part) {
			super(part.getFigure());
			setOwner(part);
		}

		/*
		 * Sets the handle the bounds
		 * 
		 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
		 */
		public void relocate(IFigure target) {
			Rectangle bounds = getReference().getBounds();

			Insets referenceInsets = getReference().getInsets();

			bounds = new PrecisionRectangle(
					new Rectangle(bounds.x + referenceInsets.left - WIDTH, bounds.y + referenceInsets.top - HEIGHT,
							WIDTH, bounds.height + HEIGHT - 1 - (referenceInsets.top + referenceInsets.bottom)));

			getReference().translateToAbsolute(bounds);
			target.translateToRelative(bounds);

			target.setBounds(bounds);
			relocateChildren(target, getReference());
		}

		private void relocateChildren(IFigure parent, IFigure reference) {
			List children = parent.getChildren();

			int size = children.size();
			int height = 0;

			Dimension pDim = parent.getSize();

			int width = pDim.width;
			int x = parent.getBounds().x;
			for (int i = 0; i < size; i++) {
				IFigure f = (IFigure) children.get(i);
				Rectangle bounds = f.getBounds().getCopy();

				bounds = new PrecisionRectangle(bounds);
				Dimension dim = new PrecisionDimension(bounds.getSize());
				if (f instanceof CornerHandle) {
					dim = new PrecisionDimension(WIDTH, HEIGHT);
				} else if (f instanceof RowDragHandle) {
					dim = new PrecisionDimension(bounds.width, 2);
				} else if (f instanceof RowHandle) {

					Object row = getOwner().getRow(((RowHandle) f).getRowNumber());

					dim = new PrecisionDimension(bounds.width, TableUtil.caleVisualHeight(getOwner(), row));
				}
				reference.translateToAbsolute(dim);
				f.translateToRelative(dim);

				if (i == 0) {
					height = bounds.y;
				}
				bounds.width = width;
				bounds.height = dim.height;
				bounds.y = height;
				bounds.x = x;
				if (!(f instanceof RowDragHandle)) {
					height = height + dim.height;
				}

				if (i == size - 1 && f instanceof RowDragHandle) {
					/**
					 * This is the last RowDragHandle, adjust the position or it can't be displayed.
					 */
					bounds.y -= dim.height;
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
