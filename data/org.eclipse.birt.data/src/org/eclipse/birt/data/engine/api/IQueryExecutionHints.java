/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api;

import java.util.List;

/**
 * This class provides hints info for a Data Engine Query to execution.
 *
 *
 */
public interface IQueryExecutionHints {
	/**
	 * Indicate whether should we enable the pushdown of sort/filter/aggregation to
	 * ODA whenever applicable.
	 *
	 * @return
	 */
	boolean enablePushDown();

	/**
	 * Set whether should the pushdown be enabled.
	 *
	 * @param enablePushDown
	 */
	void setEnablePushDown(boolean enablePushDown);

	/**
	 * Indicate whether should we do sort before grouping.
	 *
	 * @return
	 */
	boolean doSortBeforeGrouping();

	/**
	 * Return a list of IGroupInstanceInfo instances that is needed in target
	 * ResultSet.
	 *
	 * @return
	 */
	List<IGroupInstanceInfo> getTargetGroupInstances();

	/**
	 * Set whether to sort before grouping.
	 *
	 * @param doSortBeforeGrouping
	 */
	void setSortBeforeGrouping(boolean doSortBeforeGrouping);

	/**
	 * Add a target group instance.
	 *
	 * @param info
	 */
	void addTargetGroupInstance(IGroupInstanceInfo info);
}
