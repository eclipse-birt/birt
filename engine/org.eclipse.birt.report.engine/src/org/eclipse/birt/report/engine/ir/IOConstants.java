/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

public interface IOConstants {

	short PAGE_SEQUENCE_DESIGN = 1;
	short SIMPLE_MASTER_PAGE_DESIGN = 2;

	short GRID_DESIGN = 3;
	short FREE_FORM_DESIGN = 4;
	short COLUMN_DESIGN = 5;
	short ROW_DESIGN = 6;
	short CELL_DESIGN = 7;
	short LIST_DESIGN = 8;
	short LIST_GROUP_DESIGN = 9;
	short LIST_BAND_DESIGN = 10;
	short TABLE_DESIGN = 11;
	short TABLE_GROUP_DESIGN = 12;
	short TABLE_BAND_DESIGN = 13;

	short LABEL_DESIGN = 14;
	short TEXT_DESIGN = 15;
	short DATA_DESIGN = 16;
	short MULTI_LINE_DESIGN = 17;
	short IMAGE_DESIGN = 18;
	short TEMPLATE_DESIGN = 19;
	short EXTENDED_DESIGN = 20;
	short AUTO_TEXT_DESIGN = 21;

	// report element
	short FIELD_ID = 0;
	short FIELD_NAME = 1;
	short FIELD_EXTENDS = 2;
	short FIELD_JAVA_CLASS = 3;
	short FIELD_NAMED_EXPRESSIONS = 4;
	short FIELD_CUSTOM_PROPERTIES = 5;

	// styled element
	short FIELD_STYLE_NAME = 6;
	short FIELD_MAP = 7;
	short FIELD_HIGHLIGHT = 8;
	short FIELD_STYLE = 9;

	// report item
	short FIELD_X = 20;
	short FIELD_Y = 21;
	short FIELD_HEIGHT = 22;
	short FIELD_WIDTH = 23;
	short FIELD_BOOKMARK = 24;
	short FIELD_TOC = 25;
	short FIELD_ON_CREATE = 26;
	short FIELD_ON_RENDER = 27;
	short FIELD_ON_PAGE_BREAK = 28;
	short FIELD_VISIBILITY = 29;

	// listing
	short FIELD_PAGE_BREAK_INTERVAL = 30;
	short FIELD_REPEAT_HEADER = 31;

	// group
	short FIELD_GROUP_LEVEL = 40;
	short FIELD_PAGE_BREAK_BEFORE = 41;
	short FIELD_PAGE_BREAK_AFTER = 42;
	short FIELD_HEADER_REPEAT = 43;
	short FIELD_HIDE_DETAIL = 44;
	short FIELD_PAGE_BREAK_INSIDE = 45;

	// band type
	short FIELD_BAND_TYPE = 50;

	// table
	short FIELD_CAPTION = 60;

	// grid
	short FIELD_COLUMNS = 70;

	short FIELD_SUMMARY = 71;

	// column
	short FIELD_SUPPRESS_DUPLICATE = 80;
	short FIELD_HAS_DATA_ITEMS_IN_DETAIL = 81;
	short FIELD_IS_COLUMN_HEADER = 82;

	// row
	short FIELD_IS_START_OF_GROUP = 90;
	short FIELD_IS_REPEATABLE = 91;

	// cell
	short FIELD_COLUMN = 100;
	short FIELD_COL_SPAN = 101;
	short FIELD_ROW_SPAN = 102;
	short FIELD_DROP = 103;
	short FIELD_DISPLAY_GROUP_ICON = 104;
	short FIELD_DIAGONAL_NUMBER = 105;
	short FIELD_DIAGONAL_STYLE = 106;
	short FIELD_DIAGONAL_WIDTH = 107;
	short FIELD_ANTIDIAGONAL_NUMBER = 108;
	short FIELD_ANTIDIAGONAL_STYLE = 109;
	short FIELD_ANTIDIAGONAL_WIDTH = 110;
	short FIELD_DIAGONAL_COLOR = 111;
	short FIELD_ANTIDIAGONAL_COLOR = 112;
	short FIELD_HEADERS = 99;
	short FIELD_SCOPE = 98;

