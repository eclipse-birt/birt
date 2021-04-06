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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportFigureUtilities;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * The figure of the FirstLevelHandleDataItemEditPart.
 */
//TODO overhide the getMinsize and getperferedSize cale the thiangle
public class FirstLevelHandleDataItemFigure extends LabelFigure {
	public static final int TRIANGLE_HEIGHT = 10;
	private static final int SPACE = 5;

	/**
	 * Constructor
	 */
	public FirstLevelHandleDataItemFigure() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * ReportElementFigure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		paintTriangle(graphics);
	}

	private void paintTriangle(Graphics graphics) {
		graphics.pushState();

		Rectangle rect = super.getClientArea();
		// int x = bounds.x + ( bounds.width - rect.width ) / 2;
		// int y = bounds.y + ( bounds.height - rect.height ) / 2;
		Point center = getCenterPoint(rect);
		graphics.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

		ReportFigureUtilities.paintTriangle(graphics, TRIANGLE_HEIGHT, center);
		graphics.popState();
	}

	/**
	 * @param rect
	 * @return
	 */
	public Point getCenterPoint(Rectangle rect) {
		// int xOff = (int) ( ( rect.height - SPACE ) * 2 * Math.tan( Math.PI / 6 ) );
		int xOff = (int) (TRIANGLE_HEIGHT * 2 * Math.tan(Math.PI / 6));
		int x = rect.x + rect.width - xOff - SPACE;
		Point center = new Point(x, rect.y + rect.height / 2);
		return center;
	}

	private int getExtendLength() {
		return (int) (TRIANGLE_HEIGHT * 4 * Math.tan(Math.PI / 6)) + 2 * SPACE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getInsets()
	 */
	public Insets getInsets() {
		Insets retValue = super.getInsets();
		int width = getExtendLength();
		retValue.right = retValue.right + width;
		return super.getInsets();
	}
}
