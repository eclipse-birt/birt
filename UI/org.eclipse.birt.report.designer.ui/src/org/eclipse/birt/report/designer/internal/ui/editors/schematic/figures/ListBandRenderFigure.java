/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;

/**
 * Presents list band figure for list band render
 *  
 */
public class ListBandRenderFigure extends Figure
{

	private static final Insets margin = new Insets( 5, 5, 4, 4 );

	public static final int HEIGHT = 23;

	public ListBandRenderFigure( )
	{
		setLayoutManager( new ReportFlowLayout( ) );
		setBorder( new MarginBorder( margin ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure( Graphics graphics )
	{
		graphics.setForegroundColor( ReportColorConstants.ShadowLineColor );
		graphics.setLineStyle( SWT.LINE_SOLID );
		graphics.drawRectangle( getBounds( ).getCopy( ).shrink( 2, 2 ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize( int wHint, int hHint )
	{
		if ( wHint > 0 )
		{
			getBounds( ).width = wHint;
		}
		if ( hHint > 0 )
		{
			getBounds( ).height = hHint;
		}
		validate( );
		Dimension dim = super.getPreferredSize( wHint, hHint );
		if ( dim.height < HEIGHT )
		{
			dim.height = HEIGHT;
		}
		if ( wHint > 0 )
		{
			dim.width = wHint;
		}
		return dim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize( int wHint, int hHint )
	{
		Dimension retValue = super.getMinimumSize( wHint, hHint );
		if ( retValue.height < HEIGHT )
		{
			retValue.height = HEIGHT;
		}
		return retValue;
	}
}