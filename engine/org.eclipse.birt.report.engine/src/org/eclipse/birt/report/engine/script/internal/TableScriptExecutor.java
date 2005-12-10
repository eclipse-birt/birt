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

import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ITableInstance;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.element.Table;
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
			if ( handleJS( table, tableHandle.getOnPrepare( ), context )
					.didRun( ) )
				return;
			ITableEventHandler eh = ( ITableEventHandler ) getInstance( tableHandle );
			if ( eh != null )
				eh.onPrepare( table, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( TableContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign tableDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ITableInstance table = new TableInstance( content );
			if ( handleJS( table, tableDesign.getOnCreate( ), context )
					.didRun( ) )
				return;
			ITableEventHandler eh = ( ITableEventHandler ) getInstance( ( TableHandle ) tableDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( table, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( TableContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign tableDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ITableInstance table = new TableInstance( content );
			if ( handleJS( table, tableDesign.getOnRender( ), context )
					.didRun( ) )
				return;
			ITableEventHandler eh = ( ITableEventHandler ) getInstance( ( TableHandle ) tableDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( table, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
