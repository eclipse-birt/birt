/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

/**
 * 
 */

public class MenuButton extends Composite
{

	private Button button;

	private String text;
	private Image image;

	public String getText( )
	{
		return text;
	}

	public Image getImage( )
	{
		return image;
	}

	private static final int IMAGE_HEIGHT = 16, IMAGE_WIDTH = 16;

	private static int DRAW_FLAGS = SWT.DRAW_MNEMONIC
			| SWT.DRAW_TAB
			| SWT.DRAW_TRANSPARENT
			| SWT.DRAW_DELIMITER;

	private static final int MARGIN_GAP = 4;

	private static final int TRIANGLE_WIDTH = 5;

	private static final int WIDTH_MORE = 2 * MARGIN_GAP + TRIANGLE_WIDTH + 1;

	public void setText( String text )
	{
		this.text = text;
		button.setText( "" ); //$NON-NLS-1$
		layoutControl( );
	}

	public void setToolTipText( String string )
	{
		button.setToolTipText( string );
	}

	private void layoutControl( )
	{
		getParent( ).layout( );
		button.redraw( );
	}

	private Point defaultSize = new Point( 0, 0 );

	public Point computeSize( int wHint, int hHint, boolean changed )
	{

		int width;
		int height;

		Button tmp = new Button( this, button.getStyle( ) );
		if ( text != null )
		{
			tmp.setText( text );
			height = tmp.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		}
		else
		{
			tmp.setText( "" ); //$NON-NLS-1$
			height = tmp.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		}
		if ( image != null )
			tmp.setImage( image );
		Point size = tmp.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		tmp.dispose( );

		if ( menu != null )
		{
			width = size.x + WIDTH_MORE;
		}
		else
			width = size.x;

		if ( isFixed && image != null )
		{
			int imageWidth = image.getImageData( ).width;
			if ( imageWidth > IMAGE_WIDTH )
				width -= ( imageWidth - IMAGE_WIDTH );

		}
		if ( !isFixed )
			height = size.y;
		defaultSize = new Point( width, height );
		if ( wHint != SWT.DEFAULT )
			width = wHint;
		if ( hHint != SWT.DEFAULT )
			height = hHint;

		return new Point( width, height );
	}

	public void setImage( Image image )
	{
		this.image = image;
		layoutControl( );
	}

	public void setBackground( Color color )
	{
		super.setBackground( color );
		button.setBackground( color );
		button.redraw( );
	}

	public void setForeground( Color color )
	{
		super.setBackground( color );
		button.setForeground( color );
		button.redraw( );
	}

	public void setFont( Font font )
	{
		super.setFont( font );
		button.setFont( font );
		button.redraw( );
	}

	public void setEnabled( boolean enable )
	{
		super.setEnabled( enable );
		button.setEnabled( enable );
		button.redraw( );
	}

	public MenuButton( Composite parent, int style )
	{
		this( parent, style, false );
	}

	private boolean isFixed = true;

	private boolean mouseSelection = false;

	private boolean mouseDown = false;

