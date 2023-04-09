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

import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

/**
 * Definition of the TOC styles
 *
 * @since 3.3
 *
 */
public class TOCStyle implements IScriptStyle, Serializable {

	/**
	 * constant of serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Style property: background-attachment
	 */
	public static final String BACKGROUND_ATTACHMENT = "background-attachment";
	/**
	 * Style property: background-color
	 */
	public static final String BACKGROUND_COLOR = "background-color";
	/**
	 * Style property: background-image
	 */
	public static final String BACKGROUND_IMAGE = "background-image";
	/**
	 * Style property: background-image-type
	 */
	public static final String BACKGROUND_IMAGE_TYPE = "background-image-type";
	/**
	 * Style property: background image height
	 */
	public static final String BACKGROUND_SIZE_HEIGHT = "background-size-height";
	/**
	 * Style property: background image width
	 */
	public static final String BACKGROUND_SIZE_WIDTH = "background-size-width";
	/**
	 * Style property: background-position-x
	 */
	public static final String BACKGROUND_POSITION_X = "background-position-x";
	/**
	 * Style property: background-position-y
	 */
	public static final String BACKGROUND_POSITION_Y = "background-position-y";
	/**
	 * Style property: background-repeat
	 */
	public static final String BACKGROUND_REPEAT = "background-repeat";
	/**
	 * Style property: border-bottom-color
	 */
	public static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
	/**
	 * Style property: border-top-color
	 */
	public static final String BORDER_TOP_COLOR = "border-top-color";
	/**
	 * Style property: border-left-color
	 */
	public static final String BORDER_LEFT_COLOR = "border-left-color";
	/**
	 * Style property: border-right-color
	 */
	public static final String BORDER_RIGHT_COLOR = "border-right-color";
	/**
	 * Style property: border-bottom-width
	 */
	public static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";
	/**
	 * Style property: border-top-width
	 */
	public static final String BORDER_TOP_WIDTH = "border-top-width";
	/**
	 * Style property: border-left-width
	 */
	public static final String BORDER_LEFT_WIDTH = "border-left-width";
	/**
	 * Style property: border-right-width
	 */
	public static final String BORDER_RIGHT_WIDTH = "border-right-width";
	/**
	 * Style property: border-bottom-style
	 */
	public static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
	/**
	 * Style property: border-top-style
	 */
	public static final String BORDER_TOP_STYLE = "border-top-style";
	/**
	 * Style property: border-left-style
	 */
	public static final String BORDER_LEFT_STYLE = "border-left-style";
	/**
	 * Style property: border-right-style
	 */
	public static final String BORDER_RIGHT_STYLE = "border-right-style";

	/**
	 * Style property: can-shrink
	 */
	public static final String CAN_SHRINK = "can-shrink";
	/**
	 * Style property: color
	 */
	public static final String COLOR = "color";
	/**
	 * Style property: date-format
	 */
	public static final String DATE_FORMAT = "date-format";
	/**
	 * Style property: direction
	 */
	public static final String DIRECTION = "direction"; // bidi_hcg
	/**
	 * Style property: display
	 */
	public static final String DISPLAY = "display";

	/**
	 * Style property: font-family
	 */
	public static final String FONT_FAMILY = "font-family";
	/**
	 * Style property: font-size
	 */
	public static final String FONT_SIZE = "font-size";
	/**
	 * Style property: font-style
	 */
	public static final String FONT_STYLE = "font-style";
	/**
	 * Style property: font-variant
	 */
	public static final String FONT_VARIANT = "font-variant";
	/**
	 * Style property: font-weight
	 */
	public static final String FONT_WEIGHT = "font-weight";
	/**
	 * Style property: letter-spacing
	 */
	public static final String LETTER_SPACING = "letter-spacing";
	/**
	 * Style property: line-height
	 */
	public static final String LINE_HEIGHT = "line-height";

	/**
	 * Style property: margin-bottom
	 */
	public static final String MARGIN_BOTTOM = "margin-bottom";
	/**
	 * Style property: margin-left
	 */
	public static final String MARGIN_LEFT = "margin-left";
	/**
	 * Style property: margin-right
	 */
	public static final String MARGIN_RIGHT = "margin-right";
	/**
	 * Style property: margin-top
	 */
	public static final String MARGIN_TOP = "margin-top";
	/**
	 * Style property: margin-page
	 */
	public static final String MASTER_PAGE = "master-page";

	/**
	 * Style property: number-format
	 */
	public static final String NUMBER_FORMAT = "number-format";
	/**
	 * Style property: padding-bottom
	 */
	public static final String PADDING_BOTTOM = "padding-bottom";
	/**
	 * Style property: padding-left
	 */
	public static final String PADDING_LEFT = "padding-left";
	/**
	 * Style property: padding-right
	 */
	public static final String PADDING_RIGHT = "padding-right";
	/**
	 * Style property: padding-top
	 */
	public static final String PADDING_TOP = "padding-top";

