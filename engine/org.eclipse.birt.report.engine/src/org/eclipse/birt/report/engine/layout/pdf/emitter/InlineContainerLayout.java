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
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.ILineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;


public class InlineContainerLayout extends InlineStackingLayout
		implements
			IInlineStackingLayout
{
	IInlineStackingLayout lineParent = null;
	
	public InlineContainerLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
		lineParent = (IInlineStackingLayout) parent ;
	}

	public void setTextIndent( ITextContent content )
	{
		lineParent.setTextIndent( content );
	}

	protected void closeLayout( )
	{
		//TODO support specified height/width/alignment
		if ( root != null )
		{
			IStyle areaStyle = root.getStyle( );
			int width = getCurrentIP( )
					+ getOffsetX( )
					+ getDimensionValue( areaStyle
							.getProperty( StyleConstants.STYLE_PADDING_RIGHT ) )
					+ getDimensionValue( areaStyle
							.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
			root.setWidth( width );
			int height = 0;
			Iterator iter = root.getChildren( );
			while(iter.hasNext())
			{
				AbstractArea child = (AbstractArea)iter.next( );
				height = Math.max( height, child.getAllocatedHeight( ));
			}
			root.setContentHeight( height );
		}
		//FIXME verticalAlign may effect the root height.
		verticalAlign();
		parent.addArea( root );

	}

	public boolean addArea( AbstractArea area )
	{
		root.addChild( area );
		area.setAllocatedPosition( currentIP, currentBP );
		currentIP += area.getAllocatedWidth( ) ;
		return true;
	}

	protected void createRoot( )
	{
		root = (ContainerArea)AreaFactory.createInlineContainer( content );
	}

	protected void initialize( )
	{
		createRoot( );
		maxAvaWidth =  parent.getCurrentMaxContentWidth( ) ;
		maxAvaHeight = parent.getCurrentMaxContentHeight( )  ;
		currentBP = 0;
		currentIP = 0;
	}

	public boolean endLine( )
	{
		boolean ret = true;
		if ( root != null && root.getChildrenCount( ) > 0 )
		{
			closeLayout( );
		}
		if ( lineParent!=null )
		{
			ret = lineParent.endLine( );
			initialize( );
		}
		return true;
	}

	public int getMaxLineWidth( )
	{
		return lineParent.getMaxLineWidth( );
	}

	public boolean isEmptyLine( )
	{
		if ( root != null && root.getChildrenCount( ) > 0 )
		{
			return false;
		}
		return lineParent.isEmptyLine( );
	}

}
