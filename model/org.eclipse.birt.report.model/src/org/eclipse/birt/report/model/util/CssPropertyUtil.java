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

package org.eclipse.birt.report.model.util;

import java.util.regex.Pattern;

import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Utility class to do converting work between CSSs standard and Model style
 * elements.
 */

public class CssPropertyUtil {

	/**
	 * The wrong value of a CSS url.
	 */

	public static final String WRONG_URL = "URL(-1)"; //$NON-NLS-1$

	/**
	 * Regular expression for "url( images/land )".
	 * 
	 * @see java.net.URLDecoder
	 */

	private static final String URL_CSS_PATTERN_1 = "[uU][rR][lL][(]" + //$NON-NLS-1$
			"[\\s]*[\\w%.\\*\\-\\/:]*" + //$NON-NLS-1$
			"[\\s]*[)]"; //$NON-NLS-1$

	/**
	 * Compiled pattern for CSS URL.
	 */

	private static Pattern cssURLPattern_1 = Pattern.compile(URL_CSS_PATTERN_1);

	/**
	 * Regular expression for "url( "images/land" )".
	 * 
	 * @see java.net.URLDecoder
	 */

	private static final String URL_CSS_PATTERN_2 = "[uU][rR][lL][(][\\s]*[\'\"]" + //$NON-NLS-1$
			"[\\s]*[\\w%.\\*\\-\\/:]*" + //$NON-NLS-1$
			"[\\s]*[\'\"][\\s]*[)]"; //$NON-NLS-1$

	/**
	 * Compiled pattern for CSS URL.
	 */

	private static Pattern cssURLPattern_2 = Pattern.compile(URL_CSS_PATTERN_2);

	/**
	 * Gets the corresponding property name of Model defined with a given css
	 * property name.
	 * 
	 * @param cssPropertyName the css property name
	 * @return the corresponding property name of Model defined if found, otherwise
	 *         false
	 */

