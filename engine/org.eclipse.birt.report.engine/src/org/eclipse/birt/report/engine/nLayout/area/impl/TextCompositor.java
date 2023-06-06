/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

public class TextCompositor {

	private FontInfo fontInfo;

	/**
	 * This is for BIDI support.
	 *
	 * @see Chunk#getRunLevel()
	 */
	private int runLevel;

	/** offset (in characters) relative to the text in the textContent. */
	int offset = 0;

	/** the remain chunks */
	private ChunkGenerator remainChunks;

	/** the remain words in current chunk */
	private IWordRecognizer remainWords;

	/**
	 * the remaining word, which didn't fit into the current line and thus should go
	 * into the next line.
	 *
	 * Note that {@link #remainWord} and {@link #wordVestige} are alternatives, they
	 * cannot be not null at the same time.
	 */
	private Word remainWord;

	/**
	 * The remaining characters in current word after word-breaking / hyphenation.
	 *
	 * Note that {@link #remainWord} and {@link #wordVestige} are alternatives, they
	 * cannot be not null at the same time.
	 */
	private Word wordVestige;

	/**
	 * Check if current TextArea contains line break. If text wrapping need not be
	 * handled (see {@link #textWrapping}), hasNextArea() will return false when
	 * hasLineBreak is true.
	 */
	private boolean hasLineBreak = false;

	/**
	 * Denotes if we are at the beginning of a new line.
	 *
	 * We need this for handling very long words which won't fit even if they are
	 * the first word in the line. Depending on whether word breaking is enabled,
	 * such words should either be 'hyphenated' or just added to the current line
	 * anyway (in the latter case they won't be fully visible).
	 *
	 * Note: This is for similar reasons as {@link #insertFirstExceedWord}, but it's
	 * only used when the line is still empty.
	 *
	 * @see #addWordIntoTextArea(TextArea, Word)
	 */
	private boolean isNewLine = true;

	/**
	 * Denotes if the text content is empty (in this case a single blank space is
	 * used as a replacement text).
	 */
	private boolean blankText = false;

	/**
	 * Decides if the first exceed word should also been add into text area. As a
	 * consequence, that word will not be fully visible in the output.
	 *
	 * This is for the case when {@link #textWrapping} is false.
	 *
	 * Note: This is for similar reasons as {@link #isNewLine}, but it's only used
	 * when the line already contains other words.
	 */
	private boolean insertFirstExceedWord = false;

	// three possible line break collapse status
	private static int LINE_BREAK_COLLAPSE_FREE = 0;
	private static int LINE_BREAK_COLLAPSE_STANDING_BY = 1;
	private static int LINE_BREAK_COLLAPSE_OCCUPIED = 2;

	private int lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;

	private ITextContent textContent;
	private FontMappingManager fontManager;
	private LayoutContext context;

	/**
	 * Should a sequence of words be line-wrapped automatically?
	 *
	 * Setting the CSS property "white-space" to "nowrap" or "pre" may prevent this.
	 */
	private boolean textWrapping;

	private TextArea previousTextArea;
	private TextArea currentTextArea;

	public TextCompositor(ITextContent textContent, FontMappingManager fontManager, LayoutContext context) {
		this.textContent = textContent;
		this.fontManager = fontManager;
		this.context = context;

		IStyle style = textContent.getComputedStyle();
		textWrapping = context.getTextWrapping()
				&& !PropertyUtil.isWhiteSpaceNoWrap(style.getProperty(StyleConstants.STYLE_WHITE_SPACE));
		remainChunks = new ChunkGenerator(fontManager, textContent, context.getBidiProcessing(),
				context.getFontSubstitution());
	}

	public TextCompositor(ITextContent textContent, FontMappingManager fontManager, LayoutContext context,
			boolean blankText) {
		this(textContent, fontManager, context);
		this.blankText = blankText;
	}

	/**
	 * Determine if there is more text to output in the current container (which
	 * class?).
	 *
	 * @return false if textWrapping is not set and hasLineBreak is set, otherwise
	 *         return if there is more text to print.
	 */
	public boolean hasNextArea() {
		// if the text need not be wrapped, and need switch to a new line, just
		// ignore the subsequence text.
		if (!textWrapping && hasLineBreak) {
			return false;
		}
		return offset < textContent.getText().length();
	}

	/**
	 * Sets {@link #isNewLine}.
	 *
	 * If the new value is true and {@link #textWrapping} is not set, also sets
	 * {@link #insertFirstExceedWord} to true.
	 *
	 * @param status
	 */
	public void setNewLineStatus(boolean status) {
		isNewLine = status;
		if (isNewLine && !textWrapping) {
			insertFirstExceedWord = true;
		}
	}

