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
		i_bottom_style = getBorderStyle( bottom_style );
		i_bottom_width = getBorderWidth( bottom_width );

		i_top_style = getBorderStyle( top_style );
		i_top_width = getBorderWidth( top_width );

		i_left_style = getBorderStyle( left_style );
		i_left_width = getBorderWidth( left_width );

		i_right_style = getBorderStyle( right_style );
		i_right_width = getBorderWidth( right_width );

		//draw bottom line
		drawBorder( figure, g, BOTTOM, i_bottom_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, bottom_color );

		//draw top line
		drawBorder( figure, g, TOP, i_top_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, top_color );

		//draw left line
		drawBorder( figure, g, LEFT, i_left_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, left_color );

		//draw right line
		drawBorder( figure, g, RIGHT, i_right_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, right_color );

	}

	/**
	 * draw the border line with given style, width and color
	 * 
	 * @param figure
	 * @param g
	 * @param side
	 * @param style
	 * @param width
	 * @param color
	 */
	private void drawBorder( IFigure figure, Graphics g, int side, int style,
			int[] width, String color )
	{
		Rectangle r = figure.getBounds( ).getCopy( ).crop( DEFAULTINSETS );

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
			drawDefaultLine( figure, g, side, r );
		}

		g.restoreState( );
	}

}