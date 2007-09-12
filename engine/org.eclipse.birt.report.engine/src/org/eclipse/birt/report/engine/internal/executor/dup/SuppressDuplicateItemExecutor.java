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
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportItemExecutor;

public class SuppressDuplicateItemExecutor extends WrappedReportItemExecutor
{

	boolean executed;
	IContent content;

	SuppressDuplicateItemExecutor(
			SuppressDuplciateReportExecutor reportExecutor,
			IReportItemExecutor executor )
	{
		super( reportExecutor, executor );
	}

	public void close( )
	{
		content = null;
		executed = false;
		super.close( );
	}

	public IContent execute( )
	{
		if ( executed == false )
		{
			content = executor.execute( );
			if ( content != null )
			{
				content = ( (SuppressDuplciateReportExecutor) reportExecutor )
						.suppressDuplicate( content );
			}
		}
		return content;
	}

	public IContent getContent( )
	{
		return content;
	}
}
