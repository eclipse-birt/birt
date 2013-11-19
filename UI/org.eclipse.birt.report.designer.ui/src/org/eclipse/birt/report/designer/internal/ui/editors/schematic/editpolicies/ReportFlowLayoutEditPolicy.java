/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies;

import java.util.List;

import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.commands.FlowMoveChildCommand;
import org.eclipse.birt.report.designer.core.commands.PasteCommand;
import org.eclipse.birt.report.designer.core.commands.SetConstraintCommand;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.IOutsideBorder;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;

public class ReportFlowLayoutEditPolicy extends FlowLayoutEditPolicy
{

	/**
	 * Constructor
	 */
	public ReportFlowLayoutEditPolicy( )
	{
		super( );

	}

	protected Command getAddCommand( Request req )
	{
		EditPart parent = getHost( );
		ChangeBoundsRequest request = (ChangeBoundsRequest) req;
		List editParts = request.getEditParts( );
		CompoundCommand command = new CompoundCommand( );
		for ( int i = 0; i < editParts.size( ); i++ )
		{
			EditPart child = (EditPart) editParts.get( i );
			command.add( createAddCommand( parent,
					child,
					getInsertionReference( request ) ) );
		}
		return command.unwrap( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCloneCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected Command getCloneCommand( ChangeBoundsRequest request )
	{
		return getAddCommand( request );
	}

	/**
	 * @param parent
	 * @param child
	 * @param insertionReference
	 * @return command
	 */
	protected Command createAddCommand( EditPart parent, EditPart child,
			EditPart insertionReference )
	{
		Object parentModel = null;
		if ( parent.getModel( ) instanceof ListBandProxy )
		{
			parentModel = ( (ListBandProxy) parent.getModel( ) ).getSlotHandle( );
		}
		else
		{
			parentModel = parent.getModel( );
		}
		if ( !( child.getModel( ) instanceof DesignElementHandle ) )
		{
			return UnexecutableCommand.INSTANCE;
		}
		if (insertionReference != null && !(insertionReference.getModel( ) instanceof DesignElementHandle))
		{
			return UnexecutableCommand.INSTANCE;
		}
		return new PasteCommand( (DesignElementHandle) child.getModel( ),
				parentModel,
				insertionReference == null ? null
						: (DesignElementHandle) insertionReference.getModel( ),
				false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart,
	 *      org.eclipse.gef.EditPart)
	 */
	protected Command createAddCommand( EditPart child, EditPart after )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	protected org.eclipse.gef.commands.Command createAddCommand(
			EditPart child, Object constraint )
	{
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
	 */
	protected org.eclipse.gef.commands.Command getDeleteDependantCommand(
			Request request )
	{
		return null;
	}

	protected org.eclipse.gef.commands.Command getCreateCommand(
			CreateRequest request )
	{
		EditPart after = getInsertionReference( request );

		CreateCommand command = new CreateCommand( request.getExtendedData( ) );

		Object model = this.getHost( ).getModel( );
		//		if ( model instanceof ReportElementModel )
		//		{
		//			command.setParent( ( (ReportElementModel) model ).getSlotHandle( ) );
		//		}else
		if ( model instanceof SlotHandle )
		{
			command.setParent( ( (SlotHandle) model ) );
		}

		else if ( model instanceof ListBandProxy )
		{
			command.setParent( ( (ListBandProxy) model ).getSlotHandle( ) );
		}
		else
		{
			command.setParent( model );
		}
		// No previous edit part
		if ( after != null )
		{
			command.setAfter( after.getModel( ) );
		}

		return command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createMoveChildCommand(org.eclipse.gef.EditPart,
	 *      org.eclipse.gef.EditPart)
	 */
	protected Command createMoveChildCommand( EditPart child, EditPart after )
	{
		Object afterModel = null;
		if ( after != null )
		{
			afterModel = after.getModel( );
		}
		if(child.getParent( ).getModel( ) instanceof ReportItemHandle)
		{
			ReportItemHandle reportHandle = (ReportItemHandle)child.getParent( ).getModel( );
			if(reportHandle.getViews( ).contains( child.getModel( ) ))
			{
				return UnexecutableCommand.INSTANCE;
			}
		}
		FlowMoveChildCommand command = new FlowMoveChildCommand( child.getModel( ),
				afterModel,
				child.getParent( ).getModel( ) );
		return command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand( Request request )
	{
		if ( REQ_RESIZE_CHILDREN.equals( request.getType( ) ) )
			return getResizeChildrenCommand( (ChangeBoundsRequest) request );
		return super.getCommand( request );
	}

	protected Command getResizeChildrenCommand( ChangeBoundsRequest request )
	{
		CompoundCommand resize = new CompoundCommand( );
		Command c;
		GraphicalEditPart child;
		List children = request.getEditParts( );

		for ( int i = 0; i < children.size( ); i++ )
		{
			child = (GraphicalEditPart) children.get( i );
			c = createChangeConstraintCommand( request,
					child,
					getConstraintFor( request, child ) );
			resize.add( c );
		}
		return resize.unwrap( );
	}

	/**
	 * Returns the layout's origin relative to the {@link
	 * LayoutEditPolicy#getLayoutContainer()}. In other words, what Point on the
	 * parent Figure does the LayoutManager use a reference when generating the
	 * child figure's bounds from the child's constraint.
	 * <P>
	 * By default, it is assumed that the layout manager positions children
	 * relative to the client area of the layout container. Thus, when
	 * processing Viewer-relative Points or Rectangles, the clientArea's
	 * location (top-left corner) will be subtracted from the Point/Rectangle,
	 * resulting in an offset from the LayoutOrigin.
	 * 
	 * @return Point
	 */
	protected Point getLayoutOrigin( )
	{
		return getLayoutContainer( ).getClientArea( ).getLocation( );
	}

	/**
	 * @param rect
	 * @return
	 */
	protected Object getConstraintFor( Rectangle rect )
	{
		return new Rectangle( rect );
	}

	/**
	 * @param request
	 * @param child
	 * @param constraintFor
	 * @return
	 */
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, GraphicalEditPart child,
			Object constraintFor )
	{

		ReportItemHandle handle = (ReportItemHandle) child.getModel( );

		SetConstraintCommand command = new SetConstraintCommand( );

		command.setModel( handle );

		int direction = request.getResizeDirection( );
		Dimension size = new Dimension( ( (Rectangle) constraintFor ).getSize( ) );

		if ( direction == PositionConstants.EAST
				|| direction == PositionConstants.WEST )
		{
			size.height = -1;
		}
		else if ( direction == PositionConstants.SOUTH
				|| direction == PositionConstants.NORTH )
		{
			size.width = -1;
		}

		command.setSize( size );

		return command;
	}

	/**
	 * Generates a draw2d constraint object derived from the specified child
	 * EditPart using the provided Request. The returned constraint will be
	 * translated to the application's model later using
	 * {@link #translateToModelConstraint(Object)}.
	 * 
	 * @param request
	 *            the ChangeBoundsRequest
	 * @param child
	 *            the child EditPart for which the constraint should be
	 *            generated
	 * @return the draw2d constraint
	 */
	protected Object getConstraintFor( ChangeBoundsRequest request,
			GraphicalEditPart child )
	{
		IFigure figure = child.getFigure( );
		Rectangle rect = new PrecisionRectangle(figure.getBounds());
		figure.translateToAbsolute(rect);
		rect = request.getTransformedRectangle( rect );
		
		figure.translateToRelative(rect);
		rect.translate( getLayoutOrigin( ).getNegated( ) );
		if (figure instanceof IOutsideBorder)
		{
			Border border = ((IOutsideBorder)figure).getOutsideBorder( );
			if (border !=  null)
			{
				Insets insets = border.getInsets( figure );
				rect.shrink( insets.right, insets.bottom );
			}
		}

		return getConstraintFor( rect );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy( EditPart child )
	{
		//		if ( child instanceof LabelEditPart
		//				|| child instanceof TextEditPart
		//				|| child instanceof ListEditPart 
		//				|| child instanceof PlaceHolderEditPart)
		//			return new NonResizableEditPolicy( );
		//		if ( child instanceof TableEditPart )
		//		{
		//			TableResizeEditPolice rpc = new TableResizeEditPolice( );
		//			rpc.setResizeDirections( PositionConstants.SOUTH
		//					| PositionConstants.EAST
		//					| PositionConstants.SOUTH_EAST );
		//
		//			return rpc;
		//		}
		//		ReportElementResizablePolicy policy = new ReportElementResizablePolicy( );
		//		policy.setResizeDirections( PositionConstants.SOUTH
		//				| PositionConstants.EAST
		//				| PositionConstants.SOUTH_EAST );
		//		return policy;
		EditPolicy retValue = null;
		if ( child instanceof ReportElementEditPart )
		{
			retValue = ( (ReportElementEditPart) child ).getResizePolice( this );
		}
		if ( retValue == null )
		{
			retValue = new ReportElementNonResizablePolicy( );
		}

		return retValue;
	}

	/**
	 * @return <code>true</code> if the host's LayoutManager is in a
	 *         horizontal orientation
	 */
	protected boolean isHorizontal( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#showLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void showLayoutTargetFeedback( Request request )
	{
		GraphicalEditPart ep = (GraphicalEditPart) getHost( );
		// show cursor even if there is no children
		if ( getHost( ).getChildren( ).size( ) == 0 )
		{

			Rectangle bounds = getAbsoluteClientBounds( ep );

			Point p1 = new Point( bounds.x + 5, bounds.y + 2 );

			Point p2 = new Point( bounds.x + 5, bounds.y
					+ Math.min( bounds.height - 2, 18 ) );

			setTargetFeedbackPoints( p1, p2 );
			ep.getViewer( ).reveal( ep );
		}
		else
		{
			showLayoutTargetPosition( request );
		}
	}

	protected void setTargetFeedbackPoints( Point p1, Point p2 )
	{
		Transposer transposer = new Transposer( );
		transposer.setEnabled( !isHorizontal( ) );

		Rectangle parentBox = transposer.t( getAbsoluteClientBounds( (GraphicalEditPart) getHost( ) ) );
		Polyline fb = getLineFeedback( );
		if ( p2.y >= parentBox.bottom( ) && parentBox.bottom( ) - p1.y < 10 )
		{
			p2.y = p1.y;

			List list = ( (GraphicalEditPart) getHost( ) ).getChildren( );
			int size = list.size( );
			if ( size == 0 )
			{
				p2.x = p1.x + Math.min( 30, parentBox.width );
			}
			else
			{
				GraphicalEditPart last = (GraphicalEditPart) list.get( size - 1 );
				Rectangle rect = getAbsoluteBounds( last );
				p2.x = p1.x + Math.min( rect.width - 8, parentBox.width );
			}
		}
		else if ( p2.y >= parentBox.bottom( ) )
		{
			p2.y = parentBox.bottom( );
		}
		fb.translateToRelative( p1 );

		fb.translateToRelative( p2 );

		fb.setPoint( p1, 0 );
		fb.setPoint( p2, 1 );
	}

	protected boolean isEditPartFigureBlock( EditPart editPart )
	{
		if ( editPart == null
				|| !( editPart.getModel( ) instanceof ReportItemHandle ) )
		{
			return true;
		}
		ReportItemHandle handle = (ReportItemHandle) editPart.getModel( );
		ReportItemConstraint constraint = new ReportItemConstraint( );
		constraint.setDisplay( handle.getPrivateStyle( ).getDisplay( ) );
		return constraint.isBlock( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#getFeedbackIndexFor(org.eclipse.gef.Request)
	 */
	protected int getFeedbackIndexFor( Request request )
	{
		Transposer transposer = new Transposer( );
		transposer.setEnabled( !isHorizontal( ) );

		List list = getHost( ).getChildren( );
		int size = list.size( ) - 1;
		int index = getFeedbackPosition( request );
		if ( size < 0 || index < 0 || index > size )
		{
			return index;
		}

		Rectangle rect = getAbsoluteBounds( (GraphicalEditPart) list.get( size ) );
		Point p = transposer.t( getLocationFromRequest( request ) );
		if ( p.y > rect.bottom( ) )
		{
			index = -1;
		}

		return index;
	}

	/**
	 * @param request
	 *            the Request
	 * @return the index for the insertion reference
	 */
	protected int getFeedbackPosition( Request request )
	{
		List children = getHost( ).getChildren( );
		if ( children.isEmpty( ) )
			return -1;

		Transposer transposer = new Transposer( );
		transposer.setEnabled( !isHorizontal( ) );

		Point p = transposer.t( getLocationFromRequest( request ) );

		// Current row bottom, initialize to above the top.
		int rowBottom = Integer.MIN_VALUE;
		int candidate = -1;
		for ( int i = 0; i < children.size( ); i++ )
		{
			EditPart child = (EditPart) children.get( i );
			Rectangle rect = transposer.t( getAbsoluteBounds( ( (GraphicalEditPart) child ) ) );
			if ( rect.y > rowBottom )
			{
				/*
				 * We are in a new row, so if we don't have a candidate but yet
				 * are within the previous row, then the current entry becomes
				 * the candidate. This is because we know we must be to the
				 * right of center of the last Figure in the previous row, so
				 * this Figure (which is at the start of a new row) is the
				 * candidate.
				 */
				if ( p.y <= rowBottom )
				{
					if ( candidate == -1 )
						candidate = i;
					break;
				}
				else
					candidate = -1; // Mouse's Y is outside the row, so reset
				// the candidate
			}
			rowBottom = Math.max( rowBottom, rect.bottom( ) );
			if ( candidate == -1 )
			{
				/*
				 * See if we have a possible candidate. It is a candidate if the
				 * cursor is left of the center of this candidate.
				 */
				if ( p.x <= rect.x + ( rect.width / 2 ) || p.y < rect.y )
					candidate = i;
			}
			if ( candidate != -1 )
			{
				// We have a candidate, see if the rowBottom has grown to
				// include the mouse Y.
				if ( p.y <= rowBottom )
				{
					/*
					 * Now we have determined that the cursor.Y is above the
					 * bottom of the current row of figures. Stop now, to
					 * prevent the next row from being searched
					 */
					break;
				}
			}
		}
		return candidate;
	}

	/**
	 * Shows an insertion line if there is one or more current children.
	 * 
	 * @see LayoutEditPolicy#showLayoutTargetFeedback(Request)
	 */
	protected void showLayoutTargetPosition( Request request )
	{
		if ( getHost( ).getChildren( ).size( ) == 0 )
			return;
		//Polyline fb = getLineFeedback( );
		Transposer transposer = new Transposer( );
		transposer.setEnabled( !isHorizontal( ) );

		boolean before = true;
		int epIndex = getFeedbackIndexFor( request );

		//System.out.println( "index ==" + epIndex );
		Rectangle r = null;
		if ( epIndex == -1 )
		{
			before = false;
			epIndex = getHost( ).getChildren( ).size( ) - 1;
			EditPart editPart = (EditPart) getHost( ).getChildren( )
					.get( epIndex );
			r = transposer.t( getAbsoluteBounds( (GraphicalEditPart) editPart ) );
		}
		else
		{
			EditPart editPart = (EditPart) getHost( ).getChildren( )
					.get( epIndex );
			boolean isBlock = false;
			if ( epIndex == 0 )
			{
				isBlock = true;
			}
			else
			{
				EditPart preEditPart = (EditPart) getHost( ).getChildren( )
						.get( epIndex - 1 );
				isBlock = isEditPartFigureBlock( preEditPart );
			}

			r = transposer.t( getAbsoluteBounds( (GraphicalEditPart) editPart ) );
			Point p = transposer.t( getLocationFromRequest( request ) );
			if ( p.x <= r.x + ( r.width / 2 ) && isBlock )
				before = true;
			else
			{
				/*
				 * We are not to the left of this Figure, so the emphasis line
				 * needs to be to the right of the previous Figure, which must
				 * be on the previous row.
				 */
				before = false;
				//getFeedbackIndexFor( request );
				//if (epIndex != 0)
				//{
				epIndex--;
				//}
				if ( epIndex >= 0 && epIndex < getHost( ).getChildren( ).size( ) )
				{
					editPart = (EditPart) getHost( ).getChildren( )
							.get( epIndex );
					r = transposer.t( getAbsoluteBounds( (GraphicalEditPart) editPart ) );
				}
			}
		}
		int x = Integer.MIN_VALUE;
		if ( before )
		{
			/*
			 * Want the line to be halfway between the end of the previous and
			 * the beginning of this one. If at the begining of a line, then
			 * start halfway between the left edge of the parent and the
			 * beginning of the box, but no more than 5 pixels (it would be too
			 * far and be confusing otherwise).
			 */
			if ( epIndex > 0 )
			{
				// Need to determine if a line break.
				Rectangle boxPrev = transposer.t( getAbsoluteBounds( (GraphicalEditPart) getHost( ).getChildren( )
						.get( epIndex - 1 ) ) );
				int prevRight = boxPrev.right( );
				if ( prevRight < r.x )
				{
					// Not a line break
					x = prevRight + ( r.x - prevRight ) / 2;
				}
				else if ( prevRight == r.x )
				{
					x = prevRight + 1;
				}
			}
			if ( x == Integer.MIN_VALUE )
			{
				// It is a line break.
				Rectangle parentBox = transposer.t( getAbsoluteBounds( (GraphicalEditPart) getHost( ) ) );
				x = r.x - 5;
				if ( x < parentBox.x )
					x = parentBox.x + ( r.x - parentBox.x ) / 2;
			}
		}
		else
		{
			/*
			 * We only have before==false if we are at the end of a line, so go
			 * halfway between the right edge and the right edge of the parent,
			 * but no more than 5 pixels.
			 */
			Rectangle parentBox = transposer.t( getAbsoluteClientBounds( (GraphicalEditPart) getHost( ) ) );
			int rRight = r.x + r.width;
			int pRight = parentBox.x + parentBox.width;
			x = rRight + 5;
			int index = epIndex >= 0 ? epIndex : getHost( ).getChildren( )
					.size( ) - 1;
			EditPart part = epIndex < 0 ? null
					: (EditPart) getHost( ).getChildren( ).get( epIndex );

			if ( x - 4 > pRight || isEditPartFigureBlock( part ) )
			{
				//System.out.println( index);
				if ( index >= 0
						&& index < getHost( ).getChildren( ).size( ) - 1 )
				{

					EditPart editPart = (EditPart) getHost( ).getChildren( )
							.get( epIndex + 1 );

					r = transposer.t( getAbsoluteBounds( (GraphicalEditPart) editPart ) );
					parentBox = transposer.t( getAbsoluteBounds( (GraphicalEditPart) getHost( ) ) );
					x = r.x - 5;
					if ( x < parentBox.x )
						x = parentBox.x + ( r.x - parentBox.x ) / 2;

				}
				else if ( getFeedbackIndexFor( request ) == 0 )
				{

					parentBox = transposer.t( getAbsoluteBounds( (GraphicalEditPart) getHost( ) ) );
					x = r.x - 5;
					if ( x < parentBox.x )
						x = parentBox.x + ( r.x - parentBox.x ) / 2;
				}
				else
				{
					Point p1 = new Point( parentBox.x + 5, r.y + r.height + 2 );
					Point p2 = new Point( parentBox.x + 5, r.y
							+ r.height
							+ 2
							+ Math.min( parentBox.y
									+ parentBox.height
									- ( r.y + r.height + 2 ), 18 ) );

					setTargetFeedbackPoints( p1, p2 );
					return;
				}
			}
		}
		Point p1 = new Point( x, r.y - 4 );
		p1 = transposer.t( p1 );

		Point p2 = new Point( x, r.y + r.height + 4 );
		p2 = transposer.t( p2 );

		setTargetFeedbackPoints( p1, p2 );

	}

	private Rectangle getAbsoluteBounds( GraphicalEditPart ep )
	{
		Rectangle bounds = ep.getFigure( ).getBounds( ).getCopy( );
		ep.getFigure( ).translateToAbsolute( bounds );
		return bounds;
	}

	protected Rectangle getAbsoluteClientBounds( GraphicalEditPart ep )
	{
		Rectangle bounds = ep.getContentPane( ).getClientArea( ).getCopy( );
		ep.getFigure( ).translateToAbsolute( bounds );
		return bounds;
	}

	protected Point getLocationFromRequest( Request request )
	{
		return ( (DropRequest) request ).getLocation( );
	}
}