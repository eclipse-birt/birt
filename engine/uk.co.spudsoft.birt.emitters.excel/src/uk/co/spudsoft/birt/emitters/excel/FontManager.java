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

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.ListValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.w3c.dom.css.CSSValue;

/**
 * FontManager is a cache of fonts to enable POI Fonts to be reused based upon
 * their BIRT styles.
 *
 * @author Jim Talbut
 *
 */
public class FontManager {

	/**
	 * FontPair maintains the relationship between a BIRT style and a POI font.
	 *
	 * @author Jim Talbut
	 *
	 */
	private class FontPair {
		public BirtStyle birtStyle;
		public Font poiFont;

		public FontPair(BirtStyle birtStyle, Font poiFont) {
			this.birtStyle = birtStyle;
			this.poiFont = poiFont;
		}
	}

	private Workbook workbook;
	private StyleManagerUtils smu;
	private List<FontPair> fonts = new ArrayList<>();
	private Font defaultFont = null;
	private CSSEngine cssEngine;

	/**
	 * @param workbook The workbook for which fonts are being tracked.
	 * @param smu      The StyleManagerUtils instance that will be used in the
	 *                 comparison of styles and manipulation of colours.
	 */
	public FontManager(CSSEngine cssEngine, Workbook workbook, StyleManagerUtils smu) {
		this.cssEngine = cssEngine;
		this.workbook = workbook;
		this.smu = smu;
	}

	/**
	 * Obtain the CSS Engine known by this font manager.
	 */
	CSSEngine getCssEngine() {
		return cssEngine;
	}

	/**
	 * Remove quotes surrounding a string.
	 *
	 * @param family The string that may be surrounded by double quotes.
	 * @return family, without any surrounding double quotes.
	 */
	private static String cleanupQuotes(CSSValue value) {
		if (value == null) {
			return null;
		}

		if (value instanceof ListValue) {
			ListValue listValue = (ListValue) value;
			if (listValue.getLength() > 0) {
				value = listValue.item(0);
			}
		}

		String stringValue = (value instanceof StringValue ? ((StringValue) value).getStringValue()
				: value.getCssText());
		if ((stringValue == null) || stringValue.isEmpty()) {
			return stringValue;
		}
		if (stringValue.startsWith("\"") && stringValue.endsWith("\"")) {
			String newFamily = stringValue.substring(1, stringValue.length() - 1);
			return newFamily;
		}
		return stringValue;
	}

	static int COMPARE_CSS_PROPERTIES[] = { StyleConstants.STYLE_FONT_FAMILY, StyleConstants.STYLE_FONT_SIZE,
			StyleConstants.STYLE_FONT_WEIGHT, StyleConstants.STYLE_FONT_STYLE, StyleConstants.STYLE_TEXT_UNDERLINE,
			StyleConstants.STYLE_COLOR, };

