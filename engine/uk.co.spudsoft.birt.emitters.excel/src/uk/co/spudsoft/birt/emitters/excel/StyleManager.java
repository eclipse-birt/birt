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
import java.util.Locale;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.w3c.dom.css.CSSValue;

import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

/**
 * StyleManager is a cache of POI CellStyles to enable POI CellStyles to be
 * reused based upon their BIRT styles.
 *
 * @author Jim Talbut
 *
 */
public class StyleManager {

	/**
	 * StylePair maintains the relationship between a BIRT style and a POI style.
	 *
	 * @author Jim Talbut
	 *
	 */
	private class StylePair {
		public BirtStyle birtStyle;
		public CellStyle poiStyle;

		public StylePair(BirtStyle birtStyle, CellStyle poiStyle) {
			this.birtStyle = birtStyle;
			this.poiStyle = poiStyle;
		}
	}

	private Workbook workbook;
	private FontManager fm;
	private List<StylePair> styles = new ArrayList<>();
	private StyleManagerUtils smu;
	private CSSEngine cssEngine;
	// private Logger log;
	private Locale locale;

	/**
	 * @param workbook   The workbook for which styles are being tracked.
	 * @param styleStack A style stack, to allow cells to inherit properties from
	 *                   container elements.
	 * @param log        Logger to be used during processing.
	 * @param smu        Set of functions for carrying out conversions between BIRT
	 *                   and POI.
	 * @param cssEngine  BIRT CSS Engine for creating BIRT styles.
	 */
	public StyleManager(Workbook workbook, Logger log, StyleManagerUtils smu, CSSEngine cssEngine, Locale locale) {
		this.workbook = workbook;
		this.fm = new FontManager(cssEngine, workbook, smu);
		// this.log = log;
		this.smu = smu;
		this.cssEngine = cssEngine;
		this.locale = locale;
	}

	public FontManager getFontManager() {
		return fm;
	}

	public CSSEngine getCssEngine() {
		return cssEngine;
	}

	static int COMPARE_CSS_PROPERTIES[] = { StyleConstants.STYLE_TEXT_ALIGN, StyleConstants.STYLE_BACKGROUND_COLOR,
			StyleConstants.STYLE_BORDER_TOP_STYLE, StyleConstants.STYLE_BORDER_TOP_WIDTH,
			StyleConstants.STYLE_BORDER_TOP_COLOR, StyleConstants.STYLE_BORDER_LEFT_STYLE,
			StyleConstants.STYLE_BORDER_LEFT_WIDTH, StyleConstants.STYLE_BORDER_LEFT_COLOR,
			StyleConstants.STYLE_BORDER_RIGHT_STYLE, StyleConstants.STYLE_BORDER_RIGHT_WIDTH,
			StyleConstants.STYLE_BORDER_RIGHT_COLOR, StyleConstants.STYLE_BORDER_BOTTOM_STYLE,
			StyleConstants.STYLE_BORDER_BOTTOM_WIDTH, StyleConstants.STYLE_BORDER_BOTTOM_COLOR,
			StyleConstants.STYLE_WHITE_SPACE, StyleConstants.STYLE_VERTICAL_ALIGN, };

