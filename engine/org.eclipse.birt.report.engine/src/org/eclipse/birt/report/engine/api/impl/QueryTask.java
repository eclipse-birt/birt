/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.InstanceID;

public class QueryTask {

	IDataQueryDefinition query;
	DataSetID parent;
	int rowId;
	String cellId;
	InstanceID iid;

	public QueryTask(IDataQueryDefinition query, DataSetID parent, int rowId, InstanceID iid) {
		this.query = query;
		this.parent = parent;
		this.rowId = rowId;
		this.iid = iid;
	}

	public QueryTask(IDataQueryDefinition query, DataSetID parent, String cellId, InstanceID iid) {
		this.query = query;
		this.parent = parent;
		this.cellId = cellId;
		this.iid = iid;
	}

	public IDataQueryDefinition getQuery() {
		return query;
	}

	public void setQuery(IDataQueryDefinition query) {
		this.query = query;
	}

	public DataSetID getParent() {
		return parent;
	}

	public void setParent(DataSetID parent) {
		this.parent = parent;
	}

	public int getRowID() {
		return rowId;
	}

	public void setRowID(int rowId) {
		this.rowId = rowId;
	}

	public InstanceID getInstanceID() {
		return iid;
	}

	public void setInstanceID(InstanceID iid) {
		this.iid = iid;
	}

	public String getCellID() {
		return cellId;
	}
}
