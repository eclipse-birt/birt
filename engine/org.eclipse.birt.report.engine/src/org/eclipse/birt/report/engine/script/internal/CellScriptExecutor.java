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

import org.eclipse.birt.report.engine.api.script.element.ICell;
import org.eclipse.birt.report.engine.api.script.eventhandler.ICellEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ICellInstance;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Cell;
import org.eclipse.birt.report.engine.script.internal.instance.CellInstance;
import org.eclipse.birt.report.model.api.CellHandle;

public class CellScriptExecutor extends ScriptExecutor
{

	public static void handleOnPrepare( CellHandle cellHandle,
			ExecutionContext context )
	{
		try
		{
			ICell cell = new Cell( cellHandle );
			if ( handleJS( cell, cellHandle.getOnPrepare( ), context ).didRun( ) )
				return;
			ICellEventHandler eh = getEventHandler( cellHandle, context );
			if ( eh != null )
				eh.onPrepare( cell, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( ICellContent content,
			ExecutionContext context, boolean fromGrid )
	{
		try
		{
			Object generateBy = content.getGenerateBy( );
			if ( generateBy == null )
			{
				return;
			}
			ReportItemDesign cellDesign = ( ReportItemDesign ) generateBy;
			ICellInstance cell = new CellInstance( content, context,
					fromGrid );
			if ( handleJS( cell, cellDesign.getOnCreate( ), context ).didRun( ) )
				return;
			ICellEventHandler eh = getEventHandler( cellDesign, context );
			if ( eh != null )
				eh.onCreate( cell, context.getReportContext( ) );

		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnRender( ICellContent content,
			ExecutionContext context )
	{
		try
		{
			Object generateBy = content.getGenerateBy( );
			if ( generateBy == null )
			{
				return;
			}
			ReportItemDesign cellDesign = ( ReportItemDesign ) generateBy; 
			
			//fromGrid doesn't matter here since row data is null
			ICellInstance cell = new CellInstance( content, context,
					false );
			if ( handleJS( cell, cellDesign.getOnRender( ), context ).didRun( ) )
				return;
			ICellEventHandler eh = getEventHandler( cellDesign, context );
			if ( eh != null )
				eh.onRender( cell, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}
	
	public static void handleOnPageBreak( ICellContent content,
			ExecutionContext context )
	{
		try
		{
			Object generateBy = content.getGenerateBy( );
			if ( generateBy == null )
			{
				return;
			}
			ReportItemDesign cellDesign = ( ReportItemDesign ) generateBy; 
			
			//fromGrid doesn't matter here since row data is null
			ICellInstance cell = new CellInstance( content, context,
					false );
			if ( handleJS( cell, cellDesign.getOnPageBreak( ), context ).didRun( ) )
				return;
			ICellEventHandler eh = getEventHandler( cellDesign, context );
			if ( eh != null )
				eh.onPageBreak( cell, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static ICellEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		CellHandle handle = ( CellHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static ICellEventHandler getEventHandler( CellHandle handle,
			ExecutionContext context )
	{
		ICellEventHandler eh = null;
		try
		{
			eh = ( ICellEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					ICellEventHandler.class );
		}
		return eh;
	}
}
