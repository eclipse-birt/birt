/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.api.TOCStyle;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.ColorHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.FontHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;

public class TOCStyleUtil {

	private HashMap<String, TOCStyle> styleCaches = new HashMap<String, TOCStyle>();

	private ReportDesignHandle reportHandle;

	public TOCStyleUtil(ReportDesignHandle report) {
		this.reportHandle = report;
	}

	public TOCStyle getTOCStyle(int tocLevel, long elementId) throws ScriptException {
		String cacheKey = tocLevel + "." + elementId;
		TOCStyle tocStyle = styleCaches.get(cacheKey);
		if (tocStyle == null) {
			tocStyle = createTOCStyle(tocLevel, elementId);
			styleCaches.put(cacheKey, tocStyle);
		}
		return tocStyle;
	}

	private TOCStyle createTOCStyle(int tocLevel, long elementId) throws ScriptException {
		ReportElementHandle handle = (ReportElementHandle) reportHandle.getElementByID(elementId);

		String tocStyleName = TOCHandle.defaultTOCPrefixName + tocLevel;
		StyleHandle tocStyleHandle = reportHandle.findStyle(tocStyleName);
		StyleHandle sharedStyleHandle = null;
		TOCHandle tocHandle = null;
		if (handle instanceof ReportItemHandle) {
			tocHandle = ((ReportItemHandle) handle).getTOC();
		} else if (handle instanceof GroupHandle) {
			tocHandle = ((GroupHandle) handle).getTOC();
		}
		if (tocHandle != null) {
			String styleName = tocHandle.getStyleName();
			if (styleName != null) {
				sharedStyleHandle = reportHandle.findStyle(styleName);
			}
		}

		TOCStyle tocStyle = createTOCStyle(tocStyleHandle);
		TOCStyle sharedStyle = createTOCStyle(sharedStyleHandle);
		TOCStyle privateStyle = createTOCStyle(tocHandle);

		return mergeStyles(tocStyle, sharedStyle, privateStyle);
	}

	private String getColor(ColorHandle color) {
		if (color != null) {
			return color.getCssValue();
		}
		return null;
	}

	private String getDimension(DimensionHandle dimension) {
		if (dimension != null) {
			return dimension.getStringValue();
		}
		return null;
	}

	private String getDimensionIfSet(DimensionHandle dimension) {
		if (dimension != null && dimension.isSet()) {
			return dimension.getStringValue();
		}
		return null;
	}

	private String getFontFamily(FontHandle font) {
		if (font != null) {
			return font.getStringValue();
		}
		return null;
	}

	/**
	 * Creates TOCStyle from StyleHandle
	 */
	private TOCStyle createTOCStyle(StyleHandle handle) throws ScriptException {

		if (handle == null) {
			return null;
		}
		TOCStyle style = new TOCStyle();

		// Background
		style.setBackgroundColor(getColor(handle.getBackgroundColor()));
		style.setBackgroundImage(handle.getBackgroundImage());
		style.setBackgroundPositionX(getDimension(handle.getBackGroundPositionX()));
		style.setBackgroundPositionY(getDimension(handle.getBackGroundPositionY()));
		style.setBackgroundRepeat(handle.getBackgroundRepeat());

		// Text related
		style.setTextAlign(handle.getTextAlign());
		style.setTextIndent(getDimension(handle.getTextIndent()));
		style.setLetterSpacing(getDimension(handle.getLetterSpacing()));
		style.setLineHeight(getDimension(handle.getLineHeight()));
		style.setTextTransform(handle.getTextTransform());
		style.setVerticalAlign(handle.getVerticalAlign());
		style.setWhiteSpace(handle.getWhiteSpace());
		style.setWordSpacing(getDimension(handle.getWordSpacing()));

		// Section properties
		style.setDisplay(handle.getDisplay());
		style.setMasterPage(handle.getMasterPage());
		style.setPageBreakAfter(handle.getPageBreakAfter());
		style.setPageBreakBefore(handle.getPageBreakBefore());
		style.setPageBreakInside(handle.getPageBreakInside());

		// Font related

		style.setFontFamily(getFontFamily(handle.getFontFamilyHandle()));
		style.setColor(getColor(handle.getColor()));
		style.setFontSize(getDimensionIfSet(handle.getFontSize()));
		style.setFontStyle(handle.getFontStyle());
		style.setFontWeight(handle.getFontWeight());
		style.setFontVariant(handle.getFontVariant());

		// Text decoration
		style.setTextLineThrough(handle.getTextLineThrough());
		style.setTextOverline(handle.getTextOverline());
		style.setTextUnderline(handle.getTextUnderline());

		// Border
		style.setBorderBottomColor(getColor(handle.getBorderBottomColor()));
		style.setBorderBottomStyle(handle.getBorderBottomStyle());
		style.setBorderBottomWidth(getDimension(handle.getBorderBottomWidth()));
		style.setBorderLeftColor(getColor(handle.getBorderLeftColor()));
		style.setBorderLeftStyle(handle.getBorderLeftStyle());
		style.setBorderLeftWidth(getDimension(handle.getBorderLeftWidth()));
		style.setBorderRightColor(getColor(handle.getBorderRightColor()));
		style.setBorderRightStyle(handle.getBorderRightStyle());
		style.setBorderRightWidth(getDimension(handle.getBorderRightWidth()));
		style.setBorderTopColor(getColor(handle.getBorderTopColor()));
		style.setBorderTopStyle(handle.getBorderTopStyle());
		style.setBorderTopWidth(getDimension(handle.getBorderTopWidth()));

		// Margin
		style.setMarginBottom(getDimension(handle.getMarginBottom()));
		style.setMarginLeft(getDimension(handle.getMarginLeft()));
		style.setMarginRight(getDimension(handle.getMarginRight()));
		style.setMarginTop(getDimension(handle.getMarginTop()));

		// Padding
		style.setPaddingBottom(getDimension(handle.getPaddingBottom()));
		style.setPaddingLeft(getDimension(handle.getPaddingLeft()));
		style.setPaddingRight(getDimension(handle.getPaddingRight()));
		style.setPaddingTop(getDimension(handle.getPaddingTop()));

		// Format
		style.setStringFormat(handle.getStringFormat());
		style.setNumberFormat(handle.getNumberFormat());
		style.setDateFormat(handle.getDateTimeFormat());

		// bidi_hcg: Bidi related
		style.setDirection(handle.getTextDirection());
		return style;
	}

