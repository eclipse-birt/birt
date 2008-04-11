/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
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

	private static final String RESIZE_COLUMN_TRANS_LABEL = Messages.getString("ColumnDragTracker.ResizeColumn"); //$NON-NLS-1$
	private static final String PREFIX_LABEL = Messages.getString("ColumnDragTracker.Show.Label"); //$NON-NLS-1$

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
		part.getTableAdapter( ).transStar(RESIZE_COLUMN_TRANS_LABEL );
		if (isresizeMultipleColumn( ))
		{
			List list = filterEditPart( part.getViewer( ).getSelectedEditParts( ));
			boolean resizeTable = false;
			int width = 0;
			for (int i=0; i<list.size( ); i++)
			{
				int tempValue = value;
				Object model =  ((EditPart)list.get( i )).getModel( );
				ColumnHandleAdapter adapter = HandleAdapterFactory.getInstance( ).getColumnHandleAdapter( model );
				int start = adapter.getColumnNumber( );
				int end = start + 1;
				
				int ori = TableUtil.caleVisualWidth( part, model );
				int adjustWidth = TableUtil.caleVisualWidth( part, part.getColumn( getStart( ) ) ) + value;
				if (getStart( ) != start)
				{
					tempValue = adjustWidth - ori;
				}
				if (start == part.getColumnCount( ))
				{
					end = start;
					resizeTable = true;
					
				}
				else
				{
					width = width + getTrueValue( tempValue, start, end);
				}
				resizeColumn( tempValue,start, end );
				
			}
			
			if (resizeTable)
			{
				Dimension size = part.getTableAdapter( ).getSize( );
				try
				{
					part.getTableAdapter( ).setSize( new Dimension(size.width + width, size.height) );
				}
				catch ( SemanticException e )
				{
					part.getTableAdapter( ).rollBack( );
					ExceptionHandler.handle( e );
				}
			}
		}
		else
		{
			resizeColumn( value, getStart( ), getEnd( ) );
		}
		part.getTableAdapter( ).transEnd( );
	}
	
	private void resizeColumn(int value, int start, int end)
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		//int value = getLocation( ).x - getStartLocation( ).x;

		if ( start != end )
		{
			value = getTrueValue( value, start, end );
			part.resizeColumn( start, end, value );
		}
		else
		{
			/**
			 * This is the Last Column, resize the whole table.
			 */
			Dimension dimension = getDragWidth(start, end );

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
	private boolean isresizeMultipleColumn()
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		List list = filterEditPart(part.getViewer( ).getSelectedEditParts( ));
		if (list.size( ) < 2)
		{
			return false;
		}
		
		Object first =  ((EditPart)list.get( 0 )).getModel( );
		if (!(first instanceof org.eclipse.birt.report.model.api.ColumnHandle)  
				|| !((org.eclipse.birt.report.model.api.ColumnHandle)first).getContainer( ).equals( part.getModel( ) ))
		{
			return false;
		}
		for (int i=0; i<list.size( ); i++)
		{
			Object model =  ((EditPart)list.get( i )).getModel( );
			if (model.equals( part.getColumn( getStart( ) ) ) )
			{
				return true;
			}
		}
		
		
		return false;
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

	protected Dimension getDragWidth( int start, int end)
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );

		//if ( start == end )
		//{
			return new Dimension( part.getMinWidth( start )
					- getColumnWidth( start ), Integer.MAX_VALUE );
		//}

//		return new Dimension( part.getMinWidth( start )
//				- getColumnWidth( start ), getColumnWidth( end )
//				- part.getMinWidth( end ) );

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
	
	@Override
	protected String getInfomation( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		return getShowLabel( TableUtil.caleVisualWidth( part, part.getColumn( getStart( ) ) ));
	}
	
	private String getShowLabel(int pix)
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		String unit = part.getTableAdapter( ).getHandle( ).getModuleHandle( ).getDefaultUnits( );
		
		double doubleValue = MetricUtility.pixelToPixelInch( pix );
		double showValue = DimensionUtil.convertTo( doubleValue,DesignChoiceConstants.UNITS_IN, unit ).getMeasure( );
		
		return PREFIX_LABEL + " "  + getShowValue( showValue )+ " " + getUnitDisplayName(unit)  + " (" + pix +" " + PIXELS_LABEL + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}
	
	private String getShowValue(double value)
	{
		return FORMAT.format( value );
	}
	
	@Override
	protected boolean handleDragInProgress( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		boolean bool =  super.handleDragInProgress( );
		int value = getTrueValue( getLocation( ).x - getStartLocation( ).x);
		
		int adjustWidth = TableUtil.caleVisualWidth( part, part.getColumn( getStart( ) ) ) + value;
		updateInfomation( getShowLabel( adjustWidth ) );
		return bool;
		
	}
}