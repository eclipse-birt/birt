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

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;

/**
 * Default implementation of {@link org.eclipse.birt.data.engine.api.IGroupDefinition}.
 */

public class GroupDefinition extends BaseTransform implements IGroupDefinition
{
	protected String name;
	protected IScriptExpression keyExpr;
	protected String keyColumn;
	protected int interval = NO_INTERVAL;
	protected double intervalRange = 0;
	protected int sortDirection = ISortDefinition.SORT_ASC;
	protected Object intervalStart;
	
	
	/**
	 * Constructs a group with the given name
	 */
	public GroupDefinition( String name )
	{
	    this.name = name;
	}
	
	/**
	 * Constructs an unnamed group
	 * @deprecated
	 */
	public GroupDefinition()
	{
	}
	
	/**
	 * Returns the name of the group
	 * @return Name of group. Can be null if group is unnamed.
	 */
	public String getName()
	{
	    return name;
	}
	
	/**
	 * Returns the interval for grouping on a range of contiguous group key values. 
	 * Interval can be year, months, day, etc.
	 * 
	 * @return the grouping interval
	 */
	
	public int getInterval( )
	{
		return interval;
	}
	
	/**
	 * Returns the sort direction on the group key. Use this to specify a sort in the common case
	 * where the groups are ordered by the group key only. To specify other types of sort criteria,
	 * use the Sorts property. SortDirection is ignored if Sorts is defined for this group.
	 * @return The group key sort direction. If no direction is specified, <code>NO_SORT</code> is returned. This means
	 * that the data engine can choose any sort order, or no sort order at all, for this group level.
	 */
	public int getSortDirection() 
	{
		return sortDirection;
	}
	
	/**
	 * Returns the number of contiguous group intervals that form one single group, when Interval 
	 * is used to define group break level. For example, if Interval is 
	 * <code>MONTH_INTERVAL</code>, and IntervalRange
	 * is 6, each group is defined to contain a span of 6 months.
	 */
	public double getIntervalRange( )
	{
		return intervalRange;
	}
	
	/**
	 * Gets the starting value for the first interval
	 */
	public Object getIntervalStart()
	{
	    return intervalStart;
	}
	
	/**
	 * Returns the name of the column that defines the group key. Either the KeyColumn or KeyExpr can
	 * be used to define the group key.
	 */
	public String getKeyColumn( )
	{
		return keyColumn;
	}
	
	/**
	 * Returns the JavaScript expression that defines the group key. <br>
	 * Note: Presently group key must be a column. If an JavaScript expression is used to specify the group key,
	 * the expression must be in the form of row.column_name, or row["column_name"]. 
	 */
	public String getKeyExpression( )
	{
		if( keyExpr == null )
			return null;
		return keyExpr.getText( );
	}
	
	/**
	 * @param interval The interval to set.
	 */
	public void setInterval(int interval) 
	{
		this.interval = interval;
	}
	/**
	 * @param intervalRange The intervalRange to set.
	 */
	public void setIntervalRange(double intervalRange) 
	{
		this.intervalRange = intervalRange;
	}
	
	/**
	 * @param start a start value for the first interval
	 */
	public void setIntervalStart( Object start)
	{
	    this.intervalStart = start;
	}
	
	/**
	 * @param keyColumn Name of the column to group by
	 */
	public void setKeyColumn( String keyColumn ) 
	{
		this.keyColumn = keyColumn;
		this.keyExpr = null;
	}

	/**
	 * @param keyExpr Key expression to group by
	 */
	public void setKeyExpression( String keyExpr) 
	{
		this.keyExpr = new ScriptExpression( keyExpr );
		this.keyColumn = null;
	}
	
	public void setKeyExpression( IScriptExpression keyExpr )
	{
		this.keyExpr = keyExpr;
		this.keyColumn = null;
	}
	
	public IScriptExpression getKeyScriptExpression(){
		return keyExpr;
	}
	
	/**
	 * @param sortDirection The sortDirection to set.
	 */
	public void setSortDirection(int sortDirection) 
	{
		this.sortDirection = sortDirection;
	}
}
