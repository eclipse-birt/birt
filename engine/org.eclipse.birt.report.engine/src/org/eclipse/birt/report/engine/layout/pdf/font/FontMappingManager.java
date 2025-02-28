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

import com.lowagie.text.pdf.BaseFont;

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

	private Map fontEncodings = new HashMap();

	private Map searchSequences = new HashMap();

	/** The font-family replacement */
	private Map fontAliases = new HashMap();

	/** Usage of the advanced font kerning and ligatures */
	private boolean fontKerningAdvancedUsage = false;

	/**
	 * composite fonts
	 */
	private Map compositeFonts = new HashMap();

	FontMappingManager(FontMappingManagerFactory factory, FontMappingManager parent, FontMappingConfig config,
			Locale locale) {
		this.factory = factory;
		this.parent = parent;
		if (parent != null) {
			this.searchSequences.putAll(parent.getSearchSequences());
			this.fontAliases.putAll(parent.getFontAliases());
			this.fontEncodings.putAll(parent.getFontEncodings());
			this.compositeFonts.putAll(parent.getCompositeFonts());
			if (!this.fontKerningAdvancedUsage)
				this.fontKerningAdvancedUsage = parent.fontKerningAdvancedUsage;
		}
		this.fontEncodings.putAll(config.fontEncodings);
		this.searchSequences.putAll(config.searchSequences);
		this.fontAliases.putAll(config.fontAliases);
		if (!this.fontKerningAdvancedUsage)
			this.fontKerningAdvancedUsage = config.fontKerningAdvancedUsage;

		String[] sequence = getSearchSequence(locale);
		Iterator iter = config.compositeFonts.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String fontName = (String) entry.getKey();
			CompositeFontConfig fontConfig = (CompositeFontConfig) entry.getValue();
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
	public Map getFontEncodings() {
		return fontEncodings;
	}

	/**
	 * Get the font aliases
	 *
	 * @return the font aliases
	 */
	public Map getFontAliases() {
		return fontAliases;
	}

	/**
	 * Get the search sequences
	 *
	 * @return the search sequences
	 */
	public Map getSearchSequences() {
		return searchSequences;
	}

	/**
	 * Get the composite fonts
	 *
	 * @return the composite fonts
	 */
	public Map getCompositeFonts() {
		return compositeFonts;
	}

	/**
	 * Get the usage of advanced font kerning and ligatures
	 *
	 * @return the usage of advanced font kerning and ligatures
	 */
	public boolean useFontKerningAdvanced() {
		return fontKerningAdvancedUsage;
	}

	protected String[] getSearchSequence(Locale locale) {
		StringBuilder sb = new StringBuilder();
		String[] localeKeys = new String[3];
		localeKeys[2] = sb.append(locale.getLanguage()).toString();
		localeKeys[1] = sb.append('_').append(locale.getCountry()).toString();
		localeKeys[0] = sb.append('_').append(locale.getVariant()).toString();
		for (int i = 0; i < localeKeys.length; i++) {
			String[] sequence = (String[]) searchSequences.get(localeKeys[i]);
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
		return (CompositeFont) compositeFonts.get(name);
	}

	/**
	 * Get the default physical font
	 *
	 * @param c special character of the font
	 * @return the default physical font
	 */
	public String getDefaultPhysicalFont(char c) {
		CompositeFont compositeFont = (CompositeFont) compositeFonts.get(FONT_NAME_ALL_FONTS);
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
		String alias = (String) fontAliases.get(fontAlias.toLowerCase());
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
