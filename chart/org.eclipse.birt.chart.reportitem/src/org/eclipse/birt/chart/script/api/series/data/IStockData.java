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
 * Represents the data contained in the Stock Series
 */

public interface IStockData extends ISeriesData {

	/**
	 * Gets the expression of Open.
	 * 
	 * @return Open expression
	 */
	String getOpenExpr();

	/**
	 * Gets the expression of Close.
	 * 
	 * @return Close expression
	 */
	String getCloseExpr();

	/**
	 * Gets the expression of High.
	 * 
	 * @return High expression
	 */
	String getHighExpr();

	/**
	 * Gets the expression of Low.
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

	/**
	 * Sets the query expression of Open.
	 * 
	 * @param Open expression
	 */
	void setOpenExpr(String expr);

	/**
	 * Sets the query expression of Close.
	 * 
	 * @param Close expression
	 */
	void setCloseExpr(String expr);
}
