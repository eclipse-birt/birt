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
 * Represents the data contained in the Gantt Series
 */

public interface IGanttData extends ISeriesData {

	/**
	 * Gets the expression of Task name.
	 * 
	 * @return task name expression
	 */
	String getTaskNameExpr();

	/**
	 * Gets the expression of Start date.
	 * 
	 * @return Start date expression
	 */
	String getStartExpr();

	/**
	 * Gets the expression of Finish date.
	 * 
	 * @return Finish date expression
	 */
	String getFinishExpr();

	/**
	 * Sets the query expression of Task name.
	 * 
	 * @param task name expression
	 */
	void setTaskNameExpr(String expr);

	/**
	 * Sets the query expression of Start date.
	 * 
	 * @param Start date expression
	 */
	void setStartExpr(String expr);

	/**
	 * Sets the query expression of Finish date.
	 * 
	 * @param Finish date expression
	 */
	void setFinishExpr(String expr);

}
