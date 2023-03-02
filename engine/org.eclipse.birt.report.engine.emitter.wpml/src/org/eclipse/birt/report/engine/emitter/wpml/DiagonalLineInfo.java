/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.ArrayList;

/**
 * Information class of the diagonal line information
 *
 * @since 3.3
 *
 */
public class DiagonalLineInfo {

	private static final double CELL_MARGIN_COMPENSATION = 5.4;
	private int diagonalCount = -1;
	private String diagonalStyle = null;
	// unit:point
	private int diagonalWidth = 0;
	private String diagonalColor = null;

	private int antidiagonalCount = -1;
	private String antidiagonalStyle = null;
	// unit:point
	private int antidiagonalWidth = 0;
	private String antidiagonalColor = null;

	private String color = null;

	// unit:point
	private static final double DEFAULT_COORDSIZEX = 100;
	private static final double DEFAULT_COORDSIZEY = 100;
	private double width = DEFAULT_COORDSIZEX;
	private double height = DEFAULT_COORDSIZEY;
	private double coordoriginX = 0;
	private double coordoriginY = 0;

	/**
	 * Set the diagonal line details
	 *
	 * @param diagonalCount count of diagonal lines
	 * @param diagonalStyle style of the diagonal line
	 * @param diagonalWidth width of the diagonal line
	 */
	public void setDiagonalLine(int diagonalCount, String diagonalStyle, int diagonalWidth) {
		if (diagonalCount > 3) {
			this.diagonalCount = 3;
		} else {
			this.diagonalCount = diagonalCount;
		}
		this.diagonalStyle = diagonalStyle;
		this.diagonalWidth = diagonalWidth;
	}

	/**
	 * Set the antidiagonal line details
	 *
	 * @param antidiagonalCount count of diagonal lines
	 * @param antidiagonalStyle style of the antidiagonal line
	 * @param antidiagonalWidth width of the antidiagonal line
	 * @since 4.13
	 */
	public void setAntidiagonalLine(int antidiagonalCount, String antidiagonalStyle, int antidiagonalWidth) {
		if (antidiagonalCount > 3) {
			this.antidiagonalCount = 3;
		} else {
			this.antidiagonalCount = antidiagonalCount;
		}
		this.antidiagonalStyle = antidiagonalStyle;
		this.antidiagonalWidth = antidiagonalWidth;
	}

	/**
	 * Set the coordination size
	 *
	 * @param coordinateSizeX
	 * @param coordinateSizeY
	 */
	public void setCoordinateSize(double coordinateSizeX, double coordinateSizeY) {
		if (coordinateSizeX != 0) {
			this.width = coordinateSizeX;
		}
		if (coordinateSizeY != 0) {
			this.height = coordinateSizeY;
		}
	}

	/**
	 * Set the coordination origin
	 *
	 * @param coordinateOriginX
	 * @param coordinateOriginY
	 */
	public void setCoordinateOrigin(int coordinateOriginX, int coordinateOriginY) {
		this.coordoriginX = coordinateOriginX;
		this.coordoriginY = coordinateOriginY;
	}

	/**
	 * Set the color
	 *
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Get the color
	 *
	 * @return Return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Set the diagonal color of line
	 *
	 * @param color Set the diagonal color of the line
	 * @since 4.13
	 */
	public void setDiagonalColor(String color) {
		this.diagonalColor = color;
	}

	/**
	 * Get the diagonal color of line
	 *
	 * @return Return the diagonal color of the line
	 * @since 4.13
	 */
	public String getDiagonalColor() {
		return diagonalColor;
	}

	/**
	 * Set the anti-diagonal color of line
	 *
	 * @param color Set the anti-diagonal color of the line
	 * @since 4.13
	 */
	public void setAntidiagonalColor(String color) {
		this.antidiagonalColor = color;
	}

	/**
	 * Get the anti-diagonal color of line
	 *
	 * @return Return the anti-diagonal color of the line
	 * @since 4.13
	 */
	public String getAntidiagonalColor() {
		return this.antidiagonalColor;
	}

