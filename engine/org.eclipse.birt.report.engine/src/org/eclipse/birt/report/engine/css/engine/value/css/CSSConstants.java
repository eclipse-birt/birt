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
package org.eclipse.birt.report.engine.css.engine.value.css;

/**
 * This interface defines constants for CSS. Important: Constants must not
 * contain uppercase characters.
 *
 */
public interface CSSConstants {

	//
	// The CSS property.
	//

	/**
	 * CSS property: background-attachment
	 */
	public final static String CSS_BACKGROUND_ATTACHMENT_PROPERTY = "background-attachment";
	/**
	 * CSS property: background-color
	 */
	public final static String CSS_BACKGROUND_COLOR_PROPERTY = "background-color";
	/**
	 * CSS property: background-image
	 */
	public final static String CSS_BACKGROUND_IMAGE_PROPERTY = "background-image";
	/**
	 * CSS property: background-repeat
	 */
	public final static String CSS_BACKGROUND_REPEAT_PROPERTY = "background-repeat";
	/**
	 * CSS property: background-repeat-x
	 */
	public final static String CSS_BACKGROUND_REPEAT_X_PROPERTY = "background-repeat-x";
	/**
	 * CSS property: background-repeat-y
	 */
	public final static String CSS_BACKGROUND_REPEAT_Y_PROPERTY = "background-repeat-y";
	/**
	 * CSS property: background-height
	 */
	public final static String CSS_BACKGROUND_HEIGHT_PROPERTY = "background-height";
	/**
	 * CSS property: background-width
	 */
	public final static String CSS_BACKGROUND_WIDTH_PROPERTY = "background-width";

	/**
	 * CSS property: border-top-color
	 */
	public final static String CSS_BORDER_TOP_COLOR_PROPERTY = "border-top-color";
	/**
	 * CSS property: border-top-color
	 */
	public final static String CSS_BORDER_RIGHT_COLOR_PROPERTY = "border-right-color";
	/**
	 * CSS property: border-right-color
	 */
	public final static String CSS_BORDER_BOTTOM_COLOR_PROPERTY = "border-bottom-color";
	/**
	 * CSS property: border-left-color
	 */
	public final static String CSS_BORDER_LEFT_COLOR_PROPERTY = "border-left-color";
	/**
	 * CSS property: border-top-style
	 */
	public final static String CSS_BORDER_TOP_STYLE_PROPERTY = "border-top-style";
	/**
	 * CSS property: border-right-style
	 */
	public final static String CSS_BORDER_RIGHT_STYLE_PROPERTY = "border-right-style";
	/**
	 * CSS property: border-bottom-style
	 */
	public final static String CSS_BORDER_BOTTOM_STYLE_PROPERTY = "border-bottom-style";
	/**
	 * CSS property: border-left-style
	 */
	public final static String CSS_BORDER_LEFT_STYLE_PROPERTY = "border-left-style";

	/**
	 * CSS property: border-top-width
	 */
	public final static String CSS_BORDER_TOP_WIDTH_PROPERTY = "border-top-width";
	/**
	 * CSS property: border-right-width
	 */
	public final static String CSS_BORDER_RIGHT_WIDTH_PROPERTY = "border-right-width";
	/**
	 * CSS property: border-bottom-width
	 */
	public final static String CSS_BORDER_BOTTOM_WIDTH_PROPERTY = "border-bottom-width";
	/**
	 * CSS property: border-left-width
	 */
	public final static String CSS_BORDER_LEFT_WIDTH_PROPERTY = "border-left-width";

