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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents the design time style for a report element in the scripting
 * environment
 */
public interface IStyle {

	/**
	 * Returns a background attachment as a string. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>BACKGROUND_ATTACHMENT_SCROLL
	 * <li>BACKGROUND_ATTACHMENT_FIXED
	 * </ul>
	 * 
	 * @return the background attachment
	 */

	String getBackgroundAttachment();

	/**
	 * Sets the background attachment. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>BACKGROUND_ATTACHMENT_SCROLL
	 * <li>BACKGROUND_ATTACHMENT_FIXED
	 * </ul>
	 * 
	 * @param value the new background attachment
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setBackgroundAttachment(String value) throws SemanticException;

	/**
	 * Returns the address of the background image.
	 * 
	 * @return the address of the background image as a string
	 */

	String getBackgroundImage();

	/**
	 * Sets the address of the background image. The value is a URL as a string.
	 * 
	 * @param value the new background image address
	 * @throws SemanticException if the property is locked
	 */

	void setBackgroundImage(String value) throws SemanticException;

	/**
	 * Returns the pattern of the repeat for a background image. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>BACKGROUND_REPEAT_REPEAT
	 * <li>BACKGROUND_REPEAT_REPEAT_X
	 * <li>BACKGROUND_REPEAT_REPEAT_Y
	 * <li>BACKGROUND_REPEAT_NO_REPEAT
	 * </ul>
	 * 
	 * @return the repeat pattern
	 */

	String getBackgroundRepeat();

	/**
	 * Sets the repeat pattern for a background image. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>BACKGROUND_REPEAT_REPEAT
	 * <li>BACKGROUND_REPEAT_REPEAT_X
	 * <li>BACKGROUND_REPEAT_REPEAT_Y
	 * <li>BACKGROUND_REPEAT_NO_REPEAT
	 * </ul>
	 * 
	 * @param value the new repeat pattern
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setBackgroundRepeat(String value) throws SemanticException;

	/**
	 * Returns the style of the bottom line of the border. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the bottom line
	 */

	String getBorderBottomStyle();

	/**
	 * Sets the style of the bottom line of the border. The input value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value the new style of the bottom line
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setBorderBottomStyle(String value) throws SemanticException;

	/**
	 * Returns the style of the left line of the border. The return value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the left line
	 */

	String getBorderLeftStyle();

	/**
	 * Sets the style of the left line of the border. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value the new style of the left line
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setBorderLeftStyle(String value) throws SemanticException;

	/**
	 * Returns the style of the right line of the border. The return value is
	 * defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the right line
	 */

	String getBorderRightStyle();

	/**
	 * Sets the style of the right line of the border. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value the new style of the right line
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setBorderRightStyle(String value) throws SemanticException;

	/**
	 * Returns the style of the top line of the border. The return value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @return the style of the top line
	 */

	String getBorderTopStyle();

	/**
	 * Sets the style of the top line of the border. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 * 
	 * @param value the new style of the right line
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setBorderTopStyle(String value) throws SemanticException;

	/**
	 * Tests whether the section can shrink if the actual content is smaller than
	 * the design size.
	 * 
	 * @return <code>true</code> if can shrink, otherwise <code>false</code>
	 * @see #setCanShrink(boolean)
	 */

	boolean canShrink();

	/**
	 * Sets whether the section can shrink if the actual content is smaller than the
	 * design size.
	 * 
	 * @param value <code>true</code> if can shrink, <code>false</code> not.
	 * @throws SemanticException if the property is locked
	 * @see #canShrink()
	 */

	void setCanShrink(boolean value) throws SemanticException;

	/**
	 * Returns the pattern of a string format.
	 * 
	 * @return the pattern of a string format
	 */

	String getStringFormat();

	/**
	 * Sets the pattern of a string format.
	 * 
	 * @param pattern the pattern of a string forma
	 * @throws SemanticException if the property is locked
	 */

	void setStringFormat(String pattern) throws SemanticException;

	/**
	 * Returns the category of a string format.
	 * 
	 * @return the category of a string format
	 */

	String getStringFormatCategory();

