/*******************************************************************************
 * Copyright (c) 2008, 2025 Actuate Corporation and others
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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Font configuration handler
 *
 * @since 3.3
 *
 */
public class FontConfigHandler extends DefaultHandler {

	private FontMappingConfig config;

	private Stack<ParseState> states = new Stack<ParseState>();

	/**
	 * Constructor
	 *
	 * @param config font mapping configuration
	 */
	public FontConfigHandler(FontMappingConfig config) {
		this.config = config;
		this.states.push(new RootState());
	}

	/**
	 * @throws SAXException
	 */
	@Override
	public void startElement(String uri, String localName, String rawName, Attributes attrs) throws SAXException {
		ParseState state = states.peek();
		state = state.startElement(rawName);
		state.parseAttrs(attrs);
		states.push(state);
	}

	/**
	 * @throws SAXException
	 */
	@Override
	public void endElement(String uri, String localName, String rawName) throws SAXException {
		ParseState elementState = states.pop();
		elementState.end();
		ParseState state = states.peek();
		state.endElement(elementState);
	}

	private static class ParseState {

		/**
		 * Parse element attribute
		 *
		 * @param attrs attribute
		 */
		public void parseAttrs(Attributes attrs) {
		}

		/**
		 * Handle the start of element
		 *
		 * @param tagName tag name
		 * @return parsed element instance
		 */
		public ParseState startElement(String tagName) {
			return AnyElementState.instance;
		}

		/**
		 * Handle the end of element
		 *
		 * @param state parsed element instance
		 */
		public void endElement(ParseState state) {
		}

		public void end() {
		}

		private static class AnyElementState extends ParseState {

			private static AnyElementState instance = new AnyElementState();

			@Override
			public ParseState startElement(String tagName) {
				return instance;
			}
		}
	}

	private final static String TAG_FONT_PATHS = "font-paths"; //$NON-NLS-1$
	private final static String TAG_PATH = "path"; //$NON-NLS-1$
	private final static String ATTR_PATH = "path"; //$NON-NLS-1$
	private final static String TAG_FONT_ALIASES = "font-aliases"; //$NON-NLS-1$
	private final static String TAG_FONT_MAPPINGS = "font-mappings"; //$NON-NLS-1$
	private final static String TAG_MAPPING = "mapping"; //$NON-NLS-1$
	private final static String ATTR_NAME = "name"; //$NON-NLS-1$
	private final static String ATTR_FONT_FAMILY = "font-family"; //$NON-NLS-1$
	private final static String TAG_FONT_ENCODINGS = "font-encodings"; //$NON-NLS-1$
	private final static String TAG_ENCODING = "encoding"; //$NON-NLS-1$
	private final static String ATTR_ENCODING = "encoding"; //$NON-NLS-1$

	private final static String TAG_SEARCH_SEQUENCES = "search-sequences"; //$NON-NLS-1$
	private final static String TAG_SEQUENCE = "sequence"; //$NON-NLS-1$
	private final static String ATTR_LOCALE = "locale"; //$NON-NLS-1$
	private final static String TAG_CATALOG = "catalog"; //$NON-NLS-1$

	private final static String TAG_ALL_FONTS = "all-fonts"; //$NON-NLS-1$
	private final static String TAG_BLOCK = "block"; //$NON-NLS-1$
	private final static String ATTR_RANGE_START = "range-start"; //$NON-NLS-1$
	private final static String ATTR_RANGE_END = "range-end"; //$NON-NLS-1$
	private final static String ATTR_START = "start"; //$NON-NLS-1$
	private final static String ATTR_END = "end"; //$NON-NLS-1$

	private final static String TAG_COMPOSITE_FONT = "composite-font"; //$NON-NLS-1$
	private final static String TAG_FONT = "font"; //$NON-NLS-1$
	private final static String ATTR_CATALOG = "catalog"; //$NON-NLS-1$
	private static final String VALUE_DEFAULT_BLOCK = "default"; //$NON-NLS-1$
	private final static String TAG_CHARACTER = "character"; //$NON-NLS-1$
	private final static String ATTR_VALUE = "value"; //$NON-NLS-1$

	private final static String TAG_FONT_KERNING = "kerning-and-ligatures"; //$NON-NLS-1$
	private final static String ATTR_KERNING_ENABLED = "enabled"; //$NON-NLS-1$

	private class RootState extends ParseState {

		@Override
		public ParseState startElement(String tagName) {
			String tagValue = tagName.toLowerCase();
			if (TAG_FONT.equals(tagValue)) {
				return new FontState();
			}
			return super.startElement(tagName);
		}
	}

	private class FontState extends ParseState {

