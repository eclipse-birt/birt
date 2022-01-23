/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.pdf.ISplitter;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;
import org.eclipse.birt.report.engine.layout.pdf.text.LineBreakChunk;

import com.ibm.icu.text.Bidi;

public class FontSplitter implements ISplitter {
	/**
	 * If no font can display a character, replace the character with the
	 * MISSING_CHAR. Make sure MISSING_CHAR can be displayed with DEFAUTL_FONT.
	 */
	public static final char MISSING_CHAR = '?';

	private FontHandler fh = null;
	private boolean fontSubstitution;

	private int baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;
	private int runLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;
	private int baseOffset = 0;
	private char[] chunkText = null;

	private int chunkStartPos = 0;
	private int currentPos = -1;

	private FontInfo lastFontInfo = null;

	private Chunk lineBreak = null;

	private boolean replaceUnknownChar = true;

	public FontSplitter(FontMappingManager fontManager, Chunk inputChunk, ITextContent textContent,
			boolean fontSubstitution, boolean replaceUnknownChar) {
		this.fontSubstitution = fontSubstitution;
		this.chunkText = inputChunk.getText().toCharArray();
		baseOffset = inputChunk.getOffset();
		baseLevel = inputChunk.getBaseLevel();
		runLevel = inputChunk.getRunLevel();
		this.fh = new FontHandler(fontManager, textContent, fontSubstitution);
		this.replaceUnknownChar = replaceUnknownChar;
	}

	public FontSplitter(FontMappingManager fontManager, Chunk inputChunk, ITextContent textContent,
			boolean fontSubstitution) {
		this.fontSubstitution = fontSubstitution;
		this.chunkText = inputChunk.getText().toCharArray();
		baseOffset = inputChunk.getOffset();
		baseLevel = inputChunk.getBaseLevel();
		runLevel = inputChunk.getRunLevel();
		this.fh = new FontHandler(fontManager, textContent, fontSubstitution);
	}

	private Chunk buildChunk() {
		if (!fontSubstitution) {
			Chunk c = new Chunk(new String(chunkText), baseOffset, baseLevel, runLevel, fh.getFontInfo());
			chunkStartPos = chunkText.length;
			return c;
		}

		if (lineBreak != null) {
			Chunk result = lineBreak;
			lineBreak = null;
			chunkStartPos++;
			return result;
		}

		while (++currentPos < chunkText.length) {
			Chunk lineBreakChunk = processLineBreak();
			if (lineBreakChunk != null) {
				return lineBreakChunk;
			}

			// We fail to find a font to display the character,
			// we replace this character with MISSING_CHAR defined in FontHander.
			boolean fontSelected = fh.selectFont(chunkText[currentPos]);
			if (replaceUnknownChar && !fontSelected) {
				chunkText[currentPos] = MISSING_CHAR;
			}
			// If a character uses a font different from the previous character,
			// we split the chunk at the point.
			if (fh.isFontChanged()) {
				// For the first character of the chunk, although the font has changed,
				// we will just omit it rather than build a blank chunk.
				if (null == lastFontInfo) {
					lastFontInfo = fh.getFontInfo();
					continue;
				}
				Chunk c = new Chunk(new String(chunkText, chunkStartPos, currentPos - chunkStartPos),
						baseOffset + chunkStartPos, baseLevel, runLevel, lastFontInfo);
				chunkStartPos = currentPos;
				lastFontInfo = fh.getFontInfo();
				return c;
			}
		}

		// currentPos reaches the end of the input chunk.
		if (currentPos >= chunkText.length - 1) {
			Chunk c = new Chunk(new String(chunkText, chunkStartPos, chunkText.length - chunkStartPos),
					baseOffset + chunkStartPos, baseLevel, runLevel, lastFontInfo);
			chunkStartPos = currentPos + 1;
			return c;
		} else {
			return null;
		}
	}

	private Chunk processLineBreak(Chunk lineBreakChunk) {
		int returnCharacterCount = lineBreakChunk.getLength();
		if (null == lastFontInfo) {
			currentPos = currentPos + returnCharacterCount - 1;
			chunkStartPos = currentPos + 1;
			return lineBreakChunk;
		}
		lineBreak = lineBreakChunk;
		Chunk c = new Chunk(new String(chunkText, chunkStartPos, currentPos - chunkStartPos),
				baseOffset + chunkStartPos, baseLevel, runLevel, lastFontInfo);
		currentPos = currentPos + returnCharacterCount - 1;
		chunkStartPos = currentPos;
		return c;
	}

	private Chunk processLineBreak() {
		Chunk lineBreakChunk = null;
		if (chunkText[currentPos] == '\n') {
			lineBreakChunk = new LineBreakChunk("\n");
			lineBreakChunk.setOffset(currentPos);
		} else if (chunkText[currentPos] == '\r') {
			lineBreakChunk = new LineBreakChunk("\r");
			lineBreakChunk.setOffset(currentPos);
			if (currentPos + 1 < chunkText.length && chunkText[currentPos + 1] == '\n') {
				lineBreakChunk.setText("\r\n");
			} else {
				lineBreakChunk.setText("\r");
			}
		}

		if (lineBreakChunk != null) {
			return processLineBreak(lineBreakChunk);
		}
		return null;
	}

	public boolean hasMore() {
		return chunkText.length > chunkStartPos;
	}

	public Chunk getNext() {
		return buildChunk();
	}

}
