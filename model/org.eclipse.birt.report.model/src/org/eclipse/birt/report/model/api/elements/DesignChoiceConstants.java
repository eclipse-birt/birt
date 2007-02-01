/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

public interface DesignChoiceConstants
{

	// fontFamily

	public static final String CHOICE_FONT_FAMILY = "fontFamily"; //$NON-NLS-1$
	public static final String FONT_FAMILY_SERIF = "serif"; //$NON-NLS-1$ 
	public static final String FONT_FAMILY_SANS_SERIF = "sans-serif"; //$NON-NLS-1$ 
	public static final String FONT_FAMILY_CURSIVE = "cursive"; //$NON-NLS-1$ 
	public static final String FONT_FAMILY_FANTASY = "fantasy"; //$NON-NLS-1$
	public static final String FONT_FAMILY_MONOSPACE = "monospace"; //$NON-NLS-1$

	// fontStyle

	public static final String CHOICE_FONT_STYLE = "fontStyle"; //$NON-NLS-1$
	public static final String FONT_STYLE_NORMAL = "normal"; //$NON-NLS-1$
	public static final String FONT_STYLE_ITALIC = "italic"; //$NON-NLS-1$
	public static final String FONT_STYLE_OBLIQUE = "oblique"; //$NON-NLS-1$

	// fontWeight

	public static final String CHOICE_FONT_WEIGHT = "fontWeight"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_NORMAL = "normal"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_BOLD = "bold"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_BOLDER = "bolder"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_LIGHTER = "lighter"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_100 = "100"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_200 = "200"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_300 = "300"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_400 = "400"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_500 = "500"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_600 = "600"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_700 = "700"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_800 = "800"; //$NON-NLS-1$
	public static final String FONT_WEIGHT_900 = "900"; //$NON-NLS-1$

	// fontSize

	public static final String CHOICE_FONT_SIZE = "fontSize"; //$NON-NLS-1$
	public static final String FONT_SIZE_XX_SMALL = "xx-small"; //$NON-NLS-1$
	public static final String FONT_SIZE_X_SMALL = "x-small"; //$NON-NLS-1$
	public static final String FONT_SIZE_SMALL = "small"; //$NON-NLS-1$
	public static final String FONT_SIZE_MEDIUM = "medium"; //$NON-NLS-1$
	public static final String FONT_SIZE_LARGE = "large"; //$NON-NLS-1$
	public static final String FONT_SIZE_X_LARGE = "x-large"; //$NON-NLS-1$
	public static final String FONT_SIZE_XX_LARGE = "xx-large"; //$NON-NLS-1$
	public static final String FONT_SIZE_LARGER = "larger"; //$NON-NLS-1$
	public static final String FONT_SIZE_SMALLER = "smaller"; //$NON-NLS-1$

	// fontVariant

	public static final String CHOICE_FONT_VARIANT = "fontVariant"; //$NON-NLS-1$
	public static final String FONT_VARIANT_NORMAL = "normal"; //$NON-NLS-1$
	public static final String FONT_VARIANT_SMALL_CAPS = "small-caps"; //$NON-NLS-1$

	// backgroundRepeat

	public static final String CHOICE_BACKGROUND_REPEAT = "backgroundRepeat"; //$NON-NLS-1$
	public static final String BACKGROUND_REPEAT_REPEAT = "repeat"; //$NON-NLS-1$
	public static final String BACKGROUND_REPEAT_REPEAT_X = "repeat-x"; //$NON-NLS-1$
	public static final String BACKGROUND_REPEAT_REPEAT_Y = "repeat-y"; //$NON-NLS-1$
	public static final String BACKGROUND_REPEAT_NO_REPEAT = "no-repeat"; //$NON-NLS-1$

	// backgroundAttachment

	public static final String CHOICE_BACKGROUND_ATTACHMENT = "backgroundAttachment"; //$NON-NLS-1$
	public static final String BACKGROUND_ATTACHMENT_SCROLL = "scroll"; //$NON-NLS-1$
	public static final String BACKGROUND_ATTACHMENT_FIXED = "fixed"; //$NON-NLS-1$

	// backgroundPositionX

	public static final String CHOICE_BACKGROUND_POSITION_X = "backgroundPositionX"; //$NON-NLS-1$
	public static final String BACKGROUND_POSITION_LEFT = "left"; //$NON-NLS-1$
	public static final String BACKGROUND_POSITION_RIGHT = "right"; //$NON-NLS-1$
	public static final String BACKGROUND_POSITION_CENTER = "center"; //$NON-NLS-1$

	// backgroundPositionY(BACKGROUND_POSITION_CENTER is already defined)

	public static final String CHOICE_BACKGROUND_POSITION_Y = "backgroundPositionY"; //$NON-NLS-1$
	public static final String BACKGROUND_POSITION_TOP = "top"; //$NON-NLS-1$
	public static final String BACKGROUND_POSITION_BOTTOM = "bottom"; //$NON-NLS-1$

	// transform

