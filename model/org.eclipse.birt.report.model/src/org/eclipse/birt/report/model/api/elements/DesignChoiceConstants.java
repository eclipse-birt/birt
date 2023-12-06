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

package org.eclipse.birt.report.model.api.elements;

/**
 * This class collects commonly-used choice constants. These constants define
 * the internal value of choices for several property choice constants.
 *
 * @see ReportDesignConstants
 */

public interface DesignChoiceConstants {

	// fontFamily
	/** design constant: choice font family */
	String CHOICE_FONT_FAMILY = "fontFamily"; //$NON-NLS-1$

	/** design constant: font family serif */
	String FONT_FAMILY_SERIF = "serif"; //$NON-NLS-1$

	/** design constant: font family sans serif */
	String FONT_FAMILY_SANS_SERIF = "sans-serif"; //$NON-NLS-1$

	/** design constant: font family cursive */
	String FONT_FAMILY_CURSIVE = "cursive"; //$NON-NLS-1$

	/** design constant: font family fantasy */
	String FONT_FAMILY_FANTASY = "fantasy"; //$NON-NLS-1$

	/** design constant: font family monospace */
	String FONT_FAMILY_MONOSPACE = "monospace"; //$NON-NLS-1$


	// fontStyle
	/** design constant: choice font style */
	String CHOICE_FONT_STYLE = "fontStyle"; //$NON-NLS-1$

	/** design constant: font style normal */
	String FONT_STYLE_NORMAL = "normal"; //$NON-NLS-1$

	/** design constant: font style italic */
	String FONT_STYLE_ITALIC = "italic"; //$NON-NLS-1$

	/** design constant: font style oblique */
	String FONT_STYLE_OBLIQUE = "oblique"; //$NON-NLS-1$


	// fontWeight
	/** design constant: choice font weight */
	String CHOICE_FONT_WEIGHT = "fontWeight"; //$NON-NLS-1$

	/** design constant: font weight normal */
	String FONT_WEIGHT_NORMAL = "normal"; //$NON-NLS-1$

	/** design constant: font weight bold */
	String FONT_WEIGHT_BOLD = "bold"; //$NON-NLS-1$

	/** design constant: font weight bolder */
	String FONT_WEIGHT_BOLDER = "bolder"; //$NON-NLS-1$

	/** design constant: font weight lighter */
	String FONT_WEIGHT_LIGHTER = "lighter"; //$NON-NLS-1$

	/** design constant: font weight 100 */
	String FONT_WEIGHT_100 = "100"; //$NON-NLS-1$

	/** design constant: font weight 200 */
	String FONT_WEIGHT_200 = "200"; //$NON-NLS-1$

	/** design constant: font weight 300 */
	String FONT_WEIGHT_300 = "300"; //$NON-NLS-1$

	/** design constant: font weight 400 */
	String FONT_WEIGHT_400 = "400"; //$NON-NLS-1$

	/** design constant: font weight 500 */
	String FONT_WEIGHT_500 = "500"; //$NON-NLS-1$

	/** design constant: font weight 600 */
	String FONT_WEIGHT_600 = "600"; //$NON-NLS-1$

	/** design constant: font weight 700 */
	String FONT_WEIGHT_700 = "700"; //$NON-NLS-1$

	/** design constant: font weight 800 */
	String FONT_WEIGHT_800 = "800"; //$NON-NLS-1$

	/** design constant: font weight 900 */
	String FONT_WEIGHT_900 = "900"; //$NON-NLS-1$


	// fontSize
	/** design constant: choice font size */
	String CHOICE_FONT_SIZE = "fontSize"; //$NON-NLS-1$

	/** design constant: font size xx small */
	String FONT_SIZE_XX_SMALL = "xx-small"; //$NON-NLS-1$

	/** design constant: font size x small */
	String FONT_SIZE_X_SMALL = "x-small"; //$NON-NLS-1$

	/** design constant: font size small */
	String FONT_SIZE_SMALL = "small"; //$NON-NLS-1$

	/** design constant: font size medium */
	String FONT_SIZE_MEDIUM = "medium"; //$NON-NLS-1$

	/** design constant: font size large */
	String FONT_SIZE_LARGE = "large"; //$NON-NLS-1$

	/** design constant: font size x large */
	String FONT_SIZE_X_LARGE = "x-large"; //$NON-NLS-1$

	/** design constant: font size xx large */
	String FONT_SIZE_XX_LARGE = "xx-large"; //$NON-NLS-1$

	/** design constant: font size larger */
	String FONT_SIZE_LARGER = "larger"; //$NON-NLS-1$

	/** design constant: font size smaller */
	String FONT_SIZE_SMALLER = "smaller"; //$NON-NLS-1$


	// fontVariant
	/** design constant: choice font variant */
	String CHOICE_FONT_VARIANT = "fontVariant"; //$NON-NLS-1$

	/** design constant: font variant normal */
	String FONT_VARIANT_NORMAL = "normal"; //$NON-NLS-1$

	/** design constant: font variant small caps */
	String FONT_VARIANT_SMALL_CAPS = "small-caps"; //$NON-NLS-1$


	// backgroundRepeat
	/** design constant: choice background repeat */
	String CHOICE_BACKGROUND_REPEAT = "backgroundRepeat"; //$NON-NLS-1$

	/** design constant: background repeat repeat */
	String BACKGROUND_REPEAT_REPEAT = "repeat"; //$NON-NLS-1$

	/** design constant: background repeat repeat x */
	String BACKGROUND_REPEAT_REPEAT_X = "repeat-x"; //$NON-NLS-1$

	/** design constant: background repeat repeat y */
	String BACKGROUND_REPEAT_REPEAT_Y = "repeat-y"; //$NON-NLS-1$

	/** design constant: background repeat no repeat */
	String BACKGROUND_REPEAT_NO_REPEAT = "no-repeat"; //$NON-NLS-1$


	// backgroundAttachment
	/** design constant: choice background attachment */
	String CHOICE_BACKGROUND_ATTACHMENT = "backgroundAttachment"; //$NON-NLS-1$

	/** design constant: background attachment scroll */
	String BACKGROUND_ATTACHMENT_SCROLL = "scroll"; //$NON-NLS-1$

	/** design constant: background attachment fixed */
	String BACKGROUND_ATTACHMENT_FIXED = "fixed"; //$NON-NLS-1$


	// backgroundPositionX
	/** design constant: choice background position x */
	String CHOICE_BACKGROUND_POSITION_X = "backgroundPositionX"; //$NON-NLS-1$

	/** design constant: background position left */
	String BACKGROUND_POSITION_LEFT = "left"; //$NON-NLS-1$

	/** design constant: background position right */
	String BACKGROUND_POSITION_RIGHT = "right"; //$NON-NLS-1$

	/** design constant: background position center */
	String BACKGROUND_POSITION_CENTER = "center"; //$NON-NLS-1$

	// backgroundPositionY(BACKGROUND_POSITION_CENTER is already defined)

	/** design constant: choice background position y */
	String CHOICE_BACKGROUND_POSITION_Y = "backgroundPositionY"; //$NON-NLS-1$
	/** design constant: background position top */
	String BACKGROUND_POSITION_TOP = "top"; //$NON-NLS-1$

	/** design constant: background position bottom */
	String BACKGROUND_POSITION_BOTTOM = "bottom"; //$NON-NLS-1$


	// backgroundSize
	/** design constant: choice background size */
	String CHOICE_BACKGROUND_SIZE = "backgroundSize"; //$NON-NLS-1$

	/** design constant: background size auto */
	String BACKGROUND_SIZE_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: background size contain */
	String BACKGROUND_SIZE_CONTAIN = "contain"; //$NON-NLS-1$

	/** design constant: background size cover */
	String BACKGROUND_SIZE_COVER = "cover"; //$NON-NLS-1$


	// transform
	/** design constant: choice transform */
	String CHOICE_TRANSFORM = "transform"; //$NON-NLS-1$

	/** design constant: transform capitalize */
	String TRANSFORM_CAPITALIZE = "capitalize"; //$NON-NLS-1$

	/** design constant: transform uppercase */
	String TRANSFORM_UPPERCASE = "uppercase"; //$NON-NLS-1$

	/** design constant: transform lowercase */
	String TRANSFORM_LOWERCASE = "lowercase"; //$NON-NLS-1$

	/** design constant: transform none */
	String TRANSFORM_NONE = "none"; //$NON-NLS-1$


	// normal
	/** design constant: choice normal */
	String CHOICE_NORMAL = "normal"; //$NON-NLS-1$

	/** design constant: normal normal */
	String NORMAL_NORMAL = "normal"; //$NON-NLS-1$


	// verticalAlign
	/** design constant: choice vertical align */
	String CHOICE_VERTICAL_ALIGN = "verticalAlign"; //$NON-NLS-1$


	/**
	 * @deprecated Now Engine is not support it.
	 */
	@Deprecated
	/** design constant: vertical align baseline */
	String VERTICAL_ALIGN_BASELINE = "baseline"; //$NON-NLS-1$


	/**
	 * @deprecated Now Engine is not support it.
	 */
	@Deprecated
	/** design constant: vertical align sub */
	String VERTICAL_ALIGN_SUB = "sub"; //$NON-NLS-1$


	/**
	 * @deprecated Now Engine is not support it.
	 */
	@Deprecated
	/** design constant: vertical align super */
	String VERTICAL_ALIGN_SUPER = "super"; //$NON-NLS-1$

	/** design constant: vertical align top */
	String VERTICAL_ALIGN_TOP = "top"; //$NON-NLS-1$


	/**
	 * @deprecated Now Engine is not support it.
	 */
	@Deprecated
	/** design constant: vertical align text top */
	String VERTICAL_ALIGN_TEXT_TOP = "text-top"; //$NON-NLS-1$

	/** design constant: vertical align middle */
	String VERTICAL_ALIGN_MIDDLE = "middle"; //$NON-NLS-1$

	/** design constant: vertical align bottom */
	String VERTICAL_ALIGN_BOTTOM = "bottom"; //$NON-NLS-1$


	/**
	 * @deprecated Now Engine is not support it.
	 */
	@Deprecated
	/** design constant: vertical align text bottom */
	String VERTICAL_ALIGN_TEXT_BOTTOM = "text-bottom"; //$NON-NLS-1$


	// whiteSpace
	/** design constant: choice white space */
	String CHOICE_WHITE_SPACE = "whiteSpace"; //$NON-NLS-1$

	/** design constant: white space normal */
	String WHITE_SPACE_NORMAL = "normal"; //$NON-NLS-1$

	/** design constant: white space pre */
	String WHITE_SPACE_PRE = "pre"; //$NON-NLS-1$

	/** design constant: white space nowrap */
	String WHITE_SPACE_NOWRAP = "nowrap"; //$NON-NLS-1$


	// display
	/** design constant: choice display */
	String CHOICE_DISPLAY = "display"; //$NON-NLS-1$

