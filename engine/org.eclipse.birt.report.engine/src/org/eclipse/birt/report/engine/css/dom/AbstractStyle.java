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

package org.eclipse.birt.report.engine.css.dom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSValue;

/**
 * Definition of the abstract style class
 *
 * @since 3.3
 *
 */
abstract public class AbstractStyle implements IStyle {

	protected CSSEngine engine;

	/**
	 * Consructor
	 *
	 * @param engine
	 */
	public AbstractStyle(CSSEngine engine) {
		this.engine = engine;
	}

	/**
	 * Get the CSS engine
	 *
	 * @return Return the CSS engine
	 */
	public CSSEngine getCSSEngine() {
		return this.engine;
	}

	@Override
	public void setProperties(IStyle style) {
		for (int i = 0; i < NUMBER_OF_STYLE; i++) {
			CSSValue v = style.getProperty(i);
			if (v != null) {
				setProperty(i, v);
			}
		}
	}

	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < NUMBER_OF_STYLE; i++) {
			// we don't return the format in css as the format
			// is a complex object which can't be represented as
			// css string.
			if (i == StyleConstants.STYLE_DATA_FORMAT) {
				continue;
			}
			CSSValue value = getProperty(i);
			if (value != null) {
				sb.append(engine.getPropertyName(i));
				sb.append(": ");
				short type = value.getCssValueType();
				switch (type) {
				case CSSValue.CSS_PRIMITIVE_VALUE: {
					CSSPrimitiveValue pv = (CSSPrimitiveValue) value;
					short unitType = pv.getPrimitiveType();
					switch (unitType) {
					case CSSPrimitiveValue.CSS_STRING:
						sb.append("'");
						sb.append(pv.getStringValue());
						sb.append("'");
						break;
					case CSSPrimitiveValue.CSS_URI:
						sb.append("url('");
						sb.append(pv.getStringValue());
						sb.append("')");
						break;
					default:
						sb.append(value.getCssText());
					}
				}
					break;
				default:
					sb.append(value.getCssText());
				}
				sb.append("; ");
			}
		}
		if (sb.length() > 2) {
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}

	protected String getCssText(CSSValue value) {
		if (value == null) {
			return null;
		}
		return value.getCssText();
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		IStyle style = (IStyle) engine.parseStyleDeclaration(cssText);
		for (int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++) {
			CSSValue value = style.getProperty(i);
			if (value != null) {
				setProperty(i, value);
			}
		}
	}

	protected int getPropertyIndex(String propertyName) {
		return engine.getPropertyIndex(propertyName);
	}

	@Override
	public String getPropertyValue(String propertyName) {
		int index = getPropertyIndex(propertyName);
		if (index != -1) {
			return getCssText(index);
		}
		return null;
	}

	@Override
	public CSSValue getPropertyCSSValue(String propertyName) {
		int index = getPropertyIndex(propertyName);
		if (index != -1) {
			return getProperty(index);
		}
		return null;
	}

	@Override
	public String removeProperty(String propertyName) throws DOMException {
		int index = getPropertyIndex(propertyName);
		if (index != -1) {
			setProperty(index, null);
		}
		return null;
	}

	@Override
	public String getPropertyPriority(String propertyName) {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "getPropertyPriority");
	}

	@Override
	public void setProperty(String propertyName, String value, String priority) throws DOMException {
		int index = getPropertyIndex(propertyName);
		if (index != -1) {
			setCssText(index, value);
		}
	}

	@Override
	public int getLength() {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "getLength");
	}

	@Override
	public String item(int index) {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "item");
	}

	@Override
	public CSSRule getParentRule() {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "getParentRule");
	}

	@Override
	public String getCssText(int index) {
		CSSValue value = getProperty(index);
		if (value != null) {
			return value.getCssText();
		}
		return null;
	}

	@Override
	public void setCssText(int index, String cssText) {
		if (cssText != null) {
			CSSValue value = engine.parsePropertyValue(index, cssText);
			setProperty(index, value);
		} else {
			setProperty(index, null);
		}
	}

	@Override
	public String getFontFamily() {
		return getCssText(STYLE_FONT_FAMILY);
	}

	@Override
	public String getFontStyle() {
		return getCssText(STYLE_FONT_STYLE);
	}

	@Override
	public String getFontVariant() {
		return getCssText(STYLE_FONT_VARIANT);
	}

	@Override
	public String getFontWeight() {
		return getCssText(STYLE_FONT_WEIGHT);
	}

	@Override
	public String getFontSize() {
		return getCssText(STYLE_FONT_SIZE);
	}

	@Override
	public String getColor() {
		return getCssText(STYLE_COLOR);
	}

	@Override
	public String getBackgroundColor() {
		return getCssText(STYLE_BACKGROUND_COLOR);
	}

	@Override
	public String getBackgroundImage() {
		return getCssText(STYLE_BACKGROUND_IMAGE);
	}

	@Override
	public String getBackgroundImageType() {
		return getCssText(STYLE_BACKGROUND_IMAGE_TYPE);
	}

	@Override
	public String getBackgroundHeight() {
		return getCssText(STYLE_BACKGROUND_HEIGHT);
	}

	@Override
	public String getBackgroundWidth() {
		return getCssText(STYLE_BACKGROUND_WIDTH);
	}

	@Override
	public String getBackgroundRepeat() {
		return getCssText(STYLE_BACKGROUND_REPEAT);
	}

	@Override
	public String getBackgroundAttachment() {
		return getCssText(STYLE_BACKGROUND_ATTACHMENT);
	}

	@Override
	public String getBackgroundPositionX() {
		return getCssText(STYLE_BACKGROUND_POSITION_X);
	}

	@Override
	public String getBackgroundPositionY() {
		return getCssText(STYLE_BACKGROUND_POSITION_Y);
	}

	@Override
	public String getWordSpacing() {
		return getCssText(STYLE_WORD_SPACING);
	}

	@Override
	public String getLetterSpacing() {
		return getCssText(STYLE_LETTER_SPACING);
	}

	@Override
	public String getTextUnderline() {
		return getCssText(STYLE_TEXT_UNDERLINE);
	}

	@Override
	public String getTextOverline() {
		return getCssText(STYLE_TEXT_OVERLINE);
	}

	@Override
	public String getTextLineThrough() {
		return getCssText(STYLE_TEXT_LINETHROUGH);
	}

	@Override
	public String getVerticalAlign() {
		return getCssText(STYLE_VERTICAL_ALIGN);
	}

	@Override
	public String getTextTransform() {
		return getCssText(STYLE_TEXT_TRANSFORM);
	}

	@Override
	public String getTextAlign() {
		return getCssText(STYLE_TEXT_ALIGN);
	}

	@Override
	public String getTextIndent() {
		return getCssText(STYLE_TEXT_INDENT);
	}

	@Override
	public String getLineHeight() {
		return getCssText(STYLE_LINE_HEIGHT);
	}

	@Override
	public String getWhiteSpace() {
		return getCssText(STYLE_WHITE_SPACE);
	}

	@Override
	public String getMarginTop() {
		return getCssText(STYLE_MARGIN_TOP);
	}

	@Override
	public String getMarginBottom() {
		return getCssText(STYLE_MARGIN_BOTTOM);
	}

	@Override
	public String getMarginLeft() {
		return getCssText(STYLE_MARGIN_LEFT);
	}

	@Override
	public String getMarginRight() {
		return getCssText(STYLE_MARGIN_RIGHT);
	}

	@Override
	public String getPaddingTop() {
		return getCssText(STYLE_PADDING_TOP);
	}

	@Override
	public String getPaddingBottom() {
		return getCssText(STYLE_PADDING_BOTTOM);
	}

	@Override
	public String getPaddingLeft() {
		return getCssText(STYLE_PADDING_LEFT);
	}

	@Override
	public String getPaddingRight() {
		return getCssText(STYLE_PADDING_RIGHT);
	}

	@Override
	public String getBorderTopWidth() {
		return getCssText(STYLE_BORDER_TOP_WIDTH);
	}

	@Override
	public String getBorderBottomWidth() {
		return getCssText(STYLE_BORDER_BOTTOM_WIDTH);
	}

	@Override
	public String getBorderLeftWidth() {
		return getCssText(STYLE_BORDER_LEFT_WIDTH);
	}

	@Override
	public String getBorderRightWidth() {
		return getCssText(STYLE_BORDER_RIGHT_WIDTH);
	}

	@Override
	public String getBorderTopColor() {
		return getCssText(STYLE_BORDER_TOP_COLOR);
	}

	@Override
	public String getBorderBottomColor() {
		return getCssText(STYLE_BORDER_BOTTOM_COLOR);
	}

	@Override
	public String getBorderLeftColor() {
		return getCssText(STYLE_BORDER_LEFT_COLOR);
	}

	@Override
	public String getBorderRightColor() {
		return getCssText(STYLE_BORDER_RIGHT_COLOR);
	}

	@Override
	public String getBorderTopStyle() {
		return getCssText(STYLE_BORDER_TOP_STYLE);
	}

	@Override
	public String getBorderBottomStyle() {
		return getCssText(STYLE_BORDER_BOTTOM_STYLE);
	}

	@Override
	public String getBorderLeftStyle() {
		return getCssText(STYLE_BORDER_LEFT_STYLE);
	}

	@Override
	public String getBorderRightStyle() {
		return getCssText(STYLE_BORDER_RIGHT_STYLE);
	}

	@Override
	public String getDisplay() {
		return getCssText(STYLE_DISPLAY);
	}

	@Override
	public String getOrphans() {
		return getCssText(STYLE_ORPHANS);
	}

	@Override
	public String getWidows() {
		return getCssText(STYLE_WIDOWS);
	}

	@Override
	public String getPageBreakAfter() {
		return getCssText(STYLE_PAGE_BREAK_AFTER);
	}

	@Override
	public String getPageBreakBefore() {
		return getCssText(STYLE_PAGE_BREAK_BEFORE);
	}

	@Override
	public String getPageBreakInside() {
		return getCssText(STYLE_PAGE_BREAK_INSIDE);
	}

	@Override
	public String getMasterPage() {
		return getCssText(STYLE_MASTER_PAGE);
	}

	@Override
	public String getShowIfBlank() {
		return getCssText(STYLE_SHOW_IF_BLANK);
	}

	@Override
	public String getCanShrink() {
		return getCssText(STYLE_CAN_SHRINK);
	}

	@Override
	public String getVisibleFormat() {
		return getCssText(STYLE_VISIBLE_FORMAT);
	}

	/**
	 * @param backgroundAttachment The backgroundAttachment to set.
	 */
	@Override
	public void setBackgroundAttachment(String backgroundAttachment) {
		setCssText(STYLE_BACKGROUND_ATTACHMENT, backgroundAttachment);
	}

	/**
	 * @param backgroundColor The backgroundColor to set.
	 */
	@Override
	public void setBackgroundColor(String backgroundColor) {
		setCssText(STYLE_BACKGROUND_COLOR, backgroundColor);
	}

	/**
	 * @param backgroundImage The backgroundImage to set.
	 */
	@Override
	public void setBackgroundImage(String backgroundImage) {
		setCssText(STYLE_BACKGROUND_IMAGE, backgroundImage);
	}

	/**
	 * @param backgroundImageSourceType The backgroundImage to set.
	 */
	@Override
	public void setBackgroundImageType(String backgroundImageSourceType) {
		setCssText(STYLE_BACKGROUND_IMAGE_TYPE, backgroundImageSourceType);
	}

	/**
	 * @param backgroundPositionX The backgroundPositionX to set.
	 */
	@Override
	public void setBackgroundPositionX(String backgroundPositionX) {
		setCssText(STYLE_BACKGROUND_POSITION_X, backgroundPositionX);
	}

	/**
	 * @param backgroundPositionY The backgroundPositionY to set.
	 */
	@Override
	public void setBackgroundPositionY(String backgroundPositionY) {
		setCssText(STYLE_BACKGROUND_POSITION_Y, backgroundPositionY);
	}

	/**
	 * @param backgroundRepeat The backgroundRepeat to set.
	 */
	@Override
	public void setBackgroundRepeat(String backgroundRepeat) {
		setCssText(STYLE_BACKGROUND_REPEAT, backgroundRepeat);
	}

	/**
	 * @param borderBottomColor The borderBottomColor to set.
	 */
	@Override
	public void setBorderBottomColor(String borderBottomColor) {
		setCssText(STYLE_BORDER_BOTTOM_COLOR, borderBottomColor);
	}

	/**
	 * @param borderBottomStyle The borderBottomStyle to set.
	 */
	@Override
	public void setBorderBottomStyle(String borderBottomStyle) {
		setCssText(STYLE_BORDER_BOTTOM_STYLE, borderBottomStyle);
	}

	/**
	 * @param borderBottomWidth The borderBottomWidth to set.
	 */
	@Override
	public void setBorderBottomWidth(String borderBottomWidth) {
		setCssText(STYLE_BORDER_BOTTOM_WIDTH, borderBottomWidth);
	}

	/**
	 * @param borderLeftColor The borderLeftColor to set.
	 */
	@Override
	public void setBorderLeftColor(String borderLeftColor) {
		setCssText(STYLE_BORDER_LEFT_COLOR, borderLeftColor);
	}

	/**
	 * @param borderLeftStyle The borderLeftStyle to set.
	 */
	@Override
	public void setBorderLeftStyle(String borderLeftStyle) {
		setCssText(STYLE_BORDER_LEFT_STYLE, borderLeftStyle);
	}

	/**
	 * @param borderLeftWidth The borderLeftWidth to set.
	 */
	@Override
	public void setBorderLeftWidth(String borderLeftWidth) {
		setCssText(STYLE_BORDER_LEFT_WIDTH, borderLeftWidth);
	}

	/**
	 * @param borderRightColor The borderRightColor to set.
	 */
	@Override
	public void setBorderRightColor(String borderRightColor) {
		setCssText(STYLE_BORDER_RIGHT_COLOR, borderRightColor);
	}

	/**
	 * @param borderRightStyle The borderRightStyle to set.
	 */
	@Override
	public void setBorderRightStyle(String borderRightStyle) {
		setCssText(STYLE_BORDER_RIGHT_STYLE, borderRightStyle);
	}

	/**
	 * @param borderRightWidth The borderRightWidth to set.
	 */
	@Override
	public void setBorderRightWidth(String borderRightWidth) {
		setCssText(STYLE_BORDER_RIGHT_WIDTH, borderRightWidth);
	}

	/**
	 * @param borderTopColor The borderTopColor to set.
	 */
	@Override
	public void setBorderTopColor(String borderTopColor) {
		setCssText(STYLE_BORDER_TOP_COLOR, borderTopColor);
	}

	/**
	 * @param borderTopStyle The borderTopStyle to set.
	 */
	@Override
	public void setBorderTopStyle(String borderTopStyle) {
		setCssText(STYLE_BORDER_TOP_STYLE, borderTopStyle);
	}

	/**
	 * @param borderTopWidth The borderTopWidth to set.
	 */
	@Override
	public void setBorderTopWidth(String borderTopWidth) {
		setCssText(STYLE_BORDER_TOP_WIDTH, borderTopWidth);
	}

	/**
	 * @param canShrink The canShrink to set.
	 */
	@Override
	public void setCanShrink(String canShrink) {
		setCssText(STYLE_CAN_SHRINK, canShrink);
	}

	/**
	 * @param color The color to set.
	 */
	@Override
	public void setColor(String color) {
		setCssText(STYLE_COLOR, color);
	}

	/**
	 * @param display The display to set.
	 */
	@Override
	public void setDisplay(String display) {
		setCssText(STYLE_DISPLAY, display);
	}

	/**
	 * @param fontFamily The fontFamily to set.
	 */
	@Override
	public void setFontFamily(String fontFamily) {
		setCssText(STYLE_FONT_FAMILY, fontFamily);
	}

	/**
	 * @param fontSize The fontSize to set.
	 */
	@Override
	public void setFontSize(String fontSize) {
		setCssText(STYLE_FONT_SIZE, fontSize);
	}

	/**
	 * @param fontStyle The fontStyle to set.
	 */
	@Override
	public void setFontStyle(String fontStyle) {
		setCssText(STYLE_FONT_STYLE, fontStyle);
	}

	/**
	 * @param fontVariant The fontVariant to set.
	 */
	@Override
	public void setFontVariant(String fontVariant) {
		setCssText(STYLE_FONT_VARIANT, fontVariant);
	}

	/**
	 * @param fontWeight The fontWeight to set.
	 */
	@Override
	public void setFontWeight(String fontWeight) {
		setCssText(STYLE_FONT_WEIGHT, fontWeight);
	}

	/**
	 * @param letterSpacing The letterSpacing to set.
	 */
	@Override
	public void setLetterSpacing(String letterSpacing) {
		setCssText(STYLE_LETTER_SPACING, letterSpacing);
	}

	/**
	 * @param lineHeight The lineHeight to set.
	 */
	@Override
	public void setLineHeight(String lineHeight) {
		setCssText(STYLE_LINE_HEIGHT, lineHeight);
	}

	/**
	 * @param marginBottom The marginBottom to set.
	 */
	@Override
	public void setMarginBottom(String marginBottom) {
		setCssText(STYLE_MARGIN_BOTTOM, marginBottom);
	}

	/**
	 * @param marginLeft The marginLeft to set.
	 */
	@Override
	public void setMarginLeft(String marginLeft) {
		setCssText(STYLE_MARGIN_LEFT, marginLeft);
	}

	/**
	 * @param marginRight The marginRight to set.
	 */
	@Override
	public void setMarginRight(String marginRight) {
		setCssText(STYLE_MARGIN_RIGHT, marginRight);
	}

	/**
	 * @param marginTop The marginTop to set.
	 */
	@Override
	public void setMarginTop(String marginTop) {
		setCssText(STYLE_MARGIN_TOP, marginTop);
	}

	/**
	 * @param masterPage The masterPage to set.
	 */
	@Override
	public void setMasterPage(String masterPage) {
		setCssText(STYLE_MASTER_PAGE, masterPage);
	}

	/**
	 * @param orphans The orphans to set.
	 */
	@Override
	public void setOrphans(String orphans) {
		setCssText(STYLE_ORPHANS, orphans);
	}

	/**
	 * @param paddingBottom The paddingBottom to set.
	 */
	@Override
	public void setPaddingBottom(String paddingBottom) {
		setCssText(STYLE_PADDING_BOTTOM, paddingBottom);
	}

	/**
	 * @param paddingLeft The paddingLeft to set.
	 */
	@Override
	public void setPaddingLeft(String paddingLeft) {
		setCssText(STYLE_PADDING_LEFT, paddingLeft);
	}

	/**
	 * @param paddingRight The paddingRight to set.
	 */
	@Override
	public void setPaddingRight(String paddingRight) {
		setCssText(STYLE_PADDING_RIGHT, paddingRight);
	}

	/**
	 * @param paddingTop The paddingTop to set.
	 */
	@Override
	public void setPaddingTop(String paddingTop) {
		setCssText(STYLE_PADDING_TOP, paddingTop);
	}

	/**
	 * @param pageBreakAfter The pageBreakAfter to set.
	 */
	@Override
	public void setPageBreakAfter(String pageBreakAfter) {
		setCssText(STYLE_PAGE_BREAK_AFTER, pageBreakAfter);
	}

	/**
	 * @param pageBreakBefore The pageBreakBefore to set.
	 */
	@Override
	public void setPageBreakBefore(String pageBreakBefore) {
		setCssText(STYLE_PAGE_BREAK_BEFORE, pageBreakBefore);
	}

	/**
	 * @param pageBreakInside The pageBreakInside to set.
	 */
	@Override
	public void setPageBreakInside(String pageBreakInside) {
		setCssText(STYLE_PAGE_BREAK_INSIDE, pageBreakInside);
	}

	/**
	 * @param showIfBlank The showIfBlank to set.
	 */
	@Override
	public void setShowIfBlank(String showIfBlank) {
		setCssText(STYLE_SHOW_IF_BLANK, showIfBlank);
	}

	/**
	 * @param textAlign The textAlign to set.
	 */
	@Override
	public void setTextAlign(String textAlign) {
		setCssText(STYLE_TEXT_ALIGN, textAlign);
	}

	/**
	 * @param textIndent The textIndent to set.
	 */
	@Override
	public void setTextIndent(String textIndent) {
		setCssText(STYLE_TEXT_INDENT, textIndent);
	}

	/**
	 * @param textLineThrough The textLineThrough to set.
	 */
	@Override
	public void setTextLineThrough(String textLineThrough) {
		setCssText(STYLE_TEXT_LINETHROUGH, textLineThrough);
	}

	/**
	 * @param textOverline The textOverline to set.
	 */
	@Override
	public void setTextOverline(String textOverline) {
		setCssText(STYLE_TEXT_OVERLINE, textOverline);
	}

	/**
	 * @param textTransform The textTransform to set.
	 */
	@Override
	public void setTextTransform(String textTransform) {
		setCssText(STYLE_TEXT_TRANSFORM, textTransform);
	}

	/**
	 * @param textUnderline The textUnderline to set.
	 */
	@Override
	public void setTextUnderline(String textUnderline) {
		setCssText(STYLE_TEXT_UNDERLINE, textUnderline);
	}

	/**
	 * @param verticalAlign The verticalAlign to set.
	 */
	@Override
	public void setVerticalAlign(String verticalAlign) {
		setCssText(STYLE_VERTICAL_ALIGN, verticalAlign);
	}

	/**
	 * @param whiteSpace The whiteSpace to set.
	 */
	@Override
	public void setWhiteSpace(String whiteSpace) {
		setCssText(STYLE_WHITE_SPACE, whiteSpace);
	}

	/**
	 * @param widows The widows to set.
	 */
	@Override
	public void setWidows(String widows) {
		setCssText(STYLE_WIDOWS, widows);
	}

	@Override
	public void setWordSpacing(String wordSpacing) throws DOMException {
		setCssText(STYLE_WORD_SPACING, wordSpacing);
	}

	private DataFormatValue copyDataFormat(DataFormatValue old) {
		DataFormatValue newValue = DataFormatValue.createDataFormatValue(old);
		setDataFormat(newValue);
		return newValue;
	}

	@Override
	public void setStringFormat(String format) throws DOMException {
		DataFormatValue value = getDataFormat();
		DataFormatValue newValue = copyDataFormat(value);
		newValue.setStringFormat(format, value == null ? null : value.getStringLocale());
	}

	@Override
	public void setNumberFormat(String format) throws DOMException {
		DataFormatValue value = getDataFormat();
		DataFormatValue newValue = copyDataFormat(value);
		newValue.setNumberFormat(format, value == null ? null : value.getNumberLocale());
	}

	@Override
	public void setDateFormat(String format) throws DOMException {
		DataFormatValue value = getDataFormat();
		DataFormatValue newValue = copyDataFormat(value);
		newValue.setDateFormat(format, value == null ? null : value.getDateLocale());
	}

	/**
	 * Set the date time format
	 *
	 * @param format
	 * @throws DOMException
	 */
	public void setDateTimeFormat(String format) throws DOMException {
		DataFormatValue value = getDataFormat();
		DataFormatValue newValue = copyDataFormat(value);
		newValue.setDateTimeFormat(format, value == null ? null : value.getDateTimeLocale());
	}

	/**
	 * Set the time format
	 *
	 * @param format
	 * @throws DOMException
	 */
	public void setTimeFormat(String format) throws DOMException {
		DataFormatValue value = getDataFormat();
		DataFormatValue newValue = copyDataFormat(value);
		newValue.setTimeFormat(format, value == null ? null : value.getTimeLocale());
	}

	@Override
	public void setNumberAlign(String align) throws DOMException {
		setCssText(STYLE_NUMBER_ALIGN, align);
	}

	@Override
	public void setVisibleFormat(String formats) throws DOMException {
		setCssText(STYLE_VISIBLE_FORMAT, formats);
	}

	@Override
	public String getStringFormat() {
		DataFormatValue value = getDataFormat();
		if (value != null) {
			return value.getStringPattern();
		}
		return null;
	}

	@Override
	public String getNumberFormat() {
		DataFormatValue value = getDataFormat();
		if (value != null) {

			return value.getNumberPattern();
		}
		return null;
	}

	@Override
	public String getDateFormat() {
		DataFormatValue value = getDataFormat();
		if (value != null) {
			return value.getDatePattern();
		}
		return null;
	}

	@Override
	public String getDateTimeFormat() {
		DataFormatValue value = getDataFormat();
		if (value != null) {
			return value.getDateTimePattern();
		}
		return null;
	}

	@Override
	public String getTimeFormat() {
		DataFormatValue value = getDataFormat();
		if (value != null) {
			return value.getTimePattern();
		}
		return null;
	}

	@Override
	public String getNumberAlign() {
		return getCssText(STYLE_NUMBER_ALIGN);
	}

	// unsupported CSS2 properties
	@Override
	public String getAzimuth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAzimuth(String azimuth) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBackground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBackground(String background) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBackgroundPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBackgroundPosition(String backgroundPosition) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorder(String border) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderCollapse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderCollapse(String borderCollapse) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderColor(String borderColor) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderSpacing() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderSpacing(String borderSpacing) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderStyle(String borderStyle) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderTop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderTop(String borderTop) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderRight(String borderRight) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderBottom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderBottom(String borderBottom) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderLeft(String borderLeft) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBorderWidth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBorderWidth(String borderWidth) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBottom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBottom(String bottom) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCaptionSide() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCaptionSide(String captionSide) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getClear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClear(String clear) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getClip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClip(String clip) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent(String content) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCounterIncrement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterIncrement(String counterIncrement) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCounterReset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCounterReset(String counterReset) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCue(String cue) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCueAfter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCueAfter(String cueAfter) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCueBefore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCueBefore(String cueBefore) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCursor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCursor(String cursor) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDirection() {
		return getCssText(STYLE_DIRECTION); // bidi_hcg
	}

	@Override
	public void setDirection(String direction) throws DOMException {
		setCssText(STYLE_DIRECTION, direction); // bidi_hcg
	}

	@Override
	public String getElevation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setElevation(String elevation) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getEmptyCells() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEmptyCells(String emptyCells) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCssFloat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCssFloat(String cssFloat) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFont() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setFont(String font) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getFontSizeAdjust() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setFontSizeAdjust(String fontSizeAdjust) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getFontStretch() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setFontStretch(String fontStretch) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getHeight() {
		return getCssText(STYLE_HEIGHT);
	}

	@Override
	public void setHeight(String height) throws DOMException {
		setCssText(STYLE_HEIGHT, height);
	}

	@Override
	public String getLeft() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setLeft(String left) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getListStyle() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setListStyle(String listStyle) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getListStyleImage() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setListStyleImage(String listStyleImage) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getListStylePosition() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setListStylePosition(String listStylePosition) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getListStyleType() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setListStyleType(String listStyleType) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getMargin() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setMargin(String margin) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getMarkerOffset() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setMarkerOffset(String markerOffset) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getMarks() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setMarks(String marks) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getMaxHeight() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setMaxHeight(String maxHeight) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getMaxWidth() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setMaxWidth(String maxWidth) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getMinHeight() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setMinHeight(String minHeight) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getMinWidth() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setMinWidth(String minWidth) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getOutline() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setOutline(String outline) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getOutlineColor() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setOutlineColor(String outlineColor) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getOutlineStyle() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setOutlineStyle(String outlineStyle) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getOutlineWidth() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setOutlineWidth(String outlineWidth) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getOverflow() {
		return getCssText(STYLE_OVERFLOW);
	}

	@Override
	public void setOverflow(String overflow) throws DOMException {
		setCssText(STYLE_OVERFLOW, overflow);
	}

	@Override
	public String getPadding() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPadding(String padding) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPage() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPage(String page) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPause() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPause(String pause) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPauseAfter() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPauseAfter(String pauseAfter) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPauseBefore() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPauseBefore(String pauseBefore) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPitch() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPitch(String pitch) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPitchRange() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPitchRange(String pitchRange) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPlayDuring() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPlayDuring(String playDuring) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getPosition() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setPosition(String position) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getQuotes() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setQuotes(String quotes) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getRichness() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setRichness(String richness) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getRight() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setRight(String right) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getSize() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setSize(String size) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getSpeak() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setSpeak(String speak) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getSpeakHeader() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setSpeakHeader(String speakHeader) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getSpeakNumeral() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setSpeakNumeral(String speakNumeral) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getSpeakPunctuation() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setSpeakPunctuation(String speakPunctuation) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getSpeechRate() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setSpeechRate(String speechRate) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getStress() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setStress(String stress) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getTableLayout() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setTableLayout(String tableLayout) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getTextDecoration() {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public void setTextDecoration(String textDecoration) throws DOMException {
		throw createUnsupportedPropertyException("text-decoration");
	}

	@Override
	public String getTextShadow() {
		throw createUnsupportedPropertyException("text-shadow");
	}

	@Override
	public void setTextShadow(String textShadow) throws DOMException {
		throw createUnsupportedPropertyException("text-shadow");
	}

	@Override
	public String getTop() {
		throw createUnsupportedPropertyException("top");
	}

	@Override
	public void setTop(String top) throws DOMException {
		throw createUnsupportedPropertyException("top");
	}

	@Override
	public String getUnicodeBidi() {
		throw createUnsupportedPropertyException("unicode-bidi");
	}

	@Override
	public void setUnicodeBidi(String unicodeBidi) throws DOMException {
		throw createUnsupportedPropertyException("unicode-bidi");
	}

	@Override
	public String getVisibility() {
		throw createUnsupportedPropertyException("visibility");
	}

	@Override
	public void setVisibility(String visibility) throws DOMException {
		throw createUnsupportedPropertyException("visibility");
	}

	@Override
	public String getVoiceFamily() {
		throw createUnsupportedPropertyException("voice-family");
	}

	@Override
	public void setVoiceFamily(String voiceFamily) throws DOMException {
		throw createUnsupportedPropertyException("voice-family");
	}

	@Override
	public String getVolume() {
		throw createUnsupportedPropertyException("volumn");
	}

	@Override
	public void setVolume(String volume) throws DOMException {
		throw createUnsupportedPropertyException("volumn");
	}

	@Override
	public String getWidth() {
		return getCssText(STYLE_WIDTH);
	}

	@Override
	public void setWidth(String width) throws DOMException {
		setCssText(STYLE_WIDTH, width);
	}

	@Override
	public String getZIndex() {
		throw createUnsupportedPropertyException("zindex");
	}

	@Override
	public void setZIndex(String zIndex) throws DOMException {
		throw createUnsupportedPropertyException("zindex");
	}

	private DOMException createUnsupportedPropertyException(String property) {
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, property);
	}

	protected void writeCSSValue(DataOutputStream out, String propertyName, CSSValue value) throws IOException {
		int index = getPropertyIndex(propertyName);
		if (index == StyleConstants.STYLE_DATA_FORMAT) {
			DataFormatValue.write(out, (DataFormatValue) value);
		} else {
			IOUtil.writeString(out, value.getCssText());
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		// count how many valid value in the style
		int validCount = 0;
		for (int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++) {
			CSSValue value = getProperty(i);
			if (null != value) {
				validCount++;
			}
		}
		IOUtil.writeInt(out, validCount);

		// write the style's property
		for (int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++) {
			CSSValue value = getProperty(i);
			if (null != value) {
				String propertyName = engine.getPropertyName(i);
				IOUtil.writeString(out, propertyName);
				writeCSSValue(out, propertyName, value);
			}
		}
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		int validCount = IOUtil.readInt(in);
		for (int i = 0; i < validCount; i++) {
			String propertyName = IOUtil.readString(in);
			int index = getPropertyIndex(propertyName);
			if (index == -1) {
				String propertyCssText = IOUtil.readString(in);
				if (BIRTConstants.BIRT_STRING_FORMAT_PROPERTY.equalsIgnoreCase(propertyName)) {
					this.setStringFormat(propertyCssText);
				} else if (BIRTConstants.BIRT_NUMBER_FORMAT_PROPERTY.equalsIgnoreCase(propertyName)) {
					this.setNumberFormat(propertyCssText);
				} else if (BIRTConstants.BIRT_DATE_FORMAT_PROPERTY.equalsIgnoreCase(propertyName)) {
					this.setDateFormat(propertyCssText);
				} else if (BIRTConstants.BIRT_TIME_FORMAT_PROPERTY.equalsIgnoreCase(propertyName)) {
					this.setTimeFormat(propertyCssText);
				} else if (BIRTConstants.BIRT_DATE_TIME_FORMAT_PROPERTY.equalsIgnoreCase(propertyName)) {
					this.setDateTimeFormat(propertyCssText);
				} else if (BIRTConstants.BIRT_BACKGROUND_IMAGE_TYPE.equalsIgnoreCase(propertyName)) {
					this.setBackgroundImageType(propertyCssText);
					// diagonal line
				} else if (BIRTConstants.BIRT_BORDER_DIAGONAL_NUMBER.equalsIgnoreCase(propertyName)) {
					this.setDiagonalNumber(Integer.parseInt(propertyCssText));
				} else if (BIRTConstants.BIRT_BORDER_DIAGONAL_STYLE.equalsIgnoreCase(propertyName)) {
					this.setDiagonalStyle(propertyCssText);
				} else if (BIRTConstants.BIRT_BORDER_DIAGONAL_WIDTH.equalsIgnoreCase(propertyName)) {
					this.setDiagonalWidth(propertyCssText);
				} else if (BIRTConstants.BIRT_BORDER_DIAGONAL_COLOR.equalsIgnoreCase(propertyName)) {
					this.setDiagonalColor(propertyCssText);
					// antidiagonal line
				} else if (BIRTConstants.BIRT_BORDER_ANTIDIAGONAL_NUMBER.equalsIgnoreCase(propertyName)) {
					this.setAntidiagonalNumber(Integer.parseInt(propertyCssText));
				} else if (BIRTConstants.BIRT_BORDER_ANTIDIAGONAL_STYLE.equalsIgnoreCase(propertyName)) {
					this.setAntidiagonalStyle(propertyCssText);
				} else if (BIRTConstants.BIRT_BORDER_ANTIDIAGONAL_WIDTH.equalsIgnoreCase(propertyName)) {
					this.setAntidiagonalWidth(propertyCssText);
				} else if (BIRTConstants.BIRT_BORDER_ANTIDIAGONAL_COLOR.equalsIgnoreCase(propertyName)) {
					this.setAntidiagonalColor(propertyCssText);
				} else {
					throw new IOException(propertyName + " not valid");
				}
			} else if (index == StyleConstants.STYLE_DATA_FORMAT) {
				CSSValue value = DataFormatValue.read(in);
				setProperty(index, value);
			} else {
				String propertyCssText = IOUtil.readString(in);
				setCssText(index, propertyCssText);
			}
		}
	}

	@Override
	public DataFormatValue getDataFormat() {
		return (DataFormatValue) this.getProperty(StyleConstants.STYLE_DATA_FORMAT);
	}

	@Override
	public void setDataFormat(DataFormatValue value) {
		setProperty(StyleConstants.STYLE_DATA_FORMAT, value);
	}

	@Override
	public int getDiagonalNumber() {
		return Integer.parseInt(getCssText(STYLE_BORDER_DIAGONAL_NUMBER));
	}

	@Override
	public void setDiagonalNumber(Integer number) {
		setCssText(StyleConstants.STYLE_BORDER_DIAGONAL_NUMBER, number.toString());
	}

	@Override
	public String getDiagonalStyle() {
		return getCssText(StyleConstants.STYLE_BORDER_DIAGONAL_STYLE);
	}

	@Override
	public void setDiagonalStyle(String style) {
		setCssText(StyleConstants.STYLE_BORDER_DIAGONAL_STYLE, style);
	}

	@Override
	public String getDiagonalWidth() {
		return getCssText(StyleConstants.STYLE_BORDER_DIAGONAL_WIDTH);
	}

	@Override
	public void setDiagonalWidth(String width) {
		setCssText(StyleConstants.STYLE_BORDER_DIAGONAL_WIDTH, width);
	}

	@Override
	public String getDiagonalColor() {
		return getCssText(StyleConstants.STYLE_BORDER_DIAGONAL_COLOR);
	}

	@Override
	public void setDiagonalColor(String color) {
		setCssText(StyleConstants.STYLE_BORDER_DIAGONAL_COLOR, color);
	}

	@Override
	public int getAntidiagonalNumber() {
		return Integer.parseInt(this.getProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_NUMBER).toString());
	}

	@Override
	public void setAntidiagonalNumber(Integer number) {
		setCssText(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_NUMBER, number.toString());
	}

	@Override
	public String getAntidiagonalStyle() {
		return getCssText(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_STYLE);
	}

	@Override
	public void setAntidiagonalStyle(String style) {
		setCssText(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_STYLE, style);
	}

	@Override
	public String getAntidiagonalWidth() {
		return getCssText(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_WIDTH);
	}

	@Override
	public void setAntidiagonalWidth(String width) {
		setCssText(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_WIDTH, width);
	}

	@Override
	public String getAntidiagonalColor() {
		return getCssText(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_COLOR);
	}

	@Override
	public void setAntidiagonalColor(String color) {
		setCssText(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_COLOR, color);
	}
}