	/**
	 * Style property: page-break-after
	 */
	public static final String PAGE_BREAK_AFTER = "page-break-after";
	/**
	 * Style property: page-break-before
	 */
	public static final String PAGE_BREAK_BEFORE = "page-break-before";
	/**
	 * Style property: page-break-inside
	 */
	public static final String PAGE_BREAK_INSIDE = "page-break-inside";
	/**
	 * Style property: show-if-blank
	 */
	public static final String SHOW_IF_BLANK = "show-if-blank";
	/**
	 * Style property: string-format
	 */
	public static final String STRING_FORMAT = "string-format";

	/**
	 * Style property: text-align
	 */
	public static final String TEXT_ALIGN = "text-align";
	/**
	 * Style property: text-indent
	 */
	public static final String TEXT_INDENT = "text-indent";
	/**
	 * Style property: text-line-through
	 */
	public static final String TEXT_LINE_THROUGH = "text-line-through";
	/**
	 * Style property: text-overline
	 */
	public static final String TEXT_OVERLINE = "text-overline";
	/**
	 * Style property: text-transform
	 */
	public static final String TEXT_TRANSFORM = "text-transform";
	/**
	 * Style property: text-underline
	 */
	public static final String TEXT_UNDERLINE = "text-underline";

	/**
	 * Style property: vertical-align
	 */
	public static final String VERTICAL_ALIGN = "vertical-align";
	/**
	 * Style property: visible-format
	 */
	public static final String VISIBLE_FORMAT = "visible-format";
	/**
	 * Style property: white-space
	 */
	public static final String WHITE_SPACE = "white-space";
	/**
	 * Style property: word-spacing
	 */
	public static final String WORD_SPACING = "word-spacing";
	/**
	 * Style property: date-local
	 */
	public static final String DATE_LOCALE = "date-locale";
	/**
	 * Style property: number-local
	 */
	public static final String NUMBER_LOCALE = "number-locale";
	/**
	 * Style property: string-local
	 */
	public static final String STRING_LOCALE = "string-locale";

	private HashMap<String, String> properties = new HashMap<>();

	@Override
	public String getBackgroundAttachement() {
		return properties.get(BACKGROUND_ATTACHMENT);
	}

	/**
	 * Get the attachment type (either SCROLL or FIXED)
	 */
	@Override
	public String getBackgroundAttachment() {
		return properties.get(BACKGROUND_ATTACHMENT);
	}

	/**
	 * Get the background color
	 */
	@Override
	public String getBackgroundColor() {
		return properties.get(BACKGROUND_COLOR);
	}

	/**
	 * Get the background image URI
	 */
	@Override
	public String getBackgroundImage() {
		return properties.get(BACKGROUND_IMAGE);
	}

	/**
	 * Get the background image source type
	 */
	@Override
	public String getBackgroundImageType() {
		return properties.get(BACKGROUND_IMAGE_TYPE);
	}

	/**
	 * Get the X (horizontal) position of the background image
	 *
	 */
	@Override
	public String getBackgroundPositionX() {
		return properties.get(BACKGROUND_POSITION_X);
	}

	/**
	 * Get the Y (vertical) position of the background image
	 *
	 */
	@Override
	public String getBackgroundPositionY() {
		return properties.get(BACKGROUND_POSITION_Y);
	}

	/**
	 * Get the background repeat type (valid types are REPEAT, REPEAT_X, REPEAT_Y
	 * and NO_REPEAT)
	 */
	@Override
	public String getBackgroundRepeat() {
		return properties.get(BACKGROUND_REPEAT);
	}

	/**
	 * Get the background image height
	 */
	@Override
	public String getBackgroundHeight() {
		return properties.get(BACKGROUND_SIZE_HEIGHT);
	}

	/**
	 * Get the background image height
	 */
	@Override
	public String getBackgroundWidth() {
		return properties.get(BACKGROUND_SIZE_WIDTH);
	}

	/**
	 * Get the bottom border color
	 */
	@Override
	public String getBorderBottomColor() {
		return properties.get(BORDER_BOTTOM_COLOR);
	}

	/**
	 * Get the bottom border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderBottomStyle() {
		return properties.get(BORDER_BOTTOM_STYLE);
	}

	/**
	 * Get the bottom border width.
	 */
	@Override
	public String getBorderBottomWidth() {
		return properties.get(BORDER_BOTTOM_WIDTH);
	}

	/**
	 * Get the left border color
	 */
	@Override
	public String getBorderLeftColor() {
		return properties.get(BORDER_LEFT_COLOR);
	}

