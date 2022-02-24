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

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SelectColumnAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SelectRowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

/**
 * A Keyboard event delegate for Table Cell Element.
 */
public class TableCellKeyDelegate extends GraphicalViewerKeyHandler {

	/**
	 * Records last horizontal movement direction. 0 - None. Negative - West.
	 * Positive - East.
	 */
	private int lastHDir;
	// private ActionRegistry actionRegistry;

	/**
	 * Records last vertical movement direction. 0 - None. Negative - North.
	 * Positive - South.
	 */
	private int lastVDir;

	private int counter;

	private static final ArrayList NULL_LIST = new ArrayList(0);

	/**
	 * When navigating through connections, a "Node" EditPart is used as a
	 * reference.
	 */
	private WeakReference cachedNode;

	/**
	 * Default Constuctor.
	 */
	public TableCellKeyDelegate(GraphicalViewer viewer, ActionRegistry actionRegistry) {
		super(viewer);
		// this.actionRegistry = actionRegistry;
		put(KeyStroke.getPressed('r', 114, SWT.ALT | SWT.SHIFT), actionRegistry.getAction(SelectRowAction.ID));
		put(KeyStroke.getPressed('R', 114, SWT.ALT | SWT.SHIFT), actionRegistry.getAction(SelectRowAction.ID));

		put(KeyStroke.getPressed('c', 99, SWT.ALT | SWT.SHIFT), actionRegistry.getAction(SelectColumnAction.ID));
		put(KeyStroke.getPressed('C', 99, SWT.ALT | SWT.SHIFT), actionRegistry.getAction(SelectColumnAction.ID));

	}

	/**
	 * @return <code>true</code> if key pressed indicates a connection
	 *         traversal/selection
	 */
	boolean acceptConnection(KeyEvent event) {
		return event.character == '/' || event.character == '?' || event.character == '\\'
				|| event.character == '\u001c' || event.character == '|';
	}

	/**
	 * @return <code>true</code> if the keys pressed indicate to traverse inside a
	 *         container
	 */
	boolean acceptIntoContainer(KeyEvent event) {
		return ((event.stateMask & SWT.ALT) != 0) && (event.keyCode == SWT.ARROW_DOWN);
	}