	/**
	 * Sets the category of a string format. The <code>pattern</code> can be one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4</code>
	 * <li><code>DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER</code>
	 * <li>
	 * <code>DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER</code>
	 * </ul>
	 * 
	 * @param pattern the category of a string format
	 * @throws SemanticException if <code>pattern</code> is not one of the above
	 *                           values.
	 */

	void setStringFormatCategory(String pattern) throws SemanticException;

	/**
	 * Returns the pattern of a number format for a style.
	 * 
	 * @return the pattern of a number format
	 */

	String getNumberFormat();

	/**
	 * Sets the pattern of a number format.
	 * 
	 * @param pattern the pattern of a number format
	 * @throws SemanticException if the property is locked
	 */

	void setNumberFormat(String pattern) throws SemanticException;

	/**
	 * Returns the category of a number format for a style.
	 * 
	 * @return the category of a number format
	 */

	String getNumberFormatCategory();

	/**
	 * Sets the category of a number format for a highlight rule. The
	 * <code>pattern</code> can be one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD</code>
	 * <li><code>DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM</code>
	 * </ul>
	 * 
	 * @param category the category of a number format
	 * @throws SemanticException if <code>category</code> is not one of the above
	 *                           values.
	 */

	void setNumberFormatCategory(String category) throws SemanticException;

	/**
	 * Returns the pattern of the date-time-format.
	 * 
	 * @return the pattern of the date-time-format
	 */

	String getDateTimeFormat();

	/**
	 * Sets the pattern of a date time format for a highlight rule.
	 * 
	 * @param pattern the pattern of a date time format
	 * @throws SemanticException if the property is locked
	 */

	void setDateTimeFormat(String pattern) throws SemanticException;

	/**
	 * Returns the category of the date-time-format.
	 * 
	 * @return the category of the date-time-format
	 */

	String getDateTimeFormatCategory();

	/**
	 * Sets the category of a number format. The <code>pattern</code> can be one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME</code>
	 * <li><code>DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM</code>
	 * </ul>
	 * 
	 * @param pattern the category of a date-time format
	 * @throws SemanticException if <code>pattern</code> is not one of the above
	 *                           values.
	 */

	void setDateTimeFormatCategory(String pattern) throws SemanticException;

	/**
	 * Returns the value that specifies if a top-level element should be a block or
	 * in-line element. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>DISPLAY_NONE</code>
	 * <li><code>DISPLAY_INLINE</code>
	 * <li><code>DISPLAY_BLOCK</code>
	 * </ul>
	 * 
	 * @return the display value as a string
	 */

	String getDisplay();

	/**
	 * Sets the value that specifies if a top-level element should be a block or
	 * in-line element. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>DISPLAY_NONE</code>
	 * <li><code>DISPLAY_INLINE</code>
	 * <li><code>DISPLAY_BLOCK</code>
	 * </ul>
	 * 
	 * @param value the new display value
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setDisplay(String value) throws SemanticException;

	/**
	 * Returns the name of the master page on which to start this section.
	 * 
	 * @return the master page name
	 * @see #setMasterPage(String)
	 */

	String getMasterPage();

	/**
	 * Sets the master page name on which to start this section. If blank, the
	 * normal page sequence is used. If defined, the section starts on a new page,
	 * and the master page is the one defined here. The subsequent pages are those
	 * defined by the report's page sequence.
	 * 
	 * @param value the new master page name
	 * @throws SemanticException if the property is locked
	 * @see #getMasterPage()
	 */

	void setMasterPage(String value) throws SemanticException;

	/**
	 * Returns the value of orphans. The return value is either an integer as as
	 * string or one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>ORPHANS_INHERIT</code>
	 * </ul>
	 * 
	 * @return the orphans property
	 * @see #setOrphans(String)
	 */

	String getOrphans();

	/**
	 * Sets the orphans property. A orphan occurs if the first line of a multi-line
	 * paragraph appears on its own at the bottom of a page due to a page break. The
	 * input value is either an integer as as string or one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>ORPHANS_INHERIT</code>
	 * </ul>
	 * 
	 * @param value the new orphans property
	 * @throws SemanticException if the value is not an integer or one of the above
	 *                           constants.
	 * @see #getOrphans()
	 */

