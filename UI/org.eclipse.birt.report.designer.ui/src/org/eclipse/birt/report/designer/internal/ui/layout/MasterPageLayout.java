/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This layout manager lays out the components inside the master page area.
 * 
 *  
 */
public class MasterPageLayout extends XYLayout
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout( IFigure parent )
	{
		List children = parent.getChildren( );
		IFigure child;
		Rectangle clientArea = parent.getClientArea( );
		int x = clientArea.x;
		int y = clientArea.y;
		int width = clientArea.width;
		int height = clientArea.height;

		Point offset = getOrigin( parent );
		IFigure figure;
		for ( int i = 0; i < children.size( ); i++ )
		{
			figure = (IFigure) children.get( i );
			figure.getBounds( ).width = clientArea.width;
			Rectangle bounds = (Rectangle) getConstraint( figure );
			if ( bounds == null )
			{
				continue;
			}
			bounds = bounds.getTranslated( offset );
			Dimension preferredSize = figure.getPreferredSize( );
			bounds = bounds.getCopy( );
			if ( bounds.width < preferredSize.width )
			{
				bounds.width = preferredSize.width;
			}
			if ( bounds.height < preferredSize.height || bounds.height < 50 )
			{
				//TODO:50 is the default height, migrate it to preference later.
				bounds.height = Math.max( preferredSize.height, 50 );
			}
			//adapt the figure's location to make sure it's inside the client
			// area
			if ( bounds.y + bounds.height > height + y )
			{
				bounds.y = height + y - bounds.height;
			}
			if ( bounds.x + bounds.width > width + x )
			{
				bounds.x = width + x - bounds.width;
			}
			if ( bounds.x < x )
			{
				bounds.x = x;
			}
			if ( bounds.y < y )
			{
				bounds.y = y;
			}
			figure.setBounds( bounds );

		}
	}
}