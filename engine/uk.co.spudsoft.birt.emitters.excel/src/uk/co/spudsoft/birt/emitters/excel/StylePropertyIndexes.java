package uk.co.spudsoft.birt.emitters.excel;

import org.eclipse.birt.report.engine.css.engine.BIRTPropertyManagerFactory;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;

public class StylePropertyIndexes {
	
	private static final BIRTPropertyManagerFactory propMgr = new BIRTPropertyManagerFactory();

	public static final int NUMBER_OF_BIRT_PROPERTIES = propMgr.getNumberOfProperties();
	
	public static String getPropertyName(int index) {
		return propMgr.getPropertyName(index);
	}
	
	public static final int STYLE_COLOR = propMgr.getPropertyIndex(CSSConstants.CSS_COLOR_PROPERTY);
	public static final int STYLE_DATA_FORMAT = propMgr.getPropertyIndex(BIRTConstants.BIRT_STYLE_DATA_FORMAT);
	public static final int STYLE_LINE_HEIGHT = propMgr.getPropertyIndex(CSSConstants.CSS_LINE_HEIGHT_PROPERTY);
	public static final int STYLE_PADDING_LEFT = propMgr.getPropertyIndex(CSSConstants.CSS_PADDING_LEFT_PROPERTY);
	public static final int STYLE_PADDING_RIGHT = propMgr.getPropertyIndex(CSSConstants.CSS_PADDING_RIGHT_PROPERTY);
	public static final int STYLE_DIRECTION = propMgr.getPropertyIndex(CSSConstants.CSS_DIRECTION_PROPERTY);
	public static final int STYLE_PADDING_TOP = propMgr.getPropertyIndex(CSSConstants.CSS_PADDING_TOP_PROPERTY);
	public static final int STYLE_BACKGROUND_HEIGHT = propMgr.getPropertyIndex(CSSConstants.CSS_BACKGROUND_HEIGHT_PROPERTY);
	public static final int STYLE_BACKGROUND_COLOR = propMgr.getPropertyIndex(CSSConstants.CSS_BACKGROUND_COLOR_PROPERTY);
	public static final int STYLE_BACKGROUND_REPEAT = propMgr.getPropertyIndex(CSSConstants.CSS_BACKGROUND_REPEAT_PROPERTY);
	public static final int STYLE_BORDER_RIGHT_WIDTH = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_RIGHT_WIDTH_PROPERTY);
	public static final int STYLE_BORDER_BOTTOM_WIDTH = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_BOTTOM_WIDTH_PROPERTY);
	public static final int STYLE_CAN_SHRINK = propMgr.getPropertyIndex(BIRTConstants.BIRT_CAN_SHRINK_PROPERTY);
	public static final int STYLE_BORDER_TOP_COLOR = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_TOP_COLOR_PROPERTY);
	public static final int STYLE_BORDER_RIGHT_COLOR = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_RIGHT_COLOR_PROPERTY);
	public static final int STYLE_BORDER_BOTTOM_COLOR = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_BOTTOM_COLOR_PROPERTY);
	public static final int STYLE_MARGIN_LEFT = propMgr.getPropertyIndex(CSSConstants.CSS_MARGIN_LEFT_PROPERTY);
	public static final int STYLE_MARGIN_RIGHT = propMgr.getPropertyIndex(CSSConstants.CSS_MARGIN_RIGHT_PROPERTY);
	public static final int STYLE_PADDING_BOTTOM = propMgr.getPropertyIndex(CSSConstants.CSS_PADDING_BOTTOM_PROPERTY);
	public static final int STYLE_MARGIN_TOP = propMgr.getPropertyIndex(CSSConstants.CSS_MARGIN_TOP_PROPERTY);
	public static final int STYLE_TEXT_INDENT = propMgr.getPropertyIndex(CSSConstants.CSS_TEXT_INDENT_PROPERTY);
	public static final int STYLE_BORDER_RIGHT_STYLE = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_RIGHT_STYLE_PROPERTY);
	public static final int STYLE_BORDER_BOTTOM_STYLE = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_BOTTOM_STYLE_PROPERTY);
	public static final int STYLE_TEXT_ALIGN = propMgr.getPropertyIndex(CSSConstants.CSS_TEXT_ALIGN_PROPERTY);
	// public static final int STYLE_HEIGHT = propMgr.getPropertyIndex(CSSConstants.CSS_HEIGHT_PROPERTY);
	public static final int STYLE_NUMBER_ALIGN = propMgr.getPropertyIndex(BIRTConstants.BIRT_NUMBER_ALIGN_PROPERTY);
	// public static final int STYLE_WIDTH = propMgr.getPropertyIndex(CSSConstants.CSS_WIDTH_PROPERTY);
	public static final int STYLE_TEXT_LINETHROUGH = propMgr.getPropertyIndex(BIRTConstants.BIRT_TEXT_LINETHROUGH_PROPERTY);
	public static final int STYLE_ORPHANS = propMgr.getPropertyIndex(CSSConstants.CSS_ORPHANS_PROPERTY);
	public static final int STYLE_FONT_WEIGHT = propMgr.getPropertyIndex(CSSConstants.CSS_FONT_WEIGHT_PROPERTY);
	public static final int STYLE_FONT_VARIANT = propMgr.getPropertyIndex(CSSConstants.CSS_FONT_VARIANT_PROPERTY);
	public static final int STYLE_MARGIN_BOTTOM = propMgr.getPropertyIndex(CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY);
	public static final int STYLE_BACKGROUND_POSITION_X = propMgr.getPropertyIndex(BIRTConstants.BIRT_BACKGROUND_POSITION_X_PROPERTY);
	public static final int STYLE_PAGE_BREAK_BEFORE = propMgr.getPropertyIndex(CSSConstants.CSS_PAGE_BREAK_BEFORE_PROPERTY);
	public static final int STYLE_TEXT_OVERLINE = propMgr.getPropertyIndex(BIRTConstants.BIRT_TEXT_OVERLINE_PROPERTY);
	public static final int STYLE_TEXT_TRANSFORM = propMgr.getPropertyIndex(CSSConstants.CSS_TEXT_TRANSFORM_PROPERTY);
	public static final int STYLE_BACKGROUND_WIDTH = propMgr.getPropertyIndex(CSSConstants.CSS_BACKGROUND_WIDTH_PROPERTY);
	public static final int STYLE_BACKGROUND_POSITION_Y = propMgr.getPropertyIndex(BIRTConstants.BIRT_BACKGROUND_POSITION_Y_PROPERTY);
	public static final int STYLE_OVERFLOW = propMgr.getPropertyIndex(CSSConstants.CSS_OVERFLOW_PROPERTY);
	public static final int STYLE_FONT_SIZE = propMgr.getPropertyIndex(CSSConstants.CSS_FONT_SIZE_PROPERTY);
	public static final int STYLE_FONT_STYLE = propMgr.getPropertyIndex(CSSConstants.CSS_FONT_STYLE_PROPERTY);
	public static final int STYLE_BORDER_TOP_WIDTH = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_TOP_WIDTH_PROPERTY);
	public static final int STYLE_BORDER_LEFT_WIDTH = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_LEFT_WIDTH_PROPERTY);
	public static final int STYLE_SHOW_IF_BLANK = propMgr.getPropertyIndex(BIRTConstants.BIRT_SHOW_IF_BLANK_PROPERTY);
	public static final int STYLE_LETTER_SPACING = propMgr.getPropertyIndex(CSSConstants.CSS_LETTER_SPACING_PROPERTY);
	public static final int STYLE_BACKGROUND_IMAGE = propMgr.getPropertyIndex(CSSConstants.CSS_BACKGROUND_IMAGE_PROPERTY);
	public static final int STYLE_BORDER_LEFT_COLOR = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_LEFT_COLOR_PROPERTY);
	public static final int STYLE_BACKGROUND_ATTACHMENT = propMgr.getPropertyIndex(CSSConstants.CSS_BACKGROUND_ATTACHMENT_PROPERTY);
	public static final int STYLE_VERTICAL_ALIGN = propMgr.getPropertyIndex(CSSConstants.CSS_VERTICAL_ALIGN_PROPERTY);
	public static final int STYLE_BORDER_TOP_STYLE = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_TOP_STYLE_PROPERTY);
	public static final int STYLE_DISPLAY = propMgr.getPropertyIndex(CSSConstants.CSS_DISPLAY_PROPERTY);
	public static final int STYLE_MASTER_PAGE = propMgr.getPropertyIndex(BIRTConstants.BIRT_MASTER_PAGE_PROPERTY);
	public static final int STYLE_BORDER_LEFT_STYLE = propMgr.getPropertyIndex(CSSConstants.CSS_BORDER_LEFT_STYLE_PROPERTY);
	public static final int STYLE_VISIBLE_FORMAT = propMgr.getPropertyIndex(BIRTConstants.BIRT_VISIBLE_FORMAT_PROPERTY);
	public static final int STYLE_WIDOWS = propMgr.getPropertyIndex(CSSConstants.CSS_WIDOWS_PROPERTY);
	public static final int STYLE_FONT_FAMILY = propMgr.getPropertyIndex(CSSConstants.CSS_FONT_FAMILY_PROPERTY);
	public static final int STYLE_PAGE_BREAK_INSIDE = propMgr.getPropertyIndex(CSSConstants.CSS_PAGE_BREAK_INSIDE_PROPERTY);
	public static final int STYLE_PAGE_BREAK_AFTER = propMgr.getPropertyIndex(CSSConstants.CSS_PAGE_BREAK_AFTER_PROPERTY);
	public static final int STYLE_TEXT_UNDERLINE = propMgr.getPropertyIndex(BIRTConstants.BIRT_TEXT_UNDERLINE_PROPERTY);
	public static final int STYLE_WORD_SPACING = propMgr.getPropertyIndex(CSSConstants.CSS_WORD_SPACING_PROPERTY);
	public static final int STYLE_WHITE_SPACE = propMgr.getPropertyIndex(CSSConstants.CSS_WHITE_SPACE_PROPERTY);

}
