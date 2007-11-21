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

import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataSetEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.instance.DataSetInstance;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.mozilla.javascript.Scriptable;

public class DataSetScriptExecutor extends DtEScriptExecutor implements
		IBaseDataSetEventHandler
{

	private static final String ON_FETCH = "onFetch";

	protected DataSetHandle dataSetHandle;

	protected IDataSetEventHandler eventHandler;
	
	private boolean useOnFetchEventHandler = false;
	private boolean useAfterCloseEventHandler = false;
	private boolean useAfterOpenEventHandler = false;
	private boolean useBeforeOpenEventHandler = false;
	private boolean useBeforeCloseEventHandler = false;

	public DataSetScriptExecutor( DataSetHandle dataSetHandle,
			ExecutionContext context )
	{
		super( context );
		this.dataSetHandle = dataSetHandle;
		String className = dataSetHandle.getEventHandlerClass( );
		initEventHandler( className );
		useOnFetchEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getOnFetch( ) );
		useAfterCloseEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getAfterClose( ) );
		useAfterOpenEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getAfterOpen( ) );
		useBeforeOpenEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getBeforeOpen( ) );
		useBeforeCloseEventHandler = ScriptTextUtil.isNullOrComments( dataSetHandle.getBeforeClose( ) );
	}

	protected void initEventHandler( String className )
	{
		if ( className != null )
		{
			try
			{
				eventHandler = ( IDataSetEventHandler ) getInstance( className,
						context );
			} catch ( ClassCastException e )
			{
				addClassCastException( context, e, className,
						IScriptedDataSetEventHandler.class );
			}
		}
	}

	public void handleBeforeOpen( IDataSetInstanceHandle dataSet )
	{
		if ( reportContext == null )
			return;
		try
		{
			if ( !this.useBeforeOpenEventHandler )
			{
				JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
						dataSet.getName( ),
						BEFORE_OPEN,
						dataSetHandle.getBeforeOpen( ) );
				if ( status.didRun( ) )
					return;
			}
			if ( eventHandler != null )
				eventHandler.beforeOpen( new DataSetInstance( dataSet ),
						reportContext );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public void handleBeforeClose( IDataSetInstanceHandle dataSet )
	{
		if ( reportContext == null )
			return;
		try
		{
			if ( !this.useBeforeCloseEventHandler )
			{
				JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
						dataSet.getName( ),
						BEFORE_CLOSE,
						dataSetHandle.getBeforeClose( ) );
				if ( status.didRun( ) )
					return;
			}
			if ( eventHandler != null )
				eventHandler.beforeClose( new DataSetInstance( dataSet ),
						reportContext );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public void handleAfterOpen( IDataSetInstanceHandle dataSet )
	{
		if ( reportContext == null )
			return;
		try
		{
			if ( !this.useAfterOpenEventHandler )
			{
				JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
						dataSet.getName( ),
						AFTER_OPEN,
						dataSetHandle.getAfterOpen( ) );
				if ( status.didRun( ) )
					return;
			}
			if ( eventHandler != null )
				eventHandler.afterOpen( new DataSetInstance( dataSet ),
						reportContext );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public void handleAfterClose( IDataSetInstanceHandle dataSet )
	{
		if ( reportContext == null )
			return;
		try
		{
			if ( !this.useAfterCloseEventHandler )
			{
				JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
						dataSet.getName( ),
						AFTER_CLOSE,
						dataSetHandle.getAfterClose( ) );
				if ( status.didRun( ) )
					return;
			}
			if ( eventHandler != null )
				eventHandler.afterClose( reportContext );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public void handleOnFetch( IDataSetInstanceHandle dataSet, IDataRow row )
	{
		if ( reportContext == null )
			return;
		try
		{
			if ( !this.useOnFetchEventHandler )
			{
				JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
						dataSet.getName( ),
						ON_FETCH,
						dataSetHandle.getOnFetch( ) );
				if ( status.didRun( ) )
					return;
			}
			if ( eventHandler != null )
				eventHandler.onFetch( new DataSetInstance( dataSet ),
						new DataSetRow( row ), reportContext );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	protected JSScriptStatus handleJS( Scriptable scope, String name,
			String method, String script )
	{
		return handleJS( scope, DATA_SET, name, method, script );
	}

}
