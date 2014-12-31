/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;

import com.ibm.icu.util.ULocale;

/**
 * Default implementation of {@link org.eclipse.birt.data.engine.api.IBaseDataSetDesign} interface.<p>
 * Describes the static design of a data set
 * to be used by the Data Engine.
 * Each subclass defines a specific type of data set. 
 */
public class BaseDataSetDesign implements IBaseDataSetDesign
{
    private String 	name;
    private String 	dataSourceName;
    private List	parameters;
    private List	resultSetHints;
    private List	computedColumns;
    private List	filters;
    private Collection inputParamBindings;
    private String 	beforeOpenScript;
    private String 	afterOpenScript;
    private String 	onFetchScript;
    private String 	beforeCloseScript;
    private String 	afterCloseScript;
    private IBaseDataSetEventHandler eventHandler;
    private int fetchRowLimit;
	
	private int cacheRowCount;
    private boolean distinctValue;
    
    private IScriptExpression dataSetACL;
    private Map<String, IScriptExpression> columnACL = new HashMap<String, IScriptExpression>();
    private IScriptExpression rowACL;
	private String nullOrdering;
	private ULocale uLocale;
    
	private List<ISortDefinition> sortHints = null;
	private boolean needCache = true;
	private Object queryContextVisitor = null;
	
    
	/**
	 * Instantiates a data set with given name.
	 * @param name Name of data set
	 */
	public BaseDataSetDesign( String name )
	{
		this.name = name;
	}
    
