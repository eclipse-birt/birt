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
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
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

	private static final String RESIZE_COLUMN_TRANS_LABEL = "Resize Column"; //$NON-NLS-1$

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
		if ( getStart( ) == getEnd( ) )
		{
			return new Dimension( TableUtil.getMinWidth( getCrosstabTableEditPart( ),
					getStart( ) )
					- CrosstabTableUtil.caleVisualWidth( getCrosstabTableEditPart( ),
							getStart( ) ),
					Integer.MAX_VALUE );
		}

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
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart( ).getParent( );
		int value = getLocation( ).x - getStartLocation( ).x;

		if ( getStart( ) != getEnd( ) )
		{
			value = getTrueValue( value );
			resizeColumn( getStart( ), getEnd( ), value );
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

			int with = calculateWidth() + value;

			int startWidth = 0;

			startWidth = CrosstabTableUtil.caleVisualWidth( part, getStart( ) );

			part.getCrosstabHandleAdapter( )
					.getCrosstabItemHandle( )
					.getModuleHandle( )
					.getCommandStack( )
					.startTrans( RESIZE_COLUMN_TRANS_LABEL );
			part.getCrosstabHandleAdapter( ).setWidth( with );
			
			part.getCrosstabHandleAdapter( ).setColumnWidth( getStart( ), startWidth + value );
			
			part.getCrosstabHandleAdapter( )
					.getCrosstabItemHandle( )
					.getModuleHandle( )
					.getCommandStack( )
					.commit( );
		}
	}

	/**
	 * Calculates table layout size. For table supports auto layout, the layout
	 * size need to be calculated when drawing.
	 * 
	 * @return
	 */
	private int calculateWidth( )
	{
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart( ).getParent( );

		int columnCount = part.getColumnCount( );
		int samColumnWidth = 0;
		for ( int i = 0; i < columnCount; i++ )
		{
			samColumnWidth = samColumnWidth
					+ CrosstabTableUtil.caleVisualWidth( part, i + 1 );
		}

		return samColumnWidth;
	}

	/**
	 * Resets size of column.
	 * 
	 * @param start
	 * @param end
	 * @param value
	 */
	public void resizeColumn( int start, int end, int value )
	{
		CrosstabTableEditPart part = (CrosstabTableEditPart) getSourceEditPart( ).getParent( );
		CrosstabHandleAdapter crosstabAdapter = part.getCrosstabHandleAdapter( );
		// CrosstabCellHandle startHandle =
		// crosstabAdapter.getColumnOprationCell( strat );
		// CrosstabCellHandle endHandle = crosstabAdapter.getColumnOprationCell(
		// end );

		int startWidth = 0;
		int endWidth = 0;

		startWidth = CrosstabTableUtil.caleVisualWidth( part, start );
		endWidth = CrosstabTableUtil.caleVisualWidth( part, end );

		part.getCrosstabHandleAdapter( )
				.getCrosstabItemHandle( )
				.getModuleHandle( )
				.getCommandStack( )
				.startTrans( RESIZE_COLUMN_TRANS_LABEL );
		// getTableAdapter( ).transStar( RESIZE_COLUMN_TRANS_LABEL );
		// //$NON-NLS-1$
		crosstabAdapter.setColumnWidth( start, startWidth + value );
		//crosstabAdapter.setColumnWidth( end, endWidth - value );

		part.getCrosstabHandleAdapter( )
				.getCrosstabItemHandle( )
				.getModuleHandle( )
				.getCommandStack( )
				.commit( );

	}

	private CrosstabTableEditPart getCrosstabTableEditPart( )
	{
		return (CrosstabTableEditPart) getSourceEditPart( ).getParent( );
	}
}
