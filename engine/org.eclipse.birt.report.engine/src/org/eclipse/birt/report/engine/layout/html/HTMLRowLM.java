/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class HTMLRowLM extends HTMLInlineStackingLM
{

	protected HTMLTableLM tbl;

	public HTMLRowLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_ROW;
	}

	public void initialize( HTMLAbstractLM parent, IContent content,
			IReportItemExecutor executor, IContentEmitter emitter )
	{
		super.initialize( parent, content, executor, emitter );
		tbl = getTableLayoutManager( );
	}

	public boolean layout( )
	{
		context.setPageEmpty( false );
		return super.layout( );
	}
	
	protected boolean handleVisibility( )
	{
		boolean ret = super.handleVisibility( );
		// tbl.skipHiddenRow();
		return ret;
	}
}
