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
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea.SplitResult;


public class TextLineArea extends LineArea
{

	public TextLineArea( ContainerArea parent, LayoutContext context )
	{
		super( parent, context );
		//support widow and orphans, do not auto-pagebreak in textlineArea
		isInInlineStacking = true;
	}

	public TextLineArea(TextLineArea area)
	{
		super(area);
	}
	
	public TextLineArea cloneArea( )
	{
		return new TextLineArea(this);
	}
	
	public SplitResult splitLines( int lineCount ) throws BirtException
	{
		if( pageBreakBefore == IStyle.AVOID_VALUE)
		{
			return SplitResult.BEFORE_AVOID_WITH_NULL;
		}
		return SplitResult.SUCCEED_WITH_NULL;
	}


	public SplitResult split( int height, boolean force ) throws BirtException
	{
		if ( force )
		{
			TextLineArea newArea = cloneArea( );
			newArea.children.addAll( children );
			children.clear( );
			this.height = 0;
			return new SplitResult( newArea,
					SplitResult.SPLIT_SUCCEED_WITH_PART );
		}
		if ( pageBreakBefore == IStyle.AVOID_VALUE )
		{
			return SplitResult.BEFORE_AVOID_WITH_NULL;
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
	
	public void setTextIndent( ITextContent content )
	{
		if( children.isEmpty( ) )
		{
			if ( content != null )
			{
				IStyle contentStyle = content.getComputedStyle( );
				currentIP =  getDimensionValue( contentStyle
						.getProperty( StyleConstants.STYLE_TEXT_INDENT ), maxAvaWidth ) ;
			}
		}
	}

}
