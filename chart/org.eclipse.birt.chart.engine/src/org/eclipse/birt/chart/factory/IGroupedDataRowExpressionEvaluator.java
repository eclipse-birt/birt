/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.factory;

/**
 * The interface is declared to wrap <code>IQueryResultSet</code> and evaluate
 * grouped, uni-dimensional data.
 */
public interface IGroupedDataRowExpressionEvaluator extends IDataRowExpressionEvaluator {

	/**
	 * Returns the group breaks of specified group level. <code>null</code> means no
	 * group breaks.
	 * 
	 * @param groupExp
	 * @return group breaks
	 */
	int[] getGroupBreaks(int groupLevel);

	/**
	 * Returns if optional grouping needs to be done in chart engine.
	 * 
	 * @return true then do optional grouping in chart engine.
	 */
	boolean needOptionalGrouping();

	/**
	 * Returns if category grouping needs to be done in chart engine.
	 * 
	 * @return true then do category grouping in chart engine.
	 */
	boolean needCategoryGrouping();

	/**
	 * Returns if group is enabled in each group-level.
	 */
	boolean[] getGroupStatus();

}
