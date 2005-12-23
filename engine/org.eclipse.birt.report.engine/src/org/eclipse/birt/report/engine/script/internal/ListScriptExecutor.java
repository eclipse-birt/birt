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

import org.eclipse.birt.report.engine.api.script.element.IList;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IListInstance;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.List;
import org.eclipse.birt.report.engine.script.internal.instance.ListInstance;
import org.eclipse.birt.report.model.api.ListHandle;

public class ListScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( ListHandle listHandle,
			ExecutionContext context )
	{
		try
		{
			IList list = new List( listHandle );
			if ( handleJS( list, listHandle.getOnPrepare( ), context ).didRun( ) )
				return;
			IListEventHandler eh = getEventHandler( listHandle, context );
			if ( eh != null )
				eh.onPrepare( list, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( ContainerContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign listDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IListInstance list = new ListInstance( content, context );
			if ( handleJS( list, listDesign.getOnCreate( ), context ).didRun( ) )
				return;
			IListEventHandler eh = getEventHandler( listDesign, context );
			if ( eh != null )
				eh.onCreate( list, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnRender( ContainerContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign listDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IListInstance list = new ListInstance( content, context );
			if ( handleJS( list, listDesign.getOnRender( ), context ).didRun( ) )
				return;
			IListEventHandler eh = getEventHandler( listDesign, context );
			if ( eh != null )
				eh.onRender( list, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IListEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		ListHandle handle = ( ListHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static IListEventHandler getEventHandler( ListHandle handle,
			ExecutionContext context )
	{
		IListEventHandler eh = null;
		try
		{
			eh = ( IListEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IListEventHandler.class );
		}
		return eh;
	}
}
