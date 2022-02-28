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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * Figure related utilities.
 *
 */
public class ReportFigureUtilities {

	private static Polygon trianglePolygon = new Polygon();

	private static final Color[] BEVEL_COLOR = {

			ColorManager.getColor(212, 208, 200), ColorManager.getColor(255, 255, 255),
			ColorManager.getColor(64, 64, 64), ColorManager.getColor(128, 128, 128), };

	private static final Color[] SHADOW_COLOR = {

			ColorManager.getColor(195, 195, 195), ColorManager.getColor(210, 210, 210),
			ColorManager.getColor(225, 225, 225), ColorManager.getColor(240, 240, 240),

			ColorManager.getColor(207, 207, 207), ColorManager.getColor(219, 219, 219),
			ColorManager.getColor(231, 231, 231), ColorManager.getColor(243, 243, 243),

			ColorManager.getColor(219, 219, 219), ColorManager.getColor(228, 228, 228),
			ColorManager.getColor(237, 237, 237), ColorManager.getColor(246, 246, 246),

			ColorManager.getColor(231, 231, 231), ColorManager.getColor(237, 237, 237),
			ColorManager.getColor(243, 243, 243), ColorManager.getColor(249, 249, 249),

			ColorManager.getColor(243, 243, 243), ColorManager.getColor(246, 246, 246),
			ColorManager.getColor(249, 249, 249), ColorManager.getColor(252, 252, 252), };

	/**
	 * Gets points to construct a triangle by give height and central point.
	 *
	 * @param triangleHeight
	 * @param triangleCenter
	 * @return
	 */
	public static int[] getTrianglePoints(int triangleHeight, Point triangleCenter) {
		int points[] = new int[6];
		int xOff = (int) (triangleHeight * 2 * Math.tan(Math.PI / 6));
		points[0] = triangleCenter.x - xOff;
		points[1] = triangleCenter.y - triangleHeight / 2;
		points[2] = triangleCenter.x + xOff;
		points[3] = triangleCenter.y - triangleHeight / 2;
		points[4] = triangleCenter.x;
		points[5] = triangleCenter.y + triangleHeight / 2;

		return points;
	}

	/**
	 * Paints a triangle using given height and central point.
	 *
	 * @param graphics
	 * @param triangleHeight
	 * @param triangleCenter
	 */
	public static void paintTriangle(Graphics graphics, int triangleHeight, Point triangleCenter) {
		graphics.fillPolygon(getTrianglePoints(triangleHeight, triangleCenter));
	}

	/**
	 * Tests if the given point is inside the triangle constructed by given height
	 * and central point.
	 *
	 * @param triangleCenter
	 * @param triangleHeight
	 * @param pt
	 * @return
	 */
	public static boolean isInTriangle(Point triangleCenter, int triangleHeight, Point pt) {
		trianglePolygon.setPoints(new PointList(getTrianglePoints(triangleHeight, triangleCenter)));

		return trianglePolygon.containsPoint(pt);
	}

	/**
	 * Paints double arrow on list element
	 *
	 * @param graphics
	 * @param height
	 * @param center
	 */
	public static void paintDoubleArrow(Graphics graphics, int height, Point center) {
		int points[] = new int[6];
		int xOff = (int) (height * Math.tan(Math.PI / 4) / 2);
		points[0] = center.x - xOff;
		points[1] = center.y - height / 2;
		points[2] = center.x;
		points[3] = center.y;
		points[4] = center.x - xOff;
		points[5] = center.y + height / 2;

		graphics.drawPolyline(points);

		incXOffset(points, 1);
		graphics.drawPolyline(points);

		incXOffset(points, 3);
		graphics.drawPolyline(points);

		incXOffset(points, 1);
		graphics.drawPolyline(points);
	}

	/**
	 * Paints expand handle
	 *
	 * @param graphics
	 * @param height
	 * @param center
	 * @param collapsed
	 */
	public static void paintExpandHandle(Graphics graphics, int height, Point center, boolean collapsed) {
		graphics.drawRectangle(center.x - height / 2, center.y - height / 2, height, height);

		graphics.drawLine(center.x - height / 2 + 2, center.y, center.x + height / 2 - 2, center.y);

		if (collapsed) {
			graphics.drawLine(center.x, center.y - height / 2 + 2, center.x, center.y + height / 2 - 2);
		}
	}

