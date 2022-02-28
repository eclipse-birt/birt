/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.StyledComponent;

/**
 * This interface allows access/manipulation to styles for granular chart
 * components.
 */
public interface IStyleProcessor {

	/**
	 * Returns the style as per given component name.
	 *
	 * @param name
	 * @return style element
	 */
	IStyle getStyle(Chart model, StyledComponent name);

	/**
	 * Process styles for the whole chart model
	 *
	 * @param model chart model
	 * @since 2.6
	 */
	void processStyle(Chart model);

	/**
	 * To set the default background color.
	 *
	 * @param color default background color
	 */
	void setDefaultBackgroundColor(ColorDefinition cd);

	/**
	 * Get the default background color.
	 *
	 * @return color
	 */
	ColorDefinition getDefaultBackgroundColor();

	/**
	 * Use custom value to update chart model.
	 *
	 * @param model the target chart model.
	 * @param obj   this object is used to update chart model.
	 *
	 * @return <code>true</code> means updated successfully.
	 */
	boolean updateChart(Chart model, Object obj);

	/**
	 * Indicates if chart need to inherit some basic styles from container.
	 *
	 * @return
	 */
	boolean needInheritingStyles();
}
