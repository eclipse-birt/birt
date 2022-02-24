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
	// The CSS property names.
	//
	String CSS_BACKGROUND_ATTACHMENT_PROPERTY = "background-attachment";
	String CSS_BACKGROUND_COLOR_PROPERTY = "background-color";
	String CSS_BACKGROUND_IMAGE_PROPERTY = "background-image";
	String CSS_BACKGROUND_REPEAT_PROPERTY = "background-repeat";
	String CSS_BACKGROUND_REPEAT_X_PROPERTY = "background-repeat-x";
	String CSS_BACKGROUND_REPEAT_Y_PROPERTY = "background-repeat-y";
	String CSS_BACKGROUND_HEIGHT_PROPERTY = "background-height";
	String CSS_BACKGROUND_WIDTH_PROPERTY = "background-width";

	String CSS_BORDER_TOP_COLOR_PROPERTY = "border-top-color";
	String CSS_BORDER_RIGHT_COLOR_PROPERTY = "border-right-color";
	String CSS_BORDER_BOTTOM_COLOR_PROPERTY = "border-bottom-color";
	String CSS_BORDER_LEFT_COLOR_PROPERTY = "border-left-color";
	String CSS_BORDER_TOP_STYLE_PROPERTY = "border-top-style";
	String CSS_BORDER_RIGHT_STYLE_PROPERTY = "border-right-style";
	String CSS_BORDER_BOTTOM_STYLE_PROPERTY = "border-bottom-style";
	String CSS_BORDER_LEFT_STYLE_PROPERTY = "border-left-style";
	String CSS_BORDER_TOP_WIDTH_PROPERTY = "border-top-width";
	String CSS_BORDER_RIGHT_WIDTH_PROPERTY = "border-right-width";
	String CSS_BORDER_BOTTOM_WIDTH_PROPERTY = "border-bottom-width";
	String CSS_BORDER_LEFT_WIDTH_PROPERTY = "border-left-width";
	String CSS_COLOR_PROPERTY = "color";
	String CSS_DISPLAY_PROPERTY = "display";
	String CSS_FONT_FAMILY_PROPERTY = "font-family";
	String CSS_FONT_SIZE_PROPERTY = "font-size";
	String CSS_FONT_STYLE_PROPERTY = "font-style";
	String CSS_FONT_VARIANT_PROPERTY = "font-variant";
	String CSS_FONT_WEIGHT_PROPERTY = "font-weight";
	String CSS_LETTER_SPACING_PROPERTY = "letter-spacing";
	String CSS_LINE_HEIGHT_PROPERTY = "line-height";
	String CSS_MARGIN_RIGHT_PROPERTY = "margin-right";
	String CSS_MARGIN_LEFT_PROPERTY = "margin-left";
	String CSS_MARGIN_TOP_PROPERTY = "margin-top";
	String CSS_MARGIN_BOTTOM_PROPERTY = "margin-bottom";
	String CSS_ORPHANS_PROPERTY = "orphans";
	String CSS_PADDING_TOP_PROPERTY = "padding-top";
	String CSS_PADDING_RIGHT_PROPERTY = "padding-right";
	String CSS_PADDING_BOTTOM_PROPERTY = "padding-bottom";
	String CSS_PADDING_LEFT_PROPERTY = "padding-left";
	String CSS_PAGE_BREAK_AFTER_PROPERTY = "page-break-after";
	String CSS_PAGE_BREAK_BEFORE_PROPERTY = "page-break-before";
	String CSS_PAGE_BREAK_INSIDE_PROPERTY = "page-break-inside";
	String CSS_TEXT_ALIGN_PROPERTY = "text-align";
	String CSS_TEXT_INDENT_PROPERTY = "text-indent";
	String CSS_TEXT_TRANSFORM_PROPERTY = "text-transform";
	String CSS_VERTICAL_ALIGN_PROPERTY = "vertical-align";
	String CSS_WHITE_SPACE_PROPERTY = "white-space";
	String CSS_WIDOWS_PROPERTY = "widows";
	String CSS_WORD_SPACING_PROPERTY = "word-spacing";

	//
	// The CSS property values.
	//

	// background attachment
	String CSS_SCROLL_VALUE = "scroll";
	String CSS_FIXED_VALUE = "fixed";

	// background color
	String CSS_TRANSPARENT_VALUE = "transparent";

	// background position
	String CSS_CENTER_VALUE = "center";
	String CSS_LEFT_VALUE = "left";
	String CSS_BOTTOM_VALUE = "bottom";

	// background size
	String CSS_CONTAIN_VALUE = "contain";
	String CSS_COVER_VALUE = "cover";

	String CSS_100_VALUE = "100";
	String CSS_200_VALUE = "200";
	String CSS_300_VALUE = "300";
	String CSS_400_VALUE = "400";
	String CSS_500_VALUE = "500";
	String CSS_600_VALUE = "600";
	String CSS_700_VALUE = "700";
	String CSS_800_VALUE = "800";
	String CSS_900_VALUE = "900";
	String CSS_ABOVE_VALUE = "above";
	String CSS_ABSOLUTE_VALUE = "absolute";
	String CSS_ALWAYS_VALUE = "always";
	String CSS_ARMENIAN_VALUE = "armenian";
	String CSS_ATTR_VALUE = "attr()";
	String CSS_AUTO_VALUE = "auto";
	String CSS_AVOID_VALUE = "avoid";
	String CSS_BASELINE_VALUE = "baseline";
	String CSS_BEHIND_VALUE = "behind";
	String CSS_BELOW_VALUE = "below";
	String CSS_BIDI_OVERRIDE_VALUE = "bidi-override";
	String CSS_BLINK_VALUE = "blink";
	String CSS_BLOCK_VALUE = "block";
	String CSS_BOLD_VALUE = "bold";
	String CSS_BOLDER_VALUE = "bolder";
	String CSS_BOTH_VALUE = "both";
	String CSS_CAPITALIZE_VALUE = "capitalize";
	String CSS_CAPTION_VALUE = "caption";
	String CSS_CENTER_LEFT_VALUE = "center-left";
	String CSS_CENTER_RIGHT_VALUE = "center-right";
	String CSS_CIRCLE_VALUE = "circle";
	String CSS_CLOSE_QUOTE_VALUE = "close-quote";
	String CSS_CODE_VALUE = "code";
	String CSS_COLLAPSE_VALUE = "collapse";
	String CSS_CONTINUOUS_VALUE = "continuous";
	String CSS_CROSSHAIR_VALUE = "crosshair";
	String CSS_DECIMAL_VALUE = "decimal";
	String CSS_DECIMAL_LEADING_ZERO_VALUE = "decimal-leading-zero";
	String CSS_DEFAULT_VALUE = "default";
	String CSS_DIGITS_VALUE = "digits";
	String CSS_DISC_VALUE = "disc";
	String CSS_EMBED_VALUE = "embed";
	String CSS_E_RESIZE_VALUE = "e-resize";
	String CSS_FAR_LEFT_VALUE = "far-left";
	String CSS_FAR_RIGHT_VALUE = "far-right";
	String CSS_FAST_VALUE = "fast";
	String CSS_FASTER_VALUE = "faster";
	String CSS_GEORGIAN_VALUE = "georgian";
	String CSS_HELP_VALUE = "help";
	String CSS_HIDDEN_VALUE = "hidden";
	String CSS_HIDE_VALUE = "hide";
	String CSS_HIGH_VALUE = "high";
	String CSS_HIGHER_VALUE = "higher";
	String CSS_ICON_VALUE = "icon";
	String CSS_INHERIT_VALUE = "inherit";
	String CSS_INLINE_VALUE = "inline";
	String CSS_INLINE_BLOCK_VALUE = "inline-block";
	String CSS_INLINE_TABLE_VALUE = "inline-table";
	String CSS_INSIDE_VALUE = "inside";
	String CSS_INVERT_VALUE = "invert";
	String CSS_ITALIC_VALUE = "italic";
	String CSS_JUSTIFY_VALUE = "justify";
	String CSS_LEFT_SIDE_VALUE = "left-side";
	String CSS_LEFTWARDS_VALUE = "leftwards";
	String CSS_LEVEL_VALUE = "level";
	String CSS_LIGHTER_VALUE = "lighter";
	String CSS_LINE_THROUGH_VALUE = "line-through";
	String CSS_LIST_ITEM_VALUE = "list-item";
	String CSS_LOUD_VALUE = "loud";
	String CSS_LOW_VALUE = "low";
	String CSS_LOWER_VALUE = "lower";
	String CSS_LOWER_ALPHA_VALUE = "lower-alpha";
	String CSS_LOWERCASE_VALUE = "lowercase";
	String CSS_LOWER_GREEK_VALUE = "lower-greek";
	String CSS_LOWER_LATIN_VALUE = "lower-latin";
	String CSS_LOWER_ROMAN_VALUE = "lower-roman";
	String CSS_LTR_VALUE = "ltr";
	String CSS_MEDIUM_VALUE = "medium";
	String CSS_MENU_VALUE = "menu";
	String CSS_MESSAGE_BOX_VALUE = "message-box";
	String CSS_MIDDLE_VALUE = "middle";
	String CSS_MIX_VALUE = "mix";
	String CSS_MOVE_VALUE = "move";
	String CSS_NE_RESIZE_VALUE = "ne-resize";
	String CSS_NO_CLOSE_QUOTE_VALUE = "no-close-quote";
	String CSS_NONE_VALUE = "none";
	String CSS_NO_OPEN_QUOTE_VALUE = "no-open-quote";
	String CSS_NO_REPEAT_VALUE = "no-repeat";
	String CSS_NORMAL_VALUE = "normal";
	String CSS_NOWRAP_VALUE = "nowrap";
	String CSS_N_RESIZE_VALUE = "n-resize";
	String CSS_NW_RESIZE_VALUE = "nw-resize";
	String CSS_OBLIQUE_VALUE = "oblique";
	String CSS_ONCE_VALUE = "once";
	String CSS_OPEN_QUOTE_VALUE = "open-quote";
	String CSS_OUTSIDE_VALUE = "outside";
	String CSS_OVERLINE_VALUE = "overline";
	String CSS_POINTER_VALUE = "pointer";
	String CSS_PRE_VALUE = "pre";
	String CSS_PRE_LINE_VALUE = "pre-line";
	String CSS_PRE_WRAP_VALUE = "pre-wrap";
	String CSS_PROGRESS_VALUE = "progress";
	String CSS_RELATIVE_VALUE = "relative";
	String CSS_REPEAT_VALUE = "repeat";
	String CSS_REPEAT_X_VALUE = "repeat-x";
	String CSS_REPEAT_Y_VALUE = "repeat-y";
	String CSS_RIGHT_VALUE = "right";
	String CSS_RIGHT_SIDE_VALUE = "right-side";
	String CSS_RIGHTWARDS_VALUE = "rightwards";
	String CSS_RTL_VALUE = "rtl";
	String CSS_RUN_IN_VALUE = "run-in";
	String CSS_SEPARATE_VALUE = "separate";
	String CSS_SE_RESIZE_VALUE = "se-resize";
	String CSS_SHOW_VALUE = "show";
	String CSS_SILENT_VALUE = "silent";
	String CSS_SLOW_VALUE = "slow";
	String CSS_SLOWER_VALUE = "slower";
	String CSS_SMALL_CAPS_VALUE = "small-caps";
	String CSS_SMALL_CAPTION_VALUE = "small-caption";
	String CSS_SOFT_VALUE = "soft";
	String CSS_SPELL_OUT_VALUE = "spell-out";
	String CSS_SQUARE_VALUE = "square";
	String CSS_S_RESIZE_VALUE = "s-resize";
	String CSS_STATIC_VALUE = "static";
	String CSS_STATUS_BAR_VALUE = "status-bar";
	String CSS_SUB_VALUE = "sub";
	String CSS_SUPER_VALUE = "super";
	String CSS_SW_RESIZE_VALUE = "sw-resize";
	String CSS_TABLE_VALUE = "table";
	String CSS_TABLE_CAPTION_VALUE = "table-caption";
	String CSS_TABLE_CELL_VALUE = "table-cell";
	String CSS_TABLE_COLUMN_VALUE = "table-column";
	String CSS_TABLE_COLUMN_GROUP_VALUE = "table-column-group";
	String CSS_TABLE_FOOTER_GROUP_VALUE = "table-footer-group";
	String CSS_TABLE_HEADER_GROUP_VALUE = "table-header-group";
	String CSS_TABLE_ROW_VALUE = "table-row";
	String CSS_TABLE_ROW_GROUP_VALUE = "table-row-group";
	String CSS_TEXT_VALUE = "text";
	String CSS_TEXT_BOTTOM_VALUE = "text-bottom";
	String CSS_TEXT_TOP_VALUE = "text-top";
	String CSS_TOP_VALUE = "top";
	String CSS_UNDERLINE_VALUE = "underline";
	String CSS_UPPER_ALPHA_VALUE = "upper-alpha";
	String CSS_UPPERCASE_VALUE = "uppercase";
	String CSS_UPPER_LATIN_VALUE = "upper-latin";
	String CSS_UPPER_ROMAN_VALUE = "upper-roman";
	String CSS_VISIBLE_VALUE = "visible";
	String CSS_WAIT_VALUE = "wait";
	String CSS_W_RESIZE_VALUE = "w-resize";
	String CSS_X_FAST_VALUE = "x-fast";
	String CSS_X_HIGH_VALUE = "x-high";
	String CSS_X_LOUD_VALUE = "x-loud";
	String CSS_X_LOW_VALUE = "x-low";
	String CSS_X_SLOW_VALUE = "x-slow";
	String CSS_X_SOFT_VALUE = "x-soft";

	// absolute fone size
	String CSS_X_SMALL_VALUE = "x-small";
	String CSS_XX_SMALL_VALUE = "xx-small";
	String CSS_SMALL_VALUE = "small";
	String CSS_LARGE_VALUE = "large";
	String CSS_X_LARGE_VALUE = "x-large";
	String CSS_XX_LARGE_VALUE = "xx-large";

	// relative font size
	String CSS_LARGER_VALUE = "larger";
	String CSS_SMALLER_VALUE = "smaller";

	// genric font family
	String CSS_SERIF_VALUE = "serif";
	String CSS_SANS_SERIF_VALUE = "sans-serif";
	String CSS_CURSIVE_VALUE = "cursive";
	String CSS_FANTASY_VALUE = "fantasy";
	String CSS_MONOSPACE_VALUE = "monospace";

	// color
	String CSS_AQUA_VALUE = "aqua";
	String CSS_BLACK_VALUE = "black";
	String CSS_BLUE_VALUE = "blue";
	String CSS_FUCHSIA_VALUE = "fuchsia";
	String CSS_GRAY_VALUE = "gray";
	String CSS_GREEN_VALUE = "green";
	String CSS_LIME_VALUE = "lime";
	String CSS_MAROON_VALUE = "maroon";
	String CSS_NAVY_VALUE = "navy";
	String CSS_OLIVE_VALUE = "olive";
	String CSS_ORANGE_VALUE = "orange";
	String CSS_PURPLE_VALUE = "purple";
	String CSS_RED_VALUE = "red";
	String CSS_SILVER_VALUE = "silver";
	String CSS_TEAL_VALUE = "teal";
	String CSS_WHITE_VALUE = "white";
	String CSS_YELLOW_VALUE = "yellow";

	// System defined color
	String CSS_ACTIVEBORDER_VALUE = "ActiveBorder";
	String CSS_ACTIVECAPTION_VALUE = "ActiveCaption";
	String CSS_APPWORKSPACE_VALUE = "AppWorkspace";
	String CSS_BACKGROUND_VALUE = "Background";
	String CSS_BUTTONFACE_VALUE = "ButtonFace";
	String CSS_BUTTONHIGHLIGHT_VALUE = "ButtonHighlight";
	String CSS_BUTTONSHADOW_VALUE = "ButtonShadow";
	String CSS_BUTTONTEXT_VALUE = "ButtonText";
	String CSS_CAPTIONTEXT_VALUE = "CaptionText";
	String CSS_GRAYTEXT_VALUE = "GrayText";
	String CSS_HIGHLIGHT_VALUE = "Highlight";
	String CSS_HIGHLIGHTTEXT_VALUE = "HighlightText";
	String CSS_INACTIVEBORDER_VALUE = "InactiveBorder";
	String CSS_INACTIVECAPTION_VALUE = "InactiveCaption";
	String CSS_INACTIVECAPTIONTEXT_VALUE = "InactiveCaptionText";
	String CSS_INFOBACKGROUND_VALUE = "InfoBackground";
	String CSS_INFOTEXT_VALUE = "InfoText";
	// String CSS_MENU_VALUE = "Menu";
	String CSS_MENUTEXT_VALUE = "MenuText";
	String CSS_SCROLLBAR_VALUE = "Scrollbar";
	String CSS_THREEDDARKSHADOW_VALUE = "ThreeDDarkShadow";
	String CSS_THREEDFACE_VALUE = "ThreeDFace";
	String CSS_THREEDHIGHLIGHT_VALUE = "ThreeDHighlight";
	String CSS_THREEDLIGHTSHADOW_VALUE = "ThreeDLightShadow";
	String CSS_THREEDSHADOW_VALUE = "ThreeDShadow";
	String CSS_WINDOW_VALUE = "Window";
	String CSS_WINDOWFRAME_VALUE = "WindowFrame";
	String CSS_WINDOWTEXT_VALUE = "WindowText";

	String CSS_DOTTED_VALUE = "dotted";
	String CSS_DASHED_VALUE = "dashed";
	String CSS_SOLID_VALUE = "solid";
	String CSS_DOUBLE_VALUE = "double";
	String CSS_GROOVE_VALUE = "groove";
	String CSS_RIDGE_VALUE = "ridge";
	String CSS_INSET_VALUE = "inset";
	String CSS_OUTSET_VALUE = "outset";

	String CSS_THIN_VALUE = "thin";
	// String CSS_MEDIUM_VALUE = "medium";
	String CSS_THICK_VALUE = "thick";

	String CSS_OVERFLOW_AUTO_VALUE = "auto";
	String CSS_OVERFLOW_VISIBLE_VALUE = "visible";
	String CSS_OVERFLOW_SCROLL_VALUE = "scroll";
	String CSS_OVERFLOW_HIDDEN_VALUE = "hidden";

	// bidi_hcg: Bidi related
	// "rtl" and "ltr" values are already specified above.
	// We preserve here the name for direction defined in CSS specs.
	String CSS_DIRECTION_PROPERTY = "direction";

	// overflow property: auto, visible, scroll, hidden
	String CSS_OVERFLOW_PROPERTY = "overflow";

	String CSS_HEIGHT_PROPERTY = "height";
	String CSS_WIDTH_PROPERTY = "width";

}
