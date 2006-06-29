/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Actuate Corporation
 * 
 */
public class LineCanvas extends Canvas implements PaintListener, FocusListener
{

	int iLineStyle = SWT.LINE_SOLID;

	int iLineWidth = 1;

	boolean isFocusIn = false;

	public LineCanvas( Composite parent, int iStyle, int iLineStyle,
			int iLineWidth )
	{
		super( parent, iStyle );
		this.iLineStyle = iLineStyle;
		this.iLineWidth = iLineWidth;
		this.addPaintListener( this );
		this.addFocusListener( this );
	}

	public int getLineStyle( )
	{
		return this.iLineStyle;
	}

	public void setLineStyle( int iLineStyle )
	{
		this.iLineStyle = iLineStyle;
	}

	public int getLineWidth( )
	{
		return this.iLineWidth;
	}

	public void setLineWidth( int iLineWidth )
	{
		this.iLineWidth = iLineWidth;
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
			cForeground = Display.getCurrent( )
					.getSystemColor( SWT.COLOR_BLACK );
			cBackground = Display.getCurrent( )
					.getSystemColor( SWT.COLOR_LIST_BACKGROUND );
		}
		else
		{
			cForeground = Display.getCurrent( )
					.getSystemColor( SWT.COLOR_DARK_GRAY );
			cBackground = Display.getCurrent( )
					.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );
		}

		GC gc = pe.gc;

		gc.setBackground( cBackground );
		gc.fillRectangle( 0, 0, this.getSize( ).x, this.getSize( ).y );

		// Render a gray background to indicate focus
		if ( isFocusIn )
		{
			gc.setBackground( Display.getCurrent( )
					.getSystemColor( SWT.COLOR_LIST_SELECTION ) );
			gc.fillRectangle( 1, 1, getSize( ).x - 3, this.getSize( ).y - 3 );
		}

		gc.setForeground( cForeground );
		gc.setLineStyle( iLineStyle );
		gc.setLineWidth( iLineWidth );
		gc.drawLine( 10,
				this.getSize( ).y / 2,
				this.getSize( ).x - 10,
				this.getSize( ).y / 2 );

		// Render a boundary line to indicate focus
		if ( isFocusIn )
		{
			gc.setLineStyle( SWT.LINE_DOT );
			gc.setLineWidth( 1 );
			gc.setForeground( Display.getCurrent( )
					.getSystemColor( SWT.COLOR_BLACK ) );
			gc.drawRectangle( 1, 1, getSize( ).x - 3, this.getSize( ).y - 3 );
		}
	}

	public void setEnabled( boolean bState )
	{
		super.setEnabled( bState );
		redraw( );
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