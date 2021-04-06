/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

public interface IOConstants {

	static final short PAGE_SEQUENCE_DESIGN = 1;
	static final short SIMPLE_MASTER_PAGE_DESIGN = 2;

	static final short GRID_DESIGN = 3;
	static final short FREE_FORM_DESIGN = 4;
	static final short COLUMN_DESIGN = 5;
	static final short ROW_DESIGN = 6;
	static final short CELL_DESIGN = 7;
	static final short LIST_DESIGN = 8;
	static final short LIST_GROUP_DESIGN = 9;
	static final short LIST_BAND_DESIGN = 10;
	static final short TABLE_DESIGN = 11;
	static final short TABLE_GROUP_DESIGN = 12;
	static final short TABLE_BAND_DESIGN = 13;

	static final short LABEL_DESIGN = 14;
	static final short TEXT_DESIGN = 15;
	static final short DATA_DESIGN = 16;
	static final short MULTI_LINE_DESIGN = 17;
	static final short IMAGE_DESIGN = 18;
	static final short TEMPLATE_DESIGN = 19;
	static final short EXTENDED_DESIGN = 20;
	static final short AUTO_TEXT_DESIGN = 21;

	// report element
	static final short FIELD_ID = 0;
	static final short FIELD_NAME = 1;
	static final short FIELD_EXTENDS = 2;
	static final short FIELD_JAVA_CLASS = 3;
	static final short FIELD_NAMED_EXPRESSIONS = 4;
	static final short FIELD_CUSTOM_PROPERTIES = 5;

	// styled element
	static final short FIELD_STYLE_NAME = 6;
	static final short FIELD_MAP = 7;
	static final short FIELD_HIGHLIGHT = 8;
	static final short FIELD_STYLE = 9;

	// report item
	static final short FIELD_X = 20;
	static final short FIELD_Y = 21;
	static final short FIELD_HEIGHT = 22;
	static final short FIELD_WIDTH = 23;
	static final short FIELD_BOOKMARK = 24;
	static final short FIELD_TOC = 25;
	static final short FIELD_ON_CREATE = 26;
	static final short FIELD_ON_RENDER = 27;
	static final short FIELD_ON_PAGE_BREAK = 28;
	static final short FIELD_VISIBILITY = 29;

	// listing
	static final short FIELD_PAGE_BREAK_INTERVAL = 30;
	static final short FIELD_REPEAT_HEADER = 31;

	// group
	static final short FIELD_GROUP_LEVEL = 40;
	static final short FIELD_PAGE_BREAK_BEFORE = 41;
	static final short FIELD_PAGE_BREAK_AFTER = 42;
	static final short FIELD_HEADER_REPEAT = 43;
	static final short FIELD_HIDE_DETAIL = 44;
	static final short FIELD_PAGE_BREAK_INSIDE = 45;

	// band type
	static final short FIELD_BAND_TYPE = 50;

	// table
	static final short FIELD_CAPTION = 60;

	// grid
	static final short FIELD_COLUMNS = 70;

	static final short FIELD_SUMMARY = 71;

	// column
	static final short FIELD_SUPPRESS_DUPLICATE = 80;
	static final short FIELD_HAS_DATA_ITEMS_IN_DETAIL = 81;
	static final short FIELD_IS_COLUMN_HEADER = 82;

	// row
	static final short FIELD_IS_START_OF_GROUP = 90;
	static final short FIELD_IS_REPEATABLE = 91;

	// cell
	static final short FIELD_COLUMN = 100;
	static final short FIELD_COL_SPAN = 101;
	static final short FIELD_ROW_SPAN = 102;
	static final short FIELD_DROP = 103;
	static final short FIELD_DISPLAY_GROUP_ICON = 104;
	static final short FIELD_DIAGONAL_NUMBER = 105;
	static final short FIELD_DIAGONAL_STYLE = 106;
	static final short FIELD_DIAGONAL_WIDTH = 107;
	static final short FIELD_ANTIDIAGONAL_NUMBER = 108;
	static final short FIELD_ANTIDIAGONAL_STYLE = 109;
	static final short FIELD_ANTIDIAGONAL_WIDTH = 110;
	static final short FIELD_DIAGONAL_COLOR = 111;
	static final short FIELD_ANTIDIAGONAL_COLOR = 112;
	static final short FIELD_HEADERS = 99;
	static final short FIELD_SCOPE = 98;

