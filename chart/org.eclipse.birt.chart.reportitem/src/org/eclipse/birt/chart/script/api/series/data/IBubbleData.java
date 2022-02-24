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

package org.eclipse.birt.chart.script.api.series.data;

/**
 * Represents the data contained in the Bubble Series
 */

public interface IBubbleData extends ISeriesData {

	/**
	 * Gets the query expression of Bubble size.
	 * 
	 * @return Bubble size expression
	 */
	String getBubbleSizeExpr();

	/**
	 * Gets the query expression of Orthogonal value.
	 * 
	 * @return Orthogonal value expression
	 */
	String getOrthogonalValueExpr();

	/**
	 * Sets the query expression of Bubble Size.
	 * 
	 * @param expr
	 */
	void setBubbleSizeExpr(String expr);

	/**
	 * Sets the query expression of Orthogonal value.
	 * 
	 * @param expr Orthogonal value expression
	 */
	void setOrthogonalValueExpr(String expr);
}
