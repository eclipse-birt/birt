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

import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.w3c.dom.css.CSSValue;

/**
 * Class to implements the text style
 *
 * @since 3.3
 *
 */
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
	protected CSSValue align = CSSValueConstants.LEFT_VALUE;
	protected FontInfo fontInfo = null;

	/**
	 * Constructor 1
	 *
	 * @param fontInfo      font info
	 * @param color         text color
	 * @param align         text alignment
	 * @param fontSize      font size
	 * @param letterSpacing letter spacing
	 * @param wordSpacing   word spacing
	 * @param underLine     text is underlined
	 * @param lineThrough   test use line through
	 * @param overLine      text is overlined
	 * @param direction     text direction
	 */
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

	/**
	 * Constructor 2
	 *
	 * @param style text style
	 */
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

	/**
	 * Constructor 3
	 *
	 * @param fontInfo font info object
	 */
	public TextStyle(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
	}

	/**
	 * Get the text alignment
	 *
	 * @return Return the text alignment
	 */
	public CSSValue getAlign() {
		return align;
	}

	/**
	 * Set the text alignment
	 *
	 * @param align text alignment
	 */
	public void setAlign(CSSValue align) {
		this.align = align;
	}

	/**
	 * Set the text color
	 *
	 * @param color color of the text
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Get the text color
	 *
	 * @return Return the text color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the font size
	 *
	 * @param fontSize font size
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Set the font info
	 *
	 * @param fontInfo font info
	 */
	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
	}

	/**
	 * Set the letter spacing
	 *
	 * @param letterSpacing letter spacing
	 */
	public void setLetterSpacing(int letterSpacing) {
		this.letterSpacing = letterSpacing;
	}

	/**
	 * Set the word spacing
	 *
	 * @param wordSpacing word spacing
	 */
	public void setWordSpacing(int wordSpacing) {
		this.wordSpacing = wordSpacing;
	}

	/**
	 * Set the text underline
	 *
	 * @param underLine text is underlined
	 */
	public void setUnderLine(boolean underLine) {
		this.underLine = underLine;
	}

	/**
	 * Set the line through
	 *
	 * @param lineThrough text is lined through
	 */
	public void setLineThrough(boolean lineThrough) {
		this.lineThrough = lineThrough;
	}

	/**
	 * Set the overline
	 *
	 * @param overLine the text is overlined
	 */
	public void setOverLine(boolean overLine) {
		this.overLine = overLine;
	}

	/**
	 * Set the text direction
	 *
	 * @param direction text direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Check if the text direction is RtL
	 *
	 * @return Return the check result of RtL
	 */
	public boolean isRtl() {
		return direction == DIRECTION_RTL;
	}

	/**
	 * Get the font size
	 *
	 * @return Return the font size
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * Get the font info
	 *
	 * @return Return the font info
	 */
	public FontInfo getFontInfo() {
		return fontInfo;
	}

	/**
	 * Get the letter spacing
	 *
	 * @return Return the letter spacing
	 */
	public int getLetterSpacing() {
		return letterSpacing;
	}

	/**
	 * Get the word spacing
	 *
	 * @return Return the word spacing
	 */
	public int getWordSpacing() {
		return wordSpacing;
	}

	/**
	 * Check if text is underlined
	 *
	 * @return Return the check result
	 */
	public boolean isUnderline() {
		return underLine;
	}

	/**
	 * Check if the text use line through
	 *
	 * @return Return the check result
	 */
	public boolean isLinethrough() {
		return lineThrough;
	}

	/**
	 * Check if the text is underlined
	 *
	 * @return Return the check result
	 */
	public boolean isOverline() {
		return overLine;
	}

	/**
	 * Get the text direction
	 *
	 * @return Return the text direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Check if the text has a hyperlink
	 *
	 * @return Return the check result
	 */
	public boolean isHasHyperlink() {
		return hasHyperlink;
	}

	/**
	 * Set the text use a hyperlink
	 *
	 * @param hasHyperlink the text use a hyperlink
	 */
	public void setHasHyperlink(boolean hasHyperlink) {
		this.hasHyperlink = hasHyperlink;
	}

}
