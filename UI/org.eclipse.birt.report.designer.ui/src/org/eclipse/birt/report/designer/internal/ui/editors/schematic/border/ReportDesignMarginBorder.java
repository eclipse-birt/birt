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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * Presents border of magin
 *  
 */
public class ReportDesignMarginBorder extends MarginBorder
{

	/**
	 * Constructor
	 * @param insets
	 */
	public ReportDesignMarginBorder( Insets insets )
	{
		super( insets );

	}

	/**
	 * paint the border (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MarginBorder#paint(org.eclipse.draw2d.IFigure,
	 *      org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint( IFigure figure, Graphics graphics, Insets insets )
	{
		Color oldBackgroundColor = graphics.getBackgroundColor( );
		Color oldForegroundColor = graphics.getForegroundColor( );
		Rectangle rect = figure.getBounds( ).getCopy( );
		Insets margin = getInsets( figure );

		graphics.setBackgroundColor( ColorConstants.white );
		Rectangle top = new Rectangle( rect.x, rect.y, rect.width, margin.top );
		graphics.fillRectangle( top );
		Rectangle left = new Rectangle( rect.x,
				rect.y,
				margin.left,
				rect.height );
		graphics.fillRectangle( left );
		Rectangle bottom = new Rectangle( rect.x,
				rect.height - margin.bottom,
				rect.width,
				margin.bottom );
		graphics.fillRectangle( bottom );
		Rectangle right = new Rectangle( rect.width - margin.right,
				rect.y,
				margin.right,
				rect.height );
		graphics.fillRectangle( right );

		graphics.setBackgroundColor( oldBackgroundColor );

		graphics.setForegroundColor( ReportColorConstants.MarginBorderColor );
		graphics.drawRectangle( figure.getBounds( ).getCopy( ).crop( margin ) );
		graphics.setForegroundColor( oldForegroundColor );
	}
}