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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseLinkDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryExecutionHints;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;


/**
 * Default implementation of the {@link org.eclipse.birt.data.engine.api.IBaseQueryDefinition} 
 * interface.
 *
 */

abstract public class BaseQueryDefinition extends BaseTransform implements IBaseQueryDefinition
{
	protected List 		        groups = new ArrayList();
	protected boolean 			hasDetail = true;
	protected IBaseQueryDefinition 	parentQuery;
	protected int				maxRowCount = 0;
	protected int				startingRow = 0;
	protected boolean			distinctValue = false;
	
	private   boolean           cacheQueryResults = false;
	
	//	 order might be sensitive, use LinkedHashMap instead of HashMap
	private 	Map resultExprsMap = new LinkedHashMap( );
	private 	Map bindingMap = new LinkedHashMap();
	private IQueryExecutionHints queryExecutionHints = new QueryExecutionHints();
	private String name;
	private boolean isTempQuery = false;
	
    private Set<IBaseLinkDefinition> links = new HashSet<IBaseLinkDefinition>( );
	
	/**
	 * Constructs an instance with parent set to the specified <code>BaseQueryDefinition</code>
	 */
	BaseQueryDefinition( IBaseQueryDefinition parent )
	{
		parentQuery = parent;
	}
	
	/**
	 * Returns the group definitions as an ordered collection of <code>GroupDefinition</code>
	 * objects. Groups are organizations within the data that support
	 * aggregation, filtering and sorting. Reports use groups to trigger
	 * level breaks.
	 * 
	 * @return the list of groups. If no group is defined, null is returned.
	 */
	
	public List getGroups( )
	{
		return groups;
	}

	/**
	 * Appends a group definition to the group list.
	 * @param group Group definition to add
	 */
	public void addGroup( GroupDefinition group )
	{
	    groups.add( group );
	}
	
	/**
	 * Indicates if the report will use the detail rows. Allows the data
	 * transform engine to optimize the query if the details are not used.
	 * 
	 * @return true if the detail rows are used, false if not used
	 */
	public boolean usesDetails( )
	{
		return hasDetail;
	}
	
	
	/**
	 * @param usesDetails Whether detail rows are used in this query
	 */
	public void setUsesDetails(boolean usesDetails) 
	{
		this.hasDetail = usesDetails;
	}

	/**
	 * Returns the parent query. The parent query is the outer query which encloses
	 * this query
	 */
	public IBaseQueryDefinition getParentQuery() 
	{
		return parentQuery;
	}
	
	/**
	 * Gets the maximum number of detail rows that can be retrieved by this report query
	 * @return Maximum number of rows. If 0, there is no limit on how many rows this query can retrieve.
	 */
	public int getMaxRows( ) 
	{
		return maxRowCount;
	}
	
	/**
	 * Sets the maximum number of detail rows that can be retrieved by this report query
	 * 
	 */
	public void setMaxRows( int maxRows ) 
	{
	    maxRowCount = maxRows;
	}
	
	/**
	 * Sets the starting row that will be retrieved by this query
	 * @param startingRow
	 */
	public void setStartingRow( int startingRow )
	{
		this.startingRow = startingRow;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#getStartingRow()
	 */
	public int getStartingRow( )
	{
		return this.startingRow;
	}
	
	/**
	 * Sets the distinct value flag.
	 * @return
	 */
	public void setDistinctValue( boolean distinctValue )
	{
		this.distinctValue = distinctValue;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#isDistinct()
	 */
	public boolean getDistinctValue( )
	{
		return this.distinctValue;
	}
	
	/**
	 * @param name
	 * @param expression
	 * @deprecated
	 */
	public void addResultSetExpression( String name, IBaseExpression expression )
	{
		Binding binding = new Binding( name );
		binding.setExpression(expression );
		if ( expression != null )
			binding.setDataType( expression.getDataType( ) );
		this.bindingMap.put( name, binding );
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#addBinding(java.lang.String, org.eclipse.birt.data.engine.api.IBinding)
	 */
	public void addBinding( IBinding binding ) throws DataException
	{ 
		//TODO remove me
		//Temp solution for backward compatibility util Model make the changes.
		if ( binding.getExpression( )!= null && binding.getExpression().getGroupName( ).equals( IBaseExpression.GROUP_OVERALL ))
		{
			binding.getExpression( ).setGroupName( null );
		}
		final String bindingName = binding.getBindingName( );
		if ( bindingMap.containsKey( bindingName ) )
		{
			throw new DataException( ResourceConstants.DUPLICATED_BINDING_NAME, bindingName );
		}
		this.bindingMap.put( bindingName, binding );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#getBindings()
	 */
	public Map getBindings( ) 
	{
		for( Iterator it = this.resultExprsMap.keySet( ).iterator( ); it.hasNext( );)
		{
			String key = it.next( ).toString( );
			IBaseExpression expr = (IBaseExpression)this.resultExprsMap.get( key );
			if ( this.bindingMap.get( key ) == null )
			{
				Binding binding = new Binding( key );
				binding.setExpression( expr );
				this.bindingMap.put( key, binding );
			}
		}
		return this.bindingMap;
	}
	
	/* 
	 * @see org.eclipse.birt.data.engine.api.IBaseTransform#getResultSetExpressions()
	 */
	public Map getResultSetExpressions( )
	{
		return this.resultExprsMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#needCache()
	 */
	public boolean cacheQueryResults() 
	{
		return cacheQueryResults;
	}
	
	/*
	 * set whether cache query results
	 */
	public void setCacheQueryResults( boolean cacheQueryResults )
	{
		this.cacheQueryResults = cacheQueryResults ;
	}
	
	/**
	 * Set the query execution hints.
	 * 
	 * @param hints
	 */
	public void setQueryExecutionHints( IQueryExecutionHints hints )
	{
		this.queryExecutionHints = hints;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#getQueryExecutionHints()
	 */
	public IQueryExecutionHints getQueryExecutionHints()
	{
		return this.queryExecutionHints;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.INamedObject#getName()
	 */
	public String getName( )
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.INamedObject#setName(java.lang.String)
	 */
	public void setName( String name )
	{
		this.name = name;
		
	}
	
	public void setAsTempQuery()
	{
		this.isTempQuery = true;
	}
	public boolean isTempQuery()
	{
		return this.isTempQuery ;
	}
	
	public Set<IBaseLinkDefinition> getLinks()
	{
	    return this.links;
	}
	
	public void addLink(IBaseLinkDefinition link)
	{
	    this.links.add( link );
	}
}
