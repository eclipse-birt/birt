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

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

/**
 * This layout manager lays out the components inside the master page area.
 *
 *
 */
public class MasterPageLayout extends AbstractPageFlowLayout {

	/**
	 * The consturctor.
	 *
	 * @param owner
	 */
	public MasterPageLayout(GraphicalEditPart owner) {
		super(owner);
	}

	/**
	 * Minimum height for header/footer. TODO:50 is the default value, migrate it to
	 * preference later.
	 */
	public final static int MINIMUM_HEIGHT = 50;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void layout(IFigure parent) {

		Rectangle parentBounds = getInitSize();
		Result result = getReportBounds(parentBounds);

		parentBounds = result.reportSize;

		parent.setBounds(parentBounds);
		List children = parent.getChildren();

		Rectangle clientArea = parent.getClientArea();
		int y = clientArea.y;
		int height = clientArea.height;

		IFigure figure;
		for (int i = 0; i < children.size(); i++) {
			figure = (IFigure) children.get(i);

			Rectangle bounds = (Rectangle) getConstraint(figure);
			if (bounds == null) {
				continue;
			}

			bounds = convertRectangle(bounds, clientArea);

			// this is to ensue the child layout can get the correct parent
			// client area width.
			// TODO: change/use the client layoutManager's
			// calculatePrefersize(widthHint, heightHint).
			figure.getBounds().width = bounds.width;

			Dimension preferredSize = figure.getPreferredSize();
			bounds = bounds.getCopy();

//			if ( bounds.height <= 0 )
//			{
//				bounds.height = Math.max( preferredSize.height, MINIMUM_HEIGHT );
//			}
//			else if ( bounds.height < MINIMUM_HEIGHT )
//			{
//				bounds.height = MINIMUM_HEIGHT;
//			}
			if (bounds.height < 0) {
				bounds.height = Math.max(preferredSize.height, MINIMUM_HEIGHT);
			}

			// adapt the figure's location to make sure it's inside the client
			// area.
			if (bounds.height > height) {
				bounds.height = height;
			}

			if (bounds.y + bounds.height > height + y || bounds.y < y) {
				bounds.y = height + y - bounds.height;
			}

			figure.setBounds(bounds);

		}

		Rectangle rect = new Rectangle(0, 0, parentBounds.x + parentBounds.width + result.rightSpace,
				parentBounds.y + parentBounds.height + result.bottomSpace);
		setViewProperty(rect, parentBounds);
	}

	private Rectangle convertRectangle(Rectangle bounds, Rectangle clientArea) {
		Rectangle b = bounds.getCopy();

		b.width = clientArea.width;

		if (b.x == 0) {
			b.x = clientArea.getTopLeft().x;
			b.y = clientArea.getTopLeft().y;
		} else {
			b.x = clientArea.getBottomLeft().x;
			b.y = clientArea.getBottomLeft().y - ((b.height < 0) ? MasterPageLayout.MINIMUM_HEIGHT : b.height);
		}
		return b;

	}

}
