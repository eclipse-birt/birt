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

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.awt.Color;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.w3c.dom.css.CSSValue;

public class TextStyle extends AreaConstants {

	protected int fontSize = 9000;
	protected int letterSpacing = 0;
	protected int wordSpacing = 0;
	protected boolean underLine = false;
	protected boolean lineThrough = false;
	protected boolean overLine = false;
	protected boolean hasHyperlink = false;
	protected int direction = 0;
	protected Color color = Color.BLACK;
	protected CSSValue align = IStyle.LEFT_VALUE;
	protected FontInfo fontInfo = null;

	public TextStyle(FontInfo fontInfo, Color color, CSSValue align, int fontSize, int letterSpacing, int wordSpacing,
			boolean underLine, boolean lineThrough, boolean overLine, int direction) {
		this.fontSize = fontSize;
		this.color = color;
		this.align = align;
		this.fontInfo = fontInfo;
		this.letterSpacing = letterSpacing;
		this.wordSpacing = wordSpacing;
		this.underLine = underLine;
		this.overLine = overLine;
		this.lineThrough = lineThrough;
		this.direction = direction;
	}

	public TextStyle(TextStyle style) {
		this.fontSize = style.fontSize;
		this.color = style.color;
		this.align = style.align;
		this.fontInfo = style.fontInfo;
		this.letterSpacing = style.letterSpacing;
		this.wordSpacing = style.wordSpacing;
		this.underLine = style.underLine;
		this.overLine = style.overLine;
		this.lineThrough = style.lineThrough;
		this.direction = style.direction;
		this.hasHyperlink = style.isHasHyperlink();
	}

	public TextStyle(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
	}

	public CSSValue getAlign() {
		return align;
	}

	public void setAlign(CSSValue align) {
		this.align = align;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
	}

	public void setLetterSpacing(int letterSpacing) {
		this.letterSpacing = letterSpacing;
	}

	public void setWordSpacing(int wordSpacing) {
		this.wordSpacing = wordSpacing;
	}

	public void setUnderLine(boolean underLine) {
		this.underLine = underLine;
	}

	public void setLineThrough(boolean lineThrough) {
		this.lineThrough = lineThrough;
	}

	public void setOverLine(boolean overLine) {
		this.overLine = overLine;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isRtl() {
		return direction == DIRECTION_RTL;
	}

	public int getFontSize() {
		return fontSize;
	}

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public int getLetterSpacing() {
		return letterSpacing;
	}

	public int getWordSpacing() {
		return wordSpacing;
	}

	public boolean isUnderline() {
		return underLine;
	}

	public boolean isLinethrough() {
		return lineThrough;
	}

	public boolean isOverline() {
		return overLine;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isHasHyperlink() {
		return hasHyperlink;
	}

	public void setHasHyperlink(boolean hasHyperlink) {
		this.hasHyperlink = hasHyperlink;
	}

}
