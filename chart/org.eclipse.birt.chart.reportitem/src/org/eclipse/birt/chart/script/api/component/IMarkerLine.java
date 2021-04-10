/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
