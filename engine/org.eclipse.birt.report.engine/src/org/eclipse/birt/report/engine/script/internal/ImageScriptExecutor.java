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
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Image;
import org.eclipse.birt.report.engine.script.internal.instance.ImageInstance;
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
			IImageEventHandler eh = ( IImageEventHandler ) getInstance( imageHandle );
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
			IImageEventHandler eh = ( IImageEventHandler ) getInstance( ( ImageHandle ) imageDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( image, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( ImageContent content,
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
			IImageEventHandler eh = ( IImageEventHandler ) getInstance( ( ImageHandle ) imageDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( image, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
