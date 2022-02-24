/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * This class draws section border
 */
public class SectionBorder extends BaseBorder {

	private static final Insets DEFAULTINSETS = new Insets(2, 1, 2, 2);

	private Insets insets = new Insets(getDefaultPaddingInsets());

	private Dimension indicatorDimension = new Dimension();
	protected String indicatorLabel = "";//$NON-NLS-1$
	protected Image image;
	protected int gap = 0;
	protected Insets gapInsets = new Insets(2, 2, 2, 2);
	private Rectangle indicatorArea;

	/*
	 * gets the insets (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		return getTrueBorderInsets().add(insets);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#getBorderInsets()
	 */
	public Insets getBorderInsets() {
		return getTrueBorderInsets();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#getTrueBorderInsets()
	 */
	public Insets getTrueBorderInsets() {
		int t = 1, b = 1, l = 1, r = 1;

		int style = 0;

		style = getBorderStyle(bottomStyle);
		if (style != 0) {
			b = getBorderWidth(bottomWidth);
		}

		style = getBorderStyle(topStyle);
		if (style != 0) {
			t = getBorderWidth(topWidth);
		}

		style = getBorderStyle(leftStyle);
		if (style != 0) {
			l = getBorderWidth(leftWidth);
		}

		style = getBorderStyle(rightStyle);
		if (style != 0) {
			r = getBorderWidth(rightWidth);
		}

		return new Insets(t, l, b, r);
	}

	Insets getDefaultPaddingInsets() {
		return DEFAULTINSETS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#setPaddingInsets(org.eclipse.draw2d.geometry.Insets)
	 */
	public void setPaddingInsets(Insets in) {
		if (in == null || (in.left == 0 && in.right == 0 && in.top == 0 && in.bottom == 0)) {
			insets = new Insets(getDefaultPaddingInsets());
			return;
		}
		insets.top = in.top > getDefaultPaddingInsets().top ? in.top : getDefaultPaddingInsets().top;

		insets.bottom = (in.bottom > indicatorDimension.height && in.bottom > getDefaultPaddingInsets().bottom)
				? in.bottom
				: getDefaultPaddingInsets().bottom;

		insets.left = in.left > getDefaultPaddingInsets().left ? in.left : getDefaultPaddingInsets().left;
		insets.right = in.right > getDefaultPaddingInsets().right ? in.right : getDefaultPaddingInsets().right;
	}

	/*
	 * paint the border ----------------------------- | | | | | | |
	 * ------------------------- |___| (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure,
	 * org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics g, Insets in) {
		i_bottom_style = getBorderStyle(bottomStyle);
		i_bottom_width = getBorderWidth(bottomWidth);

		i_top_style = getBorderStyle(topStyle);
		i_top_width = getBorderWidth(topWidth);

		i_left_style = getBorderStyle(leftStyle);
		i_left_width = getBorderWidth(leftWidth);

		i_right_style = getBorderStyle(rightStyle);
		i_right_width = getBorderWidth(rightWidth);

		g.restoreState();

		// draw top line
		drawBorder(figure, g, in, BorderUtil.TOP, i_top_style,
				new int[] { i_top_width, i_bottom_width, i_left_width, i_right_width }, topColor);

		// draw bottom line
		drawBorder(figure, g, in, BorderUtil.BOTTOM, i_bottom_style,
				new int[] { i_top_width, i_bottom_width, i_left_width, i_right_width }, bottomColor);

		// draw left line
		drawBorder(figure, g, in, BorderUtil.LEFT, i_left_style,
				new int[] { i_top_width, i_bottom_width, i_left_width, i_right_width }, leftColor);

		// draw right line
		drawBorder(figure, g, in, BorderUtil.RIGHT, i_right_style,
				new int[] { i_top_width, i_bottom_width, i_left_width, i_right_width }, rightColor);

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
	private void drawBorder(IFigure figure, Graphics g, Insets in, int side, int style, int[] width, int color) {
		Rectangle r = figure.getBounds().getCropped(in);

		// Outline the border
		// indicatorDimension = calculateIndicatorDimension( g, width[side] );

		// if the border style is not set to "none", draw line with given style,
		// width and color
		if (style != 0) {
			// set foreground color
			g.setForegroundColor(ColorManager.getColor(color));
			BorderUtil.drawBorderLine(g, side, style, width, r);
		}

		// if the border style is set to "none", draw a black solid line as
		// default
		else {
			g.setForegroundColor(ReportColorConstants.ShadowLineColor);
			// draw default line
			BorderUtil.drawDefaultLine(g, side, r);
		}

		g.restoreState();
	}

	// /**
	// * draw the left corner
	// *
	// * @param g
	// * @param rec
	// * @param indicatorDimension
	// */
	// private void drawIndicator( Graphics g, Rectangle rec,
	// Dimension indicatorDimension, int style, int width, int side,
	// boolean db )
	// {
	// Dimension cale = calculateIndicatorDimension( g, width );
	// int indicatorWidth = cale.width;
	// int indicatorHeight = cale.height;
	// indicatorArea = new Rectangle( rec.x,
	// rec.bottom( ) - indicatorHeight,
	// indicatorWidth,
	// indicatorHeight );
	//
	// g.setLineStyle( style );
	//
	// if ( side == BOTTOM )
	// {
	// if ( db == false )
	// {
	// for ( int i = 0; i < width; i++ )
	// {
	// g.drawLine( indicatorArea.x,
	// indicatorArea.bottom( ) - 1 - i,
	// indicatorArea.x + indicatorDimension.width,
	// indicatorArea.bottom( ) - 1 - i );
	// g.drawLine( indicatorArea.x + indicatorDimension.width + i,
	// indicatorArea.y,
	// indicatorArea.x + indicatorDimension.width + i,
	// indicatorArea.bottom( ) - 1 );
	// }
	// }
	// //if the border style is "double", draw the second line with 1
	// // pixel inside the Indicator
	// else
	// {
	// for ( int i = 0; i < width; i++ )
	// {
	// g.drawLine( indicatorArea.x + leftGap,
	// indicatorArea.bottom( ) - 1 - i - width - 1,
	// indicatorArea.x
	// + indicatorDimension.width
	// - 1
	// - width,
	// indicatorArea.bottom( ) - 1 - i - width - 1 );
	// g.drawLine( indicatorArea.x
	// + indicatorDimension.width
	// + i
	// - width
	// - 1, indicatorArea.y - 1 - width, indicatorArea.x
	// + indicatorDimension.width
	// + i
	// - width
	// - 1, indicatorArea.bottom( ) - 1 - 1 - width );
	// }
	// }
	// //draw text "table"
	// int x = indicatorArea.x + gapInsets.left;
	// if ( image != null )
	// {
	// g.drawImage( image, x + 4, indicatorArea.y + gapInsets.top - 3 );
	// x += image.getBounds( ).width + gap;
	// }
	//
	// g.drawString( indicatorLabel, x + 2 * width + 2, indicatorArea.y
	// + gapInsets.top
	// - width );
	//
	// }
	// else if ( side == LEFT )
	// {
	// if ( db == false )
	// {
	// for ( int j = 0; j < width; j++ )
	// {
	// g.drawLine( indicatorArea.x + j,
	// indicatorArea.y,
	// indicatorArea.x + j,
	// indicatorArea.bottom( ) - 1 );
	// }
	// }
	// else
	// {
	// for ( int j = 0; j < width; j++ )
	// {
	// g.drawLine( indicatorArea.x + j + width + 1,
	// indicatorArea.y,
	// indicatorArea.x + j + width + 1,
	// indicatorArea.bottom( ) - 1 - bottomGap );
	// }
	// }
	// }
	//
	// }

	/**
	 * Sets the left corner label
	 * 
	 * @param indicatorLabel
	 */
	public void setIndicatorLabel(String indicatorLabel) {
		if (indicatorLabel != null) {
			this.indicatorLabel = indicatorLabel;
		}
	}

	/**
	 * Sets the left corner
	 * 
	 * @param image
	 */
	public void setIndicatorIcon(Image image) {
		this.image = image;
	}

	// /**
	// * calculates the left corner size
	// *
	// * @return
	// */
	// private Dimension calculateIndicatorDimension( Graphics g, int width )
	// {
	// return new Dimension( 0, 0 );
	//
	// // gap = 0;
	// // Dimension iconDimension = new Dimension( );
	// // if ( image != null )
	// // {
	// // iconDimension = new Dimension( image );
	// // gap = 3;
	// // }
	// // Dimension d = FigureUtilities.getTextExtents( indicatorLabel,
	// // g.getFont( ) );
	// // int incheight = 0;
	// // if ( iconDimension.height > d.height )
	// // {
	// // incheight = iconDimension.height - d.height;
	// // }
	// // d.expand( iconDimension.width
	// // + gap
	// // + gapInsets.left
	// // + gapInsets.right
	// // + 4
	// // * width
	// // + 2, incheight + gapInsets.top + gapInsets.bottom );
	// //
	// // return d;
	// }

	/**
	 * gets the left corner size
	 * 
	 * @return
	 */
	public Rectangle getIndicatorArea() {

		return indicatorArea;
	}

}
