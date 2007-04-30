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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class ReportItemEmitterExecutor implements IReportItemExecutor
{

	IContent content;

	IReportItemExecutor executor;

	IContentEmitter emitter;

	ReportItemEmitterExecutor( )
	{

	}

	ReportItemEmitterExecutor( IReportItemExecutor executor,
			IContentEmitter emitter )
	{
		content = null;
		this.executor = executor;
		this.emitter = emitter;
	}

	public void close( )
	{
		if ( content != null )
		{
			ContentEmitterUtil.endContent( content, emitter );
		}
		executor.close( );
	}

	public IContent execute( )
	{
		content = executor.execute( );
		if ( content != null )
		{
			ContentEmitterUtil.startContent( content, emitter );
		}
		return content;
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = executor.getNextChild( );
		if ( childExecutor != null )
		{
			return createExecutor( childExecutor, emitter );
		}
		return null;
	}

	public boolean hasNextChild( )
	{

		return executor.hasNextChild( );
	}

	protected IReportItemExecutor createExecutor( IReportItemExecutor executor,
			IContentEmitter emitte )
	{
		return new ReportItemEmitterExecutor( executor, emitter );
	}

}
