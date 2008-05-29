/*
 *************************************************************************
 * Copyright (c) 2004, 20085 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IQueryExecutionHints;

/**
 * This is an implementation of IQueryExecutionHints.
 * 
 *
 */
public class QueryExecutionHints implements IQueryExecutionHints
{
	//
	private boolean doSortBeforeGrouping = true;;
	
	/**
	 * 
	 * @param doSortBeforeGrouping
	 */
	public void setSortBeforeGrouping( boolean doSortBeforeGrouping )
	{
		this.doSortBeforeGrouping = doSortBeforeGrouping;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IQueryExecutionHints#doSortBeforeGrouping()
	 */
	public boolean doSortBeforeGrouping( )
	{
		return this.doSortBeforeGrouping;
	}

}
