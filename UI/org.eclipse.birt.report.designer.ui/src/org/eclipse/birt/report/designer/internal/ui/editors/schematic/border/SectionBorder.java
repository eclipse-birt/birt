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
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

/**
 * This class draws section border
 */
public class SectionBorder extends BaseBorder
{

	private static final Insets DEFAULTINSETS = new Insets( 3, 2, 23, 3 );
	//private static final Insets DEFAULTINSETS = new Insets( 3, 2, 3, 3 );
	public static final int LEFT = 10;
	private Insets insets = new Insets( DEFAULTINSETS );
	private Dimension indicatorDimension = new Dimension( );
	protected String indicatorLabel = "";//$NON-NLS-1$
	protected Image image;
	protected int gap = 0;
	protected Insets gapInsets = new Insets( 2, 2, 2, 2 );
	private Rectangle indicatorArea;

	/*
	 * gets the insets (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets( IFigure figure )
	{
		return new Insets( insets );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public void setInsets( Insets in )
	{
		if ( in != null
				&& in.left == 0
				&& in.right == 0
				&& in.top == 0
				&& in.bottom == 0 )
		{
			insets = new Insets( DEFAULTINSETS );
			return;
		}
		insets.top = in.top > 0 ? in.top : DEFAULTINSETS.top;

		insets.bottom = ( in.bottom > indicatorDimension.height && in.bottom > DEFAULTINSETS.bottom ) ? in.bottom
				: DEFAULTINSETS.bottom;

		insets.left = in.left > 0 ? in.left : DEFAULTINSETS.left;
		insets.right = in.right > 0 ? in.right : DEFAULTINSETS.right;
	}

	/*
	 * paint the border ----------------------------- | | | | | | |
	 * ------------------------- |___| (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure,
	 *      org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint( IFigure figure, Graphics g, Insets in )
	{

		i_bottom_style = Integer.parseInt( getStyeSize( bottom_style ).toString( ) );
		i_bottom_width = Integer.parseInt( getStyeWidth( bottom_width ).toString( ) );

		i_top_style = Integer.parseInt( getStyeSize( top_style ).toString( ) );
		i_top_width = Integer.parseInt( getStyeWidth( top_width ).toString( ) );

		i_left_style = Integer.parseInt( getStyeSize( left_style ).toString( ) );
		i_left_width = Integer.parseInt( getStyeWidth( left_width ).toString( ) );

		i_right_style = Integer.parseInt( getStyeSize( right_style ).toString( ) );
		i_right_width = Integer.parseInt( getStyeWidth( right_width ).toString( ) );

		//draw top line
		drawBorder( figure, g, in, "top", i_top_style, i_top_width, top_color );//$NON-NLS-1$

		//draw bottom line
		drawBorder( figure, g, in, "bottom",//$NON-NLS-1$
				i_bottom_style, i_bottom_width, bottom_color );

		//draw left line
		drawBorder( figure, g, in, "left",//$NON-NLS-1$
				i_left_style, i_left_width, left_color );

		//draw right line
		drawBorder( figure, g, in, "right",//$NON-NLS-1$
				i_right_style, i_right_width, right_color );

	}

	/**
	 * Draw border of the section
	 * 
	 * @param figure
	 * @param g
	 * @param in
	 * @param side
	 * @param style
	 * @param width
	 * @param color
	 */
	private void drawBorder( IFigure figure, Graphics g, Insets in,
			String side, int style, int width, String color )
	{
		Rectangle r = figure.getBounds( ).getCropped( in );
		//Outline the border
		indicatorDimension = calculateIndicatorDimension( g, width );
		
		//if the border style is not set to "none", draw line with given style,
		// width and color
		if ( style != 0 )
		{
			//set foreground color
			g.setForegroundColor( ColorManager.getColor( ColorUtil.parseColor( color ) ) );
			if ( style == -2 )
			{
				//drawDouble line
				DrawDoubleLine( figure, g, in, side, width, r );
			}
			else
			{
				//draw single line
				DrawSingleLine( figure, g, in, side, style, width, r );
			}
		}

		//if the border style is set to "none", draw a black solid line as
		// default
		else
		{
			g.setForegroundColor( ColorConstants.lightGray );
			//draw default line
			DrawDefaultLine( figure, g, in, side, r, width );
		}

		g.restoreState( );
	}

