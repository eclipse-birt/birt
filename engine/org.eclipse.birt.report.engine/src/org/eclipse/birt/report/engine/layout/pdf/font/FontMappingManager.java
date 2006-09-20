/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.css.CSSValueList;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

public class FontMappingManager
{
	/** The possible generic font names */
	private static final String SERIF = "serif"; //$NON-NLS-1$
	private static final String SANS_SERIF = "sans-serif"; //$NON-NLS-1$
	private static final String CURSIVE = "cursive";
	private static final String MONOSPACE = "monospace"; //$NON-NLS-1$
	private static final String FANTASY = "fantasy"; //$NON-NLS-1$
	
	/** default font for generic family "serif" */
	private static final String DEFAULT_SERIF_FONT = BaseFont.TIMES_ROMAN;

	/** default fonts for generic family "sans-serif" */
	private static final String DEFAULT_SANS_SERIF_FONT = BaseFont.HELVETICA;

	/** default fonts for generic family "cursive" */
	private static final String  DEFAULT_CURSIVE_FONT = BaseFont.ZAPFDINGBATS;

	/** default fonts for generic family "monospace" */
	private static final String DEFAULT_MONOSPACE_FONT = BaseFont.COURIER;

	/** default fonts for generic family "fantasy" */
	private static final String DEFAULT_FANTASY_FONT = BaseFont.TIMES_ROMAN;
	
	/** The number of unicode blocks */
	private static final int UNICODE_BLOCK_NUMBER = 154;
	
	/** default fonts */
	private static final String DEFAULT_FONT = BaseFont.TIMES_ROMAN;
	
	/** The font-family replacement */
	private HashMap fontMapping = null;
	
	/** The encoding for the fonts */
	private HashMap fontEncoding = null;
	
	protected static Logger logger = Logger.getLogger( FontMappingManager.class
			.getName( ) );

	/** 
	 * The array maintaining the font list which can display the character
	 * in the given unicode block.
	 */
	private LinkedList[] fontNodes = new LinkedList[UNICODE_BLOCK_NUMBER];
	
	
	FontMappingManager( )
	{
		initializeFontMapping( );
		initializeFontEncoding( );
	}
	
	protected void initializeFontMapping()
	{
		fontMapping = new HashMap();
		
		fontMapping.put(SERIF, DEFAULT_SERIF_FONT);
		fontMapping.put(SANS_SERIF, DEFAULT_SANS_SERIF_FONT);
		fontMapping.put(CURSIVE, DEFAULT_CURSIVE_FONT);
		fontMapping.put(MONOSPACE, DEFAULT_MONOSPACE_FONT);
		fontMapping.put(FANTASY, DEFAULT_FANTASY_FONT);
	}
	
	protected void initializeFontEncoding()
	{
		fontEncoding = new HashMap();
		
		fontEncoding.put(BaseFont.TIMES_ROMAN, BaseFont.WINANSI);
		fontEncoding.put(BaseFont.HELVETICA, BaseFont.WINANSI);
		fontEncoding.put(BaseFont.COURIER, BaseFont.WINANSI);
		fontEncoding.put(BaseFont.SYMBOL, BaseFont.WINANSI);
		fontEncoding.put(BaseFont.ZAPFDINGBATS, BaseFont.WINANSI);		
		fontEncoding.put("Times", BaseFont.WINANSI);	
	}
	
	
	/**
	 * Gets the index of the FontMappings array for the given character.
	 * @param c				the character.
	 * @return				the index.
	 */
	private int getBlockIndex( char c )
	{
		int low = 0;
		int high = UNICODE_BLOCK_NUMBER - 1;
		while (low <= high) 
		{
			int mid = (low + high) / 2;
			int minVal = blockEdges[mid*2];
			int maxVal = blockEdges[mid*2 + 1];
			
			if ( c < minVal )
				high = mid - 1;
			else if ( c > maxVal )
				low = mid + 1;
			else
				return mid;
		}
		return -1;
	}
	
