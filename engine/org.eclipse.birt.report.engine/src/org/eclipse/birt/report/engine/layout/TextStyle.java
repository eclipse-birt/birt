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

package org.eclipse.birt.report.engine.layout;

import java.awt.Color;

import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.w3c.dom.css.CSSValue;

public class TextStyle {
	private FontInfo fontInfo;
	private int letterSpacing, wordSpacing;
	private Color color;
	private boolean linethrough;
	private boolean overline;
	private boolean underline;
	private boolean rtl; // bidi_hcg
	private CSSValue align;

	public TextStyle(FontInfo fontInfo, int characterSpacing, int wordSpacing, Color color, boolean linethrough,
			boolean overline, boolean underline, boolean rtl, CSSValue align) {
		this.fontInfo = fontInfo;
		this.letterSpacing = characterSpacing;
		this.wordSpacing = wordSpacing;
		this.color = color;
		this.linethrough = linethrough;
		this.overline = overline;
		this.underline = underline;
		this.align = align;
		this.rtl = rtl; // bidi_hcg
	}

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
	}

	public int getLetterSpacing() {
		return letterSpacing;
	}

	public void setLetterSpacing(int letterSpacing) {
		this.letterSpacing = letterSpacing;
	}

	public int getWordSpacing() {
		return wordSpacing;
	}

	public void setWordSpacing(int wordSpacing) {
		this.wordSpacing = wordSpacing;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isLinethrough() {
		return linethrough;
	}

	public void setLinethrough(boolean linethrough) {
		this.linethrough = linethrough;
	}

	public boolean isOverline() {
		return overline;
	}

	public void setOverline(boolean overline) {
		this.overline = overline;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public CSSValue getAlign() {
		return align;
	}

	public void setAlign(CSSValue align) {
		this.align = align;
	}

	public boolean isRtl() {
		return rtl;
	}

	public void setRtl(boolean rtl) {
		this.rtl = rtl;
	}

}
