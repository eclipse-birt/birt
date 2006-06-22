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

import org.eclipse.birt.report.engine.api.script.element.IDynamicText;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDynamicTextEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDynamicTextInstance;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.DynamicText;
import org.eclipse.birt.report.engine.script.internal.instance.DynamicTextInstance;
import org.eclipse.birt.report.model.api.TextDataHandle;

public class DynamicTextScriptExecutor extends ScriptExecutor
{

	public static void handleOnPrepare( TextDataHandle textDataHandle,
			ExecutionContext context )
	{
		try
		{
			IDynamicText text = new DynamicText( textDataHandle );
			if ( handleJS( text, textDataHandle.getOnPrepare( ), context )
					.didRun( ) )
				return;
			IDynamicTextEventHandler eh = getEventHandler( textDataHandle,
					context );
			if ( eh != null )
				eh.onPrepare( text, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( IForeignContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign textItemDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IDynamicTextInstance text = new DynamicTextInstance( content,
					context );
			if ( handleJS( text, textItemDesign.getOnCreate( ), context )
					.didRun( ) )
				return;
			IDynamicTextEventHandler eh = getEventHandler( textItemDesign,
					context );
			if ( eh != null )
				eh.onCreate( text, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnRender( IForeignContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign textItemDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IDynamicTextInstance text = new DynamicTextInstance( content,
					context );
			if ( handleJS( text, textItemDesign.getOnRender( ), context )
					.didRun( ) )
				return;
			IDynamicTextEventHandler eh = getEventHandler( textItemDesign,
					context );
			if ( eh != null )
				eh.onRender( text, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnPageBreak( IForeignContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign textItemDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IDynamicTextInstance text = new DynamicTextInstance( content,
					context );
			if ( handleJS( text, textItemDesign.getOnPageBreak( ), context )
					.didRun( ) )
				return;
			IDynamicTextEventHandler eh = getEventHandler( textItemDesign,
					context );
			if ( eh != null )
				eh.onPageBreak( text, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IDynamicTextEventHandler getEventHandler(
			ReportItemDesign design, ExecutionContext context )
	{
		TextDataHandle handle = ( TextDataHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static IDynamicTextEventHandler getEventHandler(
			TextDataHandle handle, ExecutionContext context )
	{
		IDynamicTextEventHandler eh = null;
		try
		{
			eh = ( IDynamicTextEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IDynamicTextEventHandler.class );
		}
		return eh;
	}
}
