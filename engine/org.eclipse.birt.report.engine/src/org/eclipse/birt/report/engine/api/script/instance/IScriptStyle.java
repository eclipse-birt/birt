/*******************************************************************************
 * Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation and others
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

/**
 * Definition of the script style methods
 *
 * @since 3.3
 *
 */
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
	 * @return Return format to hide in. Should be one of
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
	 *
	 * @return Return the letter spacing
	 */
	String getLetterSpacing();

	/**
	 * Set the letter spacing
	 *
	 * @param spacing
	 */
	void setLetterSpacing(String spacing);

	/**
	 * Get the line height
	 *
	 * @return Return the line height
	 */
	String getLineHeight();

	/**
	 * Set the line height
	 *
	 * @param lineHeight
	 */
	void setLineHeight(String lineHeight);

	/**
	 * Get the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 *
	 * @return Return the text alignment
	 */
	String getTextAlign();

	/**
	 * Set the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 *
	 * @param align
	 */
	void setTextAlign(String align);

	/**
	 * Get the text indent
	 *
	 * @return Return the text indent
	 */
	String getTextIndent();

	/**
	 * Set text indent
	 *
	 * @param indent
	 */
	void setTextIndent(String indent);

	/**
	 * Get the text transform. Valid return values are NONE, CAPITALIZE, UPPERCASE
	 * and LOWERCASE.
	 *
	 * @return Return the text transform
	 */
	String getTextTransform();

	/**
	 * Set the text transform. Valid transform values are NONE, CAPITALIZE,
	 * UPPERCASE and LOWERCASE.
	 *
	 * @param transform
	 */
	void setTextTransform(String transform);

	/**
	 * Get the vertical alignment. Valid return values are BASELINE, SUB, SUPER,
	 * TOP, TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 *
	 * @return Return the vertical alignment
	 */
	String getVerticalAlign();

	/**
	 * Set the vertical alignment. Valid values are BASELINE, SUB, SUPER, TOP,
	 * TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 *
	 * @param valign
	 */
	void setVerticalAlign(String valign);

	/**
	 * Get the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 *
	 * @return Return the whitespace option
	 */
	String getWhiteSpace();

	/**
	 * Set the whitespace. The values retruned can be NORMAL, PRE and NOWRAP
	 *
	 * @param whitespace
	 */
	void setWhiteSpace(String whitespace);

	/**
	 * Get the word spacing
	 *
	 * @return Return the word spacing
	 */
	String getWordSpacing();

	/**
	 * Set the word spacing
	 *
	 * @param wordspacing
	 */
	void setWordSpacing(String wordspacing);

	/**
	 * Get the font color
	 *
	 * @return Return the font color
	 */
	String getColor();

	/**
	 * Set the font color
	 *
	 * @param color
	 */
	void setColor(String color);

	/**
	 * Get the attachement type (either SCROLL or FIXED)
	 *
	 * @return Return the attachment type
	 *
	 * @deprecated replaced by getBackgroundAttachment()
	 */
	@Deprecated
	String getBackgroundAttachement();

	/**
	 * Set the attachement type (either SCROLL or FIXED)
	 *
	 * @param attachement
	 *
	 * @deprecated replaced by setBackgroundAttachment( String attachment )
	 */
	@Deprecated
	void setBackgroundAttachement(String attachement);

	/**
	 * Get the attachment type (either SCROLL or FIXED)
	 *
	 * @return Return the attachement type
	 */
	String getBackgroundAttachment();

	/**
	 * Set the attachment type (either SCROLL or FIXED)
	 *
	 * @param attachment
	 */
	void setBackgroundAttachment(String attachment);

	/**
	 * Get the background color
	 *
	 * @return Return the background color
	 */
	String getBackgroundColor();

	/**
	 * Set the background color
	 *
	 * @param color
	 */
	void setBackgroundColor(String color);

	/**
	 * Get the background image URI
	 *
	 * @return Return the background image URI
	 */
	String getBackgroundImage();

	/**
	 * Set the background image URI
	 *
	 * @param imageURI
	 */
	void setBackgroundImage(String imageURI);

	/**
	 * Get the background image source type
	 *
	 * @return Return the background image source type
	 */
	String getBackgroundImageType();

	/**
	 * Set the background image source type
	 *
	 * @param imageSourceType
	 */
	void setBackgroundImageType(String imageSourceType);

	/**
	 * Get the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 *
	 * @return Return the background repeat
	 */
	String getBackgroundRepeat();

	/**
	 * Set the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 *
	 * @param repeat
	 */
	void setBackgroundRepeat(String repeat);

	/**
	 * Set the background height of the background image.
	 *
	 * @param value the new background image height
	 * @throws ScriptException if the property is locked
	 */
	void setBackgroundHeight(String value) throws ScriptException;

	/**
	 * Returns the height of the background image.
	 *
	 * @return the background image height
	 */
	String getBackgroundHeight();

	/**
	 * Set the background width of the background image.
	 *
	 * @param value the new background image width
	 * @throws ScriptException if the property is locked
	 */
	void setBackgroundWidth(String value) throws ScriptException;

	/**
	 * Returns the width of the background image.
	 *
	 * @return the background image width
	 */
	String getBackgroundWidth();

	/**
	 * Get the bottom border color
	 *
	 * @return Return the bottom border color
	 */
	String getBorderBottomColor();

	/**
	 * Set the bottom border color
	 *
	 * @param color
	 */
	void setBorderBottomColor(String color);

	/**
	 * Get the top border color
	 *
	 * @return Return the top border color
	 */
	String getBorderTopColor();

	/**
	 * Set the top border color
	 *
	 * @param color
	 */
	void setBorderTopColor(String color);

	/**
	 * Get the right border color
	 *
	 * @return Return the right border color
	 */
	String getBorderRightColor();

	/**
	 * Set the right border color
	 *
	 * @param color
	 */
	void setBorderRightColor(String color);

	/**
	 * Get the left border color
	 *
	 * @return Return the left border color
	 */
	String getBorderLeftColor();

	/**
	 * Set the left border color
	 *
	 * @param color
	 */
	void setBorderLeftColor(String color);

	/**
	 * Get the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @return Return the bottom border style.
	 */
	String getBorderBottomStyle();

	/**
	 * Set the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @param borderstyle
	 */
	void setBorderBottomStyle(String borderstyle);

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @return Return the top border style.
	 */
	String getBorderTopStyle();

	/**
	 * Set the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @param borderstyle
	 */
	void setBorderTopStyle(String borderstyle);

	/**
	 * Get the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @return Return the left border style.
	 */
	String getBorderLeftStyle();

	/**
	 * Set the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @param borderstyle
	 */
	void setBorderLeftStyle(String borderstyle);

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @return Return the right border style.
	 */
	String getBorderRightStyle();

	/**
	 * Set the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 *
	 * @param borderstyle
	 */
	void setBorderRightStyle(String borderstyle);

	/**
	 * Get the bottom border width.
	 * @return Return the bottom border width.
	 */
	String getBorderBottomWidth();

	/**
	 * Set the bottom border width.
	 *
	 * @param width
	 */
	void setBorderBottomWidth(String width);

	/**
	 * Get the top border width.
	 *
	 * @return Return the top border width.
	 */
	String getBorderTopWidth();

	/**
	 * Set the top border width.
	 *
	 * @param width
	 */
	void setBorderTopWidth(String width);

	/**
	 * Get the left border width.
	 *
	 * @return Return the left border width.
	 */
	String getBorderLeftWidth();

	/**
	 * Set the left border width.
	 *
	 * @param width
	 */
	void setBorderLeftWidth(String width);

	/**
	 * Get the left border width.
	 *
	 * @return Return the left border width.
	 */
	String getBorderRightWidth();

	/**
	 * Set the right border width.
	 *
	 * @param width
	 */
	void setBorderRightWidth(String width);

	/**
	 * Get the bottom margin
	 *
	 * @return Return the bottom margin
	 */
	String getMarginBottom();

	/**
	 * Set the bottom margin
	 *
	 * @param margin
	 */
	void setMarginBottom(String margin);

	/**
	 * Get the top margin
	 *
	 * @return Return the top margin
	 */
	String getMarginTop();

	/**
	 * Set the top margin
	 *
	 * @param margin
	 */
	void setMarginTop(String margin);

	/**
	 * Get the left margin
	 *
	 * @return Return the left margin
	 */
	String getMarginLeft();

	/**
	 * Set the left margin
	 *
	 * @param margin
	 */
	void setMarginLeft(String margin);

	/**
	 * Get the right margin
	 *
	 * @return Return the right margin
	 */
	String getMarginRight();

	/**
	 * Set the right margin
	 *
	 * @param margin
	 */
	void setMarginRight(String margin);

	/**
	 * Get the bottom padding.
	 *
	 * @return Return the bottom padding.
	 */
	String getPaddingBottom();

	/**
	 * Set the bottom padding.
	 *
	 * @param padding
	 */
	void setPaddingBottom(String padding);

	/**
	 * Get the top padding.
	 *
	 * @return Return the top padding.
	 */
	String getPaddingTop();

	/**
	 * Set the top padding.
	 *
	 * @param padding
	 */
	void setPaddingTop(String padding);

	/**
	 * Get the left padding.
	 *
	 * @return Return the left padding.
	 */
	String getPaddingLeft();

	/**
	 * Set the left padding.
	 *
	 * @param padding
	 */
	void setPaddingLeft(String padding);

	/**
	 * Get the right padding.
	 *
	 * @return Return the right padding.
	 */
	String getPaddingRight();

	/**
	 * Set the right padding.
	 *
	 * @param padding
	 */
	void setPaddingRight(String padding);

	/**
	 * Get the display type (valid types are BLOCK, INLINE and NONE)
	 *
	 * @return Return the display type (valid types are BLOCK, INLINE and NONE)
	 */
	String getDisplay();

	/**
	 * Set the display type (valid types are BLOCK, INLINE and NONE)
	 *
	 * @param display
	 */
	void setDisplay(String display);

	/**
	 * Get the master page
	 *
	 * @return Return the master page
	 */
	String getMasterPage();

	/**
	 * Set the master page
	 *
	 * @param masterPage
	 */
	void setMasterPage(String masterPage);

	/**
	 * Get the page break before.
	 *
	 * @return Return the page break before.
	 */
	String getPageBreakBefore();

	/**
	 * Set the page break before
	 *
	 * @param pageBreak
	 */
	void setPageBreakBefore(String pageBreak);

	/**
	 * Get the page break after.
	 *
	 * @return Return the page break after.
	 */
	String getPageBreakAfter();

	/**
	 * Set the page break after
	 *
	 * @param pageBreak
	 */
	void setPageBreakAfter(String pageBreak);

	/**
	 * Get the page break inside.
	 *
	 * @return Return the page break inside.
	 */
	String getPageBreakInside();

	/**
	 * Set the page break inside
	 *
	 * @param pageBreak
	 */
	void setPageBreakInside(String pageBreak);

	/**
	 * Show if blank?
	 *
	 * @return Show if blank?
	 */
	String getShowIfBlank();

	/**
	 * Set show if blank
	 *
	 * @param showIfBlank
	 */
	void setShowIfBlank(String showIfBlank);

	/**
	 * Can this element shrink?
	 *
	 * @return Can this element shrink?
	 */
	String getCanShrink();

	/**
	 * Can this element shrink?
	 *
	 * @param canShrink
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
	 * @return Return the font family
	 */
	String getFontFamily();

	/**
	 * Set the font family
	 *
	 * @param fontFamily
	 *
	 */
	void setFontFamily(String fontFamily);

	/**
	 * Get the font style
	 *
	 * @return Return the font style
	 */
	String getFontStyle();

	/**
	 * Set the font style
	 *
	 * @param fontStyle
	 *
	 */
	void setFontStyle(String fontStyle);

	/**
	 * Get the font variant
	 *
	 * @return Return the font variant
	 */
	String getFontVariant();

	/**
	 * Set the font variant
	 *
	 * @param fontVariant
	 */
	void setFontVariant(String fontVariant);

	/**
	 * Get the font weight
	 *
	 * @return Return the font weight
	 */
	String getFontWeight();

	/**
	 * Set the font weight
	 *
	 * @param fontWeight
	 */
	void setFontWeight(String fontWeight);

	/**
	 * Get the font size
	 *
	 * @return Return the font size
	 */
	String getFontSize();

	/**
	 * Set the font size
	 *
	 * @param fontSize
	 */
	void setFontSize(String fontSize);

	/**
	 * Get the text underline
	 *
	 * @return Return the text underline
	 */
	String getTextUnderline();

	/**
	 * Set the text underline
	 *
	 * @param underline
	 * @throws ScriptException
	 */
	void setTextUnderline(String underline) throws ScriptException;

	/**
	 * Get the text overline
	 *
	 * @return Return the text overline
	 */
	String getTextOverline();

	/**
	 * Set the text overline
	 *
	 * @param overline
	 * @throws ScriptException
	 */
	void setTextOverline(String overline) throws ScriptException;

	/**
	 * Get the text line through
	 *
	 * @return Return the text line through
	 */
	String getTextLineThrough();

	/**
	 * Set the text line through
	 *
	 * @param through
	 * @throws ScriptException
	 */
	void setTextLineThrough(String through) throws ScriptException;

	/**
	 * Get the text hyperlink style
	 *
	 * @return the text hyperlink style
	 */
	String getTextHyperlinkStyle();

	/**
	 * Set the text hyperlink style
	 *
	 * @param hyperlinkStyle
	 * @throws ScriptException
	 */
	void setTextHyperlinkStyle(String hyperlinkStyle) throws ScriptException;

	/**
	 * Get the X (horizontal) position of the background image
	 *
	 * @return Return the X (horizontal) position of the background image
	 */
	String getBackgroundPositionX();

	/**
	 * Set the X (horizontal) position of the background image
	 *
	 * @param x
	 * @throws ScriptException
	 */
	void setBackgroundPositionX(String x) throws ScriptException;

	/**
	 * Get the Y (vertical) position of the background image
	 *
	 * @return Return the Y (vertical) position of the background image
	 */
	String getBackgroundPositionY();

	/**
	 * Set the Y (vertical) position of the background image
	 *
	 * @param y
	 * @throws ScriptException
	 */
	void setBackgroundPositionY(String y) throws ScriptException;

	/**
	 * Get the diagonal line number
	 *
	 * @return Return the diagonal line number
	 */
	int getDiagonalNumber();

	/**
	 * Set the diagonal line number
	 *
	 * @param number
	 */
	void setDiagonalNumber(int number);

	/**
	 * Get the diagonal color
	 *
	 * @return Return the diagonal color
	 */
	String getDiagonalColor();

	/**
	 * Set the diagonal color
	 *
	 * @param color
	 */
	void setDiagonalColor(String color);

	/**
	 * Get the diagonal style
	 *
	 * @return Return the diagonal style
	 */
	String getDiagonalStyle();

	/**
	 * Set the diagonal style
	 *
	 * @param style
	 */
	void setDiagonalStyle(String style);

	/**
	 * Get the diagonal width
	 *
	 * @return Return the diagonal width
	 */
	String getDiagonalWidth();

	/**
	 * Set the diagonal width
	 *
	 * @param width
	 */
	void setDiagonalWidth(String width);

	/**
	 * Get the antidiagonal line number
	 *
	 * @return Return the antidiagonal line number
	 */
	int getAntidiagonalNumber();

	/**
	 * Set the antidiagonal line number
	 *
	 * @param number
	 */
	void setAntidiagonalNumber(int number);

	/**
	 * Get the antidiagonal color
	 *
	 * @return Return the antidiagonal color
	 */
	String getAntidiagonalColor();

	/**
	 * Set the antidiagonal color
	 *
	 * @param color
	 */
	void setAntidiagonalColor(String color);

	/**
	 * Get the antidiagonal style
	 *
	 * @return Return the antidiagonal style
	 */
	String getAntidiagonalStyle();

	/**
	 * Set the antidiagonal style
	 *
	 * @param style
	 */
	void setAntidiagonalStyle(String style);

	/**
	 * Get the antidiagonal width
	 *
	 * @return Return the antidiagonal width
	 */
	String getAntidiagonalWidth();

	/**
	 * Set the antidiagonal width
	 *
	 * @param width
	 */
	void setAntidiagonalWidth(String width);
}