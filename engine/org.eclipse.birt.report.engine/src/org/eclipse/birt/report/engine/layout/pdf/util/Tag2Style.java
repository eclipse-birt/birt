/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.ListValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * Converting HTML tag to style
 *
 * @since 3.3
 *
 */
public abstract class Tag2Style implements HTMLConstants {

	abstract void process(Element ele, StyleProperties sp);

	private static Map<String, Tag2Style> tag2Style = new HashMap<>();

	protected void setProperty(IStyle style, int index, CSSValue value) {
		CSSValue v = style.getProperty(index);
		if (v == null) {
			style.setProperty(index, value);
		}
	}

	protected void setHStyle(IStyle style, float fontSize, float margin) {
		setMarginTopAndBottom(style, margin);
		setProperty(style, StyleConstants.STYLE_FONT_SIZE, createEmValue(fontSize));
		setProperty(style, StyleConstants.STYLE_FONT_WEIGHT, CSSValueConstants.BOLD_VALUE);
		setProperty(style, StyleConstants.STYLE_PAGE_BREAK_AFTER, CSSValueConstants.AVOID_VALUE);
	}

	protected void setMarginTopAndBottom(IStyle style, float margin) {
		setProperty(style, StyleConstants.STYLE_MARGIN_TOP, createEmValue(margin));
		setProperty(style, StyleConstants.STYLE_MARGIN_BOTTOM, createEmValue(margin));
	}

	protected void setFontFamily(IStyle style, Value font) {
		ListValue fonts = new ListValue();
		fonts.append(font);
		setProperty(style, StyleConstants.STYLE_FONT_FAMILY, fonts);
	}

	protected void setFontStyle(IStyle style, Value fontStyle) {
		setProperty(style, StyleConstants.STYLE_FONT_STYLE, fontStyle);
	}

	protected void setFontWeight(IStyle style, Value fontWeight) {
		setProperty(style, StyleConstants.STYLE_FONT_WEIGHT, fontWeight);
	}

	protected void setInlineDisplay(IStyle style) {
		setProperty(style, StyleConstants.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE);
	}

	protected void setBlockDisplay(IStyle style) {
		setProperty(style, StyleConstants.STYLE_DISPLAY, CSSValueConstants.BLOCK_VALUE);
	}

	protected void processProperites(String[] properties, Element ele, StyleProperties sp) {
		PropertiesProcessor.process(properties, ele, sp);
	}

