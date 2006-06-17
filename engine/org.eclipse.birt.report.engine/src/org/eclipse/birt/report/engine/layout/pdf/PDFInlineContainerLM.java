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

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IInlineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;

public class PDFInlineContainerLM extends PDFInlineStackingLM
		implements
			IInlineStackingLayoutManager
{

	public PDFInlineContainerLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );

	}

	protected void closeLayout( )
	{
		// TODO Auto-generated method stub

	}

	protected void createRoot( )
	{
		// TODO Auto-generated method stub

	}

	protected boolean traverseChildren( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	protected void newContext( )
	{
		// TODO Auto-generated method stub

	}

	public boolean addArea( IArea area )
	{
		// TODO Auto-generated method stub
		return false;
	}

}
