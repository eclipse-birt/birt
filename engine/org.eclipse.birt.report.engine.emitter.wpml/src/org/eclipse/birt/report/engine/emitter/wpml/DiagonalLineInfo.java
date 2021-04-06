/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.ArrayList;

public class DiagonalLineInfo {

	private static final double CELL_MARGIN_COMPENSATION = 5.4;
	private int diagonalCount = -1;
	private String diagonalStyle = null;
	// unit:point
	private int diagonalWidth = 0;
	private int antiDiagonalCount = -1;
	private String antiDiagonalStyle = null;
	// unit:point
	private int antiDiagonalWidth = 0;
	private String color = null;

	// unit:point
	private static final double DEFAULT_COORDSIZEX = 100;
	private static final double DEFAULT_COORDSIZEY = 100;
	private double width = DEFAULT_COORDSIZEX;
	private double height = DEFAULT_COORDSIZEY;
	private double coordoriginX = 0;
	private double coordoriginY = 0;

	public void setDiagonalLine(int diagonalCount, String diagonalStyle, int diagonalWidth) {
		if (diagonalCount > 3) {
			this.diagonalCount = 3;
		} else {
			this.diagonalCount = diagonalCount;
		}
		this.diagonalStyle = diagonalStyle;
		this.diagonalWidth = diagonalWidth;
	}

	public void setAntidiagonalLine(int antidiagonalCount, String antidiagonalStyle, int antidiagonalWidth) {
		this.antiDiagonalCount = antidiagonalCount;
		this.antiDiagonalStyle = antidiagonalStyle;
		this.antiDiagonalWidth = antidiagonalWidth;
	}

	public void setCoordinateSize(double coordinateSizeX, double coordinateSizeY) {
		if (coordinateSizeX != 0)
			this.width = coordinateSizeX;
		if (coordinateSizeY != 0)
			this.height = coordinateSizeY;
	}

	public void setCoordinateOrigin(int coordinateOriginX, int coordinateOriginY) {
		this.coordoriginX = coordinateOriginX;
		this.coordoriginY = coordinateOriginY;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public ArrayList<Line> getDiagonalLine() {
		ArrayList<Line> diagonalLine = new ArrayList<Line>();
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

	public ArrayList<Line> getAntidiagonalLine() {
		ArrayList<Line> antiDiagonalLine = new ArrayList<Line>();
		int num = antiDiagonalCount >> 1;
		double x = 2d / (antiDiagonalCount + 1) * width;
		double y = 2d / (antiDiagonalCount + 1) * height;
		if ((antiDiagonalCount & 1) == 1) {
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

	public int getDiagonalNumber() {
		return diagonalCount;
	}

	public int getAntiDiagonalNumber() {
		return antiDiagonalCount;
	}

	public String getDiagonalStyle() {
		return diagonalStyle;
	}

	public String getAntiDiagonalStyle() {
		return antiDiagonalStyle;
	}

	public double getDiagonalLineWidth() {
		return diagonalWidth;
	}

	public double getAntiDiagonalLineWidth() {
		return antiDiagonalWidth;
	}

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

		public double getXCoordinateFrom() {
			return xCoordinateFrom;
		}

		public double getYCoordinateFrom() {
			return yCoordinateFrom;
		}

		public double getXCoordinateTo() {
			return xCoordinateTo;
		}

		public double getYCoordinateTo() {
			return yCoordinateTo;
		}
	}
}
