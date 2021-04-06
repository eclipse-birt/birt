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
 * Represents the data contained in the Difference Series
 */

public interface IDifferenceData extends ISeriesData {

	/**
	 * Gets the query expression of High.
	 * 
	 * @return High expression
	 */
	String getHighExpr();

	/**
	 * Gets the query expression of Low.
	 * 
	 * @return Low expression
	 */
	String getLowExpr();

	/**
	 * Sets the query expression of High.
	 * 
	 * @param High expression
	 */
	void setHighExpr(String expr);

	/**
	 * Sets the query expression of Low.
	 * 
	 * @param Low expression
	 */
	void setLowExpr(String expr);
}
