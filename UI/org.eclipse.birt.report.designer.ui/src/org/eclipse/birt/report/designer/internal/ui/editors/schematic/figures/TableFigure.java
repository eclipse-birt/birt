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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.TableBorder;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ViewportLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.graphics.Image;

/**
 * Presents table figure for table edit part
 */
public class TableFigure extends FreeformViewport implements IReportElementFigure {

	private static final String BORDER_TEXT = Messages.getString("TableFigure.BORDER_TEXT"); //$NON-NLS-1$

	private Image img;

	private int alignment;

	private Point position = new Point(-1, -1);

	private int repeat;

	private Insets margin = new Insets();

	private Dimension size = new Dimension();

	private Dimension propertySize = new Dimension();

	private int backgroundImageDPI = 0;

	class TableViewportLayout extends ViewportLayout {

		@Override
		protected Dimension calculatePreferredSize(IFigure figure, int wHint, int hHint) {
			getContents().invalidateTree();
			// wHint = Math.max( 0, wHint );
			// hHint = Math.max( 0, hHint );

			return ((FreeformFigure) getContents()).getFreeformExtent().getExpanded(getInsets()).union(0, 0).getSize();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.draw2d.AbstractHintLayout#isSensitiveHorizontally(org.eclipse.
		 * draw2d.IFigure)
		 */
		@Override
		protected boolean isSensitiveHorizontally(IFigure parent) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveVertically(org.eclipse.
		 * draw2d.IFigure)
		 */
		@Override
		protected boolean isSensitiveVertically(IFigure parent) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
		 */
		@Override
		public void layout(IFigure figure) {
			// Do nothing, contents updates itself.
		}
	}

	/**
	 * Get the background image dpi
	 *
	 * @return get the background image dpi
	 */
	public int getBackgroundImageDPI() {
		return backgroundImageDPI;
	}

	/**
	 * Set the background image dpi
	 *
	 * @param backgroundImageDPI background image dpi
	 */
	public void setBackgroundImageDPI(int backgroundImageDPI) {
		this.backgroundImageDPI = backgroundImageDPI;
	}

