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

/**
 * <p>
 * An abstract representation of a line of styled text (eg. with a font and font
 * size specified etc.) or a fragment thereof.
 * </p>
 */
public class TextArea extends AbstractArea implements ITextArea {

	protected String text;

	protected String cachedText = null;

	/**
	 * <p>
	 * The soft hyphen Unicode symbol.
	 * </p>
	 * <p>
	 * It needs special handling, because it should only be visible when a
	 * line-break occurs there and hidden otherwise.
	 * </p>
	 * <p>
	 * See
	 * {@link org.eclipse.birt.report.engine.emitter.wpml.writer.AbstractWordXmlWriter#SOFT_HYPHEN}
	 * for more detail.
	 * </p>
	 */
	private static final char SOFT_HYPHEN = '\u00ad';

	/**
	 * <p>
	 * This controls if Unicode SOFT HYPHEN symbols in a text should be removed from
	 * the output. The default value is <tt>true</tt> - remove soft hyphens.
	 * </p>
	 * <p>
	 * By setting the system property <tt>org.eclipse.birt.softhyphen.remove</tt> to
	 * <tt>false</tt>, the old, incorrect behavior of keeping them can be restored.
	 * </p>
	 */
	private boolean removeSoftHyphens = "true".equals(System.getProperty("org.eclipse.birt.softhyphen.remove", "true")); // $NON-NLS-1

	/**
	 * Is this the last TextArea in a line (of TextAreas)?
	 */
	private boolean lastInLine = false;

	/**
	 * Mark this TextArea as being the last in a line.
	 */
	public void markAsLastInLine() {
		lastInLine = true;
	}

	@Override
	public int getWidth() {
		if (lastInLine) {
			return width + softHyphenWidth; // TODO What about letter spacing?
		}
		return width;
	}

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

	private int softHyphenWidth = 0;

	/**
	 * @return Returns the softHyphenWidth.
	 */
	public int getSoftHyphenWidth() {
		return softHyphenWidth;
	}

	TextArea(TextArea area) {
		super(area);
		this.text = area.text;
		this.runLevel = area.runLevel;
		this.style = area.style;
		this.textLength = area.textLength;
		this.offset = area.offset;
	}

	@Override
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

	/**
	 * <p>
	 * Get a string with the text this TextArea represents.
	 * </p>
	 * <p>
	 * SOFT HYPHEN Unicode symbols inside the text are usually removed (depending on
	 * {@link #removeSoftHyphens}), except a trailing one (depending on
	 * {@link #keepTrailingSoftHyphen}).
	 * </p>
	 *
	 * @return The unformatted text.
	 */
	private String calculateText() {
		if (blankLine || text == null) {
			return "";
		}
		String textResult = text.substring(offset, offset + textLength);
		if (removeSoftHyphens) {
			// Remove all Unicode SOFT HYPHEN symbols except a trailing one.
			// FIXME: This is possibly worth performance tuning!
			int indxSoftHyphen = textResult.indexOf(SOFT_HYPHEN);
			for (; indxSoftHyphen >= 0; indxSoftHyphen = textResult.indexOf(SOFT_HYPHEN)) {
				String remaining = textResult.substring(indxSoftHyphen + 1);
				if (lastInLine && remaining.strip().length() == 0)
					break;
				textResult = textResult.substring(0, indxSoftHyphen) + remaining;
			}
		}
		System.out.println(
				"calculateText for #" + hashCode() + "(lineBreak=" + String.valueOf(lineBreak) + ", lastInLine="
						+ String.valueOf(lastInLine) + ", textResult=" + textResult);
		return textResult;
	}

	public void addWord(int textLength, WordWidth wordWidth) {
		this.textLength += textLength;
		this.width += wordWidth.width;
		this.softHyphenWidth = wordWidth.softHyphenWidth;
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

	@Override
	public String getLogicalOrderText() {
		return calculateText();
	}

	/**
	 * Gets the text in visual order.
	 *
	 * @param text the original text.
	 * @return the text in visual order.
	 */
	@Override
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

	@Override
	public TextStyle getTextStyle() {
		return style;
	}

	@Override
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

	@Override
	public TextArea cloneArea() {
		return new TextArea(this);
	}

	public int getWhiteSpaceNumber() {
		return whiteSpaceNumber;
	}

	public void setWhiteSpaceNumber(int whiteSpaceNumber) {
		this.whiteSpaceNumber = whiteSpaceNumber;
	}

	@Override
	public boolean needClip() {
		return needClip;
	}

	@Override
	public String toString() {
		return "TextArea [removeSoftHyphens=" + removeSoftHyphens
				+ ", lastInLine=" + lastInLine + ", runLevel=" + runLevel + ", , textLength=" + textLength + ", text="
				+ (text != null ? text.substring(offset, offset + textLength) : "(null)")
				+ ", lineBreak=" + lineBreak + ", blankLine=" + blankLine + ", maxWidth=" + maxWidth
				+ ", whiteSpaceNumber=" + whiteSpaceNumber + ", needClip=" + needClip + "]";
	}

}
