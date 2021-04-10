/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

public class StyleDesign implements IScriptStyleDesign {

	private org.eclipse.birt.report.model.api.simpleapi.IStyle styleImpl;

	public StyleDesign(StyleHandle style) {
		styleImpl = SimpleElementFactory.getInstance().createStyle(style);
	}

	public StyleDesign(org.eclipse.birt.report.model.api.simpleapi.IStyle style) {
		styleImpl = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackgroundAttachment()
	 */

	public String getBackgroundAttachment() {
		return styleImpl.getBackgroundAttachment();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackgroundAttachment(java.lang.String)
	 */

	public void setBackgroundAttachment(String value) throws ScriptException {
		try {
			styleImpl.setBackgroundAttachment(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundImage
	 * ()
	 */

	public String getBackgroundImage() {
		return styleImpl.getBackgroundImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundImage
	 * (java.lang.String)
	 */

	public void setBackgroundImage(String value) throws ScriptException {
		try {
			styleImpl.setBackgroundImage(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackgroundRepeat()
	 */

	public String getBackgroundRepeat() {
		return styleImpl.getBackgroundRepeat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackgroundRepeat(java.lang.String)
	 */

	public void setBackgroundRepeat(String value) throws ScriptException {
		try {
			styleImpl.setBackgroundRepeat(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderBottomStyle()
	 */

	public String getBorderBottomStyle() {
		return styleImpl.getBorderBottomStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomStyle(java.lang.String)
	 */

	public void setBorderBottomStyle(String value) throws ScriptException {
		try {
			styleImpl.setBorderBottomStyle(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftStyle
	 * ()
	 */

	public String getBorderLeftStyle() {
		return styleImpl.getBorderLeftStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftStyle
	 * (java.lang.String)
	 */

	public void setBorderLeftStyle(String value) throws ScriptException {
		try {
			styleImpl.setBorderLeftStyle(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderRightStyle()
	 */

	public String getBorderRightStyle() {
		return styleImpl.getBorderRightStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightStyle(java.lang.String)
	 */

	public void setBorderRightStyle(String value) throws ScriptException {
		try {
			styleImpl.setBorderRightStyle(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopStyle(
	 * )
	 */

	public String getBorderTopStyle() {
		return styleImpl.getBorderTopStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopStyle(
	 * java.lang.String)
	 */

	public void setBorderTopStyle(String value) throws ScriptException {
		try {
			styleImpl.setBorderTopStyle(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#canShrink()
	 */

	public boolean canShrink() {
		return styleImpl.canShrink();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setCanShrink(
	 * boolean)
	 */

	public void setCanShrink(boolean value) throws ScriptException {
		try {
			styleImpl.setCanShrink(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getStringFormat()
	 */

	public String getStringFormat() {
		return styleImpl.getStringFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getStringFormatCategory()
	 */

	public String getStringFormatCategory() {
		return styleImpl.getStringFormatCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setStringFormat(
	 * java.lang.String)
	 */

	public void setStringFormat(String pattern) throws ScriptException {
		try {
			styleImpl.setStringFormat(pattern);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setStringFormatCategory(java.lang.String)
	 */

	public void setStringFormatCategory(String pattern) throws ScriptException {
		try {
			styleImpl.setStringFormatCategory(pattern);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getNumberFormat()
	 */

	public String getNumberFormat() {
		return styleImpl.getNumberFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getNumberFormatCategory()
	 */

	public String getNumberFormatCategory() {
		return styleImpl.getNumberFormatCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setNumberFormat(
	 * java.lang.String)
	 */

	public void setNumberFormat(String pattern) throws ScriptException {
		try {
			styleImpl.setNumberFormat(pattern);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setNumberFormatCategory(java.lang.String)
	 */

	public void setNumberFormatCategory(String category) throws ScriptException {
		try {
			styleImpl.setNumberFormatCategory(category);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getDateTimeFormat(
	 * )
	 */

	public String getDateTimeFormat() {
		return styleImpl.getDateTimeFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getDateTimeFormatCategory()
	 */

	public String getDateTimeFormatCategory() {
		return styleImpl.getDateTimeFormatCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setDateTimeFormat(
	 * java.lang.String)
	 */

	public void setDateTimeFormat(String pattern) throws ScriptException {
		try {
			styleImpl.setDateTimeFormat(pattern);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setDateTimeFormatCategory(java.lang.String)
	 */

	public void setDateTimeFormatCategory(String pattern) throws ScriptException {
		try {
			styleImpl.setDateTimeFormatCategory(pattern);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getDisplay()
	 */

	public String getDisplay() {
		return styleImpl.getDisplay();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setDisplay(java.
	 * lang.String)
	 */

	public void setDisplay(String value) throws ScriptException {
		try {
			styleImpl.setDisplay(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getMasterPage()
	 */

	public String getMasterPage() {
		return styleImpl.getMasterPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setMasterPage(java
	 * .lang.String)
	 */

	public void setMasterPage(String value) throws ScriptException {
		try {
			styleImpl.setMasterPage(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getOrphans()
	 */

	public String getOrphans() {
		return styleImpl.getOrphans();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setOrphans(java.
	 * lang.String)
	 */

	public void setOrphans(String value) throws ScriptException {
		try {
			styleImpl.setOrphans(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakAfter(
	 * )
	 */

	public String getPageBreakAfter() {
		return styleImpl.getPageBreakAfter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakAfter(
	 * java.lang.String)
	 */

	public void setPageBreakAfter(String value) throws ScriptException {
		try {
			styleImpl.setPageBreakAfter(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakBefore
	 * ()
	 */

	public String getPageBreakBefore() {
		return styleImpl.getPageBreakBefore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakBefore
	 * (java.lang.String)
	 */

	public void setPageBreakBefore(String value) throws ScriptException {
		try {
			styleImpl.setPageBreakBefore(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakInside
	 * ()
	 */

	public String getPageBreakInside() {
		return styleImpl.getPageBreakInside();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakInside
	 * (java.lang.String)
	 */

	public void setPageBreakInside(String value) throws ScriptException {
		try {
			styleImpl.setPageBreakInside(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#showIfBlank()
	 */

	public boolean getShowIfBlank() {
		return styleImpl.getShowIfBlank();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setShowIfBlank(
	 * boolean)
	 */

	public void setShowIfBlank(boolean value) throws ScriptException {
		try {
			styleImpl.setShowIfBlank(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextUnderline()
	 */

	public String getTextUnderline() {
		return styleImpl.getTextUnderline();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextUnderline(
	 * java.lang.String)
	 */

	public void setTextUnderline(String value) throws ScriptException {
		try {
			styleImpl.setTextUnderline(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextOverline()
	 */

	public String getTextOverline() {
		return styleImpl.getTextOverline();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextOverline(
	 * java.lang.String)
	 */

	public void setTextOverline(String value) throws ScriptException {
		try {
			styleImpl.setTextOverline(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextLineThrough
	 * ()
	 */

	public String getTextLineThrough() {
		return styleImpl.getTextLineThrough();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextLineThrough
	 * (java.lang.String)
	 */

	public void setTextLineThrough(String value) throws ScriptException {
		try {
			styleImpl.setTextLineThrough(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextAlign()
	 */

	public String getTextAlign() {
		return styleImpl.getTextAlign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextAlign(java.
	 * lang.String)
	 */

	public void setTextAlign(String value) throws ScriptException {
		try {
			styleImpl.setTextAlign(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextTransform()
	 */

	public String getTextTransform() {
		return styleImpl.getTextTransform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextTransform(
	 * java.lang.String)
	 */

	public void setTextTransform(String value) throws ScriptException {
		try {
			styleImpl.setTextTransform(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getVerticalAlign()
	 */

	public String getVerticalAlign() {
		return styleImpl.getVerticalAlign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setVerticalAlign(
	 * java.lang.String)
	 */

	public void setVerticalAlign(String value) throws ScriptException {
		try {
			styleImpl.setVerticalAlign(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getWhiteSpace()
	 */

	public String getWhiteSpace() {
		return styleImpl.getWhiteSpace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setWhiteSpace(java
	 * .lang.String)
	 */

	public void setWhiteSpace(String value) throws ScriptException {
		try {
			styleImpl.setWhiteSpace(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getWidows()
	 */

	public String getWidows() {
		return styleImpl.getWidows();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setWidows(java.
	 * lang.String)
	 */

	public void setWidows(String value) throws ScriptException {
		try {
			styleImpl.setWidows(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getColor()
	 */

	public String getColor() {
		return styleImpl.getColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setColor(java.lang
	 * .String)
	 */
	public void setColor(String color) throws ScriptException {
		try {
			styleImpl.setColor(color);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundColor
	 * ()
	 */

	public String getBackgroundColor() {
		return styleImpl.getBackgroundColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundColor
	 * (java.lang.String)
	 */
	public void setBackgroundColor(String color) throws ScriptException {
		try {
			styleImpl.setBackgroundColor(color);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopColor(
	 * )
	 */

	public String getBorderTopColor() {
		return styleImpl.getBorderTopColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopColor(
	 * java.lang.String)
	 */
	public void setBorderTopColor(String color) throws ScriptException {
		try {
			styleImpl.setBorderTopColor(color);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftColor
	 * ()
	 */
	public String getBorderLeftColor() {
		return styleImpl.getBorderLeftColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftColor
	 * (java.lang.String)
	 */
	public void setBorderLeftColor(String color) throws ScriptException {
		try {
			styleImpl.setBorderLeftColor(color);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderRightColor()
	 */

	public String getBorderRightColor() {
		return styleImpl.getBorderRightColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightColor(java.lang.String)
	 */
	public void setBorderRightColor(String color) throws ScriptException {
		try {
			styleImpl.setBorderRightColor(color);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderBottomColor()
	 */

	public String getBorderBottomColor() {
		return styleImpl.getBorderBottomColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomColor(java.lang.String)
	 */
	public void setBorderBottomColor(String color) throws ScriptException {
		try {
			styleImpl.setBorderBottomColor(color);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackGroundPositionX()
	 */

	public String getBackGroundPositionX() {
		return styleImpl.getBackGroundPositionX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackGroundPositionX(java.lang.String)
	 */
	public void setBackGroundPositionX(String x) throws ScriptException {
		try {
			styleImpl.setBackGroundPositionX(x);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackGroundPositionY()
	 */

	public String getBackGroundPositionY() {
		return styleImpl.getBackGroundPositionY();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackGroundPositionY(java.lang.String)
	 */
	public void setBackGroundPositionY(String y) throws ScriptException {
		try {
			styleImpl.setBackGroundPositionY(y);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getLetterSpacing()
	 */

	public String getLetterSpacing() {
		return styleImpl.getLetterSpacing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setLetterSpacing(
	 * java.lang.String)
	 */
	public void setLetterSpacing(String spacing) throws ScriptException {
		try {
			styleImpl.setLetterSpacing(spacing);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getLineHeight()
	 */

	public String getLineHeight() {
		return styleImpl.getLineHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setLineHeight(java
	 * .lang.String)
	 */
	public void setLineHeight(String height) throws ScriptException {
		try {
			styleImpl.setLineHeight(height);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextIndent()
	 */

	public String getTextIndent() {
		return styleImpl.getTextIndent();
	}

	public void setTextIndent(String indent) throws ScriptException {
		try {
			styleImpl.setTextIndent(indent);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getWordSpacing()
	 */

	public String getWordSpacing() {
		return styleImpl.getWordSpacing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setWordSpacing(
	 * java.lang.String)
	 */
	public void setWordSpacing(String spacing) throws ScriptException {
		try {
			styleImpl.setWordSpacing(spacing);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopWidth(
	 * )
	 */

	public String getBorderTopWidth() {
		return styleImpl.getBorderTopWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopWidth(
	 * java.lang.String)
	 */
	public void setBorderTopWidth(String width) throws ScriptException {
		try {
			styleImpl.setBorderTopWidth(width);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftWidth
	 * ()
	 */

	public String getBorderLeftWidth() {
		return styleImpl.getBorderLeftWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftWidth
	 * (java.lang.String)
	 */
	public void setBorderLeftWidth(String width) throws ScriptException {
		try {
			styleImpl.setBorderLeftWidth(width);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderRightWidth()
	 */

	public String getBorderRightWidth() {
		return styleImpl.getBorderRightWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightWidth(java.lang.String)
	 */
	public void setBorderRightWidth(String width) throws ScriptException {
		try {
			styleImpl.setBorderRightWidth(width);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderBottomWidth()
	 */

	public String getBorderBottomWidth() {
		return styleImpl.getBorderBottomWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomWidth(java.lang.String)
	 */
	public void setBorderBottomWidth(String width) throws ScriptException {
		try {
			styleImpl.setBorderBottomWidth(width);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginTop()
	 */

	public String getMarginTop() {
		return styleImpl.getMarginTop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginTop(java.
	 * lang.String)
	 */
	public void setMarginTop(String margin) throws ScriptException {
		try {
			styleImpl.setMarginTop(margin);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public String getMarginRight() {
		return styleImpl.getMarginRight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginRight(
	 * java.lang.String)
	 */
	public void setMarginRight(String margin) throws ScriptException {
		try {
			styleImpl.setMarginRight(margin);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginLeft()
	 */

	public String getMarginLeft() {
		return styleImpl.getMarginLeft();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginLeft(
	 * margin)
	 */
	public void setMarginLeft(String margin) throws ScriptException {
		try {
			styleImpl.setMarginLeft(margin);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginBottom()
	 */

	public String getMarginBottom() {
		return styleImpl.getMarginBottom();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginBottom(
	 * java.lang.String)
	 */
	public void setMarginBottom(String margin) throws ScriptException {
		try {
			styleImpl.setMarginBottom(margin);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingTop()
	 */

	public String getPaddingTop() {
		return styleImpl.getPaddingTop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingTop(java
	 * .lang.String)
	 */
	public void setPaddingTop(String padding) throws ScriptException {
		try {
			styleImpl.setPaddingTop(padding);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingRight()
	 */

	public String getPaddingRight() {
		return styleImpl.getPaddingRight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingRight(
	 * java.lang.String)
	 */
	public void setPaddingRight(String padding) throws ScriptException {
		try {
			styleImpl.setPaddingRight(padding);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingLeft()
	 */

	public String getPaddingLeft() {
		return styleImpl.getPaddingLeft();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingLeft(
	 * java.lang.String)
	 */
	public void setPaddingLeft(String padding) throws ScriptException {
		try {
			styleImpl.setPaddingLeft(padding);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingBottom()
	 */

	public String getPaddingBottom() {
		return styleImpl.getPaddingBottom();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingBottom(
	 * java.lang.String)
	 */
	public void setPaddingBottom(String padding) throws ScriptException {
		try {
			styleImpl.setPaddingBottom(padding);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontSize()
	 */

	public String getFontSize() {
		return styleImpl.getFontSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontSize(java.
	 * lang.String)
	 */
	public void setFontSize(String fontSize) throws ScriptException {
		try {
			styleImpl.setFontSize(fontSize);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontFamily()
	 */

	public String getFontFamily() {
		return styleImpl.getFontFamily();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontFamily(java
	 * .lang.String)
	 */
	public void setFontFamily(String fontFamily) throws ScriptException {
		try {
			styleImpl.setFontFamily(fontFamily);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontWeight()
	 */

	public String getFontWeight() {
		return styleImpl.getFontWeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontWeight(java
	 * .lang.String)
	 */

	public void setFontWeight(String fontWeight) throws ScriptException {
		try {
			styleImpl.setFontWeight(fontWeight);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontVariant()
	 */

	public String getFontVariant() {
		return styleImpl.getFontVariant();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontVariant(
	 * java.lang.String)
	 */

	public void setFontVariant(String fontVariant) throws ScriptException {
		try {
			styleImpl.setFontVariant(fontVariant);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontStyle()
	 */

	public String getFontStyle() {
		return styleImpl.getFontStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontStyle(java.
	 * lang.String)
	 */

	public void setFontStyle(String fontStyle) throws ScriptException {
		try {
			styleImpl.setFontStyle(fontStyle);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

}