	/**
	 * CSS property: color
	 */
	public final static String CSS_COLOR_PROPERTY = "color";
	/**
	 * CSS property: display
	 */
	public final static String CSS_DISPLAY_PROPERTY = "display";
	/**
	 * CSS property: font-family
	 */
	public final static String CSS_FONT_FAMILY_PROPERTY = "font-family";
	/**
	 * CSS property: font-size
	 */
	public final static String CSS_FONT_SIZE_PROPERTY = "font-size";
	/**
	 * CSS property: font-style
	 */
	public final static String CSS_FONT_STYLE_PROPERTY = "font-style";
	/**
	 * CSS property: font-variant
	 */
	public final static String CSS_FONT_VARIANT_PROPERTY = "font-variant";
	/**
	 * CSS property: font-weight
	 */
	public final static String CSS_FONT_WEIGHT_PROPERTY = "font-weight";
	/**
	 * CSS property: letter-spacing
	 */
	public final static String CSS_LETTER_SPACING_PROPERTY = "letter-spacing";
	/**
	 * CSS property: line-height
	 */
	public final static String CSS_LINE_HEIGHT_PROPERTY = "line-height";
	/**
	 * CSS property: margin-right
	 */
	public final static String CSS_MARGIN_RIGHT_PROPERTY = "margin-right";
	/**
	 * CSS property: margin-left
	 */
	public final static String CSS_MARGIN_LEFT_PROPERTY = "margin-left";
	/**
	 * CSS property: margin-top
	 */
	public final static String CSS_MARGIN_TOP_PROPERTY = "margin-top";
	/**
	 * CSS property: margin-bottom
	 */
	public final static String CSS_MARGIN_BOTTOM_PROPERTY = "margin-bottom";
	/**
	 * CSS property: orphans
	 */
	public final static String CSS_ORPHANS_PROPERTY = "orphans";
	/**
	 * CSS property: padding-top
	 */
	public final static String CSS_PADDING_TOP_PROPERTY = "padding-top";
	/**
	 * CSS property: padding-right
	 */
	public final static String CSS_PADDING_RIGHT_PROPERTY = "padding-right";
	/**
	 * CSS property: padding-bottom
	 */
	public final static String CSS_PADDING_BOTTOM_PROPERTY = "padding-bottom";
	/**
	 * CSS property: padding-left
	 */
	public final static String CSS_PADDING_LEFT_PROPERTY = "padding-left";
	/**
	 * CSS property: page-break-after
	 */
	public final static String CSS_PAGE_BREAK_AFTER_PROPERTY = "page-break-after";
	/**
	 * CSS property: page-break-before
	 */
	public final static String CSS_PAGE_BREAK_BEFORE_PROPERTY = "page-break-before";
	/**
	 * CSS property: page-break-inside
	 */
	public final static String CSS_PAGE_BREAK_INSIDE_PROPERTY = "page-break-inside";
	/**
	 * CSS property: text-align
	 */
	public final static String CSS_TEXT_ALIGN_PROPERTY = "text-align";
	/**
	 * CSS property: text-indent
	 */
	public final static String CSS_TEXT_INDENT_PROPERTY = "text-indent";
	/**
	 * CSS property: text-transform
	 */
	public final static String CSS_TEXT_TRANSFORM_PROPERTY = "text-transform";
	/**
	 * CSS property: text-transform
	 */
	public final static String CSS_VERTICAL_ALIGN_PROPERTY = "vertical-align";
	/**
	 * CSS property: white-space
	 */
	public final static String CSS_WHITE_SPACE_PROPERTY = "white-space";
	/**
	 * CSS property: widows
	 */
	public final static String CSS_WIDOWS_PROPERTY = "widows";
	/**
	 * CSS property: word-spacing
	 */
	public final static String CSS_WORD_SPACING_PROPERTY = "word-spacing";


	//
	// The CSS property values.
	//

	/**
	 * CSS property value, background attachment: scroll
	 */
	public final static String CSS_SCROLL_VALUE = "scroll";
	/**
	 * CSS property value, background attachment: fixed
	 */
	public final static String CSS_FIXED_VALUE = "fixed";

	/**
	 * CSS property value, background color: transparent
	 */
	public final static String CSS_TRANSPARENT_VALUE = "transparent";

	/**
	 * CSS property value, background position: center
	 */
	public final static String CSS_CENTER_VALUE = "center";
	/**
	 * CSS property value, background position: left
	 */
	public final static String CSS_LEFT_VALUE = "left";
	/**
	 * CSS property value, background position: bottom
	 */
	public final static String CSS_BOTTOM_VALUE = "bottom";


	/**
	 * CSS property value, background size: contain
	 */
	public final static String CSS_CONTAIN_VALUE = "contain";
	/**
	 * CSS property value, background size: cover
	 */
	public final static String CSS_COVER_VALUE = "cover";

	/**
	 * CSS property value: 100
	 */
	public final static String CSS_100_VALUE = "100";
	/**
	 * CSS property value: 200
	 */
	public final static String CSS_200_VALUE = "200";
	/**
	 * CSS property value: 300
	 */
	public final static String CSS_300_VALUE = "300";
	/**
	 * CSS property value: 400
	 */
	public final static String CSS_400_VALUE = "400";
	/**
	 * CSS property value: 500
	 */
	public final static String CSS_500_VALUE = "500";
	/**
	 * CSS property value: 600
	 */
	public final static String CSS_600_VALUE = "600";
	/**
	 * CSS property value: 700
	 */
	public final static String CSS_700_VALUE = "700";
	/**
	 * CSS property value: 800
	 */
	public final static String CSS_800_VALUE = "800";
	/**
	 * CSS property value: 900
	 */
	public final static String CSS_900_VALUE = "900";
	/**
	 * CSS property value: above
	 */
	public final static String CSS_ABOVE_VALUE = "above";
	/**
	 * CSS property value: absolute
	 */
	public final static String CSS_ABSOLUTE_VALUE = "absolute";
	/**
	 * CSS property value: always
	 */
	public final static String CSS_ALWAYS_VALUE = "always";
	/**
	 * CSS property value: armenian
	 */
	public final static String CSS_ARMENIAN_VALUE = "armenian";
	/**
	 * CSS property value: attr()
	 */
	public final static String CSS_ATTR_VALUE = "attr()";