	/**
	 * Get the left border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderLeftStyle() {
		return properties.get(BORDER_LEFT_STYLE);
	}

	/**
	 * Get the left border width.
	 */
	@Override
	public String getBorderLeftWidth() {
		return properties.get(BORDER_LEFT_WIDTH);
	}

	/**
	 * Get the right border color
	 */
	@Override
	public String getBorderRightColor() {
		return properties.get(BORDER_RIGHT_COLOR);
	}

	/**
	 * Get the right border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderRightStyle() {
		return properties.get(BORDER_RIGHT_STYLE);
	}

	/**
	 * Get the right border width.
	 */
	@Override
	public String getBorderRightWidth() {
		return properties.get(BORDER_RIGHT_WIDTH);
	}

	/**
	 * Get the top border color
	 */
	@Override
	public String getBorderTopColor() {
		return properties.get(BORDER_TOP_COLOR);
	}

	/**
	 * Get the top border style. Valid styles are NONE, SOLID, DOTTED, DASHED,
	 * DOUBLE, GROVE, RIDGE, INSET and OUTSET.
	 */
	@Override
	public String getBorderTopStyle() {
		return properties.get(BORDER_TOP_STYLE);
	}

	/**
	 * Get the top border width.
	 */
	@Override
	public String getBorderTopWidth() {
		return properties.get(BORDER_TOP_WIDTH);
	}

	/**
	 * Can this element shrink?
	 */
	@Override
	public String getCanShrink() {
		return properties.get(CAN_SHRINK);
	}

	/**
	 * Get the font color
	 */
	@Override
	public String getColor() {
		return properties.get(COLOR);
	}

	/**
	 * Get the date format
	 *
	 * @return date format
	 */
	@Override
	public String getDateFormat() {
		return properties.get(DATE_FORMAT);
	}

	/**
	 * Get the direction
	 *
	 * @return Return the direction
	 */
	public String getDirection() {
		return properties.get(DIRECTION);
	}

	/**
	 * Get the display type (valid types are BLOCK, INLINE and NONE)
	 */
	@Override
	public String getDisplay() {
		return properties.get(DISPLAY);
	}

	/**
	 * Get the font family
	 *
	 */
	@Override
	public String getFontFamily() {
		return properties.get(FONT_FAMILY);
	}

	/**
	 * Get the font size
	 *
	 */
	@Override
	public String getFontSize() {
		return properties.get(FONT_SIZE);
	}

	/**
	 * Get the font style
	 *
	 */
	@Override
	public String getFontStyle() {
		return properties.get(FONT_STYLE);
	}

	/**
	 * Get the font variant
	 *
	 */
	@Override
	public String getFontVariant() {
		return properties.get(FONT_VARIANT);
	}

	/**
	 * Get the font weight
	 *
	 */
	@Override
	public String getFontWeight() {
		return properties.get(FONT_WEIGHT);
	}

	/**
	 * Get the letter spacing
	 */
	@Override
	public String getLetterSpacing() {
		return properties.get(LETTER_SPACING);
	}

	/**
	 * Get the line height
	 */
	@Override
	public String getLineHeight() {
		return properties.get(LINE_HEIGHT);
	}

	/**
	 * Get the bottom margin
	 */
	@Override
	public String getMarginBottom() {
		return properties.get(MARGIN_BOTTOM);
	}

	/**
	 * Get the left margin
	 */
	@Override
	public String getMarginLeft() {
		return properties.get(MARGIN_LEFT);
	}

	/**
	 * Get the right margin
	 */
	@Override
	public String getMarginRight() {
		return properties.get(MARGIN_RIGHT);
	}

	/**
	 * Get the top margin
	 */
	@Override
	public String getMarginTop() {
		return properties.get(MARGIN_TOP);
	}

	/**
	 * Get the master page
	 */
	@Override
	public String getMasterPage() {
		return properties.get(MASTER_PAGE);
	}

	/**
	 * Get number format
	 *
	 * @return the number format
	 */
	@Override
	public String getNumberFormat() {
		return properties.get(NUMBER_FORMAT);
	}

	/**
	 * Get the bottom padding.
	 */
	@Override
	public String getPaddingBottom() {
		return properties.get(PADDING_BOTTOM);
	}

	/**
	 * Get the left padding.
	 */
	@Override
	public String getPaddingLeft() {
		return properties.get(PADDING_LEFT);
	}

	/**
	 * Get the right padding.
	 */
	@Override
	public String getPaddingRight() {
		return properties.get(PADDING_RIGHT);
	}

	/**
	 * Get the top padding.
	 */
	@Override
	public String getPaddingTop() {
		return properties.get(PADDING_TOP);
	}

