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
			IRowEventHandler eh = ( IRowEventHandler ) getInstance( rowHandle );
			if ( eh != null )
				eh.onPrepare( row, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( RowContent content, IRowData rowData,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign rowDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IRowInstance row = new RowInstance( content );
			if ( handleJS( row, rowDesign.getOnCreate( ), context ).didRun( ) )
				return;
			IRowEventHandler eh = ( IRowEventHandler ) getInstance( ( RowHandle ) rowDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( row, rowData, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( RowContent content, IRowData rowData,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign rowDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IRowInstance row = new RowInstance( content );
			if ( handleJS( row, rowDesign.getOnRender( ), context ).didRun( ) )
				return;
			IRowEventHandler eh = ( IRowEventHandler ) getInstance( ( RowHandle ) rowDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( row, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