	/**
	 * Constructor
	 */
	public TableFigure() {

		SectionBorder border = new TableBorder();
		border.setIndicatorLabel(BORDER_TEXT);// name come from adapt set

		// table name throught Adapt may be set icon
		border.setIndicatorIcon(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_TABLE));
		setBorder(border);
		setLayoutManager(new TableViewportLayout());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {
		getContents().invalidate();
		return ((LayeredPane) ((LayeredPane) getContents()).getLayer(LayerConstants.PRINTABLE_LAYERS))
				.getLayer(LayerConstants.PRIMARY_LAYER).getMinimumSize(wHint, hHint);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void paintBorder(Graphics graphics) {
		// does nothing, table border layer paint it.
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	@Override
	protected void paintFigure(Graphics graphics) {
		if (isOpaque()) {
			if (getBorder() instanceof BaseBorder) {
				graphics.fillRectangle(getBounds().getCopy().crop(((BaseBorder) getBorder()).getBorderInsets()));
			} else {
				graphics.fillRectangle(getBounds());
			}
		}

		Image image = getImage();
		if (image == null) {
			return;
		}

		int x, y;
		Rectangle area = getBounds();

		// Calculates X
		if (position != null && position.x != -1) {
			x = area.x + position.x;
		} else {
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
		}

		// Calculates Y
		if (position != null && position.y != -1) {
			y = area.y + position.y;
		} else {
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
		}

		ArrayList<Point> xyList = createImageList(x, y);

		Iterator<Point> iter = xyList.iterator();
		Dimension imageSize = new Rectangle(image.getBounds()).getSize();
		while (iter.hasNext()) {
			Point point = iter.next();
			graphics.drawImage(image, 0, 0, imageSize.width, imageSize.height, point.x, point.y, size.width,
					size.height);
		}
		xyList.clear();
	}

	/**
	 * Create the list of all the images to be displayed.
	 *
	 * @param x the x-cordinator of the base image.
	 * @param y the y-cordinator of the base image.
	 * @return the list of all the images to be displayed.
	 */
	private ArrayList<Point> createImageList(int x, int y) {
		// Rectangle area = getOriginalClientArea( );
		Rectangle area = getBounds();

		ArrayList<Point> yList = new ArrayList<Point>();

		if ((repeat & ImageConstants.REPEAT_Y) == 0) {
			yList.add(new Point(x, y));
		} else {
			int i = 0;
			while (y + size.height * i + size.height > area.y) {
				yList.add(new Point(x, y + size.height * i));
				i--;
			}

			i = 1;
			while (y + size.height * i < area.y + area.height) {
				yList.add(new Point(x, y + size.height * i));
				i++;
			}
		}

		ArrayList<Point> xyList = new ArrayList<Point>();

		Iterator<Point> iter = yList.iterator();
		while (iter.hasNext()) {
			Point point = iter.next();

			if ((repeat & ImageConstants.REPEAT_X) == 0) {
				xyList.add(point);
			} else {
				int i = 0;
				while (point.x + size.width * i + size.width > area.x) {
					xyList.add(new Point(point.x + size.width * i, point.y));
					i--;
				}

				i = 1;
				while (point.x + size.width * i < area.x + area.width) {
					xyList.add(new Point(point.x + size.width * i, point.y));
					i++;
				}
			}
		}
		yList.clear();

		return xyList;
	}

	/**
	 * @return The Image that this Figure displays
	 */
	@Override
	public Image getImage() {
		return img;
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
	@Override
	public void setAlignment(int flag) {
		alignment = flag;
	}

	/**
	 * Sets the position of the Image within this Figure.
	 *
	 * @param point The position of the image to be displayed.
	 */
	@Override
	public void setPosition(Point point) {
		this.position = point;
	}

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
	@Override
	public void setRepeat(int flag) {
		this.repeat = flag;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * IReportElementFigure#getMargin()
	 */
	@Override
	public Insets getMargin() {
		return margin;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * IReportElementFigure#setMargin(org.eclipse.draw2d.geometry.Insets)
	 */
	@Override
	public void setMargin(Insets newMargin) {
		if (newMargin == null) {
			margin = new Insets();
		} else {
			margin = new Insets(newMargin);

			if (margin.left < 0) {
				margin.left = 0;
			}
			if (margin.right < 0) {
				margin.right = 0;
			}
			if (margin.top < 0) {
				margin.top = 0;
			}
			if (margin.bottom < 0) {
				margin.bottom = 0;
			}
		}
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 * <p>
	 * IMPORTANT: Note that it is the client's responsibility to dispose the given
	 * image.
	 *
	 * @param image The Image to be displayed. It can be <code>null</code>.
	 */
	@Override
	public void setImage(Image image) {
		setImage(image, 0, 0);
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 *
	 * @param image                 The Image to be displayed. It can be
	 *                              <code>null</code>.
	 * @param backGroundImageHeight height of the image
	 * @param backGroundImageWidth  width of the image
	 */
	@Override
	public void setImage(Image image, int backGroundImageHeight, int backGroundImageWidth) {
		if (img == image && propertySize.height == backGroundImageHeight
				&& propertySize.width == backGroundImageWidth) {
			return;
		}
		img = image;
		if (img != null) {
			propertySize.height = backGroundImageHeight;
			propertySize.width = backGroundImageWidth;

			if (backgroundImageDPI > 0 && backGroundImageHeight <= 0 && backGroundImageWidth > 0) {

				double inch = ((double) image.getBounds().height) / backgroundImageDPI;
				size.height = (int) MetricUtility.inchToPixel(inch);
				size.width = backGroundImageWidth;

			} else if (backgroundImageDPI > 0 && backGroundImageWidth <= 0 && backGroundImageHeight > 0) {

				double inch = ((double) image.getBounds().width) / backgroundImageDPI;
				size.width = (int) MetricUtility.inchToPixel(inch);
				size.height = backGroundImageHeight;

			} else if (backgroundImageDPI > 0 && (backGroundImageHeight <= 0 && backGroundImageWidth <= 0)) {

				double inch = ((double) image.getBounds().width) / backgroundImageDPI;
				size.width = (int) MetricUtility.inchToPixel(inch);

				inch = ((double) image.getBounds().height) / backgroundImageDPI;
				size.height = (int) MetricUtility.inchToPixel(inch);

			} else if (backGroundImageHeight > 0 && backGroundImageWidth > 0) {

				size.height = backGroundImageHeight;
				size.width = backGroundImageWidth;

			} else {
				size = new Rectangle(image.getBounds()).getSize();
			}
		} else {
			size = new Dimension();
		}
		revalidate();
		repaint();
	}

}
