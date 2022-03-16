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
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Font;

public class PropertyUtil {

	private static Logger logger = Logger.getLogger(PropertyUtil.class.getName());

	private static Pattern colorPattern = Pattern.compile("rgb\\(.+,.+,.+\\)");

	private static HashMap<Value, Integer> fontWeightMap = new HashMap<>();
	static {
		fontWeightMap.put(IStyle.LIGHTER_VALUE, 200);
		fontWeightMap.put(IStyle.NORMAL_VALUE, 400);
		fontWeightMap.put(IStyle.BOLD_VALUE, 700);
		fontWeightMap.put(IStyle.BOLDER_VALUE, 900);

		fontWeightMap.put(IStyle.NUMBER_100, 100);
		fontWeightMap.put(IStyle.NUMBER_200, 200);
		fontWeightMap.put(IStyle.NUMBER_300, 300);
		fontWeightMap.put(IStyle.NUMBER_400, 400);
		fontWeightMap.put(IStyle.NUMBER_500, 500);
		fontWeightMap.put(IStyle.NUMBER_600, 600);
		fontWeightMap.put(IStyle.NUMBER_700, 700);
		fontWeightMap.put(IStyle.NUMBER_800, 800);
		fontWeightMap.put(IStyle.NUMBER_900, 900);
	}

	/**
	 * Checks if the font is bold
	 *
	 * @param value the CSSValue
	 * @return true if the font is bold false if not
	 */
	public static boolean isBoldFont(int fontWeight) {
		if (fontWeight > 400) {
			return true;
		}
		return false;
	}

	public static int parseFontWeight(CSSValue value) {
		if (fontWeightMap.containsKey(value)) {
			return fontWeightMap.get(value);
		} else {
			return 400; // Normal
		}
	}

	public static boolean isDisplayNone(IContent content) {
		IStyle style = content.getStyle();
		if (style != null) {
			return IStyle.NONE_VALUE.equals(style.getProperty(IStyle.STYLE_DISPLAY));
		}
		return false;
	}

	public static boolean isInlineElement(IContent content) {
		if (content instanceof IPageContent) {
			return false;
		}
		IStyle style = content.getStyle();
		if (style != null) {
			return IStyle.INLINE_VALUE.equals(style.getProperty(IStyle.STYLE_DISPLAY));
		}
		return false;
	}

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

	public static String getBackgroundImage(CSSValue value) {
		if (value instanceof StringValue) {
			String strValue = ((StringValue) value).getStringValue();
			if (strValue != null && (!CSSConstants.CSS_NONE_VALUE.equals(strValue))) {
				return strValue;
			}
		}
		return null;
	}

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
	 * @return
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
			resolution = 96;
		}
		return resolution;
	}

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
			if (screenDpi < 96 || screenDpi > 120) {
				screenDpi = 0;
			}
		}
		return screenDpi;
	}

	public static int getDimensionValue(CSSValue value) {
		return getDimensionValue(value, 96, 0);
	}

	/**
	 * @deprecated keep for backward compatibility.
	 */
	@Deprecated
	public static int getDimensionValue(CSSValue value, int referenceLength) {
		return getDimensionValue(value, 96, referenceLength);
	}

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
					dpi = 96;
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

	public static int getDimensionValue(IContent content, DimensionType d) {
		return getDimensionValue(content, d, 0, 0);
	}

	protected static int _getDimensionValue(IContent content, DimensionType d, int renderOptionDpi,
			int referenceLength) {
		if (d.getValueType() == DimensionType.TYPE_DIMENSION) {
			String units = d.getUnits();
			if (units.equals(EngineIRConstants.UNITS_PT) || units.equals(EngineIRConstants.UNITS_CM)
					|| units.equals(EngineIRConstants.UNITS_MM) || units.equals(EngineIRConstants.UNITS_PC)
					|| units.equals(EngineIRConstants.UNITS_IN)) {
				double point = d.convertTo(EngineIRConstants.UNITS_PT) * 1000;
				return (int) point;
			} else if (units.equals(EngineIRConstants.UNITS_PX)) {
				double point = d.getMeasure() / getRenderDpi(content, renderOptionDpi) * 72000d;
				return (int) point;
			} else if (units.equals(EngineIRConstants.UNITS_PERCENTAGE)) {
				if (referenceLength < 0) {
					return -1;
				}
				double point = referenceLength * d.getMeasure() / 100.0;
				return (int) point;
			} else if (units.equals(EngineIRConstants.UNITS_EM) || units.equals(EngineIRConstants.UNITS_EX)) {
				int size = 9000;
				if (content != null) {
					IStyle style = content.getComputedStyle();
					CSSValue fontSize = style.getProperty(IStyle.STYLE_FONT_SIZE);
					size = getDimensionValue(fontSize);
				}
				double point = size * d.getMeasure();
				return (int) point;
			}
		} else if (d.getValueType() == DimensionType.TYPE_CHOICE) {
			String choice = d.getChoice();
			if (IStyle.CSS_MEDIUM_VALUE.equals(choice)) {
				return 3000;
			} else if (IStyle.CSS_THIN_VALUE.equals(choice)) {
				return 1000;
			} else if (IStyle.CSS_THICK_VALUE.equals(choice)) {
				return 5000;
			}
		}
		return 0;
	}

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

	public static int getDimensionValue(IContent content, DimensionType d, int referenceLength) {
		return getDimensionValue(content, d, 0, referenceLength);
	}

	public static int getIntAttribute(Element element, String attribute) {
		String value = element.getAttribute(attribute);
		int result = 1;
		if (value != null && value.length() != 0) {
			result = Integer.parseInt(value);
		}
		return result;
	}

	public static DimensionType getDimensionAttribute(Element ele, String attribute) {
		String value = ele.getAttribute(attribute);
		if (value == null || 0 == value.length()) {
			return null;
		}
		return DimensionType.parserUnit(value, DimensionType.UNITS_PX);
	}

	public static int getIntValue(CSSValue value) {
		if (value != null && (value instanceof FloatValue)) {
			FloatValue fv = (FloatValue) value;
			return (int) fv.getFloatValue();
		}
		return 0;
	}

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

	public static boolean isWhiteSpaceNoWrap(CSSValue value) {
		return IStyle.CSS_NOWRAP_VALUE.equals(value.getCssText());
	}

}
