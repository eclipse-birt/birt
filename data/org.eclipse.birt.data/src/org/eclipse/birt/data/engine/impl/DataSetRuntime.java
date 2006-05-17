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
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.DataRow;
import org.eclipse.birt.data.engine.script.DataSetJSEventHandler;
import org.eclipse.birt.data.engine.script.JSDataSetImpl;
import org.eclipse.birt.data.engine.script.JSOutputParams;
import org.eclipse.birt.data.engine.script.JSResultSetRow;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.JSRows;
import org.eclipse.birt.data.engine.script.ScriptDataSetJSEventHandler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Encapsulates a runtime data set definition. A data set definition
 * has two parts: design time properties specified in the report design that are
 * static, and runtime properties (e.g., SQL query statement) that
 * can be changed in scripts.
 * A data set runtime also maintains the current data row. The row can come from 
 * either of these two sources at any given time: a IResultIterator (after result set has been generated), 
 * or from a IResultObject (during data set processing).
 */

public class DataSetRuntime implements IDataSetInstanceHandle
{
	/** Static design of data set */
	private IBaseDataSetDesign	dataSetDesign;
	/** Javascript object implementing the DataSet class */
	private Scriptable		jsDataSetObject;
	
	protected IQueryExecutor queryExecutor;
	protected static Logger logger = Logger.getLogger( DataSetRuntime.class.getName( ) );

	// Fields related to current data row
	/** IResultObject which is the current data row */
    protected IResultObject resultObj;
    /** The result iterator whose iterator position is the current data row */
    protected IResultIterator resultSet;
    /** Internal index of current data row */ 
    protected int currentRowIndex = -1;
    /** Whether update to current data row is allowed */
    protected boolean	allowUpdateRowData = false;
    /** Metadata of curent row */
    protected IResultMetaData rowMetaData;
	
    /** Scriptable object implementing the Javascript "row" property */
    private JSRowObject jsRowObject;
    /** Scriptable object implementing the Javascript "rows" property */
    private JSRows jsRowsObject;
    /** Object implementing IDataRow interface */
    private DataRow dataRow;
    /** Scriptable object implementing the Javascript "outputParams" property */
    private JSOutputParams jsOutputParamsObject;
    /** Scriptable object implementing the internal "_aggr_value" property */
    private Scriptable jsAggrValueObject;
    private Scriptable jsTempAggrValueObject;   
    private IBaseDataSetEventHandler eventHandler;
    protected boolean isOpen;

	protected DataSetRuntime( IBaseDataSetDesign dataSetDesign,
			IQueryExecutor queryExecutor )
	{
		this.dataSetDesign = dataSetDesign;
		this.queryExecutor = queryExecutor;
		isOpen = true;
		
		if ( dataSetDesign != null )
			eventHandler = dataSetDesign.getEventHandler();
		
		/*
		 * TODO: TEMPORARY the follow code is temporary. It will be removed once Engine takes over
		 * script execution from DtE
		 */
		if ( eventHandler == null )
		{
			if ( dataSetDesign instanceof IScriptDataSetDesign )
				eventHandler = new ScriptDataSetJSEventHandler( 
						(IScriptDataSetDesign) dataSetDesign );
			else if ( dataSetDesign instanceof IOdaDataSetDesign)
				eventHandler = new DataSetJSEventHandler( dataSetDesign );
		}
		/*
		 * END Temporary 
		 */
	}

	public IQueryExecutor getQueryExecutor()
	{
		return queryExecutor;
	}
	
	/**
	 * Gets the instance of the Javascript 'row' object for this data set
	 */
	public Scriptable getJSRowObject()
	{
		if ( !isOpen )
			return null;
		if ( this.jsRowObject == null )
		{
			jsRowObject = new JSRowObject( this );
		}
		return jsRowObject;
	}

	/**
	 * Gets the instance of the Javascript 'outputParams' object for this data set
	 */
	public Scriptable getJSOutputParamsObject()
	{
		if ( !isOpen )
			return null;
		if ( jsOutputParamsObject == null )
		{
			jsOutputParamsObject = new JSOutputParams( this );
		}
		return jsOutputParamsObject;
	}
	
	/**
	 * Gets the instance of the Javascript 'rows' object for this data set
	 */
	public Scriptable getJSRowsObject() throws DataException
	{
		if ( !isOpen )
			return null;
		if ( this.jsRowsObject == null )
		{
			// Construct an array of nested data sets
			int size = queryExecutor.getNestedLevel( );
			IQueryExecutor executor = queryExecutor;									
			DataSetRuntime[] dataSets = new DataSetRuntime[ size ];			
			dataSets[size - 1] = executor.getDataSet( );			
			if ( size - 1 > 0 )
			{
				DataSetRuntime[] innerDSs = executor.getNestedDataSets( size - 1 );
				for ( int i = 0; i < size - 1; i++ )
					dataSets[i] = innerDSs[i];
			}
			jsRowsObject = new JSRows( dataSets );
		}
		return jsRowsObject;
	}
	
