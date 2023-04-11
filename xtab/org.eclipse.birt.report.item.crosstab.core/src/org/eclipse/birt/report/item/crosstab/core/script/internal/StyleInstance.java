/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

/**
 * A class representing the style of a report item in the scripting environment
 */
public class StyleInstance implements IScriptStyle {

	private IStyle style;

	/**
	 * Constructor
	 *
	 * @param style
	 */
	public StyleInstance(IStyle style) {
		this.style = style;
	}

	@Override
	public String getVisibleFormat() {
		return style.getVisibleFormat();
	}

	@Override
	public void setVisibleFormat(String format) {
		style.setVisibleFormat(format);
	}

	/**
	 * Get the letter spacing
	 */
	@Override
	public String getLetterSpacing() {
		return style.getLetterSpacing();
	}

	/**
	 * Set the letter spacing
	 */
	@Override
	public void setLetterSpacing(String spacing) {
		style.setLetterSpacing(spacing);
	}

	/**
	 * Get the line height
	 */
	@Override
	public String getLineHeight() {
		return style.getLineHeight();
	}

	/**
	 * Set the line height
	 */
	@Override
	public void setLineHeight(String lineHeight) {
		style.setLineHeight(lineHeight);
	}

	/**
	 * Get the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	@Override
	public String getTextAlign() {
		return style.getTextAlign();
	}

	/**
	 * Set the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	@Override
	public void setTextAlign(String align) {
		style.setTextAlign(align);
	}

	/**
	 * Get the text indent
	 */
	@Override
	public String getTextIndent() {
		return style.getTextIndent();
	}

	/**
	 * Get the text indent
	 */
	@Override
	public void setTextIndent(String indent) {
		style.setTextIndent(indent);
	}

	/**
	 * Get the text transform. Valid return values are NONE, CAPITALIZE, UPPERCASE
	 * and LOWERCASE.
	 */
	@Override
	public String getTextTransform() {
		return style.getTextTransform();
	}

	/**
	 * Set the text transform. Valid transform values are NONE, CAPITALIZE,
	 * UPPERCASE and LOWERCASE.
	 */
	@Override
	public void setTextTransform(String transform) {
		style.setTextTransform(transform);
	}

	/**
	 * Get the vertical alignment. Valid return values are BASELINE, SUB, SUPER,
	 * TOP, TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
	@Override
	public String getVerticalAlign() {
		return style.getVerticalAlign();
	}

	/**
	 * Set the vertical alignment. Valid values are BASELINE, SUB, SUPER, TOP,
	 * TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
	@Override
	public void setVerticalAlign(String valign) {
		style.setVerticalAlign(valign);
	}

	/**
	 * Get the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	@Override
	public String getWhiteSpace() {
		return style.getWhiteSpace();
	}

	/**
	 * Get the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	@Override
	public void setWhiteSpace(String whitespace) {
		style.setWhiteSpace(whitespace);
	}

	/**
	 * Get the word spacing
	 */
	@Override
	public String getWordSpacing() {
		return style.getWordSpacing();
	}

	/**
	 * Set the word spacing
	 */
	@Override
	public void setWordSpacing(String wordspacing) {
		style.setWordSpacing(wordspacing);
	}

	/**
	 * Get the font color
	 */
	@Override
	public String getColor() {
		return style.getColor();
	}

	/**
	 * Set the font color
	 */
	@Override
	public void setColor(String color) {
		style.setColor(color);
	}

	/**
	 * Get the attachement type (either SCROLL or FIXED)
	 *
	 * @deprecated replaced by getBackgroundAttachment()
	 */
	@Deprecated
	@Override
	public String getBackgroundAttachement() {
		return style.getBackgroundAttachment();
	}

	/**
	 * Set the attachement type (either SCROLL or FIXED)
	 *
	 * @deprecated replaced by setBackgroundAttachment( String attachment )
	 */
	@Deprecated
	@Override
	public void setBackgroundAttachement(String attachement) {
		style.setBackgroundAttachment(attachement);
	}

