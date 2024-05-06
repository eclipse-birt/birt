/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import java.util.BitSet;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.AbstractStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * Birt style is a class to represents the style elements
 *
 * @since 3.3
 *
 */
public class BirtStyle {

	/**
	 * constant: number of styles
	 */
	public static final int NUMBER_OF_STYLES = StyleConstants.NUMBER_OF_STYLE + 1;
	/**
	 * constant: text rotation
	 */
	public static final int TEXT_ROTATION = StyleConstants.NUMBER_OF_STYLE;

	protected static final String cssProperties[] = { "margin-left", "margin-right", "margin-top", "DATA_FORMAT",
			"border-right-color", "direction", "border-top-width", "padding-left", "border-right-width",
			"padding-bottom", "padding-top", "NUMBER_ALIGN", "padding-right", "CAN_SHRINK", "border-top-color",
			"background-repeat", "margin-bottom", "background-width", "background-height", "border-right-style",
			"border-bottom-color", "text-indent", "line-height", "border-bottom-width", "text-align",
			"background-color", "color", "overflow", "TEXT_LINETHROUGH", "border-left-color", "widows",
			"border-left-width", "border-bottom-style", "font-weight", "font-variant", "text-transform", "white-space",
			"TEXT_OVERLINE", "vertical-align", "BACKGROUND_POSITION_X", "border-left-style", "VISIBLE_FORMAT",
			"MASTER_PAGE", "orphans", "font-size", "font-style", "border-top-style", "page-break-before",
			"SHOW_IF_BLANK", "background-image", "BACKGROUND_POSITION_Y", "word-spacing", "background-attachment",
			"TEXT_UNDERLINE", "display", "font-family", "letter-spacing", "page-break-inside", "page-break-after"
			, "Rotation", "border-diagonal-color", "border-diagonal-width", "border-diagonal-style",
			"border-antidiagonal-color", "border-antidiagonal-width", "border-antidiagonal-style" };

	private IStyle elemStyle;
	private CSSValue[] propertyOverride = new CSSValue[BirtStyle.NUMBER_OF_STYLES];
	private CSSEngine cssEngine;
	private boolean useTextIndent = true;
	private String textIndentMode = "";

	/**
	 * Constructor 01
	 *
	 * @param cssEngine css engine
	 */
	public BirtStyle(CSSEngine cssEngine) {
		this.cssEngine = cssEngine;
	}

	/**
	 * Constructor 02
	 *
	 * @param element
	 */
	public BirtStyle(IContent element) {
		elemStyle = element.getComputedStyle();

		if (elemStyle instanceof AbstractStyle) {
			cssEngine = ((AbstractStyle) elemStyle).getCSSEngine();
		} else {
			throw new IllegalStateException("Unable to obtain CSSEngine from elemStyle: " + elemStyle);
		}

		Float rotation = extractRotation(element);
		if (rotation != null) {
			setFloat(TEXT_ROTATION, CSSPrimitiveValue.CSS_DEG, rotation);
		}

		// Cache the element properties to avoid calculation cost many time
		for (int i = 0; i < StyleManager.COMPARE_CSS_PROPERTIES.length; ++i) {
			int prop = StyleManager.COMPARE_CSS_PROPERTIES[i];
			propertyOverride[prop] = elemStyle.getProperty(prop);
		}
		propertyOverride[StyleConstants.STYLE_DATA_FORMAT] = elemStyle.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		for (int i = 0; i < FontManager.COMPARE_CSS_PROPERTIES.length; ++i) {
			int prop = FontManager.COMPARE_CSS_PROPERTIES[i];
			propertyOverride[prop] = elemStyle.getProperty(prop);
		}

	}

	private static Float extractRotation(IContent element) {
		Object generatorObject = element.getGenerateBy();
		if (generatorObject instanceof ReportElementDesign) {
			ReportElementDesign generatorDesign = (ReportElementDesign) generatorObject;
			Map<String, Expression> userProps = generatorDesign.getUserProperties();
			if (userProps != null) {
				Expression rotationExpression = userProps.get(ExcelEmitter.ROTATION_PROP);
				if (rotationExpression != null) {
					try {
						return Float.valueOf(rotationExpression.getScriptText());
					} catch (Exception ex) {
					}
				}
			}
		}
		return null;
	}

	/**
	 * Set CSS property value
	 *
	 * @param propIndex property index
	 * @param newValue  new css value
	 */
	public void setProperty(int propIndex, CSSValue newValue) {
		if (propertyOverride == null) {
			propertyOverride = new CSSValue[BirtStyle.NUMBER_OF_STYLES];
		}
		propertyOverride[propIndex] = newValue;
	}

	/**
	 * Get property value
	 *
	 * @param propIndex property index
	 * @return Return the property value
	 */
	public CSSValue getProperty(int propIndex) {
		return propertyOverride[propIndex];
	}

