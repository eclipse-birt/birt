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
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class PDFBlockContainerLM extends PDFBlockStackingLM
		implements
			IBlockStackingLayoutManager
{

	public PDFBlockContainerLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
	}

	protected void createRoot( )
	{
		root = (ContainerArea) AreaFactory.createBlockContainer( content,
				isFirst, false );
	}

	protected boolean traverseSingleChild( )
	{
		if ( child != null )
		{
			boolean childBreak = false;
			childBreak = child.layout( );
			if ( childBreak )
			{
				if ( child.isFinished( ) )
				{
					child = null;
				}
				else
				{
					return true;
				}
			}
			return false;
		}
		return false;
	}

}