	public IDataRow getDataRow() 
	{
		if ( !isOpen )
			return null;
		if (this.dataRow == null)
		{
			this.dataRow = new DataRow( this );
		}
		return dataRow;
	}
	
	/**
	 * @return Event handler for this data set 
	 */
	protected IBaseDataSetEventHandler getEventHandler()
	{
		return eventHandler;
	}
	
	/**
	 * Gets the IBaseDataSetDesign object which defines the design time properties
	 * associated with this data set
	 */
	protected IBaseDataSetDesign getDesign()
	{
		return dataSetDesign;
	}

	/**
	 * Gets the name of the design time properties
	 * associated with this data set
	 */
	public String getName()
	{
		if ( dataSetDesign != null)
			return dataSetDesign.getName();
		else
			return null;
	}
	
    /**
     * @return cache row count
     */
    public int getCacheRowCount( )
    {
    	if ( dataSetDesign != null )
			return dataSetDesign.getCacheRowCount( );
		return 0;
    }
    
    /**
	 * @return
	 */
	public boolean needDistinctValue( )
	{
		if ( dataSetDesign != null )
			return dataSetDesign.needDistinctValue( );
		return false;
	}
	
	public String getDataSourceName()
	{
		if ( dataSetDesign != null)
		    return dataSetDesign.getDataSourceName();
		else
			return null;
	}
	
	/**
	 * Gets the runtime Data Source definition for this data set
	 */
	public IDataSourceInstanceHandle getDataSource()
	{
		return this.queryExecutor.getDataSourceInstanceHandle( );
	}

	/**
	 * Creates an instance of the appropriate subclass based on a specified
	 * design-time data set definition
	 * @param dataSetDefn Design-time data set definition.
	 */
	public static DataSetRuntime newInstance( IBaseDataSetDesign dataSetDefn,
			IQueryExecutor queryExecutor ) throws DataException
	{
		DataSetRuntime dataSet = null;
		if ( dataSetDefn instanceof IOdaDataSetDesign )
		{
			dataSet = new OdaDataSetRuntime( (IOdaDataSetDesign) dataSetDefn, queryExecutor );
		}
		else if ( dataSetDefn instanceof IScriptDataSetDesign )
		{
			dataSet = new ScriptDataSetRuntime( (IScriptDataSetDesign) dataSetDefn, queryExecutor );
		}
		else if ( dataSetDefn instanceof IJointDataSetDesign )
		{
			dataSet = new DataSetRuntime(dataSetDefn, queryExecutor);
		}
		else
		{
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE );
		}
		
