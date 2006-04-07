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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IBaseTransform;

/**
 * Default implementation of {@link org.eclipse.birt.data.engine.api.IBaseTransform} interface.
 *
*/
abstract public class BaseTransform implements IBaseTransform
{
    // Enumeration constants for expressionTiming
    /**
     * The expression is evaluated before the first row in the series. A constant for the 
     * expressionTiming parameter of addExpression.  
     */
    public static final int BEFORE_FIRST_ROW  	= 0;
    /**
     * The expression is evaluated after the last row in the series. A constant for the 
     * expressionTiming parameter of addExpression.  
     */
    public static final int AFTER_LAST_ROW 		= 1;
    /**
     * The expression is evaluated on each row. A constant for the 
     * expressionTiming parameter of addExpression.  
     */
    public static final int ON_EACH_ROW 		= 2;
    
    protected	List filters = new ArrayList();
    protected 	List subqueries = new ArrayList();
	protected 	List sorts = new ArrayList();
	protected 	List rowExpressions = new ArrayList();
	protected 	List beforeExpressions = new ArrayList();
	protected 	List afterExpressions = new ArrayList();
	
	// order might be sensitive, use LinkedHashMap instead of HashMap
	private 	Map resultExprsMap = new LinkedHashMap( );
	
	/**
	 * Returns the filters defined in this transform, as an ordered list of 
	 * <code>IFilterDefintion</code> objects.
	 * 
	 * @return the filters. null if no filter is defined.
	 */
	public List getFilters( )
	{
		return filters;
	}

	/**
	 * Add one filter to the filter list
	 */
	public void addFilter( IFilterDefinition filter) 
	{
		filters.add(filter);
	}
	
	/**
	 * Returns an unordered collection of subqueries that are alternative views of
	 * the result set for this transform. Objects are of type <code>SubqueryDefinition</code>.
	 * 
	 * @return the subqueries for this transform
	 */
	
	public Collection getSubqueries( )
	{
		return subqueries;
	}

	/**
	 * Add a subquery to the list
	 * @param subquery one subquery to add to the subquery set
	 */
	public void addSubquery( SubqueryDefinition subquery) 
	{
	    subqueries.add(subquery);
	}
	
	/**
	 * Returns the sort criteria as an ordered list of <code>SortDefinition</code> objects.
	 * 
	 * @return the sort criteria
	 */
	
	public List getSorts( )
	{
		return sorts;
	}
	
	/**
	 * Appends one sort definition to the list of sort criteria
	 */
	public void addSort( SortDefinition sort) 
	{
		sorts.add(sort);
	}

	/**
	 * Add one Javascript expression to the list of expressions that needs evaluation as part 
	 * of this transform. 
	 * expressionTiming can be <br>
	 * <code>BEFORE_FIRST_ROW</code>: expression is evaluated at the start of the iteration over the row set for this gorup/list <br>
	 * <code>AFTER_LAST_ROW</code>: expression is evaluated at the end of the iteration<br>
	 * <code>ON_EACH_ROW</code>: expression is evaluated with each detail row within the group/list <br>  
	 * @deprecated use addResultSetExpression( ) instead
	 */
	public void addExpression(IBaseExpression expression, int expressionTiming ) 
	{
	    if ( expressionTiming == BEFORE_FIRST_ROW )
	    {
	        beforeExpressions.add( expression );
	    }
	    else if ( expressionTiming == AFTER_LAST_ROW )
	    {
	        afterExpressions.add( expression );
	    }
	    else 
	    {
	        rowExpressions.add( expression );
	    }
	}
	
	
	/**
	 * Gets the expressions that needs to be calculated per detail row, as an unordered
	 * collection of <code>IBaseExpression</code> objects
	 * @deprecated use getResultSetExpressions( ) instead
	 */
	public Collection getRowExpressions() 
	{
		return rowExpressions;
	}
	
	/**
	 * Gets the expressions that needs to be available at the end of the group/list, as an unordered
	 * collection of <code>IBaseExpression</code> objects.
	 * @deprecated use getResultSetExpressions( ) instead
	 */
	public Collection getAfterExpressions() 
	{
		return afterExpressions;
	}
	
	/**
	 * Gets the expressions that needs to be available at the beginning of the group/list, as an unordered
	 * collection of <code>IBaseExpression</code> objects.
	 * @deprecated use getResultSetExpressions( ) instead
	 */
	public Collection getBeforeExpressions() 
	{
		return beforeExpressions;
	}
	
	/**
	 * @param name
	 * @param expression
	 */
	public void addResultSetExpression( String name, IBaseExpression expression )
	{
		this.resultExprsMap.put( name, expression );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseTransform#getResultSetExpressions()
	 */
	public Map getResultSetExpressions( )
	{
		return this.resultExprsMap;
	}
	
}