	/**
	 * Gets the BaseFont object to display the given character.
	 * It will
	 * <li> traverse the customer defined font list by the specified order,
	 * try to display the character with the BaseFont using the font family name.
	 * If the font family name is a generic font, or an alias of another font family,
	 * the font family name will be replaced according to the mapping defined in the
	 * fontMapping object. 
	 * </li>
	 * <li> If the above fails, the unicode block containing the given character will
	 * be retrived. Then we will try each font defined for this block to display the 
	 * character.
	 * </li>
	 * <li>	If the above also fails, we can not find a font to display the given 
	 * character. null will be returned.
	 * </li>
	 * @param c							the given character.	
	 * @param fontFamilies				the customer defined font list.
	 * @param fontStyle					the style of the font.
	 * @return							the BaseFont. If we fail to find one, return null.
	 */
	public BaseFont getMappedFont(char c, CSSValueList fontFamilies, int fontStyle)
	{
		String fontName = null;
		BaseFont candidateFont = null;
		// Finds the font in the customer given font list.
		for (int i = 0; i < fontFamilies.getLength(); i++) 
		{
			String fontFamilyName = fontFamilies.item(i).getCssText();
			// Gets the font alias or if the font name is a generic font, use the mapped font.
			// otherwise, use the original font name.
			fontName = getConcreteFont( fontFamilyName );
			candidateFont = getBaseFont( fontName, c, fontStyle );
			if (null != candidateFont) 
				return candidateFont;
		}
		// No fonts in the customer given font list can display this character, 
		// search the fontMapping table to try to find a font.
		int blockIndex = getBlockIndex(c);
		if ( blockIndex < 0 )
		{
			return null;
		}
		// There is no font defined for this unicode block.
		if ( null == fontNodes[blockIndex] )
		{
			return null;
		}
		Iterator i = fontNodes[blockIndex].iterator( );
		while ( i.hasNext( ) )
		{
			fontName = (String) i.next( );
			candidateFont = getBaseFont( fontName, c, fontStyle );
			if ( null != candidateFont )
				return candidateFont;
		}
		// No font is suitable to the character. we will use the DEFAULT_FONT.
		return null;
	}
	

	/**
	 * Gets the BaseFont defined to display the miss character.
	 * 
	 * @param fontStyle					the style of the font.
	 * @return							the default BaseFont object.
	 */
	public BaseFont getDefaultFont ( int fontStyle )
	{
		return createFont( DEFAULT_FONT, fontStyle );
	}
	
	/**
	 * Adds a font family to the font list of a Unicode block.
	 * @param blockIndex			the block index.
	 * @param fontFamily			the font family name.
	 */
	public void setFontMappingByBlockIndex( int blockIndex, String fontFamily )
	{
		if (blockIndex < 0)
			return;
		if (fontNodes[blockIndex] == null)
		{
			fontNodes[blockIndex] = new LinkedList();
			
		}
		fontNodes[blockIndex].addLast( fontFamily );
	}
	
	public void addFontEncoding(HashMap fontEncoding)
	{
		this.fontEncoding.putAll( fontEncoding );
	}
	
	public void addFontMapping(HashMap fontMapping)
	{
		this.fontMapping.putAll( fontMapping );
	}
	
	private String getConcreteFont(String fontFamilyName)
	{
		String mappedFontFamily = (String)fontMapping.get(fontFamilyName);
		return (null == mappedFontFamily) ? fontFamilyName : mappedFontFamily;
	}
	
	private String getEncoding(String fontFamilyName)
	{
		String encoding = (String)fontEncoding.get(fontFamilyName);
		return (null == encoding) ? BaseFont.IDENTITY_H : encoding;
	}
	
	private HashMap baseFonts = new HashMap();
	
	/**
	 * Creates iText BaseFont with the given font family name.	
	 * @param ffn			the specified font family name.
	 * @param encoding		the encoding for the font.
	 * @return				the created BaseFont.
	 */
	private BaseFont createFont(String ffn, int fontStyle)
	{	
		String key = ffn + fontStyle;
		synchronized ( baseFonts )
		{
			BaseFont font = (BaseFont) baseFonts.get( key );
			if ( font == null )
			{
				String encoding = getEncoding( ffn );
				Font f = null;
				try
				{
					f = FontFactory.getFont( ffn, encoding,
						BaseFont.NOT_EMBEDDED, 14, fontStyle );
				}
				catch(Throwable de)
				{
					logger.log( Level.WARNING, de.getLocalizedMessage( ) );
					return null;
				}
				font = f.getBaseFont( );
				if(font!=null)
				{
					baseFonts.put( key, font );
				}
			}
			return font;
		}
	}
	
	private BaseFont getBaseFont(String fontName, char c, int fontStyle)
	{
		BaseFont bf = createFont(fontName,  fontStyle);
		if (null != bf && bf.charExists(c)) 
			return bf;
		else
			return null;
	}
	
