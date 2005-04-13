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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A handy class for font style preview.
 */

public class PreviewLabel extends Canvas
{

	protected static final int WEIGHT_NORMAL = 400;

	protected static final int WEIGHT_BOLD = 700;

	private String text;

	private boolean isUnderline;

	private boolean isLinethrough;

	private boolean isOverline;

	private String fontFamily = "Times New Roman"; //$NON-NLS-1$

	private int fontSize = 9;

	private int fontWeight = WEIGHT_NORMAL;

	private boolean isBold;

	private boolean isItalic;

	private boolean updateOnTheFly;

	private boolean fontCreated;

	/**
	 * Default constructor.
	 * 
	 * @param parent
	 *            widget parent.
	 * @param style
	 *            create style.
	 */
	public PreviewLabel( Composite parent, int style )
	{
		super( parent, style );

		initFields( );

		addPaintListener( new PaintListener( ) {

			public void paintControl( PaintEvent e )
			{
				PreviewLabel.this.paintControl( e );
			}
		} );

		addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				if ( fontCreated )
				{
					getFont( ).dispose( );
				}
			}
		} );
	}

	private void initFields( )
	{
		try
		{
			FontData fd = getFont( ).getFontData( )[0];

			fontFamily = fd.getName( );
			fontSize = fd.getHeight( );
			isBold = ( fd.getStyle( ) & SWT.BOLD ) != 0;
			isItalic = ( fd.getStyle( ) & SWT.ITALIC ) != 0;
		}
		catch ( Exception e )
		{
			/**
			 * Does nothing.
			 */
		}
	}

	/**
	 * Restores all state to default;
	 */
	public void restoreDefaultState( )
	{
		isUnderline = false;
		isLinethrough = false;
		isOverline = false;
		fontFamily = "Times New Roman"; //$NON-NLS-1$
		fontSize = 9;
		fontWeight = WEIGHT_NORMAL;
		isBold = false;
		isItalic = false;
		
		if ( updateOnTheFly )
		{
			updateView( );
		}
	}

	/**
	 * Sets if update the view whenever the font style has been changed.
	 * 
	 * @param onthefly
	 */
	public void setUpdateOnthefly( boolean onthefly )
	{
		this.updateOnTheFly = onthefly;
	}

	/**
	 * Sets the label text.
	 * 
	 * @param txt
	 *            text.
	 */
	public void setText( String txt )
	{
		this.text = txt;
	}

	/**
	 * Sets if the label has the underline style.
	 * 
	 * @param underline
	 */
	public void setUnderline( boolean underline )
	{
		this.isUnderline = underline;
	}

	/**
	 * Sets if the label has the line-through style.
	 * 
	 * @param linethrough
	 */
	public void setLinethrough( boolean linethrough )
	{
		this.isLinethrough = linethrough;
	}

	/**
	 * Sets if the label has the over-line style.
	 * 
	 * @param overline
	 */
	public void setOverline( boolean overline )
	{
		this.isOverline = overline;
	}

	/**
	 * Sets the font family.
	 * 
	 * @param fontFamily
	 *            family name.
	 */
	public void setFontFamily( String fontFamily )
	{
		this.fontFamily = fontFamily;

		if ( updateOnTheFly )
		{
			updateView( );
		}
	}

	/**
	 * Sets the font size.
	 * 
	 * @param fontSize
	 *            size value.
	 */
	public void setFontSize( int fontSize )
	{
		this.fontSize = fontSize;

		if ( updateOnTheFly )
		{
			updateView( );
		}
	}

	/**
	 * Sets the font weight. NOTE: only win32 system support this style.
	 * 
	 * @param fontWeight
	 *            weight value.
	 */
	public void setFontWeight( int fontWeight )
	{
		this.fontWeight = fontWeight;

		if ( updateOnTheFly )
		{
			updateView( );
		}
	}

	/**
	 * Sets if has a bold style.
	 * 
	 * @param isBold
	 */
	public void setBold( boolean isBold )
	{
		this.isBold = isBold;

		this.fontWeight = isBold ? WEIGHT_BOLD : WEIGHT_NORMAL;

		if ( updateOnTheFly )
		{
			updateView( );
		}
	}

	/**
	 * Sets if has a italic style.
	 * 
	 * @param isItalic
	 */
	public void setItalic( boolean isItalic )
	{
		this.isItalic = isItalic;

		if ( updateOnTheFly )
		{
			updateView( );
		}
	}

	/**
	 * Updates the view using current font style.
	 */
	public void updateView( )
	{
		Font oldFont = getFont( );

		int style = 0;

		if ( isBold )
		{
			style |= SWT.BOLD;
		}

		if ( isItalic )
		{
			style |= SWT.ITALIC;
		}

		if ( fontWeight >= WEIGHT_BOLD )
		{
			style |= SWT.BOLD;
		}

		setFont( new Font( Display.getCurrent( ), fontFamily, fontSize, style ) );

		if ( fontCreated )
		{
			oldFont.dispose( );
		}

		fontCreated = true;

		this.redraw( );
	}

	protected void paintControl( PaintEvent e )
	{
		GC gc = e.gc;

		if ( text == null )
		{
			text = ""; //$NON-NLS-1$
		}

		Point pt = gc.stringExtent( text );

		gc.drawString( text,
				( getSize( ).x - pt.x ) / 2,
				( getSize( ).y - pt.y ) / 2,
				true );

		if ( isUnderline )
		{
			gc.drawLine( ( getSize( ).x - pt.x ) / 2,
					( getSize( ).y - pt.y ) / 2 + pt.y,
					( getSize( ).x - pt.x ) / 2 + pt.x,
					( getSize( ).y - pt.y ) / 2 + pt.y );
		}

		if ( isLinethrough )
		{
			gc.drawLine( ( getSize( ).x - pt.x ) / 2,
					( getSize( ).y - pt.y ) / 2 + pt.y / 2,
					( getSize( ).x - pt.x ) / 2 + pt.x,
					( getSize( ).y - pt.y ) / 2 + pt.y / 2 );
		}

		if ( isOverline )
		{
			gc.drawLine( ( getSize( ).x - pt.x ) / 2,
					( getSize( ).y - pt.y ) / 2,
					( getSize( ).x - pt.x ) / 2 + pt.x,
					( getSize( ).y - pt.y ) / 2 );
		}
	}

}