	public MenuButton( Composite parent, int style, boolean fixed )
	{
		super( parent, SWT.DOUBLE_BUFFERED );
		isFixed = fixed;
		GridLayout layout = new GridLayout( );
		layout.marginHeight = layout.marginWidth = 0;
		this.setLayout( layout );

		button = new Button( this, style | SWT.DOUBLE_BUFFERED ) {

			protected void checkSubclass( )
			{

			}

			public String getText( )
			{
				String text = MenuButton.this.getText( );
				return text == null ? "" : text;
			}
		};
		GridData gd = new GridData( GridData.FILL_BOTH );
		button.setLayoutData( gd );
		button.addPaintListener( new PaintListener( ) {

			public void paintControl( final PaintEvent e )
			{
				MenuButton.this.paintControl( e );
			}
		} );

		button.addListener( SWT.MouseUp, new Listener( ) {

			public void handleEvent( Event e )
			{
				if ( !button.isEnabled( ) || e.button != 1 || !mouseDown )
					return;
				mouseDown = false;
				mouseSelection = true;
				Rectangle size = button.getBounds( );
				if ( !size.contains( e.x, e.y ) )
					return;
				int left = WIDTH_MORE + MARGIN_GAP - 1;
				if ( menu == null )
					left = 0;

				if ( e.x < size.width - left )
				{
					if ( listeners == null )
						return;

					e.widget = MenuButton.this;

					for ( int i = 0; i < listeners.size( ); i++ )
					{
						( (SelectionListener) listeners.get( i ) ).widgetSelected( new SelectionEvent( e ) );
					}
				}
				else
				{
					if ( menu != null )
					{
						menu.setLocation( button.toDisplay( new Point( 0,
								size.height - 1 ) ) );
						menu.setVisible( true );
					}
				}
			}

		} );

		button.addListener( SWT.MouseDown, new Listener( ) {

			public void handleEvent( Event e )
			{
				if ( !button.isEnabled( ) || e.button != 1 )
					return;
				mouseSelection = true;
				Rectangle size = button.getBounds( );
				if ( !size.contains( e.x, e.y ) )
					return;
				mouseDown = true;
			}

		} );
		button.addListener( SWT.KeyUp, new Listener( ) {

			public void handleEvent( Event e )
			{
				if ( e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP )
				{
					if ( menu != null )
					{
						Rectangle size = button.getBounds( );
						menu.setLocation( button.toDisplay( new Point( 0,
								size.height - 1 ) ) );
						menu.setVisible( true );
					}
				}
			}

		} );
		button.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				if ( mouseSelection == true )
				{
					mouseSelection = false;
					return;
				}

				if ( listeners == null )
					return;

				e.widget = MenuButton.this;

				for ( int i = 0; i < listeners.size( ); i++ )
				{
					( (SelectionListener) listeners.get( i ) ).widgetSelected( new SelectionEvent( e ) );
				}
			}

		} );
	}

	private Menu menu;

	public void setDropDownMenu( Menu menu )
	{
		this.menu = menu;
		layoutControl( );
	}

	private List listeners;

	public void addSelectionListener( SelectionListener listener )
	{
		if ( listeners == null )
			listeners = new ArrayList( );
		listeners.add( listener );
	}

	public void removeSelectionListener( SelectionListener listener )
	{
		if ( listeners != null )
		{
			listeners.remove( listener );
			if ( listeners.size( ) == 0 )
				listeners = null;
		}
	}

	protected void paintControl( PaintEvent e )
	{
		e.gc.setFont( getFont( ) );
		Color fg = isEnabled( ) ? getForeground( )
				: new Color( e.gc.getDevice( ),
						blend( getBackground( ).getRGB( ),
								getForeground( ).getRGB( ),
								70 ) );
		try
		{
			e.gc.setForeground( fg );
			Color bgColor = e.gc.getBackground( );
			e.gc.setBackground( e.gc.getForeground( ) );
			Rectangle size = button.getBounds( );

			if ( menu != null )
			{
				Rectangle rect = new Rectangle( size.width - 12,
						0,
						TRIANGLE_WIDTH,
						size.height );
				drawArrow( e.gc, rect, SWT.DOWN );
			}

			e.gc.setBackground( bgColor );

			int height = e.gc.textExtent( "", DRAW_FLAGS ).y; //$NON-NLS-1$

			if ( !isFixed && image != null )
			{
				int imageHeight = image.getImageData( ).height;
				if ( height < imageHeight )
					height = imageHeight;
			}

			if ( defaultSize.y > size.height )
			{
				height = height - ( defaultSize.y - size.height );
				height = e.gc.textExtent( "", DRAW_FLAGS ).y > height ? e.gc.textExtent( "", //$NON-NLS-1$ //$NON-NLS-2$
						DRAW_FLAGS ).y
						: height;
			}

			int left = WIDTH_MORE + MARGIN_GAP - 1;

			if ( menu == null )
				left = MARGIN_GAP - 1;

			if ( menu != null )
			{
				Color fgColor = e.gc.getForeground( );
				e.gc.setForeground( getDisplay( ).getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
				e.gc.drawLine( size.width - left,
						( size.height - height ) / 2,
						size.width - left,
						( size.height - height ) / 2 + height );
				e.gc.setForeground( fgColor );
			}

			if ( text != null && text.trim( ).length( ) > 0 )
			{
				int width = e.gc.textExtent( text, DRAW_FLAGS ).x;
				int fontHeight = e.gc.textExtent( text, DRAW_FLAGS ).y;
				left += ( MARGIN_GAP + width );
				e.gc.drawText( text,
						( size.width - left ) / 2 + MARGIN_GAP,
						( size.height - fontHeight ) / 2,
						DRAW_FLAGS | SWT.DRAW_TRANSPARENT );
			}

			if ( image != null )
			{
				int imageWidth = image.getImageData( ).width;
				int imageHeight = image.getImageData( ).height;

				Image imageTemp = null;

				if ( !isEnabled( ) )
					imageTemp = new Image( e.gc.getDevice( ),
							image,
							SWT.IMAGE_DISABLE );

				if ( isFixed )
				{
					imageWidth = imageWidth > IMAGE_WIDTH ? IMAGE_WIDTH
							: imageWidth;
					imageHeight = imageHeight > IMAGE_HEIGHT ? IMAGE_HEIGHT
							: imageHeight;
				}

				left += ( MARGIN_GAP + imageWidth );
				if ( !isEnabled( ) )
				{
					e.gc.drawImage( imageTemp,
							0,
							0,
							imageTemp.getImageData( ).width,
							imageTemp.getImageData( ).height,
							( size.width - left ) / 2 + MARGIN_GAP,
							Math.round( ( (float) ( size.height - imageHeight ) / 2 ) ),
							imageWidth,
							imageHeight );

					imageTemp.dispose( );
				}
				else
				{
					e.gc.drawImage( image,
							0,
							0,
							image.getImageData( ).width,
							image.getImageData( ).height,
							( size.width - left ) / 2 + MARGIN_GAP,
							Math.round( ( (float) ( size.height - imageHeight ) / 2 ) ),
							imageWidth,
							imageHeight );
				}
			}

		}
		finally
		{
			if ( !isEnabled( ) && fg != null )
				fg.dispose( );
		}
	}

	public static RGB blend( RGB c1, RGB c2, int ratio )
	{
		int r = blend( c1.red, c2.red, ratio );
		int g = blend( c1.green, c2.green, ratio );
		int b = blend( c1.blue, c2.blue, ratio );
		return new RGB( r, g, b );
	}

	private static int blend( int v1, int v2, int ratio )
	{
		int b = ( ratio * v1 + ( 100 - ratio ) * v2 ) / 100;
		return Math.min( 255, b );
	}

	public static void drawArrow( GC gc, Rectangle rect, int style )
	{
		Point point = new Point( rect.x + ( rect.width / 2 ), rect.y
				+ ( rect.height / 2 ) );
		int[] points = null;
		switch ( style )
		{
			case SWT.LEFT :
				points = new int[]{
						point.x + 2,
						point.y - 4,
						point.x + 2,
						point.y + 4,
						point.x - 2,
						point.y
				};
				gc.fillPolygon( points );
				break;

			/*
			 * Low efficiency because of Win98 bug.
			 */
			case SWT.UP :
				gc.fillRectangle( new Rectangle( point.x, point.y - 1, 1, 1 ) );
				gc.fillRectangle( new Rectangle( point.x - 1, point.y, 3, 1 ) );
				gc.fillRectangle( new Rectangle( point.x - 2, point.y + 1, 5, 1 ) );
				break;

			case SWT.RIGHT :
				points = new int[]{
						point.x - 2,
						point.y - 4,
						point.x - 2,
						point.y + 4,
						point.x + 2,
						point.y
				};
				gc.fillPolygon( points );
				break;

			/*
			 * Low efficiency because of Win98 bug.
			 */
			default :
				gc.fillRectangle( new Rectangle( point.x - 2, point.y - 1, 5, 1 ) );
				gc.fillRectangle( new Rectangle( point.x - 1, point.y, 3, 1 ) );
				gc.fillRectangle( new Rectangle( point.x, point.y + 1, 1, 1 ) );
				break;
		}

	}
}
