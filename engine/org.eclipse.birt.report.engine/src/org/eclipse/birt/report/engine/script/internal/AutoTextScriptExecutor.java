/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.api.script.element.IAutoText;
import org.eclipse.birt.report.engine.api.script.eventhandler.IAutoTextEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IAutoTextInstance;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.AutoText;
import org.eclipse.birt.report.engine.script.internal.instance.AutoTextInstance;
import org.eclipse.birt.report.model.api.AutoTextHandle;


public class AutoTextScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( AutoTextHandle autoTextHandle,
			ExecutionContext context )
	{
		try
		{
			IAutoText cell = new AutoText( autoTextHandle );
			if ( handleJS( cell, autoTextHandle.getOnPrepare( ), context ).didRun( ) )
				return;
			IAutoTextEventHandler eh = getEventHandler( autoTextHandle, context );
			if ( eh != null )
				eh.onPrepare( cell, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnCreate( IAutoTextContent content,
			ExecutionContext context )
	{
		try
		{
			Object generateBy = content.getGenerateBy( );
			if ( generateBy == null )
			{
				return;
			}
			ReportItemDesign autoTextItemDesign = ( ReportItemDesign ) generateBy;
			IAutoTextInstance autoText = new AutoTextInstance( content, context );
			if ( handleJS( autoText, autoTextItemDesign.getOnCreate( ), context ).didRun( ) )
				return;
			IAutoTextEventHandler eh = getEventHandler( autoTextItemDesign, context );
			if ( eh != null )
				eh.onCreate( autoText, context.getReportContext( ) );

		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnRender( IAutoTextContent content,
			ExecutionContext context )
	{
		try
		{
			Object generateBy = content.getGenerateBy( );
			if ( generateBy == null )
			{
				return;
			}
			ReportItemDesign autoTextDesign = ( ReportItemDesign ) generateBy; 
			
			//fromGrid doesn't matter here since row data is null
			IAutoTextInstance autoText = new AutoTextInstance( content, context );
			if ( handleJS( autoText, autoTextDesign.getOnRender( ), context ).didRun( ) )
				return;
			IAutoTextEventHandler eh = getEventHandler( autoTextDesign, context );
			if ( eh != null )
				eh.onRender( autoText, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}
	
	public static void handleOnPageBreak( IAutoTextContent content,
			ExecutionContext context )
	{
		try
		{
			Object generateBy = content.getGenerateBy( );
			if ( generateBy == null )
			{
				return;
			}
			ReportItemDesign autoTextDesign = ( ReportItemDesign ) generateBy; 
			
			//fromGrid doesn't matter here since row data is null
			IAutoTextInstance autoText = new AutoTextInstance( content, context );
			if ( handleJS( autoText, autoTextDesign.getOnPageBreak( ), context ).didRun( ) )
				return;
			IAutoTextEventHandler eh = getEventHandler( autoTextDesign, context );
			if ( eh != null )
				eh.onPageBreak( autoText, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IAutoTextEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		AutoTextHandle handle = ( AutoTextHandle ) design.getHandle( );
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static IAutoTextEventHandler getEventHandler( AutoTextHandle handle,
			ExecutionContext context )
	{
		IAutoTextEventHandler eh = null;
		try
		{
			eh = ( IAutoTextEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IAutoTextEventHandler.class );
		}
		return eh;
	}
}
