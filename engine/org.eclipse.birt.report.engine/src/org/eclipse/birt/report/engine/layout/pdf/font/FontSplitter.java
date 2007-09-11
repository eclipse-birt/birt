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

import java.text.Bidi;

import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.pdf.ISplitter;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;

public class FontSplitter implements ISplitter
{
	/**
	 * If no font can display a charater, replace the character with this one. 
	 * Make sure MISSING_CHAR can be displayed with DEFAUTL_FONT.
	 */
	public static final char MISSING_CHAR = '?';
	
	private FontHandler fh = null;
	private boolean fontSubstitution;
	
	private int baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;
	private int runDirection = Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT;
	private int baseOffset = 0;
	private char[] chunkText = null;

	private int chunkStartPos = 0;
	private int currentPos = -1;

	private FontInfo lastFontInfo = null;
	
	private boolean encounteredReturn = false;

	public FontSplitter(Chunk inputChunk, ITextContent textContent, 
			boolean fontSubstitution, String format)
	{
		this.fontSubstitution = fontSubstitution;
		this.chunkText = inputChunk.getText().toCharArray();
		baseOffset = inputChunk.getOffset();
		baseLevel = inputChunk.getBaseLevel();
		runDirection = inputChunk.getRunDirection();
		this.fh = new FontHandler(textContent, fontSubstitution, format);
	}
	
	private Chunk buildChunk()
	{
		if (!fontSubstitution)
		{
			Chunk c = new Chunk(new String(chunkText),
					baseOffset, baseLevel, runDirection, fh.getFontInfo());
			chunkStartPos = chunkText.length;
			return c;	
		}
		
		if (encounteredReturn)
		{
			encounteredReturn = false;
			chunkStartPos ++;
			return Chunk.HARD_LINE_BREAK;
		}
		
		while (++currentPos < chunkText.length)
		{	
			if (chunkText[currentPos] == '\n')
			{	
				// If the first character of a chunk is return carriage, return a 
				// Chunk.HARD_LINE_BREAK directly.
				if (null == lastFontInfo)
				{
					chunkStartPos = currentPos + 1;
					return Chunk.HARD_LINE_BREAK;
				}
				encounteredReturn = true;
				Chunk c = new Chunk(new String(chunkText, chunkStartPos, currentPos-chunkStartPos), 
				baseOffset + chunkStartPos, baseLevel, runDirection, lastFontInfo);
				chunkStartPos = currentPos;
				return c;
			}
			//We fail to find a font to display the character,
			//we replace this character with MISSING_CHAR defined in FontHander.
			if (!fh.selectFont(chunkText[currentPos]))
			{
				chunkText[currentPos] = MISSING_CHAR;
			}
			//If a character uses a font different from the previous character,
			//we split the chunk at the point.
			if (fh.isFontChanged())
			{
				//For the first character of the chunk, although the font has changed,
				//we will just omit it rather than build a blank chunk.
				if (null == lastFontInfo)
				{
					lastFontInfo = fh.getFontInfo();
					continue;
				}
				Chunk c = new Chunk(new String(chunkText, chunkStartPos, currentPos-chunkStartPos), 
						baseOffset + chunkStartPos, baseLevel, runDirection, lastFontInfo);
				chunkStartPos = currentPos;
				lastFontInfo = fh.getFontInfo();
				return c;
			}
		}
		
		//currentPos reaches the end of the input chunk. 
		if (currentPos >= chunkText.length -1)
		{
			Chunk c = new Chunk(new String(chunkText, chunkStartPos, chunkText.length - chunkStartPos),
					baseOffset + chunkStartPos, baseLevel, runDirection, lastFontInfo);
			chunkStartPos = currentPos + 1;
			return c;	
		}
		else
		{
			return null;
		}
	}
	
	public boolean hasMore()
	{
		return chunkText.length > chunkStartPos;
	}
	
	public Chunk getNext()
	{
		return buildChunk();
	}
	
}
