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
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;

public class BlockTextArea extends BlockContainerArea implements ILayout
{
	int splitOffset;
	int splitHeight;
	
	public int getSplitOffset( )
	{
		return splitOffset;
	}

	public int getSplitHeight( )
	{
		return splitHeight;
	}
	
	/**
	 * lastTotalHeight indicates the total height that is split from the original text area.
	 * This value will be used as the splitOffset of the remain blockTextArea.
	 */
	private int lastTotalHeight;
	
	private int fixedDimension;
	
	public BlockTextArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
//		InstanceID id = content.getInstanceID( );
//		if ( id != null )
//		{
//			SizeBasedContent hint = (SizeBasedContent) context
//					.getSizeBasedContentMapping( ).get( id.toUniqueString( ) );
//			if ( hint != null )
//			{
//				currentBP -= hint.offsetInContent;
//				fixedDimension = hint.dimension;
//			}
//		}
	}

	public BlockTextArea(BlockTextArea area)
	{
		super(area);
	}
	
	public void layout( ) throws BirtException
	{
		initialize();
		TextLineArea line = new TextLineArea(this, context);
		line.initialize( );
		line.setTextIndent( (ITextContent)content );
		TextAreaLayout text = new TextAreaLayout( line, context, content);
		text.initialize( );
		text.layout( );
		text.close( );
		line.close( );
		close();
	}
	
	public BlockTextArea cloneArea()
	{
		BlockTextArea newBlockText =  new BlockTextArea(this);
		newBlockText.lastTotalHeight = lastTotalHeight;
		return newBlockText;
	}
	
	protected void updateContentHeight( int height )
	{
		super.updateContentHeight( height );
		splitOffset = lastTotalHeight;
		splitHeight = height;
		lastTotalHeight += splitHeight;
	}
	
	protected void update( ) throws BirtException
	{
		if ( parent != null )
		{
			if ( context.isFixedLayout( ) && height > specifiedHeight
					&& specifiedHeight > 0 )
			{
				setHeight( specifiedHeight );
				setNeedClip( true );
			}
			if ( !isInInlineStacking && context.isAutoPageBreak( ) )
			{
				int aHeight = getAllocatedHeight( );
				int size = children.size( );
				if ( ( aHeight + parent.getAbsoluteBP( ) > context.getMaxBP( ) )
						&& ( size > 1 ) )
				{
					IStyle style = content.getComputedStyle( );
					// Minimum number of lines of a paragraph that must appear
					// at the top of a page.
					int widow = Math.min( size, PropertyUtil.getIntValue( style
							.getProperty( IStyle.STYLE_WIDOWS ) ));
					// Minimum number of lines of a paragraph that must appear
					// at the bottom of a page.
					int orphan = Math.min( size, PropertyUtil.getIntValue( style
							.getProperty( IStyle.STYLE_ORPHANS ) ));
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
			if ( fixedDimension > 0 )
			{
				this.setAllocatedHeight( fixedDimension );
			}
			parent.update( this );
		}
	}
}
