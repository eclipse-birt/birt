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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.birt.report.model.util.DimensionUtil;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * Line border for Label, Text and Data element.
 */

public class LineBorder extends BaseBorder
{

	private static final Insets DEFAULT_CROP = new Insets( 0, 0, 1, 1 );

	private Insets insets = new Insets( );

	private static final int TOP = 0;
	private static final int RIGHT = 1;
	private static final int BOTTOM = 2;
	private static final int LEFT = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets( IFigure figure )
	{
		int t = 1, b = 1, l = 1, r = 1;

		int style = 0;

		style = Integer.parseInt( getStyeSize( bottom_style ) );
		if ( style != 0 )
		{
			b = Integer.parseInt( getStyeWidth( bottom_width ).toString( ) );

			if ( style == -2 )
			{
				b = b * 2 + 1;
			}
		}

		style = Integer.parseInt( getStyeSize( top_style ) );
		if ( style != 0 )
		{
			t = Integer.parseInt( getStyeWidth( top_width ).toString( ) );

			if ( style == -2 )
			{
				t = t * 2 + 1;
			}
		}

		style = Integer.parseInt( getStyeSize( left_style ) );
		if ( style != 0 )
		{
			l = Integer.parseInt( getStyeWidth( left_width ).toString( ) );

			if ( style == -2 )
			{
				l = l * 2 + 1;
			}
		}

		style = Integer.parseInt( getStyeSize( right_style ) );
		if ( style != 0 )
		{
			r = Integer.parseInt( getStyeWidth( right_width ).toString( ) );

			if ( style == -2 )
			{
				r = r * 2 + 1;
			}
		}

		return new Insets( t, l, b, r ).add(insets);
	}

	/**
	 * Sets the insets for the border.
	 * 
	 * @param in
	 */
	public void setInsets( Insets in )
	{
		if ( in == null )
		{
			return;
		}

		if ( in.top >= 0 )
		{
			insets.top = in.top;
		}
		if ( in.bottom >= 0 )
		{
			insets.bottom = in.bottom;
		}
		if ( in.left >= 0 )
		{
			insets.left = in.left;
		}
		if ( in.right >= 0 )
		{
			insets.right = in.right;
		}
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
		drawBorder( figure,
				g,
				BOTTOM,
				i_bottom_style,
				i_bottom_width,
				bottom_color,
				insets );

		//draw top line
		drawBorder( figure, g, TOP, i_top_style, i_top_width, top_color, insets );

		//draw left line
		drawBorder( figure,
				g,
				LEFT,
				i_left_style,
				i_left_width,
				left_color,
				insets );

		//draw right line
		drawBorder( figure,
				g,
				RIGHT,
				i_right_style,
				i_right_width,
				right_color,
				insets );
	}

	private void drawBorder( IFigure figure, Graphics g, int side, int style,
			int width, String color, Insets insets )
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
			drawDefaultLine( figure, g, side, width, r );
		}

		g.restoreState( );
	}

	private void drawDoubleLine( IFigure figure, Graphics g, int side,
			int width, Rectangle r )
	{
		//draw the first line
		drawSingleLine( figure, g, side, SWT.LINE_SOLID, width, r );

		//draw the second line with 1 pixel interval
		g.setLineStyle( SWT.LINE_SOLID );

		switch ( side )
		{
			case BOTTOM :
				calLeftRightGap( );
				for ( int j = 0; j < width; j++ )
				{
					g.drawLine( r.x + leftGap,
							r.y + r.height - width - j - 1,
							r.x + r.width - rightGap,
							r.y + r.height - width - j - 1 );
				}
				break;
			case TOP :
				calLeftRightGap( );
				for ( int j = 0; j < width; j++ )
				{
					g.drawLine( r.x + leftGap, r.y + j + width + 1, r.x
							+ r.width
							- rightGap, r.y + j + width + 1 );
				}
				break;
			case LEFT :
				calTopBottomGap( );
				for ( int j = 0; j < width; j++ )
				{
					g.drawLine( r.x + width + 1 + j, r.y + topGap, r.x
							+ width
							+ 1
							+ j, r.y + r.height - bottomGap );
				}
				break;
			case RIGHT :
				calTopBottomGap( );
				for ( int j = 0; j < width; j++ )
				{
					g.drawLine( r.x + r.width - width - 1 - j,
							r.y + topGap,
							r.x + r.width - width - 1 - j,
							r.y + r.height - bottomGap );
				}
				break;
		}

	}

	private void drawSingleLine( IFigure figure, Graphics g, int side,
			int style, int width, Rectangle r )
	{
		g.setLineStyle( style );

		switch ( side )
		{
			case BOTTOM :
				for ( int i = 0; i < width; i++ )
				{
					g.drawLine( r.x, r.y + r.height - i, r.x + r.width, r.y
							+ r.height
							- i );
				}
				break;
			case TOP :
				for ( int i = 0; i < width; i++ )
				{
					g.drawLine( r.x, r.y + i, r.x + r.width, r.y + i );
				}
				break;
			case LEFT :
				for ( int i = 0; i < width; i++ )
				{
					g.drawLine( r.x + i, r.y, r.x + i, r.y + r.height );
				}
				break;
			case RIGHT :
				for ( int i = 0; i < width; i++ )
				{
					g.drawLine( r.x + r.width - i, r.y, r.x + r.width - i, r.y
							+ r.height );
				}
				break;
		}
	}

	private void drawDefaultLine( IFigure figure, Graphics g, int side,
			int width, Rectangle r )
	{
		drawSingleLine( figure, g, side, SWT.LINE_SOLID, 1, r );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.border.BaseBorder#getStyeWidth(java.lang.Object)
	 */
	protected String getStyeWidth( Object obj )
	{
		if ( obj instanceof String )
		{
			String[] rt = DEUtil.splitString( (String) obj );

			if ( rt[0] != null )
			{
				String target = DesignChoiceConstants.UNITS_PT;

				if ( DimensionUtil.isAbsoluteUnit( rt[1] ) )
				{
					if ( DEUtil.isValidNumber( rt[0] ) )
					{
						try
						{
							int width = (int) ( DimensionUtil.convertTo( rt[0],
									rt[1],
									target ) ).getMeasure( );

							return String.valueOf( width );
						}
						catch ( PropertyValueException e )
						{
							ExceptionHandler.handle( e );
						}
					}
				}

			}
		}

		return super.getStyeWidth( obj );
	}

}