	private static void incXOffset(int[] points, int offset) {
		for (int i = 0; i < points.length; i += 2) {
			points[i] += offset;
		}
	}

	/**
	 * Paints bevel
	 *
	 * @param graphics
	 * @param area
	 * @param rised
	 */
	public static void paintBevel(Graphics graphics, Rectangle area, boolean rised) {
		graphics.setForegroundColor(BEVEL_COLOR[rised ? 0 : 2]);
		graphics.drawLine(area.x, area.y, area.x, area.y + area.height - 2);
		graphics.drawLine(area.x, area.y, area.x + area.width - 2, area.y);

		graphics.setForegroundColor(BEVEL_COLOR[rised ? 1 : 3]);
		graphics.drawLine(area.x + 1, area.y + 1, area.x + 1, area.y + area.height - 3);
		graphics.drawLine(area.x + 1, area.y + 1, area.x + area.width - 3, area.y + 1);

		graphics.setForegroundColor(BEVEL_COLOR[rised ? 2 : 0]);
		graphics.drawLine(area.x, area.y + area.height - 1, area.x + area.width - 1, area.y + area.height - 1);
		graphics.drawLine(area.x + area.width - 1, area.y, area.x + area.width - 1, area.y + area.height - 1);

		graphics.setForegroundColor(BEVEL_COLOR[rised ? 3 : 1]);
		graphics.drawLine(area.x + 1, area.y + area.height - 2, area.x + area.width - 2, area.y + area.height - 2);
		graphics.drawLine(area.x + area.width - 2, area.y + 1, area.x + area.width - 2, area.y + area.height - 2);
	}

	/**
	 * Paints shadow
	 *
	 * @param graphics
	 * @param area
	 * @param drawTopLeft
	 */
	public static void paintShadow(Graphics graphics, Rectangle area, boolean drawTopLeft) {
		int inc = 1;
		int xoff = 4;
		int yoff = 4;
		int xlpos = area.x - 1;
		int xpos = area.x + area.width + 1;
		int ylpos = area.y - 1;
		int ypos = area.y + area.height + 1;

		if (drawTopLeft) {
			inc = -1;
			xoff = -4;
			yoff = -4;
			xlpos = area.x + area.width + 1;
			xpos = area.x - 1;
			ylpos = area.y + area.height + 1;
			ypos = area.y - 1;
		}

		{
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					graphics.setForegroundColor(SHADOW_COLOR[i * 4 + 4 + j]);
					graphics.drawPoint(xpos + i * inc, ypos + j * inc);
				}
			}
		}

		if (area.width > 4) {
			{
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						graphics.setForegroundColor(SHADOW_COLOR[i * 4 + 4 + j]);
						graphics.drawPoint(xlpos + 8 * inc - i * inc, ypos + j * inc);
					}
				}
			}

			if (area.width > 8) {
				for (int i = 0; i < 4; i++) {
					graphics.setForegroundColor(SHADOW_COLOR[i]);
					graphics.drawLine(area.x + 4 + xoff, ypos + inc * i, area.x + area.width + xoff - 4,
							ypos + inc * i);
				}
			}
		}

		if (area.height > 4) {
			{
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						graphics.setForegroundColor(SHADOW_COLOR[i * 4 + 4 + j]);
						graphics.drawPoint(xpos + i * inc, ylpos + 8 * inc - j * inc);
					}
				}
			}

			if (area.height > 8) {
				for (int i = 0; i < 4; i++) {
					graphics.setForegroundColor(SHADOW_COLOR[i]);
					graphics.drawLine(xpos + inc * i, area.y + 4 + yoff, xpos + inc * i,
							area.y + area.height + yoff - 4);
				}
			}
		}
	}

	public static Figure createToolTipFigure(String toolTipText, String direction, String textAlign) {
		if (toolTipText == null) {
			return null;
		}

		LabelFigure tooltip = new LabelFigure();

		// bidi_hcg start
		if (DesignChoiceConstants.BIDI_DIRECTION_RTL.equals(direction)) {
			tooltip.setDirection(direction);
		} else if (DesignChoiceConstants.BIDI_DIRECTION_LTR.equals(direction)) {
			tooltip.setDirection(direction);
		}

		tooltip.setTextAlign(textAlign);
		// bidi_hcg end

		tooltip.setText(toolTipText);
		tooltip.setBorder(new MarginBorder(0, 2, 0, 2));

		return tooltip;
	}
}
