/*******************************************************************************
 * Copyright (c) 2007,2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

public class TOCStyle implements IScriptStyle, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String BACKGROUND_ATTACHMENT = "background-attachment";
	public static final String BACKGROUND_COLOR = "background-color";
	public static final String BACKGROUND_IMAGE = "background-image";
	public static final String BACKGROUND_POSITION_X = "background-position-x";
	public static final String BACKGROUND_POSITION_Y = "background-position-y";
	public static final String BACKGROUND_REPEAT = "background-repeat";
	public static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
	public static final String BORDER_TOP_COLOR = "border-top-color";
	public static final String BORDER_LEFT_COLOR = "border-left-color";
	public static final String BORDER_RIGHT_COLOR = "border-right-color";
	public static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";
	public static final String BORDER_TOP_WIDTH = "border-top-width";
	public static final String BORDER_LEFT_WIDTH = "border-left-width";
	public static final String BORDER_RIGHT_WIDTH = "border-right-width";
	public static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
	public static final String BORDER_TOP_STYLE = "border-top-style";
	public static final String BORDER_LEFT_STYLE = "border-left-style";
	public static final String BORDER_RIGHT_STYLE = "border-right-style";
	public static final String CAN_SHRINK = "can-shrink";
	public static final String COLOR = "color";
	public static final String DATE_FORMAT = "date-format";
	public static final String DIRECTION = "direction"; // bidi_hcg
	public static final String DISPLAY = "display";
	public static final String FONT_FAMILY = "font-family";
	public static final String FONT_SIZE = "font-size";
	public static final String FONT_STYLE = "font-style";
	public static final String FONT_VARIANT = "font-variant";
	public static final String FONT_WEIGHT = "font-weight";
	public static final String LETTER_SPACING = "letter-spacing";
	public static final String LINE_HEIGHT = "line-height";
	public static final String MARGIN_BOTTOM = "margin-bottom";
	public static final String MARGIN_LEFT = "margin-left";
	public static final String MARGIN_RIGHT = "margin-right";
	public static final String MARGIN_TOP = "margin-top";
	public static final String MASTER_PAGE = "master-page";
	public static final String NUMBER_FORMAT = "number-format";
	public static final String PADDING_BOTTOM = "padding-bottom";
	public static final String PADDING_LEFT = "padding-left";
	public static final String PADDING_RIGHT = "padding-right";
	public static final String PADDING_TOP = "padding-top";
	public static final String PAGE_BREAK_AFTER = "page-break-after";
	public static final String PAGE_BREAK_BEFORE = "page-break-before";
	public static final String PAGE_BREAK_INSIDE = "page-break-inside";
	public static final String SHOW_IF_BLANK = "show-if-blank";
	public static final String STRING_FORMAT = "string-format";
	public static final String TEXT_ALIGN = "text-align";
	public static final String TEXT_INDENT = "text-indent";
	public static final String TEXT_LINE_THROUGH = "text-line-through";
	public static final String TEXT_OVERLINE = "text-overline";
	public static final String TEXT_TRANSFORM = "text-transform";
	public static final String TEXT_UNDERLINE = "text-underline";
	public static final String VERTICAL_ALIGN = "vertical-align";
	public static final String VISIBLE_FORMAT = "visible-format";
	public static final String WHITE_SPACE = "white-space";
	public static final String WORD_SPACING = "word-spacing";
	public static final String DATE_LOCALE = "date-locale";
	public static final String NUMBER_LOCALE = "number-locale";
	public static final String STRING_LOCALE = "string-locale";

	private HashMap<String, String> properties = new HashMap<>();

	@Override
	public String getBackgroundAttachement() {
		return (String) properties.get(BACKGROUND_ATTACHMENT);
	}

	/**
	 * Get the attachment type (either SCROLL or FIXED)
	 */
	@Override
	public String getBackgroundAttachment() {
		return (String) properties.get(BACKGROUND_ATTACHMENT);
	}

	/**
	 * Get the background color
	 */
	@Override
	public String getBackgroundColor() {
		return (String) properties.get(BACKGROUND_COLOR);
	}

	/**
	 * Get the background image URI
	 */
	@Override
	public String getBackgroundImage() {
		return (String) properties.get(BACKGROUND_IMAGE);
	}

	/**
	 * Get the X (horizontal) position of the background image
	 *
	 */
	@Override
	public String getBackgroundPositionX() {
		return (String) properties.get(BACKGROUND_POSITION_X);
	}

	/**
	 * Get the Y (vertical) position of the background image
	 *
	 */
	@Override
	public String getBackgroundPositionY() {
		return (String) properties.get(BACKGROUND_POSITION_Y);
	}

	/**
	 * Get the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	@Override
	public String getBackgroundRepeat() {
		return (String) properties.get(BACKGROUND_REPEAT);
	}

	/**
	 * Get the bottom border color
	 */
	@Override
	public String getBorderBottomColor() {
		return (String) properties.get(BORDER_BOTTOM_COLOR);
	}

	/**
	 * Get the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderBottomStyle() {
		return (String) properties.get(BORDER_BOTTOM_STYLE);
	}

	/**
	 * Get the bottom border width.
	 */
	@Override
	public String getBorderBottomWidth() {
		return (String) properties.get(BORDER_BOTTOM_WIDTH);
	}

	/**
	 * Get the left border color
	 */
	@Override
	public String getBorderLeftColor() {
		return (String) properties.get(BORDER_LEFT_COLOR);
	}

	/**
	 * Get the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderLeftStyle() {
		return (String) properties.get(BORDER_LEFT_STYLE);
	}

	/**
	 * Get the left border width.
	 */
	@Override
	public String getBorderLeftWidth() {
		return (String) properties.get(BORDER_LEFT_WIDTH);
	}

	/**
	 * Get the right border color
	 */
	@Override
	public String getBorderRightColor() {
		return (String) properties.get(BORDER_RIGHT_COLOR);
	}

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderRightStyle() {
		return (String) properties.get(BORDER_RIGHT_STYLE);
	}

	/**
	 * Get the right border width.
	 */
	@Override
	public String getBorderRightWidth() {
		return (String) properties.get(BORDER_RIGHT_WIDTH);
	}

	/**
	 * Get the top border color
	 */
	@Override
	public String getBorderTopColor() {
		return (String) properties.get(BORDER_TOP_COLOR);
	}

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderTopStyle() {
		return (String) properties.get(BORDER_TOP_STYLE);
	}

	/**
	 * Get the top border width.
	 */
	@Override
	public String getBorderTopWidth() {
		return (String) properties.get(BORDER_TOP_WIDTH);
	}

	/**
	 * Can this element shrink?
	 */
	@Override
	public String getCanShrink() {
		return (String) properties.get(CAN_SHRINK);
	}

	/**
	 * Get the font color
	 */
	@Override
	public String getColor() {
		return (String) properties.get(COLOR);
	}

	/**
	 * Get the date format
	 *
	 * @return date format
	 */
	@Override
	public String getDateFormat() {
		return (String) properties.get(DATE_FORMAT);
	}

	public String getDirection() {
		return (String) properties.get(DIRECTION);
	}

	/**
	 * Get the display type (valid types are BLOCK, INLINE and NONE)
	 */
	@Override
	public String getDisplay() {
		return (String) properties.get(DISPLAY);
	}

	/**
	 * Get the font family
	 *
	 */
	@Override
	public String getFontFamily() {
		return (String) properties.get(FONT_FAMILY);
	}

	/**
	 * Get the font size
	 *
	 */
	@Override
	public String getFontSize() {
		return (String) properties.get(FONT_SIZE);
	}

	/**
	 * Get the font style
	 *
	 */
	@Override
	public String getFontStyle() {
		return (String) properties.get(FONT_STYLE);
	}

	/**
	 * Get the font variant
	 *
	 */
	@Override
	public String getFontVariant() {
		return (String) properties.get(FONT_VARIANT);
	}

	/**
	 * Get the font weight
	 *
	 */
	@Override
	public String getFontWeight() {
		return (String) properties.get(FONT_WEIGHT);
	}

	/**
	 * Get the letter spacing
	 */
	@Override
	public String getLetterSpacing() {
		return (String) properties.get(LETTER_SPACING);
	}

	/**
	 * Get the line height
	 */
	@Override
	public String getLineHeight() {
		return (String) properties.get(LINE_HEIGHT);
	}

	/**
	 * Get the bottom margin
	 */
	@Override
	public String getMarginBottom() {
		return (String) properties.get(MARGIN_BOTTOM);
	}

	/**
	 * Get the left margin
	 */
	@Override
	public String getMarginLeft() {
		return (String) properties.get(MARGIN_LEFT);
	}

	/**
	 * Get the right margin
	 */
	@Override
	public String getMarginRight() {
		return (String) properties.get(MARGIN_RIGHT);
	}

	/**
	 * Get the top margin
	 */
	@Override
	public String getMarginTop() {
		return (String) properties.get(MARGIN_TOP);
	}

	/**
	 * Get the master page
	 */
	@Override
	public String getMasterPage() {
		return (String) properties.get(MASTER_PAGE);
	}

	/**
	 * Get number format
	 *
	 * @return the number format
	 */
	@Override
	public String getNumberFormat() {
		return (String) properties.get(NUMBER_FORMAT);
	}

	/**
	 * Get the bottom padding.
	 */
	@Override
	public String getPaddingBottom() {
		return (String) properties.get(PADDING_BOTTOM);
	}

	/**
	 * Get the left padding.
	 */
	@Override
	public String getPaddingLeft() {
		return (String) properties.get(PADDING_LEFT);
	}

	/**
	 * Get the right padding.
	 */
	@Override
	public String getPaddingRight() {
		return (String) properties.get(PADDING_RIGHT);
	}

	/**
	 * Get the top padding.
	 */
	@Override
	public String getPaddingTop() {
		return (String) properties.get(PADDING_TOP);
	}

	/**
	 * Get the page break after.
	 */
	@Override
	public String getPageBreakAfter() {
		return (String) properties.get(PAGE_BREAK_AFTER);
	}

	/**
	 * Get the page break before.
	 */
	@Override
	public String getPageBreakBefore() {
		return (String) properties.get(PAGE_BREAK_BEFORE);
	}

	/**
	 * Get the page break inside.
	 */
	@Override
	public String getPageBreakInside() {
		return (String) properties.get(PAGE_BREAK_INSIDE);
	}

	/**
	 * Show if blank?
	 */
	@Override
	public String getShowIfBlank() {
		return (String) properties.get(SHOW_IF_BLANK);
	}

	/**
	 * Get the string format
	 *
	 * @return the string format
	 */
	@Override
	public String getStringFormat() {
		return (String) properties.get(STRING_FORMAT);
	}

	/**
	 * Get the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	@Override
	public String getTextAlign() {
		return (String) properties.get(TEXT_ALIGN);
	}

	/**
	 * Get the text indent
	 */
	@Override
	public String getTextIndent() {
		return (String) properties.get(TEXT_INDENT);
	}

	/**
	 * Get the text line through
	 */
	@Override
	public String getTextLineThrough() {
		return (String) properties.get(TEXT_LINE_THROUGH);
	}

	/**
	 * Get the text overline
	 */
	@Override
	public String getTextOverline() {
		return (String) properties.get(TEXT_OVERLINE);
	}

	/**
	 * Get the text transform. Valid return values are NONE, CAPITALIZE, UPPERCASE
	 * and LOWERCASE.
	 */
	@Override
	public String getTextTransform() {
		return (String) properties.get(TEXT_TRANSFORM);
	}

	/**
	 * Get the text underline
	 */
	@Override
	public String getTextUnderline() {
		return (String) properties.get(TEXT_UNDERLINE);
	}

	/**
	 * Get the vertical alignment. Valid return values are BASELINE, SUB, SUPER,
	 * TOP, TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
	@Override
	public String getVerticalAlign() {
		return (String) properties.get(VERTICAL_ALIGN);
	}

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
	@Override
	public String getVisibleFormat() {
		return (String) properties.get(VISIBLE_FORMAT);
	}

	/**
	 * Get the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	@Override
	public String getWhiteSpace() {
		return (String) properties.get(WHITE_SPACE);
	}

	/**
	 * Get the word spacing
	 */
	@Override
	public String getWordSpacing() {
		return (String) properties.get(WORD_SPACING);
	}

	@Override
	public void setBackgroundAttachement(String attachement) {
		setProperty(BACKGROUND_ATTACHMENT, attachement);
	}

	/**
	 * Set the attachment type (either SCROLL or FIXED)
	 */
	@Override
	public void setBackgroundAttachment(String attachment) {
		setProperty(BACKGROUND_ATTACHMENT, attachment);
	}

	/**
	 * Set the background color
	 */
	@Override
	public void setBackgroundColor(String color) {
		setProperty(BACKGROUND_COLOR, color);
	}

	/**
	 * Set the background image URI
	 */
	@Override
	public void setBackgroundImage(String imageURI) {
		setProperty(BACKGROUND_IMAGE, imageURI);
	}

	/**
	 * Set the X (horizontal) position of the background image
	 *
	 */
	@Override
	public void setBackgroundPositionX(String x) throws ScriptException {
		setProperty(BACKGROUND_POSITION_X, x);
	}

	/**
	 * Set the Y (vertical) position of the background image
	 *
	 */
	@Override
	public void setBackgroundPositionY(String y) throws ScriptException {
		setProperty(BACKGROUND_POSITION_Y, y);
	}

	/**
	 * Set the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	@Override
	public void setBackgroundRepeat(String repeat) {
		setProperty(BACKGROUND_REPEAT, repeat);
	}

	/**
	 * Set the bottom border color
	 */
	@Override
	public void setBorderBottomColor(String color) {
		setProperty(BORDER_BOTTOM_COLOR, color);
	}

	/**
	 * Set the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderBottomStyle(String borderstyle) {
		setProperty(BORDER_BOTTOM_STYLE, borderstyle);
	}

	/**
	 * Set the bottom border width.
	 */
	@Override
	public void setBorderBottomWidth(String width) {
		setProperty(BORDER_BOTTOM_WIDTH, width);
	}

	/**
	 * Set the left border color
	 */
	@Override
	public void setBorderLeftColor(String color) {
		setProperty(BORDER_LEFT_COLOR, color);
	}

	/**
	 * Set the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderLeftStyle(String borderstyle) {
		setProperty(BORDER_LEFT_STYLE, borderstyle);
	}

	/**
	 * Set the left border width.
	 */
	@Override
	public void setBorderLeftWidth(String width) {
		setProperty(BORDER_LEFT_WIDTH, width);
	}

	/**
	 * Set the right border color
	 */
	@Override
	public void setBorderRightColor(String color) {
		setProperty(BORDER_RIGHT_COLOR, color);
	}

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderRightStyle(String borderstyle) {
		setProperty(BORDER_RIGHT_STYLE, borderstyle);
	}

	/**
	 * Set the right border width.
	 */
	@Override
	public void setBorderRightWidth(String width) {
		setProperty(BORDER_RIGHT_WIDTH, width);
	}

	/**
	 * Set the top border color
	 */
	@Override
	public void setBorderTopColor(String color) {
		setProperty(BORDER_TOP_COLOR, color);
	}

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public void setBorderTopStyle(String borderstyle) {
		setProperty(BORDER_TOP_STYLE, borderstyle);
	}

	/**
	 * Set the top border width.
	 */
	@Override
	public void setBorderTopWidth(String width) {
		setProperty(BORDER_TOP_WIDTH, width);
	}

	/**
	 * Can this element shrink?
	 */
	@Override
	public void setCanShrink(String canShrink) {
		setProperty(CAN_SHRINK, canShrink);
	}

	/**
	 * Set the font color
	 */
	@Override
	public void setColor(String color) {
		setProperty(COLOR, color);
	}

	/**
	 * Set the date format
	 *
	 * @param dateTimeFormat
	 */
	@Override
	public void setDateFormat(String dateTimeFormat) {
		setProperty(DATE_FORMAT, dateTimeFormat);
	}

	public void setDirection(String direction) {
		setProperty(DIRECTION, direction);
	}

	/**
	 * Set the display type (valid types are BLOCK, INLINE and NONE)
	 */
	@Override
	public void setDisplay(String display) {
		setProperty(DISPLAY, display);
	}

	/**
	 * Set the font family
	 *
	 */
	@Override
	public void setFontFamily(String fontFamily) {
		setProperty(FONT_FAMILY, fontFamily);
	}

	/**
	 * Set the font size
	 *
	 */
	@Override
	public void setFontSize(String fontSize) {
		setProperty(FONT_SIZE, fontSize);
	}

	/**
	 * Set the font style
	 *
	 */
	@Override
	public void setFontStyle(String fontStyle) {
		setProperty(FONT_STYLE, fontStyle);
	}

	/**
	 * Set the font variant
	 *
	 */
	@Override
	public void setFontVariant(String fontVariant) {
		setProperty(FONT_VARIANT, fontVariant);
	}

	/**
	 * Set the font weight
	 *
	 */
	@Override
	public void setFontWeight(String fontWeight) {
		setProperty(FONT_WEIGHT, fontWeight);
	}

	/**
	 * Set the letter spacing
	 */
	@Override
	public void setLetterSpacing(String spacing) {
		setProperty(LETTER_SPACING, spacing);
	}

	/**
	 * Set the line height
	 */
	@Override
	public void setLineHeight(String lineHeight) {
		setProperty(LINE_HEIGHT, lineHeight);
	}

	/**
	 * Set the bottom margin
	 */
	@Override
	public void setMarginBottom(String margin) {
		setProperty(MARGIN_BOTTOM, margin);
	}

	/**
	 * Set the left margin
	 */
	@Override
	public void setMarginLeft(String margin) {
		setProperty(MARGIN_LEFT, margin);
	}

	/**
	 * Set the right margin
	 */
	@Override
	public void setMarginRight(String margin) {
		setProperty(MARGIN_RIGHT, margin);
	}

	/**
	 * Set the top margin
	 */
	@Override
	public void setMarginTop(String margin) {
		setProperty(MARGIN_TOP, margin);
	}

	/**
	 * Set the master page
	 */
	@Override
	public void setMasterPage(String masterPage) {
		setProperty(MASTER_PAGE, masterPage);
	}

	/**
	 * Set the number format
	 *
	 * @param numberFormat
	 */
	@Override
	public void setNumberFormat(String numberFormat) {
		setProperty(NUMBER_FORMAT, numberFormat);
	}

	/**
	 * Set the bottom padding.
	 */
	@Override
	public void setPaddingBottom(String padding) {
		setProperty(PADDING_BOTTOM, padding);
	}

	/**
	 * Set the left padding.
	 */
	@Override
	public void setPaddingLeft(String padding) {
		setProperty(PADDING_LEFT, padding);
	}

	/**
	 * Set the right padding.
	 */
	@Override
	public void setPaddingRight(String padding) {
		setProperty(PADDING_RIGHT, padding);
	}

	/**
	 * Set the top padding.
	 */
	@Override
	public void setPaddingTop(String padding) {
		setProperty(PADDING_TOP, padding);
	}

	/**
	 * Set the page break after
	 */
	@Override
	public void setPageBreakAfter(String pageBreak) {
		setProperty(PAGE_BREAK_AFTER, pageBreak);
	}

	/**
	 * Set the page break before
	 */
	@Override
	public void setPageBreakBefore(String pageBreak) {
		setProperty(PAGE_BREAK_BEFORE, pageBreak);
	}

	/**
	 * Set the page break inside
	 */
	@Override
	public void setPageBreakInside(String pageBreak) {
		setProperty(PAGE_BREAK_INSIDE, pageBreak);
	}

	/**
	 * Set show if blank
	 */
	@Override
	public void setShowIfBlank(String showIfBlank) {
		setProperty(SHOW_IF_BLANK, showIfBlank);
	}

	/**
	 * Set the string format
	 *
	 * @param stringFormat
	 */
	@Override
	public void setStringFormat(String stringFormat) {
		setProperty(STRING_FORMAT, stringFormat);
	}

	/**
	 * Set the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	@Override
	public void setTextAlign(String align) {
		setProperty(TEXT_ALIGN, align);
	}

	/**
	 * Set the text indent
	 */
	@Override
	public void setTextIndent(String indent) {
		setProperty(TEXT_INDENT, indent);
	}

	/**
	 * Set the text line through
	 */
	@Override
	public void setTextLineThrough(String through) throws ScriptException {
		setProperty(TEXT_LINE_THROUGH, through);
	}

	/**
	 * Set the text overline
	 */
	@Override
	public void setTextOverline(String overline) throws ScriptException {
		setProperty(TEXT_OVERLINE, overline);
	}

	/**
	 * Set the text transform. Valid transform values are NONE, CAPITALIZE,
	 * UPPERCASE and LOWERCASE.
	 */
	@Override
	public void setTextTransform(String transform) {
		setProperty(TEXT_TRANSFORM, transform);
	}

	/**
	 * Set the text underline
	 */
	@Override
	public void setTextUnderline(String underline) throws ScriptException {
		setProperty(TEXT_UNDERLINE, underline);
	}

	/**
	 * Set the vertical alignment. Valid values are BASELINE, SUB, SUPER, TOP,
	 * TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
	@Override
	public void setVerticalAlign(String valign) {
		setProperty(VERTICAL_ALIGN, valign);
	}

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
	@Override
	public void setVisibleFormat(String format) {
		setProperty(VISIBLE_FORMAT, format);
	}

	/**
	 * Set the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	@Override
	public void setWhiteSpace(String whitespace) {
		setProperty(WHITE_SPACE, whitespace);
	}

	/**
	 * Set the word spacing
	 */
	@Override
	public void setWordSpacing(String wordspacing) {
		setProperty(WORD_SPACING, wordspacing);
	}

	@Override
	public String getDateLocale() {
		return (String) properties.get(DATE_LOCALE);
	}

	@Override
	public void setDateLocale(String locale) {
		setProperty(DATE_LOCALE, locale);
	}

	@Override
	public String getNumberLocale() {
		return (String) properties.get(NUMBER_LOCALE);
	}

	@Override
	public void setNumberLocale(String locale) {
		setProperty(NUMBER_LOCALE, locale);
	}

	@Override
	public String getStringLocale() {
		return (String) properties.get(STRING_LOCALE);
	}

	@Override
	public void setStringLocale(String locale) {
		setProperty(STRING_LOCALE, locale);
	}

	/**
	 * Set property.
	 */
	public void setProperty(String name, String value) {
		if (value != null) {
			properties.put(name, value);
		} else {
			properties.remove(name);
		}
	}

	/**
	 * Get property.
	 */
	public Map getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, String>> iter = properties.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			sb.append(entry.getKey());
			sb.append(":");
			sb.append(entry.getValue());
			sb.append(";");
		}
		return sb.toString();
	}
}
