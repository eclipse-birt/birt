/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.WordRecognizerWrapper;
import org.eclipse.birt.report.engine.layout.pdf.font.FontHandler;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.DefaultHyphenationManager;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.Hyphenation;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.IHyphenationManager;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.IWordRecognizer;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.Word;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;
import org.eclipse.birt.report.engine.layout.pdf.text.ChunkGenerator;
import org.eclipse.birt.report.engine.layout.pdf.text.LineBreakChunk;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

public class TextCompositor
{

	private FontInfo fontInfo;
	private int runLevel;

	/** offset relative to the text in the textContent. */
	int offset = 0;

	/** the remain chunks */
	private ChunkGenerator remainChunks;
	/** the remain words in current chunk */
	private IWordRecognizer remainWords;
	/** the remain word */
	private Word remainWord;
	/** the remain characters in current word after hyphenation */
	private Word wordVestige;


	/**
	 * Check if current TextArea contains line break. If text wrapping need not
	 * be handled, hasNextArea() will return false when hasLineBreak is true.
	 */
	private boolean hasLineBreak = false;
	
	private boolean isNewLine = true;
	
	private boolean blankText = false;
	
	// for no wrap text, the first exceed word should also been add into text area.
	private boolean insertFirstExceedWord = false;
	
	//three possible line break collapse status
	private static int LINE_BREAK_COLLAPSE_FREE = 0;
	private static int LINE_BREAK_COLLAPSE_STANDING_BY = 1;
	private static int LINE_BREAK_COLLAPSE_OCCUPIED = 2;
	
	private int lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;

	private ITextContent textContent;
	private FontMappingManager fontManager;
	private LayoutContext context;
	private boolean textWrapping;

	public TextCompositor( ITextContent textContent,
			FontMappingManager fontManager, LayoutContext context )
	{
		this.textContent = textContent;
		this.fontManager = fontManager;
		this.context = context;
		
		IStyle style = textContent.getComputedStyle( );
		textWrapping = context.getTextWrapping( )
				&& !PropertyUtil.isWhiteSpaceNoWrap( style
						.getProperty( StyleConstants.STYLE_WHITE_SPACE ) );
		remainChunks = new ChunkGenerator( fontManager, textContent, context
				.getBidiProcessing( ), context.getFontSubstitution( ) );
	}
	
	public TextCompositor( ITextContent textContent,
			FontMappingManager fontManager, LayoutContext context,
			boolean blankText )
	{
		this( textContent, fontManager, context );
		this.blankText = blankText;
	}

	public boolean hasNextArea( )
	{
		// if the text need not be wrapped, and need switch to a new line, just
		// ignore the subsequence text.
		if ( !textWrapping && hasLineBreak )
		{
			return false;
		}
		return offset < textContent.getText( ).length( );
	}

	public void setNewLineStatus( boolean status )
	{
		isNewLine = status;
		if ( isNewLine && !textWrapping )
		{
			insertFirstExceedWord = true;
		}
	}

	public TextArea getNextArea( int maxLineWidth )
	{
		if ( !hasNextArea( ) )
		{
			throw new RuntimeException( "No more text." );
		}
		TextArea textArea = getNextTextArea( maxLineWidth );
		if ( textArea != null )
		{
			offset += textArea.getTextLength( );
		}
		if( lineBreakCollapse == LINE_BREAK_COLLAPSE_OCCUPIED )
		{
			lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
			return null;
		}
		return textArea;
	}
	
	protected boolean isEmptyWordVestige( Word wordVestige )
	{
		String value = wordVestige.getValue( );
		for ( int i = 0; i < value.length( ); i++ )
		{
			if ( value.charAt( i ) != ' ' )
			{
				return false;
			}
		}
		return true;
	}

