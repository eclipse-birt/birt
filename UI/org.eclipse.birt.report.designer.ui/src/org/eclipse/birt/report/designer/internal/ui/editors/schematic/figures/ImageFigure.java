/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Actuate Corporation - support figure resize independence on image
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * A Figure that simply contains an Image. Use this Figure, instead of a Label,
 * when displaying Images without any accompanying text. Note that it is the
 * client's responsibility to dispose the given image. There is no "free"
 * resource management in GEF.
 */
public class ImageFigure extends ReportElementFigure implements IOutsideBorder {

	private Image img;

	private int alignment;

	private Dimension size = new Dimension();

	private boolean stretch = false;

	/**
	 * Constructor <br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 */
	public ImageFigure() {
		this(null, PositionConstants.CENTER);
	}

	/**
	 * Constructor <br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 * 
	 * @param image The Image to be displayed
	 */
	public ImageFigure(Image image) {
		this(image, PositionConstants.CENTER);
	}

	/**
	 * Constructor
	 * 
	 * @param image     The Image to be displayed
	 * @param alignment A PositionConstant indicating the alignment
	 * 
	 * @see ImageFigure#setImage(Image)
	 * @see ImageFigure#setAlignment(int)
	 */
	public ImageFigure(Image image, int alignment) {
		setImage(image);
		setAlignment(alignment);
	}

	/**
	 * @return The Image that this Figure displays
	 */
	public Image getImage() {
		return img;
	}

	/**
	 * Sets the flag if the image content is auto-stretched according to the
	 * container size. Returns this figure for convenience.
	 * 
	 * @param isStretched If the image is to be stretched.
	 */
	public void setStretched(boolean isStretched) {
		stretch = isStretched;
	}

	/**
	 * Returns the size of the Image that this Figure displays; or (0,0) if no Image
	 * has been set.
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (getBorder() != null) {
			Insets bdInsets = getBorder().getInsets(this);

			return new Dimension(size.width + bdInsets.getWidth(), size.height + bdInsets.getHeight());
		}

		return size;
	}

	private void paintStretched(Graphics g) {
		Image image = getImage();

		Rectangle area = getClientArea();
		if (area.height > 0 && area.width > 0) {
			g.drawImage(image, new Rectangle(image.getBounds()), area);
		}
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		if (isOpaque()) {
			if (getBorder() instanceof BaseBorder) {
				graphics.fillRectangle(getBounds().getCopy().crop(((BaseBorder) getBorder()).getBorderInsets()));
			} else {
				graphics.fillRectangle(getBounds());
			}
		}

		if (getImage() == null || getImage().isDisposed()) {
			return;
		}

		if (stretch) {
			paintStretched(graphics);

			return;
		}

		int x, y;
		Rectangle area = getClientArea();
		switch (alignment & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.NORTH:
			y = area.y;
			break;
		case PositionConstants.SOUTH:
			y = area.y + area.height - size.height;
			break;
		default:
			y = (area.height - size.height) / 2 + area.y;
			break;
		}
		switch (alignment & PositionConstants.EAST_WEST) {
		case PositionConstants.EAST:
			x = area.x + area.width - size.width;
			break;
		case PositionConstants.WEST:
			x = area.x;
			break;
		default:
			x = (area.width - size.width) / 2 + area.x;
			break;
		}
		graphics.drawImage(getImage(), x, y);
	}

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
	public void setAlignment(int flag) {
		alignment = flag;
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 * <p>
	 * IMPORTANT: Note that it is the client's responsibility to dispose the given
	 * image.
	 * 
	 * @param image The Image to be displayed. It can be <code>null</code>.
	 */
	public void setImage(Image image) {
		if (img == image)
			return;
		img = image;
		if (img != null)
			size = new Rectangle(image.getBounds()).getSize();
		else
			size = new Dimension();
		revalidate();
		repaint();
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#setSize(int, int)
	 */
	public void setSize(int w, int h) {
		super.setSize(w, h);
		size.width = w;
		size.height = h;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * IOutsideBorder#getOutsideBorder()
	 */
	public Border getOutsideBorder() {
		return getBorder();
	}
}
