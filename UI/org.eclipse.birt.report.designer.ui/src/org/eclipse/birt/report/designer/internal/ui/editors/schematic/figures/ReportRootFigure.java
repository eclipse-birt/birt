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
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;


/**
 * Report Editot root editpart.
 * 
 */
public class ReportRootFigure extends ReportElementFigure
{
	private static final Insets DEFAULT_MARGIN = new Insets( 3, 3, 3, 3 );
	
	private static final Point PRIVATE_POINT = new Point( );
	private static final Insets DEFAULT_CROP = new Insets( -3, -3, -2, -2 );
	private boolean showMargin;

	/**
	 * 
	 */
	public ReportRootFigure()
	{
		
	}
	
	/* Do nothing
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintBorder( Graphics graphics )
	{
		//does nothing , figure paint itself.
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getInsets()
	 */
	public Insets getInsets( )
	{
		if ( showMargin )
		{
			if ( getBorder( ) != null )
			{
				return getBorder( ).getInsets( this );
			}

			return NO_INSETS;
		}
		return DEFAULT_MARGIN;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure( Graphics graphics )
	{
		super.paintFigure( graphics );

		graphics.setForegroundColor( ReportColorConstants.MarginBorderColor );
		graphics.drawRectangle( getBounds( ).getCopy( )
				.crop( getInsets( ) )
				.crop( DEFAULT_CROP ) );
		
		graphics.setForegroundColor( ColorConstants.black );
		graphics.drawRectangle( getBounds( ).getCopy( ).crop( new Insets(0, 0, 1,1) ) );
		
//		Rectangle rect = getBounds( );
//		
//		graphics.setForegroundColor( ColorConstants.white );
//		graphics.setBackgroundColor( ColorConstants.gray );
//		graphics.fillGradient( rect.x, rect.y, 5, rect.height, false );

	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintChildren(org.eclipse.draw2d.Graphics)
	 */
	protected void paintChildren( Graphics graphics )
	{
		IFigure child;

		for ( int i = 0; i < this.getChildren( ).size( ); i++ )
		{
			child = (IFigure) this.getChildren( ).get( i );
			if ( child.isVisible( ) )
			{
				graphics.setClip( getBounds( ).getCopy( )
						.intersect( child.getBounds( ) ) );
				child.paint( graphics );
				graphics.restoreState( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#findDescendantAtExcluding(int,
	 *      int, org.eclipse.draw2d.TreeSearch)
	 */
	protected IFigure findDescendantAtExcluding( int x, int y,
			TreeSearch search )
	{
		PRIVATE_POINT.setLocation( x, y );
		translateFromParent( PRIVATE_POINT );
		if ( !getBounds( ).contains( PRIVATE_POINT ) )
			return null;

		IFigure fig;
		for ( int i = getChildren( ).size( ); i > 0; )
		{
			i--;
			fig = (IFigure) getChildren( ).get( i );
			if ( fig.isVisible( ) )
			{
				fig = fig.findFigureAt( PRIVATE_POINT.x,
						PRIVATE_POINT.y,
						search );
				if ( fig != null )
					return fig;
			}
		}
		//No descendants were found
		return null;
	}
	/**
	 * @return Returns the showMargin.
	 */
	public boolean isShowMargin( )
	{
		return showMargin;
	}
	/**
	 * @param showMargin The showMargin to set.
	 */
	public void setShowMargin( boolean showMargin )
	{
		this.showMargin = showMargin;
	}
}
