/*
 *************************************************************************
 * Copyright (c) 2004, 20085 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IQueryExecutionHints;

/**
 * This is an implementation of IQueryExecutionHints.
 * 
 *
 */
public class QueryExecutionHints implements IQueryExecutionHints {
	//
	private boolean doSortBeforeGrouping = true;
	private List<IGroupInstanceInfo> targetGroupInstances = new ArrayList<IGroupInstanceInfo>();
	private boolean enablePushdown = true;

	/**
	 * 
	 * @param doSortBeforeGrouping
	 */
	public void setSortBeforeGrouping(boolean doSortBeforeGrouping) {
		this.doSortBeforeGrouping = doSortBeforeGrouping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IQueryExecutionHints#doSortBeforeGrouping()
	 */
	public boolean doSortBeforeGrouping() {
		return this.doSortBeforeGrouping;
	}

	/**
	 * Return a list of IGroupInstanceInfo instances that is needed in target
	 * ResultSet.
	 * 
	 * @return
	 */
	public List<IGroupInstanceInfo> getTargetGroupInstances() {
		return this.targetGroupInstances;
	}

	/**
	 * add target group instance
	 */
	public void addTargetGroupInstance(IGroupInstanceInfo info) {
		this.targetGroupInstances.add(info);
	}

	public boolean enablePushDown() {
		return this.enablePushdown;
	}

	public void setEnablePushDown(boolean enablePushDown) {
		this.enablePushdown = enablePushDown;
	}
}
