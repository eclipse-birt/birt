/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.PasteCommand;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.MasterPageLayout;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * Master Page editor
 */
public class MasterPageEditPart extends ReportElementEditPart
{

	List children = new ArrayList( );

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public MasterPageEditPart( Object model )
	{
		super( model );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#elementChanged(org.eclipse.birt.model.core.DesignElement,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle element,
			NotificationEvent ev )
	{
		switch ( ev.getEventType( ) )
		{
			case NotificationEvent.CONTENT_EVENT :
			case NotificationEvent.ELEMENT_DELETE_EVENT :
			case NotificationEvent.PROPERTY_EVENT :
			case NotificationEvent.STYLE_EVENT :
			{
				markDirty( true );
				refresh( );
				//The children of master page edit part keep
				//virtual model
				//Those edit part will not get notification
				//refresh them explicit
				for ( Iterator it = getChildren( ).iterator( ); it.hasNext( ); )
				{
					( (AbstractEditPart) it.next( ) ).refresh( );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.LAYOUT_ROLE, new MasterPageEditPolicy( ) );
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

		figure.setBounds( new Rectangle( 0,
				0,
				getMasterPageSize( (MasterPageHandle) getModel( ) ).width - 1,
				getMasterPageSize( (MasterPageHandle) getModel( ) ).height - 1 ) );

		figure.setBorder( new ReportDesignMarginBorder( getMasterPageInsets( (MasterPageHandle) getModel( ) ) ) );

		figure.setLayoutManager( new MasterPageLayout( ) );

		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		int color = getBackgroundColor( (MasterPageHandle) getModel( ) );
		getFigure( ).setBackgroundColor( ColorManager.getColor( color ) );

		getFigure( ).setBounds( new Rectangle( 0,
				0,
				getMasterPageSize( (MasterPageHandle) getModel( ) ).width - 1,
				getMasterPageSize( (MasterPageHandle) getModel( ) ).height - 1 ) );

		ReportDesignMarginBorder reportDesignMarginBorder = new ReportDesignMarginBorder( getMasterPageInsets( (MasterPageHandle) getModel( ) ) );
		reportDesignMarginBorder.setBackgroundColor( ( (MasterPageHandle) getModel( ) ).getProperty( Style.BACKGROUND_COLOR_PROP ) );
		getFigure( ).setBorder( reportDesignMarginBorder );

		refreshBackground( (MasterPageHandle) getModel( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{

		ReportElementModel model = new ReportElementModel( ( (SimpleMasterPageHandle) getModel( ) ).getPageHeader( ) );

		if ( !children.contains( model ) )
		{
			children.add( model );
		}

		model = new ReportElementModel( ( (SimpleMasterPageHandle) getModel( ) ).getPageFooter( ) );

		if ( !children.contains( model ) )
		{
			children.add( model );
		}

		return children;
	}
}

/**
 * Provide getTargetEditPart for GEF framework. When the user click on margin
 * area of master page, the GEF framework need to iterator on installed edit
 * policies of target editpart to get the target edit part. This simple class is
 * written for this target and can avoid NULL pointer exception.
 */

class MasterPageEditPolicy extends GraphicalEditPolicy
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart( Request request )
	{
		if ( REQ_ADD.equals( request.getType( ) )
				|| REQ_MOVE.equals( request.getType( ) )
				|| REQ_CREATE.equals( request.getType( ) )
				|| REQ_CLONE.equals( request.getType( ) ) )
			return getHost( );
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand( Request request )
	{
		if ( REQ_ADD.equals( request.getType( ) ) )
			return getAddCommand( (ChangeBoundsRequest) request );

		return super.getCommand( request );
	}

	/**
	 * @param request
	 */
	protected Command getAddCommand( ChangeBoundsRequest request )
	{
		//Returns a invalid command to disable the whole request
		return new PasteCommand( null, null, null );
	}

}