	/** design constant: display block */
	String DISPLAY_BLOCK = "block"; //$NON-NLS-1$

	/** design constant: display inline */
	String DISPLAY_INLINE = "inline"; //$NON-NLS-1$

	/** design constant: display none */
	String DISPLAY_NONE = "none"; //$NON-NLS-1$


	// pageBreakAfter
	/** design constant: choice page break after */
	String CHOICE_PAGE_BREAK_AFTER = "pageBreakAfter"; //$NON-NLS-1$

	/** design constant: page break after auto */
	String PAGE_BREAK_AFTER_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: page break after always */
	String PAGE_BREAK_AFTER_ALWAYS = "always"; //$NON-NLS-1$

	/** design constant: page break after avoid */
	String PAGE_BREAK_AFTER_AVOID = "avoid"; //$NON-NLS-1$

	/** design constant: page break after always excluding last */
	String PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST = "always-excluding-last"; //$NON-NLS-1$


	// pageBreakBefore
	/** design constant: choice page break before */
	String CHOICE_PAGE_BREAK_BEFORE = "pageBreakBefore"; //$NON-NLS-1$

	/** design constant: page break before auto */
	String PAGE_BREAK_BEFORE_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: page break before always */
	String PAGE_BREAK_BEFORE_ALWAYS = "always"; //$NON-NLS-1$

	/** design constant: page break before avoid */
	String PAGE_BREAK_BEFORE_AVOID = "avoid"; //$NON-NLS-1$

	/** design constant: page break before always excluding first */
	String PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST = "always-excluding-first"; //$NON-NLS-1$


	// pageBreakInside
	/** design constant: choice page break inside */
	String CHOICE_PAGE_BREAK_INSIDE = "pageBreakInside"; //$NON-NLS-1$

	/** design constant: page break inside avoid */
	String PAGE_BREAK_INSIDE_AVOID = "avoid"; //$NON-NLS-1$

	/** design constant: page break inside auto */
	String PAGE_BREAK_INSIDE_AUTO = "auto"; //$NON-NLS-1$


	// margin
	/** design constant: choice margin */
	String CHOICE_MARGIN = "margin"; //$NON-NLS-1$
	/** design constant: margin auto */
	String MARGIN_AUTO = "auto"; //$NON-NLS-1$

	// textUnderline
	/** design constant: choice text underline */
	String CHOICE_TEXT_UNDERLINE = "textUnderline"; //$NON-NLS-1$

	/** design constant: text underline none */
	String TEXT_UNDERLINE_NONE = "none"; //$NON-NLS-1$

	/** design constant: text underline underline */
	String TEXT_UNDERLINE_UNDERLINE = "underline"; //$NON-NLS-1$


	// textOverline
	/** design constant: choice text overline */
	String CHOICE_TEXT_OVERLINE = "textOverline"; //$NON-NLS-1$

	/** design constant: text overline none */
	String TEXT_OVERLINE_NONE = "none"; //$NON-NLS-1$

	/** design constant: text overline overline */
	String TEXT_OVERLINE_OVERLINE = "overline"; //$NON-NLS-1$


	// textLineThrough
	/** design constant: choice text line through */
	String CHOICE_TEXT_LINE_THROUGH = "textLineThrough"; //$NON-NLS-1$

	/** design constant: text line through none */
	String TEXT_LINE_THROUGH_NONE = "none"; //$NON-NLS-1$

	/** design constant: text line through line through */
	String TEXT_LINE_THROUGH_LINE_THROUGH = "line-through"; //$NON-NLS-1$


	// lineWidth
	/** design constant: choice line width */
	String CHOICE_LINE_WIDTH = "lineWidth"; //$NON-NLS-1$

	/** design constant: line width thin */
	String LINE_WIDTH_THIN = "thin"; //$NON-NLS-1$

	/** design constant: line width medium */
	String LINE_WIDTH_MEDIUM = "medium"; //$NON-NLS-1$

	/** design constant: line width thick */
	String LINE_WIDTH_THICK = "thick"; //$NON-NLS-1$


	// lineStyle
	/** design constant: choice line style */
	String CHOICE_LINE_STYLE = "lineStyle"; //$NON-NLS-1$

	/** design constant: line style none */
	String LINE_STYLE_NONE = "none"; //$NON-NLS-1$

	/** design constant: line style solid */
	String LINE_STYLE_SOLID = "solid"; //$NON-NLS-1$

	/** design constant: line style dotted */
	String LINE_STYLE_DOTTED = "dotted"; //$NON-NLS-1$

	/** design constant: line style dashed */
	String LINE_STYLE_DASHED = "dashed"; //$NON-NLS-1$

	/** design constant: line style double */
	String LINE_STYLE_DOUBLE = "double"; //$NON-NLS-1$

	/** design constant: line style groove */
	String LINE_STYLE_GROOVE = "groove"; //$NON-NLS-1$

	/** design constant: line style ridge */
	String LINE_STYLE_RIDGE = "ridge"; //$NON-NLS-1$

	/** design constant: line style inset */
	String LINE_STYLE_INSET = "inset"; //$NON-NLS-1$

	/** design constant: line style outset */
	String LINE_STYLE_OUTSET = "outset"; //$NON-NLS-1$

	// units
	/** design constant: choice units */
	String CHOICE_UNITS = "units"; //$NON-NLS-1$

	/** design constant: units in */
	String UNITS_IN = "in"; //$NON-NLS-1$

	/** design constant: units cm */
	String UNITS_CM = "cm"; //$NON-NLS-1$

	/** design constant: units mm */
	String UNITS_MM = "mm"; //$NON-NLS-1$

	/** design constant: units pt */
	String UNITS_PT = "pt"; //$NON-NLS-1$

	/** design constant: units pc */
	String UNITS_PC = "pc"; //$NON-NLS-1$

	/** design constant: units em */
	String UNITS_EM = "em"; //$NON-NLS-1$

	/** design constant: units ex */
	String UNITS_EX = "ex"; //$NON-NLS-1$

	/** design constant: units px */
	String UNITS_PX = "px"; //$NON-NLS-1$

	/** design constant: units percentage */
	String UNITS_PERCENTAGE = "%"; //$NON-NLS-1$


	// paramType
	/** design constant: choice param type */
	String CHOICE_PARAM_TYPE = "paramType"; //$NON-NLS-1$

	/** design constant: param type string */
	String PARAM_TYPE_STRING = "string"; //$NON-NLS-1$

	/** design constant: param type float */
	String PARAM_TYPE_FLOAT = "float"; //$NON-NLS-1$

	/** design constant: param type decimal */
	String PARAM_TYPE_DECIMAL = "decimal"; //$NON-NLS-1$

	/** design constant: param type datetime */
	String PARAM_TYPE_DATETIME = "dateTime"; //$NON-NLS-1$

	/** design constant: param type boolean */
	String PARAM_TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$

	/** design constant: param type integer */
	String PARAM_TYPE_INTEGER = "integer"; //$NON-NLS-1$

	/** design constant: param type java object */
	String PARAM_TYPE_JAVA_OBJECT = "javaObject"; //$NON-NLS-1$

	/**
	 * @deprecated Now any is not supported.
	 */
	@Deprecated
	/** design constant: param type any */
	String PARAM_TYPE_ANY = "any"; //$NON-NLS-1$

	/** design constant: param type date */
	String PARAM_TYPE_DATE = "date"; //$NON-NLS-1$

	/** design constant: param type time */
	String PARAM_TYPE_TIME = "time"; //$NON-NLS-1$

	// paramValueType
	/** design constant: choice param value type */
	String CHOICE_PARAM_VALUE_TYPE = "paramType"; //$NON-NLS-1$

	/** design constant: param value type static */
	String PARAM_VALUE_TYPE_STATIC = "static"; //$NON-NLS-1$

	/** design constant: param value type dynamic */
	String PARAM_VALUE_TYPE_DYNAMIC = "dynamic"; //$NON-NLS-1$


	// paramControl
	/** design constant: choice param control */
	String CHOICE_PARAM_CONTROL = "paramControl"; //$NON-NLS-1$

	/** design constant: param control text box */
	String PARAM_CONTROL_TEXT_BOX = "text-box"; //$NON-NLS-1$

	/** design constant: param control list box */
	String PARAM_CONTROL_LIST_BOX = "list-box"; //$NON-NLS-1$

	/** design constant: param control radio button */
	String PARAM_CONTROL_RADIO_BUTTON = "radio-button"; //$NON-NLS-1$

	/** design constant: param control check box */
	String PARAM_CONTROL_CHECK_BOX = "check-box"; //$NON-NLS-1$

	/** design constant: param control auto suggest */
	String PARAM_CONTROL_AUTO_SUGGEST = "auto-suggest"; //$NON-NLS-1$


	// textAlign
	/** design constant: choice text align */
	String CHOICE_TEXT_ALIGN = "textAlign"; //$NON-NLS-1$

	/** design constant: text align left */
	String TEXT_ALIGN_LEFT = "left"; //$NON-NLS-1$

	/** design constant: text align center */
	String TEXT_ALIGN_CENTER = "center"; //$NON-NLS-1$

	/** design constant: text align right */
	String TEXT_ALIGN_RIGHT = "right"; //$NON-NLS-1$

	/** design constant: text align justify */
	String TEXT_ALIGN_JUSTIFY = "justify"; //$NON-NLS-1$


	// pageSize
	/** design constant: choice page size */
	String CHOICE_PAGE_SIZE = "pageSize"; //$NON-NLS-1$

	/** design constant: page size custom */
	String PAGE_SIZE_CUSTOM = "custom"; //$NON-NLS-1$

	/** design constant: page size us letter */
	String PAGE_SIZE_US_LETTER = "us-letter"; //$NON-NLS-1$

	/** design constant: page size us legal */
	String PAGE_SIZE_US_LEGAL = "us-legal"; //$NON-NLS-1$

	/** design constant: page size a4 */
	String PAGE_SIZE_A4 = "a4"; //$NON-NLS-1$

	/** design constant: page size a3 */
	String PAGE_SIZE_A3 = "a3"; //$NON-NLS-1$

	/** design constant: page size a5 */
	String PAGE_SIZE_A5 = "a5"; //$NON-NLS-1$

	/** design constant: page size us ledger */
	String PAGE_SIZE_US_LEDGER = "us-ledger"; //$NON-NLS-1$

	/** design constant: page size us super b */
	String PAGE_SIZE_US_SUPER_B = "us-super-b"; //$NON-NLS-1$


	// pageOrientation
	/** design constant: choice page orientation */
	String CHOICE_PAGE_ORIENTATION = "pageOrientation"; //$NON-NLS-1$

	/** design constant: page orientation auto */
	String PAGE_ORIENTATION_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: page orientation portrait */
	String PAGE_ORIENTATION_PORTRAIT = "portrait"; //$NON-NLS-1$

	/** design constant: page orientation landscape */
	String PAGE_ORIENTATION_LANDSCAPE = "landscape"; //$NON-NLS-1$


