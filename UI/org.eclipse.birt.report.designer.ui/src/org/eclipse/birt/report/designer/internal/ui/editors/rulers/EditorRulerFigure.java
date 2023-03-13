/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * add comment here
 *
 */
public class EditorRulerFigure extends Figure {

	private Rectangle leftSpace = new Rectangle();

	/**
	 * Conversion factor from inches to cm.
	 */

	private static final double CM_PER_INCH = 2.54;

	/**
	 * Conversion factor from inches to points.
	 */

	private static final double POINTS_PER_INCH = 72;

	/**
	 * Conversion factor from cm to points.
	 */

	private static final double MM_PER_INCH = 25.4;

	/**
	 * Conversion factor from picas to points.
	 */

	private static final double PICA_PER_INCH = 6;
	/**
	 * These fields allow the client to customize the look of the ruler.
	 */
	public int smallMarkWidth = 1;
	public int mediumMarkWidth = 3;
	public int textMargin = 3;
	// public int minPixelsBetweenMarks = 7;
	public int minPixelsBetweenMarks = 4;
	// public int minPixelsBetweenMajorMarks = 47;
	public int minPixelsBetweenMajorMarks = 35;

	private boolean drawFocus = false;

	protected Transposer transposer = new Transposer();
	protected ZoomManager zoomManager;
	/*
	 * This is an artificial border. When asked for the preferred size, the figure
	 * adds this width to its preferred width. The border is painted in the
	 * paintFigure(Graphics) method.
	 */
	protected static final int BORDER_WIDTH = 3;

	private boolean horizontal;
	protected int unit, interval, divisions;
	private double dpu1 = -1.0;

	private ZoomListener zoomListener = new ZoomListener() {

		@Override
		public void zoomChanged(double newZoomValue) {
			handleZoomChanged();
		}
	};

	public EditorRulerFigure(boolean isHorizontal, int measurementUnit) {
		setHorizontal(isHorizontal);
		setUnit(measurementUnit);
		setBackgroundColor(ColorConstants.listBackground);
		setForegroundColor(ColorConstants.listForeground);
		setOpaque(true);
		setLayoutManager(new EditorRulerLayout());
	}

	/**
	 * @return
	 */
	protected double getDPU() {
		if (dpu1 <= 0) {
			if (getUnit() == RulerProvider.UNIT_PIXELS) {
				dpu1 = 1.0;
			} else {
				dpu1 = transposer.t(new Dimension(Display.getCurrent().getDPI())).height;
				dpu1 = dpu1 / getUnitConver(getUnit());
			}
			if (zoomManager != null) {
				dpu1 = dpu1 * zoomManager.getZoom();
			}
		}
		return dpu1;
	}

	private double getUnitConver(int unit) {
		if (getUnit() == EditorRulerProvider.UNIT_CENTIMETERS) {
			return CM_PER_INCH;
		} else if (getUnit() == EditorRulerProvider.UNIT_MM) {
			return MM_PER_INCH;
		} else if (getUnit() == EditorRulerProvider.UNIT_PC) {
			return PICA_PER_INCH;
		} else if (getUnit() == EditorRulerProvider.UNIT_PT) {
			return POINTS_PER_INCH;
		}

		return 1.0;
	}

	/**
	 * @return
	 */
	public boolean getDrawFocus() {
		return drawFocus;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension prefSize = new Dimension();
		if (isHorizontal()) {
			prefSize.height = (textMargin * 2) + BORDER_WIDTH + FigureUtilities.getFontMetrics(getFont()).getAscent();
		} else {
			prefSize.width = (textMargin * 2) + BORDER_WIDTH + FigureUtilities.getFontMetrics(getFont()).getAscent();
		}
		return prefSize;
	}

	/**
	 * Gets the unit.
	 *
	 * @return
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 *
	 */
	protected void handleZoomChanged() {
		dpu1 = -1.0;
		repaint();
		layout();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#invalidate()
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		dpu1 = -1.0;
	}

	/**
	 * @return
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void paintFigure(Graphics graphics) {

		// set
		/*
		 * @TODO:Pratik maybe you can break this method into a few methods. that might
		 * make it a little easier to read and understand. plus, sub-classes could
		 * customize certain parts.
		 */
		setIntervalForPaint();
		double dotsPerUnit = getDPU();
		Rectangle clip = transposer.t(graphics.getClip(Rectangle.SINGLETON));
		Rectangle figClientArea = transposer.t(getClientArea());
		// Use the x and width of the client area, but the y and height of the
		// clip as the
		// bounds of the area which is to be repainted. This will increase
		// performance as the
		// entire ruler will not be repainted everytime.
		Rectangle clippedBounds = clip;
		clippedBounds.x = figClientArea.x;
		clippedBounds.width = figClientArea.width - BORDER_WIDTH;

