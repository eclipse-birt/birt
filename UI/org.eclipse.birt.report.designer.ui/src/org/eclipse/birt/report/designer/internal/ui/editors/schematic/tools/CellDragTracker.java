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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.TableCellSelectionHelper;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * Table Cell drag track
 * </p>
 * 
 *  
 */
public class CellDragTracker extends DragEditPartsTracker implements
		DragTracker
{

	/**
	 * Creates a new CellTracker, with the CROSS cursor
	 * 
	 * @param sourceEditPart
	 */
	public CellDragTracker( EditPart sourceEditPart )
	{
		super( sourceEditPart );
		setDefaultCursor( SharedCursors.CROSS );
		setUnloadWhenFinished( false );
	}

	/*
	 * Overrides the method, do nothing (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#handleFinished()
	 */
	protected void handleFinished( )
	{
	}

	static final int TOGGLE_MODE = 1;

	static final int APPEND_MODE = 2;

	private int mode;

	private Figure marqueeRectangleFigure;

	//	private List allChildren = new ArrayList( );
	private List selectedEditParts;

	private Request targetRequest;

	private static final Request MARQUEE_REQUEST = new Request( RequestConstants.REQ_SELECTION );

	private List calculateNewSelection( )
	{

		List newSelections = new ArrayList( );

		calculateNewSelection( getMarqueeSelectionRectangle( ), newSelections );
		int size = newSelections.size( );
		calculateNewSelection( getUnionBounds( newSelections ), newSelections );
		while ( size != newSelections.size( ) )
		{
			size = newSelections.size( );
			calculateNewSelection( getUnionBounds( newSelections ),
					newSelections );

		}
		return newSelections;
	}

	private void calculateNewSelection( Rectangle bounds, List list )
	{
		List children = getAllChildren( );

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
					&& child.getTargetEditPart( MARQUEE_REQUEST ) == child
					&& isFigureVisible( figure ) )
			{
				if ( !list.contains( child ) )
				{
					list.add( child );
				}

			}
		}
	}

	private Rectangle getUnionBounds( List list )
	{

//		TableEditPart table = (TableEditPart) getSourceEditPart( ).getParent( );
//
//		int size = list.size( );
//		TableCellEditPart[] parts = new TableCellEditPart[size];
//		list.toArray( parts );
//
//		TableCellEditPart[] caleNumber = table.getMinAndMaxNumber( parts );
//		TableCellEditPart minRow = caleNumber[0];
//		TableCellEditPart minColumn = caleNumber[1];
//		TableCellEditPart maxRow = caleNumber[2];
//		TableCellEditPart maxColumn = caleNumber[3];
//
//		Rectangle min = minRow.getBounds( ).getCopy( );
//		Rectangle max = maxColumn.getBounds( ).getCopy( );
//		Rectangle retValue = min.union( max )
//				.union( minColumn.getBounds( ).getCopy( ) )
//				.union( maxRow.getBounds( ).getCopy( ) )
//				.shrink( 2, 2 );
//		minRow.getFigure( ).translateToAbsolute( retValue );
//
//		return retValue;
//		TableEditPart table = (TableEditPart) getSourceEditPart( ).getParent( );

		int size = list.size( );
		if (size == 0)
		{
			return new Rectangle();
		}
		IFigure figure = ((TableCellEditPart)list.get(0)).getFigure();
		Rectangle retValue = figure.getBounds().getCopy();
		
		for (int i=1; i<size; i++)
		{
			TableCellEditPart cellPart = (TableCellEditPart)list.get(i);
			retValue.union(cellPart.getFigure().getBounds());		
		}
		retValue.shrink(2, 2);
		figure.translateToAbsolute( retValue );

		return retValue;
	}

	/**
	 * @param child
	 * @return
	 */
	private boolean isInTable( EditPart child )
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

	protected Request createTargetRequest( )
	{
		return MARQUEE_REQUEST;
	}

	/**
	 * Erases feedback if necessary and puts the tool into the terminal state.
	 */
	public void deactivate( )
	{
		if ( isInState( STATE_DRAG_IN_PROGRESS ) )
		{
			eraseMarqueeFeedback( );
			eraseTargetFeedback( );
		}
		super.deactivate( );
		//		allChildren = new ArrayList( );
		setState( STATE_TERMINAL );
	}

	private void eraseMarqueeFeedback( )
	{
		if ( marqueeRectangleFigure != null )
		{
			removeFeedback( marqueeRectangleFigure );
			marqueeRectangleFigure = null;
		}
	}

	protected void eraseTargetFeedback( )
	{
		if ( selectedEditParts == null )
			return;
		ListIterator oldEditParts = selectedEditParts.listIterator( );
		while ( oldEditParts.hasNext( ) )
		{
			EditPart editPart = (EditPart) oldEditParts.next( );
			editPart.eraseTargetFeedback( getTargetRequest( ) );
		}
	}

	/**
	 * Return a List including all of the children of the Table editpart
	 */
	private List getAllChildren( )
	{
		return getSourceEditPart( ).getParent( ).getChildren( );
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName( )
	{
		return REQ_SELECTION;
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

	private Rectangle getMarqueeSelectionRectangle( )
	{
		return new Rectangle( getStartLocation( ), getLocation( ) );
	}

	private int getSelectionMode( )
	{
		return mode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#getTargetRequest()
	 */
	protected Request getTargetRequest( )
	{
		if ( targetRequest == null )
			targetRequest = createTargetRequest( );
		return targetRequest;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
	 */
	protected boolean handleButtonDown( int button )
	{
		boolean rlt = super.handleButtonDown( button );

		if ( button == 1 && getCurrentInput( ).isShiftKeyDown( ) )
		{
			performShiftSelect( );
		}
		else if ( button == 1 && getCurrentInput( ).isControlKeyDown( ) )
		{
			performCtrlSelect( );
		}

		return rlt;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	protected boolean handleButtonUp( int button )
	{
		if ( stateTransition( STATE_DRAG_IN_PROGRESS, STATE_TERMINAL ) )
		{
			eraseTargetFeedback( );
			eraseMarqueeFeedback( );
			performMarqueeSelect( );
			return true;
		}

		boolean bool = super.handleButtonUp( button );
		handleFinished( );

		return bool;
	}

	private void performCtrlSelect( )
	{
		/**
		 * Does nothing now.
		 */
	}

	private void performShiftSelect( )
	{
		TableEditPart parent = (TableEditPart) getSourceEditPart( ).getParent( );

		/**
		 * Checks viewer consistency.
		 */
		if ( parent.getViewer( ) != getCurrentViewer( ) )
		{
			return;
		}

		ArrayList nlst;

		List slst = getCurrentViewer( ).getSelectedEditParts( );

		if ( slst != null && slst.contains( getSourceEditPart( ) ) )
		{
			nlst = new ArrayList( );

			nlst.add( slst.get( 0 ) );
		}
		else
		{
			nlst = new ArrayList( slst );
		}

		Rectangle constraint = TableCellSelectionHelper.getSelectionRectangle( (TableCellEditPart) getSourceEditPart( ),
				nlst );

		boolean refined = TableCellSelectionHelper.increaseSelectionRectangle( constraint,
				parent );

		while ( refined )
		{
			refined = TableCellSelectionHelper.increaseSelectionRectangle( constraint,
					parent );
		}

		List lst = TableCellSelectionHelper.getRectangleSelection( constraint,
				parent );

		if ( lst == null || lst.size( ) == 0 )
		{
			return;
		}

		boolean first = true;

		for ( Iterator itr = lst.iterator( ); itr.hasNext( ); )
		{
			GraphicalEditPart part = (GraphicalEditPart) itr.next( );

			if ( first )
			{
				getCurrentViewer( ).select( part );
				first = false;
			}
			else
			{
				getCurrentViewer( ).appendSelection( part );
			}

			getCurrentViewer( ).reveal( part );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performSelection()
	 */
	protected void performSelection( )
	{
		if ( hasSelectionOccurred( ) )
			return;

		/**
		 * Hacks the old selection algorithm, checks the consistency of parents
		 * of selected objects.
		 */
		if ( getCurrentInput( ).isControlKeyDown( )
				|| getCurrentInput( ).isShiftKeyDown( ) )
		{
			setFlag( FLAG_SELECTION_PERFORMED, true );
			EditPartViewer viewer = getCurrentViewer( );
			List selectedObjects = viewer.getSelectedEditParts( );

			boolean consist = true;

			EditPart sourceParent = getSourceEditPart( ).getParent( );

			for ( Iterator itr = selectedObjects.iterator( ); itr.hasNext( ); )
			{
				EditPart part = (EditPart) itr.next( );

				if ( part.getParent( ) != sourceParent )
				{
					consist = false;
					break;
				}
			}

			if ( consist )
			{
				if ( getCurrentInput( ).isControlKeyDown( ) )
				{
					/**
					 * Does nothing, leaves it to performCtrlSelect().
					 */
					return;
				}
				else if ( getCurrentInput( ).isShiftKeyDown( ) )
				{
					/**
					 * Does nothing, leaves it to performShiftSelect().
					 */
					return;
				}
			}

			viewer.select( getSourceEditPart( ) );

			return;
		}

		super.performSelection( );
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
	 */
	protected boolean handleDragInProgress( )
	{
		if ( isInState( STATE_DRAG | STATE_DRAG_IN_PROGRESS ) )
		{
			showMarqueeFeedback( );
			eraseTargetFeedback( );
			selectedEditParts = calculateNewSelection( );
			showTargetFeedback( );
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleFocusLost()
	 */
	protected boolean handleFocusLost( )
	{
		if ( isInState( STATE_DRAG | STATE_DRAG_IN_PROGRESS ) )
		{
			handleFinished( );
			return true;
		}
		return false;
	}

	/**
	 * This method is called when mouse or keyboard input is invalid and erases
	 * the feedback.
	 * 
	 * @return <code>true</code>
	 */
	protected boolean handleInvalidInput( )
	{
		eraseTargetFeedback( );
		eraseMarqueeFeedback( );
		return true;
	}

	/**
	 * Handles high-level processing of a key down event. KeyEvents are
	 * forwarded to the current viewer's {@link KeyHandler}, via
	 * {@link KeyHandler#keyPressed(KeyEvent)}.
	 * 
	 * @see AbstractTool#handleKeyDown(KeyEvent)
	 */
	protected boolean handleKeyDown( KeyEvent e )
	{
		if ( super.handleKeyDown( e ) )
			return true;
		if ( getCurrentViewer( ).getKeyHandler( ) != null
				&& getCurrentViewer( ).getKeyHandler( ).keyPressed( e ) )
			return true;
		return false;
	}

	private boolean isFigureVisible( IFigure fig )
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

	private void performMarqueeSelect( )
	{
		EditPartViewer viewer = getCurrentViewer( );

		List newSelections = calculateNewSelection( );

		// If in multi select mode, add the new selections to the already
		// selected group; otherwise, clear the selection and select the new
		// group
		//System.out.println(getSelectionMode());
		if ( getSelectionMode( ) == APPEND_MODE )
		{
			for ( int i = 0; i < newSelections.size( ); i++ )
			{
				EditPart editPart = (EditPart) newSelections.get( i );
				viewer.appendSelection( editPart );
			}
		}
		else if ( getSelectionMode( ) == TOGGLE_MODE )
		{
			List selected = new ArrayList( viewer.getSelectedEditParts( ) );
			for ( int i = 0; i < newSelections.size( ); i++ )
			{
				EditPart editPart = (EditPart) newSelections.get( i );
				if ( editPart.getSelected( ) != EditPart.SELECTED_NONE )
					selected.remove( editPart );
				else
					selected.add( editPart );
			}
			viewer.setSelection( new StructuredSelection( selected ) );
		}
		else
		{
			viewer.setSelection( new StructuredSelection( newSelections ) );
		}
	}

	/**
	 * @see org.eclipse.gef.Tool#setViewer(org.eclipse.gef.EditPartViewer)
	 */
	public void setViewer( EditPartViewer viewer )
	{
		if ( viewer == getCurrentViewer( ) )
			return;
		super.setViewer( viewer );
		if ( viewer instanceof GraphicalViewer )
			setDefaultCursor( SharedCursors.CROSS );
		else
			setDefaultCursor( SharedCursors.NO );
	}


	private void showMarqueeFeedback( )
	{
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
		for ( int i = 0; i < selectedEditParts.size( ); i++ )
		{
			EditPart editPart = (EditPart) selectedEditParts.get( i );
			editPart.showTargetFeedback( getTargetRequest( ) );
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
}