	public static final String CHOICE_TRANSFORM = "transform"; //$NON-NLS-1$
	public static final String TRANSFORM_CAPITALIZE = "capitalize"; //$NON-NLS-1$
	public static final String TRANSFORM_UPPERCASE = "uppercase"; //$NON-NLS-1$
	public static final String TRANSFORM_LOWERCASE = "lowercase"; //$NON-NLS-1$
	public static final String TRANSFORM_NONE = "none"; //$NON-NLS-1$

	// normal

	public static final String CHOICE_NORMAL = "normal"; //$NON-NLS-1$
	public static final String NORMAL_NORMAL = "normal"; //$NON-NLS-1$

	// verticalAlign

	public static final String CHOICE_VERTICAL_ALIGN = "verticalAlign"; //$NON-NLS-1$

	/**
	 * @deprecated Now Engine is not support it.
	 */

	public static final String VERTICAL_ALIGN_BASELINE = "baseline"; //$NON-NLS-1$

	/**
	 * @deprecated Now Engine is not support it.
	 */

	public static final String VERTICAL_ALIGN_SUB = "sub"; //$NON-NLS-1$

	/**
	 * @deprecated Now Engine is not support it.
	 */

	public static final String VERTICAL_ALIGN_SUPER = "super"; //$NON-NLS-1$
	public static final String VERTICAL_ALIGN_TOP = "top"; //$NON-NLS-1$

	/**
	 * @deprecated Now Engine is not support it.
	 */

	public static final String VERTICAL_ALIGN_TEXT_TOP = "text-top"; //$NON-NLS-1$
	public static final String VERTICAL_ALIGN_MIDDLE = "middle"; //$NON-NLS-1$
	public static final String VERTICAL_ALIGN_BOTTOM = "bottom"; //$NON-NLS-1$

	/**
	 * @deprecated Now Engine is not support it.
	 */

	public static final String VERTICAL_ALIGN_TEXT_BOTTOM = "text-bottom"; //$NON-NLS-1$

	// whiteSpace

	public static final String CHOICE_WHITE_SPACE = "whiteSpace"; //$NON-NLS-1$
	public static final String WHITE_SPACE_NORMAL = "normal"; //$NON-NLS-1$
	public static final String WHITE_SPACE_PRE = "pre"; //$NON-NLS-1$
	public static final String WHITE_SPACE_NOWRAP = "nowrap"; //$NON-NLS-1$

	// display

	public static final String CHOICE_DISPLAY = "display"; //$NON-NLS-1$
	public static final String DISPLAY_BLOCK = "block"; //$NON-NLS-1$
	public static final String DISPLAY_INLINE = "inline"; //$NON-NLS-1$
	public static final String DISPLAY_NONE = "none"; //$NON-NLS-1$

	// pageBreakAfter

	public static final String CHOICE_PAGE_BREAK_AFTER = "pageBreakAfter"; //$NON-NLS-1$
	public static final String PAGE_BREAK_AFTER_AUTO = "auto"; //$NON-NLS-1$
	public static final String PAGE_BREAK_AFTER_ALWAYS = "always"; //$NON-NLS-1$
	public static final String PAGE_BREAK_AFTER_AVOID = "avoid"; //$NON-NLS-1$
	public static final String PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST = "always-excluding-last"; //$NON-NLS-1$

	// pageBreakBefore

	public static final String CHOICE_PAGE_BREAK_BEFORE = "pageBreakBefore"; //$NON-NLS-1$
	public static final String PAGE_BREAK_BEFORE_AUTO = "auto"; //$NON-NLS-1$
	public static final String PAGE_BREAK_BEFORE_ALWAYS = "always"; //$NON-NLS-1$
	public static final String PAGE_BREAK_BEFORE_AVOID = "avoid"; //$NON-NLS-1$
	public static final String PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST = "always-excluding-first"; //$NON-NLS-1$

	// pageBreakInside

	public static final String CHOICE_PAGE_BREAK_INSIDE = "pageBreakInside"; //$NON-NLS-1$
	public static final String PAGE_BREAK_INSIDE_AVOID = "avoid"; //$NON-NLS-1$
	public static final String PAGE_BREAK_INSIDE_AUTO = "auto"; //$NON-NLS-1$

	// margin

	public static final String CHOICE_MARGIN = "margin"; //$NON-NLS-1$
	public static final String MARGIN_AUTO = "auto"; //$NON-NLS-1$

	// textUnderline

	public static final String CHOICE_TEXT_UNDERLINE = "textUnderline"; //$NON-NLS-1$
	public static final String TEXT_UNDERLINE_NONE = "none"; //$NON-NLS-1$
	public static final String TEXT_UNDERLINE_UNDERLINE = "underline"; //$NON-NLS-1$

	// textOverline

	public static final String CHOICE_TEXT_OVERLINE = "textOverline"; //$NON-NLS-1$
	public static final String TEXT_OVERLINE_NONE = "none"; //$NON-NLS-1$
	public static final String TEXT_OVERLINE_OVERLINE = "overline"; //$NON-NLS-1$

	// textLineThrough

