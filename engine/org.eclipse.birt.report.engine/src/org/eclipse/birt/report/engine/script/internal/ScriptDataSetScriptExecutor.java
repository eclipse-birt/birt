/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetMetaDataDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.instance.DataSetInstance;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ScriptDataSetScriptExecutor extends DataSetScriptExecutor
		implements IScriptDataSetEventHandler
{

	private static final String OPEN = "OPEN";

	private static final String CLOSE = "CLOSE";

	private static final String FETCH = "FETCH";

	private static final String DESCRIBE = "DESCRIBE";

	private IScriptedDataSetEventHandler scriptedEventHandler;
	
	private boolean useOpenEventHandler = false;
	private boolean useFetchEventHandler = false;
	private boolean useCloseEventHandler = false;
	private boolean useDescribeEventHandler = false;
	
	public ScriptDataSetScriptExecutor( ScriptDataSetHandle dataSetHandle,
			ExecutionContext context ) throws BirtException
	{
		super( dataSetHandle, context );
		useOpenEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getOpen( ) );
		useFetchEventHandler = ScriptTextUtil.isNullOrComments ( dataSetHandle.getFetch( ) );
		useCloseEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getClose( ) );
		useDescribeEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getDescribe( ) );
	}

	protected void initEventHandler( String className )
	{
		super.initEventHandler( className );
		if ( eventHandler != null )
		{
			try
			{
				scriptedEventHandler = ( IScriptedDataSetEventHandler ) eventHandler;
			} catch ( ClassCastException e )
			{
				addClassCastException( context, e, dataSetHandle,
						IScriptedDataSetEventHandler.class );
			}
		}
	}

	public void handleOpen( IDataSetInstanceHandle dataSet )
			throws BirtException
	{
		try
		{
			if ( !this.useOpenEventHandler )
			{
				ScriptStatus status = handleJS( getScriptScope( dataSet ),
						dataSet.getName( ),
						OPEN,
						( (ScriptDataSetHandle) dataSetHandle ).getOpen( ) );
				if ( status.didRun( ) )
					return;
			}
			if ( scriptedEventHandler != null )
				scriptedEventHandler.open( new DataSetInstance( dataSet ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}
	
	public void handleClose( IDataSetInstanceHandle dataSet )
	{
		try
		{
			if ( !this.useCloseEventHandler )
			{
				ScriptStatus status = handleJS( getScriptScope( dataSet ),
						dataSet.getName( ),
						CLOSE,
						( (ScriptDataSetHandle) dataSetHandle ).getClose( ) );
				if ( status.didRun( ) )
					return;
			}
			if ( scriptedEventHandler != null )
				scriptedEventHandler.close( new DataSetInstance( dataSet ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public boolean handleFetch( IDataSetInstanceHandle dataSet, IDataRow row )
	{
		try
		{
			if ( !useFetchEventHandler )
			{
				ScriptStatus status = handleJS( getScriptScope( dataSet ),
						dataSet.getName( ),
						FETCH,
						( (ScriptDataSetHandle) dataSetHandle ).getFetch( ) );
				if ( status.didRun( ) )
				{
					Object result = status.result( );
					if ( result instanceof Boolean )
						return ( (Boolean) result ).booleanValue( );
					else
						throw new DataException( ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE,
								"Fetch" );
				}
			}
			if ( scriptedEventHandler != null )
				return scriptedEventHandler.fetch(
						new DataSetInstance( dataSet ),
						new UpdatableDataSetRow( row ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
		return false;
	}

	public boolean handleDescribe( IDataSetInstanceHandle dataSet,
			IScriptDataSetMetaDataDefinition metaData ) throws BirtException
	{
		try
		{
			if ( !this.useDescribeEventHandler )
			{
				ScriptStatus status = handleJS( getScriptScope( dataSet ),
						dataSet.getName( ),
						DESCRIBE,
						( (ScriptDataSetHandle) dataSetHandle ).getDescribe( ) );
				if ( status.didRun( ) )
				{
					Object result = status.result( );
					if ( result instanceof Boolean )
						return ( (Boolean) result ).booleanValue( );
					else
						throw new DataException( ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE,
								"Describe" );
				}
			}
			if ( scriptedEventHandler != null )
				return scriptedEventHandler.describe( new DataSetInstance(
						dataSet ), new ScriptedDataSetMetaData( metaData ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
		return false;
	}
	
	private Scriptable getScriptScope( IDataSetInstanceHandle dataSet )
	{
		Scriptable shared = this.scope;
		Scriptable scope = (Scriptable) Context.javaToJS( new DataSetInstance( dataSet ),
				shared);
		scope.setParentScope( shared );
		scope.setPrototype( dataSet.getScriptScope( ) );
		return scope;
	}

}
