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

import org.eclipse.birt.report.engine.api.script.eventhandler.IDataItemEventHandler;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.element.DataItem;
import org.eclipse.birt.report.engine.script.internal.instance.DataItemInstance;
import org.eclipse.birt.report.model.api.DataItemHandle;

public class DataItemScriptExecutor extends ScriptExecutor
{
	public static void handleOnPrepare( DataItemHandle dataItem,
			ExecutionContext context )
	{
		try
		{
			if ( handleJS( dataItem.getOnPrepare( ), context ) )
				return;
			IDataItemEventHandler eh = ( IDataItemEventHandler ) getInstance( dataItem );
			if ( eh != null )
				eh.onPrepare( new DataItem( dataItem ), context
						.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnCreate( DataContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign dataItemDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			if ( handleJS( dataItemDesign.getOnCreate( ), context ) )
				return;
			IDataItemEventHandler eh = ( IDataItemEventHandler ) getInstance( ( DataItemHandle ) dataItemDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onCreate( new DataItemInstance( content ), context
						.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	public static void handleOnRender( DataContent content,
			ExecutionContext context )
	{
		try
		{
			ReportItemDesign dataItemDesign = ( ReportItemDesign ) content
					.getGenerateBy( );
			if ( handleJS( dataItemDesign.getOnRender( ), context ) )
				return;
			IDataItemEventHandler eh = ( IDataItemEventHandler ) getInstance( ( DataItemHandle ) dataItemDesign
					.getHandle( ) );
			if ( eh != null )
				eh.onRender( new DataItemInstance( content ), context
						.getReportContext( ) );
		} catch ( Exception e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}
