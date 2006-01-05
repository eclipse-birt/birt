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

import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.engine.api.script.eventhandler.IRowEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IRowInstance;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Row;
import org.eclipse.birt.report.engine.script.internal.instance.RowInstance;
import org.eclipse.birt.report.model.api.RowHandle;

public class RowScriptExecutor extends ScriptExecutor
{

	public static void handleOnPrepare( RowHandle rowHandle,
			ExecutionContext context )
	{
		try
		{
			IRow row = new Row( rowHandle );
			if ( handleJS( row, rowHandle.getOnPrepare( ), context ).didRun( ) )
				return;
			IRowEventHandler eh = getEventHandler( rowHandle, context );
			if ( eh != null )
				eh.onPrepare( row, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( RowContent content, IRowData rowData,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign rowDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IRowInstance row = new RowInstance( content, rowData, context );
			if ( handleJS( row, rowDesign.getOnCreate( ), context ).didRun( ) )
				return;
			IRowEventHandler eh = getEventHandler( rowDesign, context );
			if ( eh != null )
				eh.onCreate( row, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnRender( RowContent content, IRowData rowData,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign rowDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IRowInstance row = new RowInstance( content, rowData, context );
			if ( handleJS( row, rowDesign.getOnRender( ), context ).didRun( ) )
				return;
			IRowEventHandler eh = getEventHandler( rowDesign, context );
			if ( eh != null )
				eh.onRender( row, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IRowEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		RowHandle handle = ( RowHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static IRowEventHandler getEventHandler( RowHandle handle,
			ExecutionContext context )
	{
		IRowEventHandler eh = null;
		try
		{
			eh = ( IRowEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IRowEventHandler.class );
		}
		return eh;
	}
}
