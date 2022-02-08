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

package org.eclipse.birt.chart.script.api.component;

import org.eclipse.birt.chart.script.api.data.IDataElement;

/**
 * Represents the Marker Line of Axis in the scripting environment
 */

public interface IMarkerLine extends IChartComponent {

	/**
	 * Gets the value for defining where the line will be positioned
	 * 
	 * @return value
	 */
	IDataElement getValue();

	/**
	 * Sets the value for defining where the line will be positioned
	 * 
	 * @param value value
	 */
	void setValue(IDataElement value);
}