	/**
	 * Get the page break after.
	 */
	@Override
	public String getPageBreakAfter() {
		return properties.get(PAGE_BREAK_AFTER);
	}

	/**
	 * Get the page break before.
	 */
	@Override
	public String getPageBreakBefore() {
		return properties.get(PAGE_BREAK_BEFORE);
	}

	/**
	 * Get the page break inside.
	 */
	@Override
	public String getPageBreakInside() {
		return properties.get(PAGE_BREAK_INSIDE);
	}

	/**
	 * Show if blank?
	 */
	@Override
	public String getShowIfBlank() {
		return properties.get(SHOW_IF_BLANK);
	}

	/**
	 * Get the string format
	 *
	 * @return the string format
	 */
	@Override
	public String getStringFormat() {
		return properties.get(STRING_FORMAT);
	}

	/**
	 * Get the text alignment. Valid return types are LEFT, RIGHT, CENTER and
	 * JUSTIFY.
	 */
	@Override
	public String getTextAlign() {
		return properties.get(TEXT_ALIGN);
	}

	/**
	 * Get the text indent
	 */
	@Override
	public String getTextIndent() {
		return properties.get(TEXT_INDENT);
	}

	/**
	 * Get the text line through
	 */
	@Override
	public String getTextLineThrough() {
		return properties.get(TEXT_LINE_THROUGH);
	}

	/**
	 * Get the text overline
	 */
	@Override
	public String getTextOverline() {
		return properties.get(TEXT_OVERLINE);
	}

	/**
	 * Get the text transform. Valid return values are NONE, CAPITALIZE, UPPERCASE
	 * and LOWERCASE.
	 */
	@Override
	public String getTextTransform() {
		return properties.get(TEXT_TRANSFORM);
	}

	/**
	 * Get the text underline
	 */
	@Override
	public String getTextUnderline() {
		return properties.get(TEXT_UNDERLINE);
	}

	/**
	 * Get the vertical alignment. Valid return values are BASELINE, SUB, SUPER,
	 * TOP, TEXT_TOP, MIDDLE, BOTTOM and TEXT_BOTTOM
	 */
	@Override
	public String getVerticalAlign() {
		return properties.get(VERTICAL_ALIGN);
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
		return properties.get(VISIBLE_FORMAT);
	}

	/**
	 * Get the whitespace. Valid return values are NORMAL, PRE and NOWRAP
	 */
	@Override
	public String getWhiteSpace() {
		return properties.get(WHITE_SPACE);
	}

	/**
	 * Get the word spacing
	 */
	@Override
	public String getWordSpacing() {
		return properties.get(WORD_SPACING);
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
	 * Set the background image source type
	 */
	@Override
	public void setBackgroundImageType(String imageSourceType) {
		setProperty(BACKGROUND_IMAGE_TYPE, imageSourceType);
	}

	/**
	 * Set the X (horizontal) position of the background image
	 *
	 */
	@Override
	public void setBackgroundPositionX(String x) {
		setProperty(BACKGROUND_POSITION_X, x);
	}

	/**
	 * Set the Y (vertical) position of the background image
	 *
	 */
	@Override
	public void setBackgroundPositionY(String y) {
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
	 * Set the background image height
	 */
	@Override
	public void setBackgroundHeight(String height) {
		setProperty(BACKGROUND_SIZE_HEIGHT, height);
	}

	/**
	 * Set the background image height
	 */
	@Override
	public void setBackgroundWidth(String width) {
		setProperty(BACKGROUND_SIZE_WIDTH, width);
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

	/**
	 * Set direction
	 *
	 * @param direction
	 */
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
	public void setTextLineThrough(String through) {
		setProperty(TEXT_LINE_THROUGH, through);
	}

	/**
	 * Set the text overline
	 */
	@Override
	public void setTextOverline(String overline) {
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
	public void setTextUnderline(String underline) {
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
		return properties.get(DATE_LOCALE);
	}

	@Override
	public void setDateLocale(String locale) {
		setProperty(DATE_LOCALE, locale);
	}

	@Override
	public String getNumberLocale() {
		return properties.get(NUMBER_LOCALE);
	}

	@Override
	public void setNumberLocale(String locale) {
		setProperty(NUMBER_LOCALE, locale);
	}

	@Override
	public String getStringLocale() {
		return properties.get(STRING_LOCALE);
	}

	@Override
	public void setStringLocale(String locale) {
		setProperty(STRING_LOCALE, locale);
	}

	/**
	 * Set property.
	 *
	 * @param name
	 * @param value
	 */
	public void setProperty(String name, String value) {
		if (value != null) {
			properties.put(name, value);
		} else {
			properties.remove(name);
		}
	}

	/**
	 * Get properties.
	 *
	 * @return Return properties
	 */
	public Map<String, String> getProperties() {
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
