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

import org.eclipse.birt.report.engine.api.script.element.IImage;
import org.eclipse.birt.report.engine.api.script.eventhandler.IImageEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IImageInstance;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Image;
import org.eclipse.birt.report.engine.script.internal.instance.ImageInstance;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;

public class ImageScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( ImageHandle imageHandle,
			ExecutionContext context )
	{
		try
		{
			IImage image = new Image( imageHandle );
			if ( handleJS( image, imageHandle.getOnPrepare( ), context )
					.didRun( ) )
				return;
			IImageEventHandler eh = getEventHandler( imageHandle, context );
			if ( eh != null )
				eh.onPrepare( image, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( ImageContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign imageDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IImageInstance image = new ImageInstance( content, context );
			if ( handleJS( image, imageDesign.getOnCreate( ), context )
					.didRun( ) )
				return;
			IImageEventHandler eh = getEventHandler( imageDesign, context );
			if ( eh != null )
				eh.onCreate( image, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleOnRender( IImageContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign imageDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			IImageInstance image = new ImageInstance( content, context );
			if ( handleJS( image, imageDesign.getOnRender( ), context )
					.didRun( ) )
				return;
			IImageEventHandler eh = getEventHandler( imageDesign, context );
			if ( eh != null )
				eh.onRender( image, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	private static IImageEventHandler getEventHandler( ReportItemDesign design,
			ExecutionContext context )
	{
		DesignElementHandle designHandle = design.getHandle( );
		if ( !( designHandle instanceof ImageHandle ))
		{
			return null;
		}
		ImageHandle handle = ( ImageHandle ) designHandle;
		if ( handle == null )
			return null;
		return getEventHandler( handle, context );
	}

	private static IImageEventHandler getEventHandler( ImageHandle handle,
			ExecutionContext context )
	{
		IImageEventHandler eh = null;
		try
		{
			eh = ( IImageEventHandler ) getInstance( handle, context );
		} catch ( ClassCastException e )
		{
			addClassCastException( context, e, handle.getEventHandlerClass( ),
					IImageEventHandler.class );
		}
		return eh;
	}
}
