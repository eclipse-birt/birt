/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.HashSet;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;

public class WordUtil {

	private static final String LINESTYLE_SOLID = "solid";

	private static final String LINESTYLE_DASH = "dash";

	private static final String LINESTYLE_DOT = "dot";

	private static final String LINESTYLE_SINGLE = "single";

	private static HashSet<Character> splitChar = new HashSet<>();

	static {
		splitChar.add(Character.valueOf(' '));
		splitChar.add(Character.valueOf('\r'));
		splitChar.add(Character.valueOf('\n'));
	}

	public static final double INCH_PT = 72;

	public static final double PT_TWIPS = 20;

	public static final double INCH_TWIPS = INCH_PT * PT_TWIPS;

	/**
	 * maximum value of word column width 22in to TWIPS
	 */
	public static final int MAX_ELEMENT_WIDTH_INCH_TWIPS = 31680;

	// Bookmark names must begin with a letter and can contain numbers.
	// spaces can not be included in a bookmark name,
	// but the underscore character can be used to separate words
	public static String validBookmarkName(String name) {
		String bookmark = name.replace(' ', '_');
		bookmark = bookmark.replace('"', '_');
		return bookmark;
	}

	// convert from DimensionType to twips according to prefValue
	public static int convertTo(DimensionType value, int prefValue, int dpi) {
		if (value == null) {
			return prefValue;
		}

		if (DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase(value.getUnits())) {
			return (int) (prefValue * value.getMeasure() / 100);
		}

		return (int) convertTo(value, dpi);
	}

	public static double convertTo(DimensionType value, int dpi) {
		double INCH_PX = dpi;
		double PX_TWIPS = INCH_TWIPS / INCH_PX;

		if (value == null || DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase(value.getUnits())) {
			return -1;
		}

		if (DimensionType.UNITS_PX.equalsIgnoreCase(value.getUnits())) {
			return value.getMeasure() * PX_TWIPS;
		}

		// FIXME: We should use font size to calculate the EM/EX
		if (DimensionType.UNITS_EM.equalsIgnoreCase(value.getUnits())
				|| DimensionType.UNITS_EX.equalsIgnoreCase(value.getUnits())) {
			return value.getMeasure() * 12 * PT_TWIPS;
		}
		// The conversion is between absolute
		// the units should be one of the absolute units(CM, IN, MM, PT,PC).
		double val = value.convertTo(DimensionType.UNITS_IN);
		return val * INCH_TWIPS;
	}

	// convert image's size from DimensionType to pt according to ref
	public static double convertImageSize(DimensionType value, int ref, int dpi) {
		double INCH_PX = dpi;
		double PX_PT = INCH_PT / INCH_PX;

		if (value == null) {
			return ref * PX_PT;
		}

		if (DimensionType.UNITS_PX.equalsIgnoreCase(value.getUnits())) {
			return value.getMeasure() * PX_PT;
		} else if (DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase(value.getUnits())) {
			return (value.getMeasure() / 100) * ref * PX_PT;
		} else {
			return value.convertTo(DimensionType.UNITS_IN) * INCH_PT;
		}
	}

	public static double twipToPt(double t) {
		return t / PT_TWIPS;
	}

	// unit change from milliPt to twips
	public static int milliPt2Twips(float floatValue) {
		return (int) Math.round(floatValue / 1000 * PT_TWIPS);
	}

	// unit change from milliPt to half a point
	public static int parseFontSize(float value) {
		return Math.round(value / 500);
	}

	public static String capitalize(String text) {
		boolean capitalizeNextChar = true;
		char[] array = text.toCharArray();
		for (int i = 0; i < array.length; i++) {
			Character c = text.charAt(i);
			if (splitChar.contains(c)) {
				capitalizeNextChar = true;
			} else if (capitalizeNextChar) {
				array[i] = Character.toUpperCase(array[i]);
				capitalizeNextChar = false;
			}
		}
		return new String(array);
	}

	// convert valid color format from "rgb(0,0,0) or others" to "000000"
	public static String parseColor(String color) {
		return EmitterUtil.parseColor(color);
	}

	// run border, paragraph borders, table borders, table cell borders
	// birt accept:solid, dotted, dashed, double
	// doc and docx accept: single, dotted, dashed, double
	public static String parseBorderStyle(String style) {
		if (CSSConstants.CSS_SOLID_VALUE.equalsIgnoreCase(style)) {
			return LINESTYLE_SINGLE;
		}
		return style;
	}