	static {
		tag2Style.put(TAG_I, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setFontStyle(sp.getStyle(), CSSValueConstants.ITALIC_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_FONT, new Tag2Style() {

			String[] properties = { PROPERTY_COLOR, PROPERTY_FACE, PROPERTY_SIZE };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setInlineDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_B, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setFontWeight(sp.getStyle(), CSSValueConstants.BOLD_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_A, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_CODE, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setFontFamily(sp.getStyle(), CSSValueConstants.MONOSPACE_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});
		tag2Style.put(TAG_EM, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setFontStyle(sp.getStyle(), CSSValueConstants.ITALIC_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_OBJECT, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN, PROPERTY_WIDTH, PROPERTY_HEIGHT, PROPERTY_BORDER };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setInlineDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_IMG, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN, PROPERTY_WIDTH, PROPERTY_HEIGHT, PROPERTY_BORDER, PROPERTY_ALT,
					PROPERTY_SRC };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setInlineDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_INS, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_TEXT_UNDERLINE, CSSValueConstants.UNDERLINE_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_SPAN, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setInlineDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_STRONG, // $NON-NLS-1$
				new Tag2Style() {

					@Override
					public void process(Element ele, StyleProperties sp) {
						setFontWeight(sp.getStyle(), CSSValueConstants.BOLD_VALUE);
						setInlineDisplay(sp.getStyle());
					}
				});

		tag2Style.put(TAG_SUB, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_VERTICAL_ALIGN, CSSValueConstants.BOTTOM_VALUE);
				setProperty(sp.getStyle(), StyleConstants.STYLE_FONT_SIZE, createPercentageValue(75));
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_SUP, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_VERTICAL_ALIGN, CSSValueConstants.TOP_VALUE);
				setProperty(sp.getStyle(), StyleConstants.STYLE_FONT_SIZE, createPercentageValue(75));
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_TT, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setFontFamily(sp.getStyle(), CSSValueConstants.MONOSPACE_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_U, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_TEXT_UNDERLINE, CSSValueConstants.UNDERLINE_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});
		tag2Style.put(TAG_DEL, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_TEXT_LINETHROUGH, CSSValueConstants.LINE_THROUGH_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_STRIKE, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_TEXT_LINETHROUGH, CSSValueConstants.LINE_THROUGH_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});
		tag2Style.put(TAG_S, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_TEXT_LINETHROUGH, CSSValueConstants.LINE_THROUGH_VALUE);
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_BIG, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_FONT_SIZE, createPercentageValue(200));
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_SMALL, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_FONT_SIZE, createPercentageValue(50));
				setInlineDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_DD, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_DIV, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_DL, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setMarginTopAndBottom(sp.getStyle(), 1.0f);
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_DT, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_H1, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setHStyle(sp.getStyle(), 2, 0.67f);
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});
		tag2Style.put(TAG_H2, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setHStyle(sp.getStyle(), 1.5f, 0.75f);
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});
		tag2Style.put(TAG_H3, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setHStyle(sp.getStyle(), 1.17f, 0.83f);
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});
		tag2Style.put(TAG_H4, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setHStyle(sp.getStyle(), 1.12f, 1.12f);
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});
		tag2Style.put(TAG_H5, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setHStyle(sp.getStyle(), 0.83f, 1.5f);
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});
		tag2Style.put(TAG_H6, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setHStyle(sp.getStyle(), 0.75f, 1.67f);
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_HR, new Tag2Style() {

			String[] properties = { PROPERTY_WIDTH };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_OL, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_P, new Tag2Style() {

			String[] properties = { PROPERTY_ALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setMarginTopAndBottom(sp.getStyle(), 1.33f);
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_PRE, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setFontFamily(sp.getStyle(), CSSValueConstants.MONOSPACE_VALUE);
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_UL, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_LI, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_ADDRESS, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setFontStyle(sp.getStyle(), CSSValueConstants.ITALIC_VALUE);
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_BODY, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_CENTER, new Tag2Style() {

			@Override
			public void process(Element ele, StyleProperties sp) {
				setProperty(sp.getStyle(), StyleConstants.STYLE_TEXT_ALIGN, CSSValueConstants.CENTER_VALUE);
				setBlockDisplay(sp.getStyle());
			}
		});

		tag2Style.put(TAG_TABLE,

				new Tag2Style() {

					String[] properties = { PROPERTY_WIDTH, PROPERTY_BGCOLOR, PROPERTY_CELLPADDING, PROPERTY_BORDER };

					@Override
					public void process(Element ele, StyleProperties sp) {
						setBlockDisplay(sp.getStyle());
						PropertiesProcessor.process(properties, ele, sp);
					}
				});

		tag2Style.put(TAG_TD, new Tag2Style() {

			String[] properties = { PROPERTY_ROWSPAN, PROPERTY_COLSPAN, PROPERTY_BGCOLOR, PROPERTY_ALIGN,
					PROPERTY_VALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_COL, new Tag2Style() {

			String[] properties = { PROPERTY_WIDTH };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

		tag2Style.put(TAG_TR, new Tag2Style() {

			String[] properties = { PROPERTY_BGCOLOR, PROPERTY_ALIGN, PROPERTY_VALIGN };

			@Override
			public void process(Element ele, StyleProperties sp) {
				setBlockDisplay(sp.getStyle());
				PropertiesProcessor.process(properties, ele, sp);
			}
		});

	}

	/**
	 * Get the style
	 *
	 * @param tagName tag name
	 * @return the style
	 */
	public static Tag2Style getStyleProcess(String tagName) {
		return tag2Style.get(tagName);
	}

	/**
	 * Create an EM value
	 *
	 * @param value the EM value
	 * @return the EM value
	 */
	public static FloatValue createEmValue(float value) {
		return createFloatValue(CSSPrimitiveValue.CSS_EMS, value);
	}

	/**
	 * Create a percentage value
	 *
	 * @param value percentage value
	 * @return the percentage value
	 */
	public static FloatValue createPercentageValue(float value) {
		return createFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, value);
	}

	private static FloatValue createFloatValue(short unitType, float value) {
		return new FloatValue(unitType, value);
	}
}
