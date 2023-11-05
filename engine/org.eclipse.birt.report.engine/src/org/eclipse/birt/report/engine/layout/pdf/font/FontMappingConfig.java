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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Font mapping configuration class
 *
 * @since 3.3
 *
 */
public class FontMappingConfig {

	protected Set<String> fontPaths = new HashSet<String>();

	/** The font-family replacement */
	protected HashMap<String, String> fontAliases = new HashMap<String, String>();

	/** The encoding for the fonts */
	protected HashMap<String, String> fontEncodings = new HashMap<String, String>();

	/** the global sequences defined for composite fonts */
	protected HashMap<String, String[]> searchSequences = new HashMap<String, String[]>();

	/**
	 * composite fonts is constructed by multiple physical fonts which may cover
	 * large amount of glyph
	 */
	protected HashMap<String, CompositeFontConfig> compositeFonts = new HashMap<String, CompositeFontConfig>();

	/**
	 * Constructor
	 */
	public FontMappingConfig() {
	}

	/**
	 * merge the font configuration to the existing font setup
	 *
	 * @param config font mapping configuration
	 */
	public void merge(FontMappingConfig config) {
		fontPaths.addAll(config.fontPaths);

		// merge alias fonts, special handling in addFontAlias()
		for (Map.Entry<String, String> fontAliasEntry : config.fontAliases.entrySet()) {
			this.addFontAlias(fontAliasEntry.getKey(), fontAliasEntry.getValue());
		}
		fontEncodings.putAll(config.fontEncodings);
		searchSequences.putAll(config.searchSequences);

		// merge the composite fonts
		for (Map.Entry<String, CompositeFontConfig> compositeFontEntry : config.compositeFonts.entrySet()) {
			String fontName = compositeFontEntry.getKey();
			CompositeFontConfig newConfig = compositeFontEntry.getValue();
			CompositeFontConfig oldConfig = compositeFonts.get(fontName);
			if (oldConfig != null) {
				oldConfig.merge(newConfig);
			} else {
				compositeFonts.put(fontName, new CompositeFontConfig(newConfig));
			}
		}
	}

	/**
	 * Add the font path to the path map
	 *
	 * @param fontPath font path
	 */
	public void addFontPath(String fontPath) {
		fontPaths.add(fontPath);
	}

	/**
	 * The font-family replacement
	 *
	 * @param alias    alias name of the font
	 * @param fontName original font name
	 */
	public void addFontAlias(String alias, String fontName) {
		fontAliases.put(alias.toLowerCase(), fontName);
	}

	/**
	 * The encoding for the fonts
	 *
	 * @param fontName     font name
	 * @param fontEncoding font encoding
	 */
	public void addFontEncoding(String fontName, String fontEncoding) {
		fontEncodings.put(fontName, fontEncoding);
	}

	/**
	 * Add search sequence of font
	 *
	 * @param localeKey local key
	 * @param sequence  search sequence
	 */
	public void addSearchSequence(String localeKey, String[] sequence) {
		searchSequences.put(localeKey, sequence);
	}

	/**
	 * Get the map of the search sequences of the fonts
	 *
	 * @return Return the map of the search sequences of the fontsReturn
	 */
	public HashMap<String, String[]> getSearchSequences() {
		return searchSequences;
	}

	/**
	 * Add the composite font
	 *
	 * @param fontConfig composite font configuration
	 */
	public void addCompositeFont(CompositeFontConfig fontConfig) {
		compositeFonts.put(fontConfig.fontName, fontConfig);
	}

	/**
	 * Get all composite fonts
	 *
	 * @return Return all composite fonts
	 */
	public Collection<CompositeFontConfig> getAllCompositeFonts() {
		return compositeFonts.values();
	}
}
