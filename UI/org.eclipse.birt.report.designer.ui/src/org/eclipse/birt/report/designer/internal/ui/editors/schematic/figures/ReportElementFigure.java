/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * This is base figure for all report element figures.
 */
public class ReportElementFigure extends Figure implements IReportElementFigure
{

	private static final Rectangle PRIVATE_RECT = new Rectangle( );

	private Image img;

	private int alignment;

	private Point position = new Point( -1, -1 );

	private int repeat;

	private Dimension size = new Dimension( );

	private Rectangle clip;

	private static final Rectangle OLD_CLIP = new Rectangle( );

	/**
	 * Constructor <br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 */
	public ReportElementFigure( )
	{
		this( null, PositionConstants.CENTER );
	}

	/**
	 * Constructor <br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 * 
	 * @param image
	 *            The Image to be displayed
	 */
	public ReportElementFigure( Image image )
	{
		this( image, PositionConstants.CENTER );
	}

	/**
	 * Constructor
	 * 
	 * @param image
	 *            The Image to be displayed
	 * @param alignment
	 *            A PositionConstant indicating the alignment
	 * 
	 * @see ImageFigure#setImage(Image)
	 * @see ImageFigure#setAlignment(int)
	 */
	public ReportElementFigure( Image image, int alignment )
	{
		setImage( image );
		setAlignment( alignment );
	}

	/**
	 * @return The Image that this Figure displays
	 */
	public Image getImage( )
	{
		return img;
	}

	public void setPageClip( Rectangle clip )
	{
		this.clip = clip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintBorder( Graphics graphics )
	{
		if ( clip != null )
		{
			graphics.getClip( OLD_CLIP );
			graphics.setClip( getBounds( ).getCopy( ).intersect( clip ) );
		}

		super.paintBorder( graphics );

		if ( clip != null )
		{
			graphics.setClip( OLD_CLIP );
		}
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure( Graphics graphics )
	{
		super.paintFigure( graphics );

		Image image = getImage( );
		if ( image == null )
		{
			return;
		}

		int x, y;
		Rectangle area = getClientArea( );

		graphics.getClip( PRIVATE_RECT );
		//graphics.setClip( area );

		if ( !( position.x == -1 && position.y == -1 ) )
		{
			x = area.x + position.x;
			y = area.y + position.y;
		}
		else
		{
			switch ( alignment & PositionConstants.NORTH_SOUTH )
			{
				case PositionConstants.NORTH :
					y = area.y;
					break;
				case PositionConstants.SOUTH :
					y = area.y + area.height - size.height;
					break;
				default :
					y = ( area.height - size.height ) / 2 + area.y;
					break;
			}
			switch ( alignment & PositionConstants.EAST_WEST )
			{
				case PositionConstants.EAST :
					x = area.x + area.width - size.width;
					break;
				case PositionConstants.WEST :
					x = area.x;
					break;
				default :
					x = ( area.width - size.width ) / 2 + area.x;
					break;
			}
		}

		ArrayList xyList = createImageList( x, y );

		Iterator iter = xyList.iterator( );
		while ( iter.hasNext( ) )
		{
			Point point = (Point) iter.next( );
			graphics.drawImage( image, point );
		}
		xyList.clear( );

		graphics.setClip( PRIVATE_RECT );
	}

	/**
	 * Create the list of all the images to be displayed.
	 * 
	 * @param x
	 *            the x-cordinator of the base image.
	 * @param y
	 *            the y-cordinator of the base image.
	 * @return the list of all the images to be displayed.
	 */
	private ArrayList createImageList( int x, int y )
	{
		Rectangle area = getClientArea( );

		ArrayList yList = new ArrayList( );

		if ( ( repeat & ImageConstants.REPEAT_Y ) == 0 )
		{
			yList.add( new Point( x, y ) );
		}
		else
		{
			int i = 0;
			while ( y + size.height * i + size.height > area.y )
			{
				yList.add( new Point( x, y + size.height * i ) );
				i--;
			}

			i = 1;
			while ( y + size.height * i < area.y + area.height )
			{
				yList.add( new Point( x, y + size.height * i ) );
				i++;
			}
		}

		ArrayList xyList = new ArrayList( );

		Iterator iter = yList.iterator( );
		while ( iter.hasNext( ) )
		{
			Point point = (Point) iter.next( );

			if ( ( repeat & ImageConstants.REPEAT_X ) == 0 )
			{
				xyList.add( point );
			}
			else
			{
				int i = 0;
				while ( point.x + size.width * i + size.width > area.x )
				{
					xyList.add( new Point( point.x + size.width * i, point.y ) );
					i--;
				}

				i = 1;
				while ( point.x + size.width * i < area.x + area.width )
				{
					xyList.add( new Point( point.x + size.width * i, point.y ) );
					i++;
				}
			}
		}
		yList.clear( );

		return xyList;
	}

	/**
	 * Sets the alignment of the Image within this Figure. The alignment comes
	 * into play when the ImageFigure is larger than the Image. The alignment
	 * could be any valid combination of the following:
	 * 
	 * <UL>
	 * <LI>PositionConstants.NORTH</LI>
	 * <LI>PositionConstants.SOUTH</LI>
	 * <LI>PositionConstants.EAST</LI>
	 * <LI>PositionConstants.WEST</LI>
	 * <LI>PositionConstants.CENTER or PositionConstants.NONE</LI>
	 * </UL>
	 * 
	 * @param flag
	 *            A constant indicating the alignment
	 */
	public void setAlignment( int flag )
	{
		alignment = flag;
	}

	/**
	 * Sets the position of the Image within this Figure.
	 * 
	 * @param point
	 *            The position of the image to be displayed.
	 */
	public void setPosition( Point point )
	{
		this.position = point;
	}

	/**
	 * Sets the repeat of the Image within this Figure. The repeat could be any
	 * valid combination of the following:
	 * 
	 * <UL>
	 * <LI>no_repeat:0</LI>
	 * <LI>repeat_x:1</LI>
	 * <LI>repeat_y:2</LI>
	 * <LI>repeat:3</LI>
	 * </UL>
	 * 
	 * @param flag
	 *            A constant indicating the repeat.
	 */
	public void setRepeat( int flag )
	{
		this.repeat = flag;
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 * <p>
	 * IMPORTANT: Note that it is the client's responsibility to dispose the
	 * given image.
	 * 
	 * @param image
	 *            The Image to be displayed. It can be <code>null</code>.
	 */
	public void setImage( Image image )
	{
		if ( img == image )
			return;
		img = image;
		if ( img != null )
			size = new Rectangle( image.getBounds( ) ).getSize( );
		else
			size = new Dimension( );
		revalidate( );
		repaint( );
	}
}