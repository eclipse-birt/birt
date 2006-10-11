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
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * Presents border of margin
 *  
 */
public class ReportDesignMarginBorder extends MarginBorder
{

	private static final Insets DEFAULT_CROP = new Insets(-3, -3, -2, -2);
	
	private int backgroundColor = 0xFFFFFF;

	/**
	 * Constructor
	 * 
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
		
		graphics.setBackgroundColor( ColorManager.getColor( backgroundColor ) );

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
		graphics.drawRectangle( figure.getBounds( )
				.getCopy( )
				.crop( margin )
				.crop( DEFAULT_CROP ) );
		graphics.setForegroundColor( oldForegroundColor );
	}

//	public void setBackgroundColor( Object obj )
//	{
//		if ( obj != null )
//		{
//			if ( obj instanceof String )
//			{
//				backgroundColor = ColorUtil.parseColor( (String) obj );
//			}
//			else
//			{
//				backgroundColor = ( (Integer) obj ).intValue( );
//			}
//		}
//	}
	
	public void setBackgroundColor( int color )
	{
		if ( color != 0 )
		{
			backgroundColor = color;
		}
	}
}