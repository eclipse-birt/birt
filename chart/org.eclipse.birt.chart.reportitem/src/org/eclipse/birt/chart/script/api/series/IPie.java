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

import org.eclipse.birt.chart.script.api.component.IValueSeries;

/**
 * Represents Pie series of a Chart in the scripting environment
 */

public interface IPie extends IValueSeries {

	/**
	 * Gets the minimum value that will be included in Min Slice
	 * 
	 * @return the minimum value
	 */
	double getMinSlice();

	/**
	 * Sets the minimum value that will be included in Min Slice
	 * 
	 * @param value the minimum value
	 */
	void setMinSlice(double value);

	/**
	 * Gets the label of Min slice
	 * 
	 * @return label
	 */
	String getMinSliceLabel();

	/**
	 * Sets the label of Min slice
	 * 
	 * @param label label
	 */
	void setMinSliceLabel(String label);

	/**
	 * Gets the expression that will be used to query when the slice explodes
	 * 
	 * @return the expression
	 */
	String getExplosionExpr();

	/**
	 * Sets the expression that will be used to query when the slice explodes
	 * 
	 * @param expr the expression
	 */
	void setExplosionExpr(String expr);
}
