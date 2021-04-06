/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

public class StyleBuilder {
	public static final String C_PATTERN = "(rgb\\()(\\d+)\\,(\\s?\\d+)\\,(\\s?\\d+)\\)";

	public static final Pattern colorp = Pattern.compile(C_PATTERN, Pattern.CASE_INSENSITIVE);

	private static Logger logger = Logger.getLogger(StyleBuilder.class.getName());

	public static StyleEntry createStyleEntry(IStyle style) {
		return createStyleEntry(style, null);
	}

	public static StyleEntry createStyleEntry(IStyle style, StyleEntry parent) {
		StyleEntry entry = new StyleEntry();

		populateColor(style, StyleConstants.STYLE_BACKGROUND_COLOR, entry, StyleConstant.BACKGROUND_COLOR_PROP);

		CSSValue borderWidth = style.getProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH);
		int width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_BOTTOM_COLOR, entry,
					StyleConstant.BORDER_BOTTOM_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_BOTTOM_STYLE_PROP, convertBorderStyle(style.getBorderBottomStyle()));

			entry.setProperty(StyleConstant.BORDER_BOTTOM_WIDTH_PROP, convertBorderWeight(width));
		}

		borderWidth = style.getProperty(IStyle.STYLE_BORDER_TOP_WIDTH);
		width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_TOP_COLOR, entry, StyleConstant.BORDER_TOP_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_TOP_STYLE_PROP, convertBorderStyle(style.getBorderTopStyle()));

			entry.setProperty(StyleConstant.BORDER_TOP_WIDTH_PROP, convertBorderWeight(width));
		}

		borderWidth = style.getProperty(IStyle.STYLE_BORDER_LEFT_WIDTH);
		width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_LEFT_COLOR, entry, StyleConstant.BORDER_LEFT_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_LEFT_STYLE_PROP, convertBorderStyle(style.getBorderLeftStyle()));

			entry.setProperty(StyleConstant.BORDER_LEFT_WIDTH_PROP, convertBorderWeight(width));
		}

		borderWidth = style.getProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH);
		width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_RIGHT_COLOR, entry, StyleConstant.BORDER_RIGHT_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_RIGHT_STYLE_PROP, convertBorderStyle(style.getBorderRightStyle()));

			entry.setProperty(StyleConstant.BORDER_RIGHT_WIDTH_PROP, convertBorderWeight(width));
		}

		populateColor(style, StyleConstants.STYLE_COLOR, entry, StyleConstant.COLOR_PROP);

		entry.setProperty(StyleConstant.FONT_FAMILY_PROP, ExcelUtil.getValue(style.getFontFamily()));

		entry.setProperty(StyleConstant.FONT_SIZE_PROP, convertFontSize(style.getProperty(IStyle.STYLE_FONT_SIZE)));

		// when font style is oblique or italic , we set the font style to
		// italic.The style is consistent with the doc emitter.
		entry.setProperty(StyleConstant.FONT_STYLE_PROP, "italic".equalsIgnoreCase(style.getFontStyle())
				|| CSSConstants.CSS_OBLIQUE_VALUE.equalsIgnoreCase(style.getFontStyle()));

		entry.setProperty(StyleConstant.FONT_WEIGHT_PROP, "bold".equalsIgnoreCase(style.getFontWeight()));

		entry.setProperty(StyleConstant.TEXT_LINE_THROUGH_PROP,
				"line-through".equalsIgnoreCase(style.getTextLineThrough()));

		entry.setProperty(StyleConstant.TEXT_UNDERLINE_PROP, "underline".equalsIgnoreCase(style.getTextUnderline()));

		entry.setProperty(StyleConstant.H_ALIGN_PROP, convertHAlign(style.getTextAlign(), style.getDirection()));

		entry.setProperty(StyleConstant.V_ALIGN_PROP, convertVAlign(parent, style.getVerticalAlign()));

		entry.setProperty(StyleConstant.DATE_FORMAT_PROP, style.getDateFormat());

		String format = style.getNumberFormat();
		entry.setProperty(StyleConstant.NUMBER_FORMAT_PROP, NumberFormatValue.getInstance(format));

		entry.setProperty(StyleConstant.STRING_FORMAT_PROP, style.getStringFormat());

		entry.setProperty(StyleConstant.TEXT_TRANSFORM, style.getTextTransform());

		entry.setProperty(StyleConstant.TEXT_INDENT, style.getProperty(IStyle.STYLE_TEXT_INDENT));

		entry.setProperty(StyleConstant.DIRECTION_PROP, style.getDirection());

		entry.setProperty(StyleConstant.WHITE_SPACE, style.getWhiteSpace());

		return entry;
	}

	public static StyleEntry createEmptyStyleEntry() {
		StyleEntry entry = new StyleEntry();

		// for ( int i = 0; i < StyleEntry.COUNT; i++ )
		// {
		// entry.setProperty( i, StyleEntry.NULL );
		// }

		return entry;
	}

	public static StyleEntry applyDiagonalLine(StyleEntry entry, Color color, String style, int width) {
		if (width > 0) {
			entry.setProperty(StyleConstant.BORDER_DIAGONAL_COLOR_PROP, color);
			entry.setProperty(StyleConstant.BORDER_DIAGONAL_STYLE_PROP, convertBorderStyle(style));
			entry.setProperty(StyleConstant.BORDER_DIAGONAL_WIDTH_PROP, convertBorderWeight(width));
		}
		return entry;
	}

	private static void populateColor(IStyle style, int styleIndex, StyleEntry entry, int index) {
		CSSValue value = style.getProperty(styleIndex);
		entry.setProperty(index, PropertyUtil.getColor(value));
	}

	public static float convertFontSize(CSSValue fontSize) {
		int size = PropertyUtil.getDimensionValue(fontSize);
		float fsize = 0f;
		try {
			fsize = size / 1000f;
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return fsize;
	}

	public static int convertBorderWeight(int width) {
		int w = -1;
		if (width <= 750) {
			w = 1;
		} else if (width >= 750 && width <= 2250) {
			w = 2;
		} else if (width > 2250) {
			w = 3;
		} else {
			w = 2;
		}
		return w;
	}

	public static String convertBorderStyle(String style) {
		String bs = ExcelUtil.getValue(style);

		if (!StyleEntry.isNull(bs)) {
			if ("dotted".equalsIgnoreCase(bs)) {
				bs = "Dot";
			} else if ("dashed".equalsIgnoreCase(bs)) {
				bs = "Dash";
			} else if ("double".equalsIgnoreCase(bs)) {
				bs = "Double";
			} else {
				bs = "Continuous";
			}
		}

		return bs;
	}

	public static String convertHAlign(String align, String direction) {
		String ha = null;
		// "Left";
		align = ExcelUtil.getValue(align);

		if ("left".equalsIgnoreCase(align)) {
			ha = "Left";
		} else if ("right".equalsIgnoreCase(align)) {
			ha = "Right";
		} else if ("center".equalsIgnoreCase(align)) {
			ha = "Center";
		} else if ("rtl".equalsIgnoreCase(direction))
			ha = "Right";
		else
			ha = "Left";

		return ha;
	}

	public static String convertVAlign(StyleEntry parent, String align) {
		align = ExcelUtil.getValue(align);

		if ("bottom".equalsIgnoreCase(align)) {
			return "Bottom";
		} else if ("middle".equalsIgnoreCase(align)) {
			return "Center";
		} else if ("baseline".equalsIgnoreCase(align) && parent != null) {
			return (String) parent.getProperty(StyleConstant.V_ALIGN_PROP);
		}
		return "Top";
	}

	public static boolean isHeritable(int id) {
		if ((id >= StyleConstant.BORDER_BOTTOM_COLOR_PROP && id <= StyleConstant.BORDER_RIGHT_WIDTH_PROP)
				|| (id >= StyleConstant.BORDER_DIAGONAL_COLOR_PROP
						&& id <= StyleConstant.BORDER_ANTIDIAGONAL_WIDTH_PROP))
			return false;
		return true;
	}

	public static void mergeInheritableProp(StyleEntry cEntry, StyleEntry entry) {
		for (int i = 0; i < StyleConstant.COUNT; i++) {
			if (StyleBuilder.isHeritable(i) && StyleEntry.isNull(entry.getProperty(i))) {
				entry.setProperty(i, cEntry.getProperty(i));
			}
		}
	}

	public static void applyRightBorder(StyleEntry cEntry, StyleEntry entry) {
		if (entry == null) {
			return;
		}
		overwriteProp(cEntry, entry, StyleConstant.BORDER_RIGHT_COLOR_PROP);
		overwriteProp(cEntry, entry, StyleConstant.BORDER_RIGHT_STYLE_PROP);
		overwriteProp(cEntry, entry, StyleConstant.BORDER_RIGHT_WIDTH_PROP);
	}

	public static void applyLeftBorder(StyleEntry cEntry, StyleEntry entry) {
		if (entry == null) {
			return;
		}
		overwriteProp(cEntry, entry, StyleConstant.BORDER_LEFT_COLOR_PROP);
		overwriteProp(cEntry, entry, StyleConstant.BORDER_LEFT_STYLE_PROP);
		overwriteProp(cEntry, entry, StyleConstant.BORDER_LEFT_WIDTH_PROP);
	}

	public static void applyTopBorder(StyleEntry cEntry, StyleEntry entry) {
		if (entry == null) {
			return;
		}
		overwriteProp(cEntry, entry, StyleConstant.BORDER_TOP_COLOR_PROP);
		overwriteProp(cEntry, entry, StyleConstant.BORDER_TOP_STYLE_PROP);
		overwriteProp(cEntry, entry, StyleConstant.BORDER_TOP_WIDTH_PROP);
	}

	public static boolean applyBottomBorder(StyleEntry cEntry, StyleEntry entry) {
		if (entry == null) {
			return false;
		}
		boolean isChanged = false;
		isChanged |= overwriteProp(cEntry, entry, StyleConstant.BORDER_BOTTOM_COLOR_PROP);
		isChanged |= overwriteProp(cEntry, entry, StyleConstant.BORDER_BOTTOM_STYLE_PROP);
		isChanged |= overwriteProp(cEntry, entry, StyleConstant.BORDER_BOTTOM_WIDTH_PROP);
		return isChanged;
	}

	private static boolean overwriteProp(StyleEntry cEntry, StyleEntry entry, int id) {
		if (StyleEntry.isNull(entry.getProperty(id))) {
			Object property = cEntry.getProperty(id);
			if (property != null) {
				entry.setProperty(id, property);
				return true;
			}
			return false;
		}
		return false;
	}
}
