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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.widgets.Display;

/**
 * TableDragGuideTracker
 */
public abstract class TableDragGuideTracker extends DragEditPartsTracker
{

	private int start;

	private int end;

	private Figure marqueeRectangleFigure;

	/**
	 * Constructor
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public TableDragGuideTracker( EditPart sourceEditPart, int start, int end )
	{
		super( sourceEditPart );
		this.start = start;
		this.end = end;
	}

	protected boolean handleDragInProgress( )
	{
		return super.handleDragInProgress( );
	}

	protected boolean handleButtonUp( int button )
	{
		return super.handleButtonUp( button );
	}

	protected void performSelection( )
	{
	}

	protected void showSourceFeedback( )
	{
		//super.showSourceFeedback();
		Rectangle rect = getMarqueeSelectionRectangle( ).getCopy( );
		getMarqueeFeedbackFigure( ).translateToRelative( rect );
		getMarqueeFeedbackFigure( ).setBounds( rect );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#showTargetFeedback()
	 */
	protected void showTargetFeedback( )
	{
	}

	protected void performDrag( )
	{

		resize( );
		TableEditPart part = (TableEditPart) getSourceEditPart( );
		part.getViewer( ).setSelection( part.getViewer( ).getSelection( ) );
	}

	private IFigure getMarqueeFeedbackFigure( )
	{
		if ( marqueeRectangleFigure == null )
		{
			marqueeRectangleFigure = new MarqueeRectangleFigure( );
			addFeedback( marqueeRectangleFigure );
		}
		return marqueeRectangleFigure;
	}

	protected void eraseSourceFeedback( )
	{
		super.eraseSourceFeedback( );
		if ( marqueeRectangleFigure != null )
		{
			removeFeedback( marqueeRectangleFigure );
			marqueeRectangleFigure = null;
		}
	}

	class MarqueeRectangleFigure extends Figure
	{

		private int offset = 0;

		private boolean schedulePaint = true;

		private static final int DELAY = 110; //animation delay in millisecond

		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure( Graphics graphics )
		{
			Rectangle bounds = getBounds( ).getCopy( );
			graphics.translate( getLocation( ) );

			graphics.setXORMode( true );
			graphics.setForegroundColor( ColorConstants.white );
			graphics.setBackgroundColor( ColorConstants.black );

			graphics.setLineStyle( Graphics.LINE_DOT );

			int[] points = new int[6];

			points[0] = 0 + offset;
			points[1] = 0;
			points[2] = bounds.width - 1;
			points[3] = 0;
			points[4] = bounds.width - 1;
			points[5] = bounds.height - 1;

			graphics.drawPolyline( points );

			points[0] = 0;
			points[1] = 0 + offset;
			points[2] = 0;
			points[3] = bounds.height - 1;
			points[4] = bounds.width - 1;
			points[5] = bounds.height - 1;

			graphics.drawPolyline( points );

			graphics.translate( getLocation( ).getNegated( ) );

			if ( schedulePaint )
			{
				Display.getCurrent( ).timerExec( DELAY, new Runnable( ) {

					public void run( )
					{
						offset++;
						if ( offset > 5 )
							offset = 0;

						schedulePaint = true;
						repaint( );
					}
				} );
			}

			schedulePaint = false;
		}

	}

	public int getEnd( )
	{
		return end;
	}

	public void setEnd( int end )
	{
		this.end = end;
	}

	protected abstract void resize( );

	protected abstract Rectangle getMarqueeSelectionRectangle( );

	protected abstract Dimension getDragWidth( );

	public int getStart( )
	{
		return start;
	}

	public void setStart( int start )
	{
		this.start = start;
	}

	protected int getTrueValue( int value )
	{
		Dimension dimension = getDragWidth( );
		if ( value < dimension.width )
		{
			value = dimension.width;
		}
		else if ( value > dimension.height )
		{
			value = dimension.height;
		}
		return value;
	}

	protected int getRowHeight( Object row )
	{

		return TableUtil.caleVisualHeight( getTableEditPart( ), row );
	}

	protected int getColumnWidth( Object column )
	{
		return TableUtil.caleVisualWidth( getTableEditPart( ), column );
	}

	protected int getRowHeight( int rowNumber )
	{
		Object row = getTableEditPart( ).getRow( rowNumber );
		return getRowHeight( row );
	}

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