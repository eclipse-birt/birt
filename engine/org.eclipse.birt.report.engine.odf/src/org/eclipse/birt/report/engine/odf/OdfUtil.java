/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.odf;

import java.util.HashSet;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

@SuppressWarnings("nls")
public class OdfUtil {
	private static HashSet<Character> splitChar = new HashSet<Character>();

	static {
		splitChar.add(' ');
		splitChar.add('\r');
		splitChar.add('\n');
	};

	public static final float INCH_PT = 72f;

	public static final double PT_TWIPS = 20;

	public static final double INCH_TWIPS = INCH_PT * PT_TWIPS;

	// Bookmark names must begin with a letter and can contain numbers.
	// spaces can not be included in a bookmark name,
	// but the underscore character can be used to separate words
	public static String validBookmarkName(String name) {
		return name.replaceAll("\"", "_");
	}

	// convert from DimensionType to inches according to prefValue
	public static int convertTo(DimensionType value, int prefValue, int dpi) {
		if (value == null) {
			return prefValue;
		}

		if (DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase(value.getUnits())) {
			return (int) (prefValue * value.getMeasure() / 100);
		}

		return (int) convertTo(value, dpi);
	}

	public static double convertTo(DimensionType value, double prefValue, int dpi) {
		if (value == null) {
			return prefValue;
		}

		if (DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase(value.getUnits())) {
			return (prefValue * value.getMeasure() / 100);
		}

		return convertTo(value, dpi);
	}

	public static double convertTo(DimensionType value, int dpi) {
		double INCH_PX = dpi;

		if (value == null || DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase(value.getUnits())) {
			return -1;
		}

		if (DimensionType.UNITS_PX.equalsIgnoreCase(value.getUnits())) {
			return value.getMeasure() / INCH_PX;
		}

		// FIXME: We should use font size to calculate the EM/EX
		if (DimensionType.UNITS_EM.equalsIgnoreCase(value.getUnits())
				|| DimensionType.UNITS_EX.equalsIgnoreCase(value.getUnits())) {
			return value.getMeasure() * 12 / INCH_PX;
		}
		// The conversion is between absolute
		// the units should be one of the absolute units(CM, IN, MM, PT,PC).
		double val = value.convertTo(DimensionType.UNITS_IN);
		return val;
	}

	// convert image's size from DimensionType to inches according to ref
	public static double convertImageSize(DimensionType value, int ref, int dpi) {
		if (value == null) {
			return (double) ref / dpi;
		}

		if (DimensionType.UNITS_PX.equalsIgnoreCase(value.getUnits())) {
			return value.getMeasure() / dpi;
		} else if (DimensionType.UNITS_PERCENTAGE.equalsIgnoreCase(value.getUnits())) {
			return (value.getMeasure() / 100) * ref / dpi;
		} else {
			return value.convertTo(DimensionType.UNITS_IN);
		}
	}

	public static double twipToPt(double t) {
		return t / PT_TWIPS;
	}

	// unit change from milliPt to pt
	public static int parseFontSize(float value) {
		return Math.round(value / 1000);
	}

	// convert valid color format from "rgb(0,0,0) or others" to "000000"
	public static String parseColor(String color) {
		String sColor = EmitterUtil.parseColor(color);
		if (sColor != null) {
			return "#" + sColor; //$NON-NLS-1$
		}
		return null;
	}

	public static String[] parseBackgroundSize(String height, String width, int imageWidth, int imageHeight,
			double pageWidth, double pageHeight) {
		String actualHeight = height;
		String actualWidth = width;
		if (height == null || "auto".equalsIgnoreCase(height))
			actualHeight = String.valueOf(pageHeight) + "pt";
		if (width == null || "auto".equalsIgnoreCase(width))
			actualWidth = String.valueOf(pageWidth) + "pt";
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
			int percentValue = Integer.valueOf(percent).intValue();
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

	/**
	 * Returns the dimension value in inches.
	 */
	public static double getDimensionValue(CSSValue value, int dpi) {
		if (value != null && (value instanceof FloatValue)) {
			FloatValue fv = (FloatValue) value;
			float v = fv.getFloatValue();
			switch (fv.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_CM:
				return (v / 2.54);

			case CSSPrimitiveValue.CSS_IN:
				return (v);

			case CSSPrimitiveValue.CSS_MM:
				return (v / 25.4);

			case CSSPrimitiveValue.CSS_PC:
				return (v * 12 * 1000);

			case CSSPrimitiveValue.CSS_PX:
				return (v / dpi);

			case CSSPrimitiveValue.CSS_PT:
				return (v / 72);

			case CSSPrimitiveValue.CSS_NUMBER:
				return v;

			}
		}
		return 0;
	}

	public static String getValue(String val) {
		if (val == null) {
			return StyleConstant.NULL;
		}
		if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
			return val.substring(1, val.length() - 1);
		}

		return val;
	}

	/**
	 * @param value
	 * @param parent with of parent, the unit is 1/1000 point.
	 * @return
	 */
	public static int convertDimensionType(DimensionType value, float parent, float dpi) {
		float INCH_PX = dpi;
		float PX_PT = INCH_PT / INCH_PX;
		if (value == null) {
			return (int) (parent);
		}
		if (DimensionType.UNITS_PERCENTAGE.equals(value.getUnits())) {
			return (int) (value.getMeasure() / 100 * parent);
		}
		if (DimensionType.UNITS_PX.equalsIgnoreCase(value.getUnits())) {
			return (int) (value.getMeasure() * PX_PT * 1000);
		}

		// FIXME: We should use font size to calculate the EM/EX
		if (DimensionType.UNITS_EM.equalsIgnoreCase(value.getUnits())
				|| DimensionType.UNITS_EX.equalsIgnoreCase(value.getUnits())) {
			return (int) (value.getMeasure() * 12 * 1000);
		} else {
			return (int) (value.convertTo(DimensionType.UNITS_PT) * 1000);
		}
	}

}
