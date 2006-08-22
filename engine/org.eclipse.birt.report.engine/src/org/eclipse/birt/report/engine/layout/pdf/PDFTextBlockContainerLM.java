
package org.eclipse.birt.report.engine.layout.pdf;

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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.content.ItemExecutorWrapper;
import org.eclipse.birt.report.engine.layout.content.LineStackingExecutor;

public class PDFTextBlockContainerLM extends PDFBlockContainerLM
		implements
			IBlockStackingLayoutManager
{

	public PDFTextBlockContainerLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content,
			IReportItemExecutor executor )
	{
		super( context, parent, content,  executor );
		child = new PDFLineAreaLM( context, this, 
				new LineStackingExecutor( new ItemExecutorWrapper( executor,
						content ), executor ) );
	}

	protected boolean traverseChildren( )
	{
		return traverseSingleChild( );
	}

	protected void closeExecutor( )
	{

	}

	public boolean addArea( IArea area )
	{
		boolean added = super.addArea( area );
		if(added && isFirst)
		{
			isFirst = false;
		}
		return added;
	}

}
