/*******************************************************************************
 * Copyright (c) 2004, 2009, 2023, 2024 Actuate Corporation and others
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
package org.eclipse.birt.report.engine.css.engine;

/**
 * Definition of the style constants index based
 *
 * @since 3.3
 *
 */
public interface StyleConstants {
	// bidi_hcg: As a result of a new style added ("direction"), align the
	// constants with org.eclipse.birt.report.engine.css.engine.PerfectHash

	/** style property: margin left */
	int STYLE_MARGIN_LEFT = 0;
	/** style property: margin right */
	int STYLE_MARGIN_RIGHT = 1;

	/** style property: margin bottom */
	int STYLE_MARGIN_BOTTOM = 2;

	/** style property: data format */
	int STYLE_DATA_FORMAT = 3;

	/** style property: border right color */
	int STYLE_BORDER_RIGHT_COLOR = 4;

	/** style property: border bottom color */
	int STYLE_BORDER_BOTTOM_COLOR = 5;

	/** style property: margin top */
	int STYLE_MARGIN_TOP = 6;

	/** style property: border diagonal color */
	int STYLE_BORDER_DIAGONAL_COLOR = 7;

	/** style property: border diagonal number */
	int STYLE_BORDER_DIAGONAL_NUMBER = 8;

	/** style property: border right width */
	int STYLE_BORDER_RIGHT_WIDTH = 9;

	/** style property: border bottom width */
	int STYLE_BORDER_BOTTOM_WIDTH = 10;

	/** style property: border diagonal width */
	int STYLE_BORDER_DIAGONAL_WIDTH = 11;

	/** style property: padding left */
	int STYLE_PADDING_LEFT = 12;

	/** style property: padding bottom */
	int STYLE_PADDING_BOTTOM = 13;

	/** style property: border antidiagonal color */
	int STYLE_BORDER_ANTIDIAGONAL_COLOR = 14;

	/** style property: border antidiagonal number */
	int STYLE_BORDER_ANTIDIAGONAL_NUMBER = 15;

	/** style property: padding right */
	int STYLE_PADDING_RIGHT = 16;

	/** style property: direction */
	int STYLE_DIRECTION = 17;

	/** style property: border antidiagonal width */
	int STYLE_BORDER_ANTIDIAGONAL_WIDTH = 18;

	/** style property: padding top */
	int STYLE_PADDING_TOP = 19;

	/** style property: border right style */
	int STYLE_BORDER_RIGHT_STYLE = 20;

	/** style property: border bottom style */
	int STYLE_BORDER_BOTTOM_STYLE = 21;

	/** style property: border diagonal style */
	int STYLE_BORDER_DIAGONAL_STYLE = 22;

	/** style property: width */
	int STYLE_WIDTH = 31;

	/** style property: border top color */
	int STYLE_BORDER_TOP_COLOR = 23;

	/** style property: background repeat */
	int STYLE_BACKGROUND_REPEAT = 24;

	/** style property: border antidiagonal style */
	int STYLE_BORDER_ANTIDIAGONAL_STYLE = 25;

	/** style property: border top width */
	int STYLE_BORDER_TOP_WIDTH = 35;

	/** style property: background height */
	int STYLE_BACKGROUND_HEIGHT = 27;

	/** style property: text indent */
	int STYLE_TEXT_INDENT = 26;

	/** style property: page break before */
	int STYLE_PAGE_BREAK_BEFORE = 30;

	/** style property: overflow */
	int STYLE_OVERFLOW = 39;

	/** style property: text transform */
	int STYLE_TEXT_TRANSFORM = 28;

	/** style property: can shrink */
	int STYLE_CAN_SHRINK = 34;

	/** style property: height */
	int STYLE_HEIGHT = 29;

	/** style property: word spacing */
	int STYLE_WORD_SPACING = 42;

	/** style property: text linethrough */
	int STYLE_TEXT_LINETHROUGH = 32;

	/** style property: number align */
	int STYLE_NUMBER_ALIGN = 38;

	/** style property: visible format */
	int STYLE_VISIBLE_FORMAT = 33;

	/** style property: text align */
	int STYLE_TEXT_ALIGN = 37;

	/** style property: background color */
	int STYLE_BACKGROUND_COLOR = 41;

	/** style property: orphans */
	int STYLE_ORPHANS = 36;

	/** style property: background width */
	int STYLE_BACKGROUND_WIDTH = 50;

	/** style property: vertical align */
	int STYLE_VERTICAL_ALIGN = 43;

	/** style property: font weight */
	int STYLE_FONT_WEIGHT = 47;

	/** style property: font variant */
	int STYLE_FONT_VARIANT = 48;

	/** style property: master page */
	int STYLE_MASTER_PAGE = 44;

	/** style property: border left color */
	int STYLE_BORDER_LEFT_COLOR = 57;

	/** style property: show if blank */
	int STYLE_SHOW_IF_BLANK = 46;

	/** style property: border top style */
	int STYLE_BORDER_TOP_STYLE = 45;

	/** style property: border left width */
	int STYLE_BORDER_LEFT_WIDTH = 59;

	/** style property: text overline */
	int STYLE_TEXT_OVERLINE = 49;

	/** style property: widows */
	int STYLE_WIDOWS = 53;

	/** style property: font size */
	int STYLE_FONT_SIZE = 54;

	/** style property: font style */
	int STYLE_FONT_STYLE = 55;

	/** style property: background position x */
	int STYLE_BACKGROUND_POSITION_X = 51;

	/** style property: page break inside */
	int STYLE_PAGE_BREAK_INSIDE = 52;

	/** style property: white space */
	int STYLE_WHITE_SPACE = 61;

	/** style property: border left style */
	int STYLE_BORDER_LEFT_STYLE = 65;

	/** style property: background image */
	int STYLE_BACKGROUND_IMAGE = 56;

	/** style property: background image type */
	int STYLE_BACKGROUND_IMAGE_TYPE = 58;

	/** style property: background position y */
	int STYLE_BACKGROUND_POSITION_Y = 62;

	/** style property: text underline */
	int STYLE_TEXT_UNDERLINE = 60;

	/** style property: line height */
	int STYLE_LINE_HEIGHT = 64;

	/** style property: color */
	int STYLE_COLOR = 66;

	/** style property: display */
	int STYLE_DISPLAY = 63;

	/** style property: page break after */
	int STYLE_PAGE_BREAK_AFTER = 67;

	/** style property: font family */
	int STYLE_FONT_FAMILY = 69;

	/** style property: background attachment */
	int STYLE_BACKGROUND_ATTACHMENT = 68;

	/** style property: letter spacing */
	int STYLE_LETTER_SPACING = 70;

	/** style property: text hyperlink style */
	int STYLE_TEXT_HYPERLINK_STYLE = 40;

	/** number (count) of style constants */
	int NUMBER_OF_STYLE = 71;

}
