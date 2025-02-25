/*******************************************************************************
 * Copyright (c) 2004, 2008, 2025 Actuate Corporation and others
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

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValueList;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.LayoutProcessor;

/**
 * the font handler, which maps fontFamily, fontStyle, fontWeight properties to
 * the TrueType font.
 */
public class FontHandler {

	/** The font family names */
	private String[] fontFamilies = null;

	/** the style of the font, should be BOLD, ITALIC, BOLDITALIC or NORMAL */
	private int fontStyle = Font.NORMAL;

	/** the font weight from 100-900. 400 is NORMAL, and 700 is bold */
	private int fontWeight = 400;

	/** the font-size property */
	private float fontSize = 0f;

	/** the selected BaseFont */
	private BaseFont bf = null;

	/** the flag to show if the BaseFont has been changed */
	private boolean isFontChanged = false;

	/** the flag to show whether we need to simulate bold/italic font or not */
	private boolean simulation = false;

	private FontMappingManager fontManager = null;

	private Map<String, BaseFont> fonts = new HashMap<String, BaseFont>();

	/**
	 * the characters which prefer to use the font of their previous character.
	 */
	private static final String WEAK_FONT_CHARS = " ,.";

	/**
	 * The constructor
	 *
	 * @param fontManager
	 *
	 * @param textContent      the textContent whose font need to be handled
	 * @param fontSubstitution If it set to false, we needn't check if the character
	 *                         exists in the selected font.
	 */
	public FontHandler(FontMappingManager fontManager, ITextContent textContent, boolean fontSubstitution) {
		this.fontManager = fontManager;

		IStyle style = textContent.getComputedStyle();

		CSSValueList families = (CSSValueList) style.getProperty(StyleConstants.STYLE_FONT_FAMILY);
		this.fontFamilies = new String[families.getLength()];
		for (int i = 0; i < fontFamilies.length; i++) {
			fontFamilies[i] = families.item(i).getCssText();
		}

		this.fontWeight = PropertyUtil.parseFontWeight(style.getProperty(StyleConstants.STYLE_FONT_WEIGHT));

		if (CSSConstants.CSS_OBLIQUE_VALUE.equals(style.getFontStyle())
				|| CSSConstants.CSS_ITALIC_VALUE.equals(style.getFontStyle())) {
			this.fontStyle |= Font.ITALIC;
		}

		if (PropertyUtil.isBoldFont(fontWeight)) {
			this.fontStyle |= Font.BOLD;
		}

		this.fontSize = PropertyUtil.getDimensionValueConsiderDpi(style.getProperty(StyleConstants.STYLE_FONT_SIZE),
				textContent) / PDFConstants.LAYOUT_TO_PDF_RATIO;

		if (!fontSubstitution) {
			enableKerningAndLigatures();
			for (int i = 0; i < fontFamilies.length; i++) {
				String fontName = fontManager.getAliasedFont(fontFamilies[i]);
				bf = fontManager.createFont(fontName, fontStyle);
				if (bf != null) {
					return;
				}
			}
			bf = fontManager.createFont(FontMappingManager.DEFAULT_FONT, fontStyle);
		}
	}

	/**
	 * The constructor
	 *
	 * @param fontManager      font manager
	 * @param fontFamilies     font families
	 * @param fontStyle        font style
	 * @param fontSubstitution font substitution
	 */
	public FontHandler(FontMappingManager fontManager, String fontFamilies[], int fontStyle, boolean fontSubstitution) {
		this.fontManager = fontManager;

		// splits font-family list
		this.fontFamilies = fontFamilies;
		this.fontStyle = fontStyle;

		this.fontSize = fontSize / PDFConstants.LAYOUT_TO_PDF_RATIO;

		if (!fontSubstitution) {
			enableKerningAndLigatures();
			for (int i = 0; i < fontFamilies.length; i++) {
				String fontName = fontManager.getAliasedFont(fontFamilies[i]);
				bf = fontManager.createFont(fontName, fontStyle);
				if (bf != null) {
					return;
				}
			}
			bf = fontManager.createFont(FontMappingManager.DEFAULT_FONT, fontStyle);
		}
	}

	/**
	 * Gets the FontInfo Object.
	 *
	 * @return Return the font object
	 */
	public FontInfo getFontInfo() {
		return new FontInfo(bf, fontSize, fontStyle, fontWeight, simulation);
	}

	/**
	 * Check is font changed
	 *
	 * @return Return the check of changed font
	 */
	public boolean isFontChanged() {
		return isFontChanged;
	}

