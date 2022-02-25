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

import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * Implementation of a Connection between two columns, when a join is created An
 * icon indicating the type of join condition is also displayed on the
 * connection line.
 *
 */
public class ColumnConnection extends PolylineConnection implements AnchorListener {

	protected Label joinTypeIcon;
	protected int theJoinType;

	protected static Image equalJoin;
	protected static Image leftArrowJoin;
	protected static Image rightArrowJoin;
	protected static Image expressionJoin;

	private PolygonDecoration joinDirection = null;

	public ColumnConnection() {
		this.setOutline(true);
		joinTypeIcon = new Label(""); //$NON-NLS-1$
		joinTypeIcon.setLabelAlignment(1);
		joinTypeIcon.setTextPlacement(20);
		joinTypeIcon.setIcon(UIHelper.getImage(BuilderConstants.IMAGE_JOINS));
		this.add(joinTypeIcon);
		this.setConnectionRouter(null);

		// Decoration to Indicate the Join Directrion
		joinDirection = new PolygonDecoration();
		joinDirection.setForegroundColor(ColorConstants.darkBlue);
		this.setTargetDecoration(joinDirection);
	}

	/**
	 * @return Returns the joinDirection.
	 */
	public PolygonDecoration getJoinDirection() {
		return joinDirection;
	}

	/**
	 * @param joinDirection The joinDirection to set.
	 */
	public void setJoinDirection(PolygonDecoration joinDirection) {
		this.joinDirection = joinDirection;
	}

	/**
	 * @return: The Join Type
	 */
	public int getJoinType() {
		return theJoinType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Connection#setConnectionRouter(org.eclipse.draw2d.
	 * ConnectionRouter)
	 */
	@Override
	public void setConnectionRouter(ConnectionRouter cr) {
		if (cr == null) {
			super.setConnectionRouter(new ColumnConnectionRouter());
		} else {
			super.setConnectionRouter(cr);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void outlineShape(Graphics g) {
		g.setForegroundColor(ColorConstants.blue);
		this.setForegroundColor(this.getBackgroundColor());
		super.outlineShape(g);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.IFigure#getBounds()
	 */
	@Override
	public Rectangle getBounds() {

		if (bounds == null) {
			super.getBounds();
			if (joinTypeIcon != null) {
				bounds.union(joinTypeIcon.getBounds());
			}
		}
		return bounds;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.IFigure#validate()
	 */
	@Override
	public void validate() {
		if (!this.isValid()) {
			this.erase();

			if (getSourceAnchor() != null && getTargetAnchor() != null) {
				Point center = getTargetAnchor().getReferencePoint();
				this.setStart(getSourceAnchor().getLocation(center));
				center = getSourceAnchor().getReferencePoint();
				this.setEnd(getTargetAnchor().getLocation(center));
			}

			super.validate();

			if (getSourceAnchor().getOwner() != null && getTargetAnchor().getOwner() != null) {
				validateJoinIcon();
				this.repaint();
			}
		}
	}

	/**
	 * Validates the Join Icon. Based on the type of the Join , the icon will be
	 * different. This method also calculated the Bounds to be used by the icon
	 *
	 */
	private void validateJoinIcon() {

		PointList points = super.getPoints();
		Point leftPoint = points.getPoint(1);
		Point rightPoint = points.getPoint(2);
		int minX = Math.min(leftPoint.x, rightPoint.x);
		int maxX = Math.max(leftPoint.x, rightPoint.x);
		int minY = Math.min(leftPoint.y, rightPoint.y);
		int maxY = Math.max(leftPoint.y, rightPoint.y);
		org.eclipse.swt.graphics.Rectangle imageRect = joinTypeIcon.getIcon().getBounds();
		int x = minX + (maxX - minX) / 2 - imageRect.width / 2;
		int y = minY + (maxY - minY) / 2 - imageRect.height / 2;
		Point newPoint = new Point(x, y);
		joinTypeIcon.setLocation(newPoint);
		joinTypeIcon.setBounds(joinTypeIcon.getIconBounds());
		bounds = null;

	}
}
