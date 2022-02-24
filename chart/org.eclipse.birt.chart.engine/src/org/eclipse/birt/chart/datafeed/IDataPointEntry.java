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

package org.eclipse.birt.chart.datafeed;

import org.eclipse.birt.chart.model.attribute.FormatSpecifier;

import com.ibm.icu.util.ULocale;

/**
 * The entry is a data point that is usually made up of several data. It is
 * responsible for formatting the entry according to the formatter and locale.
 */

public interface IDataPointEntry {

	/**
	 * Returns the formatted string representation of current object by given
	 * formatter and locale.
	 * 
	 * @param formatter An formatter
	 * @param locale    Specific locale.
	 * @return The string representation
	 */
	String getFormattedString(FormatSpecifier formatter, ULocale locale);

	/**
	 * Returns the formatted string representation of current object by given
	 * formatter and locale.
	 * 
	 * @param type      data point type
	 * @param formatter An formatter
	 * @param locale    Specific locale.
	 * @return The string representation
	 * @see #getDataPointTypes()
	 */
	String getFormattedString(String type, FormatSpecifier formatter, ULocale locale);

	/**
	 * Returns if the DataPointEntry is valid.
	 * 
	 * @return value or not
	 * @since 2.5.0
	 */
	boolean isValid();
}
