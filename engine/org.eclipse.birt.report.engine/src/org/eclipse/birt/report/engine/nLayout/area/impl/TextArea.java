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

import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

import com.ibm.icu.text.Bidi;

public class TextArea extends AbstractArea implements ITextArea {

	protected String text;

	protected String cachedText = null;

	protected int runLevel;

	protected TextStyle style;

	/**
	 * the character numbers in the TextArea.
	 */
	protected int textLength;

	/**
	 * the offset relative to the TextContent, which indicates from where the
	 * TextArea starts.
	 */
	protected int offset;

	/**
	 * checks if line break happens
	 */
	protected boolean lineBreak;

	/**
	 * flag to show if the line is blank
	 */
	protected boolean blankLine = false;

	/**
	 * the max width of the TextArea( in 1/1000 points )
	 */
	protected int maxWidth;

	/**
	 * The whiteSpaceNumber indicates the number of white spaces(except for the
	 * right most white space) in current text area. This field is used to justify
	 * the textArea.
	 */
	protected int whiteSpaceNumber;

	protected boolean needClip = false;

	TextArea(TextArea area) {
		super(area);
		this.text = area.text;
		this.runLevel = area.runLevel;
		this.style = area.style;
		this.textLength = area.textLength;
		this.offset = area.offset;
	}

	public int getBaseLine() {
		if (style != null) {
			return style.getFontInfo().getBaseline();
		}
		return super.getBaseLine();
	}

	public TextArea(String text, TextStyle style) {
		this.text = text;
		this.style = style;
		this.height = (int) (style.getFontInfo().getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO);
	}

	public TextArea(TextStyle style) {
		this.style = style;
		this.height = (int) (style.getFontInfo().getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO);
	}

	public void setRunLevel(int runLevel) {
		this.runLevel = runLevel;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isLineBreak() {
		return lineBreak;
	}

	public void setLineBreak(boolean lineBreak) {
		this.lineBreak = lineBreak;
	}

	public int getTextLength() {
		return textLength;
	}

	private String calculateText() {
		if (blankLine || text == null) {
			return "";
		} else {
			return text.substring(offset, offset + textLength);
		}
	}

	public void addWord(int textLength, float wordWidth) {
		this.textLength += textLength;
		this.width += wordWidth;
	}

	public void addWordUsingMaxWidth(int textLength) {
		this.textLength += textLength;
		this.width = maxWidth;
		this.needClip = true;
	}

	public void addWordSpacing(int wordSpacing) {
		this.width += wordSpacing;
	}

	public boolean hasSpace(int width) {
		return maxWidth - this.width > width;
	}

	public boolean isEmpty() {
		return textLength == 0;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public void setStyle(TextStyle style) {
		this.style = style;
	}

	public TextStyle getStyle() {
		return style;
	}

	public String getLogicalOrderText() {
		return calculateText();
	}

	/**
	 * Gets the text in visual order.
	 * 
	 * @param text the original text.
	 * @return the text in visual order.
	 */
	public String getText() {
		if (cachedText == null) {
			if ((runLevel & 1) == 0) {
				cachedText = calculateText();
			} else {
				cachedText = flip(calculateText());
			}
		}
		return cachedText;
	}

	public void setText(String text) {
		cachedText = text;
	}

	public void setTextLength(int textLength) {
		this.textLength = textLength;
	}

	private String flip(String text) {
		return Bidi.writeReverse(text, Bidi.OUTPUT_REVERSE | Bidi.DO_MIRRORING);
	}

	public int getRunLevel() {
		return runLevel;
	}

	public TextStyle getTextStyle() {
		return style;
	}

	public void accept(IAreaVisitor visitor) {
		visitor.visitText(this);
	}

	public int getTextWidth(String text) {
		FontInfo fontInfo = style.getFontInfo();
		if (null != fontInfo) {
			return (int) (style.getFontInfo().getWordWidth(text) * PDFConstants.LAYOUT_TO_PDF_RATIO);
		} else {
			return 0;
		}
	}

	public TextArea cloneArea() {
		return new TextArea(this);
	}

	public int getWhiteSpaceNumber() {
		return whiteSpaceNumber;
	}

	public void setWhiteSpaceNumber(int whiteSpaceNumber) {
		this.whiteSpaceNumber = whiteSpaceNumber;
	}

	public boolean needClip() {
		return needClip;
	}
}
