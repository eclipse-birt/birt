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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;

public class BlockTextArea extends BlockContainerArea implements ILayout
{
	private BlockTextRenderListener listener = null;
	
	public BlockTextArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
		if ( context.isInHtmlRender( ) )
		{
			InstanceID id = content.getInstanceID( );
			if ( id != null )
			{
				SizeBasedContent hint = (SizeBasedContent) context
						.getHtmlLayoutContext( ).getPageHintManager( )
						.getSizeBasedContentMapping( ).get( id.toUniqueString( ) );
				if ( hint != null )
				{
					parent.width = hint.width;
					listener = new BlockTextRenderListener( this,
							hint.offsetInContent, hint.dimension );
				}
			}	
		}
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
		if( context.isInHtmlRender( ) )
		{
			text.addListener( listener );	
		}
		text.layout( );
		text.close( );
		line.close( );
		close( );
	}
	
	public BlockTextArea cloneArea( )
	{
		BlockTextArea newArea = new BlockTextArea( this );
		addToExtension( newArea );
		return newArea;
	}
	
	public void close( ) throws BirtException
	{
		super.close( );
		addToExtension( this );
		updateTextContent( );
	}
	
	private void addToExtension( BlockTextArea area )
	{
		if ( context.isFixedLayout( )
				&& context.getEngineTaskType( ) == IEngineTask.TASK_RUN )
		{
			ArrayList<BlockTextArea> list = (ArrayList<BlockTextArea>) content
					.getExtension( IContent.LAYOUT_EXTENSION );
			if ( list == null )
			{
				list = new ArrayList<BlockTextArea>( );
				content.setExtension( IContent.LAYOUT_EXTENSION, list );
			}
			list.add( area );
		}
	}
	
	private void updateTextContent( )
	{
		if( context.isInHtmlRender( ))
		{
			((ITextContent)content).setText( listener.getSplitText( ) );
		}
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
			parent.update( this );
		}
	}
}
