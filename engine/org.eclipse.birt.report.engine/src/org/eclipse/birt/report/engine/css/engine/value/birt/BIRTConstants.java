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
package org.eclipse.birt.report.engine.css.engine.value.birt;

import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;

/**
 * Define BIRT constants, such as tag names, attribute names and URI
 *
 */
public interface BIRTConstants extends CSSConstants {

	/////////////////////////////////////////////////////////////////////////
	// BIRT attributes
	/////////////////////////////////////////////////////////////////////////
	/**
	 * BIRT property: background image type
	 */
	String BIRT_BACKGROUND_IMAGE_TYPE = "background-image-type";
	/**
	 * BIRT property: background position X
	 */
	String BIRT_BACKGROUND_POSITION_X_PROPERTY = "background-position-x";
	/**
	 * BIRT property: background position Y
	 */
	String BIRT_BACKGROUND_POSITION_Y_PROPERTY = "background-position-y";
	/**
	 * BIRT property: can shrink
	 */
	String BIRT_CAN_SHRINK_PROPERTY = "can-shrink"; //$NON-NLS-1$
	/**
	 * BIRT property: master-page
	 */
	String BIRT_MASTER_PAGE_PROPERTY = "master-page"; //$NON-NLS-1$
	/**
	 * BIRT property: number-align
	 */
	String BIRT_NUMBER_ALIGN_PROPERTY = "number-align";
	/**
	 * BIRT property: show-if-blank
	 */
	String BIRT_SHOW_IF_BLANK_PROPERTY = "show-if-blank"; //$NON-NLS-1$
	/**
	 * BIRT property: text underline
	 */
	String BIRT_TEXT_UNDERLINE_PROPERTY = "text-underline"; //$NON-NLS-1$
	/**
	 * BIRT property: text overline
	 */
	String BIRT_TEXT_OVERLINE_PROPERTY = "text-overline"; //$NON-NLS-1$
	/**
	 * BIRT property: text linethrough
	 */
	String BIRT_TEXT_LINETHROUGH_PROPERTY = "text-linethrough"; //$NON-NLS-1$
	/**
	 * BIRT property: visible format
	 */
	String BIRT_VISIBLE_FORMAT_PROPERTY = "visible-format";
	/**
	 * BIRT property: data format
	 */
	String BIRT_STYLE_DATA_FORMAT = "data-format";

	/**
	 * BIRT property: border-diagonal-number
	 */
	String BIRT_BORDER_DIAGONAL_NUMBER = "border-diagonal-number";
	/**
	 * BIRT property: border-diagonal-style
	 */
	String BIRT_BORDER_DIAGONAL_STYLE = "border-diagonal-style";
	/**
	 * BIRT property: border-diagonal-width
	 */
	String BIRT_BORDER_DIAGONAL_WIDTH = "border-diagonal-width";
	/**
	 * BIRT property: border-diagonal-color
	 */
	String BIRT_BORDER_DIAGONAL_COLOR = "border-diagonal-color";

	/**
	 * BIRT property: border-antidiagonal-number
	 */
	String BIRT_BORDER_ANTIDIAGONAL_NUMBER = "border-antidiagonal-number";
	/**
	 * BIRT property: border-antidiagonal-style
	 */
	String BIRT_BORDER_ANTIDIAGONAL_STYLE = "border-antidiagonal-style";
	/**
	 * BIRT property: border-antidiagonal-width
	 */
	String BIRT_BORDER_ANTIDIAGONAL_WIDTH = "border-antidiagonal-width";
	/**
	 * BIRT property: border-antidiagonal-color
	 */
	String BIRT_BORDER_ANTIDIAGONAL_COLOR = "border-antidiagonal-color";

	///////////////////////////////////////////////////////////////////////
	// Deprecated format constants, they are replaced by data-format
	//////////////////////////////////////////////////////////////////////
	/** BIRT property: date format */
	String BIRT_DATE_TIME_FORMAT_PROPERTY = "date-format"; //$NON-NLS-1$
	/** BIRT property: sql time format */
	String BIRT_TIME_FORMAT_PROPERTY = "sql-time-format"; //$NON-NLS-1$
	/** BIRT property: sql date format */
	String BIRT_DATE_FORMAT_PROPERTY = "sql-date-format"; //$NON-NLS-1$
	/** BIRT property: number format */
	String BIRT_NUMBER_FORMAT_PROPERTY = "number-format"; //$NON-NLS-1$
	/** BIRT property: string format */
	String BIRT_STRING_FORMAT_PROPERTY = "string-format"; //$NON-NLS-1$

	/////////////////////////////////////////////////////////////////////////
	// BIRT attribute value
	/////////////////////////////////////////////////////////////////////////

	/**
	 * BIRT attribute value: true
	 */
	String BIRT_TRUE_VALUE = "true";
	/**
	 * BIRT attribute value: false
	 */
	String BIRT_FALSE_VALUE = "false";
	/**
	 * BIRT attribute value: all
	 */
	String BIRT_ALL_VALUE = "all";
	/**
	 * BIRT attribute value: soft
	 */
	String BIRT_SOFT_VALUE = "soft";
}