	// interval
	/** design constant: choice interval */
	String CHOICE_INTERVAL = "interval"; //$NON-NLS-1$

	/** design constant: interval none */
	String INTERVAL_NONE = "none"; //$NON-NLS-1$

	/** design constant: interval prefix */
	String INTERVAL_PREFIX = "prefix"; //$NON-NLS-1$

	/** design constant: interval year */
	String INTERVAL_YEAR = "year"; //$NON-NLS-1$

	/** design constant: interval quarter */
	String INTERVAL_QUARTER = "quarter"; //$NON-NLS-1$

	/** design constant: interval month */
	String INTERVAL_MONTH = "month"; //$NON-NLS-1$

	/** design constant: interval week */
	String INTERVAL_WEEK = "week"; //$NON-NLS-1$

	/** design constant: interval day */
	String INTERVAL_DAY = "day"; //$NON-NLS-1$

	/** design constant: interval hour */
	String INTERVAL_HOUR = "hour"; //$NON-NLS-1$

	/** design constant: interval minute */
	String INTERVAL_MINUTE = "minute"; //$NON-NLS-1$

	/** design constant: interval second */
	String INTERVAL_SECOND = "second"; //$NON-NLS-1$

	/** design constant: interval interval */
	String INTERVAL_INTERVAL = "interval"; //$NON-NLS-1$


	// intervalType
	/** design constant: choice interval type */
	String CHOICE_INTERVAL_TYPE = "intervalType"; //$NON-NLS-1$

	/** design constant: interval type none */
	String INTERVAL_TYPE_NONE = "none"; //$NON-NLS-1$

	/** design constant: interval type prefix */
	String INTERVAL_TYPE_PREFIX = "prefix"; //$NON-NLS-1$

	/** design constant: interval type interval */
	String INTERVAL_TYPE_INTERVAL = "interval"; //$NON-NLS-1$


	// sortDirection
	/** design constant: choice sort direction */
	String CHOICE_SORT_DIRECTION = "sortDirection"; //$NON-NLS-1$

	/** design constant: sort direction asc */
	String SORT_DIRECTION_ASC = "asc"; //$NON-NLS-1$

	/** design constant: sort direction desc */
	String SORT_DIRECTION_DESC = "desc"; //$NON-NLS-1$


	// mapOperator
	/** design constant: choice map operator */
	String CHOICE_MAP_OPERATOR = "mapOperator"; //$NON-NLS-1$

	/** design constant: map operator eq */
	String MAP_OPERATOR_EQ = "eq"; //$NON-NLS-1$

	/** design constant: map operator ne */
	String MAP_OPERATOR_NE = "ne"; //$NON-NLS-1$

	/** design constant: map operator lt */
	String MAP_OPERATOR_LT = "lt"; //$NON-NLS-1$

	/** design constant: map operator le */
	String MAP_OPERATOR_LE = "le"; //$NON-NLS-1$

	/** design constant: map operator ge */
	String MAP_OPERATOR_GE = "ge"; //$NON-NLS-1$

	/** design constant: map operator gt */
	String MAP_OPERATOR_GT = "gt"; //$NON-NLS-1$

	/** design constant: map operator between */
	String MAP_OPERATOR_BETWEEN = "between"; //$NON-NLS-1$

	/** design constant: map operator not between */
	String MAP_OPERATOR_NOT_BETWEEN = "not-between"; //$NON-NLS-1$

	/** design constant: map operator null */
	String MAP_OPERATOR_NULL = "is-null"; //$NON-NLS-1$

	/** design constant: map operator not null */
	String MAP_OPERATOR_NOT_NULL = "is-not-null"; //$NON-NLS-1$

	/** design constant: map operator true */
	String MAP_OPERATOR_TRUE = "is-true"; //$NON-NLS-1$

	/** design constant: map operator false */
	String MAP_OPERATOR_FALSE = "is-false"; //$NON-NLS-1$

	/** design constant: map operator like */
	String MAP_OPERATOR_LIKE = "like"; //$NON-NLS-1$

	/** design constant: map operator match */
	String MAP_OPERATOR_MATCH = "match"; //$NON-NLS-1$

	/** design constant: map operator top n */
	String MAP_OPERATOR_TOP_N = "top-n"; //$NON-NLS-1$

	/** design constant: map operator bottom n */
	String MAP_OPERATOR_BOTTOM_N = "bottom-n"; //$NON-NLS-1$

	/** design constant: map operator top percent */
	String MAP_OPERATOR_TOP_PERCENT = "top-percent"; //$NON-NLS-1$

	/** design constant: map operator bottom percent */
	String MAP_OPERATOR_BOTTOM_PERCENT = "bottom-percent"; //$NON-NLS-1$

	/** design constant: map operator not like */
	String MAP_OPERATOR_NOT_LIKE = "not-like"; //$NON-NLS-1$

	/** design constant: map operator not match */
	String MAP_OPERATOR_NOT_MATCH = "not-match"; //$NON-NLS-1$

	/** design constant: map operator any */
	String MAP_OPERATOR_ANY = "any"; //$NON-NLS-1$

	/** design constant: map operator not in */
	String MAP_OPERATOR_NOT_IN = "not-in"; //$NON-NLS-1$

	/** design constant: map operator in */
	String MAP_OPERATOR_IN = "in"; //$NON-NLS-1$


	// imageSize
	/** design constant: choice image size */
	String CHOICE_IMAGE_SIZE = "imageSize"; //$NON-NLS-1$

	/** design constant: image size size to image */
	String IMAGE_SIZE_SIZE_TO_IMAGE = "size-to-image"; //$NON-NLS-1$

	/** design constant: image size scale to item */
	String IMAGE_SIZE_SCALE_TO_ITEM = "scale-to-item"; //$NON-NLS-1$

	/** design constant: image size clip */
	String IMAGE_SIZE_CLIP = "clip"; //$NON-NLS-1$


	// lineOrientation
	/** design constant: choice line orientation */
	String CHOICE_LINE_ORIENTATION = "lineOrientation"; //$NON-NLS-1$

	/** design constant: line orientation horizontal */
	String LINE_ORIENTATION_HORIZONTAL = "horizontal"; //$NON-NLS-1$

	/** design constant: line orientation vertical */
	String LINE_ORIENTATION_VERTICAL = "vertical"; //$NON-NLS-1$


	// sectionAlign
	/** design constant: choice section align */
	String CHOICE_SECTION_ALIGN = "sectionAlign"; //$NON-NLS-1$

	/** design constant: section align left */
	String SECTION_ALIGN_LEFT = "left"; //$NON-NLS-1$

	/** design constant: section align center */
	String SECTION_ALIGN_CENTER = "center"; //$NON-NLS-1$

	/** design constant: section align right */
	String SECTION_ALIGN_RIGHT = "right"; //$NON-NLS-1$


	// dropType
	/** design constant: choice drop type */
	String CHOICE_DROP_TYPE = "dropType"; //$NON-NLS-1$

	/** design constant: drop type none */
	String DROP_TYPE_NONE = "none"; //$NON-NLS-1$

	/** design constant: drop type detail */
	String DROP_TYPE_DETAIL = "detail"; //$NON-NLS-1$

	/** design constant: drop type all */
	String DROP_TYPE_ALL = "all"; //$NON-NLS-1$


	// imageType
	/** design constant: choice image type */
	String CHOICE_IMAGE_TYPE = "imageType"; //$NON-NLS-1$

	/** design constant: image type image bmp */
	String IMAGE_TYPE_IMAGE_BMP = "image/bmp"; //$NON-NLS-1$

	/** design constant: image type image gif */
	String IMAGE_TYPE_IMAGE_GIF = "image/gif"; //$NON-NLS-1$

	/** design constant: image type image png */
	String IMAGE_TYPE_IMAGE_PNG = "image/png"; //$NON-NLS-1$

	/** design constant: image type image x png */
	String IMAGE_TYPE_IMAGE_X_PNG = "image/x-png"; //$NON-NLS-1$

	/** design constant: image type image jpeg */
	String IMAGE_TYPE_IMAGE_JPEG = "image/jpeg"; //$NON-NLS-1$

	/** design constant: image type image ico */
	String IMAGE_TYPE_IMAGE_ICO = "image/ico"; //$NON-NLS-1$

	/** design constant: image type image tiff */
	String IMAGE_TYPE_IMAGE_TIFF = "image/tiff"; //$NON-NLS-1$

	/** design constant: image type image svg */
	String IMAGE_TYPE_IMAGE_SVG = "image/svg+xml"; //$NON-NLS-1$

	/** design constant: image type image x icon */
	String IMAGE_TYPE_IMAGE_X_ICON = "image/x-icon"; //$NON-NLS-1$


	// lineSpacing
	/** design constant: choice line spacing */
	String CHOICE_LINE_SPACING = "lineSpacing"; //$NON-NLS-1$

	/** design constant: line spacing lines */
	String LINE_SPACING_LINES = "lines"; //$NON-NLS-1$

	/** design constant: line spacing exact */
	String LINE_SPACING_EXACT = "exact"; //$NON-NLS-1$


	// actionLinkTyp
	/** design constant: choice action link type */
	String CHOICE_ACTION_LINK_TYPE = "actionLinkType"; //$NON-NLS-1$

	/** design constant: action link type none */
	String ACTION_LINK_TYPE_NONE = "none"; //$NON-NLS-1$

	/** design constant: action link type hyperlink */
	String ACTION_LINK_TYPE_HYPERLINK = "hyperlink"; //$NON-NLS-1$

	/** design constant: action link type drill through */
	String ACTION_LINK_TYPE_DRILL_THROUGH = "drill-through"; //$NON-NLS-1$

	/** design constant: action link type bookmark link */
	String ACTION_LINK_TYPE_BOOKMARK_LINK = "bookmark-link"; //$NON-NLS-1$


	// actionFormatType
	/** design constant: choice action format type */
	String CHOICE_ACTION_FORMAT_TYPE = "actionFormatType"; //$NON-NLS-1$

	/** design constant: action format type html */
	String ACTION_FORMAT_TYPE_HTML = "html"; //$NON-NLS-1$

	/** design constant: action format type pdf */
	String ACTION_FORMAT_TYPE_PDF = "pdf"; //$NON-NLS-1$


	// ContentType for TextItem
	/** design constant: choice text content type */
	String CHOICE_TEXT_CONTENT_TYPE = "textContentType"; //$NON-NLS-1$

	/** design constant: text content type auto */
	String TEXT_CONTENT_TYPE_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: text content type plain */
	String TEXT_CONTENT_TYPE_PLAIN = "plain"; //$NON-NLS-1$

	/** design constant: text content type html */
	String TEXT_CONTENT_TYPE_HTML = "html"; //$NON-NLS-1$

	/** design constant: text content type rtf */
	String TEXT_CONTENT_TYPE_RTF = "rtf"; //$NON-NLS-1$


