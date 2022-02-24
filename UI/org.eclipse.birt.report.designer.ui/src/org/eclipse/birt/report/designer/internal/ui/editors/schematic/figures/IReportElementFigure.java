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

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public interface IReportElementFigure {

	/**
	 * @return The Image that this Figure displays
	 */
	Image getImage();

	/**
	 * Sets the alignment of the Image within this Figure. The alignment comes into
	 * play when the ImageFigure is larger than the Image. The alignment could be
	 * any valid combination of the following:
	 * 
	 * <UL>
	 * <LI>PositionConstants.NORTH</LI>
	 * <LI>PositionConstants.SOUTH</LI>
	 * <LI>PositionConstants.EAST</LI>
	 * <LI>PositionConstants.WEST</LI>
	 * <LI>PositionConstants.CENTER or PositionConstants.NONE</LI>
	 * </UL>
	 * 
	 * @param flag A constant indicating the alignment
	 */
	void setAlignment(int flag);

	/**
	 * Sets the position of the Image within this Figure.
	 * 
	 * @param point The position of the image to be displayed.
	 */
	void setPosition(Point point);

	/**
	 * Sets the repeat of the Image within this Figure. The repeat could be any
	 * valid combination of the following:
	 * 
	 * <UL>
	 * <LI>no_repeat:0</LI>
	 * <LI>repeat_x:1</LI>
	 * <LI>repeat_y:2</LI>
	 * <LI>repeat:3</LI>
	 * </UL>
	 * 
	 * @param flag A constant indicating the repeat.
	 */
	void setRepeat(int flag);

	/**
	 * Sets the Image that this ImageFigure displays.
	 * <p>
	 * IMPORTANT: Note that it is the client's responsibility to dispose the given
	 * image.
	 * 
	 * @param image The Image to be displayed. It can be <code>null</code>.
	 */
	void setImage(Image image);

	/**
	 * Sets the margin of current figure.
	 * 
	 * @param margin
	 */
	void setMargin(Insets margin);

	/**
	 * Returns the margin of current figure.
	 * 
	 * @return
	 */
	Insets getMargin();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.IFigure#setLocation(org.eclipse.draw2d.geometry.Point)
	 */
	void setLocation(Point p);
}
