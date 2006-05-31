/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document;

/**
 * 
 */
public class QueryResultInfo
{
	private String rootQueryResultID;
	private String parentQueryResultID;
	
	private String queryResultID;
	private String subQueryName;
	private int index;
	
	/**
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 */
	public QueryResultInfo( String rootQueryResultID,
			String parentQueryResultID, String queryResultID,
			String subQueryName, int index )
	{
		this( queryResultID, subQueryName, index );
		this.rootQueryResultID = rootQueryResultID;
		this.parentQueryResultID = parentQueryResultID;
	}
	
	/**
	 * @param queryResultID
	 * @param subQueryName
	 * @param subQueryIndex
	 */
	public QueryResultInfo( String queryResultID, String subQueryName, int index )
	{
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
		this.index = index;
	}

	/**
	 * @return
	 */
	String getRootQueryResultID()
	{
		return this.rootQueryResultID;
	}
	
	/**
	 * @return
	 */
	String getParentQueryResultID()
	{
		return this.parentQueryResultID;
	}
	
	/**
	 * @return
	 */
	String getQueryResultID( )
	{
		return this.queryResultID;
	}

	/**
	 * @return
	 */
	String getSubQueryName( )
	{
		return this.subQueryName;
	}

	/**
	 * @return
	 */
	int getIndex( )
	{
		return this.index;
	}
	
}
