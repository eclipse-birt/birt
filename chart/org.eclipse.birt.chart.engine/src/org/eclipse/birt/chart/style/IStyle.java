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

package org.eclipse.birt.chart.style;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;

/**
 * This interface defines the general style elements supported by chart.
 */
public interface IStyle {

	/**
	 * Returns the font of current style.
	 * 
	 * @return FontDefinition
	 */
	FontDefinition getFont();

	/**
	 * Returns the color of current style.
	 * 
	 * @return ColorDefinition
	 */
	ColorDefinition getColor();

	/**
	 * Returns the background color of current style.
	 * 
	 * @return ColorDefinition
	 */
	ColorDefinition getBackgroundColor();

	/**
	 * Returns the background image of current style.
	 * 
	 * @return Image
	 */
	Image getBackgroundImage();

	/**
	 * Returns the padding of current style.
	 * 
	 * @return Insets
	 */
	Insets getPadding();

	/**
	 * Returns the date time format of current style.
	 * 
	 * @return date time format
	 */
	FormatSpecifier getDateTimeFormat();

	/**
	 * Returns the number format of current style.
	 * 
	 * @return number format
	 */
	FormatSpecifier getNumberFormat();

	/**
	 * Returns the string format of current style.
	 * 
	 * @return string format
	 */
	FormatSpecifier getStringFormat();
}
