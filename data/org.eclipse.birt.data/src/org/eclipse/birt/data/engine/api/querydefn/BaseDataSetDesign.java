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

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterDefinition;
import org.eclipse.birt.data.engine.api.IOutputParameterDefinition;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Default implementation of IBaseDataSetDesign interface.<p>
 * Describes the static design of any data set to be used by 
 * the Data Engine.
 * Each subclass defines a specific type of data set. 
 */
public class BaseDataSetDesign implements IBaseDataSetDesign
{
    private String 	name;
    private String 	dataSourceName;
    private List	inputParameters;
    private List 	outputParameters;
    private List	resultSetHints;
    private List	computedColumns;
    private List	filters;
    private Collection inputParamBindings;
    private String 	beforeOpenScript;
    private String 	afterOpenScript;
    private String 	onFetchScript;
    private String 	beforeCloseScript;
    private String 	afterCloseScript;
	
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
	 * Gets the name of the data set
	 * @return Name of data set.
	 */
	public String getName( )
	{
		return name;
	}
	
	/**
	 * Returns the data source name for this data set. 
	 * @return Name of the data source (connection) for this data set. If null, no data source
	 * is specified for this data set
	 */	
	public String getDataSourceName( )
	{
		return dataSourceName;
	}

	/**
	 * Specifies the data source (connection) name.
	 * @param dataSource The name of the dataSource to set.
	 */
	public void setDataSource( String dataSourceName ) 
	{
		this.dataSourceName = dataSourceName;
	}
	
	/**
	 * Returns a list of computed columns. Contains
	 * ComputedColumn objects. Computed columns must be computed before
	 * applying filters.
	 * @return the computed columns.  
	 * 			An empty list if no computed columns are defined
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
	 * Returns a list of filters. The List contains Filter objects. The data set should discard any
	 * row that does not satisfy all the filters.
	 * @return the filters.  An empty list if no filters are defined.
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
	 * Returns the input parameter definitions as a list
	 * of InputParameterDefn objects. 
	 * @return the input parameter definitions.  
	 * 			An empty list if no input parameter are defined.
	 */	
	public List getInputParameters( )
	{
	    if ( inputParameters == null )
	        inputParameters = new ArrayList();
		return inputParameters;
	}
	
	/**
	 * Adds an input paramter definition to the data set.
	 */
	public void addInputParameter( IInputParameterDefinition param )
	{
	    if ( param != null )
	        getInputParameters().add( param );
	}
	
	/**
	 * Returns the output parameter definitions as a list
	 * of OutputParameterDefn objects.
	 * @return the output parameter definitions. 
	 * 			An empty list if no output parameters are defined.
	 */
	public List getOutputParameters( )
	{
	    if ( outputParameters == null )
	        outputParameters = new ArrayList();
		return outputParameters;
	}
	
	/**
	 * Adds an output paramter definition to the list.
	 */
	public void addOutputParameter( IOutputParameterDefinition param )
	{
	    if ( param != null )
	        getOutputParameters().add( param );
	}
		
	/**
	 * Returns the result set hints definition as a list of ColumnDefn
	 * objects. Returns null if this data set does not provide a result set
	 * hints definition. (A null pointer usually means that the data set definition
	 * can provide the definition from the underlying implementation.) 
	 * @return the result set hints definition.
	 * 			An empty list if none is defined.
	 */	
	public List getResultSetHints()
	{
	    if ( resultSetHints == null )
	        resultSetHints = new ArrayList();
		return resultSetHints;
	}
	
	/**
	 * Adds a column to the result set hints definition.
	 * @param col
	 */
	public void addResultSetHint( IColumnDefinition col )
	{
	    if ( col != null )
	        getResultSetHints().add( col );
	}

	/**
	 * Returns the set of input parameter bindings as an unordered collection
	 * of IInputParamBinding objects.
	 * @return	the data set's input parameter bindings.
	 * 			An empty collection if none is defined.
	 */
	public Collection getInputParamBindings()
	{
	    if ( inputParamBindings == null )
	        inputParamBindings = new ArrayList();
	    return inputParamBindings;
	}
	
	/**
	 * Adds an IInputParamBinding to the set of input parameter bindings.
	 * Ignores given binding if null.
	 * @param binding	Could be null.
	 */
	public void addInuptParamBinding( IInputParameterBinding binding )
	{
	    if ( binding != null )
	        getInputParamBindings().add( binding );
	}
	
	/**
	 * Returns the BeforeOpen script to be called just before opening the data
	 * set.
	 * @return the BeforeOpen script
	 */	
	public String getBeforeOpenScript( )
	{
		return beforeOpenScript;
	}

	/**
	 * Assigns the BeforeOpen script.
	 * @param beforeOpenScript The BeforeOpen script to set.
	 */
	public void setBeforeOpenScript( String beforeOpenScript ) 
	{
		this.beforeOpenScript = beforeOpenScript;
	}
	
	/**
	 * Returns the AfterOpen script to be called just after the data set is
	 * opened, but before fetching each row.
	 * @return the AfterOpen script
	 */	
	public String getAfterOpenScript( )
	{
		return afterOpenScript;
	}

	/**
	 * Assigns the AfterOpen script.
	 * @param afterOpenScript The AfterOpen script to set.
	 */
	public void setAfterOpenScript( String afterOpenScript ) 
	{
		this.afterOpenScript = afterOpenScript;
	}
	
	/**
	 * Returns the OnFetch script to be called just after the a row is read
	 * from the data set. Called after setting computed columns and only for
	 * rows that pass the filters. (Not called for rows that are filtered out
	 * of the data set.)
	 * @return the OnFetch script
	 */
	public String getOnFetchScript( )
	{
		return onFetchScript;
	}
	
	/**
	 * Specifies the OnFetch script.
	 * @param onFetchScript The OnFetch script to set.
	 */
	public void setOnFetchScript( String onFetchScript ) 
	{
		this.onFetchScript = onFetchScript;
	}
	
	/**
	 * Returns the before-close script to be called just before closing the
	 * data set.
	 * @return the before-close script
	 */	
	public String getBeforeCloseScript( )
	{
		return beforeCloseScript;
	}
	
	/**
	 * Specifies the BeforeClose script.
	 * @param beforeCloseScript The BeforeClose script to set.
	 */
	public void setBeforeCloseScript( String beforeCloseScript ) 
	{
		this.beforeCloseScript = beforeCloseScript;
	}

	/**
	 * Returns the AfterClose script to be called just after the data set is
	 * closed.
	 * @return the AfterClose script
	 */	
	public String getAfterCloseScript( )
	{
		return afterCloseScript;
	}

	/**
	 * Specifies the AfterClose script.
	 * @param afterCloseScript The AfterClose script to set.
	 */
	public void setAfterCloseScript( String afterCloseScript ) 
	{
		this.afterCloseScript = afterCloseScript;
	}

}
