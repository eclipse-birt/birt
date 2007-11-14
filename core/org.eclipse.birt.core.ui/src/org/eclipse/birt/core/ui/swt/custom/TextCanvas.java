/***********************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.core.ui.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Drawing text in graphics
 */
class TextCanvas extends Canvas implements PaintListener, FocusListener
{

	private String text;

	private Font textFont;

	private boolean isFocusIn = false;

	public TextCanvas( Composite parent, int iStyle, String text )
	{
		super( parent, iStyle );
		this.text = text;
		this.addPaintListener( this );
		this.addFocusListener( this );
	}

	public String getText( )
	{
		return this.text;
	}

	public void setText( String text )
	{
		this.text = text;
	}

	public void setTextFont( Font font )
	{
		this.textFont = font;
	}

	public Font getTextFont( )
	{
		return textFont;
	}

	public void paintControl( PaintEvent pe )
	{
		if ( isEnabled( ) && isFocusControl( ) )
		{
			isFocusIn = true;
		}

		Color cForeground = null;
		Color cBackground = null;
		if ( this.isEnabled( ) )
		{
			cForeground = getDisplay( ).getSystemColor( SWT.COLOR_LIST_FOREGROUND );
			cBackground = getDisplay( ).getSystemColor( SWT.COLOR_LIST_BACKGROUND );
		}
		else
		{
			cForeground = getDisplay( ).getSystemColor( SWT.COLOR_DARK_GRAY );
			cBackground = getDisplay( ).getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );
		}

		GC gc = pe.gc;
		if ( isFocusIn )
		{
			gc.setBackground( getDisplay( ).getSystemColor( SWT.COLOR_LIST_SELECTION ) );
			gc.setForeground( getDisplay( ).getSystemColor( SWT.COLOR_LIST_SELECTION_TEXT ) );
		}
		else
		{
			gc.setBackground( cBackground );
			gc.setForeground( cForeground );
		}

		gc.fillRectangle( 0, 0, this.getSize( ).x, this.getSize( ).y );

		if ( textFont != null )
		{
			gc.setFont( textFont );
		}

		if ( text != null )
		{
			gc.drawText( text, 2, 2 );
		}
	}

	public void focusGained( FocusEvent e )
	{
		isFocusIn = true;

	}

	public void focusLost( FocusEvent e )
	{
		isFocusIn = false;
	}
}