	public static final String CHOICE_TEXT_LINE_THROUGH = "textLineThrough"; //$NON-NLS-1$
	public static final String TEXT_LINE_THROUGH_NONE = "none"; //$NON-NLS-1$
	public static final String TEXT_LINE_THROUGH_LINE_THROUGH = "line-through"; //$NON-NLS-1$

	// lineWidth

	public static final String CHOICE_LINE_WIDTH = "lineWidth"; //$NON-NLS-1$
	public static final String LINE_WIDTH_THIN = "thin"; //$NON-NLS-1$
	public static final String LINE_WIDTH_MEDIUM = "medium"; //$NON-NLS-1$
	public static final String LINE_WIDTH_THICK = "thick"; //$NON-NLS-1$

	// lineStyle

	public static final String CHOICE_LINE_STYLE = "lineStyle"; //$NON-NLS-1$
	public static final String LINE_STYLE_NONE = "none"; //$NON-NLS-1$
	public static final String LINE_STYLE_SOLID = "solid"; //$NON-NLS-1$
	public static final String LINE_STYLE_DOTTED = "dotted"; //$NON-NLS-1$
	public static final String LINE_STYLE_DASHED = "dashed"; //$NON-NLS-1$
	public static final String LINE_STYLE_DOUBLE = "double"; //$NON-NLS-1$
	public static final String LINE_STYLE_GROOVE = "groove"; //$NON-NLS-1$
	public static final String LINE_STYLE_RIDGE = "ridge"; //$NON-NLS-1$
	public static final String LINE_STYLE_INSET = "inset"; //$NON-NLS-1$
	public static final String LINE_STYLE_OUTSET = "outset"; //$NON-NLS-1$

	// units

	public static final String CHOICE_UNITS = "units"; //$NON-NLS-1$
	public static final String UNITS_IN = "in"; //$NON-NLS-1$
	public static final String UNITS_CM = "cm"; //$NON-NLS-1$
	public static final String UNITS_MM = "mm"; //$NON-NLS-1$
	public static final String UNITS_PT = "pt"; //$NON-NLS-1$
	public static final String UNITS_PC = "pc"; //$NON-NLS-1$
	public static final String UNITS_EM = "em"; //$NON-NLS-1$
	public static final String UNITS_EX = "ex"; //$NON-NLS-1$
	public static final String UNITS_PX = "px"; //$NON-NLS-1$
	public static final String UNITS_PERCENTAGE = "%"; //$NON-NLS-1$

	// paramType

	public static final String CHOICE_PARAM_TYPE = "paramType"; //$NON-NLS-1$
	public static final String PARAM_TYPE_STRING = "string"; //$NON-NLS-1$
	public static final String PARAM_TYPE_FLOAT = "float"; //$NON-NLS-1$
	public static final String PARAM_TYPE_DECIMAL = "decimal"; //$NON-NLS-1$
	public static final String PARAM_TYPE_DATETIME = "dateTime"; //$NON-NLS-1$
	public static final String PARAM_TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$
	public static final String PARAM_TYPE_INTEGER = "integer"; //$NON-NLS-1$
	public static final String PARAM_TYPE_ANY = "any"; //$NON-NLS-1$

	// paramValueType

	public static final String CHOICE_PARAM_VALUE_TYPE = "paramType"; //$NON-NLS-1$
	public static final String PARAM_VALUE_TYPE_STATIC = "static"; //$NON-NLS-1$
	public static final String PARAM_VALUE_TYPE_DYNAMIC = "dynamic"; //$NON-NLS-1$

	// paramControl

	public static final String CHOICE_PARAM_CONTROL = "paramControl"; //$NON-NLS-1$
	public static final String PARAM_CONTROL_TEXT_BOX = "text-box"; //$NON-NLS-1$
	public static final String PARAM_CONTROL_LIST_BOX = "list-box"; //$NON-NLS-1$
	public static final String PARAM_CONTROL_RADIO_BUTTON = "radio-button"; //$NON-NLS-1$
	public static final String PARAM_CONTROL_CHECK_BOX = "check-box"; //$NON-NLS-1$

	// textAlign

	public static final String CHOICE_TEXT_ALIGN = "textAlign"; //$NON-NLS-1$
	public static final String TEXT_ALIGN_LEFT = "left"; //$NON-NLS-1$
	public static final String TEXT_ALIGN_CENTER = "center"; //$NON-NLS-1$
	public static final String TEXT_ALIGN_RIGHT = "right"; //$NON-NLS-1$
	public static final String TEXT_ALIGN_JUSTIFY = "justify"; //$NON-NLS-1$

	// pageSize

	public static final String CHOICE_PAGE_SIZE = "pageSize"; //$NON-NLS-1$
	public static final String PAGE_SIZE_CUSTOM = "custom"; //$NON-NLS-1$
	public static final String PAGE_SIZE_US_LETTER = "us-letter"; //$NON-NLS-1$
	public static final String PAGE_SIZE_US_LEGAL = "us-legal"; //$NON-NLS-1$
	public static final String PAGE_SIZE_A4 = "a4"; //$NON-NLS-1$

	// pageOrientation

