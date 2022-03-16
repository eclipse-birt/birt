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

package org.eclipse.birt.chart.script.api.series;

/**
 * Represents Bar series of a Chart in the scripting environment
 */

public interface IBar extends IStackableSeries {

	/**
	 * Gets the name of RiserType
	 *
	 * @return name of RiserType
	 */
	String getBarType();

	/**
	 * Sets the RiserType
	 *
	 * @param type name of RiserType
	 */
	void setBarType(String type);
}
