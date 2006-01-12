/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;

/**
 * A data set runtime for subquery. While a subquery doesn't have its own data set,
 * we nonetheless provide a data set runtime to simply code logic. Most of the 
 * methods are no-op
 */
public class SubqueryDataSetRuntime extends DataSetRuntime
{
	/**
	 * Constructor.
	 * @param executor Subquery executor
	 * @param outerDataSet DataSet runtime of the "real" data set associated with the outer query
	 */
	public SubqueryDataSetRuntime( PreparedQuery.Executor executor )
	{
		// Subquery data set does not have an associated data set design
		super( null, executor );
	}

	protected IBaseDataSetEventHandler getEventHandler()
	{
		return null;
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#getExtensionID()
	 */
	public String getExtensionID()
	{
		return "";
	}


}
