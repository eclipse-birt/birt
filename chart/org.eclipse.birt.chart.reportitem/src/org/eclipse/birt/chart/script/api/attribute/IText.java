/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.script.api.attribute;

import org.eclipse.birt.report.model.api.extension.IColor;
import org.eclipse.birt.report.model.api.extension.IFont;

/**
 * Represents Text object in a Chart in the scripting environment
 */

public interface IText {

	/**
	 * Gets the string value of Text
	 *
	 * @return string value
	 */
	String getValue();

	/**
	 * Sets the string value of Text
	 *
	 * @param value string value
	 */
	void setValue(String value);

	/**
	 * Gets the Font of Text
	 *
	 * @return Font
	 */
	IFont getFont();

	/**
	 * Sets the Font of Text
	 *
	 * @param font Font
	 */
	void setFont(IFont font);

	/**
	 * Gets the Color of Text
	 *
	 * @return Color of Text
	 */
	IColor getColor();

	/**
	 * Sets the Color of Text
	 *
	 * @param color Color of Text
	 */
	void setColor(IColor color);

}
