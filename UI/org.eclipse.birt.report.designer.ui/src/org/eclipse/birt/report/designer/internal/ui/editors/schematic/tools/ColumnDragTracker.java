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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Cursor;

public class ColumnDragTracker extends TableDragGuideTracker
{

	/**
	 * Creates new ColumnDragtrcker, for resize the table column width
	 * 
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public ColumnDragTracker( EditPart sourceEditPart, int start, int end )
	{
		super( sourceEditPart, start, end );
		setDisabledCursor( Cursors.SIZEWE );
	}

	protected void resize( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		int value = getLocation( ).x - getStartLocation( ).x;

		if ( getStart( ) != getEnd( ) )
		{
			value = getTrueValue( value );
			part.resizeColumn( getStart( ), getEnd( ), value );
		}
		else
		{
			/**
			 * This is the Last Column, resize the whole table.
			 */
			Dimension dimension = getDragWidth( );

			if ( value < dimension.width )
			{
				value = dimension.width;
			}

			TableHandleAdapter adp = HandleAdapterFactory.getInstance( )
					.getTableHandleAdapter( part.getModel( ) );

			Dimension dm = adp.calculateSize( );

			dm.width += value;
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

	protected Rectangle getMarqueeSelectionRectangle( )
	{
		IFigure figure = ( (TableEditPart) getSourceEditPart( ) ).getFigure( );
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

	protected Dimension getDragWidth( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );

		if ( getStart( ) == getEnd( ) )
		{
			return new Dimension( part.getMinWidth( getStart( ) )
					- getColumnWidth( getStart( ) ), Integer.MAX_VALUE );
		}

		return new Dimension( part.getMinWidth( getStart( ) )
				- getColumnWidth( getStart( ) ), getColumnWidth( getEnd( ) )
				- part.getMinWidth( getEnd( ) ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#setCursor(org.eclipse.swt.graphics.Cursor)
	 */
	protected void setCursor( Cursor cursor )
	{
		super.setCursor( cursor );
	}
	
	/**Gets the column width
	 * @param columnNumber
	 * @return
	 */
	protected int getColumnWidth( int columnNumber )
	{
		Object column = getTableEditPart( ).getColumn( columnNumber );
		if ( column == null )
		{
			return HandleAdapterFactory.getInstance( )
					.getTableHandleAdapter( getTableEditPart( ).getModel( ) )
					.getDefaultWidth( columnNumber );
		}

		return getColumnWidth( column );
	}
	
	/**Gets the column width
	 * @param column
	 * @return
	 */
	protected int getColumnWidth( Object column )
	{
		return TableUtil.caleVisualWidth( getTableEditPart( ), column );
	}
	
	/**
	 * Gets the TableEditPart
	 * 
	 * @return
	 */
	protected TableEditPart getTableEditPart( )
	{
		return (TableEditPart) getSourceEditPart( );
	}
}