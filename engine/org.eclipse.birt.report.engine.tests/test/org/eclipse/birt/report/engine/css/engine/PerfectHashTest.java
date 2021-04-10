package org.eclipse.birt.report.engine.css.engine;

import junit.framework.TestCase;

public class PerfectHashTest extends TestCase {

	/*
	 * Test method for
	 * 'org.eclipse.birt.report.engine.css.engine.PerfectHash.in_word_set(String)'
	 */
	public void testIn_word_set() {
		assertEquals(StyleConstants.STYLE_TEXT_ALIGN, PerfectHash.in_word_set("text-align"));
		assertEquals(StyleConstants.STYLE_TEXT_INDENT, PerfectHash.in_word_set("text-indent"));
		assertEquals(StyleConstants.STYLE_NUMBER_ALIGN, PerfectHash.in_word_set("number-align"));
//		assertEquals(StyleConstants.STYLE_NUMBER_FORMAT , PerfectHash.in_word_set("number-format"));
		assertEquals(StyleConstants.STYLE_VERTICAL_ALIGN, PerfectHash.in_word_set("vertical-align"));
		assertEquals(StyleConstants.STYLE_LINE_HEIGHT, PerfectHash.in_word_set("line-height"));
		assertEquals(StyleConstants.STYLE_BACKGROUND_REPEAT, PerfectHash.in_word_set("background-repeat"));
		assertEquals(StyleConstants.STYLE_BACKGROUND_ATTACHMENT, PerfectHash.in_word_set("background-attachment"));
//		assertEquals(StyleConstants.STYLE_DATE_FORMAT  , PerfectHash.in_word_set("date-format"));
//		assertEquals(StyleConstants.STYLE_SQL_DATE_FORMAT  , PerfectHash.in_word_set("sql-date-format"));
//		assertEquals(StyleConstants.STYLE_SQL_TIME_FORMAT  , PerfectHash.in_word_set("sql-time-format"));
		assertEquals(StyleConstants.STYLE_CAN_SHRINK, PerfectHash.in_word_set("can-shrink"));
		assertEquals(StyleConstants.STYLE_TEXT_OVERLINE, PerfectHash.in_word_set("text-overline"));
		assertEquals(StyleConstants.STYLE_TEXT_UNDERLINE, PerfectHash.in_word_set("text-underline"));
		assertEquals(StyleConstants.STYLE_BACKGROUND_IMAGE, PerfectHash.in_word_set("background-image"));
		assertEquals(StyleConstants.STYLE_BORDER_TOP_STYLE, PerfectHash.in_word_set("border-top-style"));
		assertEquals(StyleConstants.STYLE_BORDER_LEFT_STYLE, PerfectHash.in_word_set("border-left-style"));
		assertEquals(StyleConstants.STYLE_BORDER_RIGHT_STYLE, PerfectHash.in_word_set("border-right-style"));
		assertEquals(StyleConstants.STYLE_BORDER_BOTTOM_STYLE, PerfectHash.in_word_set("border-bottom-style"));
		assertEquals(StyleConstants.STYLE_COLOR, PerfectHash.in_word_set("color"));
		assertEquals(StyleConstants.STYLE_BORDER_TOP_WIDTH, PerfectHash.in_word_set("border-top-width"));
		assertEquals(StyleConstants.STYLE_TEXT_LINETHROUGH, PerfectHash.in_word_set("text-linethrough"));
		assertEquals(StyleConstants.STYLE_BORDER_LEFT_WIDTH, PerfectHash.in_word_set("border-left-width"));
		assertEquals(StyleConstants.STYLE_BORDER_RIGHT_WIDTH, PerfectHash.in_word_set("border-right-width"));
		assertEquals(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH, PerfectHash.in_word_set("border-bottom-width"));
		assertEquals(StyleConstants.STYLE_BACKGROUND_COLOR, PerfectHash.in_word_set("background-color"));
		assertEquals(StyleConstants.STYLE_BORDER_TOP_COLOR, PerfectHash.in_word_set("border-top-color"));
		assertEquals(StyleConstants.STYLE_BORDER_LEFT_COLOR, PerfectHash.in_word_set("border-left-color"));
		assertEquals(StyleConstants.STYLE_BORDER_RIGHT_COLOR, PerfectHash.in_word_set("border-right-color"));
		assertEquals(StyleConstants.STYLE_BORDER_BOTTOM_COLOR, PerfectHash.in_word_set("border-bottom-color"));
		assertEquals(StyleConstants.STYLE_LETTER_SPACING, PerfectHash.in_word_set("letter-spacing"));
		assertEquals(StyleConstants.STYLE_FONT_WEIGHT, PerfectHash.in_word_set("font-weight"));
		assertEquals(StyleConstants.STYLE_FONT_VARIANT, PerfectHash.in_word_set("font-variant"));
		assertEquals(StyleConstants.STYLE_MARGIN_LEFT, PerfectHash.in_word_set("margin-left"));
		assertEquals(StyleConstants.STYLE_MARGIN_RIGHT, PerfectHash.in_word_set("margin-right"));
		assertEquals(StyleConstants.STYLE_DISPLAY, PerfectHash.in_word_set("display"));
		assertEquals(StyleConstants.STYLE_TEXT_TRANSFORM, PerfectHash.in_word_set("text-transform"));
		assertEquals(StyleConstants.STYLE_BACKGROUND_POSITION_Y, PerfectHash.in_word_set("background-position-y"));
		assertEquals(StyleConstants.STYLE_PADDING_LEFT, PerfectHash.in_word_set("padding-left"));
		assertEquals(StyleConstants.STYLE_PADDING_RIGHT, PerfectHash.in_word_set("padding-right"));
		assertEquals(StyleConstants.STYLE_FONT_SIZE, PerfectHash.in_word_set("font-size"));
		assertEquals(StyleConstants.STYLE_FONT_STYLE, PerfectHash.in_word_set("font-style"));
		assertEquals(StyleConstants.STYLE_WHITE_SPACE, PerfectHash.in_word_set("white-space"));
		assertEquals(StyleConstants.STYLE_ORPHANS, PerfectHash.in_word_set("orphans"));
		assertEquals(StyleConstants.STYLE_MASTER_PAGE, PerfectHash.in_word_set("master-page"));
//		assertEquals(StyleConstants.STYLE_STRING_FORMAT , PerfectHash.in_word_set("string-format"));
		assertEquals(StyleConstants.STYLE_WORD_SPACING, PerfectHash.in_word_set("word-spacing"));
		assertEquals(StyleConstants.STYLE_BACKGROUND_POSITION_X, PerfectHash.in_word_set("background-position-x"));
		assertEquals(StyleConstants.STYLE_PAGE_BREAK_BEFORE, PerfectHash.in_word_set("page-break-before"));
		assertEquals(StyleConstants.STYLE_PAGE_BREAK_INSIDE, PerfectHash.in_word_set("page-break-inside"));
		assertEquals(StyleConstants.STYLE_SHOW_IF_BLANK, PerfectHash.in_word_set("show-if-blank"));
		assertEquals(StyleConstants.STYLE_FONT_FAMILY, PerfectHash.in_word_set("font-family"));
		assertEquals(StyleConstants.STYLE_PAGE_BREAK_AFTER, PerfectHash.in_word_set("page-break-after"));
		assertEquals(StyleConstants.STYLE_MARGIN_BOTTOM, PerfectHash.in_word_set("margin-bottom"));
		assertEquals(StyleConstants.STYLE_MARGIN_TOP, PerfectHash.in_word_set("margin-top"));
		assertEquals(StyleConstants.STYLE_WIDOWS, PerfectHash.in_word_set("widows"));
		assertEquals(StyleConstants.STYLE_PADDING_BOTTOM, PerfectHash.in_word_set("padding-bottom"));
		assertEquals(StyleConstants.STYLE_PADDING_TOP, PerfectHash.in_word_set("padding-top"));

	}

}
