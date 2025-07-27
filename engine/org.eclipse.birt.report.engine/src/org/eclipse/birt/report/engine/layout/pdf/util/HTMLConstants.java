/*******************************************************************************
 * Copyright (c) 2021, 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

/**
 * HTML constants
 *
 * @since 3.3
 *
 */
public interface HTMLConstants {

	// html tag
	/** property, html-tag: italic */
	String TAG_I = "i";

	/** property, html-tag: font */
	String TAG_FONT = "font";

	/** property, html-tag: bold */
	String TAG_B = "b";

	/** property, html-tag: link */
	String TAG_A = "a";

	/** property, html-tag: code */
	String TAG_CODE = "code";

	/** property, html-tag: em */
	String TAG_EM = "em";

	/** property, html-tag: object */
	String TAG_OBJECT = "object";

	/** property, html-tag: image */
	String TAG_IMG = "img";

	/** property, html-tag: ins */
	String TAG_INS = "ins";

	/** property, html-tag: span */
	String TAG_SPAN = "span";

	/** property, html-tag: strong */
	String TAG_STRONG = "strong";

	/** property, html-tag: sub */
	String TAG_SUB = "sub";

	/** property, html-tag: sup */
	String TAG_SUP = "sup";

	/** property, html-tag: teletype text */
	String TAG_TT = "tt";

	/** property, html-tag: underline */
	String TAG_U = "u";

	/** property, html-tag: delete */
	String TAG_DEL = "del";

	/** property, html-tag: strike */
	String TAG_STRIKE = "strike";

	/** property, html-tag: invalid text */
	String TAG_S = "s";

	/** property, html-tag: big */
	String TAG_BIG = "big";

	/** property, html-tag: small */
	String TAG_SMALL = "small";

	/** property, html-tag: dd */
	String TAG_DD = "dd"; //$NON-NLS-1$

	/** property, html-tag: div */
	String TAG_DIV = "div"; //$NON-NLS-1$

	/** property, html-tag: dl */
	String TAG_DL = "dl"; //$NON-NLS-1$

	/** property, html-tag: dt */
	String TAG_DT = "dt"; //$NON-NLS-1$

	/** property, html-tag: h1 */
	String TAG_H1 = "h1"; //$NON-NLS-1$

	/** property, html-tag: h2 */
	String TAG_H2 = "h2"; //$NON-NLS-1$

	/** property, html-tag: h3 */
	String TAG_H3 = "h3"; //$NON-NLS-1$

	/** property, html-tag: h4 */
	String TAG_H4 = "h4"; //$NON-NLS-1$

	/** property, html-tag: h5 */
	String TAG_H5 = "h5"; //$NON-NLS-1$

	/** property, html-tag: h6 */
	String TAG_H6 = "h6"; //$NON-NLS-1$

	/** property, html-tag: hr */
	String TAG_HR = "hr"; //$NON-NLS-1$

	/** property, html-tag: ol */
	String TAG_OL = "ol"; //$NON-NLS-1$

	/** property, html-tag: p */
	String TAG_P = "p"; //$NON-NLS-1$

	/** property, html-tag: pre */
	String TAG_PRE = "pre"; //$NON-NLS-1$

	/** property, html-tag: ul */
	String TAG_UL = "ul"; //$NON-NLS-1$

	/** property, html-tag: li */
	String TAG_LI = "li"; //$NON-NLS-1$

	/** property, html-tag: address */
	String TAG_ADDRESS = "address"; //$NON-NLS-1$

	/** property, html-tag: body */
	String TAG_BODY = "body"; //$NON-NLS-1$

	/** property, html-tag: center */
	String TAG_CENTER = "center"; //$NON-NLS-1$

	/** property, html-tag: table */
	String TAG_TABLE = "table"; //$NON-NLS-1$

	/** property, html-tag: td */
	String TAG_TD = "td"; //$NON-NLS-1$

	/** property, html-tag: tr */
	String TAG_TR = "tr"; //$NON-NLS-1$

	/** property, html-tag: th */
	String TAG_TH = "th"; //$NON-NLS-1$

	/** property, html-tag: col */
	String TAG_COL = "col"; //$NON-NLS-1$

	/** property, html-tag: br */
	String TAG_BR = "br";//$NON-NLS-1$

	/** property, html-tag: value-of */
	String TAG_VALUEOF = "value-of";//$NON-NLS-1$

	/** property, html-tag: image */
	String TAG_IMAGE = "image";//$NON-NLS-1$

	/** property, html-tag: script */
	String TAG_SCRIPT = "script";//$NON-NLS-1$

	/** property, property: color */
	String PROPERTY_COLOR = "color";//$NON-NLS-1$

	/** property, property: face */
	String PROPERTY_FACE = "face";//$NON-NLS-1$

	/** property, property: size */
	String PROPERTY_SIZE = "size";//$NON-NLS-1$

	/** property, property: align */
	String PROPERTY_ALIGN = "align";//$NON-NLS-1$

	/** property, property: border */
	String PROPERTY_BORDER = "border";//$NON-NLS-1$

	/** property, property: width */
	String PROPERTY_WIDTH = "width";//$NON-NLS-1$

	/** property, property: height */
	String PROPERTY_HEIGHT = "height";//$NON-NLS-1$

	/** property, property: alt */
	String PROPERTY_ALT = "alt";//$NON-NLS-1$

	/** property, property: src */
	String PROPERTY_SRC = "src";//$NON-NLS-1$

	/** property, property: start */
	String PROPERTY_OL_START = "start";//$NON-NLS-1$

	/** property, property: bgcolor */
	String PROPERTY_BGCOLOR = "bgcolor";//$NON-NLS-1$

	/** property, property: background */
	String PROPERTY_BACKGROUND = "background";//$NON-NLS-1$

	/** property, property: text */
	String PROPERTY_TEXT = "text";//$NON-NLS-1$

	/** property, property: rowspan */
	String PROPERTY_ROWSPAN = "rowspan";//$NON-NLS-1$

	/** property, property: cellpadding */
	String PROPERTY_CELLPADDING = "cellpadding";//$NON-NLS-1$

	/** property, property: colspan */
	String PROPERTY_COLSPAN = "colspan";//$NON-NLS-1$

	/** property, property: valign */
	String PROPERTY_VALIGN = "valign";//$NON-NLS-1$

	/** property, property: id */
	String PROPERTY_ID = "id";//$NON-NLS-1$

	/** property, property: name */
	String PROPERTY_NAME = "name";//$NON-NLS-1$

	/** property, property: href */
	String PROPERTY_HREF = "href";//$NON-NLS-1$

	/** property, property: target */
	String PROPERTY_TARGET = "target";//$NON-NLS-1$

	/** property, property: classid */
	String PROPERTY_CLASSID = "classid";//$NON-NLS-1$

	/** property, property: param */
	String PROPERTY_PARAM = "param";//$NON-NLS-1$

	/** property, property: value */
	String PROPERTY_VALUE = "value";//$NON-NLS-1$
}
