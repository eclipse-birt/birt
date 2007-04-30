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

package org.eclipse.birt.report.engine.internal.executor.dup;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class SuppressDuplicateItemExecutor implements IReportItemExecutor
{

	SuppressDuplciateReportExecutor reportExecutor;
	boolean executed;
	IContent content;
	IReportItemExecutor executor;

	SuppressDuplicateItemExecutor(
			SuppressDuplciateReportExecutor reportExecutor )
	{
		this.reportExecutor = reportExecutor;
	}

	public void close( )
	{
		content = null;
		executed = false;
		executor.close( );
		reportExecutor.release( this );
	}

	public IContent execute( )
	{
		if ( executed == false )
		{
			content = executor.execute( );
			if ( content != null )
			{
				content = reportExecutor.suppressDuplicate( content );
			}
		}
		return content;
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = executor.getNextChild( );
		if ( childExecutor != null )
		{
			return reportExecutor.createExecutor( childExecutor );
		}
		return null;
	}

	public boolean hasNextChild( )
	{

		return executor.hasNextChild( );
	}

}
