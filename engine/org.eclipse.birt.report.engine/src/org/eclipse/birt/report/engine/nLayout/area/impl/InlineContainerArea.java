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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;

public class InlineContainerArea extends InlineStackingArea
		implements
			IContainerArea
{

	protected transient InlineStackingArea lineParent = null;
	protected transient int lineCount = 1;

	public InlineContainerArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
		this.isInlineStacking = true;
		lineParent = (InlineStackingArea) parent;
	}

	InlineContainerArea( InlineContainerArea area )
	{
		super( area );
	}

	protected void close( boolean isLastLine ) throws BirtException
	{
		// TODO support specified height/width/alignment
		int width = currentIP + getOffsetX( )
				+ localProperties.getPaddingRight( )
				+ boxStyle.getRightBorderWidth( );
		if ( lineCount == 1 )
		{
			if ( specifiedWidth > width )
			{
				width = specifiedWidth;
			}
		}
		this.width = width;
		int height = 0;
		Iterator iter = getChildren( );
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			height = Math.max( height, child.getAllocatedHeight( ) );
		}
		setContentHeight( height );
		if ( children.size( ) > 0 )
		{
			verticalAlign();
		}
		if ( isLastLine )
		{
			checkPageBreak();
			parent.update( this );
		}
		else
		{
			InlineContainerArea area = new InlineContainerArea( this );
			area.children = children;
			area.setParent( parent );
			children = new ArrayList( );
			parent.addChild( parent.getChildrenCount( ) - 1, area );
			if ( !isInInlineStacking && context.isAutoPageBreak( ) )
			{
				int aHeight = getAllocatedHeight( );
				if ( aHeight + parent.getAbsoluteBP( ) >= context.getMaxBP( ) )
				{
					parent.autoPageBreak( );
				}
			}
			parent.update( area );
			setPosition(parent.currentIP + parent.getOffsetX( ), parent.getOffsetY() + parent.currentBP);
		}
	}

	public void close( ) throws BirtException
	{
		close( true );
		finished = true;
	}

	public void initialize( ) throws BirtException
	{
		IStyle style = content.getStyle( );
		calculateSpecifiedWidth( content );
		if ( style == null || style.isEmpty( ) )
		{
			hasStyle = false;
			boxStyle = BoxStyle.DEFAULT;
			localProperties = LocalProperties.DEFAULT;
		}
		else
		{
			buildProperties( content, context );
		}
		maxAvaWidth = parent.getCurrentMaxContentWidth( );
		currentBP = getContentY( );
		currentIP = getContentX( );
		bookmark = content.getBookmark( );
		action = content.getHyperlinkAction( );
		vAlign = style.getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		parent.add( this );
	}

	public InlineContainerArea cloneArea( )
	{
		return new InlineContainerArea( this );
	}

	public void endLine( ) throws BirtException
	{
		lineCount++;
		if ( getChildrenCount( ) > 0 )
		{
			close( false );
		}
		if ( lineParent != null )
		{
			lineParent.removeChild( this );
			lineParent.endLine( );
			initialize( );
		}
	}

	public int getMaxLineWidth( )
	{
		return lineParent.getMaxLineWidth( );
	}

	public boolean isEmptyLine( )
	{
		if ( getChildrenCount( ) > 0 )
		{
			return false;
		}
		return lineParent.isEmptyLine( );
	}

	public SplitResult split( int height, boolean force ) throws BirtException
	{
		return SplitResult.SUCCEED_WITH_NULL;
	}

	public SplitResult splitLines( int lineCount ) throws BirtException
	{
		return SplitResult.SUCCEED_WITH_NULL;
	}

}