	public static final String CHOICE_PAGE_ORIENTATION = "pageOrientation"; //$NON-NLS-1$
	public static final String PAGE_ORIENTATION_AUTO = "auto"; //$NON-NLS-1$
	public static final String PAGE_ORIENTATION_PORTRAIT = "portrait"; //$NON-NLS-1$
	public static final String PAGE_ORIENTATION_LANDSCAPE = "landscape"; //$NON-NLS-1$

	// interval

	public static final String CHOICE_INTERVAL = "interval"; //$NON-NLS-1$
	public static final String INTERVAL_NONE = "none"; //$NON-NLS-1$
	public static final String INTERVAL_PREFIX = "prefix"; //$NON-NLS-1$
	public static final String INTERVAL_YEAR = "year"; //$NON-NLS-1$
	public static final String INTERVAL_QUARTER = "quarter"; //$NON-NLS-1$
	public static final String INTERVAL_MONTH = "month"; //$NON-NLS-1$
	public static final String INTERVAL_WEEK = "week"; //$NON-NLS-1$
	public static final String INTERVAL_DAY = "day"; //$NON-NLS-1$
	public static final String INTERVAL_HOUR = "hour"; //$NON-NLS-1$
	public static final String INTERVAL_MINUTE = "minute"; //$NON-NLS-1$
	public static final String INTERVAL_SECOND = "second"; //$NON-NLS-1$
	public static final String INTERVAL_INTERVAL = "interval"; //$NON-NLS-1$

	// sortDirection

	public static final String CHOICE_SORT_DIRECTION = "sortDirection"; //$NON-NLS-1$
	public static final String SORT_DIRECTION_ASC = "asc"; //$NON-NLS-1$
	public static final String SORT_DIRECTION_DESC = "desc"; //$NON-NLS-1$

	// mapOperator

	public static final String CHOICE_MAP_OPERATOR = "mapOperator"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_EQ = "eq"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_NE = "ne"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_LT = "lt"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_LE = "le"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_GE = "ge"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_GT = "gt"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_BETWEEN = "between"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_NOT_BETWEEN = "not-between"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_NULL = "is-null"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_NOT_NULL = "is-not-null"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_TRUE = "is-true"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_FALSE = "is-false"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_LIKE = "like"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_MATCH = "match"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_TOP_N = "top-n"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_BOTTOM_N = "bottom-n"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_TOP_PERCENT = "top-percent"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_BOTTOM_PERCENT = "bottom-percent"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_NOT_LIKE = "not-like"; //$NON-NLS-1$
	public static final String MAP_OPERATOR_NOT_MATCH = "not-match"; //$NON-NLS-1$

	public static final String MAP_OPERATOR_ANY = "any"; //$NON-NLS-1$

	// imageSize

	public static final String CHOICE_IMAGE_SIZE = "imageSize"; //$NON-NLS-1$
	public static final String IMAGE_SIZE_SIZE_TO_IMAGE = "size-to-image"; //$NON-NLS-1$
	public static final String IMAGE_SIZE_SCALE_TO_ITEM = "scale-to-item"; //$NON-NLS-1$
	public static final String IMAGE_SIZE_CLIP = "clip"; //$NON-NLS-1$

	// lineOrientation

	public static final String CHOICE_LINE_ORIENTATION = "lineOrientation"; //$NON-NLS-1$
	public static final String LINE_ORIENTATION_HORIZONTAL = "horizontal"; //$NON-NLS-1$
	public static final String LINE_ORIENTATION_VERTICAL = "vertical"; //$NON-NLS-1$

	// sectionAlign

	public static final String CHOICE_SECTION_ALIGN = "sectionAlign"; //$NON-NLS-1$
	public static final String SECTION_ALIGN_LEFT = "left"; //$NON-NLS-1$
	public static final String SECTION_ALIGN_CENTER = "center"; //$NON-NLS-1$
	public static final String SECTION_ALIGN_RIGHT = "right"; //$NON-NLS-1$

	// dropType

	public static final String CHOICE_DROP_TYPE = "dropType"; //$NON-NLS-1$
	public static final String DROP_TYPE_NONE = "none"; //$NON-NLS-1$
	public static final String DROP_TYPE_DETAIL = "detail"; //$NON-NLS-1$
	public static final String DROP_TYPE_ALL = "all"; //$NON-NLS-1$

	// imageType

	public static final String CHOICE_IMAGE_TYPE = "imageType"; //$NON-NLS-1$
	public static final String IMAGE_TYPE_IMAGE_BMP = "image/bmp"; //$NON-NLS-1$
	public static final String IMAGE_TYPE_IMAGE_GIF = "image/gif"; //$NON-NLS-1$
	public static final String IMAGE_TYPE_IMAGE_PNG = "image/png"; //$NON-NLS-1$
	public static final String IMAGE_TYPE_IMAGE_X_PNG = "image/x-png"; //$NON-NLS-1$
	public static final String IMAGE_TYPE_IMAGE_JPEG = "image/jpeg"; //$NON-NLS-1$

	// lineSpacing

