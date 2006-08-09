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
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class PDFTextInlineBlockLM extends PDFBlockStackingLM
		implements
			IBlockStackingLayoutManager
{

	public PDFTextInlineBlockLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
	}

	protected void newContext( )
	{
		createRoot( );
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
		if ( null != parent )
		{
			// support user defined width
			int maxWidth = parent.getMaxAvaWidth( );
			int leftWidth = maxWidth - parent.getCurrentIP( );
			int specifiedWidth = 0;
			int width = 0;
			if ( content != null )
			{
				specifiedWidth = getDimensionValue( content.getWidth( ) );
			}
			if ( specifiedWidth > 0 )
			{
				width = Math.min( specifiedWidth, maxWidth );
			}
			else
			{
				if ( leftWidth > maxWidth / 4 )
				{
					width = leftWidth;
				}
				else
				{
					width = maxWidth;
				}
			}

			root.setAllocatedWidth( width );
			setMaxAvaWidth( root.getContentWidth( ) );
			root.setAllocatedHeight( parent.getMaxAvaHeight( )
					- parent.getCurrentBP( ) );
			setMaxAvaHeight( root.getContentHeight( ) );
		}
		// can be removed?
		setCurrentBP( 0 );
		setCurrentIP( 0 );
	}

	protected void createRoot( )
	{
		root = (ContainerArea) AreaFactory.createBlockContainer( content );
	}

}