	void setOrphans(String value) throws SemanticException;

	/**
	 * Returns the page break after property for block-level elements. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @return the page break after property
	 */

	String getPageBreakAfter();

	/**
	 * Sets the page break after property for block-level elements. The input value
	 * is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @param value the new page break after property
	 * @throws SemanticException if the value is not pre-defined.
	 */

	void setPageBreakAfter(String value) throws SemanticException;

	/**
	 * Returns the page break before property for block-level elements. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @return the page break before property
	 */

	String getPageBreakBefore();

	/**
	 * Sets the page break before property for block-level elements. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGE_BREAK_AUTO</code>
	 * <li><code>PAGE_BREAK_ALWAYS</code>
	 * <li><code>PAGE_BREAK_AVOID</code>
	 * <li><code>PAGE_BREAK_LEFT</code>
	 * <li><code>PAGE_BREAK_RIGHT</code>
	 * <li><code>PAGE_BREAK_INHERIT</code>
	 * </ul>
	 * 
	 * @param value the new page break before property
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setPageBreakBefore(String value) throws SemanticException;

	/**
	 * Returns the page break inside property for block-level elements. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGEBREAK_INSIDE_AVOID</code>
	 * <li><code>PAGEBREAK_INSIDE_AUTO</code>
	 * <li><code>PAGEBREAK_INSIDE_INHERIT</code>
	 * </ul>
	 * 
	 * @return the page break inside property
	 */

	String getPageBreakInside();

	/**
	 * Sets the page break inside property for block-level elements. The input value
	 * is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>PAGEBREAK_INSIDE_AVOID</code>
	 * <li><code>PAGEBREAK_INSIDE_AUTO</code>
	 * <li><code>PAGEBREAK_INSIDE_INHERIT</code>
	 * </ul>
	 * 
	 * @param value the new page break inside property
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setPageBreakInside(String value) throws SemanticException;

	/**
	 * Tests whether to show this frame even if it is empty, or all its data
	 * elements are empty. If <code>false</code>, the section is automatically
	 * hidden when empty.
	 * 
	 * @return <code>true</code> if show-if-blank, otherwise <code>false</code>
	 * @see #setShowIfBlank(boolean)
	 */

	boolean getShowIfBlank();

	/**
	 * Sets whether to show this frame even if it is empty, or all its data elements
	 * are empty.
	 * 
	 * @param value <code>true</code> if show the frame. <code>false</code> not.
	 * @throws SemanticException if the property is locked
	 * @see #getShowIfBlank()
	 */

	void setShowIfBlank(boolean value) throws SemanticException;

	/**
	 * Returns one 'text-decoration' property to set underline styles. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 * 
	 * @return the text underline value
	 */

	String getTextUnderline();

	/**
	 * Sets one 'text-decoration' property to set underline styles. The input value
	 * is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_UNDERLINE_NONE</code>
	 * <li><code>TEXT_UNDERLINE_UNDERLINE</code>
	 * </ul>
	 * 
	 * @param value the new text underline
	 * @throws SemanticException if the value is not pre-defined.
	 */

	void setTextUnderline(String value) throws SemanticException;

	/**
	 * Returns one 'text-decoration' property to set overline styles. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 * 
	 * @return the text overline value
	 */

	String getTextOverline();

	/**
	 * Sets one 'text-decoration' property to set overline styles. The input value
	 * is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_OVERLINE_NONE</code>
	 * <li><code>TEXT_OVERLINE_OVERLINE</code>
	 * </ul>
	 * 
	 * @param value the new text overline value
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setTextOverline(String value) throws SemanticException;

	/**
	 * Returns one 'text-decoration' property to set line-through styles. The return
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 * 
	 * @return the text line-through value
	 */

	String getTextLineThrough();

