/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.html;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.css.CSSValue;

//FIXME: code review: We should list all the properties according the CSS.

/**
 * <code>AttributeBuilder</code> is a concrete class that HTML Emitters use to
 * build the Style strings.
 *
 */
public class AttributeBuilder {

	/**
	 * Build the relative position of a component. This method is obsolete.
	 */
	public static String buildPos(DimensionType x, DimensionType y, DimensionType width, DimensionType height) {
		StringBuffer content = new StringBuffer();

		if (x != null || y != null) {
			content.append("position: relative;"); //$NON-NLS-1$
			buildSize(content, HTMLTags.ATTR_LEFT, x);
			buildSize(content, HTMLTags.ATTR_TOP, y);
		}

		buildSize(content, HTMLTags.ATTR_WIDTH, width);
		buildSize(content, HTMLTags.ATTR_HEIGHT, height);

		return content.toString();
	}

	/**
	 * Builds the Visual style string.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 */
	public static void buildVisual(StringBuffer styleBuffer, IStyle style) {
		// move display property from css file to html file
		// buildProperty( styleBuffer, "display", style.getDisplay( ) );
		// vertical-align has effect on inline-object and table-cell, so remove it
		// from the standard style builder.
		// buildProperty( styleBuffer, HTMLTags.ATTR_VERTICAL_ALIGN, style
		// .getVerticalAlign( ) );
		buildProperty(styleBuffer, HTMLTags.ATTR_LINE_HEIGHT, style.getLineHeight()); // $NON-NLS-1$
	}

	/**
	 * Build the PagedMedia style string.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 */
//	private static void buildPagedMedia( StringBuffer styleBuffer, IStyle style )
//	{
//		We should not write the extra pagination information in style classes.
//		buildProperty( styleBuffer, HTMLTags.ATTR_ORPHANS, style.getOrphans( ) );
//		buildProperty( styleBuffer, HTMLTags.ATTR_WIDOWS, style.getWidows( ) );
//		buildProperty( styleBuffer, HTMLTags.ATTR_PAGE_BREAK_BEFORE, style
//				.getPageBreakBefore( ) );
//		buildProperty( styleBuffer, HTMLTags.ATTR_PAGE_BREAK_AFTER, style
//				.getPageBreakAfter( ) );
//		buildProperty( styleBuffer, HTMLTags.ATTR_PAGE_BREAK_INSIDE, style
//				.getPageBreakInside( ) );
//	}

	/**
	 * Build the background style string.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 * @param emitter     The <code>HTMLReportEmitter</code> object which provides
	 *                    resource manager and hyperlink builder objects.
	 */
	public static void buildBackground(StringBuffer styleBuffer, IStyle style, HTMLReportEmitter emitter) {
		buildProperty(styleBuffer, HTMLTags.ATTR_BACKGROUND_COLOR, style.getBackgroundColor());

		String image = style.getBackgroundImage();
		if (image == null || "none".equalsIgnoreCase(image)) //$NON-NLS-1$
		{
			return;
		}

		image = emitter.handleStyleImage(image, true, style);
		if (image != null && image.length() > 0) {
			buildURLProperty(styleBuffer, HTMLTags.ATTR_BACKGROUND_IMAGE, image);
			buildProperty(styleBuffer, HTMLTags.ATTR_BACKGROUND_REPEAT, style.getBackgroundRepeat());
			buildProperty(styleBuffer, HTMLTags.ATTR_BACKGROUND_ATTACHEMNT, style.getBackgroundAttachment());

			String x = style.getBackgroundPositionX();
			String y = style.getBackgroundPositionY();
			if (x != null || y != null) {
				if (x == null) {
					x = "0pt";
				}
				if (y == null) {
					y = "0pt";
				}
				addPropName(styleBuffer, HTMLTags.ATTR_BACKGROUND_POSITION);
				addPropValue(styleBuffer, x);
				addPropValue(styleBuffer, y);
				styleBuffer.append(';');
			}
		}
	}

	public static void buildBackgroundColor(StringBuffer styleBuffer, IStyle style, HTMLReportEmitter emitter) {
		buildProperty(styleBuffer, HTMLTags.ATTR_BACKGROUND_COLOR, style.getBackgroundColor());
	}

