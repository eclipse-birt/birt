/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Border for table cell.
 */

public class CellBorder extends LineBorder
{

	private static final Insets DEFAULT_CROP = new Insets( 2, 2, 2, 2 );
	private static final Insets DEFAULT_SPACE_CROP = new Insets( 2, 2, 1, 1 );

	private static final Insets DEFAULTINSETS = new Insets( 5, 5, 4, 4 );

	private Insets insets = new Insets( DEFAULTINSETS );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets( IFigure figure )
	{
		return getTrueBorderInsets( ).add( insets );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder#getBorderInsets()
	 */
	public Insets getBorderInsets( )
	{
		return new Insets( super.getBorderInsets( ) ).add( DEFAULT_SPACE_CROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder#setPaddingInsets(org.eclipse.draw2d.geometry.Insets)
	 */
	public void setPaddingInsets( Insets in )
	{
		if ( in == null
				|| ( in.left == 0 && in.right == 0 && in.top == 0 && in.bottom == 0 ) )
		{
			insets = new Insets( DEFAULTINSETS );
			return;
		}

		insets.top = in.top > DEFAULTINSETS.top ? in.top : DEFAULTINSETS.top;
		insets.bottom = in.bottom > DEFAULTINSETS.bottom ? in.bottom
				: DEFAULTINSETS.bottom;
		insets.left = in.left > DEFAULTINSETS.left ? in.left
				: DEFAULTINSETS.left;
		insets.right = in.right > DEFAULTINSETS.right ? in.right
				: DEFAULTINSETS.right;
	}

	/**
	 * @param figure
	 * @param g
	 * @param side
	 * @param style
	 * @param width
	 *            the border width array, arranged by {top, bottom, left,
	 *            right};
	 * @param color
	 * @param insets
	 */
	protected void drawBorder( IFigure figure, Graphics g, int side, int style,
			int[] width, String color, Insets insets )
	{
		Rectangle r = figure.getBounds( )
				.getCopy( )
				.crop( DEFAULT_CROP )
				.crop( insets );

		if ( style != 0 )
		{
			//set ForegroundColor with the given color
			g.setForegroundColor( ColorManager.getColor( ColorUtil.parseColor( color ) ) );

			//if the border style is set to "double",
			//draw a double line with the given width and style of "solid"
			if ( style == -2 )
			{
				drawDoubleLine( figure, g, side, width, r );
			}
			// if the border style is set to "solid", "dotted" or "dashed",
			//draw a single line according to the give style and width
			else
			{
				drawSingleLine( figure, g, side, style, width, r );
			}
		}
		else
		{
			g.setForegroundColor( ColorConstants.lightGray );
			//if the border style is set to none, draw a default dot line in
			// black as default
			drawDefaultLine( figure, g, side, r );
		}

		g.restoreState( );
	}
}