	private TextArea getNextTextArea( int maxLineWidth )
	{
		// the hyphenation vestige
		if ( null != wordVestige )
		{
			if ( isEmptyWordVestige( wordVestige ) )
			{
				offset += wordVestige.getLength( );
				wordVestige = null;
				return null;
			}
			else
			{
				lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
				TextArea textArea = createTextArea( textContent, offset, runLevel,
						fontInfo );
				textArea.setMaxWidth( maxLineWidth );
				textArea.setWidth( 0 );
				addWordIntoTextArea( textArea, wordVestige );
				return textArea;
			}
		}
		if ( null != remainWord )
		{
			lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
			TextArea textArea = createTextArea( textContent, offset, runLevel,
					fontInfo );
			textArea.setMaxWidth( maxLineWidth );
			textArea.setWidth( 0 );
			addWordIntoTextArea( textArea, remainWord );
			remainWord = null;
			return textArea;
		}
		// iterate the remainWords.
		if ( null == remainWords || !remainWords.hasWord( ) )
		{
			Chunk chunk = remainChunks.getNext( );
			if ( chunk instanceof LineBreakChunk )
			{
				// return a hard line break. the line height is decided by
				// the current font's height.
				FontHandler handler = new FontHandler( fontManager,
						textContent, false );
				TextArea textArea = createTextArea( textContent, handler
						.getFontInfo( ), true );
				textArea.setTextLength( chunk.getLength( ) );
				hasLineBreak = true;
				if( lineBreakCollapse == LINE_BREAK_COLLAPSE_STANDING_BY )
				{
					lineBreakCollapse = LINE_BREAK_COLLAPSE_OCCUPIED;
				}
				return textArea;
			}
			lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
			fontInfo = chunk.getFontInfo( );
			runLevel = chunk.getRunLevel( );
			remainWords = new WordRecognizerWrapper( chunk.getText( ), context
					.getLocale( ) );
		}
		// new an empty text area.
		TextArea textArea = createTextArea( textContent, offset, runLevel,
				fontInfo );
		textArea.setMaxWidth( maxLineWidth );
		textArea.setWidth( 0 );
		addWordsIntoTextArea( textArea, remainWords );
		return textArea;
	}

	protected TextStyle textStyle = null;

	protected TextArea createTextArea( ITextContent textContent,
			FontInfo fontInfo, boolean blankLine )
	{
		if ( textStyle == null || textStyle.getFontInfo( )!= fontInfo)
		{
			textStyle = TextAreaLayout.buildTextStyle( textContent,
					fontInfo );
			if ( blankText )
			{
				textStyle.setHasHyperlink( false );
			}
		}
		TextArea area = new TextArea( /*textContent.getText( ),*/ textStyle );
		area.setOffset( offset );
		if ( blankLine )
		{
			area.lineBreak = true;
			area.blankLine = true;
		}
		else
		{
			area.setOffset( 0 );
			area.setTextLength( textContent.getText( ).length( ) );
		}
		return area;
	}

	protected TextArea createTextArea( ITextContent textContent, int offset,
			int runLevel, FontInfo fontInfo )
	{
		if ( textStyle == null || textStyle.getFontInfo( )!= fontInfo)
		{
			textStyle = TextAreaLayout.buildTextStyle( textContent,
					fontInfo );
			if ( blankText )
			{
				textStyle.setHasHyperlink( false );
				textStyle.setLineThrough( false );
				textStyle.setUnderLine( false );
			}
		}
		TextArea area = new TextArea( textContent.getText( ), textStyle );
		if ( !blankText )
		{
			area.setAction( textContent.getHyperlinkAction( ) );
		}
		area.setOffset( offset );
		area.setRunLevel( runLevel );
		area.setVerticalAlign( textContent.getComputedStyle( ).getProperty( IStyle.STYLE_VERTICAL_ALIGN ) );
		return area;
	}

	/**
	 * 
	 * @param textArea
	 * @param words
	 */
	private void addWordsIntoTextArea( TextArea textArea, IWordRecognizer words )
	{
		while ( words.hasWord( ) )
		{
			Word word = words.getNextWord( );
			addWordIntoTextArea( textArea, word );
			if ( textArea.isLineBreak( ) )
			{
				return;
			}
		}
	}