	/**
	 * Get the attachment type (either SCROLL or FIXED)
	 */
	@Override
	public String getBackgroundAttachment() {
		return style.getBackgroundAttachment();
	}

	/**
	 * Set the attachment type (either SCROLL or FIXED)
	 */
	@Override
	public void setBackgroundAttachment(String attachment) {
		style.setBackgroundAttachment(attachment);
	}

	/**
	 * Get the background color
	 */
	@Override
	public String getBackgroundColor() {
		return style.getBackgroundColor();
	}

	/**
	 * Set the background color
	 */
	@Override
	public void setBackgroundColor(String color) {
		style.setBackgroundColor(color);
	}

	/**
	 * Get the background image URI
	 */
	@Override
	public String getBackgroundImage() {
		return style.getBackgroundImage();
	}

	/**
	 * Set the background image URI
	 */
	@Override
	public void setBackgroundImage(String imageURI) {
		style.setBackgroundImage(imageURI);
	}

	/**
	 * Get the background image source type
	 */
	@Override
	public String getBackgroundImageType() {
		return style.getBackgroundImageType();
	}

	/**
	 * Set the background image source type
	 */
	@Override
	public void setBackgroundImageType(String imageSourceType) {
		style.setBackgroundImageType(imageSourceType);
	}

	/**
	 * Get the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	@Override
	public String getBackgroundRepeat() {
		return style.getBackgroundRepeat();
	}

	/**
	 * Set the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	@Override
	public void setBackgroundRepeat(String repeat) {
		style.setBackgroundRepeat(repeat);
	}

	/**
	 * Get the bottom border color
	 */
	@Override
	public String getBorderBottomColor() {
		return style.getBorderBottomColor();
	}

	/**
	 * Set the bottom border color
	 */
	@Override
	public void setBorderBottomColor(String color) {
		style.setBorderBottomColor(color);
	}

	/**
	 * Get the top border color
	 */
	@Override
	public String getBorderTopColor() {
		return style.getBorderTopColor();
	}

	/**
	 * Set the top border color
	 */
	@Override
	public void setBorderTopColor(String color) {
		style.setBorderTopColor(color);
	}

	/**
	 * Get the right border color
	 */
	@Override
	public String getBorderRightColor() {
		return style.getBorderRightColor();
	}

	/**
	 * Set the right border color
	 */
	@Override
	public void setBorderRightColor(String color) {
		style.setBorderRightColor(color);
	}

	/**
	 * Get the left border color
	 */
	@Override
	public String getBorderLeftColor() {
		return style.getBorderLeftColor();
	}

	/**
	 * Set the left border color
	 */
	@Override
	public void setBorderLeftColor(String color) {
		style.setBorderLeftColor(color);
	}

	/**
	 * Get the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderBottomStyle() {
		return style.getBorderBottomStyle();
	}

	/**
	 * Set the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderBottomStyle(String borderstyle) {
		style.setBorderBottomStyle(borderstyle);
	}

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderTopStyle() {
		return style.getBorderTopStyle();
	}

	/**
	 * Set the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderTopStyle(String borderstyle) {
		style.setBorderTopStyle(borderstyle);
	}

	/**
	 * Get the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderLeftStyle() {
		return style.getBorderLeftStyle();
	}

	/**
	 * Set the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderLeftStyle(String borderstyle) {
		style.setBorderLeftStyle(borderstyle);
	}

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderRightStyle() {
		return style.getBorderRightStyle();
	}

	/**
	 * Set the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderRightStyle(String borderstyle) {
		style.setBorderRightStyle(borderstyle);
	}

	/**
	 * Get the bottom border width.
	 */
	@Override
	public String getBorderBottomWidth() {
		return style.getBorderBottomWidth();
	}

	/**
	 * Set the bottom border width.
	 */
	@Override
	public void setBorderBottomWidth(String width) {
		style.setBorderBottomWidth(width);
	}

	/**
	 * Get the top border width.
	 */
	@Override
	public String getBorderTopWidth() {
		return style.getBorderTopWidth();
	}

