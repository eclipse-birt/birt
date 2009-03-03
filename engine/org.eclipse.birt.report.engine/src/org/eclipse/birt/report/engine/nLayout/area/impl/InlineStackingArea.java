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

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.w3c.dom.css.CSSValue;

public abstract class InlineStackingArea extends ContainerArea
{

	public InlineStackingArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
		this.isInlineStacking = true;
	}
	
	public InlineStackingArea( InlineStackingArea area )
	{
		super( area );
	}

	public abstract void endLine( ) throws BirtException;

	public abstract boolean isEmptyLine( );

	public abstract int getMaxLineWidth( );
	
	public void update( AbstractArea area ) throws BirtException
	{
		currentIP += area.getAllocatedWidth( );
	}
	
	public  void add(AbstractArea area)
	{
		children.add( area );
		area.setAllocatedPosition( currentIP + getOffsetX( ), currentBP
				+ getOffsetY( ) );

	}
	
	public int getBaseLine()
	{
		if(baseLine==0 && children.size( )>0)
		{
			Iterator iter = children.iterator( );
			while ( iter.hasNext( ) )
			{
				AbstractArea child = (AbstractArea) iter.next( );
				baseLine = Math.max( baseLine, child.getBaseLine( ) );
			}
		}
		return baseLine;
	}
	
	protected void verticalAlign( )
	{
		Iterator iter = getChildren( );
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			CSSValue vAlign = child.getVerticalAlign( );
			if ( IStyle.TOP_VALUE.equals( vAlign ) )
			{
				continue;
			}
			int spacing = getContentHeight( ) - child.getAllocatedHeight( );
			if ( spacing < 0 )
			{
				spacing = 0;
			}

			if (vAlign==null ||  IStyle.BASELINE_VALUE.equals( vAlign ) )
			{
				// FIXME to implement basline alignment
				child.setPosition( child.getX( ), child.getY( ) + getBaseLine() - child.getBaseLine( ) );
			}
			else if ( IStyle.BOTTOM_VALUE.equals( vAlign ) )
			{
				child.setPosition( child.getX( ), child.getY( ) + spacing );
			}
			else if ( IStyle.MIDDLE_VALUE.equals( vAlign ) )
			{
				child.setPosition( child.getX( ), child.getY( ) + spacing / 2 );
			}
		}
	}

}
