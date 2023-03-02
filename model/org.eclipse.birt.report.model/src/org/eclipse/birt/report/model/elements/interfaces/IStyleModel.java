/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for style element to store the constants. The constants
 * represents the ROM property names.
 */
public interface IStyleModel {

	// Property names: 66

	/**
	 * Property name: background attachment
	 */
	String BACKGROUND_ATTACHMENT_PROP = "backgroundAttachment"; //$NON-NLS-1$
	/**
	 * Property name: background color
	 */
	String BACKGROUND_COLOR_PROP = "backgroundColor"; //$NON-NLS-1$
	/**
	 * Property name: background image
	 */
	String BACKGROUND_IMAGE_PROP = "backgroundImage"; //$NON-NLS-1$
	/**
	 * Property name: background image source type
	 */
	String BACKGROUND_IMAGE_TYPE_PROP = "backgroundImageType"; //$NON-NLS-1$
	/**
	 * Property name: background position X
	 */
	String BACKGROUND_POSITION_X_PROP = "backgroundPositionX"; //$NON-NLS-1$
	/**
	 * Property name: background position Y
	 */
	String BACKGROUND_POSITION_Y_PROP = "backgroundPositionY"; //$NON-NLS-1$
	/**
	 * Property name: background repeat
	 */
	String BACKGROUND_REPEAT_PROP = "backgroundRepeat"; //$NON-NLS-1$
	/**
	 * Property name: background size width
	 */
	String BACKGROUND_SIZE_WIDTH = "backgroundSizeWidth"; //$NON-NLS-1$
	/**
	 * Property name: background size height
	 */
	String BACKGROUND_SIZE_HEIGHT = "backgroundSizeHeight"; //$NON-NLS-1$
	/**
	 * Property name: border bottom color
	 */
	String BORDER_BOTTOM_COLOR_PROP = "borderBottomColor"; //$NON-NLS-1$
	/**
	 * Property name: border bottom style
	 */
	String BORDER_BOTTOM_STYLE_PROP = "borderBottomStyle"; //$NON-NLS-1$
	/**
	 * Property name: border bottom width
	 */
	String BORDER_BOTTOM_WIDTH_PROP = "borderBottomWidth"; //$NON-NLS-1$
	/**
	 * Property name: border left color
	 */
	String BORDER_LEFT_COLOR_PROP = "borderLeftColor"; //$NON-NLS-1$
	/**
	 * Property name: border left style
	 */
	String BORDER_LEFT_STYLE_PROP = "borderLeftStyle"; //$NON-NLS-1$
	/**
	 * Property name: border left width
	 */
	String BORDER_LEFT_WIDTH_PROP = "borderLeftWidth"; //$NON-NLS-1$
	/**
	 * Property name: border right color
	 */
	String BORDER_RIGHT_COLOR_PROP = "borderRightColor"; //$NON-NLS-1$
	/**
	 * Property name: border right style
	 */
	String BORDER_RIGHT_STYLE_PROP = "borderRightStyle"; //$NON-NLS-1$
	/**
	 * Property name: border right width
	 */
	String BORDER_RIGHT_WIDTH_PROP = "borderRightWidth"; //$NON-NLS-1$
	/**
	 * Property name: border top color
	 */
	String BORDER_TOP_COLOR_PROP = "borderTopColor"; //$NON-NLS-1$
	/**
	 * Property name: border top style
	 */
	String BORDER_TOP_STYLE_PROP = "borderTopStyle"; //$NON-NLS-1$
	/**
	 * Property name: border top width
	 */
	String BORDER_TOP_WIDTH_PROP = "borderTopWidth"; //$NON-NLS-1$
	/**
	 * Property name: border diagonal color
	 */
	String BORDER_DIAGONAL_NUMBER_PROP = "diagonalNumber"; //$NON-NLS-1$
	/**
	 * Property name: border diagonal color
	 */
	String BORDER_DIAGONAL_COLOR_PROP = "diagonalColor"; //$NON-NLS-1$
	/**
	 * Property name: border diagonal style
	 */
	String BORDER_DIAGONAL_STYLE_PROP = "diagonalStyle"; //$NON-NLS-1$
	/**
	 * Property name: border diagonal width
	 */
	String BORDER_DIAGONAL_WIDTH_PROP = "diagonalThickness"; //$NON-NLS-1$
	/**
	 * Property name: border antidiagonal color
	 */
	String BORDER_ANTIDIAGONAL_NUMBER_PROP = "antidiagonalNumber"; //$NON-NLS-1$
	/**
	 * Property name: border antidiagonal color
	 */
	String BORDER_ANTIDIAGONAL_COLOR_PROP = "antidiagonalColor"; //$NON-NLS-1$
	/**
	 * Property name: border antidiagonal style
	 */
	String BORDER_ANTIDIAGONAL_STYLE_PROP = "antidiagonalStyle"; //$NON-NLS-1$
	/**
	 * Property name: border antidiagonal width
	 */
	String BORDER_ANTIDIAGONAL_WIDTH_PROP = "antidiagonalThickness"; //$NON-NLS-1$

