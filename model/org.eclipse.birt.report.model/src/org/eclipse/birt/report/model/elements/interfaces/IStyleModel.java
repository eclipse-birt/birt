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
 * The interface for style element to store the constants.
 */
public interface IStyleModel {

	// Property names: 58

	String BACKGROUND_ATTACHMENT_PROP = "backgroundAttachment"; //$NON-NLS-1$
	String BACKGROUND_COLOR_PROP = "backgroundColor"; //$NON-NLS-1$
	String BACKGROUND_IMAGE_PROP = "backgroundImage"; //$NON-NLS-1$
	String BACKGROUND_IMAGE_TYPE_PROP = "backgroundImageType"; //$NON-NLS-1$
	String BACKGROUND_POSITION_X_PROP = "backgroundPositionX"; //$NON-NLS-1$
	String BACKGROUND_POSITION_Y_PROP = "backgroundPositionY"; //$NON-NLS-1$
	String BACKGROUND_REPEAT_PROP = "backgroundRepeat"; //$NON-NLS-1$
	String BACKGROUND_SIZE_WIDTH = "backgroundSizeWidth"; //$NON-NLS-1$
	String BACKGROUND_SIZE_HEIGHT = "backgroundSizeHeight"; //$NON-NLS-1$
	String BORDER_BOTTOM_COLOR_PROP = "borderBottomColor"; //$NON-NLS-1$
	String BORDER_BOTTOM_STYLE_PROP = "borderBottomStyle"; //$NON-NLS-1$
	String BORDER_BOTTOM_WIDTH_PROP = "borderBottomWidth"; //$NON-NLS-1$
	String BORDER_LEFT_COLOR_PROP = "borderLeftColor"; //$NON-NLS-1$
	String BORDER_LEFT_STYLE_PROP = "borderLeftStyle"; //$NON-NLS-1$
	String BORDER_LEFT_WIDTH_PROP = "borderLeftWidth"; //$NON-NLS-1$
	String BORDER_RIGHT_COLOR_PROP = "borderRightColor"; //$NON-NLS-1$
	String BORDER_RIGHT_STYLE_PROP = "borderRightStyle"; //$NON-NLS-1$
	String BORDER_RIGHT_WIDTH_PROP = "borderRightWidth"; //$NON-NLS-1$
	String BORDER_TOP_COLOR_PROP = "borderTopColor"; //$NON-NLS-1$
	String BORDER_TOP_STYLE_PROP = "borderTopStyle"; //$NON-NLS-1$
	String BORDER_TOP_WIDTH_PROP = "borderTopWidth"; //$NON-NLS-1$
	String MARGIN_BOTTOM_PROP = "marginBottom"; //$NON-NLS-1$
	String MARGIN_LEFT_PROP = "marginLeft"; //$NON-NLS-1$
	String MARGIN_RIGHT_PROP = "marginRight"; //$NON-NLS-1$
	String MARGIN_TOP_PROP = "marginTop"; //$NON-NLS-1$
	String PADDING_TOP_PROP = "paddingTop"; //$NON-NLS-1$
	String PADDING_LEFT_PROP = "paddingLeft"; //$NON-NLS-1$
	String PADDING_BOTTOM_PROP = "paddingBottom"; //$NON-NLS-1$
	String PADDING_RIGHT_PROP = "paddingRight"; //$NON-NLS-1$
	String CAN_SHRINK_PROP = "canShrink"; //$NON-NLS-1$
	String COLOR_PROP = "color"; //$NON-NLS-1$
	String DATE_TIME_FORMAT_PROP = "dateTimeFormat"; //$NON-NLS-1$
	String DATE_FORMAT_PROP = "dateFormat"; //$NON-NLS-1$
	String TIME_FORMAT_PROP = "timeFormat"; //$NON-NLS-1$
	String FONT_FAMILY_PROP = "fontFamily"; //$NON-NLS-1$
	String FONT_SIZE_PROP = "fontSize"; //$NON-NLS-1$
	String FONT_STYLE_PROP = "fontStyle"; //$NON-NLS-1$
	String FONT_WEIGHT_PROP = "fontWeight"; //$NON-NLS-1$
	String FONT_VARIANT_PROP = "fontVariant"; //$NON-NLS-1$
	String TEXT_UNDERLINE_PROP = "textUnderline"; //$NON-NLS-1$
	String TEXT_OVERLINE_PROP = "textOverline"; //$NON-NLS-1$
	String TEXT_LINE_THROUGH_PROP = "textLineThrough"; //$NON-NLS-1$
	// public static final String HIGHLIGHT_TEST_EXPR_PROP =
	// "highlightTestExpr"; //$NON-NLS-1$
	String HIGHLIGHT_RULES_PROP = "highlightRules"; //$NON-NLS-1$
	String MAP_RULES_PROP = "mapRules"; //$NON-NLS-1$
	String NUMBER_FORMAT_PROP = "numberFormat"; //$NON-NLS-1$
	String NUMBER_ALIGN_PROP = "numberAlign"; //$NON-NLS-1$
	String DISPLAY_PROP = "display"; //$NON-NLS-1$
	String MASTER_PAGE_PROP = "masterPage"; //$NON-NLS-1$
	String PAGE_BREAK_BEFORE_PROP = "pageBreakBefore"; //$NON-NLS-1$
	String PAGE_BREAK_AFTER_PROP = "pageBreakAfter"; //$NON-NLS-1$
	String PAGE_BREAK_INSIDE_PROP = "pageBreakInside"; //$NON-NLS-1$
	String SHOW_IF_BLANK_PROP = "showIfBlank"; //$NON-NLS-1$
	String STRING_FORMAT_PROP = "stringFormat"; //$NON-NLS-1$
	String TEXT_ALIGN_PROP = "textAlign"; //$NON-NLS-1$
	String TEXT_INDENT_PROP = "textIndent"; //$NON-NLS-1$
	String LETTER_SPACING_PROP = "letterSpacing"; //$NON-NLS-1$
	String LINE_HEIGHT_PROP = "lineHeight"; //$NON-NLS-1$
	String ORPHANS_PROP = "orphans"; //$NON-NLS-1$
	String TEXT_TRANSFORM_PROP = "textTransform"; //$NON-NLS-1$
	String VERTICAL_ALIGN_PROP = "verticalAlign"; //$NON-NLS-1$
	String WHITE_SPACE_PROP = "whiteSpace"; //$NON-NLS-1$
	String WIDOWS_PROP = "widows"; //$NON-NLS-1$
	String WORD_SPACING_PROP = "wordSpacing"; //$NON-NLS-1$
	/*
	 * Bidi text direction property
	 */
	String TEXT_DIRECTION_PROP = "bidiTextDirection"; //$NON-NLS-1$
	String OVERFLOW_PROP = "overflow"; //$NON-NLS-1$

	String HEIGHT_PROP = "height"; //$NON-NLS-1$
	String WIDTH_PROP = "width"; //$NON-NLS-1$
}
