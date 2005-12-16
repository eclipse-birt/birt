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

import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;
import org.eclipse.birt.report.engine.content.impl.TableContent;
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
			IGridEventHandler eh = ( IGridEventHandler ) getInstance( gridHandle );
			if ( eh != null )
				eh.onPrepare( grid, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( TableContent content, IRowData rowData,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign gridDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IGridInstance grid = new GridInstance( content );
			if ( handleJS( grid, gridDesign.getOnCreate( ), context ).didRun( ) )
				return;
			IGridEventHandler eh = ( IGridEventHandler ) getInstance( ( GridHandle ) gridDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( grid, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( TableContent content, IRowData rowData,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign gridDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IGridInstance grid = new GridInstance( content );
			if ( handleJS( grid, gridDesign.getOnRender( ), context ).didRun( ) )
				return;
			IGridEventHandler eh = ( IGridEventHandler ) getInstance( ( GridHandle ) gridDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( grid, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
