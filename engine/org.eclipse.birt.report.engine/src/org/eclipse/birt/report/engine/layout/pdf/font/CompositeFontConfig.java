/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Composite font configuration
 *
 * @since 3.3
 *
 */
public class CompositeFontConfig {

	/**
	 * the composite font name.
	 */
	String fontName;
	/**
	 * the default font used by this composite font
	 */
	String defaultFont;
	/**
	 * the special handing characters.
	 *
	 * those definition has the highest priority and should be search in first.
	 */
	CharSegment[] specialCharacters;
	/**
	 * font used by this composite font. It doesn't includes the font used by the
	 * special character.
	 */
	LinkedHashSet<String> allFonts = new LinkedHashSet<String>();

	/**
	 * Each font has a catalog, the catalog is used by the search sequence to change
	 * the searching priority
	 */
	HashMap<String, String> fontCatalogs = new HashMap<String, String>();
	/**
	 * char index for the font defined in the all fonts.
	 */
	HashMap<String, CharSegment[]> charSegments = new HashMap<String, CharSegment[]>();

	CompositeFontConfig(String fontName) {
		this.fontName = fontName;
	}

	CompositeFontConfig(CompositeFontConfig config) {
		fontName = config.fontName;
		defaultFont = config.defaultFont;
		fontCatalogs.putAll(config.fontCatalogs);
		charSegments.putAll(config.charSegments);
	}

	/**
	 * Get font name
	 *
	 * @return font name
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * Set default font
	 *
	 * @param defaultFont default font name
	 */
	public void setDefaultFont(String defaultFont) {
		this.defaultFont = defaultFont;
	}

	/**
	 * Get the default font name
	 *
	 * @return the default font name
	 */
	public String getDefaultFont() {
		return defaultFont;
	}

	/**
	 * Add font
	 *
	 * @param font    font name
	 * @param catalog font catalog name
	 */
	public void addFont(String font, String catalog) {
		if (!allFonts.contains(font)) {
			allFonts.add(font);
		}
		if (catalog != null) {
			fontCatalogs.put(font, catalog);
		}
	}

	/**
	 * Get all composite fonts
	 *
	 * @return all composite fonts
	 */
	public Collection<String> getAllFonts() {
		return allFonts;
	}

	/**
	 * Get font from font catalog
	 *
	 * @param catalog font catalog name
	 * @return fonts
	 */
	public Collection<String> getFontByCatalog(String catalog) {
		Collection<String> fonts = new ArrayList<String>();
		Iterator<String> iter = allFonts.iterator();
		while (iter.hasNext()) {
			String fontName = iter.next();
			String fontCatalog = fontCatalogs.get(fontName);
			if (catalog == fontCatalog || (catalog != null && catalog.equals(fontCatalog)))

			{
				fonts.add(fontName);
			}
		}
		return fonts;
	}

	/**
	 * Add character segment
	 *
	 * @param fontName font name
	 * @param segment  character segment
	 */
	public void addCharSegment(String fontName, CharSegment[] segment) {
		charSegments.put(fontName, segment);
	}

	/**
	 * Set special characters
	 *
	 * @param segment character segments
	 */
	public void setSpecialCharacters(CharSegment[] segment) {
		this.specialCharacters = segment;
	}

	/**
	 * Get the special characters
	 *
	 * @return the special characters
	 */
	public CharSegment[] getSpecialCharacters() {
		return this.specialCharacters;
	}

	/**
	 * Get a character segment
	 *
	 * @param name segment name
	 * @return the character segment
	 */
	public CharSegment[] getCharSegment(String name) {
		return charSegments.get(name);
	}

	/**
	 * Get all character segments
	 *
	 * @return all character segments
	 */
	public Map<String, CharSegment[]> getAllCharSegments() {
		return charSegments;
	}

	/**
	 * Merge font configuration to font catalog and character segments
	 *
	 * @param config
	 */
	public void merge(CompositeFontConfig config) {
		fontCatalogs.putAll(config.fontCatalogs);
		charSegments.putAll(config.charSegments);
	}
}