	/**
	 * Test whether two BIRT styles are equivalent, as far as the attributes
	 * understood by POI are concerned. <br/>
	 * Every attribute tested in this method must be used in the construction of the
	 * CellStyle in createStyle.
	 *
	 * @param style1 The first BIRT style to be compared.
	 * @param style2 The second BIRT style to be compared.
	 * @return true if style1 and style2 would produce identical CellStyles if
	 *         passed to createStyle.
	 */
	private boolean stylesEquivalent(BirtStyle style1, BirtStyle style2) {

		// System.out.println( "style1: " + style1 );
		// System.out.println( "style2: " + style2 );

		for (int i = 0; i < COMPARE_CSS_PROPERTIES.length; ++i) {
			int prop = COMPARE_CSS_PROPERTIES[i];
			CSSValue value1 = style1.getProperty(prop);
			CSSValue value2 = style2.getProperty(prop);
			if (!StyleManagerUtils.objectsEqual(value1, value2)) {
				// System.out.println( "Differ on " + i + " because " + value1 + " != " + value2
				// );
				return false;
			}
		}
		

		// Number format
		// Font
		if (!StyleManagerUtils.objectsEqual(style1.getProperty(BirtStyle.TEXT_ROTATION),
				style2.getProperty(BirtStyle.TEXT_ROTATION)) || !StyleManagerUtils.dataFormatsEquivalent(
				(DataFormatValue) style1.getProperty(StyleConstants.STYLE_DATA_FORMAT),
				(DataFormatValue) style2.getProperty(StyleConstants.STYLE_DATA_FORMAT)) || !FontManager.fontsEquivalent(style1, style2)) {
			// System.out.println( "Differ on font" );
			return false;
		}
		return true;
	}

