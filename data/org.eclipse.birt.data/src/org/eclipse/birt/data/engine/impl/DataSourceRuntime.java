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

import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IExtendedDataSourceDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.script.JSDataSource;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * This class encapulates runtime properties of a DtE data source. Certain data source
 * properties are updatable by scripts at runtime. Value of those properties are retained
 * by this class. Value for non-modifiable properties are delegated to the design object
 */
public abstract class DataSourceRuntime implements IBaseDataSourceDesign
{
	/** Associated data source design */
	private IBaseDataSourceDesign	design;
	
	/** Javascript DataSource object that wraps this data source */
	private Scriptable				jsObject;

	/** An open OdiDataSource associated with this data source
	   If null, this data source is not open */
	private IDataSource				odiDataSource = null;
	
	private DataEngineImpl			dataEngine;
	
	protected DataSourceRuntime( IBaseDataSourceDesign dataSource, DataEngineImpl dataEngine )
	{
		assert dataSource != null;
		design = dataSource;
		this.dataEngine = dataEngine;
	}
	
	/**
	 * Gets the IBaseDataSourceDesign object which defines the design time properties
	 * associated with this data source
	 */
	public IBaseDataSourceDesign getDesign()
	{
		return design;
	}

	/**
	 * Sets the IBaseDataSourceDesign object which defines the design time properties
	 * associated with this data source
	 */
	public void setDesign( IBaseDataSourceDesign design )
	{
		assert design != null;
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSourceDesign#getAfterCloseScript()
	 */
	public String getAfterCloseScript()
	{
		return design.getAfterCloseScript();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSourceDesign#getAfterOpenScript()
	 */
	public String getAfterOpenScript()
	{
		return design.getAfterOpenScript();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSourceDesign#getBeforeCloseScript()
	 */
	public String getBeforeCloseScript()
	{
		return design.getBeforeCloseScript();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSourceDesign#getBeforeOpenScript()
	 */
	public String getBeforeOpenScript()
	{
		return design.getBeforeOpenScript();
	}
	
	/**
	 * Gets a ROM Script DataSource object wrapper for this object
	 */
	public Scriptable getScriptable( )
	{
		// Script object is created on demand
		if ( jsObject == null )
		{
			jsObject = new JSDataSource(this, dataEngine.getSharedScope() ); 
		}
		return jsObject;
	}
	
	/**
	 * Called after data source is open, to set the associated Odi data source
	 */
	public void setOdiDataSource( IDataSource odiDataSource)
	{
		this.odiDataSource = odiDataSource;
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
		if ( dataSource instanceof IExtendedDataSourceDesign )
		{
			return new ExtendedDataSourceRuntime( (IExtendedDataSourceDesign) dataSource,
						dataEngine);
		}
		else if ( dataSource instanceof IScriptDataSourceDesign )
		{
			return new ScriptDataSourceRuntime( (IScriptDataSourceDesign) dataSource, dataEngine );
		}
		else
		{
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASOURCE_TYPE );
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
	
	protected Object runScript( String script, String eventName ) throws DataException
	{
		if ( script != null && script.length() > 0 )
		{
			Context cx = Context.enter();
			
			try
			{
				return ScriptEvalUtil.evaluateJSExpr( cx, getScriptable(), 
						script, 
						"DataSource:" + getName() + "." + eventName, 
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
