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

package org.eclipse.birt.report.engine.layout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public abstract class ContainerArea extends AbstractArea
		implements
			IContainerArea
{
	protected boolean needClip;
	protected boolean isClippingContainer;
	
	ContainerArea( IContent content )
	{
		super( content );
	}

	ContainerArea( IReportContent report )
	{
		super( report );
	}

	protected ArrayList children = new ArrayList( );

	public Iterator getChildren( )
	{
		return children.iterator( );
	}

	public void addChild( IArea area )
	{
		if ( area.getX( ) < 0 || area.getX( ) + area.getWidth( ) > width
				|| area.getY( ) < 0
				|| area.getY( ) + area.getHeight( ) > height )
		{
			needClip = true;
		}
		children.add( area );
	}

	public void removeAll( )
	{
		children.clear( );
	}

	public void removeChild( IArea area )
	{
		children.remove( area );
	}

	public void accept( IAreaVisitor visitor )
	{
		visitor.visitContainer( this );
	}

	public int getChildrenCount( )
	{
		return children.size( );
	}

	public int getContentY( )
	{
		return PropertyUtil.getDimensionValue( style
				.getProperty( IStyle.STYLE_BORDER_TOP_WIDTH ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( IStyle.STYLE_PADDING_TOP ) );
	}

	public int getContentX( )
	{
		return PropertyUtil.getDimensionValue( style
				.getProperty( IStyle.STYLE_BORDER_LEFT_WIDTH ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( IStyle.STYLE_PADDING_LEFT ) );
	}
	
	//get height of empty container
	public int getIntrisicHeight()
	{
		return PropertyUtil.getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_TOP ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( IStyle.STYLE_PADDING_TOP ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( IStyle.STYLE_BORDER_TOP_WIDTH ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( IStyle.STYLE_MARGIN_BOTTOM ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( IStyle.STYLE_PADDING_BOTTOM ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( IStyle.STYLE_BORDER_BOTTOM_WIDTH ) );
	}

	public boolean needClip( )
	{
		return isClippingContainer && needClip;
	}
	
	public void setClip( boolean needClip )
	{
		this.needClip = needClip;
	}

	public void setIsClippingContainer( boolean isClippingContainer )
	{
		this.isClippingContainer = isClippingContainer;
	}
}
