/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.factory;

import com.ibm.icu.util.ULocale;

/**
 * Provides services for externalization of static text messages rendered in a
 * chart. The chart title and axis titles are presently externalizable. If chart
 * engine runs within BIRT, ChartReportItemImpl has implemented this interface
 * by default, which reuses the model's externalization mechanism, and will
 * externalize the text with the properties file of the report design or the
 * library. Otherwise the user should implement it.
 */
public interface IExternalizer {
	/**
	 * Defines a separator for a fully externalized message reference containing a
	 * key on the LHS and a value on the RHS separated by the key separator.
	 */
	char KEY_SEPARATOR = '=';

	String externalizedMessage(String sKey, String sDefaultValue, ULocale locale);
}
