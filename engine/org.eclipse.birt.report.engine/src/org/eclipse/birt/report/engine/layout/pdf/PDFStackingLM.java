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
import org.eclipse.birt.report.engine.layout.ILayoutContext;
import org.eclipse.birt.report.engine.layout.IStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public abstract class PDFStackingLM extends PDFAbstractLM
		implements
			IStackingLayoutManager,
			ILayoutContext
{

	protected int nMaxWidth = -1;

	protected int nMaxHeight = -1;

	protected int maxAvaWidth = 0;

	protected int maxAvaHeight = 0;

	protected int currentIP = 0;

	protected int currentBP = 0;

	protected int offsetX = 0;

	protected int offsetY = 0;

	protected ContainerArea root;

	protected PDFAbstractLM child;

	protected int minHeight = 0;

	protected int minWidth = 0;

	public int getMaxAvaWidth( )
	{
		return this.maxAvaWidth;
	}

	public int getMaxAvaHeight( )
	{
		return this.maxAvaHeight;
	}

	public int getCurrentIP( )
	{
		return this.currentIP;
	}

	public int getCurrentBP( )
	{
		return this.currentBP;
	}

	public void setCurrentBP( int bp )
	{
		this.currentBP = bp;
	}

	public void setCurrentIP( int ip )
	{
		this.currentIP = ip;
	}

	public void setMaxAvaHeight( int height )
	{
		this.maxAvaHeight = height;
	}

	public void setMaxAvaWidth( int width )
	{
		this.maxAvaWidth = width;
	}

	public int getOffsetX( )
	{
		return offsetX;
	}

	public void setOffsetX( int x )
	{
		this.offsetX = x;
	}

	public int getOffsetY( )
	{
		return offsetY;
	}

	public void setOffsetY( int y )
	{
		this.offsetY = y;
	}

	public PDFStackingLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content,  executor );
	}

	protected boolean layoutChildren( )
	{
		newContext( );
		boolean childBreak = false;
		childBreak = traverseChildren( );
		if ( !childBreak )
		{
			isLast = true;
		}
		if ( !isRootEmpty( ) )
		{
			closeLayout( );
			childBreak = !submitRoot( childBreak ) || childBreak;

		}
		return childBreak;
	}

	protected boolean isRootEmpty( )
	{
		return !( root != null && root.getChildrenCount( ) > 0 );
	}

	protected abstract boolean traverseChildren( );

	protected boolean submitRoot( boolean childBreak )
	{
		boolean ret = true;
		if ( parent != null )
		{
			ret = parent.addArea( root );
			if ( ret )
			{
				root = null;
			}
		}
		else
		{
			if ( content != null )
			{
				content.setExtension( IContent.LAYOUT_EXTENSION, root );
				root = null;
			}
		}
		return ret;
	}

	/**
	 * initialize dynamic layout information
	 * <ul>
	 * <li> create root area </li>
	 * <li> set MaxAvaHeight and MaxAvaWidth</li>
	 * <li> set OffsetX and OffsetY </li>
	 * <li> set CurrentIP and CurrentBP </li>
	 * </ul>
	 * 
	 * 
	 */
	protected abstract void newContext( );

	/**
	 * end current area if it is the last area of content, add bottom box
	 * property
	 * 
	 */
	protected abstract void closeLayout( );

	protected abstract void createRoot( );

	public boolean isPageEmpty( )
	{
		if ( !isRootEmpty( ) )
		{
			return false;
		}
		else
		{
			if ( parent != null )
			{
				return parent.isPageEmpty( );
			}
		}
		return true;
	}

}
