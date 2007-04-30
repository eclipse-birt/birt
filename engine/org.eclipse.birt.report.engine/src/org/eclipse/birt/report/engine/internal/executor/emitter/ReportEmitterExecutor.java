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

package org.eclipse.birt.report.engine.internal.executor.emitter;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class ReportEmitterExecutor implements IReportExecutor
{

	IReportContent report;

	IReportExecutor executor;

	IContentEmitter emitter;

	ExecutorManager manager;

	public ReportEmitterExecutor( IReportExecutor executor,
			IContentEmitter emitter )
	{
		this.manager = new ExecutorManager( );
		this.executor = executor;
		this.emitter = emitter;
	}

	public void close( )
	{
		if ( report != null )
		{
			emitter.end( report );
		}
		executor.close( );
	}

	public IReportItemExecutor createPageExecutor( long pageNumber,
			MasterPageDesign pageDesign )
	{
		return executor.createPageExecutor( pageNumber, pageDesign );
	}

	public IReportContent execute( )
	{
		report = executor.execute( );
		if ( report != null )
		{
			emitter.start( report );
		}
		return report;
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = executor.getNextChild( );
		if ( childExecutor != null )
		{
			return manager.createExecutor( childExecutor, emitter );
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return executor.hasNextChild( );
	}

}