	/**
	 * Instantiates a data set with given name and data source name.
	 * @param name Name of data set
	 * @param dataSourceName Name of data source used by this data set. 
	 * 						Can be null or empty if this data
	 * 						set does not specify a data source.
	 */
	public BaseDataSetDesign( String name, String dataSourceName )
	{
		this.name = name;
		this.dataSourceName = dataSourceName;
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getName()
     */	
	public String getName( )
	{
		return name;
	}
	
	/**
	 * @deprecated
     * @return cache row count
     */
    public int getCacheRowCount( )
    {
    	return cacheRowCount;
    }
    
    /**
     * @deprecated
     * @param cacheRowCount
     */
    public void setCacheRowCount( int cacheRowCount )
    {
    	this.cacheRowCount = cacheRowCount;
    }
    
	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#useDistinctValue()
	 */
	public boolean needDistinctValue( )
	{
		return this.distinctValue;
	}
	
	/**
	 * @param distinctValue
	 */
	public void setDistinctValue( boolean distinctValue )
	{
		this.distinctValue = distinctValue;
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getDataSourceName()
     */	
	public String getDataSourceName( )
	{
		return dataSourceName;
	}

	/**
	 * Specifies the data source (connection) name.
	 * @param dataSourceName The name of the dataSource to set.
	 */
	public void setDataSource( String dataSourceName ) 
	{
		this.dataSourceName = dataSourceName;
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getComputedColumns()()
     */	
	public List getComputedColumns( )
	{
	    if ( computedColumns == null )
	        computedColumns = new ArrayList();
		return computedColumns;
	}
	
	/**
	 * Adds a new computed column to the data set.
	 * Ignores given computed column if null.
	 * @param column	Could be null.
	 */
	public void addComputedColumn( IComputedColumn column )
	{
	    if ( column != null )
	        getComputedColumns().add( column );
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getFilters()
     */	
	public List getFilters( )
	{
	    if ( filters == null )
	        filters = new ArrayList();
	    return filters;
	}
	
	/**
	 * Adds a filter to the filter list.
	 * Ignores given filter if null.
	 * @param filter	Could be null.
	 */
	public void addFilter( IFilterDefinition filter )
	{
	    if ( filter != null )
	        getFilters().add( filter );
	}
	
	/**
	 * Get sort hints defined.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getSortHints()
	 */
	public List<ISortDefinition> getSortHints( )
	{
	    if ( sortHints == null )
	    	sortHints = new ArrayList<ISortDefinition>();
	    return sortHints;
	}
	
	/**
	 * Add a sort ordering to sort hints.<p>
	 * Ignore when sort hint is <code>NULL</code>.
	 * 
	 * @param sortHint Reference to <code>ISortDefinition</code>
	 */
	public void addSortHint( ISortDefinition sortHint )
	{
		if ( sortHint != null )
			getSortHints().add( sortHint );
	}
    
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getParameters()
     */	
    public List getParameters()
    {
	    if ( parameters == null )
	        parameters = new ArrayList();
		return parameters;
    }
	
	/**
	 * Adds a parameter definition to the data set.
	 */
	public void addParameter( IParameterDefinition param )
	{
	    if ( param != null )
	        getParameters().add( param );
	}
		
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getResultSetHints()
     */	
	public List getResultSetHints()
	{
	    if ( resultSetHints == null )
	        resultSetHints = new ArrayList();
		return resultSetHints;
	}
	
	/**
	 * Adds a column to the result set hints definition.
	 */
	public void addResultSetHint( IColumnDefinition col )
	{
	    if ( col != null )
	        getResultSetHints().add( col );
	}

    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getInputParamBindings()
     */	
	public Collection getInputParamBindings()
	{
	    if ( inputParamBindings == null )
	        inputParamBindings = new ArrayList();
	    return inputParamBindings;
	}
	
	/**
	 * Adds an input parameter binding.
	 * Ignores given binding if null.
	 * @param binding	Could be null.
	 */
	public void addInputParamBinding( IInputParameterBinding binding )
	{
	    if ( binding != null )
	        getInputParamBindings().add( binding );
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getBeforeOpenScript()
     */	
	public String getBeforeOpenScript( )
	{
		return beforeOpenScript;
	}

	/**
	 * Sets the <code>beforeOpen</code> script for the data set
	 */
	public void setBeforeOpenScript( String beforeOpenScript ) 
	{
		this.beforeOpenScript = beforeOpenScript;
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getAfterOpenScript()
     */	
	public String getAfterOpenScript( )
	{
		return afterOpenScript;
	}

	/**
	 * Sets the <code>afterOpen</code> script for the data set
	 * @param afterOpenScript The AfterOpen script to set.
	 */
	public void setAfterOpenScript( String afterOpenScript ) 
	{
		this.afterOpenScript = afterOpenScript;
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getOnFetchScript()
     */	
	public String getOnFetchScript( )
	{
		return onFetchScript;
	}
	
	/**
	 * Sets the <code>onFetch</code> script for the data set
	 */
	public void setOnFetchScript( String onFetchScript ) 
	{
		this.onFetchScript = onFetchScript;
	}
	
    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getBeforeCloseScript()
     */	
	public String getBeforeCloseScript( )
	{
		return beforeCloseScript;
	}
	
	/**
	 * Sets the <code>beforeClose</code> script for the data set
	 */
	public void setBeforeCloseScript( String beforeCloseScript ) 
	{
		this.beforeCloseScript = beforeCloseScript;
	}

    /**
     * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getAfterCloseScript()
     */	
	public String getAfterCloseScript( )
	{
		return afterCloseScript;
	}

	/**
	 * Sets the <code>afterClose</code> script for the data set
	 */
	public void setAfterCloseScript( String afterCloseScript ) 
	{
		this.afterCloseScript = afterCloseScript;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getEventHandler()
	 */
	public IBaseDataSetEventHandler getEventHandler()
	{
		return eventHandler;
	}
	
	/**
	 * Sets the event handler for this data set
	 */
	public void setEventHandler( IBaseDataSetEventHandler handler )
	{
		this.eventHandler = handler;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getRowFetchLimit()
	 */
	public int getRowFetchLimit( )
	{
		return this.fetchRowLimit;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#setRowFetchLimit(int)
	 */
	public void setRowFetchLimit( int max )
	{
		this.fetchRowLimit = max <= 0 ? 0 : max;
	}
    
	public IScriptExpression getDataSetColumnACL( String columnName )
	{
		return this.columnACL.get( columnName );
	}
	
	public IScriptExpression getDataSetACL( )
	{
		return this.dataSetACL;
	}
	
	public IScriptExpression getRowACL( )
	{
		return this.rowACL;
	}
	
	public void setDataSetACL( IScriptExpression acl )
	{
		this.dataSetACL = acl;
	}
	
	public void setDataSetColumnACL( String columnName, IScriptExpression acl )
	{
		this.columnACL.put( columnName, acl );
	}
	
	public void setRowACL( IScriptExpression expr )
	{
		this.rowACL = expr;
	}

	public ULocale getCompareLocale( )
	{
		return this.uLocale;
	}

	public String getNullsOrdering( )
	{
		return this.nullOrdering;
	}
	
	public void setCompareLocale( ULocale ulocale )
	{
		this.uLocale = ulocale;
	}
	
	public void setNullsOrdering( String nullOrdering )
	{
		this.nullOrdering = nullOrdering;
	}
	
	public boolean needCache()
	{
		return this.needCache;
	}
	
	public void setNeedCache( boolean needCache )
	{
		this.needCache = needCache;
	}
	
	public void setQueryContextVisitor( Object visitor )
	{
		this.queryContextVisitor = visitor;
	}
	
	public Object getQueryContextVisitor( )
	{
		return this.queryContextVisitor;
	}
}