	/**
	 * CSS property value: auto
	 */
	public final static String CSS_AUTO_VALUE = "auto";
	/**
	 * CSS property value: avoid
	 */
	public final static String CSS_AVOID_VALUE = "avoid";
	/**
	 * CSS property value: baseline
	 */
	public final static String CSS_BASELINE_VALUE = "baseline";
	/**
	 * CSS property value: behind
	 */
	public final static String CSS_BEHIND_VALUE = "behind";
	/**
	 * CSS property value: below
	 */
	public final static String CSS_BELOW_VALUE = "below";
	/**
	 * CSS property value:bidi-override
	 */
	public final static String CSS_BIDI_OVERRIDE_VALUE = "bidi-override";
	/**
	 * CSS property value: blink
	 */
	public final static String CSS_BLINK_VALUE = "blink";
	/**
	 * CSS property value: block
	 */
	public final static String CSS_BLOCK_VALUE = "block";
	/**
	 * CSS property value: bold
	 */
	public final static String CSS_BOLD_VALUE = "bold";
	/**
	 * CSS property value: bolder
	 */
	public final static String CSS_BOLDER_VALUE = "bolder";
	/**
	 * CSS property value: both
	 */
	public final static String CSS_BOTH_VALUE = "both";
	/**
	 * CSS property value: capitalize
	 */
	public final static String CSS_CAPITALIZE_VALUE = "capitalize";
	/**
	 * CSS property value: caption
	 */
	public final static String CSS_CAPTION_VALUE = "caption";
	/**
	 * CSS property value: center-left
	 */
	public final static String CSS_CENTER_LEFT_VALUE = "center-left";
	/**
	 * CSS property value: center-right
	 */
	public final static String CSS_CENTER_RIGHT_VALUE = "center-right";
	/**
	 * CSS property value: circle
	 */
	public final static String CSS_CIRCLE_VALUE = "circle";
	/**
	 * CSS property value: close-quote
	 */
	public final static String CSS_CLOSE_QUOTE_VALUE = "close-quote";
	/**
	 * CSS property value: code
	 */
	public final static String CSS_CODE_VALUE = "code";
	/**
	 * CSS property value: collapse
	 */
	public final static String CSS_COLLAPSE_VALUE = "collapse";
	/**
	 * CSS property value: continuous
	 */
	public final static String CSS_CONTINUOUS_VALUE = "continuous";
	/**
	 * CSS property value: crosshair
	 */
	public final static String CSS_CROSSHAIR_VALUE = "crosshair";
	/**
	 * CSS property value: decimal
	 */
	public final static String CSS_DECIMAL_VALUE = "decimal";
	/**
	 * CSS property value: decimal-leading-zero
	 */
	public final static String CSS_DECIMAL_LEADING_ZERO_VALUE = "decimal-leading-zero";
	/**
	 * CSS property value: default
	 */
	public final static String CSS_DEFAULT_VALUE = "default";
	/**
	 * CSS property value: digits
	 */
	public final static String CSS_DIGITS_VALUE = "digits";
	/**
	 * CSS property value: disc
	 */
	public final static String CSS_DISC_VALUE = "disc";
	/**
	 * CSS property value: embed
	 */
	public final static String CSS_EMBED_VALUE = "embed";
	/**
	 * CSS property value: e-resize
	 */
	public final static String CSS_E_RESIZE_VALUE = "e-resize";
	/**
	 * CSS property value: far-left
	 */
	public final static String CSS_FAR_LEFT_VALUE = "far-left";
	/**
	 * CSS property value: far-right
	 */
	public final static String CSS_FAR_RIGHT_VALUE = "far-right";
	/**
	 * CSS property value: fast
	 */
	public final static String CSS_FAST_VALUE = "fast";
	/**
	 * CSS property value: faster
	 */
	public final static String CSS_FASTER_VALUE = "faster";
	/**
	 * CSS property value: georgian
	 */
	public final static String CSS_GEORGIAN_VALUE = "georgian";
	/**
	 * CSS property value: help
	 */
	public final static String CSS_HELP_VALUE = "help";
	/**
	 * CSS property value: hidden
	 */
	public final static String CSS_HIDDEN_VALUE = "hidden";
	/**
	 * CSS property value: hide
	 */
	public final static String CSS_HIDE_VALUE = "hide";
	/**
	 * CSS property value: high
	 */
	public final static String CSS_HIGH_VALUE = "high";
	/**
	 * CSS property value: higher
	 */
	public final static String CSS_HIGHER_VALUE = "higher";
	/**
	 * CSS property value: icon
	 */
	public final static String CSS_ICON_VALUE = "icon";
	/**
	 * CSS property value: inherit
	 */
	public final static String CSS_INHERIT_VALUE = "inherit";
	/**
	 * CSS property value: inline
	 */
	public final static String CSS_INLINE_VALUE = "inline";
	/**
	 * CSS property value: inline-block
	 */
	public final static String CSS_INLINE_BLOCK_VALUE = "inline-block";
	/**
	 * CSS property value: inline-table
	 */
	public final static String CSS_INLINE_TABLE_VALUE = "inline-table";
	/**
	 * CSS property value: inside
	 */
	public final static String CSS_INSIDE_VALUE = "inside";
	/**
	 * CSS property value: invert
	 */
	public final static String CSS_INVERT_VALUE = "invert";
	/**
	 * CSS property value: italic
	 */
	public final static String CSS_ITALIC_VALUE = "italic";
	/**
	 * CSS property value: justify
	 */
	public final static String CSS_JUSTIFY_VALUE = "justify";
	/**
	 * CSS property value: left-side
	 */
	public final static String CSS_LEFT_SIDE_VALUE = "left-side";
	/**
	 * CSS property value: leftwards
	 */
	public final static String CSS_LEFTWARDS_VALUE = "leftwards";
	/**
	 * CSS property value: level
	 */
	public final static String CSS_LEVEL_VALUE = "level";
	/**
	 * CSS property value: lighter
	 */
	public final static String CSS_LIGHTER_VALUE = "lighter";
	/**
	 * CSS property value: line-through
	 */
	public final static String CSS_LINE_THROUGH_VALUE = "line-through";
	/**
	 * CSS property value: list-item
	 */
	public final static String CSS_LIST_ITEM_VALUE = "list-item";
	/**
	 * CSS property value: loud
	 */
	public final static String CSS_LOUD_VALUE = "loud";
	/**
	 * CSS property value: low
	 */
	public final static String CSS_LOW_VALUE = "low";
	/**
	 * CSS property value: lower
	 */
	public final static String CSS_LOWER_VALUE = "lower";
	/**
	 * CSS property value: lower-alpha
	 */
	public final static String CSS_LOWER_ALPHA_VALUE = "lower-alpha";
	/**
	 * CSS property value: lowercase
	 */
	public final static String CSS_LOWERCASE_VALUE = "lowercase";
	/**
	 * CSS property value: lower-greek
	 */
	public final static String CSS_LOWER_GREEK_VALUE = "lower-greek";
	/**
	 * CSS property value: lower-latin
	 */
	public final static String CSS_LOWER_LATIN_VALUE = "lower-latin";
	/**
	 * CSS property value: lower-roman
	 */
	public final static String CSS_LOWER_ROMAN_VALUE = "lower-roman";
	/**
	 * CSS property value: ltr
	 */
	public final static String CSS_LTR_VALUE = "ltr";
	/**
	 * CSS property value: medium
	 */
	public final static String CSS_MEDIUM_VALUE = "medium";
	/**
	 * CSS property value: menu
	 */
	public final static String CSS_MENU_VALUE = "menu";
	/**
	 * CSS property value: message-box
	 */
	public final static String CSS_MESSAGE_BOX_VALUE = "message-box";
	/**
	 * CSS property value: middle
	 */
	public final static String CSS_MIDDLE_VALUE = "middle";
	/**
	 * CSS property value: mix
	 */
	public final static String CSS_MIX_VALUE = "mix";
	/**
	 * CSS property value: move
	 */
	public final static String CSS_MOVE_VALUE = "move";
	/**
	 * CSS property value: ne-resize
	 */
	public final static String CSS_NE_RESIZE_VALUE = "ne-resize";
	/**
	 * CSS property value: no-close-quote
	 */
	public final static String CSS_NO_CLOSE_QUOTE_VALUE = "no-close-quote";
	/**
	 * CSS property value: none
	 */
	public final static String CSS_NONE_VALUE = "none";
	/**
	 * CSS property value: no-open-quote
	 */
	public final static String CSS_NO_OPEN_QUOTE_VALUE = "no-open-quote";
	/**
	 * CSS property value: no-repeat
	 */
	public final static String CSS_NO_REPEAT_VALUE = "no-repeat";
	/**
	 * CSS property value: normal
	 */
	public final static String CSS_NORMAL_VALUE = "normal";
	/**
	 * CSS property value: nowrap
	 */
	public final static String CSS_NOWRAP_VALUE = "nowrap";
	/**
	 * CSS property value: n-resize
	 */
	public final static String CSS_N_RESIZE_VALUE = "n-resize";
	/**
	 * CSS property value: nw-resize
	 */
	public final static String CSS_NW_RESIZE_VALUE = "nw-resize";
	/**
	 * CSS property value: oblique
	 */
	public final static String CSS_OBLIQUE_VALUE = "oblique";
	/**
	 * CSS property value: once
	 */
	public final static String CSS_ONCE_VALUE = "once";
	/**
	 * CSS property value: open-quote
	 */
	public final static String CSS_OPEN_QUOTE_VALUE = "open-quote";
	/**
	 * CSS property value: outside
	 */
	public final static String CSS_OUTSIDE_VALUE = "outside";
	/**
	 * CSS property value: overline
	 */
	public final static String CSS_OVERLINE_VALUE = "overline";
	/**
	 * CSS property value: pointer
	 */
	public final static String CSS_POINTER_VALUE = "pointer";
	/**
	 * CSS property value: pre
	 */
	public final static String CSS_PRE_VALUE = "pre";
	/**
	 * CSS property value: pre-line
	 */
	public final static String CSS_PRE_LINE_VALUE = "pre-line";
	/**
	 * CSS property value: pre-wrap
	 */
	public final static String CSS_PRE_WRAP_VALUE = "pre-wrap";
	/**
	 * CSS property value: progress
	 */
	public final static String CSS_PROGRESS_VALUE = "progress";
	/**
	 * CSS property value: relative
	 */
	public final static String CSS_RELATIVE_VALUE = "relative";
	/**
	 * CSS property value: repeat
	 */
	public final static String CSS_REPEAT_VALUE = "repeat";
	/**
	 * CSS property value: repeat-x
	 */
	public final static String CSS_REPEAT_X_VALUE = "repeat-x";
	/**
	 * CSS property value: repeat-y
	 */
	public final static String CSS_REPEAT_Y_VALUE = "repeat-y";
	/**
	 * CSS property value: right
	 */
	public final static String CSS_RIGHT_VALUE = "right";
	/**
	 * CSS property value: right-side
	 */
	public final static String CSS_RIGHT_SIDE_VALUE = "right-side";
	/**
	 * CSS property value: rightwards
	 */
	public final static String CSS_RIGHTWARDS_VALUE = "rightwards";
	/**
	 * CSS property value: rtl
	 */
	public final static String CSS_RTL_VALUE = "rtl";
	/**
	 * CSS property value: run-in
	 */
	public final static String CSS_RUN_IN_VALUE = "run-in";
	/**
	 * CSS property value: separate
	 */
	public final static String CSS_SEPARATE_VALUE = "separate";
	/**
	 * CSS property value: se-resize
	 */
	public final static String CSS_SE_RESIZE_VALUE = "se-resize";
	/**
	 * CSS property value: show
	 */
	public final static String CSS_SHOW_VALUE = "show";
	/**
	 * CSS property value: silent
	 */
	public final static String CSS_SILENT_VALUE = "silent";
	/**
	 * CSS property value: slow
	 */
	public final static String CSS_SLOW_VALUE = "slow";
	/**
	 * CSS property value: slower
	 */
	public final static String CSS_SLOWER_VALUE = "slower";
	/**
	 * CSS property value: small-caps
	 */
	public final static String CSS_SMALL_CAPS_VALUE = "small-caps";
	/**
	 * CSS property value: small-caption
	 */
	public final static String CSS_SMALL_CAPTION_VALUE = "small-caption";
	/**
	 * CSS property value: soft
	 */
	public final static String CSS_SOFT_VALUE = "soft";
	/**
	 * CSS property value: spell-out
	 */
	public final static String CSS_SPELL_OUT_VALUE = "spell-out";
	/**
	 * CSS property value: square
	 */
	public final static String CSS_SQUARE_VALUE = "square";
	/**
	 * CSS property value: s-resize
	 */
	public final static String CSS_S_RESIZE_VALUE = "s-resize";
	/**
	 * CSS property value: static
	 */
	public final static String CSS_STATIC_VALUE = "static";
	/**
	 * CSS property value: status-bar
	 */
	public final static String CSS_STATUS_BAR_VALUE = "status-bar";
	/**
	 * CSS property value: sub
	 */
	public final static String CSS_SUB_VALUE = "sub";
	/**
	 * CSS property value: super
	 */
	public final static String CSS_SUPER_VALUE = "super";
	/**
	 * CSS property value: sw-resize
	 */
	public final static String CSS_SW_RESIZE_VALUE = "sw-resize";
	/**
	 * CSS property value: table
	 */
	public final static String CSS_TABLE_VALUE = "table";
	/**
	 * CSS property value: table-caption
	 */
	public final static String CSS_TABLE_CAPTION_VALUE = "table-caption";
	/**
	 * CSS property value: table-cell
	 */
	public final static String CSS_TABLE_CELL_VALUE = "table-cell";
	/**
	 * CSS property value: table-column
	 */
	public final static String CSS_TABLE_COLUMN_VALUE = "table-column";
	/**
	 * CSS property value: table-column-group
	 */
	public final static String CSS_TABLE_COLUMN_GROUP_VALUE = "table-column-group";
	/**
	 * CSS property value: table-footer-group
	 */
	public final static String CSS_TABLE_FOOTER_GROUP_VALUE = "table-footer-group";
	/**
	 * CSS property value: table-header-group
	 */
	public final static String CSS_TABLE_HEADER_GROUP_VALUE = "table-header-group";
	/**
	 * CSS property value: table-row
	 */
	public final static String CSS_TABLE_ROW_VALUE = "table-row";
	/**
	 * CSS property value: table-row-group
	 */
	public final static String CSS_TABLE_ROW_GROUP_VALUE = "table-row-group";
	/**
	 * CSS property value: text
	 */
	public final static String CSS_TEXT_VALUE = "text";
	/**
	 * CSS property value: text-bottom
	 */
	public final static String CSS_TEXT_BOTTOM_VALUE = "text-bottom";
	/**
	 * CSS property value: text-top
	 */
	public final static String CSS_TEXT_TOP_VALUE = "text-top";
	/**
	 * CSS property value: top
	 */
	public final static String CSS_TOP_VALUE = "top";
	/**
	 * CSS property value: underline
	 */
	public final static String CSS_UNDERLINE_VALUE = "underline";
	/**
	 * CSS property value: upper-alpha
	 */
	public final static String CSS_UPPER_ALPHA_VALUE = "upper-alpha";
	/**
	 * CSS property value: uppercase
	 */
	public final static String CSS_UPPERCASE_VALUE = "uppercase";
	/**
	 * CSS property value: upper-latin
	 */
	public final static String CSS_UPPER_LATIN_VALUE = "upper-latin";
	/**
	 * CSS property value: upper-roman
	 */
	public final static String CSS_UPPER_ROMAN_VALUE = "upper-roman";
	/**
	 * CSS property value: visible
	 */
	public final static String CSS_VISIBLE_VALUE = "visible";
	/**
	 * CSS property value: wait
	 */
	public final static String CSS_WAIT_VALUE = "wait";
	/**
	 * CSS property value: w-resize
	 */
	public final static String CSS_W_RESIZE_VALUE = "w-resize";
	/**
	 * CSS property value: x-fast
	 */
	public final static String CSS_X_FAST_VALUE = "x-fast";
	/**
	 * CSS property value: x-high
	 */
	public final static String CSS_X_HIGH_VALUE = "x-high";
	/**
	 * CSS property value: x-loud
	 */
	public final static String CSS_X_LOUD_VALUE = "x-loud";
	/**
	 * CSS property value: x-low
	 */
	public final static String CSS_X_LOW_VALUE = "x-low";
	/**
	 * CSS property value: x-slow
	 */
	public final static String CSS_X_SLOW_VALUE = "x-slow";
	/**
	 * CSS property value: x-soft
	 */
	public final static String CSS_X_SOFT_VALUE = "x-soft";