		// Paint the background
		if (isOpaque()) {
			graphics.fillRectangle(transposer.t(clippedBounds));
		}

		/*
		 * A major mark is one that goes all the way from the left edge to the right
		 * edge of a ruler and for which a number is displayed. Determine the minimum
		 * number of pixels that are to be left between major marks. This will, in turn,
		 * help determine how many units are to be displayed per major mark. A major
		 * mark should have at least enough pixels to display the text and its padding.
		 * We take into the consideration the max of text's width and height so that for
		 * horizontal and vertical rulers that are of the same height, the number of
		 * units per major mark is the same.
		 */
		int unitsPerMajorMark = (int) (minPixelsBetweenMajorMarks / dotsPerUnit);
		// unitsPerMajorMark = 20;
		if (minPixelsBetweenMajorMarks % dotsPerUnit != 0.0) {
			unitsPerMajorMark++;
		}
		if (interval > 0) {
			/*
			 * If the client specified how many units are to be displayed per major mark,
			 * use that. If, however, showing that many units wouldn't leave enough room for
			 * the text, than take its smallest multiple that would leave enough room.
			 */
			int intervalMultiple = interval;
			while (intervalMultiple < unitsPerMajorMark) {
				intervalMultiple += interval;
			}
			unitsPerMajorMark = intervalMultiple;
		} else if (unitsPerMajorMark != 1 && unitsPerMajorMark % 2 != 0) {
			// if the number of units per major mark is calculated dynamically,
			// ensure that
			// it is an even number.
			unitsPerMajorMark++;
		}

		/*
		 * divsPerMajorMark indicates the number of divisions that a major mark should
		 * be divided into. for eg., a value of 2 would mean that a major mark would be
		 * shown as having two parts. that means that there would be a marker showing
		 * the beginning and end of the major marker and another right in the middle.
		 */
		int divsPerMajorMark;
		if (divisions > 0 && dotsPerUnit * unitsPerMajorMark / divisions >= minPixelsBetweenMarks) {
			/*
			 * If the client has specified the number of divisions per major mark, use that
			 * unless it would cause the minimum space between marks to be less than
			 * minPixelsBetweenMarks
			 */
			divsPerMajorMark = divisions;
		} else {
			/*
			 * If the client hasn't specified the number of divisions per major mark or the
			 * one that the client has specified is invalid, then calculate it dynamically.
			 * This algorithm will try to display 10 divisions per CM, and 16 per INCH.
			 * However, if that puts the marks too close together (i.e., the space between
			 * them is less than minPixelsBetweenMarks), then it keeps decreasing the number
			 * of divisions by a factor of 2 until there is enough space between them.
			 */
			divsPerMajorMark = 2;
			if (getUnit() == RulerProvider.UNIT_CENTIMETERS) {
				divsPerMajorMark = 10;
			} else if (getUnit() == RulerProvider.UNIT_INCHES) {
				divsPerMajorMark = 8;
			}
			// divsPerMajorMark = getPerMajorMark(getUnit());
			while (dotsPerUnit * unitsPerMajorMark / divsPerMajorMark < minPixelsBetweenMarks) {
				divsPerMajorMark /= 2;
				if (divsPerMajorMark == 0) {
					break;
				}
			}
			// This should never happen unless the client has specified a
			// minPixelsBetweenMarks that is larger than
			// minPixelsBetweenMajorMarks (which
			// is calculated using the text's size -- size of the largest number
			// to be
			// displayed).
			if (divsPerMajorMark == 0) {
				divsPerMajorMark = 1;
			}
		}

		/*
		 * mediumMarkerDivNum is used to determine which mark (line drawn to indicate a
		 * point on the ruler) in a major mark will be of medium size. If its value is 1
		 * then every mark will be of medium size. If its value is 5, then every 5th
		 * mark will be of medium size (the rest being of small size).
		 */
		int mediumMarkerDivNum = 1;
		switch (divsPerMajorMark) {
		case 20:
		case 10:
		case 5:
			mediumMarkerDivNum = 5;
			break;
		case 16:
		case 8:
			mediumMarkerDivNum = 4;
			break;
		case 4:
			mediumMarkerDivNum = 2;
			break;
		case 2:
			mediumMarkerDivNum = 1;
		}

