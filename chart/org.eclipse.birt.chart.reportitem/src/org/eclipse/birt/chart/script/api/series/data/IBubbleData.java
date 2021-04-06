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