	/**
	 * @return <code>true</code> if the keys pressed indicate to stop
	 *         traversing/selecting connection
	 */
	boolean acceptLeaveConnection(KeyEvent event) {
		int key = event.keyCode;
		if (getFocusEditPart() instanceof ConnectionEditPart) {
			if ((key == SWT.ARROW_UP) || (key == SWT.ARROW_RIGHT) || (key == SWT.ARROW_DOWN)
					|| (key == SWT.ARROW_LEFT)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return <code>true</code> if the viewer's contents has focus and one of the
	 *         arrow keys is pressed
	 */
	boolean acceptLeaveContents(KeyEvent event) {
		int key = event.keyCode;
		return getFocusEditPart() == getViewer().getContents() && ((key == SWT.ARROW_UP) || (key == SWT.ARROW_RIGHT)
				|| (key == SWT.ARROW_DOWN) || (key == SWT.ARROW_LEFT));
	}

	/**
	 * @return <code>true</code> if the keys pressed indicate to traverse to the
	 *         parent of the currently focused EditPart
	 */
	boolean acceptOutOf(KeyEvent event) {
		return ((event.stateMask & SWT.ALT) != 0) && (event.keyCode == SWT.ARROW_UP);
	}

	boolean acceptScroll(KeyEvent event) {
		return ((event.stateMask & SWT.CTRL) != 0 && (event.stateMask & SWT.SHIFT) != 0
				&& (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_LEFT
						|| event.keyCode == SWT.ARROW_RIGHT || event.keyCode == SWT.ARROW_UP));
	}

	/**
	 * Returns the cached node. It is possible that the node is not longer in the
	 * viewer but has not been garbage collected yet.
	 */
	private GraphicalEditPart getCachedNode() {
		if ((cachedNode == null) || cachedNode.isEnqueued()) {
			return null;
		}
		return (GraphicalEditPart) cachedNode.get();
	}

	private void setCachedNode(GraphicalEditPart node) {
		if (node == null) {
			cachedNode = null;
		} else {
			cachedNode = new WeakReference(node);
		}
	}

	/**
	 * Given a connection on a node, this method finds the next (or the previous)
	 * connection of that node.
	 *
	 * @param node    The EditPart whose connections are being traversed
	 * @param current The connection relative to which the next connection has to be
	 *                found
	 * @param forward <code>true</code> if the next connection has to be found;
	 *                false otherwise
	 */
	ConnectionEditPart findConnection(GraphicalEditPart node, ConnectionEditPart current, boolean forward) {
		List connections = new ArrayList(node.getSourceConnections());
		connections.addAll(node.getTargetConnections());
		if (connections.isEmpty()) {
			return null;
		}
		if (forward) {
			counter++;
		} else {
			counter--;
		}
		while (counter < 0) {
			counter += connections.size();
		}
		counter %= connections.size();
		return (ConnectionEditPart) connections.get(counter % connections.size());
	}

	/**
	 * Returns the list of editparts which are conceptually at the same level of
	 * navigation as the currently focused editpart. By default, these are the
	 * siblings of the focused part.
	 * <p>
	 * This implementation returns a list that contains the EditPart that has focus.
	 * </p>
	 *
	 * @return a list of navigation editparts
	 */
	@Override
	protected List getNavigationSiblings() {
		EditPart focusPart = getFocusEditPart();
		if (focusPart.getParent() != null) {
			return focusPart.getParent().getChildren();
		}
		List list = new ArrayList();
		list.add(focusPart);
		return list;
	}

	/**
	 * Figures' navigation points are used to determine their direction compared to
	 * one another, and the distance between them.
	 *
	 * @return the center of the given figure
	 */
	Point getNavigationPoint(IFigure figure) {
		return figure.getBounds().getCenter();
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.character == ' ') {
			processSelect(event);
			return true;
		} else if (acceptIntoContainer(event)) {
			navigateIntoContainer(event);
			return true;
		} else if (acceptOutOf(event)) {
			navigateOut(event);
			return true;
		} else if (acceptConnection(event)) {
			navigateConnections(event);
			return true;
		} else if (acceptScroll(event)) {
			scrollViewer(event);
			return true;
		} else if (acceptLeaveConnection(event)) {
			navigateOutOfConnection(event);
			return true;
		} else if (acceptLeaveContents(event)) {
			navigateIntoContainer(event);
			return true;
		}

		switch (event.keyCode) {
		case SWT.ARROW_LEFT:
			if (navigateNextSibling(event, PositionConstants.WEST)) {
				return true;
			}
			break;
		case SWT.ARROW_RIGHT:
			if (navigateNextSibling(event, PositionConstants.EAST)) {
				return true;
			}
			break;
		case SWT.ARROW_UP:
			if (navigateNextSibling(event, PositionConstants.NORTH)) {
				return true;
			}
			break;
		case SWT.ARROW_DOWN:
			if (navigateNextSibling(event, PositionConstants.SOUTH)) {
				return true;
			}
			break;

		case SWT.HOME:
			if (navigateJumpSibling(event, PositionConstants.WEST)) {
				return true;
			}
			break;
		case SWT.END:
			if (navigateJumpSibling(event, PositionConstants.EAST)) {
				return true;
			}
			break;
		case SWT.PAGE_DOWN:
			if (navigateJumpSibling(event, PositionConstants.SOUTH)) {
				return true;
			}
			break;
		case SWT.PAGE_UP:
			if (navigateJumpSibling(event, PositionConstants.NORTH)) {
				return true;
			}
		}
		return super.keyPressed(event);
	}

	/**
	 * This method navigates through connections based on the keys pressed.
	 */
	void navigateConnections(KeyEvent event) {
		GraphicalEditPart focus = getFocusEditPart();
		ConnectionEditPart current = null;
		GraphicalEditPart node = getCachedNode();
		if (focus instanceof ConnectionEditPart) {
			current = (ConnectionEditPart) focus;
			if (node == null || (node != current.getSource() && node != current.getTarget())) {
				node = (GraphicalEditPart) current.getSource();
				counter = 0;
			}
		} else {
			node = focus;
		}

		setCachedNode(node);
		boolean forward = event.character == '/' || event.character == '?';
		ConnectionEditPart next = findConnection(node, current, forward);
		navigateTo(next, event);
	}

	/**
	 * This method traverses to the closest child of the currently focused EditPart,
	 * if it has one.
	 */
	void navigateIntoContainer(KeyEvent event) {
		GraphicalEditPart focus = getFocusEditPart();
		List childList = focus.getChildren();
		Point tl = focus.getContentPane().getBounds().getTopLeft();

		int minimum = Integer.MAX_VALUE;
		int current;
		GraphicalEditPart closestPart = null;

		for (int i = 0; i < childList.size(); i++) {
			GraphicalEditPart ged = (GraphicalEditPart) childList.get(i);
			if (!ged.isSelectable()) {
				continue;
			}
			Rectangle childBounds = ged.getFigure().getBounds();

			current = (childBounds.x - tl.x) + (childBounds.y - tl.y);
			if (current < minimum) {
				minimum = current;
				closestPart = ged;
			}
		}
		if (closestPart != null) {
			navigateTo(closestPart, event);
		}
	}

	/**
	 * Navigates to the parent of the currently focused EditPart.
	 */
	void navigateOut(KeyEvent event) {
		if (getFocusEditPart() == null || getFocusEditPart() == getViewer().getContents()
				|| getFocusEditPart().getParent() == getViewer().getContents()) {
			return;
		}
		navigateTo(getFocusEditPart().getParent(), event);
	}

	/**
	 * Navigates to the source or target of the currently focused
	 * ConnectionEditPart.
	 */
	void navigateOutOfConnection(KeyEvent event) {
		GraphicalEditPart cached = getCachedNode();
		ConnectionEditPart conn = (ConnectionEditPart) getFocusEditPart();
		if (cached != null && (cached == conn.getSource() || cached == conn.getTarget())) {
			navigateTo(cached, event);
		} else {
			navigateTo(conn.getSource(), event);
		}
	}

	void scrollViewer(KeyEvent event) {
		if (!(getViewer().getControl() instanceof FigureCanvas)) {
			return;
		}
		FigureCanvas figCanvas = (FigureCanvas) getViewer().getControl();
		Point loc = figCanvas.getViewport().getViewLocation();
		Rectangle area = figCanvas.getViewport().getClientArea(Rectangle.SINGLETON).scale(.1);
		switch (event.keyCode) {
		case SWT.ARROW_DOWN:
			figCanvas.scrollToY(loc.y + area.height);
			break;
		case SWT.ARROW_UP:
			figCanvas.scrollToY(loc.y - area.height);
			break;
		case SWT.ARROW_LEFT:
			figCanvas.scrollToX(loc.x - area.width);
			break;
		case SWT.ARROW_RIGHT:
			figCanvas.scrollToX(loc.x + area.width);
		}
	}

	/**
	 * Traverses to the next sibling in the given direction.
	 *
	 * @param event     the KeyEvent for the keys that were pressed to trigger this
	 *                  traversal
	 * @param direction PositionConstants.* indicating the direction in which to
	 *                  traverse
	 */
	boolean navigateNextSibling(KeyEvent event, int direction) {
		return navigateNextSibling(event, direction, getNavigationSiblings());
	}

	/**
	 * A modified version of
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler#navigateNextSibling
	 *
	 *      for TableCellEditPart
	 *
	 *      ---------------------------------------------------------------------
	 *      Traverses to the closest EditPart in the given list that is also in the
	 *      given direction.
	 *
	 * @param event     the KeyEvent for the keys that were pressed to trigger this
	 *                  traversal
	 * @param direction PositionConstants.* indicating the direction in which to
	 *                  traverse
	 */
	boolean navigateNextSibling(KeyEvent event, int direction, List list) {
		GraphicalEditPart epStart = getFocusEditPart();
		IFigure figure = epStart.getFigure();
		Point pStart = getNavigationPoint(figure);
		figure.translateToAbsolute(pStart);

		/**
		 * Hacks the algorithm of findSibling when current editPart is
		 * TableCellEditPart.
		 */
		if (epStart instanceof AbstractCellEditPart) {
			if ((event.stateMask & SWT.SHIFT) != 0) {
				/**
				 * Processes SHIFT mask.
				 */
				List parts = findTableCellSiblings(list, pStart, direction, epStart);

				if (parts == null || parts.size() == 0) {
					return true;
				}

				navigateTo(parts, event);
			} else {
				/**
				 * Uses our algorithm to find next Table Cell.
				 */
				EditPart next = findTableCellSibling(list, pStart, direction, epStart);

				if (next == null) {
					return false;
				}

				navigateTo(next, event);
			}
		} else {
			EditPart next = findSibling(list, pStart, direction, epStart);

			if (next == null) {
				return false;
			}

			navigateTo(next, event);
		}

		return true;
	}

	/**
	 * Not yet implemented.
	 */
	boolean navigateJumpSibling(KeyEvent event, int direction) {
		// TODO: Implement navigateJumpSibling() (for PGUP, PGDN, HOME and END
		// key events)
		return false;
	}

	/**
	 * Given an absolute point (pStart) and a list of EditParts, this method finds
	 * the closest EditPart (except for the one to be excluded) in the given
	 * direction.
	 *
	 * @param siblings  List of sibling EditParts
	 * @param pStart    The starting point (must be in absolute coordinates) from
	 *                  which the next sibling is to be found.
	 * @param direction PositionConstants
	 * @param exclude   The EditPart to be excluded from the search
	 *
	 */
	GraphicalEditPart findSibling(List siblings, Point pStart, int direction, EditPart exclude) {
		GraphicalEditPart epCurrent;
		GraphicalEditPart epFinal = null;
		IFigure figure;
		Point pCurrent;
		int distance = Integer.MAX_VALUE;

		Iterator iter = siblings.iterator();
		while (iter.hasNext()) {
			epCurrent = (GraphicalEditPart) iter.next();
			if (epCurrent == exclude || !epCurrent.isSelectable()) {
				continue;
			}
			figure = epCurrent.getFigure();
			pCurrent = getNavigationPoint(figure);
			figure.translateToAbsolute(pCurrent);
			if (pStart.getPosition(pCurrent) != direction) {
				continue;
			}

			int d = pCurrent.getDistanceOrthogonal(pStart);
			if (d < distance) {
				distance = d;
				epFinal = epCurrent;
			}
		}
		return epFinal;
	}

	/**
	 * Finds the next Table Cell according to the direction.
	 *
	 * @param siblings
	 * @param pStart
	 * @param direction
	 * @param exclude
	 * @return
	 */
	protected GraphicalEditPart findTableCellSibling(List siblings, Point pStart, int direction, EditPart exclude) {
		AbstractCellEditPart start = (AbstractCellEditPart) exclude;

		int nRow = start.getRowNumber();
		int nCol = start.getColumnNumber();

		// TODO Consider isSelectable.

		switch (direction) {
		case PositionConstants.NORTH:
			nRow -= 1;
			break;
		case PositionConstants.SOUTH:
			nRow += start.getRowSpan();
			break;
		case PositionConstants.WEST:
			nCol -= 1;
			break;
		case PositionConstants.EAST:
			nCol += start.getColSpan();
			break;
		default:
			break;
		}

		AbstractTableEditPart parent = (AbstractTableEditPart) start.getParent();

		if (nRow < 1) {
			nRow = 1;
		}

		if (nRow > parent.getRowCount()) {
			nRow = parent.getRowCount();
		}

		if (nCol < 1) {
			nCol = 1;
		}

		if (nCol > parent.getColumnCount()) {
			nCol = parent.getColumnCount();
		}

		return parent.getCell(nRow, nCol);
	}

	/**
	 * Finds the appropriate Table Cell siblings when SHIFT is pressed.
	 *
	 * @param siblings
	 * @param pStart
	 * @param direction
	 * @param exclude
	 * @return
	 */
	protected List findTableCellSiblings(List siblings, Point pStart, int direction, EditPart exclude) {
		AbstractCellEditPart start = (AbstractCellEditPart) exclude;
		AbstractTableEditPart parent = (AbstractTableEditPart) start.getParent();

		StructuredSelection selection = (StructuredSelection) getViewer().getSelection();
		Object obj = selection.getFirstElement();

		if (obj instanceof AbstractCellEditPart) {
			AbstractCellEditPart first = (AbstractCellEditPart) obj;
			Rectangle constraint = TableCellSelectionHelper.getSelectionRectangle(first, selection.toList());

			boolean refined = TableCellSelectionHelper.increaseSelectionRectangle(constraint, parent);

			while (refined) {
				refined = TableCellSelectionHelper.increaseSelectionRectangle(constraint, parent);
			}

			translateRectangle(parent, constraint, direction);

			alterRectangle(constraint, direction, parent);

			return TableCellSelectionHelper.getRectangleSelection(constraint, parent);
		}

		return NULL_LIST;
	}

	/**
	 * Translates the coordinate of the rectangle by current direction, last
	 * movement direction and element's status.
	 *
	 * @param table
	 * @param rect
	 * @param direction
	 */
	void translateRectangle(AbstractTableEditPart table, Rectangle rect, int direction) {
		boolean HMovable = true;
		boolean VMovable = true;

		int xstart = Math.min(rect.x, rect.x + rect.width);
		int xend = Math.max(rect.x, rect.x + rect.width);

		int ystart = Math.min(rect.y, rect.y + rect.height);
		int yend = Math.max(rect.y, rect.y + rect.height);

		/**
		 * Checks if there has one Cell across all the columns or rows in the given
		 * rectangle.
		 */
		for (int i = xstart; i <= xend; i++) {
			for (int j = ystart; j <= yend; j++) {
				AbstractCellEditPart cell = table.getCell(j, i);

				if (HMovable && cell.getColSpan() >= xend - xstart + 1) {
					HMovable = false;
				}

				if (VMovable && cell.getRowSpan() >= yend - ystart + 1) {
					VMovable = false;
				}
			}
		}

		/**
		 * Checks if there has one vertical line which divides the rectangle into two
		 * complete parts.
		 */
		if (HMovable) {
			boolean HConnective = true;

			for (int i = xstart + 1; i <= xend; i++) {
				boolean spanned = false;

				for (int j = ystart; j <= yend; j++) {
					AbstractCellEditPart cell = table.getCell(j, i - 1);

					if (cell.getColumnNumber() + cell.getColSpan() - 1 >= i) {
						spanned = true;

						break;
					}
				}

				if (!spanned) {
					HConnective = false;

					break;
				}
			}

			if (HConnective) {
				HMovable = false;
			}
		}

		/**
		 * Checks if there has one horizontal line which divides the rectangle into two
		 * complete parts.
		 */
		if (VMovable) {
			boolean VConnective = true;

			for (int i = ystart + 1; i <= yend; i++) {
				boolean spanned = false;

				for (int j = xstart; j <= xend; j++) {
					AbstractCellEditPart cell = table.getCell(i - 1, j);

					if (cell.getRowNumber() + cell.getRowSpan() - 1 >= i) {
						spanned = true;

						break;
					}
				}

				if (!spanned) {
					VConnective = false;

					break;
				}
			}

			if (VConnective) {
				VMovable = false;
			}
		}

		/**
		 * If vertically movable and last vertical movement direction is negative, or
		 * not vertically movable and current movement direction is North, translate the
		 * coordinate.
		 */
		if ((lastVDir < 0 && VMovable) || (!VMovable && direction == PositionConstants.NORTH)) {
			int ny = rect.y + rect.height;

			int nHeight = -rect.height;

			rect.y = ny;
			rect.height = nHeight;
		}

		/**
		 * If horizontally movable and last horizontal movement direction is negative,
		 * or not horizontally movable and current movement direction is West, translate
		 * the coordinate.
		 */
		if ((lastHDir < 0 && HMovable) || (!HMovable && direction == PositionConstants.WEST)) {
			int nx = rect.x + rect.width;

			int nWidth = -rect.width;

			rect.x = nx;
			rect.width = nWidth;
		}
	}

	/**
	 * Changes the current selection rectangle according to the Direction.
	 *
	 * @param rect
	 * @param direction
	 * @param table
	 */
	void alterRectangle(Rectangle rect, int direction, AbstractTableEditPart table) {
		switch (direction) {
		case PositionConstants.NORTH:

			if (rect.height <= 0) {
				/**
				 * Increases the rectangle.
				 */
				rect.height -= 1;
				boolean refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);

				while (refined) {
					refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);
				}
			} else {
				/**
				 * Decreases the rectangle.
				 */
				rect.height -= 1;
				boolean refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);

				while (refined) {
					refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);
				}
			}