	// ContentType for TextDataItem
	/** design constant: choice text data content type */
	String CHOICE_TEXT_DATA_CONTENT_TYPE = "textDataContentType"; //$NON-NLS-1$

	/** design constant: text data content type auto */
	String TEXT_DATA_CONTENT_TYPE_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: text data content type plain */
	String TEXT_DATA_CONTENT_TYPE_PLAIN = "plain"; //$NON-NLS-1$

	/** design constant: text data content type html */
	String TEXT_DATA_CONTENT_TYPE_HTML = "html"; //$NON-NLS-1$

	/** design constant: text data content type rtf */
	String TEXT_DATA_CONTENT_TYPE_RTF = "rtf"; //$NON-NLS-1$


	// Pagination orphans
	/** design constant: choice oprhans */
	String CHOICE_OPRHANS = "orphans"; //$NON-NLS-1$

	/** design constant: orphans inherit */
	String ORPHANS_INHERIT = "inherit"; //$NON-NLS-1$


	// Pagination widows
	/** design constant: choice widows */
	String CHOICE_WIDOWS = "widows"; //$NON-NLS-1$

	/** design constant: widows inherit */
	String WIDOWS_INHERIT = "inherit"; //$NON-NLS-1$


	/**
	 * FormatType -- The target output format. The first constant is the name of
	 * FormatType choice. The followed constants are valid choice values of
	 * FormatType.
	 */
	/** design constant: choice format type */
	String CHOICE_FORMAT_TYPE = "formatType"; //$NON-NLS-1$

	/** design constant: format type all */
	String FORMAT_TYPE_ALL = "all"; //$NON-NLS-1$

	/** design constant: format type viewer */
	String FORMAT_TYPE_VIEWER = "viewer"; //$NON-NLS-1$

	/** design constant: format type email */
	String FORMAT_TYPE_EMAIL = "email"; //$NON-NLS-1$

	/** design constant: format type print */
	String FORMAT_TYPE_PRINT = "print"; //$NON-NLS-1$

	/** design constant: format type pdf */
	String FORMAT_TYPE_PDF = "pdf"; //$NON-NLS-1$

	/** design constant: format type rtf */
	String FORMAT_TYPE_RTF = "rtf"; //$NON-NLS-1$

	/** design constant: format type reportlet */
	String FORMAT_TYPE_REPORTLET = "reportlet"; //$NON-NLS-1$

	/** design constant: format type excel */
	String FORMAT_TYPE_EXCEL = "excel"; //$NON-NLS-1$

	/** design constant: format type word */
	String FORMAT_TYPE_WORD = "word"; //$NON-NLS-1$

	/** design constant: format type powerpoint */
	String FORMAT_TYPE_POWERPOINT = "powerpoint"; //$NON-NLS-1$

	/** design constant: format type doc */
	String FORMAT_TYPE_DOC = "doc"; //$NON-NLS-1$


	/**
	 * ImageRefType -- The image reference type. The first constant is the name of
	 * ImageRefType choice. The followed constants are valid choice values of
	 * ImageRefType.
	 */
	/** design constant: choice image ref type */
	String CHOICE_IMAGE_REF_TYPE = "imageRefType"; //$NON-NLS-1$

	/** design constant: image ref type none */
	String IMAGE_REF_TYPE_NONE = "none"; //$NON-NLS-1$

	/** design constant: image ref type file */
	String IMAGE_REF_TYPE_FILE = "file"; //$NON-NLS-1$

	/** design constant: image ref type url */
	String IMAGE_REF_TYPE_URL = "url"; //$NON-NLS-1$

	/** design constant: image ref type expr */
	String IMAGE_REF_TYPE_EXPR = "expr"; //$NON-NLS-1$

	/** design constant: image ref type embed */
	String IMAGE_REF_TYPE_EMBED = "embed"; //$NON-NLS-1$


	/**
	 * propertyMaskType -- The choice for the property mask. The first constant is
	 * the name of propertyMaskType choice. The followed constants are valid choice
	 * values of propertyMaskType.
	 */
	/** design constant: choice property mask type */
	String CHOICE_PROPERTY_MASK_TYPE = "propertyMaskType"; //$NON-NLS-1$

	/** design constant: property mask type change */
	String PROPERTY_MASK_TYPE_CHANGE = "change"; //$NON-NLS-1$

	/** design constant: property mask type lock */
	String PROPERTY_MASK_TYPE_LOCK = "lock"; //$NON-NLS-1$

	/** design constant: property mask type hide */
	String PROPERTY_MASK_TYPE_HIDE = "hide"; //$NON-NLS-1$


	/**
	 * scalarParamAlign -- The choice for the scalarParamter alignment. The first
	 * constant is the name of scalarParamAlign choice. The followed constants are
	 * valid choice values of scalarParamAlign.
	 */
	/** design constant: choice scalar param align */
	String CHOICE_SCALAR_PARAM_ALIGN = "scalarParamAlign"; //$NON-NLS-1$

	/** design constant: scalar param align auto */
	String SCALAR_PARAM_ALIGN_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: scalar param align left */
	String SCALAR_PARAM_ALIGN_LEFT = "left"; //$NON-NLS-1$

	/** design constant: scalar param align center */
	String SCALAR_PARAM_ALIGN_CENTER = "center"; //$NON-NLS-1$

	/** design constant: scalar param align right */
	String SCALAR_PARAM_ALIGN_RIGHT = "right"; //$NON-NLS-1$


	/*
	 *
	 * columnDataType -- The column data type The first constant is the name of
	 * columnDataType choice. The followed constants are valid choice values of
	 * columnDataType.
	 *
	 */
	/** design constant: choice column data type */
	String CHOICE_COLUMN_DATA_TYPE = "columnDataType"; //$NON-NLS-1$

	/**
	 * @deprecated this choice is removed since 2.3
	 */
	@Deprecated
	/** design constant: column data type any */
	String COLUMN_DATA_TYPE_ANY = "any"; //$NON-NLS-1$

	/** design constant: column data type integer */
	String COLUMN_DATA_TYPE_INTEGER = "integer"; //$NON-NLS-1$

	/** design constant: column data type string */
	String COLUMN_DATA_TYPE_STRING = "string"; //$NON-NLS-1$

	/** design constant: column data type datetime */
	String COLUMN_DATA_TYPE_DATETIME = "date-time"; //$NON-NLS-1$

	/** design constant: column data type decimal */
	String COLUMN_DATA_TYPE_DECIMAL = "decimal"; //$NON-NLS-1$

	/** design constant: column data type float */
	String COLUMN_DATA_TYPE_FLOAT = "float"; //$NON-NLS-1$

	/** design constant: column data type boolean */
	String COLUMN_DATA_TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$

	/** design constant: column data type date */
	String COLUMN_DATA_TYPE_DATE = "date";//$NON-NLS-1$

	/** design constant: column data type time */
	String COLUMN_DATA_TYPE_TIME = "time";//$NON-NLS-1$

	/** design constant: column data type blob */
	String COLUMN_DATA_TYPE_BLOB = "blob";//$NON-NLS-1$

	/** design constant: column data type java object */
	String COLUMN_DATA_TYPE_JAVA_OBJECT = "javaObject";//$NON-NLS-1$


	/**
	 * searchType -- The search type for column hint The first constant is the name
	 * of searchType choice. The followed constants are valid choice values of
	 * searchType.
	 */
	/** design constant: choice search type */
	String CHOICE_SEARCH_TYPE = "searchType"; //$NON-NLS-1$

	/** design constant: search type any */
	String SEARCH_TYPE_ANY = "any"; //$NON-NLS-1$

	/** design constant: search type indexed */
	String SEARCH_TYPE_INDEXED = "indexed"; //$NON-NLS-1$

	/** design constant: search type none */
	String SEARCH_TYPE_NONE = "none"; //$NON-NLS-1$

	/**
	 * exportType -- The export type for column hint The first constant is the name
	 * of exportType choice. The followed constants are valid choice values of
	 * exportType.
	 */
	/** design constant: choice export type */
	String CHOICE_EXPORT_TYPE = "exportType"; //$NON-NLS-1$

	/** design constant: export type none */
	String EXPORT_TYPE_NONE = "none"; //$NON-NLS-1$

	/** design constant: export type if realized */
	String EXPORT_TYPE_IF_REALIZED = "if-realized"; //$NON-NLS-1$

	/** design constant: export type always */
	String EXPORT_TYPE_ALWAYS = "always"; //$NON-NLS-1$


	/**
	 * analysisType -- The analysis type for column hint The first constant is the
	 * name of analysisType choice. The followed constants are valid choice values
	 * of analysisType.
	 */
	/** design constant: choice analysis type */
	String CHOICE_ANALYSIS_TYPE = "analysisType"; //$NON-NLS-1$


	/**
	 * @deprecated in 2.5.1: this choice is not supported
	 */
	@Deprecated
	/** design constant: analysis type auto */
	String ANALYSIS_TYPE_AUTO = "auto"; //$NON-NLS-1$

	/** design constant: analysis type dimension */
	String ANALYSIS_TYPE_DIMENSION = "dimension"; //$NON-NLS-1$

	/** design constant: analysis type attribute */
	String ANALYSIS_TYPE_ATTRIBUTE = "attribute"; //$NON-NLS-1$

	/** design constant: analysis type measure */
	String ANALYSIS_TYPE_MEASURE = "measure"; //$NON-NLS-1$

	/**
	 * @deprecated in 2.5.1: this choice is not supported
	 */
	@Deprecated
	/** design constant: analysis type detail */
	String ANALYSIS_TYPE_DETAIL = "detail"; //$NON-NLS-1$


	/**
	 * @deprecated in 2.5.1: this choice is not supported
	 */
	@Deprecated
	/** design constant: analysis type none */
	String ANALYSIS_TYPE_NONE = "none"; //$NON-NLS-1$


	/**
	 * filterOperator -- The filter operator for filter condition The first constant
	 * is the name of filterOperator choice. The followed constants are valid choice
	 * values of filterOperator.
	 */
	/** design constant: choice filter operator */
	String CHOICE_FILTER_OPERATOR = "filterOperator"; //$NON-NLS-1$

	/** design constant: filter operator eq */
	String FILTER_OPERATOR_EQ = "eq"; //$NON-NLS-1$

	/** design constant: filter operator ne */
	String FILTER_OPERATOR_NE = "ne"; //$NON-NLS-1$

	/** design constant: filter operator lt */
	String FILTER_OPERATOR_LT = "lt"; //$NON-NLS-1$

	/** design constant: filter operator le */
	String FILTER_OPERATOR_LE = "le"; //$NON-NLS-1$

	/** design constant: filter operator ge */
	String FILTER_OPERATOR_GE = "ge"; //$NON-NLS-1$

	/** design constant: filter operator gt */
	String FILTER_OPERATOR_GT = "gt"; //$NON-NLS-1$

	/** design constant: filter operator between */
	String FILTER_OPERATOR_BETWEEN = "between"; //$NON-NLS-1$

