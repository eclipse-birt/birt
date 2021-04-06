/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * SelectionBorder
 */
public class SelectionBorder extends AbstractBorder {

	private static boolean ENABLE_ANIMATION = false;
	private static boolean ENABLE_GRADIENT = false;

	private static final int DELAY = 100; // animation delay in millisecond

	private int offsetX = 0;
	private int offsetY = 0;

	private int lineWidth;

	private boolean painting = true;

	public SelectionBorder() {
		this(1);
	}

	public SelectionBorder(int lineWidth) {
		if (lineWidth < 1) {
			this.lineWidth = 1;
		} else {
			this.lineWidth = lineWidth;
		}
	}

	public Insets getInsets(IFigure figure) {
		return new Insets(lineWidth, lineWidth, lineWidth, lineWidth);
	}

	private void setGradientColor(Graphics g, boolean direction) {
		if (direction) {
			g.setForegroundColor(ReportColorConstants.ReportBackground);
			g.setBackgroundColor(ReportColorConstants.SelctionFillColor);
		} else {
			g.setForegroundColor(ReportColorConstants.SelctionFillColor);
			g.setBackgroundColor(ReportColorConstants.ReportBackground);
		}
	}

	private void paintRegular(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle bounds = figure.getBounds().getCopy();
		graphics.translate(bounds.getLocation());

		graphics.setBackgroundColor(ReportColorConstants.SelctionFillColor);

		graphics.fillRectangle(0, 0, bounds.width, lineWidth);
		graphics.fillRectangle(bounds.width - lineWidth, 0, lineWidth, bounds.height);
		graphics.fillRectangle(0, bounds.height - lineWidth, bounds.width, lineWidth);
		graphics.fillRectangle(0, 0, lineWidth, bounds.height);

		graphics.translate(bounds.getLocation().getNegated());
	}

	private void paintGradient(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle bounds = figure.getBounds().getCopy();
		graphics.translate(bounds.getLocation());

		int halfW = bounds.width / 2 + 1;

		setGradientColor(graphics, false);

		graphics.fillGradient(0, 0, halfW, lineWidth, false);
		graphics.fillGradient(bounds.width, 0, -halfW, lineWidth, false);

		graphics.fillGradient(0, bounds.height - lineWidth, halfW, lineWidth, false);
		graphics.fillGradient(bounds.width, bounds.height - lineWidth, -halfW, lineWidth, false);

		int halfH = bounds.height / 2 + 1;

		graphics.fillGradient(0, 0, lineWidth, halfH, true);
		graphics.fillGradient(0, bounds.height, lineWidth, -halfH, true);

		graphics.fillGradient(bounds.width - lineWidth, 0, lineWidth, halfH, true);
		graphics.fillGradient(bounds.width - lineWidth, bounds.height, lineWidth, -halfH, true);

		graphics.translate(bounds.getLocation().getNegated());

	}

	private void paintAnimated(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle bounds = figure.getBounds().getCopy();
		graphics.translate(bounds.getLocation());

		int halfW = bounds.width / 2 + 1;

		if (halfW < 32) {
			setGradientColor(graphics, false);

			graphics.fillGradient(0, 0, halfW, lineWidth, false);
			graphics.fillGradient(bounds.width, 0, -halfW, lineWidth, false);

			graphics.fillGradient(0, bounds.height - lineWidth, halfW, lineWidth, false);
			graphics.fillGradient(bounds.width, bounds.height - lineWidth, -halfW, lineWidth, false);
		} else {
			int x1;

			boolean changed = false;

			if (offsetX < halfW) {
				x1 = offsetX;
			} else {
				x1 = offsetX - halfW;
				changed = true;
			}

			setGradientColor(graphics, !changed);
			graphics.fillGradient(x1 - halfW, 0, halfW + 1, lineWidth, false);

			setGradientColor(graphics, changed);
			graphics.fillGradient(x1, 0, halfW + 1, lineWidth, false);

			setGradientColor(graphics, !changed);
			graphics.fillGradient(x1 + halfW, 0, halfW, lineWidth, false);

			setGradientColor(graphics, changed);
			graphics.fillGradient(bounds.width - x1, bounds.height - lineWidth, halfW, lineWidth, false);

			setGradientColor(graphics, !changed);
			graphics.fillGradient(bounds.width - x1 - halfW, bounds.height - lineWidth, halfW + 1, lineWidth, false);

			setGradientColor(graphics, changed);
			graphics.fillGradient(bounds.width - x1 - 2 * halfW, bounds.height - lineWidth, halfW + 1, lineWidth,
					false);
		}

		int halfH = bounds.height / 2 + 1;

		if (halfH < 32) {
			setGradientColor(graphics, false);

			graphics.fillGradient(0, 0, lineWidth, halfH, true);
			graphics.fillGradient(0, bounds.height, lineWidth, -halfH, true);

			graphics.fillGradient(bounds.width - lineWidth, 0, lineWidth, halfH, true);
			graphics.fillGradient(bounds.width - lineWidth, bounds.height, lineWidth, -halfH, true);
		} else {
			int y1;

			boolean changed = false;

			if (offsetY < halfH) {
				y1 = offsetY;
			} else {
				y1 = offsetY - halfH;
				changed = true;
			}

			setGradientColor(graphics, !changed);
			graphics.fillGradient(bounds.width - lineWidth, y1 - halfH, lineWidth, halfH + 1, true);

			setGradientColor(graphics, changed);
			graphics.fillGradient(bounds.width - lineWidth, y1, lineWidth, halfH + 1, true);

			setGradientColor(graphics, !changed);
			graphics.fillGradient(bounds.width - lineWidth, y1 + halfH, lineWidth, halfH, true);

			setGradientColor(graphics, changed);
			graphics.fillGradient(0, bounds.height - y1, lineWidth, halfH, true);

			setGradientColor(graphics, !changed);
			graphics.fillGradient(0, bounds.height - y1 - halfH, lineWidth, halfH + 1, true);

			setGradientColor(graphics, changed);
			graphics.fillGradient(0, bounds.height - y1 - 2 * halfH, lineWidth, halfH + 1, true);
		}

		graphics.translate(bounds.getLocation().getNegated());
	}

	public void paint(final IFigure figure, Graphics graphics, Insets insets) {
		if (ENABLE_ANIMATION) {
			paintAnimated(figure, graphics, insets);

			if (painting) {
				Display.getCurrent().timerExec(DELAY, new Runnable() {

					public void run() {
						offsetX += 8;
						offsetY += 8;

						if (offsetX > figure.getBounds().width) {
							offsetX = 0;
						}

						if (offsetY > figure.getBounds().height) {
							offsetY = 0;
						}

						painting = true;
						figure.repaint();
					}
				});
			}

			painting = false;
		} else if (ENABLE_GRADIENT) {
			paintGradient(figure, graphics, insets);
		} else {
			paintRegular(figure, graphics, insets);
		}

	}

	public static void enableGradient(boolean value) {
		ENABLE_GRADIENT = value;
	}

	public static void enableAnimation(boolean value) {
		ENABLE_ANIMATION = value;
	}

}
