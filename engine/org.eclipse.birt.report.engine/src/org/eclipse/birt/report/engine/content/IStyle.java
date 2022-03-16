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

	String getBackgroundPositionX();

	String getBackgroundPositionY();

	String getTextUnderline();

	String getTextOverline();

	String getTextLineThrough();

	String getMasterPage();

	String getShowIfBlank();

	String getCanShrink();

	String getStringFormat();

	String getNumberFormat();

	String getDateFormat();

	String getDateTimeFormat();

	String getTimeFormat();

	String getNumberAlign();

	String getVisibleFormat();

	void setBackgroundPositionX(String x) throws DOMException;

	void setBackgroundPositionY(String y) throws DOMException;

	void setTextUnderline(String underline) throws DOMException;

	void setTextOverline(String overline) throws DOMException;

	void setTextLineThrough(String through) throws DOMException;

	void setMasterPage(String page) throws DOMException;

	void setShowIfBlank(String blank) throws DOMException;

	void setCanShrink(String shrink) throws DOMException;

	void setStringFormat(String format) throws DOMException;

	void setNumberFormat(String format) throws DOMException;

	void setDateFormat(String format) throws DOMException;

	void setNumberAlign(String align) throws DOMException;

	void setVisibleFormat(String visibility) throws DOMException;

	CSSValue getProperty(int index);

	void setProperty(int index, CSSValue value);

	void setProperties(IStyle style);

	String getCssText(int index);

	void setCssText(int index, String value) throws DOMException;

	boolean isEmpty();

	void write(DataOutputStream out) throws IOException;

	void read(DataInputStream in) throws IOException;

	String getBackgroundHeight();

	String getBackgroundWidth();

	DataFormatValue getDataFormat();

	void setDataFormat(DataFormatValue value);
}
