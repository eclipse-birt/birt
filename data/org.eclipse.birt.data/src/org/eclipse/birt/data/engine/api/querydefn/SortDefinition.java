/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import org.eclipse.birt.data.engine.api.ISortDefinition;


/**
 * Default implementation of the ISortDefn interface <p>
 * Describes one sort (key, direction) pair in a sort sequence. The sort key can be a single column name
 * or a Javascript expression.<br>
 * NOTE: Presently only sorting on actual columns are supported. If the sort key is specified as an 
 * expression, it must be in the form row.column_name, or row["column_name"]
 */

public class SortDefinition implements ISortDefinition
{
    protected String keyExpr;
	protected String keyColumn;
	protected int direction;
	
	/**
	 * Returns the name of the column to sort on. Either the KeyColumn or KeyExpr can
	 * be used to define the sort key.
	 */
	public String getColumn( )
	{
		return keyColumn;
	}
	
	/**
	 * Returns the JavaScript expression that defines the group key. <br>
	 */
	public String getExpression( )
	{
		return keyExpr;
	}
	
	/**
	 * @param keyExpr The group key expression to set.
	 */
	public void setColumn( String keyColumn ) 
	{
		this.keyColumn = keyColumn;
		this.keyExpr = null;
	}

	/**
	 * @param keyExpr The group key expression to set.
	 */
	public void setExpression( String keyExpr) 
	{
		this.keyExpr = keyExpr;
		this.keyColumn = null;
	}
	
	/**
	 * Returns the sort direction.
	 * 
	 * @return the sort direction: one of SORT_ASC or SORT_DESC
	 */
	
	public int getSortDirection( )
	{
		return direction;
	}
	
	/**
	 * @param sortDirection The sortDirection to set.
	 */
	public void setSortDirection(int sortDirection) 
	{
		this.direction = sortDirection;
	}
}
