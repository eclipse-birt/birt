/***********************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Font;

/**
 * Utility class of properties
 *
 * @since 3.3
 *
 */
public class PropertyUtil {

	/** property: default dpi of resolution, 96dpi */
	public static final int DEFAULT_DPI = 96;

	private static Logger logger = Logger.getLogger(PropertyUtil.class.getName());

	private static Pattern colorPattern = Pattern.compile("rgb\\(.+,.+,.+\\)");

	private static HashMap<Value, Integer> fontWeightMap = new HashMap<>();
	static {
		fontWeightMap.put(CSSValueConstants.LIGHTER_VALUE, 200);
		fontWeightMap.put(CSSValueConstants.NORMAL_VALUE, 400);
		fontWeightMap.put(CSSValueConstants.BOLD_VALUE, 700);
		fontWeightMap.put(CSSValueConstants.BOLDER_VALUE, 900);

		fontWeightMap.put(CSSValueConstants.NUMBER_100, 100);
		fontWeightMap.put(CSSValueConstants.NUMBER_200, 200);
		fontWeightMap.put(CSSValueConstants.NUMBER_300, 300);
		fontWeightMap.put(CSSValueConstants.NUMBER_400, 400);
		fontWeightMap.put(CSSValueConstants.NUMBER_500, 500);
		fontWeightMap.put(CSSValueConstants.NUMBER_600, 600);
		fontWeightMap.put(CSSValueConstants.NUMBER_700, 700);
		fontWeightMap.put(CSSValueConstants.NUMBER_800, 800);
		fontWeightMap.put(CSSValueConstants.NUMBER_900, 900);
	}

	/**
	 * Checks if the font is bold
	 *
	 * @param fontWeight
	 * @return true if the font is bold false if not
	 */
	public static boolean isBoldFont(int fontWeight) {
		if (fontWeight > 400) {
			return true;
		}
		return false;
	}

	/**
	 * Parse font weight
	 *
	 * @param value
	 * @return get font weight
	 */
	public static int parseFontWeight(CSSValue value) {
		if (fontWeightMap.containsKey(value)) {
			return fontWeightMap.get(value);
		}
		return 400; // Normal
	}

	/**
	 * Is display none
	 *
	 * @param content
	 * @return true, display is none
	 */
	public static boolean isDisplayNone(IContent content) {
		IStyle style = content.getStyle();
		if (style != null) {
			return CSSValueConstants.NONE_VALUE.equals(style.getProperty(StyleConstants.STYLE_DISPLAY));
		}
		return false;
	}

	/**
	 * Is inline element
	 *
	 * @param content
	 * @return true, is inline element
	 */
	public static boolean isInlineElement(IContent content) {
		if (content instanceof IPageContent) {
			return false;
		}
		IStyle style = content.getStyle();
		if (style != null) {
			return CSSValueConstants.INLINE_VALUE.equals(style.getProperty(StyleConstants.STYLE_DISPLAY));
		}
		return false;
	}

	/**
	 * Get the line height
	 *
	 * @param lineHeight
	 * @return Return line height
	 */
	public static int getLineHeight(String lineHeight) {
		try {
			if (lineHeight.equalsIgnoreCase("normal")) //$NON-NLS-1$
			{
				// BUG 147861: we return *0* as the default value of the
				// *lineLight*
				return 0;
			}

			return (int) Float.parseFloat(lineHeight);
		} catch (NumberFormatException ex) {
			logger.log(Level.WARNING, "invalid line height: {0}", lineHeight); //$NON-NLS-1$
			return 0;
		}
	}