		Rectangle leftRect = transposer.t(getScaleLeftSpace());
		int leftMargin = leftRect.y;
		/*
		 * dotsPerDivision = number of pixels between each mark = number of pixels in a
		 * division
		 */
		double dotsPerDivision = dotsPerUnit * unitsPerMajorMark / divsPerMajorMark;
		/*
		 * startMark is the division/mark from which we are going to start painting. It
		 * should be the last major mark (one for which a number is displayed) that is
		 * before the top of the clip rectangle.
		 */
		int startMark = (int) ((clippedBounds.y - leftMargin) / (dotsPerUnit * unitsPerMajorMark)) * divsPerMajorMark;
		if (clippedBounds.y < 0) {
			// -2 / 10 = 0, not -1. so, if the top of the clip is negative, we
			// need to move
			// the startMark back by a whole major mark.
			startMark -= divsPerMajorMark;
		}
		// endMark is the first non-visible mark (doesn't have to be a major
		// mark) that is
		// beyond the end of the clip region
		int endMark = (int) (((clippedBounds.y + clippedBounds.height - leftMargin) / dotsPerDivision)) + 1;
		int leading = FigureUtilities.getFontMetrics(getFont()).getLeading();
		Rectangle forbiddenZone = new Rectangle();
		for (int div = startMark; div <= endMark; div++) {
			// y is the vertical position of the mark
			if (leftRect.bottom() <= clippedBounds.bottom()) {
				// continue;
			}
			int y = (int) (div * dotsPerDivision);
			if (div % divsPerMajorMark == 0) {
				String num = "" + (div / divsPerMajorMark) * unitsPerMajorMark; //$NON-NLS-1$
				if (isHorizontal()) {
					Dimension numSize = FigureUtilities.getStringExtents(num, getFont());
					/*
					 * FigureUtilities is increasing the width reported by GC by 1, so we remove it
					 * here. Also, if the actual width (the one reported by the GC) is even, we want
					 * to increase it by 1. This will ensure that when marks marks are erased
					 * because they are too close to the number, they are erased from both sides of
					 * that number.
					 */
					if (numSize.width % 2 == 0) {
						numSize.width--;
					}
					Point textLocation = new Point(y - (numSize.width / 2) + leftMargin,
							clippedBounds.x + textMargin - leading);
					forbiddenZone.setLocation(textLocation);
					forbiddenZone.setSize(numSize);
					forbiddenZone.expand(1, 1);
					graphics.fillRectangle(forbiddenZone);
					// Uncomment the following line of code if you want to see a
					// line at
					// the exact position of the major mark
					// graphics.drawLine(y, clippedBounds.x, y, clippedBounds.x
					// + clippedBounds.width);

					// if (canDrawNumber(num, textLocation, graphics))
					{
						graphics.drawText(num, textLocation);
					}
				} else {
					Image numImage = ImageUtilities.createRotatedImageOfString(num, getFont(), getForegroundColor(),
							getBackgroundColor());
					Point textLocation = new Point(clippedBounds.x + textMargin,
							y - (numImage.getBounds().height / 2) + leftMargin);

					forbiddenZone.setLocation(textLocation);
					forbiddenZone.setSize(numImage.getBounds().width, numImage.getBounds().height);
					forbiddenZone.expand(1, 1 + (numImage.getBounds().height % 2 == 0 ? 1 : 0));
					graphics.fillRectangle(forbiddenZone);

					// if (canDrawNumber(num, new Point(textLocation.x,
					// textLocation.y+getScaleLeftSpace().y - numImage.getBounds( ).height),
					// graphics))
					{
						graphics.drawImage(numImage, textLocation);
					}
					numImage.dispose();
				}
			} else if ((div % divsPerMajorMark) % mediumMarkerDivNum == 0) {
				// this is a medium mark, so its length should be longer than
				// the small marks
				Point start = transposer
						.t(new Point((clippedBounds.getRight().x - mediumMarkWidth) / 2, y + leftMargin));
				Point end = transposer.t(new Point(
						((clippedBounds.getRight().x - mediumMarkWidth) / 2) + mediumMarkWidth, y + leftMargin));
				if (!forbiddenZone.contains(start)) {
					graphics.drawLine(start, end);
				}
			} else {
				// small mark
				Point start = transposer
						.t(new Point((clippedBounds.getRight().x - smallMarkWidth) / 2, y + leftMargin));
				Point end = transposer.t(new Point(((clippedBounds.getRight().x - smallMarkWidth) / 2) + smallMarkWidth,
						y + leftMargin));
				if (!forbiddenZone.contains(start)) {
					graphics.drawLine(start, end);
				}
			}
		}
		// paint the border
		clippedBounds.expand(BORDER_WIDTH, 0);
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		graphics.drawLine(transposer.t(clippedBounds.getTopRight().translate(-1, -1)),
				transposer.t(clippedBounds.getBottomRight().translate(-1, -1)));
		Color c = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

