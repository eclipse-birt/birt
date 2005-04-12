/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

/**
 * Row Drag Tracker
 */
public class RowDragTracker extends TableDragGuideTracker
{

	/**
	 * Constructor
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public RowDragTracker( EditPart sourceEditPart, int start, int end )
	{
		super( sourceEditPart, start, end );
		setDisabledCursor( Cursors.SIZENS );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.examples.logicdesigner.edit.tracker.TableDragHFTracker#resize()
	 */
	protected void resize( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		int value = getLocation( ).y - getStartLocation( ).y;

		if ( getStart( ) != getEnd( ) )
		{
			value = getTrueValue( value );
			part.resizeRow( getStart( ), getEnd( ), value );
		}
		else
		{
			/**
			 * This is the Last Row, resize the whole table.
			 */
			Dimension dimension = getDragWidth( );
			if ( value < dimension.width )
			{
				value = dimension.width;
			}

			TableHandleAdapter adp = (TableHandleAdapter) HandleAdapterFactory.getInstance( )
					.getTableHandleAdapter( part.getModel( ) );

			Dimension dm = adp.calculateSize( );
			dm.height += value;
			try
			{
				adp.ajustSize( dm );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.examples.logicdesigner.edit.tracker.TableDragHFTracker#getMarqueeSelectionRectangle()
	 */
	protected Rectangle getMarqueeSelectionRectangle( )
	{
		IFigure figure = ( (TableEditPart) getSourceEditPart( ) ).getFigure( );
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

	protected Dimension getDragWidth( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		Object start = part.getRow( getStart( ) );
		return new Dimension( part.getMinHeight( getStart( ) )
				- getRowHeight( start ), Integer.MAX_VALUE );

	}
}