	public static final String CHOICE_LINE_SPACING = "lineSpacing"; //$NON-NLS-1$
	public static final String LINE_SPACING_LINES = "lines"; //$NON-NLS-1$
	public static final String LINE_SPACING_EXACT = "exact"; //$NON-NLS-1$

	// actionLinkTyp

	public static final String CHOICE_ACTION_LINK_TYPE = "actionLinkType"; //$NON-NLS-1$
	public static final String ACTION_LINK_TYPE_NONE = "none"; //$NON-NLS-1$
	public static final String ACTION_LINK_TYPE_HYPERLINK = "hyperlink"; //$NON-NLS-1$
	public static final String ACTION_LINK_TYPE_DRILL_THROUGH = "drill-through"; //$NON-NLS-1$
	public static final String ACTION_LINK_TYPE_BOOKMARK_LINK = "bookmark-link"; //$NON-NLS-1$

	// actionFormatType

	public static final String CHOICE_ACTION_FORMAT_TYPE = "actionFormatType"; //$NON-NLS-1$
	public static final String ACTION_FORMAT_TYPE_HTML = "html"; //$NON-NLS-1$
	public static final String ACTION_FORMAT_TYPE_PDF = "pdf"; //$NON-NLS-1$

	// ContentType for TextItem

	public static final String CHOICE_TEXT_CONTENT_TYPE = "textContentType"; //$NON-NLS-1$
	public static final String TEXT_CONTENT_TYPE_AUTO = "auto"; //$NON-NLS-1$
	public static final String TEXT_CONTENT_TYPE_PLAIN = "plain"; //$NON-NLS-1$
	public static final String TEXT_CONTENT_TYPE_HTML = "html"; //$NON-NLS-1$
	public static final String TEXT_CONTENT_TYPE_RTF = "rtf"; //$NON-NLS-1$

	// ContentType for TextDataItem

	public static final String CHOICE_TEXT_DATA_CONTENT_TYPE = "textDataContentType"; //$NON-NLS-1$
	public static final String TEXT_DATA_CONTENT_TYPE_AUTO = "auto"; //$NON-NLS-1$
	public static final String TEXT_DATA_CONTENT_TYPE_PLAIN = "plain"; //$NON-NLS-1$
	public static final String TEXT_DATA_CONTENT_TYPE_HTML = "html"; //$NON-NLS-1$
	public static final String TEXT_DATA_CONTENT_TYPE_RTF = "rtf"; //$NON-NLS-1$

	// Pagination orphans

	public static final String CHOICE_OPRHANS = "orphans"; //$NON-NLS-1$
	public static final String ORPHANS_INHERIT = "inherit"; //$NON-NLS-1$

	// Pagination widows

	public static final String CHOICE_WIDOWS = "widows"; //$NON-NLS-1$
	public static final String WIDOWS_INHERIT = "inherit"; //$NON-NLS-1$

	/**
	 * FormatType -- The target output format. The first constant is the name of
	 * FormatType choice. The followed constants are valid choice values of
	 * FormatType.
	 */

	public static final String CHOICE_FORMAT_TYPE = "formatType"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_ALL = "all"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_VIEWER = "viewer"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_EMAIL = "email"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_PRINT = "print"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_PDF = "pdf"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_RTF = "rtf"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_REPORTLET = "reportlet"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_EXCEL = "excel"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_WORD = "word"; //$NON-NLS-1$
	public static final String FORMAT_TYPE_POWERPOINT = "powerpoint"; //$NON-NLS-1$

	/**
	 * ImageRefType -- The image reference type. The first constant is the name
	 * of ImageRefType choice. The followed constants are valid choice values of
	 * ImageRefType.
	 */

	public static final String CHOICE_IMAGE_REF_TYPE = "imageRefType"; //$NON-NLS-1$
	public static final String IMAGE_REF_TYPE_NONE = "none"; //$NON-NLS-1$
	public static final String IMAGE_REF_TYPE_FILE = "file"; //$NON-NLS-1$
	public static final String IMAGE_REF_TYPE_URL = "url"; //$NON-NLS-1$
	public static final String IMAGE_REF_TYPE_EXPR = "expr"; //$NON-NLS-1$
	public static final String IMAGE_REF_TYPE_EMBED = "embed"; //$NON-NLS-1$

	/**
	 * propertyMaskType -- The choice for the property mask. The first constant
	 * is the name of propertyMaskType choice. The followed constants are valid
	 * choice values of propertyMaskType.
	 */

	public static final String CHOICE_PROPERTY_MASK_TYPE = "propertyMaskType"; //$NON-NLS-1$
	public static final String PROPERTY_MASK_TYPE_CHANGE = "change"; //$NON-NLS-1$
	public static final String PROPERTY_MASK_TYPE_LOCK = "lock"; //$NON-NLS-1$
	public static final String PROPERTY_MASK_TYPE_HIDE = "hide"; //$NON-NLS-1$

	/**
	 * scalarParamAlign -- The choice for the scalarParamter alignment. The
	 * first constant is the name of scalarParamAlign choice. The followed
	 * constants are valid choice values of scalarParamAlign.
	 */