	// absolute fone size
	/**
	 * CSS property value: x-small
	 */
	public final static String CSS_X_SMALL_VALUE = "x-small";
	/**
	 * CSS property value: xx-small
	 */
	public final static String CSS_XX_SMALL_VALUE = "xx-small";
	/**
	 * CSS property value: small
	 */
	public final static String CSS_SMALL_VALUE = "small";
	/**
	 * CSS property value: large
	 */
	public final static String CSS_LARGE_VALUE = "large";
	/**
	 * CSS property value: x-large
	 */
	public final static String CSS_X_LARGE_VALUE = "x-large";
	/**
	 * CSS property value: xx-large
	 */
	public final static String CSS_XX_LARGE_VALUE = "xx-large";

	// relative font size
	/**
	 * CSS property value: larger
	 */
	public final static String CSS_LARGER_VALUE = "larger";
	/**
	 * CSS property value: smaller
	 */
	public final static String CSS_SMALLER_VALUE = "smaller";

	// genric font family
	/**
	 * CSS property value: serif
	 */
	public final static String CSS_SERIF_VALUE = "serif";
	/**
	 * CSS property value: sans-serif
	 */
	public final static String CSS_SANS_SERIF_VALUE = "sans-serif";
	/**
	 * CSS property value: cursive
	 */
	public final static String CSS_CURSIVE_VALUE = "cursive";
	/**
	 * CSS property value: fantasy
	 */
	public final static String CSS_FANTASY_VALUE = "fantasy";
	/**
	 * CSS property value: monospace
	 */
	public final static String CSS_MONOSPACE_VALUE = "monospace";


