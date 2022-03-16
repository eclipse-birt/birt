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
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.Element;

public abstract class PropertiesProcessor implements HTMLConstants {

	/** the possible values for property SIZE of HTML element FONT */
	private static String[] FONT_SIZE = { "7.5pt", //$NON-NLS-1$
			"7.5pt", "7.5pt", //$NON-NLS-1$ //$NON-NLS-2$
			"7.5pt", "7.5pt", //$NON-NLS-1$//$NON-NLS-2$
			"7.5pt", "10pt", //$NON-NLS-1$ //$NON-NLS-2$
			"7.5pt", "7.5pt", //$NON-NLS-1$ //$NON-NLS-2$
			"10pt", "12pt", //$NON-NLS-1$ //$NON-NLS-2$
			"13.8pt", "18pt", //$NON-NLS-1$//$NON-NLS-2$
			"23pt", "36pt" }; //$NON-NLS-1$//$NON-NLS-2$

	public final static HashMap<String, String[]> tagPropertiesMap = new HashMap<>();

	abstract void process(String value, StyleProperties sp);

	private static Map<String, PropertiesProcessor> properties2Style = new HashMap<>();

	public static void process(String[] properties, Element ele, StyleProperties sp) {
		for (int i = 0; i < properties.length; i++) {
			PropertiesProcessor p2s = properties2Style.get(properties[i]);
			if (p2s != null) {
				String value = ele.getAttribute(properties[i]);
				if (value != null && value.length() > 0) {
					p2s.process(value, sp);
				}
			}
		}
	}

	public static void process(String property, String value, StyleProperties sp) {
		PropertiesProcessor p2s = properties2Style.get(property);
		if (p2s != null) {
			if (value != null && value.length() > 0) {
				p2s.process(value, sp);
			}
		}
	}

	protected boolean hasProperty(IStyle style, int index) {
		return style.getProperty(index) != null;
	}

	static {
		properties2Style.put("cellpadding", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						if (value != null && value.length() > 0) {
							if (value.endsWith("%")) {
								IStyle style = sp.getStyle();
								if (!hasProperty(style, IStyle.STYLE_PADDING_LEFT)) {
									style.setPaddingLeft(value);
								}
								if (!hasProperty(style, IStyle.STYLE_PADDING_RIGHT)) {
									style.setPaddingRight(value);
								}
								if (!hasProperty(style, IStyle.STYLE_PADDING_TOP)) {
									style.setPaddingTop(value);
								}
								if (!hasProperty(style, IStyle.STYLE_PADDING_BOTTOM)) {
									style.setPaddingBottom(value);
								}
							} else {
								try {
									int size = Integer.parseInt(value); // $NON-NLS-1$
									String padding = size + "px";
									IStyle style = sp.getStyle();
									if (!hasProperty(style, IStyle.STYLE_PADDING_LEFT)) {
										style.setPaddingLeft(padding);
									}
									if (!hasProperty(style, IStyle.STYLE_PADDING_RIGHT)) {
										style.setPaddingRight(padding);
									}
									if (!hasProperty(style, IStyle.STYLE_PADDING_TOP)) {
										style.setPaddingTop(padding);
									}
									if (!hasProperty(style, IStyle.STYLE_PADDING_BOTTOM)) {
										style.setPaddingBottom(padding);
									}
								} catch (Exception e) {

								}
							}
						}
					}
				});
		properties2Style.put("background", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						IStyle style = sp.getStyle();
						if (!hasProperty(style, IStyle.STYLE_BACKGROUND_COLOR)) {
							style.setBackgroundColor(value);
						}
					}
				});
		properties2Style.put("size", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						try {
							int size = Integer.parseInt(value); // $NON-NLS-1$
							size = Math.min(size, 7);
							IStyle style = sp.getStyle();
							if (!hasProperty(style, IStyle.STYLE_FONT_SIZE)) {
								style.setFontSize(FONT_SIZE[size + 7]);
							}
						} catch (Exception e) {

						}
					}
				});

		properties2Style.put("text", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						IStyle style = sp.getStyle();
						if (!hasProperty(style, IStyle.STYLE_COLOR)) {
							style.setColor(value);
						}
					}
				});

		properties2Style.put("color", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						IStyle style = sp.getStyle();
						if (!hasProperty(style, IStyle.STYLE_COLOR)) {
							style.setColor(value);
						}
					}
				});

		properties2Style.put("bgcolor", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						IStyle style = sp.getStyle();
						if (!hasProperty(style, IStyle.STYLE_BACKGROUND_COLOR)) {
							style.setBackgroundColor(value);
						}
					}
				});
		properties2Style.put("border", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						try {
							// FIXME
							int size = Integer.parseInt(value); // $NON-NLS-1$
							String width = size + "px";
							IStyle style = sp.getStyle();
							if (!hasProperty(style, IStyle.STYLE_BORDER_TOP_WIDTH)) {
								style.setBorderTopWidth(width);
							}
							if (!hasProperty(style, IStyle.STYLE_BORDER_BOTTOM_WIDTH)) {
								style.setBorderBottomWidth(width);
							}
							if (!hasProperty(style, IStyle.STYLE_BORDER_LEFT_WIDTH)) {
								style.setBorderLeftWidth(width);
							}
							if (!hasProperty(style, IStyle.STYLE_BORDER_RIGHT_WIDTH)) {
								style.setBorderRightWidth(width);
							}
							if (!hasProperty(style, IStyle.STYLE_BORDER_TOP_STYLE)) {
								style.setBorderTopStyle("solid");
							}
							if (!hasProperty(style, IStyle.STYLE_BORDER_BOTTOM_STYLE)) {
								style.setBorderBottomStyle("solid");
							}
							if (!hasProperty(style, IStyle.STYLE_BORDER_LEFT_STYLE)) {
								style.setBorderLeftStyle("solid");
							}
							if (!hasProperty(style, IStyle.STYLE_BORDER_RIGHT_STYLE)) {
								style.setBorderRightStyle("solid");
							}
						} catch (Exception e) {

						}
					}
				});
		properties2Style.put("face", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						IStyle style = sp.getStyle();
						if (!hasProperty(style, IStyle.STYLE_FONT_FAMILY)) {
							style.setFontFamily(value);
						}
					}
				});
		properties2Style.put("align", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						IStyle style = sp.getStyle();
						if (!hasProperty(style, IStyle.STYLE_TEXT_ALIGN)) {
							style.setTextAlign(value);
						}
					}
				});

		properties2Style.put("valign", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						IStyle style = sp.getStyle();
						if (!hasProperty(style, IStyle.STYLE_VERTICAL_ALIGN)) {
							style.setVerticalAlign(value);
						}
					}
				});

		properties2Style.put("width", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						if (value != null && value.length() > 0) {
							DimensionType d = DimensionType.parserUnit(value);
							if (d != null) {
								String unit = d.getUnits();
								if (unit == null || unit.length() == 0) {
									d = new DimensionType(d.getMeasure(), DimensionType.UNITS_PX);
								}
								sp.addProperty(StyleProperties.WIDTH, d);
							}
						}
					}
				});
		properties2Style.put("height", //$NON-NLS-1$
				new PropertiesProcessor() {

					@Override
					public void process(String value, StyleProperties sp) {
						if (value != null && value.length() > 0) {
							DimensionType d = DimensionType.parserUnit(value);
							if (d != null) {
								String unit = d.getUnits();
								if (unit == null || unit.length() == 0) {
									d = new DimensionType(d.getMeasure(), DimensionType.UNITS_PX);
								}
								sp.addProperty(StyleProperties.HEIGHT, d);
							}
						}
					}
				});

	}

}
