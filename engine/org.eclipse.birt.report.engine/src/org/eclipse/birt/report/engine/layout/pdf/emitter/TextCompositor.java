/***********************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Locale;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;
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
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class TextCompositor {
	private int letterSpacing = 0;
	private int wordSpacing = 0;

	private FontInfo fontInfo;
	private int lineBaseLevel;
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
	 * Check if current TextArea contains line break. If text wrapping need not be
	 * handled, hasNextArea() will return false when hasLineBreak is true.
	 */
	private boolean hasLineBreak = false;

	private boolean isNewLine = true;
	boolean textWrapping;
	boolean bidiProcessing;
	boolean fontSubstitution;
	boolean hyphenation;
	private ITextContent textContent;
	private FontMappingManager fontManager;
	private Locale locale;

	public TextCompositor(ITextContent textContent, FontMappingManager fontManager, boolean bidiProcessing,
			boolean fontSubstitution, boolean textWrapping, boolean hyphenation, Locale locale) {
		this.textContent = textContent;
		this.fontManager = fontManager;
		this.bidiProcessing = bidiProcessing;
		this.fontSubstitution = fontSubstitution;
		this.hyphenation = hyphenation;
		this.locale = locale;
		IStyle style = textContent.getComputedStyle();
		letterSpacing = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_LETTER_SPACING));
		wordSpacing = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_WORD_SPACING));
		this.textWrapping = textWrapping
				&& !PropertyUtil.isWhiteSpaceNoWrap(style.getProperty(StyleConstants.STYLE_WHITE_SPACE));
		remainChunks = new ChunkGenerator(fontManager, textContent, bidiProcessing, fontSubstitution);
	}

	public boolean hasNextArea() {
		// if the text need not be wrapped, and need switch to a new line, just
		// ignore the subsequence text.
		if (!textWrapping && hasLineBreak) {
			return false;
		}
		return offset < textContent.getText().length();
	}

	public void setNewLineStatus(boolean status) {
		isNewLine = status;
	}

	public TextArea getNextArea(int maxLineWidth) {
		if (!hasNextArea()) {
			throw new RuntimeException("No more text.");
		}
		TextArea textArea = getNextTextArea(maxLineWidth);
		offset += textArea.getTextLength();
//		System.out.print(textArea.getText( ));
//		System.out.print("["+offset+"]");
		return textArea;
	}

	private TextArea getNextTextArea(int maxLineWidth) {
		// the hyphenation vestige
		if (null != wordVestige) {
			TextArea textArea = createTextArea(textContent, offset, lineBaseLevel, runLevel, fontInfo);
			textArea.setMaxWidth(maxLineWidth);
			textArea.setWidth(0);
			addWordIntoTextArea(textArea, wordVestige);
			return textArea;
		}
		if (null != remainWord) {
			TextArea textArea = createTextArea(textContent, offset, lineBaseLevel, runLevel, fontInfo);
			textArea.setMaxWidth(maxLineWidth);
			textArea.setWidth(0);
			addWordIntoTextArea(textArea, remainWord);
			remainWord = null;
			return textArea;
		}
		// iterate the remainWords.
		if (null == remainWords || !remainWords.hasWord()) {
			Chunk chunk = remainChunks.getNext();
			if (chunk == Chunk.HARD_LINE_BREAK) {
				// return a hard line break. the line height is decided by
				// the current font's height.
				FontHandler handler = new FontHandler(fontManager, textContent, false);
				TextArea textArea = (TextArea) AreaFactory.createTextArea(textContent, handler.getFontInfo(), true);
				textArea.setTextLength(chunk.getLength());
				hasLineBreak = true;
				return textArea;
			}
			fontInfo = chunk.getFontInfo();
			lineBaseLevel = chunk.getBaseLevel();
			runLevel = chunk.getRunLevel();
			remainWords = new WordRecognizerWrapper(chunk.getText(), locale);
		}
		// new an empty text area.
		TextArea textArea = createTextArea(textContent, offset, lineBaseLevel, runLevel, fontInfo);
		textArea.setMaxWidth(maxLineWidth);
		textArea.setWidth(0);
		addWordsIntoTextArea(textArea, remainWords);
		return textArea;
	}

	// performance enhancement, cache area style for the same text content
	protected IStyle areaStyle = null;

	protected TextArea createTextArea(ITextContent textContent, int offset, int baseLevel, int runLevel,
			FontInfo fontInfo) {
		if (areaStyle == null) {
			TextArea textArea = (TextArea) AreaFactory.createTextArea(textContent, offset, lineBaseLevel, runLevel,
					fontInfo);
			areaStyle = textArea.getStyle();
			return textArea;
		} else {
			return (TextArea) AreaFactory.createTextArea(textContent, areaStyle, offset, lineBaseLevel, runLevel,
					fontInfo);
		}
	}

	/**
	 *
	 * @param textArea
	 * @param words
	 */
	private void addWordsIntoTextArea(TextArea textArea, IWordRecognizer words) {
		while (words.hasWord()) {
			Word word = words.getNextWord();
			addWordIntoTextArea(textArea, word);
			if (textArea.isLineBreak()) {
				return;
			}
		}
	}

	/**
	 * layout a word, add the word to the line buffer.
	 *
	 * @param word the word
	 *
	 */
	private void addWordIntoTextArea(TextArea textArea, Word word) {
		// get the word's size
		int textLength = word.getLength();
		int wordWidth = getWordWidth(fontInfo, word);
		// append the letter spacing
		wordWidth += letterSpacing * textLength;
		if (textArea.hasSpace(wordWidth)) {
			addWord(textArea, textLength, wordWidth);
			wordVestige = null;
			if (remainWords.hasWord()) {
				// test if we can append the word spacing
				if (textArea.hasSpace(wordSpacing)) {
					textArea.addWordSpacing(wordSpacing);
				} else {
					// we have more words, but there is no enough space for them.
					textArea.setLineBreak(true);
					hasLineBreak = true;
				}
			}
		} else {
			if (isNewLine && textArea.isEmpty()) {
				if (hyphenation) {
					doHyphenation(word.getValue(), textArea);
				} else {
					// If width of a word is larger than the max line width, add it
					// into the line directly.
					addWord(textArea, textLength, wordWidth);
				}
			} else {
				wordVestige = null;
				remainWord = word;
			}
			textArea.setLineBreak(true);
			hasLineBreak = true;
		}
	}

	private void doHyphenation(String str, TextArea area) {
		IHyphenationManager hm = new DefaultHyphenationManager();
		Hyphenation hyph = hm.getHyphenation(str);
		FontInfo fi = area.getFontInfo();
		if (area.getMaxWidth() < 0) {
			addWordVestige(area, 1, getTextWidth(fi, hyph.getHyphenText(0, 1)), str.substring(1));
			return;
		}
		int endHyphenIndex = hyphen(0, area.getMaxWidth() - area.getWidth(), hyph, fi);
		// current line can't even place one character. Force to add the first
		// character into the line.
		if (endHyphenIndex == 0 && area.getWidth() == 0) {
			addWordVestige(area, 1, getTextWidth(fi, hyph.getHyphenText(0, 1)), str.substring(1));
		} else {
			addWordVestige(area, endHyphenIndex,
					getTextWidth(fi, hyph.getHyphenText(0, endHyphenIndex)) + letterSpacing * (endHyphenIndex - 1),
					str.substring(endHyphenIndex));
		}
	}

	private void addWordVestige(TextArea area, int vestigeTextLength, int vestigeWordWidth, String vestigeString) {
		addWord(area, vestigeTextLength, vestigeWordWidth);
		if (vestigeString.length() == 0) {
			wordVestige = null;
		} else {
			wordVestige = new Word(vestigeString, 0, vestigeString.length());
		}
	}

	/**
	 * Gets the hyphenation index
	 *
	 * @param startIndex  the start index
	 * @param width       the width of the free space
	 * @param hyphenation the hyphenation
	 * @param fi          the FontInfo object of the text to be hyphened.
	 * @return the hyphenation index
	 */
	private int hyphen(int startIndex, int width, Hyphenation hyphenation, FontInfo fi) {
		assert (startIndex >= 0);
		if (startIndex > hyphenation.length() - 1) {
			return -1;
		}
		int last = 0;
		int current = 0;
		for (int i = startIndex + 1; i < hyphenation.length(); i++) {
			last = current;
			String pre = hyphenation.getHyphenText(startIndex, i);
			current = (int) (fi.getWordWidth(pre) * PDFConstants.LAYOUT_TO_PDF_RATIO) + letterSpacing * pre.length();
			if (width > last && width <= current) {
				return i - 1;
			}
		}
		return hyphenation.length() - 1;
	}

	private int getTextWidth(FontInfo fontInfo, String text) {
		return (int) (fontInfo.getWordWidth(text) * PDFConstants.LAYOUT_TO_PDF_RATIO);
	}

	private int getWordWidth(FontInfo fontInfo, Word word) {
		return getTextWidth(fontInfo, word.getValue());
	}

	private void addWord(TextArea textArea, int textLength, int wordWidth) {
		textArea.addWord(textLength, wordWidth);
	}

}
