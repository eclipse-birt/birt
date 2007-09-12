/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.engine.api.script.eventhandler.IRowEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IRowInstance;
import org.eclipse.birt.report.engine.content.IRowContent;
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
			IRowEventHandler eh = getEventHandler( rowHandle, context );
			if ( eh != null )
				eh.onPrepare( row, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( IRowContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign rowDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			if ( !needOnCreate( rowDesign ) )
			{
				return;
			}
			IRowInstance row = new RowInstance( content,  context );
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

	public static void handleOnRender( IRowContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign rowDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			if ( !needOnRender( rowDesign ) )
			{
				return;
			}
			IRowInstance row = new RowInstance( content, context );
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

	public static void handleOnPageBreak( IRowContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign rowDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			if ( !needOnPageBreak( rowDesign ) )
			{
				return;
			}
			IRowInstance row = new RowInstance( content, context );
			if ( handleJS( row, rowDesign.getOnPageBreak( ), context ).didRun( ) )
				return;
			IRowEventHandler eh = getEventHandler( rowDesign, context );
			if ( eh != null )
				eh.onPageBreak( row, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IRowEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		try
		{
			return (IRowEventHandler) getInstance( design, context );
		}
		catch ( ClassCastException e )
		{
			addClassCastException( context, e, design.getJavaClass( ),
					IRowEventHandler.class );
		}
		return null;
	}

	private static IRowEventHandler getEventHandler( RowHandle handle,
			ExecutionContext context )
	{
		try
		{
			return (IRowEventHandler) getInstance( handle, context );
		}
		catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IRowEventHandler.class );
		}
		return null;
	}
}