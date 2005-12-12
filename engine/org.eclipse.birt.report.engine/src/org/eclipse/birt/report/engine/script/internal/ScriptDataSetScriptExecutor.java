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

import java.util.logging.Level;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetMetaDataDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler;
import org.eclipse.birt.report.engine.script.internal.instance.DataSetInstance;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;

public class ScriptDataSetScriptExecutor extends DataSetScriptExecutor
		implements IScriptDataSetEventHandler
{

	private static final String OPEN = "OPEN";

	private static final String CLOSE = "CLOSE";

	private static final String FETCH = "FETCH";
	
	private static final String DESCRIBE = "DESCRIBE";

	private IScriptedDataSetEventHandler scriptedEventHandler;

	public ScriptDataSetScriptExecutor( ScriptDataSetHandle dataSetHandle,
			IReportContext reportContext )
	{
		super( dataSetHandle, reportContext );
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
				log.log( Level.WARNING, e.getMessage( ), e );
			}
		}
	}

	public void handleOpen( IDataSetInstanceHandle dataSet )
			throws BirtException
	{
		try
		{
			JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
					dataSet.getName( ), OPEN,
					( ( ScriptDataSetHandle ) dataSetHandle ).getOpen( ) );
			if ( status.didRun( ) )
				return;
			if ( scriptedEventHandler != null )
				scriptedEventHandler.open( new DataSetInstance( dataSet ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public void handleClose( IDataSetInstanceHandle dataSet )
	{
		try
		{
			JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
					dataSet.getName( ), CLOSE,
					( ( ScriptDataSetHandle ) dataSetHandle ).getClose( ) );
			if ( status.didRun( ) )
				return;
			if ( scriptedEventHandler != null )
				scriptedEventHandler.close( new DataSetInstance( dataSet ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public boolean handleFetch( IDataSetInstanceHandle dataSet, IDataRow row )
	{
		try
		{
			JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
					dataSet.getName( ), FETCH,
					( ( ScriptDataSetHandle ) dataSetHandle ).getFetch( ) );
			if ( status.didRun( ) )
			{
				Object result = status.result( );
				if ( result instanceof Boolean )
					return ( ( Boolean ) result ).booleanValue( );
				else
					throw new DataException(
							ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE, "Fetch" );
			}
			if ( scriptedEventHandler != null )
				return scriptedEventHandler
						.fetch( new DataSetInstance( dataSet ), new DataSetRow( row ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
		return false;
	}


	public boolean handleDescribe(IDataSetInstanceHandle dataSet, IScriptDataSetMetaDataDefinition metaData) 
			throws BirtException 
	{
		try
		{
			JSScriptStatus status = handleJS( dataSet.getScriptScope( ),
					dataSet.getName( ), DESCRIBE,
					( ( ScriptDataSetHandle ) dataSetHandle ).getDescribe() );
			if ( status.didRun( ) )
			{
				Object result = status.result( );
				if ( result instanceof Boolean )
					return ( ( Boolean ) result ).booleanValue( );
				else
					throw new DataException(
							ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE, "Describe" );
			}
			if ( scriptedEventHandler != null )
				return scriptedEventHandler.describe( 
						new DataSetInstance( dataSet ), 
						new ScriptedDataSetMetaData( metaData ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
		return false;
	}

}
