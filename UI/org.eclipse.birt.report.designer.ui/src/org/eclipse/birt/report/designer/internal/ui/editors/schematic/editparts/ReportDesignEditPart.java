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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.ReportDesignHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.RootDragTracker;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportDesignLayout;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DeselectAllTracker;

/**
 * <p>
 * Report design editPart This is the content edit part for report Designer. All
 * other report elements puts on to it
 * </p>
 */
public class ReportDesignEditPart extends ReportElementEditPart
{

	/**
	 * constructor
	 * 
	 * @param obj
	 *            the object
	 */
	public ReportDesignEditPart( Object obj )
	{
		super( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		Figure figure = new ReportElementFigure( );

		figure.setOpaque( true );

		ReportDesignLayout layout = new ReportDesignLayout( );

		SlotHandle slotHandle = ( (ReportDesignHandle) getModel( ) ).getMasterPages( );
		Iterator iter = slotHandle.iterator( );
		SimpleMasterPageHandle masterPageHandle = (SimpleMasterPageHandle) iter.next( );
		Dimension size = getMasterPageSize( masterPageHandle );

		Rectangle bounds = new Rectangle( 0, 0, size.width - 1, size.height - 1 );

		layout.setInitSize( bounds );

		figure.setLayoutManager( layout );

		figure.setBorder( new ReportDesignMarginBorder( getMasterPageInsets( masterPageHandle ) ) );

		figure.setBounds( bounds.getCopy( ) );

		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new ReportFlowLayoutEditPolicy( ) );
		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.ui.editor.edit.ReportElementEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		return HandleAdapterFactory.getInstance( )
				.getReportDesignHandleAdapter( )
				.getChildren( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.iard.design.core.Listener#notify(com.actuate.iard.design.core.DesignElement,
	 *      com.actuate.iard.design.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		switch ( ev.getEventType( ) )
		{
			case NotificationEvent.CONTENT_EVENT :
			case NotificationEvent.STYLE_EVENT :
			{
				refresh( );
				break;
			}
			case NotificationEvent.PROPERTY_EVENT :
			{
				refresh( );
				this.markDirty( true );
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request req )
	{
		if ( req instanceof SelectionRequest
				&& ( (SelectionRequest) req ).getLastButtonPressed( ) == 3 )
			return new DeselectAllTracker( this );
		return new RootDragTracker( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.AbstractReportEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		SlotHandle slotHandle = ( (ReportDesignHandle) getModel( ) ).getMasterPages( );
		Iterator iter = slotHandle.iterator( );
		SimpleMasterPageHandle masterPageHandle = (SimpleMasterPageHandle) iter.next( );

		Dimension size = getMasterPageSize( masterPageHandle );

		Rectangle bounds = new Rectangle( 0, 0, size.width - 1, size.height - 1 );

		( (ReportDesignLayout) getFigure( ).getLayoutManager( ) ).setInitSize( bounds );
		getFigure( ).setBounds( bounds );
		getFigure( ).setBorder( new ReportDesignMarginBorder( getMasterPageInsets( masterPageHandle ) ) );

		int color = getBackgroundColor( masterPageHandle );
		getFigure( ).setBackgroundColor( ColorManager.getColor( color ) );

		refreshBackground( masterPageHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#notifyChildrenDirty(boolean)
	 */
	protected void notifyChildrenDirty( boolean bool )
	{
		super.notifyChildrenDirty( bool );
		refreshVisuals( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate( )
	{
		super.activate( );
		if ( ( (ReportDesignHandleAdapter) getModelAdapter( ) ).getMasterPage( ) != null )
		{
			( (ReportDesignHandleAdapter) getModelAdapter( ) ).getMasterPage( )
					.addListener( this );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate( )
	{
		super.deactivate( );
		if ( ( (ReportDesignHandleAdapter) getModelAdapter( ) ).getMasterPage( ) != null )
		{
			( (ReportDesignHandleAdapter) getModelAdapter( ) ).getMasterPage( )
					.removeListener( this );
		}
	}
}