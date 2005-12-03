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
import org.eclipse.birt.report.engine.api.script.eventhandler.ICellEventHandler;
import org.eclipse.birt.report.engine.script.element.Cell;
import org.eclipse.birt.report.engine.script.element.RowData;
import org.eclipse.birt.report.engine.script.internal.instance.CellInstance;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.CellHandle;

public class CellScriptExecutor extends ScriptExecutor
{

	public static void handleOnPrepare( CellHandle cell,
			ExecutionContext context )
	{
		try
		{
			if ( handleJS( cell.getOnPrepare( ), context ) )
				return;
			ICellEventHandler eh = ( ICellEventHandler ) getInstance( cell );
			if ( eh != null )
				eh.onPrepare( new Cell( cell ), context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( CellContent content,
			IRowData rowData, ExecutionContext context )
	{
		try
		{
			ReportItemDesign cellDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			if ( handleJS( cellDesign.getOnCreate( ), context ) )
				return;
			CellHandle handle = ( CellHandle ) cellDesign.getHandle( );
			if ( handle != null )
			{
				ICellEventHandler eh = ( ICellEventHandler ) getInstance( ( CellHandle ) cellDesign
						.getHandle( ) );
				if ( eh != null )
					eh.onCreate( new CellInstance( content ),
							rowData, context.getReportContext( ) );
			}
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( CellContent content,
			RowData rowData, ExecutionContext context )
	{
		try
		{
			ReportItemDesign cellDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			if ( handleJS( cellDesign.getOnRender( ), context ) )
				return;
			ICellEventHandler eh = ( ICellEventHandler ) getInstance( ( CellHandle ) cellDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( new CellInstance( content ), context
						.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