	/**
	 * Test whether two BIRT styles are equivalent, as far as their font definitions
	 * are concerned. <br/>
	 * Every attribute tested in this method must be used in the construction of the
	 * font in createFont.
	 *
	 * @param style1 The first BIRT style to be compared.
	 * @param style2 The second BIRT style to be compared.
	 * @return true if style1 and style2 would produce identical Fonts if passed to
	 *         createFont.
	 */
	public static boolean fontsEquivalent(BirtStyle style1, BirtStyle style2) {
		for (int i = 0; i < COMPARE_CSS_PROPERTIES.length; ++i) {
			int prop = COMPARE_CSS_PROPERTIES[i];
			CSSValue value1 = style1.getProperty(prop);
			CSSValue value2 = style2.getProperty(prop);
			if (!StyleManagerUtils.objectsEqual(value1, value2)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Create a new POI Font based upon a BIRT style.
	 *
	 * @param birtStyle The BIRT style to base the Font upon.
	 * @return The Font whose attributes are described by the BIRT style.
	 */
	private Font createFont(BirtStyle birtStyle) {
		Font font = workbook.createFont();

		// Family
		String fontName = smu
				.poiFontNameFromBirt(cleanupQuotes(birtStyle.getProperty(StyleConstants.STYLE_FONT_FAMILY)));
		if (fontName == null) {
			fontName = "Calibri";
		}
		font.setFontName(fontName);
		// Size
		short fontSize = smu.fontSizeInPoints(cleanupQuotes(birtStyle.getProperty(StyleConstants.STYLE_FONT_SIZE)));
		if (fontSize > 0) {
			font.setFontHeightInPoints(fontSize);
		}
		// Weight
		// short fontWeight = smu
		// .poiFontWeightFromBirt(cleanupQuotes(birtStyle.getProperty(StyleConstants.STYLE_FONT_WEIGHT)));
		// if (fontWeight > 0) {
		// font.setBoldweight(fontWeight);
		// }
		boolean fontWeight = smu
				.poiFontWeightFromBirt(cleanupQuotes(birtStyle.getProperty(StyleConstants.STYLE_FONT_WEIGHT)));
		font.setBold(fontWeight);
		// Style
		String fontStyle = cleanupQuotes(birtStyle.getProperty(StyleConstants.STYLE_FONT_STYLE));
		if (CSSConstants.CSS_ITALIC_VALUE.equals(fontStyle) || CSSConstants.CSS_OBLIQUE_VALUE.equals(fontStyle)) {
			font.setItalic(true);
		}
		// Underline
		String fontUnderline = cleanupQuotes(birtStyle.getProperty(StyleConstants.STYLE_TEXT_UNDERLINE));
		if (CSSConstants.CSS_UNDERLINE_VALUE.equals(fontUnderline)) {
			font.setUnderline(FontUnderline.SINGLE.getByteValue());
		}
		// Colour
		smu.addColourToFont(workbook, font, cleanupQuotes(birtStyle.getProperty(StyleConstants.STYLE_COLOR)));

		fonts.add(new FontPair(birtStyle, font));
		return font;
	}

	/**
	 * <p>
	 * Return the default font for the workbook.
	 * </p>
	 * <p>
	 * At this stage this is hardcoded to return Calibri 11pt, but it could be
	 * changed to either pull a value from POI or to have a parameterised value set
	 * from the emitter (via the constructor).
	 *
	 * @return A Font object representing the default to use when no other options
	 *         are available.
	 */
	private Font getDefaultFont() {
		if (defaultFont == null) {
			defaultFont = workbook.createFont();
			defaultFont.setFontName("Calibri");
			defaultFont.setFontHeightInPoints((short) 11);
		}
		return defaultFont;
	}

	/**
	 * Get a Font matching the BIRT style, either from the cache or by creating a
	 * new one.
	 *
	 * @param birtStyle The BIRT style to base the Font upon.
	 * @return A Font whose attributes are described by the BIRT style.
	 */
	public Font getFont(BirtStyle birtStyle) {
		if (birtStyle == null) {
			return getDefaultFont();
		}

		if ((birtStyle.getProperty(StyleConstants.STYLE_FONT_FAMILY) == null)
				&& (birtStyle.getProperty(StyleConstants.STYLE_FONT_SIZE) == null)
				&& (birtStyle.getProperty(StyleConstants.STYLE_FONT_WEIGHT) == null)
				&& (birtStyle.getProperty(StyleConstants.STYLE_FONT_STYLE) == null)
				&& (birtStyle.getProperty(StyleConstants.STYLE_TEXT_UNDERLINE) == null)
				&& (birtStyle.getProperty(StyleConstants.STYLE_COLOR) == null)) {
			return getDefaultFont();
		}

		for (FontPair fontPair : fonts) {
			if (fontsEquivalent(birtStyle, fontPair.birtStyle)) {
				return fontPair.poiFont;
			}
		}

		return createFont(birtStyle);
	}

	private BirtStyle birtStyleFromFont(Font source) {
		for (FontPair fontPair : fonts) {
			if (source.equals(fontPair.poiFont)) {
				return fontPair.birtStyle.clone();
			}
		}

		return new BirtStyle(cssEngine);
	}

	/**
	 * Return a POI font created by combining a POI font with a BIRT style, where
	 * the BIRT style overrides the values in the POI font.
	 *
	 * @param source         The POI font that represents the base font.
	 * @param birtExtraStyle The BIRT style to overlay on top of the POI style.
	 * @return A POI font representing the combination of source and birtExtraStyle.
	 */
	public Font getFontWithExtraStyle(Font source, IStyle birtExtraStyle) {

		BirtStyle birtStyle = birtStyleFromFont(source);

		for (int i = 0; i < IStyle.NUMBER_OF_STYLE; ++i) {
			CSSValue value = birtExtraStyle.getProperty(i);
			if (value != null) {
				birtStyle.setProperty(i, value);
			}
		}

		Font newFont = getFont(birtStyle);
		return newFont;
	}

}
