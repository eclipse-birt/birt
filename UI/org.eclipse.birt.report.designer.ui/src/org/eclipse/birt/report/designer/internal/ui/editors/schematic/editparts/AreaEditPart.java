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

import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.AreaFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

/**
 * Provides support for area edit part.
 */
public class AreaEditPart extends ReportElementEditPart
{

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public AreaEditPart( Object model )
	{
		super( model );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		AreaFigure figure = new AreaFigure( );
		figure.setLayoutManager( new ReportFlowLayout( ) );

		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
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
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		List list = new ArrayList( );
		insertIteratorToList( ( (ReportElementModel) getModel( ) ).getSlotHandle( )
				.iterator( ),
				list );
		return list;
	}

	//TODO:move this code into util class?
	protected void insertIteratorToList( Iterator iterator, List list )
	{
		for ( Iterator it = iterator; it.hasNext( ); )
		{
			list.add( ( it.next( ) ) );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#elementChanged(org.eclipse.birt.model.core.DesignElement,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle arg0, NotificationEvent arg1 )
	{
		markDirty( true );
		refreshVisuals( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		( (MasterPageEditPart) getParent( ) ).setLayoutConstraint( this,
				figure,
				getConstraint( ) );
	}

	/**
	 * Get the default constraint of area figure.
	 * 
	 * @return
	 */
	private Rectangle getConstraint( )
	{
		IFigure parent = ( (MasterPageEditPart) getParent( ) ).getFigure( );
		AreaFigure figure = (AreaFigure) getFigure( );

		Rectangle region = parent.getClientArea( );
		Rectangle rect = new Rectangle( );

		//Define the default height value of header and footer
		rect.height = 50;
		rect.width = region.width;

		if ( ( (ReportElementModel) getModel( ) ).getSlotId( ) == SimpleMasterPage.PAGE_HEADER_SLOT )
		{
			rect.setLocation( 0, 0 );
		}
		else
		{
			rect.setLocation( region.getBottomLeft( ).x,
					region.getBottomLeft( ).y - rect.height );
		}

		return rect;
	}

}