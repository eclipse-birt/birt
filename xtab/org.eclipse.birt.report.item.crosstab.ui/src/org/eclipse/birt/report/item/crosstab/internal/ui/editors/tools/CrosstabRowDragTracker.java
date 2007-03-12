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
 * Drag the cross cell bottom border to adjust the row height.
 */

public class CrosstabRowDragTracker extends TableDragGuideTracker
{

	/**
	 * Constructor
	 * 
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public CrosstabRowDragTracker( EditPart sourceEditPart, int start, int end )
	{
		super( sourceEditPart, start, end );
		setDisabledCursor( Cursors.SIZENS );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.TableDragGuideTracker#getDragWidth()
	 */
	protected Dimension getDragWidth( )
	{
		return new Dimension( TableUtil.getMinHeight( getCrosstabTableEditPart( ),
				getStart( ) )
				- CrosstabTableUtil.caleVisualHeight( getCrosstabTableEditPart( ),
						getStart( ) ),
				Integer.MAX_VALUE );
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

		int value = getLocation( ).y - getStartLocation( ).y;
		value = getTrueValue( value );

		Point p = getStartLocation( ).getCopy( );
		figure.translateToAbsolute( p );
		figure.translateToRelative( p );
		Rectangle bounds = figure.getBounds( ).getCopy( );
		figure.translateToAbsolute( bounds );

		return new Rectangle( bounds.x + insets.left, value + p.y, bounds.width
				- ( insets.left + insets.right ), 2 );
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
