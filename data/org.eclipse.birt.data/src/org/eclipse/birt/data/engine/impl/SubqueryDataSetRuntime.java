/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;

/**
 * A data set runtime for subquery. While a subquery doesn't have its own data
 * set, we nonetheless provide a data set runtime to simply code logic. Most of
 * the methods are no-op
 */
public class SubqueryDataSetRuntime extends DataSetRuntime {
	private List computedColumns = new ArrayList();

	/**
	 * Constructor.
	 * 
	 * @param executor     Subquery executor
	 * @param outerDataSet DataSet runtime of the "real" data set associated with
	 *                     the outer query
	 */
	public SubqueryDataSetRuntime(IQueryExecutor executor, DataEngineSession session) {
		// Subquery data set does not have an associated data set design
		super(null, executor, session);
		logger.entering(SubqueryDataSetRuntime.class.getName(), "SubqueryDataSetRuntime", executor);
		logger.exiting(SubqueryDataSetRuntime.class.getName(), "SubqueryDataSetRuntime");
	}

	protected IBaseDataSetEventHandler getEventHandler() {
		return null;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#getExtensionID()
	 */
	public String getExtensionID() {
		return "";
	}

	public List getComputedColumns() {
		return this.computedColumns;
	}

}
