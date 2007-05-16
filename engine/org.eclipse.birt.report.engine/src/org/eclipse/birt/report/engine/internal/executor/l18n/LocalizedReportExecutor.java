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

package org.eclipse.birt.report.engine.internal.executor.l18n;

import java.util.LinkedList;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.wrap.WrappedReportExecutor;
import org.eclipse.birt.report.engine.presentation.LocalizedContentVisitor;

public class LocalizedReportExecutor extends WrappedReportExecutor
{

	IReportExecutor executor;
	LocalizedContentVisitor l18nVisitor;
	LinkedList freeList = new LinkedList( );

	public LocalizedReportExecutor( ExecutionContext context,
			IReportExecutor executor )
	{
		super( executor );
		this.l18nVisitor = new LocalizedContentVisitor( context );
		this.freeList = new LinkedList( );
		this.executor = executor;
	}

	protected IReportItemExecutor createWrappedExecutor( IReportItemExecutor executor )
	{
		LocalizedReportItemExecutor l18nExecutor = null;
		if ( !freeList.isEmpty( ) )
		{
			l18nExecutor = (LocalizedReportItemExecutor) freeList.removeFirst( );
			l18nExecutor.setExecutor( executor );
		}
		else
		{
			l18nExecutor = new LocalizedReportItemExecutor( this, executor );
		}
		return l18nExecutor;
	}

	protected void closeWrappedExecutor( IReportItemExecutor executor )
	{
		freeList.addLast( executor );
	}
}