		return dataSet;
	}
	
	/**
	 * Gets the Data Engine
	 */
	public Scriptable getSharedScope()
	{
		return queryExecutor.getSharedScope( );
	}
	
	/**
	 * Gets the Javascript object that wraps this data set runtime 
	 */
	public Scriptable getJSDataSetObject()
	{
		// JS wrapper is created on demand
		if ( jsDataSetObject == null )
		{
			Scriptable topScope = queryExecutor.getSharedScope();
			Context.enter();
			try
			{
				jsDataSetObject = (Scriptable) Context.javaToJS( 
							new JSDataSetImpl( this ), topScope );
				jsDataSetObject.setParentScope( topScope );
				jsDataSetObject.setPrototype( topScope );
			}
			finally
			{
				Context.exit();
			}
		}
		return jsDataSetObject;
	}
	
	public Scriptable getJSDataSetRowObject( )
	{
		return this.getJSRowObject( );
	}
	
	private JSResultSetRow resultSetRow;
	
	public void setJSResultSetRow( JSResultSetRow resultSetRow )
	{
		this.resultSetRow = resultSetRow;
	}
	
	public Scriptable getJSResultRowObject()
	{
		if ( !isOpen )
			return null;
		
		if ( resultSetRow == null )
			return this.getJSRowObject( );

		return this.resultSetRow;
	}
		
	/**
	 * Gets the internal Javascript aggregate value object
	 */
	public Scriptable getJSAggrValueObject()
	{
		if ( !isOpen )
			return null;
		if (jsAggrValueObject == null )
		{
			jsAggrValueObject = queryExecutor.getJSAggrValueObject( );
		}
		return jsAggrValueObject;
	}

	/**
	 * Gets the internal aggregate helper object
	 * TODO: review the necessity of this object
	 */
	public Scriptable getJSTempAggrValueObject()
	{
		return jsTempAggrValueObject;
	}
	
	/**
	 * Sets the internal aggregate helper object
	 * TODO: review the necessity of this object
	 */
	public void setJSTempAggrValueObject( Scriptable obj)
	{
		jsTempAggrValueObject = obj;
	}
	
	/**
	 * Returns a Javascript scope suitable for running JS event handler code.
	 * @see org.eclipse.birt.data.engine.api.script.IJavascriptContext#getScriptScope()
	 */
	public Scriptable getScriptScope()
	{
		// Data set event handlers are executed as methods on the DataSet object
		return getJSDataSetObject();
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData() throws DataException
	{
		if ( !isOpen )
			return null;
		return queryExecutor.getResultMetaData();
	}

	public Collection getInputParamBindings()
	{
		if ( dataSetDesign != null)
		    return dataSetDesign.getInputParamBindings();
		else
			return null;
	}
	
	public List getComputedColumns()
	{
		if ( dataSetDesign != null)
		    return dataSetDesign.getComputedColumns();
		else
			return null;
	}
	
	public List getFilters()
	{
		if ( dataSetDesign != null)
		    return dataSetDesign.getFilters();
		else
			return null;
	}

    public List getParameters()
    {
		if ( dataSetDesign != null)
	        return dataSetDesign.getParameters();
		else
			return null;
    }
   
	public List getResultSetHints()
	{
		if ( dataSetDesign != null)
			return dataSetDesign.getResultSetHints();
		else
			return null;
	}
	
	/** Executes the beforeOpen script associated with the data source */
	public void beforeOpen() throws DataException
	{
		if ( getEventHandler() != null )
		{
			try
			{
				getEventHandler().handleBeforeOpen( this );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** Executes the beforeClose script associated with the data source */
	public void beforeClose() throws DataException
	{
		if ( getEventHandler() != null )
		{
			try
			{
				getEventHandler().handleBeforeClose( this );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** Executes the afterOpen script associated with the data source */
	public void afterOpen() throws DataException
	{
		if ( getEventHandler() != null )
		{
			try
			{
				getEventHandler().handleAfterOpen( this );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** Executes the afterClose script associated with the data source */
	public void afterClose() throws DataException
	{
		if ( getEventHandler() != null )
		{
			try
			{
				getEventHandler().handleAfterClose( this );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** Executes the onFetch script associated with the data source */
	public void onFetch() throws DataException
	{
		if ( getEventHandler() != null )
		{
			try
			{
				getEventHandler().handleOnFetch( this, getDataRow() );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** 
	 * Performs custom action to close a data set.
	 * <p>
	 * beforeClose and afterClose event scripts are NOT run in this method */
	public void close() throws DataException
	{
		isOpen = false;
	}
	
    /**
     * Binds the row object to an odi result set. Exising binding
     * is replaced.
     * @param resultSet Odi result iterator to bind to
     * @param allowUpdate If true, update to current row's column values are allowed
     */
    public void setResultSet( IResultIterator resultSet, boolean allowUpdate )
    {
        assert resultSet != null;
        this.resultSet = resultSet;
        resultObj = null;
        this.allowUpdateRowData = allowUpdate;
        this.rowMetaData = null;
    }
    
    /**
     * Binds the row object to a IResultObject. Existing bindings 
     * is replaced
     * @param resultObj Result object to bind to.
     * @param allowUpdate If true, update to current row's column values are allowed
     */
    public void setRowObject( IResultObject resultObj, boolean allowUpdate ) 
    {
    	assert resultObj != null;
		this.resultObj = resultObj;
		resultSet = null;
        this.allowUpdateRowData = allowUpdate;
        this.rowMetaData = null;
    }

	/**
	 * Indicates the index of the current result row
	 */
	public void setCurrentRowIndex( int currentRowIndex )
	{
		this.currentRowIndex = currentRowIndex;
	}
	
    /**
     * Get result object from IResultObject or IResultSetIterator
     * @return current result object; can be null 
     */
    public IResultObject getCurrentRow( ) 
	{
		if ( !isOpen )
			return null;
		
		IResultObject resultObject;
		if ( resultSet != null )
		{
			try
			{
				resultObject = resultSet.getCurrentResult( );
			}
			catch ( DataException e )
			{
				resultObject = null;
			}
		}
		else
		{
			resultObject = resultObj;
		}
		return resultObject;
	}
	
	/**
	 * Gets value of row[0]
	 */
	public int getCurrentRowIndex( ) throws DataException
	{
		int rowID;
		if ( resultSet != null )
			rowID = resultSet.getCurrentResultIndex( );
		else
			rowID = this.currentRowIndex;

		return rowID;
	}
	
	public boolean allowUpdateRowData()
	{
		return this.allowUpdateRowData;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#getExtensionID()
	 */
	public String getExtensionID()
	{
		// Default implementation: no extension ID
		return "";
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#getAllExtensionProperties()
	 */
	public Map getAllExtensionProperties()
	{
		// Default implementation: no extension properties
		return null;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#getExtensionProperty(java.lang.String)
	 */
	public String getExtensionProperty(String name)
	{
		// Default implementation: no extension properties
		return null;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#getQueryText()
	 */
	public String getQueryText() throws BirtException
	{
		// Default implementation: no queryText support
		return null;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#setExtensionProperty(java.lang.String, java.lang.String)
	 */
	public void setExtensionProperty(String name, String value)
	{
		// Default implementation: no extension properties
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle#setQueryText(java.lang.String)
	 */
	public void setQueryText(String queryText) throws BirtException
	{
		// Default implementation: no queryText support
	}
	
	
}
