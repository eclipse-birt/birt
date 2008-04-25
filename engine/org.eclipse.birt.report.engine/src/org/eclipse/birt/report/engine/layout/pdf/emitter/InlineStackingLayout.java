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

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;


public class InlineStackingLayout extends ContainerLayout implements IInlineStackingLayout
{

	public InlineStackingLayout( LayoutEngineContext context, ContainerLayout parentContext,
			IContent content )
	{
		super(context, parentContext, content );
	}
	
	public boolean addArea(AbstractArea area)
	{
		root.addChild( area );
		area.setAllocatedPosition( currentIP + offsetX, currentBP + offsetY );
		currentIP += area.getAllocatedWidth( );
		if ( currentIP + area.getAllocatedWidth( ) > root.getContentWidth( ))
		{
			root.setNeedClip( true );
		}
		else if( currentBP > maxAvaHeight )
		{
			root.setNeedClip( true );
		}
		return true;
	}

	protected void closeLayout( )
	{
		// TODO Auto-generated method stub
		
	}

	protected void createRoot( )
	{
		// TODO Auto-generated method stub
	}
	
	protected void verticalAlign()
	{
		Iterator iter = root.getChildren( );
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			IStyle childStyle = child.getStyle( );
			String vAlign = childStyle.getVerticalAlign( );
			if ( childStyle == null )
			{
				continue;
			}
			int spacing = root.getHeight( ) - child.getAllocatedHeight( );
			if ( spacing < 0 )
			{
				spacing = 0;
			}
			if ( CSSConstants.CSS_BASELINE_VALUE.equalsIgnoreCase( vAlign ) )
			{
				int lineHeight = ( (ContainerLayout) parent ).getLineHeight( );
				if ( lineHeight > 0 )
				{
					// align to middle, fix issue 164072
					child.setPosition( child.getX( ), child.getY( )
							+ getMaxBaseLine( ) - child.getBaseLine( ) + spacing / 2 );
				}
				else
				{
					child.setPosition( child.getX( ), child.getY( )
							+ getMaxBaseLine( ) - child.getBaseLine( ) );	
				}
				
			}
			else if ( CSSConstants.CSS_BOTTOM_VALUE.equalsIgnoreCase( vAlign ) )
			{
				child.setPosition( child.getX( ), spacing + child.getY( ) );
			}
			else if ( CSSConstants.CSS_MIDDLE_VALUE.equalsIgnoreCase( vAlign ) )
			{
				child.setPosition( child.getX( ), spacing / 2 + child.getY( ) );
			}
		}
	}
	
	private int getMaxBaseLine( )
	{
		int maxChildrenBaseLine = root.getMaxChildrenBaseLine( );
		if ( maxChildrenBaseLine == 0 )
		{
			Iterator iter = root.getChildren( );
			int maxChildrenBaseLineBelow = root.getMaxChildrenBaseLineBelow( );
			while ( iter.hasNext( ) )
			{
				AbstractArea child = (AbstractArea) iter.next( );
				maxChildrenBaseLine = Math.max( maxChildrenBaseLine, child.getBaseLine( ) );
				maxChildrenBaseLineBelow = Math.max( maxChildrenBaseLineBelow, child
						.getAllocatedHeight( )
						- child.getBaseLine( ) );
			}
			root.setContentHeight( Math.max( root.getContentHeight( ),
					maxChildrenBaseLine + maxChildrenBaseLineBelow ) );
			root.setBaseLine( maxChildrenBaseLine );
			root.setMaxChildrenBaseLine( maxChildrenBaseLine );
			root.setMaxChildrenBaseLineBelow( maxChildrenBaseLineBelow );
		}
		return maxChildrenBaseLine;
	}

	protected void initialize( )
	{
		// TODO Auto-generated method stub
		
	}

	public boolean endLine( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public int getMaxLineWidth( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEmptyLine( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void setTextIndent( ITextContent textContent )
	{
		// TODO Auto-generated method stub
		
	}

	
}