			/**
			 * Stores the last movement direction.
			 */
			lastVDir = rect.height;

			break;

		case PositionConstants.SOUTH:

			if (rect.height >= 0) {
				rect.height += 1;
				boolean refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);

				while (refined) {
					refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);
				}
			} else {
				rect.height += 1;
				boolean refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);

				while (refined) {
					refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);
				}
			}

			lastVDir = rect.height;

			break;

		case PositionConstants.WEST:

			if (rect.width <= 0) {
				rect.width -= 1;
				boolean refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);

				while (refined) {
					refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);
				}
			} else {
				rect.width -= 1;
				boolean refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);

				while (refined) {
					refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);
				}
			}

			lastHDir = rect.width;

			break;

		case PositionConstants.EAST:

			if (rect.width >= 0) {
				rect.width += 1;
				boolean refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);

				while (refined) {
					refined = TableCellSelectionHelper.increaseSelectionRectangle(rect, table);
				}
			} else {
				rect.width += 1;
				boolean refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);

				while (refined) {
					refined = TableCellSelectionHelper.decreaseSelectionRectangle(rect, table, direction);
				}
			}

			lastHDir = rect.width;

			break;

		default:
			break;
		}
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler#navigateTo(org.eclipse.gef.EditPart,
	 *      org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	protected void navigateTo(EditPart part, KeyEvent event) {
		super.navigateTo(part, event);
	}

	/**
	 * A multi-elements version of previous navigateTo method.
	 *
	 * @param parts
	 * @param event
	 */
	protected void navigateTo(List parts, KeyEvent event) {
		if (parts == null || parts.size() == 0) {
			return;
		}

		getViewer().setSelection(new StructuredSelection(parts));
	}
}
