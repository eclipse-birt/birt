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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;


public abstract class ContainerLayout extends Layout
{
	
	protected ContainerArea root;
	
	protected int currentIP = 0;
	
	protected int currentBP = 0;
		
	protected int maxAvaHeight = 0;
	
	protected int maxAvaWidth = 0;
	
	protected int offsetX = 0;

	protected int offsetY = 0;

	
	public ContainerLayout( LayoutEngineContext context, ContainerLayout parentContext, IContent content )
	{
		super(context, parentContext, content);
	}
	
	public void layout()
	{
		
	}
	
	public abstract boolean addArea(AbstractArea area);
	
	public int getMaxAvaHeight()
	{
		return maxAvaHeight;
	}
	
	public int getCurrentMaxContentWidth( )
	{
		return maxAvaWidth - currentIP;
	}
	
	public int getCurrentMaxContentHeight()
	{
		return maxAvaHeight - currentBP;
	}


	public int getCurrentIP( )
	{
		return currentIP;
	}

	public int getCurrentBP( )
	{
		return this.currentBP;
	}


	public int getOffsetX( )
	{
		return offsetX;
	}


	public int getOffsetY( )
	{
		return offsetY;
	}



	protected boolean isRootEmpty( )
	{
		return !( root != null && root.getChildrenCount( ) > 0 );
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
		
	
	
}
