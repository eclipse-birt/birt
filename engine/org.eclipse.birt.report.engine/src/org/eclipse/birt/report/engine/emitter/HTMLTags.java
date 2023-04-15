/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter;

/**
 *
 * define all html tags used in html emitter
 */
public class HTMLTags {

	// attribute in html
	public static final String ATTR_CLASS = "class"; //$NON-NLS-1$
	public static final String ATTR_LEFT = "left"; //$NON-NLS-1$
	public static final String ATTR_TOP = "top"; //$NON-NLS-1$
	public static final String ATTR_WIDTH = "width"; //$NON-NLS-1$
	public static final String ATTR_MIN_WIDTH = "min-width"; //$NON-NLS-1$
	public static final String ATTR_MIN_HEIGHT = "min-height"; //$NON-NLS-1$
	public static final String ATTR_HEIGHT = "height"; //$NON-NLS-1$
	public static final String ATTR_HTML_DIR = "dir"; //$NON-NLS-1$

	public static final String ATTR_VERTICAL_ALIGN = "vertical-align"; //$NON-NLS-1$
	public static final String ATTR_LINE_HEIGHT = "line-height"; //$NON-NLS-1$
	public static final String ATTR_ORPHANS = "orphans"; //$NON-NLS-1$
	public static final String ATTR_WIDOWS = "widows"; //$NON-NLS-1$
	public static final String ATTR_PAGE_BREAK_BEFORE = "page-break-before"; //$NON-NLS-1$
	public static final String ATTR_PAGE_BREAK_AFTER = "page-break-after"; //$NON-NLS-1$
	public static final String ATTR_PAGE_BREAK_INSIDE = "page-break-inside"; //$NON-NLS-1$
	public static final String ATTR_COLOR = "color"; //$NON-NLS-1$
	public static final String ATTR_MARGIN = "margin"; //$NON-NLS-1$
	public static final String ATTR_MARGIN_TOP = "margin-top"; //$NON-NLS-1$
	public static final String ATTR_MARGIN_RIGHT = "margin-right"; //$NON-NLS-1$
	public static final String ATTR_MARGIN_BOTTOM = "margin-bottom"; //$NON-NLS-1$
	public static final String ATTR_MARGIN_LEFT = "margin-left"; //$NON-NLS-1$

	public static final String ATTR_BACKGROUND_COLOR = "background-color"; //$NON-NLS-1$
	public static final String ATTR_BACKGROUND_IMAGE = "background-image"; //$NON-NLS-1$
	public static final String ATTR_BACKGROUND_REPEAT = "background-repeat"; //$NON-NLS-1$
	public static final String ATTR_BACKGROUND_ATTACHEMNT = "background-attachment"; //$NON-NLS-1$
	public static final String ATTR_BACKGROUND_POSITION = "background-position"; //$NON-NLS-1$
	public static final String ATTR_BACKGROUND_SIZE = "background-size"; //$NON-NLS-1$

	public static final String ATTR_PADDING = "padding"; //$NON-NLS-1$
	public static final String ATTR_PADDING_TOP = "padding-top"; //$NON-NLS-1$
	public static final String ATTR_PADDING_RIGHT = "padding-right"; //$NON-NLS-1$
	public static final String ATTR_PADDING_BOTTOM = "padding-bottom"; //$NON-NLS-1$
	public static final String ATTR_PADDING_LEFT = "padding-left"; //$NON-NLS-1$

	public static final String ATTR_BOTTOM_COLOR = "border-bottom-color"; //$NON-NLS-1$
	public static final String ATTR_BORDER_BOTTOM = "border-bottom"; //$NON-NLS-1$
	public static final String ATTR_BORDER_BOTTOM_STYLE = "border-bottom-style"; //$NON-NLS-1$
	public static final String ATTR_BORDER_BOTTOM_WIDTH = "border-bottom-width"; //$NON-NLS-1$
	public static final String ATTR_BORDER_LEFT = "border-left"; //$NON-NLS-1$
	public static final String ATTR_BORDER_LEFT_COLOR = "border-left-color"; //$NON-NLS-1$
	public static final String ATTR_BORDER_LEFT_STYLE = "border-left-style"; //$NON-NLS-1$
	public static final String ATTR_BORDER_LEFT_WIDTH = "border-left-width"; //$NON-NLS-1$
	public static final String ATTR_BORDER_RIGHT = "border-right"; //$NON-NLS-1$
	public static final String ATTR_BORDER_RIGHT_COLOR = "border-right-color"; //$NON-NLS-1$
	public static final String ATTR_BORDER_RIGHT_STYLE = "border-right-style"; //$NON-NLS-1$
	public static final String ATTR_BORDER_RIGHT_WIDTH = "border-right-width"; //$NON-NLS-1$
	public static final String ATTR_BORDER = "border"; //$NON-NLS-1$
	public static final String ATTR_BORDER_TOP = "border-top"; //$NON-NLS-1$
	public static final String ATTR_BORDER_TOP_COLOR = "border-top-color"; //$NON-NLS-1$
	public static final String ATTR_BORDER_TOP_STYLE = "border-top-style"; //$NON-NLS-1$
	public static final String ATTR_BORDER_TOP_WIDTH = "border-top-width"; //$NON-NLS-1$

