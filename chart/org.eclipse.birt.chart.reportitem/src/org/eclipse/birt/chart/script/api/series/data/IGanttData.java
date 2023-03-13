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
