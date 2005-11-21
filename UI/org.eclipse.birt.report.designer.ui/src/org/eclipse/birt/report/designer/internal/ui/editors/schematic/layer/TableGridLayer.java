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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ImageConstants;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;

/**
 * Paint the grid
 *  
 */
public class TableGridLayer extends GridLayer
{
	private TableEditPart source;

	/**
	 * Constructor
	 * @param rows
	 * @param cells
	 */
	public TableGridLayer( TableEditPart source )
	{
		super( );
		this.source = source;
	}

	/**
	 * @return rows
	 */
	public List getRows( )
	{
		return source.getRows( );
	}

	/**
	 * @return columns
	 */
	public List getColumns( )
	{
		return source.getColumns( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.GridLayer#paintGrid(org.eclipse.draw2d.Graphics)
	 */
	protected void paintGrid( Graphics g )
	{

		Collections.sort( getRows( ), new NumberComparator( ) );
		Collections.sort( getColumns( ), new NumberComparator( ) );

		if ( !getColumns( ).isEmpty( ) )
		{
			drawColumns(g);
		}

		if ( !getRows( ).isEmpty( ) )
		{
			drawRows(g);
		}

	}

	protected void drawRows( Graphics g )
	{
		Rectangle clip = g.getClip( Rectangle.SINGLETON );
		List rows = getRows( );
		int size = rows.size( );
		int height = 0;
		for ( int i = 0; i < size; i++ )
		{
			//if ( height < clip.y + clip.height )
			{
				//g.fillRectangle( clip.x, height, clip.x + clip.width, height );
				drawBackgroud( rows.get( i ), g, new Rectangle(clip.x, height, clip.x + clip.width, getRowHeight( rows.get( i ))));
				drawBackgroudImage((DesignElementHandle) rows.get(i),g,new Rectangle(clip.x, height, clip.x + clip.width, getRowHeight( rows.get( i ))));
				height = height + getRowHeight( rows.get( i ) );
			}
		}

	}

	private void drawBackgroudImage( DesignElementHandle handle,Graphics g, Rectangle rectangle )
	{
		String backGroundImage = getBackgroundImage( handle );
		Object backGroundPosition = getBackgroundPosition( handle );
		int backGroundRepeat = getBackgroundRepeat( handle );
		
		if ( backGroundImage != null )
		{
			Image image = null;
			try
			{
				image = ImageManager.getInstance( ).getImage( backGroundImage );
			}
			catch ( SWTException e )
			{
				image = null;
			}

			if ( image != null )
			{
				Rectangle area = rectangle;
				int repeat = backGroundRepeat;
				int alignment = -1;
				Point position = new Point( -1, -1 );

				if ( backGroundPosition instanceof int[] )
				{
					// left, center, right, top, bottom
					alignment = ( ( (int[]) backGroundPosition )[0]
							| ( (int[]) backGroundPosition )[1] );
				}
				else if ( backGroundPosition instanceof Point )
				{
					// {1cm, 1cm}
					position = ( (Point) backGroundPosition );
				}
				else if ( backGroundPosition instanceof DimensionValue[] )
				{
					// {0%, 0%}
					int percentX = (int) ( (DimensionValue[]) backGroundPosition )[0].getMeasure( );
					int percentY = (int) ( (DimensionValue[]) backGroundPosition )[1].getMeasure( );
					
					org.eclipse.swt.graphics.Rectangle imageArea = image.getBounds( );
					int xPosition = ( area.width - imageArea.width ) * percentX / 100;
					int yPosition = ( area.height - imageArea.height ) * percentY / 100;
	
					position =  new Point( xPosition, yPosition ) ;
				}
				
				int x,y;
				Dimension size = new Rectangle( image.getBounds( ) ).getSize( );

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

				ArrayList xyList = createImageList( x, y ,size,repeat,rectangle);

				Iterator iter = xyList.iterator( );
				g.setClip( rectangle );
				while ( iter.hasNext( ) )
				{
					Point point = (Point) iter.next( );
					g.drawImage( image, point );
				}
				xyList.clear( );

			}
		}
	}
	
	
	/**
	 * Create the list of all the images to be displayed.
	 * 
	 * @param x
	 *            the x-cordinator of the base image.
	 * @param y
	 *            the y-cordinator of the base image.
	 * @param size 
	 * @param repeat 
	 * @param rectangle 
	 * @return the list of all the images to be displayed.
	 */
	private ArrayList createImageList( int x, int y, Dimension size, int repeat, Rectangle rectangle )
	{
		Rectangle area = rectangle;

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
	
	
	private RowHandleAdapter getRowAdapter(DesignElementHandle handle )
	{
		return (RowHandleAdapter) HandleAdapterFactory.getInstance( ).getRowHandleAdapter( handle );
	}
	
	private String getBackgroundImage(DesignElementHandle handle )
	{
		if(getRowAdapter(handle)!=null)
		{
			return getRowAdapter(handle).getBackgroundImage(handle);
		}
		return "";
	}
	
	private Object getBackgroundPosition(DesignElementHandle handle)
	{
		if(getRowAdapter(handle)!=null)
		{
			return getRowAdapter(handle).getBackgroundPosition(handle);
		}
		return null;
	}
	
	private int getBackgroundRepeat(DesignElementHandle handle)
	{
		if(getRowAdapter(handle)!=null)
		{
			return getRowAdapter(handle).getBackgroundRepeat(handle);
		}
		return 0;
	}
	

	protected void drawColumns( Graphics g )
	{
		g.setBackgroundColor(ReportColorConstants.greyFillColor);
		Rectangle clip = g.getClip( Rectangle.SINGLETON );
		List columns = getColumns( );
		int size = columns.size( );
		int width = 0;
		for ( int i = 0; i < size; i++ )
		{
			 
			//if ( width < clip.x + clip.width )
			{
				//g.fillRectangle( width, clip.y, width, clip.y + clip.height );
				drawBackgroud( columns.get( i ), g, new Rectangle(width, clip.y, getColumnWidth( columns.get( i )), clip.y + clip.height));
			}
			width = width + getColumnWidth( columns.get( i ) );
		}

	}

	private int getRowHeight( Object row )
	{
		return TableUtil.caleVisualHeight( source, row );
	}

	private int getColumnWidth( Object column )
	{
		return TableUtil.caleVisualWidth( source, column );
	}

	
	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 *  
	 */
	public void drawBackgroud( Object model,Graphics g, Rectangle rect )
	{
		assert model instanceof DesignElementHandle;
		DesignElementHandle handle = (DesignElementHandle)model;
		Object obj = handle.getProperty( StyleHandle.BACKGROUND_COLOR_PROP );

		if ( obj != null )
		{
			int color = 0xFFFFFF;
			if ( obj instanceof String )
			{
				color = ColorUtil.parseColor( (String) obj );
			}
			else
			{
				color = ( (Integer) obj ).intValue( );
			}
			g.setBackgroundColor( ColorManager.getColor( color ) );
			g.fillRectangle(rect);
		}
	}
	/**
	 * Sorter to be used to sort the rows with row number
	 * 
	 */
	public static class NumberComparator implements Comparator
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare( Object o1, Object o2 )
		{
			//TODO: sort the row with row number
			return 0;
		}
	}
}