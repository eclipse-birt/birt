/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

import com.ibm.icu.text.Bidi;
import com.lowagie.text.Font;

public class TextArea extends AbstractArea implements ITextArea {
	protected String text;

	protected FontInfo fi;

	/**
	 * the offset relative to the TextContent, which indicates from where the
	 * TextArea starts.
	 */
	private int offset;

	private ITextContent textContent;

	private int runLevel;
	/**
	 * checks if line break happens
	 */
	private boolean lineBreak;

	/**
	 * flag to show if the line is blank
	 */
	private boolean blankLine = false;

	/**
	 * the character numbers in the TextArea.
	 */
	private int textLength;

	/**
	 * the max width of the TextArea( in 1/1000 points )
	 */
	private int maxWidth;

	/**
	 * @deprecated
	 * @param textContent
	 * @param text
	 * @param fi
	 */
	public TextArea(ITextContent textContent, String text, FontInfo fi) {
		super(textContent);
		this.textContent = textContent;
		this.text = text;
		this.offset = 0;
		this.textLength = text.length();
		this.fi = fi;
		height = (int) (fi.getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO);
		baseLine = this.fi.getBaseline();
		removePadding();
		removeBorder();
		removeMargin();
		hasBoxProperty = false;
	}

	public TextArea(ITextContent textContent, FontInfo fi, boolean blankLine) {
		super(textContent);
		this.textContent = textContent;
		this.fi = fi;
		height = (int) (fi.getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO);
		baseLine = this.fi.getBaseline();
		if (blankLine) {
			this.lineBreak = true;
			this.blankLine = true;
		} else {
			this.offset = 0;
			this.textLength = textContent.getText().length();
		}
		removePadding();
		removeBorder();
		removeMargin();
		hasBoxProperty = false;
	}

	public TextArea(ITextContent textContent, IStyle areaStyle, int offset, int baseLevel, int runLevel,
			FontInfo fontInfo) {
		super(textContent, areaStyle);
		this.textContent = textContent;
		this.fi = fontInfo;
		height = (int) (fi.getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO);
		baseLine = this.fi.getBaseline();
		this.offset = offset;
		this.runLevel = runLevel;
		this.lineBreak = false;
	}

	public TextArea(ITextContent textContent, int offset, int baseLevel, int runLevel, FontInfo fontInfo) {
		super(textContent);
		this.textContent = textContent;
		this.fi = fontInfo;
		height = (int) (fi.getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO);
		baseLine = this.fi.getBaseline();
		this.offset = offset;
		this.runLevel = runLevel;
		this.lineBreak = false;
		removePadding();
		removeBorder();
		removeMargin();
	}

	public boolean lineBreak() {
		return lineBreak;
	}

	public boolean isEmpty() {
		return textLength == 0;
	}

	public void addWord(int textLength, float wordWidth) {
		this.textLength += textLength;
		this.width += wordWidth;
	}

	public void addWordSpacing(int wordSpacing) {
		this.width += wordSpacing;
	}

	public boolean hasSpace(int width) {
		return maxWidth - this.width > width;
	}

	public void setStyle(IStyle style) {
		this.style = style;
	}

	private void calculateText() {
		if (blankLine) {
			this.text = "";
		} else {
			this.text = textContent.getText().substring(offset, offset + textLength);
		}
	}

	public String getLogicalOrderText() {
		calculateText();
		return text;
	}

	/**
	 * Gets the text in visual order.
	 * 
	 * @param text the original text.
	 * @return the text in visual order.
	 */
	public String getText() {
		calculateText();
		if ((runLevel & 1) == 0) {
			return text;
		} else {
			return flip(text);
		}
	}

	private String flip(String text) {
		return Bidi.writeReverse(text, Bidi.OUTPUT_REVERSE | Bidi.DO_MIRRORING);
	}

	public int getRunLevel() {
		return runLevel;
	}

	public FontInfo getFontInfo() {
		return this.fi;
	}

	public void accept(IAreaVisitor visitor) {
		visitor.visitText(this);
	}

	public int getTextLength() {
		return textLength;
	}

	public void setTextLength(int textLength) {
		this.textLength = textLength;
	}

	public boolean isLineBreak() {
		return lineBreak;
	}

	public void setLineBreak(boolean lineBreak) {
		this.lineBreak = lineBreak;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public int getWidth() {
		int fontStyle = fi.getFontStyle();
		// get width for text with simulated italic font.
		if (fi.getSimulation() && (Font.ITALIC == fontStyle || Font.BOLDITALIC == fontStyle)) {
			width = (int) (width + height * EmitterUtil.getItalicHorizontalCoefficient());
		}
		return width;
	}
}
