/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.element.ReportDesign;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;

public class ReportScriptExecutor extends ScriptExecutor
{

	public static void handleInitialize( ReportDesignHandle report,
			ExecutionContext context )
	{
		try
		{
			String scriptText = report.getInitialize( );
			ScriptExpression scriptExpr = null;
			if ( null != scriptText )
			{
				String id = ModuleUtil.getScriptUID( report.getPropertyHandle( IModuleModel.INITIALIZE_METHOD ) );
				scriptExpr = new ScriptExpression( scriptText, id );
			}
			if ( handleJS( null, scriptExpr, context ).didRun( ) )
				return;
			IReportEventHandler eh = ( IReportEventHandler ) getInstance(
					report, context );
			if ( eh != null )
				eh.initialize( context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleBeforeFactory( ReportDesignHandle report,
			ExecutionContext context )
	{
		try
		{
			IDesignElement element = SimpleElementFactory.getInstance( ).getElement( report );
			IReportDesign reportDesign = new ReportDesign( report );
			String scriptText = report.getBeforeFactory( );
			ScriptExpression scriptExpr = null;
			if ( null != scriptText )
			{
				String id = ModuleUtil.getScriptUID( report.getPropertyHandle( IReportDesignModel.BEFORE_FACTORY_METHOD ) );
				scriptExpr = new ScriptExpression( scriptText, id );
			}
			if ( handleJS( element, scriptExpr, context ).didRun( ) )
				return;
			IReportEventHandler eh = ( IReportEventHandler ) getInstance(
					report, context );
			if ( eh != null )
				eh.beforeFactory( reportDesign, context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleAfterFactory( ReportDesignHandle report,
			ExecutionContext context )
	{
		try
		{
			String scriptText = report.getAfterFactory( );
			ScriptExpression scriptExpr = null;
			if ( null != scriptText )
			{
				String id = ModuleUtil.getScriptUID( report.getPropertyHandle( IReportDesignModel.AFTER_FACTORY_METHOD ) );
				scriptExpr = new ScriptExpression( scriptText, id );
			}
			if ( handleJS( null, scriptExpr, context ).didRun( ) )
				return;
			IReportEventHandler eh = ( IReportEventHandler ) getInstance(
					report, context );
			if ( eh != null )
				eh.afterFactory( context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleBeforeRender( ReportDesignHandle report,
			ExecutionContext context )
	{
		try
		{
			String scriptText = report.getBeforeRender( );
			ScriptExpression scriptExpr = null;
			if ( null != scriptText )
			{
				String id = ModuleUtil.getScriptUID( report.getPropertyHandle( IReportDesignModel.BEFORE_RENDER_METHOD ) );
				scriptExpr = new ScriptExpression( scriptText, id );
			}
			if ( handleJS( null, scriptExpr, context ).didRun( ) )
				return;
			IReportEventHandler eh = ( IReportEventHandler ) getInstance(
					report, context );
			if ( eh != null )
				eh.beforeRender( context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}

	public static void handleAfterRender( ReportDesignHandle report,
			ExecutionContext context )
	{
		try
		{
			String scriptText = report.getAfterRender( );
			ScriptExpression scriptExpr = null;
			if ( null != scriptText )
			{
				String id = ModuleUtil.getScriptUID( report.getPropertyHandle( IReportDesignModel.AFTER_RENDER_METHOD ) );
				scriptExpr = new ScriptExpression( scriptText, id );
			}
			if ( handleJS( null, scriptExpr, context ).didRun( ) )
				return;
			IReportEventHandler eh = ( IReportEventHandler ) getInstance(
					report, context );
			if ( eh != null )
				eh.afterRender( context.getReportContext( ) );
		} catch ( Exception e )
		{
			addException( context, e );
		}
	}
}
