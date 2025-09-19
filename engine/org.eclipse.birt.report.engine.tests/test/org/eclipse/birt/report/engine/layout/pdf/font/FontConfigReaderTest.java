/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.openpdf.text.Font;
import org.openpdf.text.pdf.BaseFont;

import junit.framework.TestCase;

public class FontConfigReaderTest extends TestCase {

	private FontMappingManager fontMappingManager = null;

	public void testFontMappingHandler()
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		fontMappingManager = getFontMappingManager("fontsConfig3.xml", Locale.CHINA);
		assertEquals("Courier", fontMappingManager.getAliasedFont("serif"));
		assertEquals(16, fontMappingManager.getFontEncodings().size());
		assertEquals(2, fontMappingManager.getSearchSequences().size());
		CompositeFont cf = fontMappingManager.getCompositeFont(FontMappingManager.FONT_NAME_ALL_FONTS);
		assertTrue(cf != null);
		// special characters
		assertEquals("Zapfdingbats", cf.getUsedFont('a'));
		// character which can be display in both Chinese and Japanese (Chinese "Hand")
		assertEquals("STSong-Light", cf.getUsedFont('\u624b'));
		// character which can be displayed in western scripts.
		assertEquals("Times-Roman", cf.getUsedFont('b'));

		fontMappingManager = getFontMappingManager("fontsConfig3.xml", Locale.JAPANESE);
		assertEquals("Courier", fontMappingManager.getAliasedFont("serif"));
		assertEquals(16, fontMappingManager.getFontEncodings().size());
		cf = fontMappingManager.getCompositeFont(FontMappingManager.FONT_NAME_ALL_FONTS);
		assertTrue(cf != null);
		// special characters
		assertEquals("Zapfdingbats", cf.getUsedFont('a'));
		// character which can be display in both Chinese and Japanese
		assertEquals("HeiseiMin-W3", cf.getUsedFont('\u624b'));
		// character which can be displayed in western scripts.
		assertEquals("Times-Roman", cf.getUsedFont('b'));
	}

	public void testFontMapWhenAllFontsNotDefined()
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		fontMappingManager = getFontMappingManager("fontsConfig2.xml");

		// alias: defined; composite-font: defined; block: defined; character:
		// defined by the block font.
		assertTrue(isMappedTo('1', "alias1", "Courier"));
		// alias: defined; composite-font: defined; block: defined; character:
		// not defined by the block font.
		assertTrue(isMappedTo((char) 0xe81, "alias1", "Courier"));
		// alias: defined; composite-font: defined; block: not; character:
		// not defined by the logical font.
		assertTrue(isMappedTo((char) 0x80, "alias1", "Times Roman"));
		// alias: defined; composite-font: not; character: defined by the
		// logical font.
		assertTrue(isMappedTo('1', "alias2", "Helvetica"));
		// The Mapping should be case-insensitive
		assertTrue(isMappedTo('1', "Alias2", "Helvetica"));
		// alias: not; composite-font: defined; block: defined; character:
		// defined by the block font.
		assertTrue(isMappedTo('1', "Symbol", "Courier"));
		// alias: not; composite-font: defined; block: defined; character:
		// not defined by the block font.
		assertTrue(isMappedTo((char) 0xe81, "Symbol", "Courier"));
		// alias: not; composite-font: defined; block: not; character:
		// not defined by the logical font.
		assertTrue(isMappedTo((char) 0x80, "Symbol", "Times Roman"));
		// alias: not; composite-font: not; character: defined by the logical
		// font.
		assertTrue(isMappedTo('1', "Helvetica", "Helvetica"));
	}

	public void testFontMapWhenAllFontsDefined()
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		fontMappingManager = getFontMappingManager("fontsConfig1.xml");

		// alias defined, composite font defined, block defined, and character
		// is defined by the block font.
		assertTrue(isMappedTo('1', "alias1", "Courier"));
		// alias defined, composite font not defined, and character
		// is defined by the logical font.
		assertTrue(isMappedTo('1', "alias2", "Helvetica"));

		// alias not defined, composite font defined, and character
		// is defined by the block font.
		assertTrue(isMappedTo('1', "Symbol", "Courier"));
		// alias not defined, composite font not defined, and character
		// is defined by the logical font.
		assertTrue(isMappedTo('1', "Helvetica", "Helvetica"));

		// alias: defined; composite-font: defined; block: defined;
		assertTrue(isMappedTo((char) 0xe81, "Symbol", "Courier"));
		// alias: defined; composite-font: defined; block: not defined;
		// all-fonts:not define the block
		assertTrue(isMappedTo((char) 0xff01, "Symbol", "Times Roman"));
	}

	public void testCompatibleParser()
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		fontMappingManager = getFontMappingManager("fontsConfigCompatible.xml");
		Map fontAliases = fontMappingManager.getFontAliases();
		assertEquals(2, fontAliases.size());
		assertEquals("font alias in font-aliases", fontAliases.get("font alias overridden"));
		assertEquals("font alias in font-mappings", fontAliases.get("font alias not overridden"));

		Map compositeFonts = fontMappingManager.getCompositeFonts();
		assertEquals(3, compositeFonts.size());
		CompositeFont overriddenFont = fontMappingManager.getCompositeFont("font overridden");
		assertEquals("font in composite-font", overriddenFont.getUsedFont((char) 0));
		assertEquals("font in composite-font", overriddenFont.getUsedFont((char) 0x7F));

		CompositeFont notOverriddenFont = fontMappingManager.getCompositeFont("font not overridden");
		assertEquals("font in all-fonts", notOverriddenFont.getUsedFont((char) 0));
		assertEquals("font in all-fonts", notOverriddenFont.getUsedFont((char) 0x7F));
		CompositeFont allFonts = fontMappingManager.getCompositeFont("all-fonts");
		assertEquals("Times-Roman", allFonts.getUsedFont((char) 0));
		assertEquals("Times-Roman", allFonts.getUsedFont((char) 0x7F));
		assertEquals("Helvetica", allFonts.getUsedFont((char) 0x80));
		assertEquals("Helvetica", allFonts.getUsedFont((char) 0xFF));
		assertEquals(null, allFonts.getDefaultFont());
	}

	public void testFontConfigParser()
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		fontMappingManager = getFontMappingManager("fontsConfigParser.xml");
		Map fontAliases = fontMappingManager.getFontAliases();
		assertEquals(1, fontAliases.size());
		assertEquals("Times-Roman", fontAliases.get("test alias"));

		Map compositeFonts = fontMappingManager.getCompositeFonts();
		assertEquals(1, compositeFonts.size());
		CompositeFont testFont = (CompositeFont) compositeFonts.get("test font");
		assertEquals("Symbol", testFont.getUsedFont((char) 0));
		assertEquals("Symbol", testFont.getUsedFont((char) 0x7F));
		assertEquals("Helvetica", testFont.getDefaultFont());
	}

	public void testCharacterFontMapping()
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		fontMappingManager = getFontMappingManager("fontsConfig_character.xml");
		String testFontForCharacterMapping = "testForCharacterMapping";
		assertTrue(isMappedTo('a', testFontForCharacterMapping, "Helvetica"));
		assertTrue(isMappedTo('b', testFontForCharacterMapping, "Times Roman"));
		assertTrue(isMappedTo('c', testFontForCharacterMapping, "Courier"));
		assertTrue(isMappedTo('a', "testForDefaultFont", "Symbol"));
	}

	private void testPriority(final String testDir, String format) {
		FontMappingManagerFactory factory = new FontMappingManagerFactory() {

			@Override
			protected URL getConfigURL(String configName) {
				URL fileURL = getClass().getResource(
						"/org/eclipse/birt/report/engine/layout/pdf/font/" + testDir + "/" + configName + ".xml");
				return fileURL;
			}
		};

		FontMappingManager manager = factory.getFontMappingManager(format, Locale.CHINESE);
		testMergedProperty(manager.getFontAliases(), 8);
		testMergedProperty(manager.getFontEncodings(), 9);

		Map compositeFonts = manager.getCompositeFonts();
		assertEquals(3, compositeFonts.size());

		CompositeFont compositeFont = manager.getCompositeFont("common");
		assertEquals("Courier", compositeFont.getUsedFont((char) 0));
		assertEquals("Courier", compositeFont.getUsedFont((char) 0xe80));
		compositeFont = manager.getCompositeFont("higher priority");
		assertEquals("Courier", compositeFont.getUsedFont((char) 0));
		compositeFont = manager.getCompositeFont("lower priority");
		assertEquals("Courier", compositeFont.getUsedFont((char) 0));
	}

	private void testMergedProperty(Map map, int size) {
		assertEquals(size, map.size());
		assertEquals("higher priority", map.get("common"));
		assertEquals("higher priority", map.get("higher priority"));
		assertEquals("lower priority", map.get("lower priority"));
	}

	private boolean isMappedTo(char c, String from, String to) {
		FontHandler handler = new FontHandler(fontMappingManager, new String[] { from }, Font.NORMAL, true);
		BaseFont font = handler.getMappedFont(c);
		return hasName(font, to);
	}

	private boolean hasName(BaseFont font, String fontName) {
		String[][] familyFontNames = font.getFullFontName();
		for (int i = 0; i < familyFontNames.length; i++) {
			for (int j = 0; j < familyFontNames[i].length; j++) {
				if (fontName.equals(familyFontNames[i][j])) {
					return true;
				}
			}
		}
		return false;
	}

	private FontMappingManager getFontMappingManager(String configFile)
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		return getFontMappingManager(configFile, Locale.CHINA);
	}

	private FontMappingManager getFontMappingManager(String configFile, Locale locale)
			throws IOException, FactoryConfigurationError, ParserConfigurationException, SAXException {
		URL url = getURL(configFile);
		long start = System.currentTimeMillis();
		FontMappingConfig config = new FontConfigReader().parseConfig(url);
		long end = System.currentTimeMillis();
		System.out.println("load font config in " + url + " cost " + (end - start) + "ms");

		return FontMappingManagerFactory.getInstance().createFontMappingManager(config, locale);
	}

	private URL getURL(String configFile) {
		URL fileURL = getClass().getResource("/org/eclipse/birt/report/engine/layout/pdf/font/" + configFile);
		return fileURL;
	}
}
