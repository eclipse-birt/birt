/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