	/**
	 * Set the top border width.
	 */
	@Override
	public void setBorderTopWidth(String width) {
		style.setBorderTopWidth(width);
	}

	/**
	 * Get the left border width.
	 */
	@Override
	public String getBorderLeftWidth() {
		return style.getBorderLeftWidth();
	}

	/**
	 * Set the left border width.
	 */
	@Override
	public void setBorderLeftWidth(String width) {
		style.setBorderLeftWidth(width);
	}

	/**
	 * Get the right border width.
	 */
	@Override
	public String getBorderRightWidth() {
		return style.getBorderRightWidth();
	}

	/**
	 * Set the right border width.
	 */
	@Override
	public void setBorderRightWidth(String width) {
		style.setBorderRightWidth(width);
	}

	/**
	 * Get the bottom margin
	 */
	@Override
	public String getMarginBottom() {
		return style.getMarginBottom();
	}

	/**
	 * Set the bottom margin
	 */
	@Override
	public void setMarginBottom(String margin) {
		style.setMarginBottom(margin);
	}

	/**
	 * Get the top margin
	 */
	@Override
	public String getMarginTop() {
		return style.getMarginTop();
	}

	/**
	 * Set the top margin
	 */
	@Override
	public void setMarginTop(String margin) {
		style.setMarginTop(margin);
	}

	/**
	 * Get the left margin
	 */
	@Override
	public String getMarginLeft() {
		return style.getMarginLeft();
	}

	/**
	 * Set the left margin
	 */
	@Override
	public void setMarginLeft(String margin) {
		style.setMarginLeft(margin);
	}

	/**
	 * Get the right margin
	 */
	@Override
	public String getMarginRight() {
		return style.getMarginRight();
	}

	/**
	 * Set the right margin
	 */
	@Override
	public void setMarginRight(String margin) {
		style.setMarginRight(margin);
	}

	/**
	 * Get the bottom padding.
	 */
	@Override
	public String getPaddingBottom() {
		return style.getPaddingBottom();
	}

	/**
	 * Set the bottom padding.
	 */
	@Override
	public void setPaddingBottom(String padding) {
		style.setPaddingBottom(padding);
	}

	/**
	 * Get the top padding.
	 */
	@Override
	public String getPaddingTop() {
		return style.getPaddingTop();
	}

	/**
	 * Set the top padding.
	 */
	@Override
	public void setPaddingTop(String padding) {
		style.setPaddingTop(padding);
	}

	/**
	 * Get the left padding.
	 */
	@Override
	public String getPaddingLeft() {
		return style.getPaddingLeft();
	}

	/**
	 * Set the left padding.
	 */
	@Override
	public void setPaddingLeft(String padding) {
		style.setPaddingLeft(padding);
	}

	/**
	 * Get the right padding.
	 */
	@Override
	public String getPaddingRight() {
		return style.getPaddingRight();
	}

	/**
	 * Set the right padding.
	 */
	@Override
	public void setPaddingRight(String padding) {
		style.setPaddingRight(padding);
	}

	/**
	 * Get the display type (valid types are BLOCK, INLINE and NONE)
	 */
	@Override
	public String getDisplay() {
		return style.getDisplay();
	}

	/**
	 * Set the display type (valid types are BLOCK, INLINE and NONE)
	 */
	@Override
	public void setDisplay(String display) {
		style.setDisplay(display);
	}

	/**
	 * Get the master page
	 */
	@Override
	public String getMasterPage() {
		return style.getMasterPage();
	}

	/**
	 * Set the master page
	 */
	@Override
	public void setMasterPage(String masterPage) {
		style.setMasterPage(masterPage);
	}

	/**
	 * Get the page break before.
	 */
	@Override
	public String getPageBreakBefore() {
		return style.getPageBreakBefore();
	}

	/**
	 * Set the page break before
	 */
	@Override
	public void setPageBreakBefore(String pageBreak) {
		style.setPageBreakBefore(pageBreak);
	}

	/**
	 * Get the page break after.
	 */
	@Override
	public String getPageBreakAfter() {
		return style.getPageBreakAfter();
	}

