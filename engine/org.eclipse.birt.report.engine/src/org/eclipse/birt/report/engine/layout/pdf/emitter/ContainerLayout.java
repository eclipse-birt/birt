/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;


public abstract class ContainerLayout extends Layout
{
	
	protected ContainerContext currentContext;
	
	protected LinkedList<ContainerContext> contextList = new LinkedList<ContainerContext>();
	
	protected int offsetX = 0;

	protected int offsetY = 0;
	
	protected boolean isInBlockStacking = true;

	
	public ContainerLayout( LayoutEngineContext context, ContainerLayout parent, IContent content )
	{
		super( context, parent, content );
		if ( parent != null && !parent.isInBlockStacking )
		{
			isInBlockStacking = false;
		}
	}
	
	public void layout()
	{
		
	}
	
	public boolean isPageEmpty(  )
	{
		if ( !isRootEmpty(  ) )
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
	
	public void addToRoot( AbstractArea area, int index )
	{
		ContainerContext cc = currentContext;
		currentContext = contextList.get( index );
		addToRoot(area);
		currentContext = cc;
	}
	
	protected void addToRoot(AbstractArea area)
	{
		addToRoot(area, true);
	}
	
	protected void addToRoot( AbstractArea area, boolean clipFlag )
	{
		currentContext.root.addChild( area );
		area.setAllocatedPosition( currentContext.currentIP + offsetX,
				currentContext.currentBP + offsetY );
		currentContext.currentBP += area.getAllocatedHeight( );
		if ( clipFlag )
		{
			if ( currentContext.currentIP + area.getAllocatedWidth( ) > currentContext.root
					.getContentWidth( ) )
			{
				currentContext.root.setNeedClip( true );
			}
			else if ( currentContext.currentBP > currentContext.maxAvaHeight )
			{
				currentContext.root.setNeedClip( true );
			}
		}
	}
	
	public boolean addArea(AbstractArea area)
	{
		return addArea(area, true);
	}
	
	public boolean addArea( AbstractArea area, int index )
	{
		ContainerContext cc = currentContext;
		currentContext = contextList.get( index );
		boolean ret = addArea(area);
		currentContext = cc;
		return ret;
	}
	
	protected boolean addArea(AbstractArea area, boolean clipFlag)
	{
		
		if ( currentContext != null )
		{
			if ( !context.autoPageBreak )
			{
				addToRoot( area, clipFlag );
				return true;
			}
			else
			{
				if ( area.getAllocatedHeight( ) + currentContext.currentBP > getMaxAvaHeight( ) )
				{
					if ( isPageEmpty( ) )
					{
						addToRoot( area, clipFlag );
						return true;
					}
					else
					{
						/*if ( isInBlockStacking )
						{
							flushPage( contextList.size() );
						}
						autoPageBreak( );
						addToRoot( area, clipFlag );
						return true;*/
						return false;
					}
				}
				else
				{
					addToRoot( area, clipFlag );
					return true;
				}
			}
		}
		return true;
	}
	
	/*public boolean addAreaFromLast(AbstractArea area, int index)
	{
		int size = contextList.size();
		ContainerContext cc = currentContext;
		currentContext = contextList.get( size-index-1 );
		boolean ret = addArea(area);
		currentContext = cc;
		return ret;
	}*/
	
	public void flushFinishedPage()
	{
		int size = contextList.size();
		closeLayout( size-1, false );
		parent.flushFinishedPage( );
	}
	
	public void flushPage( )
	{
		int size = contextList.size();
		closeLayout( size, false );
		parent.flushPage( );
	}
	
	public void autoPageBreak( )
	{
		if ( parent != null )
		{
			parent.autoPageBreak( );
		}
		int size = contextList.size();
		if( size==0 || size >0 && currentContext==contextList.getLast() )
		{
			initialize( );
		}
		else
		{
			int index = contextList.indexOf( currentContext ) + 1;
			if ( index >= 0 && index < contextList.size( ) )
			{
				currentContext = contextList.get( index );
			}
		}
	}
	public int getMaxAvaWidth()
	{
		return currentContext.maxAvaWidth;
	}
	public int getMaxAvaHeight()
	{
		return currentContext.maxAvaHeight;
	}
	
	public int getCurrentMaxContentWidth( )
	{
		return currentContext.maxAvaWidth - currentContext.currentIP;
	}
	
	public int getCurrentMaxContentHeight()
	{
		return currentContext.maxAvaHeight - currentContext.currentBP;
	}


	
	public int getOffsetX( )
	{
		return offsetX;
	}


	public int getOffsetY( )
	{
		return offsetY;
	}



	protected boolean isRootEmpty(  )
	{
		return !( currentContext.root != null && currentContext.root.getChildrenCount( ) > 0 );
	}

	public int getLineHeight( )
	{
		if ( content != null )
		{
			IStyle contentStyle = content.getComputedStyle( );
			return PropertyUtil.getLineHeight( contentStyle.getLineHeight( ));
		}
		return 0;
	}

	protected abstract void createRoot( );
	
	protected void closeLayout( int size, boolean finished)
	{
		if(isInBlockStacking)
		{
			for ( int i = 0; i < size; i++ )
			{
				closeLayout( contextList.removeFirst( ), i, finished && i==(size-1) );
			}
		}
		else
		{
			if ( parent != null )
			{
				int parentSize = parent.contextList.size( );
				for ( int i = 0; i < size; i++ )
				{
					closeLayout( contextList.removeFirst( ), parentSize - size
							+ i, finished && i == ( size - 1 ) );
				}
			}
			else
			{
				for ( int i = 0; i < size; i++ )
				{
					closeLayout( contextList.removeFirst( ), i, finished && i==(size-1) );
				}
			}
		}
		if ( contextList.size( ) > 0 )
		{
			currentContext = contextList.getFirst( );
		}
	}
	
	
	
	
	
	
	public void step(int step)
	{
		if ( currentContext != null )
		{
			int index = contextList.indexOf( currentContext ) + step;
			if ( index >= 0 && index < contextList.size( ) )
			{
				currentContext = contextList.get( index );
				if ( parent != null )
				{
					parent.step( step );
				}
			}
		}
	}
	public void gotoLastPage()
	{
		int size = contextList.size( );
		if ( size == 1 )
		{
			return;
		}
		else
		{
			int index = contextList.indexOf( currentContext );
			if ( index != size-1 )
			{
				currentContext = contextList.get( size-1 );
				parent.step( size-1-index );
			}
		}
	}
	
	public void gotoFirstPage()
	{
		int size = contextList.size( );
		if ( size == 1 )
		{
			return;
		}
		else
		{
			int index = contextList.indexOf( currentContext );
			if ( index > 0 )
			{
				currentContext = contextList.get( 0 );
				parent.step( 0 - index );
			}
		}
	}
	
	protected void closeLayout( )
	{
		int size = contextList.size( );
		if ( isInBlockStacking && size > 1 )
		{
			flushFinishedPage();
		}
		size = contextList.size( );
		if(size>0)
		{
			closeLayout( size, true );
		}
	}
	
	protected void closeLayout(ContainerContext currentLayout, int index, boolean finished)
	{
		
	}
	
		
	class ContainerContext 
	{
		protected ContainerArea root;
		
		protected int currentIP = 0;
		
		protected int currentBP = 0;
			
		protected int maxAvaHeight = 0;
		
		protected int maxAvaWidth = 0;
	}
	
}