	/** design constant: filter operator not between */
	String FILTER_OPERATOR_NOT_BETWEEN = "not-between"; //$NON-NLS-1$

	/** design constant: filter operator null */
	String FILTER_OPERATOR_NULL = "is-null"; //$NON-NLS-1$

	/** design constant: filter operator not null */
	String FILTER_OPERATOR_NOT_NULL = "is-not-null"; //$NON-NLS-1$

	/** design constant: filter operator true */
	String FILTER_OPERATOR_TRUE = "is-true"; //$NON-NLS-1$

	/** design constant: filter operator false */
	String FILTER_OPERATOR_FALSE = "is-false"; //$NON-NLS-1$

	/** design constant: filter operator like */
	String FILTER_OPERATOR_LIKE = "like"; //$NON-NLS-1$

	/** design constant: filter operator top n */
	String FILTER_OPERATOR_TOP_N = "top-n"; //$NON-NLS-1$

	/** design constant: filter operator bottom n */
	String FILTER_OPERATOR_BOTTOM_N = "bottom-n"; //$NON-NLS-1$

	/** design constant: filter operator top percent */
	String FILTER_OPERATOR_TOP_PERCENT = "top-percent"; //$NON-NLS-1$

	/** design constant: filter operator bottom percent */
	String FILTER_OPERATOR_BOTTOM_PERCENT = "bottom-percent"; //$NON-NLS-1$

	/** design constant: filter operator not in */
	String FILTER_OPERATOR_NOT_IN = "not-in"; //$NON-NLS-1$


	/**
	 * @deprecated in BIRT 2.1. This operator is not supported.
	 */
	@Deprecated
	/** design constant: filter operator any */
	String FILTER_OPERATOR_ANY = "any"; //$NON-NLS-1$

	/** design constant: filter operator match */
	String FILTER_OPERATOR_MATCH = "match"; //$NON-NLS-1$

	/** design constant: filter operator not like */
	String FILTER_OPERATOR_NOT_LIKE = "not-like"; //$NON-NLS-1$

	/** design constant: filter operator not match */
	String FILTER_OPERATOR_NOT_MATCH = "not-match"; //$NON-NLS-1$

	/** design constant: filter operator in */
	String FILTER_OPERATOR_IN = "in"; //$NON-NLS-1$


	/**
	 * columnAlign -- The column alignment The first constant is the name of
	 * columnAlign choice. The followed constants are valid choice values of
	 * columnAlign.
	 */
	/** design constant: choice column align */
	String CHOICE_COLUMN_ALIGN = "columnAlign"; //$NON-NLS-1$

	/** design constant: column align left */
	String COLUMN_ALIGN_LEFT = "left"; //$NON-NLS-1$

	/** design constant: column align center */
	String COLUMN_ALIGN_CENTER = "center"; //$NON-NLS-1$

	/** design constant: column align right */
	String COLUMN_ALIGN_RIGHT = "right"; //$NON-NLS-1$

	/**
	 * queryFrom -- Where the query is from The first constant is the name of
	 * queryFrom choice. The followed constants are valid choice values of
	 * queryFrom.
	 */
	/** design constant: choice query choice type */
	String CHOICE_QUERY_CHOICE_TYPE = "queryChoiceType"; //$NON-NLS-1$

	/** design constant: query choice type none */
	String QUERY_CHOICE_TYPE_NONE = "none"; //$NON-NLS-1$

	/** design constant: query choice type text */
	String QUERY_CHOICE_TYPE_TEXT = "text"; //$NON-NLS-1$

	/** design constant: query choice type script */
	String QUERY_CHOICE_TYPE_SCRIPT = "script"; //$NON-NLS-1$

	/**
	 * Common format type "Custom"
	 */
	/** design constant: value format type custom */
	String VALUE_FORMAT_TYPE_CUSTOM = "Custom"; //$NON-NLS-1$


	/**
	 * numberFormat -- the number format The first constant is the name of
	 * numberFormat choice. The followed constants are valid choice values of
	 * numberFormat.
	 */
	/** design constant: choice number format type */
	String CHOICE_NUMBER_FORMAT_TYPE = "numberFormat"; //$NON-NLS-1$

	/** design constant: number format type unformatted */
	String NUMBER_FORMAT_TYPE_UNFORMATTED = "Unformatted"; //$NON-NLS-1$

	/** design constant: number format type general number */
	String NUMBER_FORMAT_TYPE_GENERAL_NUMBER = "General Number"; //$NON-NLS-1$

	/** design constant: number format type currency */
	String NUMBER_FORMAT_TYPE_CURRENCY = "Currency"; //$NON-NLS-1$

	/** design constant: number format type fixed */
	String NUMBER_FORMAT_TYPE_FIXED = "Fixed"; //$NON-NLS-1$

	/** design constant: number format type percent */
	String NUMBER_FORMAT_TYPE_PERCENT = "Percent"; //$NON-NLS-1$

	/** design constant: number format type scientific */
	String NUMBER_FORMAT_TYPE_SCIENTIFIC = "Scientific"; //$NON-NLS-1$

	/** design constant: number format type standard */
	String NUMBER_FORMAT_TYPE_STANDARD = "Standard"; //$NON-NLS-1$

	/** design constant: number format type custom */
	String NUMBER_FORMAT_TYPE_CUSTOM = VALUE_FORMAT_TYPE_CUSTOM;

	/**
	 * dateTimeFormat -- the date/time format The first constant is the name of
	 * dateTimeFormat choice. The followed constants are valid choice values of
	 * dateTimeFormat.
	 */
	/** design constant: choice datetime format type */
	String CHOICE_DATETIME_FORMAT_TYPE = "dateTimeFormat"; //$NON-NLS-1$

	/** design constant: datetime format type unformatted */
	String DATETIME_FORMAT_TYPE_UNFORMATTED = "Unformatted"; //$NON-NLS-1$

	/** design constant: datetime format type medium date */
	String DATETIME_FORMAT_TYPE_DATE_PICKER = "Date Picker"; //$NON-NLS-1$

	/** design constant: datetime format type medium date with short time */
	String DATETIME_FORMAT_TYPE_DATE_TIME_PICKER_SHORT_TIME = "Date Picker, Short Time"; //$NON-NLS-1$

	/** design constant: datetime format type general date with medium time */
	String DATETIME_FORMAT_TYPE_DATE_TIME_PICKER_MEDIUM_TIME = "Date Picker, Medium Time"; //$NON-NLS-1$

	/** design constant: datetime format type general date */
	String DATETIME_FORMAT_TYPE_GENERAL_DATE = "General Date"; //$NON-NLS-1$

	/** design constant: datetime format type long date */
	String DATETIME_FORMAT_TYPE_LONG_DATE = "Long Date"; //$NON-NLS-1$

	/** design constant: datetime format type mudium date */
	String DATETIME_FORMAT_TYPE_MEDIUM_DATE = "Medium Date"; //$NON-NLS-1$

	/** design constant: datetime format type short date */
	String DATETIME_FORMAT_TYPE_SHORT_DATE = "Short Date"; //$NON-NLS-1$

	/** design constant: datetime format type long time */
	String DATETIME_FORMAT_TYPE_LONG_TIME = "Long Time"; //$NON-NLS-1$

	/** design constant: datetime format type medium time */
	String DATETIME_FORMAT_TYPE_MEDIUM_TIME = "Medium Time"; //$NON-NLS-1$

	/** design constant: datetime format type short time */
	String DATETIME_FORMAT_TYPE_SHORT_TIME = "Short Time"; //$NON-NLS-1$

	/** design constant: datetime format type custom */
	String DATETIME_FORMAT_TYPE_CUSTOM = VALUE_FORMAT_TYPE_CUSTOM;


	/**
	 * dateFormat choice.
	 */
	/** design constant: choice date format type */
	String CHOICE_DATE_FORMAT_TYPE = "dateFormat"; //$NON-NLS-1$

	/** design constant: date format type unformatted */
	String DATE_FORMAT_TYPE_UNFORMATTED = "Unformatted"; //$NON-NLS-1$

	/** design constant: date format type general date */
	String DATE_FORMAT_TYPE_DATE_PICKER = "Date Picker"; //$NON-NLS-1$

	/** design constant: date format type general date */
	String DATE_FORMAT_TYPE_GENERAL_DATE = "General Date"; //$NON-NLS-1$

	/** design constant: date format type long date */
	String DATE_FORMAT_TYPE_LONG_DATE = "Long Date"; //$NON-NLS-1$

	/** design constant: date format type mudium date */
	String DATE_FORMAT_TYPE_MEDIUM_DATE = "Medium Date"; //$NON-NLS-1$

	/** design constant: date format type short date */
	String DATE_FORMAT_TYPE_SHORT_DATE = "Short Date"; //$NON-NLS-1$

	/** design constant: date format type custom */
	String DATE_FORMAT_TYPE_CUSTOM = VALUE_FORMAT_TYPE_CUSTOM;


	/**
	 * timeFormat choice
	 */
	/** design constant: choice time format type */
	String CHOICE_TIME_FORMAT_TYPE = "timeFormat"; //$NON-NLS-1$

	/** design constant: date format type general date */
	String TIME_FORMAT_TYPE_TIME_PICKER_SHORT_TIME = "Time Picker, Short Time"; //$NON-NLS-1$

	/** design constant: date format type general date */
	String TIME_FORMAT_TYPE_TIME_PICKER_MEDIUM_TIME = "Time Picker, Medium Time"; //$NON-NLS-1$

	/** design constant: time format type long time */
	String TIME_FORMAT_TYPE_LONG_TIME = "Long Time"; //$NON-NLS-1$

	/** design constant: time format type medium time */
	String TIME_FORMAT_TYPE_MEDIUM_TIME = "Medium Time"; //$NON-NLS-1$

	/** design constant: time format type short time */
	String TIME_FORMAT_TYPE_SHORT_TIME = "Short Time"; //$NON-NLS-1$

	/** design constant: time format type custom */
	String TIME_FORMAT_TYPE_CUSTOM = VALUE_FORMAT_TYPE_CUSTOM;


	/**
	 * stringFormat -- the string format The first constant is the name of
	 * stringFormat choice. The followed constants are valid choice values of
	 * stringFormat.
	 */
	/** design constant: choice string format type */
	String CHOICE_STRING_FORMAT_TYPE = "stringFormat"; //$NON-NLS-1$

	/** design constant: string format type unformatted */
	String STRING_FORMAT_TYPE_UNFORMATTED = "Unformatted"; //$NON-NLS-1$

	/** design constant: string format type uppercase */
	String STRING_FORMAT_TYPE_UPPERCASE = ">"; //$NON-NLS-1$

	/** design constant: string format type lowercase */
	String STRING_FORMAT_TYPE_LOWERCASE = "<"; //$NON-NLS-1$

	/** design constant: string format type custom */
	String STRING_FORMAT_TYPE_CUSTOM = VALUE_FORMAT_TYPE_CUSTOM;