	// color
	/**
	 * CSS property value: aqua
	 */
	public final static String CSS_AQUA_VALUE = "aqua";
	/**
	 * CSS property value: black
	 */
	public final static String CSS_BLACK_VALUE = "black";
	/**
	 * CSS property value: blue
	 */
	public final static String CSS_BLUE_VALUE = "blue";
	/**
	 * CSS property value: fuchsia
	 */
	public final static String CSS_FUCHSIA_VALUE = "fuchsia";
	/**
	 * CSS property value: gray
	 */
	public final static String CSS_GRAY_VALUE = "gray";
	/**
	 * CSS property value: green
	 */
	public final static String CSS_GREEN_VALUE = "green";
	/**
	 * CSS property value: lime
	 */
	public final static String CSS_LIME_VALUE = "lime";
	/**
	 * CSS property value: maroon
	 */
	public final static String CSS_MAROON_VALUE = "maroon";
	/**
	 * CSS property value: navy
	 */
	public final static String CSS_NAVY_VALUE = "navy";
	/**
	 * CSS property value: olive
	 */
	public final static String CSS_OLIVE_VALUE = "olive";
	/**
	 * CSS property value: orange
	 */
	public final static String CSS_ORANGE_VALUE = "orange";
	/**
	 * CSS property value: purple
	 */
	public final static String CSS_PURPLE_VALUE = "purple";
	/**
	 * CSS property value: red
	 */
	public final static String CSS_RED_VALUE = "red";
	/**
	 * CSS property value: silver
	 */
	public final static String CSS_SILVER_VALUE = "silver";
	/**
	 * CSS property value: teal
	 */
	public final static String CSS_TEAL_VALUE = "teal";
	/**
	 * CSS property value: white
	 */
	public final static String CSS_WHITE_VALUE = "white";
	/**
	 * CSS property value: yellow
	 */
	public final static String CSS_YELLOW_VALUE = "yellow";


