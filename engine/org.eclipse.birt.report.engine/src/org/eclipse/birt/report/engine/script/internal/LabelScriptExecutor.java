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

import org.eclipse.birt.report.engine.api.script.eventhandler.ILabelEventHandler;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.element.Label;
import org.eclipse.birt.report.engine.script.internal.instance.LabelInstance;
import org.eclipse.birt.report.model.api.LabelHandle;

public class LabelScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( LabelHandle label,
			ExecutionContext context )
	{
		try
		{
			if ( handleJS( label.getOnPrepare( ), context ) )
				return;
			ILabelEventHandler eh = ( ILabelEventHandler ) getInstance( label );
			if ( eh != null )
				eh.onPrepare( new Label( label ), context.getReportContext( ) );
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
			if ( handleJS( labelDesign.getOnCreate( ), context ) )
				return;
			ILabelEventHandler eh = ( ILabelEventHandler ) getInstance( ( LabelHandle ) labelDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( new LabelInstance( content ), context
						.getReportContext( ) );
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
			if ( handleJS( labelDesign.getOnRender( ), context ) )
				return;
			ILabelEventHandler eh = ( ILabelEventHandler ) getInstance( ( LabelHandle ) labelDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( new LabelInstance( content ), context
						.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