	/** design constant: string format type zip code */
	String STRING_FORMAT_TYPE_ZIP_CODE = "Zip Code"; //$NON-NLS-1$

	/** design constant: string format type zip code 4 */
	String STRING_FORMAT_TYPE_ZIP_CODE_4 = "Zip Code + 4"; //$NON-NLS-1$

	/** design constant: string format type phone number */
	String STRING_FORMAT_TYPE_PHONE_NUMBER = "Phone Number"; //$NON-NLS-1$

	/** design constant: string format type social security number */
	String STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER = "Social Security Number"; //$NON-NLS-1$


	// targetNames
	/** design constant: choice target names type */
	String CHOICE_TARGET_NAMES_TYPE = "targetNames"; //$NON-NLS-1$

	/** design constant: target names type blank */
	String TARGET_NAMES_TYPE_BLANK = "_blank"; //$NON-NLS-1$

	/** design constant: target names type self */
	String TARGET_NAMES_TYPE_SELF = "_self"; //$NON-NLS-1$

	/** design constant: target names type parent */
	String TARGET_NAMES_TYPE_PARENT = "_parent"; //$NON-NLS-1$

	/** design constant: target names type top */
	String TARGET_NAMES_TYPE_TOP = "_top"; //$NON-NLS-1$


	/**
	 * templateElementType -- the template element type The first constant is the
	 * name of the templateElementType choice set. The followed constants are valid
	 * choice values of the templateElementType.
	 */
	/** design constant: choice template element type */
	String CHOICE_TEMPLATE_ELEMENT_TYPE = "templateElementType"; //$NON-NLS-1$

	/** design constant: template element type table */
	String TEMPLATE_ELEMENT_TYPE_TABLE = "Table"; //$NON-NLS-1$

	/** design constant: template element type freeform */
	String TEMPLATE_ELEMENT_TYPE_FREEFORM = "FreeForm"; //$NON-NLS-1$

	/** design constant: template element type data */
	String TEMPLATE_ELEMENT_TYPE_DATA = "Data"; //$NON-NLS-1$

	/** design constant: template element type grid */
	String TEMPLATE_ELEMENT_TYPE_GRID = "Grid"; //$NON-NLS-1$

	/** design constant: template element type image */
	String TEMPLATE_ELEMENT_TYPE_IMAGE = "Image"; //$NON-NLS-1$

	/** design constant: template element type label */
	String TEMPLATE_ELEMENT_TYPE_LABEL = "Label"; //$NON-NLS-1$

	/** design constant: template element type list */
	String TEMPLATE_ELEMENT_TYPE_LIST = "List"; //$NON-NLS-1$

	/** design constant: template element type text */
	String TEMPLATE_ELEMENT_TYPE_TEXT = "Text"; //$NON-NLS-1$

	/** design constant: template element type extended item */
	String TEMPLATE_ELEMENT_TYPE_EXTENDED_ITEM = "ExtendedItem"; //$NON-NLS-1$

	/** design constant: template element type text data */
	String TEMPLATE_ELEMENT_TYPE_TEXT_DATA = "TextData"; //$NON-NLS-1$

	/** design constant: template element type data set */
	String TEMPLATE_ELEMENT_TYPE_DATA_SET = "DataSet"; //$NON-NLS-1$


	/**
	 * sortType -- the sort type. The first constant is the name of the sortType
	 * choice set. The followed constants are valid choice values of the sortType.
	 */
	/** design constant: choice sort type */
	String CHOICE_SORT_TYPE = "sortType"; //$NON-NLS-1$

	/** design constant: sort type none */
	String SORT_TYPE_NONE = "none"; //$NON-NLS-1$

	/** design constant: sort type sort on group key */
	String SORT_TYPE_SORT_ON_GROUP_KEY = "sort-on-group-key"; //$NON-NLS-1$

	/** design constant: sort type complex sort */
	String SORT_TYPE_COMPLEX_SORT = "complex-sort"; //$NON-NLS-1$


	/**
	 * joinType -- join type of join condition.
	 */
	/** design constant: choice join type */
	String CHOICE_JOIN_TYPE = "joinType"; //$NON-NLS-1$

	/** design constant: join type inner */
	String JOIN_TYPE_INNER = "inner"; //$NON-NLS-1$

	/** design constant: join type left out */
	String JOIN_TYPE_LEFT_OUT = "left-out"; //$NON-NLS-1$

	/** design constant: join type right out */
	String JOIN_TYPE_RIGHT_OUT = "right-out"; //$NON-NLS-1$

	/** design constant: join type full out */
	String JOIN_TYPE_FULL_OUT = "full-out"; //$NON-NLS-1$


	/**
	 * joinOperator -- join operator of join condition.
	 */
	/** design constant: choice join operator */
	String CHOICE_JOIN_OPERATOR = "joinOperator"; //$NON-NLS-1$

	/** design constant: join operator eqals */
	String JOIN_OPERATOR_EQALS = "eq"; //$NON-NLS-1$


	/**
	 * actionTargetFileType -- target type of the linked file
	 */
	/** design constant: choice action target file type */
	String CHOICE_ACTION_TARGET_FILE_TYPE = "actionTargetFileType"; //$NON-NLS-1$

	/** design constant: action target file type report design */
	String ACTION_TARGET_FILE_TYPE_REPORT_DESIGN = "report-design"; //$NON-NLS-1$

	/** design constant: action target file type report document */
	String ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT = "report-document"; //$NON-NLS-1$


	/**
	 * actionBookmarkType -- target bookmark type
	 */
	/** design constant: choice action bookmark type */
	String CHOICE_ACTION_BOOKMARK_TYPE = "actionBookmarkType"; //$NON-NLS-1$

	/** design constant: action bookmark type bookmark */
	String ACTION_BOOKMARK_TYPE_BOOKMARK = "bookmark"; //$NON-NLS-1$

	/** design constant: action bookmark type toc */
	String ACTION_BOOKMARK_TYPE_TOC = "toc"; //$NON-NLS-1$


	/**
	 * autotextType -- type of auto text
	 */
	/** design constant: choice auto text type */
	String CHOICE_AUTO_TEXT_TYPE = "autoTextType"; //$NON-NLS-1$

	/** design constant: auto text page number */
	String AUTO_TEXT_PAGE_NUMBER = "page-number"; //$NON-NLS-1$

	/** design constant: auto text total page */
	String AUTO_TEXT_TOTAL_PAGE = "total-page"; //$NON-NLS-1$

	/** design constant: auto text page number unfiltered */
	String AUTO_TEXT_PAGE_NUMBER_UNFILTERED = "page-number-unfiltered"; //$NON-NLS-1$

	/** design constant: auto text total page unfiltered */
	String AUTO_TEXT_TOTAL_PAGE_UNFILTERED = "total-page-unfiltered"; //$NON-NLS-1$

	/** design constant: auto text page variable */
	String AUTO_TEXT_PAGE_VARIABLE = "page-variable"; //$NON-NLS-1$


	/**
	 * dataSetMode -- the mode to support data sets. Can be single data set and
	 */
	/** design constant: choice data set mode type */
	String CHOICE_DATA_SET_MODE_TYPE = "dataSetMode"; //$NON-NLS-1$

	/** design constant: data set mode single */
	String DATA_SET_MODE_SINGLE = "single"; //$NON-NLS-1$

	/** design constant: data set mode multiple */
	String DATA_SET_MODE_MULTIPLE = "multiple"; //$NON-NLS-1$


	/**
	 * filterTarget -- filter target type.
	 */
	/** design constant: choice filter target */
	String CHOICE_FILTER_TARGET = "filterTarget"; //$NON-NLS-1$

	/** design constant: filter target data set */
	String FILTER_TARGET_DATA_SET = "DataSet"; //$NON-NLS-1$

	/** design constant: filter target result set */
	String FILTER_TARGET_RESULT_SET = "ResultSet"; //$NON-NLS-1$


	/**
	 * View action --view action type.
	 */
	/** design constant: choice view action */
	String CHOICE_VIEW_ACTION = "viewAction"; //$NON-NLS-1$

	/** design constant: view action no change */
	String VIEW_ACTION_NO_CHANGE = "NoChange"; //$NON-NLS-1$

	/** design constant: view action changed */
	String VIEW_ACTION_CHANGED = "Changed"; //$NON-NLS-1$

	/** design constant: view action added */
	String VIEW_ACTION_ADDED = "Added"; //$NON-NLS-1$

	/** design constant: view action deleted */
	String VIEW_ACTION_DELETED = "Deleted"; //$NON-NLS-1$


	/**
	 * measure Function -- measure function type.
	 */
	/** design constant: choice aggregation function */
	String CHOICE_AGGREGATION_FUNCTION = "aggregationFunction"; //$NON-NLS-1$

	/** design constant: aggregation function sum */
	String AGGREGATION_FUNCTION_SUM = "sum"; //$NON-NLS-1$

	/** design constant: aggregation function count */
	String AGGREGATION_FUNCTION_COUNT = "count"; //$NON-NLS-1$

	/** design constant: aggregation function min */
	String AGGREGATION_FUNCTION_MIN = "min"; //$NON-NLS-1$

	/** design constant: aggregation function max */
	String AGGREGATION_FUNCTION_MAX = "max"; //$NON-NLS-1$

	/** design constant: aggregation function average */
	String AGGREGATION_FUNCTION_AVERAGE = "average"; //$NON-NLS-1$

	/** design constant: aggregation function weightedavg */
	String AGGREGATION_FUNCTION_WEIGHTEDAVG = "weighted-avg"; //$NON-NLS-1$

	/** design constant: aggregation function stddev */
	String AGGREGATION_FUNCTION_STDDEV = "stddev"; //$NON-NLS-1$

	/** design constant: aggregation function first */
	String AGGREGATION_FUNCTION_FIRST = "first"; //$NON-NLS-1$

	/** design constant: aggregation function last */
	String AGGREGATION_FUNCTION_LAST = "last"; //$NON-NLS-1$

	/** design constant: aggregation function mode */
	String AGGREGATION_FUNCTION_MODE = "mode"; //$NON-NLS-1$

	/** design constant: aggregation function movingave */
	String AGGREGATION_FUNCTION_MOVINGAVE = "moving-ave"; //$NON-NLS-1$

	/** design constant: aggregation function median */
	String AGGREGATION_FUNCTION_MEDIAN = "median"; //$NON-NLS-1$

	/** design constant: aggregation function variance */
	String AGGREGATION_FUNCTION_VARIANCE = "variance"; //$NON-NLS-1$

	/** design constant: aggregation function runningsum */
	String AGGREGATION_FUNCTION_RUNNINGSUM = "running-sum"; //$NON-NLS-1$

	/** design constant: aggregation function irr */
	String AGGREGATION_FUNCTION_IRR = "irr"; //$NON-NLS-1$

	/** design constant: aggregation function mirr */
	String AGGREGATION_FUNCTION_MIRR = "mirr"; //$NON-NLS-1$