	/**
	 * Set a float value of a property
	 *
	 * @param propIndex property index
	 * @param units     property unit
	 * @param newValue  new value
	 */
	public void setFloat(int propIndex, short units, float newValue) {
		if (propertyOverride == null) {
			propertyOverride = new CSSValue[BirtStyle.NUMBER_OF_STYLES];
		}
		propertyOverride[propIndex] = new FloatValue(units, newValue);
	}

	/**
	 * Set a string value of a property
	 *
	 * @param propIndex property index
	 * @param newValue  new value
	 */
	public void parseString(int propIndex, String newValue) {
		if (propertyOverride == null) {
			propertyOverride = new CSSValue[BirtStyle.NUMBER_OF_STYLES];
		}

		if (propIndex < StyleConstants.NUMBER_OF_STYLE) {
			propertyOverride[propIndex] = cssEngine.parsePropertyValue(propIndex, newValue);
		} else {
			propertyOverride[propIndex] = new StringValue(CSSPrimitiveValue.CSS_STRING, newValue);
		}
	}

	/**
	 * Get the property value like a string
	 *
	 * @param propIndex property index
	 * @return Return the property value like a string
	 */
	public String getString(int propIndex) {
		CSSValue value = getProperty(propIndex);
		if (value != null) {
			return value.getCssText();
		}
		return null;
	}

	@Override
	protected BirtStyle clone() {
		BirtStyle result = new BirtStyle(this.cssEngine);

		result.propertyOverride = new CSSValue[BirtStyle.NUMBER_OF_STYLES];

		for (int i = 0; i < NUMBER_OF_STYLES; ++i) {
			CSSValue value = getProperty(i);
			if (value != null) {
				if (value instanceof DataFormatValue) {
					value = StyleManagerUtils.cloneDataFormatValue((DataFormatValue) value);
				}

				result.propertyOverride[i] = value;
			}
		}

		return result;
	}

	private static final BitSet SPECIAL_OVERLAY_PROPERTIES = PrepareSpecialOverlayProperties();

	private static BitSet PrepareSpecialOverlayProperties() {
		BitSet result = new BitSet(BirtStyle.NUMBER_OF_STYLES);
		result.set(StyleConstants.STYLE_MARGIN_LEFT);
		result.set(StyleConstants.STYLE_MARGIN_RIGHT);
		result.set(StyleConstants.STYLE_PADDING_LEFT);
		result.set(StyleConstants.STYLE_PADDING_RIGHT);
		result.set(StyleConstants.STYLE_BACKGROUND_COLOR);
		result.set(StyleConstants.STYLE_BORDER_BOTTOM_STYLE);
		result.set(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH);
		result.set(StyleConstants.STYLE_BORDER_BOTTOM_COLOR);
		result.set(StyleConstants.STYLE_BORDER_LEFT_STYLE);
		result.set(StyleConstants.STYLE_BORDER_LEFT_WIDTH);
		result.set(StyleConstants.STYLE_BORDER_LEFT_COLOR);
		result.set(StyleConstants.STYLE_BORDER_RIGHT_STYLE);
		result.set(StyleConstants.STYLE_BORDER_RIGHT_WIDTH);
		result.set(StyleConstants.STYLE_BORDER_RIGHT_COLOR);
		result.set(StyleConstants.STYLE_BORDER_TOP_STYLE);
		result.set(StyleConstants.STYLE_BORDER_TOP_WIDTH);
		result.set(StyleConstants.STYLE_BORDER_TOP_COLOR);
		result.set(StyleConstants.STYLE_BORDER_DIAGONAL_STYLE);
		result.set(StyleConstants.STYLE_BORDER_DIAGONAL_WIDTH);
		result.set(StyleConstants.STYLE_BORDER_DIAGONAL_COLOR);
		result.set(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_STYLE);
		result.set(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_WIDTH);
		result.set(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_COLOR);
		result.set(StyleConstants.STYLE_VERTICAL_ALIGN);
		result.set(StyleConstants.STYLE_DATA_FORMAT);
		return result;
	}

	private void overlayBorder(IStyle style, int propStyle, int propWidth, int propColour) {
		CSSValue ovlStyle = style.getProperty(propStyle);
		CSSValue ovlWidth = style.getProperty(propWidth);
		CSSValue ovlColour = style.getProperty(propColour);
		if ((ovlStyle != null) && (ovlWidth != null) && (ovlColour != null)
				&& (!CSSConstants.CSS_NONE_VALUE.equals(ovlStyle.getCssText()))) {
			setProperty(propStyle, ovlStyle);
			setProperty(propWidth, ovlWidth);
			setProperty(propColour, ovlColour);
		}
	}