	// label
	short FIELD_TEXT = 110;
	short FIELD_HELP_TEXT = 111;
	short FIELD_ACTION = 112;
	short FIELD_ACTION_V1 = 113;

	// data
	short FIELD_VALUE = 120;
	short FIELD_BINDING_COLUMN = 121;
	short FIELD_NEED_REFRESH_MAPPING = 122;

	// text
	short FIELD_TEXT_TYPE = 130;

	// mutiline text
	short FIELD_CONTENT_TYPE = 140;
	short FIELD_CONTENT = 141;

	// image item
	short FIELD_IMAGE_SOURCE = 150;
	short FIELD_ALT_TEXT = 151;
	short FIELD_FIT_TO_CONTAINER = 152;
	short FIELD_PROPORTIONAL_SCALE = 153;

	// extended item

	// auto text
	short FIELD_TYPE = 160;

	// template
	short FIELD_ALLOWED_TYPE = 170;
	short FIELD_PROMPT_TEXT = 171;

	// master page
	short FIELD_PAGE_TYPE = 200;
	short FIELD_PAGE_SIZE = 201;
	short FIELD_MARGIN = 202;
	short FIELD_ORIENTATION = 203;
	short FIELD_BODY_STYLE = 204;

	// simple master page
	short FIELD_SHOW_HEADER_ON_FIRST = 210;
	short FIELD_SHOW_FOOTER_ON_LAST = 211;
	short FIELD_FLOATING_FOOTER = 212;
	short FEILD_HEADER_HEIGHT = 213;
	short FEILD_FOOTER_HEIGHT = 214;

	short FIELD_COLUMN_SPACING = 220;

	// report item added
	short FIELD_USE_CACHED_RESULT = 230;

	short FIELD_REPORT_STYLES = 240;
	short FIELD_REPORT_NAMED_EXPRESSIONS = 241;
	short FIELD_REPORT_MASTER_PAGES = 242;
	short FIELD_REPORT_BODY = 243;

	// added from V6
	short FIELD_REPORT_VARIABLE = 245;
	short FIELD_ON_PAGE_START = 246;
	short FIELD_ON_PAGE_END = 247;

	// added from V7
	short FIELD_REPORT_VERSION = 248;
	short FIELD_TEXT_HAS_EXPRESSION = 249;
	short FIELD_EXPRESSION_WITH_LANGUAGE = 250;
	short FIELD_EXPRESSION_WITHOUT_LANGUAGE = 251;
	short FIELD_REPORT_SCRIPT_LANGUAGE = 252;
	short FIELD_USER_PROPERTIES = 253;
	short FIELD_REPORT_USER_PROPERTIES = 254;

	// added from V11
	short FIELD_REPORT_LOCALE = 255;
	short FIELD_JTIDY = 256;

	long ENGINE_IR_VERSION_0 = 0L;
	// Version 1: remove write isBookmark of ActionDesign.
	long ENGINE_IR_VERSION_1 = 1L;
	// Version 2: remove write base path and unit of report.
	long ENGINE_IR_VERSION_2 = 2L;
	// Version 3: add extended item's children.
	long ENGINE_IR_VERSION_3 = 3L;
	// Version 4: change the way of writing and reading the style.
	long ENGINE_IR_VERSION_4 = 4L;
	/**
	 * Version 5: support attribute as expression/constant.
	 *
	 * @deprecated since 2.5.0 It is a internal version, won't supported
	 */
	@Deprecated
	long ENGINE_IR_VERSION_5 = 5L;
	/**
	 * version 6: support page variable/onPageStart/onPageEnd in the report and
	 * master page.
	 *
	 * @deprecated since 2.5.0 It is a internal version, won't supported
	 */
	@Deprecated
	long ENGINE_IR_VERSION_6 = 6L;

	/**
	 * version 7: support script expression.
	 */
	long ENGINE_IR_VERSION_7 = 7L;

	/**
	 * version 8: support expression list parameter binding.
	 */
	long ENGINE_IR_VERSION_8 = 8L;
	/**
	 * version 9: support expression alt text
	 */
	long ENGINE_IR_VERSION_9 = 9L;
	long ENGINE_IR_VERSION_CURRENT = ENGINE_IR_VERSION_9;

}