	/**
	 * create TOCStyle from TOC handle
	 */
	private TOCStyle createTOCStyle(TOCHandle handle) throws ScriptException {
		if (handle == null) {
			return null;
		}
		TOCStyle style = new TOCStyle();
		// Background
		style.setBackgroundColor(getColor(handle.getBackgroundColor()));

		// Text related
		style.setTextAlign(handle.getTextAlign());
		style.setTextIndent(getDimension(handle.getTextIndent()));
		style.setTextTransform(handle.getTextTransform());

		// Section properties

		// Font related

		style.setFontFamily(getFontFamily(handle.getFontFamily()));
		style.setColor(getColor(handle.getColor()));
		style.setFontSize(getDimensionIfSet(handle.getFontSize()));
		style.setFontStyle(handle.getFontStyle());
		style.setFontWeight(handle.getFontWeight());
		style.setFontVariant(handle.getFontVariant());

		// Text decoration
		style.setTextLineThrough(handle.getTextLineThrough());
		style.setTextOverline(handle.getTextOverline());
		style.setTextUnderline(handle.getTextUnderline());

		// Border
		style.setBorderBottomColor(getColor(handle.getBorderBottomColor()));
		style.setBorderBottomStyle(handle.getBorderBottomStyle());
		style.setBorderBottomWidth(getDimension(handle.getBorderBottomWidth()));
		style.setBorderLeftColor(getColor(handle.getBorderLeftColor()));
		style.setBorderLeftStyle(handle.getBorderLeftStyle());
		style.setBorderLeftWidth(getDimension(handle.getBorderLeftWidth()));
		style.setBorderRightColor(getColor(handle.getBorderRightColor()));
		style.setBorderRightStyle(handle.getBorderRightStyle());
		style.setBorderRightWidth(getDimension(handle.getBorderRightWidth()));
		style.setBorderTopColor(getColor(handle.getBorderTopColor()));
		style.setBorderTopStyle(handle.getBorderTopStyle());
		style.setBorderTopWidth(getDimension(handle.getBorderTopWidth()));

		// Margin

		// Padding

		// Format
		style.setStringFormat(handle.getStringFormat());
		style.setNumberFormat(handle.getNumberFormat());
		style.setDateFormat(handle.getDateTimeFormat());

		// bidi_hcg: Bidi related
		style.setDirection(handle.getTextDirection());
		return style;
	}

	private TOCStyle mergeStyles(TOCStyle s1, TOCStyle s2, TOCStyle s3) {
		TOCStyle style = new TOCStyle();
		Map properties = style.getProperties();
		if (s1 != null) {
			properties.putAll(s1.getProperties());
		}
		if (s2 != null) {
			properties.putAll(s2.getProperties());
		}
		if (s3 != null) {
			properties.putAll(s3.getProperties());
		}
		return style;
	}
}
