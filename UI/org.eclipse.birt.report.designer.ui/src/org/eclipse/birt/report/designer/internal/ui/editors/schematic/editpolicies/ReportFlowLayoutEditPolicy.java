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
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.LabelEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TextEditPart;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

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

	/**
	 * @param parent
	 * @param child
	 * @param insertionReference
	 * @return command
	 */
	private Command createAddCommand( EditPart parent, EditPart child,
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
		return createAddCommand( null, child, after );
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

		if ( getTargetEditPart( request ) instanceof ReportDesignEditPart )
		{
			Rectangle bound = getHostFigure( ).getClientArea( );
			//forbid DND outside client area
			if ( !bound.contains( request.getLocation( ) ) )
			{
				return null;
			}
		}

		EditPart after = getInsertionReference( request );

		CreateCommand command = new CreateCommand( request.getExtendedData( ) );

		Object model = this.getHost( ).getModel( );
		if ( model instanceof ReportElementModel )
		{
			command.setParent( ( (ReportElementModel) model ).getSlotHandle( ) );
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
		command.setSize( ( (Rectangle) constraintFor ).getSize( ) );

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
		Rectangle rect = child.getFigure( ).getBounds( );
		rect = request.getTransformedRectangle( rect );
		rect.translate( getLayoutOrigin( ).getNegated( ) );
		return getConstraintFor( rect );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy( EditPart child )
	{
		if ( child instanceof LabelEditPart
				|| child instanceof TextEditPart
				|| child instanceof ListEditPart )
			return new NonResizableEditPolicy( );
		ReportElementResizePolicy policy = new ReportElementResizePolicy( );
		policy.setResizeDirections( PositionConstants.SOUTH
				| PositionConstants.EAST
				| PositionConstants.SOUTH_EAST );
		return policy;
	}

	/**
	 * @return <code>true</code> if the host's LayoutManager is in a
	 *         horizontal orientation
	 */
	protected boolean isHorizontal( )
	{
		return true;
	}
}