	/** design constant: aggregation function npv */
	String AGGREGATION_FUNCTION_NPV = "npv"; //$NON-NLS-1$

	/** design constant: aggregation function runningnpv */
	String AGGREGATION_FUNCTION_RUNNINGNPV = "running-npv"; //$NON-NLS-1$

	/** design constant: aggregation function countdistinct */
	String AGGREGATION_FUNCTION_COUNTDISTINCT = "count-distinct"; //$NON-NLS-1$

	/** design constant: aggregation function runningcount */
	String AGGREGATION_FUNCTION_RUNNINGCOUNT = "running-count"; //$NON-NLS-1$

	/** design constant: aggregation function is top n */
	String AGGREGATION_FUNCTION_IS_TOP_N = "is-top-n"; //$NON-NLS-1$

	/** design constant: aggregation function is bottom n */
	String AGGREGATION_FUNCTION_IS_BOTTOM_N = "is-bottom-n"; //$NON-NLS-1$

	/** design constant: aggregation function is top n percent */
	String AGGREGATION_FUNCTION_IS_TOP_N_PERCENT = "is-top-n-percent"; //$NON-NLS-1$

	/** design constant: aggregation function is bottom n percent */
	String AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT = "is-bottom-n-percent"; //$NON-NLS-1$

	/** design constant: aggregation function percent rank */
	String AGGREGATION_FUNCTION_PERCENT_RANK = "percent-rank"; //$NON-NLS-1$

	/** design constant: aggregation function percentile */
	String AGGREGATION_FUNCTION_PERCENTILE = "percentile"; //$NON-NLS-1$

	/** design constant: aggregation function top quartile */
	String AGGREGATION_FUNCTION_TOP_QUARTILE = "quartile"; //$NON-NLS-1$

	/** design constant: aggregation function percent sum */
	String AGGREGATION_FUNCTION_PERCENT_SUM = "percent-sum"; //$NON-NLS-1$

	/** design constant: aggregation function rank */
	String AGGREGATION_FUNCTION_RANK = "rank"; //$NON-NLS-1$


	/**
	 * measure Function -- measure function type.
	 */
	/** design constant: choice measure function */
	String CHOICE_MEASURE_FUNCTION = "measureFunction"; //$NON-NLS-1$

	/** design constant: measure function sum */
	String MEASURE_FUNCTION_SUM = AGGREGATION_FUNCTION_SUM;

	/** design constant: measure function count */
	String MEASURE_FUNCTION_COUNT = AGGREGATION_FUNCTION_COUNT;

	/** design constant: measure function min */
	String MEASURE_FUNCTION_MIN = AGGREGATION_FUNCTION_MIN;

	/** design constant: measure function max */
	String MEASURE_FUNCTION_MAX = AGGREGATION_FUNCTION_MAX;

	/** design constant: measure function average */
	String MEASURE_FUNCTION_AVERAGE = AGGREGATION_FUNCTION_AVERAGE;

	/** design constant: measure function weightedavg */
	String MEASURE_FUNCTION_WEIGHTEDAVG = AGGREGATION_FUNCTION_WEIGHTEDAVG;

	/** design constant: measure function stddev */
	String MEASURE_FUNCTION_STDDEV = AGGREGATION_FUNCTION_STDDEV;

	/** design constant: measure function first */
	String MEASURE_FUNCTION_FIRST = AGGREGATION_FUNCTION_FIRST;

	/** design constant: measure function last */
	String MEASURE_FUNCTION_LAST = AGGREGATION_FUNCTION_LAST;

	/** design constant: measure function mode */
	String MEASURE_FUNCTION_MODE = AGGREGATION_FUNCTION_MODE;

	/** design constant: measure function movingave */
	String MEASURE_FUNCTION_MOVINGAVE = AGGREGATION_FUNCTION_MOVINGAVE;

	/** design constant: measure function median */
	String MEASURE_FUNCTION_MEDIAN = AGGREGATION_FUNCTION_MOVINGAVE;

	/** design constant: measure function variance */
	String MEASURE_FUNCTION_VARIANCE = AGGREGATION_FUNCTION_VARIANCE;

	/** design constant: measure function runningsum */
	String MEASURE_FUNCTION_RUNNINGSUM = AGGREGATION_FUNCTION_RUNNINGSUM;

	/** design constant: measure function irr */
	String MEASURE_FUNCTION_IRR = AGGREGATION_FUNCTION_IRR;

	/** design constant: measure function mirr */
	String MEASURE_FUNCTION_MIRR = AGGREGATION_FUNCTION_IRR;

	/** design constant: measure function npv */
	String MEASURE_FUNCTION_NPV = AGGREGATION_FUNCTION_NPV;

	/** design constant: measure function runningnpv */
	String MEASURE_FUNCTION_RUNNINGNPV = AGGREGATION_FUNCTION_RUNNINGNPV;

	/** design constant: measure function countdistinct */
	String MEASURE_FUNCTION_COUNTDISTINCT = AGGREGATION_FUNCTION_COUNTDISTINCT;

	/** design constant: measure function runningcount */
	String MEASURE_FUNCTION_RUNNINGCOUNT = AGGREGATION_FUNCTION_RUNNINGCOUNT;


	/**
	 * Level type constants.
	 */
	/** design constant: choice level type */
	String CHOICE_LEVEL_TYPE = "levelType"; //$NON-NLS-1$

	/** design constant: level type dynamic */
	String LEVEL_TYPE_DYNAMIC = "dynamic"; //$NON-NLS-1$

	/** design constant: level type mirrored */
	String LEVEL_TYPE_MIRRORED = "mirrored"; //$NON-NLS-1$


	/**
	 * measure Function -- measure function type.
	 */
	/** design constant: choice access permission */
	String CHOICE_ACCESS_PERMISSION = "accessPermission"; //$NON-NLS-1$

	/** design constant: access permission allow */
	String ACCESS_PERMISSION_ALLOW = "allow"; //$NON-NLS-1$

	/** design constant: access permission disallow */
	String ACCESS_PERMISSION_DISALLOW = "disallow"; //$NON-NLS-1$


	/**
	 * Parameter sort values -- can be value or display text.
	 */
	/** design constant: choice param sort values */
	String CHOICE_PARAM_SORT_VALUES = "paramSortValues"; //$NON-NLS-1$

	/** design constant: param sort values value */
	String PARAM_SORT_VALUES_VALUE = "value"; //$NON-NLS-1$

	/** design constant: param sort values label */
	String PARAM_SORT_VALUES_LABEL = "label"; //$NON-NLS-1$


	/**
	 * Report layout preference -- layout type for report design.
	 */
	/** design constant: choice report layout preference */
	String CHOICE_REPORT_LAYOUT_PREFERENCE = "reportLayoutPreference"; //$NON-NLS-1$

	/** design constant: report layout preference fixed layout */
	String REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT = "fixed layout"; //$NON-NLS-1$

	/** design constant: report layout preference auto layout */
	String REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT = "auto layout"; //$NON-NLS-1$


	/**
	 * Date time level type -- type for date-time cube level.
	 */
	/** design constant: choice date time level type */
	String CHOICE_DATE_TIME_LEVEL_TYPE = "dateTimeLevelType"; //$NON-NLS-1$

	/** design constant: date time level type year */
	String DATE_TIME_LEVEL_TYPE_YEAR = "year"; //$NON-NLS-1$

	/** design constant: date time level type month */
	String DATE_TIME_LEVEL_TYPE_MONTH = "month"; //$NON-NLS-1$

	/** design constant: date time level type quarter */
	String DATE_TIME_LEVEL_TYPE_QUARTER = "quarter"; //$NON-NLS-1$

	/** design constant: date time level type week of year */
	String DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR = "week-of-year"; //$NON-NLS-1$

	/** design constant: date time level type week of quarter */
	String DATE_TIME_LEVEL_TYPE_WEEK_OF_QUARTER = "week-of-quarter"; //$NON-NLS-1$

	/** design constant: date time level type week of month */
	String DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH = "week-of-month"; //$NON-NLS-1$

	/** design constant: date time level type day of year */
	String DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR = "day-of-year"; //$NON-NLS-1$

	/** design constant: date time level type day of quarter */
	String DATE_TIME_LEVEL_TYPE_DAY_OF_QUARTER = "day-of-quarter"; //$NON-NLS-1$

	/** design constant: date time level type day of month */
	String DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH = "day-of-month"; //$NON-NLS-1$

	/** design constant: date time level type day of week */
	String DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK = "day-of-week"; //$NON-NLS-1$

	/** design constant: date time level type hour */
	String DATE_TIME_LEVEL_TYPE_HOUR = "hour"; //$NON-NLS-1$

	/** design constant: date time level type minute */
	String DATE_TIME_LEVEL_TYPE_MINUTE = "minute"; //$NON-NLS-1$

	/** design constant: date time level type second */
	String DATE_TIME_LEVEL_TYPE_SECOND = "second"; //$NON-NLS-1$


	/**
	 * @deprecated This is replaced by DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR and
	 *             DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH, and not used anymore.
	 */
	@Deprecated
	/** design constant: date time level type week */
	String DATE_TIME_LEVEL_TYPE_WEEK = "week"; //$NON-NLS-1$


	/**
	 * @deprecated This is replaced by DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR,
	 *             DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH and
	 *             DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK, and not used anymore.
	 */
	@Deprecated
	/** design constant: date time level type day */
	String DATE_TIME_LEVEL_TYPE_DAY = "day"; //$NON-NLS-1$


	/**
	 * Scalar parameter type.
	 */
	/** design constant: choice scalar param type */
	String CHOICE_SCALAR_PARAM_TYPE = "scalarParamType"; //$NON-NLS-1$

	/** design constant: scalar param type simple */
	String SCALAR_PARAM_TYPE_SIMPLE = "simple"; //$NON-NLS-1$

	/** design constant: scalar param type multi value */
	String SCALAR_PARAM_TYPE_MULTI_VALUE = "multi-value"; //$NON-NLS-1$

	/** design constant: scalar param type ad hoc */
	String SCALAR_PARAM_TYPE_AD_HOC = "ad-hoc"; //$NON-NLS-1$


	/**
	 * Choice constants for direction and orientation
	 */
	/** design constant: choice bidi direction */
	String CHOICE_BIDI_DIRECTION = "bidiDirection"; //$NON-NLS-1$

	/** design constant: bidi direction rtl */
	String BIDI_DIRECTION_RTL = "rtl"; //$NON-NLS-1$

	/** design constant: bidi direction ltr */
	String BIDI_DIRECTION_LTR = "ltr"; //$NON-NLS-1$


	/**
	 * Choice constants for scope type.
	 */
	/** design constant: choice scope type */
	String CHOICE_SCOPE_TYPE = "scopeType"; //$NON-NLS-1$

	/** design constant: scope type row */
	String SCOPE_TYPE_ROW = "row"; //$NON-NLS-1$

	/** design constant: scope type col */
	String SCOPE_TYPE_COL = "col"; //$NON-NLS-1$