	public static final String CHOICE_SCALAR_PARAM_ALIGN = "scalarParamAlign"; //$NON-NLS-1$
	public static final String SCALAR_PARAM_ALIGN_AUTO = "auto"; //$NON-NLS-1$
	public static final String SCALAR_PARAM_ALIGN_LEFT = "left"; //$NON-NLS-1$
	public static final String SCALAR_PARAM_ALIGN_CENTER = "center"; //$NON-NLS-1$
	public static final String SCALAR_PARAM_ALIGN_RIGHT = "right"; //$NON-NLS-1$

	/*
	 * columnDataType -- The column data type The first constant is the name of
	 * columnDataType choice. The followed constants are valid choice values of
	 * columnDataType.
	 */

	public static final String CHOICE_COLUMN_DATA_TYPE = "columnDataType"; //$NON-NLS-1$
	public static final String COLUMN_DATA_TYPE_ANY = "any"; //$NON-NLS-1$
	public static final String COLUMN_DATA_TYPE_INTEGER = "integer"; //$NON-NLS-1$
	public static final String COLUMN_DATA_TYPE_STRING = "string"; //$NON-NLS-1$
	public static final String COLUMN_DATA_TYPE_DATETIME = "date-time"; //$NON-NLS-1$
	public static final String COLUMN_DATA_TYPE_DECIMAL = "decimal"; //$NON-NLS-1$
	public static final String COLUMN_DATA_TYPE_FLOAT = "float"; //$NON-NLS-1$
	public static final String COLUMN_DATA_TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$
	
	/**
	 * searchType -- The search type for column hint The first constant is the
	 * name of searchType choice. The followed constants are valid choice values
	 * of searchType.
	 */

	public static final String CHOICE_SEARCH_TYPE = "searchType"; //$NON-NLS-1$
	public static final String SEARCH_TYPE_ANY = "any"; //$NON-NLS-1$
	public static final String SEARCH_TYPE_INDEXED = "indexed"; //$NON-NLS-1$
	public static final String SEARCH_TYPE_NONE = "none"; //$NON-NLS-1$

	/**
	 * exportType -- The export type for column hint The first constant is the
	 * name of exportType choice. The followed constants are valid choice values
	 * of exportType.
	 */

	public static final String CHOICE_EXPORT_TYPE = "exportType"; //$NON-NLS-1$
	public static final String EXPORT_TYPE_NONE = "none"; //$NON-NLS-1$
	public static final String EXPORT_TYPE_IF_REALIZED = "if-realized"; //$NON-NLS-1$
	public static final String EXPORT_TYPE_ALWAYS = "always"; //$NON-NLS-1$

	/**
	 * analysisType -- The analysis type for column hint The first constant is
	 * the name of analysisType choice. The followed constants are valid choice
	 * values of analysisType.
	 */

	public static final String CHOICE_ANALYSIS_TYPE = "analysisType"; //$NON-NLS-1$
	public static final String ANALYSIS_TYPE_AUTO = "auto"; //$NON-NLS-1$
	public static final String ANALYSIS_TYPE_DIMENSION = "dimension"; //$NON-NLS-1$
	public static final String ANALYSIS_TYPE_MEASURE = "measure"; //$NON-NLS-1$
	public static final String ANALYSIS_TYPE_DETAIL = "detail"; //$NON-NLS-1$
	public static final String ANALYSIS_TYPE_NONE = "none"; //$NON-NLS-1$

	/**
	 * filterOperator -- The filter operator for filter condition The first
	 * constant is the name of filterOperator choice. The followed constants are
	 * valid choice values of filterOperator.
	 */

	public static final String CHOICE_FILTER_OPERATOR = "filterOperator"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_EQ = "eq"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_NE = "ne"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_LT = "lt"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_LE = "le"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_GE = "ge"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_GT = "gt"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_BETWEEN = "between"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_NOT_BETWEEN = "not-between"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_NULL = "is-null"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_NOT_NULL = "is-not-null"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_TRUE = "is-true"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_FALSE = "is-false"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_LIKE = "like"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_TOP_N = "top-n"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_BOTTOM_N = "bottom-n"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_TOP_PERCENT = "top-percent"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_BOTTOM_PERCENT = "bottom-percent"; //$NON-NLS-1$

	/**
	 * @deprecated in BIRT 2.1. This operator is not supported.
	 */

	public static final String FILTER_OPERATOR_ANY = "any"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_MATCH = "match"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_NOT_LIKE = "not-like"; //$NON-NLS-1$
	public static final String FILTER_OPERATOR_NOT_MATCH = "not-match"; //$NON-NLS-1$

	/**
	 * columnAlign -- The column alignment The first constant is the name of
	 * columnAlign choice. The followed constants are valid choice values of
	 * columnAlign.
	 */

	public static final String CHOICE_COLUMN_ALIGN = "columnAlign"; //$NON-NLS-1$
	public static final String COLUMN_ALIGN_LEFT = "left"; //$NON-NLS-1$
	public static final String COLUMN_ALIGN_CENTER = "center"; //$NON-NLS-1$
	public static final String COLUMN_ALIGN_RIGHT = "right"; //$NON-NLS-1$

