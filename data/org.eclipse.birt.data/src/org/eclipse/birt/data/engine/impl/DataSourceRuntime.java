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

package org.eclipse.birt.data.engine.impl;

import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IBaseDataSourceEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.script.JSDataSource;
import org.eclipse.birt.data.engine.script.DataSourceJSEventHandler;
import org.eclipse.birt.data.engine.script.ScriptDataSourceJSEventHandler;
import org.mozilla.javascript.Scriptable;

/**
 * This class encapulates runtime properties of a DtE data source. Certain data source
 * properties are updatable by scripts at runtime. Value of those properties are retained
 * by this class. Value for non-modifiable properties are delegated to the design object
 */
public abstract class  DataSourceRuntime implements IDataSourceInstanceHandle
{
	/** Associated data source design */
	private IBaseDataSourceDesign	design;
	
	/** Javascript DataSource object that wraps this data source */
	private Scriptable				jsDataSourceObject;

	/** An open OdiDataSource associated with this data source
	   If null, this data source is not open */
	private IDataSource				odiDataSource = null;
	
	protected DataEngineImpl		dataEngine;
	
	protected static Logger logger = Logger.getLogger( DataSourceRuntime.class.getName( ) );
	
	private IBaseDataSourceEventHandler eventHandler;
	
	protected DataSourceRuntime( IBaseDataSourceDesign dataSourceDesign, DataEngineImpl dataEngine )
	{
		assert dataSourceDesign != null;
		design = dataSourceDesign;
		this.dataEngine = dataEngine;
		eventHandler = dataSourceDesign.getEventHandler();
		
		/*
		 * TODO: TEMPORARY the follow code is temporary. It will be removed once Engine takes over
		 * script execution from DtE
		 */
		if ( eventHandler == null )
		{
			if ( dataSourceDesign instanceof IScriptDataSourceDesign )
				eventHandler = new ScriptDataSourceJSEventHandler( 
						(IScriptDataSourceDesign) dataSourceDesign );
			else
				eventHandler = new DataSourceJSEventHandler( dataSourceDesign );
		}
		/*
		 * END Temporary 
		 */
	}
	
	protected IBaseDataSourceEventHandler getEventHandler()
	{
		return eventHandler;
	}
	
	/**
	 * Gets the IBaseDataSourceDesign object which defines the design time properties
	 * associated with this data source
	 */
	public IBaseDataSourceDesign getDesign()
	{
		return design;
	}

	public void setDesign( IBaseDataSourceDesign design )
	{
		this.design = design;
	}
	
	/**
	 * Gets the name of the design time properties
	 * associated with this data source
	 */
	public String getName()
	{
		return design.getName();
	}
	
	/**
	 * Gets a ROM Script DataSource object wrapper for this object
	 */
	public Scriptable getJSDataSourceObject( )
	{
		// Script object is created on demand
		if ( jsDataSourceObject == null )
		{
			jsDataSourceObject = new JSDataSource(this, dataEngine.getSharedScope()); 
		}

		return jsDataSourceObject;
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.api.script.IJavascriptContext#getScriptScope()
	 */
	public Scriptable getScriptScope()
	{
		// Data source event handlers are executed as methods on the DataSet object
		return getJSDataSourceObject();
	}
	
	/**
	 * Opens the specified odiDataSource and associate it with this data source runtime.
	 * Event scripts associated with this data source are NOT run in this method
	 */
	public void openOdiDataSource( IDataSource odiDataSource ) throws DataException
	{
		odiDataSource.open();
		this.odiDataSource = odiDataSource;
	}
	
	/**
	 * Closes the associated odiDataSource. Event scripts associated with this data source
	 * are NOT runt in this method
	 */
	public void closeOdiDataSource() throws DataException
	{
		if ( odiDataSource != null )
		{
			odiDataSource.close();
			odiDataSource = null;
		}
	}
	
	/**
	 * Gets the associated odi data source. If null, data source is not open 
	 */
	public IDataSource getOdiDataSource( )
	{
		return this.odiDataSource;
	}
	
	/**
	 * Creates an instance of the appropriate subclass based on a specified
	 * design-time data source definition
	 * @param dataSetDefn Design-time data source definition.
	 */
	public static DataSourceRuntime newInstance( IBaseDataSourceDesign dataSource, DataEngineImpl dataEngine ) 
			throws DataException
	{
		if ( dataSource instanceof IOdaDataSourceDesign )
		{
			return new OdaDataSourceRuntime( (IOdaDataSourceDesign) dataSource,
						dataEngine);
		}
		else if ( dataSource instanceof IScriptDataSourceDesign )
		{
			return new ScriptDataSourceRuntime( (IScriptDataSourceDesign) dataSource, dataEngine );
		}
		else
		{
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASOURCE_TYPE,
					dataSource.getName() );
		}
	}
	
	/**
	 * Returns true if data source is currently open. 
	 */
	public boolean isOpen()
	{
		// A data source is open if it has an associated odi data source
		return odiDataSource != null;
	}
	
	/** Executes the beforeOpen script associated with the data source */
	public void beforeOpen() throws DataException
	{
		if ( eventHandler != null )
		{
			try
			{
				eventHandler.handleBeforeOpen( this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** Executes the beforeClose script associated with the data source */
	public void beforeClose() throws DataException
	{
		if ( eventHandler != null )
		{
			try
			{
				eventHandler.handleBeforeClose( this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** Executes the afterOpen script associated with the data source */
	public void afterOpen() throws DataException
	{
		if ( eventHandler != null )
		{
			try
			{
				eventHandler.handleAfterOpen( this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
	}
	
	/** Executes the afterClose script associated with the data source */
	public void afterClose() throws DataException
	{
		if ( eventHandler != null )
		{
			try
			{
				eventHandler.handleAfterClose( this );
			}
			catch (BirtException e)
			{
				throw DataException.wrap(e);
			}
		}
	}
	
}