		@Override
		public ParseState startElement(String tagName) {
			String tagValue = tagName.toLowerCase();
			if (TAG_FONT_PATHS.equals(tagValue)) {
				return new FontPathsState();
			}
			if (TAG_FONT_ALIASES.equals(tagValue) || TAG_FONT_MAPPINGS.equals(tagValue)) {
				return new FontAliasesState();
			}
			if (TAG_FONT_ENCODINGS.equals(tagValue)) {
				return new FontEncodingsState();
			}
			if (TAG_SEARCH_SEQUENCES.equals(tagValue)) {
				return new SearchSequencesState(config.searchSequences);
			}
			if (TAG_ALL_FONTS.equals(tagValue)) {
				return new AllFontState();
			}
			if (TAG_COMPOSITE_FONT.equals(tagValue)) {
				return new CompositeFontState();
			}
			if (TAG_FONT_KERNING.equals(tagValue)) {
				return new FontKerningState();
			}
			return super.startElement(tagName);
		}
	}

	private class FontPathsState extends ParseState {

		@Override
		public ParseState startElement(String tagName) {
			String tagValue = tagName.toLowerCase();
			if (TAG_PATH.equals(tagValue)) {
				return new PathState();
			}
			return super.startElement(tagName);
		}

		class PathState extends ParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String path = getStringValue(attrs, ATTR_PATH);
				if (path != null) {
					config.addFontPath(path);
				}
			}
		}
	}

	private class FontKerningState extends ParseState {

		@Override
		public void parseAttrs(Attributes attrs) {
			String kerning = getStringValue(attrs, ATTR_KERNING_ENABLED);
			if (kerning != null) {
				config.setFontKerning(Boolean.valueOf(kerning));
			}
		}
	}

	private class FontAliasesState extends ParseState {

		@Override
		public ParseState startElement(String tagName) {
			String tagValue = tagName.toLowerCase();
			if (TAG_MAPPING.equals(tagValue)) {
				return new AliasState();
			}
			return super.startElement(tagName);
		}

		class AliasState extends ParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String fontName = getStringValue(attrs, ATTR_NAME);
				String fontFamily = getStringValue(attrs, ATTR_FONT_FAMILY);
				if (fontName != null && fontFamily != null) {
					config.addFontAlias(fontName, fontFamily);
				}
			}
		}
	}

	private class FontEncodingsState extends ParseState {

		@Override
		public ParseState startElement(String tagName) {
			String tagValue = tagName.toLowerCase();
			if (TAG_ENCODING.equals(tagValue)) {
				return new EncodingState();
			}
			return super.startElement(tagName);
		}

		class EncodingState extends ParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String fontFamily = getStringValue(attrs, ATTR_FONT_FAMILY);
				String fontEncoding = getStringValue(attrs, ATTR_ENCODING);
				if (fontEncoding != null && fontFamily != null) {
					config.addFontEncoding(fontFamily, fontEncoding);
				}
			}
		}
	}

	private class SearchSequencesState extends ParseState {

		private HashMap<String, String[]> sequences;

		SearchSequencesState(HashMap<String, String[]> sequences) {
			this.sequences = sequences;
		}

		@Override
		public ParseState startElement(String tagName) {
			String tagValue = tagName.toLowerCase();
			if (TAG_SEQUENCE.equals(tagValue)) {
				return new SequenceState();
			}
			return super.startElement(tagName);
		}

		class SequenceState extends ParseState {

			private String locale;
			private ArrayList<String> catalogs = new ArrayList<String>();

			@Override
			public void parseAttrs(Attributes attrs) {
				locale = getStringValue(attrs, ATTR_LOCALE);
			}

			@Override
			public ParseState startElement(String tagName) {
				String tagValue = tagName.toLowerCase();
				if (TAG_CATALOG.equals(tagValue)) {
					return new CatalogState();
				}
				return super.startElement(tagName);
			}

			@Override
			public void end() {
				if (locale != null && !catalogs.isEmpty()) {
					sequences.put(locale, catalogs.toArray(new String[] {}));
				}
			}

			class CatalogState extends ParseState {

				@Override
				public void parseAttrs(Attributes attrs) {
					String name = getStringValue(attrs, ATTR_NAME);
					if (name != null) {
						if (!catalogs.contains(name)) {
							catalogs.add(name);
						}
					}
				}
			}
		}
	}

	private class AllFontState extends ParseState {

		LinkedHashMap<String, LinkedHashMap<String, ArrayList<CharSegment>>> fonts = new LinkedHashMap<String, LinkedHashMap<String, ArrayList<CharSegment>>>();

		void addComponent(String fontName, int start, int end, String fontFamily) {
			LinkedHashMap<String, ArrayList<CharSegment>> font = fonts.get(fontName);
			if (font == null) {
				font = new LinkedHashMap<String, ArrayList<CharSegment>>();
				fonts.put(fontName, font);
			}
			ArrayList<CharSegment> charSegs = font.get(fontFamily);
			if (charSegs == null) {
				charSegs = new ArrayList<CharSegment>();
				font.put(fontFamily, charSegs);
			}
			charSegs.add(new CharSegment(start, end, fontFamily));
		}

		CompositeFontConfig createCompositeFont(String name, Map<String, ArrayList<CharSegment>> fonts) {
			CompositeFontConfig config = new CompositeFontConfig(name);
			Iterator<Entry<String, ArrayList<CharSegment>>> iter = fonts.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, ArrayList<CharSegment>> entry = iter.next();
				String fontName = entry.getKey();
				Collection<CharSegment> charSegs = entry.getValue();
				CharSegment[] segment = charSegs.toArray(new CharSegment[] {});
				CharSegment.sort(segment);
				config.addFont(fontName, null);
				config.addCharSegment(fontName, segment);
			}
			return config;
		}

		@Override
		public void end() {
			Iterator<Entry<String, LinkedHashMap<String, ArrayList<CharSegment>>>> iter = fonts.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, LinkedHashMap<String, ArrayList<CharSegment>>> entry = iter.next();
				String fontName = entry.getKey();
				LinkedHashMap<String, ArrayList<CharSegment>> fonts = entry
						.getValue();
				CompositeFontConfig fontConfig = createCompositeFont(fontName, fonts);
				if (!fontConfig.getAllFonts().isEmpty()) {
					config.addCompositeFont(fontConfig);
				}
			}

		}

		@Override
		public ParseState startElement(String tagName) {
			String tagValue = tagName.toLowerCase();
			if (TAG_BLOCK.equals(tagValue)) {
				return new BlockState();
			}
			return super.startElement(tagName);
		}

		class BlockState extends ParseState {

			int rangeStart;
			int rangeEnd;

			@Override
			public void parseAttrs(Attributes attrs) {
				rangeStart = getHexValue(attrs, ATTR_RANGE_START, -1);
				rangeEnd = getHexValue(attrs, ATTR_RANGE_END, -1);
			}

			@Override
			public ParseState startElement(String tagName) {
				if (rangeStart != -1 && rangeEnd != -1) {
					String tagValue = tagName.toLowerCase();
					if (TAG_MAPPING.equals(tagValue)) {
						return new MappingState();
					}
				}
				return super.startElement(tagName);
			}

			class MappingState extends ParseState {

				@Override
				public void parseAttrs(Attributes attrs) {
					String name = getStringValue(attrs, ATTR_NAME);
					String fontFamily = getStringValue(attrs, ATTR_FONT_FAMILY);
					if (name != null && fontFamily != null) {
						addComponent(name, rangeStart, rangeEnd, fontFamily);
					}
				}
			}
		}
	}

	private class CompositeFontState extends ParseState {

		private String fontName;
		private String defaultFont;
		private LinkedHashSet<String> allFonts = new LinkedHashSet<String>();
		private HashMap<String, String> fontCatalogs = new HashMap<String, String>();
		private HashMap<Integer, String> fontCharacters = new HashMap<Integer, String>();
		private LinkedHashMap<String, ArrayList<CharSegment>> fontBlocks = new LinkedHashMap<String, ArrayList<CharSegment>>();

		private void addCharacter(String fontFamily, int ch) {
			fontCharacters.put(Integer.valueOf(ch), fontFamily);
		}

		private void addBlock(String fontFamily, int start, int end) {
			allFonts.add(fontFamily);
			ArrayList<CharSegment> list = fontBlocks.get(fontFamily);
			if (list == null) {
				list = new ArrayList<CharSegment>();
				fontBlocks.put(fontFamily, list);
			}
			list.add(new CharSegment(start, end, fontFamily));
		}

		private void addFont(String fontFamily, String catalog) {
			allFonts.add(fontFamily);
			fontCatalogs.put(fontFamily, catalog);
		}

		private CompositeFontConfig createCompositeFont() {
			if (fontName != null) {
				CompositeFontConfig fontConfig = new CompositeFontConfig(fontName);
				fontConfig.setDefaultFont(defaultFont);
				// the character always has the highest priority
				ArrayList<CharSegment> characters = new ArrayList<CharSegment>();
				Iterator<Entry<Integer, String>> iterChars = fontCharacters.entrySet().iterator();
				while (iterChars.hasNext()) {
					Entry<Integer, String> entry = iterChars.next();
					int ch = entry.getKey().intValue();
					String fontFamily = entry.getValue();
					characters.add(new CharSegment(ch, ch, fontFamily));
				}
				if (!characters.isEmpty()) {
					CharSegment[] seg = characters.toArray(new CharSegment[] {});
					CharSegment.sort(seg);
					fontConfig.setSpecialCharacters(seg);
				}
				// add the font
				fontConfig.allFonts.addAll(allFonts);
				// add the font catalog
				fontConfig.fontCatalogs.putAll(fontCatalogs);
				// append the font indexes
				Iterator<Entry<String, ArrayList<CharSegment>>> iterBlocks = fontBlocks.entrySet().iterator();
				while (iterBlocks.hasNext()) {
					Entry<String, ArrayList<CharSegment>> entry = iterBlocks.next();
					String fontFamily = entry.getKey();
					Collection<CharSegment> blocks = entry.getValue();
					CharSegment[] seg = blocks.toArray(new CharSegment[] {});
					CharSegment.normalize(seg);
					fontConfig.addCharSegment(fontFamily, seg);
				}
				return fontConfig;
			}
			return null;
		}

		@Override
		public void parseAttrs(Attributes attrs) {
			fontName = getStringValue(attrs, ATTR_NAME);
			if (fontName != null) {
				defaultFont = getStringValue(attrs, ATTR_FONT_FAMILY);
			}
		}

		@Override
		public void end() {
			CompositeFontConfig fontConfig = createCompositeFont();
			if (fontConfig != null) {
				config.addCompositeFont(fontConfig);
			}
		}

		@Override
		public ParseState startElement(String tagName) {
			if (fontName != null) {
				String tagValue = tagName.toLowerCase();
				if (TAG_FONT.equals(tagValue)) {
					return new FontState();
				}
				if (TAG_BLOCK.equals(tagValue)) {
					return new BlockState();
				}
				if (TAG_CHARACTER.equals(tagValue)) {
					return new CharacterState();
				}
			}
			return super.startElement(tagName);
		}

		private class FontState extends ParseState {

			private String fontFamily;
			private String catalog;

			@Override
			public void parseAttrs(Attributes attrs) {
				fontFamily = getStringValue(attrs, ATTR_FONT_FAMILY);
				catalog = getStringValue(attrs, ATTR_CATALOG);
				if (fontFamily != null) {
					addFont(fontFamily, catalog);
				}
			}

			@Override
			public ParseState startElement(String tagName) {
				if (fontFamily != null) {
					String tagValue = tagName.toLowerCase();
					if (TAG_BLOCK.equals(tagValue)) {
						return new BlockState();
					}
				}
				return super.startElement(tagName);
			}

			private class BlockState extends ParseState {

				@Override
				public void parseAttrs(Attributes attrs) {
					int start = getIntValue(attrs, ATTR_START, -1);
					int end = getIntValue(attrs, ATTR_END, start);
					if (start != -1) {
						addBlock(fontFamily, start, end);
					}
				}
			}
		}

		private class BlockState extends ParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String fontFamily = getStringValue(attrs, ATTR_FONT_FAMILY);
				if (fontFamily != null) {
					String name = getStringValue(attrs, ATTR_NAME);
					if (VALUE_DEFAULT_BLOCK.equals(name)) {
						if (defaultFont == null) {
							defaultFont = fontFamily;
						}
					} else {
						int rangeStart = getHexValue(attrs, ATTR_RANGE_START, -1);
						int rangeEnd = getHexValue(attrs, ATTR_RANGE_END, -1);
						if (rangeStart != -1 && rangeEnd != -1) {
							addBlock(fontFamily, rangeStart, rangeEnd);
						}
					}
				}
			}
		}

		private class CharacterState extends ParseState {

			@Override
			public void parseAttrs(Attributes attrs) {
				String fontFamily = getStringValue(attrs, ATTR_FONT_FAMILY);
				if (fontFamily != null) {
					String value = getStringValue(attrs, ATTR_VALUE);
					if (value != null) {
						int ch = getCharValue(value);
						if (ch != -1) {
							addCharacter(fontFamily, ch);
						}
					}
				}
			}
		}
	}

	private String getStringValue(Attributes attrs, String name) {
		String value = attrs.getValue(name);
		if (value != null) {
			value = value.trim();
			if (value.length() != 0) {
				return value;
			}
		}
		return null;
	}

	private int getIntValue(Attributes attrs, String attrName, int defaultValue) {
		String value = attrs.getValue(attrName);
		if (value == null) {
			return defaultValue;
		}
		return Integer.parseInt(value);
	}

	private int getHexValue(Attributes attrs, String attrName, int defaultValue) {
		String value = attrs.getValue(attrName);
		if (value == null) {
			return defaultValue;
		}
		return Integer.parseInt(value, 16);
	}

	private int getCharValue(String value) {
		if (value.length() == 1) {
			return value.charAt(0);
		} else if (value.matches("\\\\u\\p{XDigit}{4}")) //$NON-NLS-1$
		{
			String unicode = value.substring(2);
			return Integer.parseInt(unicode, 16);
		}
		return -1;
	}

}
