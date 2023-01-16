/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;

/**
 * Definition of the model style
 *
 * @since 3.3
 *
 */
public class Style implements IStyle {

	private StyleHandle style;

	/**
	 * Constructor
	 *
	 * @param style
	 */
	public Style(StyleHandle style) {
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackgroundAttachment()
	 */

	@Override
	public String getBackgroundAttachment() {
		return style.getBackgroundAttachment();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackgroundAttachment(java.lang.String)
	 */

	@Override
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

	@Override
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

	@Override
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
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getBackgroundImageType()
	 */

	@Override
	public String getBackgroundImageType() {
		return style.getBackgroundImageType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackgroundImageType(java.lang.String)
	 */

	@Override
	public void setBackgroundImageType(String value) throws SemanticException {
		ActivityStack cmdStack = style.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			style.setBackgroundImageType(value);
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

	@Override
	public String getBackgroundRepeat() {
		return style.getBackgroundRepeat();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackgroundRepeat(java.lang.String)
	 */

	@Override
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

	@Override
	public String getBorderBottomStyle() {
		return style.getBorderBottomStyle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomStyle(java.lang.String)
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
	public String getBorderRightStyle() {
		return style.getBorderRightStyle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightStyle(java.lang.String)
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
	public boolean canShrink() {
		return style.canShrink();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setCanShrink
	 * (boolean)
	 */

	@Override
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

	@Override
	public String getStringFormat() {
		return style.getStringFormat();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getStringFormatCategory()
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
	public String getNumberFormat() {
		return style.getNumberFormat();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getNumberFormatCategory()
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
	public String getDateTimeFormat() {
		return style.getDateTimeFormat();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * getDateTimeFormatCategory()
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
	public String getDisplay() {
		return style.getDisplay();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setDisplay
	 * (java.lang.String)
	 */

	@Override
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

	@Override
	public String getMasterPage() {
		return style.getMasterPage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMasterPage
	 * (java.lang.String)
	 */

	@Override
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

	@Override
	public String getOrphans() {
		return style.getOrphans();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setOrphans
	 * (java.lang.String)
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
	public String getTextAlign() {
		return style.getTextAlign();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setTextAlign
	 * (java.lang.String)
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
	public String getWhiteSpace() {
		return style.getWhiteSpace();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setWhiteSpace
	 * (java.lang.String)
	 */

	@Override
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

	@Override
	public String getWidows() {
		return style.getWidows();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setWidows(
	 * java.lang.String)
	 */

	@Override
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

	@Override
	public String getColor() {
		return style.getColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setColor(java
	 * .lang.String)
	 */
	@Override
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

	@Override
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
	@Override
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

	@Override
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
	@Override
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
	@Override
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
	@Override
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

	@Override
	public String getBorderRightColor() {
		return style.getBorderRightColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightColor(java.lang.String)
	 */
	@Override
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

	@Override
	public String getBorderBottomColor() {
		return style.getBorderBottomColor().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomColor(java.lang.String)
	 */
	@Override
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

	@Override
	public String getBackGroundPositionX() {
		return style.getBackGroundPositionX().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackGroundPositionX(java.lang.String)
	 */
	@Override
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

	@Override
	public String getBackGroundPositionY() {
		return style.getBackGroundPositionY().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBackGroundPositionY(java.lang.String)
	 */
	@Override
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

	@Override
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
	@Override
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

	@Override
	public String getLineHeight() {
		return style.getLineHeight().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setLineHeight
	 * (java.lang.String)
	 */
	@Override
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

	@Override
	public String getTextIndent() {
		return style.getTextIndent().getStringValue();
	}

	@Override
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

	@Override
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
	@Override
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

	@Override
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
	@Override
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

	@Override
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
	@Override
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

	@Override
	public String getBorderRightWidth() {
		return style.getBorderRightWidth().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderRightWidth(java.lang.String)
	 */
	@Override
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

	@Override
	public String getBorderBottomWidth() {
		return style.getBorderBottomWidth().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.script.element.IStyleDesign#
	 * setBorderBottomWidth(java.lang.String)
	 */
	@Override
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

	@Override
	public String getMarginTop() {
		return style.getMarginTop().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginTop
	 * (java.lang.String)
	 */
	@Override
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

	@Override
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
	@Override
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

	@Override
	public String getMarginLeft() {
		return style.getMarginLeft().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setMarginLeft
	 * (margin)
	 */
	@Override
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

	@Override
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
	@Override
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

	@Override
	public String getPaddingTop() {
		return style.getPaddingTop().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setPaddingTop
	 * (java.lang.String)
	 */
	@Override
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

	@Override
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
	@Override
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

	@Override
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
	@Override
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

	@Override
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
	@Override
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

	@Override
	public String getFontSize() {
		return style.getFontSize().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontSize
	 * (java.lang.String)
	 */
	@Override
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

	@Override
	public String getFontFamily() {
		return style.getFontFamilyHandle().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontFamily
	 * (java.lang.String)
	 */
	@Override
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

	@Override
	public String getFontWeight() {
		return style.getFontWeight();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontWeight
	 * (java.lang.String)
	 */

	@Override
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

	@Override
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

	@Override
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

	@Override
	public String getFontStyle() {
		return style.getFontStyle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setFontStyle
	 * (java.lang.String)
	 */

	@Override
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

	@Override
	public String getTextDirection() {
		return style.getTextDirection();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.element.IStyleDesign#setDirection
	 * (java.lang.String)
	 */

	@Override
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
	@Override
	public String getOverflow() {
		return style.getOverflow();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setOverflow(java.lang
	 * .String)
	 */
	@Override
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
	@Override
	public String getHeight() {
		return style.getHeight();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setHeight(java.lang
	 * .String)
	 */
	@Override
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
	@Override
	public String getWidth() {
		return style.getWidth();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setWidth(java.lang
	 * .String)
	 */
	@Override
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
