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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableDragGuideTracker;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableUtil;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

/**
 * Drag the cross cell right border to adjust the coumn width
 */
public class CrosstabColumnDragTracker extends TableDragGuideTracker
{

	/**
	 * Constructor
	 * 
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public CrosstabColumnDragTracker( EditPart sourceEditPart, int start,
			int end )
	{
		super( sourceEditPart, start, end );
		setDisabledCursor( Cursors.SIZEWE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableDragGuideTracker#getDragWidth()
	 */
	protected Dimension getDragWidth( )
	{
		return new Dimension( TableUtil.getMinWidth( getCrosstabTableEditPart( ),
				getStart( ) )
				- CrosstabTableUtil.caleVisualWidth( getCrosstabTableEditPart( ),
						getStart( ) ),
				CrosstabTableUtil.caleVisualWidth( getCrosstabTableEditPart( ),
						getEnd( ) )
						- TableUtil.getMinWidth( getCrosstabTableEditPart( ),
								getEnd( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableDragGuideTracker#getMarqueeSelectionRectangle()
	 */
	protected Rectangle getMarqueeSelectionRectangle( )
	{
		IFigure figure = getCrosstabTableEditPart( ).getFigure( );
		Insets insets = figure.getInsets( );

		int value = getLocation( ).x - getStartLocation( ).x;
		value = getTrueValue( value );

		Point p = getStartLocation( ).getCopy( );
		figure.translateToAbsolute( p );
		figure.translateToRelative( p );
		Rectangle bounds = figure.getBounds( ).getCopy( );
		figure.translateToAbsolute( bounds );

		return new Rectangle( value + p.x,
				bounds.y + insets.top,
				2,
				bounds.height - ( insets.top + insets.bottom ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableDragGuideTracker#resize()
	 */
	protected void resize( )
	{
		System.out.println( "resize" );
	}

	private CrosstabTableEditPart getCrosstabTableEditPart( )
	{
		return (CrosstabTableEditPart) getSourceEditPart( ).getParent( );
	}
}
