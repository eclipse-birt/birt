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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;


public class TextLineArea extends LineArea
{

	public TextLineArea( ContainerArea parent, LayoutContext context )
	{
		super( parent, context );
	}

	public TextLineArea(TextLineArea area)
	{
		super(area);
	}
	
	public TextLineArea cloneArea( )
	{
		return new TextLineArea(this);
	}

	public SplitResult split( int height, boolean force ) throws BirtException
	{
		if ( force )
		{
			TextLineArea newArea= cloneArea();
			newArea.children.addAll( children );
			children.clear( );
			this.height = 0;
			return new SplitResult( newArea, SplitResult.SPLIT_SUCCEED_WITH_PART );
		}
		return SplitResult.SUCCEED_WITH_NULL;
	}
	
	public void update( AbstractArea area ) throws BirtException
	{
		super.update( area );
		if ( currentIP + area.getAllocatedWidth( ) >getContentWidth( ))
		{
			setNeedClip( true );
		}
		
	}

	public boolean isPageBreakInsideAvoid()
	{
		return true;
	}

}