	/**
	 * Set the overlay of the element
	 *
	 * @param element The element for overlay setting
	 */
	public void overlay(IContent element) {

		// System.out.println( "overlay: Before - " + this.toString() );

		IStyle style = element.getComputedStyle();
		for (int propIndex = 0; propIndex < StyleConstants.NUMBER_OF_STYLE; ++propIndex) {
			if (!SPECIAL_OVERLAY_PROPERTIES.get(propIndex)) {
				CSSValue overlayValue = style.getProperty(propIndex);
				CSSValue localValue = getProperty(propIndex);
				if ((overlayValue != null) && !overlayValue.equals(localValue)) {
					setProperty(propIndex, overlayValue);
				}
			}
		}

		// Background color, only overlay if not null and not transparent
		CSSValue overlayBgColour = style.getProperty(StyleConstants.STYLE_BACKGROUND_COLOR);
		CSSValue localBgColour = getProperty(StyleConstants.STYLE_BACKGROUND_COLOR);
		if ((overlayBgColour != null) && (!CSSConstants.CSS_TRANSPARENT_VALUE.equals(overlayBgColour.getCssText()))
				&& (!overlayBgColour.equals(localBgColour))) {
			setProperty(StyleConstants.STYLE_BACKGROUND_COLOR, overlayBgColour);
		}

		// Borders, only overlay if all three components are not null - and then overlay
		// all three
		overlayBorder(style, StyleConstants.STYLE_BORDER_BOTTOM_STYLE, StyleConstants.STYLE_BORDER_BOTTOM_WIDTH,
				StyleConstants.STYLE_BORDER_BOTTOM_COLOR);
		overlayBorder(style, StyleConstants.STYLE_BORDER_LEFT_STYLE, StyleConstants.STYLE_BORDER_LEFT_WIDTH,
				StyleConstants.STYLE_BORDER_LEFT_COLOR);
		overlayBorder(style, StyleConstants.STYLE_BORDER_RIGHT_STYLE, StyleConstants.STYLE_BORDER_RIGHT_WIDTH,
				StyleConstants.STYLE_BORDER_RIGHT_COLOR);
		overlayBorder(style, StyleConstants.STYLE_BORDER_TOP_STYLE, StyleConstants.STYLE_BORDER_TOP_WIDTH,
				StyleConstants.STYLE_BORDER_TOP_COLOR);

		// Vertical align, not computed safely, so only check immediate style
		CSSValue verticalAlign = element.getStyle().getProperty(StyleConstants.STYLE_VERTICAL_ALIGN);
		if (verticalAlign != null) {
			CSSValue localValue = getProperty(StyleConstants.STYLE_VERTICAL_ALIGN);
			if (!verticalAlign.equals(localValue)) {
				setProperty(StyleConstants.STYLE_VERTICAL_ALIGN, verticalAlign);
			}
		}

		// Data format
		CSSValue overlayDataFormat = style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		CSSValue localDataFormat = getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (!StyleManagerUtils.dataFormatsEquivalent((DataFormatValue) overlayDataFormat,
				(DataFormatValue) localDataFormat)) {
			setProperty(StyleConstants.STYLE_DATA_FORMAT,
					StyleManagerUtils.cloneDataFormatValue((DataFormatValue) overlayDataFormat));
		}

		// Rotation
		Float rotation = extractRotation(element);
		if (rotation != null) {
			setFloat(TEXT_ROTATION, CSSPrimitiveValue.CSS_DEG, rotation);
		}

		// System.out.println( "overlay: After - " + this.toString() );
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < NUMBER_OF_STYLES; ++i) {
			CSSValue val = getProperty(i);
			if (val != null) {
				try {
					result.append(cssProperties[i]).append(':').append(val.getCssText()).append("; ");
				} catch (Exception ex) {
					result.append(cssProperties[i]).append(":{").append(ex.getMessage()).append("}; ");
				}
			}
		}
		return result.toString();
	}

	/**
	 * Set the flag if the text indent is in use
	 *
	 * @param useTextIndent flag to define is the text indent is in use
	 */
	public void setTextIndentInUse(boolean useTextIndent) {
		this.useTextIndent = useTextIndent;
	}

	/**
	 * Get the result if the text indent is in use
	 *
	 * @return Return true if the text indent is in use
	 */
	public boolean isTextIndentInUse() {
		return this.useTextIndent;
	}

	/**
	 * Set the text indent mode
	 *
	 * @param textIndentMode type of the text indent mode
	 */
	public void setTextIndentMode(String textIndentMode) {
		this.textIndentMode = textIndentMode;
	}

	/**
	 * Get the text indent mode
	 *
	 * @return Return the text indent mode
	 */
	public String getTextIndentMode() {
		return this.textIndentMode;
	}
}
