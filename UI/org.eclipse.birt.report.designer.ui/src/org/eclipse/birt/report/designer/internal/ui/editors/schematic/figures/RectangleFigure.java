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
	@Override
	protected void fillShape(Graphics graphics) {
		Rectangle bounds = getBounds().getCopy();
		Border border = getBorder();
		if (border != null) {
			bounds = bounds.crop(border.getInsets(null));
		}
		if (isOpaque()) {
			graphics.fillRectangle(bounds);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Shape#outlineShape(Graphics)
	 */
	@Override
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
	@Override
	public boolean isOpaque() {
		return opaque;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.IFigure#setOpaque(boolean)
	 */
	@Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(isOpaque);
		this.opaque = isOpaque;
	}

}
