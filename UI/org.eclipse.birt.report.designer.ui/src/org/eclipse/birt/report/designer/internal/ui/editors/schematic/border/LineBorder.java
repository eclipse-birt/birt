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

/**
 * Line border for Label, Text and Data element.
 */

public class LineBorder extends BaseBorder
{

	private static final Insets DEFAULT_CROP = new Insets( 0, 0, 1, 1 );

	private Insets paddingInsets = new Insets( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets( IFigure figure )
	{
		int t = 1, b = 1, l = 1, r = 1;

		int style = 0;

		style = getBorderStyle( bottom_style );
		if ( style != 0 )
		{
			b = getBorderWidth( bottom_width );
		}

		style = getBorderStyle( top_style );
		if ( style != 0 )
		{
			t = getBorderWidth( top_width );
		}

		style = getBorderStyle( left_style );
		if ( style != 0 )
		{
			l = getBorderWidth( left_width );
		}

		style = getBorderStyle( right_style );
		if ( style != 0 )
		{
			r = getBorderWidth( right_width );
		}

		return new Insets( t, l, b, r ).add( paddingInsets );
	}

	/**
	 * Sets the insets for the border.
	 * 
	 * @param in
	 */
	public void setInsets( Insets padding )
	{
		if ( padding != null )
		{
			if ( padding.top >= 0 )
			{
				paddingInsets.top = padding.top;
			}
			if ( padding.bottom >= 0 )
			{
				paddingInsets.bottom = padding.bottom;
			}
			if ( padding.left >= 0 )
			{
				paddingInsets.left = padding.left;
			}
			if ( padding.right >= 0 )
			{
				paddingInsets.right = padding.right;
			}
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
		i_bottom_style = getBorderStyle( bottom_style );
		i_bottom_width = getBorderWidth( bottom_width );

		i_top_style = getBorderStyle( top_style );
		i_top_width = getBorderWidth( top_width );

		i_left_style = getBorderStyle( left_style );
		i_left_width = getBorderWidth( left_width );

		i_right_style = getBorderStyle( right_style );
		i_right_width = getBorderWidth( right_width );

		g.restoreState( );

		//draw bottom line
		drawBorder( figure, g, BOTTOM, i_bottom_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, bottom_color, insets );

		//draw top line
		drawBorder( figure, g, TOP, i_top_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, top_color, insets );

		//draw left line
		drawBorder( figure, g, LEFT, i_left_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, left_color, insets );

		//draw right line
		drawBorder( figure, g, RIGHT, i_right_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, right_color, insets );
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
	private void drawBorder( IFigure figure, Graphics g, int side, int style,
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.border.BaseBorder#getStyeWidth(java.lang.Object)
	 */
	protected int getBorderWidth( Object obj )
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

							return width;
						}
						catch ( PropertyValueException e )
						{
							ExceptionHandler.handle( e );
						}
					}
				}

			}
		}

		return super.getBorderWidth( obj );
	}

}