	/**
	 * queryFrom -- Where the query is from The first constant is the name of
	 * queryFrom choice. The followed constants are valid choice values of
	 * queryFrom.
	 */

	public static final String CHOICE_QUERY_CHOICE_TYPE = "queryChoiceType"; //$NON-NLS-1$
	public static final String QUERY_CHOICE_TYPE_NONE = "none"; //$NON-NLS-1$
	public static final String QUERY_CHOICE_TYPE_TEXT = "text"; //$NON-NLS-1$
	public static final String QUERY_CHOICE_TYPE_SCRIPT = "script"; //$NON-NLS-1$

	/**
	 * numberFormat -- the number format The first constant is the name of
	 * numberFormat choice. The followed constants are valid choice values of
	 * numberFormat.
	 */

	public static final String CHOICE_NUMBER_FORMAT_TYPE = "numberFormat"; //$NON-NLS-1$
	public static final String NUMBER_FORMAT_TYPE_UNFORMATTED = "Unformatted"; //$NON-NLS-1$	
	public static final String NUMBER_FORMAT_TYPE_GENERAL_NUMBER = "General Number"; //$NON-NLS-1$
	public static final String NUMBER_FORMAT_TYPE_CURRENCY = "Currency"; //$NON-NLS-1$
	public static final String NUMBER_FORMAT_TYPE_FIXED = "Fixed"; //$NON-NLS-1$
	public static final String NUMBER_FORMAT_TYPE_PERCENT = "Percent"; //$NON-NLS-1$
	public static final String NUMBER_FORMAT_TYPE_SCIENTIFIC = "Scientific"; //$NON-NLS-1$
	public static final String NUMBER_FORMAT_TYPE_STANDARD = "Standard"; //$NON-NLS-1$
	public static final String NUMBER_FORMAT_TYPE_CUSTOM = "Custom"; //$NON-NLS-1$

	/**
	 * dateTimeFormat -- the date/time format The first constant is the name of
	 * dateTimeFormat choice. The followed constants are valid choice values of
	 * dateTimeFormat.
	 */

	public static final String CHOICE_DATETIME_FORMAT_TYPE = "dateTimeFormat"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_UNFORMATTED = "Unformatted"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_GENERAL_DATE = "General Date"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_LONG_DATE = "Long Date"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_MUDIUM_DATE = "Medium Date"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_SHORT_DATE = "Short Date"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_LONG_TIME = "Long Time"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_MEDIUM_TIME = "Medium Time"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_SHORT_TIME = "Short Time"; //$NON-NLS-1$
	public static final String DATETIEM_FORMAT_TYPE_CUSTOM = "Custom"; //$NON-NLS-1$

	/**
	 * stringFormat -- the string format The first constant is the name of
	 * stringFormat choice. The followed constants are valid choice values of
	 * stringFormat.
	 */

	public static final String CHOICE_STRING_FORMAT_TYPE = "stringFormat"; //$NON-NLS-1$
	public static final String STRING_FORMAT_TYPE_UNFORMATTED = "Unformatted"; //$NON-NLS-1$
	public static final String STRING_FORMAT_TYPE_UPPERCASE = ">"; //$NON-NLS-1$
	public static final String STRING_FORMAT_TYPE_LOWERCASE = "<"; //$NON-NLS-1$
	public static final String STRING_FORMAT_TYPE_CUSTOM = "Custom"; //$NON-NLS-1$
	public static final String STRING_FORMAT_TYPE_ZIP_CODE = "Zip Code"; //$NON-NLS-1$	
	public static final String STRING_FORMAT_TYPE_ZIP_CODE_4 = "Zip Code + 4"; //$NON-NLS-1$	
	public static final String STRING_FORMAT_TYPE_PHONE_NUMBER = "Phone Number"; //$NON-NLS-1$
	public static final String STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER = "Social Security Number"; //$NON-NLS-1$

	// targetNames

	public static final String CHOICE_TARGET_NAMES_TYPE = "targetNames"; //$NON-NLS-1$
	public static final String TARGET_NAMES_TYPE_BLANK = "_blank"; //$NON-NLS-1$
	public static final String TARGET_NAMES_TYPE_SELF = "_self"; //$NON-NLS-1$
	public static final String TARGET_NAMES_TYPE_PARENT = "_parent"; //$NON-NLS-1$
	public static final String TARGET_NAMES_TYPE_TOP = "_top"; //$NON-NLS-1$

	/**
	 * templateElementType -- the template element type The first constant is
	 * the name of the templateElementType choice set. The followed constants
	 * are valid choice values of the templateElementType.
	 */

	public static final String CHOICE_TEMPLATE_ELEMENT_TYPE = "templateElementType"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_TABLE = "Table"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_FREEFORM = "FreeForm"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_DATA = "Data"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_GRID = "Grid"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_IMAGE = "Image"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_LABEL = "Label"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_LIST = "List"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_TEXT = "Text"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_EXTENDED_ITEM = "ExtendedItem"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_TEXT_DATA = "TextData"; //$NON-NLS-1$
	public static final String TEMPLATE_ELEMENT_TYPE_DATA_SET = "DataSet"; //$NON-NLS-1$