	/**
	 * Sets one 'text-decoration' property to set line-through styles. The input
	 * value is one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_LINE_THROUGH_NONE</code>
	 * <li><code>TEXT_LINE_THROUGH_LINE_THROUGH</code>
	 * </ul>
	 * 
	 * @param value the new text line-through value
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setTextLineThrough(String value) throws SemanticException;

	/**
	 * Returns the text align for block-level elements. The return value is one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * @return the text align value
	 */

	String getTextAlign();

	/**
	 * Sets the text align for block-level elements. The input value is one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * @param value the new text align
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setTextAlign(String value) throws SemanticException;

	/**
	 * Returns the value to transform the text. The return value is one of constants
	 * defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 * 
	 * @return the text transform
	 */

	String getTextTransform();

	/**
	 * Sets the value used to transform the text. The input value is one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TRANSFORM_CAPITALIZE</code>
	 * <li><code>TRANSFORM_UPPERCASE</code>
	 * <li><code>TRANSFORM_LOWERCASE</code>
	 * <li><code>TRANSFORM_NONE</code>
	 * </ul>
	 * 
	 * @param value the new text transform
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setTextTransform(String value) throws SemanticException;

	/**
	 * Returns the value of the vertical align property for inline elements. The
	 * return value is defined in <code>DesignChoiceConstants</code> and can be one
	 * of:
	 * <ul>
	 * <li>VERTICAL_ALIGN_BASELINE
	 * <li>VERTICAL_ALIGN_SUB
	 * <li>VERTICAL_ALIGN_SUPER
	 * <li>VERTICAL_ALIGN_TOP
	 * <li>VERTICAL_ALIGN_TEXT_TOP
	 * <li>VERTICAL_ALIGN_MIDDLE
	 * <li>VERTICAL_ALIGN_BOTTOM
	 * <li>VERTICAL_ALIGN_TEXT_BOTTOM
	 * </ul>
	 * 
	 * @return the value of the vertical align property
	 */

	String getVerticalAlign();

	/**
	 * Sets the value of the vertical align property for inline elements. The input
	 * value is defined in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>VERTICAL_ALIGN_BASELINE
	 * <li>VERTICAL_ALIGN_SUB
	 * <li>VERTICAL_ALIGN_SUPER
	 * <li>VERTICAL_ALIGN_TOP
	 * <li>VERTICAL_ALIGN_TEXT_TOP
	 * <li>VERTICAL_ALIGN_MIDDLE
	 * <li>VERTICAL_ALIGN_BOTTOM
	 * <li>VERTICAL_ALIGN_TEXT_BOTTOM
	 * </ul>
	 * 
	 * @param value the new vertical align
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setVerticalAlign(String value) throws SemanticException;

	/**
	 * Returns the white space for block elements. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>WHITE_SPACE_NORMAL</code>
	 * <li><code>WHITE_SPACE_PRE</code>
	 * <li><code>WHITE_SPACE_NOWRAP</code>
	 * </ul>
	 * 
	 * @return the white space
	 */

	String getWhiteSpace();

	/**
	 * Sets the white space property for block elements. The return value is defined
	 * in <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>WHITE_SPACE_NORMAL</code>
	 * <li><code>WHITE_SPACE_PRE</code>
	 * <li><code>WHITE_SPACE_NOWRAP</code>
	 * </ul>
	 * 
	 * @param value the new white space
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setWhiteSpace(String value) throws SemanticException;

	/**
	 * Returns the value of widows. The return value is either an integer as as
	 * string or one of constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>WIDOWS_INHERIT</code>
	 * </ul>
	 * 
	 * @return the widows property
	 * @see #setWidows(String)
	 */

	String getWidows();

	/**
	 * Sets the widows property. A 'widow' occurs when the last line of a multi-line
	 * paragraph appears on its own at the top of a page due to a page break. The
	 * input value is either an integer as as string or one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>WIDOWS_INHERIT</code>
	 * </ul>
	 * 
	 * @param value the new windows property
	 * @throws SemanticException if the value is not an integer or one of the above
	 *                           constants.
	 * @see #getWidows()
	 */

	void setWidows(String value) throws SemanticException;

	/**
	 * Get the font color.
	 * 
	 * @return color
	 * 
	 */
	String getColor();

