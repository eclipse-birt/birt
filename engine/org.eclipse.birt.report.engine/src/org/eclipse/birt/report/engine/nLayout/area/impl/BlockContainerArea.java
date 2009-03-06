/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;

public class BlockContainerArea extends ContainerArea implements IContainerArea
{

	public BlockContainerArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
		isInInlineStacking = ( parent != null
				? parent.isInInlineStacking
				: false );
	}

	public BlockContainerArea( )
	{
		super( );
	}

	BlockContainerArea( BlockContainerArea area )
	{
		super( area );
	}

	public void add( AbstractArea area )
	{
		children.add( area );
		area.setAllocatedPosition( currentIP + getOffsetX( ), currentBP
				+ getOffsetY( ) );

	}

	public void update( AbstractArea area ) throws BirtException
	{
		int aHeight = area.getAllocatedHeight( );
		currentBP += aHeight;
		height += aHeight;
	}

	public void close( ) throws BirtException
	{
		if ( hasStyle )
		{
			int height = currentBP + localProperties.getPaddingTop( )
					+ boxStyle.getTopBorderWidth( )
					+ localProperties.getPaddingBottom( )
					+ boxStyle.getBottomBorderWidth( );

			if ( specifiedHeight > height )
			{

				if ( IStyle.BOTTOM_VALUE.equals( vAlign ) )
				{
					int offset = specifiedHeight - height;
					Iterator<IArea> iter = getChildren( );
					while ( iter.hasNext( ) )
					{
						AbstractArea child = (AbstractArea) iter.next( );
						child.setY( offset + child.getY( ) );
					}
				}
				else if ( IStyle.MIDDLE_VALUE.equals( vAlign ) )
				{
					int offset = ( specifiedHeight - height ) / 2;
					Iterator iter = getChildren( );
					while ( iter.hasNext( ) )
					{
						AbstractArea child = (AbstractArea) iter.next( );
						child.setY( child.getY( ) + offset );
					}
				}

				height = specifiedHeight;
			}
			this.height = height;
		}
		else
		{
			if ( specifiedHeight > currentBP )
			{
				height = specifiedHeight;
			}
			else
			{
				height = currentBP;
			}
		}
		if ( parent != null )
		{
			if ( !isInInlineStacking && context.isAutoPageBreak( ) )
			{
				int aHeight = getAllocatedHeight( );
				while ( aHeight + parent.getAbsoluteBP( ) >= context.getMaxBP( ) )
				{
					parent.autoPageBreak( );
					aHeight = getAllocatedHeight( );
				}
			}
			parent.update( this );
		}
		finished = true;
	}

	public void initialize( ) throws BirtException
	{
		if ( content == null )
		{
			this.maxAvaWidth = width;
			return;
		}
		IStyle style = content.getStyle( );
		calculateSpecifiedWidth( content );
		calculateSpecifiedHeight( content );

		if ( style == null || style.isEmpty( ) )
		{
			hasStyle = false;
			boxStyle = BoxStyle.DEFAULT;
			localProperties = LocalProperties.DEFAULT;
			if ( specifiedWidth > 0 )
			{
				this.width = specifiedWidth;
			}
			else
			{
				if ( parent != null )
				{
					this.width = parent.getMaxAvaWidth( );
				}
			}
			this.maxAvaWidth = width;
		}
		else
		{
			buildProperties( content, context );
			if ( specifiedWidth > 0 )
			{
				this.width = specifiedWidth;
			}
			else
			{
				if ( parent != null )
				{
					setAllocatedWidth( parent.getMaxAvaWidth( ) );
				}
			}
			maxAvaWidth = getContentWidth( );
		}
		this.bookmark = content.getBookmark( );
		this.action = content.getHyperlinkAction( );
		parent.add( this );
	}

	public int getLineHeight( )
	{
		if ( content != null )
		{
			IStyle contentStyle = content.getComputedStyle( );
			return PropertyUtil.getLineHeight( contentStyle.getLineHeight( ) );
		}
		return 0;
	}

	public BlockContainerArea cloneArea( )
	{
		return new BlockContainerArea( this );
	}

	public SplitResult splitLines( int lineCount ) throws BirtException
	{
		if ( isPageBreakInsideAvoid( ) )
		{
			if(isPageBreakBeforeAvoid())
			{
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}
			else
			{
				return SplitResult.SUCCEED_WITH_NULL;
			}
		}
		int contentHeight = getContentHeight();
		LinkedList result = new LinkedList( );
		int size = children.size( );
		SplitResult childSplit = null;
		int status = SplitResult.SPLIT_BREFORE_AVOID_WITH_NULL;
		for ( int i = size - 1; i >= 0; i-- )
		{
			ContainerArea child = (ContainerArea) children.get( i );
			int ah = child.getAllocatedHeight( );
			childSplit = child.splitLines( lineCount );
			if ( childSplit.status == SplitResult.SPLIT_BREFORE_AVOID_WITH_NULL )
			{
				result.addFirst( child );
				contentHeight -= ah;
			}
			else if ( childSplit.status == SplitResult.SPLIT_SUCCEED_WITH_NULL )
			{
				result.addFirst( child );
				contentHeight -= ah;
				if ( i > 0 )
				{
					ContainerArea preChild = (ContainerArea) children
							.get( i - 1 );
					if ( preChild.isPageBreakAfterAvoid( ) )
					{
						continue;
					}
					else
					{
						status = SplitResult.SPLIT_SUCCEED_WITH_PART;
						contentHeight = contentHeight - ah + child.getAllocatedHeight( );
						BlockContainerArea newContainer = cloneArea();
						newContainer.setContentHeight( contentHeight );
						Iterator iter = children.iterator( );
						while(iter.hasNext( ))
						{
							ContainerArea childArea = (ContainerArea)iter.next( );
							if(!result.contains( childArea ))
							{
								iter.remove( );
								newContainer.addChild( childArea );
								newContainer.setParent( newContainer );
							}
						}
						updateChildrenPosition( );
						return new SplitResult(newContainer, SplitResult.SPLIT_SUCCEED_WITH_PART);
					}
				}
				else
				{
					if(isPageBreakBeforeAvoid( ))
					{
						return SplitResult.BEFORE_AVOID_WITH_NULL;
					}
					else
					{
						return SplitResult.SUCCEED_WITH_NULL;
					}
				}
			}
			else if ( childSplit.status == SplitResult.SPLIT_SUCCEED_WITH_PART )
			{
				result.addFirst( child );
				ContainerArea splitChildArea = childSplit.getResult( );
				contentHeight = contentHeight - ah + splitChildArea.getAllocatedHeight( );
				BlockContainerArea newContainer = cloneArea();
				newContainer.setContentHeight( contentHeight );
				Iterator iter = children.iterator( );
				while(iter.hasNext( ))
				{
					ContainerArea childArea = (ContainerArea)iter.next( );
					if(!result.contains( childArea ))
					{
						iter.remove( );
						newContainer.addChild( childArea );
						newContainer.setParent( newContainer );
					}
				}
				newContainer.addChild( splitChildArea );
				updateChildrenPosition( );
				return new SplitResult(newContainer, SplitResult.SPLIT_SUCCEED_WITH_PART);
			}
		}
		return SplitResult.BEFORE_AVOID_WITH_NULL;
	}
	
	public SplitResult split( int height, boolean force ) throws BirtException
	{
		if ( force )
		{
			return _split( height, true );
		}
		else if ( isPageBreakInsideAvoid( ) )
		{
			if ( isPageBreakBeforeAvoid( ) )
			{
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}
			else
			{
				return SplitResult.SUCCEED_WITH_NULL;
			}
		}
		else
		{
			return _split( height, false );
		}
	}


	protected SplitResult _split( int height, boolean force )
			throws BirtException
	{
		if(children.size()==0)
		{
			if(isPageBreakBeforeAvoid( ) && !force)
			{
				updateChildrenPosition( );
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}
			else
			{
				updateChildrenPosition( );
				return SplitResult.SUCCEED_WITH_NULL;
			}
		}
		BlockContainerArea newContainer = null;
		int status = SplitResult.SPLIT_BREFORE_AVOID_WITH_NULL;
		int cheight = getContentHeight( height );
		ListIterator iter = children.listIterator( );
		int contentHeight = 0;
		ArrayList result = new ArrayList( );
		ContainerArea current = null;
		ContainerArea previous = null;
		while ( iter.hasNext( ) )
		{
			previous = current;
			current = (ContainerArea) iter.next( );
			int ah = current.getAllocatedHeight( );
			contentHeight += ah;
			if ( contentHeight <= cheight && current.finished )
			{
				result.add( current );
				continue;
			}
			else
			{
				contentHeight -= ah;
				int childSplitHeight = cheight - contentHeight;
				SplitResult splitResult = current.split( childSplitHeight,
						force&&result.isEmpty( ) );
				if ( splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_PART )
				{
					ContainerArea splitChildArea = splitResult.getResult( );
					result.add( splitChildArea );
					status = SplitResult.SPLIT_SUCCEED_WITH_PART;
					contentHeight += splitChildArea.getAllocatedHeight( );
					break;
				}
				else if ( splitResult.status == SplitResult.SPLIT_BREFORE_AVOID_WITH_NULL )
				{
					if(force)
					{
						if(result.size( )>0)
						{
							status = SplitResult.SPLIT_SUCCEED_WITH_PART;
						}
					}
					break;
				}
				else if ( splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_NULL )
				{
					if ( previous != null )
					{
						if ( force&&result.isEmpty( ) )
						{
							return SplitResult.SUCCEED_WITH_NULL;
						}
						else
						{
							if ( previous.isPageBreakAfterAvoid( ) )
							{
								status = SplitResult.SPLIT_BREFORE_AVOID_WITH_NULL;
								break;
							}
							else
							{
								status = SplitResult.SPLIT_SUCCEED_WITH_PART;
								break;
							}
						}
					}
					else
					{
						if ( force )
						{
							status = SplitResult.SPLIT_SUCCEED_WITH_PART;
							break;
						}
						else
						{
							if ( isPageBreakBeforeAvoid( ) )
							{
								return SplitResult.BEFORE_AVOID_WITH_NULL;
							}
							else
							{
								return SplitResult.SUCCEED_WITH_NULL;
							}
						}
					}
				}
			}
		}
		if( result.size( ) == children.size( ) )
		{
			status = SplitResult.SPLIT_SUCCEED_WITH_PART;
		}
		
		if ( !force && status == SplitResult.SPLIT_BREFORE_AVOID_WITH_NULL )
		{
			if(result.size()==0)
			{
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}
			// locate current child
			iter.previous( );
			while ( iter.hasPrevious( ) )
			{
				current = (ContainerArea) iter.previous( );
				int ah = current.getAllocatedHeight( );
				SplitResult splitResult = current.splitLines( 1 );
				if ( splitResult.status == SplitResult.SPLIT_BREFORE_AVOID_WITH_NULL )
				{
					result.remove( current );
					contentHeight -= ah;
					continue;
				}
				else if ( splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_PART )
				{
					result.remove( current );
					ContainerArea splitChildArea = splitResult.getResult( );
					contentHeight = contentHeight - ah
							+ splitChildArea.getAllocatedHeight( );
					result.add( splitChildArea );
					status = SplitResult.SPLIT_SUCCEED_WITH_PART;
					break;
				}
				else if ( splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_NULL )
				{
					result.remove( current );
					int preIndex = iter.previousIndex( );
					if ( preIndex >= 0 )
					{
						ContainerArea prev = (ContainerArea) children
								.get( preIndex );
						if ( prev.isPageBreakAfterAvoid( ) )
						{
							continue;
						}
						else
						{
							status = SplitResult.SPLIT_SUCCEED_WITH_PART;
							break;
						}
					}
					else
					{
						if ( isPageBreakBeforeAvoid( ) )
						{
							return SplitResult.BEFORE_AVOID_WITH_NULL;
						}
						else
						{
							return SplitResult.SUCCEED_WITH_NULL;
						}
					}
				}
			}
			if(result.size( )==0)
			{
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}

		}

		if ( status == SplitResult.SPLIT_SUCCEED_WITH_PART )
		{
			newContainer = cloneArea( );
			for ( int i = 0; i < result.size( ); i++ )
			{
				ContainerArea child = (ContainerArea) result.get( i );
				child.setParent( newContainer );
				newContainer.addChild( child );
				children.remove( child );
			}
			newContainer.setContentHeight( contentHeight );

		}

		if ( newContainer != null )
		{
			updateChildrenPosition( );
		}
		return new SplitResult( newContainer, status );
	}

	public void autoPageBreak( ) throws BirtException
	{
		if ( parent != null )
		{
			parent.autoPageBreak( );
			// updateChildrenPosition( );
		}
	}

	protected void updateChildrenPosition( ) throws BirtException
	{
		currentBP = 0;
		if ( children.size( ) > 0 )
		{
			Iterator iter = children.iterator( );
			int y = getOffsetY( );
			int h = 0;
			while ( iter.hasNext( ) )
			{
				ContainerArea area = (ContainerArea) iter.next( );
				// if(iter.hasNext( ))
				{
					area.setAllocatedPosition( getOffsetX( ), y );
					int ah = area.getAllocatedHeight( );
					y += ah;
					h += ah;
					if ( area.finished )
					{
						currentBP += ah;
					}
				}
			}
			setContentHeight( h );
		}
		else
		{
			setContentHeight(0);
		}

	}

}