	// System defined color
	/**
	 * CSS property value: ActiveBorder
	 */
	public final static String CSS_ACTIVEBORDER_VALUE = "ActiveBorder";
	/**
	 * CSS property value: ActiveCaption
	 */
	public final static String CSS_ACTIVECAPTION_VALUE = "ActiveCaption";
	/**
	 * CSS property value: AppWorkspace
	 */
	public final static String CSS_APPWORKSPACE_VALUE = "AppWorkspace";
	/**
	 * CSS property value: Background
	 */
	public final static String CSS_BACKGROUND_VALUE = "Background";
	/**
	 * CSS property value: ButtonFace
	 */
	public final static String CSS_BUTTONFACE_VALUE = "ButtonFace";
	/**
	 * CSS property value: ButtonHighlight
	 */
	public final static String CSS_BUTTONHIGHLIGHT_VALUE = "ButtonHighlight";
	/**
	 * CSS property value: ButtonShadow
	 */
	public final static String CSS_BUTTONSHADOW_VALUE = "ButtonShadow";
	/**
	 * CSS property value: ButtonText
	 */
	public final static String CSS_BUTTONTEXT_VALUE = "ButtonText";
	/**
	 * CSS property value: CaptionText
	 */
	public final static String CSS_CAPTIONTEXT_VALUE = "CaptionText";
	/**
	 * CSS property value: GrayText
	 */
	public final static String CSS_GRAYTEXT_VALUE = "GrayText";
	/**
	 * CSS property value: Highlight
	 */
	public final static String CSS_HIGHLIGHT_VALUE = "Highlight";
	/**
	 * CSS property value: HighlightText
	 */
	public final static String CSS_HIGHLIGHTTEXT_VALUE = "HighlightText";
	/**
	 * CSS property value: InactiveBorder
	 */
	public final static String CSS_INACTIVEBORDER_VALUE = "InactiveBorder";
	/**
	 * CSS property value: InactiveCaption
	 */
	public final static String CSS_INACTIVECAPTION_VALUE = "InactiveCaption";
	/**
	 * CSS property value: InactiveCaptionText
	 */
	public final static String CSS_INACTIVECAPTIONTEXT_VALUE = "InactiveCaptionText";
	/**
	 * CSS property value: InfoBackground
	 */
	public final static String CSS_INFOBACKGROUND_VALUE = "InfoBackground";
	/**
	 * CSS property value: InfoText
	 */
	public final static String CSS_INFOTEXT_VALUE = "InfoText";