	/** design constant: scope type rowgroup */
	String SCOPE_TYPE_ROWGROUP = "rowgroup"; //$NON-NLS-1$

	/** design constant: scope type colgroup */
	String SCOPE_TYPE_COLGROUP = "colgroup"; //$NON-NLS-1$


	// dynamic filter
	/** design constant: choice dynamic filter */
	String CHOICE_DYNAMIC_FILTER = "dynamicFilterChoice"; //$NON-NLS-1$

	/** design constant: dynamic filter simple */
	String DYNAMIC_FILTER_SIMPLE = "simple"; //$NON-NLS-1$

	/** design constant: dynamic filter advanced */
	String DYNAMIC_FILTER_ADVANCED = "advanced"; //$NON-NLS-1$


	/**
	 * Choice constants for workMode.
	 */
	/** design constant: choice variable type */
	String CHOICE_VARIABLE_TYPE = "variableType"; //$NON-NLS-1$

	/** design constant: variable type report */
	String VARIABLE_TYPE_REPORT = "report"; //$NON-NLS-1$

	/** design constant: variable type page */
	String VARIABLE_TYPE_PAGE = "page"; //$NON-NLS-1$


	/**
	 * Choice constants for nullValueOrderingType.
	 */
	/** design constant: choice null value ordering type */
	String CHOICE_NULL_VALUE_ORDERING_TYPE = "nullValueOrderingType"; //$NON-NLS-1$

	/** design constant: null value ordering type unknown */
	String NULL_VALUE_ORDERING_TYPE_UNKNOWN = "unknown"; //$NON-NLS-1$

	/** design constant: null value ordering type nullisfirst */
	String NULL_VALUE_ORDERING_TYPE_NULLISFIRST = "nullIsFirst"; //$NON-NLS-1$

	/** design constant: null value ordering type nullislast */
	String NULL_VALUE_ORDERING_TYPE_NULLISLAST = "nullIsLast"; //$NON-NLS-1$


	/**
	 * Choice constants for dataVersion.
	 */
	/** design constant: choice data version */
	String CHOICE_DATA_VERSION = "dataVersion"; //$NON-NLS-1$

	/** design constant: data version transient */
	String DATA_VERSION_TRANSIENT = "transient"; //$NON-NLS-1$

	/** design constant: data version latest */
	String DATA_VERSION_LATEST = "latest"; //$NON-NLS-1$


	/**
	 * Choice constants for dataSelector type.
	 */
	/** design constant: choice data selector type */
	String CHOICE_DATA_SELECTOR_TYPE = "selectorType"; //$NON-NLS-1$

	/** design constant: data selector type list */
	String DATA_SELECTOR_TYPE_LIST = "list"; //$NON-NLS-1$

	/** design constant: data selector type dropdown */
	String DATA_SELECTOR_TYPE_DROPDOWN = "dropdown";//$NON-NLS-1$

	/** design constant: data selector type slider */
	String DATA_SELECTOR_TYPE_SLIDER = "slider"; //$NON-NLS-1$

	/** design constant: data selector type check box */
	String DATA_SELECTOR_TYPE_CHECK_BOX = "checkBox";//$NON-NLS-1$

	/** design constant: data selector type radio button */
	String DATA_SELECTOR_TYPE_RADIO_BUTTON = "radioButton";//$NON-NLS-1$

	/** design constant: data selector type text box */
	String DATA_SELECTOR_TYPE_TEXT_BOX = "textBox";//$NON-NLS-1$

	/** design constant: data selector type calendar */
	String DATA_SELECTOR_TYPE_CALENDAR = "calendar";//$NON-NLS-1$

	/** design constant: data selector type current selections */
	String DATA_SELECTOR_TYPE_CURRENT_SELECTIONS = "currentSelections";//$NON-NLS-1$

	/** design constant: data selector type list group */
	String DATA_SELECTOR_TYPE_LIST_GROUP = "listGroup";//$NON-NLS-1$

	/** design constant: data selector type checkboxtree group */
	String DATA_SELECTOR_TYPE_CHECKBOXTREE_GROUP = "checkboxtreeGroup";//$NON-NLS-1$

	/** design constant: data selector type dropdown group */
	String DATA_SELECTOR_TYPE_DROPDOWN_GROUP = "dropdownGroup";//$NON-NLS-1$


	/**
	 * Choice constants for thumb type.
	 */
	/** design constant: choice thumb type */
	String CHOICE_THUMB_TYPE = "thumbType"; //$NON-NLS-1$

	/** design constant: thumb type single */
	String THUMB_TYPE_SINGLE = "single"; //$NON-NLS-1$

	/** design constant: thumb type dual */
	String THUMB_TYPE_DUAL = "dual";//$NON-NLS-1$


	/**
	 * Choice constants for access type.
	 */
	/** design constant: choice access type */
	String CHOICE_ACCESS_TYPE = "accessType"; //$NON-NLS-1$

	/** design constant: access type transient */
	String ACCESS_TYPE_TRANSIENT = "transient"; //$NON-NLS-1$

	/** design constant: access type specific version */
	String ACCESS_TYPE_SPECIFIC_VERSION = "specificVersion"; //$NON-NLS-1$

	/** design constant: access type latest */
	String ACCESS_TYPE_LATEST = "latest"; //$NON-NLS-1$


	/**
	 * Choice constants for overflow.
	 */
	/** design constant: choice overflow */
	String CHOICE_OVERFLOW = "overflow"; //$NON-NLS-1$

	/** design constant: overflow visible */
	String OVERFLOW_VISIBLE = "visible"; //$NON-NLS-1$

	/** design constant: overflow hidden */
	String OVERFLOW_HIDDEN = "hidden"; //$NON-NLS-1$

	/** design constant: overflow scroll */
	String OVERFLOW_SCROLL = "scroll"; //$NON-NLS-1$

	/** design constant: overflow auto */
	String OVERFLOW_AUTO = "auto"; //$NON-NLS-1$


	/**
	 * @deprecated Use <code>CHOICE_GADGET_CONTENT_TYPE</code> instead.
	 */
	@Deprecated
	/** design constant: choice html gadget content type */
	String CHOICE_HTML_GADGET_CONTENT_TYPE = "HTMLGadgetContentType"; //$NON-NLS-1$


	/**
	 * @deprecated Use <code>GADGET_CONTENT_TYPE_HTML</code> instead.
	 */
	@Deprecated
	/** design constant: html gadget content type html */
	String HTML_GADGET_CONTENT_TYPE_HTML = "html"; //$NON-NLS-1$


	/**
	 * @deprecated Use <code>GADGET_CONTENT_TYPE_URL</code> instead.
	 */
	@Deprecated
	/** design constant: html gadget content type url */
	String HTML_GADGET_CONTENT_TYPE_URL = "url"; //$NON-NLS-1$

	/** design constant: choice gadget content type */
	String CHOICE_GADGET_CONTENT_TYPE = "GadgetContentType"; //$NON-NLS-1$

	/** design constant: gadget content type html */
	String GADGET_CONTENT_TYPE_HTML = "html"; //$NON-NLS-1$

	/** design constant: gadget content type url */
	String GADGET_CONTENT_TYPE_URL = "url"; //$NON-NLS-1$

	/** design constant: gadget content type video */
	String GADGET_CONTENT_TYPE_VIDEO = "video"; //$NON-NLS-1$

	/** design constant: gadget content type videourl */
	String GADGET_CONTENT_TYPE_VIDEOURL = "videourl"; //$NON-NLS-1$

	/** design constant: gadget content type text */
	String GADGET_CONTENT_TYPE_TEXT = "text"; //$NON-NLS-1$

	/** design constant: gadget content type image */
	String GADGET_CONTENT_TYPE_IMAGE = "image"; //$NON-NLS-1$

	/** design constant: choice window status */
	String CHOICE_WINDOW_STATUS = "windowStatus"; //$NON-NLS-1$

	/** design constant: window status normal */
	String WINDOW_STATUS_NORMAL = "normal"; //$NON-NLS-1$

	/** design constant: window status maximized */
	String WINDOW_STATUS_MAXIMIZED = "maximized"; //$NON-NLS-1$


	/**
	 * Choice constants for reportItemThemeType.
	 */
	/** design constant: choice report item theme type */
	String CHOICE_REPORT_ITEM_THEME_TYPE = "reportItemThemeType"; //$NON-NLS-1$

	/** design constant: report item theme type table */
	String REPORT_ITEM_THEME_TYPE_TABLE = "Table"; //$NON-NLS-1$

	/** design constant: report item theme type list */
	String REPORT_ITEM_THEME_TYPE_LIST = "List"; //$NON-NLS-1$

	/** design constant: report item theme type grid */
	String REPORT_ITEM_THEME_TYPE_GRID = "Grid"; //$NON-NLS-1$


	/**
	 * Choice constants for nullsOrdering.
	 */
	/** design constant: choice nulls ordering */
	String CHOICE_NULLS_ORDERING = "nullsOrdering"; //$NON-NLS-1$

	/** design constant: nulls ordering nulls lowest */
	String NULLS_ORDERING_NULLS_LOWEST = "nulls lowest"; //$NON-NLS-1$

	/** design constant: nulls ordering nulls highest */
	String NULLS_ORDERING_NULLS_HIGHEST = "nulls highest"; //$NON-NLS-1$

	/** design constant: nulls ordering exclude nulls */
	String NULLS_ORDERING_EXCLUDE_NULLS = "exclude nulls"; //$NON-NLS-1$

	/** design constant: choice filter condition type */
	String CHOICE_FILTER_CONDITION_TYPE = "filterConditionType"; //$NON-NLS-1$

	/** design constant: filter condition type slicer */
	String FILTER_CONDITION_TYPE_SLICER = "slicer"; //$NON-NLS-1$

	/** design constant: filter condition type simple */
	String FILTER_CONDITION_TYPE_SIMPLE = "simple"; //$NON-NLS-1$

	/** design constant: choice flyout position */
	String CHOICE_FLYOUT_POSITION = "flyoutPosition"; //$NON-NLS-1$

	/** design constant: flyout position left */
	String FLYOUT_POSITION_LEFT = "left"; //$NON-NLS-1$

	/** design constant: flyout position right */
	String FLYOUT_POSITION_RIGHT = "right"; //$NON-NLS-1$


	/**
	 * Choice constants for reference date type.
	 */
	/** design constant: choice reference date type */
	String CHOICE_REFERENCE_DATE_TYPE = "referenceDateType"; //$NON-NLS-1$

	/** design constant: reference date type today */
	String REFERENCE_DATE_TYPE_TODAY = "today"; //$NON-NLS-1$

	/** design constant: reference date type fixed date */
	String REFERENCE_DATE_TYPE_FIXED_DATE = "fixedDate"; //$NON-NLS-1$

	/** design constant: reference date type ending date in dimension */
	String REFERENCE_DATE_TYPE_ENDING_DATE_IN_DIMENSION = "endingDateInDimension"; //$NON-NLS-1$

}