	/**
	 * Build the Box style string.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 */
	public static void buildBox(StringBuffer styleBuffer, IStyle style) {
		buildMargins(styleBuffer, style);
		buildPaddings(styleBuffer, style);
		buildBorders(styleBuffer, style);
	}

	/**
	 * Build the margins.
	 *
	 * @param styleBuffer
	 * @param style
	 */
	public static void buildMargins(StringBuffer styleBuffer, IStyle style) {
		// build the margins
		String topMargin = style.getMarginTop();
		String rightMargin = style.getMarginRight();
		String bottomMargin = style.getMarginBottom();
		String leftMargin = style.getMarginLeft();

		if (null != topMargin && null != rightMargin && null != bottomMargin && null != leftMargin) {
			if (rightMargin.equals(leftMargin)) {
				if (topMargin.equals(bottomMargin)) {
					if (topMargin.equals(rightMargin)) {
						// The four margins have the same value
						buildProperty(styleBuffer, HTMLTags.ATTR_MARGIN, topMargin);
					} else {
						// The top & bottom margins have the same value. The
						// right & left margins have the same value.
						addPropName(styleBuffer, HTMLTags.ATTR_MARGIN);
						addPropValue(styleBuffer, topMargin);
						addPropValue(styleBuffer, rightMargin);
						styleBuffer.append(';');
					}
				} else {
					// only the right & left margins have the same value.
					addPropName(styleBuffer, HTMLTags.ATTR_MARGIN);
					addPropValue(styleBuffer, topMargin);
					addPropValue(styleBuffer, rightMargin);
					addPropValue(styleBuffer, bottomMargin);
					styleBuffer.append(';');
				}
			} else {
				// four margins have different values.
				addPropName(styleBuffer, HTMLTags.ATTR_MARGIN);
				addPropValue(styleBuffer, topMargin);
				addPropValue(styleBuffer, rightMargin);
				addPropValue(styleBuffer, bottomMargin);
				addPropValue(styleBuffer, leftMargin);
				styleBuffer.append(';');
			}
		} else {
			// At least one margin has null value.
			buildProperty(styleBuffer, HTMLTags.ATTR_MARGIN_TOP, topMargin);
			buildProperty(styleBuffer, HTMLTags.ATTR_MARGIN_RIGHT, rightMargin);
			buildProperty(styleBuffer, HTMLTags.ATTR_MARGIN_BOTTOM, bottomMargin);
			buildProperty(styleBuffer, HTMLTags.ATTR_MARGIN_LEFT, leftMargin);
		}
	}

	/**
	 * Build the paddings.
	 *
	 * @param styleBuffer
	 * @param style
	 */
	public static void buildPaddings(StringBuffer styleBuffer, IStyle style) {
		// build the paddings
		String topPadding = style.getPaddingTop();
		String rightPadding = style.getPaddingRight();
		String bottomPadding = style.getPaddingBottom();
		String leftPadding = style.getPaddingLeft();
		if (null != topPadding && null != rightPadding && null != bottomPadding && null != leftPadding) {
			if (rightPadding.equals(leftPadding)) {
				if (topPadding.equals(bottomPadding)) {
					if (topPadding.equals(rightPadding)) {
						// The four paddings have the same value
						buildProperty(styleBuffer, HTMLTags.ATTR_PADDING, topPadding);
					} else {
						// The top & bottom paddings have the same value. The
						// right & left paddings have the same value.
						addPropName(styleBuffer, HTMLTags.ATTR_PADDING);
						addPropValue(styleBuffer, topPadding);
						addPropValue(styleBuffer, rightPadding);
						styleBuffer.append(';');
					}
				} else {
					// only the right & left paddings have the same value.
					addPropName(styleBuffer, HTMLTags.ATTR_PADDING);
					addPropValue(styleBuffer, topPadding);
					addPropValue(styleBuffer, rightPadding);
					addPropValue(styleBuffer, bottomPadding);
					styleBuffer.append(';');
				}
			} else {
				// four paddings have different values.
				addPropName(styleBuffer, HTMLTags.ATTR_PADDING);
				addPropValue(styleBuffer, topPadding);
				addPropValue(styleBuffer, rightPadding);
				addPropValue(styleBuffer, bottomPadding);
				addPropValue(styleBuffer, leftPadding);
				styleBuffer.append(';');
			}
		} else {
			// At least one paddings has null value.
			buildProperty(styleBuffer, HTMLTags.ATTR_PADDING_TOP, topPadding);
			buildProperty(styleBuffer, HTMLTags.ATTR_PADDING_RIGHT, rightPadding);
			buildProperty(styleBuffer, HTMLTags.ATTR_PADDING_BOTTOM, bottomPadding);
			buildProperty(styleBuffer, HTMLTags.ATTR_PADDING_LEFT, leftPadding);
		}
	}

