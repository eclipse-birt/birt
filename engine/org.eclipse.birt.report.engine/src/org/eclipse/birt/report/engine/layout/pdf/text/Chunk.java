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

package org.eclipse.birt.report.engine.layout.pdf.text;

import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

import com.ibm.icu.text.Bidi;

/**
 * A Chunk is a piece of text and formatting metadata like what font to use and
 * BIDI setting. The font info is needed for computing width and height.
 */
public class Chunk {
	private String text;
	private int offset;
	private FontInfo fontInfo;
	private int baseLevel;
	private int runLevel;

	/** property: line break */
	public static final Chunk HARD_LINE_BREAK = new Chunk("\n"); //$NON-NLS-1$

	/**
	 * Constructor 1
	 *
	 * @param text chunk text
	 */
	public Chunk(String text) {
		this(text, 0, Bidi.DIRECTION_LEFT_TO_RIGHT, Bidi.DIRECTION_LEFT_TO_RIGHT, null);
	}

	/**
	 * Constructor 2
	 *
	 * @param text   chunk text
	 * @param offset offset
	 * @param fi     font info
	 */
	public Chunk(String text, int offset, FontInfo fi) {
		this(text, offset, Bidi.DIRECTION_LEFT_TO_RIGHT, Bidi.DIRECTION_LEFT_TO_RIGHT, fi);
	}

	/**
	 * Constructor 3
	 *
	 * @param chunk chunk of text
	 */
	public Chunk(Chunk chunk) {
		this(chunk.text, chunk.offset, chunk.baseLevel, chunk.runLevel, null);
	}

	/**
	 * Constructor 4
	 *
	 * @param text      chunk text
	 * @param offset    text offset
	 * @param baseLevel base level
	 * @param runLevel  run level
	 */
	public Chunk(String text, int offset, int baseLevel, int runLevel) {
		this(text, offset, baseLevel, runLevel, null);
	}

	/**
	 * Constructor 5
	 *
	 * @param text      chunk text
	 * @param offset    text offset
	 * @param baseLevel base level
	 * @param runLevel  run level
	 * @param fi        font info
	 */
	public Chunk(String text, int offset, int baseLevel, int runLevel, FontInfo fi) {
		this.text = text;
		this.offset = offset;
		this.fontInfo = null;
		this.baseLevel = baseLevel;
		this.runLevel = runLevel;
		this.fontInfo = fi;
	}

	/**
	 * Set the text
	 *
	 * @param text chunk text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Set the text offset
	 *
	 * @param offset text offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Get the chunk text
	 *
	 * @return Return the chunk text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Get the chunk text length
	 *
	 * @return Return the chunk text length
	 */
	public int getLength() {
		if (text != null) {
			return text.length();
		}
		return 0;
	}

	/**
	 * Set the font info
	 *
	 * @param fi font info
	 */
	public void setFontInfo(FontInfo fi) {
		this.fontInfo = fi;
	}

	/**
	 * Get the font info
	 *
	 * @return Return the ont info
	 */
	public FontInfo getFontInfo() {
		return this.fontInfo;
	}

	/**
	 * Return the offset
	 *
	 * @return Get the offset
	 */
	public int getOffset() {
		return this.offset;
	}

	/**
	 * Set the base level
	 *
	 * @param baseLevel base level
	 */
	public void setBaseLevel(int baseLevel) {
		this.baseLevel = baseLevel;
	}

	/**
	 * Return the base level
	 *
	 * @return Get the base level
	 */
	public int getBaseLevel() {
		return this.baseLevel;
	}

	/**
	 * Sets run level of this chunk.
	 *
	 * @param runLevel An integer value from 0 to 62
	 *
	 * @see #getRunLevel()
	 *
	 * @author bidi_hcg
	 */
	public void setRunLevel(int runLevel) {
		this.runLevel = runLevel;
	}

	/**
	 * Returns an absolute embedding (nesting) level of this chunk.<br>
	 * Can be an integer value from 0 to 62. See<br>
	 * <a href="http://unicode.org/unicode/standard/reports/tr9/tr9-6.html">The
	 * Bidirectional Algorithm</a>
	 *
	 * @return Embedding level
	 *
	 * @author bidi_hcg
	 */
	public int getRunLevel() {
		return this.runLevel;
	}
}
