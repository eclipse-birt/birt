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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.layout.ListLayout;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * List item figure
 *  
 */
public class ListFigure extends ReportElementFigure
{

	private static final String BORDER_TEXT = Messages.getString( "ListFigure.BORDER_TEXT" ); //$NON-NLS-1$

	public ListFigure( )
	{
		SectionBorder border = new SectionBorder( );
		border.setIndicatorLabel( BORDER_TEXT );
		border.setIndicatorIcon( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_LIST ) );
		setBorder( border );
		setLayoutManager( new ListLayout( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure( Graphics graphics )
	{
		super.paintFigure( graphics );
		//graphics.drawRectangle(getBounds().getCopy().shrink(2,2));
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
		return super.getPreferredSize( wHint, hHint );
	}

}