	// image borders style
	// birt accept: solid, dotted, dashed, double
	// doc and docx accept in vml: single, dot, dash, double
	public static String parseImageBorderStyle(String style) {
		if (CSSConstants.CSS_DOTTED_VALUE.equalsIgnoreCase(style)) {
			return LINESTYLE_DOT;
		}
		if (CSSConstants.CSS_DASHED_VALUE.equalsIgnoreCase(style)) {
			return LINESTYLE_DASH;
		}
		if (CSSConstants.CSS_SOLID_VALUE.equalsIgnoreCase(style)) {
			return LINESTYLE_SINGLE;
		}
		return style;
	}

	// align: bottom, middle, top
	// doc and docx accept: bottom, center, top
	public static String parseVerticalAlign(String align) {
		if (CSSConstants.CSS_MIDDLE_VALUE.equals(align)) {
			return "center";
		}
		return align;
	}

	public static String removeQuote(String val) {
		if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
			return val.substring(1, val.length() - 1);
		}
		return val;
	}

	// unit: eighth of a point
	public static int parseBorderSize(float size) {
		int w = Math.round(size);
		return (8 * w) / 1000;
	}

	public static String parseLineStyle(String style) {
		if (CSSConstants.CSS_DOTTED_VALUE.equalsIgnoreCase(style)) {
			return LINESTYLE_DOT;
		}
		if (CSSConstants.CSS_DASHED_VALUE.equalsIgnoreCase(style)) {
			return LINESTYLE_DASH;
		}
		if (CSSConstants.CSS_DOUBLE_VALUE.equalsIgnoreCase(style)) {
			return LINESTYLE_SOLID;
		}
		return style;
	}

	public static String[] parseBackgroundSize(String height, String width, int imageWidth, int imageHeight,
			double pageWidth, double pageHeight) {
		String actualHeight = height;
		String actualWidth = width;
		if (height == null || "auto".equalsIgnoreCase(height)) {
			actualHeight = String.valueOf(pageHeight) + "pt";
		}
		if (width == null || "auto".equalsIgnoreCase(width)) {
			actualWidth = String.valueOf(pageWidth) + "pt";
		}
		actualHeight = actualHeight.trim();
		actualWidth = actualWidth.trim();

		if ("contain".equalsIgnoreCase(actualWidth) || ("contain").equalsIgnoreCase(actualHeight)) {
			double rh = imageHeight / pageHeight;
			double rw = imageWidth / pageWidth;
			if (rh > rw) {
				actualHeight = String.valueOf(pageHeight) + "pt";
				actualWidth = String.valueOf(imageWidth * pageHeight / imageHeight) + "pt";
			} else {
				actualWidth = String.valueOf(pageWidth) + "pt";
				actualHeight = String.valueOf(imageHeight * pageWidth / imageWidth) + "pt";
			}
		} else if ("cover".equals(actualWidth) || "cover".equals(actualHeight)) {
			double rh = imageHeight / pageHeight;
			double rw = imageWidth / pageWidth;
			if (rh > rw) {
				actualWidth = String.valueOf(pageWidth) + "pt";
				actualHeight = String.valueOf(imageHeight * pageWidth / imageWidth) + "pt";
			} else {
				actualHeight = String.valueOf(pageHeight) + "pt";
				actualWidth = String.valueOf(imageWidth * pageHeight / imageHeight) + "pt";
			}
		}
		if (height != null && height.endsWith("%")) {
			actualHeight = getPercentValue(height, pageHeight) + "pt";
		}
		if (width != null && width.endsWith("%")) {
			actualWidth = getPercentValue(width, pageWidth) + "pt";
		}
		return new String[] { actualHeight, actualWidth };
	}

	private static String getPercentValue(String height, double pageHeight) {
		String value = null;
		try {
			String percent = height.substring(0, height.length() - 1);
			int percentValue = Integer.parseInt(percent);
			value = String.valueOf(pageHeight * percentValue / 100);
		} catch (NumberFormatException e) {
			value = height;
		}
		return value;
	}

	public static boolean isField(int autoTextType) {
		return autoTextType == IAutoTextContent.PAGE_NUMBER || autoTextType == IAutoTextContent.TOTAL_PAGE;
	}

	public static boolean isField(IContent content) {
		if (content.getContentType() == IContent.AUTOTEXT_CONTENT) {
			IAutoTextContent autoText = (IAutoTextContent) content;
			int type = autoText.getType();
			return isField(type);
		}
		return false;
	}
}
