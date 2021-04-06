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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;

public class Style implements IStyle {

	private StyleHandle style;

	public Style(StyleHandle style) {
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackgroundAttachment()
	 */

	public String getBackgroundAttachment() {
		return style.getBackgroundAttachment();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackgroundAttachment(java.lang.String)
	 */

	public void setBackgroundAttachment(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBackgroundAttachment(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundImage
	 * ()
	 */

	public String getBackgroundImage() {
		return style.getBackgroundImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundImage
	 * (java.lang.String)
	 */

	public void setBackgroundImage(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBackgroundImage(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackgroundRepeat()
	 */

	public String getBackgroundRepeat() {
		return style.getBackgroundRepeat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackgroundRepeat(java.lang.String)
	 */

	public void setBackgroundRepeat(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBackgroundRepeat(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderBottomStyle()
	 */

	public String getBorderBottomStyle() {
		return style.getBorderBottomStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomStyle(java.lang.String)
	 */

	public void setBorderBottomStyle(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBorderBottomStyle(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftStyle
	 * ()
	 */

	public String getBorderLeftStyle() {
		return style.getBorderLeftStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftStyle
	 * (java.lang.String)
	 */

	public void setBorderLeftStyle(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBorderLeftStyle(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderRightStyle()
	 */

	public String getBorderRightStyle() {
		return style.getBorderRightStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightStyle(java.lang.String)
	 */

	public void setBorderRightStyle(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBorderRightStyle(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopStyle
	 * ()
	 */

	public String getBorderTopStyle() {
		return style.getBorderTopStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopStyle
	 * (java.lang.String)
	 */

	public void setBorderTopStyle(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBorderTopStyle(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#canShrink()
	 */

	public boolean canShrink() {
		return style.canShrink();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setCanShrink
	 * (boolean)
	 */

	public void setCanShrink(boolean value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setCanShrink(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getStringFormat ()
	 */

	public String getStringFormat() {
		return style.getStringFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getStringFormatCategory()
	 */

	public String getStringFormatCategory() {
		return style.getStringFormatCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setStringFormat
	 * (java.lang.String)
	 */

	public void setStringFormat(String pattern) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setStringFormat(pattern);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setStringFormatCategory(java.lang.String)
	 */

	public void setStringFormatCategory(String pattern) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setStringFormatCategory(pattern);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getNumberFormat ()
	 */

	public String getNumberFormat() {
		return style.getNumberFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getNumberFormatCategory()
	 */

	public String getNumberFormatCategory() {
		return style.getNumberFormatCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setNumberFormat
	 * (java.lang.String)
	 */

	public void setNumberFormat(String pattern) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setNumberFormat(pattern);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setNumberFormatCategory(java.lang.String)
	 */

	public void setNumberFormatCategory(String category) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setNumberFormatCategory(category);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getDateTimeFormat
	 * ()
	 */

	public String getDateTimeFormat() {
		return style.getDateTimeFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getDateTimeFormatCategory()
	 */

	public String getDateTimeFormatCategory() {
		return style.getDateTimeFormatCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setDateTimeFormat
	 * (java.lang.String)
	 */

	public void setDateTimeFormat(String pattern) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setDateTimeFormat(pattern);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setDateTimeFormatCategory(java.lang.String)
	 */

	public void setDateTimeFormatCategory(String pattern) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setDateTimeFormatCategory(pattern);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getDisplay()
	 */

	public String getDisplay() {
		return style.getDisplay();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setDisplay
	 * (java.lang.String)
	 */

	public void setDisplay(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setDisplay(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getMasterPage
	 * ()
	 */

	public String getMasterPage() {
		return style.getMasterPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMasterPage
	 * (java.lang.String)
	 */

	public void setMasterPage(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setMasterPage(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getOrphans()
	 */

	public String getOrphans() {
		return style.getOrphans();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setOrphans
	 * (java.lang.String)
	 */

	public void setOrphans(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setOrphans(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakAfter
	 * ()
	 */

	public String getPageBreakAfter() {
		return style.getPageBreakAfter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakAfter
	 * (java.lang.String)
	 */

	public void setPageBreakAfter(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setPageBreakAfter(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakBefore
	 * ()
	 */

	public String getPageBreakBefore() {
		return style.getPageBreakBefore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakBefore
	 * (java.lang.String)
	 */

	public void setPageBreakBefore(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setPageBreakBefore(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPageBreakInside
	 * ()
	 */

	public String getPageBreakInside() {
		return style.getPageBreakInside();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPageBreakInside
	 * (java.lang.String)
	 */

	public void setPageBreakInside(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setPageBreakInside(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#showIfBlank()
	 */

	public boolean getShowIfBlank() {
		return style.showIfBlank();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setShowIfBlank
	 * (boolean)
	 */

	public void setShowIfBlank(boolean value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setShowIfBlank(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextUnderline
	 * ()
	 */

	public String getTextUnderline() {
		return style.getTextUnderline();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextUnderline
	 * (java.lang.String)
	 */

	public void setTextUnderline(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setTextUnderline(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextOverline ()
	 */

	public String getTextOverline() {
		return style.getTextOverline();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextOverline
	 * (java.lang.String)
	 */

	public void setTextOverline(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setTextOverline(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextLineThrough
	 * ()
	 */

	public String getTextLineThrough() {
		return style.getTextLineThrough();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextLineThrough
	 * (java.lang.String)
	 */

	public void setTextLineThrough(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setTextLineThrough(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextAlign()
	 */

	public String getTextAlign() {
		return style.getTextAlign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextAlign
	 * (java.lang.String)
	 */

	public void setTextAlign(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setTextAlign(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextTransform
	 * ()
	 */

	public String getTextTransform() {
		return style.getTextTransform();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextTransform
	 * (java.lang.String)
	 */

	public void setTextTransform(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setTextTransform(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getVerticalAlign
	 * ()
	 */

	public String getVerticalAlign() {
		return style.getVerticalAlign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setVerticalAlign
	 * (java.lang.String)
	 */

	public void setVerticalAlign(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setVerticalAlign(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getWhiteSpace
	 * ()
	 */

	public String getWhiteSpace() {
		return style.getWhiteSpace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setWhiteSpace
	 * (java.lang.String)
	 */

	public void setWhiteSpace(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setWhiteSpace(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getWidows()
	 */

	public String getWidows() {
		return style.getWidows();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setWidows(
	 * java.lang.String)
	 */

	public void setWidows(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setWidows(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getColor()
	 */

	public String getColor() {
		return style.getColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setColor(java
	 * .lang.String)
	 */
	public void setColor(String color) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getColor().setValue(color);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBackgroundColor
	 * ()
	 */

	public String getBackgroundColor() {
		return style.getBackgroundColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBackgroundColor
	 * (java.lang.String)
	 */
	public void setBackgroundColor(String color) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBackgroundColor().setValue(color);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopColor
	 * ()
	 */

	public String getBorderTopColor() {
		return style.getBorderTopColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopColor
	 * (java.lang.String)
	 */
	public void setBorderTopColor(String color) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderTopColor().setValue(color);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftColor
	 * ()
	 */
	public String getBorderLeftColor() {
		return style.getBorderLeftColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftColor
	 * (java.lang.String)
	 */
	public void setBorderLeftColor(String color) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderLeftColor().setValue(color);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderRightColor()
	 */

	public String getBorderRightColor() {
		return style.getBorderRightColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightColor(java.lang.String)
	 */
	public void setBorderRightColor(String color) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderRightColor().setValue(color);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderBottomColor()
	 */

	public String getBorderBottomColor() {
		return style.getBorderBottomColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomColor(java.lang.String)
	 */
	public void setBorderBottomColor(String color) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderBottomColor().setValue(color);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackGroundPositionX()
	 */

	public String getBackGroundPositionX() {
		return style.getBackGroundPositionX().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackGroundPositionX(java.lang.String)
	 */
	public void setBackGroundPositionX(String x) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBackGroundPositionX().setValue(x);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackGroundPositionY()
	 */

	public String getBackGroundPositionY() {
		return style.getBackGroundPositionY().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackGroundPositionY(java.lang.String)
	 */
	public void setBackGroundPositionY(String y) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBackGroundPositionY().setValue(y);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getLetterSpacing
	 * ()
	 */

	public String getLetterSpacing() {
		return style.getLetterSpacing().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setLetterSpacing
	 * (java.lang.String)
	 */
	public void setLetterSpacing(String spacing) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getLetterSpacing().setValue(spacing);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getLineHeight
	 * ()
	 */

	public String getLineHeight() {
		return style.getLineHeight().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setLineHeight
	 * (java.lang.String)
	 */
	public void setLineHeight(String height) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getLineHeight().setValue(height);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getTextIndent
	 * ()
	 */

	public String getTextIndent() {
		return style.getTextIndent().getStringValue();
	}

	public void setTextIndent(String indent) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getTextIndent().setValue(indent);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getWordSpacing ()
	 */

	public String getWordSpacing() {
		return style.getWordSpacing().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setWordSpacing
	 * (java.lang.String)
	 */
	public void setWordSpacing(String spacing) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getWordSpacing().setValue(spacing);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderTopWidth
	 * ()
	 */

	public String getBorderTopWidth() {
		return style.getBorderTopWidth().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderTopWidth
	 * (java.lang.String)
	 */
	public void setBorderTopWidth(String width) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderTopWidth().setValue(width);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getBorderLeftWidth
	 * ()
	 */

	public String getBorderLeftWidth() {
		return style.getBorderLeftWidth().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setBorderLeftWidth
	 * (java.lang.String)
	 */
	public void setBorderLeftWidth(String width) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderLeftWidth().setValue(width);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderRightWidth()
	 */

	public String getBorderRightWidth() {
		return style.getBorderRightWidth().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightWidth(java.lang.String)
	 */
	public void setBorderRightWidth(String width) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderRightWidth().setValue(width);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBorderBottomWidth()
	 */

	public String getBorderBottomWidth() {
		return style.getBorderBottomWidth().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomWidth(java.lang.String)
	 */
	public void setBorderBottomWidth(String width) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getBorderBottomWidth().setValue(width);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginTop()
	 */

	public String getMarginTop() {
		return style.getMarginTop().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginTop
	 * (java.lang.String)
	 */
	public void setMarginTop(String margin) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getMarginTop().setValue(margin);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	public String getMarginRight() {
		return style.getMarginRight().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginRight
	 * (java.lang.String)
	 */
	public void setMarginRight(String margin) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getMarginRight().setValue(margin);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginLeft
	 * ()
	 */

	public String getMarginLeft() {
		return style.getMarginLeft().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginLeft
	 * (margin)
	 */
	public void setMarginLeft(String margin) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getMarginLeft().setValue(margin);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getMarginBottom ()
	 */

	public String getMarginBottom() {
		return style.getMarginBottom().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginBottom
	 * (java.lang.String)
	 */
	public void setMarginBottom(String margin) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getMarginBottom().setValue(margin);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingTop
	 * ()
	 */

	public String getPaddingTop() {
		return style.getPaddingTop().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingTop
	 * (java.lang.String)
	 */
	public void setPaddingTop(String padding) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getPaddingTop().setValue(padding);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingRight ()
	 */

	public String getPaddingRight() {
		return style.getPaddingRight().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingRight
	 * (java.lang.String)
	 */
	public void setPaddingRight(String padding) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getPaddingRight().setValue(padding);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingLeft ()
	 */

	public String getPaddingLeft() {
		return style.getPaddingLeft().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingLeft
	 * (java.lang.String)
	 */
	public void setPaddingLeft(String padding) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getPaddingLeft().setValue(padding);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getPaddingBottom
	 * ()
	 */

	public String getPaddingBottom() {
		return style.getPaddingBottom().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingBottom
	 * (java.lang.String)
	 */
	public void setPaddingBottom(String padding) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getPaddingBottom().setValue(padding);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontSize()
	 */

	public String getFontSize() {
		return style.getFontSize().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontSize
	 * (java.lang.String)
	 */
	public void setFontSize(String fontSize) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getFontSize().setValue(fontSize);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontFamily
	 * ()
	 */

	public String getFontFamily() {
		return style.getFontFamilyHandle().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontFamily
	 * (java.lang.String)
	 */
	public void setFontFamily(String fontFamily) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.getFontFamilyHandle().setValue(fontFamily);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontWeight
	 * ()
	 */

	public String getFontWeight() {
		return style.getFontWeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontWeight
	 * (java.lang.String)
	 */

	public void setFontWeight(String fontWeight) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setFontWeight(fontWeight);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontVariant ()
	 */

	public String getFontVariant() {
		return style.getFontVariant();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontVariant
	 * (java.lang.String)
	 */

	public void setFontVariant(String fontVariant) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setFontVariant(fontVariant);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getFontStyle()
	 */

	public String getFontStyle() {
		return style.getFontStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontStyle
	 * (java.lang.String)
	 */

	public void setFontStyle(String fontStyle) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setFontStyle(fontStyle);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.element.IStyleDesign#getDirection()
	 */

	public String getTextDirection() {
		return style.getTextDirection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setDirection
	 * (java.lang.String)
	 */

	public void setTextDirection(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setTextDirection(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#getOverflow()
	 */
	public String getOverflow() {
		return style.getOverflow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setOverflow(java.lang
	 * .String)
	 */
	public void setOverflow(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setOverflow(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#getHeight()
	 */
	public String getHeight() {
		return style.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setHeight(java.lang
	 * .String)
	 */
	public void setHeight(String height) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setHeight(height);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#getWidth()
	 */
	public String getWidth() {
		return style.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setWidth(java.lang
	 * .String)
	 */
	public void setWidth(String width) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setWidth(width);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}
}
