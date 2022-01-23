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
	LinkedHashSet allFonts = new LinkedHashSet();

	/**
	 * Each font has a catalog, the catalog is used by the search sequence to change
	 * the searching priority
	 */
	HashMap fontCatalogs = new HashMap();
	/**
	 * char index for the font defined in the all fonts.
	 */
	HashMap charSegments = new HashMap();

	CompositeFontConfig(String fontName) {
		this.fontName = fontName;
	}

	CompositeFontConfig(CompositeFontConfig config) {
		fontName = config.fontName;
		defaultFont = config.defaultFont;
		fontCatalogs.putAll(config.fontCatalogs);
		charSegments.putAll(config.charSegments);
	}

	public String getFontName() {
		return fontName;
	}

	public void setDefaultFont(String defaultFont) {
		this.defaultFont = defaultFont;
	}

	public String getDefaultFont() {
		return defaultFont;
	}

	public void addFont(String font, String catalog) {
		if (!allFonts.contains(font)) {
			allFonts.add(font);
		}
		if (catalog != null) {
			fontCatalogs.put(font, catalog);
		}
	}

	public Collection getAllFonts() {
		return allFonts;
	}

	public Collection getFontByCatalog(String catalog) {
		Collection fonts = new ArrayList();
		Iterator iter = allFonts.iterator();
		while (iter.hasNext()) {
			String fontName = (String) iter.next();
			String fontCatalog = (String) fontCatalogs.get(fontName);
			if (catalog == fontCatalog || (catalog != null && catalog.equals(fontCatalog)))

			{
				fonts.add(fontName);
			}
		}
		return fonts;
	}

	public void addCharSegment(String fontName, CharSegment[] segment) {
		charSegments.put(fontName, segment);
	}

	public void setSpecialCharacters(CharSegment[] segment) {
		this.specialCharacters = segment;
	}

	public CharSegment[] getSpecialCharacters() {
		return this.specialCharacters;
	}

	public CharSegment[] getCharSegment(String name) {
		return (CharSegment[]) charSegments.get(name);
	}

	public Map getAllCharSegments() {
		return charSegments;
	}

	public void merge(CompositeFontConfig config) {
		fontCatalogs.putAll(config.fontCatalogs);
		charSegments.putAll(config.charSegments);
	}
}