	/**
	 * Build the borders.
	 *
	 * @param styleBuffer
	 * @param style
	 */
	public static void buildBorders(StringBuffer styleBuffer, IStyle style) {
		// build the borders
		String topBorderWidth = style.getBorderTopWidth();
		String topBorderStyle = style.getBorderTopStyle();
		String topBorderColor = style.getBorderTopColor();
		String rightBorderWidth = style.getBorderRightWidth();
		String rightBorderStyle = style.getBorderRightStyle();
		String rightBorderColor = style.getBorderRightColor();
		String bottomBorderWidth = style.getBorderBottomWidth();
		String bottomBorderStyle = style.getBorderBottomStyle();
		String bottomBorderColor = style.getBorderBottomColor();
		String leftBorderWidth = style.getBorderLeftWidth();
		String leftBorderStyle = style.getBorderLeftStyle();
		String leftBorderColor = style.getBorderLeftColor();

		if ((null != topBorderWidth && topBorderWidth.equals(rightBorderWidth)
				&& topBorderWidth.equals(bottomBorderWidth) && topBorderWidth.equals(leftBorderWidth))
				|| (null == topBorderWidth && null == rightBorderWidth && null == bottomBorderWidth
						&& null == leftBorderWidth)) {
			if ((null != topBorderStyle && topBorderStyle.equals(rightBorderStyle)
					&& topBorderStyle.equals(bottomBorderStyle) && topBorderStyle.equals(leftBorderStyle))
					|| (null == topBorderStyle && null == rightBorderStyle && null == bottomBorderStyle
							&& null == leftBorderStyle)) {
				if ((null != topBorderColor && topBorderColor.equals(rightBorderColor)
						&& topBorderColor.equals(bottomBorderColor) && topBorderColor.equals(leftBorderColor))
						|| (null == topBorderColor && null == rightBorderColor && null == bottomBorderColor
								&& null == leftBorderColor)) {
					// if the four borders have the same value, compact html
					// ouput
					buildBorder(styleBuffer, HTMLTags.ATTR_BORDER, topBorderWidth, topBorderStyle, topBorderColor);
					return;
				}
			}
		}

		buildBorder(styleBuffer, HTMLTags.ATTR_BORDER_TOP, topBorderWidth, topBorderStyle, topBorderColor);

		buildBorder(styleBuffer, HTMLTags.ATTR_BORDER_RIGHT, rightBorderWidth, rightBorderStyle, rightBorderColor);

		buildBorder(styleBuffer, HTMLTags.ATTR_BORDER_BOTTOM, bottomBorderWidth, bottomBorderStyle, bottomBorderColor);

		buildBorder(styleBuffer, HTMLTags.ATTR_BORDER_LEFT, leftBorderWidth, leftBorderStyle, leftBorderColor);
	}

	/**
	 * Build the Text style string.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 * @param bContainer  true: shouldn't output the text-decoration.
	 */
	public static void buildText(StringBuffer styleBuffer, IStyle style) {
		buildProperty(styleBuffer, HTMLTags.ATTR_TEXT_INDENT, style.getTextIndent());
		// buildProperty( styleBuffer, HTMLTags.ATTR_TEXT_ALIGN, style.getTextAlign( )
		// );

		// as the HTML handles text-decoration different in IE/FIREFOX, so we need
		// handle the text-decoration as computed column. It doesn't need output to
		// style definition.
		// if ( !bContainer )
		// {
		// buildTextDecoration( styleBuffer, style );
		// }

		buildProperty(styleBuffer, HTMLTags.ATTR_LETTER_SPACING, style.getLetterSpacing());
		buildProperty(styleBuffer, HTMLTags.ATTR_WORD_SPACING, style.getWordSpacing());
		buildProperty(styleBuffer, HTMLTags.ATTR_TEXT_TRANSFORM, style.getTextTransform());
		buildProperty(styleBuffer, HTMLTags.ATTR_WHITE_SPACE, style.getWhiteSpace());
	}

