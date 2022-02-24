/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.eclipse.swt.graphics.Image;

/**
 * The class is the table, list grid guide handle
 * 
 */
public class TableGuideHandle extends AbstractGuideHandle {

	protected Image image;
	protected String indicatorLabel = "  ";//$NON-NLS-1$
	private static final Dimension DEFAULT_VALUE = new Dimension(40, 20);
	protected Insets gapInsets = new Insets(3, 3, 3, 3);
	int gap;

	public TableGuideHandle(GraphicalEditPart owner) {
		super(owner, new TableGuideHandleLocator(owner));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
	 */
	public void paintFigure(Graphics graphics) {
		Dimension rect = calculateIndicatorDimension(graphics, 1);

		int width = 1;
		if (!getBounds().getSize().equals(rect)) {
			setSize(rect);
		}

		Rectangle bounds = getBounds().getCopy();
		bounds.y = bounds.y + 2;
		graphics.setBackgroundColor(ReportColorConstants.TableGuideFillColor);
		graphics.fillRectangle(bounds);
		graphics.setForegroundColor(ReportColorConstants.ShadowLineColor);
		bounds = getBounds().getCopy();
		graphics.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height);
		graphics.drawLine(bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width,
				bounds.y + bounds.height - 1);
		graphics.drawLine(bounds.x + bounds.width - 1, bounds.y, bounds.x + bounds.width - 1, bounds.y + bounds.height);
		graphics.setForegroundColor(ReportColorConstants.TableGuideFillColor);
		graphics.drawLine(bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y);
		int x = getBounds().x + gapInsets.left;
		if (image != null) {
			graphics.drawImage(image, x, bounds.y + gapInsets.top);
			x += image.getBounds().width + gap;
		}
		graphics.setForegroundColor(ReportColorConstants.TableGuideTextColor);
		graphics.drawString(indicatorLabel, x + 2 * width, bounds.y + 2 + gapInsets.top - width);
	}

	private static class TableGuideHandleLocator extends MoveHandleLocator {

		private GraphicalEditPart owner;

		/**
		 * @param ref
		 */
		public TableGuideHandleLocator(GraphicalEditPart part) {
			super(part.getFigure());
			setOwner(part);
		}

		/*
		 * Sets the handle the bounds
		 * 
		 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
		 */
		public void relocate(IFigure target) {
			// System.out.println("relocate");
			Rectangle bounds;
			if (getReference() instanceof HandleBounds)
				bounds = ((HandleBounds) getReference()).getHandleBounds();
			else
				bounds = getReference().getBounds();

			bounds = new PrecisionRectangle(
					new Rectangle(bounds.x, bounds.y + bounds.height, DEFAULT_VALUE.width, DEFAULT_VALUE.height));

			getReference().translateToAbsolute(bounds);
			target.translateToRelative(bounds);
			target.setLocation(bounds.getLocation());

			if (target.getBounds().width < bounds.width || target.getBounds().height < bounds.height) {
				target.setSize(bounds.getSize());
			}
		}

		public GraphicalEditPart getOwner() {
			return owner;
		}

		public void setOwner(GraphicalEditPart owner) {
			this.owner = owner;
		}
	}

	private Dimension calculateIndicatorDimension(Graphics g, int width) {
		gap = 0;
		Dimension iconDimension = new Dimension();
		if (image != null) {
			iconDimension = new Dimension(image);
			gap = 2;
		}
		Dimension d = FigureUtilities.getTextExtents(indicatorLabel, g.getFont());
		int incheight = 0;
		if (iconDimension.height > d.height) {
			incheight = iconDimension.height - d.height;
		}
		d.expand(iconDimension.width + gap + gapInsets.left + gapInsets.right + 2 * width + 2,
				incheight + gapInsets.top + gapInsets.bottom);

		return d;
	}

	/**
	 * Sets the left corner label
	 * 
	 * @param indicatorLabel
	 */
	public void setIndicatorLabel(String indicatorLabel) {
		if (indicatorLabel != null) {
			this.indicatorLabel = indicatorLabel;
		}
	}

	/**
	 * Sets the left corner
	 * 
	 * @param image
	 */
	public void setIndicatorIcon(Image image) {
		this.image = image;
	}

}
