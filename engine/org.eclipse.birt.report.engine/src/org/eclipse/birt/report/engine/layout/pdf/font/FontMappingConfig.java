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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FontMappingConfig {

	protected Set fontPaths = new HashSet();

	/** The font-family replacement */
	protected HashMap fontAliases = new HashMap();;

	/** The encoding for the fonts */
	protected HashMap fontEncodings = new HashMap();;

	/** the global sequences defined for composite fonts */
	protected HashMap searchSequences = new HashMap();

	/**
	 * composite fonts is constructed by multiple physical fonts which may cover
	 * large amount of glyph
	 */
	protected HashMap compositeFonts = new HashMap();

	public FontMappingConfig() {
	}

	public void merge(FontMappingConfig config) {
		fontPaths.addAll(config.fontPaths);
		fontAliases.putAll(config.fontAliases);
		fontEncodings.putAll(config.fontEncodings);
		searchSequences.putAll(config.searchSequences);

		// merge the composite fonts
		Iterator iter = config.compositeFonts.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String fontName = (String) entry.getKey();
			CompositeFontConfig newConfig = (CompositeFontConfig) entry.getValue();
			CompositeFontConfig oldConfig = (CompositeFontConfig) compositeFonts.get(fontName);
			if (oldConfig != null) {
				oldConfig.merge(newConfig);
			} else {
				compositeFonts.put(fontName, new CompositeFontConfig(newConfig));
			}
		}
	}

	public void addFontPath(String fontPath) {
		fontPaths.add(fontPath);
	}

	/** The font-family replacement */
	public void addFontAlias(String alias, String fontName) {
		fontAliases.put(alias, fontName);
	}

	/** The encoding for the fonts */
	public void addFontEncoding(String fontName, String fontEncoding) {
		fontEncodings.put(fontName, fontEncoding);
	}

	public void addSearchSequence(String localeKey, String[] sequence) {
		searchSequences.put(localeKey, sequence);
	}

	public Map getSearchSequences() {
		return searchSequences;
	}

	public void addCompositeFont(CompositeFontConfig fontConfig) {
		compositeFonts.put(fontConfig.fontName, fontConfig);
	}

	public Collection getAllCompositeFonts() {
		return compositeFonts.values();
	}
}
