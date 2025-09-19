/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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

import java.util.Collection;
import java.util.LinkedHashSet;

import org.openpdf.text.Font;
import org.openpdf.text.pdf.BaseFont;

/**
 *
 * The composite font is defined by multiple physical fonts.
 * <p>
 * Each font can be used to display different character. The physical fonts are
 * owned by different catalogs. The catalogs defines the search sequence of
 * those fonts.
 * <p>
 * The composite font can be full indexed. The full indexed means the char
 * segments describes all the character in the font, so it is no need to call
 * each font's charExits() to see if it can display a character. It always has
 * more better performance.
 */
public class CompositeFont {

	CompositeFontConfig config;

	CompositeFont parent;

	/**
	 * the internal fonts used by this composite font, the order is the prefer order
	 */
	String[] usedFonts;

	/**
	 * if the composite font is full indexed.
	 */
	boolean fullIndexed;

	CharSegment[] specialCharacters;
	/**
	 * the index for internal fonts, in the same order with usedFonts
	 */
	CharSegment[][] fontsIndex;
	/**
	 * base fonts used to test if the char exits, in the same order with usedFonts
	 */
	BaseFont[] baseFonts;

	/**
	 * index of all the chars in the composite font. It is only used when
	 * fullIndexed.
	 */
	CharSegment[] fullIndex;

	/**
	 * Constructor
	 *
	 * @param manager  font mapping manager
	 * @param config   composite font configuration
	 * @param sequence font sequence
	 */
	public CompositeFont(FontMappingManager manager, CompositeFontConfig config, String[] sequence) {
		FontMappingManager parentManager = manager.getParent();
		if (parentManager != null) {
			this.parent = parentManager.getCompositeFont(config.fontName);
		}
		this.config = config;
		this.specialCharacters = config.getSpecialCharacters();
		// create the fonts follows the sequence
		LinkedHashSet<String> fonts = new LinkedHashSet<String>();
		if (sequence != null) {
			for (int i = 0; i < sequence.length; i++) {
				Collection<String> catalogFonts = config.getFontByCatalog(sequence[i]);
				if (catalogFonts != null) {
					fonts.addAll(catalogFonts);
				}
			}
		}
		fonts.addAll(config.getAllFonts());
		usedFonts = fonts.toArray(new String[] {});

		fullIndexed = true;
		fontsIndex = new CharSegment[usedFonts.length][];
		for (int i = 0; i < usedFonts.length; i++) {
			fontsIndex[i] = config.getCharSegment(usedFonts[i]);
			if (fontsIndex[i] == null) {
				fullIndexed = false;
			}
		}
		if (fullIndexed) {
			fullIndex = CharSegment.merge(fontsIndex);
		} else {
			baseFonts = new BaseFont[usedFonts.length];
			for (int i = 0; i < baseFonts.length; i++) {
				baseFonts[i] = manager.createFont(usedFonts[i], Font.NORMAL);
			}
		}
	}

	/**
	 * Get the font name
	 *
	 * @return font name
	 */
	public String getFontName() {
		return config.fontName;
	}

	/**
	 * Get the default font name
	 *
	 * @return default font name
	 */
	public String getDefaultFont() {
		if (config.defaultFont != null) {
			return config.defaultFont;
		}
		if (parent != null) {
			return parent.getDefaultFont();
		}
		return null;
	}

	/**
	 * Get the used font based on font specific character
	 *
	 * @param ch font specific character
	 * @return font name
	 */
	public String getUsedFont(char ch) {
		String usedFont = findUsedFont(ch);
		if (usedFont != null) {
			return usedFont;
		}
		return getDefaultFont();
	}

	protected String findUsedFont(char ch) {
		if (specialCharacters != null) {
			int index = CharSegment.search(specialCharacters, ch);
			if (index != -1) {
				return specialCharacters[index].name;
			}
		}
		if (fullIndexed) {
			int index = CharSegment.search(fullIndex, ch);
			if (index != -1) {
				return fullIndex[index].name;
			}
		} else {
			// search one by one
			for (int i = 0; i < usedFonts.length; i++) {
				if (fontsIndex[i] != null) {
					if (CharSegment.search(fontsIndex[i], ch) != -1) {
						return usedFonts[i];
					}
				} else if (baseFonts[i] != null) {
					if (baseFonts[i].charExists(ch)) {
						return usedFonts[i];
					}
				}
			}
		}
		if (parent != null) {
			return parent.getUsedFont(ch);
		}
		return null;
	}
}
