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

package org.eclipse.birt.report.engine.parser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.elements.Style;

public class StyleUtil {

	public static Map<String, Integer> styleName2Index = null;
	public static Set<String> colorProperties;
	public static Map<String, Integer> ruleStyleName2Index = null;
	public static Map<String, Integer> customName2Index = null;

	public static void main(String[] args) {
		Field[] fields = HighlightRule.class.getFields();
		for (Field field : fields) {
			String name = field.getName();
			System.out.println("ruleStyleName2Index.put( HighlightRule." + name + ", StyleConstants.STYLE_"
					+ name.substring(0, name.length() - 7) + ");");
		}
	}

	static {
		styleName2Index = new HashMap<>();
		styleName2Index.put(Style.BACKGROUND_ATTACHMENT_PROP, StyleConstants.STYLE_BACKGROUND_ATTACHMENT);
		styleName2Index.put(Style.BACKGROUND_COLOR_PROP, StyleConstants.STYLE_BACKGROUND_COLOR);
		styleName2Index.put(Style.BACKGROUND_IMAGE_PROP, StyleConstants.STYLE_BACKGROUND_IMAGE);
		styleName2Index.put(Style.BACKGROUND_IMAGE_TYPE_PROP, StyleConstants.STYLE_BACKGROUND_IMAGE_TYPE);
		styleName2Index.put(Style.BACKGROUND_SIZE_HEIGHT, StyleConstants.STYLE_BACKGROUND_HEIGHT);
		styleName2Index.put(Style.BACKGROUND_SIZE_WIDTH, StyleConstants.STYLE_BACKGROUND_WIDTH);
		styleName2Index.put(Style.BACKGROUND_POSITION_X_PROP, StyleConstants.STYLE_BACKGROUND_POSITION_X);
		styleName2Index.put(Style.BACKGROUND_POSITION_Y_PROP, StyleConstants.STYLE_BACKGROUND_POSITION_Y);
		styleName2Index.put(Style.BACKGROUND_REPEAT_PROP, StyleConstants.STYLE_BACKGROUND_REPEAT);
		styleName2Index.put(Style.BORDER_BOTTOM_COLOR_PROP, StyleConstants.STYLE_BORDER_BOTTOM_COLOR);
		styleName2Index.put(Style.BORDER_BOTTOM_STYLE_PROP, StyleConstants.STYLE_BORDER_BOTTOM_STYLE);
		styleName2Index.put(Style.BORDER_BOTTOM_WIDTH_PROP, StyleConstants.STYLE_BORDER_BOTTOM_WIDTH);
		styleName2Index.put(Style.BORDER_LEFT_COLOR_PROP, StyleConstants.STYLE_BORDER_LEFT_COLOR);
		styleName2Index.put(Style.BORDER_LEFT_STYLE_PROP, StyleConstants.STYLE_BORDER_LEFT_STYLE);
		styleName2Index.put(Style.BORDER_LEFT_WIDTH_PROP, StyleConstants.STYLE_BORDER_LEFT_WIDTH);
		styleName2Index.put(Style.BORDER_RIGHT_COLOR_PROP, StyleConstants.STYLE_BORDER_RIGHT_COLOR);
		styleName2Index.put(Style.BORDER_RIGHT_STYLE_PROP, StyleConstants.STYLE_BORDER_RIGHT_STYLE);
		styleName2Index.put(Style.BORDER_RIGHT_WIDTH_PROP, StyleConstants.STYLE_BORDER_RIGHT_WIDTH);
		styleName2Index.put(Style.BORDER_TOP_COLOR_PROP, StyleConstants.STYLE_BORDER_TOP_COLOR);
		styleName2Index.put(Style.BORDER_TOP_STYLE_PROP, StyleConstants.STYLE_BORDER_TOP_STYLE);
		styleName2Index.put(Style.BORDER_TOP_WIDTH_PROP, StyleConstants.STYLE_BORDER_TOP_WIDTH);
		styleName2Index.put(Style.MARGIN_BOTTOM_PROP, StyleConstants.STYLE_MARGIN_BOTTOM);
		styleName2Index.put(Style.MARGIN_LEFT_PROP, StyleConstants.STYLE_MARGIN_LEFT);
		styleName2Index.put(Style.MARGIN_RIGHT_PROP, StyleConstants.STYLE_MARGIN_RIGHT);
		styleName2Index.put(Style.MARGIN_TOP_PROP, StyleConstants.STYLE_MARGIN_TOP);
		styleName2Index.put(Style.PADDING_TOP_PROP, StyleConstants.STYLE_PADDING_TOP);
		styleName2Index.put(Style.PADDING_LEFT_PROP, StyleConstants.STYLE_PADDING_LEFT);
		styleName2Index.put(Style.PADDING_BOTTOM_PROP, StyleConstants.STYLE_PADDING_BOTTOM);
		styleName2Index.put(Style.PADDING_RIGHT_PROP, StyleConstants.STYLE_PADDING_RIGHT);
		styleName2Index.put(Style.CAN_SHRINK_PROP, StyleConstants.STYLE_CAN_SHRINK);
		styleName2Index.put(Style.COLOR_PROP, StyleConstants.STYLE_COLOR);
		/*
		 * styleName2Index.put( Style.DATE_TIME_FORMAT_PROP,
		 * StyleConstants.STYLE_DATE_FORMAT ); styleName2Index.put(
		 * Style.DATE_FORMAT_PROP, StyleConstants.STYLE_SQL_DATE_FORMAT );
		 * styleName2Index.put( Style.TIME_FORMAT_PROP,
		 * StyleConstants.STYLE_SQL_TIME_FORMAT );
		 */
		styleName2Index.put(Style.FONT_FAMILY_PROP, StyleConstants.STYLE_FONT_FAMILY);
		styleName2Index.put(Style.FONT_SIZE_PROP, StyleConstants.STYLE_FONT_SIZE);
		styleName2Index.put(Style.FONT_STYLE_PROP, StyleConstants.STYLE_FONT_STYLE);
		styleName2Index.put(Style.FONT_WEIGHT_PROP, StyleConstants.STYLE_FONT_WEIGHT);
		styleName2Index.put(Style.FONT_VARIANT_PROP, StyleConstants.STYLE_FONT_VARIANT);
		styleName2Index.put(Style.TEXT_UNDERLINE_PROP, StyleConstants.STYLE_TEXT_UNDERLINE);
		styleName2Index.put(Style.TEXT_OVERLINE_PROP, StyleConstants.STYLE_TEXT_OVERLINE);
		styleName2Index.put(Style.TEXT_LINE_THROUGH_PROP, StyleConstants.STYLE_TEXT_LINETHROUGH);
		/*
		 * styleName2Index.put( Style.NUMBER_FORMAT_PROP,
		 * StyleConstants.STYLE_NUMBER_FORMAT );
		 */
		styleName2Index.put(Style.NUMBER_ALIGN_PROP, StyleConstants.STYLE_NUMBER_ALIGN);
		styleName2Index.put(Style.DISPLAY_PROP, StyleConstants.STYLE_DISPLAY);
		styleName2Index.put(Style.MASTER_PAGE_PROP, StyleConstants.STYLE_MASTER_PAGE);
		styleName2Index.put(Style.PAGE_BREAK_BEFORE_PROP, StyleConstants.STYLE_PAGE_BREAK_BEFORE);
		styleName2Index.put(Style.PAGE_BREAK_AFTER_PROP, StyleConstants.STYLE_PAGE_BREAK_AFTER);
		styleName2Index.put(Style.PAGE_BREAK_INSIDE_PROP, StyleConstants.STYLE_PAGE_BREAK_INSIDE);
		styleName2Index.put(Style.SHOW_IF_BLANK_PROP, StyleConstants.STYLE_SHOW_IF_BLANK);
		/*
		 * styleName2Index.put( Style.STRING_FORMAT_PROP,
		 * StyleConstants.STYLE_STRING_FORMAT );
		 */
		styleName2Index.put(Style.TEXT_ALIGN_PROP, StyleConstants.STYLE_TEXT_ALIGN);
		styleName2Index.put(Style.TEXT_INDENT_PROP, StyleConstants.STYLE_TEXT_INDENT);
		styleName2Index.put(Style.LETTER_SPACING_PROP, StyleConstants.STYLE_LETTER_SPACING);
		styleName2Index.put(Style.LINE_HEIGHT_PROP, StyleConstants.STYLE_LINE_HEIGHT);
		styleName2Index.put(Style.ORPHANS_PROP, StyleConstants.STYLE_ORPHANS);
		styleName2Index.put(Style.TEXT_TRANSFORM_PROP, StyleConstants.STYLE_TEXT_TRANSFORM);
		styleName2Index.put(Style.VERTICAL_ALIGN_PROP, StyleConstants.STYLE_VERTICAL_ALIGN);
		styleName2Index.put(Style.WHITE_SPACE_PROP, StyleConstants.STYLE_WHITE_SPACE);
		styleName2Index.put(Style.WIDOWS_PROP, StyleConstants.STYLE_WIDOWS);
		styleName2Index.put(Style.WORD_SPACING_PROP, StyleConstants.STYLE_WORD_SPACING);
		styleName2Index.put(Style.TEXT_DIRECTION_PROP, StyleConstants.STYLE_DIRECTION);
		styleName2Index.put(Style.OVERFLOW_PROP, StyleConstants.STYLE_OVERFLOW);
		styleName2Index.put(Style.HEIGHT_PROP, StyleConstants.STYLE_HEIGHT);
		styleName2Index.put(Style.WIDTH_PROP, StyleConstants.STYLE_WIDTH);

		colorProperties = new HashSet<>();
		colorProperties.add(Style.BACKGROUND_COLOR_PROP);
		colorProperties.add(Style.BORDER_BOTTOM_COLOR_PROP);
		colorProperties.add(Style.BORDER_LEFT_COLOR_PROP);
		colorProperties.add(Style.BORDER_RIGHT_COLOR_PROP);
		colorProperties.add(Style.BORDER_TOP_COLOR_PROP);
		colorProperties.add(Style.COLOR_PROP);

		ruleStyleName2Index = new HashMap<>();
		ruleStyleName2Index.put(HighlightRule.BORDER_TOP_STYLE_MEMBER, StyleConstants.STYLE_BORDER_TOP_STYLE);
		ruleStyleName2Index.put(HighlightRule.BORDER_TOP_WIDTH_MEMBER, StyleConstants.STYLE_BORDER_TOP_WIDTH);
		ruleStyleName2Index.put(HighlightRule.BORDER_TOP_COLOR_MEMBER, StyleConstants.STYLE_BORDER_TOP_COLOR);
		ruleStyleName2Index.put(HighlightRule.BORDER_LEFT_STYLE_MEMBER, StyleConstants.STYLE_BORDER_LEFT_STYLE);
		ruleStyleName2Index.put(HighlightRule.BORDER_LEFT_WIDTH_MEMBER, StyleConstants.STYLE_BORDER_LEFT_WIDTH);
		ruleStyleName2Index.put(HighlightRule.BORDER_LEFT_COLOR_MEMBER, StyleConstants.STYLE_BORDER_LEFT_COLOR);
		ruleStyleName2Index.put(HighlightRule.BORDER_BOTTOM_STYLE_MEMBER, StyleConstants.STYLE_BORDER_BOTTOM_STYLE);
		ruleStyleName2Index.put(HighlightRule.BORDER_BOTTOM_WIDTH_MEMBER, StyleConstants.STYLE_BORDER_BOTTOM_WIDTH);
		ruleStyleName2Index.put(HighlightRule.BORDER_BOTTOM_COLOR_MEMBER, StyleConstants.STYLE_BORDER_BOTTOM_COLOR);
		ruleStyleName2Index.put(HighlightRule.BORDER_RIGHT_STYLE_MEMBER, StyleConstants.STYLE_BORDER_RIGHT_STYLE);
		ruleStyleName2Index.put(HighlightRule.BORDER_RIGHT_WIDTH_MEMBER, StyleConstants.STYLE_BORDER_RIGHT_WIDTH);
		ruleStyleName2Index.put(HighlightRule.BORDER_RIGHT_COLOR_MEMBER, StyleConstants.STYLE_BORDER_RIGHT_COLOR);
		ruleStyleName2Index.put(HighlightRule.BACKGROUND_COLOR_MEMBER, StyleConstants.STYLE_BACKGROUND_COLOR);
		ruleStyleName2Index.put(HighlightRule.BACKGROUND_IMAGE_MEMBER, StyleConstants.STYLE_BACKGROUND_IMAGE);
		/*
		 * ruleStyleName2Index.put( HighlightRule.DATE_TIME_FORMAT_MEMBER,
		 * StyleConstants.STYLE_SQL_DATE_FORMAT ); ruleStyleName2Index.put(
		 * HighlightRule.NUMBER_FORMAT_MEMBER, StyleConstants.STYLE_NUMBER_FORMAT );
		 */
		ruleStyleName2Index.put(HighlightRule.NUMBER_ALIGN_MEMBER, StyleConstants.STYLE_NUMBER_ALIGN);
		/*
		 * ruleStyleName2Index.put( HighlightRule.STRING_FORMAT_MEMBER,
		 * StyleConstants.STYLE_STRING_FORMAT );
		 */
		ruleStyleName2Index.put(HighlightRule.FONT_FAMILY_MEMBER, StyleConstants.STYLE_FONT_FAMILY);
		ruleStyleName2Index.put(HighlightRule.FONT_SIZE_MEMBER, StyleConstants.STYLE_FONT_SIZE);
		ruleStyleName2Index.put(HighlightRule.FONT_STYLE_MEMBER, StyleConstants.STYLE_FONT_STYLE);
		ruleStyleName2Index.put(HighlightRule.FONT_WEIGHT_MEMBER, StyleConstants.STYLE_FONT_WEIGHT);
		ruleStyleName2Index.put(HighlightRule.FONT_VARIANT_MEMBER, StyleConstants.STYLE_FONT_VARIANT);
		ruleStyleName2Index.put(HighlightRule.COLOR_MEMBER, StyleConstants.STYLE_COLOR);
		ruleStyleName2Index.put(HighlightRule.TEXT_UNDERLINE_MEMBER, StyleConstants.STYLE_TEXT_UNDERLINE);
		ruleStyleName2Index.put(HighlightRule.TEXT_OVERLINE_MEMBER, StyleConstants.STYLE_TEXT_OVERLINE);
		ruleStyleName2Index.put(HighlightRule.TEXT_LINE_THROUGH_MEMBER, StyleConstants.STYLE_TEXT_LINETHROUGH);
		ruleStyleName2Index.put(HighlightRule.TEXT_ALIGN_MEMBER, StyleConstants.STYLE_TEXT_ALIGN);
		ruleStyleName2Index.put(HighlightRule.TEXT_TRANSFORM_MEMBER, StyleConstants.STYLE_TEXT_TRANSFORM);
		ruleStyleName2Index.put(HighlightRule.TEXT_INDENT_MEMBER, StyleConstants.STYLE_TEXT_INDENT);
		ruleStyleName2Index.put(HighlightRule.TEXT_DIRECTION_MEMBER, StyleConstants.STYLE_DIRECTION);
		ruleStyleName2Index.put(HighlightRule.PADDING_TOP_MEMBER, StyleConstants.STYLE_PADDING_TOP);
		ruleStyleName2Index.put(HighlightRule.PADDING_LEFT_MEMBER, StyleConstants.STYLE_PADDING_LEFT);
		ruleStyleName2Index.put(HighlightRule.PADDING_BOTTOM_MEMBER, StyleConstants.STYLE_PADDING_BOTTOM);
		ruleStyleName2Index.put(HighlightRule.PADDING_RIGHT_MEMBER, StyleConstants.STYLE_PADDING_RIGHT);
		ruleStyleName2Index.put(HighlightRule.BACKGROUND_REPEAT_MEMBER, StyleConstants.STYLE_BACKGROUND_REPEAT);
		ruleStyleName2Index.put(HighlightRule.LINE_HEIGHT_MEMBER, StyleConstants.STYLE_LINE_HEIGHT);

		customName2Index = new HashMap<>();
		customName2Index.put(BIRTConstants.BIRT_STYLE_DATA_FORMAT, StyleConstants.STYLE_DATA_FORMAT);
	}
}
