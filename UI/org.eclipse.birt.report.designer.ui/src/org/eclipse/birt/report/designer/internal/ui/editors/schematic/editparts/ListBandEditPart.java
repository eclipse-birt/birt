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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ListBandControlFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ListBandFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ListBandRenderFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ListBandControlFigure.ListBandControlVisible;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ListBandControlFigure.ListControlDisplayNameFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ListBandControlFigure.ListIconFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.ListData;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.action.IAction;

/**
 * List band proxy edit part
 *  
 */
public class ListBandEditPart extends ReportElementEditPart
{

	ListBandControlFigure controlFigure;

	ListBandRenderFigure renderFigure;

	/**
	 * @param obj
	 */
	public ListBandEditPart( Object obj )
	{
		super( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle arg0, NotificationEvent arg1 )
	{
		//model is proxy node, so the listener do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE, null );
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new ReportFlowLayoutEditPolicy( ) );
		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		//TODO set edit part property
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		ListBandFigure figure = new ListBandFigure( );
		controlFigure = createControlFigure( );
		renderFigure = createRenderFigure( );

		figure.setContend( renderFigure );
		figure.setControlFigure( controlFigure );

		figure.add( controlFigure );
		figure.add( renderFigure );

		return figure;
	}

	/**
	 * Creates the render figure
	 *  
	 */
	private ListBandRenderFigure createRenderFigure( )
	{
		renderFigure = new ListBandRenderFigure( );
		return renderFigure;
	}

	/**
	 * Creates the control figure
	 *  
	 */
	private ListBandControlFigure createControlFigure( )
	{
		controlFigure = new ListBandControlFigure( this );

		controlFigure.add( new ListControlDisplayNameFigure( this ) );

		controlFigure.add( new ListIconFigure( this ) );

		controlFigure.add( new ListBandControlVisible( this ) );

		//Sets the background
		for ( Iterator itr = controlFigure.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			IFigure fig = (IFigure) itr.next( );
			fig.setOpaque( false );
		}
		return controlFigure;
	}

	/*
	 * Sets the order in list edit part.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	public void refreshChildren( )
	{
		super.refreshChildren( );
		( (AbstractGraphicalEditPart) getParent( ) ).setLayoutConstraint( this,
				getFigure( ),
				getConstraint( ) );
	}

	/**
	 * @return The constraint
	 */
	protected Object getConstraint( )
	{
		ListData data = new ListData( );
		data.order = ( (ListEditPart) getParent( ) ).getModelChildren( )
				.indexOf( getModel( ) );

		return data;
	}

	/*
	 * Gets the content pane, the child of list figures adds in this figure
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane( )
	{
		return renderFigure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		return ( (ListBandProxy) getModel( ) ).getChildren( );
	}

	/*
	 * Gets the list band tracker, the tracker do nothing
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request req )
	{

		return new DragEditPartsTracker( this ) {

			protected boolean handleDragInProgress( )
			{
				return true;
			}
		};
	}

	/**
	 * Sets the render figure visible
	 * 
	 * @param bool
	 */
	public void setRenderVisile( boolean bool )
	{
		ListBandFigure figure = (ListBandFigure) getFigure( );
		figure.setShowing( bool );
		if ( bool )
		{
			markDirty( bool );
		}
	}

	/**
	 * Gets the if the render figure is visible
	 * 
	 * @return visible or not
	 */
	public boolean isRenderVisile( )
	{
		ListBandFigure figure = (ListBandFigure) getFigure( );
		return figure.isControlShowing( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest( Request request )
	{
		if ( RequestConstants.REQ_OPEN.equals( request.getType( ) ) )
		{
			ListBandProxy listBand = (ListBandProxy) getModel( );
			if ( listBand.getElemtHandle( ) instanceof ListGroupHandle )
			{
				IAction action = new EditGroupAction( null,
						(ListGroupHandle) listBand.getElemtHandle( ) );
				if ( action.isEnabled( ) )
				{
					action.run( );
				}
			}
		}
	}
}