	/**
	 * Set the font color.
	 * 
	 * @param color
	 * @throws SemanticException
	 * 
	 */
	void setColor(String color) throws SemanticException;

	/**
	 * Get the background color.
	 * 
	 * @return background color
	 * 
	 */
	String getBackgroundColor();

	/**
	 * Set the background color.
	 * 
	 * @param color
	 * @throws SemanticException
	 * 
	 */
	void setBackgroundColor(String color) throws SemanticException;

	/**
	 * Get the color of the top side of the border.
	 * 
	 * @return color of top border
	 * 
	 */
	String getBorderTopColor();

	/**
	 * Set the color of the top side of the border.
	 * 
	 * @param color
	 * @throws SemanticException
	 * 
	 */
	void setBorderTopColor(String color) throws SemanticException;

	/**
	 * Get the color of the left side of the border.
	 * 
	 * @return color of left side of border
	 * 
	 */
	String getBorderLeftColor();

	/**
	 * Set the color of the left side of the border.
	 * 
	 * @param color
	 * @throws SemanticException
	 * 
	 */
	void setBorderLeftColor(String color) throws SemanticException;

	/**
	 * Get the color of the right side of the border.
	 * 
	 * @return color of right side of border
	 */
	String getBorderRightColor();

	/**
	 * Set the color of the right side of the border.
	 * 
	 * @param color
	 * @throws SemanticException
	 */
	void setBorderRightColor(String color) throws SemanticException;

	/**
	 * Get the color of the bottom side of the border.
	 * 
	 * @return color of bottom side of border
	 */
	String getBorderBottomColor();

	/**
	 * Set the color of the bottom side of the border.
	 * 
	 * @param color
	 * @throws SemanticException
	 */
	void setBorderBottomColor(String color) throws SemanticException;

	/**
	 * Get the x position for the background.
	 * 
	 * @return x position
	 */
	String getBackGroundPositionX();

	/**
	 * Set the x position for the background.
	 * 
	 * @param x
	 * @throws SemanticException
	 */
	void setBackGroundPositionX(String x) throws SemanticException;

	/**
	 * Get the y position for the background.
	 * 
	 * @return y position
	 */
	String getBackGroundPositionY();

	/**
	 * Set the y position for the background.
	 * 
	 * @param y
	 * @throws SemanticException
	 */
	void setBackGroundPositionY(String y) throws SemanticException;

	/**
	 * Get the spacing between individual letters.
	 * 
	 * @return spacing
	 */
	String getLetterSpacing();

	/**
	 * Set the spacing between individual letters.
	 * 
	 * @param spacing
	 * @throws SemanticException
	 */
	void setLetterSpacing(String spacing) throws SemanticException;

	/**
	 * Get the height of a line. Implies spacing between lines.
	 * 
	 * @return height of a line
	 */
	String getLineHeight();

	/**
	 * Set the height of a line. Implies spacing between lines.
	 * 
	 * @param height
	 * @throws SemanticException
	 */
	void setLineHeight(String height) throws SemanticException;

	/**
	 * Get the text indent.
	 * 
	 * @return text indent
	 */
	String getTextIndent();

	/**
	 * Set the text indent.
	 * 
	 * @param indent
	 * @throws SemanticException
	 */
	void setTextIndent(String indent) throws SemanticException;

	/**
	 * Get the spacing between two words.
	 * 
	 * @return spacing
	 */
	String getWordSpacing();

	/**
	 * Set the spacing between two words.
	 * 
	 * @param spacing
	 * @throws SemanticException
	 */
	void setWordSpacing(String spacing) throws SemanticException;

	/**
	 * Get the width of the top side of the border.
	 * 
	 * @return width of top side of border
	 */
	String getBorderTopWidth();

	/**
	 * Set the width of the top side of the border.
	 * 
	 * @param width
	 * @throws SemanticException
	 */
	void setBorderTopWidth(String width) throws SemanticException;

	/**
	 * Get the width of left side of the border.
	 * 
	 * @return width of left side of border
	 */
	String getBorderLeftWidth();

