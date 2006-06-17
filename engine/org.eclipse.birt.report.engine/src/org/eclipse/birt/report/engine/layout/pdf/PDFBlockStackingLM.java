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

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.content.BlockStackingExecutor;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

/**
 * represents block stacking layout manager
 * 
 */
public abstract class PDFBlockStackingLM extends PDFStackingLM
		implements
			IBlockStackingLayoutManager
{

	protected IReportItemExecutor blockExecutor = null;

	protected void addChild( PDFAbstractLM child )
	{
		this.child = child;
	}

	public PDFBlockStackingLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
	}

	protected boolean traverseChildren( )
	{
		boolean childBreak = false;
		if ( child != null )
		{
			child.setEmitter( emitter );
			childBreak = child.layout( );
			if ( childBreak )
			{
				if ( child.isFinished( ) )
				{
					child = null;
				}
				return true;
			}

		}
		if ( blockExecutor == null )
		{
			// get first child of this container
			blockExecutor = createExecutor( );
		}
		while ( blockExecutor.hasNextChild( ) )
		{
			IReportItemExecutor childExecutor = blockExecutor.getNextChild( );
			if(childExecutor!=null)
			{
				if ( layoutChildNode( childExecutor ) )
				{
					return true;
				}
			}
		}
		return false;
	}

	protected IReportItemExecutor createExecutor( )
	{
		return new BlockStackingExecutor( content, executor );
	}

	private boolean layoutChildNode( IReportItemExecutor childExecutor )
	{
		boolean childBreak = false;
		IContent childContent = childExecutor.execute( );
		PDFAbstractLM childLM = getFactory( ).createLayoutManager( this,
				childContent, emitter, childExecutor );
		childBreak = childLM.layout( );
		if ( childBreak && !childLM.isFinished( ) )
		{
			child = childLM;
		}
		return childBreak;
	}

	protected void newContext( )
	{
		createRoot( );
		// validateBoxProperty( root.getStyle( ) );
		if ( null != parent )
		{
			// support user defined width
			int max = parent.getMaxAvaWidth( ) - parent.getCurrentIP( );
			if ( content != null )
			{
				int specifiedWidth = getDimensionValue( content.getWidth( ) );
				if ( specifiedWidth > 0 )
				{
					max = Math.min( max, specifiedWidth );
				}
			}
			root.setAllocatedWidth( max );
			setMaxAvaWidth( root.getContentWidth( ) );
			root.setAllocatedHeight( parent.getMaxAvaHeight( )
					- parent.getCurrentBP( ) );
			setMaxAvaHeight( root.getContentHeight( ) );

		}
		// initialize offsetX and offsetY
		IStyle areaStyle = root.getStyle( );
		setOffsetX( getDimensionValue( areaStyle
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) )
				+ getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_PADDING_LEFT ) ) );
		setOffsetY( isFirst
				? ( getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) ) + getDimensionValue( areaStyle
						.getProperty( StyleConstants.STYLE_PADDING_TOP ) ) )
				: 0 );
		// can be removed?
		setCurrentBP( 0 );
		setCurrentIP( 0 );
	}

	protected void closeLayout( )
	{
		IStyle contentStyle = content.getComputedStyle( );
		IStyle areaStyle = root.getStyle( );
		// we needn't flush empty container?
		// if(root.getChildrenCount()==0)
		// {
		// return;
		// }
		if ( isLast )
		{
			// set dimension property for root TODO suppport user defined height
			areaStyle
					.setProperty(
							IStyle.STYLE_BORDER_BOTTOM_WIDTH,
							contentStyle
									.getProperty( IStyle.STYLE_BORDER_BOTTOM_WIDTH ) );
			areaStyle
					.setProperty(
							IStyle.STYLE_BORDER_BOTTOM_STYLE,
							contentStyle
									.getProperty( IStyle.STYLE_BORDER_BOTTOM_STYLE ) );
			areaStyle
					.setProperty(
							IStyle.STYLE_BORDER_BOTTOM_COLOR,
							contentStyle
									.getProperty( IStyle.STYLE_BORDER_BOTTOM_COLOR ) );
			areaStyle.setProperty( IStyle.STYLE_MARGIN_BOTTOM, contentStyle
					.getProperty( IStyle.STYLE_MARGIN_BOTTOM ) );
		}
		root
				.setHeight( getCurrentBP( )
						+ getOffsetY( )
						+ getDimensionValue( contentStyle
								.getProperty( StyleConstants.STYLE_PADDING_BOTTOM ) )
						+ getDimensionValue( contentStyle
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) ) );
	}

	public boolean addArea( IArea area )
	{
		// ignore empty area
		if ( area == null )
		{
			return true;
		}
		AbstractArea aArea = (AbstractArea) area;
		if ( aArea.getAllocatedHeight( ) + getCurrentBP( ) <= getMaxAvaHeight( )
				|| isPageEmpty( ) )
		{
			aArea.setAllocatedPosition( getCurrentIP( ) + getOffsetX( ),
					getCurrentBP( ) + getOffsetY( ) );
			setCurrentBP( getCurrentBP( ) + aArea.getAllocatedHeight( ) );
			root.addChild( area );
			return true;
		}

		return false;
	}

	public int getLineHeight( )
	{
		if ( content != null )
		{
			IStyle contentStyle = content.getComputedStyle( );
			return PropertyUtil.getLineHeight( contentStyle.getLineHeight( ) );
		}
		// FIXME return text size?
		return 0;
	}

	public String getTextAlign( )
	{
		if ( content != null )
		{
			IStyle contentStyle = content.getComputedStyle( );
			return contentStyle.getTextAlign( );
		}
		return null;
	}

	public int getTextIndent( )
	{
		if ( content != null )
		{
			IStyle contentStyle = content.getComputedStyle( );
			return getDimensionValue( contentStyle
					.getProperty( StyleConstants.STYLE_TEXT_INDENT ) );
		}
		return 0;
	}

	public boolean isInlineFlow( )
	{
		return false;
	}

	/*
	 * protected void setupMinHeight( ) { if(content!=null) { int
	 * specifiedHeight = PropertyUtil.getDimensionValue(content.getHeight( ));
	 * if(specifiedHeight>0 && specifiedHeight<getNMaxHeight( )) { minHeight =
	 * specifiedHeight; } } }
	 */

	protected void cancelChildren( )
	{
		if ( child != null )
		{
			child.cancel( );
		}
	}
}
