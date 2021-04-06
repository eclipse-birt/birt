/*******************************************************************************
 * Copyright (c) 2007,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	private HashMap<String, String> properties = new HashMap<String, String>();

	public String getBackgroundAttachement() {
		return (String) properties.get(BACKGROUND_ATTACHMENT);
	}

	/**
	 * Get the attachment type (either SCROLL or FIXED)
	 */
	public String getBackgroundAttachment() {
		return (String) properties.get(BACKGROUND_ATTACHMENT);
	}

	/**
	 * Get the background color
	 */
	public String getBackgroundColor() {
		return (String) properties.get(BACKGROUND_COLOR);
	}

	/**
	 * Get the background image URI
	 */
	public String getBackgroundImage() {
		return (String) properties.get(BACKGROUND_IMAGE);
	}

	/**
	 * Get the X (horizontal) position of the background image
	 * 
	 */
	public String getBackgroundPositionX() {
		return (String) properties.get(BACKGROUND_POSITION_X);
	}

	/**
	 * Get the Y (vertical) position of the background image
	 * 
	 */
	public String getBackgroundPositionY() {
		return (String) properties.get(BACKGROUND_POSITION_Y);
	}

	/**
	 * Get the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	public String getBackgroundRepeat() {
		return (String) properties.get(BACKGROUND_REPEAT);
	}

	/**
	 * Get the bottom border color
	 */
	public String getBorderBottomColor() {
		return (String) properties.get(BORDER_BOTTOM_COLOR);
	}

	/**
	 * Get the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public String getBorderBottomStyle() {
		return (String) properties.get(BORDER_BOTTOM_STYLE);
	}

	/**
	 * Get the bottom border width.
	 */
	public String getBorderBottomWidth() {
		return (String) properties.get(BORDER_BOTTOM_WIDTH);
	}

	/**
	 * Get the left border color
	 */
	public String getBorderLeftColor() {
		return (String) properties.get(BORDER_LEFT_COLOR);
	}

	/**
	 * Get the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public String getBorderLeftStyle() {
		return (String) properties.get(BORDER_LEFT_STYLE);
	}

	/**
	 * Get the left border width.
	 */
	public String getBorderLeftWidth() {
		return (String) properties.get(BORDER_LEFT_WIDTH);
	}

	/**
	 * Get the right border color
	 */
	public String getBorderRightColor() {
		return (String) properties.get(BORDER_RIGHT_COLOR);
	}

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public String getBorderRightStyle() {
		return (String) properties.get(BORDER_RIGHT_STYLE);
	}

	/**
	 * Get the right border width.
	 */
	public String getBorderRightWidth() {
		return (String) properties.get(BORDER_RIGHT_WIDTH);
	}

	/**
	 * Get the top border color
	 */
	public String getBorderTopColor() {
		return (String) properties.get(BORDER_TOP_COLOR);
	}

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public String getBorderTopStyle() {
		return (String) properties.get(BORDER_TOP_STYLE);
	}

	/**
	 * Get the top border width.
	 */
	public String getBorderTopWidth() {
		return (String) properties.get(BORDER_TOP_WIDTH);
	}

	/**
	 * Can this element shrink?
	 */
	public String getCanShrink() {
		return (String) properties.get(CAN_SHRINK);
	}

	/**
	 * Get the font color
	 */
	public String getColor() {
		return (String) properties.get(COLOR);
	}

	/**
	 * Get the date format
	 * 
	 * @return date format
	 */
	public String getDateFormat() {
		return (String) properties.get(DATE_FORMAT);
	}

	public String getDirection() {
		return (String) properties.get(DIRECTION);
	}

	/**
	 * Get the display type (valid types are BLOCK, INLINE and NONE)
	 */
	public String getDisplay() {
		return (String) properties.get(DISPLAY);
	}

	/**
	 * Get the font family
	 * 
	 */
	public String getFontFamily() {
		return (String) properties.get(FONT_FAMILY);
	}

	/**
	 * Get the font size
	 * 
	 */
	public String getFontSize() {
		return (String) properties.get(FONT_SIZE);
	}

	/**
	 * Get the font style
	 * 
	 */
	public String getFontStyle() {
		return (String) properties.get(FONT_STYLE);
	}

	/**
	 * Get the font variant
	 * 
	 */
	public String getFontVariant() {
		return (String) properties.get(FONT_VARIANT);
	}

	/**
	 * Get the font weight
	 * 
	 */
	public String getFontWeight() {
		return (String) properties.get(FONT_WEIGHT);
	}

	/**
	 * Get the letter spacing
	 */
	public String getLetterSpacing() {
		return (String) properties.get(LETTER_SPACING);
	}

	/**
	 * Get the line height
	 */
	public String getLineHeight() {
		return (String) properties.get(LINE_HEIGHT);
	}

	/**
	 * Get the bottom margin
	 */
	public String getMarginBottom() {
		return (String) properties.get(MARGIN_BOTTOM);
	}

	/**
	 * Get the left margin
	 */
	public String getMarginLeft() {
		return (String) properties.get(MARGIN_LEFT);
	}

	/**
	 * Get the right margin
	 */
	public String getMarginRight() {
		return (String) properties.get(MARGIN_RIGHT);
	}

	/**
	 * Get the top margin
	 */
	public String getMarginTop() {
		return (String) properties.get(MARGIN_TOP);
	}

	/**
	 * Get the master page
	 */
	public String getMasterPage() {
		return (String) properties.get(MASTER_PAGE);
	}

	/**
	 * Get number format
	 * 
	 * @return the number format
	 */
	public String getNumberFormat() {
		return (String) properties.get(NUMBER_FORMAT);
	}

	/**
	 * Get the bottom padding.
	 */
	public String getPaddingBottom() {
		return (String) properties.get(PADDING_BOTTOM);
	}

	/**
	 * Get the left padding.
	 */
	public String getPaddingLeft() {
		return (String) properties.get(PADDING_LEFT);
	}

	/**
	 * Get the right padding.
	 */
	public String getPaddingRight() {
		return (String) properties.get(PADDING_RIGHT);
	}

	/**
	 * Get the top padding.
	 */
	public String getPaddingTop() {
		return (String) properties.get(PADDING_TOP);
	}

	/**
	 * Get the page break after.
	 */
	public String getPageBreakAfter() {
		return (String) properties.get(PAGE_BREAK_AFTER);
	}

	/**
	 * Get the page break before.
	 */
	public String getPageBreakBefore() {
		return (String) properties.get(PAGE_BREAK_BEFORE);
	}

	/**
	 * Get the page break inside.
	 */
	public String getPageBreakInside() {
		return (String) properties.get(PAGE_BREAK_INSIDE);
	}

	/**
	 * Show if blank?
	 */
	public String getShowIfBlank() {
		return (String) properties.get(SHOW_IF_BLANK);
	}

	/**
	 * Get the string format
	 * 
	 * @return the string format
	 */
	public String getStringFormat() {
		return (String) properties.get(STRING_FORMAT);
	}

	/**
	 * Get the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	public String getTextAlign() {
		return (String) properties.get(TEXT_ALIGN);
	}

	/**
	 * Get the text indent
	 */
	public String getTextIndent() {
		return (String) properties.get(TEXT_INDENT);
	}

	/**
	 * Get the text line through
	 */
	public String getTextLineThrough() {
		return (String) properties.get(TEXT_LINE_THROUGH);
	}

	/**
	 * Get the text overline
	 */
	public String getTextOverline() {
		return (String) properties.get(TEXT_OVERLINE);
	}

	/**
	 * Get the text transform. Valid return values are NONE, CAPITALIZE, UPPERCASE
	 * and LOWERCASE.
	 */
	public String getTextTransform() {
		return (String) properties.get(TEXT_TRANSFORM);
	}

	/**
	 * Get the text underline
	 */
	public String getTextUnderline() {
		return (String) properties.get(TEXT_UNDERLINE);
	}

	/**
	 * Get the vertical alignment. Valid return values are BASELINE, SUB, SUPER,
	 * TOP, TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
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
	public String getVisibleFormat() {
		return (String) properties.get(VISIBLE_FORMAT);
	}

	/**
	 * Get the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	public String getWhiteSpace() {
		return (String) properties.get(WHITE_SPACE);
	}

	/**
	 * Get the word spacing
	 */
	public String getWordSpacing() {
		return (String) properties.get(WORD_SPACING);
	}

	public void setBackgroundAttachement(String attachement) {
		setProperty(BACKGROUND_ATTACHMENT, attachement);
	}

	/**
	 * Set the attachment type (either SCROLL or FIXED)
	 */
	public void setBackgroundAttachment(String attachment) {
		setProperty(BACKGROUND_ATTACHMENT, attachment);
	}

	/**
	 * Set the background color
	 */
	public void setBackgroundColor(String color) {
		setProperty(BACKGROUND_COLOR, color);
	}

	/**
	 * Set the background image URI
	 */
	public void setBackgroundImage(String imageURI) {
		setProperty(BACKGROUND_IMAGE, imageURI);
	}

	/**
	 * Set the X (horizontal) position of the background image
	 * 
	 */
	public void setBackgroundPositionX(String x) throws ScriptException {
		setProperty(BACKGROUND_POSITION_X, x);
	}

	/**
	 * Set the Y (vertical) position of the background image
	 * 
	 */
	public void setBackgroundPositionY(String y) throws ScriptException {
		setProperty(BACKGROUND_POSITION_Y, y);
	}

	/**
	 * Set the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	public void setBackgroundRepeat(String repeat) {
		setProperty(BACKGROUND_REPEAT, repeat);
	}

	/**
	 * Set the bottom border color
	 */
	public void setBorderBottomColor(String color) {
		setProperty(BORDER_BOTTOM_COLOR, color);
	}

	/**
	 * Set the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public void setBorderBottomStyle(String borderstyle) {
		setProperty(BORDER_BOTTOM_STYLE, borderstyle);
	}

	/**
	 * Set the bottom border width.
	 */
	public void setBorderBottomWidth(String width) {
		setProperty(BORDER_BOTTOM_WIDTH, width);
	}

	/**
	 * Set the left border color
	 */
	public void setBorderLeftColor(String color) {
		setProperty(BORDER_LEFT_COLOR, color);
	}

	/**
	 * Set the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public void setBorderLeftStyle(String borderstyle) {
		setProperty(BORDER_LEFT_STYLE, borderstyle);
	}

	/**
	 * Set the left border width.
	 */
	public void setBorderLeftWidth(String width) {
		setProperty(BORDER_LEFT_WIDTH, width);
	}

	/**
	 * Set the right border color
	 */
	public void setBorderRightColor(String color) {
		setProperty(BORDER_RIGHT_COLOR, color);
	}

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public void setBorderRightStyle(String borderstyle) {
		setProperty(BORDER_RIGHT_STYLE, borderstyle);
	}

	/**
	 * Set the right border width.
	 */
	public void setBorderRightWidth(String width) {
		setProperty(BORDER_RIGHT_WIDTH, width);
	}

	/**
	 * Set the top border color
	 */
	public void setBorderTopColor(String color) {
		setProperty(BORDER_TOP_COLOR, color);
	}

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	public void setBorderTopStyle(String borderstyle) {
		setProperty(BORDER_TOP_STYLE, borderstyle);
	}

	/**
	 * Set the top border width.
	 */
	public void setBorderTopWidth(String width) {
		setProperty(BORDER_TOP_WIDTH, width);
	}

	/**
	 * Can this element shrink?
	 */
	public void setCanShrink(String canShrink) {
		setProperty(CAN_SHRINK, canShrink);
	}

	/**
	 * Set the font color
	 */
	public void setColor(String color) {
		setProperty(COLOR, color);
	}

	/**
	 * Set the date format
	 * 
	 * @param dateTimeFormat
	 */
	public void setDateFormat(String dateTimeFormat) {
		setProperty(DATE_FORMAT, dateTimeFormat);
	}

	public void setDirection(String direction) {
		setProperty(DIRECTION, direction);
	}

	/**
	 * Set the display type (valid types are BLOCK, INLINE and NONE)
	 */
	public void setDisplay(String display) {
		setProperty(DISPLAY, display);
	}

	/**
	 * Set the font family
	 * 
	 */
	public void setFontFamily(String fontFamily) {
		setProperty(FONT_FAMILY, fontFamily);
	}

	/**
	 * Set the font size
	 * 
	 */
	public void setFontSize(String fontSize) {
		setProperty(FONT_SIZE, fontSize);
	}

	/**
	 * Set the font style
	 * 
	 */
	public void setFontStyle(String fontStyle) {
		setProperty(FONT_STYLE, fontStyle);
	}

	/**
	 * Set the font variant
	 * 
	 */
	public void setFontVariant(String fontVariant) {
		setProperty(FONT_VARIANT, fontVariant);
	}

	/**
	 * Set the font weight
	 * 
	 */
	public void setFontWeight(String fontWeight) {
		setProperty(FONT_WEIGHT, fontWeight);
	}

	/**
	 * Set the letter spacing
	 */
	public void setLetterSpacing(String spacing) {
		setProperty(LETTER_SPACING, spacing);
	}

	/**
	 * Set the line height
	 */
	public void setLineHeight(String lineHeight) {
		setProperty(LINE_HEIGHT, lineHeight);
	}

	/**
	 * Set the bottom margin
	 */
	public void setMarginBottom(String margin) {
		setProperty(MARGIN_BOTTOM, margin);
	}

	/**
	 * Set the left margin
	 */
	public void setMarginLeft(String margin) {
		setProperty(MARGIN_LEFT, margin);
	}

	/**
	 * Set the right margin
	 */
	public void setMarginRight(String margin) {
		setProperty(MARGIN_RIGHT, margin);
	}

	/**
	 * Set the top margin
	 */
	public void setMarginTop(String margin) {
		setProperty(MARGIN_TOP, margin);
	}

	/**
	 * Set the master page
	 */
	public void setMasterPage(String masterPage) {
		setProperty(MASTER_PAGE, masterPage);
	}

	/**
	 * Set the number format
	 * 
	 * @param numberFormat
	 */
	public void setNumberFormat(String numberFormat) {
		setProperty(NUMBER_FORMAT, numberFormat);
	}

	/**
	 * Set the bottom padding.
	 */
	public void setPaddingBottom(String padding) {
		setProperty(PADDING_BOTTOM, padding);
	}

	/**
	 * Set the left padding.
	 */
	public void setPaddingLeft(String padding) {
		setProperty(PADDING_LEFT, padding);
	}

	/**
	 * Set the right padding.
	 */
	public void setPaddingRight(String padding) {
		setProperty(PADDING_RIGHT, padding);
	}

	/**
	 * Set the top padding.
	 */
	public void setPaddingTop(String padding) {
		setProperty(PADDING_TOP, padding);
	}

	/**
	 * Set the page break after
	 */
	public void setPageBreakAfter(String pageBreak) {
		setProperty(PAGE_BREAK_AFTER, pageBreak);
	}

	/**
	 * Set the page break before
	 */
	public void setPageBreakBefore(String pageBreak) {
		setProperty(PAGE_BREAK_BEFORE, pageBreak);
	}

	/**
	 * Set the page break inside
	 */
	public void setPageBreakInside(String pageBreak) {
		setProperty(PAGE_BREAK_INSIDE, pageBreak);
	}

	/**
	 * Set show if blank
	 */
	public void setShowIfBlank(String showIfBlank) {
		setProperty(SHOW_IF_BLANK, showIfBlank);
	}

	/**
	 * Set the string format
	 * 
	 * @param stringFormat
	 */
	public void setStringFormat(String stringFormat) {
		setProperty(STRING_FORMAT, stringFormat);
	}

	/**
	 * Set the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	public void setTextAlign(String align) {
		setProperty(TEXT_ALIGN, align);
	}

	/**
	 * Set the text indent
	 */
	public void setTextIndent(String indent) {
		setProperty(TEXT_INDENT, indent);
	}

	/**
	 * Set the text line through
	 */
	public void setTextLineThrough(String through) throws ScriptException {
		setProperty(TEXT_LINE_THROUGH, through);
	}

	/**
	 * Set the text overline
	 */
	public void setTextOverline(String overline) throws ScriptException {
		setProperty(TEXT_OVERLINE, overline);
	}

	/**
	 * Set the text transform. Valid transform values are NONE, CAPITALIZE,
	 * UPPERCASE and LOWERCASE.
	 */
	public void setTextTransform(String transform) {
		setProperty(TEXT_TRANSFORM, transform);
	}

	/**
	 * Set the text underline
	 */
	public void setTextUnderline(String underline) throws ScriptException {
		setProperty(TEXT_UNDERLINE, underline);
	}

	/**
	 * Set the vertical alignment. Valid values are BASELINE, SUB, SUPER, TOP,
	 * TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
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
	public void setVisibleFormat(String format) {
		setProperty(VISIBLE_FORMAT, format);
	}

	/**
	 * Set the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	public void setWhiteSpace(String whitespace) {
		setProperty(WHITE_SPACE, whitespace);
	}

	/**
	 * Set the word spacing
	 */
	public void setWordSpacing(String wordspacing) {
		setProperty(WORD_SPACING, wordspacing);
	}

	public String getDateLocale() {
		return (String) properties.get(DATE_LOCALE);
	}

	public void setDateLocale(String locale) {
		setProperty(DATE_LOCALE, locale);
	}

	public String getNumberLocale() {
		return (String) properties.get(NUMBER_LOCALE);
	}

	public void setNumberLocale(String locale) {
		setProperty(NUMBER_LOCALE, locale);
	}

	public String getStringLocale() {
		return (String) properties.get(STRING_LOCALE);
	}

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
