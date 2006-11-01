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
			PDFStackingLM parent, IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
	}

	protected boolean traverseChildren( )
	{
		boolean childBreak = false;
		if ( child != null )
		{
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
				childContent, childExecutor );
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
		validateBoxProperty( root.getStyle( ), parent.getMaxAvaWidth( ),
				context.getMaxHeight( ) );
		if ( null != parent )
		{
			calculateSpecifiedWidth( );
			// support user defined width
			int maxW = parent.getMaxAvaWidth( ) - parent.getCurrentIP( );
			if ( specifiedWidth > 0 )
			{
				maxW = Math.min( maxW, specifiedWidth );
			}
			root.setAllocatedWidth( maxW );
			setMaxAvaWidth( root.getContentWidth( ) );
			root.setAllocatedHeight( parent.getMaxAvaHeight( )
					- parent.getCurrentBP( ) );
			setMaxAvaHeight( root.getContentHeight( ) );
		}
		// initialize offsetX and offsetY
		setOffsetX( root.getContentX( ) );
		setOffsetY( isFirst ? root.getContentY( ) : 0 );
		// can be removed?
		setCurrentBP( 0 );
		setCurrentIP( 0 );
	}

	protected void closeLayout( )
	{
		IStyle areaStyle = root.getStyle( );
		if ( !isLast )
		{
			// set dimension property for root
			// TODO suppport user defined height
			areaStyle.setProperty( IStyle.STYLE_BORDER_BOTTOM_WIDTH,
					IStyle.NUMBER_0 );
			areaStyle.setProperty( IStyle.STYLE_PADDING_BOTTOM,
					IStyle.NUMBER_0 );
			areaStyle.setProperty( IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0 );
		}
		
		// FIXME
		root
				.setHeight( getCurrentBP( )
						+ getOffsetY( )
						+ getDimensionValue( areaStyle
								.getProperty( StyleConstants.STYLE_PADDING_BOTTOM ) )
						+ getDimensionValue( areaStyle
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH )) );
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
			return PropertyUtil.getLineHeight( contentStyle.getLineHeight( ));
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
					.getProperty( StyleConstants.STYLE_TEXT_INDENT ), maxAvaWidth );
		}
		return 0;
	}

	protected void cancelChildren( )
	{
		if ( child != null )
		{
			child.cancel( );
		}
	}
	
	protected boolean hasNextChild()
	{
		if(child==null && (blockExecutor!=null && !blockExecutor.hasNextChild( )))
		{
			return false;
		}
		return true;
				
	}
}