	/**
	 * Build Font style string.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 */
	public static void buildFont(StringBuffer styleBuffer, IStyle style) {
		buildProperty(styleBuffer, HTMLTags.ATTR_FONT_FAMILY, style.getFontFamily());

		buildProperty(styleBuffer, HTMLTags.ATTR_FONT_STYLE, style.getFontStyle());

		buildProperty(styleBuffer, HTMLTags.ATTR_FONT_VARIANT, style.getFontVariant());

		buildProperty(styleBuffer, HTMLTags.ATTR_FONT_WEIGTH, style.getFontWeight());

		buildProperty(styleBuffer, HTMLTags.ATTR_FONT_SIZE, style.getFontSize());

		buildProperty(styleBuffer, HTMLTags.ATTR_COLOR, style.getColor());
	}

	/**
	 * Build the Text-Decoration style string.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param linethrough The line-through value.
	 * @param underline   The underline value.
	 * @param overline    The overline value.
	 */
	public static void buildTextDecoration(StringBuffer styleBuffer, IStyle style) {
		CSSValue linethrough = style.getProperty(IStyle.STYLE_TEXT_LINETHROUGH);
		CSSValue underline = style.getProperty(IStyle.STYLE_TEXT_UNDERLINE);
		CSSValue overline = style.getProperty(IStyle.STYLE_TEXT_OVERLINE);

		if (linethrough == IStyle.LINE_THROUGH_VALUE || underline == IStyle.UNDERLINE_VALUE
				|| overline == IStyle.OVERLINE_VALUE) {
			styleBuffer.append(" text-decoration:"); //$NON-NLS-1$
			if (IStyle.LINE_THROUGH_VALUE == linethrough) {
				addPropValue(styleBuffer, "line-through");
			}
			if (IStyle.UNDERLINE_VALUE == underline) {
				addPropValue(styleBuffer, "underline");
			}
			if (IStyle.OVERLINE_VALUE == overline) {
				addPropValue(styleBuffer, "overline");
			}
			styleBuffer.append(';');
		}
	}

	/**
	 * Build the border string.
	 * <li>ignore all the border styles is style is null
	 * <li>CSS default border-color is the font-color, while BIRT is black
	 * <li>border-color is not inheritable.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param name        The proerty name.
	 * @param width       The border-width value.
	 * @param style       The border-style value.
	 * @param color       The border-color value
	 */
	static void buildBorder(StringBuffer styleBuffer, String name, String width, String style, String color) {
		if (style == null || style.length() <= 0) {
			return;
		}
		addPropName(styleBuffer, name);
		addPropValue(styleBuffer, width);
		addPropValue(styleBuffer, style);
		addPropValue(styleBuffer, color == null ? "black" : color); //$NON-NLS-1$
		styleBuffer.append(';');
	}

	/**
	 * Build size style string say, "width: 10.0mm;".
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param name        The property name
	 * @param value       The values of the property
	 */
	public static void buildSize(StringBuffer styleBuffer, String name, DimensionType value) {
		if (value != null) {
			if (HTMLTags.ATTR_MIN_HEIGHT.equals(name)) {
				// To solve the problem that IE do not support min-height.
				// Use this way to make Firefox and IE both work well.
				addPropName(styleBuffer, HTMLTags.ATTR_HEIGHT);
				addPropValue(styleBuffer, "auto !important");
				styleBuffer.append(';');
				addPropName(styleBuffer, HTMLTags.ATTR_HEIGHT);
				addPropValue(styleBuffer, value.toString());
				styleBuffer.append(';');
				addPropName(styleBuffer, HTMLTags.ATTR_MIN_HEIGHT);
				addPropValue(styleBuffer, value.toString());
				styleBuffer.append(';');
			} else {
				addPropName(styleBuffer, name);
				addPropValue(styleBuffer, value.toString());
				styleBuffer.append(';');
			}
		}
	}

