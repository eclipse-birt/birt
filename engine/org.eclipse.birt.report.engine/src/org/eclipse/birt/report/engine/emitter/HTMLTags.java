/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008, 2024 Actuate Corporation and others
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

	/** html tag attribute: class */
	public static final String ATTR_CLASS = "class"; //$NON-NLS-1$
	/** html tag attribute: left */
	public static final String ATTR_LEFT = "left"; //$NON-NLS-1$
	/** html tag attribute: top */
	public static final String ATTR_TOP = "top"; //$NON-NLS-1$
	/** html tag attribute: width */
	public static final String ATTR_WIDTH = "width"; //$NON-NLS-1$
	/** html tag attribute: min-width */
	public static final String ATTR_MIN_WIDTH = "min-width"; //$NON-NLS-1$
	/** html tag attribute: min-height */
	public static final String ATTR_MIN_HEIGHT = "min-height"; //$NON-NLS-1$
	/** html tag attribute: height */
	public static final String ATTR_HEIGHT = "height"; //$NON-NLS-1$
	/** html tag attribute: dir */
	public static final String ATTR_HTML_DIR = "dir"; //$NON-NLS-1$

	/** html tag attribute: vertical-align */
	public static final String ATTR_VERTICAL_ALIGN = "vertical-align"; //$NON-NLS-1$
	/** html tag attribute: line-height */
	public static final String ATTR_LINE_HEIGHT = "line-height"; //$NON-NLS-1$
	/** html tag attribute: orphans */
	public static final String ATTR_ORPHANS = "orphans"; //$NON-NLS-1$
	/** html tag attribute: widows */
	public static final String ATTR_WIDOWS = "widows"; //$NON-NLS-1$
	/** html tag attribute: page-break-before */
	public static final String ATTR_PAGE_BREAK_BEFORE = "page-break-before"; //$NON-NLS-1$
	/** html tag attribute: page-break-after */
	public static final String ATTR_PAGE_BREAK_AFTER = "page-break-after"; //$NON-NLS-1$
	/** html tag attribute: page-break-inside */
	public static final String ATTR_PAGE_BREAK_INSIDE = "page-break-inside"; //$NON-NLS-1$
	/** html tag attribute: color */
	public static final String ATTR_COLOR = "color"; //$NON-NLS-1$
	/** html tag attribute: margin */
	public static final String ATTR_MARGIN = "margin"; //$NON-NLS-1$
	/** html tag attribute: margin-top */
	public static final String ATTR_MARGIN_TOP = "margin-top"; //$NON-NLS-1$
	/** html tag attribute: margin-right */
	public static final String ATTR_MARGIN_RIGHT = "margin-right"; //$NON-NLS-1$
	/** html tag attribute: margin-bottom */
	public static final String ATTR_MARGIN_BOTTOM = "margin-bottom"; //$NON-NLS-1$
	/** html tag attribute: margin-left */
	public static final String ATTR_MARGIN_LEFT = "margin-left"; //$NON-NLS-1$

	/** html tag attribute: background-color */
	public static final String ATTR_BACKGROUND_COLOR = "background-color"; //$NON-NLS-1$
	/** html tag attribute: background-image */
	public static final String ATTR_BACKGROUND_IMAGE = "background-image"; //$NON-NLS-1$
	/** html tag attribute: background-repeat */
	public static final String ATTR_BACKGROUND_REPEAT = "background-repeat"; //$NON-NLS-1$
	/** html tag attribute: background-attachment */
	public static final String ATTR_BACKGROUND_ATTACHEMNT = "background-attachment"; //$NON-NLS-1$
	/** html tag attribute: background-position */
	public static final String ATTR_BACKGROUND_POSITION = "background-position"; //$NON-NLS-1$
	/** html tag attribute: background-size */
	public static final String ATTR_BACKGROUND_SIZE = "background-size"; //$NON-NLS-1$

	/** html tag attribute: padding */
	public static final String ATTR_PADDING = "padding"; //$NON-NLS-1$
	/** html tag attribute: padding-top */
	public static final String ATTR_PADDING_TOP = "padding-top"; //$NON-NLS-1$
	/** html tag attribute: padding-right */
	public static final String ATTR_PADDING_RIGHT = "padding-right"; //$NON-NLS-1$
	/** html tag attribute: padding-bottom */
	public static final String ATTR_PADDING_BOTTOM = "padding-bottom"; //$NON-NLS-1$
	/** html tag attribute: padding-left */
	public static final String ATTR_PADDING_LEFT = "padding-left"; //$NON-NLS-1$

	/** html tag attribute: border-bottom-color */
	public static final String ATTR_BOTTOM_COLOR = "border-bottom-color"; //$NON-NLS-1$
	/** html tag attribute: border-bottom */
	public static final String ATTR_BORDER_BOTTOM = "border-bottom"; //$NON-NLS-1$
	/** html tag attribute: border-bottom-style */
	public static final String ATTR_BORDER_BOTTOM_STYLE = "border-bottom-style"; //$NON-NLS-1$
	/** html tag attribute: border-bottom-width */
	public static final String ATTR_BORDER_BOTTOM_WIDTH = "border-bottom-width"; //$NON-NLS-1$
	/** html tag attribute: border-left */
	public static final String ATTR_BORDER_LEFT = "border-left"; //$NON-NLS-1$
	/** html tag attribute: border-left-color */
	public static final String ATTR_BORDER_LEFT_COLOR = "border-left-color"; //$NON-NLS-1$
	/** html tag attribute: border-left-style */
	public static final String ATTR_BORDER_LEFT_STYLE = "border-left-style"; //$NON-NLS-1$
	/** html tag attribute: border-left-width */
	public static final String ATTR_BORDER_LEFT_WIDTH = "border-left-width"; //$NON-NLS-1$
	/** html tag attribute: border-right */
	public static final String ATTR_BORDER_RIGHT = "border-right"; //$NON-NLS-1$
	/** html tag attribute: border-right-color */
	public static final String ATTR_BORDER_RIGHT_COLOR = "border-right-color"; //$NON-NLS-1$
	/** html tag attribute: border-right-style */
	public static final String ATTR_BORDER_RIGHT_STYLE = "border-right-style"; //$NON-NLS-1$
	/** html tag attribute: border-right-width */
	public static final String ATTR_BORDER_RIGHT_WIDTH = "border-right-width"; //$NON-NLS-1$
	/** html tag attribute: border */
	public static final String ATTR_BORDER = "border"; //$NON-NLS-1$
	/** html tag attribute: border-top */
	public static final String ATTR_BORDER_TOP = "border-top"; //$NON-NLS-1$
	/** html tag attribute: border-top-color */
	public static final String ATTR_BORDER_TOP_COLOR = "border-top-color"; //$NON-NLS-1$
	/** html tag attribute: border-top-style */
	public static final String ATTR_BORDER_TOP_STYLE = "border-top-style"; //$NON-NLS-1$
	/** html tag attribute: border-top-width */
	public static final String ATTR_BORDER_TOP_WIDTH = "border-top-width"; //$NON-NLS-1$

	/** html tag attribute: text-align */
	public static final String ATTR_TEXT_ALIGN = "text-align"; //$NON-NLS-1$
	/** html tag attribute: text-indent */
	public static final String ATTR_TEXT_INDENT = "text-indent"; //$NON-NLS-1$
	/** html tag attribute: letter-spacing */
	public static final String ATTR_LETTER_SPACING = "letter-spacing"; //$NON-NLS-1$
	/** html tag attribute: word-spacing */
	public static final String ATTR_WORD_SPACING = "word-spacing"; //$NON-NLS-1$
	/** html tag attribute: text-transform */
	public static final String ATTR_TEXT_TRANSFORM = "text-transform"; //$NON-NLS-1$
	/** html tag attribute: white-space */
	public static final String ATTR_WHITE_SPACE = "white-space"; //$NON-NLS-1$

	/** html tag attribute: font-familiy */
	public static final String ATTR_FONT_FAMILY = "font-family"; //$NON-NLS-1$
	/** html tag attribute: font-size */
	public static final String ATTR_FONT_SIZE = "font-size"; //$NON-NLS-1$
	/** html tag attribute: font-style */
	public static final String ATTR_FONT_STYLE = "font-style"; //$NON-NLS-1$
	/** html tag attribute: font-variant */
	public static final String ATTR_FONT_VARIANT = "font-variant"; //$NON-NLS-1$
	/** html tag attribute: font-weight */
	public static final String ATTR_FONT_WEIGTH = "font-weight"; //$NON-NLS-1$
	/** html tag attribute: (font) size of tag font */
	public static final String ATTR_TAG_FONT_SIZE = "size"; //$NON-NLS-1$
	/** html tag attribute: (font) color of tag font */
	public static final String ATTR_TAG_FONT_COLOR = "color"; //$NON-NLS-1$
	/** html tag attribute: (font) face of tag font */
	public static final String ATTR_TAG_FONT_FACE = "face"; //$NON-NLS-1$

	/** html tag attribute: id */
	public static final String ATTR_ID = "id"; //$NON-NLS-1$
	/** html tag attribute: name */
	public static final String ATTR_NAME = "name"; //$NON-NLS-1$
	/** html tag attribute: href */
	public static final String ATTR_HREF = "href"; //$NON-NLS-1$
	/** html tag attribute: target */
	public static final String ATTR_TARGET = "target"; //$NON-NLS-1$
	/** html tag attribute: img */
	public static final String ATTR_IMAGE = "img"; //$NON-NLS-1$
	/** html tag attribute: style */
	public static final String ATTR_STYLE = "style"; //$NON-NLS-1$
	/** html tag attribute: src */
	public static final String ATTR_SRC = "src"; //$NON-NLS-1$
	/** html tag attribute: data */
	public static final String ATTR_DATA = "data"; //$NON-NLS-1$
	/** html tag attribute: usemap */
	public static final String ATTR_USEMAP = "usemap"; //$NON-NLS-1$
	/** html tag attribute: alt */
	public static final String ATTR_ALT = "alt"; //$NON-NLS-1$
	/** html tag attribute: title */
	public static final String ATTR_TITLE = "title"; //$NON-NLS-1$
	/** html tag attribute: http-equiv */
	public static final String ATTR_HTTP_EQUIV = "http-equiv"; //$NON-NLS-1$
	/** html tag attribute: content */
	public static final String ATTR_CONTENT = "content"; //$NON-NLS-1$
	/** html tag attribute: type */
	public static final String ATTR_TYPE = "type"; //$NON-NLS-1$
	/** html tag attribute: span */
	public static final String ATTR_SPAN = "span"; //$NON-NLS-1$
	/** html tag attribute: embedded images */
	public static final String ATTR_POINTER_EVENTS = "pointer-events"; //$NON-NLS-1$

	/** html tag attribute: colspan */
	public static final String ATTR_COLSPAN = "colspan"; //$NON-NLS-1$
	/** html tag attribute: rowspan */
	public static final String ATTR_ROWSPAN = "rowspan"; //$NON-NLS-1$
	/** html tag attribute: align */
	public static final String ATTR_ALIGN = "align"; //$NON-NLS-1$
	/** html tag attribute: valign */
	public static final String ATTR_VALIGN = "valign"; //$NON-NLS-1$
	/** html tag attribute: onload */
	public static final String ATTR_ONLOAD = "onload"; //$NON-NLS-1$
	/** html tag attribute: group-id */
	public static final String ATTR_GOURP_ID = "group-id"; //$NON-NLS-1$
	/** html tag attribute: row-type */
	public static final String ATTR_ROW_TYPE = "row-type"; //$NON-NLS-1$
	/** html tag attribute: onclick */
	public static final String ATTR_ONCLICK = "onclick"; //$NON-NLS-1$
	/** html tag attribute: column */
	public static final String ATTR_COLUMN = "column"; //$NON-NLS-1$
	/** html tag attribute: summary */
	public static final String ATTR_SUMMARY = "summary";//$NON-NLS-1$
	/** html tag attribute: rel */
	public static final String ATTR_REL = "rel";//$NON-NLS-1$
	/** html tag attribute: raw_data */
	public static final String ATTR_RAW_DATA = "raw_data";//$NON-NLS-1$


	// tags in html

	/** html tag: img */
	public static final String TAG_IMAGE = "img"; //$NON-NLS-1$
	/** html tag: embed */
	public static final String TAG_EMBED = "embed"; //$NON-NLS-1$
	/** html tag: object */
	public static final String TAG_OBJECT = "object"; //$NON-NLS-1$
	/** html tag: map */
	public static final String TAG_MAP = "map"; //$NON-NLS-1$
	/** html tag: html */
	public static final String TAG_HTML = "html"; //$NON-NLS-1$
	/** html tag: meta */
	public static final String TAG_META = "meta"; //$NON-NLS-1$
	/** html tag: head */
	public static final String TAG_HEAD = "head"; //$NON-NLS-1$
	/** html tag: title */
	public static final String TAG_TITLE = "title"; //$NON-NLS-1$
	/** html tag: style */
	public static final String TAG_STYLE = "style"; //$NON-NLS-1$
	/** html tag: body */
	public static final String TAG_BODY = "body"; //$NON-NLS-1$
	/** html tag: table */
	public static final String TAG_TABLE = "table"; //$NON-NLS-1$
	/** html tag: caption */
	public static final String TAG_CAPTION = "caption"; //$NON-NLS-1$
	/** html tag: col */
	public static final String TAG_COL = "col"; //$NON-NLS-1$
	/** html tag: tr */
	public static final String TAG_TR = "tr"; //$NON-NLS-1$
	/** html tag: th */
	public static final String TAG_TH = "th"; //$NON-NLS-1$
	/** html tag: td */
	public static final String TAG_TD = "td"; //$NON-NLS-1$
	/** html tag: thead */
	public static final String TAG_THEAD = "thead"; //$NON-NLS-1$
	/** html tag: tfoot */
	public static final String TAG_TFOOT = "tfoot"; //$NON-NLS-1$
	/** html tag: tbody */
	public static final String TAG_TBODY = "tbody"; //$NON-NLS-1$
	/** html tag: br */
	public static final String TAG_BR = "br"; //$NON-NLS-1$
	/** html tag: pre */
	public static final String TAG_PRE = "pre"; //$NON-NLS-1$

	/** html tag: a */
	public static final String TAG_A = "a"; //$NON-NLS-1$
	/** html tag: div */
	public static final String TAG_DIV = "div"; //$NON-NLS-1$
	/** html tag: span */
	public static final String TAG_SPAN = "span"; //$NON-NLS-1$
	/** html tag: script */
	public static final String TAG_SCRIPT = "script"; //$NON-NLS-1$
	/** html tag: link */
	public static final String TAG_LINK = "link";//$NON-NLS-1$
	/** html tag: font paragraph */
	public static final String TAG_P = "p";//$NON-NLS-1$
	/** html tag: font (HTML 4.01) */
	public static final String TAG_FONT = "font";//$NON-NLS-1$
}
