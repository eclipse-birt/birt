/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * The class is used for the EditorGuideEditPart EditPolicy.PRIMARY_DRAG_ROLE
 * policy
 *  
 */
public class EditorDragGuidePolicy extends GraphicalEditPolicy
{

	private List attachedEditParts = null;
	private IFigure dummyGuideFigure, dummyLineFigure;

	//private boolean dragInProgress = false;

	/**
	 * Creates the Line Figure, when drag the margin guide.
	 * 
	 * @return
	 */
	protected IFigure createDummyLineFigure( )
	{
		return new Figure( )
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
			 */
			protected void paintFigure( Graphics graphics )
			{
				graphics.setLineStyle( Graphics.LINE_DOT );
				graphics.setXORMode( true );
				graphics.setForegroundColor( ColorConstants.darkGray );
				if ( bounds.width > bounds.height )
				{
					graphics.drawLine( bounds.x, bounds.y, bounds.right( ),
							bounds.y );
					graphics.drawLine( bounds.x + 2, bounds.y, bounds.right( ),
							bounds.y );
				}
				else
				{
					graphics.drawLine( bounds.x, bounds.y, bounds.x, bounds
							.bottom( ) );
					graphics.drawLine( bounds.x, bounds.y + 2, bounds.x, bounds
							.bottom( ) );
				}
			}
		};
	}

	/**
	 * Creates the Guide Figure, when drag the margin guide.
	 * 
	 * @return
	 */
	protected EditorGuideFigure createDummyGuideFigure( )
	{
		return new EditorGuidePlaceHolder( getGuideEditPart( ).isHorizontal( ) );
	}

	/*
	 * If you undo guide creation while dragging that guide, it was leaving
	 * behind drag feedback. This was because by the time eraseSourceFeedback()
	 * was being called, the guide edit part had been deactivated (and hence
	 * eraseSourceFeedback is never called on this policy). So we make sure that
	 * this policy cleans up when it is deactivated.
	 */
	public void deactivate( )
	{
		removeFeedback( );
		super.deactivate( );
	}

	/**
	 * When drag the margin guide, the attache editparts move with the guide.
	 * Now do nothing
	 * 
	 * @param request
	 */
	private void eraseAttachedPartsFeedback( Request request )
	{
		if ( attachedEditParts != null )
		{
			ChangeBoundsRequest req = new ChangeBoundsRequest( request
					.getType( ) );
			req.setEditParts( attachedEditParts );

			Iterator i = attachedEditParts.iterator( );

			while ( i.hasNext( ) )
				( (EditPart) i.next( ) ).eraseSourceFeedback( req );
			attachedEditParts = null;
		}
	}

	/*
	 * Erases the draw source feedback (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	public void eraseSourceFeedback( Request request )
	{
		getGuideEditPart( ).updateLocationOfFigures(
				getGuideEditPart( ).getZoomedPosition( ) );
		getHostFigure( ).setVisible( true );
		getGuideEditPart( ).getGuideLineFigure( ).setVisible( true );
		removeFeedback( );
		getGuideEditPart( ).setCurrentCursor( null );
		//dragInProgress = false;

		eraseAttachedPartsFeedback( request );
	}

	private List getAttachedEditParts( )
	{
		if ( attachedEditParts == null )
			attachedEditParts = getGuideEditPart( ).getRulerProvider( )
					.getAttachedEditParts(
							getHost( ).getModel( ),
							( (EditorRulerEditPart) getHost( ).getParent( ) )
									.getDiagramViewer( ) );
		return attachedEditParts;
	}

	/*
	 * Gets the commande with specific request (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand( Request request )
	{
		Command cmd;
		final ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		if ( isDeleteRequest( req ) )
		{
			cmd = getGuideEditPart( ).getRulerProvider( )
					.getDeleteGuideCommand( getHost( ).getModel( ) );
		}
		else
		{
			int pDelta;
			if ( getGuideEditPart( ).isHorizontal( ) )
			{
				pDelta = req.getMoveDelta( ).y;
			}
			else
			{
				pDelta = req.getMoveDelta( ).x;
			}
			if ( isMoveValid( getGuideEditPart( ).getZoomedPosition( ) + pDelta ) )
			{
				ZoomManager zoomManager = getGuideEditPart( ).getZoomManager( );
				if ( zoomManager != null )
				{
					pDelta = (int) Math.round( pDelta / zoomManager.getZoom( ) );
				}
				cmd = getGuideEditPart( ).getRulerProvider( )
						.getMoveGuideCommand( getHost( ).getModel( ), pDelta );
			}
			else
			{
				cmd = UnexecutableCommand.INSTANCE;
			}
		}
		return cmd;
	}

	/**
	 * Creates the Guide Figure, when drag the margin guide.
	 * 
	 * @return
	 */
	protected IFigure getDummyGuideFigure( )
	{
		if ( dummyGuideFigure == null )
		{
			dummyGuideFigure = createDummyGuideFigure( );
		}
		return dummyGuideFigure;
	}

	/**
	 * Gets the line figure when drag the margin guide
	 * 
	 * @return
	 */
	protected IFigure getDummyLineFigure( )
	{
		if ( dummyLineFigure == null )
		{
			dummyLineFigure = createDummyLineFigure( );
		}
		return dummyLineFigure;
	}

	/**
	 * Gets the GuideEditPart
	 * 
	 * @return
	 */
	protected EditorGuideEditPart getGuideEditPart( )
	{
		return (EditorGuideEditPart) getHost( );
	}

	/**
	 * Now return false
	 * 
	 * @param req
	 * @return if the darg is delete the margin guide
	 */
	protected boolean isDeleteRequest( ChangeBoundsRequest req )
	{
		return false;
	}

	/**
	 * Now return true
	 * 
	 * @param zoomedPosition
	 * @return return true if the drag is valid.
	 */
	protected boolean isMoveValid( int zoomedPosition )
	{
		return true;
	}

	private void removeFeedback( )
	{
		if ( getDummyGuideFigure( ).getParent( ) != null )
		{
			getDummyGuideFigure( ).getParent( ).remove( getDummyGuideFigure( ) );
		}
		if ( getDummyLineFigure( ).getParent( ) != null )
		{
			getDummyLineFigure( ).getParent( ).remove( getDummyLineFigure( ) );
		}
	}

	private void showAttachedPartsFeedback( ChangeBoundsRequest request )
	{
		ChangeBoundsRequest req = new ChangeBoundsRequest( request.getType( ) );
		req.setEditParts( getAttachedEditParts( ) );

		if ( getGuideEditPart( ).isHorizontal( ) )
			req.setMoveDelta( new Point( 0, request.getMoveDelta( ).y ) );
		else
			req.setMoveDelta( new Point( request.getMoveDelta( ).x, 0 ) );

		Iterator i = getAttachedEditParts( ).iterator( );

		while ( i.hasNext( ) )
			( (EditPart) i.next( ) ).showSourceFeedback( req );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
	 */
	public void showSourceFeedback( Request request )
	{
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;

		// add the placeholder guide figure to the ruler
		getHostFigure( ).getParent( ).add( getDummyGuideFigure( ), 0 );
		( (GraphicalEditPart) getHost( ).getParent( ) ).setLayoutConstraint(
				getHost( ), getDummyGuideFigure( ), Integer.valueOf(
						getGuideEditPart( ).getZoomedPosition( ) ) );
		getDummyGuideFigure( ).setBounds( getHostFigure( ).getBounds( ) );
		// add the invisible placeholder line figure to the primary viewer
		getGuideEditPart( ).getGuideLayer( ).add( getDummyLineFigure( ), 0 );
		getGuideEditPart( ).getGuideLayer( ).setConstraint(
				getDummyLineFigure( ),
				Boolean.valueOf( getGuideEditPart( ).isHorizontal( ) ) );
		//			getDummyLineFigure( ).setBounds(
		//					getGuideEditPart( ).getGuideLineFigure( ).getBounds( ) );
		getDummyLineFigure( ).setBounds( getDummyLineFigureBounds( req ) );
		// move the guide being dragged to the last index so that it's drawn
		// on
		// top of other guides
		List children = getHostFigure( ).getParent( ).getChildren( );
		children.remove( getHostFigure( ) );
		children.add( getHostFigure( ) );

		if ( isDeleteRequest( req ) )
		{
			getHostFigure( ).setVisible( false );
			getGuideEditPart( ).getGuideLineFigure( ).setVisible( false );
			getGuideEditPart( ).setCurrentCursor( SharedCursors.ARROW );
			eraseAttachedPartsFeedback( request );
		}
		else
		{
			int newPosition;
			if ( getGuideEditPart( ).isHorizontal( ) )
			{
				newPosition = getGuideEditPart( ).getZoomedPosition( )
						+ req.getMoveDelta( ).y;
			}
			else
			{
				newPosition = getGuideEditPart( ).getZoomedPosition( )
						+ req.getMoveDelta( ).x;
			}
			getHostFigure( ).setVisible( true );
			getGuideEditPart( ).getGuideLineFigure( ).setVisible( true );
			if ( isMoveValid( newPosition ) )
			{
				getGuideEditPart( ).setCurrentCursor( null );
				getGuideEditPart( ).updateLocationOfFigures( newPosition );
				showAttachedPartsFeedback( req );
			}
			else
			{
				getGuideEditPart( ).setCurrentCursor( SharedCursors.NO );
				getGuideEditPart( ).updateLocationOfFigures(
						getGuideEditPart( ).getZoomedPosition( ) );
				eraseAttachedPartsFeedback( request );
			}
		}
	}

	private EditorRulerEditPart getRulerEditPart( )
	{
		return (EditorRulerEditPart) getHost( ).getParent( );
	}

	private Rectangle getDummyLineFigureBounds( ChangeBoundsRequest request )
	{
		Rectangle bounds = new Rectangle( );
		EditorRulerEditPart source = getRulerEditPart( );
		if ( source.isHorizontal( ) )
		{
			bounds.x = getCurrentPositionZoomed( request );
			bounds.y = source.getGuideLayer( ).getBounds( ).y;
			bounds.width = 1;
			bounds.height = source.getGuideLayer( ).getBounds( ).height;
		}
		else
		{
			bounds.x = source.getGuideLayer( ).getBounds( ).x;
			bounds.y = getCurrentPositionZoomed( request );
			bounds.width = source.getGuideLayer( ).getBounds( ).width;
			bounds.height = 1;
		}
		return bounds;
	}

	private int getCurrentPositionZoomed( ChangeBoundsRequest request )
	{

		int newPosition;
		if ( getGuideEditPart( ).isHorizontal( ) )
		{
			newPosition = getGuideEditPart( ).getZoomedPosition( )
					+ request.getMoveDelta( ).y;
		}
		else
		{
			newPosition = getGuideEditPart( ).getZoomedPosition( )
					+ request.getMoveDelta( ).x;
		}
		return newPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
	 */
	public boolean understandsRequest( Request req )
	{
		return req.getType( ).equals( REQ_MOVE );
	}

}