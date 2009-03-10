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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;
import org.w3c.dom.css.CSSValue;



public class BlockTextArea extends BlockContainerArea implements ILayout
{
	public BlockTextArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
	}
	
	public BlockTextArea(BlockTextArea area)
	{
		super(area);
	}
	
	public void layout( ) throws BirtException
	{
		initialize();
		LineArea line = new TextLineArea(this, context);
		line.initialize( );
		TextAreaLayout text = new TextAreaLayout( line, context, content);
		text.initialize( );
		text.layout( );
		text.close( );
		line.close( );
		close();
	}
	
	public BlockTextArea cloneArea()
	{
		return new BlockTextArea(this);
	}
	
	protected void update( ) throws BirtException
	{
		if ( parent != null )
		{
			if ( !isInInlineStacking && context.isAutoPageBreak( ) )
			{
				int aHeight = getAllocatedHeight( );
				int size = children.size( );
				if ( ( aHeight + parent.getAbsoluteBP( ) >= context.getMaxBP( ) )
						&& ( size > 1 ) )
				{
					IStyle style = content.getComputedStyle( );
					// Minimum number of lines of a paragraph that must appear
					// at the top of a page.
					int widow = PropertyUtil.getIntValue( style
							.getProperty( IStyle.STYLE_WIDOWS ) );
					// Minimum number of lines of a paragraph that must appear
					// at the bottom of a page.
					int orphan = PropertyUtil.getIntValue( style
							.getProperty( IStyle.STYLE_ORPHANS ) );
					for ( int i = 0; i < size; i++ )
					{
						TextLineArea line = (TextLineArea) children.get( i );
						if ( i > 0 && i < orphan )
						{
							line.setPageBreakBefore( IStyle.AVOID_VALUE );
						}
						else if ( i > size - widow )
						{
							line.setPageBreakBefore( IStyle.AVOID_VALUE );
						}
					}
				}
				while ( aHeight + parent.getAbsoluteBP( ) >= context.getMaxBP( ) )
				{
					parent.autoPageBreak( );
					aHeight = getAllocatedHeight( );
				}
			}
			parent.update( this );
		}
	}
	
	
	
}