	/** The edge of each unicode block */
	private static final int[] blockEdges =
	{
			0x0000,		0x007F,				//Basic Latin
			0x0080,		0x00FF,				//Latin-1 Supplement
			0x0100,		0x017F,				//Latin Extended-A
			0x0180,		0x024F,				//Latin Extended-B
			0x0250,		0x02AF,				//IPA Extensions
			0x02B0,		0x02FF,				//Spacing Modifier Letters
			0x0300,		0x036F,				//Combining Diacritical Marks
			0x0370,		0x03FF,				//Greek and Coptic
			0x0400,		0x04FF,				//Cyrillic
			0x0500,		0x052F,				//Cyrillic Supplement
			0x0530,		0x058F,				//Armenian
			0x0590,		0x05FF,				//Hebrew
			0x0600,		0x06FF,				//Arabic
			0x0700,		0x074F,				//Syriac
			0x0750,		0x077F,				//Arabic Supplement
			0x0780,		0x07BF,				//Thaana
			0x07C0,		0x07FF,				//NKo
			0x0900,		0x097F,				//Devanagari
			0x0980,		0x09FF,				//Bengali
			0x0A00,		0x0A7F,				//Gurmukhi
			0x0A80,		0x0AFF,				//Gujarati
			0x0B00,		0x0B7F,				//Oriya
			0x0B80,		0x0BFF,				//Tamil
			0x0C00,		0x0C7F,				//Telugu
			0x0C80,		0x0CFF,				//Kannada
			0x0D00,		0x0D7F,				//Malayalam
			0x0D80,		0x0DFF,				//Sinhala
			0x0E00,		0x0E7F,				//Thai
			0x0E80,		0x0EFF,				//Lao
			0x0F00,		0x0FFF,				//Tibetan
			0x1000,		0x109F,				//Myanmar
			0x10A0,		0x10FF,				//Georgian
			0x1100,		0x11FF,				//Hangul Jamo
			0x1200,		0x137F,				//Ethiopic
			0x1380,		0x139F,				//Ethiopic Supplement
			0x13A0,		0x13FF,				//Cherokee
			0x1400,		0x167F,				//Unified Canadian Aboriginal Syllabics
			0x1680,		0x169F,				//Ogham
			0x16A0,		0x16FF,				//Runic
			0x1700,		0x171F,				//Tagalog
			0x1720,		0x173F,				//Hanunoo
			0x1740,		0x175F,				//Buhid
			0x1760,		0x177F,				//Tagbanwa
			0x1780,		0x17FF,				//Khmer
			0x1800,		0x18AF,				//Mongolian
			0x1900,		0x194F,				//Limbu
			0x1950,		0x197F,				//Tai Le
			0x1980,		0x19DF,				//New Tai Lue
			0x19E0,		0x19FF,				//Khmer Symbols
			0x1A00,		0x1A1F,				//Buginese
			0x1B00,		0x1B7F,				//Balinese
			0x1D00,		0x1D7F,				//Phonetic Extensions
			0x1D80,		0x1DBF,				//Phonetic Extensions Supplement
			0x1DC0,		0x1DFF,				//Combining Diacritical Marks Supplement
			0x1E00,		0x1EFF,				//Latin Extended Additional
			0x1F00,		0x1FFF,				//Greek Extended
			0x2000,		0x206F,				//General Punctuation
			0x2070,		0x209F,				//Superscripts and Subscripts
			0x20A0,		0x20CF,				//Currency Symbols
			0x20D0,		0x20FF,				//Combining Diacritical Marks for Symbols
			0x2100,		0x214F,				//Letterlike Symbols
			0x2150,		0x218F,				//Number Forms
			0x2190,		0x21FF,				//Arrows
			0x2200,		0x22FF,				//Mathematical Operators
			0x2300,		0x23FF,				//Miscellaneous Technical
			0x2400,		0x243F,				//Control Pictures
			0x2440,		0x245F,				//Optical Character Recognition
			0x2460,		0x24FF,				//Enclosed Alphanumerics
			0x2500,		0x257F,				//Box Drawing
			0x2580,		0x259F,				//Block Elements
			0x25A0,		0x25FF,				//Geometric Shapes
			0x2600,		0x26FF,				//Miscellaneous Symbols
			0x2700,		0x27BF,				//Dingbats
			0x27C0,		0x27EF,				//Miscellaneous Mathematical Symbols-A
			0x27F0,		0x27FF,				//Supplemental Arrows-A
			0x2800,		0x28FF,				//Braille Patterns
			0x2900,		0x297F,				//Supplemental Arrows-B
			0x2980,		0x29FF,				//Miscellaneous Mathematical Symbols-B
			0x2A00,		0x2AFF,				//Supplemental Mathematical Operators
			0x2B00,		0x2BFF,				//Miscellaneous Symbols and Arrows
			0x2C00,		0x2C5F,				//Glagolitic
			0x2C60,		0x2C7F,				//Latin Extended-C
			0x2C80,		0x2CFF,				//Coptic
			0x2D00,		0x2D2F,				//Georgian Supplement
			0x2D30,		0x2D7F,				//Tifinagh
			0x2D80,		0x2DDF,				//Ethiopic Extended
			0x2E00,		0x2E7F,				//Supplemental Punctuation
			0x2E80,		0x2EFF,				//CJK Radicals Supplement
			0x2F00,		0x2FDF,				//Kangxi Radicals
			0x2FF0,		0x2FFF,				//Ideographic Description Characters
			0x3000,		0x303F,				//CJK Symbols and Punctuation
			0x3040,		0x309F,				//Hiragana
			0x30A0,		0x30FF,				//Katakana
			0x3100,		0x312F,				//Bopomofo
			0x3130,		0x318F,				//Hangul Compatibility Jamo
			0x3190,		0x319F,				//Kanbun
			0x31A0,		0x31BF,				//Bopomofo Extended
			0x31C0,		0x31EF,				//CJK Strokes
			0x31F0,		0x31FF,				//Katakana Phonetic Extensions
			0x3200,		0x32FF,				//Enclosed CJK Letters and Months
			0x3300,		0x33FF,				//CJK Compatibility
			0x3400,		0x4DBF,				//CJK Unified Ideographs Extension A
			0x4DC0,		0x4DFF,				//Yijing Hexagram Symbols
			0x4E00,		0x9FFF,				//CJK Unified Ideographs
			0xA000,		0xA48F,				//Yi Syllables
			0xA490,		0xA4CF,				//Yi Radicals
			0xA700,		0xA71F,				//Modifier Tone Letters
			0xA720,		0xA7FF,				//Latin Extended-D
			0xA800,		0xA82F,				//Syloti Nagri
			0xA840,		0xA87F,				//Phags-pa
			0xAC00,		0xD7AF,				//Hangul Syllables
			0xD800,		0xDB7F,				//High Surrogates
			0xDB80,		0xDBFF,				//High Private Use Surrogates
			0xDC00,		0xDFFF,				//Low Surrogates
			0xE000,		0xF8FF,				//Private Use Area
			0xF900,		0xFAFF,				//CJK Compatibility Ideographs
			0xFB00,		0xFB4F,				//Alphabetic Presentation Forms
			0xFB50,		0xFDFF,				//Arabic Presentation Forms-A
			0xFE00,		0xFE0F,				//Variation Selectors
			0xFE10,		0xFE1F,				//Vertical Forms
			0xFE20,		0xFE2F,				//Combining Half Marks
			0xFE30,		0xFE4F,				//CJK Compatibility Forms
			0xFE50,		0xFE6F,				//Small Form Variants
			0xFE70,		0xFEFF,				//Arabic Presentation Forms-B
			0xFF00,		0xFFEF,				//Halfwidth and Fullwidth Forms
			0xFFF0,		0xFFFF,				//Specials
			0x10000,	0x1007F,			//Linear B Syllabary
			0x10080,	0x100FF,			//Linear B Ideograms
			0x10100,	0x1013F,			//Aegean Numbers
			0x10140,	0x1018F,			//Ancient Greek Numbers
			0x10300,	0x1032F,			//Old Italic
			0x10330,	0x1034F,			//Gothic
			0x10380,	0x1039F,			//Ugaritic
			0x103A0,	0x103DF,			//Old Persian
			0x10400,	0x1044F,			//Deseret
			0x10450,	0x1047F,			//Shavian
			0x10480,	0x104AF,			//Osmanya
			0x10800,	0x1083F,			//Cypriot Syllabary
			0x10900,	0x1091F,			//Phoenician
			0x10A00,	0x10A5F,			//Kharoshthi
			0x12000,	0x123FF,			//Cuneiform
			0x12400,	0x1247F,			//Cuneiform Numbers and Punctuation
			0x1D000,	0x1D0FF,			//Byzantine Musical Symbols
			0x1D100,	0x1D1FF,			//Musical Symbols
			0x1D200,	0x1D24F,			//Ancient Greek Musical Notation
			0x1D300,	0x1D35F,			//Tai Xuan Jing Symbols
			0x1D360,	0x1D37F,			//Counting Rod Numerals
			0x1D400,	0x1D7FF,			//Mathematical Alphanumeric Symbols
			0x20000,	0x2A6DF,			//CJK Unified Ideographs Extension B
			0x2F800,	0x2FA1F,			//CJK Compatibility Ideographs Supplement
			0xE0000,	0xE007F,			//Tags
			0xE0100,	0xE01EF,			//Variation Selectors Supplement
			0xF0000,	0xFFFFF,			//Supplementary Private Use Area-A
			0x100000,	0x10FFFF			//Supplementary Private Use Area-B
	};	
	
}