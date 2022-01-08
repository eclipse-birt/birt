/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.instance;

import org.eclipse.birt.report.engine.api.script.ScriptException;

public interface IScriptStyle {

	/**
	 * Get format to hide in. Should be one of
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_ALL
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_VIEWER
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_EMAIL
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_PRINT
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_PDF
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_RTF
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_REPORTLET
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_EXCEL
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_WORD
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_POWERPOINT
	 * 
	 */
	String getVisibleFormat();

	/**
	 * Set format to hide in. Should be one of
	 * 
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_ALL
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_VIEWER
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_EMAIL
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_PRINT
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_PDF
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_RTF
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_REPORTLET
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_EXCEL
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_WORD
	 * org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.FORMAT_TYPE_POWERPOINT
	 * 
	 * @param format
	 */
	void setVisibleFormat(String format);

	/**
	 * Get the letter spacing
	 */
	String getLetterSpacing();

	/**
	 * Set the letter spacing
	 */
	void setLetterSpacing(String spacing);

	/**
	 * Get the line height
	 */
	String getLineHeight();

	/**
	 * Set the line height
	 */
	void setLineHeight(String lineHeight);

	/**
	 * Get the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	String getTextAlign();

	/**
	 * Set the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	void setTextAlign(String align);

	/**
	 * Get the text indent
	 */
	String getTextIndent();

	/**
	 * Set text indent
	 */
	void setTextIndent(String indent);

	/**
	 * Get the text transform. Valid return values are NONE, CAPITALIZE, UPPERCASE
	 * and LOWERCASE.
	 */
	String getTextTransform();

	/**
	 * Set the text transform. Valid transform values are NONE, CAPITALIZE,
	 * UPPERCASE and LOWERCASE.
	 */
	void setTextTransform(String transform);

	/**
	 * Get the vertical alignment. Valid return values are BASELINE, SUB, SUPER,
	 * TOP, TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
	String getVerticalAlign();

	/**
	 * Set the vertical alignment. Valid values are BASELINE, SUB, SUPER, TOP,
	 * TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
	void setVerticalAlign(String valign);

	/**
	 * Get the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	String getWhiteSpace();

	/**
	 * Set the whitespace. The values retruned can be NORMAL, PRE and NOWRAP
	 */
	void setWhiteSpace(String whitespace);

	/**
	 * Get the word spacing
	 */
	String getWordSpacing();

	/**
	 * Set the word spacing
	 */
	void setWordSpacing(String wordspacing);

	/**
	 * Get the font color
	 */
	String getColor();

	/**
	 * Set the font color
	 */
	void setColor(String color);

	/**
	 * Get the attachement type (either SCROLL or FIXED)
	 * 
	 * @deprecated replaced by getBackgroundAttachment()
	 */
	String getBackgroundAttachement();

	/**
	 * Set the attachement type (either SCROLL or FIXED)
	 * 
	 * @deprecated replaced by setBackgroundAttachment( String attachment )
	 */
	void setBackgroundAttachement(String attachement);

	/**
	 * Get the attachment type (either SCROLL or FIXED)
	 */
	String getBackgroundAttachment();

	/**
	 * Set the attachment type (either SCROLL or FIXED)
	 */
	void setBackgroundAttachment(String attachment);

	/**
	 * Get the background color
	 */
	String getBackgroundColor();

	/**
	 * Set the background color
	 */
	void setBackgroundColor(String color);

	/**
	 * Get the background image URI
	 */
	String getBackgroundImage();

	/**
	 * Set the background image URI
	 */
	void setBackgroundImage(String imageURI);

	/**
	 * Get the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	String getBackgroundRepeat();

	/**
	 * Set the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	void setBackgroundRepeat(String repeat);

	/**
	 * Get the bottom border color
	 */
	String getBorderBottomColor();

	/**
	 * Set the bottom border color
	 */
	void setBorderBottomColor(String color);

	/**
	 * Get the top border color
	 */
	String getBorderTopColor();

	/**
	 * Set the top border color
	 */
	void setBorderTopColor(String color);

	/**
	 * Get the right border color
	 */
	String getBorderRightColor();

	/**
	 * Set the right border color
	 */
	void setBorderRightColor(String color);

	/**
	 * Get the left border color
	 */
	String getBorderLeftColor();

	/**
	 * Set the left border color
	 */
	void setBorderLeftColor(String color);

	/**
	 * Get the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	String getBorderBottomStyle();

	/**
	 * Set the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	void setBorderBottomStyle(String borderstyle);

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	String getBorderTopStyle();

	/**
	 * Set the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	void setBorderTopStyle(String borderstyle);

	/**
	 * Get the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	String getBorderLeftStyle();

	/**
	 * Set the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	void setBorderLeftStyle(String borderstyle);

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	String getBorderRightStyle();

	/**
	 * Set the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	void setBorderRightStyle(String borderstyle);

	/**
	 * Get the bottom border width.
	 */
	String getBorderBottomWidth();

	/**
	 * Set the bottom border width.
	 */
	void setBorderBottomWidth(String width);

	/**
	 * Get the top border width.
	 */
	String getBorderTopWidth();

	/**
	 * Set the top border width.
	 */
	void setBorderTopWidth(String width);

	/**
	 * Get the left border width.
	 */
	String getBorderLeftWidth();

	/**
	 * Set the left border width.
	 */
	void setBorderLeftWidth(String width);

	/**
	 * Get the right border width.
	 */
	String getBorderRightWidth();

	/**
	 * Set the right border width.
	 */
	void setBorderRightWidth(String width);

	/**
	 * Get the bottom margin
	 */
	String getMarginBottom();

	/**
	 * Set the bottom margin
	 */
	void setMarginBottom(String margin);