	/**
	 * Property name: margin bottom
	 */
	String MARGIN_BOTTOM_PROP = "marginBottom"; //$NON-NLS-1$
	/**
	 * Property name: margin left
	 */
	String MARGIN_LEFT_PROP = "marginLeft"; //$NON-NLS-1$
	/**
	 * Property name: margin right
	 */
	String MARGIN_RIGHT_PROP = "marginRight"; //$NON-NLS-1$
	/**
	 * Property name: margin top
	 */
	String MARGIN_TOP_PROP = "marginTop"; //$NON-NLS-1$

	/**
	 * Property name: padding top
	 */
	String PADDING_TOP_PROP = "paddingTop"; //$NON-NLS-1$
	/**
	 * Property name: padding left
	 */
	String PADDING_LEFT_PROP = "paddingLeft"; //$NON-NLS-1$
	/**
	 * Property name: padding bottom
	 */
	String PADDING_BOTTOM_PROP = "paddingBottom"; //$NON-NLS-1$
	/**
	 * Property name: padding right
	 */
	String PADDING_RIGHT_PROP = "paddingRight"; //$NON-NLS-1$

	/**
	 * Property name: can shrink
	 */
	String CAN_SHRINK_PROP = "canShrink"; //$NON-NLS-1$
	/**
	 * Property name: color
	 */
	String COLOR_PROP = "color"; //$NON-NLS-1$

	/**
	 * Property name: format date time
	 */
	String DATE_TIME_FORMAT_PROP = "dateTimeFormat"; //$NON-NLS-1$
	/**
	 * Property name: format date
	 */
	String DATE_FORMAT_PROP = "dateFormat"; //$NON-NLS-1$
	/**
	 * Property name: format time
	 */
	String TIME_FORMAT_PROP = "timeFormat"; //$NON-NLS-1$

	/**
	 * Property name: font family
	 */
	String FONT_FAMILY_PROP = "fontFamily"; //$NON-NLS-1$
	/**
	 * Property name: font size
	 */
	String FONT_SIZE_PROP = "fontSize"; //$NON-NLS-1$
	/**
	 * Property name: font style
	 */
	String FONT_STYLE_PROP = "fontStyle"; //$NON-NLS-1$
	/**
	 * Property name: font weight
	 */
	String FONT_WEIGHT_PROP = "fontWeight"; //$NON-NLS-1$
	/**
	 * Property name: font variant
	 */
	String FONT_VARIANT_PROP = "fontVariant"; //$NON-NLS-1$

