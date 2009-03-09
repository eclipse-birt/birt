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

import org.eclipse.birt.report.engine.ir.DimensionType;

public class DiagonalLineInfo
{

	private int diagonalCount = -1;
	private String diagonalStyle = null;
	private DimensionType diagonalWidth = null;
	private int antiDiagonalCount = -1;
	private String antiDiagonalStyle = null;
	private DimensionType antiDiagonalWidth = null;
	private String color = null;

	// point
	private final double DEFAULT_COORDSIZEX = 100;
	private final double DEFAULT_COORDSIZEY = 100;
	private double width = DEFAULT_COORDSIZEX;
	private double height = DEFAULT_COORDSIZEY;
	private double coordoriginX = 0;
	private double coordoriginY = 0;

	public void setDiagonalLine( int diagonalCount, String diagonalStyle,
			DimensionType diagonalWidth )
	{
		this.diagonalCount = diagonalCount;
		this.diagonalStyle = diagonalStyle;
		this.diagonalWidth = diagonalWidth;
	}

	public void setAntidiagonalLine( int antidiagonalCount,
			String antidiagonalStyle, DimensionType antidiagonalWidth )
	{
		this.antiDiagonalCount = antidiagonalCount;
		this.antiDiagonalStyle = antidiagonalStyle;
		this.antiDiagonalWidth = antidiagonalWidth;
	}

	public void setCoordinateSize( double coordinateSizeX,
			double coordinateSizeY )
	{
		if ( coordinateSizeX != 0 )
			this.width = coordinateSizeX;
		if ( coordinateSizeY != 0 )
			this.height = coordinateSizeY;
	}

	public void setCoordinateOrigin( int coordinateOriginX,
			int coordinateOriginY )
	{
		this.coordoriginX = coordinateOriginX;
		this.coordoriginY = coordinateOriginY;
	}

	public void setColor( String color )
	{
		this.color = color;
	}

	public String getColor( )
	{
		return color;
	}

	public ArrayList<Line> getDiagonalLine( )
	{
		ArrayList<Line> diagonalLine = new ArrayList<Line>( );
		int num = diagonalCount >> 1;
		double x = 2d / ( diagonalCount + 1 ) * width;
		double y = 2d / ( diagonalCount + 1 ) * height;

		if ( diagonalCount % 2 == 1 )
		{
			diagonalLine.add( new Line( coordoriginX, coordoriginY,
					coordoriginX + width, coordoriginY + height ) );
		}
		for ( int i = 1; i <= num; i++ )
		{
			diagonalLine
					.add( new Line( coordoriginX + width - i * x, coordoriginY,
							coordoriginX + width, coordoriginY + height ) );
			diagonalLine.add( new Line( coordoriginX, coordoriginY + height - i
					* y, coordoriginX + width, coordoriginY + height ) );
		}
		return diagonalLine;
	}

	public ArrayList<Line> getAntidiagonalLine( )
	{
		ArrayList<Line> antiDiagonalLine = new ArrayList<Line>( );
		int num = antiDiagonalCount >> 1;
		double x = 2d / ( antiDiagonalCount + 1 ) * width;
		double y = 2d / ( antiDiagonalCount + 1 ) * height;
		if ( antiDiagonalCount % 2 == 1 )
		{
			antiDiagonalLine
					.add( new Line( coordoriginX, coordoriginY + height,
							coordoriginX + width, coordoriginY ) );
		}
		for ( int i = 1; i <= num; i++ )
		{
			antiDiagonalLine
					.add( new Line( coordoriginX, coordoriginY + height,
							coordoriginX + i * x, coordoriginY ) );
			antiDiagonalLine.add( new Line( coordoriginX,
					coordoriginY + height, coordoriginX + width, coordoriginY
							+ height - i * y ) );
		}
		return antiDiagonalLine;
	}

	public int getDiagonalNumber( )
	{
		return diagonalCount;
	}

	public int getAntiDiagonalNumber( )
	{
		return antiDiagonalCount;
	}

	public String getDiagonalStyle( )
	{
		return diagonalStyle;
	}

	public String getAntiDiagonalStyle( )
	{
		return antiDiagonalStyle;
	}

	public double getDiagonalLineWidth( )
	{
		return WordUtil.convertTo( diagonalWidth, 0 ) / 20d;
	}

	public double getAntiDiagonalLineWidth( )
	{
		return WordUtil.convertTo( antiDiagonalWidth, 0 ) / 20d;
	}

	public class Line
	{

		double xCoordinateFrom;
		double yCoordinateFrom;
		double xCoordinateTo;
		double yCoordinateTo;

		Line( double xFrom, double yFrom, double xTo, double yTo )
		{
			xCoordinateFrom = xFrom;
			yCoordinateFrom = yFrom;
			xCoordinateTo = xTo;
			yCoordinateTo = yTo;
		}

		public double getXCoordinateFrom( )
		{
			return xCoordinateFrom;
		}

		public double getYCoordinateFrom( )
		{
			return yCoordinateFrom;
		}

		public double getXCoordinateTo( )
		{
			return xCoordinateTo;
		}

		public double getYCoordinateTo( )
		{
			return yCoordinateTo;
		}
	}
}
