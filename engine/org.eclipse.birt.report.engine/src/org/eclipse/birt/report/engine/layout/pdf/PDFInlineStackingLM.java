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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IInlineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.ILayoutManager;

public abstract class PDFInlineStackingLM extends PDFStackingLM
		implements
			IInlineStackingLayoutManager
{

	protected List children = new ArrayList( );

	public List getChildren( )
	{
		return children;
	}

	public PDFInlineStackingLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
	}

	protected void addChild( PDFAbstractLM child )
	{
		children.add( child );
		this.child = child;
	}

	protected void cancelChildren( )
	{
		for ( int i = 0; i < this.children.size( ); i++ )
		{
			ILayoutManager child = (ILayoutManager) children.get( i );
			child.cancel( );
		}
	}
}