	/**
	 * draw a single line with given style and width
	 * 
	 * @param figure
	 * @param g
	 * @param in
	 * @param side
	 * @param style
	 * @param width
	 * @param r
	 */
	private void DrawSingleLine( IFigure figure, Graphics g, Insets in,
			String side, int style, int width, Rectangle r )
	{
		g.setLineStyle( style );
		if ( side.equals( "bottom" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.x + indicatorDimension.width, r.bottom( )
						- 1
						- indicatorDimension.height
						- i, r.right( ) - 1, r.bottom( )
						- 1
						- indicatorDimension.height
						- i );
			}
			drawIndicator( g,
					figure.getBounds( ).getCropped( in ),
					indicatorDimension,
					style,
					width,
					"bottom",//$NON-NLS-1$
					false );
		}
		if ( side.equals( "top" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.x, r.y + i, r.right( ) - 1, r.y + i );
			}
		}
		if ( side.equals( "left" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.x + i, r.y, r.x + i, r.bottom( )
						- 1
						- indicatorDimension.height );
			}
			drawIndicator( g,
					figure.getBounds( ).getCropped( in ),
					indicatorDimension,
					style,
					width,
					"left",//$NON-NLS-1$
					false );
		}
		if ( side.equals( "right" ) )//$NON-NLS-1$
		{
			for ( int i = 0; i < width; i++ )
			{
				g.drawLine( r.right( ) - 1 - i, r.bottom( )
						- 1
						- indicatorDimension.height, r.right( ) - 1 - i, r.y );
			}
		}
	}

	/**
	 * Draw a double-line is equivalent to draw a solid-line twice with the
	 * interval of 1 pixel
	 * 
	 * @param figure
	 * @param g
	 * @param in
	 * @param side
	 * @param width
	 * @param r
	 */

	private void DrawDoubleLine( IFigure figure, Graphics g, Insets in,
			String side, int width, Rectangle r )
	{
		//draw the first line
		DrawSingleLine( figure, g, in, side, SWT.LINE_SOLID, width, r );
		//draw the second line with 1 pixel interval
		g.setLineStyle( SWT.LINE_SOLID );
		if ( side.equals( "bottom" ) )//$NON-NLS-1$
		{
			calLeftRightGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.x + indicatorDimension.width - 1 - width,
						r.bottom( )
								- 1
								- indicatorDimension.height
								- j
								- width
								- 1,
						r.right( ) - 1 - rightGap,
						r.bottom( )
								- 1
								- indicatorDimension.height
								- j
								- width
								- 1 );
			}
			drawIndicator( g,
					figure.getBounds( ).getCropped( in ),
					indicatorDimension,
					SWT.LINE_SOLID,
					width,
					"bottom",//$NON-NLS-1$
					true );
		}
		if ( side.equals( "top" ) )//$NON-NLS-1$
		{
			calLeftRightGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.x + leftGap, r.y + j + width + 1, r.right( )
						- 1
						- rightGap, r.y + j + width + 1 );
			}
		}
		if ( side.equals( "left" ) )//$NON-NLS-1$
		{
			calTopBottomGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.x + j + width + 1, r.y + topGap, r.x
						+ j
						+ width
						+ 1, r.bottom( ) - 1 - indicatorDimension.height );
			}
			drawIndicator( g,
					figure.getBounds( ).getCropped( in ),
					indicatorDimension,
					SWT.LINE_SOLID,
					width,
					"left", //$NON-NLS-1$
					true );
		}
		if ( side.equals( "right" ) )//$NON-NLS-1$
		{
			calTopBottomGap( );
			for ( int j = 0; j < width; j++ )
			{
				g.drawLine( r.right( ) - 1 - j - width - 1, r.bottom( )
						- 1
						- indicatorDimension.height
						- bottomGap, r.right( ) - 1 - j - width - 1, r.y
						+ topGap );
			}
		}

	}

	/**
	 * Draw a black solid line
	 * 
	 * @param figure,
	 *            g, in, side, r, width
	 */
	private void DrawDefaultLine( IFigure figure, Graphics g, Insets in,
			String side, Rectangle r, int width )
	{
		DrawSingleLine( figure, g, in, side, SWT.LINE_SOLID, 1, r );

	}

	/**
	 * draw the left corner
	 * 
	 * @param g
	 * @param rec
	 * @param indicatorDimension
	 */
	private void drawIndicator( Graphics g, Rectangle rec,
			Dimension indicatorDimension, int style, int width, String side,
			boolean db )
	{
		Dimension cale = calculateIndicatorDimension( g, width );
		int indicatorWidth = cale.width;
		int indicatorHeight = cale.height;
		indicatorArea = new Rectangle( rec.x,
				rec.bottom( ) - indicatorHeight,
				indicatorWidth,
				indicatorHeight );

		g.setLineStyle( style );
		if ( side.equals( "bottom" ) )//$NON-NLS-1$
		{
			if ( db == false )
			{
				for ( int i = 0; i < width; i++ )
				{
					g.drawLine( indicatorArea.x,
							indicatorArea.bottom( ) - 1 - i,
							indicatorArea.x + indicatorDimension.width,
							indicatorArea.bottom( ) - 1 - i );
					g.drawLine( indicatorArea.x + indicatorDimension.width + i,
							indicatorArea.y,
							indicatorArea.x + indicatorDimension.width + i,
							indicatorArea.bottom( ) - 1 );
				}
			}
			//if the border style is "double", draw the second line with 1
			// pixel inside the Indicator
			else
			{
				for ( int i = 0; i < width; i++ )
				{
					g.drawLine( indicatorArea.x + leftGap,
							indicatorArea.bottom( ) - 1 - i - width - 1,
							indicatorArea.x
									+ indicatorDimension.width
									- 1
									- width,
							indicatorArea.bottom( ) - 1 - i - width - 1 );
					g.drawLine( indicatorArea.x
							+ indicatorDimension.width
							+ i
							- width
							- 1, indicatorArea.y - 1 - width, indicatorArea.x
							+ indicatorDimension.width
							+ i
							- width
							- 1, indicatorArea.bottom( ) - 1 - 1 - width );
				}
			}
			//draw text "table"
			int x = indicatorArea.x + gapInsets.left;
			if ( image != null )
			{
				g.drawImage( image, x + 4, indicatorArea.y + gapInsets.top - 3 );
				x += image.getBounds( ).width + gap;
			}

			g.drawString( indicatorLabel, x + 2 * width + 2, indicatorArea.y
					+ gapInsets.top
					- width );

		}
		if ( side.equals( "left" ) )//$NON-NLS-1$
		{
			if ( db == false )
			{
				for ( int j = 0; j < width; j++ )
				{
					g.drawLine( indicatorArea.x + j,
							indicatorArea.y,
							indicatorArea.x + j,
							indicatorArea.bottom( ) - 1 );
				}
			}
			else
			{
				for ( int j = 0; j < width; j++ )
				{
					g.drawLine( indicatorArea.x + j + width + 1,
							indicatorArea.y,
							indicatorArea.x + j + width + 1,
							indicatorArea.bottom( ) - 1 - bottomGap );
				}
			}
		}

	}

	/**
	 * Sets the left corner label
	 * 
	 * @param indicatorLabel
	 */
	public void setIndicatorLabel( String indicatorLabel )
	{
		if ( indicatorLabel != null )
		{
			this.indicatorLabel = indicatorLabel;
		}
	}

	/**
	 * Sets the left corner
	 * 
	 * @param image
	 */
	public void setIndicatorIcon( Image image )
	{
		this.image = image;
	}

	/**
	 * calculates the left corner size
	 * 
	 * @return
	 */
	private Dimension calculateIndicatorDimension( Graphics g, int width )
	{
		gap = 0;
		Dimension iconDimension = new Dimension( );
		if ( image != null )
		{
			iconDimension = new Dimension( image );
			gap = 3;
		}
		Dimension d = FigureUtilities.getTextExtents( indicatorLabel,
				g.getFont( ) );
		int incheight = 0;
		if ( iconDimension.height > d.height )
		{
			incheight = iconDimension.height - d.height;
		}
		d.expand( iconDimension.width
				+ gap
				+ gapInsets.left
				+ gapInsets.right
				+ 4
				* width
				+ 2, incheight + gapInsets.top + gapInsets.bottom );

		return d;
	}

	/**
	 * gets the left corner size
	 * 
	 * @return
	 */
	public Rectangle getIndicatorArea( )
	{

		return indicatorArea;
	}

}