	/**
	 * Set the width of left side of the border.
	 * 
	 * @param width
	 * @throws SemanticException
	 */
	void setBorderLeftWidth(String width) throws SemanticException;

	/**
	 * Get the width of right side of the border.
	 * 
	 * @return width of right side of border
	 */
	String getBorderRightWidth();

	/**
	 * Set the width of the right side of the border.
	 * 
	 * @param width
	 * @throws SemanticException
	 */
	void setBorderRightWidth(String width) throws SemanticException;

	/**
	 * Get the width of the bottom side of the border.
	 * 
	 * @return width of bottom side of border
	 */
	String getBorderBottomWidth();

	/**
	 * Set the width of the bottom side of the border.
	 * 
	 * @param width
	 * @throws SemanticException
	 */
	void setBorderBottomWidth(String width) throws SemanticException;

	/**
	 * Get the margin of the top side.
	 * 
	 * @return margin of top side.
	 */
	String getMarginTop();

	/**
	 * Set the margin of the top side.
	 * 
	 * @param margin
	 * @throws SemanticException
	 */
	void setMarginTop(String margin) throws SemanticException;

	/**
	 * Get the margin of the right side.
	 * 
	 * @return margin of right side
	 */
	String getMarginRight();

	/**
	 * Set the margin of the right side.
	 * 
	 * @param margin
	 * @throws SemanticException
	 */
	void setMarginRight(String margin) throws SemanticException;

	/**
	 * Get the margin of the left side.
	 * 
	 * @return margin of left side
	 */
	String getMarginLeft();

	/**
	 * Set the margin of the left side.
	 * 
	 * @param margin
	 * @throws SemanticException
	 */
	void setMarginLeft(String margin) throws SemanticException;

	/**
	 * Get the margin of the bottom side.
	 * 
	 * @return margin of bottom side
	 */
	String getMarginBottom();

	/**
	 * Set the margin of the bottom side.
	 * 
	 * @param margin
	 * @throws SemanticException
	 */
	void setMarginBottom(String margin) throws SemanticException;

	/**
	 * Get the padding of the top side.
	 * 
	 * @return padding of top side
	 */
	String getPaddingTop();

	/**
	 * Set the padding of the top side.
	 * 
	 * @param padding
	 * @throws SemanticException
	 */
	void setPaddingTop(String padding) throws SemanticException;

	/**
	 * Get the padding of the right side.
	 * 
	 * @return padding of right side
	 */
	String getPaddingRight();

	/**
	 * Set the padding of the right side.
	 * 
	 * @param padding
	 * @throws SemanticException
	 */
	void setPaddingRight(String padding) throws SemanticException;

	/**
	 * Get the padding of the left side.
	 * 
	 * @return padding of left side
	 */
	String getPaddingLeft();

	/**
	 * Set the padding of the left side.
	 * 
	 * @param padding
	 * @throws SemanticException
	 */
	void setPaddingLeft(String padding) throws SemanticException;

	/**
	 * Get the padding of the bottom side.
	 * 
	 * @return padding of bottom side
	 */
	String getPaddingBottom();

	/**
	 * Set the padding of the bottom side.
	 * 
	 * @param padding
	 * @throws SemanticException
	 */
	void setPaddingBottom(String padding) throws SemanticException;

	/**
	 * Get the font size.
	 * 
	 * @return font size
	 */
	String getFontSize();

	/**
	 * Set the font size.
	 * 
	 * @param fontSize
	 * @throws SemanticException
	 */
	void setFontSize(String fontSize) throws SemanticException;

	/**
	 * Get the font family.
	 * 
	 * @return font family
	 */
	String getFontFamily();

	/**
	 * Set the font family.
	 * 
	 * @param fontFamily
	 * @throws SemanticException
	 */
	void setFontFamily(String fontFamily) throws SemanticException;

	/**
	 * Get the weight of the font.
	 * 
	 * @return weight of font
	 */
	String getFontWeight();