	public static final String ATTR_TEXT_ALIGN = "text-align"; //$NON-NLS-1$
	public static final String ATTR_TEXT_INDENT = "text-indent"; //$NON-NLS-1$
	public static final String ATTR_LETTER_SPACING = "letter-spacing"; //$NON-NLS-1$
	public static final String ATTR_WORD_SPACING = "word-spacing"; //$NON-NLS-1$
	public static final String ATTR_TEXT_TRANSFORM = "text-transform"; //$NON-NLS-1$
	public static final String ATTR_WHITE_SPACE = "white-space"; //$NON-NLS-1$

	public static final String ATTR_FONT_FAMILY = "font-family"; //$NON-NLS-1$
	public static final String ATTR_FONT_SIZE = "font-size"; //$NON-NLS-1$
	public static final String ATTR_FONT_STYLE = "font-style"; //$NON-NLS-1$
	public static final String ATTR_FONT_VARIANT = "font-variant"; //$NON-NLS-1$
	public static final String ATTR_FONT_WEIGTH = "font-weight"; //$NON-NLS-1$

	public static final String ATTR_ID = "id"; //$NON-NLS-1$
	public static final String ATTR_NAME = "name"; //$NON-NLS-1$
	public static final String ATTR_HREF = "href"; //$NON-NLS-1$
	public static final String ATTR_TARGET = "target"; //$NON-NLS-1$
	public static final String ATTR_IMAGE = "img"; //$NON-NLS-1$
	public static final String ATTR_STYLE = "style"; //$NON-NLS-1$
	public static final String ATTR_SRC = "src"; //$NON-NLS-1$
	public static final String ATTR_DATA = "data"; //$NON-NLS-1$
	public static final String ATTR_USEMAP = "usemap"; //$NON-NLS-1$
	public static final String ATTR_ALT = "alt"; //$NON-NLS-1$
	public static final String ATTR_TITLE = "title"; //$NON-NLS-1$
	public static final String ATTR_HTTP_EQUIV = "http-equiv"; //$NON-NLS-1$
	public static final String ATTR_CONTENT = "content"; //$NON-NLS-1$
	public static final String ATTR_TYPE = "type"; //$NON-NLS-1$
	public static final String ATTR_SPAN = "span"; //$NON-NLS-1$
	public static final String ATTR_COLSPAN = "colspan"; //$NON-NLS-1$
	public static final String ATTR_ROWSPAN = "rowspan"; //$NON-NLS-1$
	public static final String ATTR_ALIGN = "align"; //$NON-NLS-1$
	public static final String ATTR_VALIGN = "valign"; //$NON-NLS-1$
	public static final String ATTR_ONLOAD = "onload"; //$NON-NLS-1$
	public static final String ATTR_GOURP_ID = "group-id"; //$NON-NLS-1$
	public static final String ATTR_ROW_TYPE = "row-type"; //$NON-NLS-1$
	public static final String ATTR_ONCLICK = "onclick"; //$NON-NLS-1$
	public static final String ATTR_COLUMN = "column"; //$NON-NLS-1$
	public static final String ATTR_SUMMARY = "summary";//$NON-NLS-1$
	public static final String ATTR_REL = "rel";//$NON-NLS-1$
	public static final String ATTR_RAW_DATA = "raw_data";//$NON-NLS-1$

	// tags in html
	public static final String TAG_IMAGE = "img"; //$NON-NLS-1$
	public static final String TAG_EMBED = "embed"; //$NON-NLS-1$
	public static final String TAG_OBJECT = "object"; //$NON-NLS-1$
	public static final String TAG_MAP = "map"; //$NON-NLS-1$
	public static final String TAG_HTML = "html"; //$NON-NLS-1$
	public static final String TAG_META = "meta"; //$NON-NLS-1$
	public static final String TAG_HEAD = "head"; //$NON-NLS-1$
	public static final String TAG_TITLE = "title"; //$NON-NLS-1$
	public static final String TAG_STYLE = "style"; //$NON-NLS-1$
	public static final String TAG_BODY = "body"; //$NON-NLS-1$
	public static final String TAG_TABLE = "table"; //$NON-NLS-1$
	public static final String TAG_CAPTION = "caption"; //$NON-NLS-1$
	public static final String TAG_COL = "col"; //$NON-NLS-1$
	public static final String TAG_TR = "tr"; //$NON-NLS-1$
	public static final String TAG_TH = "th"; //$NON-NLS-1$
	public static final String TAG_TD = "td"; //$NON-NLS-1$
	public static final String TAG_THEAD = "thead"; //$NON-NLS-1$
	public static final String TAG_TFOOT = "tfoot"; //$NON-NLS-1$
	public static final String TAG_TBODY = "tbody"; //$NON-NLS-1$
	public static final String TAG_BR = "br"; //$NON-NLS-1$
	public static final String TAG_PRE = "pre"; //$NON-NLS-1$

	public static final String TAG_A = "a"; //$NON-NLS-1$
	public static final String TAG_DIV = "div"; //$NON-NLS-1$
	public static final String TAG_SPAN = "span"; //$NON-NLS-1$
	public static final String TAG_SCRIPT = "script"; //$NON-NLS-1$

	public static final String TAG_LINK = "link";//$NON-NLS-1$
}