	// String CSS_MENU_VALUE = "Menu";
	/**
	 * CSS property value: MenuText
	 */
	public final static String CSS_MENUTEXT_VALUE = "MenuText";
	/**
	 * CSS property value: Scrollbar
	 */
	public final static String CSS_SCROLLBAR_VALUE = "Scrollbar";
	/**
	 * CSS property value: ThreeDDarkShadow
	 */
	public final static String CSS_THREEDDARKSHADOW_VALUE = "ThreeDDarkShadow";
	/**
	 * CSS property value: ThreeDFace
	 */
	public final static String CSS_THREEDFACE_VALUE = "ThreeDFace";
	/**
	 * CSS property value: ThreeDHighlight
	 */
	public final static String CSS_THREEDHIGHLIGHT_VALUE = "ThreeDHighlight";
	/**
	 * CSS property value: ThreeDLightShadow
	 */
	public final static String CSS_THREEDLIGHTSHADOW_VALUE = "ThreeDLightShadow";
	/**
	 * CSS property value: ThreeDShadow
	 */
	public final static String CSS_THREEDSHADOW_VALUE = "ThreeDShadow";
	/**
	 * CSS property value: Window
	 */
	public final static String CSS_WINDOW_VALUE = "Window";
	/**
	 * CSS property value: WindowFrame
	 */
	public final static String CSS_WINDOWFRAME_VALUE = "WindowFrame";
	/**
	 * CSS property value: WindowText
	 */
	public final static String CSS_WINDOWTEXT_VALUE = "WindowText";

