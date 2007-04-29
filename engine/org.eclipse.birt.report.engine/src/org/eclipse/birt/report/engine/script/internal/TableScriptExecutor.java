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

import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ITableInstance;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Table;
import org.eclipse.birt.report.engine.script.internal.instance.TableInstance;
import org.eclipse.birt.report.model.api.TableHandle;

public class TableScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( TableHandle tableHandle,
			ExecutionContext context )
	{
		try
		{
			ITable table = new Table( tableHandle );
			ITableEventHandler eh = getEventHandler( tableHandle, context );
			if ( eh != null )
				eh.onPrepare( table, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( ITableContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign tableDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ITableInstance table = new TableInstance( content, context );
			if ( handleJS( table, tableDesign.getOnCreate( ), context )
					.didRun( ) )
				return;
			ITableEventHandler eh = getEventHandler( tableDesign, context );
			if ( eh != null )
				eh.onCreate( table, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnRender( ITableContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign tableDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ITableInstance table = new TableInstance( content, context );
			if ( handleJS( table, tableDesign.getOnRender( ), context )
					.didRun( ) )
				return;
			ITableEventHandler eh = getEventHandler( tableDesign, context );
			if ( eh != null )
				eh.onRender( table, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnPageBreak( ITableContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign tableDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ITableInstance table = new TableInstance( content, context );
			if ( handleJS( table, tableDesign.getOnPageBreak( ), context )
					.didRun( ) )
				return;
			ITableEventHandler eh = getEventHandler( tableDesign, context );
			if ( eh != null )
				eh.onPageBreak( table, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static ITableEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		TableHandle handle = ( TableHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static ITableEventHandler getEventHandler( TableHandle handle,
			ExecutionContext context )
	{
		ITableEventHandler eh = null;
		try
		{
			eh = ( ITableEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					ITableEventHandler.class );
		}
		return eh;
	}
}