	public static final String getPropertyName(String cssPropertyName) {
		if (cssPropertyName == null)
			return null;
		String name = cssPropertyName.toLowerCase();

		if (CssPropertyConstants.ATTR_FONT_FAMILY.equals(name))
			return IStyleModel.FONT_FAMILY_PROP;
		if (CssPropertyConstants.ATTR_FONT_SIZE.equals(name))
			return IStyleModel.FONT_SIZE_PROP;
		if (CssPropertyConstants.ATTR_FONT_STYLE.equals(name))
			return IStyleModel.FONT_STYLE_PROP;
		if (CssPropertyConstants.ATTR_FONT_VARIANT.equals(name))
			return IStyleModel.FONT_VARIANT_PROP;
		if (CssPropertyConstants.ATTR_FONT_WEIGTH.equals(name))
			return IStyleModel.FONT_WEIGHT_PROP;

		if (CssPropertyConstants.ATTR_TEXT_ALIGN.equals(name))
			return IStyleModel.TEXT_ALIGN_PROP;
		if (CssPropertyConstants.ATTR_TEXT_INDENT.equals(name))
			return IStyleModel.TEXT_INDENT_PROP;
		if (CssPropertyConstants.ATTR_LETTER_SPACING.equals(name))
			return IStyleModel.LETTER_SPACING_PROP;
		if (CssPropertyConstants.ATTR_WORD_SPACING.equals(name))
			return IStyleModel.WORD_SPACING_PROP;
		if (CssPropertyConstants.ATTR_TEXT_TRANSFORM.equals(name))
			return IStyleModel.TEXT_TRANSFORM_PROP;
		if (CssPropertyConstants.ATTR_WHITE_SPACE.equals(name))
			return IStyleModel.WHITE_SPACE_PROP;

		if (CssPropertyConstants.ATTR_MARGIN_TOP.equals(name))
			return IStyleModel.MARGIN_TOP_PROP;
		if (CssPropertyConstants.ATTR_MARGIN_RIGHT.equals(name))
			return IStyleModel.MARGIN_RIGHT_PROP;
		if (CssPropertyConstants.ATTR_MARGIN_BOTTOM.equals(name))
			return IStyleModel.MARGIN_BOTTOM_PROP;
		if (CssPropertyConstants.ATTR_MARGIN_LEFT.equals(name))
			return IStyleModel.MARGIN_LEFT_PROP;

		if (CssPropertyConstants.ATTR_PADDING_TOP.equals(name))
			return IStyleModel.PADDING_TOP_PROP;
		if (CssPropertyConstants.ATTR_PADDING_RIGHT.equals(name))
			return IStyleModel.PADDING_RIGHT_PROP;
		if (CssPropertyConstants.ATTR_PADDING_BOTTOM.equals(name))
			return IStyleModel.PADDING_BOTTOM_PROP;
		if (CssPropertyConstants.ATTR_PADDING_LEFT.equals(name))
			return IStyleModel.PADDING_LEFT_PROP;

		if (CssPropertyConstants.ATTR_COLOR.equals(name))
			return IStyleModel.COLOR_PROP;
		if (CssPropertyConstants.ATTR_BACKGROUND_COLOR.equals(name))
			return IStyleModel.BACKGROUND_COLOR_PROP;
		if (CssPropertyConstants.ATTR_BACKGROUND_IMAGE.equals(name))
			return IStyleModel.BACKGROUND_IMAGE_PROP;
		if (CssPropertyConstants.ATTR_BACKGROUND_REPEAT.equals(name))
			return IStyleModel.BACKGROUND_REPEAT_PROP;
		if (CssPropertyConstants.ATTR_BACKGROUND_ATTACHEMNT.equals(name))
			return IStyleModel.BACKGROUND_ATTACHMENT_PROP;

		if (CssPropertyConstants.ATTR_ORPHANS.equals(name))
			return IStyleModel.ORPHANS_PROP;
		if (CssPropertyConstants.ATTR_WIDOWS.equals(name))
			return IStyleModel.WIDOWS_PROP;
		if (CssPropertyConstants.ATTR_DISPLAY.equals(name))
			return IStyleModel.DISPLAY_PROP;
		if (CssPropertyConstants.ATTR_PAGE_BREAK_BEFORE.equals(name))
			return IStyleModel.PAGE_BREAK_BEFORE_PROP;
		if (CssPropertyConstants.ATTR_PAGE_BREAK_AFTER.equals(name))
			return IStyleModel.PAGE_BREAK_AFTER_PROP;
		if (CssPropertyConstants.ATTR_PAGE_BREAK_INSIDE.equals(name))
			return IStyleModel.PAGE_BREAK_INSIDE_PROP;

		if (CssPropertyConstants.ATTR_VERTICAL_ALIGN.equals(name))
			return IStyleModel.VERTICAL_ALIGN_PROP;
		if (CssPropertyConstants.ATTR_LINE_HEIGHT.equals(name))
			return IStyleModel.LINE_HEIGHT_PROP;

		if (CssPropertyConstants.ATTR_BORDER_BOTTOM_COLOR.equals(name))
			return IStyleModel.BORDER_BOTTOM_COLOR_PROP;
		if (CssPropertyConstants.ATTR_BORDER_BOTTOM_STYLE.equals(name))
			return IStyleModel.BORDER_BOTTOM_STYLE_PROP;
		if (CssPropertyConstants.ATTR_BORDER_BOTTOM_WIDTH.equals(name))
			return IStyleModel.BORDER_BOTTOM_WIDTH_PROP;
		if (CssPropertyConstants.ATTR_BORDER_LEFT_COLOR.equals(name))
			return IStyleModel.BORDER_LEFT_COLOR_PROP;
		if (CssPropertyConstants.ATTR_BORDER_LEFT_STYLE.equals(name))
			return IStyleModel.BORDER_LEFT_STYLE_PROP;
		if (CssPropertyConstants.ATTR_BORDER_LEFT_WIDTH.equals(name))
			return IStyleModel.BORDER_LEFT_WIDTH_PROP;
		if (CssPropertyConstants.ATTR_BORDER_RIGHT_COLOR.equals(name))
			return IStyleModel.BORDER_RIGHT_COLOR_PROP;
		if (CssPropertyConstants.ATTR_BORDER_RIGHT_STYLE.equals(name))
			return IStyleModel.BORDER_RIGHT_STYLE_PROP;
		if (CssPropertyConstants.ATTR_BORDER_RIGHT_WIDTH.equals(name))
			return IStyleModel.BORDER_RIGHT_WIDTH_PROP;
		if (CssPropertyConstants.ATTR_BORDER_TOP_COLOR.equals(name))
			return IStyleModel.BORDER_TOP_COLOR_PROP;
		if (CssPropertyConstants.ATTR_BORDER_TOP_STYLE.equals(name))
			return IStyleModel.BORDER_TOP_STYLE_PROP;
		if (CssPropertyConstants.ATTR_BORDER_TOP_WIDTH.equals(name))
			return IStyleModel.BORDER_TOP_WIDTH_PROP;
		if (CssPropertyConstants.DIRECTION.equals(name))
			return IStyleModel.TEXT_DIRECTION_PROP;

		if (CssPropertyConstants.HEIGHT.equals(name))
			return IStyleModel.HEIGHT_PROP;
		if (CssPropertyConstants.WIDTH.equals(name))
			return IStyleModel.WIDTH_PROP;

		return null;
	}

	/**
	 * Translates the URL value in CSS format to BIRT format.
	 * 
	 * @param cssValue the URL value in CSS format
	 * @return URL value in the BIRT format if the input matches the pattern,
	 *         otherwise <code>WRONG_URL</code>
	 */

	public static String getURLValue(String cssValue) {
		if (cssValue == null)
			return null;

		if (cssURLPattern_1.matcher(cssValue).matches()) {
			int start = cssValue.indexOf('(');
			int end = cssValue.indexOf(')');

			String value = cssValue.substring(start + 1, end).trim();
			return value;
		} else if (cssURLPattern_2.matcher(cssValue).matches()) {
			int start = cssValue.indexOf('(');
			int end = cssValue.indexOf(')');

			// discard the URL and ()

			String value = cssValue.substring(start + 1, end).trim();

			// discard the "" or ''

			int length = value.length();
			value = value.substring(1, length - 1);
			value = value.trim();
			return value;

		}
		return WRONG_URL;

	}
}
