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

package org.eclipse.birt.report.engine.layout.pdf.text;

import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

import com.ibm.icu.text.Bidi;

public class Chunk {
	private String text;
	private int offset;
	private FontInfo fontInfo;
	private int baseLevel;
	private int runLevel;

	public static final Chunk HARD_LINE_BREAK = new Chunk("\n"); //$NON-NLS-1$

	public Chunk(String text) {
		this(text, 0, Bidi.DIRECTION_LEFT_TO_RIGHT, Bidi.DIRECTION_LEFT_TO_RIGHT, null);
	}

	public Chunk(String text, int offset, FontInfo fi) {
		this(text, offset, Bidi.DIRECTION_LEFT_TO_RIGHT, Bidi.DIRECTION_LEFT_TO_RIGHT, fi);
	}

	public Chunk(Chunk chunk) {
		this(chunk.text, chunk.offset, chunk.baseLevel, chunk.runLevel, null);
	}

	public Chunk(String text, int offset, int baseLevel, int runLevel) {
		this(text, offset, baseLevel, runLevel, null);
	}

	public Chunk(String text, int offset, int baseLevel, int runLevel, FontInfo fi) {
		this.text = text;
		this.offset = offset;
		this.fontInfo = null;
		this.baseLevel = baseLevel;
		this.runLevel = runLevel;
		this.fontInfo = fi;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getText() {
		return this.text;
	}

	public int getLength() {
		if (text != null) {
			return text.length();
		}
		return 0;
	}

	public void setFontInfo(FontInfo fi) {
		this.fontInfo = fi;
	}

	public FontInfo getFontInfo() {
		return this.fontInfo;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setBaseLevel(int baseLevel) {
		this.baseLevel = baseLevel;
	}

	public int getBaseLevel() {
		return this.baseLevel;
	}

	/**
	 * Sets direction of this chunk.
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
