/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

/**
 * <p>
 * ClientAnchorConversions provides a small set of functions for converting the
 * values used with ClientAnchors.
 * </p>
 * <p>
 * This class is very heavily based on the ConvertImageUnits class from the POI
 * examples. The differences between that class and this are:
 * <ol>
 * <li>This class contains only the functionality that I need.</li>
 * <li>This class contains no public static values, only methods.</li>
 * </ol>
 * <p>
 *
 * @author Jim Talbut
 *
 */
public class ClientAnchorConversions {

	// Constants that defines how many pixels and points there are in a
	// millimetre. These values are required for the conversion algorithm.
	private static final double PIXELS_PER_MILLIMETRES = 3.78; // MB
	private static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
	private static final int UNIT_OFFSET_LENGTH = 7;
	private static final int[] UNIT_OFFSET_MAP = { 0, 36, 73, 109, 146, 182, 219 };

	/**
	 * Convert a measure in column width units (1/256th of a character) to a measure
	 * in millimetres. <BR>
	 * Makes assumptions about font size and relevant DPI.
	 *
	 * @param widthUnits The size in width units.
	 * @return The size in millimetres.
	 */
	public static double widthUnits2Millimetres(int widthUnits) {
		int pixels = (widthUnits / EXCEL_COLUMN_WIDTH_FACTOR) * UNIT_OFFSET_LENGTH;
		int offsetWidthUnits = widthUnits % EXCEL_COLUMN_WIDTH_FACTOR;
		pixels += Math.round(offsetWidthUnits / ((float) EXCEL_COLUMN_WIDTH_FACTOR / UNIT_OFFSET_LENGTH));
		return pixels / PIXELS_PER_MILLIMETRES;
	}

	/**
	 * Convert a measure of millimetres to width units.
	 *
	 * @param millimetres The size in millimetres.
	 * @return The size in width units.
	 */
	public static int millimetres2WidthUnits(double millimetres) {
		int pixels = (int) (millimetres * PIXELS_PER_MILLIMETRES);
		short widthUnits = (short) (EXCEL_COLUMN_WIDTH_FACTOR * (pixels / UNIT_OFFSET_LENGTH));
		widthUnits += UNIT_OFFSET_MAP[(pixels % UNIT_OFFSET_LENGTH)];
		return widthUnits;
	}

	/**
	 * Convert a measure of pixels to millimetres (for column widths).
	 *
	 * @param pixels The size in pixels.
	 * @return The size in millimetres.
	 */
	public static double pixels2Millimetres(double pixels) {
		return pixels / PIXELS_PER_MILLIMETRES;
	}

	/**
	 * Convert a measure of millimetres to pixels (for column widths)
	 *
	 * @param millimetres The size in millimetres.
	 * @return The size in pixels.
	 */
	public static int millimetres2Pixels(double millimetres) {
		return (int) (millimetres * PIXELS_PER_MILLIMETRES);
	}

}
