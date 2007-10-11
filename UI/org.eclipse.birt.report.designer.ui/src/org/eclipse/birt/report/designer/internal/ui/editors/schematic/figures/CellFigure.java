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
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintBorder( Graphics graphics )
	{
		//does nothing, table border layer paint it.
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure( Graphics graphics )
	{
		super.paintFigure( graphics );
		
		if ( blankString != null && blankString.length( ) > 0 )
		{
			graphics.setForegroundColor( ReportColorConstants.ShadowLineColor );
			drawBlankString( graphics, blankString );
			graphics.restoreState( );
		}
		
	}

	protected void drawBlankString( Graphics g, String s )
	{
		TextLayout tl = new TextLayout( Display.getCurrent( ) );

		tl.setText( s );
		Rectangle rc = tl.getBounds( );

		int left = ( getClientArea( ).width - rc.width ) / 2;
		int top = ( getClientArea( ).height - rc.height ) / 2;

		g.drawText( s, getClientArea( ).x + left, getClientArea( ).y + top );
		tl.dispose();
	}

	/**
	 * @param blankString
	 *            The blankString to set.
	 */
	public void setBlankString( String blankString )
	{
		this.blankString = blankString;
	}

	
	public String getBlankString( )
	{
		return blankString;
	}
}