	/**
	 * sortType -- the sort type. The first constant is the name of the sortType
	 * choice set. The followed constants are valid choice values of the
	 * sortType.
	 */

	public static final String CHOICE_SORT_TYPE = "sortType"; //$NON-NLS-1$
	public static final String SORT_TYPE_NONE = "none"; //$NON-NLS-1$
	public static final String SORT_TYPE_SORT_ON_GROUP_KEY = "sortOnGroupkey"; //$NON-NLS-1$
	public static final String SORT_TYPE_COMPLEX_SORT = "complexSort"; //$NON-NLS-1$

	/**
	 * joinType -- join type of join condition.
	 */

	public static final String CHOICE_JOIN_TYPE = "joinType"; //$NON-NLS-1$
	public static final String JOIN_TYPE_INNER = "inner"; //$NON-NLS-1$
	public static final String JOIN_TYPE_LEFT_OUT = "left-out"; //$NON-NLS-1$
	public static final String JOIN_TYPE_RIGHT_OUT = "right-out"; //$NON-NLS-1$
	public static final String JOIN_TYPE_FULL_OUT = "full-out"; //$NON-NLS-1$

	/**
	 * joinOperator -- join operator of join condition.
	 */

	public static final String CHOICE_JOIN_OPERATOR = "joinOperator"; //$NON-NLS-1$
	public static final String JOIN_OPERATOR_EQALS = "eq"; //$NON-NLS-1$

	/**
	 * actionTargetFileType -- target type of the linked file
	 */

	public static final String CHOICE_ACTION_TARGET_FILE_TYPE = "actionTargetFileType"; //$NON-NLS-1$
	public static final String ACTION_TARGET_FILE_TYPE_REPORT_DESIGN = "report-design"; //$NON-NLS-1$
	public static final String ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT = "report-document"; //$NON-NLS-1$

	/**
	 * actionBookmarkType -- target bookmark type
	 */

	public static final String CHOICE_ACTION_BOOKMARK_TYPE = "actionBookmarkType"; //$NON-NLS-1$
	public static final String ACTION_BOOKMARK_TYPE_BOOKMARK = "bookmark"; //$NON-NLS-1$
	public static final String ACTION_BOOKMARK_TYPE_TOC = "toc"; //$NON-NLS-1$

	/**
	 * autotextType -- type of auto text
	 */

	public static final String CHOICE_AUTO_TEXT_TYPE = "autoTextType"; //$NON-NLS-1$
	public static final String AUTO_TEXT_PAGE_NUMBER = "page-number"; //$NON-NLS-1$
	public static final String AUTO_TEXT_TOTAL_PAGE = "total-page"; //$NON-NLS-1$

	/**
	 * dataSetMode -- the mode to support data sets. Can be single data set and
	 */

	public static final String CHOICE_DATA_SET_MODE_TYPE = "dataSetMode"; //$NON-NLS-1$
	public static final String DATA_SET_MODE_SINGLE = "single"; //$NON-NLS-1$
	public static final String DATA_SET_MODE_MULTIPLE = "multiple"; //$NON-NLS-1$

	/**
	 * filterTarget -- filter target type.
	 */

	public static final String CHOICE_FILTER_TARGET = "filterTarget"; //$NON-NLS-1$
	public static final String FILTER_TARGET_DATA_SET = "DataSet"; //$NON-NLS-1$
	public static final String FILTER_TARGET_RESULT_SET = "ResultSet"; //$NON-NLS-1$

	/**
	 * View action --view action type.
	 */

	public static final String CHOICE_VIEW_ACTION = "viewAction"; //$NON-NLS-1$
	public static final String VIEW_ACTION_NO_CHANGE = "NoChange"; //$NON-NLS-1$
	public static final String VIEW_ACTION_CHANGED = "Changed"; //$NON-NLS-1$
	public static final String VIEW_ACTION_ADDED = "Added"; //$NON-NLS-1$
	public static final String VIEW_ACTION_DELETED = "Deleted"; //$NON-NLS-1$
	
	/**
	 * measure Function -- measure function type.
	 */
	
	public static final String CHOICE_MEASURE_FUNCTION = "measureFunction"; //$NON-NLS-1$
	public static final String MEASURE_FUNCTION_SUM = "sum"; //$NON-NLS-1$
	public static final String MEASURE_FUNCTION_COUNT = "count"; //$NON-NLS-1$
	public static final String MEASURE_FUNCTION_MIN = "min"; //$NON-NLS-1$
	public static final String MEASURE_FUNCTION_MAX = "max"; //$NON-NLS-1$
	
	/**
	 * Level type constants.
	 */
	String CHOICE_LEVEL_TYPE = "levelType"; //$NON-NLS-1$
	String LEVEL_TYPE_DYNAMIC = "dynamic"; //$NON-NLS-1$
	String LEVEL_TYPE_MIRRORED = "mirrored"; //$NON-NLS-1$
};