	/**
	 * Create a new POI CellStyle based upon a BIRT style.
	 *
	 * @param birtStyle The BIRT style to base the CellStyle upon.
	 * @return The CellStyle whose attributes are described by the BIRT style.
	 */
	private CellStyle createStyle(BirtStyle birtStyle) {
		CellStyle poiStyle = workbook.createCellStyle();
		// Font
		Font font = fm.getFont(birtStyle);
		if (font != null) {
			poiStyle.setFont(font);
		}
		// Alignment
		// poiStyle.setAlignment(smu.poiAlignmentFromBirtAlignment(birtStyle.getString(StyleConstants.STYLE_TEXT_ALIGN)));
		HorizontalAlignment alignment = smu
				.poiAlignmentFromBirtAlignment(birtStyle.getString(StyleConstants.STYLE_TEXT_ALIGN));
		poiStyle.setAlignment(alignment);
		// Background colour
		smu.addBackgroundColourToStyle(workbook, poiStyle, birtStyle.getString(StyleConstants.STYLE_BACKGROUND_COLOR));
		// Top border
		smu.applyBorderStyle(workbook, poiStyle, BorderSide.TOP,
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_COLOR),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_STYLE),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH));
		// Left border
		smu.applyBorderStyle(workbook, poiStyle, BorderSide.LEFT,
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH));
		// Right border
		smu.applyBorderStyle(workbook, poiStyle, BorderSide.RIGHT,
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
		// Bottom border
		smu.applyBorderStyle(workbook, poiStyle, BorderSide.BOTTOM,
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE),
				birtStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
		// Number format
		smu.applyNumberFormat(workbook, birtStyle, poiStyle, locale);
		// Whitespace/wrap
		if (CSSConstants.CSS_PRE_VALUE.equals(birtStyle.getString(StyleConstants.STYLE_WHITE_SPACE))) {
			poiStyle.setWrapText(true);
		}
		// Vertical alignment
		if (CSSConstants.CSS_TOP_VALUE.equals(birtStyle.getString(StyleConstants.STYLE_VERTICAL_ALIGN))) {
			poiStyle.setVerticalAlignment(VerticalAlignment.TOP);
		} else if (CSSConstants.CSS_MIDDLE_VALUE.equals(birtStyle.getString(StyleConstants.STYLE_VERTICAL_ALIGN))) {
			poiStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		} else if (CSSConstants.CSS_BOTTOM_VALUE.equals(birtStyle.getString(StyleConstants.STYLE_VERTICAL_ALIGN))) {
			poiStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
		}
		// Rotation
		CSSValue rotation = birtStyle.getProperty(BirtStyle.TEXT_ROTATION);
		if (rotation instanceof FloatValue) {
			poiStyle.setRotation((short) ((FloatValue) rotation).getFloatValue());
		}

		styles.add(new StylePair(birtStyle.clone(), poiStyle));
		return poiStyle;
	}

	public CellStyle getStyle(BirtStyle birtStyle) {
		for (StylePair stylePair : styles) {
			if (stylesEquivalent(birtStyle, stylePair.birtStyle)) {
				// System.err.println( "Equivalent :\n\t" + birtStyle + "\n\t" +
				// stylePair.birtStyle );
				return stylePair.poiStyle;
			}
		}

		return createStyle(birtStyle);
	}

	private BirtStyle birtStyleFromCellStyle(CellStyle source) {
		for (StylePair stylePair : styles) {
			if (source.equals(stylePair.poiStyle)) {
				return stylePair.birtStyle.clone();
			}
		}

		return new BirtStyle(cssEngine);
	}

	/**
	 * Given a POI CellStyle, add border definitions to it and obtain a CellStyle
	 * (from the cache or newly created) based upon that.
	 *
	 * @param source             The POI CellStyle to form the base style.
	 * @param borderStyleBottom  The BIRT style of the bottom border.
	 * @param borderWidthBottom  The BIRT with of the bottom border.
	 * @param borderColourBottom The BIRT colour of the bottom border.
	 * @param borderStyleLeft    The BIRT style of the left border.
	 * @param borderWidthLeft    The BIRT width of the left border.
	 * @param borderColourLeft   The BIRT colour of the left border.
	 * @param borderStyleRight   The BIRT width of the right border.
	 * @param borderWidthRight   The BIRT colour of the right border.
	 * @param borderColourRight  The BIRT style of the right border.
	 * @param borderStyleTop     The BIRT style of the top border.
	 * @param borderWidthTop     The BIRT width of the top border.
	 * @param borderColourTop    The BIRT colour of the top border.
	 * @return A POI CellStyle equivalent to the source CellStyle with all the
	 *         defined borders added to it.
	 */
	public CellStyle getStyleWithBorders(CellStyle source, CSSValue borderStyleBottom, CSSValue borderWidthBottom,
			CSSValue borderColourBottom, CSSValue borderStyleLeft, CSSValue borderWidthLeft, CSSValue borderColourLeft,
			CSSValue borderStyleRight, CSSValue borderWidthRight, CSSValue borderColourRight, CSSValue borderStyleTop,
			CSSValue borderWidthTop, CSSValue borderColourTop) {

		BirtStyle birtStyle = birtStyleFromCellStyle(source);
		if ((borderStyleBottom != null) && (borderWidthBottom != null) && (borderColourBottom != null)) {
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE, borderStyleBottom);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH, borderWidthBottom);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR, borderColourBottom);
		}
		if ((borderStyleLeft != null) && (borderWidthLeft != null) && (borderColourLeft != null)) {
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE, borderStyleLeft);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH, borderWidthLeft);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR, borderColourLeft);
		}
		if ((borderStyleRight != null) && (borderWidthRight != null) && (borderColourRight != null)) {
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE, borderStyleRight);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH, borderWidthRight);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR, borderColourRight);
		}
		if ((borderStyleTop != null) && (borderWidthTop != null) && (borderColourTop != null)) {
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_TOP_STYLE, borderStyleTop);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH, borderWidthTop);
			birtStyle.setProperty(StyleConstants.STYLE_BORDER_TOP_COLOR, borderColourTop);
		}

		CellStyle newStyle = getStyle(birtStyle);
		return newStyle;
	}

	/**
	 * Return a POI style created by combining a POI style with a BIRT style, where
	 * the BIRT style overrides the values in the POI style.
	 *
	 * @param source         The POI style that represents the base style.
	 * @param birtExtraStyle The BIRT style to overlay on top of the POI style.
	 * @return A POI style representing the combination of source and
	 *         birtExtraStyle.
	 */
	public CellStyle getStyleWithExtraStyle(CellStyle source, IStyle birtExtraStyle) {

		BirtStyle birtStyle = birtStyleFromCellStyle(source);

		for (int i = 0; i < BirtStyle.NUMBER_OF_STYLES; ++i) {
			CSSValue value = birtExtraStyle.getProperty(i);
			if (value != null) {
				birtStyle.setProperty(i, value);
			}
		}

		CellStyle newStyle = getStyle(birtStyle);
		return newStyle;
	}

}