	/**
	 * Build size style, set height and width
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 */
	public static void buildSize(StringBuffer styleBuffer, IStyle style) {
		buildProperty(styleBuffer, HTMLTags.ATTR_HEIGHT, style.getHeight());
		buildProperty(styleBuffer, HTMLTags.ATTR_WIDTH, style.getWidth());
	}

	/**
	 * Build the style property.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param name        The name of the property
	 * @param value       The values of the property
	 */
	private static void buildProperty(StringBuffer styleBuffer, String name, String value) {
		if (value != null) {
			addPropName(styleBuffer, name);
			addPropValue(styleBuffer, value);
			styleBuffer.append(';');
		}
	}

	private static void buildURLProperty(StringBuffer styleBuffer, String name, String url) {
		if (url != null) {
			addPropName(styleBuffer, name);
			addURLValue(styleBuffer, url);
			styleBuffer.append(';');
		}
	}

	/**
	 * Add property name to the Style string.
	 *
	 * @param styleBuffer The StringBuffer to which the result should be output.
	 * @param name        The property name.
	 */
	private static void addPropName(StringBuffer styleBuffer, String name) {
		styleBuffer.append(' ');
		styleBuffer.append(name);
		styleBuffer.append(':');
	}

	/**
	 * Add property value to the Style styleBuffer.
	 *
	 * @param styleBuffer - specifies the StringBuffer to which the result should be
	 *                    output
	 * @param value       - specifies the values of the property
	 */
	private static void addPropValue(StringBuffer styleBuffer, String value) {
		if (value != null) {
			styleBuffer.append(' ');
			styleBuffer.append(value);
		}
	}

	/**
	 * Add URL property name to the Style styleBuffer.
	 *
	 * @param styleBuffer - specifies the StringBuffer to which the result should be
	 *                    output
	 * @param url         - specifies the values of the property
	 */
	private static void addURLValue(StringBuffer styleBuffer, String url) {
		if (url == null) {
			return;
		}

		// we needn't escape the URL as the URL has been encoded with UTF-8.
		/*
		 * // escape the URL string StringBuffer escapedUrl = null; for ( int i = 0, max
		 * = url.length( ), delta = 0; i < max; i++ ) { char c = url.charAt( i ); String
		 * replacement = null; if ( c == '\\' ) { replacement = "%5c"; //$NON-NLS-1$ }
		 * else if ( c == '#' ) { replacement = "%23"; //$NON-NLS-1$ } else if ( c ==
		 * '%' ) { replacement = "%25"; //$NON-NLS-1$ } else if ( c == '\'' ) {
		 * replacement = "%27"; //$NON-NLS-1$ } else if ( c >= 0x80 ) { replacement =
		 * '%' + Integer.toHexString( c ); }
		 *
		 * if ( replacement != null ) { if ( escapedUrl == null ) { escapedUrl = new
		 * StringBuffer( url ); } escapedUrl.replace( i + delta, i + delta + 1,
		 * replacement ); delta += ( replacement.length( ) - 1 ); } }
		 *
		 * if ( escapedUrl != null ) { url = escapedUrl.toString( ); }
		 */
		if (url.length() > 0) {
			styleBuffer.append(" url('"); //$NON-NLS-1$
			styleBuffer.append(url);
			styleBuffer.append("')"); //$NON-NLS-1$
		}
	}

	/**
	 * Builds the direction style.
	 *
	 * @param styleBuffer The <code>StringBuffer</code> to which the result is
	 *                    output.
	 * @param style       The style object.
	 *
	 * @author bidi_hcg
	 */
	public static void buildBidiDirection(StringBuffer styleBuffer, IStyle style) {
		if (style != null) {
			String direction = style.getDirection();
			if (direction != null) {
				buildProperty(styleBuffer, IStyle.CSS_DIRECTION_PROPERTY, direction);
			}
		}
	}

}
