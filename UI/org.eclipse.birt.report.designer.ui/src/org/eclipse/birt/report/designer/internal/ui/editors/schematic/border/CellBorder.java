/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Border for table cell.
 */

public class CellBorder extends LineBorder {

	public static final int FROM_ROW = 0;
	public static final int FROM_CELL = 1;
	private static final Insets DEFAULT_CROP = new Insets(2, 2, 2, 2);

	private static final Insets DEFAULTINSETS = new Insets(3, 3, 2, 2);

	private Insets paddingInsets = new Insets(DEFAULTINSETS);
	private Insets borderInsets;
	private int bottomFrom = FROM_CELL;
	private int topFrom = FROM_CELL;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * LineBorder#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		if (borderInsets != null) {
			return new Insets(borderInsets).add(paddingInsets);
		}

		return new Insets(paddingInsets);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * LineBorder#getBorderInsets()
	 */
	public Insets getBorderInsets() {
		if (borderInsets != null) {
			return new Insets(borderInsets);
		}

		return Figure.NO_INSETS;
	}

	/**
	 * Sets the border insets.
	 * 
	 * @param borderInsets
	 */
	public void setBorderInsets(Insets borderInsets) {
		this.borderInsets = borderInsets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#setPaddingInsets(org.eclipse.draw2d.geometry.Insets)
	 */
	public void setPaddingInsets(Insets in) {
		if (in == null || (in.left == 0 && in.right == 0 && in.top == 0 && in.bottom == 0)) {
			paddingInsets = new Insets(DEFAULTINSETS);
			return;
		}

		paddingInsets.top = in.top > DEFAULTINSETS.top ? in.top : DEFAULTINSETS.top;
		paddingInsets.bottom = in.bottom > DEFAULTINSETS.bottom ? in.bottom : DEFAULTINSETS.bottom;
		paddingInsets.left = in.left > DEFAULTINSETS.left ? in.left : DEFAULTINSETS.left;
		paddingInsets.right = in.right > DEFAULTINSETS.right ? in.right : DEFAULTINSETS.right;
	}

	/**
	 * @param figure
	 * @param g
	 * @param side
	 * @param style
	 * @param width  the border width array, arranged by {top, bottom, left, right};
	 * @param color
	 * @param insets
	 */
	protected void drawBorder(IFigure figure, Graphics g, int side, int style, int[] width, String color,
			Insets insets) {
		Rectangle r = figure.getBounds().getCopy().crop(DEFAULT_CROP).crop(insets);

		if (style != 0) {
			// set ForegroundColor with the given color
			g.setForegroundColor(ColorManager.getColor(ColorUtil.parseColor(color)));
			BorderUtil.drawBorderLine(g, side, style, width, r);
		} else {
			g.setForegroundColor(ReportColorConstants.ShadowLineColor);
			// if the border style is set to none, draw a default dot line in
			// black as default
			BorderUtil.drawDefaultLine(g, side, r);
		}

		g.restoreState();
	}

	/**
	 * @return
	 */
	public int getBottomFrom() {
		return bottomFrom;
	}

	/**
	 * @param bottomFrom
	 */
	public void setBottomFrom(int bottomFrom) {
		this.bottomFrom = bottomFrom;
	}

	/**
	 * @return
	 */
	public int getTopFrom() {
		return topFrom;
	}

	/**
	 * @param topFrom
	 */
	public void setTopFrom(int topFrom) {
		this.topFrom = topFrom;
	}

}