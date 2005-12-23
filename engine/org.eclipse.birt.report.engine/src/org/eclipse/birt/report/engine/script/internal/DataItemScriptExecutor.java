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

import org.eclipse.birt.report.engine.api.script.element.IDataItem;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataItemEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.DataItem;
import org.eclipse.birt.report.engine.script.internal.instance.DataItemInstance;
import org.eclipse.birt.report.model.api.DataItemHandle;

public class DataItemScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( DataItemHandle dataItemHandle,
			ExecutionContext context )
	{
		try
		{
			IDataItem dataItem = new DataItem( dataItemHandle );
			if ( handleJS( dataItem, dataItemHandle.getOnPrepare( ), context )
					.didRun( ) )
				return;
			IDataItemEventHandler eh = ( IDataItemEventHandler ) getInstance( dataItemHandle );
			if ( eh != null )
				eh.onPrepare( dataItem, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( DataContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign dataItemDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IDataItemInstance dataItem = new DataItemInstance( content, context );
			if ( handleJS( dataItem, dataItemDesign.getOnCreate( ), context )
					.didRun( ) )
				return;
			IDataItemEventHandler eh = ( IDataItemEventHandler ) getInstance( ( DataItemHandle ) dataItemDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( dataItem, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( DataContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign dataItemDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IDataItemInstance dataItem = new DataItemInstance( content, context );
			if ( handleJS( dataItem, dataItemDesign.getOnRender( ), context )
					.didRun( ) )
				return;
			IDataItemEventHandler eh = ( IDataItemEventHandler ) getInstance( ( DataItemHandle ) dataItemDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( dataItem, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
