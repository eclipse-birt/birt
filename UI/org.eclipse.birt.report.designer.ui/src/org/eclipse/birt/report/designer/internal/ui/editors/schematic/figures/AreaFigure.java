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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * A figure for page header& footer.
 * 
 */
public class AreaFigure extends Figure {

	private static final int LINE_STYLE = SWT.LINE_DASHDOT;

	private static final Insets DEFAULT_EXPAND = new Insets(2, 2, 2, 2);

	private static final int inset = 5;

	/**
	 * Creates a figure with a margin border.
	 */
	public AreaFigure() {
		super();

		setOpaque(false);

		setBorder(new MarginBorder(inset));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		Rectangle rect = getClientArea().expand(DEFAULT_EXPAND);
		Color forecolor = graphics.getForegroundColor();

		if (getBackgroundColor().equals(ColorConstants.blue)) {
			// paint the figure with blue when it's highlighted
			graphics.fillRectangle(rect);
		}

		graphics.setForegroundColor(ReportColorConstants.MarginBorderColor);

		drawLine(graphics, rect, SWT.LEFT, LINE_STYLE);
		drawLine(graphics, rect, SWT.TOP, LINE_STYLE);
		drawLine(graphics, rect, SWT.RIGHT, LINE_STYLE);
		drawLine(graphics, rect, SWT.BOTTOM, LINE_STYLE);

		graphics.setForegroundColor(forecolor);
	}

	/**
	 * Draws line with specified line style.
	 * 
	 * @param graphics
	 * @param rect      the rectangle to draw
	 * @param position  the
	 * @param lineStyle the line style to use
	 */
	private void drawLine(Graphics graphics, Rectangle rect, int position, int lineStyle) {
		graphics.setLineStyle(lineStyle);
		switch (position) {
		case SWT.LEFT:
			graphics.drawLine(rect.x, rect.y, rect.x, rect.bottom());
			break;
		case SWT.TOP:
			graphics.drawLine(rect.x, rect.y, rect.right(), rect.y);
			break;
		case SWT.RIGHT:
			graphics.drawLine(rect.right(), rect.y, rect.right(), rect.bottom());
			break;
		case SWT.BOTTOM:
			graphics.drawLine(rect.x, rect.bottom(), rect.right(), rect.bottom());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		validate();
		return super.getPreferredSize(wHint, hHint);
	}
}