	public TextArea getNextArea(int maxLineWidth) {
		if (!hasNextArea()) {
			throw new RuntimeException("No more text.");
		}
		TextArea textArea = getNextTextArea(maxLineWidth);
		previousTextArea = currentTextArea;
		currentTextArea = textArea;
		if (textArea != null) {
			offset += textArea.getTextLength();
		}
		if (lineBreakCollapse == LINE_BREAK_COLLAPSE_OCCUPIED) {
			lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
			return null;
		}
		return textArea;
	}

	protected boolean isEmptyWordVestige(Word wordVestige) {
		String value = wordVestige.getValue();
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * This "consumes" some text and returns a TextArea with the consumed text.
	 *
	 * Before calling this method, {@link #hasNextArea()} must be called to check if
	 * there actually is more text to consume.
	 *
	 * @param maxLineWidth
	 * @return a TextArea - or null in case of an empty word vestige.
	 */
	private TextArea getNextTextArea(int maxLineWidth) {

		// First, we handle two similar special cases:

		// Special case 1: There is a wordVestige (the remainder of a word which did not
		// fit into the previous line after word-breaking).
		if (null != wordVestige) {
			if (isEmptyWordVestige(wordVestige)) {
				offset += wordVestige.getLength();
				wordVestige = null;
				return null;
			}
			lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
			TextArea textArea = createTextArea(textContent, offset, runLevel, fontInfo);
			textArea.setMaxWidth(maxLineWidth);
			textArea.setWidth(0);
			addWordIntoTextArea(textArea, wordVestige);
			return textArea;
		}

		// Special case 2: There is a remaining word (which did not fit into the
		// previous line).
		if (null != remainWord) {
			lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
			TextArea textArea = createTextArea(textContent, offset, runLevel, fontInfo);
			textArea.setMaxWidth(maxLineWidth);
			textArea.setWidth(0);
			addWordIntoTextArea(textArea, remainWord);
			remainWord = null;
			return textArea;
			// FIXME: Why do we return here already?
			// This return here in a way contradicts the idea of the algorithm, which is to
			// stuff as many words as possible into a TextArea,
			// because it results in a (e.g. PDF) text line consisting of two (more than
			// one) TextAreas A and B, where A is a TextArea with exactly one Word (= word
			// fragment) that did not fit into the previous line, and B contains the next
			// Words.
			// This results in slightly larger PDF files than necessary and it and makes it
			// slightly harder for accessibility software to understand the file.
		}

		// This is what happens most of the time.

		// iterate the remainWords.
		if (null == remainWords || !remainWords.hasWord()) {
			Chunk chunk = remainChunks.getNext();
			if (chunk instanceof LineBreakChunk) {
				// Return a hard line break. The line height is decided by
				// the current font's height.
				FontHandler handler = new FontHandler(fontManager, textContent, false);
				TextArea textArea = createTextArea(textContent, handler.getFontInfo(), true);
				textArea.setTextLength(chunk.getLength());
				hasLineBreak = true;
				if (lineBreakCollapse == LINE_BREAK_COLLAPSE_STANDING_BY) {
					lineBreakCollapse = LINE_BREAK_COLLAPSE_OCCUPIED;
				}
				return textArea;
			}
			lineBreakCollapse = LINE_BREAK_COLLAPSE_FREE;
			fontInfo = chunk.getFontInfo();
			runLevel = chunk.getRunLevel();
			remainWords = new WordRecognizerWrapper(chunk.getText(), context.getLocale());
		}
		// new and empty text area.
		TextArea textArea = createTextArea(textContent, offset, runLevel, fontInfo);
		textArea.setMaxWidth(maxLineWidth);
		textArea.setWidth(0);
		addWordsIntoTextArea(textArea, remainWords);
		return textArea;
	}

	protected TextStyle textStyle = null;

	protected TextArea createTextArea(ITextContent textContent, FontInfo fontInfo, boolean blankLine) {
		if (textStyle == null || textStyle.getFontInfo() != fontInfo) {
			textStyle = TextAreaLayout.buildTextStyle(textContent, fontInfo);
			if (blankText) {
				textStyle.setHasHyperlink(false);
			}
		}
		TextArea area = new TextArea( /* textContent.getText( ), */ textStyle);
		area.setOffset(offset);
		if (blankLine) {
			area.lineBreak = true;
			area.blankLine = true;
		} else {
			area.setOffset(0);
			area.setTextLength(textContent.getText().length());
		}
		return area;
	}

	protected TextArea createTextArea(ITextContent textContent, int offset, int runLevel, FontInfo fontInfo) {
		if (textStyle == null || textStyle.getFontInfo() != fontInfo) {
			textStyle = TextAreaLayout.buildTextStyle(textContent, fontInfo);
			if (blankText) {
				textStyle.setHasHyperlink(false);
				textStyle.setLineThrough(false);
				textStyle.setUnderLine(false);
			}
		}
		TextArea area = new TextArea(textContent.getText(), textStyle);
		if (!blankText) {
			area.setAction(textContent.getHyperlinkAction());
		}
		area.setOffset(offset);
		area.setRunLevel(runLevel);
		area.setVerticalAlign(textContent.getComputedStyle().getProperty(IStyle.STYLE_VERTICAL_ALIGN));
		return area;
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
		WordWidth wordWidth = getWordWidth(fontInfo, word);

		// append the letter spacing
		int letterSpacing = textStyle.getLetterSpacing();
		int width = wordWidth.width + letterSpacing * textLength;
		if (wordWidth.softHyphenWidth > 0) {
			width = width - wordWidth.softHyphenWidth + letterSpacing;
		}

		int adjustWordSize = fontInfo.getItalicAdjust() + width;
		if (textArea.hasSpace(adjustWordSize + wordWidth.softHyphenWidth * letterSpacing)) {
			addWord(textArea, textLength, wordWidth);
			wordVestige = null;
			if (remainWords.hasWord()) {
				// test if we can append the word spacing
				if (textArea.hasSpace(textStyle.getWordSpacing())) {
					textArea.addWordSpacing(textStyle.getWordSpacing());
				} else {
					// we have more words, but there is not enough space for
					// them.
					textArea.setLineBreak(true);
					hasLineBreak = true;
					lineBreakCollapse = LINE_BREAK_COLLAPSE_STANDING_BY;
				}
			}
		} else {
			// for no wrap text, the first exceed word should also been add into text area.
			if (!textWrapping && insertFirstExceedWord) {
				addWord(textArea, textLength);
				wordVestige = null;
				insertFirstExceedWord = false;
			}
			if (isNewLine && textArea.isEmpty()) {
				if (context.isEnableWordbreak()) {
					doWordBreak(word.getValue(), textArea);
				} else {
					// If width of a word is larger than the max line width,
					// add it into the line directly.
					addWord(textArea, textLength, wordWidth);
				}
			} else {
				wordVestige = null;
				remainWord = word;
			}
			textArea.setLineBreak(true);
			hasLineBreak = true;
			lineBreakCollapse = LINE_BREAK_COLLAPSE_STANDING_BY;
		}
	}

	private void doWordBreak(String str, TextArea area) {
		IHyphenationManager hm = new DefaultHyphenationManager();
		Hyphenation wb = hm.getHyphenation(str);
		FontInfo fi = area.getStyle().getFontInfo();
		if (area.getMaxWidth() < 0) {
			addWordVestige(area, 1, new WordWidth(fi, wb.getHyphenText(0, 1)), str.substring(1));
			return;
		}
		int endHyphenIndex = hyphen(0, area.getMaxWidth() - area.getWidth(), wb, fi);
		// current line can't even place one character. Force to add the first
		// character into the line.
		if (endHyphenIndex == 0 && area.getWidth() == 0) {
			addWordVestige(area, 1, new WordWidth(fi, wb.getHyphenText(0, 1)), str.substring(1));
		} else {
			WordWidth wordWidth = new WordWidth(fi, wb.getHyphenText(0, endHyphenIndex));
			// Take letter spacing into account
			wordWidth = new WordWidth(wordWidth.width + textStyle.getLetterSpacing() * (endHyphenIndex - 1), 0);
			addWordVestige(area, endHyphenIndex, wordWidth, str.substring(endHyphenIndex));
		}
	}

	private void addWordVestige(TextArea area, int vestigeTextLength, WordWidth vestigeWordWidth,
			String vestigeString) {
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
			current = (int) (fi.getWordWidth(pre) * PDFConstants.LAYOUT_TO_PDF_RATIO)
					+ textStyle.getLetterSpacing() * pre.length();
			if (width > last && width <= current) {
				return i - 1;
			}
		}
		return hyphenation.length() - 1;
	}

	/**
	 * Returns the width needed for displaying the word in the given font. If the
	 * word ends with a Unicode SOFT HYPHEN symbol (SHY), this includes the hyphen
	 * width
	 *
	 * @param fontInfo
	 * @param word
	 * @return
	 */
	private WordWidth getWordWidth(FontInfo fontInfo, Word word) {
		return new WordWidth(fontInfo, word.getValue());
	}

	private void addWord(TextArea textArea, int textLength, WordWidth wordWidth) {
		textArea.addWord(textLength, wordWidth);
	}

	private void addWord(TextArea textArea, int textLength) {
		textArea.addWordUsingMaxWidth(textLength);
	}

	/**
	 * @return Returns the previousTextArea.
	 */
	public TextArea getPreviousTextArea() {
		return previousTextArea;
	}

}
