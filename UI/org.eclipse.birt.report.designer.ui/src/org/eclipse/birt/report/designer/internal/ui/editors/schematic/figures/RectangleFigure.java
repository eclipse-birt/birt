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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This is the rectangle to show the border
 */
public class RectangleFigure extends Shape {

	private boolean opaque = false;

	/**
	 * Constructor for RectangleFigure.
	 */
	public RectangleFigure() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		Rectangle bounds = getBounds().getCopy();
		Border border = getBorder();
		if (border != null) {
			bounds = bounds.crop(border.getInsets(null));
		}
		if (isOpaque())
			graphics.fillRectangle(bounds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		Rectangle bounds = getBounds().getCopy();
		Border border = getBorder();
		if (border != null) {
			bounds = bounds.crop(border.getInsets(null));
		}
		graphics.drawRectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#isOpaque()
	 */
	public boolean isOpaque() {
		return opaque;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#setOpaque(boolean)
	 */
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(isOpaque);
		this.opaque = isOpaque;
	}

}