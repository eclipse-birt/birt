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

import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Grid;
import org.eclipse.birt.report.engine.script.internal.instance.GridInstance;
import org.eclipse.birt.report.model.api.GridHandle;

public class GridScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( GridHandle gridHandle,
			ExecutionContext context )
	{
		try
		{
			IGrid grid = new Grid( gridHandle );
			if ( handleJS( grid, gridHandle.getOnPrepare( ), context ).didRun( ) )
				return;
			IGridEventHandler eh = getEventHandler( gridHandle, context );
			if ( eh != null )
				eh.onPrepare( grid, context.getReportContext( ) );
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
			ReportItemDesign gridDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IGridInstance grid = new GridInstance( content, context );
			if ( handleJS( grid, gridDesign.getOnCreate( ), context ).didRun( ) )
				return;
			IGridEventHandler eh = getEventHandler( gridDesign, context );
			if ( eh != null )
				eh.onCreate( grid, context.getReportContext( ) );
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
			ReportItemDesign gridDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IGridInstance grid = new GridInstance( content, context );
			if ( handleJS( grid, gridDesign.getOnRender( ), context ).didRun( ) )
				return;
			IGridEventHandler eh = getEventHandler( gridDesign, context );
			if ( eh != null )
				eh.onRender( grid, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IGridEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		GridHandle handle = ( GridHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static IGridEventHandler getEventHandler( GridHandle handle,
			ExecutionContext context )
	{
		IGridEventHandler eh = null;
		try
		{
			eh = ( IGridEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IGridEventHandler.class );
		}
		return eh;
	}
}
