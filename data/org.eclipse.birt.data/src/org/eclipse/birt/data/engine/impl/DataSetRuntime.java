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

package org.eclipse.birt.data.engine.impl;

import java.util.List;
import java.util.Collection;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.script.JSDataSet;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Encapsulates a runtime data set definition. A data set definition
 * has two parts: design time properties specified in the report design that are
 * static, and runtime properties (e.g., SQL query statement) that
 * can be changed in scripts.
 */

public abstract class DataSetRuntime implements IBaseDataSetDesign
{
	private IBaseDataSetDesign	m_design;
	
	private Scriptable		jsObject;
	private PreparedQuery.Executor queryExecutor;
	
	protected DataSetRuntime( IBaseDataSetDesign dataSet )
	{
		assert dataSet != null;
		m_design = dataSet;
	}

	public Scriptable getParentScope()
	{
		return queryExecutor.scope;
	}
	
	public Scriptable getJSRowObject()
	{
		return queryExecutor.rowObject;
	}
	
	public Scriptable getJSRowsObject()
	{
		return queryExecutor.rowsObject;
	}
	
	/**
	 * Gets the IBaseDataSetDesign object which defines the design time properties
	 * associated with this data set
	 */
	public IBaseDataSetDesign getDesign()
	{
		return m_design;
	}

	/**
	 * Sets the IBaseDataSetDesign object which defines the design time properties
	 * associated with this data set
	 */
	public void setDesign( IBaseDataSetDesign design )
	{
		m_design = design;
	}
	
	
	/**
	 * Gets the name of the design time properties
	 * associated with this data set
	 */
	public String getName()
	{
		return m_design.getName();
	}
	
	public String getDataSourceName()
	{
	    return m_design.getDataSourceName();
	}
	
	/**
	 * Gets the runtime Data Source definition for this data set
	 */
	public DataSourceRuntime getDataSource()
	{
		return this.queryExecutor.dataSource;
	}

	/**
	 * Creates an instance of the appropriate subclass based on a specified
	 * design-time data set definition
	 * @param dataSetDefn Design-time data set definition.
	 */
	public static DataSetRuntime newInstance( IBaseDataSetDesign dataSetDefn, 
			PreparedQuery.Executor queryExecutor ) throws DataException
	{
		DataSetRuntime dataSet = null;
		if ( dataSetDefn instanceof IOdaDataSetDesign )
		{
			dataSet = new OdaDataSetRuntime( (IOdaDataSetDesign) dataSetDefn );
		}
		else if ( dataSetDefn instanceof IScriptDataSetDesign )
		{
			dataSet = new ScriptDataSetRuntime( (IScriptDataSetDesign) dataSetDefn );
		}
		else
		{
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE );
		}
		
		dataSet.queryExecutor = queryExecutor;
		return dataSet;
	}
	
	/**
	 * Gets the Javascript object that wraps this data set runtime 
	 */
	public Scriptable getScriptable( )
	{
		// JS wrapper is created on deman
		if ( jsObject == null )
		{
			jsObject = new JSDataSet( this);
		}
		return jsObject;
	}
	
	public Collection getInputParamBindings()
	{
	    return m_design.getInputParamBindings();
	}
	
	public List getComputedColumns()
	{
	    return m_design.getComputedColumns();
	}
	
	public List getFilters()
	{
	    return m_design.getFilters();
	}

    public List getParameters()
    {
        return m_design.getParameters();
    }
	
    /**
     * @deprecated use getParameters()
     */
    public List getInputParameters()
    {
        return getParameters();
    }
	
    /**
     * @deprecated use getParameters()
     */
    public List getOutputParameters()
    {
        return getParameters();
    }
   
	public List getResultSetHints()
	{
		return m_design.getResultSetHints();
	}
	
	public String getAfterCloseScript()
	{
		return m_design.getAfterCloseScript();
	}
	
	public String getAfterOpenScript()
	{
		return m_design.getAfterOpenScript();
	}
	
	public String getBeforeCloseScript()
	{
		return m_design.getBeforeCloseScript();
	}
	
	public String getBeforeOpenScript()
	{
		return m_design.getBeforeOpenScript();
	}
	
	public String getOnFetchScript()
	{
		return m_design.getOnFetchScript();
	}
	
	/** Executes the beforeOpen script associated with the data source */
	public void beforeOpen() throws DataException
	{
		runScript( getBeforeOpenScript(), "beforeOpen" );
	}
	
	/** Executes the beforeClose script associated with the data source */
	public void beforeClose() throws DataException
	{
		runScript( getBeforeCloseScript(), "beforeClose" );
	}
	
	/** Executes the afterOpen script associated with the data source */
	public void afterOpen() throws DataException
	{
		runScript( getAfterOpenScript(), "afterOpen" );
	}
	
	/** Executes the afterClose script associated with the data source */
	public void afterClose() throws DataException
	{
		runScript( getAfterCloseScript(), "afterClose" );
	}
	
	/** Executes the onFetch script associated with the data source */
	public void onFetch() throws DataException
	{
		runScript( getAfterCloseScript(), "onFetch" );
	}
	
	/** Performs custom action to close a data set. beforeClose and afterClose
	 * event scripts are NOT run in this method */
	public void close() throws DataException
	{
		
	}
	
	protected Object runScript( String script, String eventName ) throws DataException
	{
		if ( script != null && script.length() > 0 )
		{
			Context cx = Context.enter();
			
			try
			{
				return ScriptEvalUtil.evaluateJSExpr( cx, getScriptable(), 
						script, 
						"DataSet:" + getName() + "." + eventName, 
						0 ); 
			}
			finally
			{
				Context.exit();
			}
		}
		return null;
	}
	
}
