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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.CellPaddingBorder;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

/**
 * This is Cell figure for cell edit part.
 * 
 *  
 */
public class CellFigure extends ReportElementFigure
{

	private String blankString;

	/**
	 *  Constructor
	 */
	public CellFigure( )
	{
		setBorder( new CellPaddingBorder( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure( Graphics graphics )
	{

		if ( isOpaque( ) )
			graphics.fillRectangle( getBounds( ).x + 1,
					getBounds( ).y + 1,
					getBounds( ).width - 2,
					getBounds( ).height - 2 );


		super.paintFigure( graphics );
		
		if ( blankString != null && blankString.length( ) > 0 )
		{
			graphics.setForegroundColor( ReportColorConstants.greyFillColor );
			drawBlankString( graphics, blankString );
			graphics.restoreState( );
		}
		
	}

	protected void drawBlankString( Graphics g, String s )
	{
		TextLayout tl = new TextLayout( Display.getCurrent( ) );

		tl.setText( s );
		Rectangle rc = tl.getBounds( );

		int left = ( getBounds( ).width - rc.width ) / 2;
		int top = ( getBounds( ).height - rc.height ) / 2;

		g.drawString( s, getBounds( ).x + left, getBounds( ).y + top );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#setBorder(org.eclipse.draw2d.Border)
	 */
	public void setBorder( Border border )
	{
		if ( border instanceof CellPaddingBorder )
		{
			super.setBorder( border );
		}
		else
		{
			( (CellPaddingBorder) getBorder( ) ).setChainBorder( border );
		}
	}

	/**
	 * @param blankString
	 *            The blankString to set.
	 */
	public void setBlankString( String blankString )
	{
		this.blankString = blankString;
	}
}