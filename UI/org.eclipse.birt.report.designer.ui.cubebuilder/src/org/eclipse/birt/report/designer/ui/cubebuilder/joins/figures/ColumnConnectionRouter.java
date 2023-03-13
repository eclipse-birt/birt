/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The figure for the connection router used when creating a join between
 * tables. Has methods to calculate the coordinates of the connection line shown
 * etc.
 */
public class ColumnConnectionRouter extends AbstractRouter {

	public static final int END_LENGTH = 30;

	public static final int FIG1_HORIZONTAL_INTERSECT_FIG2 = 0;
	public static final int FIG1_LEFTOF_FIG2 = 1;
	public static final int FIG1_RIGHTOF_FIG2 = 2;

	protected Rectangle getChopRectangle(ConnectionAnchor anchor) {
		IFigure chopFigure = ((ColumnConnectionAnchor) anchor).getChopFigure();
		return chopFigure.getBounds().getCropped(chopFigure.getInsets());
	}

	protected IFigure getOwnerFigure(ConnectionAnchor anchor) {
		return anchor.getOwner();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 */
	@Override
	public void route(Connection connection) {

		// We only support routing ColumnConnection
		// Assert.isTrue( connection instanceof ColumnConnection );
		if (!(connection instanceof ColumnConnection)) {
			return;
		}

		// Get Figure
		ColumnConnection conn = (ColumnConnection) connection;
		Figure startFigure = null;
		Figure endFigure = null;

		if (conn.getSourceAnchor() instanceof ColumnConnectionAnchor) {
			startFigure = (Figure) ((ColumnConnectionAnchor) conn.getSourceAnchor()).getChopFigure();
		}
		if (conn.getTargetAnchor() instanceof ColumnConnectionAnchor) {
			endFigure = (Figure) ((ColumnConnectionAnchor) conn.getTargetAnchor()).getChopFigure();
		}

		// Route. Calculate y positions is the key
		if (startFigure != null && endFigure != null) {
			// bounds for two figures
			Rectangle startContainerRect = startFigure.getBounds();
			Rectangle endContainerRect = endFigure.getBounds();
			Point startPoint = conn.getSourceAnchor().getReferencePoint();
			startFigure.translateToRelative(startPoint);
			Point endPoint = conn.getTargetAnchor().getReferencePoint();
			endFigure.translateToRelative(endPoint);

			int startY = calcPos(startPoint.y, startContainerRect);
			int endY = calcPos(endPoint.y, endContainerRect);

			if (startFigure.equals(((ColumnConnectionAnchor) conn.getSourceAnchor()).getOwner())
					|| endFigure.equals(((ColumnConnectionAnchor) conn.getTargetAnchor()).getOwner())) {
				startY = startContainerRect.y + 10;
				endY = endContainerRect.y + 10;
			}

			conn.removeAllPoints();
			int position = getRelativeXPosition(startContainerRect, endContainerRect);
			if (position == FIG1_LEFTOF_FIG2) {
				conn.addPoint(new Point((startContainerRect.x + startContainerRect.width), startY));
				conn.addPoint(new Point((startContainerRect.x + startContainerRect.width + END_LENGTH), startY));
				conn.addPoint(new Point(endContainerRect.x - END_LENGTH, endY));
				conn.addPoint(new Point(endContainerRect.x - 1, endY));

			} else if (position == FIG1_RIGHTOF_FIG2) {
				conn.addPoint(new Point(startContainerRect.x - 1, startY));
				conn.addPoint(new Point(startContainerRect.x - END_LENGTH, startY));
				conn.addPoint(new Point((endContainerRect.x + endContainerRect.width + END_LENGTH), endY));
				conn.addPoint(new Point((endContainerRect.x + endContainerRect.width), endY));
			} else { // FIG1_HORIZONTAL_INTERSECT_FIG2
				int xpos = (Math.min(startContainerRect.x, endContainerRect.x) - END_LENGTH);
				conn.addPoint(new Point(startContainerRect.x, startY));
				conn.addPoint(new Point(xpos, startY));
				conn.addPoint(new Point(xpos, endY));
				conn.addPoint(new Point(endContainerRect.x, endY));
			}
		}

	}

	/**
	 * @param i
	 * @param startContainerRect
	 * @return
	 */
	private int calcPos(int pos, Rectangle rect) {
		if (pos < rect.y) {
			return rect.y + 5;
		}
		if (pos > rect.y + rect.height) {
			return rect.y + rect.height - 5;
		}
		return pos;
	}

	/**
	 * @param parent
	 * @return
	 */
	public Dimension getMinimumSize(IFigure parent) {
		return new Dimension(0, 0);
	}

	/**
	 * @param parent
	 * @return
	 */
	public Dimension getPreferredSize(IFigure parent) {
		return getMinimumSize(parent);
	}

	private int getRelativeXPosition(Rectangle r1, Rectangle r2) {
		if (r2.x + r2.width < r1.x) {
			return FIG1_RIGHTOF_FIG2;
		}
		if (r1.x + r1.width < r2.x) {
			return FIG1_LEFTOF_FIG2;
		}
		return FIG1_HORIZONTAL_INTERSECT_FIG2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.AbstractRouter#getStartPoint(org.eclipse.draw2d.
	 * Connection)
	 */
	@Override
	public Point getStartPoint(Connection conn) {
		Rectangle rec = conn.getTargetAnchor().getOwner().getBounds();
		return conn.getSourceAnchor().getLocation(rec.getCenter());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.draw2d.AbstractRouter#getEndPoint(org.eclipse.draw2d.Connection)
	 */
	@Override
	public Point getEndPoint(Connection conn) {
		Rectangle rec = conn.getSourceAnchor().getOwner().getBounds();
		return conn.getTargetAnchor().getLocation(rec.getCenter());
	}

}