	/**
	 * Selects a proper font for a character.
	 *
	 * @param character character to validate the font
	 *
	 * @return true: we find a font which can be used to display the character.
	 *         false: no font can display the character.
	 */
	public boolean selectFont(char character) {
		assert (fontManager != null);
		// FIXME: code review : return null when no mapped font defined the
		// character so that charExist only need to be invoked once.
		BaseFont candidateFont = getMappedFont(character);
		assert (candidateFont != null);
		if (bf == candidateFont) {
			isFontChanged = false;
		} else {
			isFontChanged = true;
			bf = candidateFont;
			simulation = needSimulate();
		}
		return candidateFont.charExists(character);
	}

	/**
	 * Gets the BaseFont object to display the given character.
	 *
	 * The search sequence is:
	 * <li>try the font family defined in the families to see if one can be used to
	 * display the character.</li>
	 * <li>try to use the default font to display the character.</li>
	 * <li>if none of the above success, return NULL for the character.</li>
	 *
	 * @param c the given character.
	 * @return the BaseFont. it always return a font.
	 */
	public BaseFont getMappedFont(char c) {
		if (WEAK_FONT_CHARS.indexOf(c) != -1) {
			if (bf != null && bf.charExists(c)) {
				return bf;
			}
		}
		// search in the font family to find one to display the character
		enableKerningAndLigatures();
		for (int i = 0; i < fontFamilies.length; i++) {
			// Translate the font alias to font family
			String fontFamily = fontManager.getAliasedFont(fontFamilies[i]);

			// test if it is a composite font
			CompositeFont cf = fontManager.getCompositeFont(fontFamily);
			if (cf != null) {
				// once it is created by the composite font, we needn't test if
				// the character can be displayed by the returned font as the
				// returned font is
				// either defined by the user or test through the base font.
				String usedFont = cf.getUsedFont(c);
				if (usedFont != null) {
					BaseFont bf = createBaseFont(usedFont);
					if (bf != null) {
						return bf;
					}
				}
			} else {
				BaseFont bf = createBaseFont(fontFamily);
				if (bf != null && bf.charExists(c)) {
					return bf;
				}
			}
		}
		// Use the default font to display this character
		CompositeFont df = fontManager.getCompositeFont(FontMappingManager.FONT_NAME_ALL_FONTS);
		if (df != null) {
			String usedFont = df.getUsedFont(c);
			if (usedFont != null) {
				BaseFont bf = createBaseFont(usedFont);
				if (bf != null) {
					return bf;
				}
			}
		}
		// it's the last choice to use the default fonts
		BaseFont bf = createBaseFont(FontMappingManager.DEFAULT_FONT);
		if (bf == null) {
			throw new NullPointerException(
					"Failed to create " + FontMappingManager.DEFAULT_FONT + " which is not allow");
		}
		return bf;
	}

	private BaseFont createBaseFont(String physicalFont) {
		BaseFont font = fonts.get(physicalFont);
		if (font == null) {
			if (fonts.containsKey(physicalFont)) {
				return null;
			}
			font = fontManager.createFont(physicalFont, fontStyle);
			fonts.put(physicalFont, font);
		}
		return font;
	}

	/**
	 * If the BaseFont can NOT find the correct physical glyph, we need to simulate
	 * the proper style for the font. The "simulate" flag will be set if we need to
	 * simulate it.
	 */
	private boolean needSimulate() {
		if (fontStyle == Font.NORMAL) {
			return false;
		}

		String[][] fullNames = bf.getFullFontName();
		String fullName = getEnglishName(fullNames);
		String lcf = fullName.toLowerCase();

		int fs = Font.NORMAL;
		if (lcf.indexOf("bold") != -1) {
			fs |= Font.BOLD;
		}
		if (lcf.indexOf("italic") != -1 || lcf.indexOf("oblique") != -1) {
			fs |= Font.ITALIC;
		}
		if ((fontStyle & Font.BOLDITALIC) == fs) {
			if (fontWeight > 400 && fontWeight != 700) {
				// not a regular bold font.
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the English font name or font family name from the given naming array
	 *
	 * @param names the naming array
	 * @return the English name
	 */
	private String getEnglishName(String[][] names) {
		String tmp = null;
		for (int i = 0; i < names.length; i++) {
			if ("0".equals(names[i][2])) //$NON-NLS-1$
			{
				return names[i][3];
			}
			// FIXME: code review : check the logic.
			if ("1033".equals(names[i][2])) //$NON-NLS-1$
			{
				tmp = names[i][3];
			}
			if ("".equals(names[i][2])) //$NON-NLS-1$
			{
				tmp = names[i][3];
			}
		}

		return tmp;
	}

	/**
	 * Enable the font mode to handle advanced kerning and ligatures
	 *
	 * @since 4.19
	 */
	private void enableKerningAndLigatures() {
		if (fontManager.useFontKerningAdvanced() && !LayoutProcessor.isEnabled()) {
			LayoutProcessor.enableKernLiga();
		}
	}
}
