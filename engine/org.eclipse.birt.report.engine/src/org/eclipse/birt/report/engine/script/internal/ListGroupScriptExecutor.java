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

import org.eclipse.birt.report.engine.api.script.element.IListGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListGroupEventHandler;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.ListGroup;
import org.eclipse.birt.report.engine.script.internal.instance.ReportElementInstance;
import org.eclipse.birt.report.model.api.ListGroupHandle;

public class ListGroupScriptExecutor extends ScriptExecutor
{

	public static void handleOnPrepare( ListGroupHandle groupHandle,
			ExecutionContext context )
	{
		try
		{
			IListGroup group = new ListGroup( groupHandle );
			IListGroupEventHandler eh = getEventHandler( groupHandle, context );
			if ( eh != null )
				eh.onPrepare( group, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}
	
	public static void handleOnPageBreak( IListGroupContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign listGroupDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ReportElementInstance list = new ReportElementInstance( content, context );
			if ( handleJS( list, listGroupDesign.getOnPageBreak( ), context ).didRun( ) )
				return;
			IListGroupEventHandler eh = getEventHandler( listGroupDesign, context );
			if ( eh != null )
				eh.onPageBreak( list, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IListGroupEventHandler getEventHandler(
			ListGroupHandle handle, ExecutionContext context )
	{
		IListGroupEventHandler eh = null;
		try
		{
			eh = ( IListGroupEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IListGroupEventHandler.class );
		}
		return eh;
	}
	

	private static IListGroupEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		ListGroupHandle handle = ( ListGroupHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

}
