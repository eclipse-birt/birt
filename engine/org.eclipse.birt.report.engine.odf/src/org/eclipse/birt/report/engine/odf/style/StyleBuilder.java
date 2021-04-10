/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf.style;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.w3c.dom.css.CSSValue;

@SuppressWarnings("nls")
public class StyleBuilder {
	public static final String C_PATTERN = "(rgb\\()(\\d+)\\,(\\s?\\d+)\\,(\\s?\\d+)\\)";

	public static final Pattern colorp = Pattern.compile(C_PATTERN, Pattern.CASE_INSENSITIVE);

	private static final Set<Integer> NON_INHERITABLE_PROPS;

	private static Logger logger = Logger.getLogger(StyleBuilder.class.getName());

	public static StyleEntry createStyleEntry(IStyle style, int type) {
		return createStyleEntry(style, type, null);
	}

	public static StyleEntry createStyleEntry(IStyle style, StyleEntry parent) {
		return createStyleEntry(style, parent.getType());
	}

	public static StyleEntry createStyleEntry(IStyle style, int type, StyleEntry parent) {
		StyleEntry entry = new StyleEntry(style, type);

		populateColor(style, StyleConstants.STYLE_BACKGROUND_COLOR, entry, StyleConstant.BACKGROUND_COLOR_PROP);

		CSSValue borderWidth = style.getProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH);
		int width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_BOTTOM_COLOR, entry,
					StyleConstant.BORDER_BOTTOM_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_BOTTOM_STYLE_PROP, style.getBorderBottomStyle());

			entry.setProperty(StyleConstant.BORDER_BOTTOM_WIDTH_PROP, borderWidth);
		}

		borderWidth = style.getProperty(IStyle.STYLE_BORDER_TOP_WIDTH);
		width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_TOP_COLOR, entry, StyleConstant.BORDER_TOP_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_TOP_STYLE_PROP, style.getBorderTopStyle());

			entry.setProperty(StyleConstant.BORDER_TOP_WIDTH_PROP, borderWidth);
		}

		borderWidth = style.getProperty(IStyle.STYLE_BORDER_LEFT_WIDTH);
		width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_LEFT_COLOR, entry, StyleConstant.BORDER_LEFT_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_LEFT_STYLE_PROP, style.getBorderLeftStyle());

			entry.setProperty(StyleConstant.BORDER_LEFT_WIDTH_PROP, borderWidth);
		}

		borderWidth = style.getProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH);
		width = PropertyUtil.getDimensionValue(borderWidth);
		if (width > 0) {
			populateColor(style, StyleConstants.STYLE_BORDER_RIGHT_COLOR, entry, StyleConstant.BORDER_RIGHT_COLOR_PROP);

			entry.setProperty(StyleConstant.BORDER_RIGHT_STYLE_PROP, style.getBorderRightStyle());

			entry.setProperty(StyleConstant.BORDER_RIGHT_WIDTH_PROP, borderWidth);
		}

		populateColor(style, StyleConstants.STYLE_COLOR, entry, StyleConstant.COLOR_PROP);

		entry.setProperty(StyleConstant.FONT_FAMILY_PROP, OdfUtil.getValue(style.getFontFamily()));

		entry.setProperty(StyleConstant.FONT_SIZE_PROP, convertFontSize(style.getProperty(IStyle.STYLE_FONT_SIZE)));

		entry.setProperty(StyleConstant.FONT_STYLE_PROP, "italic".equalsIgnoreCase(style.getFontStyle()));

		entry.setProperty(StyleConstant.FONT_WEIGHT_PROP, style.getFontWeight());

		entry.setProperty(StyleConstant.TEXT_LINE_THROUGH_PROP,
				"line-through".equalsIgnoreCase(style.getTextLineThrough()));

		entry.setProperty(StyleConstant.TEXT_UNDERLINE_PROP, "underline".equalsIgnoreCase(style.getTextUnderline()));
		entry.setProperty(StyleConstant.TEXT_OVERLINE_PROP, "overline".equalsIgnoreCase(style.getTextOverline()));

		entry.setProperty(StyleConstant.H_ALIGN_PROP, convertHAlign(style.getTextAlign(), style.getDirection()));

		entry.setProperty(StyleConstant.V_ALIGN_PROP, convertVAlign(parent, style.getVerticalAlign()));

		entry.setProperty(StyleConstant.DATE_FORMAT_PROP, style.getDateFormat());
		entry.setProperty(StyleConstant.NUMBER_FORMAT_PROP, style.getNumberFormat());
		entry.setProperty(StyleConstant.STRING_FORMAT_PROP, style.getStringFormat());

		entry.setProperty(StyleConstant.TEXT_TRANSFORM, style.getTextTransform());

		entry.setProperty(StyleConstant.DIRECTION_PROP, style.getDirection());

		entry.setProperty(StyleConstant.WHITE_SPACE, style.getWhiteSpace());

		entry.setProperty(StyleConstant.PADDING_TOP, style.getProperty(IStyle.STYLE_PADDING_TOP));
		entry.setProperty(StyleConstant.PADDING_BOTTOM, style.getProperty(IStyle.STYLE_PADDING_BOTTOM));
		entry.setProperty(StyleConstant.PADDING_LEFT, style.getProperty(IStyle.STYLE_PADDING_LEFT));
		entry.setProperty(StyleConstant.PADDING_RIGHT, style.getProperty(IStyle.STYLE_PADDING_RIGHT));

		entry.setProperty(StyleConstant.MARGIN_TOP, style.getProperty(IStyle.STYLE_MARGIN_TOP));
		entry.setProperty(StyleConstant.MARGIN_BOTTOM, style.getProperty(IStyle.STYLE_MARGIN_BOTTOM));
		entry.setProperty(StyleConstant.MARGIN_LEFT, style.getProperty(IStyle.STYLE_MARGIN_LEFT));
		entry.setProperty(StyleConstant.MARGIN_RIGHT, style.getProperty(IStyle.STYLE_MARGIN_RIGHT));

		entry.setProperty(StyleConstant.LINE_HEIGHT, style.getProperty(StyleConstants.STYLE_LINE_HEIGHT));

		entry.setProperty(StyleConstant.LETTER_SPACING, style.getProperty(StyleConstants.STYLE_LETTER_SPACING));

		entry.setProperty(StyleConstant.TEXT_INDENT, style.getProperty(StyleConstants.STYLE_TEXT_INDENT));

		String imageUri = style.getBackgroundImage();
		if (imageUri != null && imageUri.length() > 0 && !"none".equals(imageUri)) {
			entry.setProperty(StyleConstant.BACKGROUND_IMAGE_URL, style.getBackgroundImage());
			entry.setProperty(StyleConstant.BACKGROUND_IMAGE_WIDTH, style.getBackgroundWidth());
			entry.setProperty(StyleConstant.BACKGROUND_IMAGE_HEIGHT, style.getBackgroundHeight());
			entry.setProperty(StyleConstant.BACKGROUND_IMAGE_LEFT, style.getBackgroundPositionX());
			entry.setProperty(StyleConstant.BACKGROUND_IMAGE_TOP, style.getBackgroundPositionY());
			entry.setProperty(StyleConstant.BACKGROUND_IMAGE_REPEAT, style.getBackgroundRepeat());
		}

		return entry;
	}

	public static StyleEntry createEmptyStyleEntry(int type) {
		StyleEntry entry = new StyleEntry(type);

		// for ( int i = 0; i < StyleEntry.COUNT; i++ )
		// {
		// entry.setProperty( i, StyleEntry.NULL );
		// }

		return entry;
	}

	public static StyleEntry applyDiagonalLine(StyleEntry entry, Color color, String style, int width) {
		if (width > 0) {
			entry.setProperty(StyleConstant.BORDER_DIAGONAL_COLOR_PROP, color);
			entry.setProperty(StyleConstant.BORDER_DIAGONAL_STYLE_PROP, style);
			entry.setProperty(StyleConstant.BORDER_DIAGONAL_WIDTH_PROP, width);
		}
		return entry;
	}

	private static void populateColor(IStyle style, int styleIndex, StyleEntry entry, int index) {
		CSSValue value = style.getProperty(styleIndex);
		if (value != null && !"transparent".equals(value.getCssText()) && !"auto".equals(value.getCssText())) {
			entry.setProperty(index, value.getCssText());
		}
	}

	public static Integer convertFontSize(CSSValue fontSize) {
		int size = PropertyUtil.getDimensionValue(fontSize);
		Integer fsize = null;
		try {
			fsize = size / 1000;
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}

		return fsize;
	}

	public static String convertHAlign(String align, String direction) {
		align = OdfUtil.getValue(align);

		if (align == null || "NULL".equals(align)) {
			// FIXME: hack, it seems that style writing mode doesn't affect this
			if ("rtl".equals(direction)) {
				return "end";
			} else {
				return "start";
			}
		}

		if ("left".equals(align)) {
			return "start";
		}
		if ("right".equals(align)) {
			return "end";
		}

		return align;
	}

	public static String convertVAlign(StyleEntry parent, String align) {
		align = OdfUtil.getValue(align);

		if (align == null) {
			return "auto";
		}
		return align;
	}

	public static boolean isHeritable(int id) {
		return !NON_INHERITABLE_PROPS.contains(id);
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

	static {
		Set<Integer> props = new HashSet<Integer>();
		for (int i = StyleConstant.BORDER_BOTTOM_COLOR_PROP; i <= StyleConstant.BORDER_RIGHT_WIDTH_PROP; i++) {
			props.add(i);
		}

		for (int i = StyleConstant.BORDER_DIAGONAL_COLOR_PROP; i <= StyleConstant.BORDER_ANTIDIAGONAL_WIDTH_PROP; i++) {
			props.add(i);
		}

		// note: background color is not included here
		for (int i = StyleConstant.BACKGROUND_IMAGE_URL; i <= StyleConstant.BACKGROUND_IMAGE_REPEAT; i++) {
			props.add(i);
		}

		NON_INHERITABLE_PROPS = Collections.unmodifiableSet(props);
	}
}