	/**
	 * Sets the weight of the font. The input value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_WEIGHT_NORMAL</code>
	 * <li><code>FONT_WEIGHT_BOLD</code>
	 * <li><code>FONT_WEIGHT_BOLDER</code>
	 * <li><code>FONT_WEIGHT_LIGHTER</code>
	 * <li><code>FONT_WEIGHT_100</code>
	 * <li><code>FONT_WEIGHT_200</code>
	 * <li><code>FONT_WEIGHT_300</code>
	 * <li><code>FONT_WEIGHT_400</code>
	 * <li><code>FONT_WEIGHT_500</code>
	 * <li><code>FONT_WEIGHT_600</code>
	 * <li><code>FONT_WEIGHT_700</code>
	 * <li><code>FONT_WEIGHT_800</code>
	 * <li><code>FONT_WEIGHT_900</code>
	 * </ul>
	 * 
	 * @param fontWeight the new font weight
	 * @throws SemanticException if the input value is not one of the above.
	 */

	void setFontWeight(String fontWeight) throws SemanticException;

	/**
	 * Returns the variant of the font. The return value is one of constants defined
	 * in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 * 
	 * @return the font variant in a string.
	 */
	String getFontVariant();

	/**
	 * Sets the variant of the font. The input value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_VARIANT_NORMAL</code>
	 * <li><code>FONT_VARIANT_SMALL_CAPS</code>
	 * </ul>
	 * 
	 * @param fontVariant the new font variant.
	 * @throws SemanticException if the input value is not one of the above.
	 */
	void setFontVariant(String fontVariant) throws SemanticException;

	/**
	 * Returns the style of the font. The return value is one of constants defined
	 * in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 * 
	 * @return the font style in string.
	 */
	String getFontStyle();

	/**
	 * Sets the style of the font. The input value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>FONT_STYLE_NORMAL</code>
	 * <li><code>FONT_STYLE_ITALIC</code>
	 * <li><code>FONT_STYLE_OBLIQUE</code>
	 * </ul>
	 * 
	 * @param fontStyle the new font style.
	 * @throws SemanticException if the input value is not one of the above.
	 */
	void setFontStyle(String fontStyle) throws SemanticException;

	/**
	 * Returns the value of direction property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_DIRECTION_LTR</code>
	 * <li><code>BIDI_DIRECTION_RTL</code>
	 * </ul>
	 * 
	 * @return the value of direction property
	 */

	public String getTextDirection();

	/**
	 * Sets the Bidi direction property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>BIDI_DIRECTION_LTR</code>
	 * <li><code>BIDI_DIRECTION_RTL</code>
	 * </ul>
	 * 
	 * @param value the new direction value
	 * @throws SemanticException if the value is not one of the above.
	 * 
	 */

	public void setTextDirection(String value) throws SemanticException;

	/**
	 * Returns the value of overflow property. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>OVERFLOW_AUTO</code>
	 * <li><code>OVERFLOW_HIDDEN</code>
	 * <li><code>OVERFLOW_SCROLL</code>
	 * <li><code>OVERFLOW_VISIBLE</code>
	 * </ul>
	 * 
	 * @return the value of overflow property.
	 */
	public String getOverflow();

	/**
	 * Sets the value of overflow property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>OVERFLOW_AUTO</code>
	 * <li><code>OVERFLOW_HIDDEN</code>
	 * <li><code>OVERFLOW_SCROLL</code>
	 * <li><code>OVERFLOW_VISIBLE</code>
	 * </ul>
	 * 
	 * @param value the new overflow value
	 * @throws SemanticException
	 */
	public void setOverflow(String value) throws SemanticException;

	/**
	 * Returns the value of height property.
	 * 
	 * @return the value of height property.
	 */
	public String getHeight();

	/**
	 * Sets the value of height property.
	 * 
	 * @param value the new height value
	 * @throws SemanticException
	 */
	public void setHeight(String height) throws SemanticException;

	/**
	 * Returns the value of width property.
	 * 
	 * @return the value of width property.
	 */
	public String getWidth();

	/**
	 * Sets the value of width property.
	 * 
	 * @param value the new width value
	 * @throws SemanticException
	 */
	public void setWidth(String width) throws SemanticException;
}
