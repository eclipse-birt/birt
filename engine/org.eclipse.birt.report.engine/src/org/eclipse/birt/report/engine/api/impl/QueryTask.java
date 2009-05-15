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

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.InstanceID;

public class QueryTask
{

	IBaseQueryDefinition query;
	DataSetID parent;
	int rowId;
	InstanceID iid;

	QueryTask( IBaseQueryDefinition query, DataSetID parent, int rowId,
			InstanceID iid )
	{
		this.query = query;
		this.parent = parent;
		this.rowId = rowId;
		this.iid = iid;
	}

	public IBaseQueryDefinition getQuery( )
	{
		return query;
	}

	public void setQuery( IBaseQueryDefinition query )
	{
		this.query = query;
	}

	public DataSetID getParent( )
	{
		return parent;
	}

	public void setParent( DataSetID parent )
	{
		this.parent = parent;
	}

	public int getRowID( )
	{
		return rowId;
	}

	public void setRowID( int rowId )
	{
		this.rowId = rowId;
	}

	public InstanceID getInstanceID( )
	{
		return iid;
	}

	public void setInstanceID( InstanceID iid )
	{
		this.iid = iid;
	}
}
