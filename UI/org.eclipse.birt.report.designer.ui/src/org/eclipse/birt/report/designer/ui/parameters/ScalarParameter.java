/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Adaptor class of <code>IParameter</code>
 * 
 */

public abstract class ScalarParameter implements IParameter
{

	/**
	 * Parameter Group
	 */
	protected IParameterGroup group = null;

	private Object selectionValue = null;

	/**
	 * Scalar parameter handle.
	 */
	protected ScalarParameterHandle handle;

	/**
	 * engine task.
	 */
	protected IEngineTask engineTask;
	private Object oriDefaultValue;
	private Map taskContext;

	/**
	 * Constructor
	 * 
	 * @param handle
	 *            scalar parameter handle.
	 * @param engineTask
	 *            engine task.
	 */

	public ScalarParameter( ScalarParameterHandle handle, IEngineTask engineTask )
	{
		this.handle = handle;
		this.engineTask = engineTask;
		taskContext = engineTask.getAppContext( );
	}

	/**
	 * Gets values of parameter.
	 * 
	 * @return value list.
	 */
	public abstract List getValueList( );

	/**
	 * Gets selection value.
	 * 
	 * @return selection value.
	 */
	public Object getSelectionValue( )
	{
		return selectionValue;
	}

	/**
	 * Sets selection value.
	 * 
	 * @param value
	 */
	public void setSelectionValue( Object value )
	{
		selectionValue = value;
	}

	/**
	 * Sets parameter group
	 */
	public void setParentGroup( IParameterGroup group )
	{
		this.group = group;
	}

	/**
	 * Gets parameter group
	 */
	public IParameterGroup getParentGroup( )
	{
		return group;
	}

	public Object getDefaultObject()
	{
		return oriDefaultValue;
	}
	
	public IGetParameterDefinitionTask createParameterDefinitionTask()
	{
		IGetParameterDefinitionTask task = null;
		if (engineTask != null)
		{
			task = engineTask.getEngine( )
				.createGetParameterDefinitionTask( engineTask.getReportRunnable( ) );
		}
		if (taskContext != null)
		{
		
			Map context = new HashMap( );
			Iterator itor = taskContext.keySet( ).iterator( );
			while(itor.hasNext( ))
			{
				Object obj = itor.next( );
				context.put( obj, taskContext.get( obj ) );
			}
			// TODO replace with DtE constant
			context.put( "com.actuate.birt.data.linkeddatamodel.LinkedDataModelDataModeSize",
					ReportPlugin.getDefault( ).getPluginPreferences( ).getString( ReportPlugin.DATA_MODEL_MEMORY_LIMIT_PREFERENCE ) );
			task.setAppContext( context );
			
		}
		return task;
	}
	/**
	 * Gets default value.
	 * 
	 * @return default value
	 */

	public String getDefaultValue( )
	{
		IGetParameterDefinitionTask task = createParameterDefinitionTask( );
		try
		{
			Object obj = task.getDefaultValue( handle.getName( ) );
			if (obj == null)
			{
				return null;
			}
			if (obj instanceof Object[] )
			{
				Object[] objs = (Object[])obj;
				if (objs.length > 0)
				{
					oriDefaultValue = objs[0];
					return objs[0] != null ? objs[0].toString( ) : null;
				}
				else
				{
					return null;
				}
			}
			oriDefaultValue = obj;
			if (obj instanceof Date)
			{
				try
				{
					return DataTypeUtil.toString( obj );
				}
				catch ( BirtException e )
				{
					//return toString
				}
			}
			return obj.toString( );
		}
		finally
		{
			if ( task != null )
				task.close();
		}
	}
	
	public List getDefaultValues( )
	{
		IGetParameterDefinitionTask task = createParameterDefinitionTask( );

		try
		{
			Object obj =  task.getDefaultValue( handle.getName( ) );
			List retValue = new ArrayList();
			if (obj == null)
			{
				return retValue;
			}
			if (obj instanceof Object[])
			{
				Object[] objs = (Object[])obj;
				for (int i=0; i<objs.length; i++)
				{
					retValue.add( objs[i] );
				}
			}
			else if (obj instanceof Collection)
			{
				Collection collection = (Collection)obj;
				Iterator itor = collection.iterator( );
				while(itor.hasNext( ))
				{
					retValue.add( itor.next( ) );
				}
			}
			else
			{
				retValue.add( obj );
			}
			return retValue;
		}
		finally
		{
			if ( task != null )
				task.close();
		}
	}

	/**
	 * Get parameter handle.
	 * 
	 * @return parameter handle.
	 */

	public ScalarParameterHandle getHandle( )
	{
		return handle;
	}

	public String format( String input ) throws BirtException
	{
		return ParameterUtil.format( handle, input );
	}

	public Object converToDataType( Object value ) throws BirtException
	{
		if ( value instanceof Object[] )
		{
			Object[] values = (Object[]) value;
			Object[] rtValues = new Object[values.length];
			for ( int i = 0; i < values.length; i++ )
				rtValues[i] = ParameterUtil.convert( values[i],
						handle.getDataType( ) );
			return rtValues;
		}
		return ParameterUtil.convert( value, handle.getDataType( ) );
	}

	/**
	 * Gets isRequired property.
	 * 
	 * @return
	 */
	public boolean isRequired( )
	{
		return handle.isRequired( );
	}

}
