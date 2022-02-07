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

package org.eclipse.birt.report.engine.emitter.html.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

/**
 * 
 */

public class DiagonalLineImage {

	/**
	 * The number of the diagonal line.
	 */
	private int diagonalNumber = -1;
	/**
	 * The style of the diagonal line.
	 */
	private String diagonalStyle = null;
	/**
	 * The width of the diagonal line.
	 */
	private DimensionType diagonalWidth = null;
	/**
	 * The number of the antidiagonal line.
	 */
	private int antidiagonalNumber = -1;
	/**
	 * The color of the diagonal line.
	 */
	private String diagonalColor = null;
	/**
	 * The style of the antidiagonal line.
	 */
	private String antidiagonalStyle = null;
	/**
	 * The width of the antidiagonal line.
	 */
	private DimensionType antidiagonalWidth = null;
	/**
	 * The color of the antidiagonal line.
	 */
	private String antidiagonalColor = null;

	/**
	 * The font color. Default value is black;
	 */
	private Color color = null;
	/**
	 * Image width.
	 */
	DimensionType imageWidth = null;
	/**
	 * Image height.
	 */
	DimensionType imageHeight = null;
	/**
	 * Image DPI.
	 */
	protected int imageDpi = -1;

	Stroke originStroke = null;

	/**
	 * Default image pixel width.
	 */
	private static int DEFAULT_IMAGE_PX_WIDTH = 200;
	/**
	 * Default image pixel height.
	 */
	private static int DEFAULT_IMAGE_PX_HEIGHT = 200;
	/**
	 * Default image DPI.
	 */
	private static int DEFAULT_IMAGE_DPI = 96;

	public void setDiagonalLine(int diagonalNumber, String diagonalStyle, DimensionType diagonalWidth,
			String diagonalColor) {
		this.diagonalNumber = diagonalNumber;
		this.diagonalStyle = diagonalStyle;
		this.diagonalWidth = diagonalWidth;
		this.diagonalColor = diagonalColor;
	}

