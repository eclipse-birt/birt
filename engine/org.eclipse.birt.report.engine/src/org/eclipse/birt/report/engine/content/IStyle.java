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

package org.eclipse.birt.report.engine.content;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTValueConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSS2Properties;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

/**
 * Provides the interfaces for the ROM style
 *
 * the properties must be string as the user may change them in script.
 *
 */
public interface IStyle extends CSSStyleDeclaration, StyleConstants, CSS2Properties, CSSConstants, CSSValueConstants,
		BIRTConstants, BIRTValueConstants {

	/**
	 * Get the background position X
	 *
	 * @return Return the background position X
	 */
	String getBackgroundPositionX();

	/**
	 * Get the background position Y
	 *
	 * @return Return the background position Y
	 */
	String getBackgroundPositionY();

	/**
	 * Get the text underline
	 *
	 * @return Return the text underline
	 */
	String getTextUnderline();

	/**
	 * Get the text overline
	 *
	 * @return Return the text overline
	 */
	String getTextOverline();

	/**
	 * Get the text line through
	 *
	 * @return Return the text line through
	 */
	String getTextLineThrough();

	/**
	 * Get the master page
	 *
	 * @return Return the master page
	 */
	String getMasterPage();

	/**
	 * Get the option show if blank
	 *
	 * @return Return the option show if blank
	 */
	String getShowIfBlank();

	/**
	 * Get the option can shrink
	 *
	 * @return Return the option can shrink
	 */
	String getCanShrink();

	/**
	 * Get the string format
	 *
	 * @return Return the string format
	 */
	String getStringFormat();

	/**
	 * Get the number format
	 *
	 * @return Return the number format
	 */
	String getNumberFormat();

	/**
	 * Get the date format
	 *
	 * @return Return the date format
	 */
	String getDateFormat();

	/**
	 * Get the date time format
	 *
	 * @return Return the date time format
	 */
	String getDateTimeFormat();

	/**
	 * Get the time format
	 *
	 * @return Return the time format
	 */
	String getTimeFormat();

	/**
	 * Get the number align
	 *
	 * @return Return the number align
	 */
	String getNumberAlign();

	/**
	 * Get the visible format
	 *
	 * @return Return the visible format
	 */
	String getVisibleFormat();

	/**
	 * Set the background position X
	 *
	 * @param x
	 * @throws DOMException
	 */
	void setBackgroundPositionX(String x) throws DOMException;

	/**
	 * Set the background position Y
	 *
	 * @param y
	 * @throws DOMException
	 */
	void setBackgroundPositionY(String y) throws DOMException;

	/**
	 * Set the text underline
	 *
	 * @param underline
	 * @throws DOMException
	 */
	void setTextUnderline(String underline) throws DOMException;

	/**
	 * Set the text overline
	 *
	 * @param overline
	 * @throws DOMException
	 */
	void setTextOverline(String overline) throws DOMException;

	/**
	 * Set the text line through
	 *
	 * @param through
	 * @throws DOMException
	 */
	void setTextLineThrough(String through) throws DOMException;

	/**
	 * Set the master page
	 *
	 * @param page
	 * @throws DOMException
	 */
	void setMasterPage(String page) throws DOMException;

	/**
	 * Set the show if blank
	 *
	 * @param blank
	 * @throws DOMException
	 */
	void setShowIfBlank(String blank) throws DOMException;

	/**
	 * Set the option can shrink
	 *
	 * @param shrink
	 * @throws DOMException
	 */
	void setCanShrink(String shrink) throws DOMException;

	/**
	 * Set the string format
	 *
	 * @param format
	 * @throws DOMException
	 */
	void setStringFormat(String format) throws DOMException;

	/**
	 * Set the number format
	 *
	 * @param format
	 * @throws DOMException
	 */
	void setNumberFormat(String format) throws DOMException;

	/**
	 * Set the date format
	 *
	 * @param format
	 * @throws DOMException
	 */
	void setDateFormat(String format) throws DOMException;

	/**
	 * Set the number align
	 *
	 * @param align
	 * @throws DOMException
	 */
	void setNumberAlign(String align) throws DOMException;

	/**
	 * Set visible format
	 *
	 * @param visibility
	 * @throws DOMException
	 */
	void setVisibleFormat(String visibility) throws DOMException;

	/**
	 * Get the property value
	 *
	 * @param index
	 * @return Return the property value
	 */
	CSSValue getProperty(int index);

	/**
	 * Set the property value
	 *
	 * @param index
	 * @param value
	 */
	void setProperty(int index, CSSValue value);

	/**
	 * Set the properties style based
	 *
	 * @param style
	 */
	void setProperties(IStyle style);

	/**
	 * Get the CSS text
	 *
	 * @param index
	 * @return Return the CSS text
	 */
	String getCssText(int index);

	/**
	 * Set the CSS text
	 *
	 * @param index
	 * @param value
	 * @throws DOMException
	 */
	void setCssText(int index, String value) throws DOMException;

	/**
	 * Is empty
	 *
	 * @return true, is empty
	 */
	boolean isEmpty();

	/**
	 * Write the report ROM stream
	 *
	 * @param out
	 * @throws IOException
	 */
	void write(DataOutputStream out) throws IOException;

	/**
	 * Read the report ROM stream
	 *
	 * @param in
	 * @throws IOException
	 */
	void read(DataInputStream in) throws IOException;

	/**
	 * Get the background image height
	 *
	 * @return Return the background image height
	 */
	String getBackgroundHeight();

	/**
	 * Get the background image width
	 *
	 * @return Return the background image width
	 */
	String getBackgroundWidth();

	/**
	 * Get the date format
	 *
	 * @return Return the date format
	 */
	DataFormatValue getDataFormat();

	/**
	 * Set the date format
	 *
	 * @param value
	 */
	void setDataFormat(DataFormatValue value);

	/**
	 * Get the background image source type
	 *
	 * @return Return the background image source type
	 */
	String getBackgroundImageType();

	/**
	 * Set the image background type
	 *
	 * @param imageType
	 */
	void setBackgroundImageType(String imageType);
}
