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

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * This class represents the cell border
 *  
 */

public class CellBorder extends BaseBorder
{

	private static final Insets DEFAULTINSETS = new Insets( 2, 2, 2, 2 );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets( IFigure figure )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure,
	 *      org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint( IFigure figure, Graphics g, Insets insets )
	{
		i_bottom_style = Integer.parseInt( getStyeSize( bottom_style ).toString( ) );
		i_bottom_width = Integer.parseInt( getStyeWidth( bottom_width ).toString( ) );

		i_top_style = Integer.parseInt( getStyeSize( top_style ).toString( ) );
		i_top_width = Integer.parseInt( getStyeWidth( top_width ).toString( ) );

		i_left_style = Integer.parseInt( getStyeSize( left_style ).toString( ) );
		i_left_width = Integer.parseInt( getStyeWidth( left_width ).toString( ) );

		i_right_style = Integer.parseInt( getStyeSize( right_style ).toString( ) );
		i_right_width = Integer.parseInt( getStyeWidth( right_width ).toString( ) );

		//draw bottom line
		drawBorder( figure, g, "bottom",//$NON-NLS-1$
				i_bottom_style, i_bottom_width, bottom_color );

		//draw top line
		drawBorder( figure, g, "top", i_top_style, i_top_width, top_color );//$NON-NLS-1$

		//draw left line
		drawBorder( figure, g, "left", i_left_style, i_left_width, left_color );//$NON-NLS-1$

		//draw right line
		drawBorder( figure, g, "right",//$NON-NLS-1$
				i_right_style, i_right_width, right_color );

	}

	/**
	 * draw the border line with give style, width and color
	 * 
	 * @param figure
	 * @param g
	 * @param side
	 * @param style
	 * @param width
	 * @param color
	 */
	private void drawBorder( IFigure figure, Graphics g, String side,
			int style, int width, String color )
	{
		Rectangle r = figure.getBounds( ).getCopy( ).crop( DEFAULTINSETS );// figure.getInsets(
																		   // ) );

		//draw line
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
			//if the border style is set to solid, draw a 1 width line in
			// gray as default
			g.setForegroundColor( ReportColorConstants.InnerLineColor );
			drawDefaultLine( figure, g, side, r, width );
		}

		g.restoreState( );
	}

	/**
	 * Draw a double-line is equivalent to draw a solid-line twice with the
	 * interval of 1 pixel
	 * 
	 * @param g
	 * @param side
	 * @param width
	 * @param r
	 */

	private void drawDoubleLine( IFigure figure, Graphics g, String side,
			int width, Rectangle r )
	{
		//draw the first line
		drawSingleLine( figure, g, side, SWT.LINE_SOLID, width, r );
		//draw the second line with 1 pixel interval
		g.setLineStyle( SWT.LINE_SOLID );
		if ( side.equals( "bottom" ) )//$NON-NLS-1$
		{
			calLeftRightGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.x + leftGap, r.y + r.height - width - j - 1, r.x
						+ r.width - rightGap, r.y + r.height - width - j - 1 );
			}
		}
		if ( side.equals( "top" ) )//$NON-NLS-1$
		{
			calLeftRightGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.x + leftGap, r.y - j + width + 1, r.x
						+ r.width - rightGap, r.y - j + width + 1 );
			}
		}
		if ( side.equals( "left" ) )//$NON-NLS-1$
		{
			calTopBottomGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.x + width + 1 + j, r.y + topGap, r.x
						+ width + 1 + j, r.y + r.height - bottomGap );
			}
		}
		if ( side.equals( "right" ) )//$NON-NLS-1$
		{
			calTopBottomGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.x + r.width - width - 1 - j, r.y + topGap, r.x
						+ r.width - width - 1 - j, r.y + r.height - bottomGap );
			}
		}
	}

	/**
	 * draw a single line with given style and width
	 * 
	 * @param g
	 * @param side
	 * @param style
	 * @param width
	 * @param r
	 */
	private void drawSingleLine( IFigure figure, Graphics g, String side,
			int style, int width, Rectangle r )
	{
		g.setLineStyle( style );
		if ( side.equals( "bottom" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.x, r.y + r.height - i, r.x + r.width, r.y
						+ r.height - i );
			}
		}
		if ( side.equals( "top" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.x, r.y + i, r.x + r.width, r.y + i );
			}
		}
		if ( side.equals( "left" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.x + i, r.y, r.x + i, r.y + r.height );
			}
		}
		if ( side.equals( "right" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.x + r.width - i, r.y, r.x + r.width - i, r.y
						+ r.height );
			}
		}
	}

	/**
	 * Draw a black dot-line
	 * 
	 * @param figure
	 * @param g
	 * @param side
	 * @param r
	 */
	private void drawDefaultLine( IFigure figure, Graphics g, String side,
			Rectangle r, int width )
	{
		drawSingleLine( figure, g, side, SWT.LINE_SOLID, 1, r );
	}
}