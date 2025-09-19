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
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.openpdf.text.pdf.BaseFont;

/**
 * Manager to handle the font mapping configuration
 *
 * @since 3.3
 *
 */
public class FontMappingManager {

	/** all fonts key */
	public static final String FONT_NAME_ALL_FONTS = "all-fonts";

	/** default fonts */
	public static final String DEFAULT_FONT = BaseFont.TIMES_ROMAN;

	private FontMappingManagerFactory factory;

	private FontMappingManager parent;

	private Map<String, String> fontEncodings = new HashMap<String, String>();

	private Map<String, String[]> searchSequences = new HashMap<String, String[]>();

	/** The font-family replacement */
	private Map<String, String> fontAliases = new HashMap<String, String>();

	/** Usage of the advanced font kerning and ligatures */
	private boolean fontKerningAndLigaturesUsage = false;

	/**
	 * composite fonts
	 */
	private Map<String, CompositeFont> compositeFonts = new HashMap<String, CompositeFont>();

	FontMappingManager(FontMappingManagerFactory factory, FontMappingManager parent, FontMappingConfig config,
			Locale locale) {
		this.factory = factory;
		this.parent = parent;
		if (parent != null) {
			this.searchSequences.putAll(parent.getSearchSequences());
			this.fontAliases.putAll(parent.getFontAliases());
			this.fontEncodings.putAll(parent.getFontEncodings());
			this.compositeFonts.putAll(parent.getCompositeFonts());
			if (!this.fontKerningAndLigaturesUsage)
				this.fontKerningAndLigaturesUsage = parent.fontKerningAndLigaturesUsage;
		}
		this.fontEncodings.putAll(config.fontEncodings);
		this.searchSequences.putAll(config.searchSequences);
		this.fontAliases.putAll(config.fontAliases);
		if (!this.fontKerningAndLigaturesUsage)
			this.fontKerningAndLigaturesUsage = config.fontKerningAndLigaturesUsage;

		String[] sequence = getSearchSequence(locale);
		Iterator<Entry<String, CompositeFontConfig>> iter = config.compositeFonts.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, CompositeFontConfig> entry = iter.next();
			String fontName = entry.getKey();
			CompositeFontConfig fontConfig = entry.getValue();
			CompositeFont font = factory.createCompositeFont(this, fontConfig, sequence);
			compositeFonts.put(fontName, font);
		}
	}

	/**
	 * Get the parent font mapping manager
	 *
	 * @return the parent font mapping manager
	 */
	public FontMappingManager getParent() {
		return parent;
	}

	/**
	 * Get the font encodings
	 *
	 * @return the font encodings
	 */
	public Map<String, String> getFontEncodings() {
		return fontEncodings;
	}

	/**
	 * Get the font aliases
	 *
	 * @return the font aliases
	 */
	public Map<String, String> getFontAliases() {
		return fontAliases;
	}

	/**
	 * Get the search sequences
	 *
	 * @return the search sequences
	 */
	public Map<String, String[]> getSearchSequences() {
		return searchSequences;
	}

	/**
	 * Get the composite fonts
	 *
	 * @return the composite fonts
	 */
	public Map<String, CompositeFont> getCompositeFonts() {
		return compositeFonts;
	}

	/**
	 * Get the usage of advanced font kerning and ligatures
	 *
	 * @return the usage of advanced font kerning and ligatures
	 */
	public boolean useFontKerningAndLigatures() {
		return fontKerningAndLigaturesUsage;
	}

	protected String[] getSearchSequence(Locale locale) {
		StringBuilder sb = new StringBuilder();
		String[] localeKeys = new String[3];
		localeKeys[2] = sb.append(locale.getLanguage()).toString();
		localeKeys[1] = sb.append('_').append(locale.getCountry()).toString();
		localeKeys[0] = sb.append('_').append(locale.getVariant()).toString();
		for (int i = 0; i < localeKeys.length; i++) {
			String[] sequence = searchSequences.get(localeKeys[i]);
			if (sequence != null) {
				return sequence;
			}
		}
		return null;
	}

	/**
	 * Get the composite font based at font name
	 *
	 * @param name font name
	 * @return the composite font based at font name
	 */
	public CompositeFont getCompositeFont(String name) {
		return compositeFonts.get(name);
	}

	/**
	 * Get the default physical font
	 *
	 * @param c special character of the font
	 * @return the default physical font
	 */
	public String getDefaultPhysicalFont(char c) {
		CompositeFont compositeFont = compositeFonts.get(FONT_NAME_ALL_FONTS);
		if (compositeFont != null) {
			String font = compositeFont.getUsedFont(c);
			if (font != null) {
				return font;
			}
			return compositeFont.getDefaultFont();
		}
		return null;
	}

	/**
	 * Get the aliased font
	 *
	 * @param fontAlias font alias name
	 * @return the aliased font
	 */
	public String getAliasedFont(String fontAlias) {
		String alias = fontAliases.get(fontAlias.toLowerCase());
		if (alias != null) {
			return alias;
		}
		return fontAlias;
	}

	/**
	 * Creates iText BaseFont with the given font family name.
	 *
	 * @param fontFamily the specified font family name.
	 * @param fontStyle  font style
	 * @return the created BaseFont.
	 */
	public BaseFont createFont(String fontFamily, int fontStyle) {
		return factory.createFont(fontFamily, fontStyle);
	}
}