	/**
	 * layout a word, add the word to the line buffer.
	 * 
	 * @param word
	 *            the word
	 * 
	 */
	private void addWordIntoTextArea( TextArea textArea, Word word )
	{
		// get the word's size
		int textLength = word.getLength( );
		int wordWidth = getWordWidth( fontInfo, word );
		// append the letter spacing
		wordWidth += textStyle.getLetterSpacing( ) * textLength;
		int adjustWordSize = fontInfo.getItalicAdjust( ) + wordWidth;
		if ( textArea.hasSpace( adjustWordSize ) )
		{
			addWord( textArea, textLength, wordWidth );
			wordVestige = null;
			if ( remainWords.hasWord( ) )
			{
				// test if we can append the word spacing
				if ( textArea.hasSpace( textStyle.getWordSpacing( ) ) )
				{
					textArea.addWordSpacing( textStyle.getWordSpacing( ) );
				}
				else
				{
					// we have more words, but there is no enough space for
					// them.
					textArea.setLineBreak( true );
					hasLineBreak = true;
					lineBreakCollapse = LINE_BREAK_COLLAPSE_STANDING_BY;
				}
			}
		}
		else
		{
			// for no wrap text, the first exceed word should also been add into text area.
			if ( !textWrapping && insertFirstExceedWord )
			{
				addWord( textArea, textLength );
				wordVestige = null;
				insertFirstExceedWord = false;
			}
			if ( isNewLine && textArea.isEmpty( ) )
			{
				if ( context.isEnableHyphenation( ) )
				{
					doHyphenation( word.getValue( ), textArea );
				}
				else
				{
					// If width of a word is larger than the max line width, add
					// it into the line directly.
					addWord( textArea, textLength, wordWidth );
				}
			}
			else
			{
				wordVestige = null;
				remainWord = word;
			}
			textArea.setLineBreak( true );
			hasLineBreak = true;
			lineBreakCollapse = LINE_BREAK_COLLAPSE_STANDING_BY;
		}
	}

	private void doHyphenation( String str, TextArea area )
	{
		IHyphenationManager hm = new DefaultHyphenationManager( );
		Hyphenation hyph = hm.getHyphenation( str );
		FontInfo fi = area.getStyle( ).getFontInfo( );
		if ( area.getMaxWidth( ) < 0 )
		{
			addWordVestige( area, 1, getTextWidth( fi, hyph
					.getHyphenText( 0, 1 ) ), str.substring( 1, str.length( ) ) );
			return;
		}
		int endHyphenIndex = hyphen( 0, area.getMaxWidth( ) - area.getWidth( ),
				hyph, fi );
		// current line can't even place one character. Force to add the first
		// character into the line.
		if ( endHyphenIndex == 0 && area.getWidth( ) == 0 )
		{
			addWordVestige( area, 1, getTextWidth( fi, hyph
					.getHyphenText( 0, 1 ) ), str.substring( 1, str.length( ) ) );
		}
		else
		{
			addWordVestige( area, endHyphenIndex, getTextWidth( fi, hyph
					.getHyphenText( 0, endHyphenIndex ) )
					+ textStyle.getLetterSpacing( ) * ( endHyphenIndex - 1 ), str.substring(
					endHyphenIndex, str.length( ) ) );
		}
	}

	private void addWordVestige( TextArea area, int vestigeTextLength,
			int vestigeWordWidth, String vestigeString )
	{
		addWord( area, vestigeTextLength, vestigeWordWidth );
		if ( vestigeString.length( ) == 0 )
		{
			wordVestige = null;
		}
		else
		{
			wordVestige = new Word( vestigeString, 0, vestigeString.length( ) );
		}
	}

	/**
	 * Gets the hyphenation index
	 * 
	 * @param startIndex
	 *            the start index
	 * @param width
	 *            the width of the free space
	 * @param hyphenation
	 *            the hyphenation
	 * @param fi
	 *            the FontInfo object of the text to be hyphened.
	 * @return the hyphenation index
	 */
	private int hyphen( int startIndex, int width, Hyphenation hyphenation,
			FontInfo fi )
	{
		assert ( startIndex >= 0 );
		if ( startIndex > hyphenation.length( ) - 1 )
		{
			return -1;
		}
		int last = 0;
		int current = 0;
		for ( int i = startIndex + 1; i < hyphenation.length( ); i++ )
		{
			last = current;
			String pre = hyphenation.getHyphenText( startIndex, i );
			current = (int) ( fi.getWordWidth( pre ) * PDFConstants.LAYOUT_TO_PDF_RATIO )
					+ textStyle.getLetterSpacing( ) * pre.length( );
			if ( width > last && width <= current )
			{
				return i - 1;
			}
		}
		return hyphenation.length( ) - 1;
	}

	private int getTextWidth( FontInfo fontInfo, String text )
	{
		return (int) ( fontInfo.getWordWidth( text ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
	}

	private int getWordWidth( FontInfo fontInfo, Word word )
	{
		return getTextWidth( fontInfo, word.getValue( ) );
	}

	private void addWord( TextArea textArea, int textLength, int wordWidth )
	{
		textArea.addWord( textLength, wordWidth );
	}
	
	private void addWord( TextArea textArea, int textLength )
	{
		textArea.addWordUsingMaxWidth( textLength );
	}
	
}