	/**
	 * Get the diagonal lines
	 *
	 * @return Return the diagonal lines
	 */
	public ArrayList<Line> getDiagonalLine() {
		ArrayList<Line> diagonalLine = new ArrayList<>();
		int num = diagonalCount >> 1;
		double x = 2d / (diagonalCount + 1) * width;
		double y = 2d / (diagonalCount + 1) * height;

		if ((diagonalCount & 1) == 1) {
			diagonalLine.add(new Line(coordoriginX - CELL_MARGIN_COMPENSATION, coordoriginY,
					coordoriginX + width - CELL_MARGIN_COMPENSATION, coordoriginY + height));
		}
		for (int i = 1; i <= num; i++) {
			diagonalLine.add(new Line(coordoriginX + width - i * x - CELL_MARGIN_COMPENSATION, coordoriginY,
					coordoriginX + width - CELL_MARGIN_COMPENSATION, coordoriginY + height));
			diagonalLine.add(new Line(coordoriginX - CELL_MARGIN_COMPENSATION, coordoriginY + height - i * y,
					coordoriginX + width - CELL_MARGIN_COMPENSATION, coordoriginY + height));
		}
		return diagonalLine;
	}

	/**
	 * Get the antidiagonal lines
	 *
	 * @return Return the antidiagonal lines
	 */
	public ArrayList<Line> getAntidiagonalLine() {
		ArrayList<Line> antiDiagonalLine = new ArrayList<>();
		int num = antidiagonalCount >> 1;
		double x = 2d / (antidiagonalCount + 1) * width;
		double y = 2d / (antidiagonalCount + 1) * height;
		if ((antidiagonalCount & 1) == 1) {
			antiDiagonalLine.add(new Line(coordoriginX - CELL_MARGIN_COMPENSATION, coordoriginY + height,
					coordoriginX + width - CELL_MARGIN_COMPENSATION, coordoriginY));
		}
		for (int i = 1; i <= num; i++) {
			antiDiagonalLine.add(new Line(coordoriginX - CELL_MARGIN_COMPENSATION, coordoriginY + height,
					coordoriginX + i * x - CELL_MARGIN_COMPENSATION, coordoriginY));
			antiDiagonalLine.add(new Line(coordoriginX - CELL_MARGIN_COMPENSATION, coordoriginY + height,
					coordoriginX + width - CELL_MARGIN_COMPENSATION, coordoriginY + height - i * y));
		}
		return antiDiagonalLine;
	}

	/**
	 * Get the diagonal number count
	 *
	 * @return Return the diagonal number count
	 */
	public int getDiagonalNumber() {
		return diagonalCount;
	}

	/**
	 * Get the antidiagonal number count
	 *
	 * @return Return the antidiagonal number count
	 */
	public int getAntidiagonalNumber() {
		return antidiagonalCount;
	}

	/**
	 * Get the diagonal style
	 *
	 * @return Return the diagonal style
	 */
	public String getDiagonalStyle() {
		return diagonalStyle;
	}

	/**
	 * Get the antidiagonal style
	 *
	 * @return Return the antidiagonal style
	 */
	public String getAntidiagonalStyle() {
		return antidiagonalStyle;
	}

	/**
	 * Get the diagonal line width
	 *
	 * @return Return the diagonal line width
	 */
	public double getDiagonalLineWidth() {
		return diagonalWidth;
	}

	/**
	 * Get the antidiagonal line width
	 *
	 * @return Return the antidiagonal line width
	 */
	public double getAntidiagonalLineWidth() {
		return antidiagonalWidth;
	}

	/**
	 * Static class the represent the line of diagonals
	 *
	 * @since 3.3
	 *
	 */
	public static class Line {

		double xCoordinateFrom;
		double yCoordinateFrom;
		double xCoordinateTo;
		double yCoordinateTo;

		Line(double xFrom, double yFrom, double xTo, double yTo) {
			xCoordinateFrom = xFrom;
			yCoordinateFrom = yFrom;
			xCoordinateTo = xTo;
			yCoordinateTo = yTo;
		}

		/**
		 * Get the x coordinate from
		 *
		 * @return Return the x coordinate from
		 */
		public double getXCoordinateFrom() {
			return xCoordinateFrom;
		}

		/**
		 * Get the y coordinate from
		 *
		 * @return Return the y coordinate from
		 */
		public double getYCoordinateFrom() {
			return yCoordinateFrom;
		}

		/**
		 * Get the x coordinate to
		 *
		 * @return Return the x coordinate to
		 */
		public double getXCoordinateTo() {
			return xCoordinateTo;
		}

		/**
		 * Get the y coordinate to
		 *
		 * @return Return the y coordinate to
		 */
		public double getYCoordinateTo() {
			return yCoordinateTo;
		}
	}
}
