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

import org.eclipse.birt.report.engine.api.script.element.ILabel;
import org.eclipse.birt.report.engine.api.script.eventhandler.ILabelEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ILabelInstance;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Label;
import org.eclipse.birt.report.engine.script.internal.instance.LabelInstance;
import org.eclipse.birt.report.model.api.LabelHandle;

public class LabelScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( LabelHandle labelHandle,
			ExecutionContext context )
	{
		try
		{
			ILabel label = new Label( labelHandle );
			if ( handleJS( label, labelHandle.getOnPrepare( ), context )
					.didRun( ) )
				return;
			ILabelEventHandler eh = ( ILabelEventHandler ) getInstance( labelHandle );
			if ( eh != null )
				eh.onPrepare( label, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( LabelContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign labelDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ILabelInstance label = new LabelInstance( content, context );
			if ( handleJS( label, labelDesign.getOnCreate( ), context )
					.didRun( ) )
				return;
			ILabelEventHandler eh = ( ILabelEventHandler ) getInstance( ( LabelHandle ) labelDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( label, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( LabelContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign labelDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			ILabelInstance label = new LabelInstance( content, context );
			if ( handleJS( label, labelDesign.getOnRender( ), context )
					.didRun( ) )
				return;
			ILabelEventHandler eh = ( ILabelEventHandler ) getInstance( ( LabelHandle ) labelDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( label, context.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