	/**
	 * Set the page break after
	 */
	@Override
	public void setPageBreakAfter(String pageBreak) {
		style.setPageBreakAfter(pageBreak);
	}

	/**
	 * Get the page break inside.
	 */
	@Override
	public String getPageBreakInside() {
		return style.getPageBreakInside();
	}

	/**
	 * Set the page break inside
	 */
	@Override
	public void setPageBreakInside(String pageBreak) {
		style.setPageBreakInside(pageBreak);
	}

	/**
	 * Show if blank?
	 */
	@Override
	public String getShowIfBlank() {
		return style.getShowIfBlank();
	}

	/**
	 * Set show if blank
	 */
	@Override
	public void setShowIfBlank(String showIfBlank) {
		style.setShowIfBlank(showIfBlank);
	}

	/**
	 * Can this element shrink?
	 */
	@Override
	public String getCanShrink() {
		return style.getCanShrink();
	}

	/**
	 * Can this element shrink?
	 */
	@Override
	public void setCanShrink(String canShrink) {
		style.setCanShrink(canShrink);
	}

	/**
	 * Get number format
	 *
	 * @return the number format
	 */
	@Override
	public String getNumberFormat() {
		return style.getNumberFormat();
	}

	/**
	 * Set the number format
	 *
	 * @param numberFormat
	 */
	@Override
	public void setNumberFormat(String numberFormat) {
		style.setNumberFormat(numberFormat);
	}

	/**
	 * Get the date format
	 *
	 * @return date format
	 */
	@Override
	public String getDateFormat() {
		return style.getDateFormat();
	}

	/**
	 * Set the date format
	 *
	 * @param dateTimeFormat
	 */
	@Override
	public void setDateFormat(String dateTimeFormat) {
		style.setDateFormat(dateTimeFormat);
	}

	/**
	 * Get the string format
	 *
	 * @return the string format
	 */
	@Override
	public String getStringFormat() {
		return style.getStringFormat();
	}

	/**
	 * Set the string format
	 *
	 * @param stringFormat
	 */
	@Override
	public void setStringFormat(String stringFormat) {
		style.setStringFormat(stringFormat);
	}

	@Override
	public String getFontFamily() {
		return style.getFontFamily();
	}

	@Override
	public void setFontFamily(String fontFamily) {
		style.setFontFamily(fontFamily);
	}

	@Override
	public String getFontStyle() {
		return style.getFontStyle();
	}

	@Override
	public void setFontStyle(String fontStyle) {
		style.setFontStyle(fontStyle);
	}

	@Override
	public String getFontVariant() {
		return style.getFontVariant();
	}

	@Override
	public void setFontVariant(String fontVariant) {
		style.setFontVariant(fontVariant);
	}

	@Override
	public String getFontWeight() {
		return style.getFontWeight();
	}

	@Override
	public void setFontWeight(String fontWeight) {
		style.setFontWeight(fontWeight);
	}

	@Override
	public String getFontSize() {
		return style.getFontSize();
	}

	@Override
	public void setFontSize(String fontSize) {
		style.setFontSize(fontSize);
	}

	@Override
	public String getTextUnderline() {
		return style.getTextUnderline();
	}