	// label
	static final short FIELD_TEXT = 110;
	static final short FIELD_HELP_TEXT = 111;
	static final short FIELD_ACTION = 112;
	static final short FIELD_ACTION_V1 = 113;

	// data
	static final short FIELD_VALUE = 120;
	static final short FIELD_BINDING_COLUMN = 121;
	static final short FIELD_NEED_REFRESH_MAPPING = 122;

	// text
	static final short FIELD_TEXT_TYPE = 130;

	// mutiline text
	static final short FIELD_CONTENT_TYPE = 140;
	static final short FIELD_CONTENT = 141;

	// image item
	static final short FIELD_IMAGE_SOURCE = 150;
	static final short FIELD_ALT_TEXT = 151;
	static final short FIELD_FIT_TO_CONTAINER = 152;
	static final short FIELD_PROPORTIONAL_SCALE = 153;

	// extended item

	// auto text
	static final short FIELD_TYPE = 160;

	// template
	static final short FIELD_ALLOWED_TYPE = 170;
	static final short FIELD_PROMPT_TEXT = 171;

	// master page
	static final short FIELD_PAGE_TYPE = 200;
	static final short FIELD_PAGE_SIZE = 201;
	static final short FIELD_MARGIN = 202;
	static final short FIELD_ORIENTATION = 203;
	static final short FIELD_BODY_STYLE = 204;

	// simple master page
	static final short FIELD_SHOW_HEADER_ON_FIRST = 210;
	static final short FIELD_SHOW_FOOTER_ON_LAST = 211;
	static final short FIELD_FLOATING_FOOTER = 212;
	static final short FEILD_HEADER_HEIGHT = 213;
	static final short FEILD_FOOTER_HEIGHT = 214;

	static final short FIELD_COLUMN_SPACING = 220;

	// report item added
	static final short FIELD_USE_CACHED_RESULT = 230;

	static final short FIELD_REPORT_STYLES = 240;
	static final short FIELD_REPORT_NAMED_EXPRESSIONS = 241;
	static final short FIELD_REPORT_MASTER_PAGES = 242;
	static final short FIELD_REPORT_BODY = 243;

	// added from V6
	static final short FIELD_REPORT_VARIABLE = 245;
	static final short FIELD_ON_PAGE_START = 246;
	static final short FIELD_ON_PAGE_END = 247;

	// added from V7
	static final short FIELD_REPORT_VERSION = 248;
	static final short FIELD_TEXT_HAS_EXPRESSION = 249;
	static final short FIELD_EXPRESSION_WITH_LANGUAGE = 250;
	static final short FIELD_EXPRESSION_WITHOUT_LANGUAGE = 251;
	static final short FIELD_REPORT_SCRIPT_LANGUAGE = 252;
	static final short FIELD_USER_PROPERTIES = 253;
	static final short FIELD_REPORT_USER_PROPERTIES = 254;

	// added from V11
	static final short FIELD_REPORT_LOCALE = 255;
	static final short FIELD_JTIDY = 256;

	static final long ENGINE_IR_VERSION_0 = 0L;
	// Version 1: remove write isBookmark of ActionDesign.
	static final long ENGINE_IR_VERSION_1 = 1L;
	// Version 2: remove write base path and unit of report.
	static final long ENGINE_IR_VERSION_2 = 2L;
	// Version 3: add extended item's children.
	static final long ENGINE_IR_VERSION_3 = 3L;
	// Version 4: change the way of writing and reading the style.
	static final long ENGINE_IR_VERSION_4 = 4L;
	/**
	 * Version 5: support attribute as expression/constant.
	 * 
	 * @deprecated since 2.5.0 It is a internal version, won't supported
	 */
	static final long ENGINE_IR_VERSION_5 = 5L;
	/**
	 * version 6: support page variable/onPageStart/onPageEnd in the report and
	 * master page.
	 * 
	 * @deprecated since 2.5.0 It is a internal version, won't supported
	 */
	static final long ENGINE_IR_VERSION_6 = 6L;

	/**
	 * version 7: support script expression.
	 */
	static final long ENGINE_IR_VERSION_7 = 7L;

	/**
	 * version 8: support expression list parameter binding.
	 */
	static final long ENGINE_IR_VERSION_8 = 8L;
	/**
	 * version 9: support expression alt text
	 */
	static final long ENGINE_IR_VERSION_9 = 9L;
	static final long ENGINE_IR_VERSION_CURRENT = ENGINE_IR_VERSION_9;

}
