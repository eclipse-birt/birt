/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

public class NamingRelation
{

	private String queryResultId;
	private String queryResultName;
	private String queryDefnName;

	/**
	 * @param queryResultId
	 * @param queryDefnName
	 * @param queryResultName
	 */
	public NamingRelation( String queryResultId, String queryDefnName,
			String queryResultName )
	{
		this.queryResultId = queryResultId;
		this.queryDefnName = queryDefnName;
		this.queryResultName = queryResultName;
	}

	/**
	 * @return the queryResultId
	 */
	public String getQueryResultId( )
	{
		return queryResultId;
	}

	/**
	 * @param queryResultId the queryResultId to set
	 */
	public void setQueryResultId( String queryResultId )
	{
		this.queryResultId = queryResultId;
	}

	/**
	 * @return the queryResultName
	 */
	public String getQueryResultName( )
	{
		return queryResultName;
	}

	/**
	 * @param queryResultName the queryResultName to set
	 */
	public void setQueryResultName( String queryResultName )
	{
		this.queryResultName = queryResultName;
	}

	/**
	 * @return the queryDefnName
	 */
	public String getQueryDefnName( )
	{
		return queryDefnName;
	}

	/**
	 * @param queryDefnName the queryDefnName to set
	 */
	public void setQueryDefnName( String queryDefnName )
	{
		this.queryDefnName = queryDefnName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString( )
	{
		return queryResultId + "#" + queryDefnName + "#" + queryResultName;
	}

}