	public void setAntidiagonalLine(int antidiagonalNumber, String antidiagonalStyle, DimensionType antidiagonalWidth,
			String antidiagonalColor) {
		this.antidiagonalNumber = antidiagonalNumber;
		this.antidiagonalStyle = antidiagonalStyle;
		this.antidiagonalWidth = antidiagonalWidth;
		this.antidiagonalColor = antidiagonalColor;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setImageDpi(int dpi) {
		this.imageDpi = dpi;
	}

	public void setImageSize(DimensionType imageWidth, DimensionType imageHeight) {
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	public byte[] drawImage() throws IOException {
		if (diagonalNumber <= 0 && antidiagonalNumber <= 0) {
			return null;
		}
		if (imageDpi < 0) {
			imageDpi = DEFAULT_IMAGE_DPI;
		}
		int imagePXWidth = HTMLEmitterUtil.getDimensionPixelValue(imageWidth, imageDpi);
		int imagePXHeight = HTMLEmitterUtil.getDimensionPixelValue(imageHeight, imageDpi);
		if (imagePXWidth <= 0) {
			imagePXWidth = DEFAULT_IMAGE_PX_WIDTH;
		}
		if (imagePXHeight <= 0) {
			imagePXHeight = DEFAULT_IMAGE_PX_HEIGHT;
		}

		int diagonalPXWidth = HTMLEmitterUtil.getDimensionPixelValue(diagonalWidth, imageDpi);
		if (diagonalPXWidth <= 0) {
			diagonalPXWidth = 1;
		}
		int antidiagonalPXWidth = HTMLEmitterUtil.getDimensionPixelValue(antidiagonalWidth, imageDpi);
		if (antidiagonalPXWidth <= 0) {
			antidiagonalPXWidth = 1;
		}

		// Create a buffered image in which to draw
		BufferedImage bufferedImage = new BufferedImage(imagePXWidth, imagePXHeight, BufferedImage.TYPE_INT_ARGB);
		// Create a graphics contents on the buffered image
		Graphics2D g2d = bufferedImage.createGraphics();
		originStroke = g2d.getStroke();

		try {
			if (diagonalStyle != null && !"none".equalsIgnoreCase(diagonalStyle)) {
				// set color
				Color lineColor = PropertyUtil.getColor(diagonalColor);
				if (lineColor == null) {
					lineColor = color;
				}
				if (lineColor != null) {
					g2d.setColor(lineColor);
				}

				// Draw diagonal line.
				// FIXME continue: Double style hasn't been implemented yet, and
				// it
				// will be treated as solid style. The double style should be
				// implemented in the future.

				// if ( "double".equalsIgnoreCase( diagonalStyle ) && (
				// diagonalPXWidth > 2 ) ) { // Double line has the same effect
				// with the solid line when the // line width equal 1px or 2px.
				// } else if ( "dotted".equalsIgnoreCase( diagonalStyle ) )

				if ("dotted".equalsIgnoreCase(diagonalStyle)) {
					float dash[] = { 1, (diagonalPXWidth * 2) - 1 + (diagonalPXWidth % 2) };
					Stroke stroke = new BasicStroke(diagonalPXWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
							10.0f, dash, (diagonalPXWidth * 2) - (diagonalPXWidth / 2));
					g2d.setStroke(stroke);

					if (diagonalNumber == 1) {
						g2d.drawLine(0, diagonalPXWidth / 2, imagePXWidth - 1,
								imagePXHeight - 1 + (diagonalPXWidth / 2));
					} else if (diagonalNumber == 2) {
						g2d.drawLine((imagePXWidth / 3) - 1, diagonalPXWidth / 2, imagePXWidth - 1,
								imagePXHeight - 1 + (diagonalPXWidth / 2));
						g2d.drawLine(0, (imagePXHeight / 3) - 1 + (diagonalPXWidth / 2), imagePXWidth - 1,
								imagePXHeight - 1 + (diagonalPXWidth / 2));
					} else if (diagonalNumber >= 3) {
						g2d.drawLine((imagePXWidth / 2) - 1, diagonalPXWidth / 2, imagePXWidth - 1,
								imagePXHeight - 1 + (diagonalPXWidth / 2));
						g2d.drawLine(0, diagonalPXWidth / 2, imagePXWidth - 1,
								imagePXHeight - 1 + (diagonalPXWidth / 2));
						g2d.drawLine(0, (imagePXHeight / 2) - 1 + (diagonalPXWidth / 2), imagePXWidth - 1,
								imagePXHeight - 1 + (diagonalPXWidth / 2));
					}
				} else {
					if ("dashed".equalsIgnoreCase(diagonalStyle)) {
						float dash[] = { 3 * diagonalPXWidth };
						Stroke stroke = new BasicStroke(diagonalPXWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
								10.0f, dash, 0.0f);
						g2d.setStroke(stroke);
					} else {
						// Solid is the default value
						// Use the default stroke when the diagonalPXWidth is 1.
						if (diagonalPXWidth > 1) {
							g2d.setStroke(new BasicStroke(diagonalPXWidth));
						}

					}

					if (diagonalNumber == 1) {
						g2d.drawLine(0, 0, imagePXWidth - 1, imagePXHeight - 1);
					} else if (diagonalNumber == 2) {
						g2d.drawLine((imagePXWidth / 3) - 1, 0, imagePXWidth - 1, imagePXHeight - 1);
						g2d.drawLine(0, (imagePXHeight / 3) - 1, imagePXWidth - 1, imagePXHeight - 1);
					} else if (diagonalNumber >= 3) {
						g2d.drawLine((imagePXWidth / 2) - 1, 0, imagePXWidth - 1, imagePXHeight - 1);
						g2d.drawLine(0, 0, imagePXWidth - 1, imagePXHeight - 1);
						g2d.drawLine(0, (imagePXHeight / 2) - 1, imagePXWidth - 1, imagePXHeight - 1);
					}
				}
			}
			/*
			 * if ( antidiagonalStyle != null && !"none".equalsIgnoreCase( antidiagonalStyle
			 * ) ) { // set color Color lineColor = PropertyUtil.getColor( antidiagonalColor
			 * ); if ( lineColor == null ) { lineColor = color; } if ( lineColor != null ) {
			 * g2d.setColor( lineColor ); }
			 * 
			 * // Draw antidiagonal line. // FIXME continue: Double style hasn't been
			 * implemented yet, and // it // will be treated as solid style. The double
			 * style should be // implemented in the future.
			 * 
			 * // if ( "double".equalsIgnoreCase( antidiagonalStyle ) && ( //
			 * antidiagonalPXWidth > 2 ) ) { // Double line has the same // effect with the
			 * solid line when the // line width equal 1px // or 2px. // FIXME continue:
			 * implement the left double part. } // else if ( "dotted".equalsIgnoreCase(
			 * antidiagonalStyle ) )
			 * 
			 * if ( "dotted".equalsIgnoreCase( antidiagonalStyle ) ) { float dash[] = { 1, (
			 * antidiagonalPXWidth * 2 ) - 1 + ( antidiagonalPXWidth % 2 ) }; Stroke stroke
			 * = new BasicStroke( antidiagonalPXWidth, BasicStroke.CAP_ROUND,
			 * BasicStroke.JOIN_ROUND, 10.0f, dash, ( antidiagonalPXWidth * 2 ) - (
			 * antidiagonalPXWidth / 2 ) ); g2d.setStroke( stroke );
			 * 
			 * if ( antidiagonalNumber == 1 ) { g2d.drawLine( imagePXWidth - 1,
			 * antidiagonalPXWidth / 2, 0, imagePXHeight - 1 + ( antidiagonalPXWidth / 2 )
			 * ); } else if ( antidiagonalNumber == 2 ) { g2d.drawLine( ( imagePXWidth * 2 /
			 * 3 ) - 1, antidiagonalPXWidth / 2, 0, imagePXHeight - 1 + (
			 * antidiagonalPXWidth / 2 ) ); g2d.drawLine( imagePXWidth - 1, ( imagePXHeight
			 * / 3 ) - 1 + ( antidiagonalPXWidth / 2 ), 0, imagePXHeight - 1 + (
			 * antidiagonalPXWidth / 2 ) ); } else if ( antidiagonalNumber >= 3 ) {
			 * g2d.drawLine( ( imagePXWidth / 2 ) - 1, antidiagonalPXWidth / 2, 0,
			 * imagePXHeight - 1 + ( antidiagonalPXWidth / 2 ) ); g2d.drawLine( imagePXWidth
			 * - 1, antidiagonalPXWidth / 2, 0, imagePXHeight - 1 + ( antidiagonalPXWidth /
			 * 2 ) ); g2d.drawLine( imagePXWidth - 1, ( imagePXHeight / 2 ) - 1 + (
			 * antidiagonalPXWidth / 2 ), 0, imagePXHeight - 1 + ( antidiagonalPXWidth / 2 )
			 * ); } } else { if ( "dashed".equalsIgnoreCase( antidiagonalStyle ) ) { float
			 * dash[] = { 3 * antidiagonalPXWidth }; Stroke stroke = new BasicStroke(
			 * antidiagonalPXWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
			 * dash, 0.0f ); g2d.setStroke( stroke ); } else { // Solid is the default value
			 * if ( antidiagonalPXWidth > 1 ) { g2d.setStroke( new BasicStroke(
			 * antidiagonalPXWidth ) ); } else { // Use the default stroke when the
			 * diagonalPXWidth // is 1. g2d.setStroke( originStroke ); } }
			 * 
			 * if ( antidiagonalNumber == 1 ) { g2d.drawLine( imagePXWidth - 1, 0, 0,
			 * imagePXHeight - 1 ); } else if ( antidiagonalNumber == 2 ) { g2d.drawLine( (
			 * imagePXWidth * 2 / 3 ) - 1, 0, 0, imagePXHeight - 1 ); g2d.drawLine(
			 * imagePXWidth - 1, ( imagePXHeight / 3 ) - 1, 0, imagePXHeight - 1 ); } else
			 * if ( antidiagonalNumber >= 3 ) { g2d.drawLine( ( imagePXWidth / 2 ) - 1, 0,
			 * 0, imagePXHeight - 1 ); g2d.drawLine( imagePXWidth - 1, 0, 0, imagePXHeight -
			 * 1 ); g2d.drawLine( imagePXWidth - 1, ( imagePXHeight / 2 ) - 1, 0,
			 * imagePXHeight - 1 ); } } }
			 */
		} finally {
			// Graphics context no longer needed so dispose it
			g2d.dispose();
		}

		byte[] resultImageByteArray = null;

		ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
		// write the image data into a stream in png format.
		ImageIO.write(bufferedImage, "png", imageStream);
		imageStream.flush();
		// convert the png image data to a byte array.
		resultImageByteArray = imageStream.toByteArray();
		imageStream.close();

		return resultImageByteArray;
	}
}
