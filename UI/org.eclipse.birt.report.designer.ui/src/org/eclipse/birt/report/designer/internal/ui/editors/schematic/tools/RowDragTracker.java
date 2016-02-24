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

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite;
import org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerComposite.DragGuideInfo;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Cursor;

/**
 * Row Drag Tracker
 */
public class RowDragTracker extends TableDragGuideTracker
{
	private static final String RESIZE_COLUMN_TRANS_LABEL = Messages.getString("RowDragTracker.ResizeRow"); //$NON-NLS-1$
	private static final String PREFIX_LABEL = Messages.getString("RowDragTracker.Show.Label"); //$NON-NLS-1$
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

	@Override
	protected Cursor getDefaultCursor( )
	{
		if (isCloneActive())
		{
			return Cursors.SIZENS;
		}
		return super.getDefaultCursor( );
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.examples.logicdesigner.edit.tracker.TableDragHFTracker#resize()
	 */
	protected void resize( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		int value = getMouseTrueValueY( );
		part.getTableAdapter( ).transStar(RESIZE_COLUMN_TRANS_LABEL );
		if (isresizeMultipleRow( ))
		{
			List list = filterEditPart( part.getViewer( ).getSelectedEditParts( ));
			boolean resizeTable = false;
			int height = 0;
			for (int i=0; i<list.size( ); i++)
			{
				int tempValue = value;
				Object model =  ((EditPart)list.get( i )).getModel( );
				RowHandleAdapter adapter = HandleAdapterFactory.getInstance( ).getRowHandleAdapter( model );
				int start = adapter.getRowNumber( );
				int end = start + 1;
				
				int ori = TableUtil.caleVisualHeight( part, model );
				int adjustHeight = TableUtil.caleVisualHeight( part, part.getRow(  getStart( ) ) ) + value;
				if (getStart( ) != start)
				{
					tempValue = adjustHeight - ori;
				}
				if (start == part.getRowCount( ))
				{
					end = start;
					resizeTable = true;
					
				}
				else
				{
					height = height + getTrueValue( tempValue, start, end);
				}
				resizeRow( tempValue,start, end );
				
			}
			
			if (resizeTable)
			{
				Dimension size = part.getTableAdapter( ).getSize( );
				try
				{
					part.getTableAdapter( ).setSize( new Dimension(-1, size.height + height) );
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
			resizeRow( value, getStart( ), getEnd( ) );
		}
		part.getTableAdapter( ).transEnd( );
	}

	private void resizeRow(int value, int start, int end)
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );

		if ( start != end )
		{
			value = getTrueValue( value, start, end );
			part.resizeRow( start, end, value );
		}
		else
		{
			/**
			 * This is the Last Row, resize the whole table.
			 */
			Dimension dimension = getDragWidth(start, end );
			if ( value < dimension.width )
			{
				value = dimension.width;
			}

			TableHandleAdapter adp = HandleAdapterFactory.getInstance( )
					.getTableHandleAdapter( part.getModel( ) );

			Dimension dm = adp.calculateSize( );
			dm.height += value;
			dm.width = -1;
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
	
	private void resizeFixRow(int value, int start, int end)
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		Object row = part.getRow( start );
		
		if (!(row instanceof RowHandle))	
		{
			return ;
		}
		
		int rowHeight = TableUtil.caleVisualHeight( part, row );
		
		try
		{
			MetricUtility.updateDimension( ( (RowHandle) row ).getHeight( ),
					rowHeight + getTrueValue( value ) );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}
	
	private boolean isresizeMultipleRow()
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		List list = filterEditPart(part.getViewer( ).getSelectedEditParts( ));
		if (list.size( ) < 2)
		{
			return false;
		}
		
		Object first =  ((EditPart)list.get( 0 )).getModel( );
		if (!(first instanceof org.eclipse.birt.report.model.api.RowHandle)  
				|| !((org.eclipse.birt.report.model.api.RowHandle)first).getContainer( ).equals( part.getModel( ) ))
		{
			return false;
		}
		for (int i=0; i<list.size( ); i++)
		{
			Object model =  ((EditPart)list.get( i )).getModel( );
			if (model.equals( part.getRow( getStart( ) ) ) )
			{
				return true;
			}
		}
		
		
		return false;
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
		value = getTrueValueAbsolute( value );

		Point p = getStartLocation( ).getCopy( );
		figure.translateToAbsolute( p );
		figure.translateToRelative( p );
		Rectangle bounds = figure.getBounds( ).getCopy( );
		figure.translateToAbsolute( bounds );

		return new Rectangle( bounds.x + insets.left, value + p.y, bounds.width
				- ( insets.left + insets.right ), 2 );
	}

	protected Dimension getDragWidth(int startNumber, int endNumber )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		Object start = part.getRow( startNumber );
		return new Dimension( part.getMinHeight( startNumber )
				- getRowHeight( start ), Integer.MAX_VALUE );

	}
	
	/**Gets the row height
	 * @param row
	 * @return
	 */
	protected int getRowHeight( Object row )
	{
		return TableUtil.caleVisualHeight( getTableEditPart( ), row );
	}
	
	/**Gets the row height
	 * @param rowNumber
	 * @return
	 */
	protected int getRowHeight( int rowNumber )
	{
		Object row = getTableEditPart( ).getRow( rowNumber );
		return getRowHeight( row );
	}
	
	/**
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
		return getShowLabel( TableUtil.caleVisualHeight( part, part.getRow( getStart( ) ) ));
	}
	
	private String getShowLabel(int pix)
	{
		String unit = getDefaultUnits( );
		
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
		//int value = getTrueValue( getLocation( ).y - getStartLocation( ).y);
		int value = getTrueValue( getMouseTrueValueY( ) );
		
		int adjustWidth = TableUtil.caleVisualHeight(  part, part.getRow( getStart( ) ) ) + value;
		updateInfomation( getShowLabel( adjustWidth ) );
		return bool;
		
	}
	
	@Override
	protected void fitResize( )
	{
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		int value = getMouseTrueValueY( );
		part.getTableAdapter( ).transStar(RESIZE_COLUMN_TRANS_LABEL );
		int height = 0;
		if (isresizeMultipleRow( ))
		{
			List list = filterEditPart( part.getViewer( ).getSelectedEditParts( ));

			for (int i=0; i<list.size( ); i++)
			{
				int tempValue = value;
				Object model =  ((EditPart)list.get( i )).getModel( );
				RowHandleAdapter adapter = HandleAdapterFactory.getInstance( ).getRowHandleAdapter( model );
				int start = adapter.getRowNumber( );
				int end = start + 1;
				
				int ori = TableUtil.caleVisualHeight( part, model );
				int adjustHeight = TableUtil.caleVisualHeight( part, part.getRow(  getStart( ) ) ) + value;
				if (getStart( ) != start)
				{
					tempValue = adjustHeight - ori;
				}
				if (start == part.getRowCount( ))
				{
					end = start;
					
				}
				height = height + getTrueValue( tempValue, start, end);
				resizeFixRow( tempValue,start, end );
				
			}
		}
		else
		{
			height = height + getTrueValue( value, getStart( ), getEnd( ));
			resizeFixRow( value, getStart( ), getEnd( ) );
		}
		
		Dimension tableSize = part.getFigure( ).getSize( ); 
		try
		{
			if (part.getTableAdapter( ).isSupportHeight( ))
			{
				ReportItemHandle handle = part.getTableAdapter( ).getReportItemHandle( );
				//DimensionHandle dimension = handle.getWidth();
				//dimension.s
				//part.getTableAdapter( ).setSize( new Dimension(tableSize.width + width, -1) );
				double tbWidth = converPixToDefaultUnit( tableSize.height + height );
				
				DimensionValue dimensionValue = new DimensionValue( tbWidth,
						getDefaultUnits( ) );
				handle.getHeight( ).setValue( dimensionValue );
			}

		}
		catch ( SemanticException e )
		{
			part.getTableAdapter( ).rollBack( );
			ExceptionHandler.handle( e );
		}
		
		part.getTableAdapter( ).transEnd( );
		
	}
	
	@Override
	protected DragGuideInfo createDragGuideInfo( )
	{
		int value = getTrueValue( getMouseTrueValueY());
		Point p = getStartLocation( ).getCopy( );
		
		getAbstractTableEditPart( ).getFigure( ).translateToRelative( p );
		value = value + p.y;
		EditorRulerComposite.DragGuideInfo info = new EditorRulerComposite.DragGuideInfo(false, value );
		return info;
	}
}