		graphics.setBackgroundColor(c);
//		Rectangle rect = new Rectangle( 0, 0, clippedBounds.height,
//				leftMargin - 2 );
		Rectangle rect = new Rectangle(0, 0, transposer.t(getBounds()).width, leftMargin);

		rect = transposer.t(rect);
		graphics.fillRectangle(rect);

		graphics.fillRectangle(getEndRect(graphics.getClip(Rectangle.SINGLETON)));
	}

	private Rectangle getEndRect(Rectangle clip) {
		Rectangle rect = getScaleLeftSpace();

		Rectangle retValue = new Rectangle();
		if (rect.equals(new Rectangle())) {
			return retValue;
		}
		if (isHorizontal()) {
			retValue.height = clip.height;
			retValue.y = clip.y;
			if (clip.right() > rect.right()) {
				retValue.x = rect.right();
				retValue.width = clip.right() - rect.right();

			}
		} else {
			retValue.width = clip.width;
			retValue.x = clip.x;
			if (clip.bottom() > rect.bottom()) {
				retValue.y = rect.bottom();
				retValue.height = clip.bottom() - rect.bottom();

			}
		}
		return retValue;
	}

	/**
	 * @param drawFocus
	 */
	public void setDrawFocus(boolean drawFocus) {
		if (this.drawFocus != drawFocus) {
			this.drawFocus = drawFocus;
			repaint();
		}
	}

	/**
	 * @param isHorizontal
	 */
	public void setHorizontal(boolean isHorizontal) {
		horizontal = isHorizontal;
		transposer.setEnabled(isHorizontal);
	}

	/**
	 * Allows the client to set the number of units to be displayed per major mark,
	 * and the number of divisions to be shown per major mark.
	 *
	 * A number on the ruler is considered to be a major mark.
	 *
	 * @param unitsPerMajorMark     if less than 1, it will be ignored; if there is
	 *                              not enough space to display that many units per
	 *                              major mark, its smallest multiple that leaves
	 *                              enough room will be used.
	 * @param divisionsPerMajorMark if less than 1, it will be ignored; if
	 *                              displaying that many divisions does not leave
	 *                              enough room between marks, it will be ignored.
	 *
	 */
	public void setInterval(int unitsPerMajorMark, int divisionsPerMajorMark) {
		// don't use
		interval = unitsPerMajorMark;
		divisions = divisionsPerMajorMark;
		repaint();
	}

	private void setIntervalForPaint() {
		double zoom = zoomManager.getZoom();
		if (unit == EditorRulerProvider.UNIT_INCHES) {
			interval = 1;
			divisions = 8;
		} else if (unit == EditorRulerProvider.UNIT_CENTIMETERS) {
			if (zoom <= 0.75) {
				interval = 2;
				divisions = 4;
			} else {
				interval = 1;
				divisions = 4;
			}
		}

		else if (unit == EditorRulerProvider.UNIT_MM) {
			if (zoom <= 0.5) {
				interval = 20;
				divisions = 2;
			} else if (zoom <= 0.75) {
				interval = 20;
				divisions = 4;
			} else {
				interval = 20;
				divisions = 8;
			}
		} else if (unit == EditorRulerProvider.UNIT_PT) {
			if (zoom <= 0.5) {
				interval = 36;
				divisions = 1;
			} else if (zoom <= 0.75) {
				interval = 36;
				divisions = 2;
			} else {
				interval = 36;
				divisions = 6;
			}
		} else if (unit == EditorRulerProvider.UNIT_PC) {
			interval = 6;
			divisions = 6;
		} else {
			interval = 0;
			divisions = 0;
		}
	}

	/**
	 * @param newUnit
	 */
	public void setUnit(int newUnit) {
		if (unit != newUnit) {
			unit = newUnit;
			dpu1 = -1.0;
			repaint();
		}
	}

	/**
	 * @param leftSpace The leftSpace to set.
	 */
	public void setLeftSpace(Rectangle leftSpace) {
		this.leftSpace = leftSpace;
	}

	/**
	 * @return
	 */
	public Rectangle getLeftSpace() {
		return leftSpace;
	}

	/**
	 * @return
	 */
	public Rectangle getScaleLeftSpace() {
		PrecisionRectangle rect = new PrecisionRectangle(getLeftSpace());
		rect.performScale(zoomManager.getZoom());
		return rect;
	}

	/**
	 * @param manager
	 */
	public void setZoomManager(ZoomManager manager) {
		if (zoomManager != manager) {
			if (zoomManager != null) {
				zoomManager.removeZoomListener(zoomListener);
			}
			zoomManager = manager;
			if (zoomManager != null) {
				zoomManager.addZoomListener(zoomListener);
			}
		}
	}

}
