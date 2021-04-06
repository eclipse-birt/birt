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
