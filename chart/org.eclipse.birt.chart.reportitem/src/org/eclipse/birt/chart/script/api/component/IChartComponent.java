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

import org.eclipse.birt.chart.script.api.attribute.ILabel;

/**
 * Represents an abstract component in the scripting environment. A component
 * can be Axis, Series, Legend, and etc.
 */

public interface IChartComponent {

	/**
	 * Checks if current component is visible
	 *
	 * @return visible or not
	 */
	boolean isVisible();

	/**
	 * Sets if current component is visible
	 *
	 * @param visible
	 */
	void setVisible(boolean visible);

	/**
	 * Gets the title of component
	 *
	 * @return title string
	 */
	ILabel getTitle();

}