	/**
	 * Get the top margin
	 */
	String getMarginTop();

	/**
	 * Set the top margin
	 */
	void setMarginTop(String margin);

	/**
	 * Get the left margin
	 */
	String getMarginLeft();

	/**
	 * Set the left margin
	 */
	void setMarginLeft(String margin);

	/**
	 * Get the right margin
	 */
	String getMarginRight();

	/**
	 * Set the right margin
	 */
	void setMarginRight(String margin);

	/**
	 * Get the bottom padding.
	 */
	String getPaddingBottom();

	/**
	 * Set the bottom padding.
	 */
	void setPaddingBottom(String padding);

	/**
	 * Get the top padding.
	 */
	String getPaddingTop();

	/**
	 * Set the top padding.
	 */
	void setPaddingTop(String padding);

	/**
	 * Get the left padding.
	 */
	String getPaddingLeft();

	/**
	 * Set the left padding.
	 */
	void setPaddingLeft(String padding);

	/**
	 * Get the right padding.
	 */
	String getPaddingRight();

	/**
	 * Set the right padding.
	 */
	void setPaddingRight(String padding);

	/**
	 * Get the display type (valid types are BLOCK, INLINE and NONE)
	 */
	String getDisplay();

	/**
	 * Set the display type (valid types are BLOCK, INLINE and NONE)
	 */
	void setDisplay(String display);

	/**
	 * Get the master page
	 */
	String getMasterPage();

	/**
	 * Set the master page
	 */
	void setMasterPage(String masterPage);

	/**
	 * Get the page break before.
	 */
	String getPageBreakBefore();

	/**
	 * Set the page break before
	 */
	void setPageBreakBefore(String pageBreak);

	/**
	 * Get the page break after.
	 */
	String getPageBreakAfter();

	/**
	 * Set the page break after
	 */
	void setPageBreakAfter(String pageBreak);

	/**
	 * Get the page break inside.
	 */
	String getPageBreakInside();

	/**
	 * Set the page break inside
	 */
	void setPageBreakInside(String pageBreak);

	/**
	 * Show if blank?
	 */
	String getShowIfBlank();

	/**
	 * Set show if blank
	 */
	void setShowIfBlank(String showIfBlank);

	/**
	 * Can this element shrink?
	 */
	String getCanShrink();

	/**
	 * Can this element shrink?
	 */
	void setCanShrink(String canShrink);

	/**
	 * Get number format
	 * 
	 * @return the number format
	 */
	String getNumberFormat();

	/**
	 * Set the number format
	 * 
	 * @param numberFormat
	 */
	void setNumberFormat(String numberFormat);

	/**
	 * Get the number locale
	 * 
	 * @return number locale
	 */
	String getNumberLocale();

	/**
	 * Set the number locale
	 * 
	 * @param locale number locale
	 */
	void setNumberLocale(String locale);

	/**
	 * Get the date format
	 * 
	 * @return date format
	 */
	String getDateFormat();

	/**
	 * Set the date format
	 * 
	 * @param dateTimeFormat
	 */
	void setDateFormat(String dateTimeFormat);

	/**
	 * Get the date locale
	 * 
	 * @return teh date locale
	 */
	String getDateLocale();

	/**
	 * Set the date locale
	 * 
	 * @param locale the date locale
	 */
	void setDateLocale(String locale);

	/**
	 * Get the string format
	 * 
	 * @return the string format
	 */
	String getStringFormat();

	/**
	 * Set the string format
	 * 
	 * @param stringFormat
	 */
	void setStringFormat(String stringFormat);

	/**
	 * Get the string locale
	 * 
	 * @return the string locale
	 */
	String getStringLocale();

	/**
	 * Set the string locale
	 * 
	 * @param locale string locale
	 */
	void setStringLocale(String locale);

	/**
	 * Get the font family
	 * 
	 */
	String getFontFamily();

	/**
	 * Set the font family
	 * 
	 */
	void setFontFamily(String fontFamily);

	/**
	 * Get the font style
	 * 
	 */
	String getFontStyle();

	/**
	 * Set the font style
	 * 
	 */
	void setFontStyle(String fontStyle);

	/**
	 * Get the font variant
	 * 
	 */
	String getFontVariant();

	/**
	 * Set the font variant
	 * 
	 */
	void setFontVariant(String fontVariant);

	/**
	 * Get the font weight
	 * 
	 */
	String getFontWeight();

	/**
	 * Set the font weight
	 * 
	 */
	void setFontWeight(String fontWeight);

	/**
	 * Get the font size
	 * 
	 */
	String getFontSize();

	/**
	 * Set the font size
	 * 
	 */
	void setFontSize(String fontSize);

	/**
	 * Get the text underline
	 */
	public String getTextUnderline();

	/**
	 * Set the text underline
	 */
	public void setTextUnderline(String underline) throws ScriptException;

	/**
	 * Get the text overline
	 */
	public String getTextOverline();

	/**
	 * Set the text overline
	 */
	public void setTextOverline(String overline) throws ScriptException;

	/**
	 * Get the text line through
	 */
	public String getTextLineThrough();

	/**
	 * Set the text line through
	 */
	public void setTextLineThrough(String through) throws ScriptException;

	/**
	 * Get the X (horizontal) position of the background image
	 * 
	 */
	public String getBackgroundPositionX();

	/**
	 * Set the X (horizontal) position of the background image
	 * 
	 */
	public void setBackgroundPositionX(String x) throws ScriptException;

	/**
	 * Get the Y (vertical) position of the background image
	 * 
	 */
	public String getBackgroundPositionY();

	/**
	 * Set the Y (vertical) position of the background image
	 * 
	 */
	public void setBackgroundPositionY(String y) throws ScriptException;

}