	/**
	 * Property name: text underline
	 */
	String TEXT_UNDERLINE_PROP = "textUnderline"; //$NON-NLS-1$
	/**
	 * Property name: text overline
	 */
	String TEXT_OVERLINE_PROP = "textOverline"; //$NON-NLS-1$
	/**
	 * Property name: text line through
	 */
	String TEXT_LINE_THROUGH_PROP = "textLineThrough"; //$NON-NLS-1$
	// public static final String HIGHLIGHT_TEST_EXPR_PROP =
	// "highlightTestExpr"; //$NON-NLS-1$

	/**
	 * Property name: highlight rules
	 */
	String HIGHLIGHT_RULES_PROP = "highlightRules"; //$NON-NLS-1$
	/**
	 * Property name: map rules
	 */
	String MAP_RULES_PROP = "mapRules"; //$NON-NLS-1$
	/**
	 * Property name: number format
	 */
	String NUMBER_FORMAT_PROP = "numberFormat"; //$NON-NLS-1$
	/**
	 * Property name: number align
	 */
	String NUMBER_ALIGN_PROP = "numberAlign"; //$NON-NLS-1$
	/**
	 * Property name: display
	 */
	String DISPLAY_PROP = "display"; //$NON-NLS-1$
	/**
	 * Property name: master page
	 */
	String MASTER_PAGE_PROP = "masterPage"; //$NON-NLS-1$

	/**
	 * Property name: page break before
	 */
	String PAGE_BREAK_BEFORE_PROP = "pageBreakBefore"; //$NON-NLS-1$
	/**
	 * Property name: page break after
	 */
	String PAGE_BREAK_AFTER_PROP = "pageBreakAfter"; //$NON-NLS-1$
	/**
	 * Property name: page break inside
	 */
	String PAGE_BREAK_INSIDE_PROP = "pageBreakInside"; //$NON-NLS-1$

	/**
	 * Property name: show if blank
	 */
	String SHOW_IF_BLANK_PROP = "showIfBlank"; //$NON-NLS-1$
	/**
	 * Property name: string format
	 */
	String STRING_FORMAT_PROP = "stringFormat"; //$NON-NLS-1$
	/**
	 * Property name: text align
	 */
	String TEXT_ALIGN_PROP = "textAlign"; //$NON-NLS-1$
	/**
	 * Property name: text indent
	 */
	String TEXT_INDENT_PROP = "textIndent"; //$NON-NLS-1$
	/**
	 * Property name: letter spacing
	 */
	String LETTER_SPACING_PROP = "letterSpacing"; //$NON-NLS-1$
	/**
	 * Property name: line height
	 */
	String LINE_HEIGHT_PROP = "lineHeight"; //$NON-NLS-1$
	/**
	 * Property name: orphans
	 */
	String ORPHANS_PROP = "orphans"; //$NON-NLS-1$
	/**
	 * Property name: text transform
	 */
	String TEXT_TRANSFORM_PROP = "textTransform"; //$NON-NLS-1$
	/**
	 * Property name: vertical align
	 */
	String VERTICAL_ALIGN_PROP = "verticalAlign"; //$NON-NLS-1$
	/**
	 * Property name: white space
	 */
	String WHITE_SPACE_PROP = "whiteSpace"; //$NON-NLS-1$
	/**
	 * Property name: widows
	 */
	String WIDOWS_PROP = "widows"; //$NON-NLS-1$
	/**
	 * Property name: word spacing
	 */
	String WORD_SPACING_PROP = "wordSpacing"; //$NON-NLS-1$

	/**
	 * Bidi text direction property
	 */
	String TEXT_DIRECTION_PROP = "bidiTextDirection"; //$NON-NLS-1$
	/**
	 * Property name: overfow
	 */
	String OVERFLOW_PROP = "overflow"; //$NON-NLS-1$

	/**
	 * Property name: height
	 */
	String HEIGHT_PROP = "height"; //$NON-NLS-1$
	/**
	 * Property name: width
	 */
	String WIDTH_PROP = "width"; //$NON-NLS-1$
}
