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

	public String getBackgroundPositionX();

	public String getBackgroundPositionY();

	public String getTextUnderline();

	public String getTextOverline();

	public String getTextLineThrough();

	public String getMasterPage();

	public String getShowIfBlank();

	public String getCanShrink();

	public String getStringFormat();

	public String getNumberFormat();

	public String getDateFormat();

	public String getDateTimeFormat();

	public String getTimeFormat();

	public String getNumberAlign();

	public String getVisibleFormat();

	public void setBackgroundPositionX(String x) throws DOMException;

	public void setBackgroundPositionY(String y) throws DOMException;

	public void setTextUnderline(String underline) throws DOMException;

	public void setTextOverline(String overline) throws DOMException;

	public void setTextLineThrough(String through) throws DOMException;

	public void setMasterPage(String page) throws DOMException;

	public void setShowIfBlank(String blank) throws DOMException;

	public void setCanShrink(String shrink) throws DOMException;

	public void setStringFormat(String format) throws DOMException;

	public void setNumberFormat(String format) throws DOMException;

	public void setDateFormat(String format) throws DOMException;

	public void setNumberAlign(String align) throws DOMException;

	public void setVisibleFormat(String visibility) throws DOMException;

	public CSSValue getProperty(int index);

	public void setProperty(int index, CSSValue value);

	public void setProperties(IStyle style);

	public String getCssText(int index);

	public void setCssText(int index, String value) throws DOMException;

	boolean isEmpty();

	void write(DataOutputStream out) throws IOException;

	void read(DataInputStream in) throws IOException;

	public String getBackgroundHeight();

	public String getBackgroundWidth();

	public DataFormatValue getDataFormat();

	public void setDataFormat(DataFormatValue value);
}
