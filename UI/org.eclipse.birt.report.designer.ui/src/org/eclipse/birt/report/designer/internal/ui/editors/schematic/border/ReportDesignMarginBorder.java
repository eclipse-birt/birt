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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportRootFigure;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Presents border of margin
 *
 */
public class ReportDesignMarginBorder extends LineBorder {

	private boolean needChangeStyle = true;

	private int backgroundColor = 0xFFFFFF;

	private Insets marginInsets;

	/**
	 * Constructor
	 *
	 * @param insets
	 */
	public ReportDesignMarginBorder(Insets insets) {
		// super( insets );
		setMarginInsets(insets);
	}

	/**
	 * @param color
	 */
	public void setBackgroundColor(int color) {
		if (color != 0) {
			backgroundColor = color;
		}
	}

	/**
	 * @return
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	protected void drawBorder(IFigure figure, Graphics g, int side, int style, int[] width, int color, Insets insets) {
		Rectangle r = figure.getBounds().getCopy().crop(getMarginInsets()).crop(insets)
				.crop(ReportRootFigure.DEFAULT_CROP).crop(new Insets(1, 1, 1, 1));

		if (style != 0 && needChangeStyle) {
			// set ForegroundColor with the given color
			g.setForegroundColor(ColorManager.getColor(color));
			BorderUtil.drawBorderLine(g, side, style, width, r);
		} else {
			g.setForegroundColor(ReportColorConstants.MarginBorderColor);
			// if the border style is set to none, draw a default dot line in
			// black as default
			BorderUtil.drawDefaultLine(g, side, r);
		}

		g.restoreState();
	}

	@Override
	public Insets getBorderInsets() {
		return new Insets(marginInsets).add(super.getBorderInsets());
		// return super.getBorderInsets( );
	}

	/**
	 * Reset the style
	 */
	public void reInitStyle() {
		needChangeStyle = false;
	}

	/**
	 * Gets the margin insets
	 *
	 * @return
	 */
	public Insets getMarginInsets() {
		return new Insets(marginInsets);
	}

	@Override
	public Insets getInsets(IFigure figure) {
		return new Insets(marginInsets).add(super.getInsets(figure));
	}

	/**
	 * Gets the margin insets
	 *
	 * @param marginInsets
	 */
	public void setMarginInsets(Insets marginInsets) {
		this.marginInsets = marginInsets;
	}
}