	/**
	 * Get the color
	 *
	 * @param value
	 * @return Return the color
	 */
	public static Color getColor(CSSValue value) {
		if (value instanceof RGBColorValue) {
			RGBColorValue color = (RGBColorValue) value;
			try {
				return new Color(color.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER) / 255.0f,
						color.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER) / 255.0f,
						color.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER) / 255.0f);
			} catch (RuntimeException ex) {
				logger.log(Level.WARNING, "invalid color: {0}", value); //$NON-NLS-1$
			}
		} else if (value instanceof StringValue) {
			return getColor(value.toString());
		}
		return null;
	}

	/**
	 * Gets the color from a CSSValue converted string.
	 *
	 * @param color CSSValue converted string.
	 * @return java.awt.Color
	 */
	public static Color getColor(String color) {
		if (color == null || color.length() == 0) {
			return null;
		}
		if (color.charAt(0) == '#') {
			return hexToColor(color);
		} else if (color.equalsIgnoreCase("Black")) {
			return Color.black;
		} else if (color.equalsIgnoreCase("Gray")) {
			return Color.gray;
		} else if (color.equalsIgnoreCase("White")) {
			return Color.white;
		} else if (color.equalsIgnoreCase("Red")) {
			return Color.red;
		} else if (color.equalsIgnoreCase("Green")) {
			return Color.green;
		} else if (color.equalsIgnoreCase("Yellow")) {
			return Color.yellow;
		} else if (color.equalsIgnoreCase("Blue")) {
			return Color.blue;
		} else if (color.equalsIgnoreCase("Teal")) {
			return hexToColor("#008080");
		} else if (color.equalsIgnoreCase("Aqua")) {
			return hexToColor("#00FFFF");
		} else if (color.equalsIgnoreCase("Silver")) {
			return hexToColor("#C0C0C0");
		} else if (color.equalsIgnoreCase("Navy")) {
			return hexToColor("#000080");
		} else if (color.equalsIgnoreCase("Lime")) {
			return hexToColor("#00FF00");
		} else if (color.equalsIgnoreCase("Olive")) {
			return hexToColor("#808000");
		} else if (color.equalsIgnoreCase("Purple")) {
			return hexToColor("#800080");
		} else if (color.equalsIgnoreCase("Fuchsia")) {
			return hexToColor("#FF00FF");
		} else if (color.equalsIgnoreCase("Maroon")) {
			return hexToColor("#800000");
		} else {
			Matcher m = colorPattern.matcher(color);
			if (m.find()) {
				String[] rgb = color.substring(m.start() + 4, m.end() - 1).split(",");
				if (rgb.length == 3) {
					try {
						int red = Integer.parseInt(rgb[0].trim());
						int green = Integer.parseInt(rgb[1].trim());
						int blue = Integer.parseInt(rgb[2].trim());
						return new Color(red, green, blue);
					} catch (IllegalArgumentException ex) {
						return null;
					}
				}
			}
		}
		return null;
	}

	static final Color hexToColor(String value) {
		String digits;
		if (value.startsWith("#")) {
			digits = value.substring(1, Math.min(value.length(), 7));
		} else {
			digits = value;
		}
		String hstr = "0x" + digits;
		Color c;
		try {
			c = Color.decode(hstr);
		} catch (NumberFormatException nfe) {
			c = null;
		}
		return c;
	}

	/**
	 * Get font style
	 *
	 * @param fontStyle
	 * @param fontWeight
	 * @return Return the font style
	 */
	public static int getFontStyle(String fontStyle, String fontWeight) {
		int styleValue = Font.NORMAL;

		if (CSSConstants.CSS_OBLIQUE_VALUE.equals(fontStyle) || CSSConstants.CSS_ITALIC_VALUE.equals(fontStyle)) {
			styleValue |= Font.ITALIC;
		}

		if (CSSConstants.CSS_BOLD_VALUE.equals(fontWeight) || CSSConstants.CSS_BOLDER_VALUE.equals(fontWeight)
				|| CSSConstants.CSS_600_VALUE.equals(fontWeight) || CSSConstants.CSS_700_VALUE.equals(fontWeight)
				|| CSSConstants.CSS_800_VALUE.equals(fontWeight) || CSSConstants.CSS_900_VALUE.equals(fontWeight)) {
			styleValue |= Font.BOLD;
		}
		return styleValue;
	}

	/**
	 * Get the background image
	 *
	 * @param value
	 * @return Return the background image
	 */
	public static String getBackgroundImage(CSSValue value) {
		if (value instanceof StringValue) {
			String strValue = ((StringValue) value).getStringValue();
			if (strValue != null && (!CSSConstants.CSS_NONE_VALUE.equals(strValue))) {
				return strValue;
			}
		}
		return null;
	}

	/**
	 * Get the image dpi
	 *
	 * @param content
	 * @param imageFileDpi
	 * @param renderOptionDpi
	 * @return Return the image dpi
	 */
	public static int getImageDpi(IImageContent content, int imageFileDpi, int renderOptionDpi) {
		// The DPI resolution of the image.
		// the preference of the DPI setting is:
		// 1. the resolution in image file.
		// 2. use the render DPI.
		int resolution = imageFileDpi;
		if (0 == resolution) {
			resolution = getRenderDpi(content, renderOptionDpi);
		}
		return resolution;
	}

	/**
	 * The DPI resolution used in render. the preference of the DPI setting is: 1.
	 * use the DPI in render options. 2. the DPI in report designHandle. 3. the JRE
	 * screen resolution. 4. the default DPI (96).
	 *
	 * @param content
	 * @param renderOptionDpi
	 * @return Return the render dpi
	 */
	public static int getRenderDpi(IReportContent content, int renderOptionDpi) {
		int resolution = renderOptionDpi;
		if (0 == resolution) {
			ReportDesignHandle designHandle = content.getDesign().getReportDesign();
			resolution = designHandle.getImageDPI();
		}
		if (0 == resolution) {
			resolution = getScreenDpi();
		}
		if (0 == resolution) {
			resolution = DEFAULT_DPI;
		}
		return resolution;
	}

	/**
	 * Get the render dpi
	 *
	 * @param content
	 * @param renderOptionDpi
	 * @return Return the render dpi
	 */
	public static int getRenderDpi(IContent content, int renderOptionDpi) {
		return getRenderDpi(content.getReportContent(), renderOptionDpi);
	}

	private static int screenDpi = -1;

	/**
	 * Get the screen DPI. If the return value is 0, it means the screen dpi is
	 * invalid, otherwise it should be between 96 and 120.
	 *
	 * @return the screen DPI.
	 */
	private static int getScreenDpi() {
		if (-1 == screenDpi) {
			try {
				screenDpi = Toolkit.getDefaultToolkit().getScreenResolution();
			} catch (HeadlessException e) {
				screenDpi = 0;
			}
			if (screenDpi < DEFAULT_DPI || screenDpi > 120) {
				screenDpi = 0;
			}
		}
		return screenDpi;
	}

	/**
	 * Get dimension value
	 *
	 * @param value
	 * @return Return the dimension value
	 */
	public static int getDimensionValue(CSSValue value) {
		return getDimensionValue(value, DEFAULT_DPI, 0);
	}

	/**
	 * Get dimension value
	 *
	 * @param value
	 * @param referenceLength
	 * @return Return the dimension value
	 * @deprecated keep for backward compatibility.
	 */
	@Deprecated
	public static int getDimensionValue(CSSValue value, int referenceLength) {
		return getDimensionValue(value, DEFAULT_DPI, referenceLength);
	}

	/**
	 * Get dimension value with consideration of dpi
	 *
	 * @param value
	 * @param content
	 * @return Return the dimension value
	 */
	public static int getDimensionValueConsiderDpi(CSSValue value, IContent content) {
		if (value != null && (value instanceof FloatValue)) {
			FloatValue fv = (FloatValue) value;
			float v = fv.getFloatValue();
			switch (fv.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_CM:
				return (int) (v * 72000 / 2.54);

			case CSSPrimitiveValue.CSS_IN:
				return (int) (v * 72000);

			case CSSPrimitiveValue.CSS_MM:
				return (int) (v * 7200 / 2.54);

			case CSSPrimitiveValue.CSS_PC:
				return (int) (v * 12 * 1000);

			case CSSPrimitiveValue.CSS_PX:
				ReportDesignHandle designHandle = content.getReportContent().getDesign().getReportDesign();
				int dpi = designHandle.getImageDPI();
				if (dpi == 0) {
					dpi = DEFAULT_DPI;
				}
				return (int) (v / dpi * 72000f);

			case CSSPrimitiveValue.CSS_PT:
				return (int) (v * 1000);

			case CSSPrimitiveValue.CSS_NUMBER:
				return (int) v;
			}
		}
		return 0;
	}

	private static int getDimensionValue(CSSValue value, int dpi, int referenceLength) {
		if (value != null && (value instanceof FloatValue)) {
			FloatValue fv = (FloatValue) value;
			float v = fv.getFloatValue();
			switch (fv.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_CM:
				return (int) (v * 72000 / 2.54);

			case CSSPrimitiveValue.CSS_IN:
				return (int) (v * 72000);

			case CSSPrimitiveValue.CSS_MM:
				return (int) (v * 7200 / 2.54);

			case CSSPrimitiveValue.CSS_PC:
				return (int) (v * 12 * 1000);

			case CSSPrimitiveValue.CSS_PX:
				return (int) (v / dpi * 72000f);

			case CSSPrimitiveValue.CSS_PT:
				return (int) (v * 1000);

			case CSSPrimitiveValue.CSS_NUMBER:
				return (int) v;

			case CSSPrimitiveValue.CSS_PERCENTAGE:
				return (int) (referenceLength * v / 100.0);
			}
		}
		return 0;
	}

	/**
	 * Get dimension value
	 *
	 * @param content
	 * @param d
	 * @return Return the dimension value
	 */
	public static int getDimensionValue(IContent content, DimensionType d) {
		return getDimensionValue(content, d, 0, 0);
	}

	protected static int _getDimensionValue(IContent content, DimensionType d, int renderOptionDpi,
			int referenceLength) {
		if (d.getValueType() == DimensionType.TYPE_DIMENSION) {
			String units = d.getUnits();
			if (units.equals(DesignChoiceConstants.UNITS_PT) || units.equals(DesignChoiceConstants.UNITS_CM)
					|| units.equals(DesignChoiceConstants.UNITS_MM) || units.equals(DesignChoiceConstants.UNITS_PC)
					|| units.equals(DesignChoiceConstants.UNITS_IN)) {
				double point = d.convertTo(DesignChoiceConstants.UNITS_PT) * 1000;
				return (int) point;
			} else if (units.equals(DesignChoiceConstants.UNITS_PX)) {
				double point = d.getMeasure() / getRenderDpi(content, renderOptionDpi) * 72000d;
				return (int) point;
			} else if (units.equals(DesignChoiceConstants.UNITS_PERCENTAGE)) {
				if (referenceLength < 0) {
					return -1;
				}
				double point = referenceLength * d.getMeasure() / 100.0;
				return (int) point;
			} else if (units.equals(DesignChoiceConstants.UNITS_EM) || units.equals(DesignChoiceConstants.UNITS_EX)) {
				int size = 9000;
				if (content != null) {
					IStyle style = content.getComputedStyle();
					CSSValue fontSize = style.getProperty(StyleConstants.STYLE_FONT_SIZE);
					size = getDimensionValue(fontSize);
				}
				double point = size * d.getMeasure();
				return (int) point;
			}
		} else if (d.getValueType() == DimensionType.TYPE_CHOICE) {
			String choice = d.getChoice();
			if (CSSConstants.CSS_MEDIUM_VALUE.equals(choice)) {
				return 3000;
			} else if (CSSConstants.CSS_THIN_VALUE.equals(choice)) {
				return 1000;
			} else if (CSSConstants.CSS_THICK_VALUE.equals(choice)) {
				return 5000;
			}
		}
		return 0;
	}

	/**
	 * Get the image dimension value
	 *
	 * @param content
	 * @param d
	 * @param renderOptionDpi
	 * @param referenceLength
	 * @return Return image dimension value
	 */
	public static int getImageDimensionValue(IContent content, DimensionType d, int renderOptionDpi,
			int referenceLength) {
		if (d == null) {
			return -1;
		}
		try {
			return _getDimensionValue(content, d, renderOptionDpi, referenceLength);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
			return -1;
		}
	}

	/**
	 * Get the dimension value
	 *
	 * @param content
	 * @param d
	 * @param dpi
	 * @param referenceLength
	 * @return Return the dimension value
	 */
	public static int getDimensionValue(IContent content, DimensionType d, int dpi, int referenceLength) {
		if (d == null) {
			return 0;
		}
		try {
			return _getDimensionValue(content, d, dpi, referenceLength);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
			return 0;
		}
	}

	/**
	 * Get the dimension value
	 *
	 * @param content
	 * @param d
	 * @param referenceLength
	 * @return Return the dimension value
	 */
	public static int getDimensionValue(IContent content, DimensionType d, int referenceLength) {
		return getDimensionValue(content, d, 0, referenceLength);
	}

	/**
	 * Get int from attribute
	 *
	 * @param element
	 * @param attribute
	 * @return Return int of attribute
	 */
	public static int getIntAttribute(Element element, String attribute) {
		String value = element.getAttribute(attribute);
		int result = 1;
		if (value != null && value.length() != 0) {
			result = Integer.parseInt(value);
		}
		return result;
	}

	/**
	 * Get dimension type from attribute
	 *
	 * @param ele
	 * @param attribute
	 * @return Return dimension tye
	 */
	public static DimensionType getDimensionAttribute(Element ele, String attribute) {
		String value = ele.getAttribute(attribute);
		if (value == null || 0 == value.length()) {
			return null;
		}
		return DimensionType.parserUnit(value, DimensionType.UNITS_PX);
	}

	/**
	 * Get int value
	 *
	 * @param value
	 * @return Return int value
	 */
	public static int getIntValue(CSSValue value) {
		if (value != null && (value instanceof FloatValue)) {
			FloatValue fv = (FloatValue) value;
			return (int) fv.getFloatValue();
		}
		return 0;
	}

	/**
	 * Get percentage value
	 *
	 * @param value
	 * @return Return percentage value
	 */
	public static float getPercentageValue(CSSValue value) {
		if (value != null && (value instanceof FloatValue)) {
			FloatValue fv = (FloatValue) value;
			float v = fv.getFloatValue();
			if (CSSPrimitiveValue.CSS_PERCENTAGE == fv.getPrimitiveType()) {
				return v / 100.0f;
			}
		}
		return 0.0f;
	}

	/**
	 * Is white space no wrap
	 *
	 * @param value
	 * @return true, white space no wrap
	 */
	public static boolean isWhiteSpaceNoWrap(CSSValue value) {
		return CSSConstants.CSS_NOWRAP_VALUE.equals(value.getCssText());
	}

}