	@Override
	public void setTextUnderline(String underline) throws ScriptException {
		try {
			style.setTextUnderline(underline);
		} catch (DOMException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getTextOverline() {
		return style.getTextOverline();
	}

	@Override
	public void setTextOverline(String overline) throws ScriptException {
		try {
			style.setTextOverline(overline);
		} catch (DOMException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getTextLineThrough() {
		return style.getTextLineThrough();
	}

	@Override
	public void setTextLineThrough(String through) throws ScriptException {
		try {
			style.setTextLineThrough(through);
		} catch (DOMException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getBackgroundPositionX() {
		return style.getBackgroundPositionX();
	}

	@Override
	public void setBackgroundPositionX(String x) throws ScriptException {
		try {
			style.setBackgroundPositionX(x);
		} catch (DOMException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getBackgroundPositionY() {
		return style.getBackgroundPositionY();
	}

	@Override
	public void setBackgroundPositionY(String y) throws ScriptException {
		try {
			style.setBackgroundPositionY(y);
		} catch (DOMException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Get the property
	 *
	 * @param index
	 * @return Return the property
	 */
	public CSSValue getProperty(int index) {
		return style.getProperty(index);
	}

	/**
	 * Get the text direction. Valid return types are LTR and RTL.
	 *
	 * @author bidi_hcg
	 * @return Return the text direction.
	 */
	public String getDirection() {
		return style.getDirection();
	}

	/**
	 * Set the text direction. Valid return types are LTR and RTL.
	 *
	 * @author bidi_hcg
	 * @param dir
	 */
	public void setDirection(String dir) {
		style.setDirection(dir);
	}

	@Override
	public String getDateLocale() {
		DataFormatValue value = style.getDataFormat();
		if (value == null) {
			return null;
		}
		return value.getDateTimeLocale();
	}

	@Override
	public String getNumberLocale() {
		DataFormatValue value = style.getDataFormat();
		if (value == null) {
			return null;
		}
		return value.getNumberLocale();
	}

	@Override
	public String getStringLocale() {
		DataFormatValue value = style.getDataFormat();
		if (value == null) {
			return null;
		}
		return value.getStringLocale();
	}

	@Override
	public void setDateLocale(String locale) {
		DataFormatValue oldValue = style.getDataFormat();
		DataFormatValue value = DataFormatValue.createDataFormatValue(oldValue);
		style.setDataFormat(value);
		value.setDateTimeFormat(oldValue == null ? null : oldValue.getDateTimePattern(), locale);
	}

	@Override
	public void setNumberLocale(String locale) {
		DataFormatValue oldValue = style.getDataFormat();
		DataFormatValue value = DataFormatValue.createDataFormatValue(oldValue);
		style.setDataFormat(value);
		value.setNumberFormat(oldValue == null ? null : oldValue.getNumberPattern(), locale);
	}

	@Override
	public void setStringLocale(String locale) {
		DataFormatValue oldValue = style.getDataFormat();
		DataFormatValue value = DataFormatValue.createDataFormatValue(oldValue);
		style.setDataFormat(value);
		value.setStringFormat(oldValue == null ? null : oldValue.getStringPattern(), locale);
	}

	@Override
	public int getDiagonalNumber() {
		return this.style.getDiagonalNumber();
	}

	@Override
	public void setDiagonalNumber(int number) {
		this.style.setDiagonalNumber(number);
	}

	@Override
	public String getDiagonalStyle() {
		return this.style.getDiagonalStyle();
	}

	@Override
	public void setDiagonalStyle(String style) {
		this.style.setDiagonalStyle(style);
	}

	@Override
	public String getDiagonalWidth() {
		return this.style.getDiagonalWidth();
	}

	@Override
	public void setDiagonalWidth(String width) {
		this.style.setDiagonalWidth(width);
	}

	@Override
	public String getDiagonalColor() {
		return this.style.getDiagonalColor();
	}

	@Override
	public void setDiagonalColor(String color) {
		this.style.setDiagonalColor(color);
	}

	@Override
	public int getAntidiagonalNumber() {
		return this.style.getAntidiagonalNumber();
	}

	@Override
	public void setAntidiagonalNumber(int number) {
		this.style.setAntidiagonalNumber(number);
	}

	@Override
	public String getAntidiagonalStyle() {
		return this.style.getAntidiagonalStyle();
	}

	@Override
	public void setAntidiagonalStyle(String style) {
		this.style.setAntidiagonalStyle(style);
	}

	@Override
	public String getAntidiagonalWidth() {
		return this.style.getAntidiagonalWidth();
	}

	@Override
	public void setAntidiagonalWidth(String width) {
		this.style.setAntidiagonalWidth(width);
	}

	@Override
	public String getAntidiagonalColor() {
		return this.style.getAntidiagonalColor();
	}

	@Override
	public void setAntidiagonalColor(String color) {
		this.style.setAntidiagonalColor(color);
	}
}