	// line property value
	/**
	 * CSS property value: dotted
	 */
	public final static String CSS_DOTTED_VALUE = "dotted";
	/**
	 * CSS property value: dashed
	 */
	public final static String CSS_DASHED_VALUE = "dashed";
	/**
	 * CSS property value: solid
	 */
	public final static String CSS_SOLID_VALUE = "solid";
	/**
	 * CSS property value: double
	 */
	public final static String CSS_DOUBLE_VALUE = "double";
	/**
	 * CSS property value: groove
	 */
	public final static String CSS_GROOVE_VALUE = "groove";
	/**
	 * CSS property value: ridge
	 */
	public final static String CSS_RIDGE_VALUE = "ridge";
	/**
	 * CSS property value: inset
	 */
	public final static String CSS_INSET_VALUE = "inset";
	/**
	 * CSS property value: outset
	 */
	public final static String CSS_OUTSET_VALUE = "outset";

	/**
	 * CSS property value: thin
	 */
	public final static String CSS_THIN_VALUE = "thin";

	// String CSS_MEDIUM_VALUE = "medium";
	/**
	 * CSS property value: thick
	 */
	public final static String CSS_THICK_VALUE = "thick";

	/**
	 * CSS property value: auto
	 */
	public final static String CSS_OVERFLOW_AUTO_VALUE = "auto";
	/**
	 * CSS property value: visible
	 */
	public final static String CSS_OVERFLOW_VISIBLE_VALUE = "visible";
	/**
	 * CSS property value: scroll
	 */
	public final static String CSS_OVERFLOW_SCROLL_VALUE = "scroll";
	/**
	 * CSS property value: hidden
	 */
	public final static String CSS_OVERFLOW_HIDDEN_VALUE = "hidden";

	// bidi_hcg: Bidi related
	// "rtl" and "ltr" values are already specified above.
	// We preserve here the name for direction defined in CSS specs.
	/**
	 * CSS property value: direction
	 */
	public final static String CSS_DIRECTION_PROPERTY = "direction";

	// overflow property: auto, visible, scroll, hidden
	/**
	 * CSS property value: overflow
	 */
	public final static String CSS_OVERFLOW_PROPERTY = "overflow";

	/**
	 * CSS property value: height
	 */
	public final static String CSS_HEIGHT_PROPERTY = "height";
	/**
	 * CSS property value: width
	 */
	public final static String CSS_WIDTH_PROPERTY = "width";

	/**
	 * CSS property value: url
	 */
	public final static String CSS_URL_VALUE = "url";
	/**
	 * CSS property value: independent
	 */
	public final static String CSS_INDEPENDENT_VALUE = "independent";
}
