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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.CellDragTracker;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;

/**
 * Table Utility class
 *  
 */
public class TableUtil
{

	/**
	 * Calculate the real selected objects in the selected bounds, complement
	 * the list if not in it.
	 * 
	 * @param bounds
	 * @param selection
	 * @param children
	 */
	public static void calculateNewSelection( Rectangle bounds, List selection,
			List children )
	{

		for ( int i = 0; i < children.size( ); i++ )
		{
			EditPart child = (EditPart) children.get( i );
			if ( !child.isSelectable( ) || isInTable( child ) )
				continue;
			IFigure figure = ( (GraphicalEditPart) child ).getFigure( );
			Rectangle r = figure.getBounds( ).getCopy( );
			figure.translateToAbsolute( r );

			Rectangle rect = bounds.getCopy( ).intersect( r );

			if ( rect.width > 0
					&& rect.height > 0
					&& figure.isShowing( )
					&& child.getTargetEditPart( CellDragTracker.MARQUEE_REQUEST ) == child
					&& isFigureVisible( figure ) )
			{
				if ( !selection.contains( child ) )
				{
					selection.add( child );
				}

			}
		}
	}

	/**
	 * Returns the union bounds for all the tabelCell in the given List.
	 * 
	 * @param list
	 * @return
	 */
	public static Rectangle getUnionBounds( List list )
	{

		int size = list.size( );
		if ( size == 0 )
		{
			return new Rectangle( );
		}
		IFigure figure = ( (TableCellEditPart) list.get( 0 ) ).getFigure( );
		Rectangle retValue = figure.getBounds( ).getCopy( );

		for ( int i = 1; i < size; i++ )
		{
			TableCellEditPart cellPart = (TableCellEditPart) list.get( i );
			retValue.union( cellPart.getFigure( ).getBounds( ) );
		}
		retValue.shrink( 2, 2 );
		figure.translateToAbsolute( retValue );

		return retValue;
	}

	/**
	 * Checks if the given editPart child is in a Table.
	 * 
	 * @param child
	 * @return
	 */
	private static boolean isInTable( EditPart child )
	{
		if ( child instanceof TableCellEditPart )
		{
			return false;
		}
		EditPart part = child.getParent( );
		while ( part != null )
		{
			if ( part instanceof TableCellEditPart )
			{
				return true;
			}
			part = part.getParent( );
		}
		return false;
	}

	/**
	 * Checks if the given figure is visible.
	 * 
	 * @param fig
	 * @return
	 */
	private static boolean isFigureVisible( IFigure fig )
	{
		Rectangle figBounds = fig.getBounds( ).getCopy( );
		IFigure walker = fig.getParent( );
		while ( !figBounds.isEmpty( ) && walker != null )
		{
			walker.translateToParent( figBounds );
			figBounds.intersect( walker.getBounds( ) );
			walker = walker.getParent( );
		}
		return !figBounds.isEmpty( );
	}

	/**
	 * Calculates the X value of row
	 * 
	 * @param part
	 * @param i
	 * @return
	 */
	public static int caleY( TableEditPart part, int row )
	{
		IFigure figure = part.getLayer( LayerConstants.PRIMARY_LAYER );
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager( )
				.getConstraint( figure );
		// if the Layout data is not existed, use the model data instead
		if ( data == null )
		{
			return 0;
		}
		int height = 0;
		for ( int i = 1; i < row; i++ )
		{
			height = height + data.findRowData( i ).height;
		}
		return height;
	}

	/**
	 * Calculates height of row
	 * 
	 * @param part
	 * @param row
	 * @return
	 */
	public static int caleVisualHeight( TableEditPart part, Object row )
	{

		RowHandleAdapter adapt = HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( row );

		IFigure figure = part.getLayer( LayerConstants.PRIMARY_LAYER );
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager( )
				.getConstraint( figure );
		if ( data == null )
		{
			return adapt.getHeight( );
		}
		int rowNumber = adapt.getRowNumber( );
		if ( rowNumber <= data.rowHeights.length )
		{
			return data.findRowData( rowNumber ).height;
		}
		return 0;
	}

	/**
	 * Calculates the width of column
	 * 
	 * @param part
	 * @param Column
	 * @return
	 */
	public static int caleVisualWidth( TableEditPart part, Object Column )
	{
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( Column );

		IFigure figure = part.getLayer( LayerConstants.PRIMARY_LAYER );
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager( )
				.getConstraint( figure );
		if ( data == null )
		{
			return adapt.getWidth( );
		}

		int columnNumber = adapt.getColumnNumber( );
		if ( columnNumber <= data.columnWidths.length )
		{
			return data.findColumnData( columnNumber ).width;
		}
		return 0;
	}

	/**
	 * Calculates the Y value of column
	 * 
	 * @param part
	 * @param i
	 * @return
	 */
	public static int caleX( TableEditPart part, int column )
	{

		IFigure figure = part.getLayer( LayerConstants.PRIMARY_LAYER );
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager( )
				.getConstraint( figure );
		if ( data == null )
		{
			return 0;
		}
		int height = 0;
		for ( int i = 1; i < column; i++ )
		{
			height = height + data.findColumnData( i ).width;
		}
		return height;
	}

	/**
	 * Get selected cells
	 * 
	 * @param part
	 * @return
	 */
	public static List getSelectionCells( TableEditPart part )
	{
		List list = part.getViewer( ).getSelectedEditParts( );
		List temp = new ArrayList( );

		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			if ( list.get( i ) instanceof TableCellEditPart )
			{
				temp.add( list.get( i ) );
			}
		}
		return temp;
	}

	/**
	 * Get minimum height of row.
	 * 
	 * @param part
	 * @param rowNumber
	 * @return
	 */
	public static int getMinHeight( TableEditPart part, int rowNumber )
	{

		IFigure figure = part.getLayer( LayerConstants.PRIMARY_LAYER );
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager( )
				.getConstraint( figure );
		if ( data == null )
		{
			return 0;
		}
		if ( rowNumber <= data.rowHeights.length )
		{
			return data.findRowData( rowNumber ).minRowHeight;
		}
		return 0;
	}

	/**
	 * Get minimum width of column.
	 * 
	 * @param part
	 * @param columnNumber
	 * @return
	 */
	public static int getMinWidth( TableEditPart part, int columnNumber )
	{

		IFigure figure = part.getLayer( LayerConstants.PRIMARY_LAYER );
		TableLayout.WorkingData data = (TableLayout.WorkingData) figure.getLayoutManager( )
				.getConstraint( figure );
		if ( data == null )
		{
			return 0;
		}
		if ( columnNumber <= data.columnWidths.length )
		{
			return data.findColumnData( columnNumber ).minColumnWidth;
		}
		return 0;
	}
}