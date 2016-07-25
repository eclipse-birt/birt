/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;

public class HTMLBlockStackingLM extends HTMLStackingLM
{

	ILayoutManager childLayout;
	IReportItemExecutor childExecutor;
	IContent childContent;

	public HTMLBlockStackingLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_BLOCK;
	}

	protected boolean isChildrenFinished( ) throws BirtException
	{
		return childExecutor == null && !executor.hasNextChild( );
	}
	
	protected boolean layoutNodes( ) throws BirtException
	{
		boolean hasNext = false;
		
		// first we need layout the remain content
		if ( childLayout != null )
		{
			hasNext = childLayout.layout( );
			if (childLayout.isFinished( ))
			{
				childLayout.close( );
				childExecutor.close( );
				childLayout = null;
				childExecutor = null;
			}
			if ( hasNext )
			{
				return true;
			}
		}
		// then layout the next content
		boolean firstIteration = true;
		while ( executor.hasNextChild( ) && !context.getCancelFlag( ) )
		{
			childExecutor = (IReportItemExecutor) executor.getNextChild( );
			childContent = childExecutor.execute( );
			if ( childContent != null )
			{
				childLayout = engine.createLayoutManager( this, childContent,
						childExecutor, emitter );

				if ( childContent instanceof TableBandContent )
				{
					if ( !executor.hasNextChild( ) )
					{
						( (TableBandContent) childContent )
								.setLastTableBand( true );
					}
					if ( firstIteration )
					{
						( (TableBandContent) childContent )
								.setFirstTableBand( true );
					}
				}
				firstIteration = false;
				hasNext = childLayout.layout( );
	
				if ( hasNext )
				{
					if ( childLayout.isFinished( ) )
					{
						childLayout.close( );
						childExecutor.close( );
						childLayout = null;
						childExecutor = null;
					}
					return true;
				}
				childLayout.close( );
				childLayout = null;
			}
			childExecutor.close( );
			childExecutor = null;
		}
		if ( childContent != null )
		{
			childContent.setLastChild( true );
		}
		return false;
	}

	public void close( ) throws BirtException
	{
		childLayout = null;
		childExecutor = null;
		childContent = null;
		super.close( );
	}

}
